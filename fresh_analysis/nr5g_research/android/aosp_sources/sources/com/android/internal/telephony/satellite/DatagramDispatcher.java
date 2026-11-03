package com.android.internal.telephony.satellite;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PersistentLogger;
import android.telephony.satellite.SatelliteDatagram;
import android.telephony.satellite.SatelliteManager;
import android.telephony.satellite.SatelliteSessionStats;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.metrics.ControllerMetricsStats;
import com.android.internal.telephony.satellite.metrics.SessionMetricsStats;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongUnaryOperator;

/* JADX INFO: loaded from: classes.dex */
public class DatagramDispatcher extends Handler {
    private static final Long TIMEOUT_DATAGRAM_DELAY_IN_DEMO_MODE = 10000L;
    private static AtomicLong mNextDatagramId = new AtomicLong(0);
    private static DatagramDispatcher sInstance;
    private AtomicInteger mConnectedStateCounter;
    private final Context mContext;
    private final ControllerMetricsStats mControllerMetricsStats;
    private final DatagramController mDatagramController;
    private AtomicLong mDemoTimeoutDuration;
    private final FeatureFlags mFeatureFlags;
    private AtomicBoolean mHasEnteredConnectedState;
    private AtomicBoolean mIsAligned;
    private AtomicBoolean mIsDemoMode;
    private AtomicBoolean mIsEmergencyCommunicationEstablished;
    private AtomicBoolean mIsMtSmsPollingThrottled;
    private AtomicInteger mLastSendRequestDatagramType;
    private final Object mLock;
    private AtomicInteger mModemState;
    private final LinkedHashMap mPendingEmergencyDatagramsMap;
    private final LinkedHashMap mPendingNonEmergencyDatagramsMap;
    private final LinkedHashMap mPendingSmsMap;
    private PersistentLogger mPersistentLogger;
    private DatagramDispatcherHandlerRequest mSendSatelliteDatagramRequest;
    private AtomicBoolean mSendingInProgress;
    private final SessionMetricsStats mSessionMetricsStats;
    private AtomicBoolean mShouldPollMtSms;
    private AtomicBoolean mShouldSendDatagramToModemInDemoMode;
    private AtomicLong mSmsTransmissionStartTime;
    private AtomicLong mWaitTimeForDatagramSendingForLastMessageResponse;
    private AtomicLong mWaitTimeForDatagramSendingResponse;

    public static DatagramDispatcher make(Context context, Looper looper, FeatureFlags featureFlags, DatagramController datagramController) {
        if (sInstance == null) {
            sInstance = new DatagramDispatcher(context, looper, featureFlags, datagramController);
        }
        return sInstance;
    }

    public static DatagramDispatcher getInstance() {
        if (sInstance == null) {
            loge("DatagramDispatcher was not yet initialized.");
        }
        return sInstance;
    }

    protected DatagramDispatcher(Context context, Looper looper, FeatureFlags featureFlags, DatagramController datagramController) {
        super(looper);
        this.mPersistentLogger = null;
        this.mIsDemoMode = new AtomicBoolean(false);
        this.mIsAligned = new AtomicBoolean(false);
        this.mShouldSendDatagramToModemInDemoMode = null;
        this.mDemoTimeoutDuration = new AtomicLong(TIMEOUT_DATAGRAM_DELAY_IN_DEMO_MODE.longValue());
        this.mIsEmergencyCommunicationEstablished = new AtomicBoolean(false);
        this.mSendingInProgress = new AtomicBoolean(false);
        this.mWaitTimeForDatagramSendingResponse = new AtomicLong(0L);
        this.mWaitTimeForDatagramSendingForLastMessageResponse = new AtomicLong(0L);
        this.mLastSendRequestDatagramType = new AtomicInteger(0);
        this.mModemState = new AtomicInteger(-1);
        this.mHasEnteredConnectedState = new AtomicBoolean(false);
        this.mShouldPollMtSms = new AtomicBoolean(false);
        this.mIsMtSmsPollingThrottled = new AtomicBoolean(false);
        this.mConnectedStateCounter = new AtomicInteger(0);
        this.mSmsTransmissionStartTime = new AtomicLong(0L);
        this.mSendSatelliteDatagramRequest = null;
        this.mLock = new Object();
        this.mPendingEmergencyDatagramsMap = new LinkedHashMap();
        this.mPendingNonEmergencyDatagramsMap = new LinkedHashMap();
        this.mPendingSmsMap = new LinkedHashMap();
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mDatagramController = datagramController;
        this.mControllerMetricsStats = ControllerMetricsStats.getInstance();
        this.mSessionMetricsStats = SessionMetricsStats.getInstance();
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        this.mSendingInProgress.set(false);
        this.mWaitTimeForDatagramSendingResponse.set(getWaitForDatagramSendingResponseTimeoutMillis());
        this.mWaitTimeForDatagramSendingForLastMessageResponse.set(getWaitForDatagramSendingResponseForLastMessageTimeoutMillis());
    }

    private static final class DatagramDispatcherHandlerRequest {
        public Object argument;
        public Phone phone;
        public Object result;

        DatagramDispatcherHandlerRequest(Object obj, Phone phone) {
            this.argument = obj;
            this.phone = phone;
        }
    }

    private static final class SendSatelliteDatagramArgument {
        public Consumer<Integer> callback;
        public SatelliteDatagram datagram;
        public long datagramId;
        public long datagramStartTime;
        public int datagramType;
        public boolean needFullScreenPointingUI;
        public boolean skipCheckingSatelliteAligned = false;
        public int subId;

        SendSatelliteDatagramArgument(int i, long j, int i2, SatelliteDatagram satelliteDatagram, boolean z, Consumer consumer) {
            this.subId = i;
            this.datagramId = j;
            this.datagramType = i2;
            this.datagram = satelliteDatagram;
            this.needFullScreenPointingUI = z;
            this.callback = consumer;
        }

        public int getDatagramRoundedSizeBytes() {
            if (this.datagram.getSatelliteDatagram() != null) {
                return (int) (Math.round(((double) this.datagram.getSatelliteDatagram().length) / 10.0d) * 10);
            }
            return 0;
        }

        public void setDatagramStartTime() {
            long jCurrentTimeMillis = this.datagramStartTime;
            if (jCurrentTimeMillis == 0) {
                jCurrentTimeMillis = System.currentTimeMillis();
            }
            this.datagramStartTime = jCurrentTimeMillis;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                plogd("CMD_SEND_SATELLITE_DATAGRAM mIsDemoMode=" + this.mIsDemoMode.get() + ", shouldSendDatagramToModemInDemoMode=" + shouldSendDatagramToModemInDemoMode());
                DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest = (DatagramDispatcherHandlerRequest) message.obj;
                SendSatelliteDatagramArgument sendSatelliteDatagramArgument = (SendSatelliteDatagramArgument) datagramDispatcherHandlerRequest.argument;
                sendSatelliteDatagramArgument.setDatagramStartTime();
                Message messageObtainMessage = obtainMessage(2, datagramDispatcherHandlerRequest);
                if (this.mIsDemoMode.get() && !shouldSendDatagramToModemInDemoMode()) {
                    AsyncResult.forMessage(messageObtainMessage, 0, (Throwable) null);
                    sendMessageDelayed(messageObtainMessage, getDemoTimeoutDuration());
                    return;
                } else {
                    SatelliteModemInterface.getInstance().sendSatelliteDatagram(sendSatelliteDatagramArgument.datagram, SatelliteServiceUtils.isSosMessage(sendSatelliteDatagramArgument.datagramType), sendSatelliteDatagramArgument.needFullScreenPointingUI, messageObtainMessage);
                    startWaitForDatagramSendingResponseTimer(sendSatelliteDatagramArgument);
                    return;
                }
            case 2:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest2 = (DatagramDispatcherHandlerRequest) asyncResult.userObj;
                int satelliteError = SatelliteServiceUtils.getSatelliteError(asyncResult, "sendDatagram");
                SendSatelliteDatagramArgument sendSatelliteDatagramArgument2 = (SendSatelliteDatagramArgument) datagramDispatcherHandlerRequest2.argument;
                if (this.mIsDemoMode.get() && satelliteError == 0) {
                    if (sendSatelliteDatagramArgument2.skipCheckingSatelliteAligned) {
                        plogd("Satellite was already aligned. No need to check alignment again");
                    } else if (this.mDatagramController.waitForAligningToSatellite(this.mIsAligned.get())) {
                        plogd("Satellite is not aligned in demo mode, wait for the alignment.");
                        startSatelliteAlignedTimer(datagramDispatcherHandlerRequest2);
                        return;
                    }
                }
                plogd("EVENT_SEND_SATELLITE_DATAGRAM_DONE error: " + satelliteError + ", mIsDemoMode=" + this.mIsDemoMode.get());
                if (!shouldProcessEventSendSatelliteDatagramDone(sendSatelliteDatagramArgument2)) {
                    plogw("The message " + sendSatelliteDatagramArgument2.datagramId + " was already processed");
                    return;
                }
                stopWaitForDatagramSendingResponseTimer();
                this.mSendingInProgress.set(false);
                reportSendDatagramCompleted(sendSatelliteDatagramArgument2, satelliteError);
                synchronized (this.mLock) {
                    try {
                        if (SatelliteServiceUtils.isSosMessage(sendSatelliteDatagramArgument2.datagramType)) {
                            this.mPendingEmergencyDatagramsMap.remove(Long.valueOf(sendSatelliteDatagramArgument2.datagramId));
                            if (satelliteError == 0) {
                                this.mIsEmergencyCommunicationEstablished.set(true);
                            }
                        } else {
                            this.mPendingNonEmergencyDatagramsMap.remove(Long.valueOf(sendSatelliteDatagramArgument2.datagramId));
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                    break;
                }
                DatagramController datagramController = this.mDatagramController;
                if (satelliteError == 0) {
                    datagramController.updateSendStatus(sendSatelliteDatagramArgument2.subId, sendSatelliteDatagramArgument2.datagramType, 2, getPendingMessagesCount(), satelliteError);
                    startWaitForSimulatedPollDatagramsDelayTimer(datagramDispatcherHandlerRequest2);
                } else {
                    datagramController.updateSendStatus(sendSatelliteDatagramArgument2.subId, sendSatelliteDatagramArgument2.datagramType, 3, getPendingMessagesCount(), satelliteError);
                }
                if (getPendingMessagesCount() > 0) {
                    sendSatelliteDatagramArgument2.callback.accept(Integer.valueOf(satelliteError));
                    sendPendingMessages();
                    return;
                } else {
                    this.mDatagramController.updateSendStatus(sendSatelliteDatagramArgument2.subId, sendSatelliteDatagramArgument2.datagramType, 0, 0, 0);
                    sendSatelliteDatagramArgument2.callback.accept(Integer.valueOf(satelliteError));
                    return;
                }
            case 3:
                handleEventSatelliteAlignedTimeout((DatagramDispatcherHandlerRequest) message.obj);
                return;
            case 4:
                handleEventDatagramWaitForConnectedStateTimedOut(((Integer) message.obj).intValue());
                return;
            case 5:
                handleEventWaitForDatagramSendingResponseTimedOut((SendSatelliteDatagramArgument) message.obj);
                return;
            case 6:
            default:
                plogw("DatagramDispatcherHandler: unexpected message code: " + message.what);
                return;
            case 7:
                handleEventWaitForSimulatedPollDatagramsDelayTimedOut((SendSatelliteDatagramArgument) ((DatagramDispatcherHandlerRequest) message.obj).argument);
                return;
            case 8:
                SmsDispatchersController.PendingRequest pendingRequest = (SmsDispatchersController.PendingRequest) message.obj;
                Phone satellitePhone = SatelliteController.getInstance().getSatellitePhone();
                if (satellitePhone == null) {
                    ploge("CMD_SEND_SMS: satellitePhone is null.");
                    return;
                }
                SmsDispatchersController smsDispatchersController = satellitePhone.getSmsDispatchersController();
                if (smsDispatchersController == null) {
                    ploge("CMD_SEND_SMS: smsDispatchersController is null.");
                    return;
                } else {
                    this.mSmsTransmissionStartTime.set(System.currentTimeMillis());
                    smsDispatchersController.sendCarrierRoamingNbIotNtnText(pendingRequest);
                    return;
                }
            case 9:
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleEventSendSmsDone(((Integer) someArgs.arg1).intValue(), ((Long) someArgs.arg2).longValue(), ((Boolean) someArgs.arg3).booleanValue());
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 10:
                this.mIsMtSmsPollingThrottled.set(false);
                if (allowMtSmsPolling()) {
                    sendMessage(obtainMessage(11));
                    return;
                }
                return;
            case 11:
                plogd("CMD_SEND_MT_SMS_POLLING_MESSAGE");
                handleCmdSendMtSmsPollingMessage();
                return;
            case 12:
                plogd("EVENT_SATELLITE_MODEM_STATE_CHANGED");
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                try {
                    handleEventSatelliteModemStateChanged(((Integer) someArgs2.arg1).intValue());
                    return;
                } finally {
                    someArgs2.recycle();
                }
        }
    }

    public void sendSatelliteDatagram(int i, int i2, SatelliteDatagram satelliteDatagram, boolean z, Consumer<Integer> consumer) {
        Phone phone = SatelliteServiceUtils.getPhone();
        long andUpdate = mNextDatagramId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.satellite.DatagramDispatcher$$ExternalSyntheticLambda0
            @Override // java.util.function.LongUnaryOperator
            public final long applyAsLong(long j) {
                return DatagramDispatcher.$r8$lambda$yVNEDUTzdfpOei2ojryNrcANNKY(j);
            }
        });
        SendSatelliteDatagramArgument sendSatelliteDatagramArgument = new SendSatelliteDatagramArgument(i, andUpdate, i2, satelliteDatagram, z, consumer);
        this.mLastSendRequestDatagramType.set(i2);
        synchronized (this.mLock) {
            try {
                if (SatelliteServiceUtils.isSosMessage(i2)) {
                    this.mPendingEmergencyDatagramsMap.put(Long.valueOf(andUpdate), sendSatelliteDatagramArgument);
                } else {
                    this.mPendingNonEmergencyDatagramsMap.put(Long.valueOf(andUpdate), sendSatelliteDatagramArgument);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (this.mDatagramController.needsWaitingForSatelliteConnected(i2)) {
            plogd("sendDatagram: wait for satellite connected");
            this.mDatagramController.updateSendStatus(i, i2, 8, getPendingMessagesCount(), 0);
            startDatagramWaitForConnectedStateTimer(sendSatelliteDatagramArgument.datagramType);
        } else if (!this.mSendingInProgress.get() && this.mDatagramController.isPollingInIdleState()) {
            this.mSendingInProgress.set(true);
            this.mDatagramController.updateSendStatus(i, i2, 1, getPendingMessagesCount(), 0);
            sendRequestAsync(1, sendSatelliteDatagramArgument, phone);
        } else {
            plogd("sendDatagram: mSendingInProgress=" + this.mSendingInProgress.get() + ", isPollingInIdleState=" + this.mDatagramController.isPollingInIdleState());
        }
    }

    public static /* synthetic */ long $r8$lambda$yVNEDUTzdfpOei2ojryNrcANNKY(long j) {
        return (j + 1) % DatagramController.MAX_DATAGRAM_ID;
    }

    public void retrySendingDatagrams() {
        sendPendingMessages();
    }

    protected void setDemoMode(boolean z) {
        this.mIsDemoMode.set(z);
        plogd("setDemoMode: mIsDemoMode=" + z);
    }

    public void setDeviceAlignedWithSatellite(boolean z) {
        this.mIsAligned.set(z);
        plogd("setDeviceAlignedWithSatellite: " + z);
        if (z && this.mIsDemoMode.get()) {
            handleEventSatelliteAligned();
        }
        if (allowMtSmsPolling()) {
            sendMessage(obtainMessage(11));
        }
    }

    private void startSatelliteAlignedTimer(DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest) {
        if (isSatelliteAlignedTimerStarted()) {
            plogd("Satellite aligned timer was already started");
        } else {
            this.mSendSatelliteDatagramRequest = datagramDispatcherHandlerRequest;
            sendMessageDelayed(obtainMessage(3, datagramDispatcherHandlerRequest), getSatelliteAlignedTimeoutDuration());
        }
    }

    protected long getSatelliteAlignedTimeoutDuration() {
        return this.mDatagramController.getSatelliteAlignedTimeoutDuration();
    }

    private void handleEventSatelliteAligned() {
        if (isSatelliteAlignedTimerStarted()) {
            stopSatelliteAlignedTimer();
            DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest = this.mSendSatelliteDatagramRequest;
            if (datagramDispatcherHandlerRequest == null) {
                ploge("handleEventSatelliteAligned: mSendSatelliteDatagramRequest is null");
                return;
            }
            ((SendSatelliteDatagramArgument) datagramDispatcherHandlerRequest.argument).skipCheckingSatelliteAligned = true;
            Message messageObtainMessage = obtainMessage(2, datagramDispatcherHandlerRequest);
            this.mSendSatelliteDatagramRequest = null;
            AsyncResult.forMessage(messageObtainMessage, (Object) null, (Throwable) null);
            messageObtainMessage.sendToTarget();
            plogd("handleEventSatelliteAligned: EVENT_SEND_SATELLITE_DATAGRAM_DONE");
        }
    }

    private void handleEventSatelliteAlignedTimeout(DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest) {
        plogd("handleEventSatelliteAlignedTimeout");
        this.mSendSatelliteDatagramRequest = null;
        SatelliteManager.SatelliteException satelliteException = new SatelliteManager.SatelliteException(18);
        Message messageObtainMessage = obtainMessage(2, datagramDispatcherHandlerRequest);
        AsyncResult.forMessage(messageObtainMessage, (Object) null, satelliteException);
        messageObtainMessage.sendToTarget();
    }

    private boolean isSatelliteAlignedTimerStarted() {
        return hasMessages(3);
    }

    private void stopSatelliteAlignedTimer() {
        removeMessages(3);
    }

    private void sendPendingMessages() {
        plogd("sendPendingMessages");
        if (getPendingDatagramCount() > 0) {
            sendPendingDatagrams();
        } else if (getPendingSmsCount() > 0) {
            sendPendingSms();
        }
    }

    private void sendPendingDatagrams() {
        Set setEntrySet;
        plogd("sendPendingDatagrams()");
        if (!this.mDatagramController.isPollingInIdleState()) {
            plogd("sendPendingDatagrams: modem is receiving datagrams");
            return;
        }
        if (getPendingDatagramCount() <= 0) {
            plogd("sendPendingDatagrams: no pending datagrams to send");
            return;
        }
        Phone phone = SatelliteServiceUtils.getPhone();
        synchronized (this.mLock) {
            try {
                if (!this.mSendingInProgress.get() && !this.mPendingEmergencyDatagramsMap.isEmpty()) {
                    setEntrySet = this.mPendingEmergencyDatagramsMap.entrySet();
                } else {
                    setEntrySet = (this.mSendingInProgress.get() || this.mPendingNonEmergencyDatagramsMap.isEmpty()) ? null : this.mPendingNonEmergencyDatagramsMap.entrySet();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (setEntrySet == null || !setEntrySet.iterator().hasNext()) {
            return;
        }
        SendSatelliteDatagramArgument sendSatelliteDatagramArgument = (SendSatelliteDatagramArgument) ((Map.Entry) setEntrySet.iterator().next()).getValue();
        if (this.mDatagramController.needsWaitingForSatelliteConnected(sendSatelliteDatagramArgument.datagramType)) {
            plogd("sendPendingDatagrams: wait for satellite connected");
            this.mDatagramController.updateSendStatus(sendSatelliteDatagramArgument.subId, sendSatelliteDatagramArgument.datagramType, 8, getPendingMessagesCount(), 0);
            startDatagramWaitForConnectedStateTimer(sendSatelliteDatagramArgument.datagramType);
        } else {
            this.mSendingInProgress.set(true);
            this.mDatagramController.updateSendStatus(sendSatelliteDatagramArgument.subId, sendSatelliteDatagramArgument.datagramType, 1, getPendingMessagesCount(), 0);
            sendRequestAsync(1, sendSatelliteDatagramArgument, phone);
        }
    }

    private void sendErrorCodeAndCleanupPendingDatagrams(LinkedHashMap linkedHashMap, int i) {
        synchronized (this.mLock) {
            try {
                if (linkedHashMap.size() == 0) {
                    return;
                }
                ploge("sendErrorCodeAndCleanupPendingDatagrams: cleaning up resources");
                Iterator it = linkedHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    SendSatelliteDatagramArgument sendSatelliteDatagramArgument = (SendSatelliteDatagramArgument) ((Map.Entry) it.next()).getValue();
                    reportSendDatagramCompleted(sendSatelliteDatagramArgument, i);
                    sendSatelliteDatagramArgument.callback.accept(Integer.valueOf(i));
                }
                linkedHashMap.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void abortSendingPendingDatagrams(int i, int i2) {
        plogd("abortSendingPendingDatagrams()");
        synchronized (this.mLock) {
            sendErrorCodeAndCleanupPendingDatagrams(this.mPendingEmergencyDatagramsMap, i2);
            sendErrorCodeAndCleanupPendingDatagrams(this.mPendingNonEmergencyDatagramsMap, i2);
            sendErrorCodeAndCleanupPendingSms(this.mPendingSmsMap, i2);
        }
    }

    public int getPendingMessagesCount() {
        return getPendingDatagramCount() + getPendingSmsCount();
    }

    public int getPendingDatagramCount() {
        int size;
        synchronized (this.mLock) {
            size = this.mPendingEmergencyDatagramsMap.size() + this.mPendingNonEmergencyDatagramsMap.size();
        }
        return size;
    }

    public int getPendingSmsCount() {
        int size;
        synchronized (this.mLock) {
            size = this.mPendingSmsMap.size();
        }
        return size;
    }

    public int getPendingUserMessagesCount() {
        int size;
        synchronized (this.mLock) {
            try {
                Iterator it = this.mPendingNonEmergencyDatagramsMap.entrySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    if (((SendSatelliteDatagramArgument) ((Map.Entry) it.next()).getValue()).datagramType != 3) {
                        i++;
                    }
                }
                size = i + this.mPendingEmergencyDatagramsMap.size();
            } catch (Throwable th) {
                throw th;
            }
        }
        return size;
    }

    private void sendRequestAsync(int i, Object obj, Phone phone) {
        obtainMessage(i, new DatagramDispatcherHandlerRequest(obj, phone)).sendToTarget();
    }

    private void reportSendSmsCompleted(SmsDispatchersController.PendingRequest pendingRequest, int i) {
        int i2 = pendingRequest.isMtSmsPolling ? 7 : 6;
        if (i == 0) {
            long jCurrentTimeMillis = this.mSmsTransmissionStartTime.get() > 0 ? System.currentTimeMillis() - this.mSmsTransmissionStartTime.get() : 0L;
            this.mControllerMetricsStats.reportOutgoingDatagramSuccessCount(i2, false);
            this.mSessionMetricsStats.addCountOfSuccessfulOutgoingDatagram(i2, jCurrentTimeMillis);
        } else {
            this.mControllerMetricsStats.reportOutgoingDatagramFailCount(i2, false);
            this.mSessionMetricsStats.addCountOfFailedOutgoingDatagram(i2, i);
        }
    }

    private void reportSendDatagramCompleted(SendSatelliteDatagramArgument sendSatelliteDatagramArgument, int i) {
        long jCurrentTimeMillis = sendSatelliteDatagramArgument.datagramStartTime > 0 ? System.currentTimeMillis() - sendSatelliteDatagramArgument.datagramStartTime : 0L;
        boolean z = this.mIsDemoMode.get();
        SatelliteStats.getInstance().onSatelliteOutgoingDatagramMetrics(new SatelliteStats.SatelliteOutgoingDatagramParams.Builder().setDatagramType(sendSatelliteDatagramArgument.datagramType).setResultCode(i).setDatagramSizeBytes(sendSatelliteDatagramArgument.getDatagramRoundedSizeBytes()).setDatagramTransferTimeMillis(jCurrentTimeMillis).setIsDemoMode(z).setCarrierId(SatelliteController.getInstance().getSatelliteCarrierId()).setIsNtnOnlyCarrier(SatelliteController.getInstance().isNtnOnlyCarrier()).setSupportedConnectionMode(SatelliteController.getInstance().getSupportedConnectTypeMetrics()).setSessionConnectionMode(SatelliteController.getInstance().getSessionConnectTypeMetrics()).setPlmn(SatelliteController.getInstance().getSatellitePlmnForMetrics()).build());
        ControllerMetricsStats controllerMetricsStats = this.mControllerMetricsStats;
        if (i == 0) {
            controllerMetricsStats.reportOutgoingDatagramSuccessCount(sendSatelliteDatagramArgument.datagramType, z);
            this.mSessionMetricsStats.addCountOfSuccessfulOutgoingDatagram(sendSatelliteDatagramArgument.datagramType, jCurrentTimeMillis);
        } else {
            controllerMetricsStats.reportOutgoingDatagramFailCount(sendSatelliteDatagramArgument.datagramType, z);
            this.mSessionMetricsStats.addCountOfFailedOutgoingDatagram(sendSatelliteDatagramArgument.datagramType, i);
        }
    }

    public void destroy() {
        sInstance = null;
    }

    public void onSatelliteModemStateChanged(int i) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        sendMessage(obtainMessage(12, someArgsObtain));
    }

    private void handleEventSatelliteModemStateChanged(int i) {
        plogd("handleEventSatelliteModemStateChanged: state = " + i);
        this.mModemState.set(i);
        if (i == 4 || i == 5) {
            plogd("onSatelliteModemStateChanged: cleaning up resources");
            cleanUpResources();
        } else if (i == 0) {
            sendPendingMessages();
        }
        if (i == 7) {
            this.mHasEnteredConnectedState.set(true);
            this.mConnectedStateCounter.incrementAndGet();
            if (isFirstConnected()) {
                this.mShouldPollMtSms.set(shouldPollMtSms());
            }
            if (isDatagramWaitForConnectedStateTimerStarted()) {
                stopDatagramWaitForConnectedStateTimer();
                sendPendingMessages();
            }
        }
        if (i == 6 && this.mHasEnteredConnectedState.get()) {
            this.mHasEnteredConnectedState.set(false);
            this.mShouldPollMtSms.set(shouldPollMtSms());
        }
        if (allowMtSmsPolling()) {
            sendMessage(obtainMessage(11));
        }
    }

    private boolean isFirstConnected() {
        return this.mConnectedStateCounter.get() == 1;
    }

    private void cleanUpResources() {
        plogd("cleanUpResources");
        this.mSendingInProgress.set(false);
        this.mIsEmergencyCommunicationEstablished.set(false);
        int selectedSatelliteSubId = SatelliteController.getInstance().getSelectedSatelliteSubId();
        if (getPendingMessagesCount() > 0) {
            this.mDatagramController.updateSendStatus(selectedSatelliteSubId, this.mLastSendRequestDatagramType.get(), 3, getPendingMessagesCount(), 15);
        }
        this.mDatagramController.updateSendStatus(selectedSatelliteSubId, this.mLastSendRequestDatagramType.get(), 0, 0, 0);
        abortSendingPendingDatagrams(selectedSatelliteSubId, 15);
        stopSatelliteAlignedTimer();
        stopDatagramWaitForConnectedStateTimer();
        stopWaitForDatagramSendingResponseTimer();
        stopWaitForSimulatedPollDatagramsDelayTimer();
        this.mIsDemoMode.set(false);
        this.mSendSatelliteDatagramRequest = null;
        this.mIsAligned.set(false);
        this.mLastSendRequestDatagramType.set(0);
        this.mModemState.set(-1);
        this.mHasEnteredConnectedState.set(false);
        this.mShouldPollMtSms.set(false);
        this.mConnectedStateCounter.set(0);
        stopMtSmsPollingThrottle();
    }

    protected boolean isEmergencyCommunicationEstablished() {
        return this.mIsEmergencyCommunicationEstablished.get();
    }

    private void startDatagramWaitForConnectedStateTimer(int i) {
        if (isDatagramWaitForConnectedStateTimerStarted()) {
            plogd("DatagramWaitForConnectedStateTimer is already started");
        } else {
            sendMessageDelayed(obtainMessage(4, Integer.valueOf(i)), this.mDatagramController.getDatagramWaitTimeForConnectedState(SatelliteServiceUtils.isLastSosMessage(i)));
        }
    }

    private void stopDatagramWaitForConnectedStateTimer() {
        removeMessages(4);
    }

    public boolean isDatagramWaitForConnectedStateTimerStarted() {
        return hasMessages(4);
    }

    void setWaitTimeForDatagramSendingResponse(boolean z, long j) {
        AtomicLong atomicLong = this.mWaitTimeForDatagramSendingResponse;
        if (z) {
            atomicLong.set(getWaitForDatagramSendingResponseTimeoutMillis());
        } else {
            atomicLong.set(j);
        }
    }

    private void startWaitForDatagramSendingResponseTimer(SendSatelliteDatagramArgument sendSatelliteDatagramArgument) {
        long j;
        if (hasMessages(5)) {
            plogd("WaitForDatagramSendingResponseTimer was already started");
            return;
        }
        if (SatelliteServiceUtils.isLastSosMessage(sendSatelliteDatagramArgument.datagramType)) {
            j = this.mWaitTimeForDatagramSendingForLastMessageResponse.get();
        } else {
            j = this.mWaitTimeForDatagramSendingResponse.get();
        }
        logd("startWaitForDatagramSendingResponseTimer: datagramType=" + sendSatelliteDatagramArgument.datagramType + ", waitTime=" + j);
        sendMessageDelayed(obtainMessage(5, sendSatelliteDatagramArgument), j);
    }

    private void stopWaitForDatagramSendingResponseTimer() {
        removeMessages(5);
    }

    private void handleEventDatagramWaitForConnectedStateTimedOut(int i) {
        plogw("Timed out to wait for satellite connected before sending datagrams");
        int selectedSatelliteSubId = SatelliteController.getInstance().getSelectedSatelliteSubId();
        this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i, 3, getPendingMessagesCount(), 18);
        this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i, 0, 0, 0);
        abortSendingPendingDatagrams(selectedSatelliteSubId, 18);
    }

    private boolean shouldSendDatagramToModemInDemoMode() {
        AtomicBoolean atomicBoolean = this.mShouldSendDatagramToModemInDemoMode;
        if (atomicBoolean != null) {
            return atomicBoolean.get();
        }
        try {
            AtomicBoolean atomicBoolean2 = new AtomicBoolean(this.mContext.getResources().getBoolean(R.bool.config_supportsInsecureLockScreen));
            this.mShouldSendDatagramToModemInDemoMode = atomicBoolean2;
            return atomicBoolean2.get();
        } catch (Resources.NotFoundException e) {
            ploge("shouldSendDatagramToModemInDemoMode: id= 17891948, ex=" + e);
            return false;
        }
    }

    private long getWaitForDatagramSendingResponseTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_duration);
    }

    private long getWaitForDatagramSendingResponseForLastMessageTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_delay);
    }

    private boolean shouldProcessEventSendSatelliteDatagramDone(SendSatelliteDatagramArgument sendSatelliteDatagramArgument) {
        synchronized (this.mLock) {
            try {
                if (SatelliteServiceUtils.isSosMessage(sendSatelliteDatagramArgument.datagramType)) {
                    return this.mPendingEmergencyDatagramsMap.containsKey(Long.valueOf(sendSatelliteDatagramArgument.datagramId));
                }
                return this.mPendingNonEmergencyDatagramsMap.containsKey(Long.valueOf(sendSatelliteDatagramArgument.datagramId));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void handleEventWaitForDatagramSendingResponseTimedOut(SendSatelliteDatagramArgument sendSatelliteDatagramArgument) {
        plogw("Timed out to wait for the response of the request to send the datagram " + sendSatelliteDatagramArgument.datagramId);
        SatelliteModemInterface.getInstance().abortSendingSatelliteDatagrams(obtainMessage(6, sendSatelliteDatagramArgument));
        this.mSendingInProgress.set(false);
        this.mDatagramController.updateSendStatus(sendSatelliteDatagramArgument.subId, sendSatelliteDatagramArgument.datagramType, 3, getPendingMessagesCount(), 24);
        this.mDatagramController.updateSendStatus(sendSatelliteDatagramArgument.subId, sendSatelliteDatagramArgument.datagramType, 0, 0, 0);
        sendSatelliteDatagramArgument.callback.accept(24);
        reportSendDatagramCompleted(sendSatelliteDatagramArgument, 24);
        synchronized (this.mLock) {
            try {
                if (SatelliteServiceUtils.isSosMessage(sendSatelliteDatagramArgument.datagramType)) {
                    this.mPendingEmergencyDatagramsMap.remove(Long.valueOf(sendSatelliteDatagramArgument.datagramId));
                } else {
                    this.mPendingNonEmergencyDatagramsMap.remove(Long.valueOf(sendSatelliteDatagramArgument.datagramId));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        abortSendingPendingDatagrams(sendSatelliteDatagramArgument.subId, 24);
    }

    protected void setShouldSendDatagramToModemInDemoMode(Boolean bool) {
        StringBuilder sb = new StringBuilder("setShouldSendDatagramToModemInDemoMode(");
        sb.append(bool == null ? "null" : bool);
        sb.append(")");
        plogd(sb.toString());
        if (bool == null) {
            this.mShouldSendDatagramToModemInDemoMode = null;
            return;
        }
        AtomicBoolean atomicBoolean = this.mShouldSendDatagramToModemInDemoMode;
        if (atomicBoolean == null) {
            this.mShouldSendDatagramToModemInDemoMode = new AtomicBoolean(bool.booleanValue());
        } else {
            atomicBoolean.set(bool.booleanValue());
        }
    }

    private void startWaitForSimulatedPollDatagramsDelayTimer(DatagramDispatcherHandlerRequest datagramDispatcherHandlerRequest) {
        if (this.mIsDemoMode.get()) {
            plogd("startWaitForSimulatedPollDatagramsDelayTimer");
            sendMessageDelayed(obtainMessage(7, datagramDispatcherHandlerRequest), getDemoTimeoutDuration());
        } else {
            plogd("Should not start WaitForSimulatedPollDatagramsDelayTimer in non-demo mode");
        }
    }

    private void stopWaitForSimulatedPollDatagramsDelayTimer() {
        removeMessages(7);
    }

    private void handleEventWaitForSimulatedPollDatagramsDelayTimedOut(SendSatelliteDatagramArgument sendSatelliteDatagramArgument) {
        if (this.mIsDemoMode.get()) {
            plogd("handleEventWaitForSimulatedPollDatagramsDelayTimedOut");
            this.mDatagramController.pushDemoModeDatagram(sendSatelliteDatagramArgument.datagramType, sendSatelliteDatagramArgument.datagram);
            this.mDatagramController.pollPendingSatelliteDatagrams(sendSatelliteDatagramArgument.subId, new Consumer<Integer>() { // from class: com.android.internal.telephony.satellite.DatagramDispatcher.1
                @Override // java.util.function.Consumer
                public void accept(Integer num) {
                    DatagramDispatcher.this.plogd("pollPendingSatelliteDatagrams result: " + num);
                }
            });
            return;
        }
        plogd("Unexpected EVENT_WAIT_FOR_SIMULATED_POLL_DATAGRAMS_DELAY_TIMED_OUT in non-demo mode");
    }

    long getDemoTimeoutDuration() {
        return this.mDemoTimeoutDuration.get();
    }

    void setTimeoutDatagramDelayInDemoMode(boolean z, long j) {
        if (this.mIsDemoMode.get()) {
            AtomicLong atomicLong = this.mDemoTimeoutDuration;
            if (z) {
                atomicLong.set(TIMEOUT_DATAGRAM_DELAY_IN_DEMO_MODE.longValue());
            } else {
                atomicLong.set(j);
            }
            plogd("setTimeoutDatagramDelayInDemoMode " + this.mDemoTimeoutDuration.get() + " reset=" + z);
        }
    }

    public void sendSms(SmsDispatchersController.PendingRequest pendingRequest) {
        SatelliteController.getInstance().startPointingUI();
        int selectedSatelliteSubId = SatelliteController.getInstance().getSelectedSatelliteSubId();
        long j = pendingRequest.uniqueMessageId;
        plogd("sendSms: subId=" + selectedSatelliteSubId + " messageId:" + j);
        synchronized (this.mLock) {
            this.mPendingSmsMap.put(Long.valueOf(j), pendingRequest);
        }
        int i = pendingRequest.isMtSmsPolling ? 7 : 6;
        this.mLastSendRequestDatagramType.set(i);
        if (this.mDatagramController.needsWaitingForSatelliteConnected(i)) {
            plogd("sendSms: wait for satellite connected");
            this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i, 8, getPendingMessagesCount(), 0);
            startDatagramWaitForConnectedStateTimer(i);
        } else if (!this.mSendingInProgress.get() && this.mDatagramController.isPollingInIdleState()) {
            this.mSendingInProgress.set(true);
            this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i, 1, getPendingMessagesCount(), 0);
            sendMessage(obtainMessage(8, pendingRequest));
        } else {
            plogd("sendSms: mSendingInProgress=" + this.mSendingInProgress.get() + ", isPollingInIdleState=" + this.mDatagramController.isPollingInIdleState());
        }
    }

    private void sendPendingSms() {
        Set setEntrySet;
        plogd("sendPendingSms");
        if (!this.mDatagramController.isPollingInIdleState()) {
            plogd("sendPendingSms: modem is receiving datagrams");
            return;
        }
        int selectedSatelliteSubId = SatelliteController.getInstance().getSelectedSatelliteSubId();
        synchronized (this.mLock) {
            try {
                setEntrySet = !this.mSendingInProgress.get() ? this.mPendingSmsMap.entrySet() : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (setEntrySet != null && setEntrySet.iterator().hasNext()) {
            SmsDispatchersController.PendingRequest pendingRequest = (SmsDispatchersController.PendingRequest) ((Map.Entry) setEntrySet.iterator().next()).getValue();
            int i = pendingRequest.isMtSmsPolling ? 7 : 6;
            if (this.mDatagramController.needsWaitingForSatelliteConnected(6)) {
                plogd("sendPendingSms: wait for satellite connected");
                int i2 = i;
                this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i2, 8, getPendingMessagesCount(), 0);
                startDatagramWaitForConnectedStateTimer(i2);
                return;
            }
            this.mSendingInProgress.set(true);
            this.mDatagramController.updateSendStatus(selectedSatelliteSubId, i, 1, getPendingMessagesCount(), 0);
            sendMessage(obtainMessage(8, pendingRequest));
            return;
        }
        plogd("sendPendingSms: mSendingInProgress=" + this.mSendingInProgress.get() + " pendingSmsCount=" + getPendingSmsCount());
    }

    public void onSendSmsDone(int i, long j, boolean z) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Long.valueOf(j);
        someArgsObtain.arg3 = Boolean.valueOf(z);
        sendMessage(obtainMessage(9, someArgsObtain));
    }

    private void sendErrorCodeAndCleanupPendingSms(LinkedHashMap linkedHashMap, int i) {
        synchronized (this.mLock) {
            try {
                if (linkedHashMap.size() == 0) {
                    plogd("sendErrorCodeAndCleanupPendingSms: pendingSmsMap is empty.");
                    return;
                }
                ploge("sendErrorCodeAndCleanupPendingSms: cleaning up resources. pendingSmsMap size=" + getPendingSmsCount());
                Phone satellitePhone = SatelliteController.getInstance().getSatellitePhone();
                if (satellitePhone == null) {
                    ploge("sendErrorCodeAndCleanupPendingSms: satellitePhone is null.");
                    linkedHashMap.clear();
                    return;
                }
                SmsDispatchersController smsDispatchersController = satellitePhone.getSmsDispatchersController();
                if (smsDispatchersController == null) {
                    ploge("sendErrorCodeAndCleanupPendingSms: smsDispatchersController is null.");
                    linkedHashMap.clear();
                    return;
                }
                Iterator it = linkedHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    SmsDispatchersController.PendingRequest pendingRequest = (SmsDispatchersController.PendingRequest) ((Map.Entry) it.next()).getValue();
                    smsDispatchersController.onSendCarrierRoamingNbIotNtnTextError(pendingRequest, i);
                    reportSendSmsCompleted(pendingRequest, i);
                }
                linkedHashMap.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void handleEventSendSmsDone(int i, long j, boolean z) {
        SmsDispatchersController.PendingRequest pendingRequest;
        int i2;
        synchronized (this.mLock) {
            pendingRequest = (SmsDispatchersController.PendingRequest) this.mPendingSmsMap.remove(Long.valueOf(j));
        }
        if (pendingRequest == null) {
            plogd("handleEventSendSmsDone there is no pendingSms for messageId=" + j);
            return;
        }
        this.mSendingInProgress.set(false);
        int i3 = pendingRequest.isMtSmsPolling ? 7 : 6;
        plogd("handleEventSendSmsDone subId=" + i + " messageId=" + j + " success=" + z + " datagramType=" + i3);
        DatagramController datagramController = this.mDatagramController;
        if (z) {
            i2 = i;
            datagramController.updateSendStatus(i2, i3, 2, getPendingMessagesCount(), 0);
            reportSendSmsCompleted(pendingRequest, 0);
            if (i3 == 7) {
                startMtSmsPollingThrottle();
                this.mShouldPollMtSms.set(false);
            }
        } else {
            i2 = i;
            datagramController.updateSendStatus(i2, i3, 3, getPendingMessagesCount(), 5);
            reportSendSmsCompleted(pendingRequest, 5);
        }
        if (getPendingMessagesCount() > 0) {
            sendPendingMessages();
        } else {
            this.mDatagramController.updateSendStatus(i2, i3, 0, 0, 0);
        }
    }

    private boolean isEnabledMtSmsPolling() {
        return this.mContext.getResources().getBoolean(R.bool.config_ignoreVibrationsOnWirelessCharger);
    }

    private long getMtSmsPollingThrottleMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_pressedStateDurationMillis);
    }

    private boolean shouldPollMtSms() {
        SatelliteController satelliteController = SatelliteController.getInstance();
        return isEnabledMtSmsPolling() && satelliteController.shouldSendSmsToDatagramDispatcher(satelliteController.getSatellitePhone());
    }

    private void handleCmdSendMtSmsPollingMessage() {
        if (!this.mShouldPollMtSms.get()) {
            plogd("sendMtSmsPollingMessage: mShouldPollMtSms=" + this.mShouldPollMtSms.get());
            return;
        }
        plogd("sendMtSmsPollingMessage");
        if (!allowCheckMessageInNotConnected()) {
            this.mShouldPollMtSms.set(false);
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null && (satelliteController.isSatelliteDisabled() || satelliteController.isSatelliteBeingDisabled())) {
            plogd("sendMtSmsPollingMessage: Not sending polling SMS as satellite is 'being disabled' or 'disabled'");
            return;
        }
        synchronized (this.mLock) {
            try {
                Iterator it = this.mPendingSmsMap.entrySet().iterator();
                while (it.hasNext()) {
                    if (((SmsDispatchersController.PendingRequest) ((Map.Entry) it.next()).getValue()).isMtSmsPolling) {
                        plogd("sendMtSmsPollingMessage: mPendingSmsMap already has the polling message.");
                        return;
                    }
                }
                Phone satellitePhone = SatelliteController.getInstance().getSatellitePhone();
                if (satellitePhone == null) {
                    ploge("sendMtSmsPollingMessage: satellitePhone is null.");
                    return;
                }
                SmsDispatchersController smsDispatchersController = satellitePhone.getSmsDispatchersController();
                if (smsDispatchersController == null) {
                    ploge("sendMtSmsPollingMessage: smsDispatchersController is null.");
                } else {
                    smsDispatchersController.sendMtSmsPollingMessage();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void startMtSmsPollingThrottle() {
        plogd("startMtSmsPollingThrottle");
        this.mIsMtSmsPollingThrottled.set(true);
        sendMessageDelayed(obtainMessage(10), getMtSmsPollingThrottleMillis());
    }

    private void stopMtSmsPollingThrottle() {
        this.mIsMtSmsPollingThrottled.set(false);
        removeMessages(10);
    }

    private boolean allowMtSmsPolling() {
        SatelliteController satelliteController = SatelliteController.getInstance();
        int selectedSatelliteSubId = satelliteController.getSelectedSatelliteSubId();
        if (satelliteController.isP2PSmsDisallowedOnCarrierRoamingNtn(selectedSatelliteSubId)) {
            plogd("allowMtSmsPolling: P2P SMS disallowed, subId = " + selectedSatelliteSubId);
            return false;
        }
        boolean z = this.mModemState.get() == 7 || this.mModemState.get() == 2;
        if (this.mIsMtSmsPollingThrottled.get()) {
            plogd("allowMtSmsPolling: polling is throttled");
            return false;
        }
        if (!this.mIsAligned.get()) {
            plogd("allowMtSmsPolling: not aligned");
            return false;
        }
        if (!z && !allowCheckMessageInNotConnected()) {
            plogd("allowMtSmsPolling: not in service and allow_check_message_in_not_connected is disabled");
            return false;
        }
        plogd("allowMtSmsPolling: return true");
        return true;
    }

    private boolean allowCheckMessageInNotConnected() {
        return this.mContext.getResources().getBoolean(R.bool.config_supportProfilesOnNonMainUser);
    }

    private static void logd(String str) {
        Log.d("DatagramDispatcher", str);
    }

    private static void loge(String str) {
        Log.e("DatagramDispatcher", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("DatagramDispatcher", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("DatagramDispatcher", str);
        }
    }

    private void plogw(String str) {
        Log.w("DatagramDispatcher", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.warn("DatagramDispatcher", str);
        }
    }

    private void ploge(String str) {
        Log.e("DatagramDispatcher", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("DatagramDispatcher", str);
        }
    }

    public void updateSessionStatsWithPendingUserMsgCount(SatelliteSessionStats satelliteSessionStats) {
        synchronized (this.mLock) {
            try {
                Log.d("SessionMetricsStats1", " mPendingEmergencyDatagramsMap size = " + this.mPendingEmergencyDatagramsMap.size());
                Log.d("SessionMetricsStats1", " mPendingNonEmergencyDatagramsMap size = " + this.mPendingNonEmergencyDatagramsMap.size());
                Log.d("SessionMetricsStats1", " mPendingSmsMap size = " + this.mPendingSmsMap.size());
                Iterator it = this.mPendingEmergencyDatagramsMap.entrySet().iterator();
                while (it.hasNext()) {
                    SendSatelliteDatagramArgument sendSatelliteDatagramArgument = (SendSatelliteDatagramArgument) ((Map.Entry) it.next()).getValue();
                    Log.d("SessionMetricsStats1", "DataGramType1 =  " + sendSatelliteDatagramArgument.datagramType);
                    satelliteSessionStats.updateCountOfUserMessagesInQueueToBeSent(sendSatelliteDatagramArgument.datagramType);
                }
                Iterator it2 = this.mPendingNonEmergencyDatagramsMap.entrySet().iterator();
                while (it2.hasNext()) {
                    SendSatelliteDatagramArgument sendSatelliteDatagramArgument2 = (SendSatelliteDatagramArgument) ((Map.Entry) it2.next()).getValue();
                    Log.d("SessionMetricsStats1", "DataGramType2 =  " + sendSatelliteDatagramArgument2.datagramType);
                    satelliteSessionStats.updateCountOfUserMessagesInQueueToBeSent(sendSatelliteDatagramArgument2.datagramType);
                }
                Iterator it3 = this.mPendingSmsMap.entrySet().iterator();
                while (it3.hasNext()) {
                    int i = ((SmsDispatchersController.PendingRequest) ((Map.Entry) it3.next()).getValue()).isMtSmsPolling ? 7 : 6;
                    Log.d("SessionMetricsStats1", "DataGramType3 =  " + i);
                    satelliteSessionStats.updateCountOfUserMessagesInQueueToBeSent(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
