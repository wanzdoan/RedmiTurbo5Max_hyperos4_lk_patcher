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
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UEventObserver;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.ot.pubsub.b.a;
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
    private static final String[] CEList;
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
    private static final String[] FCCList;
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
    private static final List mCEList;
    private static boolean mCallStateActive;
    private static boolean mDataStateActive;
    private static HandlerThread mDeviceStateHandlerThread;
    private static final List mFCCList;
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
    private static final List mSarSensorSubstance1Group = new ArrayList(Arrays.asList("rodin"));
    private static Map mListenerStateMap = new HashMap();
    private static Context mContext = null;
    private static SarDeviceStateListener mInstance = null;
    private static Map mRegRequestInfoMap = new HashMap();
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
    private static Map mCloudControlStateMap = new HashMap();
    private static final SensorEventListener mSensorListenerForOneType = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.4
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                float[] fArr = sensorEvent.values;
                if (fArr.length > 1) {
                    int i = (int) fArr[1];
                    ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChangedForOneType sensor2 distance = " + i);
                    if (i != SarDeviceStateListener.mCurrentSarSensor2State) {
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(21, i);
                        SarDeviceStateListener.mCurrentSarSensor2State = i;
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
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                int i = 2;
                switch (sensorEvent.sensor.getType()) {
                    case 9:
                        int i2 = Float.compare(sensorEvent.values[1], -9.5f) >= 0 ? 0 : 1;
                        if (i2 != SarDeviceStateListener.mCurrentGravityYAxesState) {
                            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged gravityState state = " + i2);
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(18, i2);
                            SarDeviceStateListener.mCurrentGravityYAxesState = i2;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                        int i3 = (int) sensorEvent.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor1 distance = " + i3);
                        if (i3 != SarDeviceStateListener.mCurrentSarSensorState) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(3, i3);
                            SarDeviceStateListener.mCurrentSarSensorState = i3;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                        int i4 = (int) sensorEvent.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor2 distance = " + i4);
                        if (i4 != SarDeviceStateListener.mCurrentSarSensor2State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(9, i4);
                            SarDeviceStateListener.mCurrentSarSensor2State = i4;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_HALLSENSOR2 /* 33171068 */:
                        float f = sensorEvent.values[0];
                        if (((int) f) == 0) {
                            i = 1;
                        } else if (((int) f) != 1) {
                            i = 0;
                        }
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged hall2state = " + i);
                        if (i != SarDeviceStateListener.mCurrentHallSensor2State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(20, i);
                            SarDeviceStateListener.mCurrentHallSensor2State = i;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_HALLSENSOR /* 33171087 */:
                        float f2 = sensorEvent.values[0];
                        if (((int) f2) == 0) {
                            i = 1;
                        } else if (((int) f2) != 1) {
                            i = 0;
                        }
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged hallstate = " + i);
                        if (i != SarDeviceStateListener.mCurrentHallSensorState) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(4, i);
                            SarDeviceStateListener.mCurrentHallSensorState = i;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_SARSENSOR3 /* 33171109 */:
                        int i5 = (int) sensorEvent.values[0];
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged sensor3 distance = " + i5);
                        if (i5 != SarDeviceStateListener.mCurrentSarSensor3State) {
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(22, i5);
                            SarDeviceStateListener.mCurrentSarSensor3State = i5;
                        }
                        break;
                    case SarDeviceStateListener.SENSOR_TYPE_GPDSENSOR /* 33171120 */:
                        int i6 = (int) sensorEvent.values[0];
                        if (i6 != SarDeviceStateListener.mCurrentGPDSensorState) {
                            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged gpdState state = " + i6);
                            SarDeviceStateListener.notifyDeviceStateChangeEvent(17, i6);
                            SarDeviceStateListener.mCurrentGPDSensorState = i6;
                        }
                        break;
                    default:
                        ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onSensorChanged unknown event for sensor: " + sensorEvent.sensor.getType());
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
        public void onCallStateChanged(int i, String str) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onCallStateChanged: state = " + i);
            SarDeviceStateListener.this.refreshPhoneListener(32, i);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int i) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onDataActivity: direction = " + i);
            SarDeviceStateListener.this.refreshPhoneListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, i);
        }

        public void onRadioPowerStateChanged(int i) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "PhoneStateListener.onRadioPowerStateChanged: direction = " + i);
            SarDeviceStateListener.this.refreshPhoneListener(8388608, i);
        }
    };

    private static int convertEventTypeToFeatureType(int i) {
        if (i == 16) {
            return CLOUD_CONTROL_FEATURE_TYPE_SCAN_CODE;
        }
        if (i != 28) {
            return -1;
        }
        return CLOUD_CONTROL_FEATURE_TYPE_HORIZONTAL_GAME_LIST;
    }

    static {
        String[] strArr = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar"};
        FCCList = strArr;
        mFCCList = Arrays.asList(strArr);
        String[] strArr2 = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "sa", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "th", "ph", "kh", "pk", "my", "au", "sg", "hk", "tw", "bd", "np", "lk", "jp"};
        CEList = strArr2;
        mCEList = Arrays.asList(strArr2);
    }

    public static void registerDeviceStateChangeEvent(Context context, int i, Handler handler, int i2, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerDeviceStateChangeEvent, context = " + context + ", eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", handler = " + handler + ", what = " + i2 + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    RegistrantList registrantList = new RegistrantList();
                    registrantList.addUnique(handler, i2, obj);
                    mRegRequestInfoMap.put(Integer.valueOf(i), registrantList);
                } else {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).addUnique(handler, i2, obj);
                }
                startDeviceStateChangeListener(i, new Registrant(handler, i2, obj));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void unregisterDeviceStateChangeEvent(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    return;
                }
                ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).remove(handler);
                if (((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).size() == 0) {
                    stopDeviceStateChangeListener(i, handler);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int i, String str) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", stringValue = " + str);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) != null) {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).notifyRegistrants(new AsyncResult((Object) null, str.getBytes(StandardCharsets.UTF_8), (Throwable) null));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", intValue = " + i2);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) != null) {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).notifyRegistrants(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        sendMsgToHandler(200, i, i2);
    }

    private static void notifyDeviceStateChangeEvent(int i, Registrant registrant, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", r = " + registrant + ", intValue = " + i2);
        if (registrant == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
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
        if (context == null) {
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
            HandlerThread handlerThread = new HandlerThread("RadDeviceStateListenerBgThread");
            mDeviceStateHandlerThread = handlerThread;
            handlerThread.start();
            if (mDeviceStateHandler == null) {
                mDeviceStateHandler = new DeviceStateHandler(mDeviceStateHandlerThread.getLooper());
            }
        }
    }

    private void startBcIntentListen() {
        ModemSarLog.i(LOG_TAG, "startBcIntentListen");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction(BC_INTENT_ACTION_MODEM_SAR_STATE_CHANGE);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction(BC_INTENT_ACTION_SMART_TX_CHANGED);
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        if (!ModemSarUtils.isNtnSarSwitchOff()) {
            intentFilter.addAction(BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        }
        intentFilter.addAction(BC_INTENT_ACTION_CLOUD_CONTROL);
        intentFilter.addAction(ModemSarController.BC_INTENT_ACTION_DSI_VALUE_CHANGED);
        intentFilter.addAction(BC_INTENT_ACTION_HST_ENHANCEMENT_CHANGED);
        mContext.registerReceiver(this.mReceiver, intentFilter, 2);
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
        final Intent intent2 = new Intent(intent);
        Handler handler = mDeviceStateHandler;
        if (handler == null) {
            ModemSarLog.e(LOG_TAG, "dispatchBroadcastIntent, mDeviceStateHandler is null");
        } else {
            if (handler.post(new Runnable() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        SarDeviceStateListener.handleBroadcastIntent(intent2);
                    } catch (Exception e) {
                        ModemSarLog.e(SarDeviceStateListener.LOG_TAG, "handleBroadcastIntent happens Exception, action = " + intent2.getAction() + ", e = " + e);
                    }
                }
            })) {
                return;
            }
            ModemSarLog.e(LOG_TAG, "dispatchBroadcastIntent, post failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleBroadcastIntent(Intent intent) {
        String action = intent.getAction();
        int i = 0;
        if ("android.net.wifi.STATE_CHANGE".equals(action)) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (networkInfo == null) {
                ModemSarLog.e(LOG_TAG, "Wifi state changed, network info is null");
                return;
            }
            boolean zIsConnected = networkInfo.isConnected();
            if (zIsConnected != mCurrentWifiState) {
                ModemSarLog.d(LOG_TAG, "Wifi state changed, state = " + (zIsConnected ? 1 : 0));
                notifyDeviceStateChangeEvent(0, zIsConnected ? 1 : 0);
                mCurrentWifiState = zIsConnected ? 1 : 0;
                return;
            }
            return;
        }
        int i2 = 1;
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
            i = 13 == intent.getIntExtra("wifi_state", 0) ? 1 : 0;
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
            if (1 == intExtra) {
                i = 1;
            } else if (intExtra != 0) {
                i = -1;
            }
            if (i == mCurrentBluetoothPanState || i == -1) {
                return;
            }
            if (1 == i) {
                mCurrentBluetoothState = 1;
                ModemSarLog.d(LOG_TAG, "BlueTooth is enabled by blueTooth pan");
                notifyDeviceStateChangeEvent(8, 1);
            }
            ModemSarLog.d(LOG_TAG, "BlueTooth pan state changed, state = " + i);
            notifyDeviceStateChangeEvent(7, i);
            mCurrentBluetoothPanState = i;
            return;
        }
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
            int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            if (12 != intExtra2) {
                if (10 != intExtra2) {
                    i2 = -1;
                } else if (intent.getBooleanExtra("android.xiaomi.bluetooth.BLUETOOTH_RESTRICT_STATE", false)) {
                    ModemSarLog.d(LOG_TAG, "ignore bluetooth fake off state");
                } else {
                    i2 = 0;
                }
            }
            if (i2 == mCurrentBluetoothState || i2 == -1) {
                return;
            }
            ModemSarLog.d(LOG_TAG, "BlueTooth state changed, state = " + i2);
            notifyDeviceStateChangeEvent(8, i2);
            mCurrentBluetoothState = i2;
            if (i2 == 0) {
                mCurrentBluetoothPanState = 0;
                ModemSarLog.d(LOG_TAG, "BlueTooth pan is disabled by blueTooth");
                notifyDeviceStateChangeEvent(7, 0);
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
            int intExtra4 = intent.getIntExtra(a.d, 0);
            int intExtra5 = intent.getIntExtra("scale", 100);
            ModemSarLog.d(LOG_TAG, "Battery changed, usb plug = " + intExtra3);
            if ((intExtra3 == 1 || intExtra3 == 2) && mCurrentUsbState != 1) {
                notifyDeviceStateChangeEvent(11, 1);
                mCurrentUsbState = 1;
            }
            if (intExtra5 > 0) {
                int i3 = (intExtra4 * 100) / intExtra5;
                if (i3 != mCurrentBatteryLevelState) {
                    notifyDeviceStateChangeEvent(26, i3);
                    mCurrentBatteryLevelState = i3;
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
                    if (booleanExtra) {
                        return;
                    }
                    unregisterForNtnRadioStateChanged();
                    return;
                }
            }
            return;
        }
        if ("android.intent.action.SCREEN_OFF".equals(action)) {
            ModemSarLog.d(LOG_TAG, "SCREEN OFF");
            mCurrentScreenState = 0;
            notifyDeviceStateChangeEvent(24, 0);
            return;
        }
        if ("android.intent.action.SCREEN_ON".equals(action)) {
            ModemSarLog.d(LOG_TAG, "SCREEN ON");
            mCurrentScreenState = 1;
            notifyDeviceStateChangeEvent(24, 1);
            return;
        }
        if (BC_INTENT_ACTION_CLOUD_CONTROL.equals(action)) {
            int intExtra7 = intent.getIntExtra(BC_INTENT_EXTRA_FEATURE_TYPE, -1);
            int intExtra8 = intent.getIntExtra(BC_INTENT_EXTRA_CONTROL_CODE, -1);
            ModemSarLog.i(LOG_TAG, "Cloud control state changed, featureType = " + intExtra7 + ", controlCode = " + intExtra8);
            if (intExtra7 < 1000 || intExtra7 > CLOUD_CONTROL_FEATURE_TYPE_END) {
                return;
            }
            sendDelayedMsgToHandler(100, intExtra7, intExtra8, 10000);
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
            int intExtra10 = intent.getIntExtra("value", 0);
            ModemSarLog.d(LOG_TAG, "HST enhancement changed, hstEnhancement = " + intExtra10);
            if (intExtra10 != mCurrentHstEnhancementState) {
                mCurrentHstEnhancementState = intExtra10;
                notifyDeviceStateChangeEvent(30, intExtra10);
            }
        }
    }

    private static void sendMsgToHandler(int i, int i2) {
        if (mDeviceStateHandler != null) {
            byte[] bytes = ModemUtils.getBytes(i2);
            Handler handler = mDeviceStateHandler;
            handler.sendMessage(handler.obtainMessage(i, new AsyncResult((Object) null, bytes, (Throwable) null)));
        }
    }

    private static void sendMsgToHandler(int i, int i2, int i3) {
        if (mDeviceStateHandler != null) {
            byte[] bytes = ModemSarUtils.getBytes(i2, i3);
            Handler handler = mDeviceStateHandler;
            handler.sendMessage(handler.obtainMessage(i, new AsyncResult((Object) null, bytes, (Throwable) null)));
        }
    }

    private static void sendDelayedMsgToHandler(int i, int i2, int i3, int i4) {
        if (mDeviceStateHandler != null) {
            byte[] bytes = ModemSarUtils.getBytes(i2, i3);
            Handler handler = mDeviceStateHandler;
            handler.sendMessageDelayed(handler.obtainMessage(i, new AsyncResult((Object) null, bytes, (Throwable) null)), i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSimMcc() {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        SubscriptionManager subscriptionManager = mSubscriptionManager;
        if (subscriptionManager == null || mTelephonyManager == null || (activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList()) == null || activeSubscriptionInfoList.isEmpty()) {
            return ",";
        }
        int phoneCount = mTelephonyManager.getPhoneCount();
        String[] strArr = new String[phoneCount];
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            if (subscriptionInfo != null) {
                int mcc = subscriptionInfo.getMcc();
                int simSlotIndex = subscriptionInfo.getSimSlotIndex();
                String str = String.format("%03d", Integer.valueOf(mcc));
                if (str.length() == 3) {
                    strArr[simSlotIndex] = str;
                }
            }
        }
        for (int i = 0; i < phoneCount; i++) {
            if (strArr[i] == null) {
                strArr[i] = "";
            }
        }
        ModemSarLog.d(LOG_TAG, "simMcc = " + String.join(",", strArr));
        return String.join(",", strArr);
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
        WifiManager wifiManager = mWifiManager;
        if (wifiManager == null) {
            return 0;
        }
        int wifiApState = wifiManager.getWifiApState();
        ModemSarLog.d(LOG_TAG, "getWifiApState = " + wifiApState);
        return 13 == wifiApState ? 1 : 0;
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
                if (zIsEnabled) {
                    return;
                }
                mCurrentBluetoothPanState = 0;
                ModemSarLog.d(LOG_TAG, "checkBluetoothState, blueTooth is disabled, set blueTooth pan disabled");
                notifyDeviceStateChangeEvent(7, 0);
            }
        }
    }

    private static void checkBluetoothPanState() {
        ModemSarLog.i(LOG_TAG, "checkBluetoothPanState start");
        final BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            return;
        }
        ModemSarLog.i(LOG_TAG, "checkBluetoothPanState, start to getProfileProxy");
        defaultAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.3
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
                    defaultAdapter.closeProfileProxy(5, bluetoothProfile2);
                }
            }

            @Override // android.bluetooth.BluetoothProfile.ServiceListener
            public void onServiceDisconnected(int i) {
                ModemSarLog.i(SarDeviceStateListener.LOG_TAG, "checkBluetoothPanState onServiceDisconnected, profile = " + i);
            }
        }, 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:24:0x0079  */
    public static int getMccState(String str) {
        String str2 = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (str2.equals("")) {
            ModemSarLog.d(LOG_TAG, "country code change to " + str);
        } else {
            str = str2;
        }
        int i = 3;
        if (str.length() < 2) {
            ModemSarLog.d(LOG_TAG, "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] strArrSplit = str.split(",");
        int i2 = 0;
        for (int i3 = 0; i3 < strArrSplit.length; i3++) {
            ModemSarLog.d(LOG_TAG, "mCountryCode[" + i3 + "] = " + strArrSplit[i3]);
            String lowerCase = strArrSplit[i3].toLowerCase();
            strArrSplit[i3] = lowerCase;
            if (lowerCase != null && !lowerCase.isEmpty()) {
                if (!mFCCList.contains(strArrSplit[i3])) {
                    if (!mCEList.contains(strArrSplit[i3])) {
                        i2 = 3;
                        break;
                    }
                    if (i3 <= 0 || i2 == 1 || i2 == 0) {
                        i2 = 1;
                    } else {
                        i2 = 3;
                    }
                } else if (i3 <= 0 || i2 == 2 || i2 == 0) {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
            }
        }
        if (i2 == 0) {
            ModemSarLog.d(LOG_TAG, "Mcc state is 0, can't get network countryCode, use MCC_DEFAULT");
        } else {
            i = i2;
        }
        ModemSarLog.d(LOG_TAG, "mNetworkCountryCode: " + str + ", mccState: " + i);
        return i;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x002e  */
    private static boolean isSarSensorSubstanceInactive() {
        boolean z;
        if (mSarSensorSubstance1Group.contains(SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_NAME, "null"))) {
            ModemSarLog.d(LOG_TAG, "isSarSensorSubstanceInactive, device is in sarSensorSubstance1Group");
            SensorManager sensorManager = mSensorManager;
            if (sensorManager == null || mSarSensorSubstance1 != null) {
                z = false;
            } else {
                Sensor defaultSensor = sensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE1);
                mSarSensorSubstance1 = defaultSensor;
                if (defaultSensor == null) {
                    z = true;
                } else {
                    z = false;
                }
            }
        } else {
            z = false;
        }
        ModemSarLog.d(LOG_TAG, "isSarSensorSubstanceInactive, isInactive = " + z);
        return z;
    }

    class SarCountryCodeChangeListener implements WifiNl80211Manager.CountryCodeChangedListener {
        private SarCountryCodeChangeListener() {
        }

        public void onCountryCodeChanged(String str) {
            ModemSarLog.d(SarDeviceStateListener.LOG_TAG, "onCountryCodeChanged: " + str);
            int mccState = SarDeviceStateListener.getMccState(str);
            if (mccState != SarDeviceStateListener.mCurrentMccState) {
                SarDeviceStateListener.notifyDeviceStateChangeEvent(6, mccState);
                SarDeviceStateListener.mCurrentMccState = mccState;
            }
        }
    }

    private static void checkAudioReceiverState() {
        int i;
        String str = SystemProperties.get(PROPERTY_RECEIVER_STATUS, PROP_AUDIO_RECEIVER_STATE_OFF);
        if (str.equals(PROP_AUDIO_RECEIVER_STATE_ON)) {
            i = 1;
        } else {
            str.equals(PROP_AUDIO_RECEIVER_STATE_OFF);
            i = 0;
        }
        if (i != mCurrentAudioReceiverState) {
            ModemSarLog.d(LOG_TAG, "current audioReceiverState = " + i);
            notifyDeviceStateChangeEvent(5, i);
            mCurrentAudioReceiverState = i;
            sendAudioReceiverStateBroadcast(i);
        }
    }

    private static void sendAudioReceiverStateBroadcast(int i) {
        Intent intent = new Intent(BC_INTENT_ACTION_RECEIVER_STATE_CHANGED);
        intent.putExtra(MEModuleSignalT1s.EXTRA_STATE, i);
        mContext.sendBroadcast(intent);
    }

    private static final void startPollAudioReceiverStatus() {
        new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.6
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollReceiver();
            }
        }.start();
        mListenerStateMap.put(5, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void pollReceiver() {
        ModemSarLog.d(LOG_TAG, "Polling audio is running");
        int i = 0;
        while (!mIsLoopFinished) {
            String str = SystemProperties.get(PROPERTY_RECEIVER_STATUS, PROP_AUDIO_RECEIVER_STATE_OFF);
            if (str.equals(PROP_AUDIO_RECEIVER_STATE_ON)) {
                i = 1;
            } else if (str.equals(PROP_AUDIO_RECEIVER_STATE_OFF)) {
                i = 0;
            }
            if (i != mCurrentAudioReceiverState) {
                ModemSarLog.d(LOG_TAG, "audioReceiverState: " + i);
                notifyDeviceStateChangeEvent(5, i);
                mCurrentAudioReceiverState = i;
                sendAudioReceiverStateBroadcast(i);
            }
            SystemClock.sleep(1000L);
        }
        mListenerStateMap.put(5, 0);
        ModemSarLog.d(LOG_TAG, "Polling audio is stopped");
    }

    private static final void startUseCaseTest() {
        new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollUseCaseTest();
            }
        }.start();
        mListenerStateMap.put(12, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void pollUseCaseTest() {
        ModemSarLog.d(LOG_TAG, "Polling use case is running");
        while (!mIsLoopFinished) {
            int i = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (i != mCurrentUseCaseTest) {
                ModemSarLog.d(LOG_TAG, "usetest: " + i);
                notifyDeviceStateChangeEvent(12, i);
                mCurrentUseCaseTest = i;
            }
            SystemClock.sleep(1000L);
        }
        mListenerStateMap.put(12, 0);
        ModemSarLog.d(LOG_TAG, "Polling use case is stopped");
    }

    private static final void startPollCableStatus() {
        new Thread() { // from class: com.xiaomi.internal.telephony.SarDeviceStateListener.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SarDeviceStateListener.pollCableStatus();
            }
        }.start();
        mListenerStateMap.put(10, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void pollCableStatus() {
        if (new File(DEVICE_CABLE_FILE).exists()) {
            ModemSarLog.d(LOG_TAG, "enter pollCableStatus ...");
            new CableStatusObserver().startObserving("DEVPATH=/sys/bus/platform/devices/soc/soc:md_testing_mode");
        } else {
            ModemSarLog.d(LOG_TAG, "Cable file is not exist.");
            mListenerStateMap.put(10, 0);
        }
    }

    class CableStatusObserver extends UEventObserver {
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
    public void refreshPhoneListener(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "modem or radio power state change, messageType: " + i + ", state: " + i2);
        if (i == 128) {
            if (i2 == 1 || i2 == 2 || i2 == 3) {
                mDataStateActive = true;
            } else if (i2 == 0 || i2 == 4) {
                mDataStateActive = false;
            }
        } else if (i == 32) {
            if (i2 == 1 || i2 == 2) {
                mCallStateActive = true;
            } else if (i2 == 0) {
                mCallStateActive = false;
            }
        } else if (i == 8388608 && i2 == 1) {
            ModemSarLog.d(LOG_TAG, "refreshPhoneListener, radio power on");
            notifyDeviceStateChangeEvent(13, i2);
        }
        int i3 = (mDataStateActive || mCallStateActive) ? 1 : 0;
        if (i3 != mCurrentModemState) {
            notifyDeviceStateChangeEvent(2, i3);
            mCurrentModemState = i3;
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
        int i = mCurrentBatteryLevelState;
        if (i != -1) {
            notifyDeviceStateChangeEvent(26, i);
        }
    }

    private static void startDeviceStateChangeListener(int i, Registrant registrant) {
        ModemSarLog.d(LOG_TAG, "startDeviceStateChangeListener, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", r = " + registrant);
        synchronized (mListenerStateMap) {
            try {
                switch (i) {
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
                        ModemSarLog.d(LOG_TAG, "This listener is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                        break;
                    case 3:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(3, registrant, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null , set sensor1 valid");
                            } else {
                                Sensor defaultSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                                mSarSensor1 = defaultSensor;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor, 3)) {
                                    notifyDeviceStateChangeEvent(3, registrant, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor1 registration failed, set sensor1 valid");
                                } else {
                                    mListenerStateMap.put(3, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(3, registrant, mCurrentSarSensorState);
                        }
                        break;
                    case 4:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            SensorManager sensorManager = mSensorManager;
                            if (sensorManager == null) {
                                notifyDeviceStateChangeEvent(4, registrant, 0);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set hall abnomal");
                            } else {
                                Sensor defaultSensor2 = sensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, false);
                                mHallSensor = defaultSensor2;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor2, 3)) {
                                    notifyDeviceStateChangeEvent(4, registrant, 0);
                                    ModemSarLog.d(LOG_TAG, "hall sensor registration failed , set hall abnomal");
                                } else {
                                    mListenerStateMap.put(4, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HALL_SENSOR_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(4, registrant, mCurrentHallSensorState);
                        }
                        break;
                    case 5:
                        checkAudioReceiverState();
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            startPollAudioReceiverStatus();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_AUDIO_RECEIVER_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(5, registrant, mCurrentAudioReceiverState);
                        }
                        break;
                    case 6:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            if (mWifiCondManager == null) {
                                ModemSarLog.d(LOG_TAG, "mWifiCondManager is null, CountryCodeChangedListener valid");
                            } else {
                                mSarCountryCodeChangeListener = new SarCountryCodeChangeListener();
                                mWifiCondManager.registerCountryCodeChangedListener(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), mSarCountryCodeChangeListener);
                                mListenerStateMap.put(6, 1);
                                ModemSarLog.d(LOG_TAG, "TYPE_MCC_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_MCC_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(6, registrant, mCurrentMccState);
                        }
                        break;
                    case 7:
                        checkBluetoothPanState();
                        break;
                    case 8:
                        checkBluetoothState();
                        break;
                    case 9:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            if (mSarSensor2 != null) {
                                ModemSarLog.e(LOG_TAG, "sensor2 is not null");
                                notifyDeviceStateChangeEvent(9, registrant, mCurrentSarSensor2State);
                            } else if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(9, registrant, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set sensor2 valid");
                            } else {
                                Sensor defaultSensor3 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR2, true);
                                mSarSensor2 = defaultSensor3;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor3, 3)) {
                                    notifyDeviceStateChangeEvent(9, registrant, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor2 registration failed, set sensor2 valid");
                                } else {
                                    mListenerStateMap.put(9, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR2_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(9, registrant, mCurrentSarSensor2State);
                        }
                        break;
                    case 10:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            startPollCableStatus();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_CABLE_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                        }
                        break;
                    case 12:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            startUseCaseTest();
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_USE_CASE_TEST is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(12, registrant, mCurrentUseCaseTest);
                        }
                        break;
                    case 16:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            registerForWhiteListStateChanged();
                            mListenerStateMap.put(Integer.valueOf(i), 1);
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_WHITE_LIST_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            int i2 = mCurrentWhiteListStatus;
                            if (i2 != -1) {
                                notifyDeviceStateChangeEvent(16, registrant, i2);
                            }
                        }
                        break;
                    case 17:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            SensorManager sensorManager2 = mSensorManager;
                            if (sensorManager2 == null) {
                                notifyDeviceStateChangeEvent(17, -2);
                                ModemSarLog.e(LOG_TAG, "mSensorManager is null, set GPD big move");
                            } else {
                                Sensor defaultSensor4 = sensorManager2.getDefaultSensor(SENSOR_TYPE_GPDSENSOR, false);
                                mGPDSensor = defaultSensor4;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor4, 3)) {
                                    notifyDeviceStateChangeEvent(17, -2);
                                    ModemSarLog.e(LOG_TAG, "GPD sensor registration failed , set GPD big move");
                                } else {
                                    mListenerStateMap.put(17, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_GPD_SENSOR_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_GPD_SENSOR_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(17, registrant, mCurrentGPDSensorState);
                        }
                        break;
                    case 18:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            SensorManager sensorManager3 = mSensorManager;
                            if (sensorManager3 == null) {
                                notifyDeviceStateChangeEvent(18, registrant, 0);
                                ModemSarLog.e(LOG_TAG, "mSensorManager is null, set gravity horizontal");
                            } else {
                                Sensor defaultSensor5 = sensorManager3.getDefaultSensor(9, false);
                                mGravitySensor = defaultSensor5;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor5, 3)) {
                                    notifyDeviceStateChangeEvent(18, 0);
                                    ModemSarLog.e(LOG_TAG, "gravity sensor registration failed, set gravity horizontal");
                                } else {
                                    mListenerStateMap.put(18, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(18, registrant, mCurrentGravityYAxesState);
                        }
                        break;
                    case 20:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            SensorManager sensorManager4 = mSensorManager;
                            if (sensorManager4 == null) {
                                notifyDeviceStateChangeEvent(20, registrant, 0);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set hall2 abnomal");
                            } else {
                                Sensor defaultSensor6 = sensorManager4.getDefaultSensor(SENSOR_TYPE_HALLSENSOR2, false);
                                mHallSensor2 = defaultSensor6;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor6, 3)) {
                                    notifyDeviceStateChangeEvent(20, registrant, 0);
                                    ModemSarLog.e(LOG_TAG, "hall2 sensor registration failed , set hall abnomal");
                                } else {
                                    mListenerStateMap.put(20, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(20, registrant, mCurrentHallSensor2State);
                        }
                        break;
                    case 21:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            if (mSarSensor2 != null) {
                                ModemSarLog.e(LOG_TAG, "sensor2ForOneType is not null");
                                notifyDeviceStateChangeEvent(21, registrant, mCurrentSarSensor2State);
                            } else if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(21, registrant, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null, set sensor2 valid");
                            } else {
                                Sensor defaultSensor7 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                                mSarSensor2 = defaultSensor7;
                                if (!mSensorManager.registerListener(mSensorListenerForOneType, defaultSensor7, 3)) {
                                    notifyDeviceStateChangeEvent(21, registrant, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor2 registration failed, set sensor2 valid");
                                } else {
                                    mListenerStateMap.put(21, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR2_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(21, registrant, mCurrentSarSensor2State);
                        }
                        break;
                    case 22:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            if (mSensorManager == null || isSarSensorSubstanceInactive()) {
                                notifyDeviceStateChangeEvent(22, registrant, 1);
                                ModemSarLog.d(LOG_TAG, "mSensorManager is null , set sensor3 valid");
                            } else {
                                Sensor defaultSensor8 = mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR3, true);
                                mSarSensor3 = defaultSensor8;
                                if (!mSensorManager.registerListener(mSensorEventListener, defaultSensor8, 3)) {
                                    notifyDeviceStateChangeEvent(22, registrant, 1);
                                    ModemSarLog.e(LOG_TAG, "sensor1 registration failed, set sensor1 valid");
                                } else {
                                    mListenerStateMap.put(22, 1);
                                    ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                }
                            }
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_SENSOR_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            notifyDeviceStateChangeEvent(22, registrant, mCurrentSarSensorState);
                        }
                        break;
                    case 25:
                        registerForLandscapeStateChanged();
                        break;
                    case 26:
                        notifyBatteryStateChanged();
                        break;
                    case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            registerForHorizontalGameStateChanged();
                            mListenerStateMap.put(Integer.valueOf(i), 1);
                        } else {
                            ModemSarLog.i(LOG_TAG, "TYPE_HORIZONTAL_GAME_LIST_STATE is already running, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            int i3 = mCurrentHorizontalGameListState;
                            if (i3 != -1) {
                                notifyDeviceStateChangeEvent(28, registrant, i3);
                            }
                        }
                        break;
                    case 29:
                        int i4 = mCurrentDsiValueState;
                        if (i4 != -1) {
                            notifyDeviceStateChangeEvent(29, registrant, i4);
                        }
                        break;
                    case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                        int i5 = mCurrentHstEnhancementState;
                        if (i5 != -1) {
                            notifyDeviceStateChangeEvent(30, registrant, i5);
                        }
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void stopDeviceStateChangeListener(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", h = " + handler);
        if (mSensorManager == null) {
            ModemSarLog.e(LOG_TAG, "mSensorManager is null");
            return;
        }
        synchronized (mListenerStateMap) {
            try {
                if (i == 3) {
                    Sensor sensor = mSarSensor1;
                    if (sensor == null) {
                        ModemSarLog.e(LOG_TAG, "mSensor1 is null");
                    } else {
                        mSensorManager.unregisterListener(mSensorEventListener, sensor);
                        mSarSensor1 = null;
                        mListenerStateMap.put(Integer.valueOf(i), 0);
                        ModemSarLog.d(LOG_TAG, "TYPE_SENSOR_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                    }
                    mCurrentSarSensorState = -1;
                } else if (i == 4) {
                    Sensor sensor2 = mHallSensor;
                    if (sensor2 == null) {
                        ModemSarLog.e(LOG_TAG, "mHallSensor is null");
                    } else {
                        mSensorManager.unregisterListener(mSensorEventListener, sensor2);
                        mHallSensor = null;
                        mListenerStateMap.put(Integer.valueOf(i), 0);
                        ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                    }
                    mCurrentHallSensorState = -1;
                } else if (i == 9) {
                    Sensor sensor3 = mSarSensor2;
                    if (sensor3 == null) {
                        ModemSarLog.e(LOG_TAG, "mSensor2 is null");
                    } else {
                        mSensorManager.unregisterListener(mSensorEventListener, sensor3);
                        mSarSensor2 = null;
                        mListenerStateMap.put(Integer.valueOf(i), 0);
                        ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                    }
                    mCurrentSarSensor2State = -1;
                } else if (i == 25) {
                    unregisterForLandscapeStateChanged();
                    mCurrentLandScapeGameState = -1;
                } else if (i != 28) {
                    switch (i) {
                        case 16:
                            if (mListenerStateMap.get(Integer.valueOf(i)) != null && ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 1) {
                                unregisterForWhiteListStateChanged();
                                mListenerStateMap.put(Integer.valueOf(i), 0);
                            }
                            mCurrentWhiteListStatus = -1;
                            break;
                        case 17:
                            Sensor sensor4 = mGPDSensor;
                            if (sensor4 == null) {
                                ModemSarLog.e(LOG_TAG, "mGPDSensor is null");
                            } else {
                                mSensorManager.unregisterListener(mSensorEventListener, sensor4);
                                mGPDSensor = null;
                                mListenerStateMap.put(Integer.valueOf(i), 0);
                                ModemSarLog.d(LOG_TAG, "TYPE_GPD_SENSOR_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            }
                            mCurrentGPDSensorState = -1;
                            break;
                        case 18:
                            Sensor sensor5 = mGravitySensor;
                            if (sensor5 == null) {
                                ModemSarLog.e(LOG_TAG, "mGravitySensor is null");
                            } else {
                                mSensorManager.unregisterListener(mSensorEventListener, sensor5);
                                mGravitySensor = null;
                                mListenerStateMap.put(Integer.valueOf(i), 0);
                                ModemSarLog.d(LOG_TAG, "TYPE_GRAVITY_Y_AXES_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                            }
                            mCurrentGravityYAxesState = -1;
                            break;
                        default:
                            switch (i) {
                                case 20:
                                    Sensor sensor6 = mHallSensor2;
                                    if (sensor6 == null) {
                                        ModemSarLog.e(LOG_TAG, "mHallSensor2 is null");
                                    } else {
                                        mSensorManager.unregisterListener(mSensorEventListener, sensor6);
                                        mHallSensor2 = null;
                                        mListenerStateMap.put(Integer.valueOf(i), 0);
                                        ModemSarLog.d(LOG_TAG, "TYPE_HALL_SENSOR2_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                    }
                                    mCurrentHallSensor2State = -1;
                                    break;
                                case 21:
                                    Sensor sensor7 = mSarSensor2;
                                    if (sensor7 == null) {
                                        ModemSarLog.e(LOG_TAG, "mSensor2AForOneType is null");
                                    } else {
                                        mSensorManager.unregisterListener(mSensorListenerForOneType, sensor7);
                                        mSarSensor2 = null;
                                        mListenerStateMap.put(Integer.valueOf(i), 0);
                                        ModemSarLog.d(LOG_TAG, "TYPE_SENSOR2_USE_SENSOR1_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                    }
                                    mCurrentSarSensor2State = -1;
                                    break;
                                case 22:
                                    Sensor sensor8 = mSarSensor3;
                                    if (sensor8 == null) {
                                        ModemSarLog.e(LOG_TAG, "mSensor3 is null");
                                    } else {
                                        mSensorManager.unregisterListener(mSensorEventListener, sensor8);
                                        mSarSensor3 = null;
                                        mListenerStateMap.put(Integer.valueOf(i), 0);
                                        ModemSarLog.d(LOG_TAG, "TYPE_SENSOR3_STATE is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                    }
                                    mCurrentSarSensor3State = -1;
                                    break;
                                default:
                                    ModemSarLog.d(LOG_TAG, "This listener is stopped, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
                                    break;
                            }
                            break;
                    }
                } else {
                    if (mListenerStateMap.get(Integer.valueOf(i)) != null && ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 1) {
                        unregisterForHorizontalGameStateChanged();
                        mListenerStateMap.put(Integer.valueOf(i), 0);
                    }
                    mCurrentHorizontalGameListState = -1;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    class DeviceStateHandler extends Handler {
        private static final String TAG = "RadDeviceStateListener_Handler: ";

        public DeviceStateHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemSarLog.d(TAG, "handleMessage, msg = " + message.what);
            ByteBuffer buffer = ModemUtils.getBuffer((byte[]) ((AsyncResult) message.obj).result);
            int i = message.what;
            if (i == 16) {
                int i2 = buffer.getInt();
                ModemSarLog.d(TAG, "white list state = " + i2);
                if (i2 != SarDeviceStateListener.mCurrentWhiteListStatus) {
                    SarDeviceStateListener.mCurrentWhiteListStatus = i2;
                    SarDeviceStateListener.notifyDeviceStateChangeEvent(16, i2);
                    return;
                }
                return;
            }
            if (i == 19) {
                int i3 = buffer.getInt();
                ModemSarLog.d(TAG, "ntn radio power state = " + i3);
                if (1 == i3) {
                    SarDeviceStateListener.notifyDeviceStateChangeEvent(19, i3);
                    return;
                }
                return;
            }
            if (i == 25) {
                int i4 = buffer.getInt();
                ModemSarLog.d(TAG, "landscape game state = " + i4);
                if (i4 != SarDeviceStateListener.mCurrentLandScapeGameState) {
                    SarDeviceStateListener.mCurrentLandScapeGameState = i4;
                    SarDeviceStateListener.notifyDeviceStateChangeEvent(25, i4);
                    return;
                }
                return;
            }
            if (i != 100) {
                if (i == 200) {
                    SarDeviceStateListener.saveAndBroadcastDeviceStateChangeEvent(buffer.getInt(), buffer.getInt());
                    return;
                }
                if (i != 27) {
                    if (i != 28) {
                        return;
                    }
                    int i5 = buffer.getInt();
                    ModemSarLog.d(TAG, "horizontal game list state = " + i5);
                    if (i5 != SarDeviceStateListener.mCurrentHorizontalGameListState) {
                        SarDeviceStateListener.mCurrentHorizontalGameListState = i5;
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(28, i5);
                        return;
                    }
                    return;
                }
                int i6 = buffer.getInt();
                ModemSarLog.d(TAG, "sim mcc state = " + i6);
                if (i6 == 1 || i6 == 0) {
                    String simMcc = SarDeviceStateListener.getSimMcc();
                    if (simMcc.equals(SarDeviceStateListener.mCurrentSimMccState)) {
                        return;
                    }
                    SarDeviceStateListener.mCurrentSimMccState = simMcc;
                    SarDeviceStateListener.notifyDeviceStateChangeEvent(27, SarDeviceStateListener.mCurrentSimMccState);
                    ModemSarLog.d(TAG, "new sim mcc = " + SarDeviceStateListener.mCurrentSimMccState);
                    return;
                }
                return;
            }
            int i7 = buffer.getInt();
            int i8 = buffer.getInt();
            ModemSarLog.d(TAG, "featureType = " + i7 + ", controlCode = " + i8);
            if (i7 == SarDeviceStateListener.CLOUD_CONTROL_FEATURE_TYPE_HORIZONTAL_GAME_LIST) {
                if (i8 == 1) {
                    SarDeviceStateListener.setCloudControlState(28, 1);
                    SarDeviceStateListener.registerForHorizontalGameStateChanged();
                    return;
                } else {
                    if (i8 == 0) {
                        SarDeviceStateListener.unregisterForHorizontalGameStateChanged();
                        SarDeviceStateListener.setCloudControlState(28, 0);
                        SarDeviceStateListener.mCurrentHorizontalGameListState = 0;
                        SarDeviceStateListener.notifyDeviceStateChangeEvent(28, SarDeviceStateListener.mCurrentHorizontalGameListState);
                        return;
                    }
                    return;
                }
            }
            if (i7 == SarDeviceStateListener.CLOUD_CONTROL_FEATURE_TYPE_SCAN_CODE) {
                if (i8 == 1) {
                    SarDeviceStateListener.setCloudControlState(16, 1);
                    SarDeviceStateListener.registerForWhiteListStateChanged();
                } else if (i8 == 0) {
                    SarDeviceStateListener.unregisterForWhiteListStateChanged();
                    SarDeviceStateListener.setCloudControlState(16, 0);
                    SarDeviceStateListener.mCurrentWhiteListStatus = 0;
                    SarDeviceStateListener.notifyDeviceStateChangeEvent(16, SarDeviceStateListener.mCurrentWhiteListStatus);
                }
            }
        }
    }

    private static boolean isFeatureSupported(int i) {
        boolean z = getCloudControlState(i) != 0;
        ModemSarLog.i(LOG_TAG, "isFeatureSupported, isSupported = " + z + ", eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i));
        return z;
    }

    private static int getCloudControlState(int i) {
        int i2;
        int iIntValue;
        int iConvertEventTypeToFeatureType = convertEventTypeToFeatureType(i);
        if (iConvertEventTypeToFeatureType < 1000 || iConvertEventTypeToFeatureType > CLOUD_CONTROL_FEATURE_TYPE_END) {
            return -1;
        }
        if (mCloudControlStateMap.get(Integer.valueOf(i)) != null && ((iIntValue = ((Integer) mCloudControlStateMap.get(Integer.valueOf(i))).intValue()) == 1 || iIntValue == 0)) {
            ModemSarLog.i(LOG_TAG, "getCloudControlState from map, featureType = " + iConvertEventTypeToFeatureType + ", result = " + iIntValue);
            return iIntValue;
        }
        StringBuilder sb = new StringBuilder(PROPERTY_CLOUD_CONTROL_PREFIX);
        sb.append(iConvertEventTypeToFeatureType - 1000);
        try {
            i2 = Integer.parseInt(SystemProperties.get(sb.toString(), "-1"));
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "getCloudControlState happens Exception, featureType = " + iConvertEventTypeToFeatureType);
            e.printStackTrace();
            i2 = -1;
        }
        if (i2 != -1) {
            mCloudControlStateMap.put(Integer.valueOf(i), Integer.valueOf(i2));
        }
        ModemSarLog.i(LOG_TAG, "getCloudControlState from prop, featureType = " + iConvertEventTypeToFeatureType + ", result = " + i2);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCloudControlState(int i, int i2) {
        int iConvertEventTypeToFeatureType = convertEventTypeToFeatureType(i);
        if (iConvertEventTypeToFeatureType < 1000 || iConvertEventTypeToFeatureType > CLOUD_CONTROL_FEATURE_TYPE_END) {
            return;
        }
        if (i2 == 1 || i2 == 0) {
            ModemSarLog.i(LOG_TAG, "setCloudControlState, featureType = " + iConvertEventTypeToFeatureType + ", controlCode = " + i2);
            mCloudControlStateMap.put(Integer.valueOf(i), Integer.valueOf(i2));
            StringBuilder sb = new StringBuilder(PROPERTY_CLOUD_CONTROL_PREFIX);
            sb.append(iConvertEventTypeToFeatureType + (-1000));
            try {
                SystemProperties.set(sb.toString(), "" + i2);
            } catch (Exception e) {
                ModemSarLog.e(LOG_TAG, "setCloudControlState happens Exception, featureType = " + iConvertEventTypeToFeatureType + ", controlCode = " + i2);
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveAndBroadcastDeviceStateChangeEvent(int i, int i2) {
        try {
            saveDeviceStateChangeEvent(i, i2);
            broadcastDeviceStateChangeEvent(i, i2);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "saveAndBroadcastDeviceStateChangeEvent happens Exception, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", intValue = " + i2);
            e.printStackTrace();
        }
    }

    private static void saveDeviceStateChangeEvent(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "saveDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", intValue = " + i2);
        String deviceEventTypeString = DeviceStateListenerBase.getDeviceEventTypeString(i);
        if (deviceEventTypeString == null) {
            ModemSarLog.d(LOG_TAG, "Not find eventType, saveDeviceStateChangeEvent do nothing, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", intValue = " + i2);
            return;
        }
        String str = PROPERTY_DEVICE_STATE_PREFIX + deviceEventTypeString.toLowerCase();
        try {
            SystemProperties.set(str, "" + i2);
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "saveDeviceStateChangeEvent happen exception, prop = ".concat(str));
            e.printStackTrace();
        }
    }

    private static void broadcastDeviceStateChangeEvent(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "broadcastDeviceStateChangeEvent, eventType = " + DeviceStateListenerBase.getDeviceEventTypeStringEx(i) + ", intValue = " + i2);
        Intent intent = new Intent(BC_INTENT_ACTION_DEVICE_STATE_CHANED);
        intent.putExtra(BC_INTENT_EXTRA_DEVICE_STATE_TYPE, i);
        intent.putExtra(BC_INTENT_EXTRA_DEVICE_STATE_VAL, i2);
        mContext.sendBroadcast(intent);
    }
}
