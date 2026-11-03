package com.android.internal.telephony.satellite;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.PersistentLogger;
import android.telephony.satellite.ISatelliteDatagramCallback;
import android.telephony.satellite.SatelliteDatagram;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class DatagramController {
    public static final int BOOLEAN_TYPE_WAIT_FOR_DEVICE_ALIGNMENT_IN_DEMO_DATAGRAM = 1;
    public static final int ROUNDING_UNIT = 10;
    public static final int TIMEOUT_TYPE_ALIGN = 1;
    public static final int TIMEOUT_TYPE_DATAGRAM_DELAY_IN_DEMO_MODE = 4;
    public static final int TIMEOUT_TYPE_DATAGRAM_WAIT_FOR_CONNECTED_STATE = 2;
    public static final int TIMEOUT_TYPE_WAIT_FOR_DATAGRAM_SENDING_RESPONSE = 3;
    private static DatagramController sInstance;
    private final Context mContext;
    private final DatagramDispatcher mDatagramDispatcher;
    private final DatagramReceiver mDatagramReceiver;
    private final List mDemoModeDatagramList;
    private final FeatureFlags mFeatureFlags;
    private PersistentLogger mPersistentLogger;
    private final PointingAppController mPointingAppController;
    public static final long MAX_DATAGRAM_ID = (long) Math.pow(2.0d, 16.0d);
    public static final long SATELLITE_ALIGN_TIMEOUT = SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS;
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    private AtomicInteger mSendSubId = new AtomicInteger(0);
    private AtomicInteger mDatagramType = new AtomicInteger(0);
    private AtomicInteger mSendDatagramTransferState = new AtomicInteger(0);
    private AtomicInteger mSendPendingCount = new AtomicInteger(0);
    private AtomicInteger mSendErrorCode = new AtomicInteger(0);
    private AtomicInteger mReceiveSubId = new AtomicInteger(0);
    private AtomicInteger mReceiveDatagramTransferState = new AtomicInteger(0);
    private AtomicInteger mReceivePendingCount = new AtomicInteger(0);
    private AtomicInteger mReceiveErrorCode = new AtomicInteger(0);
    private AtomicBoolean mIsDemoMode = new AtomicBoolean(false);
    private AtomicLong mAlignTimeoutDuration = new AtomicLong(SATELLITE_ALIGN_TIMEOUT);
    private AtomicLong mDatagramWaitTimeForConnectedState = new AtomicLong(0);
    private AtomicLong mModemImageSwitchingDuration = new AtomicLong(0);
    private AtomicBoolean mWaitForDeviceAlignmentInDemoDatagram = new AtomicBoolean(false);
    private AtomicLong mDatagramWaitTimeForConnectedStateForLastMessage = new AtomicLong(0);
    private AtomicInteger mSatelltieModemState = new AtomicInteger(-1);
    private final Object mLock = new Object();

    static DatagramController getInstance() {
        if (sInstance == null) {
            loge("DatagramController was not yet initialized.");
        }
        return sInstance;
    }

    static DatagramController make(Context context, Looper looper, FeatureFlags featureFlags, PointingAppController pointingAppController) {
        if (sInstance == null) {
            sInstance = new DatagramController(context, looper, featureFlags, pointingAppController);
        }
        return sInstance;
    }

    protected DatagramController(Context context, Looper looper, FeatureFlags featureFlags, PointingAppController pointingAppController) {
        this.mPersistentLogger = null;
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mPointingAppController = pointingAppController;
        this.mDatagramDispatcher = DatagramDispatcher.make(context, looper, featureFlags, this);
        this.mDatagramReceiver = DatagramReceiver.make(context, looper, featureFlags, this);
        this.mDatagramWaitTimeForConnectedState.set(getDatagramWaitForConnectedStateTimeoutMillis());
        this.mModemImageSwitchingDuration.set(getSatelliteModemImageSwitchingDurationMillis());
        this.mWaitForDeviceAlignmentInDemoDatagram.set(getWaitForDeviceAlignmentInDemoDatagramFromResources());
        this.mDatagramWaitTimeForConnectedStateForLastMessage.set(getDatagramWaitForConnectedStateForLastMessageTimeoutMillis());
        this.mDemoModeDatagramList = new ArrayList();
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
    }

    protected int registerForSatelliteDatagram(int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        return this.mDatagramReceiver.registerForSatelliteDatagram(i, iSatelliteDatagramCallback);
    }

    protected void unregisterForSatelliteDatagram(int i, ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        this.mDatagramReceiver.unregisterForSatelliteDatagram(i, iSatelliteDatagramCallback);
    }

    protected void pollPendingSatelliteDatagrams(int i, Consumer<Integer> consumer) {
        plogd("pollPendingSatelliteDatagrams");
        this.mDatagramReceiver.pollPendingSatelliteDatagrams(i, consumer);
    }

    protected void sendSatelliteDatagram(int i, int i2, SatelliteDatagram satelliteDatagram, boolean z, Consumer<Integer> consumer) {
        this.mDatagramDispatcher.sendSatelliteDatagram(i, i2, satelliteDatagram, z, consumer);
        this.mPointingAppController.onSendDatagramRequested(i, i2);
    }

    protected void updateSendStatus(int i, int i2, int i3, int i4, int i5) {
        plogd("updateSendStatus subId: " + i + " datagramType: " + i2 + " datagramTransferState: " + i3 + " sendPendingCount: " + i4 + " errorCode: " + i5);
        if (shouldSuppressDatagramTransferStateUpdate(i2)) {
            plogd("Ignore the request to update send status");
            return;
        }
        this.mSendSubId.set(i);
        this.mDatagramType.set(i2);
        this.mSendDatagramTransferState.set(i3);
        this.mSendPendingCount.set(i4);
        this.mSendErrorCode.set(i5);
        notifyDatagramTransferStateChangedToSessionController(i2);
        this.mPointingAppController.updateSendDatagramTransferState(i, i2, i3, i4, i5);
        retryPollPendingDatagramsInDemoMode();
    }

    private boolean shouldSuppressDatagramTransferStateUpdate(int i) {
        return SatelliteController.getInstance().isSatelliteAttachRequired() && i == 3 && this.mSatelltieModemState.get() == 6;
    }

    protected void updateReceiveStatus(int i, int i2, int i3, int i4, int i5) {
        plogd("updateReceiveStatus subId: " + i + " datagramType: " + i2 + " datagramTransferState: " + i3 + " receivePendingCount: " + i4 + " errorCode: " + i5);
        this.mReceiveSubId.set(i);
        this.mDatagramType.set(i2);
        this.mReceiveDatagramTransferState.set(i3);
        this.mReceivePendingCount.set(i4);
        this.mReceiveErrorCode.set(i5);
        notifyDatagramTransferStateChangedToSessionController(i2);
        this.mPointingAppController.updateReceiveDatagramTransferState(i, i3, i4, i5);
        retryPollPendingDatagramsInDemoMode();
        if (isPollingInIdleState()) {
            this.mDatagramDispatcher.retrySendingDatagrams();
        }
    }

    protected int getReceivePendingCount() {
        return this.mReceivePendingCount.get();
    }

    protected boolean isEmergencyCommunicationEstablished() {
        return this.mDatagramDispatcher.isEmergencyCommunicationEstablished();
    }

    protected void onSatelliteModemStateChanged(int i) {
        this.mSatelltieModemState.set(i);
        this.mDatagramDispatcher.onSatelliteModemStateChanged(i);
        this.mDatagramReceiver.onSatelliteModemStateChanged(i);
    }

    void onSmsReceived(int i) {
        updateReceiveStatus(i, 6, 4, getReceivePendingCount(), 0);
        updateReceiveStatus(i, 6, 5, getReceivePendingCount(), 0);
        updateReceiveStatus(i, 6, 0, getReceivePendingCount(), 0);
    }

    protected void setDeviceAlignedWithSatellite(boolean z) {
        this.mDatagramDispatcher.setDeviceAlignedWithSatellite(z);
        this.mDatagramReceiver.setDeviceAlignedWithSatellite(z);
        if (z) {
            retryPollPendingDatagramsInDemoMode();
        }
    }

    protected boolean isReceivingDatagrams() {
        return this.mReceiveDatagramTransferState.get() == 4;
    }

    protected boolean needsWaitingForSatelliteConnected(int i) {
        if (!SatelliteController.getInstance().isSatelliteAttachRequired()) {
            return false;
        }
        int i2 = this.mSatelltieModemState.get();
        plogd("needsWaitingForSatelliteConnected: datagramType=" + i + " satelliteModemState=" + i2);
        if (i == 3 && i2 == 6) {
            return false;
        }
        return ((i == 7 && i2 == 6 && this.mContext.getResources().getBoolean(R.bool.config_supportProfilesOnNonMainUser)) || i2 == 7 || i2 == 2) ? false : true;
    }

    protected boolean isSendingInIdleState() {
        return this.mSendDatagramTransferState.get() == 0;
    }

    protected boolean isPollingInIdleState() {
        return this.mReceiveDatagramTransferState.get() == 0;
    }

    protected void setDemoMode(boolean z) {
        this.mIsDemoMode.set(z);
        this.mDatagramDispatcher.setDemoMode(z);
        this.mDatagramReceiver.setDemoMode(z);
        if (!z) {
            synchronized (this.mLock) {
                this.mDemoModeDatagramList.clear();
            }
            setDeviceAlignedWithSatellite(false);
        }
        plogd("setDemoMode: mIsDemoMode=" + z);
    }

    protected SatelliteDatagram popDemoModeDatagram() {
        SatelliteDatagram satelliteDatagram;
        if (!this.mIsDemoMode.get()) {
            return null;
        }
        synchronized (this.mLock) {
            try {
                plogd("popDemoModeDatagram");
                satelliteDatagram = this.mDemoModeDatagramList.size() > 0 ? (SatelliteDatagram) this.mDemoModeDatagramList.remove(0) : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        return satelliteDatagram;
    }

    protected void pushDemoModeDatagram(int i, SatelliteDatagram satelliteDatagram) {
        if (this.mIsDemoMode.get() && SatelliteServiceUtils.isSosMessage(i)) {
            synchronized (this.mLock) {
                this.mDemoModeDatagramList.add(satelliteDatagram);
                plogd("pushDemoModeDatagram size=" + this.mDemoModeDatagramList.size());
            }
        }
    }

    long getSatelliteAlignedTimeoutDuration() {
        return this.mAlignTimeoutDuration.get();
    }

    protected long getDatagramWaitTimeForConnectedState(boolean z) {
        long j;
        if (z) {
            j = this.mDatagramWaitTimeForConnectedStateForLastMessage.get();
        } else {
            j = this.mDatagramWaitTimeForConnectedState.get();
        }
        int i = this.mSatelltieModemState.get();
        logd("getDatagramWaitTimeForConnectedState: isLastSosMessage=" + z + ", timeout=" + j + ", modemState=" + i);
        return (i == 4 || i == 0) ? j + this.mModemImageSwitchingDuration.get() : j;
    }

    boolean setDatagramControllerTimeoutDuration(boolean z, int i, long j) {
        if (!isMockModemAllowed()) {
            ploge("Updating timeout duration is not allowed");
            return false;
        }
        plogd("setDatagramControllerTimeoutDuration: timeoutMillis=" + j + ", reset=" + z + ", timeoutType=" + i);
        if (i == 1) {
            AtomicLong atomicLong = this.mAlignTimeoutDuration;
            if (z) {
                atomicLong.set(SATELLITE_ALIGN_TIMEOUT);
            } else {
                atomicLong.set(j);
            }
        } else if (i == 2) {
            AtomicLong atomicLong2 = this.mDatagramWaitTimeForConnectedState;
            if (z) {
                atomicLong2.set(getDatagramWaitForConnectedStateTimeoutMillis());
                this.mModemImageSwitchingDuration.set(getSatelliteModemImageSwitchingDurationMillis());
            } else {
                atomicLong2.set(j);
                this.mModemImageSwitchingDuration.set(0L);
            }
        } else if (i == 3) {
            this.mDatagramDispatcher.setWaitTimeForDatagramSendingResponse(z, j);
        } else if (i == 4) {
            this.mDatagramDispatcher.setTimeoutDatagramDelayInDemoMode(z, j);
        } else {
            ploge("Invalid timeout type " + i);
            return false;
        }
        return true;
    }

    boolean setDatagramControllerBooleanConfig(boolean z, int i, boolean z2) {
        if (!isMockModemAllowed()) {
            loge("Updating boolean config is not allowed");
            return false;
        }
        logd("setDatagramControllerTimeoutDuration: booleanType=" + i + ", reset=" + z + ", enable=" + z2);
        if (i == 1) {
            AtomicBoolean atomicBoolean = this.mWaitForDeviceAlignmentInDemoDatagram;
            if (z) {
                atomicBoolean.set(getWaitForDeviceAlignmentInDemoDatagramFromResources());
            } else {
                atomicBoolean.set(z2);
            }
            return true;
        }
        loge("Invalid boolean type " + i);
        return false;
    }

    private boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true);
    }

    private void notifyDatagramTransferStateChangedToSessionController(int i) {
        SatelliteSessionController satelliteSessionController = SatelliteSessionController.getInstance();
        if (satelliteSessionController == null) {
            ploge("notifyDatagramTransferStateChangeToSessionController: SatelliteSessionController is not initialized yet");
        } else {
            satelliteSessionController.onDatagramTransferStateChanged(this.mSendDatagramTransferState.get(), this.mReceiveDatagramTransferState.get(), i);
        }
    }

    private long getDatagramWaitForConnectedStateTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_defaultUiModeType);
    }

    private long getSatelliteModemImageSwitchingDurationMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_veryLongPressTimeout);
    }

    private long getDatagramWaitForConnectedStateForLastMessageTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_defaultRingVibrationIntensity);
    }

    void setShouldSendDatagramToModemInDemoMode(boolean z) {
        this.mDatagramDispatcher.setShouldSendDatagramToModemInDemoMode(Boolean.valueOf(z));
    }

    private void retryPollPendingDatagramsInDemoMode() {
        if (this.mIsDemoMode.get() && isSendingInIdleState() && isPollingInIdleState() && !isDemoModeDatagramListEmpty()) {
            pollPendingSatelliteDatagrams(SatelliteController.getInstance().getSelectedSatelliteSubId(), new Consumer<Integer>() { // from class: com.android.internal.telephony.satellite.DatagramController.1
                @Override // java.util.function.Consumer
                public void accept(Integer num) {
                    if (num.intValue() != 0) {
                        DatagramController.this.plogd("retryPollPendingDatagramsInDemoMode result: " + num);
                    }
                }
            });
        }
    }

    private boolean isDemoModeDatagramListEmpty() {
        boolean zIsEmpty;
        synchronized (this.mLock) {
            zIsEmpty = this.mDemoModeDatagramList.isEmpty();
        }
        return zIsEmpty;
    }

    protected boolean waitForAligningToSatellite(boolean z) {
        if (z) {
            return false;
        }
        return getWaitForDeviceAlignmentInDemoDatagram();
    }

    private boolean getWaitForDeviceAlignmentInDemoDatagram() {
        return this.mWaitForDeviceAlignmentInDemoDatagram.get();
    }

    private boolean getWaitForDeviceAlignmentInDemoDatagramFromResources() {
        try {
            return this.mContext.getResources().getBoolean(17892086);
        } catch (Resources.NotFoundException e) {
            loge("getWaitForDeviceAlignmentInDemoDatagram: ex=" + e);
            return false;
        }
    }

    private static void logd(String str) {
        Log.d("DatagramController", str);
    }

    private static void loge(String str) {
        Log.e("DatagramController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("DatagramController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("DatagramController", str);
        }
    }

    private void ploge(String str) {
        Log.e("DatagramController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("DatagramController", str);
        }
    }
}
