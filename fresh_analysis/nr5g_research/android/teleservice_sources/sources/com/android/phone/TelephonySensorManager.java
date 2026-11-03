package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.IUtilityModule;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class TelephonySensorManager {
    private static TelephonySensorManager mInstnace;
    private Context mContext;
    ContentObserver mFeatureEnabledObserver;
    private SensorHandler mHandler;
    private SensorStateManager mSensorStateManager;
    private HandlerThread mWorkThread;
    private boolean VDBG = false;
    private SensorManager mSensorManager = null;
    private boolean mIsAirplaneEnabled = false;
    private boolean mIsSpecialNetworkEnabled = false;
    private boolean mIsAiInfoSupport = false;
    private boolean mIsInsterenSim = false;
    private boolean mIsScreenOn = false;
    private int mLastDriveState = -1;
    private boolean mIsSatelliteEnabled = false;
    private boolean mIsInElevator = false;
    private boolean mIsInSubway = false;
    private HashMap mSensors = new HashMap();
    private HashMap mSensorEventListeners = new HashMap();
    private final ArrayList mSensorStateListeners = new ArrayList();
    private long mSubwayOutTime = -1;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.TelephonySensorManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if ("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action)) {
                TelephonySensorManager.this.mHandler.obtainMessage(1, intent).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                TelephonySensorManager.this.mHandler.obtainMessage(4, intent).sendToTarget();
                return;
            }
            if (SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
                if (TelephonySensorManager.this.mIsSatelliteEnabled != booleanExtra) {
                    TelephonySensorManager.this.mIsSatelliteEnabled = booleanExtra;
                    TelephonySensorManager.this.log("satellite state changed enabled=" + TelephonySensorManager.this.mIsSatelliteEnabled);
                    TelephonySensorManager.this.mHandler.obtainMessage(7).sendToTarget();
                    return;
                }
                return;
            }
            if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                TelephonySensorManager.this.mHandler.obtainMessage(6, intent).sendToTarget();
            } else if ("scene.intent.debugall".equals(action)) {
                TelephonySensorManager.this.VDBG = true;
            }
        }
    };
    private final SensorEventListener mElevatorSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.3
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onElevatorSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mSubwaySensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.4
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onSubwaySensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mBasementSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.5
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onBasementSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mSubWayStartStopSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.6
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mExtentMoveSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.7
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onExtentMoveSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mHighSpeedTrainSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.8
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onHighSpeedTrainSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mHikingSensorListenser = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.9
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mAirportSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.10
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mDriveSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.11
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onDriveSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final SensorEventListener mGripPostureSensorListener = new SensorEventListener() { // from class: com.android.phone.TelephonySensorManager.12
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            TelephonySensorManager.this.log("onGripPostureSensorChanged:" + sensorEvent);
            if (sensorEvent != null) {
                TelephonySensorManager.this.mHandler.obtainMessage(2, sensorEvent).sendToTarget();
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.TelephonySensorManager$$ExternalSyntheticLambda0
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public final void onEvent(BaseEvent baseEvent) {
            this.f$0.lambda$new$0(baseEvent);
        }
    };

    public interface SensorStateListener {
        default void onBasementStateChanged(boolean z) {
        }

        default void onDriveStateChanged(boolean z) {
        }

        default void onElevatorStateChanged(boolean z) {
        }

        default void onHstStateChanged(boolean z) {
        }

        default void onScreenStateChanged(boolean z) {
        }

        void onSubwayStateChanged(boolean z);
    }

    private boolean isValidSensorType(int i) {
        return i == 33171034 || i == 33171116 || i == 33171094 || i == 33171099 || i == 33171117 || i == 33171093 || i == 33171122 || i == 33171091 || i == 33171120;
    }

    public static TelephonySensorManager init(Context context) {
        if (isCtsMode()) {
            return null;
        }
        if (mInstnace == null) {
            mInstnace = new TelephonySensorManager(context);
        }
        return mInstnace;
    }

    public static TelephonySensorManager getInstance() {
        return mInstnace;
    }

    private TelephonySensorManager(Context context) {
        this.mContext = null;
        this.mWorkThread = null;
        this.mHandler = null;
        this.mFeatureEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.TelephonySensorManager.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                TelephonySensorManager.this.mHandler.obtainMessage(3).sendToTarget();
            }
        };
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("TelephonySensorManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SensorHandler sensorHandler = new SensorHandler(this.mWorkThread.getLooper());
        this.mHandler = sensorHandler;
        sensorHandler.obtainMessage(0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        initSensorSettingValue();
        initSensor();
        registerIntent();
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener, this.mHandler);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("special_network_optimization"), false, this.mFeatureEnabledObserver);
        this.mIsAirplaneEnabled = isAirPlaneModeOn();
        this.mIsSpecialNetworkEnabled = MiuiPhoneUtils.isSpecialNetworkModeEnabled(this.mContext);
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        this.mIsAiInfoSupport = MiuiPhoneUtils.isAiInfoSupported();
        registerSubwaySensor();
        registerHikingSensor();
        registerAirportSensor();
        registerBasementSensor();
        registerDriveSensor();
        registerGripPostureSensor();
        this.mSensorStateManager = SensorStateManager.getInstance(this.mContext);
        log("TelephonySensorManager init");
    }

    private void initSensor() {
        Sensor defaultSensor = this.mSensorManager.getDefaultSensor(33171034, true);
        log("elevatorSensor = " + defaultSensor);
        if (defaultSensor != null) {
            this.mSensors.put(33171034, defaultSensor);
        }
        Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(33171116, false);
        log("subwayStartStopSensor = " + defaultSensor2);
        if (defaultSensor2 != null) {
            this.mSensors.put(33171116, defaultSensor2);
        }
        Sensor defaultSensor3 = this.mSensorManager.getDefaultSensor(33171093, true);
        log("subwaySensor = " + defaultSensor3);
        if (defaultSensor3 != null) {
            this.mSensors.put(33171093, defaultSensor3);
        }
        Sensor defaultSensor4 = this.mSensorManager.getDefaultSensor(33171122, false);
        log("basementSensor = " + defaultSensor4);
        if (defaultSensor4 != null) {
            this.mSensors.put(33171122, defaultSensor4);
        }
        Sensor defaultSensor5 = this.mSensorManager.getDefaultSensor(33171094, true);
        log("extentMoveSensor = " + defaultSensor5);
        if (defaultSensor5 != null) {
            this.mSensors.put(33171094, defaultSensor5);
            putSettingValue("sensor_state_extent_move", 0);
        } else {
            putSettingValue("sensor_state_extent_move", -1);
        }
        log("highSpeedTrainSensor = null");
        putSettingValue("sensor_state_highspeedtrain", -1);
        Sensor defaultSensor6 = this.mSensorManager.getDefaultSensor(33171099, true);
        log("hikingSensor = " + defaultSensor6);
        if (defaultSensor6 != null) {
            this.mSensors.put(33171099, defaultSensor6);
        }
        Sensor defaultSensor7 = this.mSensorManager.getDefaultSensor(33171117, true);
        log("airplaneSensor = " + defaultSensor7);
        if (defaultSensor7 != null) {
            this.mSensors.put(33171117, defaultSensor7);
        }
        Sensor defaultSensor8 = this.mSensorManager.getDefaultSensor(33171091, true);
        log("driveSensor = " + defaultSensor8);
        if (defaultSensor8 != null) {
            this.mSensors.put(33171091, defaultSensor8);
        }
        Sensor defaultSensor9 = this.mSensorManager.getDefaultSensor(33171120, false);
        log("gripPostureSensor = " + defaultSensor9);
        if (defaultSensor9 != null) {
            this.mSensors.put(33171120, defaultSensor9);
        }
    }

    private void registerIntent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        intentFilter.addAction("scene.intent.debugall");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class SensorHandler extends Handler {
        public SensorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            TelephonySensorManager.this.log("msg.what = " + message.what);
            switch (message.what) {
                case 0:
                    TelephonySensorManager.this.initAction();
                    break;
                case 1:
                    TelephonySensorManager.this.onSimStateChanged((Intent) message.obj);
                    break;
                case 2:
                    TelephonySensorManager.this.onSensorEventChanged((SensorEvent) message.obj);
                    break;
                case 3:
                    TelephonySensorManager.this.onFeatureEnabledChanged();
                    break;
                case 4:
                    TelephonySensorManager.this.onAirplaneModeChanged();
                    break;
                case 5:
                    TelephonySensorManager.this.onScreenStateChanged();
                    break;
                case 6:
                    Intent intent = (Intent) message.obj;
                    if (intent != null) {
                        TelephonySensorManager.this.notifyHstStateChanged(intent.getBooleanExtra("mIsHstMode", false));
                    }
                    break;
                case 7:
                    TelephonySensorManager.this.onSatelliteStateChanged();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        this.mIsInsterenSim = MiuiPhoneUtils.getInsertedSimCount() > 0;
        registerElevatorSensor();
        registerBasementSensor();
        registerSubwaySensor();
        registerSpecialNewrokSensor();
        registerHighSpeedTrainSensor();
        registerHikingSensor();
        registerAirportSensor();
        registerDriveSensor();
        registerGripPostureSensor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAirplaneModeChanged() {
        this.mIsAirplaneEnabled = isAirPlaneModeOn();
        registerElevatorSensor();
        registerBasementSensor();
        registerSpecialNewrokSensor();
        registerHighSpeedTrainSensor();
        registerAirportSensor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFeatureEnabledChanged() {
        this.mIsSpecialNetworkEnabled = MiuiPhoneUtils.isSpecialNetworkModeEnabled(this.mContext);
        registerSpecialNewrokSensor();
        registerElevatorSensor();
        registerBasementSensor();
        registerSubwaySensor();
        registerHighSpeedTrainSensor();
        registerHikingSensor();
        registerAirportSensor();
        registerDriveSensor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenStateChanged() {
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        registerElevatorSensor();
        registerBasementSensor();
        registerSpecialNewrokSensor();
        registerHighSpeedTrainSensor();
        registerAirportSensor();
        registerDriveSensor();
        notifyScreenStateChanged(this.mIsScreenOn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSatelliteStateChanged() {
        registerSpecialNewrokSensor();
        registerElevatorSensor();
        registerBasementSensor();
        registerSubwaySensor();
        registerHighSpeedTrainSensor();
        registerHikingSensor();
        registerAirportSensor();
        registerDriveSensor();
        registerGripPostureSensor();
    }

    private synchronized void initSensorSettingValue() {
        try {
            if (getSensorSubwayState() != -1) {
                putSettingValue("sensor_state_subway", -1);
            }
            if (getSensorAirplaneState() != 2) {
                putSettingValue("sensor_airplane_state", 2);
            }
            if (getSensorDrivingState() != 0) {
                putSettingValue("sensor_drive_state", 0);
            }
            if (getSensorGripPostureState() != 0) {
                putSettingValue("sensor_grip_posture_state", 0);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean isNecessaryCase() {
        return this.mIsInsterenSim && !this.mIsAirplaneEnabled && this.mIsScreenOn && this.mIsSpecialNetworkEnabled && !this.mIsSatelliteEnabled;
    }

    private boolean isNecessaryCaseForSubway() {
        return this.mIsInsterenSim && this.mIsSpecialNetworkEnabled && !this.mIsSatelliteEnabled;
    }

    private boolean isNecessaryCaseForHiking() {
        return this.mIsInsterenSim && this.mIsSpecialNetworkEnabled && !this.mIsSatelliteEnabled;
    }

    private boolean isNecessaryCaseForAirportMode() {
        if (this.mIsInsterenSim) {
            return (this.mIsAirplaneEnabled || this.mIsScreenOn) && this.mIsSpecialNetworkEnabled && !this.mIsSatelliteEnabled;
        }
        return false;
    }

    private boolean isNecessaryCaseForDriveMode() {
        return this.mIsInsterenSim && this.mIsSpecialNetworkEnabled && !this.mIsSatelliteEnabled && this.mIsScreenOn;
    }

    private boolean isNecessaryCaseForGripPosture() {
        return this.mIsInsterenSim && !this.mIsSatelliteEnabled && ModemProvider.IS_QCOM;
    }

    private void registerElevatorSensor() {
        if (isNecessaryCase() && !isCloudElevatorOff()) {
            registerSensor(33171034);
        } else {
            unRegisterSensor(33171034);
        }
    }

    private void registerBasementSensor() {
        if (MiuiPhoneUtils.isSupportBasementOptimization() && ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportBasementByCloud()) {
            if (isNecessaryCase()) {
                registerSensor(33171122);
            } else {
                unRegisterSensor(33171122);
            }
        }
    }

    private void registerSubwaySensor() {
        if (isNecessaryCaseForSubway() && MiuiPhoneUtils.isSubwayOptimizationSupported()) {
            registerSensor(33171093);
        } else {
            unRegisterSensor(33171093);
            unRegisterSensor(33171116);
        }
    }

    private void registerHighSpeedTrainSensor() {
        if (isNecessaryCase() && MiuiPhoneUtils.isHighSpeedTrainSensorSupported()) {
            registerSensor(33171107);
        } else {
            unRegisterSensor(33171107);
        }
    }

    private void registerSpecialNewrokSensor() {
        boolean z = isNecessaryCase() && !isCloudSpecialNetworkOff();
        log("insterenSim: " + this.mIsInsterenSim + ", airplaneMode: " + this.mIsAirplaneEnabled + ", screenOn: " + this.mIsScreenOn + ", enabled: " + this.mIsSpecialNetworkEnabled + ", cloudDisabled: " + isCloudSpecialNetworkOff());
        if (z) {
            registerSensor(33171094);
        } else {
            unRegisterSensor(33171094);
        }
    }

    private void registerHikingSensor() {
        if (isNecessaryCaseForHiking() && MiuiPhoneUtils.isHikingOptimizationSupported()) {
            registerSensor(33171099);
        } else {
            unRegisterSensor(33171099);
        }
    }

    private void registerAirportSensor() {
        if ((MiuiPhoneUtils.isAirportOptimizationSupported() || ModemProvider.IS_XRING) && isNecessaryCaseForAirportMode()) {
            registerSensor(33171117);
        } else {
            unRegisterSensor(33171117);
        }
    }

    private void registerDriveSensor() {
        if (MiuiPhoneUtils.isDriveOptimizationSupported() && isNecessaryCaseForDriveMode()) {
            registerSensor(33171091);
        } else {
            unRegisterSensor(33171091);
        }
    }

    private void registerGripPostureSensor() {
        if (MiuiPhoneUtils.isSupportSixRX() && isNecessaryCaseForGripPosture()) {
            registerSensor(33171120);
        } else {
            unRegisterSensor(33171120);
        }
    }

    private void putSettingValue(String str, int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSensorEventChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        log("onSensorEventChanged:" + type);
        if (isValidSensorType(type)) {
            if (type == 33171034) {
                if (isBlockedBySubwayOrHstForOptimization()) {
                    return;
                }
                int i = (int) sensorEvent.values[0];
                if (i == 2) {
                    shouldToastedSensorState("sensor_state_elevator", 2, "ELEVATOR_IN", 1);
                } else if (ModemProvider.IS_QCOM && i == 1) {
                    shouldToastedSensorState("sensor_state_elevator", 1, "ELEVATOR_OUT", 2);
                } else if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && i == 4) {
                    shouldToastedSensorState("sensor_state_elevator", 4, "ELEVATOR_OUT", 2);
                }
                putSettingValue("sensor_state_elevator", i);
                ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).onOutElevatorOrBasementEvent(sensorEvent);
                elevatorStateNotification(sensorEvent);
                return;
            }
            processSensorState(type, sensorEvent);
        }
    }

    private boolean isBlockedBySubwayOrHstForOptimization() {
        if (!MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            return false;
        }
        if (this.mIsInSubway || (this.mSubwayOutTime != -1 && SystemClock.elapsedRealtime() - this.mSubwayOutTime <= 1800000)) {
            log("ElevatorModeController onSensorEventChanged : in subway state");
            return true;
        }
        if (!((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHighSpeedTrain()) {
            return false;
        }
        log("ElevatorModeController onSensorEventChanged : in hst state!");
        return true;
    }

    private void elevatorStateNotification(SensorEvent sensorEvent) {
        int i;
        try {
            i = (int) sensorEvent.values[0];
        } catch (Exception e) {
            log("ElevatorStateNotification Exception:" + e);
            i = Integer.MIN_VALUE;
        }
        log("ElevatorStateNotification:" + i);
        this.mIsInElevator = i == 2;
        Intent intent = new Intent("com.android.phone.intent.action.ELEVATOR_STATE_SCENE");
        intent.putExtra("elevatorState", i);
        this.mContext.sendBroadcast(intent);
        notifyElevatorStateChanged(this.mIsInElevator);
        this.mSensorStateManager.setElevatorState(this.mIsInElevator);
    }

    public boolean isInElevator() {
        return this.mIsInElevator;
    }

    private void processSensorState(int i, SensorEvent sensorEvent) {
        try {
            int i2 = (int) sensorEvent.values[0];
            log("processSensorState:" + i2);
            switch (i) {
                case 33171091:
                    if (i2 == 10) {
                        shouldToastedSensorState("sensor_drive_state", 10, "DRIVVING_IN", 12);
                        notifyDriveStateChanged(true);
                    } else if (i2 == 5) {
                        shouldToastedSensorState("sensor_drive_state", 5, "DRIVVING_OUT", 13);
                        notifyDriveStateChanged(false);
                    } else if (i2 == 0) {
                        shouldToastedSensorState("sensor_drive_state", 0, "DRIVVING_NO", 14);
                        notifyDriveStateChanged(false);
                    }
                    if (this.mLastDriveState != i2) {
                        putSettingValue("sensor_drive_state", i2);
                        this.mLastDriveState = i2;
                    }
                    break;
                case 33171093:
                    if (i2 == 1) {
                        shouldToastedSensorState("sensor_state_subway", 1, "SUBWAY_IN", 9);
                        notifySubwayStateChanged(true);
                        this.mIsInSubway = true;
                        this.mSensorStateManager.setSubwayState(true);
                    } else if (i2 == -1) {
                        shouldToastedSensorState("sensor_state_subway", -1, "SUBWAY_OUT", 10);
                        notifySubwayStateChanged(false);
                        this.mIsInSubway = false;
                        this.mSubwayOutTime = SystemClock.elapsedRealtime();
                        this.mSensorStateManager.setSubwayState(false);
                    }
                    putSettingValue("sensor_state_subway", i2);
                    if (i2 == 1) {
                        registerSensor(33171116);
                    } else if (i2 == -1) {
                        unRegisterSensor(33171116);
                    }
                    break;
                case 33171094:
                    if (i2 == 2 || i2 == 4) {
                        putSettingValue("sensor_state_extent_move", 2);
                        this.mSensorStateManager.setMovementState(false);
                    } else {
                        putSettingValue("sensor_state_extent_move", 1);
                        this.mSensorStateManager.setMovementState(true);
                    }
                    putSettingValue("extent_move_time", (int) sensorEvent.values[1]);
                    break;
                case 33171099:
                    if (i2 == 1) {
                        shouldToastedSensorState("sensor_state_hiking", 1, "HIKING_IN", 5);
                    } else if (i2 == 2) {
                        shouldToastedSensorState("sensor_state_hiking", 2, "HIKING_OUT", 6);
                    }
                    int i3 = (int) sensorEvent.values[1];
                    log("processSensorState:" + i2 + "," + i3);
                    putSettingValue("sensor_state_hiking", i2);
                    putSettingValue("sensor_state_hiking_height", i3);
                    break;
                case 33171107:
                    int i4 = (int) sensorEvent.values[1];
                    log("processSensorState:" + i2 + "," + i4);
                    if (i2 == 1 || i4 == 1) {
                        putSettingValue("sensor_state_highspeedtrain", 1);
                    } else {
                        putSettingValue("sensor_state_highspeedtrain", 0);
                    }
                    break;
                case 33171116:
                    if (i2 == 1) {
                        shouldToastedSensorState("sensor_state_subway_start_stop", 1, "SUBWAY_START", 3);
                        this.mSensorStateManager.setSubwayRunningState(true);
                    } else if (i2 == 2) {
                        shouldToastedSensorState("sensor_state_subway_start_stop", 2, "SUBWAY_STOP", 4);
                        this.mSensorStateManager.setSubwayRunningState(false);
                    }
                    putSettingValue("sensor_state_subway_start_stop", i2);
                    break;
                case 33171117:
                    if (i2 == 1) {
                        shouldToastedSensorState("sensor_airplane_state", 1, "AIRPLANE_FLY", 7);
                        this.mSensorStateManager.setAirportState(false);
                    } else if (i2 == 2) {
                        shouldToastedSensorState("sensor_airplane_state", 2, "AIRPLANE_LAND", 8);
                        this.mSensorStateManager.setAirportState(true);
                    }
                    putSettingValue("sensor_airplane_state", (int) sensorEvent.values[0]);
                    break;
                case 33171120:
                    putSettingValue("sensor_grip_posture_state", i2);
                    break;
                case 33171122:
                    if (i2 == -1) {
                        shouldToastedSensorState("sensor_state_basement", -1, "BASEMENT_OUT", 11);
                        this.mSensorStateManager.setBasementState(false);
                    }
                    putSettingValue("sensor_state_basement", i2);
                    if (i2 == -1) {
                        notifyBasementStateChanged(true);
                        if (!isBlockedBySubwayOrHstForOptimization()) {
                            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).onOutElevatorOrBasementEvent(sensorEvent);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            log("onSensorEventChanged Exception:" + e);
        }
    }

    private void shouldToastedSensorState(String str, int i, String str2, int i2) {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), str, 0) != i) {
            localLog("scene", str2);
            if (this.mIsAiInfoSupport) {
                Toast.makeText(this.mContext, str2, 1).show();
                userSceneStateNotification(i2);
            }
        }
    }

    private void userSceneStateNotification(int i) {
        log("userSceneStateNotification:" + i);
        Intent intent = new Intent("com.android.phone.intent.action.USER_SCENE_CHANGE");
        intent.putExtra("userSceneState", i);
        this.mContext.sendBroadcast(intent);
    }

    private static boolean isCtsMode() {
        return !SystemProperties.getBoolean("persist.sys.miui_optimization", !"1".equals(SystemProperties.get("ro.miui.cts")));
    }

    private synchronized void unRegisterSensor(int i) {
        try {
            if (this.mSensorManager != null && isValidSensorType(i)) {
                Sensor sensor = (Sensor) this.mSensors.get(Integer.valueOf(i));
                SensorEventListener sensorEventListener = (SensorEventListener) this.mSensorEventListeners.get(Integer.valueOf(i));
                log("unRegisterSensor:" + i + ", listener:" + sensorEventListener);
                if (sensor != null && sensorEventListener != null) {
                    this.mSensorManager.unregisterListener(sensorEventListener);
                    this.mSensorEventListeners.remove(Integer.valueOf(i));
                    switch (i) {
                        case 33171034:
                            putSettingValue("sensor_state_elevator", 0);
                            break;
                        case 33171091:
                            notifyDriveStateChanged(false);
                            this.mLastDriveState = -1;
                            putSettingValue("sensor_drive_state", 0);
                            break;
                        case 33171093:
                            notifySubwayStateChanged(false);
                            this.mIsInSubway = false;
                            putSettingValue("sensor_state_subway", 0);
                            break;
                        case 33171094:
                            putSettingValue("sensor_state_extent_move", 0);
                            break;
                        case 33171099:
                            putSettingValue("sensor_state_hiking", 0);
                            break;
                        case 33171107:
                            putSettingValue("sensor_state_highspeedtrain", 0);
                            break;
                        case 33171116:
                            putSettingValue("sensor_state_subway_start_stop", 0);
                            break;
                        case 33171117:
                            putSettingValue("sensor_airplane_state", 0);
                            break;
                        case 33171120:
                            putSettingValue("sensor_grip_posture_state", 0);
                            break;
                        case 33171122:
                            putSettingValue("sensor_state_basement", 0);
                            break;
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void registerSensor(int i) {
        SensorEventListener sensorEventListener;
        try {
            if (this.mSensorManager != null && isValidSensorType(i)) {
                Sensor sensor = (Sensor) this.mSensors.get(Integer.valueOf(i));
                if (sensor != null && this.mSensorEventListeners.get(Integer.valueOf(i)) == null) {
                    switch (i) {
                        case 33171034:
                            sensorEventListener = this.mElevatorSensorListener;
                            break;
                        case 33171091:
                            sensorEventListener = this.mDriveSensorListener;
                            break;
                        case 33171093:
                            sensorEventListener = this.mSubwaySensorListener;
                            break;
                        case 33171094:
                            sensorEventListener = this.mExtentMoveSensorListener;
                            break;
                        case 33171099:
                            sensorEventListener = this.mHikingSensorListenser;
                            break;
                        case 33171107:
                            sensorEventListener = this.mHighSpeedTrainSensorListener;
                            break;
                        case 33171116:
                            sensorEventListener = this.mSubWayStartStopSensorListener;
                            break;
                        case 33171117:
                            sensorEventListener = this.mAirportSensorListener;
                            break;
                        case 33171120:
                            sensorEventListener = this.mGripPostureSensorListener;
                            break;
                        case 33171122:
                            sensorEventListener = this.mBasementSensorListener;
                            break;
                        default:
                            sensorEventListener = null;
                            break;
                    }
                    this.mSensorEventListeners.put(Integer.valueOf(i), sensorEventListener);
                    log("registerSensor:" + i + ", sensorFlag:" + this.mSensorManager.registerListener(sensorEventListener, sensor, 3));
                }
                log("already registered:" + i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void updateCloudSensorState() {
        registerElevatorSensor();
        registerSpecialNewrokSensor();
    }

    private boolean isCloudElevatorOff() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        return cloudData != null && cloudData.getInt("elevator_status") == 0;
    }

    private boolean isCloudSpecialNetworkOff() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        return cloudData != null && cloudData.getInt("special_network_status") == 0;
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if ((baseEvent instanceof DeviceStateEvent.DisplayEvent) && ((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
            this.mHandler.obtainMessage(5).sendToTarget();
        }
    }

    public void registerSensorStateListener(SensorStateListener sensorStateListener) {
        if (sensorStateListener == null) {
            return;
        }
        synchronized (this) {
            try {
                if (findSensorStateListener(sensorStateListener) < 0) {
                    this.mSensorStateListeners.add(sensorStateListener);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void unregisterSensorStateListener(SensorStateListener sensorStateListener) {
        if (sensorStateListener == null) {
            return;
        }
        synchronized (this) {
            try {
                int iFindSensorStateListener = findSensorStateListener(sensorStateListener);
                if (iFindSensorStateListener >= 0) {
                    this.mSensorStateListeners.remove(iFindSensorStateListener);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int findSensorStateListener(SensorStateListener sensorStateListener) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            if (this.mSensorStateListeners.get(i) == sensorStateListener) {
                return i;
            }
        }
        return -1;
    }

    private void notifyScreenStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onScreenStateChanged(z);
        }
    }

    private void notifySubwayStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onSubwayStateChanged(z);
        }
    }

    private void notifyDriveStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onDriveStateChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyHstStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onHstStateChanged(z);
        }
    }

    private void notifyBasementStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onBasementStateChanged(z);
        }
    }

    private void notifyElevatorStateChanged(boolean z) {
        int size = this.mSensorStateListeners.size();
        for (int i = 0; i < size; i++) {
            ((SensorStateListener) this.mSensorStateListeners.get(i)).onElevatorStateChanged(z);
        }
    }

    private int getSensorSubwayState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_subway", 0);
    }

    private int getSensorAirplaneState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_airplane_state", 0);
    }

    private int getSensorDrivingState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_drive_state", 0);
    }

    public int getSensorGripPostureState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_grip_posture_state", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("TelephonySensorManager", str);
    }

    private void localLog(String str, String str2) {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).localLog("TelephonySensorManager", str, str2);
    }
}
