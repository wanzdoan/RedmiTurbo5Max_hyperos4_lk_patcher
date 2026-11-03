package com.xiaomi.internal.telephony;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.nl80211.WifiNl80211Manager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UEventObserver;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SarDeviceStateListener extends DeviceStateListenerBase {
    public static final int AUDIO_RECEIVER_STATE_OFF = 0;
    public static final int AUDIO_RECEIVER_STATE_ON = 1;
    private static final String BC_INTENT_ACTION_CLOUD_CONTROL = "com.android.phone.intent.action.ME_CLOUD_CONTROL";
    private static final String BC_INTENT_ACTION_DEVICE_STATE_CHANED = "xiaomi.intent.action.DEVICE_STATE";
    private static final String BC_INTENT_ACTION_HST_ENHANCEMENT_CHANGED = "com.android.phone.intent.action.HST_TUNNEL_SMARTLINK";
    private static final String BC_INTENT_ACTION_MODEM_SAR_STATE_CHANGE = "com.android.phone.modemsar.state";
    private static final String BC_INTENT_ACTION_RECEIVER_STATE_CHANGED = "com.android.phone.intent.action.receiver_state_changed";
    public static final String BC_INTENT_ACTION_SATELLITE_STATE_CHANGE = "com.android.app.action.SATELLITE_STATE_CHANGE";
    private static final String BC_INTENT_ACTION_SMART_TX_CHANGED = "com.android.phone.intent.action.SMART_TX_STATE";
    private static final String BC_INTENT_ACTION_WIFI_COUNTRY_CODE_CHANGE = "android.net.wifi.COUNTRY_CODE_CHANGED";
    private static final String BC_INTENT_EXTRA_AIRPLANE_STATE = "state";
    private static final String BC_INTENT_EXTRA_CONTROL_CODE = "CONTROL_CODE";
    private static final String BC_INTENT_EXTRA_DEVICE_STATE_TYPE = "BC_PARAMS_DEVICE_STATE_TYPE";
    private static final String BC_INTENT_EXTRA_DEVICE_STATE_VAL = "BC_PARAMS_DEVICE_STATE_VAL";
    private static final String BC_INTENT_EXTRA_FEATURE_TYPE = "FEATURE_TYPE";
    private static final String BC_INTENT_EXTRA_HST_ENHANCEMENT_STATE = "value";
    private static final String BC_INTENT_EXTRA_MODEM_SAR_STATE = "modem_sar_state";
    private static final String BC_INTENT_EXTRA_RECEIVER_STATE = "state";
    public static final String BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE = "is_enable";
    public static final String BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID = "phone_id";
    private static final String BC_INTENT_EXTRA_SMART_TX_STATE = "smart_tx_state";
    public static final int BLUETOOTH_PAN_STATE_OFF = 0;
    public static final int BLUETOOTH_PAN_STATE_ON = 1;
    public static final int BLUETOOTH_STATE_OFF = 0;
    public static final int BLUETOOTH_STATE_ON = 1;
    private static final int CLOUD_CONTROL_DELAY_TIME = 10000;
    public static final int CLOUD_CONTROL_DISABLE = 0;
    public static final int CLOUD_CONTROL_ENABLE = 1;
    private static final int CLOUD_CONTROL_FEATURE_TYPE_END = 1100;
    private static final int CLOUD_CONTROL_FEATURE_TYPE_HORIZONTAL_GAME_LIST = 1047;
    private static final int CLOUD_CONTROL_FEATURE_TYPE_SCAN_CODE = 1036;
    private static final int CLOUD_CONTROL_FEATURE_TYPE_START = 1000;
    private static final String DEVICE_CABLE_FILE = "/sys/bus/platform/devices/soc/soc:md_testing_mode";
    private static final int DEVICE_STATE_LISTENER_STATE_IDLE = 0;
    private static final int DEVICE_STATE_LISTENER_STATE_RUNING = 1;
    public static final int EVENT_DEFAULT_STATE = -1;
    public static final int GPD_BIG_MOVE_STATE = -2;
    public static final int GPD_STATIC_STATE = -1;
    public static final int GRAVITY_Y_AXES_HORIZONTAL_STATE = 0;
    public static final int GRAVITY_Y_AXES_VERTICAL_STATE = 1;
    public static final float GRAVITY_Y_AXES_VERTICAL_THRESHOLD = -9.5f;
    public static final int HALL_ABNORMAL_VALUE = 0;
    public static final int HALL_STATE_FOLD = 2;
    public static final int HALL_STATE_UNFOLD = 1;
    public static final int HOTSPOT_STATE_OFF = 0;
    public static final int HOTSPOT_STATE_ON = 1;
    private static final int HST_ENHANCEMENT_DISABLE = 0;
    private static final int HST_ENHANCEMENT_ENABLE = 1;
    private static final String LOG_TAG = "RadDeviceStateListener";
    public static final int MCC_CE = 1;
    public static final int MCC_DEFAULT = 3;
    public static final int MCC_FCC = 2;
    public static final int MODEM_STATE_ACTIVE = 1;
    public static final int MODEM_STATE_INACTIVE = 0;
    private static final String PROPERTY_CLOUD_CONTROL_PREFIX = "persist.radio.sar.fr";
    private static final String PROPERTY_DEVICE_STATE_PREFIX = "debug.device.";
    private static final String PROPERTY_OPERATOR_ISO_COUNTRY = "gsm.operator.iso-country";
    private static final String PROPERTY_RECEIVER_STATUS = "vendor.audio.voice.receiver.status";
    private static final String PROPERTY_USECASE_TEST = "persist.vendor.radio.usetest";
    public static final String PROP_AUDIO_RECEIVER_STATE_OFF = "off";
    public static final String PROP_AUDIO_RECEIVER_STATE_ON = "on";
    public static final int SAR_DISTANCE_LONG = 0;
    public static final int SAR_DISTANCE_MID = 2;
    public static final int SAR_DISTANCE_SHORT = 1;
    public static final int SATELLITE_SWITCH_STATE_OFF = 0;
    public static final int SATELLITE_SWITCH_STATE_ON = 1;
    public static final int SCREEN_OFF = 0;
    public static final int SCREEN_ON = 1;
    private static final int SENSOR_TYPE_GPDSENSOR = 33171120;
    private static final int SENSOR_TYPE_GRAVITYSENSOR = 9;
    private static final int SENSOR_TYPE_HALLSENSOR = 33171087;
    private static final int SENSOR_TYPE_HALLSENSOR2 = 33171068;
    private static final int SENSOR_TYPE_SARSENSOR = 33171028;
    private static final int SENSOR_TYPE_SARSENSOR2 = 33171059;
    private static final int SENSOR_TYPE_SARSENSOR3 = 33171109;
    private static final int SENSOR_TYPE_SARSENSOR_SUBSTANCE1 = 33171015;
    public static final String SIM_MCC_DEFAULT_STATE = ",";
    public static final int SIM_STATE_ABSENT = 0;
    public static final int SIM_STATE_LOADED = 1;
    public static final int TYPE_CLOUD_CONTROL = 100;
    public static final int TYPE_SAVE_AND_BROADCAST_STATE = 200;
    public static final int USB_STATE_CONNECTED = 1;
    public static final int USB_STATE_DISCONNECTED = 0;
    public static final int WIFI_STATE_CONNECTED = 1;
    public static final int WIFI_STATE_DISCONNECTED = 0;
    private static AudioManager mAudioManager;
    private static boolean mCallStateActive;
    private static boolean mDataStateActive;
    private static HandlerThread mDeviceStateHandlerThread;
    private static Sensor mGPDSensor;
    private static Sensor mGravitySensor;
    private static Sensor mHallSensor;
    private static Sensor mHallSensor2;
    private static SarCountryCodeChangeListener mSarCountryCodeChangeListener;
    private static Sensor mSarSensor1;
    private static Sensor mSarSensor2;
    private static Sensor mSarSensor3;
    private static Sensor mSarSensorSubstance1;
    private static SensorManager mSensorManager;
    private static SubscriptionManager mSubscriptionManager;
    private static TelephonyManager mTelephonyManager;
    private static WifiNl80211Manager mWifiCondManager;
    private static WifiManager mWifiManager;
    private static final List<String> mSarSensorSubstance1Group = new ArrayList(Arrays.asList("rodin"));
    private static final String[] FCCList = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar"};
    private static final List<String> mFCCList = Arrays.asList(FCCList);
    private static final String[] CEList = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "sa", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "th", "ph", "kh", "pk", "my", "au", "sg", "hk", "tw", "bd", "np", "lk", "jp"};
    private static final List<String> mCEList = Arrays.asList(CEList);
    private static Map<Integer, Integer> mListenerStateMap = new HashMap();
    private static Context mContext = null;
    private static SarDeviceStateListener mInstance = null;
    private static Map<Integer, RegistrantList> mRegRequestInfoMap = new HashMap();
    private static int mCurrentWifiState = -1;
    private static int mCurrentHotspotState = -1;
    private static int mCurrentModemState = -1;
    private static int mCurrentSarSensorState = -1;
    private static int mCurrentSarSensor2State = -1;
    private static int mCurrentSarSensor3State = -1;
    private static int mCableGpioStatus = -1;
    private static int mCurrentHallSensorState = -1;
    private static int mCurrentHallSensor2State = -1;
    private static int mCurrentAudioReceiverState = -1;
    private static int mCurrentMccState = -1;
    private static int mCurrentBluetoothPanState = -1;
    private static int mCurrentBluetoothState = -1;
    private static int mCurrentAirplaneState = -1;
    private static int mModemSarEnable = -1;
    private static int mCurrentUseCaseTest = -1;
    private static int mCurrentUsbState = -1;
    private static int mCurrentSmartTxState = -1;
    private static int mCurrentWhiteListStatus = -1;
    private static int mCurrentGPDSensorState = -1;
    private static int mCurrentGravityYAxesState = -1;
    private static int mCurrentSatelliteSwitchState = -1;
    private static int mCurrentScreenState = -1;
    private static int mCurrentLandScapeGameState = -1;
    private static int mCurrentBatteryLevelState = -1;
    private static int mCurrentHorizontalGameListState = -1;
    private static int mCurrentDsiValueState = -1;
    private static int mCurrentHstEnhancementState = -1;
    private static String mCurrentSimMccState = null;
    private static boolean mIsLoopFinished = false;
    private static Handler mDeviceStateHandler = null;
    private static Map<Integer, Integer> mCloudControlStateMap = new HashMap();
    private static final SensorEventListener mSensorListenerForOneType = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.4
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            if (event != null) {
                if (event.values.length > 1) {
                    int sar2State = (int) event.values[1];
                    ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChangedForOneType sensor2 distance = " + sar2State);
                    if (sar2State != SarDeviceStateListener.mCurrentSarSensor2State) {
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(21, sar2State);
                        SarDeviceStateListener.mCurrentSarSensor2State = sar2State;
                        return;
                    }
                    return;
                }
                ModemSarLog.e(SarDeviceStateListener.LOG_TAG, "onSensorChangedForOneType length error");
            }
        }
    };
    private static final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.5
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            int yAxesStateHandle;
            if (event != null) {
                switch (event.sensor.getType()) {
                    case 9:
                        float yAxesState = event.values[1];
                        if (Float.compare(yAxesState, -9.5f) < 0) {
                            yAxesStateHandle = 1;
                        } else {
                            yAxesStateHandle = 0;
                        }
                        if (yAxesStateHandle != SarDeviceStateListener.mCurrentGravityYAxesState) {
                            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged gravityState state = " + yAxesStateHandle);
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(18, yAxesStateHandle);
                            SarDeviceStateListener.mCurrentGravityYAxesState = yAxesStateHandle;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                        int sarState = (int) event.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor1 distance = " + sarState);
                        if (sarState != SarDeviceStateListener.mCurrentSarSensorState) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(3, sarState);
                            SarDeviceStateListener.mCurrentSarSensorState = sarState;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                        int sar2State = (int) event.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor2 distance = " + sar2State);
                        if (sar2State != SarDeviceStateListener.mCurrentSarSensor2State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(9, sar2State);
                            SarDeviceStateListener.mCurrentSarSensor2State = sar2State;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_HALLSENSOR2 /* 33171068 */:
                        int hall2State = 0;
                        if (((int) event.values[0]) == 0) {
                            hall2State = 1;
                        } else if (((int) event.values[0]) == 1) {
                            hall2State = 2;
                        }
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged hall2state = " + hall2State);
                        if (hall2State != SarDeviceStateListener.mCurrentHallSensor2State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(20, hall2State);
                            SarDeviceStateListener.mCurrentHallSensor2State = hall2State;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_HALLSENSOR /* 33171087 */:
                        int hallState = 0;
                        if (((int) event.values[0]) == 0) {
                            hallState = 1;
                        } else if (((int) event.values[0]) == 1) {
                            hallState = 2;
                        }
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged hallstate = " + hallState);
                        if (hallState != SarDeviceStateListener.mCurrentHallSensorState) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(4, hallState);
                            SarDeviceStateListener.mCurrentHallSensorState = hallState;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR3 /* 33171109 */:
                        int sar3State = (int) event.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor3 distance = " + sar3State);
                        if (sar3State != SarDeviceStateListener.mCurrentSarSensor3State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(22, sar3State);
                            SarDeviceStateListener.mCurrentSarSensor3State = sar3State;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_GPDSENSOR /* 33171120 */:
                        int gpdState = (int) event.values[0];
                        if (gpdState != SarDeviceStateListener.mCurrentGPDSensorState) {
                            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged gpdState state = " + gpdState);
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(17, gpdState);
                            SarDeviceStateListener.mCurrentGPDSensorState = gpdState;
                        }
                        break;
                    default:
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged unknown event for sensor: " + event.sensor.getType());
                        break;
                }
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver(this) { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SarDeviceStateListener.dispatchBroadcastIntent(intent);
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.9
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onCallStateChanged: state = " + state);
            SarDeviceStateListener.this.refreshPhoneListener(32, state);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onDataActivity: direction = " + direction);
            SarDeviceStateListener.this.refreshPhoneListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, direction);
        }

        public void onRadioPowerStateChanged(int direction) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onRadioPowerStateChanged: direction = " + direction);
            SarDeviceStateListener.this.refreshPhoneListener(8388608, direction);
        }
    };

    public static void registerDeviceStateChangeEvent(Context context, int eventType, Handler handler, int what, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerDeviceStateChangeEvent, context = " + context + ", eventType = " + getDeviceEventTypeStringEx(eventType) + ", handler = " + handler + ", what = " + what + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                RegistrantList registrants = new RegistrantList();
                registrants.addUnique(handler, what, obj);
                mRegRequestInfoMap.put(Integer.valueOf(eventType), registrants);
            } else {
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).addUnique(handler, what, obj);
            }
            startDeviceStateChangeListener(eventType, new Registrant(handler, what, obj));
        }
    }

    public static void unregisterDeviceStateChangeEvent(int eventType, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                return;
            }
            mRegRequestInfoMap.get(Integer.valueOf(eventType)).remove(handler);
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)).size() == 0) {
                stopDeviceStateChangeListener(eventType, handler);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int eventType, String stringValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", stringValue = " + stringValue);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) != null) {
                byte[] bytes = stringValue.getBytes(StandardCharsets.UTF_8);
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).notifyRegistrants(new AsyncResult((Object) null, bytes, (Throwable) null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int eventType, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", intValue = " + intValue);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) != null) {
                byte[] bytes = ModemUtils.getBytes(intValue);
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).notifyRegistrants(new AsyncResult((Object) null, bytes, (Throwable) null));
            }
        }
        sendMsgToHandler(TYPE_SAVE_AND_BROADCAST_STATE, eventType, intValue);
    }

    private static void notifyDeviceStateChangeEvent(int eventType, Registrant r, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", r = " + r + ", intValue = " + intValue);
        if (r == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            byte[] bytes = ModemUtils.getBytes(intValue);
            r.notifyRegistrant(new AsyncResult((Object) null, bytes, (Throwable) null));
        }
    }

    private static synchronized void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SarDeviceStateListener(context);
            ModemSarLog.d(LOG_TAG, "initInstance done");
        }
    }

    private SarDeviceStateListener(Context context) {
        ModemSarLog.d(LOG_TAG, "Constructor, context = " + context);
        mContext = context;
        if (mContext == null) {
            ModemSarLog.e(LOG_TAG, "Constructor, mContext is null");
            return;
        }
        paraInit();
        startGetSystemService();
        startPhoneListener();
        startBcIntentListen();
        ModemSarLog.d(LOG_TAG, "Constructor init done");
    }

    private void paraInit() {
        ModemSarLog.d(LOG_TAG, "paraInit");
        if (mDeviceStateHandlerThread == null) {
            mDeviceStateHandlerThread = new HandlerThread("RadDeviceStateListenerBgThread");
            mDeviceStateHandlerThread.start();
            if (mDeviceStateHandler == null) {
                mDeviceStateHandler = new DeviceStateHandler(mDeviceStateHandlerThread.getLooper());
            }
        }
    }

    private void startBcIntentListen() {
        ModemSarLog.i(LOG_TAG, "startBcIntentListen");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction("android.bluetooth.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction(BC_INTENT_ACTION_MODEM_SAR_STATE_CHANGE);
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        filter.addAction(BC_INTENT_ACTION_SMART_TX_CHANGED);
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        if (!ModemSarUtils.isNtnSarSwitchOff()) {
            filter.addAction(BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        }
        filter.addAction(BC_INTENT_ACTION_CLOUD_CONTROL);
        filter.addAction(ModemSarController.BC_INTENT_ACTION_DSI_VALUE_CHANGED);
        filter.addAction(BC_INTENT_ACTION_HST_ENHANCEMENT_CHANGED);
        mContext.registerReceiver(this.mReceiver, filter, 2);
        ModemSarLog.i(LOG_TAG, "startBcIntentListen done");
    }

    private void startGetSystemService() {
        ModemSarLog.i(LOG_TAG, "startGetSystemService");
        mWifiManager = (WifiManager) mContext.getSystemService("wifi");
        mSensorManager = (SensorManager) mContext.getSystemService("sensor");
        mTelephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        mWifiCondManager = (WifiNl80211Manager) mContext.getSystemService("wifinl80211");
        mAudioManager = (AudioManager) mContext.getSystemService("audio");
        mSubscriptionManager = (SubscriptionManager) mContext.getSystemService("telephony_subscription_service");
        ModemSarLog.i(LOG_TAG, "startGetSystemService done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispatchBroadcastIntent(Intent intent) {
        if (intent == null) {
            ModemSarLog.e(LOG_TAG, "dispatchBroadcastIntent, intent is null");
            return;
        }
        final Intent intentCopy = new Intent(intent);
        if (mDeviceStateHandler == null) {
            ModemSarLog.e(LOG_TAG, "dispatchBroadcastIntent, mDeviceStateHandler is null");
            return;
        }
        boolean posted = mDeviceStateHandler.post(new Runnable() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    SarDeviceStateListener.handleBroadcastIntent(intentCopy);
                } catch (Exception e) {
                    ModemSarLog.e(SarDeviceStateListener.LOG_TAG, "handleBroadcastIntent happens Exception, action = " + intentCopy.getAction() + ", e = " + e);
                }
            }
        });
        if (!posted) {
            ModemSarLog.e(LOG_TAG, "dispatchBroadcastIntent, post failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleBroadcastIntent(Intent intent) {
        int i;
        int i2;
        String action = intent.getAction();
        if ("android.net.wifi.STATE_CHANGE".equals(action)) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (networkInfo == null) {
                ModemSarLog.e(LOG_TAG, "Wifi state changed, network info is null");
                return;
            }
            if (networkInfo.isConnected()) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            if (i2 != mCurrentWifiState) {
                ModemSarLog.d(LOG_TAG, "Wifi state changed, state = " + i2);
                notifyDeviceStateChangeEvent(0, i2);
                mCurrentWifiState = i2;
                return;
            }
            return;
        }
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
            if (13 == intent.getIntExtra("wifi_state", 0)) {
                i = 1;
            } else {
                i = 0;
            }
            if (i != mCurrentHotspotState) {
                ModemSarLog.d(LOG_TAG, "Wifi ap state changed, state = " + i);
                notifyDeviceStateChangeEvent(1, i);
                mCurrentHotspotState = i;
                return;
            }
            return;
        }
        if ("android.bluetooth.action.STATE_CHANGED".equals(action)) {
            int intExtra = intent.getIntExtra("android.bluetooth.extra.LOCAL_STATE", -1);
            int i3 = -1;
            if (1 == intExtra) {
                i3 = 1;
            } else if (intExtra == 0) {
                i3 = 0;
            }
            if (i3 != mCurrentBluetoothPanState && i3 != -1) {
                if (1 == i3) {
                    mCurrentBluetoothState = 1;
                    ModemSarLog.d(LOG_TAG, "BlueTooth is enabled by blueTooth pan");
                    notifyDeviceStateChangeEvent(8, 1);
                }
                ModemSarLog.d(LOG_TAG, "BlueTooth pan state changed, state = " + i3);
                notifyDeviceStateChangeEvent(7, i3);
                mCurrentBluetoothPanState = i3;
                return;
            }
            return;
        }
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
            int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            int i4 = -1;
            if (12 == intExtra2) {
                i4 = 1;
            } else if (10 == intExtra2) {
                i4 = 0;
                if (intent.getBooleanExtra("android.xiaomi.bluetooth.BLUETOOTH_RESTRICT_STATE", false)) {
                    i4 = 1;
                    ModemSarLog.d(LOG_TAG, "ignore bluetooth fake off state");
                }
            }
            if (i4 != mCurrentBluetoothState && i4 != -1) {
                ModemSarLog.d(LOG_TAG, "BlueTooth state changed, state = " + i4);
                notifyDeviceStateChangeEvent(8, i4);
                mCurrentBluetoothState = i4;
                if (mCurrentBluetoothState == 0) {
                    mCurrentBluetoothPanState = 0;
                    ModemSarLog.d(LOG_TAG, "BlueTooth pan is disabled by blueTooth");
                    notifyDeviceStateChangeEvent(7, 0);
                    return;
                }
                return;
            }
            return;
        }
        if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
            String stringExtra = intent.getStringExtra("ss");
            ModemSarLog.d(LOG_TAG, "Sim state = " + stringExtra + ", slotId = " + intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1));
            if ("LOADED".equals(stringExtra)) {
                ModemSarLog.d(LOG_TAG, "Sim is loaded");
                notifyDeviceStateChangeEvent(14, 0);
                sendMsgToHandler(27, 1);
                return;
            } else {
                if ("ABSENT".equals(stringExtra)) {
                    ModemSarLog.d(LOG_TAG, "Sim is absent");
                    sendMsgToHandler(27, 0);
                    return;
                }
                return;
            }
        }
        if (BC_INTENT_ACTION_MODEM_SAR_STATE_CHANGE.equals(action)) {
            String stringExtra2 = intent.getStringExtra(BC_INTENT_EXTRA_MODEM_SAR_STATE);
            if (stringExtra2 == null) {
                ModemSarLog.e(LOG_TAG, "Modem sar state is null");
                return;
            }
            try {
                mModemSarEnable = Integer.parseInt(stringExtra2);
                ModemSarLog.d(LOG_TAG, "set md state " + mModemSarEnable);
                return;
            } catch (NumberFormatException e) {
                ModemSarLog.e(LOG_TAG, "Modem sar state is invalid, state = " + stringExtra2 + ", e = " + e);
                return;
            }
        }
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action) || "android.intent.action.ACTION_POWER_CONNECTED".equals(action)) {
            ModemSarLog.d(LOG_TAG, "Usb or power connected, action: " + action);
            if (mCurrentUsbState != 1) {
                notifyDeviceStateChangeEvent(11, 1);
                mCurrentUsbState = 1;
                return;
            }
            return;
        }
        if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) || "android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
            ModemSarLog.d(LOG_TAG, "Usb or power disconnected, action: " + action);
            if (mCurrentUsbState != 0) {
                notifyDeviceStateChangeEvent(11, 0);
                mCurrentUsbState = 0;
                return;
            }
            return;
        }
        if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
            int intExtra3 = intent.getIntExtra("plugged", -1);
            int intExtra4 = intent.getIntExtra("level", 0);
            int intExtra5 = intent.getIntExtra("scale", 100);
            ModemSarLog.d(LOG_TAG, "Battery changed, usb plug = " + intExtra3);
            if ((intExtra3 == 1 || intExtra3 == 2) && mCurrentUsbState != 1) {
                notifyDeviceStateChangeEvent(11, 1);
                mCurrentUsbState = 1;
            }
            if (intExtra5 > 0) {
                int i5 = (intExtra4 * 100) / intExtra5;
                if (i5 != mCurrentBatteryLevelState) {
                    notifyDeviceStateChangeEvent(26, i5);
                    mCurrentBatteryLevelState = i5;
                    return;
                }
                return;
            }
            ModemSarLog.e(LOG_TAG, "Battery changed, scale is invalid, scale = " + intExtra5);
            return;
        }
        if (BC_INTENT_ACTION_SMART_TX_CHANGED.equals(action)) {
            int intExtra6 = intent.getIntExtra(BC_INTENT_EXTRA_SMART_TX_STATE, -1);
            ModemSarLog.d(LOG_TAG, "Smart env changed, old state = " + mCurrentSmartTxState + ", new state = " + intExtra6);
            if (mCurrentSmartTxState != intExtra6) {
                notifyDeviceStateChangeEvent(15, intExtra6);
                mCurrentSmartTxState = intExtra6;
                return;
            }
            return;
        }
        if (BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(action)) {
            boolean booleanExtra = intent.getBooleanExtra(BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
            ModemSarLog.d(LOG_TAG, "Satellite state changed, state = " + (booleanExtra ? 1 : 0));
            if (mCurrentSatelliteSwitchState != booleanExtra) {
                notifyDeviceStateChangeEvent(23, booleanExtra ? 1 : 0);
                mCurrentSatelliteSwitchState = booleanExtra ? 1 : 0;
                if (true == booleanExtra) {
                    registerForNtnRadioStateChanged();
                    return;
                } else {
                    if (!booleanExtra) {
                        unregisterForNtnRadioStateChanged();
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if ("android.intent.action.SCREEN_OFF".equals(action)) {
            ModemSarLog.d(LOG_TAG, "SCREEN OFF");
            mCurrentScreenState = 0;
            notifyDeviceStateChangeEvent(24, mCurrentScreenState);
            return;
        }
        if ("android.intent.action.SCREEN_ON".equals(action)) {
            ModemSarLog.d(LOG_TAG, "SCREEN ON");
            mCurrentScreenState = 1;
            notifyDeviceStateChangeEvent(24, mCurrentScreenState);
            return;
        }
        if (BC_INTENT_ACTION_CLOUD_CONTROL.equals(action)) {
            int intExtra7 = intent.getIntExtra(BC_INTENT_EXTRA_FEATURE_TYPE, -1);
            int intExtra8 = intent.getIntExtra(BC_INTENT_EXTRA_CONTROL_CODE, -1);
            ModemSarLog.i(LOG_TAG, "Cloud control state changed, featureType = " + intExtra7 + ", controlCode = " + intExtra8);
            if (intExtra7 >= 1000 && intExtra7 <= 1100) {
                sendDelayedMsgToHandler(100, intExtra7, intExtra8, CLOUD_CONTROL_DELAY_TIME);
                return;
            }
            return;
        }
        if (ModemSarController.BC_INTENT_ACTION_DSI_VALUE_CHANGED.equals(action)) {
            int intExtra9 = intent.getIntExtra(ModemSarController.BC_INTENT_EXTRA_DSI_VALUE, 0);
            ModemSarLog.i(LOG_TAG, "DSI value changed, dsiValue = " + intExtra9);
            if (intExtra9 != mCurrentDsiValueState) {
                mCurrentDsiValueState = intExtra9;
                notifyDeviceStateChangeEvent(29, intExtra9);
                return;
            }
            return;
        }
        if (BC_INTENT_ACTION_HST_ENHANCEMENT_CHANGED.equals(action)) {
            int intExtra10 = intent.getIntExtra(BC_INTENT_EXTRA_HST_ENHANCEMENT_STATE, 0);
            ModemSarLog.d(LOG_TAG, "HST enhancement changed, hstEnhancement = " + intExtra10);
            if (intExtra10 != mCurrentHstEnhancementState) {
                mCurrentHstEnhancementState = intExtra10;
                notifyDeviceStateChangeEvent(30, intExtra10);
            }
        }
    }

    private static void sendMsgToHandler(int what, int value) {
        if (mDeviceStateHandler != null) {
            byte[] data = ModemUtils.getBytes(value);
            mDeviceStateHandler.sendMessage(mDeviceStateHandler.obtainMessage(what, new AsyncResult((Object) null, data, (Throwable) null)));
        }
    }

    private static void sendMsgToHandler(int what, int arg1, int arg2) {
        if (mDeviceStateHandler != null) {
            byte[] data = ModemSarUtils.getBytes(arg1, arg2);
            mDeviceStateHandler.sendMessage(mDeviceStateHandler.obtainMessage(what, new AsyncResult((Object) null, data, (Throwable) null)));
        }
    }

    private static void sendDelayedMsgToHandler(int what, int arg1, int arg2, int ms) {
        if (mDeviceStateHandler != null) {
            byte[] data = ModemSarUtils.getBytes(arg1, arg2);
            mDeviceStateHandler.sendMessageDelayed(mDeviceStateHandler.obtainMessage(what, new AsyncResult((Object) null, data, (Throwable) null)), ms);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSimMcc() {
        List<SubscriptionInfo> subscriptionInfoList;
        if (mSubscriptionManager == null || mTelephonyManager == null || (subscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList()) == null || subscriptionInfoList.isEmpty()) {
            return SIM_MCC_DEFAULT_STATE;
        }
        int phoneCount = mTelephonyManager.getPhoneCount();
        String[] mccArray = new String[phoneCount];
        for (SubscriptionInfo Info : subscriptionInfoList) {
            if (Info != null) {
                int mcc = Info.getMcc();
                int slotId = Info.getSimSlotIndex();
                String mccStr = String.format("%03d", Integer.valueOf(mcc));
                if (mccStr.length() == 3) {
                    mccArray[slotId] = mccStr;
                }
            }
        }
        for (int i = 0; i < mccArray.length; i++) {
            if (mccArray[i] == null) {
                mccArray[i] = "";
            }
        }
        ModemSarLog.d(LOG_TAG, "simMcc = " + String.join(SIM_MCC_DEFAULT_STATE, mccArray));
        return String.join(SIM_MCC_DEFAULT_STATE, mccArray);
    }

    private static void checkWifiApStatus() {
        int wifiApState = getWifiApState();
        if (wifiApState != mCurrentHotspotState) {
            ModemSarLog.d(LOG_TAG, "checkWifiApStatus, state = " + wifiApState);
            notifyDeviceStateChangeEvent(1, wifiApState);
            mCurrentHotspotState = wifiApState;
        }
    }

    private static int getWifiApState() {
        if (mWifiManager == null) {
            return 0;
        }
        int wifiApState = mWifiManager.getWifiApState();
        ModemSarLog.d(LOG_TAG, "getWifiApState = " + wifiApState);
        if (13 == wifiApState) {
            return 1;
        }
        return 0;
    }

    private static void checkBluetoothState() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            ModemSarLog.i(LOG_TAG, "checkBluetoothState, start to get bluetooth state");
            boolean zIsEnabled = defaultAdapter.isEnabled();
            if (zIsEnabled != mCurrentBluetoothState) {
                ModemSarLog.d(LOG_TAG, "checkBluetoothState, state = " + (zIsEnabled ? 1 : 0));
                notifyDeviceStateChangeEvent(8, zIsEnabled ? 1 : 0);
                mCurrentBluetoothState = zIsEnabled ? 1 : 0;
                if (!zIsEnabled) {
                    mCurrentBluetoothPanState = 0;
                    ModemSarLog.d(LOG_TAG, "checkBluetoothState, blueTooth is disabled, set blueTooth pan disabled");
                    notifyDeviceStateChangeEvent(7, 0);
                }
            }
        }
    }

    private static void checkBluetoothPanState() {
        ModemSarLog.i(LOG_TAG, "checkBluetoothPanState start");
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            ModemSarLog.i(LOG_TAG, "checkBluetoothPanState, start to getProfileProxy");
            bluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.3
                @Override // android.bluetooth.BluetoothProfile.ServiceListener
                public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                    ModemSarLog.i(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState onServiceConnected, profile = " + i);
                    if (5 == i) {
                        BluetoothProfile bluetoothProfile2 = (BluetoothPan) bluetoothProfile;
                        boolean zIsTetheringOn = bluetoothProfile2.isTetheringOn();
                        ModemSarLog.i(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState onServiceConnected, tetheringState = " + (zIsTetheringOn ? 1 : 0));
                        if (zIsTetheringOn != SarDeviceStateListener.mCurrentBluetoothPanState) {
                            if (true == zIsTetheringOn) {
                                SarDeviceStateListener.mCurrentBluetoothState = 1;
                                ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState, blueTooth pan is enabled, set blueTooth state enabled");
                                SarDeviceStateListener.notifyDeviceStateChangeEvent(8, 1);
                            }
                            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState, pan state = " + (zIsTetheringOn ? 1 : 0));
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(7, zIsTetheringOn ? 1 : 0);
                            SarDeviceStateListener.mCurrentBluetoothPanState = zIsTetheringOn ? 1 : 0;
                        }
                        bluetoothAdapter.closeProfileProxy(5, bluetoothProfile2);
                    }
                }

                @Override // android.bluetooth.BluetoothProfile.ServiceListener
                public void onServiceDisconnected(int profile) {
                    ModemSarLog.i(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState onServiceDisconnected, profile = " + profile);
                }
            }, 5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getMccState(String country) {
        int mccState = 0;
        String mNetworkCountryCode = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (mNetworkCountryCode.equals("")) {
            mNetworkCountryCode = country;
            ModemSarLog.d(LOG_TAG, "country code change to " + country);
        }
        if (mNetworkCountryCode.length() < 2) {
            ModemSarLog.d(LOG_TAG, "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] mCountryCode = mNetworkCountryCode.split(SIM_MCC_DEFAULT_STATE);
        for (int i = 0; i < mCountryCode.length; i++) {
            ModemSarLog.d(LOG_TAG, "mCountryCode[" + i + "] = " + mCountryCode[i]);
            mCountryCode[i] = mCountryCode[i].toLowerCase();
            if (mCountryCode[i] != null && !mCountryCode[i].isEmpty()) {
                if (mFCCList.contains(mCountryCode[i])) {
                    if (i > 0) {
                        if (mccState == 2 || mccState == 0) {
                            mccState = 2;
                        } else {
                            mccState = 3;
                        }
                    } else {
                        mccState = 2;
                    }
                } else if (mCEList.contains(mCountryCode[i])) {
                    if (i > 0) {
                        if (mccState == 1 || mccState == 0) {
                            mccState = 1;
                        } else {
                            mccState = 3;
                        }
                    } else {
                        mccState = 1;
                    }
                } else {
                    mccState = 3;
                    break;
                }
            }
        }
        if (mccState == 0) {
            ModemSarLog.d(LOG_TAG, "Mcc state is 0, can't get network countryCode, use MCC_DEFAULT");
            mccState = 3;
        }
        ModemSarLog.d(LOG_TAG, "mNetworkCountryCode: " + mNetworkCountryCode + ", mccState: " + mccState);
        return mccState;
    }

    private static boolean isSarSensorSubstanceInactive() {
        boolean isInactive = false;
        if (mSarSensorSubstance1Group.contains(SystemProperties.get("ro.product.device", "null"))) {
            ModemSarLog.d(LOG_TAG, "isSarSensorSubstanceInactive, device is in sarSensorSubstance1Group");
            if (mSensorManager != null && mSarSensorSubstance1 == null) {
                mSarSensorSubstance1 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE1);
                if (mSarSensorSubstance1 == null) {
                    isInactive = true;
                }
            }
        }
        ModemSarLog.d(LOG_TAG, "isSarSensorSubstanceInactive, isInactive = " + isInactive);
        return isInactive;
    }

    private static class SarCountryCodeChangeListener implements WifiNl80211Manager.CountryCodeChangedListener {
        private SarCountryCodeChangeListener() {
        }

        public void onCountryCodeChanged(String country) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onCountryCodeChanged: " + country);
            int mccState = SarDeviceStateListener.getMccState(country);
            if (mccState != SarDeviceStateListener.mCurrentMccState) {
                SarDeviceStateListener.notifyDeviceStateChangeEvent(6, mccState);
                SarDeviceStateListener.mCurrentMccState = mccState;
            }
        }
    }

    private static void checkAudioReceiverState() {
        int receiverState = 0;
        String propAudioReceiverState = SystemProperties.get(PROPERTY_RECEIVER_STATUS, "off");
        if (propAudioReceiverState.equals("on")) {
            receiverState = 1;
        } else if (propAudioReceiverState.equals("off")) {
            receiverState = 0;
        }
        if (receiverState != mCurrentAudioReceiverState) {
            ModemSarLog.d(LOG_TAG, "current audioReceiverState = " + receiverState);
            notifyDeviceStateChangeEvent(5, receiverState);
            mCurrentAudioReceiverState = receiverState;
            sendAudioReceiverStateBroadcast(receiverState);
        }
    }

    private static void sendAudioReceiverStateBroadcast(int state) {
        Intent intent = new Intent(BC_INTENT_ACTION_RECEIVER_STATE_CHANGED);
        intent.putExtra("state", state);
        mContext.sendBroadcast(intent);
    }

    private static final void startPollAudioReceiverStatus() {
        Thread ReceiverPollThread = new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.6
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollReceiver();
            }
        };
        ReceiverPollThread.start();
        mListenerStateMap.put(5, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void pollReceiver() {
        int state = 0;
        ModemSarLog.d(LOG_TAG, "Polling audio is running");
        while (!mIsLoopFinished) {
            String propAudioReceiverState = SystemProperties.get(PROPERTY_RECEIVER_STATUS, "off");
            if (propAudioReceiverState.equals("on")) {
                state = 1;
            } else if (propAudioReceiverState.equals("off")) {
                state = 0;
            }
            if (state != mCurrentAudioReceiverState) {
                ModemSarLog.d(LOG_TAG, "audioReceiverState: " + state);
                notifyDeviceStateChangeEvent(5, state);
                mCurrentAudioReceiverState = state;
                sendAudioReceiverStateBroadcast(state);
            }
            SystemClock.sleep(1000L);
        }
        mListenerStateMap.put(5, 0);
        ModemSarLog.d(LOG_TAG, "Polling audio is stopped");
    }

    private static final void startUseCaseTest() {
        Thread UseCasePollThread = new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollUseCaseTest();
            }
        };
        UseCasePollThread.start();
        mListenerStateMap.put(12, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void pollUseCaseTest() {
        ModemSarLog.d(LOG_TAG, "Polling use case is running");
        while (!mIsLoopFinished) {
            int usetest = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (usetest != mCurrentUseCaseTest) {
                ModemSarLog.d(LOG_TAG, "usetest: " + usetest);
                notifyDeviceStateChangeEvent(12, usetest);
                mCurrentUseCaseTest = usetest;
            }
            SystemClock.sleep(1000L);
        }
        mListenerStateMap.put(12, 0);
        ModemSarLog.d(LOG_TAG, "Polling use case is stopped");
    }

    private static final void startPollCableStatus() {
        Thread CablePollThread = new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollCableStatus();
            }
        };
        CablePollThread.start();
        mListenerStateMap.put(10, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void pollCableStatus() {
        if (new File(DEVICE_CABLE_FILE).exists()) {
            ModemSarLog.d(LOG_TAG, "enter pollCableStatus ...");
            UEventObserver cableStatusObserver = new CableStatusObserver();
            cableStatusObserver.startObserving("DEVPATH=/sys/bus/platform/devices/soc/soc:md_testing_mode");
        } else {
            ModemSarLog.d(LOG_TAG, "Cable file is not exist.");
            mListenerStateMap.put(10, 0);
        }
    }

    private static class CableStatusObserver extends UEventObserver {
        private CableStatusObserver() {
        }

        public void onUEvent(UEventObserver.UEvent uEvent) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onUEvent: " + uEvent);
            boolean zEquals = "1".equals(uEvent.get("STATUS"));
            if (zEquals != SarDeviceStateListener.mCableGpioStatus) {
                ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "cableStatus changed: " + (zEquals ? 1 : 0));
                SarDeviceStateListener.notifyDeviceStateChangeEvent(10, zEquals ? 1 : 0);
                SarDeviceStateListener.mCableGpioStatus = zEquals ? 1 : 0;
            }
        }
    }

    private void startPhoneListener() {
        mTelephonyManager.listen(this.mPhoneStateListener, 8388768);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshPhoneListener(int messageType, int state) {
        ModemSarLog.d(LOG_TAG, "modem or radio power state change, messageType: " + messageType + ", state: " + state);
        if (messageType == 128) {
            if (state == 1 || state == 2 || state == 3) {
                mDataStateActive = true;
            } else if (state == 0 || state == 4) {
                mDataStateActive = false;
            }
        } else if (messageType == 32) {
            if (state == 1 || state == 2) {
                mCallStateActive = true;
            } else if (state == 0) {
                mCallStateActive = false;
            }
        } else if (messageType == 8388608 && state == 1) {
            ModemSarLog.d(LOG_TAG, "refreshPhoneListener, radio power on");
            notifyDeviceStateChangeEvent(13, state);
        }
        int modemState = (mDataStateActive || mCallStateActive) ? 1 : 0;
        if (modemState != mCurrentModemState) {
            notifyDeviceStateChangeEvent(2, modemState);
            mCurrentModemState = modemState;
        }
    }

    private static void registerForNtnRadioStateChanged() {
        NtnStateListener.registerNtnStateChangeEvent(mContext, 1, mDeviceStateHandler, 19, null);
    }

    private static void unregisterForNtnRadioStateChanged() {
        NtnStateListener.unregisterNtnStateChangeEvent(1, mDeviceStateHandler);
    }

    private static void registerForLandscapeStateChanged() {
        GameStateListener.registerGameStateListener(mContext, 1, mDeviceStateHandler, 25, null);
    }

    private static void unregisterForLandscapeStateChanged() {
        GameStateListener.unregisterGameStateChangeEvent(1, mDeviceStateHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerForHorizontalGameStateChanged() {
        if (isFeatureSupported(28)) {
            GameStateListener.registerGameStateListener(mContext, 2, mDeviceStateHandler, 28, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void unregisterForHorizontalGameStateChanged() {
        if (isFeatureSupported(28)) {
            GameStateListener.unregisterGameStateChangeEvent(2, mDeviceStateHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerForWhiteListStateChanged() {
        if (isFeatureSupported(16)) {
            WhiteListAppStateListener.registerWhiteListAppStateListener(mContext, 1, mDeviceStateHandler, 16, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void unregisterForWhiteListStateChanged() {
        if (isFeatureSupported(16)) {
            WhiteListAppStateListener.unregisterWhiteListAppStateChangeEvent(1, mDeviceStateHandler);
        }
    }

    private static void notifyBatteryStateChanged() {
        if (mCurrentBatteryLevelState != -1) {
            notifyDeviceStateChangeEvent(26, mCurrentBatteryLevelState);
        }
    }

    private static void startDeviceStateChangeListener(int eventType, Registrant r) {
        ModemSarLog.d(LOG_TAG, "startDeviceStateChangeListener, eventType = " + getDeviceEventTypeStringEx(eventType) + ", r = " + r);
        synchronized (mListenerStateMap) {
            try {
                switch (eventType) {
                    case 1:
                        checkWifiApStatus();
                        break;
                    case 2:
                    case 11:
                    case 13:
                    case 14:
                    case 15:
                    case 19:
                    case 23:
                    case 24:
                    case 27:
                    default:
                        ModemSarLog.d(LOG_TAG, "This listener is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                        break;
                    case 3:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager != null && !isSarSensorSubstanceInactive()) {
                                mSarSensor1 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                                boolean sensorFlag = mSensorManager.registerListener(mSensorEventListener, mSarSensor1, 3);
                                if (!sensorFlag) {
                                    notifyDeviceStateChangeEvent(3, r, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor1 registration failed, set sensor1 valid");
                                } else {
                                    mListenerStateMap.put(3, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            } else {
                                notifyDeviceStateChangeEvent(3, r, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null , set sensor1 valid");
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(3, r, mCurrentSarSensorState);
                        }
                        break;
                    case 4:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager == null) {
                                notifyDeviceStateChangeEvent(4, r, 0);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set hall abnomal");
                            } else {
                                mHallSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, false);
                                boolean sensorFlag2 = mSensorManager.registerListener(mSensorEventListener, mHallSensor, 3);
                                if (!sensorFlag2) {
                                    notifyDeviceStateChangeEvent(4, r, 0);
                                    ModemSarLog.d(LOG_TAG, "hall sensor registration failed , set hall abnomal");
                                } else {
                                    mListenerStateMap.put(4, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HALL_SENSOR_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(4, r, mCurrentHallSensorState);
                        }
                        break;
                    case 5:
                        checkAudioReceiverState();
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            startPollAudioReceiverStatus();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_AUDIO_RECEIVER_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(5, r, mCurrentAudioReceiverState);
                        }
                        break;
                    case 6:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mWifiCondManager == null) {
                                ModemSarLog.d(LOG_TAG, "mWifiCondManager is null, CountryCodeChangedListener valid");
                            } else {
                                mSarCountryCodeChangeListener = new SarCountryCodeChangeListener();
                                mWifiCondManager.registerCountryCodeChangedListener(new DynamicSarServiceSarV8550$$ExternalSyntheticLambda0(), mSarCountryCodeChangeListener);
                                mListenerStateMap.put(6, 1);
                                ModemSarLog.d(LOG_TAG, "TYPE_MCC_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_MCC_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(6, r, mCurrentMccState);
                        }
                        break;
                    case 7:
                        checkBluetoothPanState();
                        break;
                    case 8:
                        checkBluetoothState();
                        break;
                    case 9:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSarSensor2 != null) {
                                ModemSarLog.e(LOG_TAG, "sensor2 is not null");
                                notifyDeviceStateChangeEvent(9, r, mCurrentSarSensor2State);
                            } else if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(9, r, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set sensor2 valid");
                            } else {
                                mSarSensor2 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR2, true);
                                boolean sensorFlag3 = mSensorManager.registerListener(mSensorEventListener, mSarSensor2, 3);
                                if (!sensorFlag3) {
                                    notifyDeviceStateChangeEvent(9, r, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor2 registration failed, set sensor2 valid");
                                } else {
                                    mListenerStateMap.put(9, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR2_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(9, r, mCurrentSarSensor2State);
                        }
                        break;
                    case 10:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            startPollCableStatus();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_CABLE_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        break;
                    case 12:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            startUseCaseTest();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_USE_CASE_TEST is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(12, r, mCurrentUseCaseTest);
                        }
                        break;
                    case 16:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            registerForWhiteListStateChanged();
                            mListenerStateMap.put(Integer.valueOf(eventType), 1);
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_WHITE_LIST_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            if (mCurrentWhiteListStatus != -1) {
                                notifyDeviceStateChangeEvent(16, r, mCurrentWhiteListStatus);
                            }
                        }
                        break;
                    case 17:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager == null) {
                                notifyDeviceStateChangeEvent(17, -2);
                                ModemSarLog.e(LOG_TAG, "mSensorManager is null, set GPD big move");
                            } else {
                                mGPDSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE_GPDSENSOR, false);
                                boolean sensorFlag4 = mSensorManager.registerListener(mSensorEventListener, mGPDSensor, 3);
                                if (!sensorFlag4) {
                                    notifyDeviceStateChangeEvent(17, -2);
                                    ModemSarLog.e(LOG_TAG, "GPD sensor registration failed , set GPD big move");
                                } else {
                                    mListenerStateMap.put(17, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_GPD_SENSOR_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_GPD_SENSOR_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(17, r, mCurrentGPDSensorState);
                        }
                        break;
                    case 18:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager == null) {
                                notifyDeviceStateChangeEvent(18, r, 0);
                                ModemSarLog.e(LOG_TAG, "mSensorManager is null, set gravity horizontal");
                            } else {
                                mGravitySensor = mSensorManager.getDefaultSensor(9, false);
                                boolean sensorFlag5 = mSensorManager.registerListener(mSensorEventListener, mGravitySensor, 3);
                                if (!sensorFlag5) {
                                    notifyDeviceStateChangeEvent(18, 0);
                                    ModemSarLog.e(LOG_TAG, "gravity sensor registration failed, set gravity horizontal");
                                } else {
                                    mListenerStateMap.put(18, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(18, r, mCurrentGravityYAxesState);
                        }
                        break;
                    case 20:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager == null) {
                                notifyDeviceStateChangeEvent(20, r, 0);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set hall2 abnomal");
                            } else {
                                mHallSensor2 = mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR2, false);
                                boolean sensorFlag6 = mSensorManager.registerListener(mSensorEventListener, mHallSensor2, 3);
                                if (!sensorFlag6) {
                                    notifyDeviceStateChangeEvent(20, r, 0);
                                    ModemSarLog.e(LOG_TAG, "hall2 sensor registration failed , set hall abnomal");
                                } else {
                                    mListenerStateMap.put(20, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(20, r, mCurrentHallSensor2State);
                        }
                        break;
                    case 21:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSarSensor2 != null) {
                                ModemSarLog.e(LOG_TAG, "sensor2ForOneType is not null");
                                notifyDeviceStateChangeEvent(21, r, mCurrentSarSensor2State);
                            } else if (mSensorManager != null && !isSarSensorSubstanceInactive()) {
                                mSarSensor2 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                                boolean sensorFlag7 = mSensorManager.registerListener(mSensorListenerForOneType, mSarSensor2, 3);
                                if (!sensorFlag7) {
                                    notifyDeviceStateChangeEvent(21, r, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor2 registration failed, set sensor2 valid");
                                } else {
                                    mListenerStateMap.put(21, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            } else {
                                notifyDeviceStateChangeEvent(21, r, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set sensor2 valid");
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR2_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(21, r, mCurrentSarSensor2State);
                        }
                        break;
                    case 22:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(22, r, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null , set sensor3 valid");
                            } else {
                                mSarSensor3 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR3, true);
                                boolean sensorFlag8 = mSensorManager.registerListener(mSensorEventListener, mSarSensor3, 3);
                                if (!sensorFlag8) {
                                    notifyDeviceStateChangeEvent(22, r, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor1 registration failed, set sensor1 valid");
                                } else {
                                    mListenerStateMap.put(22, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is running, eventType = " + getDeviceEventTypeStringEx(eventType));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            notifyDeviceStateChangeEvent(22, r, mCurrentSarSensorState);
                        }
                        break;
                    case 25:
                        registerForLandscapeStateChanged();
                        break;
                    case 26:
                        notifyBatteryStateChanged();
                        break;
                    case 28:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            registerForHorizontalGameStateChanged();
                            mListenerStateMap.put(Integer.valueOf(eventType), 1);
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HORIZONTAL_GAME_LIST_STATE is already running, eventType = " + getDeviceEventTypeStringEx(eventType));
                            if (mCurrentHorizontalGameListState != -1) {
                                notifyDeviceStateChangeEvent(28, r, mCurrentHorizontalGameListState);
                            }
                        }
                        break;
                    case 29:
                        if (mCurrentDsiValueState != -1) {
                            notifyDeviceStateChangeEvent(29, r, mCurrentDsiValueState);
                        }
                        break;
                    case 30:
                        if (mCurrentHstEnhancementState != -1) {
                            notifyDeviceStateChangeEvent(30, r, mCurrentHstEnhancementState);
                        }
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void stopDeviceStateChangeListener(int eventType, Handler h) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + getDeviceEventTypeStringEx(eventType) + ", h = " + h);
        if (mSensorManager == null) {
            ModemSarLog.e(LOG_TAG, "mSensorManager is null");
            return;
        }
        synchronized (mListenerStateMap) {
            try {
                switch (eventType) {
                    case 3:
                        if (mSarSensor1 == null) {
                            ModemSarLog.e(LOG_TAG, "mSensor1 is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mSarSensor1);
                            mSarSensor1 = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentSarSensorState = -1;
                        break;
                    case 4:
                        if (mHallSensor == null) {
                            ModemSarLog.e(LOG_TAG, "mHallSensor is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mHallSensor);
                            mHallSensor = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentHallSensorState = -1;
                        break;
                    case 9:
                        if (mSarSensor2 == null) {
                            ModemSarLog.e(LOG_TAG, "mSensor2 is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mSarSensor2);
                            mSarSensor2 = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentSarSensor2State = -1;
                        break;
                    case 16:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) != null && mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 1) {
                            unregisterForWhiteListStateChanged();
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                        }
                        mCurrentWhiteListStatus = -1;
                        break;
                    case 17:
                        if (mGPDSensor == null) {
                            ModemSarLog.e(LOG_TAG, "mGPDSensor is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mGPDSensor);
                            mGPDSensor = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_GPD_SENSOR_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentGPDSensorState = -1;
                        break;
                    case 18:
                        if (mGravitySensor == null) {
                            ModemSarLog.e(LOG_TAG, "mGravitySensor is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mGravitySensor);
                            mGravitySensor = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentGravityYAxesState = -1;
                        break;
                    case 20:
                        if (mHallSensor2 == null) {
                            ModemSarLog.e(LOG_TAG, "mHallSensor2 is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mHallSensor2);
                            mHallSensor2 = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentHallSensor2State = -1;
                        break;
                    case 21:
                        if (mSarSensor2 == null) {
                            ModemSarLog.e(LOG_TAG, "mSensor2AForOneType is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorListenerForOneType, mSarSensor2);
                            mSarSensor2 = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_USE_SENSOR1_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentSarSensor2State = -1;
                        break;
                    case 22:
                        if (mSarSensor3 == null) {
                            ModemSarLog.e(LOG_TAG, "mSensor3 is null");
                        } else {
                            mSensorManager.unregisterListener(mSensorEventListener, mSarSensor3);
                            mSarSensor3 = null;
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            ModemSarLog.d(LOG_TAG, "TYPE_SENSOR3_STATE is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        }
                        mCurrentSarSensor3State = -1;
                        break;
                    case 25:
                        unregisterForLandscapeStateChanged();
                        mCurrentLandScapeGameState = -1;
                        break;
                    case 28:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) != null && mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 1) {
                            unregisterForHorizontalGameStateChanged();
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                        }
                        mCurrentHorizontalGameListState = -1;
                        break;
                    default:
                        ModemSarLog.d(LOG_TAG, "This listener is stopped, eventType = " + getDeviceEventTypeStringEx(eventType));
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static class DeviceStateHandler extends Handler {
        private static final String TAG = "RadDeviceStateListener_Handler: ";

        public DeviceStateHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ModemSarLog.d(TAG, "handleMessage, msg = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            byte[] bytes = (byte[]) ar.result;
            ByteBuffer buffer = ModemUtils.getBuffer(bytes);
            switch (msg.what) {
                case 16:
                    int state = buffer.getInt();
                    ModemSarLog.d(TAG, "white list state = " + state);
                    if (state != SarDeviceStateListener.mCurrentWhiteListStatus) {
                        SarDeviceStateListener.mCurrentWhiteListStatus = state;
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(16, state);
                    }
                    break;
                case 19:
                    int state2 = buffer.getInt();
                    ModemSarLog.d(TAG, "ntn radio power state = " + state2);
                    if (1 == state2) {
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(19, state2);
                    }
                    break;
                case 25:
                    int state3 = buffer.getInt();
                    ModemSarLog.d(TAG, "landscape game state = " + state3);
                    if (state3 != SarDeviceStateListener.mCurrentLandScapeGameState) {
                        SarDeviceStateListener.mCurrentLandScapeGameState = state3;
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(25, state3);
                    }
                    break;
                case 27:
                    int state4 = buffer.getInt();
                    ModemSarLog.d(TAG, "sim mcc state = " + state4);
                    if (state4 == 1 || state4 == 0) {
                        String mcc = SarDeviceStateListener.getSimMcc();
                        if (!mcc.equals(SarDeviceStateListener.mCurrentSimMccState)) {
                            SarDeviceStateListener.mCurrentSimMccState = mcc;
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(27, SarDeviceStateListener.mCurrentSimMccState);
                            ModemSarLog.d(TAG, "new sim mcc = " + SarDeviceStateListener.mCurrentSimMccState);
                        }
                    }
                    break;
                case 28:
                    int state5 = buffer.getInt();
                    ModemSarLog.d(TAG, "horizontal game list state = " + state5);
                    if (state5 != SarDeviceStateListener.mCurrentHorizontalGameListState) {
                        SarDeviceStateListener.mCurrentHorizontalGameListState = state5;
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(28, state5);
                    }
                    break;
                case 100:
                    int featureType = buffer.getInt();
                    int controlCode = buffer.getInt();
                    ModemSarLog.d(TAG, "featureType = " + featureType + ", controlCode = " + controlCode);
                    if (featureType == SarDeviceStateListener.CLOUD_CONTROL_FEATURE_TYPE_HORIZONTAL_GAME_LIST) {
                        if (controlCode == 1) {
                            SarDeviceStateListener.setCloudControlState(28, 1);
                            SarDeviceStateListener.registerForHorizontalGameStateChanged();
                        } else if (controlCode == 0) {
                            SarDeviceStateListener.unregisterForHorizontalGameStateChanged();
                            SarDeviceStateListener.setCloudControlState(28, 0);
                            SarDeviceStateListener.mCurrentHorizontalGameListState = 0;
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(28, SarDeviceStateListener.mCurrentHorizontalGameListState);
                        }
                    } else if (featureType == SarDeviceStateListener.CLOUD_CONTROL_FEATURE_TYPE_SCAN_CODE) {
                        if (controlCode == 1) {
                            SarDeviceStateListener.setCloudControlState(16, 1);
                            SarDeviceStateListener.registerForWhiteListStateChanged();
                        } else if (controlCode == 0) {
                            SarDeviceStateListener.unregisterForWhiteListStateChanged();
                            SarDeviceStateListener.setCloudControlState(16, 0);
                            SarDeviceStateListener.mCurrentWhiteListStatus = 0;
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(16, SarDeviceStateListener.mCurrentWhiteListStatus);
                        }
                    }
                    break;
                case SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE /* 200 */:
                    int eventType = buffer.getInt();
                    SarDeviceStateListener.saveAndBroadcastDeviceStateChangeEvent(eventType, buffer.getInt());
                    break;
            }
        }
    }

    private static boolean isFeatureSupported(int eventType) {
        boolean isSupported = true;
        if (getCloudControlState(eventType) == 0) {
            isSupported = false;
        }
        ModemSarLog.i(LOG_TAG, "isFeatureSupported, isSupported = " + isSupported + ", eventType = " + getDeviceEventTypeStringEx(eventType));
        return isSupported;
    }

    private static int getCloudControlState(int eventType) {
        int state;
        int result = -1;
        int featureType = convertEventTypeToFeatureType(eventType);
        if (featureType < 1000 || featureType > 1100) {
            return -1;
        }
        if (mCloudControlStateMap.get(Integer.valueOf(eventType)) != null && ((state = mCloudControlStateMap.get(Integer.valueOf(eventType)).intValue()) == 1 || state == 0)) {
            ModemSarLog.i(LOG_TAG, "getCloudControlState from map, featureType = " + featureType + ", result = " + state);
            return state;
        }
        String propControl = PROPERTY_CLOUD_CONTROL_PREFIX + (featureType - 1000);
        try {
            result = Integer.parseInt(SystemProperties.get(propControl, "-1"));
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "getCloudControlState happens Exception, featureType = " + featureType);
            e.printStackTrace();
        }
        if (result != -1) {
            mCloudControlStateMap.put(Integer.valueOf(eventType), Integer.valueOf(result));
        }
        ModemSarLog.i(LOG_TAG, "getCloudControlState from prop, featureType = " + featureType + ", result = " + result);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCloudControlState(int eventType, int controlCode) {
        int featureType = convertEventTypeToFeatureType(eventType);
        if (featureType < 1000 || featureType > 1100) {
            return;
        }
        if (controlCode != 1 && controlCode != 0) {
            return;
        }
        ModemSarLog.i(LOG_TAG, "setCloudControlState, featureType = " + featureType + ", controlCode = " + controlCode);
        mCloudControlStateMap.put(Integer.valueOf(eventType), Integer.valueOf(controlCode));
        String propControl = PROPERTY_CLOUD_CONTROL_PREFIX + (featureType - 1000);
        String propValue = "" + controlCode;
        try {
            SystemProperties.set(propControl, propValue);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "setCloudControlState happens Exception, featureType = " + featureType + ", controlCode = " + controlCode);
            e.printStackTrace();
        }
    }

    private static int convertEventTypeToFeatureType(int eventType) {
        switch (eventType) {
            case 16:
                return CLOUD_CONTROL_FEATURE_TYPE_SCAN_CODE;
            case 28:
                return CLOUD_CONTROL_FEATURE_TYPE_HORIZONTAL_GAME_LIST;
            default:
                return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveAndBroadcastDeviceStateChangeEvent(int eventType, int intValue) {
        try {
            saveDeviceStateChangeEvent(eventType, intValue);
            broadcastDeviceStateChangeEvent(eventType, intValue);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "saveAndBroadcastDeviceStateChangeEvent happens Exception, eventType = " + getDeviceEventTypeStringEx(eventType) + ", intValue = " + intValue);
            e.printStackTrace();
        }
    }

    private static void saveDeviceStateChangeEvent(int eventType, int intValue) {
        ModemSarLog.d(LOG_TAG, "saveDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", intValue = " + intValue);
        String strDeviceState = getDeviceEventTypeString(eventType);
        if (strDeviceState == null) {
            ModemSarLog.d(LOG_TAG, "Not find eventType, saveDeviceStateChangeEvent do nothing, eventType = " + getDeviceEventTypeStringEx(eventType) + ", intValue = " + intValue);
            return;
        }
        String strPorpName = PROPERTY_DEVICE_STATE_PREFIX + strDeviceState.toLowerCase();
        try {
            SystemProperties.set(strPorpName, "" + intValue);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "saveDeviceStateChangeEvent happen exception, prop = " + strPorpName);
            e.printStackTrace();
        }
    }

    private static void broadcastDeviceStateChangeEvent(int eventType, int intValue) {
        ModemSarLog.d(LOG_TAG, "broadcastDeviceStateChangeEvent, eventType = " + getDeviceEventTypeStringEx(eventType) + ", intValue = " + intValue);
        Intent intent = new Intent(BC_INTENT_ACTION_DEVICE_STATE_CHANED);
        intent.putExtra(BC_INTENT_EXTRA_DEVICE_STATE_TYPE, eventType);
        intent.putExtra(BC_INTENT_EXTRA_DEVICE_STATE_VAL, intValue);
        mContext.sendBroadcast(intent);
    }
}
