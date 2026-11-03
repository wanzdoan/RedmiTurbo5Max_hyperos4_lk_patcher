package com.android.internal.telephony.metrics;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.SparseIntArray;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierIdMismatch;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteControllerStats;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteSession;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularDataServiceSwitch;
import com.android.internal.telephony.nano.PersistAtomsProto$CellularServiceState;
import com.android.internal.telephony.nano.PersistAtomsProto$DataCallSession;
import com.android.internal.telephony.nano.PersistAtomsProto$DataNetworkValidation;
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
import com.android.internal.telephony.nano.PersistAtomsProto$PersistAtoms;
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
import com.android.internal.telephony.nano.PersistAtomsProto$UnmeteredNetworks;
import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallRatUsage;
import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallSession;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.util.ArrayUtils;
import com.android.telephony.Rlog;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

/* JADX INFO: loaded from: classes.dex */
public class PersistAtomsStorage {
    private static final String TAG = "PersistAtomsStorage";
    private static final SecureRandom sRandom = new SecureRandom();
    protected PersistAtomsProto$PersistAtoms mAtoms;
    private final Context mContext;
    private final Handler mHandler;
    private final int mMaxNumCarrierIdMismatches;
    private final int mMaxNumCellularDataSwitches;
    private final int mMaxNumCellularServiceStates;
    private final int mMaxNumDataCallSessions;
    private final int mMaxNumDataNetworkValidation;
    private final int mMaxNumDedicatedBearerEventStats;
    private final int mMaxNumDedicatedBearerListenerEventStats;
    private final int mMaxNumGbaEventStats;
    private final int mMaxNumImsRegistrationFeatureStats;
    private final int mMaxNumImsRegistrationServiceDescStats;
    private final int mMaxNumImsRegistrationStats;
    private final int mMaxNumImsRegistrationTerminations;
    private final int mMaxNumMessagingReadRestrictionEvent;
    private final int mMaxNumOtpStats;
    private final int mMaxNumPresenceNotifyEventStats;
    private final int mMaxNumRcsAcsProvisioningStats;
    private final int mMaxNumRcsClientProvisioningStats;
    private final int mMaxNumSatelliteStats;
    private final int mMaxNumSipDelegateStats;
    private final int mMaxNumSipMessageResponseStats;
    private final int mMaxNumSipTransportFeatureTagStats;
    private final int mMaxNumSipTransportSessionStats;
    private final int mMaxNumSms;
    private final int mMaxNumUceEventStats;
    private final int mMaxNumVoiceCallSessions;
    private final int mMaxOutgoingShortCodeSms;
    protected boolean mSaveImmediately;
    private Runnable mSaveRunnable = new Runnable() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage.1
        @Override // java.lang.Runnable
        public void run() {
            PersistAtomsStorage.this.saveAtomsToFileNow();
        }
    };
    private final VoiceCallRatTracker mVoiceCallRatTracker;

    public PersistAtomsStorage(Context context) {
        this.mContext = context;
        if (context.getPackageManager().hasSystemFeature("android.hardware.ram.low")) {
            Rlog.i(TAG, "Low RAM device");
            this.mMaxNumVoiceCallSessions = 10;
            this.mMaxNumSms = 5;
            this.mMaxNumCarrierIdMismatches = 8;
            this.mMaxNumDataCallSessions = 5;
            this.mMaxNumCellularServiceStates = 10;
            this.mMaxNumCellularDataSwitches = 5;
            this.mMaxNumImsRegistrationStats = 5;
            this.mMaxNumImsRegistrationTerminations = 5;
            this.mMaxNumImsRegistrationFeatureStats = 15;
            this.mMaxNumRcsClientProvisioningStats = 5;
            this.mMaxNumRcsAcsProvisioningStats = 5;
            this.mMaxNumSipMessageResponseStats = 10;
            this.mMaxNumSipTransportSessionStats = 10;
            this.mMaxNumSipDelegateStats = 5;
            this.mMaxNumSipTransportFeatureTagStats = 15;
            this.mMaxNumDedicatedBearerListenerEventStats = 5;
            this.mMaxNumDedicatedBearerEventStats = 5;
            this.mMaxNumImsRegistrationServiceDescStats = 15;
            this.mMaxNumUceEventStats = 5;
            this.mMaxNumPresenceNotifyEventStats = 10;
            this.mMaxNumGbaEventStats = 5;
            this.mMaxOutgoingShortCodeSms = 5;
            this.mMaxNumSatelliteStats = 5;
            this.mMaxNumDataNetworkValidation = 5;
            this.mMaxNumOtpStats = 10;
            this.mMaxNumMessagingReadRestrictionEvent = 10;
        } else {
            this.mMaxNumVoiceCallSessions = 50;
            this.mMaxNumSms = 25;
            this.mMaxNumCarrierIdMismatches = 40;
            this.mMaxNumDataCallSessions = 15;
            this.mMaxNumCellularServiceStates = 50;
            this.mMaxNumCellularDataSwitches = 50;
            this.mMaxNumImsRegistrationStats = 10;
            this.mMaxNumImsRegistrationTerminations = 10;
            this.mMaxNumImsRegistrationFeatureStats = 25;
            this.mMaxNumRcsClientProvisioningStats = 10;
            this.mMaxNumRcsAcsProvisioningStats = 10;
            this.mMaxNumSipMessageResponseStats = 25;
            this.mMaxNumSipTransportSessionStats = 25;
            this.mMaxNumSipDelegateStats = 10;
            this.mMaxNumSipTransportFeatureTagStats = 25;
            this.mMaxNumDedicatedBearerListenerEventStats = 10;
            this.mMaxNumDedicatedBearerEventStats = 10;
            this.mMaxNumImsRegistrationServiceDescStats = 25;
            this.mMaxNumUceEventStats = 25;
            this.mMaxNumPresenceNotifyEventStats = 50;
            this.mMaxNumGbaEventStats = 10;
            this.mMaxOutgoingShortCodeSms = 10;
            this.mMaxNumSatelliteStats = 15;
            this.mMaxNumDataNetworkValidation = 15;
            this.mMaxNumOtpStats = 100;
            this.mMaxNumMessagingReadRestrictionEvent = 100;
        }
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtomsLoadAtomsFromFile = loadAtomsFromFile();
        this.mAtoms = persistAtomsProto$PersistAtomsLoadAtomsFromFile;
        this.mVoiceCallRatTracker = VoiceCallRatTracker.fromProto(persistAtomsProto$PersistAtomsLoadAtomsFromFile.voiceCallRatUsage);
        this.mHandler = new Handler(BackgroundThread.get().getLooper());
        this.mSaveImmediately = false;
    }

    public synchronized void addVoiceCallSession(PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.voiceCallSession = (PersistAtomsProto$VoiceCallSession[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.voiceCallSession, persistAtomsProto$VoiceCallSession, this.mMaxNumVoiceCallSessions);
        saveAtomsToFile(30000);
        Rlog.d(TAG, "Add new voice call session: " + persistAtomsProto$VoiceCallSession.toString());
    }

    public synchronized void addVoiceCallRatUsage(VoiceCallRatTracker voiceCallRatTracker) {
        this.mVoiceCallRatTracker.mergeWith(voiceCallRatTracker);
        this.mAtoms.voiceCallRatUsage = this.mVoiceCallRatTracker.toProto();
        saveAtomsToFile(30000);
    }

    public synchronized void addIncomingSms(PersistAtomsProto$IncomingSms persistAtomsProto$IncomingSms) {
        persistAtomsProto$IncomingSms.hashCode = SmsStats.getSmsHashCode(persistAtomsProto$IncomingSms);
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.incomingSms = (PersistAtomsProto$IncomingSms[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.incomingSms, persistAtomsProto$IncomingSms, this.mMaxNumSms);
        saveAtomsToFile(30000);
        Rlog.d(TAG, "Add new incoming SMS atom: " + persistAtomsProto$IncomingSms.toString());
    }

    public synchronized void addOutgoingSms(PersistAtomsProto$OutgoingSms persistAtomsProto$OutgoingSms) {
        int i;
        try {
            persistAtomsProto$OutgoingSms.hashCode = SmsStats.getSmsHashCode(persistAtomsProto$OutgoingSms);
            for (PersistAtomsProto$OutgoingSms persistAtomsProto$OutgoingSms2 : this.mAtoms.outgoingSms) {
                if (persistAtomsProto$OutgoingSms2.messageId == persistAtomsProto$OutgoingSms.messageId && (i = persistAtomsProto$OutgoingSms2.retryId) >= persistAtomsProto$OutgoingSms.retryId) {
                    persistAtomsProto$OutgoingSms.retryId = i + 1;
                }
            }
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            persistAtomsProto$PersistAtoms.outgoingSms = (PersistAtomsProto$OutgoingSms[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.outgoingSms, persistAtomsProto$OutgoingSms, this.mMaxNumSms);
            saveAtomsToFile(30000);
            Rlog.d(TAG, "Add new outgoing SMS atom: " + persistAtomsProto$OutgoingSms.toString());
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addCellularServiceStateAndCellularDataServiceSwitch(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState, PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch) {
        try {
            PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceStateFind = find(persistAtomsProto$CellularServiceState);
            if (persistAtomsProto$CellularServiceStateFind != null) {
                persistAtomsProto$CellularServiceStateFind.totalTimeMillis += persistAtomsProto$CellularServiceState.totalTimeMillis;
                persistAtomsProto$CellularServiceStateFind.lastUsedMillis = getWallTimeMillis();
            } else {
                persistAtomsProto$CellularServiceState.lastUsedMillis = getWallTimeMillis();
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.cellularServiceState = (PersistAtomsProto$CellularServiceState[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.cellularServiceState, persistAtomsProto$CellularServiceState, this.mMaxNumCellularServiceStates);
            }
            if (persistAtomsProto$CellularDataServiceSwitch != null) {
                PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitchFind = find(persistAtomsProto$CellularDataServiceSwitch);
                if (persistAtomsProto$CellularDataServiceSwitchFind != null) {
                    persistAtomsProto$CellularDataServiceSwitchFind.switchCount += persistAtomsProto$CellularDataServiceSwitch.switchCount;
                    persistAtomsProto$CellularDataServiceSwitchFind.lastUsedMillis = getWallTimeMillis();
                } else {
                    persistAtomsProto$CellularDataServiceSwitch.lastUsedMillis = getWallTimeMillis();
                    PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
                    persistAtomsProto$PersistAtoms2.cellularDataServiceSwitch = (PersistAtomsProto$CellularDataServiceSwitch[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms2.cellularDataServiceSwitch, persistAtomsProto$CellularDataServiceSwitch, this.mMaxNumCellularDataSwitches);
                }
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addDataCallSession(PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession) {
        try {
            int iFindIndex = findIndex(persistAtomsProto$DataCallSession);
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            if (iFindIndex >= 0) {
                PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession2 = persistAtomsProto$PersistAtoms.dataCallSession[iFindIndex];
                persistAtomsProto$DataCallSession.ratSwitchCount += persistAtomsProto$DataCallSession2.ratSwitchCount;
                persistAtomsProto$DataCallSession.durationMinutes += persistAtomsProto$DataCallSession2.durationMinutes;
                persistAtomsProto$DataCallSession.handoverFailureCauses = IntStream.concat(Arrays.stream(persistAtomsProto$DataCallSession.handoverFailureCauses), Arrays.stream(persistAtomsProto$DataCallSession2.handoverFailureCauses)).limit(15L).toArray();
                persistAtomsProto$DataCallSession.handoverFailureRat = IntStream.concat(Arrays.stream(persistAtomsProto$DataCallSession.handoverFailureRat), Arrays.stream(persistAtomsProto$DataCallSession2.handoverFailureRat)).limit(15L).toArray();
                this.mAtoms.dataCallSession[iFindIndex] = persistAtomsProto$DataCallSession;
            } else {
                persistAtomsProto$PersistAtoms.dataCallSession = (PersistAtomsProto$DataCallSession[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.dataCallSession, persistAtomsProto$DataCallSession, this.mMaxNumDataCallSessions);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean addCarrierIdMismatch(PersistAtomsProto$CarrierIdMismatch persistAtomsProto$CarrierIdMismatch) {
        try {
            if (find(persistAtomsProto$CarrierIdMismatch) != null) {
                return false;
            }
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            PersistAtomsProto$CarrierIdMismatch[] persistAtomsProto$CarrierIdMismatchArr = persistAtomsProto$PersistAtoms.carrierIdMismatch;
            int length = persistAtomsProto$CarrierIdMismatchArr.length;
            int i = this.mMaxNumCarrierIdMismatches;
            if (length == i) {
                System.arraycopy(persistAtomsProto$CarrierIdMismatchArr, 1, persistAtomsProto$CarrierIdMismatchArr, 0, i - 1);
                this.mAtoms.carrierIdMismatch[this.mMaxNumCarrierIdMismatches - 1] = persistAtomsProto$CarrierIdMismatch;
            } else {
                persistAtomsProto$PersistAtoms.carrierIdMismatch = (PersistAtomsProto$CarrierIdMismatch[]) ArrayUtils.appendElement(PersistAtomsProto$CarrierIdMismatch.class, persistAtomsProto$CarrierIdMismatchArr, persistAtomsProto$CarrierIdMismatch, true);
            }
            saveAtomsToFile(30000);
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsRegistrationStats(PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats) {
        try {
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStatsFind = find(persistAtomsProto$ImsRegistrationStats);
            if (persistAtomsProto$ImsRegistrationStatsFind != null) {
                persistAtomsProto$ImsRegistrationStatsFind.registeredMillis += persistAtomsProto$ImsRegistrationStats.registeredMillis;
                persistAtomsProto$ImsRegistrationStatsFind.voiceCapableMillis += persistAtomsProto$ImsRegistrationStats.voiceCapableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.voiceAvailableMillis += persistAtomsProto$ImsRegistrationStats.voiceAvailableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.smsCapableMillis += persistAtomsProto$ImsRegistrationStats.smsCapableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.smsAvailableMillis += persistAtomsProto$ImsRegistrationStats.smsAvailableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.videoCapableMillis += persistAtomsProto$ImsRegistrationStats.videoCapableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.videoAvailableMillis += persistAtomsProto$ImsRegistrationStats.videoAvailableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.utCapableMillis += persistAtomsProto$ImsRegistrationStats.utCapableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.utAvailableMillis += persistAtomsProto$ImsRegistrationStats.utAvailableMillis;
                persistAtomsProto$ImsRegistrationStatsFind.registeringMillis += persistAtomsProto$ImsRegistrationStats.registeringMillis;
                persistAtomsProto$ImsRegistrationStatsFind.unregisteredMillis += persistAtomsProto$ImsRegistrationStats.unregisteredMillis;
                persistAtomsProto$ImsRegistrationStatsFind.registeredTimes += persistAtomsProto$ImsRegistrationStats.registeredTimes;
                persistAtomsProto$ImsRegistrationStatsFind.lastUsedMillis = getWallTimeMillis();
            } else {
                persistAtomsProto$ImsRegistrationStats.lastUsedMillis = getWallTimeMillis();
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsRegistrationStats = (PersistAtomsProto$ImsRegistrationStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsRegistrationStats, persistAtomsProto$ImsRegistrationStats, this.mMaxNumImsRegistrationStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsRegistrationTermination(PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTermination) {
        try {
            PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTerminationFind = find(persistAtomsProto$ImsRegistrationTermination);
            if (persistAtomsProto$ImsRegistrationTerminationFind != null) {
                persistAtomsProto$ImsRegistrationTerminationFind.count += persistAtomsProto$ImsRegistrationTermination.count;
                persistAtomsProto$ImsRegistrationTerminationFind.lastUsedMillis = getWallTimeMillis();
            } else {
                persistAtomsProto$ImsRegistrationTermination.lastUsedMillis = getWallTimeMillis();
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsRegistrationTermination = (PersistAtomsProto$ImsRegistrationTermination[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsRegistrationTermination, persistAtomsProto$ImsRegistrationTermination, this.mMaxNumImsRegistrationTerminations);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean setCarrierIdTableVersion(int i) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (persistAtomsProto$PersistAtoms.carrierIdTableVersion >= i) {
            return false;
        }
        persistAtomsProto$PersistAtoms.carrierIdTableVersion = i;
        saveAtomsToFile(30000);
        return true;
    }

    public synchronized void recordToggledAutoDataSwitch() {
        this.mAtoms.autoDataSwitchToggleCount++;
        saveAtomsToFile(30000);
    }

    public synchronized void addNetworkRequestsV2(PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV2) {
        try {
            PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV2Find = find(persistAtomsProto$NetworkRequestsV2);
            if (persistAtomsProto$NetworkRequestsV2Find != null) {
                persistAtomsProto$NetworkRequestsV2Find.requestCount += persistAtomsProto$NetworkRequestsV2.requestCount;
            } else {
                PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV3 = new PersistAtomsProto$NetworkRequestsV2();
                persistAtomsProto$NetworkRequestsV3.capability = persistAtomsProto$NetworkRequestsV2.capability;
                persistAtomsProto$NetworkRequestsV3.carrierId = persistAtomsProto$NetworkRequestsV2.carrierId;
                persistAtomsProto$NetworkRequestsV3.requestCount = persistAtomsProto$NetworkRequestsV2.requestCount;
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.networkRequestsV2 = (PersistAtomsProto$NetworkRequestsV2[]) ArrayUtils.appendElement(PersistAtomsProto$NetworkRequestsV2.class, persistAtomsProto$PersistAtoms.networkRequestsV2, persistAtomsProto$NetworkRequestsV3, true);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsRegistrationFeatureTagStats(PersistAtomsProto$ImsRegistrationFeatureTagStats persistAtomsProto$ImsRegistrationFeatureTagStats) {
        try {
            PersistAtomsProto$ImsRegistrationFeatureTagStats persistAtomsProto$ImsRegistrationFeatureTagStatsFind = find(persistAtomsProto$ImsRegistrationFeatureTagStats);
            if (persistAtomsProto$ImsRegistrationFeatureTagStatsFind != null) {
                persistAtomsProto$ImsRegistrationFeatureTagStatsFind.registeredMillis += persistAtomsProto$ImsRegistrationFeatureTagStats.registeredMillis;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsRegistrationFeatureTagStats = (PersistAtomsProto$ImsRegistrationFeatureTagStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsRegistrationFeatureTagStats, persistAtomsProto$ImsRegistrationFeatureTagStats, this.mMaxNumImsRegistrationFeatureStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addRcsClientProvisioningStats(PersistAtomsProto$RcsClientProvisioningStats persistAtomsProto$RcsClientProvisioningStats) {
        try {
            PersistAtomsProto$RcsClientProvisioningStats persistAtomsProto$RcsClientProvisioningStatsFind = find(persistAtomsProto$RcsClientProvisioningStats);
            if (persistAtomsProto$RcsClientProvisioningStatsFind != null) {
                persistAtomsProto$RcsClientProvisioningStatsFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.rcsClientProvisioningStats = (PersistAtomsProto$RcsClientProvisioningStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.rcsClientProvisioningStats, persistAtomsProto$RcsClientProvisioningStats, this.mMaxNumRcsClientProvisioningStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addRcsAcsProvisioningStats(PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStats) {
        try {
            PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStatsFind = find(persistAtomsProto$RcsAcsProvisioningStats);
            if (persistAtomsProto$RcsAcsProvisioningStatsFind != null) {
                persistAtomsProto$RcsAcsProvisioningStatsFind.count++;
                persistAtomsProto$RcsAcsProvisioningStatsFind.stateTimerMillis += persistAtomsProto$RcsAcsProvisioningStats.stateTimerMillis;
            } else {
                persistAtomsProto$RcsAcsProvisioningStats.count = 1;
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.rcsAcsProvisioningStats = (PersistAtomsProto$RcsAcsProvisioningStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.rcsAcsProvisioningStats, persistAtomsProto$RcsAcsProvisioningStats, this.mMaxNumRcsAcsProvisioningStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSipDelegateStats(PersistAtomsProto$SipDelegateStats persistAtomsProto$SipDelegateStats) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.sipDelegateStats = (PersistAtomsProto$SipDelegateStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.sipDelegateStats, persistAtomsProto$SipDelegateStats, this.mMaxNumSipDelegateStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addSipTransportFeatureTagStats(PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStats) {
        try {
            PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStatsFind = find(persistAtomsProto$SipTransportFeatureTagStats);
            if (persistAtomsProto$SipTransportFeatureTagStatsFind != null) {
                persistAtomsProto$SipTransportFeatureTagStatsFind.associatedMillis += persistAtomsProto$SipTransportFeatureTagStats.associatedMillis;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.sipTransportFeatureTagStats = (PersistAtomsProto$SipTransportFeatureTagStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.sipTransportFeatureTagStats, persistAtomsProto$SipTransportFeatureTagStats, this.mMaxNumSipTransportFeatureTagStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSipMessageResponse(PersistAtomsProto$SipMessageResponse persistAtomsProto$SipMessageResponse) {
        try {
            PersistAtomsProto$SipMessageResponse persistAtomsProto$SipMessageResponseFind = find(persistAtomsProto$SipMessageResponse);
            if (persistAtomsProto$SipMessageResponseFind != null) {
                persistAtomsProto$SipMessageResponseFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.sipMessageResponse = (PersistAtomsProto$SipMessageResponse[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.sipMessageResponse, persistAtomsProto$SipMessageResponse, this.mMaxNumSipMessageResponseStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addCompleteSipTransportSession(PersistAtomsProto$SipTransportSession persistAtomsProto$SipTransportSession) {
        try {
            PersistAtomsProto$SipTransportSession persistAtomsProto$SipTransportSessionFind = find(persistAtomsProto$SipTransportSession);
            if (persistAtomsProto$SipTransportSessionFind != null) {
                persistAtomsProto$SipTransportSessionFind.sessionCount++;
                if (persistAtomsProto$SipTransportSession.isEndedGracefully) {
                    persistAtomsProto$SipTransportSessionFind.endedGracefullyCount++;
                }
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.sipTransportSession = (PersistAtomsProto$SipTransportSession[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.sipTransportSession, persistAtomsProto$SipTransportSession, this.mMaxNumSipTransportSessionStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsDedicatedBearerListenerEvent(PersistAtomsProto$ImsDedicatedBearerListenerEvent persistAtomsProto$ImsDedicatedBearerListenerEvent) {
        try {
            PersistAtomsProto$ImsDedicatedBearerListenerEvent persistAtomsProto$ImsDedicatedBearerListenerEventFind = find(persistAtomsProto$ImsDedicatedBearerListenerEvent);
            if (persistAtomsProto$ImsDedicatedBearerListenerEventFind != null) {
                persistAtomsProto$ImsDedicatedBearerListenerEventFind.eventCount++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsDedicatedBearerListenerEvent = (PersistAtomsProto$ImsDedicatedBearerListenerEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsDedicatedBearerListenerEvent, persistAtomsProto$ImsDedicatedBearerListenerEvent, this.mMaxNumDedicatedBearerListenerEventStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsDedicatedBearerEvent(PersistAtomsProto$ImsDedicatedBearerEvent persistAtomsProto$ImsDedicatedBearerEvent) {
        try {
            PersistAtomsProto$ImsDedicatedBearerEvent persistAtomsProto$ImsDedicatedBearerEventFind = find(persistAtomsProto$ImsDedicatedBearerEvent);
            if (persistAtomsProto$ImsDedicatedBearerEventFind != null) {
                persistAtomsProto$ImsDedicatedBearerEventFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsDedicatedBearerEvent = (PersistAtomsProto$ImsDedicatedBearerEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsDedicatedBearerEvent, persistAtomsProto$ImsDedicatedBearerEvent, this.mMaxNumDedicatedBearerEventStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addImsRegistrationServiceDescStats(PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStats) {
        try {
            PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStatsFind = find(persistAtomsProto$ImsRegistrationServiceDescStats);
            if (persistAtomsProto$ImsRegistrationServiceDescStatsFind != null) {
                persistAtomsProto$ImsRegistrationServiceDescStatsFind.publishedMillis += persistAtomsProto$ImsRegistrationServiceDescStats.publishedMillis;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.imsRegistrationServiceDescStats = (PersistAtomsProto$ImsRegistrationServiceDescStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.imsRegistrationServiceDescStats, persistAtomsProto$ImsRegistrationServiceDescStats, this.mMaxNumImsRegistrationServiceDescStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addUceEventStats(PersistAtomsProto$UceEventStats persistAtomsProto$UceEventStats) {
        try {
            PersistAtomsProto$UceEventStats persistAtomsProto$UceEventStatsFind = find(persistAtomsProto$UceEventStats);
            if (persistAtomsProto$UceEventStatsFind != null) {
                persistAtomsProto$UceEventStatsFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.uceEventStats = (PersistAtomsProto$UceEventStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.uceEventStats, persistAtomsProto$UceEventStats, this.mMaxNumUceEventStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addPresenceNotifyEvent(PersistAtomsProto$PresenceNotifyEvent persistAtomsProto$PresenceNotifyEvent) {
        try {
            PersistAtomsProto$PresenceNotifyEvent persistAtomsProto$PresenceNotifyEventFind = find(persistAtomsProto$PresenceNotifyEvent);
            if (persistAtomsProto$PresenceNotifyEventFind != null) {
                persistAtomsProto$PresenceNotifyEventFind.rcsCapsCount += persistAtomsProto$PresenceNotifyEvent.rcsCapsCount;
                persistAtomsProto$PresenceNotifyEventFind.mmtelCapsCount += persistAtomsProto$PresenceNotifyEvent.mmtelCapsCount;
                persistAtomsProto$PresenceNotifyEventFind.noCapsCount += persistAtomsProto$PresenceNotifyEvent.noCapsCount;
                persistAtomsProto$PresenceNotifyEventFind.count += persistAtomsProto$PresenceNotifyEvent.count;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.presenceNotifyEvent = (PersistAtomsProto$PresenceNotifyEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.presenceNotifyEvent, persistAtomsProto$PresenceNotifyEvent, this.mMaxNumPresenceNotifyEventStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addGbaEvent(PersistAtomsProto$GbaEvent persistAtomsProto$GbaEvent) {
        try {
            PersistAtomsProto$GbaEvent persistAtomsProto$GbaEventFind = find(persistAtomsProto$GbaEvent);
            if (persistAtomsProto$GbaEventFind != null) {
                persistAtomsProto$GbaEventFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.gbaEvent = (PersistAtomsProto$GbaEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.gbaEvent, persistAtomsProto$GbaEvent, this.mMaxNumGbaEventStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addUnmeteredNetworks(int i, int i2, long j) {
        try {
            PersistAtomsProto$UnmeteredNetworks persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks = findUnmeteredNetworks(i);
            if (persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks == null) {
                PersistAtomsProto$UnmeteredNetworks persistAtomsProto$UnmeteredNetworks = new PersistAtomsProto$UnmeteredNetworks();
                persistAtomsProto$UnmeteredNetworks.phoneId = i;
                persistAtomsProto$UnmeteredNetworks.carrierId = i2;
                persistAtomsProto$UnmeteredNetworks.unmeteredNetworksBitmask = j;
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.unmeteredNetworks = (PersistAtomsProto$UnmeteredNetworks[]) ArrayUtils.appendElement(PersistAtomsProto$UnmeteredNetworks.class, persistAtomsProto$PersistAtoms.unmeteredNetworks, persistAtomsProto$UnmeteredNetworks, true);
            } else {
                if (persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.carrierId != i2) {
                    persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.carrierId = i2;
                    persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.unmeteredNetworksBitmask = 0L;
                }
                long j2 = persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.unmeteredNetworksBitmask;
                if ((j2 | j) != j2) {
                    persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.unmeteredNetworksBitmask = j2 | j;
                }
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addOutgoingShortCodeSms(PersistAtomsProto$OutgoingShortCodeSms persistAtomsProto$OutgoingShortCodeSms) {
        try {
            PersistAtomsProto$OutgoingShortCodeSms persistAtomsProto$OutgoingShortCodeSmsFind = find(persistAtomsProto$OutgoingShortCodeSms);
            if (persistAtomsProto$OutgoingShortCodeSmsFind != null) {
                persistAtomsProto$OutgoingShortCodeSmsFind.shortCodeSmsCount++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.outgoingShortCodeSms = (PersistAtomsProto$OutgoingShortCodeSms[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.outgoingShortCodeSms, persistAtomsProto$OutgoingShortCodeSms, this.mMaxOutgoingShortCodeSms);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteControllerStats(PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteController) {
        try {
            PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteControllerFind = find(persistAtomsProto$SatelliteController);
            if (persistAtomsProto$SatelliteControllerFind != null) {
                persistAtomsProto$SatelliteControllerFind.countOfSatelliteServiceEnablementsSuccess += persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfSatelliteServiceEnablementsFail += persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsFail;
                persistAtomsProto$SatelliteControllerFind.countOfOutgoingDatagramSuccess += persistAtomsProto$SatelliteController.countOfOutgoingDatagramSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfOutgoingDatagramFail += persistAtomsProto$SatelliteController.countOfOutgoingDatagramFail;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramSuccess += persistAtomsProto$SatelliteController.countOfIncomingDatagramSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramFail += persistAtomsProto$SatelliteController.countOfIncomingDatagramFail;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeSosSmsSuccess += persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeSosSmsFail += persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsFail;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeLocationSharingSuccess += persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeLocationSharingFail += persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingFail;
                persistAtomsProto$SatelliteControllerFind.countOfProvisionSuccess += persistAtomsProto$SatelliteController.countOfProvisionSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfProvisionFail += persistAtomsProto$SatelliteController.countOfProvisionFail;
                persistAtomsProto$SatelliteControllerFind.countOfDeprovisionSuccess += persistAtomsProto$SatelliteController.countOfDeprovisionSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDeprovisionFail += persistAtomsProto$SatelliteController.countOfDeprovisionFail;
                persistAtomsProto$SatelliteControllerFind.totalServiceUptimeSec += persistAtomsProto$SatelliteController.totalServiceUptimeSec;
                persistAtomsProto$SatelliteControllerFind.totalBatteryConsumptionPercent += persistAtomsProto$SatelliteController.totalBatteryConsumptionPercent;
                persistAtomsProto$SatelliteControllerFind.totalBatteryChargedTimeSec += persistAtomsProto$SatelliteController.totalBatteryChargedTimeSec;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeSatelliteServiceEnablementsSuccess += persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeSatelliteServiceEnablementsFail += persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsFail;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeOutgoingDatagramSuccess += persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeOutgoingDatagramFail += persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramFail;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeIncomingDatagramSuccess += persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDemoModeIncomingDatagramFail += persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramFail;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeKeepAliveSuccess += persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfDatagramTypeKeepAliveFail += persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveFail;
                persistAtomsProto$SatelliteControllerFind.countOfAllowedSatelliteAccess += persistAtomsProto$SatelliteController.countOfAllowedSatelliteAccess;
                persistAtomsProto$SatelliteControllerFind.countOfDisallowedSatelliteAccess += persistAtomsProto$SatelliteController.countOfDisallowedSatelliteAccess;
                persistAtomsProto$SatelliteControllerFind.countOfSatelliteAccessCheckFail += persistAtomsProto$SatelliteController.countOfSatelliteAccessCheckFail;
                persistAtomsProto$SatelliteControllerFind.isProvisioned = persistAtomsProto$SatelliteController.isProvisioned;
                persistAtomsProto$SatelliteControllerFind.countOfSatelliteAllowedStateChangedEvents += persistAtomsProto$SatelliteController.countOfSatelliteAllowedStateChangedEvents;
                persistAtomsProto$SatelliteControllerFind.countOfSuccessfulLocationQueries += persistAtomsProto$SatelliteController.countOfSuccessfulLocationQueries;
                persistAtomsProto$SatelliteControllerFind.countOfFailedLocationQueries += persistAtomsProto$SatelliteController.countOfFailedLocationQueries;
                persistAtomsProto$SatelliteControllerFind.countOfP2PSmsAvailableNotificationShown += persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationShown;
                persistAtomsProto$SatelliteControllerFind.countOfP2PSmsAvailableNotificationRemoved += persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationRemoved;
                persistAtomsProto$SatelliteControllerFind.isNtnOnlyCarrier = persistAtomsProto$SatelliteController.isNtnOnlyCarrier;
                persistAtomsProto$SatelliteControllerFind.versionOfSatelliteAccessConfig = persistAtomsProto$SatelliteController.versionOfSatelliteAccessConfig;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramTypeSosSmsSuccess += persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramTypeSosSmsFail += persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsFail;
                persistAtomsProto$SatelliteControllerFind.countOfOutgoingDatagramTypeSmsSuccess += persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfOutgoingDatagramTypeSmsFail += persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsFail;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramTypeSmsSuccess += persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsSuccess;
                persistAtomsProto$SatelliteControllerFind.countOfIncomingDatagramTypeSmsFail += persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsFail;
                persistAtomsProto$SatelliteControllerFind.carrierRoamingSatelliteConfigVersion = persistAtomsProto$SatelliteController.carrierRoamingSatelliteConfigVersion;
                persistAtomsProto$SatelliteControllerFind.maxAllowedDataMode = persistAtomsProto$SatelliteController.maxAllowedDataMode;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.satelliteController = (PersistAtomsProto$SatelliteController[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteController, persistAtomsProto$SatelliteController, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteSessionStats(PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSession) {
        try {
            PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSessionFind = find(persistAtomsProto$SatelliteSession);
            if (persistAtomsProto$SatelliteSessionFind != null) {
                persistAtomsProto$SatelliteSessionFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.satelliteSession = (PersistAtomsProto$SatelliteSession[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteSession, persistAtomsProto$SatelliteSession, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteIncomingDatagramStats(PersistAtomsProto$SatelliteIncomingDatagram persistAtomsProto$SatelliteIncomingDatagram) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.satelliteIncomingDatagram = (PersistAtomsProto$SatelliteIncomingDatagram[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteIncomingDatagram, persistAtomsProto$SatelliteIncomingDatagram, this.mMaxNumSatelliteStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addSatelliteOutgoingDatagramStats(PersistAtomsProto$SatelliteOutgoingDatagram persistAtomsProto$SatelliteOutgoingDatagram) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.satelliteOutgoingDatagram = (PersistAtomsProto$SatelliteOutgoingDatagram[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteOutgoingDatagram, persistAtomsProto$SatelliteOutgoingDatagram, this.mMaxNumSatelliteStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addSatelliteProvisionStats(PersistAtomsProto$SatelliteProvision persistAtomsProto$SatelliteProvision) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.satelliteProvision = (PersistAtomsProto$SatelliteProvision[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteProvision, persistAtomsProto$SatelliteProvision, this.mMaxNumSatelliteStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addSatelliteSosMessageRecommenderStats(PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommender) {
        try {
            PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommenderFind = find(persistAtomsProto$SatelliteSosMessageRecommender);
            if (persistAtomsProto$SatelliteSosMessageRecommenderFind != null) {
                persistAtomsProto$SatelliteSosMessageRecommenderFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.satelliteSosMessageRecommender = (PersistAtomsProto$SatelliteSosMessageRecommender[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteSosMessageRecommender, persistAtomsProto$SatelliteSosMessageRecommender, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addDataNetworkValidation(PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation) {
        try {
            PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidationFind = find(persistAtomsProto$DataNetworkValidation);
            if (persistAtomsProto$DataNetworkValidationFind != null) {
                int i = persistAtomsProto$DataNetworkValidationFind.networkValidationCount;
                int i2 = persistAtomsProto$DataNetworkValidation.networkValidationCount;
                int i3 = i + i2;
                long j = ((persistAtomsProto$DataNetworkValidation.elapsedTimeInMillis * ((long) i2)) + (persistAtomsProto$DataNetworkValidationFind.elapsedTimeInMillis * ((long) i))) / ((long) i3);
                persistAtomsProto$DataNetworkValidationFind.networkValidationCount = i3;
                persistAtomsProto$DataNetworkValidationFind.elapsedTimeInMillis = j;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.dataNetworkValidation = (PersistAtomsProto$DataNetworkValidation[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.dataNetworkValidation, persistAtomsProto$DataNetworkValidation, this.mMaxNumDataCallSessions);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addCarrierRoamingSatelliteSessionStats(PersistAtomsProto$CarrierRoamingSatelliteSession persistAtomsProto$CarrierRoamingSatelliteSession) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.carrierRoamingSatelliteSession = (PersistAtomsProto$CarrierRoamingSatelliteSession[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.carrierRoamingSatelliteSession, persistAtomsProto$CarrierRoamingSatelliteSession, this.mMaxNumSatelliteStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addCarrierRoamingSatelliteControllerStats(PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStats) {
        try {
            PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind = find(persistAtomsProto$CarrierRoamingSatelliteControllerStats);
            if (persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind != null) {
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfEntitlementStatusQueryRequest += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfEntitlementStatusQueryRequest;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfSatelliteConfigUpdateRequest += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteConfigUpdateRequest;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfSatelliteNotificationDisplayed += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteNotificationDisplayed;
                int i = persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMinSec;
                if (i != -1) {
                    persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.satelliteSessionGapMinSec = i;
                }
                int i2 = persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapAvgSec;
                if (i2 != -1) {
                    persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.satelliteSessionGapAvgSec = i2;
                }
                int i3 = persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMaxSec;
                if (i3 != -1) {
                    persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.satelliteSessionGapMaxSec = i3;
                }
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.isDeviceEntitled = persistAtomsProto$CarrierRoamingSatelliteControllerStats.isDeviceEntitled;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.isMultiSim = persistAtomsProto$CarrierRoamingSatelliteControllerStats.isMultiSim;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfSatelliteSessions += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteSessions;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.isNbIotNtn = persistAtomsProto$CarrierRoamingSatelliteControllerStats.isNbIotNtn;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfSessionConnectionModeAutomatic += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeAutomatic;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.countOfSessionConnectionModeManual += persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeManual;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.serviceDataPolicy = persistAtomsProto$CarrierRoamingSatelliteControllerStats.serviceDataPolicy;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.totalSessionDurationSec += persistAtomsProto$CarrierRoamingSatelliteControllerStats.totalSessionDurationSec;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.satelliteAttachSupported = persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteAttachSupported;
                persistAtomsProto$CarrierRoamingSatelliteControllerStatsFind.eligibilitySource = persistAtomsProto$CarrierRoamingSatelliteControllerStats.eligibilitySource;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.carrierRoamingSatelliteControllerStats = (PersistAtomsProto$CarrierRoamingSatelliteControllerStats[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.carrierRoamingSatelliteControllerStats, persistAtomsProto$CarrierRoamingSatelliteControllerStats, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteEntitlementStats(PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlement) {
        try {
            PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlementFind = find(persistAtomsProto$SatelliteEntitlement);
            if (persistAtomsProto$SatelliteEntitlementFind != null) {
                persistAtomsProto$SatelliteEntitlementFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.satelliteEntitlement = (PersistAtomsProto$SatelliteEntitlement[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteEntitlement, persistAtomsProto$SatelliteEntitlement, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteConfigUpdaterStats(PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdater) {
        try {
            PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdaterFind = find(persistAtomsProto$SatelliteConfigUpdater);
            if (persistAtomsProto$SatelliteConfigUpdaterFind != null) {
                persistAtomsProto$SatelliteConfigUpdaterFind.count++;
            } else {
                PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
                persistAtomsProto$PersistAtoms.satelliteConfigUpdater = (PersistAtomsProto$SatelliteConfigUpdater[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteConfigUpdater, persistAtomsProto$SatelliteConfigUpdater, this.mMaxNumSatelliteStats);
            }
            saveAtomsToFile(30000);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void addSatelliteAccessControllerStats(PersistAtomsProto$SatelliteAccessController persistAtomsProto$SatelliteAccessController) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.satelliteAccessController = (PersistAtomsProto$SatelliteAccessController[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.satelliteAccessController, persistAtomsProto$SatelliteAccessController, this.mMaxNumSatelliteStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addOtpEvaluationEvent(PersistAtomsProto$OtpEvaluationEvent persistAtomsProto$OtpEvaluationEvent) {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.otpEvaluationEvent = (PersistAtomsProto$OtpEvaluationEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.otpEvaluationEvent, persistAtomsProto$OtpEvaluationEvent, this.mMaxNumOtpStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addOtpRedactionEvent(PersistAtomsProto$OtpRedactionEvent persistAtomsProto$OtpRedactionEvent) {
        for (PersistAtomsProto$OtpRedactionEvent persistAtomsProto$OtpRedactionEvent2 : this.mAtoms.otpRedactionEvent) {
            if (persistAtomsProto$OtpRedactionEvent.uid == persistAtomsProto$OtpRedactionEvent2.uid) {
                persistAtomsProto$OtpRedactionEvent2.count++;
                saveAtomsToFile(30000);
                return;
            }
        }
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.otpRedactionEvent = (PersistAtomsProto$OtpRedactionEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.otpRedactionEvent, persistAtomsProto$OtpRedactionEvent, this.mMaxNumOtpStats);
        saveAtomsToFile(30000);
    }

    public synchronized void addMessagingReadRestrictionEvent(PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent) {
        for (PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent2 : this.mAtoms.messagingReadRestrictionEvent) {
            if (persistAtomsProto$MessagingReadRestrictionEvent.callerUid == persistAtomsProto$MessagingReadRestrictionEvent2.callerUid && persistAtomsProto$MessagingReadRestrictionEvent.contentProvider == persistAtomsProto$MessagingReadRestrictionEvent2.contentProvider && persistAtomsProto$MessagingReadRestrictionEvent.eventType == persistAtomsProto$MessagingReadRestrictionEvent2.eventType && persistAtomsProto$MessagingReadRestrictionEvent.readRestrictedMessagesAppOpMode == persistAtomsProto$MessagingReadRestrictionEvent2.readRestrictedMessagesAppOpMode) {
                persistAtomsProto$MessagingReadRestrictionEvent2.count++;
                saveAtomsToFile(30000);
                return;
            }
        }
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.messagingReadRestrictionEvent = (PersistAtomsProto$MessagingReadRestrictionEvent[]) insertAtRandomPlace(persistAtomsProto$PersistAtoms.messagingReadRestrictionEvent, persistAtomsProto$MessagingReadRestrictionEvent, this.mMaxNumMessagingReadRestrictionEvent);
        saveAtomsToFile(30000);
    }

    public synchronized PersistAtomsProto$VoiceCallSession[] getVoiceCallSessions(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.voiceCallSessionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.voiceCallSessionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$VoiceCallSession[] persistAtomsProto$VoiceCallSessionArr = persistAtomsProto$PersistAtoms2.voiceCallSession;
        persistAtomsProto$PersistAtoms2.voiceCallSession = new PersistAtomsProto$VoiceCallSession[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$VoiceCallSessionArr;
    }

    public synchronized PersistAtomsProto$VoiceCallRatUsage[] getVoiceCallRatUsages(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.voiceCallRatUsagePullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.voiceCallRatUsagePullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$VoiceCallRatUsage[] persistAtomsProto$VoiceCallRatUsageArr = this.mAtoms.voiceCallRatUsage;
        this.mVoiceCallRatTracker.clear();
        this.mAtoms.voiceCallRatUsage = new PersistAtomsProto$VoiceCallRatUsage[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$VoiceCallRatUsageArr;
    }

    public synchronized PersistAtomsProto$IncomingSms[] getIncomingSms(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.incomingSmsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.incomingSmsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$IncomingSms[] persistAtomsProto$IncomingSmsArr = persistAtomsProto$PersistAtoms2.incomingSms;
        persistAtomsProto$PersistAtoms2.incomingSms = new PersistAtomsProto$IncomingSms[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$IncomingSmsArr;
    }

    public synchronized PersistAtomsProto$OutgoingSms[] getOutgoingSms(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.outgoingSmsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.outgoingSmsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$OutgoingSms[] persistAtomsProto$OutgoingSmsArr = persistAtomsProto$PersistAtoms2.outgoingSms;
        persistAtomsProto$PersistAtoms2.outgoingSms = new PersistAtomsProto$OutgoingSms[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$OutgoingSmsArr;
    }

    public synchronized PersistAtomsProto$DataCallSession[] getDataCallSessions(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.dataCallSessionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.dataCallSessionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$DataCallSession[] persistAtomsProto$DataCallSessionArr = persistAtomsProto$PersistAtoms2.dataCallSession;
        persistAtomsProto$PersistAtoms2.dataCallSession = new PersistAtomsProto$DataCallSession[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        for (PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession : persistAtomsProto$DataCallSessionArr) {
            sortBaseOnArray(persistAtomsProto$DataCallSession.handoverFailureCauses, persistAtomsProto$DataCallSession.handoverFailureRat);
        }
        return persistAtomsProto$DataCallSessionArr;
    }

    private void sortBaseOnArray(final int[] iArr, int[] iArr2) {
        if (iArr2.length != iArr.length) {
            return;
        }
        int[] array = IntStream.range(0, iArr.length).boxed().sorted(Comparator.comparingInt(new ToIntFunction() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda3
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return PersistAtomsStorage.m1223$r8$lambda$2qnY12lnIoX3FfG4XXjeDpYlgU(iArr, (Integer) obj);
            }
        })).mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
        int[] iArrCopyOf = Arrays.copyOf(iArr, iArr.length);
        int[] iArrCopyOf2 = Arrays.copyOf(iArr2, iArr2.length);
        for (int i = 0; i < array.length; i++) {
            iArr[i] = iArrCopyOf[array[i]];
            iArr2[i] = iArrCopyOf2[array[i]];
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$2qnY12lnIoX3FfG4XXjeDpYlg-U, reason: not valid java name */
    public static /* synthetic */ int m1223$r8$lambda$2qnY12lnIoX3FfG4XXjeDpYlgU(int[] iArr, Integer num) {
        return iArr[num.intValue()];
    }

    public synchronized PersistAtomsProto$CellularServiceState[] getCellularServiceStates(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.cellularServiceStatePullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.cellularServiceStatePullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$CellularServiceState[] persistAtomsProto$CellularServiceStateArr = this.mAtoms.cellularServiceState;
        Arrays.stream(persistAtomsProto$CellularServiceStateArr).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PersistAtomsProto$CellularServiceState) obj).lastUsedMillis = 0L;
            }
        });
        this.mAtoms.cellularServiceState = new PersistAtomsProto$CellularServiceState[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$CellularServiceStateArr;
    }

    public synchronized PersistAtomsProto$CellularDataServiceSwitch[] getCellularDataServiceSwitches(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.cellularDataServiceSwitchPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.cellularDataServiceSwitchPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$CellularDataServiceSwitch[] persistAtomsProto$CellularDataServiceSwitchArr = this.mAtoms.cellularDataServiceSwitch;
        Arrays.stream(persistAtomsProto$CellularDataServiceSwitchArr).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PersistAtomsProto$CellularDataServiceSwitch) obj).lastUsedMillis = 0L;
            }
        });
        this.mAtoms.cellularDataServiceSwitch = new PersistAtomsProto$CellularDataServiceSwitch[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$CellularDataServiceSwitchArr;
    }

    public synchronized PersistAtomsProto$ImsRegistrationStats[] getImsRegistrationStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        long j2 = wallTimeMillis - persistAtomsProto$PersistAtoms.imsRegistrationStatsPullTimestampMillis;
        if (j2 <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.imsRegistrationStatsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$ImsRegistrationStats[] persistAtomsProto$ImsRegistrationStatsArr = this.mAtoms.imsRegistrationStats;
        Arrays.stream(persistAtomsProto$ImsRegistrationStatsArr).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PersistAtomsProto$ImsRegistrationStats) obj).lastUsedMillis = 0L;
            }
        });
        this.mAtoms.imsRegistrationStats = new PersistAtomsProto$ImsRegistrationStats[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return normalizeData(persistAtomsProto$ImsRegistrationStatsArr, j2);
    }

    public synchronized PersistAtomsProto$ImsRegistrationTermination[] getImsRegistrationTerminations(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.imsRegistrationTerminationPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.imsRegistrationTerminationPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$ImsRegistrationTermination[] persistAtomsProto$ImsRegistrationTerminationArr = this.mAtoms.imsRegistrationTermination;
        Arrays.stream(persistAtomsProto$ImsRegistrationTerminationArr).forEach(new Consumer() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PersistAtomsProto$ImsRegistrationTermination) obj).lastUsedMillis = 0L;
            }
        });
        this.mAtoms.imsRegistrationTermination = new PersistAtomsProto$ImsRegistrationTermination[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$ImsRegistrationTerminationArr;
    }

    public synchronized PersistAtomsProto$NetworkRequestsV2[] getNetworkRequestsV2(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.networkRequestsV2PullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.networkRequestsV2PullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$NetworkRequestsV2[] persistAtomsProto$NetworkRequestsV2Arr = persistAtomsProto$PersistAtoms2.networkRequestsV2;
        persistAtomsProto$PersistAtoms2.networkRequestsV2 = new PersistAtomsProto$NetworkRequestsV2[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$NetworkRequestsV2Arr;
    }

    public synchronized int getAutoDataSwitchToggleCount() {
        int i;
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        i = persistAtomsProto$PersistAtoms.autoDataSwitchToggleCount;
        if (i > 0) {
            persistAtomsProto$PersistAtoms.autoDataSwitchToggleCount = 0;
            saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        }
        return i;
    }

    public synchronized PersistAtomsProto$ImsRegistrationFeatureTagStats[] getImsRegistrationFeatureTagStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.rcsAcsProvisioningStatsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.imsRegistrationFeatureTagStatsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$ImsRegistrationFeatureTagStats[] persistAtomsProto$ImsRegistrationFeatureTagStatsArr = persistAtomsProto$PersistAtoms2.imsRegistrationFeatureTagStats;
        persistAtomsProto$PersistAtoms2.imsRegistrationFeatureTagStats = new PersistAtomsProto$ImsRegistrationFeatureTagStats[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$ImsRegistrationFeatureTagStatsArr;
    }

    public synchronized PersistAtomsProto$RcsClientProvisioningStats[] getRcsClientProvisioningStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.rcsClientProvisioningStatsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.rcsClientProvisioningStatsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$RcsClientProvisioningStats[] persistAtomsProto$RcsClientProvisioningStatsArr = persistAtomsProto$PersistAtoms2.rcsClientProvisioningStats;
        persistAtomsProto$PersistAtoms2.rcsClientProvisioningStats = new PersistAtomsProto$RcsClientProvisioningStats[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$RcsClientProvisioningStatsArr;
    }

    public synchronized PersistAtomsProto$RcsAcsProvisioningStats[] getRcsAcsProvisioningStats(long j) {
        try {
            long wallTimeMillis = getWallTimeMillis();
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            long j2 = wallTimeMillis - persistAtomsProto$PersistAtoms.rcsAcsProvisioningStatsPullTimestampMillis;
            if (j2 <= j) {
                return null;
            }
            persistAtomsProto$PersistAtoms.rcsAcsProvisioningStatsPullTimestampMillis = getWallTimeMillis();
            PersistAtomsProto$RcsAcsProvisioningStats[] persistAtomsProto$RcsAcsProvisioningStatsArr = this.mAtoms.rcsAcsProvisioningStats;
            for (PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStats : persistAtomsProto$RcsAcsProvisioningStatsArr) {
                if (j2 > 86400000) {
                    persistAtomsProto$RcsAcsProvisioningStats.stateTimerMillis = normalizeDurationTo24H(persistAtomsProto$RcsAcsProvisioningStats.stateTimerMillis, j2);
                }
            }
            this.mAtoms.rcsAcsProvisioningStats = new PersistAtomsProto$RcsAcsProvisioningStats[0];
            saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
            return persistAtomsProto$RcsAcsProvisioningStatsArr;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized PersistAtomsProto$SipDelegateStats[] getSipDelegateStats(long j) {
        try {
            long wallTimeMillis = getWallTimeMillis();
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            long j2 = wallTimeMillis - persistAtomsProto$PersistAtoms.sipDelegateStatsPullTimestampMillis;
            if (j2 <= j) {
                return null;
            }
            persistAtomsProto$PersistAtoms.sipDelegateStatsPullTimestampMillis = getWallTimeMillis();
            PersistAtomsProto$SipDelegateStats[] persistAtomsProto$SipDelegateStatsArr = this.mAtoms.sipDelegateStats;
            for (PersistAtomsProto$SipDelegateStats persistAtomsProto$SipDelegateStats : persistAtomsProto$SipDelegateStatsArr) {
                if (j2 > 86400000) {
                    persistAtomsProto$SipDelegateStats.uptimeMillis = normalizeDurationTo24H(persistAtomsProto$SipDelegateStats.uptimeMillis, j2);
                }
            }
            this.mAtoms.sipDelegateStats = new PersistAtomsProto$SipDelegateStats[0];
            saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
            return persistAtomsProto$SipDelegateStatsArr;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized PersistAtomsProto$SipTransportFeatureTagStats[] getSipTransportFeatureTagStats(long j) {
        try {
            long wallTimeMillis = getWallTimeMillis();
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            long j2 = wallTimeMillis - persistAtomsProto$PersistAtoms.sipTransportFeatureTagStatsPullTimestampMillis;
            if (j2 <= j) {
                return null;
            }
            persistAtomsProto$PersistAtoms.sipTransportFeatureTagStatsPullTimestampMillis = getWallTimeMillis();
            PersistAtomsProto$SipTransportFeatureTagStats[] persistAtomsProto$SipTransportFeatureTagStatsArr = this.mAtoms.sipTransportFeatureTagStats;
            for (PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStats : persistAtomsProto$SipTransportFeatureTagStatsArr) {
                if (j2 > 86400000) {
                    persistAtomsProto$SipTransportFeatureTagStats.associatedMillis = normalizeDurationTo24H(persistAtomsProto$SipTransportFeatureTagStats.associatedMillis, j2);
                }
            }
            this.mAtoms.sipTransportFeatureTagStats = new PersistAtomsProto$SipTransportFeatureTagStats[0];
            saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
            return persistAtomsProto$SipTransportFeatureTagStatsArr;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized PersistAtomsProto$SipMessageResponse[] getSipMessageResponse(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.sipMessageResponsePullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.sipMessageResponsePullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SipMessageResponse[] persistAtomsProto$SipMessageResponseArr = persistAtomsProto$PersistAtoms2.sipMessageResponse;
        persistAtomsProto$PersistAtoms2.sipMessageResponse = new PersistAtomsProto$SipMessageResponse[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SipMessageResponseArr;
    }

    public synchronized PersistAtomsProto$SipTransportSession[] getSipTransportSession(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.sipTransportSessionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.sipTransportSessionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SipTransportSession[] persistAtomsProto$SipTransportSessionArr = persistAtomsProto$PersistAtoms2.sipTransportSession;
        persistAtomsProto$PersistAtoms2.sipTransportSession = new PersistAtomsProto$SipTransportSession[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SipTransportSessionArr;
    }

    public synchronized PersistAtomsProto$ImsDedicatedBearerListenerEvent[] getImsDedicatedBearerListenerEvent(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.imsDedicatedBearerListenerEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.imsDedicatedBearerListenerEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$ImsDedicatedBearerListenerEvent[] persistAtomsProto$ImsDedicatedBearerListenerEventArr = persistAtomsProto$PersistAtoms2.imsDedicatedBearerListenerEvent;
        persistAtomsProto$PersistAtoms2.imsDedicatedBearerListenerEvent = new PersistAtomsProto$ImsDedicatedBearerListenerEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$ImsDedicatedBearerListenerEventArr;
    }

    public synchronized PersistAtomsProto$ImsDedicatedBearerEvent[] getImsDedicatedBearerEvent(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.imsDedicatedBearerEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.imsDedicatedBearerEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$ImsDedicatedBearerEvent[] persistAtomsProto$ImsDedicatedBearerEventArr = persistAtomsProto$PersistAtoms2.imsDedicatedBearerEvent;
        persistAtomsProto$PersistAtoms2.imsDedicatedBearerEvent = new PersistAtomsProto$ImsDedicatedBearerEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$ImsDedicatedBearerEventArr;
    }

    public synchronized PersistAtomsProto$ImsRegistrationServiceDescStats[] getImsRegistrationServiceDescStats(long j) {
        try {
            long wallTimeMillis = getWallTimeMillis();
            PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
            long j2 = wallTimeMillis - persistAtomsProto$PersistAtoms.imsRegistrationServiceDescStatsPullTimestampMillis;
            if (j2 <= j) {
                return null;
            }
            persistAtomsProto$PersistAtoms.imsRegistrationServiceDescStatsPullTimestampMillis = getWallTimeMillis();
            PersistAtomsProto$ImsRegistrationServiceDescStats[] persistAtomsProto$ImsRegistrationServiceDescStatsArr = this.mAtoms.imsRegistrationServiceDescStats;
            for (PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStats : persistAtomsProto$ImsRegistrationServiceDescStatsArr) {
                if (j2 > 86400000) {
                    persistAtomsProto$ImsRegistrationServiceDescStats.publishedMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationServiceDescStats.publishedMillis, j2);
                }
            }
            this.mAtoms.imsRegistrationServiceDescStats = new PersistAtomsProto$ImsRegistrationServiceDescStats[0];
            saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
            return persistAtomsProto$ImsRegistrationServiceDescStatsArr;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized PersistAtomsProto$UceEventStats[] getUceEventStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.uceEventStatsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.uceEventStatsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$UceEventStats[] persistAtomsProto$UceEventStatsArr = persistAtomsProto$PersistAtoms2.uceEventStats;
        persistAtomsProto$PersistAtoms2.uceEventStats = new PersistAtomsProto$UceEventStats[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$UceEventStatsArr;
    }

    public synchronized PersistAtomsProto$PresenceNotifyEvent[] getPresenceNotifyEvent(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.presenceNotifyEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.presenceNotifyEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$PresenceNotifyEvent[] persistAtomsProto$PresenceNotifyEventArr = persistAtomsProto$PersistAtoms2.presenceNotifyEvent;
        persistAtomsProto$PersistAtoms2.presenceNotifyEvent = new PersistAtomsProto$PresenceNotifyEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$PresenceNotifyEventArr;
    }

    public synchronized PersistAtomsProto$GbaEvent[] getGbaEvent(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.gbaEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.gbaEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$GbaEvent[] persistAtomsProto$GbaEventArr = persistAtomsProto$PersistAtoms2.gbaEvent;
        persistAtomsProto$PersistAtoms2.gbaEvent = new PersistAtomsProto$GbaEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$GbaEventArr;
    }

    public synchronized long getUnmeteredNetworks(int i, int i2) {
        PersistAtomsProto$UnmeteredNetworks persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks = findUnmeteredNetworks(i);
        long j = 0;
        if (persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks == null) {
            return 0L;
        }
        if (persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.carrierId == i2) {
            j = persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks.unmeteredNetworksBitmask;
        }
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        persistAtomsProto$PersistAtoms.unmeteredNetworks = (PersistAtomsProto$UnmeteredNetworks[]) sanitizeAtoms((PersistAtomsProto$UnmeteredNetworks[]) ArrayUtils.removeElement(PersistAtomsProto$UnmeteredNetworks.class, persistAtomsProto$PersistAtoms.unmeteredNetworks, persistAtomsProto$UnmeteredNetworksFindUnmeteredNetworks), PersistAtomsProto$UnmeteredNetworks.class);
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return j;
    }

    public synchronized PersistAtomsProto$OutgoingShortCodeSms[] getOutgoingShortCodeSms(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.outgoingShortCodeSmsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.outgoingShortCodeSmsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$OutgoingShortCodeSms[] persistAtomsProto$OutgoingShortCodeSmsArr = persistAtomsProto$PersistAtoms2.outgoingShortCodeSms;
        persistAtomsProto$PersistAtoms2.outgoingShortCodeSms = new PersistAtomsProto$OutgoingShortCodeSms[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$OutgoingShortCodeSmsArr;
    }

    public synchronized PersistAtomsProto$SatelliteController[] getSatelliteControllerStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteControllerPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteControllerPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteController[] persistAtomsProto$SatelliteControllerArr = persistAtomsProto$PersistAtoms2.satelliteController;
        persistAtomsProto$PersistAtoms2.satelliteController = new PersistAtomsProto$SatelliteController[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteControllerArr;
    }

    public synchronized PersistAtomsProto$SatelliteSession[] getSatelliteSessionStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteSessionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteSessionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteSession[] persistAtomsProto$SatelliteSessionArr = persistAtomsProto$PersistAtoms2.satelliteSession;
        persistAtomsProto$PersistAtoms2.satelliteSession = new PersistAtomsProto$SatelliteSession[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteSessionArr;
    }

    public synchronized PersistAtomsProto$SatelliteIncomingDatagram[] getSatelliteIncomingDatagramStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteIncomingDatagramPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteIncomingDatagramPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteIncomingDatagram[] persistAtomsProto$SatelliteIncomingDatagramArr = persistAtomsProto$PersistAtoms2.satelliteIncomingDatagram;
        persistAtomsProto$PersistAtoms2.satelliteIncomingDatagram = new PersistAtomsProto$SatelliteIncomingDatagram[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteIncomingDatagramArr;
    }

    public synchronized PersistAtomsProto$SatelliteOutgoingDatagram[] getSatelliteOutgoingDatagramStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteOutgoingDatagramPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteOutgoingDatagramPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteOutgoingDatagram[] persistAtomsProto$SatelliteOutgoingDatagramArr = persistAtomsProto$PersistAtoms2.satelliteOutgoingDatagram;
        persistAtomsProto$PersistAtoms2.satelliteOutgoingDatagram = new PersistAtomsProto$SatelliteOutgoingDatagram[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteOutgoingDatagramArr;
    }

    public synchronized PersistAtomsProto$SatelliteProvision[] getSatelliteProvisionStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteProvisionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteProvisionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteProvision[] persistAtomsProto$SatelliteProvisionArr = persistAtomsProto$PersistAtoms2.satelliteProvision;
        persistAtomsProto$PersistAtoms2.satelliteProvision = new PersistAtomsProto$SatelliteProvision[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteProvisionArr;
    }

    public synchronized PersistAtomsProto$SatelliteSosMessageRecommender[] getSatelliteSosMessageRecommenderStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteSosMessageRecommenderPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteSosMessageRecommenderPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteSosMessageRecommender[] persistAtomsProto$SatelliteSosMessageRecommenderArr = persistAtomsProto$PersistAtoms2.satelliteSosMessageRecommender;
        persistAtomsProto$PersistAtoms2.satelliteSosMessageRecommender = new PersistAtomsProto$SatelliteSosMessageRecommender[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteSosMessageRecommenderArr;
    }

    public synchronized PersistAtomsProto$DataNetworkValidation[] getDataNetworkValidation(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.dataNetworkValidationPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.dataNetworkValidationPullTimestampMillis = wallTimeMillis;
        PersistAtomsProto$DataNetworkValidation[] persistAtomsProto$DataNetworkValidationArr = persistAtomsProto$PersistAtoms.dataNetworkValidation;
        persistAtomsProto$PersistAtoms.dataNetworkValidation = new PersistAtomsProto$DataNetworkValidation[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$DataNetworkValidationArr;
    }

    public synchronized PersistAtomsProto$CarrierRoamingSatelliteSession[] getCarrierRoamingSatelliteSessionStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.carrierRoamingSatelliteSessionPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.carrierRoamingSatelliteSessionPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$CarrierRoamingSatelliteSession[] persistAtomsProto$CarrierRoamingSatelliteSessionArr = persistAtomsProto$PersistAtoms2.carrierRoamingSatelliteSession;
        persistAtomsProto$PersistAtoms2.carrierRoamingSatelliteSession = new PersistAtomsProto$CarrierRoamingSatelliteSession[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$CarrierRoamingSatelliteSessionArr;
    }

    public synchronized PersistAtomsProto$CarrierRoamingSatelliteControllerStats[] getCarrierRoamingSatelliteControllerStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.carrierRoamingSatelliteControllerStatsPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.carrierRoamingSatelliteControllerStatsPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$CarrierRoamingSatelliteControllerStats[] persistAtomsProto$CarrierRoamingSatelliteControllerStatsArr = persistAtomsProto$PersistAtoms2.carrierRoamingSatelliteControllerStats;
        persistAtomsProto$PersistAtoms2.carrierRoamingSatelliteControllerStats = new PersistAtomsProto$CarrierRoamingSatelliteControllerStats[0];
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                SatelliteStats.getInstance().resetCarrierRoamingSatelliteControllerStats();
            }
        });
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$CarrierRoamingSatelliteControllerStatsArr;
    }

    public synchronized PersistAtomsProto$SatelliteEntitlement[] getSatelliteEntitlementStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteEntitlementPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteEntitlementPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteEntitlement[] persistAtomsProto$SatelliteEntitlementArr = persistAtomsProto$PersistAtoms2.satelliteEntitlement;
        persistAtomsProto$PersistAtoms2.satelliteEntitlement = new PersistAtomsProto$SatelliteEntitlement[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteEntitlementArr;
    }

    public synchronized PersistAtomsProto$SatelliteConfigUpdater[] getSatelliteConfigUpdaterStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteConfigUpdaterPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteConfigUpdaterPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteConfigUpdater[] persistAtomsProto$SatelliteConfigUpdaterArr = persistAtomsProto$PersistAtoms2.satelliteConfigUpdater;
        persistAtomsProto$PersistAtoms2.satelliteConfigUpdater = new PersistAtomsProto$SatelliteConfigUpdater[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteConfigUpdaterArr;
    }

    public synchronized PersistAtomsProto$SatelliteAccessController[] getSatelliteAccessControllerStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.satelliteAccessControllerPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.satelliteAccessControllerPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$SatelliteAccessController[] persistAtomsProto$SatelliteAccessControllerArr = persistAtomsProto$PersistAtoms2.satelliteAccessController;
        persistAtomsProto$PersistAtoms2.satelliteAccessController = new PersistAtomsProto$SatelliteAccessController[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$SatelliteAccessControllerArr;
    }

    public synchronized PersistAtomsProto$OtpEvaluationEvent[] getOtpEvaluationEventStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.otpEvaluationEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.otpEvaluationEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$OtpEvaluationEvent[] persistAtomsProto$OtpEvaluationEventArr = persistAtomsProto$PersistAtoms2.otpEvaluationEvent;
        persistAtomsProto$PersistAtoms2.otpEvaluationEvent = new PersistAtomsProto$OtpEvaluationEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$OtpEvaluationEventArr;
    }

    public synchronized PersistAtomsProto$OtpRedactionEvent[] getOtpRedactionEventStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.otpRedactionEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.otpRedactionEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$OtpRedactionEvent[] persistAtomsProto$OtpRedactionEventArr = persistAtomsProto$PersistAtoms2.otpRedactionEvent;
        persistAtomsProto$PersistAtoms2.otpRedactionEvent = new PersistAtomsProto$OtpRedactionEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$OtpRedactionEventArr;
    }

    public synchronized PersistAtomsProto$MessagingReadRestrictionEvent[] getMessagingReadRestrictionEventStats(long j) {
        long wallTimeMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (wallTimeMillis - persistAtomsProto$PersistAtoms.messagingReadRestrictionEventPullTimestampMillis <= j) {
            return null;
        }
        persistAtomsProto$PersistAtoms.messagingReadRestrictionEventPullTimestampMillis = getWallTimeMillis();
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms2 = this.mAtoms;
        PersistAtomsProto$MessagingReadRestrictionEvent[] persistAtomsProto$MessagingReadRestrictionEventArr = persistAtomsProto$PersistAtoms2.messagingReadRestrictionEvent;
        persistAtomsProto$PersistAtoms2.messagingReadRestrictionEvent = new PersistAtomsProto$MessagingReadRestrictionEvent[0];
        saveAtomsToFile(SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD);
        return persistAtomsProto$MessagingReadRestrictionEventArr;
    }

    public synchronized void flushAtoms() {
        saveAtomsToFile(0);
    }

    public synchronized void clearAtoms() {
        this.mAtoms = makeNewPersistAtoms();
        saveAtomsToFile(0);
    }

    public synchronized byte[] getAtomsProtoBytes() {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = this.mAtoms;
        if (persistAtomsProto$PersistAtoms == null) {
            Rlog.w(TAG, "getAtomsProtoBytes: mAtoms is null, returning null.");
            return null;
        }
        return MessageNano.toByteArray(persistAtomsProto$PersistAtoms);
    }

    private PersistAtomsProto$PersistAtoms loadAtomsFromFile() {
        try {
            PersistAtomsProto$PersistAtoms from = PersistAtomsProto$PersistAtoms.parseFrom(Files.readAllBytes(this.mContext.getFileStreamPath("persist_atoms.pb").toPath()));
            if (!Build.FINGERPRINT.equals(from.buildFingerprint)) {
                Rlog.d(TAG, "Build changed");
                return makeNewPersistAtoms();
            }
            from.voiceCallRatUsage = (PersistAtomsProto$VoiceCallRatUsage[]) sanitizeAtoms(from.voiceCallRatUsage, PersistAtomsProto$VoiceCallRatUsage.class);
            from.voiceCallSession = (PersistAtomsProto$VoiceCallSession[]) sanitizeAtoms(from.voiceCallSession, PersistAtomsProto$VoiceCallSession.class, this.mMaxNumVoiceCallSessions);
            from.incomingSms = (PersistAtomsProto$IncomingSms[]) sanitizeAtoms(from.incomingSms, PersistAtomsProto$IncomingSms.class, this.mMaxNumSms);
            from.outgoingSms = (PersistAtomsProto$OutgoingSms[]) sanitizeAtoms(from.outgoingSms, PersistAtomsProto$OutgoingSms.class, this.mMaxNumSms);
            from.carrierIdMismatch = (PersistAtomsProto$CarrierIdMismatch[]) sanitizeAtoms(from.carrierIdMismatch, PersistAtomsProto$CarrierIdMismatch.class, this.mMaxNumCarrierIdMismatches);
            from.dataCallSession = (PersistAtomsProto$DataCallSession[]) sanitizeAtoms(from.dataCallSession, PersistAtomsProto$DataCallSession.class, this.mMaxNumDataCallSessions);
            from.cellularServiceState = (PersistAtomsProto$CellularServiceState[]) sanitizeAtoms(from.cellularServiceState, PersistAtomsProto$CellularServiceState.class, this.mMaxNumCellularServiceStates);
            from.cellularDataServiceSwitch = (PersistAtomsProto$CellularDataServiceSwitch[]) sanitizeAtoms(from.cellularDataServiceSwitch, PersistAtomsProto$CellularDataServiceSwitch.class, this.mMaxNumCellularDataSwitches);
            from.imsRegistrationStats = (PersistAtomsProto$ImsRegistrationStats[]) sanitizeAtoms(from.imsRegistrationStats, PersistAtomsProto$ImsRegistrationStats.class, this.mMaxNumImsRegistrationStats);
            from.imsRegistrationTermination = (PersistAtomsProto$ImsRegistrationTermination[]) sanitizeAtoms(from.imsRegistrationTermination, PersistAtomsProto$ImsRegistrationTermination.class, this.mMaxNumImsRegistrationTerminations);
            from.networkRequestsV2 = (PersistAtomsProto$NetworkRequestsV2[]) sanitizeAtoms(from.networkRequestsV2, PersistAtomsProto$NetworkRequestsV2.class);
            from.imsRegistrationFeatureTagStats = (PersistAtomsProto$ImsRegistrationFeatureTagStats[]) sanitizeAtoms(from.imsRegistrationFeatureTagStats, PersistAtomsProto$ImsRegistrationFeatureTagStats.class, this.mMaxNumImsRegistrationFeatureStats);
            from.rcsClientProvisioningStats = (PersistAtomsProto$RcsClientProvisioningStats[]) sanitizeAtoms(from.rcsClientProvisioningStats, PersistAtomsProto$RcsClientProvisioningStats.class, this.mMaxNumRcsClientProvisioningStats);
            from.rcsAcsProvisioningStats = (PersistAtomsProto$RcsAcsProvisioningStats[]) sanitizeAtoms(from.rcsAcsProvisioningStats, PersistAtomsProto$RcsAcsProvisioningStats.class, this.mMaxNumRcsAcsProvisioningStats);
            from.sipDelegateStats = (PersistAtomsProto$SipDelegateStats[]) sanitizeAtoms(from.sipDelegateStats, PersistAtomsProto$SipDelegateStats.class, this.mMaxNumSipDelegateStats);
            from.sipTransportFeatureTagStats = (PersistAtomsProto$SipTransportFeatureTagStats[]) sanitizeAtoms(from.sipTransportFeatureTagStats, PersistAtomsProto$SipTransportFeatureTagStats.class, this.mMaxNumSipTransportFeatureTagStats);
            from.sipMessageResponse = (PersistAtomsProto$SipMessageResponse[]) sanitizeAtoms(from.sipMessageResponse, PersistAtomsProto$SipMessageResponse.class, this.mMaxNumSipMessageResponseStats);
            from.sipTransportSession = (PersistAtomsProto$SipTransportSession[]) sanitizeAtoms(from.sipTransportSession, PersistAtomsProto$SipTransportSession.class, this.mMaxNumSipTransportSessionStats);
            from.imsDedicatedBearerListenerEvent = (PersistAtomsProto$ImsDedicatedBearerListenerEvent[]) sanitizeAtoms(from.imsDedicatedBearerListenerEvent, PersistAtomsProto$ImsDedicatedBearerListenerEvent.class, this.mMaxNumDedicatedBearerListenerEventStats);
            from.imsDedicatedBearerEvent = (PersistAtomsProto$ImsDedicatedBearerEvent[]) sanitizeAtoms(from.imsDedicatedBearerEvent, PersistAtomsProto$ImsDedicatedBearerEvent.class, this.mMaxNumDedicatedBearerEventStats);
            from.imsRegistrationServiceDescStats = (PersistAtomsProto$ImsRegistrationServiceDescStats[]) sanitizeAtoms(from.imsRegistrationServiceDescStats, PersistAtomsProto$ImsRegistrationServiceDescStats.class, this.mMaxNumImsRegistrationServiceDescStats);
            from.uceEventStats = (PersistAtomsProto$UceEventStats[]) sanitizeAtoms(from.uceEventStats, PersistAtomsProto$UceEventStats.class, this.mMaxNumUceEventStats);
            from.presenceNotifyEvent = (PersistAtomsProto$PresenceNotifyEvent[]) sanitizeAtoms(from.presenceNotifyEvent, PersistAtomsProto$PresenceNotifyEvent.class, this.mMaxNumPresenceNotifyEventStats);
            from.gbaEvent = (PersistAtomsProto$GbaEvent[]) sanitizeAtoms(from.gbaEvent, PersistAtomsProto$GbaEvent.class, this.mMaxNumGbaEventStats);
            from.unmeteredNetworks = (PersistAtomsProto$UnmeteredNetworks[]) sanitizeAtoms(from.unmeteredNetworks, PersistAtomsProto$UnmeteredNetworks.class);
            from.outgoingShortCodeSms = (PersistAtomsProto$OutgoingShortCodeSms[]) sanitizeAtoms(from.outgoingShortCodeSms, PersistAtomsProto$OutgoingShortCodeSms.class, this.mMaxOutgoingShortCodeSms);
            from.satelliteController = (PersistAtomsProto$SatelliteController[]) sanitizeAtoms(from.satelliteController, PersistAtomsProto$SatelliteController.class, this.mMaxNumSatelliteStats);
            from.satelliteSession = (PersistAtomsProto$SatelliteSession[]) sanitizeAtoms(from.satelliteSession, PersistAtomsProto$SatelliteSession.class, this.mMaxNumSatelliteStats);
            from.satelliteIncomingDatagram = (PersistAtomsProto$SatelliteIncomingDatagram[]) sanitizeAtoms(from.satelliteIncomingDatagram, PersistAtomsProto$SatelliteIncomingDatagram.class, this.mMaxNumSatelliteStats);
            from.satelliteOutgoingDatagram = (PersistAtomsProto$SatelliteOutgoingDatagram[]) sanitizeAtoms(from.satelliteOutgoingDatagram, PersistAtomsProto$SatelliteOutgoingDatagram.class, this.mMaxNumSatelliteStats);
            from.satelliteProvision = (PersistAtomsProto$SatelliteProvision[]) sanitizeAtoms(from.satelliteProvision, PersistAtomsProto$SatelliteProvision.class, this.mMaxNumSatelliteStats);
            from.satelliteSosMessageRecommender = (PersistAtomsProto$SatelliteSosMessageRecommender[]) sanitizeAtoms(from.satelliteSosMessageRecommender, PersistAtomsProto$SatelliteSosMessageRecommender.class, this.mMaxNumSatelliteStats);
            from.dataNetworkValidation = (PersistAtomsProto$DataNetworkValidation[]) sanitizeAtoms(from.dataNetworkValidation, PersistAtomsProto$DataNetworkValidation.class, this.mMaxNumDataNetworkValidation);
            from.carrierRoamingSatelliteSession = (PersistAtomsProto$CarrierRoamingSatelliteSession[]) sanitizeAtoms(from.carrierRoamingSatelliteSession, PersistAtomsProto$CarrierRoamingSatelliteSession.class, this.mMaxNumSatelliteStats);
            from.carrierRoamingSatelliteControllerStats = (PersistAtomsProto$CarrierRoamingSatelliteControllerStats[]) sanitizeAtoms(from.carrierRoamingSatelliteControllerStats, PersistAtomsProto$CarrierRoamingSatelliteControllerStats.class, this.mMaxNumSatelliteStats);
            from.satelliteEntitlement = (PersistAtomsProto$SatelliteEntitlement[]) sanitizeAtoms(from.satelliteEntitlement, PersistAtomsProto$SatelliteEntitlement.class, this.mMaxNumSatelliteStats);
            from.satelliteConfigUpdater = (PersistAtomsProto$SatelliteConfigUpdater[]) sanitizeAtoms(from.satelliteConfigUpdater, PersistAtomsProto$SatelliteConfigUpdater.class, this.mMaxNumSatelliteStats);
            from.satelliteAccessController = (PersistAtomsProto$SatelliteAccessController[]) sanitizeAtoms(from.satelliteAccessController, PersistAtomsProto$SatelliteAccessController.class, this.mMaxNumSatelliteStats);
            from.voiceCallRatUsagePullTimestampMillis = sanitizeTimestamp(from.voiceCallRatUsagePullTimestampMillis);
            from.voiceCallSessionPullTimestampMillis = sanitizeTimestamp(from.voiceCallSessionPullTimestampMillis);
            from.incomingSmsPullTimestampMillis = sanitizeTimestamp(from.incomingSmsPullTimestampMillis);
            from.outgoingSmsPullTimestampMillis = sanitizeTimestamp(from.outgoingSmsPullTimestampMillis);
            from.dataCallSessionPullTimestampMillis = sanitizeTimestamp(from.dataCallSessionPullTimestampMillis);
            from.cellularServiceStatePullTimestampMillis = sanitizeTimestamp(from.cellularServiceStatePullTimestampMillis);
            from.cellularDataServiceSwitchPullTimestampMillis = sanitizeTimestamp(from.cellularDataServiceSwitchPullTimestampMillis);
            from.imsRegistrationStatsPullTimestampMillis = sanitizeTimestamp(from.imsRegistrationStatsPullTimestampMillis);
            from.imsRegistrationTerminationPullTimestampMillis = sanitizeTimestamp(from.imsRegistrationTerminationPullTimestampMillis);
            from.networkRequestsV2PullTimestampMillis = sanitizeTimestamp(from.networkRequestsV2PullTimestampMillis);
            from.imsRegistrationFeatureTagStatsPullTimestampMillis = sanitizeTimestamp(from.imsRegistrationFeatureTagStatsPullTimestampMillis);
            from.rcsClientProvisioningStatsPullTimestampMillis = sanitizeTimestamp(from.rcsClientProvisioningStatsPullTimestampMillis);
            from.rcsAcsProvisioningStatsPullTimestampMillis = sanitizeTimestamp(from.rcsAcsProvisioningStatsPullTimestampMillis);
            from.sipDelegateStatsPullTimestampMillis = sanitizeTimestamp(from.sipDelegateStatsPullTimestampMillis);
            from.sipTransportFeatureTagStatsPullTimestampMillis = sanitizeTimestamp(from.sipTransportFeatureTagStatsPullTimestampMillis);
            from.sipMessageResponsePullTimestampMillis = sanitizeTimestamp(from.sipMessageResponsePullTimestampMillis);
            from.sipTransportSessionPullTimestampMillis = sanitizeTimestamp(from.sipTransportSessionPullTimestampMillis);
            from.imsDedicatedBearerListenerEventPullTimestampMillis = sanitizeTimestamp(from.imsDedicatedBearerListenerEventPullTimestampMillis);
            from.imsDedicatedBearerEventPullTimestampMillis = sanitizeTimestamp(from.imsDedicatedBearerEventPullTimestampMillis);
            from.imsRegistrationServiceDescStatsPullTimestampMillis = sanitizeTimestamp(from.imsRegistrationServiceDescStatsPullTimestampMillis);
            from.uceEventStatsPullTimestampMillis = sanitizeTimestamp(from.uceEventStatsPullTimestampMillis);
            from.presenceNotifyEventPullTimestampMillis = sanitizeTimestamp(from.presenceNotifyEventPullTimestampMillis);
            from.gbaEventPullTimestampMillis = sanitizeTimestamp(from.gbaEventPullTimestampMillis);
            from.outgoingShortCodeSmsPullTimestampMillis = sanitizeTimestamp(from.outgoingShortCodeSmsPullTimestampMillis);
            from.satelliteControllerPullTimestampMillis = sanitizeTimestamp(from.satelliteControllerPullTimestampMillis);
            from.satelliteSessionPullTimestampMillis = sanitizeTimestamp(from.satelliteSessionPullTimestampMillis);
            from.satelliteIncomingDatagramPullTimestampMillis = sanitizeTimestamp(from.satelliteIncomingDatagramPullTimestampMillis);
            from.satelliteOutgoingDatagramPullTimestampMillis = sanitizeTimestamp(from.satelliteOutgoingDatagramPullTimestampMillis);
            from.satelliteProvisionPullTimestampMillis = sanitizeTimestamp(from.satelliteProvisionPullTimestampMillis);
            from.satelliteSosMessageRecommenderPullTimestampMillis = sanitizeTimestamp(from.satelliteSosMessageRecommenderPullTimestampMillis);
            from.dataNetworkValidationPullTimestampMillis = sanitizeTimestamp(from.dataNetworkValidationPullTimestampMillis);
            from.carrierRoamingSatelliteSessionPullTimestampMillis = sanitizeTimestamp(from.carrierRoamingSatelliteSessionPullTimestampMillis);
            from.carrierRoamingSatelliteControllerStatsPullTimestampMillis = sanitizeTimestamp(from.carrierRoamingSatelliteControllerStatsPullTimestampMillis);
            from.satelliteEntitlementPullTimestampMillis = sanitizeTimestamp(from.satelliteEntitlementPullTimestampMillis);
            from.satelliteConfigUpdaterPullTimestampMillis = sanitizeTimestamp(from.satelliteConfigUpdaterPullTimestampMillis);
            from.satelliteAccessControllerPullTimestampMillis = sanitizeTimestamp(from.satelliteAccessControllerPullTimestampMillis);
            return from;
        } catch (NoSuchFileException unused) {
            Rlog.d(TAG, "PersistAtoms file not found");
            return makeNewPersistAtoms();
        } catch (IOException | NullPointerException e) {
            Rlog.e(TAG, "cannot load/parse PersistAtoms", e);
            return makeNewPersistAtoms();
        }
    }

    private synchronized void saveAtomsToFile(int i) {
        this.mHandler.removeCallbacks(this.mSaveRunnable);
        if (i <= 0 || this.mSaveImmediately || !this.mHandler.postDelayed(this.mSaveRunnable, i)) {
            saveAtomsToFileNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void saveAtomsToFileNow() {
        try {
            FileOutputStream fileOutputStreamOpenFileOutput = this.mContext.openFileOutput("persist_atoms.pb", 0);
            try {
                fileOutputStreamOpenFileOutput.write(MessageNano.toByteArray(this.mAtoms));
                fileOutputStreamOpenFileOutput.close();
            } catch (Throwable th) {
                if (fileOutputStreamOpenFileOutput != null) {
                    try {
                        fileOutputStreamOpenFileOutput.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            Rlog.e(TAG, "cannot save PersistAtoms", e);
        }
    }

    private PersistAtomsProto$CellularServiceState find(PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState) {
        for (PersistAtomsProto$CellularServiceState persistAtomsProto$CellularServiceState2 : this.mAtoms.cellularServiceState) {
            if (persistAtomsProto$CellularServiceState2.voiceRat == persistAtomsProto$CellularServiceState.voiceRat && persistAtomsProto$CellularServiceState2.dataRat == persistAtomsProto$CellularServiceState.dataRat && persistAtomsProto$CellularServiceState2.voiceRoamingType == persistAtomsProto$CellularServiceState.voiceRoamingType && persistAtomsProto$CellularServiceState2.dataRoamingType == persistAtomsProto$CellularServiceState.dataRoamingType && persistAtomsProto$CellularServiceState2.isEndc == persistAtomsProto$CellularServiceState.isEndc && persistAtomsProto$CellularServiceState2.simSlotIndex == persistAtomsProto$CellularServiceState.simSlotIndex && persistAtomsProto$CellularServiceState2.isMultiSim == persistAtomsProto$CellularServiceState.isMultiSim && persistAtomsProto$CellularServiceState2.carrierId == persistAtomsProto$CellularServiceState.carrierId && persistAtomsProto$CellularServiceState2.isEmergencyOnly == persistAtomsProto$CellularServiceState.isEmergencyOnly && persistAtomsProto$CellularServiceState2.isInternetPdnUp == persistAtomsProto$CellularServiceState.isInternetPdnUp && persistAtomsProto$CellularServiceState2.foldState == persistAtomsProto$CellularServiceState.foldState && persistAtomsProto$CellularServiceState2.overrideVoiceService == persistAtomsProto$CellularServiceState.overrideVoiceService && persistAtomsProto$CellularServiceState2.isDataEnabled == persistAtomsProto$CellularServiceState.isDataEnabled && persistAtomsProto$CellularServiceState2.isIwlanCrossSim == persistAtomsProto$CellularServiceState.isIwlanCrossSim && persistAtomsProto$CellularServiceState2.isNtn == persistAtomsProto$CellularServiceState.isNtn && persistAtomsProto$CellularServiceState2.isNbIotNtn == persistAtomsProto$CellularServiceState.isNbIotNtn && persistAtomsProto$CellularServiceState2.isOpportunistic == persistAtomsProto$CellularServiceState.isOpportunistic && Objects.equals(persistAtomsProto$CellularServiceState2.plmn, persistAtomsProto$CellularServiceState.plmn)) {
                return persistAtomsProto$CellularServiceState2;
            }
        }
        return null;
    }

    private PersistAtomsProto$CellularDataServiceSwitch find(PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch) {
        for (PersistAtomsProto$CellularDataServiceSwitch persistAtomsProto$CellularDataServiceSwitch2 : this.mAtoms.cellularDataServiceSwitch) {
            if (persistAtomsProto$CellularDataServiceSwitch2.ratFrom == persistAtomsProto$CellularDataServiceSwitch.ratFrom && persistAtomsProto$CellularDataServiceSwitch2.ratTo == persistAtomsProto$CellularDataServiceSwitch.ratTo && persistAtomsProto$CellularDataServiceSwitch2.simSlotIndex == persistAtomsProto$CellularDataServiceSwitch.simSlotIndex && persistAtomsProto$CellularDataServiceSwitch2.isMultiSim == persistAtomsProto$CellularDataServiceSwitch.isMultiSim && persistAtomsProto$CellularDataServiceSwitch2.carrierId == persistAtomsProto$CellularDataServiceSwitch.carrierId && persistAtomsProto$CellularDataServiceSwitch2.isOpportunistic == persistAtomsProto$CellularDataServiceSwitch.isOpportunistic) {
                return persistAtomsProto$CellularDataServiceSwitch2;
            }
        }
        return null;
    }

    private PersistAtomsProto$CarrierIdMismatch find(PersistAtomsProto$CarrierIdMismatch persistAtomsProto$CarrierIdMismatch) {
        for (PersistAtomsProto$CarrierIdMismatch persistAtomsProto$CarrierIdMismatch2 : this.mAtoms.carrierIdMismatch) {
            if (persistAtomsProto$CarrierIdMismatch2.mccMnc.equals(persistAtomsProto$CarrierIdMismatch.mccMnc) && persistAtomsProto$CarrierIdMismatch2.gid1.equals(persistAtomsProto$CarrierIdMismatch.gid1) && persistAtomsProto$CarrierIdMismatch2.spn.equals(persistAtomsProto$CarrierIdMismatch.spn) && persistAtomsProto$CarrierIdMismatch2.pnn.equals(persistAtomsProto$CarrierIdMismatch.pnn)) {
                return persistAtomsProto$CarrierIdMismatch2;
            }
        }
        return null;
    }

    private PersistAtomsProto$ImsRegistrationStats find(PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats) {
        for (PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats2 : this.mAtoms.imsRegistrationStats) {
            if (persistAtomsProto$ImsRegistrationStats2.carrierId == persistAtomsProto$ImsRegistrationStats.carrierId && persistAtomsProto$ImsRegistrationStats2.simSlotIndex == persistAtomsProto$ImsRegistrationStats.simSlotIndex && persistAtomsProto$ImsRegistrationStats2.rat == persistAtomsProto$ImsRegistrationStats.rat && persistAtomsProto$ImsRegistrationStats2.isIwlanCrossSim == persistAtomsProto$ImsRegistrationStats.isIwlanCrossSim) {
                return persistAtomsProto$ImsRegistrationStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$ImsRegistrationTermination find(PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTermination) {
        for (PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTermination2 : this.mAtoms.imsRegistrationTermination) {
            if (persistAtomsProto$ImsRegistrationTermination2.carrierId == persistAtomsProto$ImsRegistrationTermination.carrierId && persistAtomsProto$ImsRegistrationTermination2.isMultiSim == persistAtomsProto$ImsRegistrationTermination.isMultiSim && persistAtomsProto$ImsRegistrationTermination2.ratAtEnd == persistAtomsProto$ImsRegistrationTermination.ratAtEnd && persistAtomsProto$ImsRegistrationTermination2.isIwlanCrossSim == persistAtomsProto$ImsRegistrationTermination.isIwlanCrossSim && persistAtomsProto$ImsRegistrationTermination2.setupFailed == persistAtomsProto$ImsRegistrationTermination.setupFailed && persistAtomsProto$ImsRegistrationTermination2.reasonCode == persistAtomsProto$ImsRegistrationTermination.reasonCode && persistAtomsProto$ImsRegistrationTermination2.extraCode == persistAtomsProto$ImsRegistrationTermination.extraCode && persistAtomsProto$ImsRegistrationTermination2.extraMessage.equals(persistAtomsProto$ImsRegistrationTermination.extraMessage)) {
                return persistAtomsProto$ImsRegistrationTermination2;
            }
        }
        return null;
    }

    private PersistAtomsProto$NetworkRequestsV2 find(PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV2) {
        for (PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV3 : this.mAtoms.networkRequestsV2) {
            if (persistAtomsProto$NetworkRequestsV3.carrierId == persistAtomsProto$NetworkRequestsV2.carrierId && persistAtomsProto$NetworkRequestsV3.capability == persistAtomsProto$NetworkRequestsV2.capability) {
                return persistAtomsProto$NetworkRequestsV3;
            }
        }
        return null;
    }

    private int findIndex(PersistAtomsProto$DataCallSession persistAtomsProto$DataCallSession) {
        int i = 0;
        while (true) {
            PersistAtomsProto$DataCallSession[] persistAtomsProto$DataCallSessionArr = this.mAtoms.dataCallSession;
            if (i >= persistAtomsProto$DataCallSessionArr.length) {
                return -1;
            }
            if (persistAtomsProto$DataCallSessionArr[i].dimension == persistAtomsProto$DataCallSession.dimension) {
                return i;
            }
            i++;
        }
    }

    private PersistAtomsProto$ImsDedicatedBearerListenerEvent find(PersistAtomsProto$ImsDedicatedBearerListenerEvent persistAtomsProto$ImsDedicatedBearerListenerEvent) {
        for (PersistAtomsProto$ImsDedicatedBearerListenerEvent persistAtomsProto$ImsDedicatedBearerListenerEvent2 : this.mAtoms.imsDedicatedBearerListenerEvent) {
            if (persistAtomsProto$ImsDedicatedBearerListenerEvent2.carrierId == persistAtomsProto$ImsDedicatedBearerListenerEvent.carrierId && persistAtomsProto$ImsDedicatedBearerListenerEvent2.slotId == persistAtomsProto$ImsDedicatedBearerListenerEvent.slotId && persistAtomsProto$ImsDedicatedBearerListenerEvent2.ratAtEnd == persistAtomsProto$ImsDedicatedBearerListenerEvent.ratAtEnd && persistAtomsProto$ImsDedicatedBearerListenerEvent2.qci == persistAtomsProto$ImsDedicatedBearerListenerEvent.qci && persistAtomsProto$ImsDedicatedBearerListenerEvent2.dedicatedBearerEstablished == persistAtomsProto$ImsDedicatedBearerListenerEvent.dedicatedBearerEstablished) {
                return persistAtomsProto$ImsDedicatedBearerListenerEvent2;
            }
        }
        return null;
    }

    private PersistAtomsProto$ImsDedicatedBearerEvent find(PersistAtomsProto$ImsDedicatedBearerEvent persistAtomsProto$ImsDedicatedBearerEvent) {
        for (PersistAtomsProto$ImsDedicatedBearerEvent persistAtomsProto$ImsDedicatedBearerEvent2 : this.mAtoms.imsDedicatedBearerEvent) {
            if (persistAtomsProto$ImsDedicatedBearerEvent2.carrierId == persistAtomsProto$ImsDedicatedBearerEvent.carrierId && persistAtomsProto$ImsDedicatedBearerEvent2.slotId == persistAtomsProto$ImsDedicatedBearerEvent.slotId && persistAtomsProto$ImsDedicatedBearerEvent2.ratAtEnd == persistAtomsProto$ImsDedicatedBearerEvent.ratAtEnd && persistAtomsProto$ImsDedicatedBearerEvent2.qci == persistAtomsProto$ImsDedicatedBearerEvent.qci && persistAtomsProto$ImsDedicatedBearerEvent2.bearerState == persistAtomsProto$ImsDedicatedBearerEvent.bearerState && persistAtomsProto$ImsDedicatedBearerEvent2.localConnectionInfoReceived == persistAtomsProto$ImsDedicatedBearerEvent.localConnectionInfoReceived && persistAtomsProto$ImsDedicatedBearerEvent2.remoteConnectionInfoReceived == persistAtomsProto$ImsDedicatedBearerEvent.remoteConnectionInfoReceived && persistAtomsProto$ImsDedicatedBearerEvent2.hasListeners == persistAtomsProto$ImsDedicatedBearerEvent.hasListeners) {
                return persistAtomsProto$ImsDedicatedBearerEvent2;
            }
        }
        return null;
    }

    private PersistAtomsProto$ImsRegistrationFeatureTagStats find(PersistAtomsProto$ImsRegistrationFeatureTagStats persistAtomsProto$ImsRegistrationFeatureTagStats) {
        for (PersistAtomsProto$ImsRegistrationFeatureTagStats persistAtomsProto$ImsRegistrationFeatureTagStats2 : this.mAtoms.imsRegistrationFeatureTagStats) {
            if (persistAtomsProto$ImsRegistrationFeatureTagStats2.carrierId == persistAtomsProto$ImsRegistrationFeatureTagStats.carrierId && persistAtomsProto$ImsRegistrationFeatureTagStats2.slotId == persistAtomsProto$ImsRegistrationFeatureTagStats.slotId && persistAtomsProto$ImsRegistrationFeatureTagStats2.featureTagName == persistAtomsProto$ImsRegistrationFeatureTagStats.featureTagName && persistAtomsProto$ImsRegistrationFeatureTagStats2.registrationTech == persistAtomsProto$ImsRegistrationFeatureTagStats.registrationTech) {
                return persistAtomsProto$ImsRegistrationFeatureTagStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$RcsClientProvisioningStats find(PersistAtomsProto$RcsClientProvisioningStats persistAtomsProto$RcsClientProvisioningStats) {
        for (PersistAtomsProto$RcsClientProvisioningStats persistAtomsProto$RcsClientProvisioningStats2 : this.mAtoms.rcsClientProvisioningStats) {
            if (persistAtomsProto$RcsClientProvisioningStats2.carrierId == persistAtomsProto$RcsClientProvisioningStats.carrierId && persistAtomsProto$RcsClientProvisioningStats2.slotId == persistAtomsProto$RcsClientProvisioningStats.slotId && persistAtomsProto$RcsClientProvisioningStats2.event == persistAtomsProto$RcsClientProvisioningStats.event) {
                return persistAtomsProto$RcsClientProvisioningStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$RcsAcsProvisioningStats find(PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStats) {
        for (PersistAtomsProto$RcsAcsProvisioningStats persistAtomsProto$RcsAcsProvisioningStats2 : this.mAtoms.rcsAcsProvisioningStats) {
            if (persistAtomsProto$RcsAcsProvisioningStats2.carrierId == persistAtomsProto$RcsAcsProvisioningStats.carrierId && persistAtomsProto$RcsAcsProvisioningStats2.slotId == persistAtomsProto$RcsAcsProvisioningStats.slotId && persistAtomsProto$RcsAcsProvisioningStats2.responseCode == persistAtomsProto$RcsAcsProvisioningStats.responseCode && persistAtomsProto$RcsAcsProvisioningStats2.responseType == persistAtomsProto$RcsAcsProvisioningStats.responseType && persistAtomsProto$RcsAcsProvisioningStats2.isSingleRegistrationEnabled == persistAtomsProto$RcsAcsProvisioningStats.isSingleRegistrationEnabled) {
                return persistAtomsProto$RcsAcsProvisioningStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SipMessageResponse find(PersistAtomsProto$SipMessageResponse persistAtomsProto$SipMessageResponse) {
        for (PersistAtomsProto$SipMessageResponse persistAtomsProto$SipMessageResponse2 : this.mAtoms.sipMessageResponse) {
            if (persistAtomsProto$SipMessageResponse2.carrierId == persistAtomsProto$SipMessageResponse.carrierId && persistAtomsProto$SipMessageResponse2.slotId == persistAtomsProto$SipMessageResponse.slotId && persistAtomsProto$SipMessageResponse2.sipMessageMethod == persistAtomsProto$SipMessageResponse.sipMessageMethod && persistAtomsProto$SipMessageResponse2.sipMessageResponse == persistAtomsProto$SipMessageResponse.sipMessageResponse && persistAtomsProto$SipMessageResponse2.sipMessageDirection == persistAtomsProto$SipMessageResponse.sipMessageDirection && persistAtomsProto$SipMessageResponse2.messageError == persistAtomsProto$SipMessageResponse.messageError) {
                return persistAtomsProto$SipMessageResponse2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SipTransportSession find(PersistAtomsProto$SipTransportSession persistAtomsProto$SipTransportSession) {
        for (PersistAtomsProto$SipTransportSession persistAtomsProto$SipTransportSession2 : this.mAtoms.sipTransportSession) {
            if (persistAtomsProto$SipTransportSession2.carrierId == persistAtomsProto$SipTransportSession.carrierId && persistAtomsProto$SipTransportSession2.slotId == persistAtomsProto$SipTransportSession.slotId && persistAtomsProto$SipTransportSession2.sessionMethod == persistAtomsProto$SipTransportSession.sessionMethod && persistAtomsProto$SipTransportSession2.sipMessageDirection == persistAtomsProto$SipTransportSession.sipMessageDirection && persistAtomsProto$SipTransportSession2.sipResponse == persistAtomsProto$SipTransportSession.sipResponse) {
                return persistAtomsProto$SipTransportSession2;
            }
        }
        return null;
    }

    private PersistAtomsProto$ImsRegistrationServiceDescStats find(PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStats) {
        for (PersistAtomsProto$ImsRegistrationServiceDescStats persistAtomsProto$ImsRegistrationServiceDescStats2 : this.mAtoms.imsRegistrationServiceDescStats) {
            if (persistAtomsProto$ImsRegistrationServiceDescStats2.carrierId == persistAtomsProto$ImsRegistrationServiceDescStats.carrierId && persistAtomsProto$ImsRegistrationServiceDescStats2.slotId == persistAtomsProto$ImsRegistrationServiceDescStats.slotId && persistAtomsProto$ImsRegistrationServiceDescStats2.serviceIdName == persistAtomsProto$ImsRegistrationServiceDescStats.serviceIdName && persistAtomsProto$ImsRegistrationServiceDescStats2.serviceIdVersion == persistAtomsProto$ImsRegistrationServiceDescStats.serviceIdVersion && persistAtomsProto$ImsRegistrationServiceDescStats2.registrationTech == persistAtomsProto$ImsRegistrationServiceDescStats.registrationTech) {
                return persistAtomsProto$ImsRegistrationServiceDescStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$UceEventStats find(PersistAtomsProto$UceEventStats persistAtomsProto$UceEventStats) {
        for (PersistAtomsProto$UceEventStats persistAtomsProto$UceEventStats2 : this.mAtoms.uceEventStats) {
            if (persistAtomsProto$UceEventStats2.carrierId == persistAtomsProto$UceEventStats.carrierId && persistAtomsProto$UceEventStats2.slotId == persistAtomsProto$UceEventStats.slotId && persistAtomsProto$UceEventStats2.type == persistAtomsProto$UceEventStats.type && persistAtomsProto$UceEventStats2.successful == persistAtomsProto$UceEventStats.successful && persistAtomsProto$UceEventStats2.commandCode == persistAtomsProto$UceEventStats.commandCode && persistAtomsProto$UceEventStats2.networkResponse == persistAtomsProto$UceEventStats.networkResponse) {
                return persistAtomsProto$UceEventStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$PresenceNotifyEvent find(PersistAtomsProto$PresenceNotifyEvent persistAtomsProto$PresenceNotifyEvent) {
        for (PersistAtomsProto$PresenceNotifyEvent persistAtomsProto$PresenceNotifyEvent2 : this.mAtoms.presenceNotifyEvent) {
            if (persistAtomsProto$PresenceNotifyEvent2.carrierId == persistAtomsProto$PresenceNotifyEvent.carrierId && persistAtomsProto$PresenceNotifyEvent2.slotId == persistAtomsProto$PresenceNotifyEvent.slotId && persistAtomsProto$PresenceNotifyEvent2.reason == persistAtomsProto$PresenceNotifyEvent.reason && persistAtomsProto$PresenceNotifyEvent2.contentBodyReceived == persistAtomsProto$PresenceNotifyEvent.contentBodyReceived) {
                return persistAtomsProto$PresenceNotifyEvent2;
            }
        }
        return null;
    }

    private PersistAtomsProto$GbaEvent find(PersistAtomsProto$GbaEvent persistAtomsProto$GbaEvent) {
        for (PersistAtomsProto$GbaEvent persistAtomsProto$GbaEvent2 : this.mAtoms.gbaEvent) {
            if (persistAtomsProto$GbaEvent2.carrierId == persistAtomsProto$GbaEvent.carrierId && persistAtomsProto$GbaEvent2.slotId == persistAtomsProto$GbaEvent.slotId && persistAtomsProto$GbaEvent2.successful == persistAtomsProto$GbaEvent.successful && persistAtomsProto$GbaEvent2.failedReason == persistAtomsProto$GbaEvent.failedReason) {
                return persistAtomsProto$GbaEvent2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SipTransportFeatureTagStats find(PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStats) {
        for (PersistAtomsProto$SipTransportFeatureTagStats persistAtomsProto$SipTransportFeatureTagStats2 : this.mAtoms.sipTransportFeatureTagStats) {
            if (persistAtomsProto$SipTransportFeatureTagStats2.carrierId == persistAtomsProto$SipTransportFeatureTagStats.carrierId && persistAtomsProto$SipTransportFeatureTagStats2.slotId == persistAtomsProto$SipTransportFeatureTagStats.slotId && persistAtomsProto$SipTransportFeatureTagStats2.featureTagName == persistAtomsProto$SipTransportFeatureTagStats.featureTagName && persistAtomsProto$SipTransportFeatureTagStats2.sipTransportDeregisteredReason == persistAtomsProto$SipTransportFeatureTagStats.sipTransportDeregisteredReason && persistAtomsProto$SipTransportFeatureTagStats2.sipTransportDeniedReason == persistAtomsProto$SipTransportFeatureTagStats.sipTransportDeniedReason) {
                return persistAtomsProto$SipTransportFeatureTagStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$UnmeteredNetworks findUnmeteredNetworks(int i) {
        for (PersistAtomsProto$UnmeteredNetworks persistAtomsProto$UnmeteredNetworks : this.mAtoms.unmeteredNetworks) {
            if (persistAtomsProto$UnmeteredNetworks.phoneId == i) {
                return persistAtomsProto$UnmeteredNetworks;
            }
        }
        return null;
    }

    private PersistAtomsProto$OutgoingShortCodeSms find(PersistAtomsProto$OutgoingShortCodeSms persistAtomsProto$OutgoingShortCodeSms) {
        for (PersistAtomsProto$OutgoingShortCodeSms persistAtomsProto$OutgoingShortCodeSms2 : this.mAtoms.outgoingShortCodeSms) {
            if (persistAtomsProto$OutgoingShortCodeSms2.category == persistAtomsProto$OutgoingShortCodeSms.category && persistAtomsProto$OutgoingShortCodeSms2.xmlVersion == persistAtomsProto$OutgoingShortCodeSms.xmlVersion) {
                return persistAtomsProto$OutgoingShortCodeSms2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SatelliteSession find(PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSession) {
        for (PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSession2 : this.mAtoms.satelliteSession) {
            if (persistAtomsProto$SatelliteSession2.satelliteServiceInitializationResult == persistAtomsProto$SatelliteSession.satelliteServiceInitializationResult && persistAtomsProto$SatelliteSession2.satelliteTechnology == persistAtomsProto$SatelliteSession.satelliteTechnology && persistAtomsProto$SatelliteSession2.satelliteServiceTerminationResult == persistAtomsProto$SatelliteSession.satelliteServiceTerminationResult && persistAtomsProto$SatelliteSession2.initializationProcessingTimeMillis == persistAtomsProto$SatelliteSession.initializationProcessingTimeMillis && persistAtomsProto$SatelliteSession2.terminationProcessingTimeMillis == persistAtomsProto$SatelliteSession.terminationProcessingTimeMillis && persistAtomsProto$SatelliteSession2.sessionDurationSeconds == persistAtomsProto$SatelliteSession.sessionDurationSeconds && persistAtomsProto$SatelliteSession2.countOfOutgoingDatagramSuccess == persistAtomsProto$SatelliteSession.countOfOutgoingDatagramSuccess && persistAtomsProto$SatelliteSession2.countOfOutgoingDatagramFailed == persistAtomsProto$SatelliteSession.countOfOutgoingDatagramFailed && persistAtomsProto$SatelliteSession2.countOfIncomingDatagramSuccess == persistAtomsProto$SatelliteSession.countOfIncomingDatagramSuccess && persistAtomsProto$SatelliteSession2.countOfIncomingDatagramFailed == persistAtomsProto$SatelliteSession.countOfIncomingDatagramFailed && persistAtomsProto$SatelliteSession2.isDemoMode == persistAtomsProto$SatelliteSession.isDemoMode && persistAtomsProto$SatelliteSession2.maxNtnSignalStrengthLevel == persistAtomsProto$SatelliteSession.maxNtnSignalStrengthLevel && persistAtomsProto$SatelliteSession2.carrierId == persistAtomsProto$SatelliteSession.carrierId && persistAtomsProto$SatelliteSession2.countOfSatelliteNotificationDisplayed == persistAtomsProto$SatelliteSession.countOfSatelliteNotificationDisplayed && persistAtomsProto$SatelliteSession2.countOfAutoExitDueToScreenOff == persistAtomsProto$SatelliteSession.countOfAutoExitDueToScreenOff && persistAtomsProto$SatelliteSession2.countOfAutoExitDueToTnNetwork == persistAtomsProto$SatelliteSession.countOfAutoExitDueToTnNetwork && persistAtomsProto$SatelliteSession2.isEmergency == persistAtomsProto$SatelliteSession.isEmergency && persistAtomsProto$SatelliteSession2.maxInactivityDurationSec == persistAtomsProto$SatelliteSession.maxInactivityDurationSec && persistAtomsProto$SatelliteSession2.supportedConnectionMode == persistAtomsProto$SatelliteSession.supportedConnectionMode && persistAtomsProto$SatelliteSession2.sessionConnectionMode == persistAtomsProto$SatelliteSession.sessionConnectionMode && Objects.equals(persistAtomsProto$SatelliteSession2.plmn, persistAtomsProto$SatelliteSession.plmn)) {
                return persistAtomsProto$SatelliteSession2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SatelliteSosMessageRecommender find(PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommender) {
        for (PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommender2 : this.mAtoms.satelliteSosMessageRecommender) {
            if (persistAtomsProto$SatelliteSosMessageRecommender2.isDisplaySosMessageSent == persistAtomsProto$SatelliteSosMessageRecommender.isDisplaySosMessageSent && persistAtomsProto$SatelliteSosMessageRecommender2.countOfTimerStarted == persistAtomsProto$SatelliteSosMessageRecommender.countOfTimerStarted && persistAtomsProto$SatelliteSosMessageRecommender2.isImsRegistered == persistAtomsProto$SatelliteSosMessageRecommender.isImsRegistered && persistAtomsProto$SatelliteSosMessageRecommender2.cellularServiceState == persistAtomsProto$SatelliteSosMessageRecommender.cellularServiceState && persistAtomsProto$SatelliteSosMessageRecommender2.isMultiSim == persistAtomsProto$SatelliteSosMessageRecommender.isMultiSim && persistAtomsProto$SatelliteSosMessageRecommender2.recommendingHandoverType == persistAtomsProto$SatelliteSosMessageRecommender.recommendingHandoverType && persistAtomsProto$SatelliteSosMessageRecommender2.isSatelliteAllowedInCurrentLocation == persistAtomsProto$SatelliteSosMessageRecommender.isSatelliteAllowedInCurrentLocation && persistAtomsProto$SatelliteSosMessageRecommender2.isWifiConnected == persistAtomsProto$SatelliteSosMessageRecommender.isWifiConnected && persistAtomsProto$SatelliteSosMessageRecommender2.carrierId == persistAtomsProto$SatelliteSosMessageRecommender.carrierId && persistAtomsProto$SatelliteSosMessageRecommender2.supportedConnectionMode == persistAtomsProto$SatelliteSosMessageRecommender.supportedConnectionMode && persistAtomsProto$SatelliteSosMessageRecommender2.sessionConnectionMode == persistAtomsProto$SatelliteSosMessageRecommender.sessionConnectionMode && Objects.equals(persistAtomsProto$SatelliteSosMessageRecommender2.plmn, persistAtomsProto$SatelliteSosMessageRecommender.plmn) && persistAtomsProto$SatelliteSosMessageRecommender2.isInCarrierRoamingNtnMode == persistAtomsProto$SatelliteSosMessageRecommender.isInCarrierRoamingNtnMode && persistAtomsProto$SatelliteSosMessageRecommender2.carrierRoamingSatelliteEmergencyMessagingProvider == persistAtomsProto$SatelliteSosMessageRecommender.carrierRoamingSatelliteEmergencyMessagingProvider && persistAtomsProto$SatelliteSosMessageRecommender2.emergencyNumberSourceUsedInHandoverIntent == persistAtomsProto$SatelliteSosMessageRecommender.emergencyNumberSourceUsedInHandoverIntent) {
                return persistAtomsProto$SatelliteSosMessageRecommender2;
            }
        }
        return null;
    }

    private PersistAtomsProto$DataNetworkValidation find(PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation) {
        for (PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation2 : this.mAtoms.dataNetworkValidation) {
            if (persistAtomsProto$DataNetworkValidation2.networkType == persistAtomsProto$DataNetworkValidation.networkType && persistAtomsProto$DataNetworkValidation2.apnTypeBitmask == persistAtomsProto$DataNetworkValidation.apnTypeBitmask && persistAtomsProto$DataNetworkValidation2.signalStrength == persistAtomsProto$DataNetworkValidation.signalStrength && persistAtomsProto$DataNetworkValidation2.validationResult == persistAtomsProto$DataNetworkValidation.validationResult && persistAtomsProto$DataNetworkValidation2.handoverAttempted == persistAtomsProto$DataNetworkValidation.handoverAttempted) {
                return persistAtomsProto$DataNetworkValidation2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SatelliteController find(PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteController) {
        for (PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteController2 : this.mAtoms.satelliteController) {
            if (persistAtomsProto$SatelliteController2.carrierId == persistAtomsProto$SatelliteController.carrierId && persistAtomsProto$SatelliteController2.supportedConnectionMode == persistAtomsProto$SatelliteController.supportedConnectionMode) {
                return persistAtomsProto$SatelliteController2;
            }
        }
        return null;
    }

    private PersistAtomsProto$CarrierRoamingSatelliteControllerStats find(PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStats) {
        for (PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStats2 : this.mAtoms.carrierRoamingSatelliteControllerStats) {
            if (persistAtomsProto$CarrierRoamingSatelliteControllerStats2.carrierId == persistAtomsProto$CarrierRoamingSatelliteControllerStats.carrierId && persistAtomsProto$CarrierRoamingSatelliteControllerStats2.supportedConnectionMode == persistAtomsProto$CarrierRoamingSatelliteControllerStats.supportedConnectionMode) {
                return persistAtomsProto$CarrierRoamingSatelliteControllerStats2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SatelliteEntitlement find(PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlement) {
        for (PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlement2 : this.mAtoms.satelliteEntitlement) {
            if (persistAtomsProto$SatelliteEntitlement2.carrierId == persistAtomsProto$SatelliteEntitlement.carrierId && persistAtomsProto$SatelliteEntitlement2.result == persistAtomsProto$SatelliteEntitlement.result && persistAtomsProto$SatelliteEntitlement2.entitlementStatus == persistAtomsProto$SatelliteEntitlement.entitlementStatus && persistAtomsProto$SatelliteEntitlement2.isRetry == persistAtomsProto$SatelliteEntitlement.isRetry && persistAtomsProto$SatelliteEntitlement2.supportedConnectionMode == persistAtomsProto$SatelliteEntitlement.supportedConnectionMode) {
                return persistAtomsProto$SatelliteEntitlement2;
            }
        }
        return null;
    }

    private PersistAtomsProto$SatelliteConfigUpdater find(PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdater) {
        for (PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdater2 : this.mAtoms.satelliteConfigUpdater) {
            if (persistAtomsProto$SatelliteConfigUpdater2.configVersion == persistAtomsProto$SatelliteConfigUpdater.configVersion && persistAtomsProto$SatelliteConfigUpdater2.oemConfigResult == persistAtomsProto$SatelliteConfigUpdater.oemConfigResult && persistAtomsProto$SatelliteConfigUpdater2.carrierConfigResult == persistAtomsProto$SatelliteConfigUpdater.carrierConfigResult) {
                return persistAtomsProto$SatelliteConfigUpdater2;
            }
        }
        return null;
    }

    private static Object[] insertAtRandomPlace(Object[] objArr, Object obj, int i) {
        int length = objArr.length;
        int i2 = length + 1;
        boolean z = i2 > i;
        if (!z) {
            i = i2;
        }
        Object[] objArrCopyOf = Arrays.copyOf(objArr, i);
        if (i2 == 1) {
            objArrCopyOf[0] = obj;
            return objArrCopyOf;
        }
        if (z) {
            if ((obj instanceof PersistAtomsProto$OutgoingSms) || (obj instanceof PersistAtomsProto$IncomingSms)) {
                mergeSmsOrEvictInFullStorage(objArrCopyOf, obj);
                return objArrCopyOf;
            }
            objArrCopyOf[findItemToEvict(objArr)] = obj;
            return objArrCopyOf;
        }
        int iNextInt = sRandom.nextInt(i2);
        objArrCopyOf[length] = objArrCopyOf[iNextInt];
        objArrCopyOf[iNextInt] = obj;
        return objArrCopyOf;
    }

    private static void mergeSmsOrEvictInFullStorage(Object[] objArr, Object obj) {
        new SparseIntArray();
        int i = -1;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < objArr.length; i3++) {
            if (areSmsMergeable(objArr[i3], obj)) {
                objArr[i3] = mergeSms(objArr[i3], obj);
                return;
            }
            int smsCount = getSmsCount(objArr[i3]);
            if (smsCount < i2) {
                i = i3;
                i2 = smsCount;
            }
        }
        objArr[i] = obj;
    }

    private static int getSmsHashCode(Object obj) {
        return obj instanceof PersistAtomsProto$OutgoingSms ? ((PersistAtomsProto$OutgoingSms) obj).hashCode : ((PersistAtomsProto$IncomingSms) obj).hashCode;
    }

    private static int getSmsCount(Object obj) {
        return obj instanceof PersistAtomsProto$OutgoingSms ? ((PersistAtomsProto$OutgoingSms) obj).count : ((PersistAtomsProto$IncomingSms) obj).count;
    }

    private static boolean areSmsMergeable(Object obj, Object obj2) {
        return getSmsHashCode(obj) == getSmsHashCode(obj2);
    }

    private static Object mergeSms(Object obj, Object obj2) {
        if (obj instanceof PersistAtomsProto$OutgoingSms) {
            PersistAtomsProto$OutgoingSms persistAtomsProto$OutgoingSms = (PersistAtomsProto$OutgoingSms) obj;
            PersistAtomsProto$OutgoingSms persistAtomsProto$OutgoingSms2 = (PersistAtomsProto$OutgoingSms) obj2;
            long j = persistAtomsProto$OutgoingSms.intervalMillis;
            int i = persistAtomsProto$OutgoingSms.count;
            long j2 = persistAtomsProto$OutgoingSms2.intervalMillis;
            int i2 = persistAtomsProto$OutgoingSms2.count;
            persistAtomsProto$OutgoingSms.intervalMillis = ((j * ((long) i)) + (j2 * ((long) i2))) / ((long) (i + i2));
            persistAtomsProto$OutgoingSms.count = i + i2;
            return obj;
        }
        if (obj instanceof PersistAtomsProto$IncomingSms) {
            ((PersistAtomsProto$IncomingSms) obj).count += ((PersistAtomsProto$IncomingSms) obj2).count;
        }
        return obj;
    }

    private static int findItemToEvict(Object[] objArr) {
        if (objArr instanceof PersistAtomsProto$CellularServiceState[]) {
            final PersistAtomsProto$CellularServiceState[] persistAtomsProto$CellularServiceStateArr = (PersistAtomsProto$CellularServiceState[]) objArr;
            return IntStream.range(0, persistAtomsProto$CellularServiceStateArr.length).reduce(new IntBinaryOperator() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda5
                @Override // java.util.function.IntBinaryOperator
                public final int applyAsInt(int i, int i2) {
                    return PersistAtomsStorage.m1228$r8$lambda$tX3s3JO_u7sEg1yo9Ywxz1wZo(persistAtomsProto$CellularServiceStateArr, i, i2);
                }
            }).getAsInt();
        }
        if (objArr instanceof PersistAtomsProto$CellularDataServiceSwitch[]) {
            final PersistAtomsProto$CellularDataServiceSwitch[] persistAtomsProto$CellularDataServiceSwitchArr = (PersistAtomsProto$CellularDataServiceSwitch[]) objArr;
            return IntStream.range(0, persistAtomsProto$CellularDataServiceSwitchArr.length).reduce(new IntBinaryOperator() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda6
                @Override // java.util.function.IntBinaryOperator
                public final int applyAsInt(int i, int i2) {
                    return PersistAtomsStorage.m1227$r8$lambda$pRGNNWMzTeqPKNaLzuM_BfkWjM(persistAtomsProto$CellularDataServiceSwitchArr, i, i2);
                }
            }).getAsInt();
        }
        if (objArr instanceof PersistAtomsProto$ImsRegistrationStats[]) {
            final PersistAtomsProto$ImsRegistrationStats[] persistAtomsProto$ImsRegistrationStatsArr = (PersistAtomsProto$ImsRegistrationStats[]) objArr;
            return IntStream.range(0, persistAtomsProto$ImsRegistrationStatsArr.length).reduce(new IntBinaryOperator() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda7
                @Override // java.util.function.IntBinaryOperator
                public final int applyAsInt(int i, int i2) {
                    return PersistAtomsStorage.m1224$r8$lambda$3s0XitPvI3I7GRymVaD3v6fh28(persistAtomsProto$ImsRegistrationStatsArr, i, i2);
                }
            }).getAsInt();
        }
        if (objArr instanceof PersistAtomsProto$ImsRegistrationTermination[]) {
            final PersistAtomsProto$ImsRegistrationTermination[] persistAtomsProto$ImsRegistrationTerminationArr = (PersistAtomsProto$ImsRegistrationTermination[]) objArr;
            return IntStream.range(0, persistAtomsProto$ImsRegistrationTerminationArr.length).reduce(new IntBinaryOperator() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda8
                @Override // java.util.function.IntBinaryOperator
                public final int applyAsInt(int i, int i2) {
                    return PersistAtomsStorage.$r8$lambda$2f17q_hnpQm2dJ3k9IvKFEeuous(persistAtomsProto$ImsRegistrationTerminationArr, i, i2);
                }
            }).getAsInt();
        }
        if (objArr instanceof PersistAtomsProto$VoiceCallSession[]) {
            final PersistAtomsProto$VoiceCallSession[] persistAtomsProto$VoiceCallSessionArr = (PersistAtomsProto$VoiceCallSession[]) objArr;
            int[] array = IntStream.range(0, persistAtomsProto$VoiceCallSessionArr.length).filter(new IntPredicate() { // from class: com.android.internal.telephony.metrics.PersistAtomsStorage$$ExternalSyntheticLambda9
                @Override // java.util.function.IntPredicate
                public final boolean test(int i) {
                    return PersistAtomsStorage.m1226$r8$lambda$QHegR5QaDhS04MH5ey11OJHRrw(persistAtomsProto$VoiceCallSessionArr, i);
                }
            }).toArray();
            if (array.length > 0) {
                return array[sRandom.nextInt(array.length)];
            }
        }
        if (objArr instanceof PersistAtomsProto$OtpEvaluationEvent[]) {
            PersistAtomsProto$OtpEvaluationEvent[] persistAtomsProto$OtpEvaluationEventArr = (PersistAtomsProto$OtpEvaluationEvent[]) objArr;
            SparseIntArray sparseIntArray = new SparseIntArray();
            for (int i = 0; i < persistAtomsProto$OtpEvaluationEventArr.length; i++) {
                PersistAtomsProto$OtpEvaluationEvent persistAtomsProto$OtpEvaluationEvent = persistAtomsProto$OtpEvaluationEventArr[i];
                int i2 = sparseIntArray.get(persistAtomsProto$OtpEvaluationEvent.result, -1);
                if (i2 < 0 || persistAtomsProto$OtpEvaluationEvent.redactionTimeMs > persistAtomsProto$OtpEvaluationEventArr[i2].redactionTimeMs) {
                    sparseIntArray.put(persistAtomsProto$OtpEvaluationEvent.result, i);
                }
            }
            int iNextInt = sRandom.nextInt(objArr.length);
            if (objArr.length == sparseIntArray.size()) {
                return iNextInt;
            }
            while (sparseIntArray.indexOfValue(iNextInt) != -1) {
                iNextInt = (iNextInt + 1) % objArr.length;
            }
            return iNextInt;
        }
        return sRandom.nextInt(objArr.length);
    }

    /* JADX INFO: renamed from: $r8$lambda$tX3s3JO_u7sEg1-yo9Ywxz1wZ-o, reason: not valid java name */
    public static /* synthetic */ int m1228$r8$lambda$tX3s3JO_u7sEg1yo9Ywxz1wZo(PersistAtomsProto$CellularServiceState[] persistAtomsProto$CellularServiceStateArr, int i, int i2) {
        return persistAtomsProto$CellularServiceStateArr[i].lastUsedMillis < persistAtomsProto$CellularServiceStateArr[i2].lastUsedMillis ? i : i2;
    }

    /* JADX INFO: renamed from: $r8$lambda$pRGNNWMzTeqPKNaLzuM_Bfk-WjM, reason: not valid java name */
    public static /* synthetic */ int m1227$r8$lambda$pRGNNWMzTeqPKNaLzuM_BfkWjM(PersistAtomsProto$CellularDataServiceSwitch[] persistAtomsProto$CellularDataServiceSwitchArr, int i, int i2) {
        return persistAtomsProto$CellularDataServiceSwitchArr[i].lastUsedMillis < persistAtomsProto$CellularDataServiceSwitchArr[i2].lastUsedMillis ? i : i2;
    }

    /* JADX INFO: renamed from: $r8$lambda$3s0X-itPvI3I7GRymVaD3v6fh28, reason: not valid java name */
    public static /* synthetic */ int m1224$r8$lambda$3s0XitPvI3I7GRymVaD3v6fh28(PersistAtomsProto$ImsRegistrationStats[] persistAtomsProto$ImsRegistrationStatsArr, int i, int i2) {
        return persistAtomsProto$ImsRegistrationStatsArr[i].lastUsedMillis < persistAtomsProto$ImsRegistrationStatsArr[i2].lastUsedMillis ? i : i2;
    }

    public static /* synthetic */ int $r8$lambda$2f17q_hnpQm2dJ3k9IvKFEeuous(PersistAtomsProto$ImsRegistrationTermination[] persistAtomsProto$ImsRegistrationTerminationArr, int i, int i2) {
        return persistAtomsProto$ImsRegistrationTerminationArr[i].lastUsedMillis < persistAtomsProto$ImsRegistrationTerminationArr[i2].lastUsedMillis ? i : i2;
    }

    /* JADX INFO: renamed from: $r8$lambda$QHegR5QaDh-S04MH5ey11OJHRrw, reason: not valid java name */
    public static /* synthetic */ boolean m1226$r8$lambda$QHegR5QaDhS04MH5ey11OJHRrw(PersistAtomsProto$VoiceCallSession[] persistAtomsProto$VoiceCallSessionArr, int i) {
        return !persistAtomsProto$VoiceCallSessionArr[i].isEmergency;
    }

    private Object[] sanitizeAtoms(Object[] objArr, Class cls) {
        return ArrayUtils.emptyIfNull(objArr, cls);
    }

    private Object[] sanitizeAtoms(Object[] objArr, Class cls, int i) {
        Object[] objArrSanitizeAtoms = sanitizeAtoms(objArr, cls);
        return objArrSanitizeAtoms.length > i ? Arrays.copyOf(objArrSanitizeAtoms, i) : objArrSanitizeAtoms;
    }

    private long sanitizeTimestamp(long j) {
        return j <= 0 ? getWallTimeMillis() : j;
    }

    private PersistAtomsProto$ImsRegistrationStats[] normalizeData(PersistAtomsProto$ImsRegistrationStats[] persistAtomsProto$ImsRegistrationStatsArr, long j) {
        for (int i = 0; i < persistAtomsProto$ImsRegistrationStatsArr.length; i++) {
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats.registeredMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats.registeredMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats2 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats2.voiceCapableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats2.voiceCapableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats3 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats3.voiceAvailableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats3.voiceAvailableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats4 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats4.smsCapableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats4.smsCapableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats5 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats5.smsAvailableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats5.smsAvailableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats6 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats6.videoCapableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats6.videoCapableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats7 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats7.videoAvailableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats7.videoAvailableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats8 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats8.utCapableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats8.utCapableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats9 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats9.utAvailableMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats9.utAvailableMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats10 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats10.registeringMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats10.registeringMillis, j);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats11 = persistAtomsProto$ImsRegistrationStatsArr[i];
            persistAtomsProto$ImsRegistrationStats11.unregisteredMillis = normalizeDurationTo24H(persistAtomsProto$ImsRegistrationStats11.unregisteredMillis, j);
        }
        return persistAtomsProto$ImsRegistrationStatsArr;
    }

    private long normalizeDurationTo24H(long j, long j2) {
        return (((j / 1000) * 86400) / (j2 < 1000 ? 1L : j2 / 1000)) * 1000;
    }

    private PersistAtomsProto$PersistAtoms makeNewPersistAtoms() {
        PersistAtomsProto$PersistAtoms persistAtomsProto$PersistAtoms = new PersistAtomsProto$PersistAtoms();
        long wallTimeMillis = getWallTimeMillis();
        persistAtomsProto$PersistAtoms.buildFingerprint = Build.FINGERPRINT;
        persistAtomsProto$PersistAtoms.voiceCallRatUsagePullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.voiceCallSessionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.incomingSmsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.outgoingSmsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.carrierIdTableVersion = -1;
        persistAtomsProto$PersistAtoms.dataCallSessionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.cellularServiceStatePullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.cellularDataServiceSwitchPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsRegistrationStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsRegistrationTerminationPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.networkRequestsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.networkRequestsV2PullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsRegistrationFeatureTagStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.rcsClientProvisioningStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.rcsAcsProvisioningStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.sipDelegateStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.sipTransportFeatureTagStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.sipMessageResponsePullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.sipTransportSessionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsDedicatedBearerListenerEventPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsDedicatedBearerEventPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.imsRegistrationServiceDescStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.uceEventStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.presenceNotifyEventPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.gbaEventPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.outgoingShortCodeSmsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteControllerPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteSessionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteIncomingDatagramPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteOutgoingDatagramPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteProvisionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteSosMessageRecommenderPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.dataNetworkValidationPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.carrierRoamingSatelliteSessionPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.carrierRoamingSatelliteControllerStatsPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteEntitlementPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteConfigUpdaterPullTimestampMillis = wallTimeMillis;
        persistAtomsProto$PersistAtoms.satelliteAccessControllerPullTimestampMillis = wallTimeMillis;
        Rlog.d(TAG, "created new PersistAtoms");
        return persistAtomsProto$PersistAtoms;
    }

    protected long getWallTimeMillis() {
        return System.currentTimeMillis();
    }

    public synchronized void setSaveImmediately(boolean z) {
        Rlog.d(TAG, "setSaveImmediately(" + z + ")");
        this.mSaveImmediately = z;
    }
}
