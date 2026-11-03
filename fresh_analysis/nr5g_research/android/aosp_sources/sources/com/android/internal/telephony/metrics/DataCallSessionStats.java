package com.android.internal.telephony.metrics;

import android.net.NetworkCapabilities;
import android.os.SystemClock;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.DataCallResponse;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.nano.PersistAtomsProto$DataCallSession;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class DataCallSessionStats {
    private static final Random RANDOM = new Random();
    public static final int SIZE_LIMIT_HANDOVER_FAILURES = 15;
    private static final String TAG = "DataCallSessionStats";
    private PersistAtomsProto$DataCallSession mDataCallSession;
    private final Phone mPhone;
    private long mStartTime;
    private final PersistAtomsStorage mAtomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
    private final DefaultNetworkMonitor mDefaultNetworkMonitor = PhoneFactory.getMetricsCollector().getDefaultNetworkMonitor();
    private final SatelliteController mSatelliteController = SatelliteController.getInstance();

    public DataCallSessionStats(Phone phone) {
        this.mPhone = phone;
    }

    private boolean isSystemDefaultNetworkMobile() {
        NetworkCapabilities networkCapabilities = this.mDefaultNetworkMonitor.getNetworkCapabilities();
        return networkCapabilities != null && networkCapabilities.hasTransport(0);
    }

    public synchronized void onSetupDataCall(int i, boolean z, int i2, int i3) {
        this.mDataCallSession = getDefaultProto(i, z, i2, i3);
        this.mStartTime = getTimeMillis();
        PhoneFactory.getMetricsCollector().registerOngoingDataCallStat(this);
    }

    public synchronized void onSetupDataCallResponse(DataCallResponse dataCallResponse, int i, int i2, int i3, int i4) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
        boolean z = false;
        if (persistAtomsProto$DataCallSession == null) {
            loge("onSetupDataCallResponse: no DataCallSession atom has been initiated.", new Object[0]);
            return;
        }
        if (i != 0) {
            persistAtomsProto$DataCallSession.ratAtEnd = i;
            persistAtomsProto$DataCallSession.bandAtEnd = i == 18 ? 0 : ServiceStateStats.getBand(this.mPhone);
            PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = this.mDataCallSession;
            if (i == 18 && isSystemDefaultNetworkMobile()) {
                z = true;
            }
            persistAtomsProto$DataCallSession2.isIwlanCrossSim = z;
        }
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession3 = this.mDataCallSession;
        if (persistAtomsProto$DataCallSession3.apnTypeBitmask == 0) {
            persistAtomsProto$DataCallSession3.apnTypeBitmask = i2;
        }
        persistAtomsProto$DataCallSession3.ipType = i3;
        persistAtomsProto$DataCallSession3.failureCause = i4;
        if (dataCallResponse != null) {
            persistAtomsProto$DataCallSession3.suggestedRetryMillis = (int) Math.min(dataCallResponse.getRetryDurationMillis(), 2147483647L);
            if (i4 != 0) {
                this.mDataCallSession.setupFailed = true;
                endDataCallSession();
            }
        }
    }

    public synchronized void setDeactivateDataCallReason(int i) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
        if (persistAtomsProto$DataCallSession == null) {
            loge("setDeactivateDataCallReason: no DataCallSession atom has been initiated.", new Object[0]);
        } else {
            persistAtomsProto$DataCallSession.deactivateReason = i + 4;
        }
    }

    public synchronized void onDataCallDisconnected(int i) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
        if (persistAtomsProto$DataCallSession == null) {
            logi("onDataCallDisconnected: no DataCallSession atom has been initiated.", new Object[0]);
            return;
        }
        persistAtomsProto$DataCallSession.failureCause = i;
        persistAtomsProto$DataCallSession.durationMinutes = convertMillisToMinutes(getTimeMillis() - this.mStartTime);
        endDataCallSession();
    }

    public synchronized void onHandoverFailure(int i, int i2, int i3) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
        if (persistAtomsProto$DataCallSession != null) {
            int[] iArr = persistAtomsProto$DataCallSession.handoverFailureCauses;
            if (iArr.length < 15) {
                int[] iArr2 = persistAtomsProto$DataCallSession.handoverFailureRat;
                int i4 = i2 | (i3 << 16);
                for (int i5 = 0; i5 < iArr.length; i5++) {
                    if (iArr[i5] == i && iArr2[i5] == i4) {
                        return;
                    }
                }
                this.mDataCallSession.handoverFailureCauses = Arrays.copyOf(iArr, iArr.length + 1);
                PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = this.mDataCallSession;
                persistAtomsProto$DataCallSession2.handoverFailureCauses[iArr.length] = i;
                persistAtomsProto$DataCallSession2.handoverFailureRat = Arrays.copyOf(iArr2, iArr2.length + 1);
                this.mDataCallSession.handoverFailureRat[iArr2.length] = i4;
            }
        }
    }

    public synchronized void onDrsOrRatChanged(int i) {
        try {
            PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
            if (persistAtomsProto$DataCallSession != null && i != 0) {
                int band = 0;
                if (persistAtomsProto$DataCallSession.ratAtEnd != i) {
                    persistAtomsProto$DataCallSession.ratSwitchCount++;
                    persistAtomsProto$DataCallSession.ratAtEnd = i;
                    persistAtomsProto$DataCallSession.isIwlanCrossSim = i == 18 && isSystemDefaultNetworkMobile();
                }
                PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = this.mDataCallSession;
                if (i != 18) {
                    band = ServiceStateStats.getBand(this.mPhone);
                }
                persistAtomsProto$DataCallSession2.bandAtEnd = band;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void onUnmeteredUpdate(int i) {
        this.mAtomsStorage.addUnmeteredNetworks(this.mPhone.getPhoneId(), this.mPhone.getCarrierId(), TelephonyManager.getBitMaskForNetworkType(i));
    }

    public synchronized void conclude() {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = this.mDataCallSession;
        if (persistAtomsProto$DataCallSession != null) {
            PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSessionCopyOf = copyOf(persistAtomsProto$DataCallSession);
            long timeMillis = getTimeMillis();
            persistAtomsProto$DataCallSessionCopyOf.durationMinutes = convertMillisToMinutes(timeMillis - this.mStartTime);
            this.mStartTime = timeMillis;
            PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = this.mDataCallSession;
            persistAtomsProto$DataCallSession2.ratSwitchCount = 0L;
            persistAtomsProto$DataCallSession2.handoverFailureCauses = new int[0];
            persistAtomsProto$DataCallSession2.handoverFailureRat = new int[0];
            this.mAtomsStorage.addDataCallSession(persistAtomsProto$DataCallSessionCopyOf);
        }
    }

    private void endDataCallSession() {
        this.mDataCallSession.oosAtEnd = getIsOos();
        this.mDataCallSession.ongoing = false;
        SubscriptionInfo subscriptionInfo = SubscriptionManagerService.getInstance().getSubscriptionInfo(this.mPhone.getSubId());
        if (this.mPhone.getSubId() != SubscriptionManager.getDefaultDataSubscriptionId() && (this.mDataCallSession.apnTypeBitmask & 17) == 17 && subscriptionInfo != null && !subscriptionInfo.isOpportunistic()) {
            this.mDataCallSession.isNonDds = true;
        }
        PhoneFactory.getMetricsCollector().unregisterOngoingDataCallStat(this);
        this.mAtomsStorage.addDataCallSession(this.mDataCallSession);
        this.mDataCallSession = null;
    }

    private static long convertMillisToMinutes(long j) {
        return Math.round(j / 60000.0d);
    }

    private static PersistAtomsProto$DataCallSession copyOf(PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = new PersistAtomsProto$DataCallSession();
        persistAtomsProto$DataCallSession2.dimension = persistAtomsProto$DataCallSession.dimension;
        persistAtomsProto$DataCallSession2.isMultiSim = persistAtomsProto$DataCallSession.isMultiSim;
        persistAtomsProto$DataCallSession2.isEsim = persistAtomsProto$DataCallSession.isEsim;
        persistAtomsProto$DataCallSession2.apnTypeBitmask = persistAtomsProto$DataCallSession.apnTypeBitmask;
        persistAtomsProto$DataCallSession2.carrierId = persistAtomsProto$DataCallSession.carrierId;
        persistAtomsProto$DataCallSession2.isRoaming = persistAtomsProto$DataCallSession.isRoaming;
        persistAtomsProto$DataCallSession2.ratAtEnd = persistAtomsProto$DataCallSession.ratAtEnd;
        persistAtomsProto$DataCallSession2.oosAtEnd = persistAtomsProto$DataCallSession.oosAtEnd;
        persistAtomsProto$DataCallSession2.ratSwitchCount = persistAtomsProto$DataCallSession.ratSwitchCount;
        persistAtomsProto$DataCallSession2.isOpportunistic = persistAtomsProto$DataCallSession.isOpportunistic;
        persistAtomsProto$DataCallSession2.ipType = persistAtomsProto$DataCallSession.ipType;
        persistAtomsProto$DataCallSession2.setupFailed = persistAtomsProto$DataCallSession.setupFailed;
        persistAtomsProto$DataCallSession2.failureCause = persistAtomsProto$DataCallSession.failureCause;
        persistAtomsProto$DataCallSession2.suggestedRetryMillis = persistAtomsProto$DataCallSession.suggestedRetryMillis;
        persistAtomsProto$DataCallSession2.deactivateReason = persistAtomsProto$DataCallSession.deactivateReason;
        persistAtomsProto$DataCallSession2.durationMinutes = persistAtomsProto$DataCallSession.durationMinutes;
        persistAtomsProto$DataCallSession2.ongoing = persistAtomsProto$DataCallSession.ongoing;
        persistAtomsProto$DataCallSession2.bandAtEnd = persistAtomsProto$DataCallSession.bandAtEnd;
        int[] iArr = persistAtomsProto$DataCallSession.handoverFailureCauses;
        persistAtomsProto$DataCallSession2.handoverFailureCauses = Arrays.copyOf(iArr, iArr.length);
        int[] iArr2 = persistAtomsProto$DataCallSession.handoverFailureRat;
        persistAtomsProto$DataCallSession2.handoverFailureRat = Arrays.copyOf(iArr2, iArr2.length);
        persistAtomsProto$DataCallSession2.isNonDds = persistAtomsProto$DataCallSession.isNonDds;
        persistAtomsProto$DataCallSession2.isIwlanCrossSim = persistAtomsProto$DataCallSession.isIwlanCrossSim;
        persistAtomsProto$DataCallSession2.isNtn = persistAtomsProto$DataCallSession.isNtn;
        persistAtomsProto$DataCallSession2.isSatelliteTransport = persistAtomsProto$DataCallSession.isSatelliteTransport;
        persistAtomsProto$DataCallSession2.isProvisioningProfile = persistAtomsProto$DataCallSession.isProvisioningProfile;
        persistAtomsProto$DataCallSession2.isNbIotNtn = persistAtomsProto$DataCallSession.isNbIotNtn;
        persistAtomsProto$DataCallSession2.sliceCapability = persistAtomsProto$DataCallSession.sliceCapability;
        persistAtomsProto$DataCallSession2.plmn = persistAtomsProto$DataCallSession.plmn;
        persistAtomsProto$DataCallSession2.connectionCapability = persistAtomsProto$DataCallSession.connectionCapability;
        return persistAtomsProto$DataCallSession2;
    }

    private PersistAtomsProto$DataCallSession getDefaultProto(int i, boolean z, int i2, int i3) {
        PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession = new PersistAtomsProto$DataCallSession();
        persistAtomsProto$DataCallSession.dimension = RANDOM.nextInt();
        persistAtomsProto$DataCallSession.isMultiSim = SimSlotState.isMultiSim();
        persistAtomsProto$DataCallSession.isEsim = SimSlotState.isEsim(this.mPhone.getPhoneId());
        persistAtomsProto$DataCallSession.apnTypeBitmask = i;
        persistAtomsProto$DataCallSession.carrierId = this.mPhone.getCarrierId();
        persistAtomsProto$DataCallSession.isRoaming = getIsRoaming();
        persistAtomsProto$DataCallSession.oosAtEnd = false;
        persistAtomsProto$DataCallSession.ratSwitchCount = 0L;
        persistAtomsProto$DataCallSession.isOpportunistic = getIsOpportunistic();
        persistAtomsProto$DataCallSession.ipType = 0;
        persistAtomsProto$DataCallSession.setupFailed = false;
        persistAtomsProto$DataCallSession.failureCause = 0;
        persistAtomsProto$DataCallSession.suggestedRetryMillis = 0;
        persistAtomsProto$DataCallSession.deactivateReason = 0;
        persistAtomsProto$DataCallSession.durationMinutes = 0L;
        persistAtomsProto$DataCallSession.ongoing = true;
        persistAtomsProto$DataCallSession.handoverFailureCauses = new int[0];
        persistAtomsProto$DataCallSession.handoverFailureRat = new int[0];
        persistAtomsProto$DataCallSession.isNonDds = false;
        persistAtomsProto$DataCallSession.isIwlanCrossSim = false;
        SatelliteController satelliteController = this.mSatelliteController;
        if (satelliteController != null) {
            persistAtomsProto$DataCallSession.isNtn = satelliteController.isInSatelliteModeForCarrierRoaming(this.mPhone);
            persistAtomsProto$DataCallSession.isNbIotNtn = this.mSatelliteController.isInCarrierRoamingNbIotNtn(this.mPhone);
            persistAtomsProto$DataCallSession.plmn = this.mSatelliteController.getSatellitePlmnForMetrics(this.mPhone);
        } else {
            persistAtomsProto$DataCallSession.isNtn = false;
            persistAtomsProto$DataCallSession.isNbIotNtn = false;
            persistAtomsProto$DataCallSession.plmn = "";
        }
        persistAtomsProto$DataCallSession.isSatelliteTransport = z;
        persistAtomsProto$DataCallSession.isProvisioningProfile = getIsProvisioningProfile();
        persistAtomsProto$DataCallSession.sliceCapability = i2;
        persistAtomsProto$DataCallSession.connectionCapability = i3;
        return persistAtomsProto$DataCallSession;
    }

    private boolean getIsRoaming() {
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        return ServiceStateStats.isNetworkRoaming(serviceStateTracker != null ? serviceStateTracker.getServiceState() : null, 2);
    }

    private boolean getIsOpportunistic() {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(this.mPhone.getSubId());
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isOpportunistic();
    }

    private boolean getIsProvisioningProfile() {
        try {
            return SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(this.mPhone.getSubId()).getProfileClass() == 1;
        } catch (Exception e) {
            loge("getIsProvisioningProfile: " + e.getMessage(), new Object[0]);
            return false;
        }
    }

    private boolean getIsOos() {
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        ServiceState serviceState = serviceStateTracker != null ? serviceStateTracker.getServiceState() : null;
        return serviceState != null && serviceState.getDataRegistrationState() == 1;
    }

    private void logi(String str, Object... objArr) {
        Rlog.i(TAG, "[" + this.mPhone.getPhoneId() + "]" + String.format(str, objArr));
    }

    private void loge(String str, Object... objArr) {
        Rlog.e(TAG, "[" + this.mPhone.getPhoneId() + "]" + String.format(str, objArr));
    }

    protected long getTimeMillis() {
        return SystemClock.elapsedRealtime();
    }
}
