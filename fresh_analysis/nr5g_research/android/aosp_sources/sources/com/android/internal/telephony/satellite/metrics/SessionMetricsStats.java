package com.android.internal.telephony.satellite.metrics;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.satellite.SatelliteSessionStats;
import android.util.Log;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.DatagramDispatcher;
import com.android.internal.telephony.satellite.SatelliteConstants;

/* JADX INFO: loaded from: classes.dex */
public class SessionMetricsStats {
    private static final String TAG = "SessionMetricsStats";
    private static SessionMetricsStats sInstance;
    private int mCarrierId;
    private int mCountOfAutoExitDueToScreenOff;
    private int mCountOfAutoExitDueToTnNetwork;
    private int mCountOfFailedOutgoingDatagram;
    private int mCountOfIncomingDatagramFailed;
    private int mCountOfSatelliteNotificationDisplayed;
    private int mCountOfSuccessfulIncomingDatagram;
    private int mCountOfSuccessfulOutgoingDatagram;
    private int mCountOfTimedOutUserMessagesWaitingForAck;
    private int mCountOfTimedOutUserMessagesWaitingForConnection;
    private SatelliteSessionStats mDatagramStats;
    private long mInitializationProcessingTimeMillis;
    private int mInitializationResult;
    private boolean mIsDemoMode;
    private boolean mIsEmergency;
    private boolean mIsNtnOnlyCarrier;
    private int mMaxInactivityDurationSec;
    private int mMaxNtnSignalStrengthLevel;
    private String mPlmn;
    private int mRadioTechnology;
    private int mSessionConnectionMode;
    private int mSessionDurationSec;
    private int mShadowCountOfFailedOutgoingDatagram;
    private int mShadowCountOfSuccessfulOutgoingDatagram;
    private int mShadowCountOfTimedOutUserMessagesWaitingForAck;
    private int mShadowCountOfTimedOutUserMessagesWaitingForConnection;
    private int mSupportedConnectionMode;
    private long mTerminationProcessingTimeMillis;
    private int mTerminationResult;

    private SessionMetricsStats() {
        initializeSessionMetricsParam();
        this.mDatagramStats = new SatelliteSessionStats();
    }

    public static SessionMetricsStats getInstance() {
        if (sInstance == null) {
            loge("create new SessionMetricsStats.");
            sInstance = new SessionMetricsStats();
        }
        return sInstance;
    }

    public SessionMetricsStats setInitializationResult(int i) {
        logd("setInitializationResult(" + i + ")");
        this.mInitializationResult = i;
        return this;
    }

    public SessionMetricsStats setSatelliteTechnology(int i) {
        logd("setSatelliteTechnology(" + i + ")");
        this.mRadioTechnology = i;
        return this;
    }

    public SessionMetricsStats setTerminationResult(int i) {
        logd("setTerminationResult(" + i + ")");
        this.mTerminationResult = i;
        return this;
    }

    public SessionMetricsStats setInitializationProcessingTime(long j) {
        logd("setInitializationProcessingTime(" + j + ")");
        this.mInitializationProcessingTimeMillis = j;
        return this;
    }

    public SessionMetricsStats setTerminationProcessingTime(long j) {
        logd("setTerminationProcessingTime(" + j + ")");
        this.mTerminationProcessingTimeMillis = j;
        return this;
    }

    public SessionMetricsStats setSessionDurationSec(int i) {
        logd("setSessionDuration(" + i + ")");
        this.mSessionDurationSec = i;
        return this;
    }

    public SessionMetricsStats addCountOfSuccessfulOutgoingDatagram(int i, long j) {
        logd("addCountOfSuccessfulOutgoingDatagram: datagramType=" + i);
        this.mDatagramStats.recordSuccessfulOutgoingDatagramStats(i, j);
        if (i == 3) {
            return this;
        }
        this.mCountOfSuccessfulOutgoingDatagram++;
        this.mShadowCountOfSuccessfulOutgoingDatagram++;
        return this;
    }

    public SessionMetricsStats addCountOfFailedOutgoingDatagram(int i, int i2) {
        logd("addCountOfFailedOutgoingDatagram: datagramType=" + i + "  resultCode = " + i2);
        this.mDatagramStats.addCountOfUnsuccessfulUserMessages(i, i2);
        if (i != 3) {
            this.mCountOfFailedOutgoingDatagram++;
            this.mShadowCountOfFailedOutgoingDatagram++;
            if (i2 == 18) {
                addCountOfTimedOutUserMessagesWaitingForConnection(i);
                return this;
            }
            if (i2 == 24) {
                addCountOfTimedOutUserMessagesWaitingForAck(i);
            }
        }
        return this;
    }

    private SessionMetricsStats addCountOfTimedOutUserMessagesWaitingForConnection(int i) {
        if (i == 3) {
            return this;
        }
        this.mCountOfTimedOutUserMessagesWaitingForConnection++;
        this.mShadowCountOfTimedOutUserMessagesWaitingForConnection++;
        logd("addCountOfTimedOutUserMessagesWaitingForConnection: current count=" + this.mCountOfTimedOutUserMessagesWaitingForConnection);
        return this;
    }

    private SessionMetricsStats addCountOfTimedOutUserMessagesWaitingForAck(int i) {
        if (i == 3) {
            return this;
        }
        this.mCountOfTimedOutUserMessagesWaitingForAck++;
        this.mShadowCountOfTimedOutUserMessagesWaitingForAck++;
        logd("addCountOfTimedOutUserMessagesWaitingForAck: current count=" + this.mCountOfTimedOutUserMessagesWaitingForAck);
        return this;
    }

    public SessionMetricsStats addCountOfSuccessfulIncomingDatagram() {
        this.mCountOfSuccessfulIncomingDatagram++;
        logd("addCountOfSuccessfulIncomingDatagram: current count=" + this.mCountOfSuccessfulIncomingDatagram);
        return this;
    }

    public SessionMetricsStats addCountOfFailedIncomingDatagram() {
        this.mCountOfIncomingDatagramFailed++;
        logd("addCountOfFailedIncomingDatagram: current count=" + this.mCountOfIncomingDatagramFailed);
        return this;
    }

    public SessionMetricsStats setIsDemoMode(boolean z) {
        this.mIsDemoMode = z;
        logd("setIsDemoMode(" + this.mIsDemoMode + ")");
        return this;
    }

    public SessionMetricsStats updateMaxNtnSignalStrengthLevel(int i) {
        if (i > this.mMaxNtnSignalStrengthLevel) {
            this.mMaxNtnSignalStrengthLevel = i;
        }
        logd("updateMaxNtnSignalsStrength: latest signal strength=" + i + ", max signal strength=" + this.mMaxNtnSignalStrengthLevel);
        return this;
    }

    public SessionMetricsStats setCarrierId(int i) {
        this.mCarrierId = i;
        logd("setCarrierId(" + i + ")");
        return this;
    }

    public SessionMetricsStats addCountOfSatelliteNotificationDisplayed() {
        this.mCountOfSatelliteNotificationDisplayed++;
        logd("addCountOfSatelliteNotificationDisplayed: current count=" + this.mCountOfSatelliteNotificationDisplayed);
        return this;
    }

    public SessionMetricsStats addCountOfAutoExitDueToScreenOff() {
        this.mCountOfAutoExitDueToScreenOff++;
        logd("addCountOfAutoExitDueToScreenOff: current count=" + this.mCountOfAutoExitDueToScreenOff);
        return this;
    }

    public SessionMetricsStats addCountOfAutoExitDueToTnNetwork() {
        this.mCountOfAutoExitDueToTnNetwork++;
        logd("addCountOfAutoExitDueToTnNetwork: current count=" + this.mCountOfAutoExitDueToTnNetwork);
        return this;
    }

    public SessionMetricsStats setIsEmergency(boolean z) {
        this.mIsEmergency = z;
        logd("setIsEmergency(" + this.mIsEmergency + ")");
        return this;
    }

    public SessionMetricsStats setIsNtnOnlyCarrier(boolean z) {
        this.mIsNtnOnlyCarrier = z;
        logd("setIsNtnOnlyCarrier(" + this.mIsNtnOnlyCarrier + ")");
        return this;
    }

    public SessionMetricsStats updateMaxInactivityDurationSec(int i) {
        if (i > this.mMaxInactivityDurationSec) {
            this.mMaxInactivityDurationSec = i;
        }
        logd("updateMaxInactivityDurationSec: latest inactivty duration (sec)=" + i + ", max inactivity duration=" + this.mMaxInactivityDurationSec);
        return this;
    }

    public SessionMetricsStats setSupportedConnectionMode(int i) {
        this.mSupportedConnectionMode = i;
        logd("setSupportedConnectionMode(" + this.mSupportedConnectionMode + ")");
        return this;
    }

    public SessionMetricsStats setSessionConnectionMode(int i) {
        this.mSessionConnectionMode = i;
        logd("setSessionConnectionMode(" + this.mSessionConnectionMode + ")");
        return this;
    }

    public SessionMetricsStats setPlmn(String str) {
        this.mPlmn = str;
        logd("plmn(" + this.mPlmn + ")");
        return this;
    }

    public void reportSessionMetrics() {
        SatelliteStats.SatelliteSessionParams satelliteSessionParamsBuild = new SatelliteStats.SatelliteSessionParams.Builder().setSatelliteServiceInitializationResult(this.mInitializationResult).setSatelliteTechnology(this.mRadioTechnology).setTerminationResult(this.mTerminationResult).setInitializationProcessingTime(this.mInitializationProcessingTimeMillis).setTerminationProcessingTime(this.mTerminationProcessingTimeMillis).setSessionDuration(this.mSessionDurationSec).setCountOfOutgoingDatagramSuccess(this.mCountOfSuccessfulOutgoingDatagram).setCountOfOutgoingDatagramFailed(this.mCountOfFailedOutgoingDatagram).setCountOfIncomingDatagramSuccess(this.mCountOfSuccessfulIncomingDatagram).setCountOfIncomingDatagramFailed(this.mCountOfIncomingDatagramFailed).setIsDemoMode(this.mIsDemoMode).setMaxNtnSignalStrengthLevel(this.mMaxNtnSignalStrengthLevel).setCarrierId(this.mCarrierId).setCountOfSatelliteNotificationDisplayed(this.mCountOfSatelliteNotificationDisplayed).setCountOfAutoExitDueToScreenOff(this.mCountOfAutoExitDueToScreenOff).setCountOfAutoExitDueToTnNetwork(this.mCountOfAutoExitDueToTnNetwork).setIsEmergency(this.mIsEmergency).setIsNtnOnlyCarrier(this.mIsNtnOnlyCarrier).setMaxInactivityDurationSec(this.mMaxInactivityDurationSec).setSupportedConnectionMode(this.mSupportedConnectionMode).setSessionConnectionMode(this.mSessionConnectionMode).setPlmn(this.mPlmn).build();
        logd("reportSessionMetrics: " + satelliteSessionParamsBuild.toString());
        SatelliteStats.getInstance().onSatelliteSessionMetrics(satelliteSessionParamsBuild);
        initializeSessionMetricsParam();
    }

    public void requestSatelliteSessionStats(int i, ResultReceiver resultReceiver) {
        String str = TAG;
        Log.i(str, "requestSatelliteSessionStats called");
        Bundle bundle = new Bundle();
        bundle.putParcelable("session_stats", new SatelliteSessionStats.Builder().setCountOfSuccessfulUserMessages(this.mShadowCountOfSuccessfulOutgoingDatagram).setCountOfUnsuccessfulUserMessages(this.mShadowCountOfFailedOutgoingDatagram).setCountOfTimedOutUserMessagesWaitingForConnection(this.mShadowCountOfTimedOutUserMessagesWaitingForConnection).setCountOfTimedOutUserMessagesWaitingForAck(this.mShadowCountOfTimedOutUserMessagesWaitingForAck).setCountOfUserMessagesInQueueToBeSent(DatagramDispatcher.getInstance().getPendingUserMessagesCount()).build());
        this.mDatagramStats.resetCountOfUserMessagesInQueueToBeSent();
        DatagramDispatcher.getInstance().updateSessionStatsWithPendingUserMsgCount(this.mDatagramStats);
        bundle.putParcelable("session_stats_v2", this.mDatagramStats);
        Log.i(str, "[END] DatagramStats = " + this.mDatagramStats);
        resultReceiver.send(0, bundle);
    }

    public long getSessionInitializationProcessingTimeMillis() {
        return this.mInitializationProcessingTimeMillis;
    }

    public long getSessionTerminationProcessingTimeMillis() {
        return this.mTerminationProcessingTimeMillis;
    }

    private void initializeSessionMetricsParam() {
        this.mInitializationResult = 0;
        this.mRadioTechnology = 0;
        this.mTerminationResult = 0;
        this.mInitializationProcessingTimeMillis = 0L;
        this.mTerminationProcessingTimeMillis = 0L;
        this.mSessionDurationSec = 0;
        this.mCountOfSuccessfulOutgoingDatagram = 0;
        this.mCountOfFailedOutgoingDatagram = 0;
        this.mCountOfTimedOutUserMessagesWaitingForConnection = 0;
        this.mCountOfTimedOutUserMessagesWaitingForAck = 0;
        this.mCountOfSuccessfulIncomingDatagram = 0;
        this.mCountOfIncomingDatagramFailed = 0;
        this.mIsDemoMode = false;
        this.mMaxNtnSignalStrengthLevel = 0;
        this.mCarrierId = -1;
        this.mCountOfSatelliteNotificationDisplayed = 0;
        this.mCountOfAutoExitDueToScreenOff = 0;
        this.mCountOfAutoExitDueToTnNetwork = 0;
        this.mIsEmergency = false;
        this.mIsNtnOnlyCarrier = false;
        this.mMaxInactivityDurationSec = 0;
        this.mSupportedConnectionMode = 0;
        this.mSessionConnectionMode = 0;
        this.mPlmn = SatelliteConstants.DEFAULT_PLMN;
    }

    public void resetSessionStatsShadowCounters() {
        logd("resetTheStatsCounters");
        this.mShadowCountOfSuccessfulOutgoingDatagram = 0;
        this.mShadowCountOfFailedOutgoingDatagram = 0;
        this.mShadowCountOfTimedOutUserMessagesWaitingForConnection = 0;
        this.mShadowCountOfTimedOutUserMessagesWaitingForAck = 0;
        this.mDatagramStats.clear();
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }

    private static void loge(String str) {
        Log.e(TAG, str);
    }
}
