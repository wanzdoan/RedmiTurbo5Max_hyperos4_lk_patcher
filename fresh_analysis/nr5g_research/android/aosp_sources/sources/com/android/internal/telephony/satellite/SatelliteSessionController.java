package com.android.internal.telephony.satellite;

import android.R;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncResult;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.telephony.PersistentLogger;
import android.telephony.ServiceState;
import android.telephony.satellite.ISatelliteModemStateCallback;
import android.telephony.satellite.stub.ISatelliteGateway;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.IState;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.metrics.SessionMetricsStats;
import com.android.internal.telephony.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteSessionController extends StateMachine {
    public static final long DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS = 30000;
    public static final long DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS = 3000;
    protected static final int EVENT_ESOS_INACTIVITY_TIMER_TIMED_OUT = 11;
    protected static final int EVENT_NB_IOT_INACTIVITY_TIMER_TIMED_OUT = 6;
    protected static final int EVENT_P2P_SMS_INACTIVITY_TIMER_TIMED_OUT = 14;
    protected static final int EVENT_PERIODIC_SUSPENSION_TIMER_EXPIRED = 17;
    protected static final int EVENT_SCREEN_OFF_INACTIVITY_TIMER_TIMED_OUT = 10;
    public static final String SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS = "satellite_stay_at_listening_from_receiving_millis";
    public static final String SATELLITE_STAY_AT_LISTENING_FROM_SENDING_MILLIS = "satellite_stay_at_listening_from_sending_millis";
    private static SatelliteSessionController sInstance;
    private final AlarmManager.OnAlarmListener mAlarmListener;
    private AlarmManager mAlarmManager;
    private final ConnectedState mConnectedState;
    private final Context mContext;
    private int mCurrentState;
    private final DatagramController mDatagramController;
    private DeviceStateMonitor mDeviceStateMonitor;
    private final DisablingState mDisablingState;
    private final EnablingState mEnablingState;
    private final Executor mExecutor;
    private final ExponentialBackoff mExponentialBackoff;
    private FeatureFlags mFeatureFlags;
    private final IdleState mIdleState;
    private boolean mIgnoreCellularServiceState;
    private long mInactivityStartTimestamp;
    private AtomicBoolean mIsBinding;
    private AtomicBoolean mIsBound;
    private Boolean mIsConcurrentTnScanningSupportedForCtsTest;
    private boolean mIsDemoMode;
    private boolean mIsDeviceAlignedWithSatellite;
    private boolean mIsRegisteredScreenStateChanged;
    final boolean mIsSatelliteSupported;
    boolean mIsScreenOn;
    protected AtomicBoolean mIsSendingTriggeredDuringTransferringState;
    private Boolean mIsTnScanningDuringSatelliteSessionAllowedForCtsTest;
    private DatagramTransferState mLastDatagramTransferState;
    private final ConcurrentHashMap mListeners;
    private final ListeningState mListeningState;
    private LocationManager mLocationManager;
    private final NotConnectedState mNotConnectedState;
    private PersistentLogger mPersistentLogger;
    private final PowerOffState mPowerOffState;
    private int mPreviousState;
    private final SatelliteController mSatelliteController;
    private ISatelliteGateway mSatelliteGatewayService;
    private SatelliteGatewayServiceConnection mSatelliteGatewayServiceConnection;
    private String mSatelliteGatewayServicePackageName;
    private final SatelliteModemInterface mSatelliteModemInterface;
    private long mSatelliteNbIotInactivityTimeoutMillis;
    private long mSatelliteStayAtListeningFromReceivingMillis;
    private long mSatelliteStayAtListeningFromSendingMillis;
    private SessionMetricsStats mSessionMetricsStats;
    protected long mSuspensionIntervalMillis;
    private final SuspensionState mSuspensionState;
    private final TransferringState mTransferringState;
    private final UnavailableState mUnavailableState;
    private long mWaitTimeForSatelliteSuspendingResponse;
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    public static final long DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_SENDING_MILLIS = 180000;
    private static final long LOCATION_TIMEOUT_MS = DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_SENDING_MILLIS;

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isReceiving(int i) {
        return i == 4 || i == 5 || i == 6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSending(int i) {
        return i == 1 || i == 2;
    }

    public static SatelliteSessionController getInstance() {
        if (sInstance == null) {
            Log.e("SatelliteSessionController", "SatelliteSessionController was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteSessionController make(Context context, Looper looper, FeatureFlags featureFlags, boolean z) {
        ConcurrentHashMap concurrentHashMap;
        boolean z2;
        SatelliteSessionController satelliteSessionController = sInstance;
        if (satelliteSessionController == null || z != satelliteSessionController.mIsSatelliteSupported) {
            if (satelliteSessionController != null) {
                concurrentHashMap = satelliteSessionController.mListeners;
                z2 = satelliteSessionController.mIgnoreCellularServiceState;
                satelliteSessionController.cleanUpResource();
            } else {
                concurrentHashMap = null;
                z2 = false;
            }
            sInstance = new SatelliteSessionController(context, looper, featureFlags, z, SatelliteModemInterface.getInstance());
            if (concurrentHashMap != null) {
                Log.d("SatelliteSessionController", "make() existingListeners: " + concurrentHashMap.size());
                sInstance.mListeners.putAll(concurrentHashMap);
            }
            if (z2) {
                Log.d("SatelliteSessionController", "make() existIgnoreCellularServiceState is true");
                sInstance.mIgnoreCellularServiceState = true;
            }
        }
        return sInstance;
    }

    protected SatelliteSessionController(Context context, Looper looper, FeatureFlags featureFlags, boolean z, SatelliteModemInterface satelliteModemInterface) {
        super("SatelliteSessionController", looper);
        this.mIsBound = new AtomicBoolean(false);
        this.mIsBinding = new AtomicBoolean(false);
        this.mSatelliteGatewayServicePackageName = "";
        this.mIsRegisteredScreenStateChanged = false;
        UnavailableState unavailableState = new UnavailableState();
        this.mUnavailableState = unavailableState;
        PowerOffState powerOffState = new PowerOffState();
        this.mPowerOffState = powerOffState;
        EnablingState enablingState = new EnablingState();
        this.mEnablingState = enablingState;
        DisablingState disablingState = new DisablingState();
        this.mDisablingState = disablingState;
        IdleState idleState = new IdleState();
        this.mIdleState = idleState;
        SuspensionState suspensionState = new SuspensionState();
        this.mSuspensionState = suspensionState;
        TransferringState transferringState = new TransferringState();
        this.mTransferringState = transferringState;
        ListeningState listeningState = new ListeningState();
        this.mListeningState = listeningState;
        NotConnectedState notConnectedState = new NotConnectedState();
        this.mNotConnectedState = notConnectedState;
        ConnectedState connectedState = new ConnectedState();
        this.mConnectedState = connectedState;
        this.mIgnoreCellularServiceState = false;
        this.mIsConcurrentTnScanningSupportedForCtsTest = null;
        this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest = null;
        this.mIsDemoMode = false;
        this.mIsScreenOn = true;
        this.mIsDeviceAlignedWithSatellite = false;
        this.mInactivityStartTimestamp = 0L;
        this.mLastDatagramTransferState = new DatagramTransferState(-1, -1, 0);
        this.mPersistentLogger = null;
        this.mAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                SatelliteSessionController.this.plogd("onAlarm: screen off timer expired");
                SatelliteSessionController.this.sendMessage(10);
            }
        };
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mSatelliteModemInterface = satelliteModemInterface;
        SatelliteController satelliteController = SatelliteController.getInstance();
        this.mSatelliteController = satelliteController;
        this.mDatagramController = DatagramController.getInstance();
        this.mSatelliteStayAtListeningFromSendingMillis = getSatelliteStayAtListeningFromSendingMillis();
        this.mSatelliteStayAtListeningFromReceivingMillis = getSatelliteStayAtListeningFromReceivingMillis();
        this.mSatelliteNbIotInactivityTimeoutMillis = getSatelliteNbIotInactivityTimeoutMillis();
        this.mWaitTimeForSatelliteSuspendingResponse = getWaitForSatelliteSuspensionResponseTimeoutMillis();
        this.mSuspensionIntervalMillis = getSuspensionIntervalMillis();
        this.mListeners = new ConcurrentHashMap();
        this.mIsSendingTriggeredDuringTransferringState = new AtomicBoolean(false);
        this.mPreviousState = -1;
        this.mCurrentState = -1;
        this.mIsSatelliteSupported = z;
        this.mExponentialBackoff = new ExponentialBackoff(2000L, 64000L, 2, looper, new Runnable() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
        Phone satellitePhone = satelliteController.getSatellitePhone();
        this.mDeviceStateMonitor = (satellitePhone == null ? SatelliteServiceUtils.getPhone() : satellitePhone).getDeviceStateMonitor();
        this.mSessionMetricsStats = SessionMetricsStats.getInstance();
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
        this.mExecutor = new Executor() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda3
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.lambda$new$1(runnable);
            }
        };
        addState(unavailableState);
        addState(powerOffState);
        addState(enablingState);
        addState(disablingState);
        addState(idleState);
        addState(transferringState);
        addState(listeningState);
        addState(notConnectedState);
        addState(connectedState);
        addState(suspensionState);
        setInitialState(z);
        start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if ((!this.mIsBound.get() || this.mSatelliteGatewayService == null) && !this.mIsBinding.get()) {
            if (this.mSatelliteGatewayServiceConnection != null) {
                this.mIsBound.set(false);
                this.mIsBinding.set(false);
                unbindService();
            }
            bindService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Runnable runnable) {
        getHandler().post(runnable);
    }

    public void onDatagramTransferStateChanged(int i, int i2, int i3) {
        DatagramTransferState datagramTransferState = new DatagramTransferState(i, i2, i3);
        this.mLastDatagramTransferState = datagramTransferState;
        sendMessage(1, datagramTransferState);
        if (i == 1) {
            this.mIsSendingTriggeredDuringTransferringState.set(true);
        }
        if (datagramTransferState.isIdle()) {
            checkForInactivity();
        } else {
            endUserInactivity();
        }
    }

    public void onSatelliteEnabledStateChanged(boolean z) {
        plogd("onSatelliteEnabledStateChanged: enabled= " + z);
        sendMessage(3, Boolean.valueOf(z));
    }

    public void onSatelliteEnablementStarted(boolean z) {
        sendMessage(7, Boolean.valueOf(z));
    }

    public void onSatelliteEnablementFailed(boolean z) {
        sendMessage(8, Boolean.valueOf(z));
    }

    public void onSatelliteModemStateChanged(int i) {
        sendMessage(4, i);
    }

    public void onEmergencyModeChanged(final boolean z) {
        plogd("onEmergencyModeChanged " + z);
        final ArrayList arrayList = new ArrayList();
        this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onEmergencyModeChanged$2(z, arrayList, (ISatelliteModemStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onEmergencyModeChanged$3((ISatelliteModemStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyModeChanged$2(boolean z, List list, ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        try {
            iSatelliteModemStateCallback.onEmergencyModeChanged(z);
        } catch (RemoteException e) {
            plogd("onEmergencyModeChanged RemoteException: " + e);
            list.add(iSatelliteModemStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyModeChanged$3(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        this.mListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    public void registerForSatelliteModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        try {
            plogd("registerForSatelliteModemStateChanged: callback=" + iSatelliteModemStateCallback + ", mCurrentState=" + this.mCurrentState);
            iSatelliteModemStateCallback.onSatelliteModemStateChanged(this.mCurrentState);
            iSatelliteModemStateCallback.onEmergencyModeChanged(this.mSatelliteController.getRequestIsEmergency());
            this.mListeners.put(iSatelliteModemStateCallback.asBinder(), iSatelliteModemStateCallback);
        } catch (RemoteException e) {
            ploge("registerForSatelliteModemStateChanged: Got RemoteException ex=" + e);
        }
    }

    public void unregisterForSatelliteModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        this.mListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    boolean setSatelliteListeningTimeoutDuration(long j) {
        if (!isMockModemAllowed()) {
            ploge("Updating listening timeout duration is not allowed");
            return false;
        }
        plogd("setSatelliteListeningTimeoutDuration: timeoutMillis=" + j);
        if (j == 0) {
            this.mSatelliteStayAtListeningFromSendingMillis = getSatelliteStayAtListeningFromSendingMillis();
            this.mSatelliteStayAtListeningFromReceivingMillis = getSatelliteStayAtListeningFromReceivingMillis();
            this.mSatelliteNbIotInactivityTimeoutMillis = getSatelliteNbIotInactivityTimeoutMillis();
            return true;
        }
        this.mSatelliteStayAtListeningFromSendingMillis = j;
        this.mSatelliteStayAtListeningFromReceivingMillis = j;
        this.mSatelliteNbIotInactivityTimeoutMillis = j;
        return true;
    }

    public boolean setSatelliteIgnoreCellularServiceState(boolean z) {
        plogd("setSatelliteIgnoreCellularServiceState : old = " + this.mIgnoreCellularServiceState + " new : " + z);
        this.mIgnoreCellularServiceState = z;
        return true;
    }

    boolean setSatelliteGatewayServicePackageName(String str) {
        if (!isMockModemAllowed()) {
            ploge("setSatelliteGatewayServicePackageName: modifying satellite gateway service package name is not allowed");
            return false;
        }
        plogd("setSatelliteGatewayServicePackageName: config_satellite_gateway_service_package is updated, new packageName=" + str);
        if (str == null || str.equals("null")) {
            this.mSatelliteGatewayServicePackageName = "";
        } else {
            this.mSatelliteGatewayServicePackageName = str;
        }
        if (this.mSatelliteGatewayServiceConnection == null) {
            return true;
        }
        this.mIsBound.set(false);
        this.mIsBinding.set(false);
        unbindService();
        bindService();
        return true;
    }

    boolean setTnScanningSupport(boolean z, boolean z2, boolean z3) {
        if (!isMockModemAllowed()) {
            ploge("setTnScanningSupport: modifying TN scanning support is not allowed");
            return false;
        }
        plogd("setTnScanningSupport: reset=" + z + ", concurrentTnScanningSupported=" + z2 + ", tnScanningDuringSatelliteSessionAllowed=" + z3);
        if (z) {
            this.mIsConcurrentTnScanningSupportedForCtsTest = null;
            this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest = null;
            return true;
        }
        this.mIsConcurrentTnScanningSupportedForCtsTest = Boolean.valueOf(z2);
        this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest = Boolean.valueOf(z3);
        return true;
    }

    public void setDemoMode(boolean z) {
        this.mIsDemoMode = z;
    }

    public void setDeviceAlignedWithSatellite(boolean z) {
        this.mIsDeviceAlignedWithSatellite = z;
        plogd("setDeviceAlignedWithSatellite: isAligned " + z);
        if (this.mIsDeviceAlignedWithSatellite) {
            stopEsosInactivityTimer();
            stopP2pSmsInactivityTimer();
            endUserInactivity();
            return;
        }
        int i = this.mCurrentState;
        if (i == 6 || i == 7 || i == 0) {
            evaluateStartingEsosInactivityTimer();
            evaluateStartingP2pSmsInactivityTimer();
        }
        checkForInactivity();
    }

    public boolean isInEnablingState() {
        try {
            IState currentState = getCurrentState();
            plogd("isInEnablingState: getCurrentState=" + currentState);
            return currentState == this.mEnablingState;
        } catch (Exception e) {
            plogw("isInEnablingState: Exception: " + e + ", mCurrentState=" + this.mCurrentState);
            return this.mCurrentState == 8;
        }
    }

    public boolean isInDisablingState() {
        try {
            IState currentState = getCurrentState();
            plogd("isInDisablingState: getCurrentState=" + currentState);
            return currentState == this.mDisablingState;
        } catch (Exception e) {
            plogw("isInDisablingState: Exception: " + e + ", mCurrentState=" + this.mCurrentState);
            return this.mCurrentState == 9;
        }
    }

    public void cleanUpResource() {
        plogd("cleanUpResource");
        this.mIsDeviceAlignedWithSatellite = false;
        this.mInactivityStartTimestamp = 0L;
        unregisterForScreenStateChanged();
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager != null) {
            alarmManager.cancel(this.mAlarmListener);
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null) {
                phone.unregisterForServiceStateChanged(getHandler());
                plogd("cleanUpResource: unregisterForServiceStateChanged phoneId " + phone.getPhoneId());
            }
        }
        quitNow();
    }

    public void onCellularServiceStateChanged(ServiceState serviceState) {
        sendMessage(13, new AsyncResult((Object) null, serviceState, (Throwable) null));
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.mAlarmManager = alarmManager;
    }

    private boolean isDemoMode() {
        return this.mIsDemoMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionToSuspensionOrIdle() {
        if (isSuspendAllowedDuringSatelliteSession()) {
            transitionTo(this.mSuspensionState);
        } else {
            transitionTo(this.mIdleState);
        }
    }

    private static class DatagramTransferState {
        public int datagramType;
        public int receiveState;
        public int sendState;

        DatagramTransferState(int i, int i2, int i3) {
            this.sendState = i;
            this.receiveState = i2;
            this.datagramType = i3;
        }

        public boolean isIdle() {
            return this.sendState == 0 && this.receiveState == 0;
        }
    }

    private class UnavailableState extends State {
        private UnavailableState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering UnavailableState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 5;
            SatelliteSessionController.this.notifyStateChangedEvent(5);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.ploge("UnavailableState: receive msg " + SatelliteSessionController.this.getWhatToString(message.what) + " unexpectedly");
            return true;
        }
    }

    private class PowerOffState extends State {
        private PowerOffState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering PowerOffState");
            SatelliteSessionController.this.mSatelliteController.moveSatelliteToOffStateAndCleanUpResources(15);
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 4;
            SatelliteSessionController.this.mIsSendingTriggeredDuringTransferringState.set(false);
            SatelliteSessionController.this.unbindService();
            SatelliteSessionController.this.stopNbIotInactivityTimer();
            SatelliteSessionController.this.stopEsosInactivityTimer();
            SatelliteSessionController.this.stopP2pSmsInactivityTimer();
            SatelliteSessionController.this.endUserInactivity();
            DemoSimulator.getInstance().onSatelliteModeOff();
            SatelliteSessionController.this.notifyStateChangedEvent(4);
            SatelliteSessionController.this.unregisterForScreenStateChanged();
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting PowerOffState");
            SatelliteSessionController.this.plogd("Attempting to bind to SatelliteGatewayService.");
            SatelliteSessionController.this.bindService();
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("PowerOffState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            if (message.what != 7) {
                return true;
            }
            handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
            return true;
        }

        private void handleSatelliteEnablementStarted(boolean z) {
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (z) {
                satelliteSessionController.transitionTo(satelliteSessionController.mEnablingState);
            } else {
                satelliteSessionController.plogw("Unexpected satellite disablement started in PowerOff state");
            }
        }
    }

    private class EnablingState extends State {
        private EnablingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering EnablingState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 8;
            SatelliteSessionController.this.notifyStateChangedEvent(8);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting EnablingState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("EnablingState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 3) {
                handleSatelliteEnabledStateChanged(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i == 4) {
                handleSatelliteModemStateChanged(message);
                return true;
            }
            if (i == 7) {
                SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i != 8) {
                return true;
            }
            boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (zBooleanValue) {
                satelliteSessionController.transitionTo(satelliteSessionController.mPowerOffState);
                return true;
            }
            satelliteSessionController.ploge("Unexpected failed disable event in EnablingState");
            return true;
        }

        private void handleSatelliteEnabledStateChanged(boolean z) {
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (!z) {
                satelliteSessionController.removeDeferredMessages(4);
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mPowerOffState);
                return;
            }
            boolean zIsSatelliteAttachRequired = satelliteSessionController.mSatelliteController.isSatelliteAttachRequired();
            SatelliteSessionController satelliteSessionController3 = SatelliteSessionController.this;
            if (zIsSatelliteAttachRequired) {
                satelliteSessionController3.transitionTo(satelliteSessionController3.mNotConnectedState);
            } else {
                satelliteSessionController3.transitionToSuspensionOrIdle();
            }
            DemoSimulator.getInstance().onSatelliteModeOn();
            SatelliteSessionController.this.registerForScreenStateChanged();
        }

        private void handleSatelliteModemStateChanged(Message message) {
            int i = message.arg1;
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (i == 4) {
                satelliteSessionController.transitionTo(satelliteSessionController.mPowerOffState);
            } else {
                satelliteSessionController.deferMessage(message);
            }
        }
    }

    private class DisablingState extends State {
        private DisablingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering DisablingState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 9;
            SatelliteSessionController.this.notifyStateChangedEvent(9);
            SatelliteSessionController.this.unregisterForScreenStateChanged();
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting DisablingState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("DisablingState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 3) {
                handleSatelliteEnabledStateChanged(((Boolean) message.obj).booleanValue());
            } else if (i == 4) {
                handleEventSatelliteModemStateChanged(message);
            } else if (i == 8) {
                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                if (zBooleanValue) {
                    satelliteSessionController.mPreviousState = 4;
                    SatelliteSessionController.this.plogd("Enable request has failed. Set mPreviousState to OFF");
                } else if (satelliteSessionController.mPreviousState == 7 || SatelliteSessionController.this.mPreviousState == 2 || SatelliteSessionController.this.mPreviousState == 1) {
                    SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                    satelliteSessionController2.transitionTo(satelliteSessionController2.mConnectedState);
                } else {
                    int i2 = SatelliteSessionController.this.mPreviousState;
                    SatelliteSessionController satelliteSessionController3 = SatelliteSessionController.this;
                    if (i2 == 8) {
                        satelliteSessionController3.transitionTo(satelliteSessionController3.mEnablingState);
                    } else {
                        int i3 = satelliteSessionController3.mPreviousState;
                        SatelliteSessionController satelliteSessionController4 = SatelliteSessionController.this;
                        if (i3 == 4) {
                            satelliteSessionController4.transitionTo(satelliteSessionController4.mPowerOffState);
                        } else {
                            satelliteSessionController4.transitionTo(satelliteSessionController4.mNotConnectedState);
                        }
                    }
                }
            }
            return true;
        }

        private void handleSatelliteEnabledStateChanged(boolean z) {
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (z) {
                satelliteSessionController.mPreviousState = 6;
                SatelliteSessionController.this.plogd("Enable request has succeeded. Set mPreviousState to NOT_CONNECTED");
            } else {
                satelliteSessionController.transitionTo(satelliteSessionController.mPowerOffState);
            }
        }

        private void handleEventSatelliteModemStateChanged(Message message) {
            int i = message.arg1;
            if (i == 6) {
                SatelliteSessionController.this.mPreviousState = i;
            } else if (i == 4) {
                SatelliteSessionController.this.plogd("Modem OFF state will be processed after getting the confirmation of the disable request");
                SatelliteSessionController.this.deferMessage(message);
            }
        }
    }

    private class IdleState extends State {
        private IdleState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering IdleState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 0;
            SatelliteSessionController.this.mIsSendingTriggeredDuringTransferringState.set(false);
            SatelliteSessionController.this.stopNbIotInactivityTimer();
            FeatureFlags unused = SatelliteSessionController.this.mFeatureFlags;
            if (SatelliteSessionController.this.isSuspendAllowedDuringSatelliteSession()) {
                startPeriodicSuspensionTimer();
            }
            boolean zIsTnScanningAllowedDuringSatelliteSession = SatelliteSessionController.this.isTnScanningAllowedDuringSatelliteSession();
            SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
            if (zIsTnScanningAllowedDuringSatelliteSession) {
                SatelliteSessionController.this.mSatelliteModemInterface.enableCellularModemWhileSatelliteModeIsOn(true, satelliteSessionController2.obtainMessage(12));
            } else {
                satelliteSessionController2.plogd("Device does not allow cellular modem scanning");
            }
            if (SatelliteSessionController.this.isConcurrentTnScanningSupported() || !zIsTnScanningAllowedDuringSatelliteSession) {
                SatelliteSessionController.this.plogd("IDLE state is hidden from clients");
            } else {
                SatelliteSessionController.this.notifyStateChangedEvent(0);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            Object obj;
            SatelliteSessionController.this.plogd("IdleState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                handleEventDatagramTransferStateChanged((DatagramTransferState) message.obj);
            } else if (i == 7) {
                SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
            } else if (i == 17) {
                SatelliteSessionController.this.plogd("IdleState: EVENT_PERIODIC_SUSPENSION_TIMER_EXPIRED received");
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mSuspensionState);
            } else if (i == 3) {
                SatelliteSessionController.this.handleSatelliteEnabledStateChanged(!((Boolean) message.obj).booleanValue(), "IdleState");
            } else if (i == 4) {
                handleSatelliteModemStateChanged(message);
            } else if (i == 5) {
                handleEventDisableCellularModemWhileSatelliteModeIsOnDone((AsyncResult) message.obj);
            } else if (i == 9) {
                SatelliteSessionController.this.handleEventScreenStateChanged((AsyncResult) message.obj);
            } else if (i == 10) {
                SatelliteSessionController.this.handleEventScreenOffInactivityTimerTimedOut();
            } else if (i == 12) {
                SatelliteSessionController.this.plogd("EVENT_ENABLE_CELLULAR_MODEM_WHILE_SATELLITE_MODE_IS_ON_DONE");
            } else if (i == 13) {
                if (!SatelliteSessionController.this.mIgnoreCellularServiceState) {
                    Object obj2 = message.obj;
                    AsyncResult asyncResult = obj2 != null ? (AsyncResult) obj2 : null;
                    if (asyncResult == null || (obj = asyncResult.result) == null) {
                        SatelliteSessionController.this.plogd("IdleState: processing: can't access ServiceState");
                    } else {
                        handleEventServiceStateChanged((ServiceState) obj);
                    }
                } else {
                    SatelliteSessionController.this.plogd("IdleState: processing: ignore EVENT_SERVICE_STATE_CHANGED");
                }
            }
            return true;
        }

        private void startPeriodicSuspensionTimer() {
            SatelliteSessionController.this.removeMessages(17);
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.sendMessageDelayed(17, satelliteSessionController.mSuspensionIntervalMillis);
        }

        private void stopPeriodicSuspensionTimer() {
            SatelliteSessionController.this.removeMessages(17);
        }

        private void handleEventDatagramTransferStateChanged(DatagramTransferState datagramTransferState) {
            int i;
            int i2 = datagramTransferState.sendState;
            if (i2 == 1 || (i = datagramTransferState.receiveState) == 4) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mTransferringState);
            } else if (i2 == 8 || i == 8) {
                boolean zIsSatelliteAttachRequired = SatelliteSessionController.this.mSatelliteController.isSatelliteAttachRequired();
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                if (zIsSatelliteAttachRequired) {
                    satelliteSessionController2.transitionTo(satelliteSessionController2.mNotConnectedState);
                } else {
                    satelliteSessionController2.ploge("Unexpected transferring state received for non-NB-IOT NTN");
                }
            }
        }

        private void handleEventServiceStateChanged(ServiceState serviceState) {
            if (serviceState.getVoiceRegState() != 0 && serviceState.getDataRegState() != 0 && !serviceState.isEmergencyOnly()) {
                SatelliteSessionController.this.plogd("handleEventServiceStateChanged: is not IN_SERVICE or EMERGENCY_ONLY");
                return;
            }
            boolean requestIsEmergency = SatelliteSessionController.this.mSatelliteController.getRequestIsEmergency();
            if (requestIsEmergency) {
                boolean zIsEmergencyCommunicationEstablished = SatelliteSessionController.this.mDatagramController == null ? false : SatelliteSessionController.this.mDatagramController.isEmergencyCommunicationEstablished();
                boolean zTurnOffSatelliteSessionForEmergencyCall = SatelliteSessionController.this.mSatelliteController.turnOffSatelliteSessionForEmergencyCall(SatelliteSessionController.this.getSubId());
                if (zIsEmergencyCommunicationEstablished || !zTurnOffSatelliteSessionForEmergencyCall) {
                    SatelliteSessionController.this.logd("handleEventServiceStateChanged: can't disable emergency satellite session");
                    return;
                }
            }
            SatelliteSessionController.this.mSatelliteController.requestSatelliteEnabled(false, false, requestIsEmergency, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.IdleState.1
                public void accept(int i) {
                    SatelliteSessionController.this.plogd("requestSatelliteEnabled result=" + i);
                    if (i == 0) {
                        SatelliteSessionController.this.mSessionMetricsStats.addCountOfAutoExitDueToTnNetwork();
                    }
                }
            });
        }

        private void handleEventDisableCellularModemWhileSatelliteModeIsOnDone(AsyncResult asyncResult) {
            int satelliteError = SatelliteServiceUtils.getSatelliteError(asyncResult, "DisableCellularModemWhileSatelliteModeIsOnDone");
            SatelliteSessionController.this.plogd("Disable TN scanning done with result: " + satelliteError);
        }

        private void handleSatelliteModemStateChanged(Message message) {
            int i = message.arg1;
            if (i == 4) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mPowerOffState);
                return;
            }
            if (i == 6 || i == 7) {
                boolean zIsConcurrentTnScanningSupported = SatelliteSessionController.this.isConcurrentTnScanningSupported();
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                if (zIsConcurrentTnScanningSupported) {
                    satelliteSessionController2.plogd("Notifying the new state " + i + " to clients but still stay at IDLE state internally");
                    SatelliteSessionController.this.notifyStateChangedEvent(i);
                    return;
                }
                satelliteSessionController2.plogd("Ignoring the modem state " + i);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting IdleState");
            SatelliteSessionController.this.mSatelliteModemInterface.enableCellularModemWhileSatelliteModeIsOn(false, null);
            stopPeriodicSuspensionTimer();
        }
    }

    class SuspensionState extends State {
        private CancellationSignal mCancellationSignal;
        private List mDeferredMessages = new ArrayList();
        private int mLocationAttemptCount;

        SuspensionState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering SuspensionState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 10;
            this.mLocationAttemptCount = 0;
            this.mDeferredMessages.clear();
            suspendSatelliteMode();
            SatelliteSessionController.this.notifyStateChangedEvent(10);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("SuspensionState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1 || i == 7 || i == 3 || i == 4 || i == 9 || i == 10) {
                SatelliteSessionController.this.plogd("Deferring message: " + SatelliteSessionController.this.getWhatToString(message.what));
                this.mDeferredMessages.add(Message.obtain(message));
                resumeSatelliteMode();
            } else {
                if (i == 15) {
                    SatelliteSessionController.this.plogd("SuspensionState: EVENT_SUSPEND_SATELLITE_MODE_DONE received");
                    SatelliteSessionController.this.removeMessages(19);
                    Object obj = message.obj;
                    int satelliteError = SatelliteServiceUtils.getSatelliteError(obj != null ? (AsyncResult) obj : null, "SatelliteSessionController SuspensionState");
                    if (satelliteError == 0) {
                        queryCurrentLocation();
                    } else {
                        SatelliteSessionController.this.ploge("Modem suspend failed with result: " + satelliteError);
                        SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                        satelliteSessionController.transitionTo(satelliteSessionController.mIdleState);
                    }
                } else if (i != 16) {
                    switch (i) {
                        case 18:
                            SatelliteSessionController.this.loge("queryCurrentLocation: Location fetch timed out, attempt " + this.mLocationAttemptCount);
                            queryCurrentLocation();
                            break;
                        case 19:
                            SatelliteSessionController.this.plogd("SuspensionState:EVENT_WAIT_FOR_SUSPEND_SATELLITE_MODE_RESPONSE_TIMEOUT");
                            SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                            satelliteSessionController2.transitionTo(satelliteSessionController2.mIdleState);
                            break;
                        case 20:
                            SatelliteSessionController.this.plogd("SuspensionState:EVENT_WAIT_FOR_RESUME_SATELLITE_MODE_RESPONSE_TIMEOUT");
                            SatelliteSessionController.this.mSatelliteController.requestSatelliteEnabled(false, false, SatelliteSessionController.this.mSatelliteController.getRequestIsEmergency(), new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.SuspensionState.2
                                public void accept(int i2) {
                                    SatelliteSessionController.this.plogd("SuspensionState: requestSatelliteEnabled result=" + i2);
                                }
                            });
                            SatelliteSessionController satelliteSessionController3 = SatelliteSessionController.this;
                            satelliteSessionController3.transitionTo(satelliteSessionController3.mIdleState);
                            break;
                    }
                } else {
                    SatelliteSessionController.this.plogd("SuspensionState: EVENT_RESUME_SATELLITE_MODE_DONE received");
                    Object obj2 = message.obj;
                    int satelliteError2 = SatelliteServiceUtils.getSatelliteError(obj2 != null ? (AsyncResult) obj2 : null, "SatelliteSessionController Resume");
                    if (satelliteError2 != 0) {
                        SatelliteSessionController.this.ploge("Modem suspend failed with result: " + satelliteError2);
                        SatelliteSessionController.this.mSatelliteController.requestSatelliteEnabled(false, false, SatelliteSessionController.this.mSatelliteController.getRequestIsEmergency(), new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.SuspensionState.1
                            public void accept(int i2) {
                                SatelliteSessionController.this.plogd("SuspensionState: requestSatelliteEnabled result=" + i2);
                            }
                        });
                    }
                    SatelliteSessionController satelliteSessionController4 = SatelliteSessionController.this;
                    satelliteSessionController4.transitionTo(satelliteSessionController4.mIdleState);
                }
            }
            return true;
        }

        private void suspendSatelliteMode() {
            Message messageObtainMessage = SatelliteSessionController.this.obtainMessage(15);
            try {
                SatelliteSessionController.this.logd("Calling requestSatelliteSuspended(true)");
                SatelliteSessionController.this.mSatelliteModemInterface.requestSatelliteSuspended(true, messageObtainMessage);
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.sendMessageDelayed(19, satelliteSessionController.mWaitTimeForSatelliteSuspendingResponse);
            } catch (Exception e) {
                SatelliteSessionController.this.loge("SuspensionState: Failed to suspend satellite mode: " + e);
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mIdleState);
            }
        }

        private void cancelOngoingLocationQuery() {
            CancellationSignal cancellationSignal = this.mCancellationSignal;
            if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
                try {
                    this.mCancellationSignal.cancel();
                } catch (Exception e) {
                    SatelliteSessionController.this.loge("Error cancelling signal: " + e);
                }
            }
            SatelliteSessionController.this.removeMessages(18);
        }

        private void queryCurrentLocation() {
            int i = this.mLocationAttemptCount;
            if (i >= 2) {
                SatelliteSessionController.this.logd("queryCurrentLocation: Max location attempts reached.");
                resumeSatelliteMode();
                return;
            }
            this.mLocationAttemptCount = i + 1;
            SatelliteSessionController.this.logd("queryCurrentLocation: Attempt " + this.mLocationAttemptCount + "/2");
            cancelOngoingLocationQuery();
            this.mCancellationSignal = new CancellationSignal();
            try {
                SatelliteSessionController.this.mLocationManager.getCurrentLocation("gps", this.mCancellationSignal, SatelliteSessionController.this.mExecutor, new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$SuspensionState$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.onCurrentLocationAvailable((Location) obj);
                    }
                });
                SatelliteSessionController.this.sendMessageDelayed(18, SatelliteSessionController.LOCATION_TIMEOUT_MS);
            } catch (Exception e) {
                SatelliteSessionController.this.loge("queryCurrentLocation: Exception while requesting location: " + e);
                onCurrentLocationAvailable(null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onCurrentLocationAvailable(Location location) {
            cancelOngoingLocationQuery();
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (location != null) {
                satelliteSessionController.logd("queryCurrentLocation: Location fetched successfully: " + location);
                resumeSatelliteMode();
                return;
            }
            satelliteSessionController.loge("queryCurrentLocation: Location fetch failed (result = null), attempt " + this.mLocationAttemptCount);
            queryCurrentLocation();
        }

        private void deferMessages() {
            if (this.mDeferredMessages.isEmpty()) {
                return;
            }
            SatelliteSessionController.this.plogd("Processing " + this.mDeferredMessages.size() + " deferred messages.");
            for (Message message : this.mDeferredMessages) {
                SatelliteSessionController.this.plogd("Re-routing deferred message: " + SatelliteSessionController.this.getWhatToString(message.what));
                SatelliteSessionController.this.deferMessage(message);
            }
            this.mDeferredMessages.clear();
        }

        private void resumeSatelliteMode() {
            if (SatelliteSessionController.this.hasMessages(16)) {
                return;
            }
            Message messageObtainMessage = SatelliteSessionController.this.obtainMessage(16);
            try {
                SatelliteSessionController.this.logd("Calling requestSatelliteSuspended(false)");
                SatelliteSessionController.this.mSatelliteModemInterface.requestSatelliteSuspended(false, messageObtainMessage);
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.sendMessageDelayed(20, satelliteSessionController.mWaitTimeForSatelliteSuspendingResponse);
            } catch (Exception e) {
                SatelliteSessionController.this.loge("SuspensionState: Failed to resume satellite mode: " + e);
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mIdleState);
            }
        }

        private void stopAllTimersInSuspendedState() {
            SatelliteSessionController.this.removeMessages(20);
            SatelliteSessionController.this.removeMessages(19);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting SuspensionState");
            cancelOngoingLocationQuery();
            deferMessages();
            stopAllTimersInSuspendedState();
        }
    }

    private class TransferringState extends State {
        private TransferringState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering TransferringState");
            SatelliteSessionController.this.stopNbIotInactivityTimer();
            SatelliteSessionController.this.stopEsosInactivityTimer();
            SatelliteSessionController.this.stopP2pSmsInactivityTimer();
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 2;
            SatelliteSessionController.this.notifyStateChangedEvent(2);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("TransferringState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                handleEventDatagramTransferStateChanged((DatagramTransferState) message.obj);
                return true;
            }
            if (i == 7) {
                SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
            } else if (i == 3) {
                SatelliteSessionController.this.handleSatelliteEnabledStateChanged(!((Boolean) message.obj).booleanValue(), "TransferringState");
            } else if (i == 4) {
                handleEventSatelliteModemStateChange(message.arg1);
            } else if (i == 9) {
                SatelliteSessionController.this.handleEventScreenStateChanged((AsyncResult) message.obj);
            } else if (i == 10) {
                SatelliteSessionController.this.handleEventScreenOffInactivityTimerTimedOut();
            }
            return true;
        }

        private void handleEventDatagramTransferStateChanged(DatagramTransferState datagramTransferState) {
            if (SatelliteSessionController.this.isSending(datagramTransferState.sendState) || SatelliteSessionController.this.isReceiving(datagramTransferState.receiveState)) {
                return;
            }
            if (SatelliteSessionController.this.mSatelliteController.isSatelliteAttachRequired()) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mConnectedState);
            } else if (datagramTransferState.sendState == 3 || datagramTransferState.receiveState == 7) {
                SatelliteSessionController.this.transitionToSuspensionOrIdle();
            } else {
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mListeningState);
            }
        }

        private void handleEventSatelliteModemStateChange(int i) {
            if (i == 6) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mNotConnectedState);
            } else if (i == 4) {
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mPowerOffState);
            }
        }
    }

    private class ListeningState extends State {
        private ListeningState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering ListeningState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 1;
            SatelliteSessionController.this.sendMessageDelayed(2, updateListeningMode(true));
            SatelliteSessionController.this.mIsSendingTriggeredDuringTransferringState.set(false);
            SatelliteSessionController.this.notifyStateChangedEvent(1);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting ListeningState");
            SatelliteSessionController.this.removeMessages(2);
            updateListeningMode(false);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("ListeningState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                handleEventDatagramTransferStateChanged((DatagramTransferState) message.obj);
            } else if (i == 2) {
                SatelliteSessionController.this.transitionToSuspensionOrIdle();
            } else if (i == 3) {
                SatelliteSessionController.this.handleSatelliteEnabledStateChanged(!((Boolean) message.obj).booleanValue(), "ListeningState");
            } else if (i == 4) {
                handleEventSatelliteModemStateChange(message);
            } else if (i == 7) {
                SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
            } else if (i == 9) {
                SatelliteSessionController.this.handleEventScreenStateChanged((AsyncResult) message.obj);
            } else if (i == 10) {
                SatelliteSessionController.this.handleEventScreenOffInactivityTimerTimedOut();
            }
            return true;
        }

        private long updateListeningMode(boolean z) {
            long j;
            boolean z2 = SatelliteSessionController.this.mIsSendingTriggeredDuringTransferringState.get();
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            if (z2) {
                j = satelliteSessionController.mSatelliteStayAtListeningFromSendingMillis;
            } else {
                j = satelliteSessionController.mSatelliteStayAtListeningFromReceivingMillis;
            }
            SatelliteSessionController.this.mSatelliteModemInterface.requestSatelliteListeningEnabled(z, (int) j, null);
            return j;
        }

        private void handleEventDatagramTransferStateChanged(DatagramTransferState datagramTransferState) {
            if (datagramTransferState.sendState == 1 || datagramTransferState.receiveState == 4) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mTransferringState);
            }
        }

        private void handleEventSatelliteModemStateChange(Message message) {
            if (message.arg1 == 4) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mPowerOffState);
            }
        }
    }

    private class NotConnectedState extends State {
        private NotConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering NotConnectedState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 6;
            SatelliteSessionController.this.notifyStateChangedEvent(6);
            SatelliteSessionController.this.startNbIotInactivityTimer();
            SatelliteSessionController.this.evaluateStartingEsosInactivityTimer();
            SatelliteSessionController.this.evaluateStartingP2pSmsInactivityTimer();
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting NotConnectedState");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("NotConnectedState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i != 1) {
                if (i != 14) {
                    if (i != 3) {
                        if (i != 4) {
                            if (i != 6) {
                                if (i == 7) {
                                    SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
                                } else {
                                    switch (i) {
                                        case 9:
                                            SatelliteSessionController.this.handleEventScreenStateChanged((AsyncResult) message.obj);
                                            break;
                                        case 10:
                                            SatelliteSessionController.this.handleEventScreenOffInactivityTimerTimedOut();
                                            break;
                                        case 11:
                                            boolean zIsP2pSmsInActivityTimerStarted = SatelliteSessionController.this.isP2pSmsInActivityTimerStarted();
                                            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                                            if (zIsP2pSmsInActivityTimerStarted) {
                                                satelliteSessionController.plogd("NotConnectedState: processing: P2P_SMS inactivity timer running can not move to IDLE");
                                            } else {
                                                satelliteSessionController.transitionToSuspensionOrIdle();
                                            }
                                            break;
                                    }
                                }
                            } else {
                                SatelliteSessionController.this.transitionToSuspensionOrIdle();
                            }
                        } else {
                            handleEventSatelliteModemStateChanged(message.arg1);
                        }
                    } else {
                        SatelliteSessionController.this.handleSatelliteEnabledStateChanged(!((Boolean) message.obj).booleanValue(), "NotConnectedState");
                    }
                } else {
                    SatelliteSessionController.this.handleEventP2pSmsInactivityTimerTimedOut();
                }
            } else {
                handleEventDatagramTransferStateChanged((DatagramTransferState) message.obj);
            }
            return true;
        }

        private void handleEventSatelliteModemStateChanged(int i) {
            if (i == 7) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mConnectedState);
            } else if (i == 4) {
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mPowerOffState);
            }
        }

        private void handleEventDatagramTransferStateChanged(DatagramTransferState datagramTransferState) {
            int i;
            int i2 = datagramTransferState.sendState;
            if (i2 == 8 || (i = datagramTransferState.receiveState) == 8) {
                SatelliteSessionController.this.stopNbIotInactivityTimer();
                if (SatelliteSessionController.this.mSatelliteController.getRequestIsEmergency()) {
                    SatelliteSessionController.this.stopEsosInactivityTimer();
                }
                SatelliteSessionController.this.stopP2pSmsInactivityTimer();
                return;
            }
            if (i2 == 0 && i == 0) {
                SatelliteSessionController.this.startNbIotInactivityTimer();
                SatelliteSessionController.this.evaluateStartingEsosInactivityTimer();
                SatelliteSessionController.this.evaluateStartingP2pSmsInactivityTimer();
                return;
            }
            if (SatelliteSessionController.this.isSending(i2) || SatelliteSessionController.this.isReceiving(datagramTransferState.receiveState)) {
                SatelliteSessionController.this.stopNbIotInactivityTimer();
                int i3 = datagramTransferState.datagramType;
                if (i3 == 1) {
                    SatelliteSessionController.this.stopEsosInactivityTimer();
                    return;
                }
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                if (i3 == 6) {
                    satelliteSessionController.stopP2pSmsInactivityTimer();
                    return;
                }
                satelliteSessionController.plogd("datagram type is not SOS_Message and SMS " + i3);
            }
        }
    }

    private class ConnectedState extends State {
        private ConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            SatelliteSessionController.this.plogd("Entering ConnectedState");
            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
            satelliteSessionController.mPreviousState = satelliteSessionController.mCurrentState;
            SatelliteSessionController.this.mCurrentState = 7;
            SatelliteSessionController.this.notifyStateChangedEvent(7);
            SatelliteSessionController.this.startNbIotInactivityTimer();
            SatelliteSessionController.this.evaluateStartingEsosInactivityTimer();
            SatelliteSessionController.this.evaluateStartingP2pSmsInactivityTimer();
            SatelliteSessionController.this.mSatelliteController.updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(SatelliteSessionController.this.mSatelliteController.getSatellitePhone());
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            SatelliteSessionController.this.plogd("Exiting ConnectedState");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            SatelliteSessionController.this.plogd("ConnectedState: processing " + SatelliteSessionController.this.getWhatToString(message.what));
            int i = message.what;
            if (i != 1) {
                if (i != 14) {
                    if (i != 3) {
                        if (i != 4) {
                            if (i != 6) {
                                if (i == 7) {
                                    SatelliteSessionController.this.handleSatelliteEnablementStarted(((Boolean) message.obj).booleanValue());
                                } else {
                                    switch (i) {
                                        case 9:
                                            SatelliteSessionController.this.handleEventScreenStateChanged((AsyncResult) message.obj);
                                            break;
                                        case 10:
                                            SatelliteSessionController.this.handleEventScreenOffInactivityTimerTimedOut();
                                            break;
                                        case 11:
                                            boolean zIsP2pSmsInActivityTimerStarted = SatelliteSessionController.this.isP2pSmsInActivityTimerStarted();
                                            SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                                            if (zIsP2pSmsInActivityTimerStarted) {
                                                satelliteSessionController.plogd("ConnectedState: processing: P2P_SMS inactivity timer running can not move to IDLE");
                                            } else {
                                                satelliteSessionController.transitionToSuspensionOrIdle();
                                            }
                                            break;
                                    }
                                }
                            } else {
                                SatelliteSessionController.this.transitionToSuspensionOrIdle();
                            }
                        } else {
                            handleEventSatelliteModemStateChanged(message.arg1);
                        }
                    } else {
                        SatelliteSessionController.this.handleSatelliteEnabledStateChanged(!((Boolean) message.obj).booleanValue(), "ConnectedState");
                    }
                } else {
                    SatelliteSessionController.this.handleEventP2pSmsInactivityTimerTimedOut();
                }
            } else {
                handleEventDatagramTransferStateChanged((DatagramTransferState) message.obj);
            }
            return true;
        }

        private void handleEventSatelliteModemStateChanged(int i) {
            if (i == 6) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mNotConnectedState);
            } else if (i == 4) {
                SatelliteSessionController satelliteSessionController2 = SatelliteSessionController.this;
                satelliteSessionController2.transitionTo(satelliteSessionController2.mPowerOffState);
            }
        }

        private void handleEventDatagramTransferStateChanged(DatagramTransferState datagramTransferState) {
            if (SatelliteSessionController.this.isSending(datagramTransferState.sendState) || SatelliteSessionController.this.isReceiving(datagramTransferState.receiveState)) {
                SatelliteSessionController satelliteSessionController = SatelliteSessionController.this;
                satelliteSessionController.transitionTo(satelliteSessionController.mTransferringState);
            }
        }
    }

    @Override // com.android.internal.telephony.StateMachine
    protected String getWhatToString(int i) {
        switch (i) {
            case 1:
                return "EVENT_DATAGRAM_TRANSFER_STATE_CHANGED";
            case 2:
                return "EVENT_LISTENING_TIMER_TIMEOUT";
            case 3:
                return "EVENT_SATELLITE_ENABLED_STATE_CHANGED";
            case 4:
                return "EVENT_SATELLITE_MODEM_STATE_CHANGED";
            case 5:
                return "EVENT_DISABLE_CELLULAR_MODEM_WHILE_SATELLITE_MODE_IS_ON_DONE";
            case 6:
                return "EVENT_NB_IOT_INACTIVITY_TIMER_TIMED_OUT";
            case 7:
                return "EVENT_SATELLITE_ENABLEMENT_STARTED";
            case 8:
                return "EVENT_SATELLITE_ENABLEMENT_FAILED";
            case 9:
                return "EVENT_SCREEN_STATE_CHANGED";
            case 10:
                return "EVENT_SCREEN_OFF_INACTIVITY_TIMER_TIMED_OUT";
            case 11:
                return "EVENT_ESOS_INACTIVITY_TIMER_TIMED_OUT";
            case 12:
                return "EVENT_ENABLE_CELLULAR_MODEM_WHILE_SATELLITE_MODE_IS_ON_DONE";
            case 13:
                return "EVENT_SERVICE_STATE_CHANGED";
            case 14:
                return "EVENT_P2P_SMS_INACTIVITY_TIMER_TIMED_OUT";
            case 15:
                return "EVENT_SUSPEND_SATELLITE_MODE_DONE";
            case 16:
                return "EVENT_RESUME_SATELLITE_MODE_DONE";
            case 17:
                return "EVENT_PERIODIC_SUSPENSION_TIMER_EXPIRED";
            case 18:
                return "EVENT_WAIT_FOR_CURRENT_LOCATION_TIMEOUT";
            case 19:
                return "EVENT_WAIT_FOR_SUSPEND_SATELLITE_MODE_RESPONSE_TIMEOUT";
            case 20:
                return "EVENT_WAIT_FOR_RESUME_SATELLITE_MODE_RESPONSE_TIMEOUT";
            default:
                return "UNKNOWN EVENT " + i;
        }
    }

    private void setInitialState(boolean z) {
        if (z) {
            setInitialState(this.mPowerOffState);
        } else {
            setInitialState(this.mUnavailableState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubId() {
        return this.mSatelliteController.getSelectedSatelliteSubId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStateChangedEvent(final int i) {
        this.mDatagramController.onSatelliteModemStateChanged(i);
        final ArrayList arrayList = new ArrayList();
        this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyStateChangedEvent$4(i, arrayList, (ISatelliteModemStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyStateChangedEvent$5((ISatelliteModemStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyStateChangedEvent$4(int i, List list, ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        try {
            iSatelliteModemStateCallback.onSatelliteModemStateChanged(i);
        } catch (RemoteException e) {
            plogd("notifyStateChangedEvent RemoteException: " + e);
            list.add(iSatelliteModemStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyStateChangedEvent$5(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        this.mListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSatelliteEnabledStateChanged(boolean z, String str) {
        if (z) {
            transitionTo(this.mPowerOffState);
            return;
        }
        ploge(str + ": Unexpected satellite radio powered-on state changed event");
    }

    private String getSatelliteGatewayPackageName() {
        if (!TextUtils.isEmpty(this.mSatelliteGatewayServicePackageName)) {
            return this.mSatelliteGatewayServicePackageName;
        }
        return TextUtils.emptyIfNull(this.mContext.getResources().getString(R.string.dream_preview_title));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindService() {
        if (this.mIsBinding.get() || this.mIsBound.get()) {
            return;
        }
        this.mIsBinding.set(true);
        this.mExponentialBackoff.start();
        String satelliteGatewayPackageName = getSatelliteGatewayPackageName();
        if (TextUtils.isEmpty(satelliteGatewayPackageName)) {
            ploge("Unable to bind to the satellite gateway service because the package is undefined.");
            this.mIsBinding.set(false);
            this.mExponentialBackoff.stop();
            return;
        }
        Intent intent = new Intent("android.telephony.satellite.SatelliteGatewayService");
        intent.setPackage(satelliteGatewayPackageName);
        SatelliteGatewayServiceConnection satelliteGatewayServiceConnection = new SatelliteGatewayServiceConnection();
        this.mSatelliteGatewayServiceConnection = satelliteGatewayServiceConnection;
        try {
            if (this.mContext.bindService(intent, satelliteGatewayServiceConnection, 1)) {
                plogd("Successfully bound to the satellite gateway service.");
                return;
            }
            this.mIsBinding.set(false);
            this.mExponentialBackoff.notifyFailed();
            ploge("Error binding to the satellite gateway service. Retrying in " + this.mExponentialBackoff.getCurrentDelay() + " ms.");
        } catch (Exception e) {
            this.mIsBinding.set(false);
            this.mExponentialBackoff.notifyFailed();
            ploge("Exception binding to the satellite gateway service. Retrying in " + this.mExponentialBackoff.getCurrentDelay() + " ms. Exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService() {
        plogd("unbindService");
        this.mExponentialBackoff.stop();
        this.mSatelliteGatewayService = null;
        this.mIsBinding.set(false);
        this.mIsBound.set(false);
        SatelliteGatewayServiceConnection satelliteGatewayServiceConnection = this.mSatelliteGatewayServiceConnection;
        if (satelliteGatewayServiceConnection != null) {
            this.mContext.unbindService(satelliteGatewayServiceConnection);
            this.mSatelliteGatewayServiceConnection = null;
        }
    }

    private class SatelliteGatewayServiceConnection implements ServiceConnection {
        private SatelliteGatewayServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SatelliteSessionController.this.plogd("onServiceConnected: ComponentName=" + componentName);
            SatelliteSessionController.this.mIsBound.set(true);
            SatelliteSessionController.this.mIsBinding.set(false);
            SatelliteSessionController.this.mSatelliteGatewayService = ISatelliteGateway.Stub.asInterface(iBinder);
            SatelliteSessionController.this.mExponentialBackoff.stop();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            SatelliteSessionController.this.ploge("onServiceDisconnected: Waiting for reconnect.");
            SatelliteSessionController.this.mIsBinding.set(false);
            SatelliteSessionController.this.mIsBound.set(false);
            SatelliteSessionController.this.mSatelliteGatewayService = null;
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            SatelliteSessionController.this.ploge("onBindingDied: Unbinding and rebinding service.");
            SatelliteSessionController.this.mIsBound.set(false);
            SatelliteSessionController.this.mIsBinding.set(false);
            SatelliteSessionController.this.unbindService();
            SatelliteSessionController.this.mExponentialBackoff.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSatelliteEnablementStarted(boolean z) {
        if (z) {
            return;
        }
        transitionTo(this.mDisablingState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForScreenStateChanged() {
        DeviceStateMonitor deviceStateMonitor;
        if (!this.mIsRegisteredScreenStateChanged && (deviceStateMonitor = this.mDeviceStateMonitor) != null) {
            deviceStateMonitor.registerForScreenStateChanged(getHandler(), 9, null);
            this.mIsRegisteredScreenStateChanged = true;
            plogd("registerForScreenStateChanged: registered");
        } else {
            StringBuilder sb = new StringBuilder("registerForScreenStateChanged: skip register, mIsRegisteredScreenStateChanged=");
            sb.append(this.mIsRegisteredScreenStateChanged);
            sb.append(", mDeviceStateMonitor=");
            sb.append(this.mDeviceStateMonitor != null);
            plogw(sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForScreenStateChanged() {
        DeviceStateMonitor deviceStateMonitor;
        if (!this.mIsRegisteredScreenStateChanged || (deviceStateMonitor = this.mDeviceStateMonitor) == null) {
            return;
        }
        deviceStateMonitor.unregisterForScreenStateChanged(getHandler());
        removeMessages(9);
        removeMessages(10);
        removeDeferredMessages(9);
        removeDeferredMessages(10);
        this.mIsRegisteredScreenStateChanged = false;
        plogd("unregisterForScreenStateChanged: unregistered");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventScreenStateChanged(AsyncResult asyncResult) {
        if (asyncResult == null) {
            ploge("handleEventScreenStateChanged: asyncResult is null");
            return;
        }
        boolean zBooleanValue = ((Boolean) asyncResult.result).booleanValue();
        if (this.mIsScreenOn == zBooleanValue) {
            plogd("handleEventScreenStateChanged: screen state is not changed");
            return;
        }
        this.mIsScreenOn = zBooleanValue;
        if (!this.mSatelliteController.isInCarrierRoamingNbIotNtn()) {
            logd("handleEventScreenStateChanged: device is not in CarrierRoamingNbIotNtn");
            return;
        }
        if (this.mSatelliteController.getRequestIsEmergency()) {
            logd("handleEventScreenStateChanged: Emergency mode");
            return;
        }
        if (!isP2pSmsSupportedOnCarrierRoamingNtn(getSubId())) {
            plogd("handleEventScreenStateChanged: P2P_SMS is not supported");
            return;
        }
        if (!zBooleanValue) {
            int screenOffInactivityTimeoutDurationSec = getScreenOffInactivityTimeoutDurationSec() * 1000;
            AlarmManager alarmManager = this.mAlarmManager;
            if (alarmManager == null) {
                plogd("handleEventScreenStateChanged: can not access AlarmManager to start timer");
                return;
            }
            alarmManager.setExact(2, SystemClock.elapsedRealtime() + ((long) screenOffInactivityTimeoutDurationSec), "SatelliteSessionController", new HandlerExecutor(getHandler()), new WorkSource(), this.mAlarmListener);
            plogd("handleEventScreenStateChanged: start timer " + screenOffInactivityTimeoutDurationSec);
            return;
        }
        removeMessages(10);
        AlarmManager alarmManager2 = this.mAlarmManager;
        if (alarmManager2 == null) {
            plogd("handleEventScreenStateChanged: can not access AlarmManager to stop timer");
        } else {
            alarmManager2.cancel(this.mAlarmListener);
            plogd("handleEventScreenStateChanged: stop timer");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventP2pSmsInactivityTimerTimedOut() {
        if (isEsosInActivityTimerStarted()) {
            plogd("handleEventP2pSmsInactivityTimerTimedOut: processing: ESOS inactivity timer running can not move to IDLE");
            return;
        }
        if (isTnScanningAllowedDuringSatelliteSession()) {
            plogd("handleEventP2pSmsInactivityTimerTimedOut: Transition to IDLE state");
            transitionToSuspensionOrIdle();
        } else {
            if (this.mSatelliteController.getRequestIsEmergency()) {
                plogd("handleEventP2pSmsInactivityTimerTimedOut: Emergency mode");
                return;
            }
            plogd("handleEventP2pSmsInactivityTimerTimedOut: request disable satellite");
            SatelliteController satelliteController = this.mSatelliteController;
            satelliteController.requestSatelliteEnabled(false, false, satelliteController.getRequestIsEmergency(), new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.2
                public void accept(int i) {
                    SatelliteSessionController.this.plogd("requestSatelliteEnabled result=" + i);
                }
            });
        }
    }

    private int getScreenOffInactivityTimeoutDurationSec() {
        return this.mSatelliteController.getPersistableBundle(getSubId()).getInt("satellite_roaming_screen_off_inactivity_timeout_sec_int", 30);
    }

    private int getEsosInactivityTimeoutDurationSec() {
        return this.mSatelliteController.getPersistableBundle(getSubId()).getInt("satellite_roaming_esos_inactivity_timeout_sec_int", NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ENHANCED_RADIO_CAPABILITY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateStartingEsosInactivityTimer() {
        if (isEsosInActivityTimerStarted()) {
            plogd("isEsosInActivityTimerStarted: ESOS inactivity timer already started");
            return;
        }
        if (!this.mSatelliteController.isSatelliteEsosSupported(getSubId())) {
            plogd("evaluateStartingEsosInactivityTimer: ESOS is not supported");
            return;
        }
        if (!this.mSatelliteController.getRequestIsEmergency()) {
            plogd("evaluateStartingEsosInactivityTimer: request is not emergency");
            return;
        }
        if (this.mIsDeviceAlignedWithSatellite) {
            plogd("evaluateStartingEsosInactivityTimer: can't start ESOS inactivity timer due to device aligned satellite");
            return;
        }
        int esosInactivityTimeoutDurationSec = getEsosInactivityTimeoutDurationSec() * 1000;
        DatagramController datagramController = DatagramController.getInstance();
        if (datagramController.isSendingInIdleState() && datagramController.isPollingInIdleState()) {
            sendMessageDelayed(11, esosInactivityTimeoutDurationSec);
            plogd("evaluateStartingEsosInactivityTimer: start ESOS inactivity timer " + esosInactivityTimeoutDurationSec);
            return;
        }
        plogd("evaluateStartingEsosInactivityTimer: can't start ESOS inactivity timer");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopEsosInactivityTimer() {
        if (isEsosInActivityTimerStarted()) {
            removeMessages(11);
            plogd("stopEsosInactivityTimer: ESOS inactivity timer stopped");
        }
    }

    public boolean isEsosInActivityTimerStarted() {
        return hasMessages(11);
    }

    private int getP2pSmsInactivityTimeoutDurationSec() {
        return this.mSatelliteController.getPersistableBundle(getSubId()).getInt("satellite_roaming_p2p_sms_inactivity_timeout_sec_int", 180);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateStartingP2pSmsInactivityTimer() {
        if (isP2pSmsInActivityTimerStarted()) {
            plogd("isP2pSmsInActivityTimerStarted: P2P_SMS inactivity timer already started");
            return;
        }
        if (!isP2pSmsSupportedOnCarrierRoamingNtn(getSubId())) {
            plogd("evaluateStartingP2pSmsInactivityTimer: P2P_SMS is not supported");
            return;
        }
        if (this.mIsDeviceAlignedWithSatellite) {
            plogd("evaluateStartingP2pSmsInactivityTimer: can't start P2P_SMS inactivity timer due to device aligned satellite");
            return;
        }
        int p2pSmsInactivityTimeoutDurationSec = getP2pSmsInactivityTimeoutDurationSec() * 1000;
        DatagramController datagramController = DatagramController.getInstance();
        if (datagramController.isSendingInIdleState() && datagramController.isPollingInIdleState()) {
            sendMessageDelayed(14, p2pSmsInactivityTimeoutDurationSec);
            plogd("evaluateStartingP2pSmsInactivityTimer: start P2P_SMS inactivity timer " + p2pSmsInactivityTimeoutDurationSec);
            return;
        }
        plogd("evaluateStartingP2pSmsInactivityTimer: can't start P2P_SMS inactivity timer");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopP2pSmsInactivityTimer() {
        if (isP2pSmsInActivityTimerStarted()) {
            removeMessages(14);
            plogd("stopP2pSmsInactivityTimer: P2P_SMS inactivity timer stopped");
        }
    }

    public boolean isP2pSmsInActivityTimerStarted() {
        return hasMessages(14);
    }

    private void checkForInactivity() {
        if (this.mInactivityStartTimestamp == 0 && this.mLastDatagramTransferState.isIdle() && !this.mIsDeviceAlignedWithSatellite) {
            this.mInactivityStartTimestamp = SystemClock.elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endUserInactivity() {
        if (this.mInactivityStartTimestamp != 0) {
            this.mSessionMetricsStats.updateMaxInactivityDurationSec((int) ((SystemClock.elapsedRealtime() - this.mInactivityStartTimestamp) / 1000));
            this.mInactivityStartTimestamp = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventScreenOffInactivityTimerTimedOut() {
        if (this.mSatelliteController.getRequestIsEmergency()) {
            loge("handleEventScreenOffInactivityTimerTimedOut: Emergency mode");
            return;
        }
        plogd("handleEventScreenOffInactivityTimerTimedOut: request disable satellite");
        SatelliteController satelliteController = this.mSatelliteController;
        satelliteController.requestSatelliteEnabled(false, false, satelliteController.getRequestIsEmergency(), new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSessionController.3
            public void accept(int i) {
                SatelliteSessionController.this.plogd("requestSatelliteEnabled result=" + i);
                if (i == 0) {
                    SatelliteSessionController.this.mSessionMetricsStats.addCountOfAutoExitDueToScreenOff();
                }
            }
        });
    }

    private boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true);
    }

    private long getSatelliteStayAtListeningFromSendingMillis() {
        return isDemoMode() ? DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS : this.mContext.getResources().getInteger(R.integer.config_vibrationWaveformRampStepDuration);
    }

    private long getSatelliteStayAtListeningFromReceivingMillis() {
        return isDemoMode() ? DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS : this.mContext.getResources().getInteger(R.integer.config_vibrationWaveformRampDownDuration);
    }

    private long getSatelliteNbIotInactivityTimeoutMillis() {
        int integer;
        boolean zIsDemoMode = isDemoMode();
        Context context = this.mContext;
        if (zIsDemoMode) {
            integer = context.getResources().getInteger(R.integer.config_unfoldTransitionHalfFoldedTimeout);
        } else {
            integer = context.getResources().getInteger(R.integer.config_vibrationPipelineMaxDuration);
        }
        return integer;
    }

    private long getWaitForSatelliteSuspensionResponseTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_out_content_duration);
    }

    private long getSuspensionIntervalMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_vibratorControlServiceDumpAggregationTimeMillisLimit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNbIotInactivityTimer() {
        if (!isSatelliteEnabledForNtnOnlySubscription()) {
            plogd("startNbIotInactivityTimer: Can't start timer because satellite was not enabled for OEM based NB IOT");
            return;
        }
        if (isNbIotInactivityTimerStarted()) {
            plogd("NB IOT inactivity timer is already started");
            return;
        }
        DatagramController datagramController = DatagramController.getInstance();
        if (datagramController.isSendingInIdleState() && datagramController.isPollingInIdleState()) {
            plogd("NB IOT  timer is started " + this.mSatelliteNbIotInactivityTimeoutMillis);
            sendMessageDelayed(6, this.mSatelliteNbIotInactivityTimeoutMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopNbIotInactivityTimer() {
        removeMessages(6);
    }

    private boolean isNbIotInactivityTimerStarted() {
        return hasMessages(6);
    }

    protected boolean isSatelliteEnabledForNtnOnlySubscription() {
        if (SatelliteServiceUtils.getNtnOnlySubscriptionId(this.mContext) == getSubId()) {
            return true;
        }
        plogd("isSatelliteEnabledForOemBasedNbIot: highest priority satellite subscription is not NTN-only subscription");
        return false;
    }

    private boolean isP2pSmsSupportedOnCarrierRoamingNtn(int i) {
        if (!this.mSatelliteController.isSatelliteRoamingP2pSmSSupported(i)) {
            plogd("isP2pSmsSupportedOnCarrierRoamingNtn: P2P_SMS is not supported");
            return false;
        }
        if (!ArrayUtils.contains(this.mSatelliteController.getSupportedServicesOnCarrierRoamingNtn(i), 3)) {
            plogd("isP2pSmsSupportedOnCarrierRoamingNtn: P2P_SMS service is not supported on carrier roaming ntn.");
            return false;
        }
        plogd("isP2pSmsSupportedOnCarrierRoamingNtn: P2_SMS is supported");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isConcurrentTnScanningSupported() {
        if (this.mIsConcurrentTnScanningSupportedForCtsTest != null) {
            plogd("isConcurrentTnScanningSupported: mIsConcurrentTnScanningSupportedForCtsTest=" + this.mIsConcurrentTnScanningSupportedForCtsTest);
            return this.mIsConcurrentTnScanningSupportedForCtsTest.booleanValue();
        }
        try {
            return this.mContext.getResources().getBoolean(R.bool.config_supportTelephonyTimeZoneFallback);
        } catch (RuntimeException e) {
            plogd("isConcurrentTnScanningSupported: ex=" + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTnScanningAllowedDuringSatelliteSession() {
        if (this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest != null) {
            plogd("isTnScanningAllowedDuringSatelliteSession: mIsTnScanningDuringSatelliteSessionAllowedForCtsTest=" + this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest);
            return this.mIsTnScanningDuringSatelliteSessionAllowedForCtsTest.booleanValue();
        }
        try {
            return this.mContext.getResources().getBoolean(R.bool.config_supportSpeakerNearUltrasound);
        } catch (RuntimeException e) {
            plogd("isTnScanningAllowedDuringSatelliteSession: ex=" + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSuspendAllowedDuringSatelliteSession() {
        try {
            return this.mContext.getResources().getBoolean(R.bool.config_supportShortPressPowerWhenDefaultDisplayOn);
        } catch (RuntimeException e) {
            plogd("isSuspendAllowedDuringSatelliteSession: ex=" + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        logd(str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteSessionController", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogw(String str) {
        logw(str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.warn("SatelliteSessionController", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ploge(String str) {
        loge(str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("SatelliteSessionController", str);
        }
    }
}
