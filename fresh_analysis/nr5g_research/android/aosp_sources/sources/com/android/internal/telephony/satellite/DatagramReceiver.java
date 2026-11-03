package com.android.internal.telephony.satellite;

import android.R;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Telephony;
import android.telephony.PersistentLogger;
import android.telephony.satellite.ISatelliteDatagramCallback;
import android.telephony.satellite.SatelliteDatagram;
import android.telephony.satellite.SatelliteManager;
import android.util.Log;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.IVoidConsumer;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.metrics.ControllerMetricsStats;
import com.android.internal.telephony.satellite.metrics.SessionMetricsStats;
import com.android.internal.util.FunctionalUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongUnaryOperator;

/* JADX INFO: loaded from: classes.dex */
public class DatagramReceiver extends Handler {
    private static AtomicLong mNextDatagramId = new AtomicLong(0);
    private static DatagramReceiver sInstance;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final ControllerMetricsStats mControllerMetricsStats;
    private final DatagramController mDatagramController;
    private AtomicLong mDatagramTransferStartTime;
    private DatagramReceiverHandlerRequest mDemoPollPendingSatelliteDatagramsRequest;
    private final FeatureFlags mFeatureFlags;
    private AtomicBoolean mIsAligned;
    private AtomicBoolean mIsDemoMode;
    private final Looper mLooper;
    private final ConcurrentHashMap mPendingAckCountHashMap;
    private DatagramReceiverHandlerRequest mPendingPollSatelliteDatagramsRequest;
    private PersistentLogger mPersistentLogger;
    private final ConcurrentHashMap mSatelliteDatagramListenerHandlers;
    private final SessionMetricsStats mSessionMetricsStats;
    private SharedPreferences mSharedPreferences;

    public static DatagramReceiver make(Context context, Looper looper, FeatureFlags featureFlags, DatagramController datagramController) {
        if (sInstance == null) {
            sInstance = new DatagramReceiver(context, looper, featureFlags, datagramController);
        }
        return sInstance;
    }

    protected DatagramReceiver(Context context, Looper looper, FeatureFlags featureFlags, DatagramController datagramController) {
        super(looper);
        this.mSharedPreferences = null;
        this.mPersistentLogger = null;
        this.mDatagramTransferStartTime = new AtomicLong(0L);
        this.mIsDemoMode = new AtomicBoolean(false);
        this.mIsAligned = new AtomicBoolean(false);
        this.mSatelliteDatagramListenerHandlers = new ConcurrentHashMap();
        this.mPendingAckCountHashMap = new ConcurrentHashMap();
        this.mDemoPollPendingSatelliteDatagramsRequest = null;
        this.mPendingPollSatelliteDatagramsRequest = null;
        this.mContext = context;
        this.mLooper = looper;
        this.mFeatureFlags = featureFlags;
        this.mContentResolver = context.getContentResolver();
        this.mDatagramController = datagramController;
        this.mControllerMetricsStats = ControllerMetricsStats.getInstance();
        this.mSessionMetricsStats = SessionMetricsStats.getInstance();
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        try {
            this.mSharedPreferences = context.getSharedPreferences(SatelliteController.SATELLITE_SHARED_PREF, 0);
        } catch (Exception e) {
            ploge("Cannot get default shared preferences: " + e);
        }
    }

    private static final class DatagramReceiverHandlerRequest {
        public Object argument;
        public Phone phone;
        public Object result;
        public int subId;

        DatagramReceiverHandlerRequest(Object obj, Phone phone, int i) {
            this.argument = obj;
            this.phone = phone;
            this.subId = i;
        }
    }

    public static final class SatelliteDatagramListenerHandler extends Handler {
        public static final int EVENT_RECEIVED_ACK = 3;
        public static final int EVENT_RETRY_DELIVERING_RECEIVED_DATAGRAM = 2;
        public static final int EVENT_SATELLITE_DATAGRAM_RECEIVED = 1;
        private final ConcurrentHashMap mListeners;
        private final int mSubId;

        private static final class DatagramRetryArgument {
            public SatelliteDatagram datagram;
            public long datagramId;
            public ISatelliteDatagramCallback listener;
            public int pendingCount;

            DatagramRetryArgument(long j, SatelliteDatagram satelliteDatagram, int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
                this.datagramId = j;
                this.datagram = satelliteDatagram;
                this.pendingCount = i;
                this.listener = iSatelliteDatagramCallback;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj != null && DatagramRetryArgument.class == obj.getClass()) {
                    DatagramRetryArgument datagramRetryArgument = (DatagramRetryArgument) obj;
                    if (this.datagramId == datagramRetryArgument.datagramId && this.datagram.equals(datagramRetryArgument.datagram) && this.pendingCount == datagramRetryArgument.pendingCount && this.listener.equals(datagramRetryArgument.listener)) {
                        return true;
                    }
                }
                return false;
            }
        }

        public SatelliteDatagramListenerHandler(Looper looper, int i) {
            super(looper);
            this.mSubId = i;
            this.mListeners = new ConcurrentHashMap();
        }

        public void addListener(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
            this.mListeners.put(iSatelliteDatagramCallback.asBinder(), iSatelliteDatagramCallback);
        }

        public void removeListener(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
            this.mListeners.remove(iSatelliteDatagramCallback.asBinder());
        }

        public boolean hasListeners() {
            return !this.mListeners.isEmpty();
        }

        public int getNumOfListeners() {
            return this.mListeners.size();
        }

        private int getTimeoutToReceiveAck() {
            return DatagramReceiver.sInstance.mContext.getResources().getInteger(R.integer.device_idle_min_light_maintenance_time_ms);
        }

        private long getDatagramId() {
            if (DatagramReceiver.sInstance.mSharedPreferences == null) {
                try {
                    DatagramReceiver.sInstance.mSharedPreferences = DatagramReceiver.sInstance.mContext.getSharedPreferences(SatelliteController.SATELLITE_SHARED_PREF, 0);
                } catch (Exception e) {
                    DatagramReceiver.loge("Cannot get default shared preferences: " + e);
                }
            }
            if (DatagramReceiver.sInstance.mSharedPreferences != null) {
                long j = (DatagramReceiver.sInstance.mSharedPreferences.getLong("satellite_datagram_id_key", DatagramReceiver.mNextDatagramId.get()) + 1) % DatagramController.MAX_DATAGRAM_ID;
                DatagramReceiver.mNextDatagramId.set(j);
                DatagramReceiver.sInstance.mSharedPreferences.edit().putLong("satellite_datagram_id_key", j).commit();
                return j;
            }
            DatagramReceiver.loge("Shared preferences is null - returning default datagramId");
            return DatagramReceiver.mNextDatagramId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.satellite.DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda2
                @Override // java.util.function.LongUnaryOperator
                public final long applyAsLong(long j2) {
                    return DatagramReceiver.SatelliteDatagramListenerHandler.m1544$r8$lambda$cJpc320AaYmOxyAOPNgZ6GnQ0Q(j2);
                }
            });
        }

        /* JADX INFO: renamed from: $r8$lambda$cJpc320A-aYmOxyAOPNgZ6GnQ0Q, reason: not valid java name */
        public static /* synthetic */ long m1544$r8$lambda$cJpc320AaYmOxyAOPNgZ6GnQ0Q(long j) {
            return (j + 1) % DatagramController.MAX_DATAGRAM_ID;
        }

        private void insertDatagram(long j, SatelliteDatagram satelliteDatagram) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("datagram_id", Long.valueOf(j));
            contentValues.put("datagram", satelliteDatagram.getSatelliteDatagram());
            if (DatagramReceiver.sInstance.mContentResolver.insert(Telephony.SatelliteDatagrams.CONTENT_URI, contentValues) == null) {
                DatagramReceiver.loge("Cannot insert datagram with datagramId: " + j);
            } else {
                DatagramReceiver.logd("Inserted datagram with datagramId: " + j);
            }
        }

        /* JADX WARN: Code duplicated, block: B:13:0x0060 A[Catch: all -> 0x004d, TRY_LEAVE, TryCatch #2 {all -> 0x004d, blocks: (B:5:0x0027, B:7:0x002e, B:9:0x003d, B:12:0x0050, B:13:0x0060), top: B:30:0x0027, outer: #1 }] */
        /* JADX WARN: Instruction removed from duplicated block: B:13:0x0060, please report this as an issue */
        private void deleteDatagram(long j) {
            String str = "datagram_id=" + j;
            try {
                ContentResolver contentResolver = DatagramReceiver.sInstance.mContentResolver;
                Uri uri = Telephony.SatelliteDatagrams.CONTENT_URI;
                Cursor cursorQuery = contentResolver.query(uri, null, str, null, null);
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.getCount() == 1) {
                            if (DatagramReceiver.sInstance.mContentResolver.delete(uri, str, null) == 0) {
                                DatagramReceiver.loge("Cannot delete datagram with datagramId: " + j);
                            } else {
                                DatagramReceiver.logd("Deleted datagram with datagramId: " + j);
                            }
                        } else {
                            DatagramReceiver.loge("Datagram with datagramId: " + j + " is not present in DB.");
                        }
                    } catch (Throwable th) {
                        if (cursorQuery == null) {
                            throw th;
                        }
                        try {
                            cursorQuery.close();
                            throw th;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            throw th;
                        }
                    }
                } else {
                    DatagramReceiver.loge("Datagram with datagramId: " + j + " is not present in DB.");
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (SQLException e) {
                DatagramReceiver.loge("deleteDatagram SQLException e:" + e);
            }
        }

        private void onSatelliteDatagramReceived(final DatagramRetryArgument datagramRetryArgument) {
            try {
                datagramRetryArgument.listener.onSatelliteDatagramReceived(datagramRetryArgument.datagramId, datagramRetryArgument.datagram, datagramRetryArgument.pendingCount, new IVoidConsumer.Stub() { // from class: com.android.internal.telephony.satellite.DatagramReceiver.SatelliteDatagramListenerHandler.1
                    public void accept() {
                        DatagramReceiver.logd("acknowledgeSatelliteDatagramReceived: datagramId=" + datagramRetryArgument.datagramId);
                        SatelliteDatagramListenerHandler satelliteDatagramListenerHandler = SatelliteDatagramListenerHandler.this;
                        satelliteDatagramListenerHandler.sendMessage(satelliteDatagramListenerHandler.obtainMessage(3, datagramRetryArgument));
                    }
                });
            } catch (RemoteException e) {
                DatagramReceiver.logd("EVENT_SATELLITE_DATAGRAM_RECEIVED RemoteException: " + e);
            }
        }

        /* JADX WARN: Code duplicated, block: B:29:0x0159  */
        /* JADX WARN: Code duplicated, block: B:31:0x016a  */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            final SatelliteDatagramListenerHandler satelliteDatagramListenerHandler;
            int i = message.what;
            if (i != 1) {
                if (i == 2) {
                    DatagramRetryArgument datagramRetryArgument = (DatagramRetryArgument) message.obj;
                    DatagramReceiver.logd("Received EVENT_RETRY_DELIVERING_RECEIVED_DATAGRAM datagramId:" + datagramRetryArgument.datagramId);
                    onSatelliteDatagramReceived(datagramRetryArgument);
                    return;
                }
                if (i == 3) {
                    DatagramRetryArgument datagramRetryArgument2 = (DatagramRetryArgument) message.obj;
                    if (!DatagramReceiver.sInstance.mPendingAckCountHashMap.containsKey(Long.valueOf(datagramRetryArgument2.datagramId))) {
                        DatagramReceiver.logd("The datagram " + datagramRetryArgument2.datagramId + " should have been deleted.");
                        return;
                    }
                    int iIntValue = ((Integer) DatagramReceiver.sInstance.mPendingAckCountHashMap.get(Long.valueOf(datagramRetryArgument2.datagramId))).intValue() - 1;
                    DatagramReceiver.sInstance.mPendingAckCountHashMap.put(Long.valueOf(datagramRetryArgument2.datagramId), Integer.valueOf(iIntValue));
                    DatagramReceiver.logd("Received EVENT_RECEIVED_ACK datagramId:" + datagramRetryArgument2.datagramId);
                    removeMessages(2, datagramRetryArgument2);
                    if (iIntValue <= 0) {
                        deleteDatagram(datagramRetryArgument2.datagramId);
                        DatagramReceiver.sInstance.mPendingAckCountHashMap.remove(Long.valueOf(datagramRetryArgument2.datagramId));
                        return;
                    }
                    return;
                }
                DatagramReceiver.loge("SatelliteDatagramListenerHandler unknown event: " + message.what);
                return;
            }
            Pair pair = (Pair) ((AsyncResult) message.obj).result;
            final SatelliteDatagram satelliteDatagram = (SatelliteDatagram) pair.first;
            final int iIntValue2 = ((Integer) pair.second).intValue();
            DatagramReceiver.logd("Received EVENT_SATELLITE_DATAGRAM_RECEIVED for subId=" + this.mSubId + " pendingCount:" + iIntValue2);
            if (iIntValue2 <= 0 && satelliteDatagram == null) {
                DatagramReceiver.sInstance.mDatagramController.updateReceiveStatus(this.mSubId, 1, 6, iIntValue2, 0);
            } else {
                if (satelliteDatagram != null) {
                    DatagramReceiver.sInstance.mDatagramController.updateReceiveStatus(this.mSubId, 1, 5, iIntValue2, 0);
                    final long datagramId = getDatagramId();
                    DatagramReceiver.sInstance.mPendingAckCountHashMap.put(Long.valueOf(datagramId), Integer.valueOf(getNumOfListeners()));
                    insertDatagram(datagramId, satelliteDatagram);
                    satelliteDatagramListenerHandler = this;
                    this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            this.f$0.lambda$handleMessage$1(datagramId, satelliteDatagram, iIntValue2, (ISatelliteDatagramCallback) obj);
                        }
                    });
                }
                DatagramReceiver.sInstance.reportMetrics(satelliteDatagram, 0);
                if (iIntValue2 <= 0) {
                    DatagramReceiver.sInstance.mDatagramController.updateReceiveStatus(satelliteDatagramListenerHandler.mSubId, 1, 0, iIntValue2, 0);
                } else {
                    DatagramReceiver.sInstance.pollPendingSatelliteDatagramsInternal(satelliteDatagramListenerHandler.mSubId, FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(new IIntegerConsumer.Stub(satelliteDatagramListenerHandler) { // from class: com.android.internal.telephony.satellite.DatagramReceiver.SatelliteDatagramListenerHandler.2
                        public void accept(int i2) {
                            DatagramReceiver.logd("pollPendingSatelliteDatagram result: " + i2);
                        }
                    })));
                }
            }
            satelliteDatagramListenerHandler = this;
            DatagramReceiver.sInstance.reportMetrics(satelliteDatagram, 0);
            if (iIntValue2 <= 0) {
                DatagramReceiver.sInstance.mDatagramController.updateReceiveStatus(satelliteDatagramListenerHandler.mSubId, 1, 0, iIntValue2, 0);
            } else {
                DatagramReceiver.sInstance.pollPendingSatelliteDatagramsInternal(satelliteDatagramListenerHandler.mSubId, FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(new IIntegerConsumer.Stub(satelliteDatagramListenerHandler) { // from class: com.android.internal.telephony.satellite.DatagramReceiver.SatelliteDatagramListenerHandler.2
                    public void accept(int i2) {
                        DatagramReceiver.logd("pollPendingSatelliteDatagram result: " + i2);
                    }
                })));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1(long j, SatelliteDatagram satelliteDatagram, int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
            DatagramRetryArgument datagramRetryArgument = new DatagramRetryArgument(j, satelliteDatagram, i, iSatelliteDatagramCallback);
            onSatelliteDatagramReceived(datagramRetryArgument);
            sendMessageDelayed(obtainMessage(2, datagramRetryArgument), getTimeoutToReceiveAck());
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                SatelliteModemInterface.getInstance().pollPendingSatelliteDatagrams(obtainMessage(2, (DatagramReceiverHandlerRequest) message.obj));
                return;
            case 2:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = (DatagramReceiverHandlerRequest) asyncResult.userObj;
                int satelliteError = SatelliteServiceUtils.getSatelliteError(asyncResult, "pollPendingSatelliteDatagrams");
                if (this.mIsDemoMode.get() && satelliteError == 0) {
                    SatelliteDatagram satelliteDatagramPopDemoModeDatagram = this.mDatagramController.popDemoModeDatagram();
                    SatelliteDatagramListenerHandler satelliteDatagramListenerHandler = (SatelliteDatagramListenerHandler) this.mSatelliteDatagramListenerHandlers.get(Integer.MAX_VALUE);
                    if (satelliteDatagramListenerHandler != null) {
                        satelliteDatagramListenerHandler.sendMessage(satelliteDatagramListenerHandler.obtainMessage(1, new AsyncResult((Object) null, new Pair(satelliteDatagramPopDemoModeDatagram, 0), (Throwable) null)));
                    } else {
                        satelliteError = 6;
                    }
                }
                int i = satelliteError;
                plogd("EVENT_POLL_PENDING_SATELLITE_DATAGRAMS_DONE error: " + i);
                if (i != 0) {
                    DatagramController datagramController = this.mDatagramController;
                    datagramController.updateReceiveStatus(datagramReceiverHandlerRequest.subId, 1, 7, datagramController.getReceivePendingCount(), i);
                    DatagramController datagramController2 = this.mDatagramController;
                    datagramController2.updateReceiveStatus(datagramReceiverHandlerRequest.subId, 1, 0, datagramController2.getReceivePendingCount(), 0);
                    reportMetrics(null, i);
                }
                ((Consumer) datagramReceiverHandlerRequest.argument).accept(Integer.valueOf(i));
                return;
            case 3:
                handleEventSatelliteAlignedTimeout((DatagramReceiverHandlerRequest) message.obj);
                return;
            case 4:
                handleEventDatagramWaitForConnectedStateTimedOut();
                return;
            case 5:
                plogd("REQUEST_POLL_PENDING_SATELLITE_DATAGRAMS");
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleRequestPollPendingSatelliteDatagrams(((Integer) someArgs.arg1).intValue(), (Consumer) someArgs.arg2);
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 6:
                plogd("EVENT_SATELLITE_MODEM_STATE_CHANGED");
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                try {
                    handleEventSatelliteModemStateChanged(((Integer) someArgs2.arg1).intValue());
                    return;
                } finally {
                    someArgs2.recycle();
                }
            case 7:
                plogd("REQUEST_SET_DEVICE_ALIGNED_WITH_SATELLITE");
                SomeArgs someArgs3 = (SomeArgs) message.obj;
                try {
                    handleRequestSetDeviceAlignedWithSatellite(((Boolean) someArgs3.arg1).booleanValue());
                    return;
                } finally {
                    someArgs3.recycle();
                }
            default:
                plogw("DatagramDispatcherHandler: unexpected message code: " + message.what);
                return;
        }
    }

    protected int registerForSatelliteDatagram(int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        if (!SatelliteController.getInstance().isSatelliteSupportedViaOem()) {
            return 20;
        }
        SatelliteDatagramListenerHandler satelliteDatagramListenerHandler = (SatelliteDatagramListenerHandler) this.mSatelliteDatagramListenerHandlers.get(Integer.MAX_VALUE);
        if (satelliteDatagramListenerHandler == null) {
            satelliteDatagramListenerHandler = new SatelliteDatagramListenerHandler(this.mLooper, Integer.MAX_VALUE);
            SatelliteModemInterface.getInstance().registerForSatelliteDatagramsReceived(satelliteDatagramListenerHandler, 1, null);
        }
        satelliteDatagramListenerHandler.addListener(iSatelliteDatagramCallback);
        this.mSatelliteDatagramListenerHandlers.put(Integer.MAX_VALUE, satelliteDatagramListenerHandler);
        return 0;
    }

    void unregisterForSatelliteDatagram(int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        SatelliteDatagramListenerHandler satelliteDatagramListenerHandler = (SatelliteDatagramListenerHandler) this.mSatelliteDatagramListenerHandlers.get(Integer.MAX_VALUE);
        if (satelliteDatagramListenerHandler != null) {
            satelliteDatagramListenerHandler.removeListener(iSatelliteDatagramCallback);
            if (satelliteDatagramListenerHandler.hasListeners()) {
                return;
            }
            this.mSatelliteDatagramListenerHandlers.remove(Integer.MAX_VALUE);
            SatelliteModemInterface.getInstance().unregisterForSatelliteDatagramsReceived(satelliteDatagramListenerHandler);
        }
    }

    protected void pollPendingSatelliteDatagrams(int i, Consumer<Integer> consumer) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = consumer;
        sendMessage(obtainMessage(5, someArgsObtain));
    }

    private void handleRequestPollPendingSatelliteDatagrams(int i, Consumer consumer) {
        plogd("handleRequestPollPendingSatelliteDatagrams");
        if (!this.mDatagramController.isPollingInIdleState()) {
            plogd("pollPendingSatelliteDatagrams: satellite modem is busy receiving datagrams.");
            consumer.accept(22);
        } else {
            pollPendingSatelliteDatagramsInternal(Integer.MAX_VALUE, consumer);
        }
    }

    private void handleSatelliteConnectedEvent() {
        if (isDatagramWaitForConnectedStateTimerStarted()) {
            stopDatagramWaitForConnectedStateTimer();
            DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = this.mPendingPollSatelliteDatagramsRequest;
            if (datagramReceiverHandlerRequest == null) {
                ploge("handleSatelliteConnectedEvent: mPendingPollSatelliteDatagramsRequest is null");
                return;
            }
            pollPendingSatelliteDatagramsInternal(datagramReceiverHandlerRequest.subId, (Consumer) datagramReceiverHandlerRequest.argument);
            this.mPendingPollSatelliteDatagramsRequest = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pollPendingSatelliteDatagramsInternal(int i, Consumer consumer) {
        if (!this.mDatagramController.isSendingInIdleState()) {
            plogd("pollPendingSatelliteDatagramsInternal: satellite modem is busy sending datagrams.");
            consumer.accept(22);
            return;
        }
        if (this.mDatagramController.needsWaitingForSatelliteConnected(0)) {
            plogd("pollPendingSatelliteDatagramsInternal: wait for satellite connected");
            this.mPendingPollSatelliteDatagramsRequest = new DatagramReceiverHandlerRequest(consumer, SatelliteServiceUtils.getPhone(), i);
            DatagramController datagramController = this.mDatagramController;
            datagramController.updateReceiveStatus(i, 1, 8, datagramController.getReceivePendingCount(), 0);
            startDatagramWaitForConnectedStateTimer();
            return;
        }
        DatagramController datagramController2 = this.mDatagramController;
        datagramController2.updateReceiveStatus(i, 1, 4, datagramController2.getReceivePendingCount(), 0);
        this.mDatagramTransferStartTime.set(System.currentTimeMillis());
        Phone phone = SatelliteServiceUtils.getPhone();
        if (this.mIsDemoMode.get()) {
            DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = new DatagramReceiverHandlerRequest(consumer, phone, i);
            if (!this.mDatagramController.waitForAligningToSatellite(this.mIsAligned.get())) {
                Message messageObtainMessage = obtainMessage(2, datagramReceiverHandlerRequest);
                AsyncResult.forMessage(messageObtainMessage, (Object) null, (Throwable) null);
                messageObtainMessage.sendToTarget();
                return;
            }
            startSatelliteAlignedTimer(datagramReceiverHandlerRequest);
            return;
        }
        sendRequestAsync(1, consumer, phone, i);
    }

    protected void onSatelliteModemStateChanged(int i) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        sendMessage(obtainMessage(6, someArgsObtain));
    }

    private void handleEventSatelliteModemStateChanged(int i) {
        plogd("handleEventSatelliteModemStateChanged: state = " + i);
        if (i == 4 || i == 5) {
            plogd("onSatelliteModemStateChanged: cleaning up resources");
            cleanUpResources();
        } else if (i == 7) {
            handleSatelliteConnectedEvent();
        }
    }

    private void cleanupDemoModeResources() {
        if (isSatelliteAlignedTimerStarted()) {
            stopSatelliteAlignedTimer();
            DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = this.mDemoPollPendingSatelliteDatagramsRequest;
            if (datagramReceiverHandlerRequest == null) {
                ploge("Satellite aligned timer was started but mDemoPollPendingSatelliteDatagramsRequest is null");
            } else {
                ((Consumer) datagramReceiverHandlerRequest.argument).accept(15);
            }
        }
        this.mIsDemoMode.set(false);
        this.mDemoPollPendingSatelliteDatagramsRequest = null;
        this.mIsAligned.set(false);
    }

    private void cleanUpResources() {
        DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = this.mPendingPollSatelliteDatagramsRequest;
        if (datagramReceiverHandlerRequest != null) {
            ((Consumer) datagramReceiverHandlerRequest.argument).accept(15);
            this.mPendingPollSatelliteDatagramsRequest = null;
        }
        stopDatagramWaitForConnectedStateTimer();
        int selectedSatelliteSubId = SatelliteController.getInstance().getSelectedSatelliteSubId();
        if (this.mDatagramController.isReceivingDatagrams()) {
            DatagramController datagramController = this.mDatagramController;
            datagramController.updateReceiveStatus(selectedSatelliteSubId, 1, 7, datagramController.getReceivePendingCount(), 15);
        }
        this.mDatagramController.updateReceiveStatus(selectedSatelliteSubId, 1, 0, 0, 0);
        cleanupDemoModeResources();
    }

    private void sendRequestAsync(int i, Object obj, Phone phone, int i2) {
        obtainMessage(i, new DatagramReceiverHandlerRequest(obj, phone, i2)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportMetrics(SatelliteDatagram satelliteDatagram, int i) {
        long jCurrentTimeMillis;
        int iRound = -1;
        if (satelliteDatagram != null) {
            iRound = satelliteDatagram.getSatelliteDatagram() != null ? (int) (Math.round(((double) satelliteDatagram.getSatelliteDatagram().length) / 10.0d) * 10) : -1;
            jCurrentTimeMillis = System.currentTimeMillis() - this.mDatagramTransferStartTime.get();
        } else {
            jCurrentTimeMillis = 0;
        }
        this.mDatagramTransferStartTime.set(0L);
        SatelliteStats.getInstance().onSatelliteIncomingDatagramMetrics(new SatelliteStats.SatelliteIncomingDatagramParams.Builder().setResultCode(i).setDatagramSizeBytes(iRound).setDatagramTransferTimeMillis(jCurrentTimeMillis).setIsDemoMode(this.mIsDemoMode.get()).setCarrierId(SatelliteController.getInstance().getSatelliteCarrierId()).setIsNtnOnlyCarrier(SatelliteController.getInstance().isNtnOnlyCarrier()).setSupportedConnectionMode(SatelliteController.getInstance().getSupportedConnectTypeMetrics()).setSessionConnectionMode(SatelliteController.getInstance().getSessionConnectTypeMetrics()).setPlmn(SatelliteController.getInstance().getSatellitePlmnForMetrics()).build());
        this.mControllerMetricsStats.reportIncomingDatagramCount(i, this.mIsDemoMode.get());
        SessionMetricsStats sessionMetricsStats = this.mSessionMetricsStats;
        if (i == 0) {
            sessionMetricsStats.addCountOfSuccessfulIncomingDatagram();
        } else {
            sessionMetricsStats.addCountOfFailedIncomingDatagram();
        }
    }

    protected void setDemoMode(boolean z) {
        this.mIsDemoMode.set(z);
    }

    protected void setDeviceAlignedWithSatellite(boolean z) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Boolean.valueOf(z);
        sendMessage(obtainMessage(7, someArgsObtain));
    }

    private void handleRequestSetDeviceAlignedWithSatellite(boolean z) {
        this.mIsAligned.set(z);
        plogd("setDeviceAlignedWithSatellite: " + z);
        if (z && this.mIsDemoMode.get()) {
            handleEventSatelliteAligned();
        }
    }

    private void startSatelliteAlignedTimer(DatagramReceiverHandlerRequest datagramReceiverHandlerRequest) {
        if (isSatelliteAlignedTimerStarted()) {
            plogd("Satellite aligned timer was already started");
        } else {
            this.mDemoPollPendingSatelliteDatagramsRequest = datagramReceiverHandlerRequest;
            sendMessageDelayed(obtainMessage(3, datagramReceiverHandlerRequest), getSatelliteAlignedTimeoutDuration());
        }
    }

    protected long getSatelliteAlignedTimeoutDuration() {
        return this.mDatagramController.getSatelliteAlignedTimeoutDuration();
    }

    private void handleEventSatelliteAligned() {
        if (isSatelliteAlignedTimerStarted()) {
            stopSatelliteAlignedTimer();
            DatagramReceiverHandlerRequest datagramReceiverHandlerRequest = this.mDemoPollPendingSatelliteDatagramsRequest;
            if (datagramReceiverHandlerRequest == null) {
                ploge("handleSatelliteAlignedTimer: mDemoPollPendingSatelliteDatagramsRequest is null");
                return;
            }
            Message messageObtainMessage = obtainMessage(2, datagramReceiverHandlerRequest);
            this.mDemoPollPendingSatelliteDatagramsRequest = null;
            AsyncResult.forMessage(messageObtainMessage, (Object) null, (Throwable) null);
            messageObtainMessage.sendToTarget();
        }
    }

    private void handleEventSatelliteAlignedTimeout(DatagramReceiverHandlerRequest datagramReceiverHandlerRequest) {
        SatelliteManager.SatelliteException satelliteException = new SatelliteManager.SatelliteException(18);
        Message messageObtainMessage = obtainMessage(2, datagramReceiverHandlerRequest);
        AsyncResult.forMessage(messageObtainMessage, (Object) null, satelliteException);
        messageObtainMessage.sendToTarget();
    }

    private boolean isSatelliteAlignedTimerStarted() {
        return hasMessages(3);
    }

    private void stopSatelliteAlignedTimer() {
        removeMessages(3);
    }

    private void startDatagramWaitForConnectedStateTimer() {
        if (isDatagramWaitForConnectedStateTimerStarted()) {
            plogd("DatagramWaitForConnectedStateTimer is already started");
        } else {
            sendMessageDelayed(obtainMessage(4), this.mDatagramController.getDatagramWaitTimeForConnectedState(false));
        }
    }

    private void stopDatagramWaitForConnectedStateTimer() {
        removeMessages(4);
    }

    protected boolean isDatagramWaitForConnectedStateTimerStarted() {
        return hasMessages(4);
    }

    private void handleEventDatagramWaitForConnectedStateTimedOut() {
        if (this.mPendingPollSatelliteDatagramsRequest == null) {
            plogw("handleEventDatagramWaitForConnectedStateTimedOut: mPendingPollSatelliteDatagramsRequest is null");
            return;
        }
        plogw("Timed out to wait for satellite connected before polling datagrams");
        DatagramController datagramController = this.mDatagramController;
        datagramController.updateReceiveStatus(this.mPendingPollSatelliteDatagramsRequest.subId, 1, 7, datagramController.getReceivePendingCount(), 18);
        DatagramController datagramController2 = this.mDatagramController;
        datagramController2.updateReceiveStatus(this.mPendingPollSatelliteDatagramsRequest.subId, 1, 0, datagramController2.getReceivePendingCount(), 0);
        reportMetrics(null, 18);
        ((Consumer) this.mPendingPollSatelliteDatagramsRequest.argument).accept(18);
        this.mPendingPollSatelliteDatagramsRequest = null;
    }

    public void destroy() {
        sInstance = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("DatagramReceiver", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("DatagramReceiver", str);
    }

    private void plogd(String str) {
        Log.d("DatagramReceiver", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("DatagramReceiver", str);
        }
    }

    private void plogw(String str) {
        Log.w("DatagramReceiver", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.warn("DatagramReceiver", str);
        }
    }

    private void ploge(String str) {
        Log.e("DatagramReceiver", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("DatagramReceiver", str);
        }
    }
}
