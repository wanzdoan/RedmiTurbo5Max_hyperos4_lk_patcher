package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import android.telephony.AccessNetworkUtils;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularDataServiceSwitch;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularServiceState;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.telephony.Rlog;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateStats extends DataNetworkController.DataNetworkControllerCallback {
    private static final String TAG = "ServiceStateStats";
    private int mCurrentDataRat;
    private final DeviceStateHelper mDeviceStateHelper;
    private boolean mExistAnyConnectedInternetPdn;
    private final AtomicReference mLastState;
    private final AtomicBoolean mOverrideVoiceService;
    private final Phone mPhone;
    private final PersistAtomsStorage mStorage;

    public ServiceStateStats(Phone phone) {
        super(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0());
        this.mLastState = new AtomicReference(new TimestampedServiceState(null, 0L));
        this.mOverrideVoiceService = new AtomicBoolean(false);
        this.mCurrentDataRat = 0;
        this.mPhone = phone;
        this.mStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
        this.mDeviceStateHelper = PhoneFactory.getMetricsCollector().getDeviceStateHelper();
    }

    public void conclude() {
        final long timeMillis = getTimeMillis();
        addServiceState((TimestampedServiceState) this.mLastState.getAndUpdate(new UnaryOperator() { // from class: com.android.internal.telephony.metrics.ServiceStateStats$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ServiceStateStats.$r8$lambda$qeXdWq0NdFhfdzjpUXakHQIVc8Y(timeMillis, (ServiceStateStats.TimestampedServiceState) obj);
            }
        }), timeMillis);
    }

    public static /* synthetic */ TimestampedServiceState $r8$lambda$qeXdWq0NdFhfdzjpUXakHQIVc8Y(long j, TimestampedServiceState timestampedServiceState) {
        return new TimestampedServiceState(timestampedServiceState.mServiceState, j);
    }

    public void onImsVoiceRegistrationChanged() {
        final long timeMillis = getTimeMillis();
        addServiceState((TimestampedServiceState) this.mLastState.getAndUpdate(new UnaryOperator() { // from class: com.android.internal.telephony.metrics.ServiceStateStats$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$onImsVoiceRegistrationChanged$1(timeMillis, (ServiceStateStats.TimestampedServiceState) obj);
            }
        }), timeMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ TimestampedServiceState lambda$onImsVoiceRegistrationChanged$1(long j, TimestampedServiceState timestampedServiceState) {
        if (timestampedServiceState.mServiceState == null) {
            return new TimestampedServiceState(null, j);
        }
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateCopyOf = copyOf(timestampedServiceState.mServiceState);
        Phone phone = this.mPhone;
        persistAtomsProto$CellularServiceStateCopyOf.voiceRat = getVoiceRat(phone, getServiceStateForPhone(phone));
        persistAtomsProto$CellularServiceStateCopyOf.isIwlanCrossSim = isCrossSimCallingRegistered(this.mPhone);
        return new TimestampedServiceState(persistAtomsProto$CellularServiceStateCopyOf, j);
    }

    public void registerDataNetworkControllerCallback() {
        this.mPhone.getDataNetworkController().registerDataNetworkControllerCallback(this);
    }

    @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
    public void onConnectedInternetDataNetworksChanged(Set<DataNetwork> set) {
        boolean z = !set.isEmpty();
        if (this.mExistAnyConnectedInternetPdn != z) {
            this.mExistAnyConnectedInternetPdn = z;
            onInternetDataNetworkChanged(z);
        }
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        long timeMillis = getTimeMillis();
        if (isModemOff(serviceState)) {
            addServiceState((TimestampedServiceState) this.mLastState.getAndSet(new TimestampedServiceState(null, timeMillis)), timeMillis);
        } else {
            SatelliteController satelliteController = SatelliteController.getInstance();
            PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState = new PersistAtomsProto$CellularServiceState();
            persistAtomsProto$CellularServiceState.voiceRat = getVoiceRat(this.mPhone, serviceState);
            persistAtomsProto$CellularServiceState.dataRat = getRat(serviceState, 2);
            persistAtomsProto$CellularServiceState.voiceRoamingType = getNetworkRoamingState(serviceState, 1);
            persistAtomsProto$CellularServiceState.dataRoamingType = getNetworkRoamingState(serviceState, 2);
            persistAtomsProto$CellularServiceState.isEndc = isEndc(serviceState);
            persistAtomsProto$CellularServiceState.simSlotIndex = this.mPhone.getPhoneId();
            persistAtomsProto$CellularServiceState.isMultiSim = SimSlotState.isMultiSim();
            persistAtomsProto$CellularServiceState.carrierId = this.mPhone.getCarrierId();
            persistAtomsProto$CellularServiceState.isEmergencyOnly = isEmergencyOnly(serviceState);
            persistAtomsProto$CellularServiceState.isInternetPdnUp = isInternetPdnUp(this.mPhone);
            persistAtomsProto$CellularServiceState.foldState = this.mDeviceStateHelper.getFoldState();
            persistAtomsProto$CellularServiceState.overrideVoiceService = this.mOverrideVoiceService.get();
            persistAtomsProto$CellularServiceState.isDataEnabled = this.mPhone.getDataSettingsManager().isDataEnabled();
            persistAtomsProto$CellularServiceState.isIwlanCrossSim = isCrossSimCallingRegistered(this.mPhone);
            if (satelliteController != null) {
                persistAtomsProto$CellularServiceState.isNtn = satelliteController.isInSatelliteModeForCarrierRoaming(this.mPhone);
                persistAtomsProto$CellularServiceState.isNbIotNtn = satelliteController.isInCarrierRoamingNbIotNtn(this.mPhone);
                persistAtomsProto$CellularServiceState.plmn = satelliteController.getSatellitePlmnForMetrics(this.mPhone);
            } else {
                persistAtomsProto$CellularServiceState.isNtn = false;
                persistAtomsProto$CellularServiceState.isNbIotNtn = false;
                persistAtomsProto$CellularServiceState.plmn = "";
            }
            TimestampedServiceState timestampedServiceState = (TimestampedServiceState) this.mLastState.getAndSet(new TimestampedServiceState(persistAtomsProto$CellularServiceState, timeMillis));
            addServiceStateAndSwitch(timestampedServiceState, timeMillis, getDataServiceSwitch(timestampedServiceState.mServiceState, persistAtomsProto$CellularServiceState));
        }
        writeDataRatAtom(serviceState);
    }

    public void onFoldStateChanged(final int i) {
        final long timeMillis = getTimeMillis();
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState = ((TimestampedServiceState) this.mLastState.get()).mServiceState;
        if (persistAtomsProto$CellularServiceState == null || persistAtomsProto$CellularServiceState.foldState == i) {
            return;
        }
        addServiceState((TimestampedServiceState) this.mLastState.getAndUpdate(new UnaryOperator() { // from class: com.android.internal.telephony.metrics.ServiceStateStats$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ServiceStateStats.m1498$r8$lambda$2uF6qbqjxSO_duJdOmtYaI2CbM(i, timeMillis, (ServiceStateStats.TimestampedServiceState) obj);
            }
        }), timeMillis);
    }

    /* JADX INFO: renamed from: $r8$lambda$2uF6qbq-jxSO_duJdOmtYaI2CbM, reason: not valid java name */
    public static /* synthetic */ TimestampedServiceState m1498$r8$lambda$2uF6qbqjxSO_duJdOmtYaI2CbM(int i, long j, TimestampedServiceState timestampedServiceState) {
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateCopyOf = copyOf(timestampedServiceState.mServiceState);
        persistAtomsProto$CellularServiceStateCopyOf.foldState = i;
        return new TimestampedServiceState(persistAtomsProto$CellularServiceStateCopyOf, j);
    }

    public void onVoiceServiceStateOverrideChanged(boolean z) {
        if (z == this.mOverrideVoiceService.get()) {
            return;
        }
        this.mOverrideVoiceService.set(z);
        final long timeMillis = getTimeMillis();
        addServiceState((TimestampedServiceState) this.mLastState.getAndUpdate(new UnaryOperator() { // from class: com.android.internal.telephony.metrics.ServiceStateStats$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$onVoiceServiceStateOverrideChanged$3(timeMillis, (ServiceStateStats.TimestampedServiceState) obj);
            }
        }), timeMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ TimestampedServiceState lambda$onVoiceServiceStateOverrideChanged$3(long j, TimestampedServiceState timestampedServiceState) {
        if (timestampedServiceState.mServiceState == null) {
            return new TimestampedServiceState(null, j);
        }
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateCopyOf = copyOf(timestampedServiceState.mServiceState);
        persistAtomsProto$CellularServiceStateCopyOf.overrideVoiceService = this.mOverrideVoiceService.get();
        return new TimestampedServiceState(persistAtomsProto$CellularServiceStateCopyOf, j);
    }

    private void addServiceState(TimestampedServiceState timestampedServiceState, long j) {
        addServiceStateAndSwitch(timestampedServiceState, j, null);
    }

    private void addServiceStateAndSwitch(TimestampedServiceState timestampedServiceState, long j, PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch) {
        if (timestampedServiceState.mServiceState == null) {
            return;
        }
        if (j >= timestampedServiceState.mTimestamp) {
            PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateCopyOf = copyOf(timestampedServiceState.mServiceState);
            persistAtomsProto$CellularServiceStateCopyOf.totalTimeMillis = j - timestampedServiceState.mTimestamp;
            this.mStorage.addCellularServiceStateAndCellularDataServiceSwitch(persistAtomsProto$CellularServiceStateCopyOf, persistAtomsProto$CellularDataServiceSwitch);
            return;
        }
        Rlog.e(TAG, "addServiceState: durationMillis<0");
    }

    private PersistAtomsProto$CellularDataServiceSwitch getDataServiceSwitch(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState, PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState2) {
        if (persistAtomsProto$CellularServiceState == null || persistAtomsProto$CellularServiceState.isMultiSim != persistAtomsProto$CellularServiceState2.isMultiSim || persistAtomsProto$CellularServiceState.carrierId != persistAtomsProto$CellularServiceState2.carrierId || persistAtomsProto$CellularServiceState.dataRat == persistAtomsProto$CellularServiceState2.dataRat) {
            return null;
        }
        PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch = new PersistAtomsProto$CellularDataServiceSwitch();
        persistAtomsProto$CellularDataServiceSwitch.ratFrom = persistAtomsProto$CellularServiceState.dataRat;
        persistAtomsProto$CellularDataServiceSwitch.ratTo = persistAtomsProto$CellularServiceState2.dataRat;
        persistAtomsProto$CellularDataServiceSwitch.isMultiSim = persistAtomsProto$CellularServiceState2.isMultiSim;
        persistAtomsProto$CellularDataServiceSwitch.simSlotIndex = persistAtomsProto$CellularServiceState2.simSlotIndex;
        persistAtomsProto$CellularDataServiceSwitch.carrierId = persistAtomsProto$CellularServiceState2.carrierId;
        persistAtomsProto$CellularDataServiceSwitch.switchCount = 1;
        return persistAtomsProto$CellularDataServiceSwitch;
    }

    private static ServiceState getServiceStateForPhone(Phone phone) {
        ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
        if (serviceStateTracker != null) {
            return serviceStateTracker.getServiceState();
        }
        return null;
    }

    static int getBand(Phone phone) {
        return getBand(getServiceStateForPhone(phone));
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0053  */
    /* JADX WARN: Code duplicated, block: B:28:0x0058  */
    /* JADX WARN: Code duplicated, block: B:29:0x005d  */
    static int getBand(ServiceState serviceState) {
        int operatingBandForArfcn;
        if (serviceState == null) {
            Rlog.w(TAG, "getBand: serviceState=null");
            return 0;
        }
        int channelNumber = serviceState.getChannelNumber();
        int rat = getRat(serviceState, 2);
        if (rat == 0) {
            rat = serviceState.getVoiceNetworkType();
        }
        if (rat == 1 || rat == 2) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForArfcn(channelNumber);
        } else if (rat == 3) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForUarfcn(channelNumber);
        } else if (rat == 13) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForEarfcn(channelNumber);
        } else if (rat == 15) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForUarfcn(channelNumber);
        } else if (rat == 16) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForArfcn(channelNumber);
        } else if (rat == 19) {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForEarfcn(channelNumber);
        } else if (rat != 20) {
            switch (rat) {
                case 8:
                case 9:
                case 10:
                    operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForUarfcn(channelNumber);
                    break;
                default:
                    Rlog.w(TAG, "getBand: unknown WWAN RAT " + rat);
                    operatingBandForArfcn = 0;
                    break;
            }
        } else {
            operatingBandForArfcn = AccessNetworkUtils.getOperatingBandForNrarfcn(channelNumber);
        }
        if (operatingBandForArfcn != -1) {
            return operatingBandForArfcn;
        }
        Rlog.w(TAG, "getBand: band invalid for rat=" + rat + " ch=" + channelNumber);
        return 0;
    }

    private static PersistAtomsProto$CellularServiceState copyOf(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState) {
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState2 = new PersistAtomsProto$CellularServiceState();
        persistAtomsProto$CellularServiceState2.voiceRat = persistAtomsProto$CellularServiceState.voiceRat;
        persistAtomsProto$CellularServiceState2.dataRat = persistAtomsProto$CellularServiceState.dataRat;
        persistAtomsProto$CellularServiceState2.voiceRoamingType = persistAtomsProto$CellularServiceState.voiceRoamingType;
        persistAtomsProto$CellularServiceState2.dataRoamingType = persistAtomsProto$CellularServiceState.dataRoamingType;
        persistAtomsProto$CellularServiceState2.isEndc = persistAtomsProto$CellularServiceState.isEndc;
        persistAtomsProto$CellularServiceState2.simSlotIndex = persistAtomsProto$CellularServiceState.simSlotIndex;
        persistAtomsProto$CellularServiceState2.isMultiSim = persistAtomsProto$CellularServiceState.isMultiSim;
        persistAtomsProto$CellularServiceState2.carrierId = persistAtomsProto$CellularServiceState.carrierId;
        persistAtomsProto$CellularServiceState2.totalTimeMillis = persistAtomsProto$CellularServiceState.totalTimeMillis;
        persistAtomsProto$CellularServiceState2.isEmergencyOnly = persistAtomsProto$CellularServiceState.isEmergencyOnly;
        persistAtomsProto$CellularServiceState2.isInternetPdnUp = persistAtomsProto$CellularServiceState.isInternetPdnUp;
        persistAtomsProto$CellularServiceState2.foldState = persistAtomsProto$CellularServiceState.foldState;
        persistAtomsProto$CellularServiceState2.overrideVoiceService = persistAtomsProto$CellularServiceState.overrideVoiceService;
        persistAtomsProto$CellularServiceState2.isDataEnabled = persistAtomsProto$CellularServiceState.isDataEnabled;
        persistAtomsProto$CellularServiceState2.isIwlanCrossSim = persistAtomsProto$CellularServiceState.isIwlanCrossSim;
        persistAtomsProto$CellularServiceState2.isNtn = persistAtomsProto$CellularServiceState.isNtn;
        persistAtomsProto$CellularServiceState2.isNbIotNtn = persistAtomsProto$CellularServiceState.isNbIotNtn;
        persistAtomsProto$CellularServiceState2.plmn = persistAtomsProto$CellularServiceState.plmn;
        return persistAtomsProto$CellularServiceState2;
    }

    private static boolean isModemOff(ServiceState serviceState) {
        return serviceState.getVoiceRegState() == 3;
    }

    static int getVoiceRat(Phone phone, ServiceState serviceState) {
        return getVoiceRat(phone, serviceState, 0);
    }

    public static int getVoiceRat(Phone phone, ServiceState serviceState, int i) {
        int imsVoiceRadioTech;
        if (serviceState == null) {
            return 0;
        }
        ImsPhone imsPhone = (ImsPhone) phone.getImsPhone();
        if (i != 1 && imsPhone != null && (imsVoiceRadioTech = imsPhone.getImsStats().getImsVoiceRadioTech()) != 0 && (imsVoiceRadioTech == 18 || getRat(serviceState, 2) != 0)) {
            return imsVoiceRadioTech;
        }
        if (i == 2) {
            return 0;
        }
        return getRat(serviceState, 1);
    }

    private boolean isCrossSimCallingRegistered(Phone phone) {
        return phone.getImsPhone() != null && phone.getImsPhone().getImsRegistrationTech() == 2;
    }

    public static int getRat(ServiceState serviceState, int i) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(i, 1);
        if (networkRegistrationInfo == null || !networkRegistrationInfo.isInService()) {
            return 0;
        }
        return networkRegistrationInfo.getAccessNetworkTechnology();
    }

    private static boolean isEmergencyOnly(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1);
        return (networkRegistrationInfo == null || networkRegistrationInfo.isInService() || !networkRegistrationInfo.isEmergencyEnabled()) ? false : true;
    }

    private static boolean isEndc(ServiceState serviceState) {
        if (getRat(serviceState, 2) != 13) {
            return false;
        }
        int nrState = serviceState.getNrState();
        return nrState == 3 || nrState == 2;
    }

    private static boolean isInternetPdnUp(Phone phone) {
        DataNetworkController dataNetworkController = phone.getDataNetworkController();
        return dataNetworkController != null && dataNetworkController.getInternetDataNetworkState() == 2;
    }

    private void onInternetDataNetworkChanged(final boolean z) {
        final long timeMillis = getTimeMillis();
        addServiceState((TimestampedServiceState) this.mLastState.getAndUpdate(new UnaryOperator() { // from class: com.android.internal.telephony.metrics.ServiceStateStats$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ServiceStateStats.$r8$lambda$2FfGbpmycSAbjm9lOowU6VIzEkI(timeMillis, z, (ServiceStateStats.TimestampedServiceState) obj);
            }
        }), timeMillis);
    }

    public static /* synthetic */ TimestampedServiceState $r8$lambda$2FfGbpmycSAbjm9lOowU6VIzEkI(long j, boolean z, TimestampedServiceState timestampedServiceState) {
        if (timestampedServiceState.mServiceState == null) {
            return new TimestampedServiceState(null, j);
        }
        PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateCopyOf = copyOf(timestampedServiceState.mServiceState);
        persistAtomsProto$CellularServiceStateCopyOf.isInternetPdnUp = z;
        return new TimestampedServiceState(persistAtomsProto$CellularServiceStateCopyOf, j);
    }

    private static int getNetworkRoamingState(ServiceState serviceState, int i) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(i, 1);
        if (networkRegistrationInfo == null) {
            return 0;
        }
        int roamingType = networkRegistrationInfo.getRoamingType();
        if (networkRegistrationInfo.isNetworkRoaming() && roamingType == 0) {
            return 1;
        }
        return roamingType;
    }

    public static boolean isNetworkRoaming(ServiceState serviceState, int i) {
        NetworkRegistrationInfo networkRegistrationInfo;
        return (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(i, 1)) == null || !networkRegistrationInfo.isNetworkRoaming()) ? false : true;
    }

    public static boolean isNetworkRoaming(ServiceState serviceState) {
        return isNetworkRoaming(serviceState, 1) || isNetworkRoaming(serviceState, 2);
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0030 A[PHI: r1
  0x0030: PHI (r1v4 int) = (r1v2 int), (r1v3 int) binds: [B:9:0x002e, B:49:0x007a] A[DONT_GENERATE, DONT_INLINE]] */
    private void writeDataRatAtom(ServiceState serviceState) {
        if (DataConnectionStateTracker.getActiveDataSubId() != this.mPhone.getSubId()) {
            return;
        }
        int i = 2;
        int i2 = 1;
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null) {
            return;
        }
        int accessNetworkTechnology = networkRegistrationInfo.getAccessNetworkTechnology();
        int nrFrequencyRange = serviceState.getNrFrequencyRange();
        int nrState = serviceState.getNrState();
        if (!SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
            i = i2;
        } else if (accessNetworkTechnology == 14 || accessNetworkTechnology == 15 || accessNetworkTechnology == 3 || accessNetworkTechnology == 8 || accessNetworkTechnology == 9 || accessNetworkTechnology == 10 || accessNetworkTechnology == 5 || accessNetworkTechnology == 6 || accessNetworkTechnology == 12) {
            i = 3;
        } else if (accessNetworkTechnology != 7 && accessNetworkTechnology != 1 && accessNetworkTechnology != 2) {
            i2 = 4;
            if (accessNetworkTechnology != 4 && accessNetworkTechnology != 16) {
                if (accessNetworkTechnology == 20) {
                    i = nrFrequencyRange != 4 ? 8 : 9;
                } else if (accessNetworkTechnology != 13) {
                    i = 0;
                } else if (nrState == 3) {
                    i = nrFrequencyRange != 4 ? 6 : 7;
                } else if (nrState == 2) {
                    i = 5;
                } else {
                    i = i2;
                }
            }
        }
        if (i != this.mCurrentDataRat) {
            TelephonyStatsLog.write(854, i);
            this.mCurrentDataRat = i;
        }
    }

    int getCurrentDataRat() {
        return this.mCurrentDataRat;
    }

    protected long getTimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class TimestampedServiceState {
        private final PersistAtomsProto$CellularServiceState mServiceState;
        private final long mTimestamp;

        TimestampedServiceState(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState, long j) {
            this.mServiceState = persistAtomsProto$CellularServiceState;
            this.mTimestamp = j;
        }
    }
}
