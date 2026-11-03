package com.android.internal.telephony.metrics;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.ims.ImsReasonInfo;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallSession;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.uicc.UiccController;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class VoiceCallSessionStats {
    private static final String TAG = "VoiceCallSessionStats";
    private final FeatureFlags mFlags;
    private final Phone mPhone;
    private final int mPhoneId;
    private final SatelliteController mSatelliteController;
    private static final SparseIntArray CS_CODEC_MAP = buildGsmCdmaCodecMap();
    private static final SparseIntArray IMS_CODEC_MAP = buildImsCodecMap();
    private static final SparseIntArray CALL_DURATION_MAP = buildCallDurationMap();
    private static final UUID CONCURRENT_CALL_ANOMALY_UUID = UUID.fromString("76780b5a-623e-48a4-be3f-925e05177c9c");
    private final SparseArray mCallProtos = new SparseArray();
    private final SparseArray mCodecUsage = new SparseArray();
    private final VoiceCallRatTracker mRatUsage = new VoiceCallRatTracker();
    private final PersistAtomsStorage mAtomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
    private final UiccController mUiccController = UiccController.getInstance();
    private final DeviceStateHelper mDeviceStateHelper = PhoneFactory.getMetricsCollector().getDeviceStateHelper();
    private final VonrHelper mVonrHelper = PhoneFactory.getMetricsCollector().getVonrHelper();

    private int getCodecQuality(int i) {
        switch (i) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                return 1;
            case 2:
            case 6:
            case 18:
                return 2;
            case 19:
                return 3;
            case 20:
                return 4;
            default:
                return 0;
        }
    }

    public VoiceCallSessionStats(int i, Phone phone, FeatureFlags featureFlags) {
        this.mPhoneId = i;
        this.mPhone = phone;
        this.mFlags = featureFlags;
        DataConnectionStateTracker.getInstance(i).start(phone);
        this.mSatelliteController = SatelliteController.getInstance();
    }

    public synchronized void onRilAcceptCall(List<Connection> list) {
        Iterator<Connection> it = list.iterator();
        while (it.hasNext()) {
            acceptCall(it.next());
        }
    }

    public synchronized void onRilDial(Connection connection) {
        addCall(connection);
    }

    public synchronized void onRilCallListChanged(List<GsmCdmaConnection> list) {
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.CallAnalytics callAnalytics;
        try {
            for (GsmCdmaConnection gsmCdmaConnection : list) {
                int connectionId = getConnectionId(gsmCdmaConnection);
                if (!this.mCallProtos.contains(connectionId)) {
                    if (gsmCdmaConnection.getDisconnectCause() == 0) {
                        addCall(gsmCdmaConnection);
                        checkCallSetup(gsmCdmaConnection, (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId));
                    } else {
                        logd("onRilCallListChanged: skip adding disconnected connection, connectionId=%d", Integer.valueOf(connectionId));
                    }
                } else {
                    PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
                    checkCallSetup(gsmCdmaConnection, persistAtomsProto$VoiceCallSession);
                    if (gsmCdmaConnection.getDisconnectCause() != 0) {
                        persistAtomsProto$VoiceCallSession.bearerAtEnd = getBearer(gsmCdmaConnection);
                        persistAtomsProto$VoiceCallSession.disconnectReasonCode = gsmCdmaConnection.getDisconnectCause();
                        persistAtomsProto$VoiceCallSession.disconnectExtraCode = gsmCdmaConnection.getPreciseDisconnectCause();
                        persistAtomsProto$VoiceCallSession.disconnectExtraMessage = gsmCdmaConnection.getVendorDisconnectCause();
                        persistAtomsProto$VoiceCallSession.callDuration = classifyCallDuration(gsmCdmaConnection.getDurationMillis());
                        Phone phone = this.mPhone;
                        if (phone != null && (telephonyAnalytics = phone.getTelephonyAnalytics()) != null && (callAnalytics = telephonyAnalytics.getCallAnalytics()) != null) {
                            boolean z = persistAtomsProto$VoiceCallSession.isEmergency;
                            Phone phone2 = this.mPhone;
                            callAnalytics.onCallTerminated(z, false, getVoiceRatWithVoNRFix(phone2, phone2.getServiceState(), persistAtomsProto$VoiceCallSession.bearerAtEnd), persistAtomsProto$VoiceCallSession.simSlotIndex, persistAtomsProto$VoiceCallSession.disconnectReasonCode);
                        }
                        finishCall(connectionId);
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onImsDial(ImsPhoneConnection imsPhoneConnection) {
        addCall(imsPhoneConnection);
        if (imsPhoneConnection.hasRttTextStream()) {
            setRttStarted(imsPhoneConnection);
        }
    }

    public synchronized void onImsCallReceived(ImsPhoneConnection imsPhoneConnection) {
        addCall(imsPhoneConnection);
        if (imsPhoneConnection.hasRttTextStream()) {
            setRttStarted(imsPhoneConnection);
        }
    }

    public synchronized void onImsAcceptCall(List<Connection> list) {
        Iterator<Connection> it = list.iterator();
        while (it.hasNext()) {
            acceptCall(it.next());
        }
    }

    public synchronized void onImsCallStartFailed(ImsPhoneConnection imsPhoneConnection, ImsReasonInfo imsReasonInfo) {
        onImsCallTerminated(imsPhoneConnection, imsReasonInfo);
    }

    public synchronized void onImsCallTerminated(ImsPhoneConnection imsPhoneConnection, ImsReasonInfo imsReasonInfo) {
        try {
            if (imsPhoneConnection == null) {
                List imsConnectionIds = getImsConnectionIds();
                if (imsConnectionIds.size() == 1) {
                    loge("onImsCallTerminated: ending IMS call w/ conn=null", new Object[0]);
                    finishImsCall(((Integer) imsConnectionIds.get(0)).intValue(), imsReasonInfo, 0L);
                } else {
                    loge("onImsCallTerminated: %d IMS calls w/ conn=null", Integer.valueOf(imsConnectionIds.size()));
                }
            } else {
                int connectionId = getConnectionId(imsPhoneConnection);
                if (this.mCallProtos.contains(connectionId)) {
                    finishImsCall(connectionId, imsReasonInfo, imsPhoneConnection.getDurationMillis());
                } else {
                    loge("onImsCallTerminated: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
                    addCall(imsPhoneConnection);
                    finishImsCall(connectionId, imsReasonInfo, imsPhoneConnection.getDurationMillis());
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onRttStarted(ImsPhoneConnection imsPhoneConnection) {
        setRttStarted(imsPhoneConnection);
    }

    public synchronized void onAudioCodecChanged(Connection connection, int i) {
        int connectionId = getConnectionId(connection);
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
        if (persistAtomsProto$VoiceCallSession == null) {
            loge("onAudioCodecChanged: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
            return;
        }
        int iAudioQualityToCodec = audioQualityToCodec(persistAtomsProto$VoiceCallSession.bearerAtEnd, i);
        persistAtomsProto$VoiceCallSession.codecBitmask |= 1 << iAudioQualityToCodec;
        if (this.mCodecUsage.contains(connectionId)) {
            ((LongSparseArray) this.mCodecUsage.get(connectionId)).append(getTimeMillis(), Integer.valueOf(iAudioQualityToCodec));
        } else {
            LongSparseArray longSparseArray = new LongSparseArray();
            longSparseArray.append(getTimeMillis(), Integer.valueOf(iAudioQualityToCodec));
            this.mCodecUsage.put(connectionId, longSparseArray);
        }
    }

    public synchronized void onVideoStateChange(ImsPhoneConnection imsPhoneConnection, int i) {
        int connectionId = getConnectionId(imsPhoneConnection);
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
        if (persistAtomsProto$VoiceCallSession == null) {
            loge("onVideoStateChange: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
            return;
        }
        logd("onVideoStateChange: video state=%d, connectionId=%d", Integer.valueOf(i), Integer.valueOf(connectionId));
        if (i != 0) {
            persistAtomsProto$VoiceCallSession.videoEnabled = true;
        }
    }

    public synchronized void onMultipartyChange(ImsPhoneConnection imsPhoneConnection, boolean z) {
        int connectionId = getConnectionId(imsPhoneConnection);
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
        if (persistAtomsProto$VoiceCallSession == null) {
            loge("onMultipartyChange: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
            return;
        }
        logd("onMultipartyChange: isMultiparty=%b, connectionId=%d", Boolean.valueOf(z), Integer.valueOf(connectionId));
        if (z) {
            persistAtomsProto$VoiceCallSession.isMultiparty = true;
        }
    }

    public synchronized void onCallStateChanged(Call call) {
        try {
            ArrayList<Connection> connections = call.getConnections();
            int size = connections.size();
            int i = 0;
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                Connection connection2 = connection;
                int connectionId = getConnectionId(connection2);
                PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
                if (persistAtomsProto$VoiceCallSession != null) {
                    checkCallSetup(connection2, persistAtomsProto$VoiceCallSession);
                } else {
                    loge("onCallStateChanged: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onRilSrvccStateChanged(int i) {
        ArrayList<Connection> handoverConnection;
        List imsConnectionIds;
        try {
            if (this.mPhone.getImsPhone() != null) {
                handoverConnection = this.mPhone.getImsPhone().getHandoverConnection();
            } else {
                loge("onRilSrvccStateChanged: ImsPhone is null", new Object[0]);
                handoverConnection = null;
            }
            if (handoverConnection == null) {
                imsConnectionIds = getImsConnectionIds();
                loge("onRilSrvccStateChanged: ImsPhone has no handover, we have %d", Integer.valueOf(imsConnectionIds.size()));
            } else {
                imsConnectionIds = (List) handoverConnection.stream().map(new Function() { // from class: com.android.internal.telephony.metrics.VoiceCallSessionStats$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return Integer.valueOf(VoiceCallSessionStats.getConnectionId((Connection) obj));
                    }
                }).collect(Collectors.toList());
            }
            if (i == 1) {
                Iterator it = imsConnectionIds.iterator();
                while (it.hasNext()) {
                    PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(((Integer) it.next()).intValue());
                    persistAtomsProto$VoiceCallSession.srvccCompleted = true;
                    persistAtomsProto$VoiceCallSession.bearerAtEnd = 1;
                    Phone phone = this.mPhone;
                    updateRatAtEnd(persistAtomsProto$VoiceCallSession, getVoiceRatWithVoNRFix(phone, phone.getServiceState(), persistAtomsProto$VoiceCallSession.bearerAtEnd));
                }
            } else if (i == 2) {
                Iterator it2 = imsConnectionIds.iterator();
                while (it2.hasNext()) {
                    ((PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(((Integer) it2.next()).intValue())).srvccFailureCount++;
                }
            } else if (i == 3) {
                Iterator it3 = imsConnectionIds.iterator();
                while (it3.hasNext()) {
                    ((PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(((Integer) it3.next()).intValue())).srvccCancellationCount++;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onServiceStateChanged(ServiceState serviceState) {
        if (hasCalls()) {
            updateRatTracker(serviceState);
        }
    }

    public synchronized void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
        if (hasCalls()) {
            updateVoiceCallSessionBearerState(preciseDataConnectionState);
        }
    }

    private void acceptCall(Connection connection) {
        int connectionId = getConnectionId(connection);
        if (this.mCallProtos.contains(connectionId)) {
            logd("acceptCall: resetting setup info, connectionId=%d", Integer.valueOf(connectionId));
            ((PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId)).setupBeginMillis = getTimeMillis();
            return;
        }
        loge("acceptCall: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
    }

    private void addCall(Connection connection) {
        int connectionId = getConnectionId(connection);
        if (this.mCallProtos.contains(connectionId)) {
            loge("addCall: already tracked connection, connectionId=%d, connectionInfo=%s", Integer.valueOf(connectionId), connection);
            return;
        }
        int bearer = getBearer(connection);
        ServiceState serviceState = getServiceState();
        int voiceRatWithVoNRFix = getVoiceRatWithVoNRFix(this.mPhone, serviceState, bearer);
        int videoState = connection.getVideoState();
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = new PersistAtomsProto$VoiceCallSession();
        this.mVonrHelper.updateVonrEnabledState();
        persistAtomsProto$VoiceCallSession.bearerAtStart = bearer;
        persistAtomsProto$VoiceCallSession.bearerAtEnd = bearer;
        persistAtomsProto$VoiceCallSession.direction = getDirection(connection);
        persistAtomsProto$VoiceCallSession.setupFailed = true;
        persistAtomsProto$VoiceCallSession.disconnectReasonCode = connection.getDisconnectCause();
        persistAtomsProto$VoiceCallSession.disconnectExtraCode = connection.getPreciseDisconnectCause();
        persistAtomsProto$VoiceCallSession.disconnectExtraMessage = connection.getVendorDisconnectCause();
        persistAtomsProto$VoiceCallSession.ratAtStart = voiceRatWithVoNRFix;
        persistAtomsProto$VoiceCallSession.ratAtConnected = 0;
        persistAtomsProto$VoiceCallSession.ratAtEnd = voiceRatWithVoNRFix;
        persistAtomsProto$VoiceCallSession.ratSwitchCount = 0L;
        persistAtomsProto$VoiceCallSession.ratSwitchCountAfterConnected = 0L;
        persistAtomsProto$VoiceCallSession.codecBitmask = 0L;
        persistAtomsProto$VoiceCallSession.simSlotIndex = this.mPhoneId;
        persistAtomsProto$VoiceCallSession.isMultiSim = SimSlotState.isMultiSim();
        persistAtomsProto$VoiceCallSession.isEsim = SimSlotState.isEsim(this.mPhoneId);
        persistAtomsProto$VoiceCallSession.carrierId = this.mPhone.getCarrierId();
        persistAtomsProto$VoiceCallSession.srvccCompleted = false;
        persistAtomsProto$VoiceCallSession.srvccFailureCount = 0L;
        persistAtomsProto$VoiceCallSession.srvccCancellationCount = 0L;
        persistAtomsProto$VoiceCallSession.rttEnabled = false;
        persistAtomsProto$VoiceCallSession.isEmergency = connection.isEmergencyCall() || connection.isNetworkIdentifiedEmergencyCall();
        persistAtomsProto$VoiceCallSession.isRoaming = ServiceStateStats.isNetworkRoaming(serviceState);
        persistAtomsProto$VoiceCallSession.isMultiparty = connection.isMultiparty();
        persistAtomsProto$VoiceCallSession.lastKnownRat = voiceRatWithVoNRFix;
        persistAtomsProto$VoiceCallSession.videoEnabled = videoState != 0;
        persistAtomsProto$VoiceCallSession.handoverInProgress = isHandoverInProgress(bearer, persistAtomsProto$VoiceCallSession.isEmergency);
        boolean zIsCrossSimCall = isCrossSimCall(connection);
        persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtStart = zIsCrossSimCall;
        persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtEnd = zIsCrossSimCall;
        persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtConnected = zIsCrossSimCall;
        if (getDirection(connection) == 2) {
            persistAtomsProto$VoiceCallSession.setupBeginMillis = 0L;
        } else {
            persistAtomsProto$VoiceCallSession.setupBeginMillis = getTimeMillis();
        }
        int iAudioQualityToCodec = audioQualityToCodec(bearer, connection.getAudioCodec());
        if (iAudioQualityToCodec != 0) {
            persistAtomsProto$VoiceCallSession.codecBitmask = 1 << iAudioQualityToCodec;
        }
        int size = this.mCallProtos.size();
        persistAtomsProto$VoiceCallSession.concurrentCallCountAtStart = size;
        if (size > 3) {
            AnomalyReporter.reportAnomaly(CONCURRENT_CALL_ANOMALY_UUID, "Anomalous number of concurrent calls");
        }
        this.mCallProtos.put(connectionId, persistAtomsProto$VoiceCallSession);
        updateRatTracker(serviceState);
    }

    private void finishCall(int i) {
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(i);
        if (persistAtomsProto$VoiceCallSession == null) {
            loge("finishCall: could not find call to be removed, connectionId=%d", Integer.valueOf(i));
            return;
        }
        if (persistAtomsProto$VoiceCallSession.setupFailed && persistAtomsProto$VoiceCallSession.setupBeginMillis != 0 && persistAtomsProto$VoiceCallSession.setupDurationMillis == 0) {
            persistAtomsProto$VoiceCallSession.preciseCallStateOnSetup = convertCallStateEnumToInt(Call.State.DISCONNECTED);
            persistAtomsProto$VoiceCallSession.setupDurationMillis = (int) (getTimeMillis() - persistAtomsProto$VoiceCallSession.setupBeginMillis);
        }
        this.mCallProtos.delete(i);
        persistAtomsProto$VoiceCallSession.concurrentCallCountAtEnd = this.mCallProtos.size();
        persistAtomsProto$VoiceCallSession.signalStrengthAtEnd = getSignalStrength(persistAtomsProto$VoiceCallSession.ratAtEnd);
        persistAtomsProto$VoiceCallSession.mainCodecQuality = finalizeMainCodecQuality(i);
        persistAtomsProto$VoiceCallSession.setupBeginMillis = 0L;
        if (persistAtomsProto$VoiceCallSession.disconnectExtraMessage == null) {
            persistAtomsProto$VoiceCallSession.disconnectExtraMessage = "";
        }
        if (persistAtomsProto$VoiceCallSession.carrierId <= 0) {
            persistAtomsProto$VoiceCallSession.carrierId = this.mPhone.getCarrierId();
        }
        updateRatAtEnd(persistAtomsProto$VoiceCallSession, getVoiceRatWithVoNRFix(this.mPhone, getServiceState(), persistAtomsProto$VoiceCallSession.bearerAtEnd));
        persistAtomsProto$VoiceCallSession.foldState = this.mDeviceStateHelper.getFoldState();
        persistAtomsProto$VoiceCallSession.vonrEnabled = this.mVonrHelper.getVonrEnabled(this.mPhone.getSubId());
        persistAtomsProto$VoiceCallSession.supportsBusinessCallComposer = isBusinessCallSupported();
        persistAtomsProto$VoiceCallSession.callComposerStatus = getCallComposerStatusForPhone() + 1;
        SatelliteController satelliteController = this.mSatelliteController;
        persistAtomsProto$VoiceCallSession.isNtn = satelliteController != null ? satelliteController.isInSatelliteModeForCarrierRoaming(this.mPhone) : false;
        this.mAtomsStorage.addVoiceCallSession(persistAtomsProto$VoiceCallSession);
        if (hasCalls()) {
            return;
        }
        this.mRatUsage.conclude(getTimeMillis());
        this.mAtomsStorage.addVoiceCallRatUsage(this.mRatUsage);
        this.mRatUsage.clear();
    }

    private void setRttStarted(ImsPhoneConnection imsPhoneConnection) {
        int connectionId = getConnectionId(imsPhoneConnection);
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(connectionId);
        if (persistAtomsProto$VoiceCallSession == null) {
            loge("onRttStarted: untracked connection, connectionId=%d", Integer.valueOf(connectionId));
            return;
        }
        if (persistAtomsProto$VoiceCallSession.bearerAtStart != getBearer(imsPhoneConnection) || persistAtomsProto$VoiceCallSession.bearerAtEnd != getBearer(imsPhoneConnection)) {
            loge("onRttStarted: connection bearer mismatch but proceeding, connectionId=%d", Integer.valueOf(connectionId));
        }
        persistAtomsProto$VoiceCallSession.rttEnabled = true;
    }

    private Set getConnectionIds() {
        HashSet hashSet = new HashSet();
        for (int i = 0; i < this.mCallProtos.size(); i++) {
            hashSet.add(Integer.valueOf(this.mCallProtos.keyAt(i)));
        }
        return hashSet;
    }

    private List getImsConnectionIds() {
        ArrayList arrayList = new ArrayList(this.mCallProtos.size());
        for (int i = 0; i < this.mCallProtos.size(); i++) {
            if (((PersistAtomsProto$VoiceCallSession) this.mCallProtos.valueAt(i)).bearerAtEnd == 2) {
                arrayList.add(Integer.valueOf(this.mCallProtos.keyAt(i)));
            }
        }
        return arrayList;
    }

    private boolean hasCalls() {
        return this.mCallProtos.size() > 0;
    }

    private void checkCallSetup(Connection connection, PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession) {
        if (persistAtomsProto$VoiceCallSession.setupBeginMillis != 0 && isSetupFinished(connection.getCall())) {
            persistAtomsProto$VoiceCallSession.preciseCallStateOnSetup = convertCallStateEnumToInt(connection.getState());
            persistAtomsProto$VoiceCallSession.setupDurationMillis = (int) (getTimeMillis() - persistAtomsProto$VoiceCallSession.setupBeginMillis);
            persistAtomsProto$VoiceCallSession.setupBeginMillis = 0L;
        }
        if (persistAtomsProto$VoiceCallSession.setupFailed && connection.getState() == Call.State.ACTIVE) {
            persistAtomsProto$VoiceCallSession.setupFailed = false;
            persistAtomsProto$VoiceCallSession.ratAtConnected = getVoiceRatWithVoNRFix(this.mPhone, getServiceState(), persistAtomsProto$VoiceCallSession.bearerAtEnd);
            persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtConnected = isCrossSimCall(connection);
            resetCodecList(connection);
        }
    }

    private void updateRatTracker(ServiceState serviceState) {
        this.mRatUsage.add(this.mPhone.getCarrierId(), getVoiceRatWithVoNRFix(this.mPhone, serviceState, 0), getTimeMillis(), getConnectionIds());
        for (int i = 0; i < this.mCallProtos.size(); i++) {
            PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.valueAt(i);
            int voiceRatWithVoNRFix = getVoiceRatWithVoNRFix(this.mPhone, serviceState, persistAtomsProto$VoiceCallSession.bearerAtEnd);
            updateRatAtEnd(persistAtomsProto$VoiceCallSession, voiceRatWithVoNRFix);
            persistAtomsProto$VoiceCallSession.bandAtEnd = voiceRatWithVoNRFix == 18 ? 0 : ServiceStateStats.getBand(serviceState);
        }
    }

    private void updateRatAtEnd(PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession, int i) {
        if (persistAtomsProto$VoiceCallSession.ratAtEnd != i) {
            persistAtomsProto$VoiceCallSession.ratSwitchCount++;
            if (!persistAtomsProto$VoiceCallSession.setupFailed) {
                persistAtomsProto$VoiceCallSession.ratSwitchCountAfterConnected++;
            }
            persistAtomsProto$VoiceCallSession.ratAtEnd = i;
            if (i != 0) {
                persistAtomsProto$VoiceCallSession.lastKnownRat = i;
            }
        }
        persistAtomsProto$VoiceCallSession.isIwlanCrossSimAtEnd = isCrossSimCall(this.mPhone);
    }

    private void finishImsCall(int i, ImsReasonInfo imsReasonInfo, long j) {
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.CallAnalytics callAnalytics;
        PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.get(i);
        persistAtomsProto$VoiceCallSession.bearerAtEnd = 2;
        persistAtomsProto$VoiceCallSession.disconnectReasonCode = imsReasonInfo.mCode;
        persistAtomsProto$VoiceCallSession.disconnectExtraCode = imsReasonInfo.mExtraCode;
        persistAtomsProto$VoiceCallSession.disconnectExtraMessage = ImsStats.filterExtraMessage(imsReasonInfo.mExtraMessage);
        persistAtomsProto$VoiceCallSession.callDuration = classifyCallDuration(j);
        Phone phone = this.mPhone;
        if (phone != null && (telephonyAnalytics = phone.getTelephonyAnalytics()) != null && (callAnalytics = telephonyAnalytics.getCallAnalytics()) != null) {
            boolean z = persistAtomsProto$VoiceCallSession.isEmergency;
            Phone phone2 = this.mPhone;
            callAnalytics.onCallTerminated(z, true, getVoiceRatWithVoNRFix(phone2, phone2.getServiceState(), persistAtomsProto$VoiceCallSession.bearerAtEnd), persistAtomsProto$VoiceCallSession.simSlotIndex, persistAtomsProto$VoiceCallSession.disconnectReasonCode);
        }
        finishCall(i);
    }

    private ServiceState getServiceState() {
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        if (serviceStateTracker != null) {
            return serviceStateTracker.getServiceState();
        }
        return null;
    }

    private static int getDirection(Connection connection) {
        return connection.isIncoming() ? 2 : 1;
    }

    private static int getBearer(Connection connection) {
        int phoneType = connection.getPhoneType();
        if (phoneType == 1 || phoneType == 2) {
            return 1;
        }
        if (phoneType == 5) {
            return 2;
        }
        loge("getBearer: unknown phoneType=%d", Integer.valueOf(phoneType));
        return 0;
    }

    private int getSignalStrength(int i) {
        if (i == 18) {
            return getSignalStrengthWifi();
        }
        return getSignalStrengthCellular();
    }

    private int getSignalStrengthWifi() {
        WifiManager wifiManager = (WifiManager) this.mPhone.getContext().getSystemService("wifi");
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return 0;
        }
        int iCalculateSignalLevel = wifiManager.calculateSignalLevel(connectionInfo.getRssi());
        int maxSignalLevel = wifiManager.getMaxSignalLevel();
        int i = (iCalculateSignalLevel * 4) / maxSignalLevel;
        logd("WiFi level: " + i + " (" + iCalculateSignalLevel + "/" + maxSignalLevel + ")", new Object[0]);
        return i;
    }

    private int getSignalStrengthCellular() {
        return this.mPhone.getSignalStrength().getLevel();
    }

    private boolean isHandoverInProgress(int i, boolean z) {
        if (i != 2) {
            return false;
        }
        return DataConnectionStateTracker.getInstance(this.mPhoneId).getDataState(z ? NtnRILConstants.MI_NTN_OEM_ERROR_12 : 64) == 5;
    }

    private static int getVoiceRatWithVoNRFix(Phone phone, ServiceState serviceState, int i) {
        if (serviceState == null) {
            return 0;
        }
        ImsPhone imsPhone = (ImsPhone) phone.getImsPhone();
        if (i != 1 && imsPhone != null) {
            int imsVoiceRadioTech = imsPhone.getImsStats().getImsVoiceRadioTech();
            int rat = ServiceStateStats.getRat(serviceState, 2);
            if (imsVoiceRadioTech != 0 && (imsVoiceRadioTech == 18 || rat != 0)) {
                int voiceRat = ServiceStateStats.getVoiceRat(phone, serviceState, i);
                if (imsVoiceRadioTech != 18) {
                    imsVoiceRadioTech = rat;
                }
                logd("getVoiceRatWithVoNRFix: oldRat=%d, newRat=%d", Integer.valueOf(voiceRat), Integer.valueOf(imsVoiceRadioTech));
                return imsVoiceRadioTech;
            }
        }
        return ServiceStateStats.getRat(serviceState, 1);
    }

    private void resetCodecList(Connection connection) {
        int connectionId = getConnectionId(connection);
        LongSparseArray longSparseArray = (LongSparseArray) this.mCodecUsage.get(connectionId);
        if (longSparseArray != null) {
            Integer num = (Integer) longSparseArray.valueAt(longSparseArray.size() - 1);
            num.intValue();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            longSparseArray2.append(getTimeMillis(), num);
            this.mCodecUsage.put(connectionId, longSparseArray2);
        }
    }

    private int finalizeMainCodecQuality(int i) {
        if (!this.mCodecUsage.contains(i)) {
            return 0;
        }
        LongSparseArray longSparseArray = (LongSparseArray) this.mCodecUsage.get(i);
        this.mCodecUsage.delete(i);
        longSparseArray.put(getTimeMillis(), 0);
        long[] jArr = new long[5];
        int i2 = 0;
        int i3 = 0;
        while (i2 < longSparseArray.size() - 1) {
            int i4 = i2 + 1;
            long jKeyAt = longSparseArray.keyAt(i4) - longSparseArray.keyAt(i2);
            int codecQuality = getCodecQuality(((Integer) longSparseArray.valueAt(i2)).intValue());
            jArr[codecQuality] = jArr[codecQuality] + jKeyAt;
            i3 = (int) (((long) i3) + jKeyAt);
            i2 = i4;
        }
        logd("Time per codec quality = " + Arrays.toString(jArr), new Object[0]);
        long j = (long) ((i3 * 70) / 100);
        long j2 = 0;
        for (int i5 = 4; i5 >= 0; i5--) {
            j2 += jArr[i5];
            if (j2 >= j) {
                return i5;
            }
        }
        return 0;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.metrics.VoiceCallSessionStats$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$Call$State;

        static {
            int[] iArr = new int[Call.State.values().length];
            $SwitchMap$com$android$internal$telephony$Call$State = iArr;
            try {
                iArr[Call.State.ACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.ALERTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.IDLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.HOLDING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DIALING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.INCOMING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.WAITING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DISCONNECTED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DISCONNECTING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    private static boolean isSetupFinished(Call call) {
        if (call == null) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$Call$State[call.getState().ordinal()];
        return i == 1 || i == 2;
    }

    private static int audioQualityToCodec(int i, int i2) {
        if (i == 1) {
            return CS_CODEC_MAP.get(i2, 0);
        }
        if (i == 2) {
            return IMS_CODEC_MAP.get(i2, 0);
        }
        loge("audioQualityToCodec: unknown bearer %d", Integer.valueOf(i));
        return 0;
    }

    private static int classifyCallDuration(long j) {
        int i = 0;
        if (j == 0) {
            return 0;
        }
        while (true) {
            SparseIntArray sparseIntArray = CALL_DURATION_MAP;
            if (i >= sparseIntArray.size()) {
                return 6;
            }
            if (j < sparseIntArray.keyAt(i)) {
                return sparseIntArray.valueAt(i);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getConnectionId(Connection connection) {
        if (connection == null) {
            return 0;
        }
        return (int) connection.getCreateTime();
    }

    private boolean isCrossSimCall(Connection connection) {
        if (connection instanceof ImsPhoneConnection) {
            return ((ImsPhoneConnection) connection).isCrossSimCall();
        }
        return false;
    }

    private boolean isCrossSimCall(Phone phone) {
        return phone.getImsPhone() != null && phone.getImsPhone().getImsRegistrationTech() == 2;
    }

    private int getCallComposerStatusForPhone() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mPhone.getContext().getSystemService(TelephonyManager.class);
        if (telephonyManager == null) {
            return 0;
        }
        return telephonyManager.createForSubscriptionId(this.mPhone.getSubId()).getCallComposerStatus();
    }

    private boolean isBusinessCallSupported() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            return false;
        }
        try {
            configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId(), "supports_business_call_composer_bool");
        } catch (RuntimeException unused) {
            loge("CarrierConfigLoader is not available.", new Object[0]);
            configForSubId = null;
        }
        if (configForSubId == null || configForSubId.isEmpty()) {
            return false;
        }
        return configForSubId.getBoolean("supports_business_call_composer_bool");
    }

    protected long getTimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    private static void logd(String str, Object... objArr) {
        Rlog.d(TAG, String.format(str, objArr));
    }

    private static void loge(String str, Object... objArr) {
        Rlog.e(TAG, String.format(str, objArr));
    }

    private static SparseIntArray buildGsmCdmaCodecMap() {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(1, 1);
        sparseIntArray.put(2, 2);
        sparseIntArray.put(3, 8);
        sparseIntArray.put(4, 9);
        sparseIntArray.put(5, 10);
        sparseIntArray.put(6, 4);
        sparseIntArray.put(7, 5);
        sparseIntArray.put(8, 6);
        sparseIntArray.put(9, 7);
        return sparseIntArray;
    }

    private static SparseIntArray buildImsCodecMap() {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(1, 1);
        sparseIntArray.put(2, 2);
        sparseIntArray.put(3, 3);
        sparseIntArray.put(4, 4);
        sparseIntArray.put(5, 5);
        sparseIntArray.put(6, 6);
        sparseIntArray.put(7, 7);
        sparseIntArray.put(8, 8);
        sparseIntArray.put(9, 9);
        sparseIntArray.put(10, 10);
        sparseIntArray.put(11, 11);
        sparseIntArray.put(12, 12);
        sparseIntArray.put(13, 13);
        sparseIntArray.put(14, 14);
        sparseIntArray.put(15, 15);
        sparseIntArray.put(16, 16);
        sparseIntArray.put(17, 17);
        sparseIntArray.put(18, 18);
        sparseIntArray.put(19, 19);
        sparseIntArray.put(20, 20);
        return sparseIntArray;
    }

    private static SparseIntArray buildCallDurationMap() {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(ServiceStateTracker.DEFAULT_GPRS_CHECK_PERIOD_MILLIS, 1);
        sparseIntArray.put(300000, 2);
        sparseIntArray.put(600000, 3);
        sparseIntArray.put(1800000, 4);
        sparseIntArray.put(3600000, 5);
        return sparseIntArray;
    }

    private void updateVoiceCallSessionBearerState(PreciseDataConnectionState preciseDataConnectionState) {
        ApnSetting apnSetting = preciseDataConnectionState.getApnSetting();
        if (apnSetting == null) {
            return;
        }
        int apnTypeBitmask = apnSetting.getApnTypeBitmask();
        int i = apnTypeBitmask & 64;
        if (i == 0 && (apnTypeBitmask & NtnRILConstants.MI_NTN_OEM_ERROR_12) == 0) {
            return;
        }
        for (int i2 = 0; i2 < this.mCallProtos.size(); i2++) {
            PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession = (PersistAtomsProto$VoiceCallSession) this.mCallProtos.valueAt(i2);
            if (persistAtomsProto$VoiceCallSession.bearerAtEnd == 2) {
                if (!persistAtomsProto$VoiceCallSession.isEmergency && i != 0) {
                    updateHandoverState(persistAtomsProto$VoiceCallSession, preciseDataConnectionState.getState());
                }
                if (persistAtomsProto$VoiceCallSession.isEmergency && (apnTypeBitmask & NtnRILConstants.MI_NTN_OEM_ERROR_12) != 0) {
                    updateHandoverState(persistAtomsProto$VoiceCallSession, preciseDataConnectionState.getState());
                }
            }
        }
    }

    private void updateHandoverState(PersistAtomsProto$VoiceCallSession persistAtomsProto$VoiceCallSession, int i) {
        if (i == 5) {
            persistAtomsProto$VoiceCallSession.handoverInProgress = true;
        } else {
            persistAtomsProto$VoiceCallSession.handoverInProgress = false;
        }
    }

    private int convertCallStateEnumToInt(Call.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$Call$State[state.ordinal()]) {
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 1;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            default:
                return 0;
        }
    }
}
