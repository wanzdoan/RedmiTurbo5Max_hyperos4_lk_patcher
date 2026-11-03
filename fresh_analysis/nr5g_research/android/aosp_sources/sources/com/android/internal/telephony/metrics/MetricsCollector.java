package com.android.internal.telephony.metrics;

import android.R;
import android.app.StatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemProperties;
import android.telephony.SubscriptionManager;
import android.util.StatsEvent;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteControllerStats;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteSession;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularDataServiceSwitch;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularServiceState;
import com.android.internal.telephony.nano.PersistAtomsProto$DataCallSession;
import com.android.internal.telephony.nano.PersistAtomsProto$DataNetworkValidation;
import com.android.internal.telephony.nano.PersistAtomsProto$EmergencyNumbersInfo;
import com.android.internal.telephony.nano.PersistAtomsProto$GbaEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsDedicatedBearerEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsDedicatedBearerListenerEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationFeatureTagStats;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationServiceDescStats;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationStats;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationTermination;
import com.android.internal.telephony.nano.PersistAtomsProto$IncomingSms;
import com.android.internal.telephony.nano.PersistAtomsProto$MessagingReadRestrictionEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$NetworkRequestsV2;
import com.android.internal.telephony.nano.PersistAtomsProto$OtpEvaluationEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$OtpRedactionEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$OutgoingShortCodeSms;
import com.android.internal.telephony.nano.PersistAtomsProto$OutgoingSms;
import com.android.internal.telephony.nano.PersistAtomsProto$PresenceNotifyEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$RcsAcsProvisioningStats;
import com.android.internal.telephony.nano.PersistAtomsProto$RcsClientProvisioningStats;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteAccessController;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteConfigUpdater;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteController;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteEntitlement;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteIncomingDatagram;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteOutgoingDatagram;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteProvision;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteSession;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteSosMessageRecommender;
import com.android.internal.telephony.nano.PersistAtomsProto$SipDelegateStats;
import com.android.internal.telephony.nano.PersistAtomsProto$SipMessageResponse;
import com.android.internal.telephony.nano.PersistAtomsProto$SipTransportFeatureTagStats;
import com.android.internal.telephony.nano.PersistAtomsProto$SipTransportSession;
import com.android.internal.telephony.nano.PersistAtomsProto$UceEventStats;
import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallRatUsage;
import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallSession;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.ConcurrentUtils;
import com.android.telephony.Rlog;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

/* JADX INFO: loaded from: classes.dex */
public class MetricsCollector implements StatsManager.StatsPullAtomCallback {
    private static final long CELL_SERVICE_DURATION_BUCKET_MILLIS;
    private static final long DURATION_BUCKET_MILLIS;
    private static final long MILLIS_PER_HOUR;
    private static final long MILLIS_PER_MINUTE;
    private static final long MILLIS_PER_SECOND;
    private static final long MIN_COOLDOWN_MILLIS;
    private static final String TAG = MetricsCollector.class.getSimpleName();
    private static final Random sRandom;
    private final AirplaneModeStats mAirplaneModeStats;
    private final Context mContext;
    private final DefaultNetworkMonitor mDefaultNetworkMonitor;
    private final DeviceStateHelper mDeviceStateHelper;
    private final Set mOngoingDataCallStats;
    private final long mPowerCorrelatedMinCooldownMillis;
    private final StatsManager mStatsManager;
    private final PersistAtomsStorage mStorage;
    private final VonrHelper mVonrHelper;

    static {
        long millis = Duration.ofHours(1L).toMillis();
        MILLIS_PER_HOUR = millis;
        long millis2 = Duration.ofMinutes(1L).toMillis();
        MILLIS_PER_MINUTE = millis2;
        long millis3 = Duration.ofSeconds(1L).toMillis();
        MILLIS_PER_SECOND = millis3;
        MIN_COOLDOWN_MILLIS = millis * 23;
        DURATION_BUCKET_MILLIS = millis2 * 5;
        CELL_SERVICE_DURATION_BUCKET_MILLIS = TelephonyUtils.IS_DEBUGGABLE ? millis3 * 2 : millis2 * 5;
        sRandom = new Random();
    }

    public MetricsCollector(Context context, FeatureFlags featureFlags) {
        this(context, new PersistAtomsStorage(context), new DeviceStateHelper(context), new VonrHelper(featureFlags), new DefaultNetworkMonitor(context, featureFlags), featureFlags);
    }

    public MetricsCollector(Context context, PersistAtomsStorage persistAtomsStorage, DeviceStateHelper deviceStateHelper, VonrHelper vonrHelper, DefaultNetworkMonitor defaultNetworkMonitor, FeatureFlags featureFlags) {
        this.mOngoingDataCallStats = ConcurrentHashMap.newKeySet();
        this.mStorage = persistAtomsStorage;
        this.mDeviceStateHelper = deviceStateHelper;
        StatsManager statsManager = (StatsManager) context.getSystemService("stats");
        this.mStatsManager = statsManager;
        this.mVonrHelper = vonrHelper;
        this.mContext = context;
        if (statsManager != null) {
            registerAtom(10091);
            registerAtom(10090);
            registerAtom(10078);
            registerAtom(10079);
            registerAtom(10077);
            registerAtom(10076);
            registerAtom(10086);
            registerAtom(10087);
            registerAtom(10088);
            registerAtom(10089);
            registerAtom(10094);
            registerAtom(10093);
            registerAtom(10153);
            registerAtom(10133);
            registerAtom(10134);
            registerAtom(10135);
            registerAtom(10136);
            registerAtom(10137);
            registerAtom(10138);
            registerAtom(10139);
            registerAtom(10154);
            registerAtom(10140);
            registerAtom(10141);
            registerAtom(10142);
            registerAtom(10143);
            registerAtom(10144);
            registerAtom(10145);
            registerAtom(10146);
            registerAtom(10162);
            registerAtom(10180);
            registerAtom(10182);
            registerAtom(10183);
            registerAtom(10184);
            registerAtom(10185);
            registerAtom(10186);
            registerAtom(10187);
            registerAtom(10207);
            registerAtom(10211);
            registerAtom(10212);
            registerAtom(10214);
            registerAtom(10215);
            registerAtom(10219);
            registerAtom(1137);
            registerAtom(1136);
            registerAtom(1348);
            Rlog.d(TAG, "registered");
        } else {
            Rlog.e(TAG, "could not get StatsManager, atoms not registered");
        }
        this.mAirplaneModeStats = new AirplaneModeStats(context);
        this.mDefaultNetworkMonitor = new DefaultNetworkMonitor(context, featureFlags);
        this.mPowerCorrelatedMinCooldownMillis = TelephonyUtils.IS_DEBUGGABLE ? MILLIS_PER_MINUTE * 4 : context.getResources().getInteger(R.integer.config_pdp_reject_retry_delay_ms);
    }

    public int onPullAtom(int i, List<StatsEvent> list) {
        if (i == 1136) {
            return pullOtpRedactionEvent(list);
        }
        if (i == 1137) {
            return pullOtpEvaluationEvent(list);
        }
        if (i == 1348) {
            return pullMessagingReadRestrictionReported(list);
        }
        if (i == 10162) {
            return pullOutgoingShortCodeSms(list);
        }
        if (i == 10180) {
            return pullEmergencyNumbersInfo(list);
        }
        if (i == 10207) {
            return pullDataNetworkValidation(list);
        }
        if (i == 10219) {
            return pullSatelliteAccessController(list);
        }
        if (i == 10093) {
            return pullImsRegistrationTermination(list);
        }
        if (i == 10094) {
            return pullImsRegistrationStats(list);
        }
        if (i == 10153) {
            return pullTelephonyNetworkRequestsV2(list);
        }
        if (i == 10154) {
            return pullDeviceTelephonyProperties(list);
        }
        if (i == 10211) {
            return pullCarrierRoamingSatelliteSession(list);
        }
        if (i == 10212) {
            return pullCarrierRoamingSatelliteControllerStats(list);
        }
        if (i == 10214) {
            return pullSatelliteEntitlement(list);
        }
        if (i != 10215) {
            switch (i) {
                case 10076:
                    return pullVoiceCallSessions(list);
                case 10077:
                    return pullVoiceCallRatUsages(list);
                case 10078:
                    return pullSimSlotState(list);
                case 10079:
                    return pullSupportedRadioAccessFamily(list);
                default:
                    switch (i) {
                        case 10086:
                            return pullIncomingSms(list);
                        case 10087:
                            return pullOutgoingSms(list);
                        case 10088:
                            return pullCarrierIdTableVersion(list);
                        case 10089:
                            return pullDataCallSession(list);
                        case 10090:
                            return pullCellularServiceState(list);
                        case 10091:
                            return pullCellularDataServiceSwitch(list);
                        default:
                            switch (i) {
                                case 10133:
                                    return pullImsRegistrationFeatureTagStats(list);
                                case 10134:
                                    return pullRcsClientProvisioningStats(list);
                                case 10135:
                                    return pullRcsAcsProvisioningStats(list);
                                case 10136:
                                    return pullSipDelegateStats(list);
                                case 10137:
                                    return pullSipTransportFeatureTagStats(list);
                                case 10138:
                                    return pullSipMessageResponse(list);
                                case 10139:
                                    return pullSipTransportSession(list);
                                case 10140:
                                    return pullImsDedicatedBearerListenerEvent(list);
                                case 10141:
                                    return pullImsDedicatedBearerEvent(list);
                                case 10142:
                                    return pullImsRegistrationServiceDescStats(list);
                                case 10143:
                                    return pullUceEventStats(list);
                                case 10144:
                                    return pullPresenceNotifyEvent(list);
                                case 10145:
                                    return pullGbaEvent(list);
                                case 10146:
                                    return pullPerSimStatus(list);
                                default:
                                    switch (i) {
                                        case 10182:
                                            return pullSatelliteController(list);
                                        case 10183:
                                            return pullSatelliteSession(list);
                                        case 10184:
                                            return pullSatelliteIncomingDatagram(list);
                                        case 10185:
                                            return pullSatelliteOutgoingDatagram(list);
                                        case 10186:
                                            return pullSatelliteProvision(list);
                                        case 10187:
                                            return pullSatelliteSosMessageRecommender(list);
                                        default:
                                            Rlog.e(TAG, String.format("unexpected atom ID %d", Integer.valueOf(i)));
                                            return 1;
                                    }
                            }
                    }
            }
        }
        return pullSatelliteConfigUpdater(list);
    }

    public PersistAtomsStorage getAtomsStorage() {
        return this.mStorage;
    }

    public DeviceStateHelper getDeviceStateHelper() {
        return this.mDeviceStateHelper;
    }

    public VonrHelper getVonrHelper() {
        return this.mVonrHelper;
    }

    public void flushAtomsStorage() {
        concludeAll();
        this.mStorage.flushAtoms();
    }

    public void clearAtomsStorage() {
        concludeAll();
        this.mStorage.clearAtoms();
    }

    public void setSaveFileImmediately(boolean z) {
        this.mStorage.setSaveImmediately(z);
    }

    public byte[] getAtomsProtoBytes() {
        concludeAll();
        return this.mStorage.getAtomsProtoBytes();
    }

    public void registerOngoingDataCallStat(DataCallSessionStats dataCallSessionStats) {
        this.mOngoingDataCallStats.add(dataCallSessionStats);
    }

    public void unregisterOngoingDataCallStat(DataCallSessionStats dataCallSessionStats) {
        this.mOngoingDataCallStats.remove(dataCallSessionStats);
    }

    public DefaultNetworkMonitor getDefaultNetworkMonitor() {
        return this.mDefaultNetworkMonitor;
    }

    private void concludeDataCallSessionStats() {
        Iterator it = this.mOngoingDataCallStats.iterator();
        while (it.hasNext()) {
            ((DataCallSessionStats) it.next()).conclude();
        }
    }

    private void concludeImsStats() {
        for (Phone phone : getPhonesIfAny()) {
            ImsPhone imsPhone = (ImsPhone) phone.getImsPhone();
            if (imsPhone != null) {
                imsPhone.getImsStats().conclude();
            }
        }
    }

    private void concludeServiceStateStats() {
        for (Phone phone : getPhonesIfAny()) {
            phone.getServiceStateTracker().getServiceStateStats().conclude();
        }
    }

    private void concludeRcsStats() {
        RcsStats rcsStats = RcsStats.getInstance();
        if (rcsStats != null) {
            rcsStats.concludeSipTransportFeatureTagsStat();
            rcsStats.onFlushIncompleteRcsAcsProvisioningStats();
            rcsStats.onFlushIncompleteImsRegistrationServiceDescStats();
            rcsStats.onFlushIncompleteImsRegistrationFeatureTagStats();
        }
    }

    private void concludeAll() {
        concludeDataCallSessionStats();
        concludeImsStats();
        concludeServiceStateStats();
        concludeRcsStats();
    }

    private static int pullSimSlotState(List list) {
        try {
            SimSlotState currentState = SimSlotState.getCurrentState();
            list.add(TelephonyStatsLog.buildStatsEvent(10078, currentState.numActiveSlots, currentState.numActiveSims, currentState.numActiveEsims, currentState.numActiveEsimSlots, currentState.numActiveMepSlots, currentState.numEsimSlots));
            return 0;
        } catch (RuntimeException unused) {
            return 1;
        }
    }

    private static int pullSupportedRadioAccessFamily(List list) {
        if (getPhonesIfAny().length == 0) {
            return 1;
        }
        long radioAccessFamily = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            radioAccessFamily |= (long) phone.getRadioAccessFamily();
        }
        list.add(TelephonyStatsLog.buildStatsEvent(10079, radioAccessFamily));
        return 0;
    }

    private static int pullCarrierIdTableVersion(List list) {
        Phone[] phonesIfAny = getPhonesIfAny();
        if (phonesIfAny.length == 0) {
            return 1;
        }
        list.add(TelephonyStatsLog.buildStatsEvent(10088, phonesIfAny[0].getCarrierIdListVersion()));
        return 0;
    }

    private int pullVoiceCallRatUsages(final List list) {
        PersistAtomsProto$VoiceCallRatUsage[] voiceCallRatUsages = this.mStorage.getVoiceCallRatUsages(MIN_COOLDOWN_MILLIS);
        if (voiceCallRatUsages != null) {
            Arrays.stream(voiceCallRatUsages).sorted(Comparator.comparingLong(new ToLongFunction() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda29
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    return MetricsCollector.m1215$r8$lambda$Kc1E0MSHQKkjmGUQNEzhtsr6w((PersistAtomsProto$VoiceCallRatUsage) obj);
                }
            })).filter(new Predicate() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda30
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return MetricsCollector.m1211$r8$lambda$BeuGNxMgTipax6kppRdy0CR1Nk((PersistAtomsProto$VoiceCallRatUsage) obj);
                }
            }).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$VoiceCallRatUsage) obj));
                }
            });
            Rlog.d(TAG, String.format("%d out of %d VOICE_CALL_RAT_USAGE pulled", Integer.valueOf(list.size()), Integer.valueOf(voiceCallRatUsages.length)));
            return 0;
        }
        Rlog.w(TAG, "VOICE_CALL_RAT_USAGE pull too frequent, skipping");
        return 1;
    }

    /* JADX INFO: renamed from: $r8$lambda$Kc1E0MS-HQKkj-mGUQNEzhtsr6w, reason: not valid java name */
    public static /* synthetic */ long m1215$r8$lambda$Kc1E0MSHQKkjmGUQNEzhtsr6w(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
        return (((long) persistAtomsProto$VoiceCallRatUsage.carrierId) << 32) | ((long) persistAtomsProto$VoiceCallRatUsage.rat);
    }

    /* JADX INFO: renamed from: $r8$lambda$BeuGNxMgTipax6kppRdy0CR-1Nk, reason: not valid java name */
    public static /* synthetic */ boolean m1211$r8$lambda$BeuGNxMgTipax6kppRdy0CR1Nk(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
        return persistAtomsProto$VoiceCallRatUsage.callCount >= 5;
    }

    private int pullVoiceCallSessions(final List list) {
        PersistAtomsProto$VoiceCallSession[] voiceCallSessions = this.mStorage.getVoiceCallSessions(MIN_COOLDOWN_MILLIS);
        if (voiceCallSessions != null) {
            Arrays.stream(voiceCallSessions).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$VoiceCallSession) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "VOICE_CALL_SESSION pull too frequent, skipping");
        return 1;
    }

    private int pullIncomingSms(final List list) {
        PersistAtomsProto$IncomingSms[] incomingSms = this.mStorage.getIncomingSms(MIN_COOLDOWN_MILLIS);
        if (incomingSms != null) {
            Arrays.stream(incomingSms).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$IncomingSms) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "INCOMING_SMS pull too frequent, skipping");
        return 1;
    }

    private int pullOutgoingSms(final List list) {
        PersistAtomsProto$OutgoingSms[] outgoingSms = this.mStorage.getOutgoingSms(MIN_COOLDOWN_MILLIS);
        if (outgoingSms != null) {
            Arrays.stream(outgoingSms).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$OutgoingSms) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "OUTGOING_SMS pull too frequent, skipping");
        return 1;
    }

    private int pullDataCallSession(final List list) {
        concludeDataCallSessionStats();
        PersistAtomsProto$DataCallSession[] dataCallSessions = this.mStorage.getDataCallSessions(this.mPowerCorrelatedMinCooldownMillis);
        if (dataCallSessions != null) {
            Arrays.stream(dataCallSessions).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$DataCallSession) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "DATA_CALL_SESSION pull too frequent, skipping");
        return 1;
    }

    private int pullCellularDataServiceSwitch(final List list) {
        PersistAtomsProto$CellularDataServiceSwitch[] cellularDataServiceSwitches = this.mStorage.getCellularDataServiceSwitches(MIN_COOLDOWN_MILLIS);
        if (cellularDataServiceSwitches != null) {
            Arrays.stream(cellularDataServiceSwitches).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda36
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$CellularDataServiceSwitch) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "CELLULAR_DATA_SERVICE_SWITCH pull too frequent, skipping");
        return 1;
    }

    private int pullCellularServiceState(final List list) {
        concludeServiceStateStats();
        PersistAtomsProto$CellularServiceState[] cellularServiceStates = this.mStorage.getCellularServiceStates(this.mPowerCorrelatedMinCooldownMillis);
        if (cellularServiceStates != null) {
            Arrays.stream(cellularServiceStates).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda21
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$CellularServiceState) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "CELLULAR_SERVICE_STATE pull too frequent, skipping");
        return 1;
    }

    private int pullImsRegistrationStats(final List list) {
        concludeImsStats();
        PersistAtomsProto$ImsRegistrationStats[] imsRegistrationStats = this.mStorage.getImsRegistrationStats(MIN_COOLDOWN_MILLIS);
        if (imsRegistrationStats != null) {
            Arrays.stream(imsRegistrationStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda37
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsRegistrationStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_REGISTRATION_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullImsRegistrationTermination(final List list) {
        PersistAtomsProto$ImsRegistrationTermination[] imsRegistrationTerminations = this.mStorage.getImsRegistrationTerminations(this.mPowerCorrelatedMinCooldownMillis);
        if (imsRegistrationTerminations != null) {
            Arrays.stream(imsRegistrationTerminations).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsRegistrationTermination) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_REGISTRATION_TERMINATION pull too frequent, skipping");
        return 1;
    }

    private int pullTelephonyNetworkRequestsV2(final List list) {
        PersistAtomsProto$NetworkRequestsV2[] networkRequestsV2 = this.mStorage.getNetworkRequestsV2(MIN_COOLDOWN_MILLIS);
        if (networkRequestsV2 != null) {
            Arrays.stream(networkRequestsV2).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda35
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$NetworkRequestsV2) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "TELEPHONY_NETWORK_REQUESTS_V2 pull too frequent, skipping");
        return 1;
    }

    private int pullDeviceTelephonyProperties(List list) {
        Phone[] phonesIfAny = getPhonesIfAny();
        if (phonesIfAny.length == 0) {
            return 1;
        }
        boolean zAnyMatch = Arrays.stream(phonesIfAny).anyMatch(new Predicate() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MetricsCollector.$r8$lambda$AQWUsoQc1IuIZOsWgd8_Y5xfMoo((Phone) obj);
            }
        });
        boolean zAnyMatch2 = Arrays.stream(phonesIfAny).anyMatch(new Predicate() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((Phone) obj).isManagedProfile();
            }
        });
        int iCount = (int) Arrays.stream(UiccController.getInstance().getUiccSlots()).filter(new Predicate() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((UiccSlot) obj).isMultipleEnabledProfileSupported();
            }
        }).count();
        Context context = this.mContext;
        PackageManager packageManager = context != null ? context.getPackageManager() : null;
        int autoDataSwitchToggleCount = this.mStorage.getAutoDataSwitchToggleCount();
        int i = Build.VERSION.DEVICE_INITIAL_SDK_INT;
        list.add(TelephonyStatsLog.buildStatsEvent(10154, true, zAnyMatch, autoDataSwitchToggleCount, zAnyMatch2, iCount, SystemProperties.getInt("ro.vendor.api_level", i), SystemProperties.getInt("ro.board_api_level", i), getTelephonyFeaturesBitmask(packageManager)));
        return 0;
    }

    public static /* synthetic */ boolean $r8$lambda$AQWUsoQc1IuIZOsWgd8_Y5xfMoo(Phone phone) {
        return phone.getSubId() != SubscriptionManager.getDefaultDataSubscriptionId() && phone.getDataSettingsManager().isMobileDataPolicyEnabled(3);
    }

    private long getTelephonyFeaturesBitmask(PackageManager packageManager) {
        if (packageManager == null) {
            return 0L;
        }
        long j = packageManager.hasSystemFeature("android.hardware.telephony.calling") ? 1L : 0L;
        if (packageManager.hasSystemFeature("android.hardware.telephony.data")) {
            j |= 2;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.messaging")) {
            j |= 4;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.radio.access")) {
            j |= 8;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.subscription")) {
            j |= 16;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony")) {
            j |= 32;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.ims")) {
            j |= 64;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.euicc")) {
            j |= 128;
        }
        if (packageManager.hasSystemFeature("android.hardware.telephony.euicc.mep")) {
            j |= 256;
        }
        return packageManager.hasSystemFeature("android.hardware.telephony.satellite") ? 512 | j : j;
    }

    private int pullImsRegistrationFeatureTagStats(final List list) {
        RcsStats.getInstance().onFlushIncompleteImsRegistrationFeatureTagStats();
        PersistAtomsProto$ImsRegistrationFeatureTagStats[] imsRegistrationFeatureTagStats = this.mStorage.getImsRegistrationFeatureTagStats(MIN_COOLDOWN_MILLIS);
        if (imsRegistrationFeatureTagStats != null) {
            Arrays.stream(imsRegistrationFeatureTagStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda26
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsRegistrationFeatureTagStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_REGISTRATION_FEATURE_TAG_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullRcsClientProvisioningStats(final List list) {
        PersistAtomsProto$RcsClientProvisioningStats[] rcsClientProvisioningStats = this.mStorage.getRcsClientProvisioningStats(MIN_COOLDOWN_MILLIS);
        if (rcsClientProvisioningStats != null) {
            Arrays.stream(rcsClientProvisioningStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$RcsClientProvisioningStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "RCS_CLIENT_PROVISIONING_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullRcsAcsProvisioningStats(final List list) {
        RcsStats.getInstance().onFlushIncompleteRcsAcsProvisioningStats();
        PersistAtomsProto$RcsAcsProvisioningStats[] rcsAcsProvisioningStats = this.mStorage.getRcsAcsProvisioningStats(MIN_COOLDOWN_MILLIS);
        if (rcsAcsProvisioningStats != null) {
            Arrays.stream(rcsAcsProvisioningStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda17
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$RcsAcsProvisioningStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "RCS_ACS_PROVISIONING_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullSipDelegateStats(final List list) {
        PersistAtomsProto$SipDelegateStats[] sipDelegateStats = this.mStorage.getSipDelegateStats(MIN_COOLDOWN_MILLIS);
        if (sipDelegateStats != null) {
            Arrays.stream(sipDelegateStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda34
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SipDelegateStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SIP_DELEGATE_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullSipTransportFeatureTagStats(final List list) {
        RcsStats.getInstance().concludeSipTransportFeatureTagsStat();
        PersistAtomsProto$SipTransportFeatureTagStats[] sipTransportFeatureTagStats = this.mStorage.getSipTransportFeatureTagStats(MIN_COOLDOWN_MILLIS);
        if (sipTransportFeatureTagStats != null) {
            Arrays.stream(sipTransportFeatureTagStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda40
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SipTransportFeatureTagStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SIP_DELEGATE_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullSipMessageResponse(final List list) {
        PersistAtomsProto$SipMessageResponse[] sipMessageResponse = this.mStorage.getSipMessageResponse(MIN_COOLDOWN_MILLIS);
        if (sipMessageResponse != null) {
            Arrays.stream(sipMessageResponse).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda19
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SipMessageResponse) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "RCS_SIP_MESSAGE_RESPONSE pull too frequent, skipping");
        return 1;
    }

    private int pullSipTransportSession(final List list) {
        PersistAtomsProto$SipTransportSession[] sipTransportSession = this.mStorage.getSipTransportSession(MIN_COOLDOWN_MILLIS);
        if (sipTransportSession != null) {
            Arrays.stream(sipTransportSession).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SipTransportSession) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "RCS_SIP_TRANSPORT_SESSION pull too frequent, skipping");
        return 1;
    }

    private int pullImsDedicatedBearerListenerEvent(final List list) {
        PersistAtomsProto$ImsDedicatedBearerListenerEvent[] imsDedicatedBearerListenerEvent = this.mStorage.getImsDedicatedBearerListenerEvent(MIN_COOLDOWN_MILLIS);
        if (imsDedicatedBearerListenerEvent != null) {
            Arrays.stream(imsDedicatedBearerListenerEvent).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda44
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsDedicatedBearerListenerEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_DEDICATED_BEARER_LISTENER_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullImsDedicatedBearerEvent(final List list) {
        PersistAtomsProto$ImsDedicatedBearerEvent[] imsDedicatedBearerEvent = this.mStorage.getImsDedicatedBearerEvent(MIN_COOLDOWN_MILLIS);
        if (imsDedicatedBearerEvent != null) {
            Arrays.stream(imsDedicatedBearerEvent).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda39
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsDedicatedBearerEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_DEDICATED_BEARER_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullImsRegistrationServiceDescStats(final List list) {
        RcsStats.getInstance().onFlushIncompleteImsRegistrationServiceDescStats();
        PersistAtomsProto$ImsRegistrationServiceDescStats[] imsRegistrationServiceDescStats = this.mStorage.getImsRegistrationServiceDescStats(MIN_COOLDOWN_MILLIS);
        if (imsRegistrationServiceDescStats != null) {
            Arrays.stream(imsRegistrationServiceDescStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda24
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$ImsRegistrationServiceDescStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "IMS_REGISTRATION_SERVICE_DESC_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullUceEventStats(final List list) {
        PersistAtomsProto$UceEventStats[] uceEventStats = this.mStorage.getUceEventStats(MIN_COOLDOWN_MILLIS);
        if (uceEventStats != null) {
            Arrays.stream(uceEventStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$UceEventStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "UCE_EVENT_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullPresenceNotifyEvent(final List list) {
        PersistAtomsProto$PresenceNotifyEvent[] presenceNotifyEvent = this.mStorage.getPresenceNotifyEvent(MIN_COOLDOWN_MILLIS);
        if (presenceNotifyEvent != null) {
            Arrays.stream(presenceNotifyEvent).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda32
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$PresenceNotifyEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "PRESENCE_NOTIFY_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullGbaEvent(final List list) {
        PersistAtomsProto$GbaEvent[] gbaEvent = this.mStorage.getGbaEvent(MIN_COOLDOWN_MILLIS);
        if (gbaEvent != null) {
            Arrays.stream(gbaEvent).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda41
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$GbaEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "GBA_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullPerSimStatus(List list) {
        Phone[] phonesIfAny = getPhonesIfAny();
        int i = 1;
        int i2 = 0;
        for (int length = phonesIfAny.length; i2 < length; length = length) {
            Phone phone = phonesIfAny[i2];
            PerSimStatus currentState = PerSimStatus.getCurrentState(phone);
            if (currentState != null) {
                list.add(TelephonyStatsLog.buildStatsEvent(10146, phone.getPhoneId(), currentState.carrierId, currentState.phoneNumberSourceUicc, currentState.phoneNumberSourceCarrier, currentState.phoneNumberSourceIms, currentState.advancedCallingSettingEnabled, currentState.voWiFiSettingEnabled, currentState.voWiFiModeSetting, currentState.voWiFiRoamingModeSetting, currentState.vtSettingEnabled, currentState.dataRoamingEnabled, currentState.preferredNetworkType, currentState.disabled2g, currentState.pin1Enabled, currentState.minimumVoltageClass, currentState.userModifiedApnTypes, currentState.unmeteredNetworks, currentState.vonrEnabled, currentState.crossSimCallingEnabled));
                i = 0;
            }
            i2++;
            phonesIfAny = phonesIfAny;
        }
        return i;
    }

    private int pullOutgoingShortCodeSms(final List list) {
        PersistAtomsProto$OutgoingShortCodeSms[] outgoingShortCodeSms = this.mStorage.getOutgoingShortCodeSms(MIN_COOLDOWN_MILLIS);
        if (outgoingShortCodeSms != null) {
            Arrays.stream(outgoingShortCodeSms).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda38
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$OutgoingShortCodeSms) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "OUTGOING_SHORT_CODE_SMS pull too frequent, skipping");
        return 1;
    }

    private int pullEmergencyNumbersInfo(final List list) {
        EmergencyNumberTracker emergencyNumberTracker;
        int i = 0;
        for (Phone phone : getPhonesIfAny()) {
            if (phone != null && (emergencyNumberTracker = phone.getEmergencyNumberTracker()) != null) {
                Arrays.stream(emergencyNumberTracker.getEmergencyNumbersProtoArray()).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda42
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$EmergencyNumbersInfo) obj));
                    }
                });
                i = 1;
            }
        }
        return i ^ 1;
    }

    private int pullSatelliteController(final List list) {
        PersistAtomsProto$SatelliteController[] satelliteControllerStats = this.mStorage.getSatelliteControllerStats(MIN_COOLDOWN_MILLIS);
        if (satelliteControllerStats != null) {
            Arrays.stream(satelliteControllerStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteController) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_CONTROLLER pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteSession(final List list) {
        PersistAtomsProto$SatelliteSession[] satelliteSessionStats = this.mStorage.getSatelliteSessionStats(MIN_COOLDOWN_MILLIS);
        if (satelliteSessionStats != null) {
            Arrays.stream(satelliteSessionStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda43
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteSession) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_SESSION pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteIncomingDatagram(final List list) {
        PersistAtomsProto$SatelliteIncomingDatagram[] satelliteIncomingDatagramStats = this.mStorage.getSatelliteIncomingDatagramStats(MIN_COOLDOWN_MILLIS);
        if (satelliteIncomingDatagramStats != null) {
            Arrays.stream(satelliteIncomingDatagramStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteIncomingDatagram) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_INCOMING_DATAGRAM pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteOutgoingDatagram(final List list) {
        PersistAtomsProto$SatelliteOutgoingDatagram[] satelliteOutgoingDatagramStats = this.mStorage.getSatelliteOutgoingDatagramStats(MIN_COOLDOWN_MILLIS);
        if (satelliteOutgoingDatagramStats != null) {
            Arrays.stream(satelliteOutgoingDatagramStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteOutgoingDatagram) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_OUTGOING_DATAGRAM pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteProvision(final List list) {
        PersistAtomsProto$SatelliteProvision[] satelliteProvisionStats = this.mStorage.getSatelliteProvisionStats(MIN_COOLDOWN_MILLIS);
        if (satelliteProvisionStats != null) {
            Arrays.stream(satelliteProvisionStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteProvision) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_PROVISION pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteSosMessageRecommender(final List list) {
        PersistAtomsProto$SatelliteSosMessageRecommender[] satelliteSosMessageRecommenderStats = this.mStorage.getSatelliteSosMessageRecommenderStats(MIN_COOLDOWN_MILLIS);
        if (satelliteSosMessageRecommenderStats != null) {
            Arrays.stream(satelliteSosMessageRecommenderStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda27
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteSosMessageRecommender) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_SOS_MESSAGE_RECOMMENDER pull too frequent, skipping");
        return 1;
    }

    private int pullDataNetworkValidation(final List list) {
        PersistAtomsProto$DataNetworkValidation[] dataNetworkValidation = this.mStorage.getDataNetworkValidation(this.mPowerCorrelatedMinCooldownMillis);
        if (dataNetworkValidation != null) {
            Arrays.stream(dataNetworkValidation).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$DataNetworkValidation) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "DATA_NETWORK_VALIDATION pull too frequent, skipping");
        return 1;
    }

    private int pullCarrierRoamingSatelliteSession(final List list) {
        PersistAtomsProto$CarrierRoamingSatelliteSession[] carrierRoamingSatelliteSessionStats = this.mStorage.getCarrierRoamingSatelliteSessionStats(MIN_COOLDOWN_MILLIS);
        if (carrierRoamingSatelliteSessionStats != null) {
            Arrays.stream(carrierRoamingSatelliteSessionStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda28
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$CarrierRoamingSatelliteSession) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "CARRIER_ROAMING_SATELLITE_SESSION pull too frequent, skipping");
        return 1;
    }

    private int pullCarrierRoamingSatelliteControllerStats(final List list) {
        PersistAtomsProto$CarrierRoamingSatelliteControllerStats[] carrierRoamingSatelliteControllerStats = this.mStorage.getCarrierRoamingSatelliteControllerStats(MIN_COOLDOWN_MILLIS);
        if (carrierRoamingSatelliteControllerStats != null) {
            Arrays.stream(carrierRoamingSatelliteControllerStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$CarrierRoamingSatelliteControllerStats) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "CARRIER_ROAMING_SATELLITE_CONTROLLER_STATS pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteEntitlement(final List list) {
        PersistAtomsProto$SatelliteEntitlement[] satelliteEntitlementStats = this.mStorage.getSatelliteEntitlementStats(MIN_COOLDOWN_MILLIS);
        if (satelliteEntitlementStats != null) {
            Arrays.stream(satelliteEntitlementStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteEntitlement) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_ENTITLEMENT pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteConfigUpdater(final List list) {
        PersistAtomsProto$SatelliteConfigUpdater[] satelliteConfigUpdaterStats = this.mStorage.getSatelliteConfigUpdaterStats(MIN_COOLDOWN_MILLIS);
        if (satelliteConfigUpdaterStats != null) {
            Arrays.stream(satelliteConfigUpdaterStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda25
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteConfigUpdater) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_CONFIG_UPDATER pull too frequent, skipping");
        return 1;
    }

    private int pullSatelliteAccessController(final List list) {
        PersistAtomsProto$SatelliteAccessController[] satelliteAccessControllerStats = this.mStorage.getSatelliteAccessControllerStats(MIN_COOLDOWN_MILLIS);
        if (satelliteAccessControllerStats != null) {
            Arrays.stream(satelliteAccessControllerStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$SatelliteAccessController) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "SATELLITE_ACCESS_CONTROLLER pull too frequent, skipping");
        return 1;
    }

    private int pullOtpRedactionEvent(final List list) {
        PersistAtomsProto$OtpRedactionEvent[] otpRedactionEventStats = this.mStorage.getOtpRedactionEventStats(MIN_COOLDOWN_MILLIS);
        if (otpRedactionEventStats != null) {
            Arrays.stream(otpRedactionEventStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$OtpRedactionEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "OTP_REDACTION_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullOtpEvaluationEvent(final List list) {
        PersistAtomsProto$OtpEvaluationEvent[] otpEvaluationEventStats = this.mStorage.getOtpEvaluationEventStats(MIN_COOLDOWN_MILLIS);
        if (otpEvaluationEventStats != null) {
            Arrays.stream(otpEvaluationEventStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda23
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$OtpEvaluationEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "OTP_EVALUATION_EVENT pull too frequent, skipping");
        return 1;
    }

    private int pullMessagingReadRestrictionReported(final List list) {
        PersistAtomsProto$MessagingReadRestrictionEvent[] messagingReadRestrictionEventStats = this.mStorage.getMessagingReadRestrictionEventStats(MIN_COOLDOWN_MILLIS);
        if (messagingReadRestrictionEventStats != null) {
            Arrays.stream(messagingReadRestrictionEventStats).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.MetricsCollector$$ExternalSyntheticLambda33
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    list.add(MetricsCollector.buildStatsEvent((PersistAtomsProto$MessagingReadRestrictionEvent) obj));
                }
            });
            return 0;
        }
        Rlog.w(TAG, "MESSAGING_READ_RESTRICTION_REPORTED pull too frequent, skipping");
        return 1;
    }

    private void registerAtom(int i) {
        this.mStatsManager.setPullAtomCallback(i, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch) {
        return TelephonyStatsLog.buildStatsEvent(10091, persistAtomsProto$CellularDataServiceSwitch.ratFrom, persistAtomsProto$CellularDataServiceSwitch.ratTo, persistAtomsProto$CellularDataServiceSwitch.simSlotIndex, persistAtomsProto$CellularDataServiceSwitch.isMultiSim, persistAtomsProto$CellularDataServiceSwitch.carrierId, persistAtomsProto$CellularDataServiceSwitch.switchCount, persistAtomsProto$CellularDataServiceSwitch.isOpportunistic);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState) {
        return TelephonyStatsLog.buildStatsEvent(10090, persistAtomsProto$CellularServiceState.voiceRat, persistAtomsProto$CellularServiceState.dataRat, persistAtomsProto$CellularServiceState.voiceRoamingType, persistAtomsProto$CellularServiceState.dataRoamingType, persistAtomsProto$CellularServiceState.isEndc, persistAtomsProto$CellularServiceState.simSlotIndex, persistAtomsProto$CellularServiceState.isMultiSim, persistAtomsProto$CellularServiceState.carrierId, roundAndConvertMillisToSeconds(persistAtomsProto$CellularServiceState.totalTimeMillis, CELL_SERVICE_DURATION_BUCKET_MILLIS), persistAtomsProto$CellularServiceState.isEmergencyOnly, persistAtomsProto$CellularServiceState.isInternetPdnUp, persistAtomsProto$CellularServiceState.foldState, persistAtomsProto$CellularServiceState.overrideVoiceService, persistAtomsProto$CellularServiceState.isDataEnabled, persistAtomsProto$CellularServiceState.isIwlanCrossSim, persistAtomsProto$CellularServiceState.isNtn, persistAtomsProto$CellularServiceState.isNbIotNtn, persistAtomsProto$CellularServiceState.isOpportunistic, persistAtomsProto$CellularServiceState.plmn);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
        return TelephonyStatsLog.buildStatsEvent(10077, persistAtomsProto$VoiceCallRatUsage.carrierId, persistAtomsProto$VoiceCallRatUsage.rat, roundAndConvertMillisToSeconds(persistAtomsProto$VoiceCallRatUsage.totalDurationMillis), persistAtomsProto$VoiceCallRatUsage.callCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession) {
        return TelephonyStatsLog.buildStatsEvent(10076, persistAtomsProto$VoiceCallSession.bearerAtStart, persistAtomsProto$VoiceCallSession.bearerAtEnd, persistAtomsProto$VoiceCallSession.direction, 0, persistAtomsProto$VoiceCallSession.setupFailed, persistAtomsProto$VoiceCallSession.disconnectReasonCode, persistAtomsProto$VoiceCallSession.disconnectExtraCode, persistAtomsProto$VoiceCallSession.disconnectExtraMessage, persistAtomsProto$VoiceCallSession.ratAtStart, persistAtomsProto$VoiceCallSession.ratAtEnd, persistAtomsProto$VoiceCallSession.ratSwitchCount, persistAtomsProto$VoiceCallSession.codecBitmask, persistAtomsProto$VoiceCallSession.concurrentCallCountAtStart, persistAtomsProto$VoiceCallSession.concurrentCallCountAtEnd, persistAtomsProto$VoiceCallSession.simSlotIndex, persistAtomsProto$VoiceCallSession.isMultiSim, persistAtomsProto$VoiceCallSession.isEsim, persistAtomsProto$VoiceCallSession.carrierId, persistAtomsProto$VoiceCallSession.srvccCompleted, persistAtomsProto$VoiceCallSession.srvccFailureCount, persistAtomsProto$VoiceCallSession.srvccCancellationCount, persistAtomsProto$VoiceCallSession.rttEnabled, persistAtomsProto$VoiceCallSession.isEmergency, persistAtomsProto$VoiceCallSession.isRoaming, sRandom.nextInt(), persistAtomsProto$VoiceCallSession.signalStrengthAtEnd, persistAtomsProto$VoiceCallSession.bandAtEnd, persistAtomsProto$VoiceCallSession.setupDurationMillis, persistAtomsProto$VoiceCallSession.mainCodecQuality, persistAtomsProto$VoiceCallSession.videoEnabled, persistAtomsProto$VoiceCallSession.ratAtConnected, persistAtomsProto$VoiceCallSession.isMultiparty, persistAtomsProto$VoiceCallSession.callDuration, persistAtomsProto$VoiceCallSession.lastKnownRat, persistAtomsProto$VoiceCallSession.foldState, persistAtomsProto$VoiceCallSession.ratSwitchCountAfterConnected, persistAtomsProto$VoiceCallSession.handoverInProgress, persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtStart, persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtEnd, persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtConnected, persistAtomsProto$VoiceCallSession.vonrEnabled, persistAtomsProto$VoiceCallSession.isNtn, persistAtomsProto$VoiceCallSession.supportsBusinessCallComposer, persistAtomsProto$VoiceCallSession.callComposerStatus, persistAtomsProto$VoiceCallSession.preciseCallStateOnSetup);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$IncomingSms persistAtomsProto$IncomingSms) {
        return TelephonyStatsLog.buildStatsEvent(10086, persistAtomsProto$IncomingSms.smsFormat, persistAtomsProto$IncomingSms.smsTech, persistAtomsProto$IncomingSms.rat, persistAtomsProto$IncomingSms.smsType, persistAtomsProto$IncomingSms.totalParts, persistAtomsProto$IncomingSms.receivedParts, persistAtomsProto$IncomingSms.blocked, persistAtomsProto$IncomingSms.error, persistAtomsProto$IncomingSms.isRoaming, persistAtomsProto$IncomingSms.simSlotIndex, persistAtomsProto$IncomingSms.isMultiSim, persistAtomsProto$IncomingSms.isEsim, persistAtomsProto$IncomingSms.carrierId, persistAtomsProto$IncomingSms.messageId, persistAtomsProto$IncomingSms.count, persistAtomsProto$IncomingSms.isManagedProfile, persistAtomsProto$IncomingSms.isNtn, persistAtomsProto$IncomingSms.isEmergency, persistAtomsProto$IncomingSms.isNbIotNtn, persistAtomsProto$IncomingSms.pduLength, persistAtomsProto$IncomingSms.plmn);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$OutgoingSms persistAtomsProto$OutgoingSms) {
        return TelephonyStatsLog.buildStatsEvent(10087, persistAtomsProto$OutgoingSms.smsFormat, persistAtomsProto$OutgoingSms.smsTech, persistAtomsProto$OutgoingSms.rat, persistAtomsProto$OutgoingSms.sendResult, persistAtomsProto$OutgoingSms.errorCode, persistAtomsProto$OutgoingSms.isRoaming, persistAtomsProto$OutgoingSms.isFromDefaultApp, persistAtomsProto$OutgoingSms.simSlotIndex, persistAtomsProto$OutgoingSms.isMultiSim, persistAtomsProto$OutgoingSms.isEsim, persistAtomsProto$OutgoingSms.carrierId, persistAtomsProto$OutgoingSms.messageId, persistAtomsProto$OutgoingSms.retryId, persistAtomsProto$OutgoingSms.intervalMillis, persistAtomsProto$OutgoingSms.count, persistAtomsProto$OutgoingSms.sendErrorCode, persistAtomsProto$OutgoingSms.networkErrorCode, persistAtomsProto$OutgoingSms.isManagedProfile, persistAtomsProto$OutgoingSms.isEmergency, persistAtomsProto$OutgoingSms.isNtn, persistAtomsProto$OutgoingSms.isMtSmsPolling, persistAtomsProto$OutgoingSms.isNbIotNtn, persistAtomsProto$OutgoingSms.pduLength, persistAtomsProto$OutgoingSms.callingPackageName, persistAtomsProto$OutgoingSms.appUid, persistAtomsProto$OutgoingSms.plmn, persistAtomsProto$OutgoingSms.satelliteMessageTrigger);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession) {
        return TelephonyStatsLog.buildStatsEvent(10089, persistAtomsProto$DataCallSession.dimension, persistAtomsProto$DataCallSession.isMultiSim, persistAtomsProto$DataCallSession.isEsim, 0, persistAtomsProto$DataCallSession.apnTypeBitmask, persistAtomsProto$DataCallSession.carrierId, persistAtomsProto$DataCallSession.isRoaming, persistAtomsProto$DataCallSession.ratAtEnd, persistAtomsProto$DataCallSession.oosAtEnd, persistAtomsProto$DataCallSession.ratSwitchCount, persistAtomsProto$DataCallSession.isOpportunistic, persistAtomsProto$DataCallSession.ipType, persistAtomsProto$DataCallSession.setupFailed, persistAtomsProto$DataCallSession.failureCause, persistAtomsProto$DataCallSession.suggestedRetryMillis, persistAtomsProto$DataCallSession.deactivateReason, roundAndConvertMillisToMinutes(persistAtomsProto$DataCallSession.durationMinutes * MILLIS_PER_MINUTE), persistAtomsProto$DataCallSession.ongoing, persistAtomsProto$DataCallSession.bandAtEnd, persistAtomsProto$DataCallSession.handoverFailureCauses, persistAtomsProto$DataCallSession.handoverFailureRat, persistAtomsProto$DataCallSession.isNonDds, persistAtomsProto$DataCallSession.isIwlanCrossSim, persistAtomsProto$DataCallSession.isNtn, persistAtomsProto$DataCallSession.isSatelliteTransport, persistAtomsProto$DataCallSession.isProvisioningProfile, persistAtomsProto$DataCallSession.isNbIotNtn, persistAtomsProto$DataCallSession.sliceCapability, persistAtomsProto$DataCallSession.plmn, persistAtomsProto$DataCallSession.connectionCapability);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats) {
        return TelephonyStatsLog.buildStatsEvent(10094, persistAtomsProto$ImsRegistrationStats.carrierId, persistAtomsProto$ImsRegistrationStats.simSlotIndex, persistAtomsProto$ImsRegistrationStats.rat, roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.registeredMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.voiceCapableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.voiceAvailableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.smsCapableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.smsAvailableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.videoCapableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.videoAvailableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.utCapableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.utAvailableMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.registeringMillis), roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationStats.unregisteredMillis), persistAtomsProto$ImsRegistrationStats.isIwlanCrossSim, persistAtomsProto$ImsRegistrationStats.registeredTimes);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTermination) {
        return TelephonyStatsLog.buildStatsEvent(10093, persistAtomsProto$ImsRegistrationTermination.carrierId, persistAtomsProto$ImsRegistrationTermination.isMultiSim, persistAtomsProto$ImsRegistrationTermination.ratAtEnd, persistAtomsProto$ImsRegistrationTermination.setupFailed, persistAtomsProto$ImsRegistrationTermination.reasonCode, persistAtomsProto$ImsRegistrationTermination.extraCode, persistAtomsProto$ImsRegistrationTermination.extraMessage, persistAtomsProto$ImsRegistrationTermination.count, persistAtomsProto$ImsRegistrationTermination.isIwlanCrossSim);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV2) {
        return TelephonyStatsLog.buildStatsEvent(10153, persistAtomsProto$NetworkRequestsV2.carrierId, persistAtomsProto$NetworkRequestsV2.capability, persistAtomsProto$NetworkRequestsV2.requestCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsRegistrationFeatureTagStats persistAtomsProto$ImsRegistrationFeatureTagStats) {
        return TelephonyStatsLog.buildStatsEvent(10133, persistAtomsProto$ImsRegistrationFeatureTagStats.carrierId, persistAtomsProto$ImsRegistrationFeatureTagStats.slotId, persistAtomsProto$ImsRegistrationFeatureTagStats.featureTagName, persistAtomsProto$ImsRegistrationFeatureTagStats.registrationTech, roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationFeatureTagStats.registeredMillis));
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$RcsClientProvisioningStats persistAtomsProto$RcsClientProvisioningStats) {
        return TelephonyStatsLog.buildStatsEvent(10134, persistAtomsProto$RcsClientProvisioningStats.carrierId, persistAtomsProto$RcsClientProvisioningStats.slotId, persistAtomsProto$RcsClientProvisioningStats.event, persistAtomsProto$RcsClientProvisioningStats.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStats) {
        return TelephonyStatsLog.buildStatsEvent(10135, persistAtomsProto$RcsAcsProvisioningStats.carrierId, persistAtomsProto$RcsAcsProvisioningStats.slotId, persistAtomsProto$RcsAcsProvisioningStats.responseCode, persistAtomsProto$RcsAcsProvisioningStats.responseType, persistAtomsProto$RcsAcsProvisioningStats.isSingleRegistrationEnabled, persistAtomsProto$RcsAcsProvisioningStats.count, roundAndConvertMillisToSeconds(persistAtomsProto$RcsAcsProvisioningStats.stateTimerMillis));
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SipDelegateStats persistAtomsProto$SipDelegateStats) {
        return TelephonyStatsLog.buildStatsEvent(10136, persistAtomsProto$SipDelegateStats.dimension, persistAtomsProto$SipDelegateStats.carrierId, persistAtomsProto$SipDelegateStats.slotId, roundAndConvertMillisToSeconds(persistAtomsProto$SipDelegateStats.uptimeMillis), persistAtomsProto$SipDelegateStats.destroyReason);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStats) {
        return TelephonyStatsLog.buildStatsEvent(10137, persistAtomsProto$SipTransportFeatureTagStats.carrierId, persistAtomsProto$SipTransportFeatureTagStats.slotId, persistAtomsProto$SipTransportFeatureTagStats.featureTagName, persistAtomsProto$SipTransportFeatureTagStats.sipTransportDeniedReason, persistAtomsProto$SipTransportFeatureTagStats.sipTransportDeregisteredReason, roundAndConvertMillisToSeconds(persistAtomsProto$SipTransportFeatureTagStats.associatedMillis));
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SipMessageResponse persistAtomsProto$SipMessageResponse) {
        return TelephonyStatsLog.buildStatsEvent(10138, persistAtomsProto$SipMessageResponse.carrierId, persistAtomsProto$SipMessageResponse.slotId, persistAtomsProto$SipMessageResponse.sipMessageMethod, persistAtomsProto$SipMessageResponse.sipMessageResponse, persistAtomsProto$SipMessageResponse.sipMessageDirection, persistAtomsProto$SipMessageResponse.messageError, persistAtomsProto$SipMessageResponse.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SipTransportSession persistAtomsProto$SipTransportSession) {
        return TelephonyStatsLog.buildStatsEvent(10139, persistAtomsProto$SipTransportSession.carrierId, persistAtomsProto$SipTransportSession.slotId, persistAtomsProto$SipTransportSession.sessionMethod, persistAtomsProto$SipTransportSession.sipMessageDirection, persistAtomsProto$SipTransportSession.sipResponse, persistAtomsProto$SipTransportSession.sessionCount, persistAtomsProto$SipTransportSession.endedGracefullyCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsDedicatedBearerListenerEvent persistAtomsProto$ImsDedicatedBearerListenerEvent) {
        return TelephonyStatsLog.buildStatsEvent(10140, persistAtomsProto$ImsDedicatedBearerListenerEvent.carrierId, persistAtomsProto$ImsDedicatedBearerListenerEvent.slotId, persistAtomsProto$ImsDedicatedBearerListenerEvent.ratAtEnd, persistAtomsProto$ImsDedicatedBearerListenerEvent.qci, persistAtomsProto$ImsDedicatedBearerListenerEvent.dedicatedBearerEstablished, persistAtomsProto$ImsDedicatedBearerListenerEvent.eventCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsDedicatedBearerEvent persistAtomsProto$ImsDedicatedBearerEvent) {
        return TelephonyStatsLog.buildStatsEvent(10141, persistAtomsProto$ImsDedicatedBearerEvent.carrierId, persistAtomsProto$ImsDedicatedBearerEvent.slotId, persistAtomsProto$ImsDedicatedBearerEvent.ratAtEnd, persistAtomsProto$ImsDedicatedBearerEvent.qci, persistAtomsProto$ImsDedicatedBearerEvent.bearerState, persistAtomsProto$ImsDedicatedBearerEvent.localConnectionInfoReceived, persistAtomsProto$ImsDedicatedBearerEvent.remoteConnectionInfoReceived, persistAtomsProto$ImsDedicatedBearerEvent.hasListeners, persistAtomsProto$ImsDedicatedBearerEvent.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStats) {
        return TelephonyStatsLog.buildStatsEvent(10142, persistAtomsProto$ImsRegistrationServiceDescStats.carrierId, persistAtomsProto$ImsRegistrationServiceDescStats.slotId, persistAtomsProto$ImsRegistrationServiceDescStats.serviceIdName, persistAtomsProto$ImsRegistrationServiceDescStats.serviceIdVersion, persistAtomsProto$ImsRegistrationServiceDescStats.registrationTech, roundAndConvertMillisToSeconds(persistAtomsProto$ImsRegistrationServiceDescStats.publishedMillis));
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$UceEventStats persistAtomsProto$UceEventStats) {
        return TelephonyStatsLog.buildStatsEvent(10143, persistAtomsProto$UceEventStats.carrierId, persistAtomsProto$UceEventStats.slotId, persistAtomsProto$UceEventStats.type, persistAtomsProto$UceEventStats.successful, persistAtomsProto$UceEventStats.commandCode, persistAtomsProto$UceEventStats.networkResponse, persistAtomsProto$UceEventStats.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$PresenceNotifyEvent persistAtomsProto$PresenceNotifyEvent) {
        return TelephonyStatsLog.buildStatsEvent(10144, persistAtomsProto$PresenceNotifyEvent.carrierId, persistAtomsProto$PresenceNotifyEvent.slotId, persistAtomsProto$PresenceNotifyEvent.reason, persistAtomsProto$PresenceNotifyEvent.contentBodyReceived, persistAtomsProto$PresenceNotifyEvent.rcsCapsCount, persistAtomsProto$PresenceNotifyEvent.mmtelCapsCount, persistAtomsProto$PresenceNotifyEvent.noCapsCount, persistAtomsProto$PresenceNotifyEvent.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$GbaEvent persistAtomsProto$GbaEvent) {
        return TelephonyStatsLog.buildStatsEvent(10145, persistAtomsProto$GbaEvent.carrierId, persistAtomsProto$GbaEvent.slotId, persistAtomsProto$GbaEvent.successful, persistAtomsProto$GbaEvent.failedReason, persistAtomsProto$GbaEvent.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$OutgoingShortCodeSms persistAtomsProto$OutgoingShortCodeSms) {
        return TelephonyStatsLog.buildStatsEvent(10162, persistAtomsProto$OutgoingShortCodeSms.category, persistAtomsProto$OutgoingShortCodeSms.xmlVersion, persistAtomsProto$OutgoingShortCodeSms.shortCodeSmsCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$EmergencyNumbersInfo persistAtomsProto$EmergencyNumbersInfo) {
        return TelephonyStatsLog.buildStatsEvent(10180, persistAtomsProto$EmergencyNumbersInfo.isDbVersionIgnored, persistAtomsProto$EmergencyNumbersInfo.assetVersion, persistAtomsProto$EmergencyNumbersInfo.otaVersion, persistAtomsProto$EmergencyNumbersInfo.number, persistAtomsProto$EmergencyNumbersInfo.countryIso, persistAtomsProto$EmergencyNumbersInfo.mnc, persistAtomsProto$EmergencyNumbersInfo.route, persistAtomsProto$EmergencyNumbersInfo.urns, persistAtomsProto$EmergencyNumbersInfo.serviceCategories, persistAtomsProto$EmergencyNumbersInfo.sources);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteController) {
        return TelephonyStatsLog.buildStatsEvent(10182, persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsSuccess, persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsFail, persistAtomsProto$SatelliteController.countOfOutgoingDatagramSuccess, persistAtomsProto$SatelliteController.countOfOutgoingDatagramFail, persistAtomsProto$SatelliteController.countOfIncomingDatagramSuccess, persistAtomsProto$SatelliteController.countOfIncomingDatagramFail, persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsSuccess, persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsFail, persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingSuccess, persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingFail, persistAtomsProto$SatelliteController.countOfProvisionSuccess, persistAtomsProto$SatelliteController.countOfProvisionFail, persistAtomsProto$SatelliteController.countOfDeprovisionSuccess, persistAtomsProto$SatelliteController.countOfDeprovisionFail, persistAtomsProto$SatelliteController.totalServiceUptimeSec, persistAtomsProto$SatelliteController.totalBatteryConsumptionPercent, persistAtomsProto$SatelliteController.totalBatteryChargedTimeSec, persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsSuccess, persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsFail, persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramSuccess, persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramFail, persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramSuccess, persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramFail, persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveSuccess, persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveFail, persistAtomsProto$SatelliteController.countOfAllowedSatelliteAccess, persistAtomsProto$SatelliteController.countOfDisallowedSatelliteAccess, persistAtomsProto$SatelliteController.countOfSatelliteAccessCheckFail, persistAtomsProto$SatelliteController.isProvisioned, persistAtomsProto$SatelliteController.carrierId, persistAtomsProto$SatelliteController.countOfSatelliteAllowedStateChangedEvents, persistAtomsProto$SatelliteController.countOfSuccessfulLocationQueries, persistAtomsProto$SatelliteController.countOfFailedLocationQueries, persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationShown, persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationRemoved, persistAtomsProto$SatelliteController.isNtnOnlyCarrier, persistAtomsProto$SatelliteController.versionOfSatelliteAccessConfig, persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsSuccess, persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsFail, persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsSuccess, persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsFail, persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsSuccess, persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsFail, persistAtomsProto$SatelliteController.carrierRoamingSatelliteConfigVersion, persistAtomsProto$SatelliteController.maxAllowedDataMode, persistAtomsProto$SatelliteController.supportedConnectionMode);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSession) {
        return TelephonyStatsLog.buildStatsEvent(10183, persistAtomsProto$SatelliteSession.satelliteServiceInitializationResult, persistAtomsProto$SatelliteSession.satelliteTechnology, persistAtomsProto$SatelliteSession.count, persistAtomsProto$SatelliteSession.satelliteServiceTerminationResult, persistAtomsProto$SatelliteSession.initializationProcessingTimeMillis, persistAtomsProto$SatelliteSession.terminationProcessingTimeMillis, persistAtomsProto$SatelliteSession.sessionDurationSeconds, persistAtomsProto$SatelliteSession.countOfOutgoingDatagramSuccess, persistAtomsProto$SatelliteSession.countOfOutgoingDatagramFailed, persistAtomsProto$SatelliteSession.countOfIncomingDatagramSuccess, persistAtomsProto$SatelliteSession.countOfIncomingDatagramFailed, persistAtomsProto$SatelliteSession.isDemoMode, persistAtomsProto$SatelliteSession.maxNtnSignalStrengthLevel, persistAtomsProto$SatelliteSession.carrierId, persistAtomsProto$SatelliteSession.countOfSatelliteNotificationDisplayed, persistAtomsProto$SatelliteSession.countOfAutoExitDueToScreenOff, persistAtomsProto$SatelliteSession.countOfAutoExitDueToTnNetwork, persistAtomsProto$SatelliteSession.isEmergency, persistAtomsProto$SatelliteSession.isNtnOnlyCarrier, persistAtomsProto$SatelliteSession.maxInactivityDurationSec, persistAtomsProto$SatelliteSession.supportedConnectionMode, persistAtomsProto$SatelliteSession.sessionConnectionMode, persistAtomsProto$SatelliteSession.plmn, persistAtomsProto$SatelliteSession.screenOnTimeSec, persistAtomsProto$SatelliteSession.batteryLevelDropPercent, persistAtomsProto$SatelliteSession.wasChargingDuringSession, persistAtomsProto$SatelliteSession.batteryDesignCapacityMah, persistAtomsProto$SatelliteSession.energyConsumedNwh);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteIncomingDatagram persistAtomsProto$SatelliteIncomingDatagram) {
        return TelephonyStatsLog.buildStatsEvent(10184, persistAtomsProto$SatelliteIncomingDatagram.resultCode, persistAtomsProto$SatelliteIncomingDatagram.datagramSizeBytes, persistAtomsProto$SatelliteIncomingDatagram.datagramTransferTimeMillis, persistAtomsProto$SatelliteIncomingDatagram.isDemoMode, persistAtomsProto$SatelliteIncomingDatagram.carrierId, persistAtomsProto$SatelliteIncomingDatagram.isNtnOnlyCarrier, persistAtomsProto$SatelliteIncomingDatagram.supportedConnectionMode, persistAtomsProto$SatelliteIncomingDatagram.sessionConnectionMode, persistAtomsProto$SatelliteIncomingDatagram.plmn);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteOutgoingDatagram persistAtomsProto$SatelliteOutgoingDatagram) {
        return TelephonyStatsLog.buildStatsEvent(10185, persistAtomsProto$SatelliteOutgoingDatagram.datagramType, persistAtomsProto$SatelliteOutgoingDatagram.resultCode, persistAtomsProto$SatelliteOutgoingDatagram.datagramSizeBytes, persistAtomsProto$SatelliteOutgoingDatagram.datagramTransferTimeMillis, persistAtomsProto$SatelliteOutgoingDatagram.isDemoMode, persistAtomsProto$SatelliteOutgoingDatagram.carrierId, persistAtomsProto$SatelliteOutgoingDatagram.isNtnOnlyCarrier, persistAtomsProto$SatelliteOutgoingDatagram.supportedConnectionMode, persistAtomsProto$SatelliteOutgoingDatagram.sessionConnectionMode, persistAtomsProto$SatelliteOutgoingDatagram.plmn);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteProvision persistAtomsProto$SatelliteProvision) {
        return TelephonyStatsLog.buildStatsEvent(10186, persistAtomsProto$SatelliteProvision.resultCode, persistAtomsProto$SatelliteProvision.provisioningTimeSec, persistAtomsProto$SatelliteProvision.isProvisionRequest, persistAtomsProto$SatelliteProvision.isCanceled, persistAtomsProto$SatelliteProvision.carrierId, persistAtomsProto$SatelliteProvision.isNtnOnlyCarrier, persistAtomsProto$SatelliteProvision.supportedConnectionMode);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommender) {
        return TelephonyStatsLog.buildStatsEvent(10187, persistAtomsProto$SatelliteSosMessageRecommender.isDisplaySosMessageSent, persistAtomsProto$SatelliteSosMessageRecommender.countOfTimerStarted, persistAtomsProto$SatelliteSosMessageRecommender.isImsRegistered, persistAtomsProto$SatelliteSosMessageRecommender.cellularServiceState, persistAtomsProto$SatelliteSosMessageRecommender.count, persistAtomsProto$SatelliteSosMessageRecommender.isMultiSim, persistAtomsProto$SatelliteSosMessageRecommender.recommendingHandoverType, persistAtomsProto$SatelliteSosMessageRecommender.isSatelliteAllowedInCurrentLocation, persistAtomsProto$SatelliteSosMessageRecommender.isWifiConnected, persistAtomsProto$SatelliteSosMessageRecommender.carrierId, persistAtomsProto$SatelliteSosMessageRecommender.isNtnOnlyCarrier, persistAtomsProto$SatelliteSosMessageRecommender.supportedConnectionMode, persistAtomsProto$SatelliteSosMessageRecommender.sessionConnectionMode, persistAtomsProto$SatelliteSosMessageRecommender.plmn, persistAtomsProto$SatelliteSosMessageRecommender.isInCarrierRoamingNtnMode, persistAtomsProto$SatelliteSosMessageRecommender.carrierRoamingSatelliteEmergencyMessagingProvider, persistAtomsProto$SatelliteSosMessageRecommender.emergencyNumberSourceUsedInHandoverIntent);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation) {
        return TelephonyStatsLog.buildStatsEvent(10207, persistAtomsProto$DataNetworkValidation.networkType, persistAtomsProto$DataNetworkValidation.apnTypeBitmask, persistAtomsProto$DataNetworkValidation.signalStrength, persistAtomsProto$DataNetworkValidation.validationResult, persistAtomsProto$DataNetworkValidation.elapsedTimeInMillis, persistAtomsProto$DataNetworkValidation.handoverAttempted, persistAtomsProto$DataNetworkValidation.networkValidationCount);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$CarrierRoamingSatelliteSession persistAtomsProto$CarrierRoamingSatelliteSession) {
        return TelephonyStatsLog.buildStatsEvent(10211, persistAtomsProto$CarrierRoamingSatelliteSession.carrierId, persistAtomsProto$CarrierRoamingSatelliteSession.isNtnRoamingInHomeCountry, persistAtomsProto$CarrierRoamingSatelliteSession.totalSatelliteModeTimeSec, persistAtomsProto$CarrierRoamingSatelliteSession.numberOfSatelliteConnections, persistAtomsProto$CarrierRoamingSatelliteSession.avgDurationOfSatelliteConnectionSec, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapMinSec, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapAvgSec, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapMaxSec, persistAtomsProto$CarrierRoamingSatelliteSession.rsrpAvg, persistAtomsProto$CarrierRoamingSatelliteSession.rsrpMedian, persistAtomsProto$CarrierRoamingSatelliteSession.rssnrAvg, persistAtomsProto$CarrierRoamingSatelliteSession.rssnrMedian, persistAtomsProto$CarrierRoamingSatelliteSession.countOfIncomingSms, persistAtomsProto$CarrierRoamingSatelliteSession.countOfOutgoingSms, persistAtomsProto$CarrierRoamingSatelliteSession.countOfIncomingMms, persistAtomsProto$CarrierRoamingSatelliteSession.countOfOutgoingMms, persistAtomsProto$CarrierRoamingSatelliteSession.supportedSatelliteServices, persistAtomsProto$CarrierRoamingSatelliteSession.serviceDataPolicy, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteDataConsumedBytes, persistAtomsProto$CarrierRoamingSatelliteSession.isMultiSim, persistAtomsProto$CarrierRoamingSatelliteSession.isNbIotNtn, persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataConnections, persistAtomsProto$CarrierRoamingSatelliteSession.lastFailCauses, persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataDisconnections, persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataStalls, persistAtomsProto$CarrierRoamingSatelliteSession.averageUplinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.averageDownlinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.minUplinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.maxUplinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.minDownlinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.maxDownlinkBandwidthKbps, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteSupportedApps, persistAtomsProto$CarrierRoamingSatelliteSession.satelliteSupportedUids, persistAtomsProto$CarrierRoamingSatelliteSession.perAppSatelliteDataConsumedBytes, persistAtomsProto$CarrierRoamingSatelliteSession.supportedConnectionMode, persistAtomsProto$CarrierRoamingSatelliteSession.sessionConnectionMode, persistAtomsProto$CarrierRoamingSatelliteSession.plmn, persistAtomsProto$CarrierRoamingSatelliteSession.isWifiEnabled, persistAtomsProto$CarrierRoamingSatelliteSession.isWfcEnabled, persistAtomsProto$CarrierRoamingSatelliteSession.isWfcRegistered, persistAtomsProto$CarrierRoamingSatelliteSession.screenOnTimeSec, persistAtomsProto$CarrierRoamingSatelliteSession.batteryLevelDropPercent, persistAtomsProto$CarrierRoamingSatelliteSession.wasChargingDuringSession, persistAtomsProto$CarrierRoamingSatelliteSession.batteryDesignCapacityMah, persistAtomsProto$CarrierRoamingSatelliteSession.energyConsumedNwh, persistAtomsProto$CarrierRoamingSatelliteSession.eligibilitySource, persistAtomsProto$CarrierRoamingSatelliteSession.isWifiConnected, persistAtomsProto$CarrierRoamingSatelliteSession.countOfNonEmergencyDialerDialogDisplayed, persistAtomsProto$CarrierRoamingSatelliteSession.countOfEmergencyDialerButtonDisplayed, persistAtomsProto$CarrierRoamingSatelliteSession.countOfSatelliteNotificationDisplayed, persistAtomsProto$CarrierRoamingSatelliteSession.totalRxDataBytes, persistAtomsProto$CarrierRoamingSatelliteSession.totalTxDataBytes, persistAtomsProto$CarrierRoamingSatelliteSession.perAppRxDataBytes, persistAtomsProto$CarrierRoamingSatelliteSession.perAppTxDataBytes);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStats) {
        return TelephonyStatsLog.buildStatsEvent(10212, persistAtomsProto$CarrierRoamingSatelliteControllerStats.configDataSource, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfEntitlementStatusQueryRequest, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteConfigUpdateRequest, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteNotificationDisplayed, persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMinSec, persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapAvgSec, persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMaxSec, persistAtomsProto$CarrierRoamingSatelliteControllerStats.carrierId, persistAtomsProto$CarrierRoamingSatelliteControllerStats.isDeviceEntitled, persistAtomsProto$CarrierRoamingSatelliteControllerStats.isMultiSim, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteSessions, persistAtomsProto$CarrierRoamingSatelliteControllerStats.isNbIotNtn, persistAtomsProto$CarrierRoamingSatelliteControllerStats.supportedConnectionMode, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeAutomatic, persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeManual, persistAtomsProto$CarrierRoamingSatelliteControllerStats.serviceDataPolicy, persistAtomsProto$CarrierRoamingSatelliteControllerStats.totalSessionDurationSec, persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteAttachSupported, persistAtomsProto$CarrierRoamingSatelliteControllerStats.eligibilitySource);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlement) {
        return TelephonyStatsLog.buildStatsEvent(10214, persistAtomsProto$SatelliteEntitlement.carrierId, persistAtomsProto$SatelliteEntitlement.result, persistAtomsProto$SatelliteEntitlement.entitlementStatus, persistAtomsProto$SatelliteEntitlement.isRetry, persistAtomsProto$SatelliteEntitlement.count, persistAtomsProto$SatelliteEntitlement.isAllowedServiceEntitlement, persistAtomsProto$SatelliteEntitlement.entitlementServiceType, persistAtomsProto$SatelliteEntitlement.entitlementDataPolicy, persistAtomsProto$SatelliteEntitlement.supportedConnectionMode, persistAtomsProto$SatelliteEntitlement.httpStatusCode, persistAtomsProto$SatelliteEntitlement.triggerEvent);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdater) {
        return TelephonyStatsLog.buildStatsEvent(10215, persistAtomsProto$SatelliteConfigUpdater.configVersion, persistAtomsProto$SatelliteConfigUpdater.oemConfigResult, persistAtomsProto$SatelliteConfigUpdater.carrierConfigResult, persistAtomsProto$SatelliteConfigUpdater.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$SatelliteAccessController persistAtomsProto$SatelliteAccessController) {
        return TelephonyStatsLog.buildStatsEvent(10219, persistAtomsProto$SatelliteAccessController.accessControlType, persistAtomsProto$SatelliteAccessController.locationQueryTimeMillis, persistAtomsProto$SatelliteAccessController.onDeviceLookupTimeMillis, persistAtomsProto$SatelliteAccessController.totalCheckingTimeMillis, persistAtomsProto$SatelliteAccessController.isAllowed, persistAtomsProto$SatelliteAccessController.isEmergency, persistAtomsProto$SatelliteAccessController.resultCode, persistAtomsProto$SatelliteAccessController.countryCodes, persistAtomsProto$SatelliteAccessController.configDataSource, persistAtomsProto$SatelliteAccessController.carrierId, persistAtomsProto$SatelliteAccessController.triggeringEvent, persistAtomsProto$SatelliteAccessController.isNtnOnlyCarrier, persistAtomsProto$SatelliteAccessController.supportedConnectionMode, persistAtomsProto$SatelliteAccessController.sessionConnectionMode);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$OtpRedactionEvent persistAtomsProto$OtpRedactionEvent) {
        return TelephonyStatsLog.buildStatsEvent(1136, persistAtomsProto$OtpRedactionEvent.uid, 1, 1, persistAtomsProto$OtpRedactionEvent.count);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$OtpEvaluationEvent persistAtomsProto$OtpEvaluationEvent) {
        return TelephonyStatsLog.buildStatsEvent(1136, persistAtomsProto$OtpEvaluationEvent.result, persistAtomsProto$OtpEvaluationEvent.redactionTimeMs, 1, 1);
    }

    private static StatsEvent buildStatsEvent(PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent) {
        return TelephonyStatsLog.buildStatsEvent(1348, persistAtomsProto$MessagingReadRestrictionEvent.callerUid, persistAtomsProto$MessagingReadRestrictionEvent.contentProvider, persistAtomsProto$MessagingReadRestrictionEvent.eventType, persistAtomsProto$MessagingReadRestrictionEvent.readRestrictedMessagesAppOpMode, persistAtomsProto$MessagingReadRestrictionEvent.count);
    }

    static Phone[] getPhonesIfAny() {
        try {
            return PhoneFactory.getPhones();
        } catch (IllegalStateException unused) {
            return new Phone[0];
        }
    }

    private static int roundAndConvertMillisToSeconds(long j) {
        return roundAndConvertMillisToSeconds(j, DURATION_BUCKET_MILLIS);
    }

    private static int roundAndConvertMillisToSeconds(long j, long j2) {
        return (int) ((Math.round(j / j2) * j2) / MILLIS_PER_SECOND);
    }

    private static int roundAndConvertMillisToMinutes(long j) {
        long j2 = DURATION_BUCKET_MILLIS;
        return (int) ((Math.round(j / j2) * j2) / MILLIS_PER_MINUTE);
    }
}
