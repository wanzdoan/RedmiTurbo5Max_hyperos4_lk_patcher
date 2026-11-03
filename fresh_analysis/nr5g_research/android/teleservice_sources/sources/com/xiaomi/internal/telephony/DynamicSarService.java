package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DynamicSarService {
    private static final String ACTION_MODEM_SAR_STATE_CHANGE = "com.android.phone.modemsar.state";
    public static final String ACTION_WIFI_DATA_DISTINGUISH = "android.net.wifi.action.WIFI_DATA_DISTINGUISH";
    public static final int AUDIO_RECEIVER_STATE_OFF = 0;
    public static final int AUDIO_RECEIVER_STATE_ON = 1;
    public static final int BLUETOOTH_PAN_STATE_OFF = 0;
    public static final int BLUETOOTH_PAN_STATE_ON = 1;
    public static final int BLUETOOTH_STATE_OFF = 0;
    public static final int BLUETOOTH_STATE_ON = 1;
    private static final String CABLE_DEVICE_PATH = "/sys/devices/platform/odm/odm:testing-mode/";
    private static final String CABLE_DEVICE_PATH_L11A = "/sys/devices/platform/soc/soc:odm/soc:odm:testing-mode/";
    private static final String[] CEList;
    private static final String DEVICE_TYPE_K10 = "ares";
    private static final String DEVICE_TYPE_K10A = "chopin";
    private static final String DEVICE_TYPE_K16 = "pissarro";
    private static final String DEVICE_TYPE_L11 = "matisse";
    private static final String DEVICE_TYPE_L11A = "rubens";
    private static final String DEVICE_TYPE_L12A = "plato";
    private static final String DEVICE_TYPE_M12 = "corot";
    private static final String DEVICE_TYPE_M12A = "aristotle";
    private static final String DEVICE_TYPE_M12A_KDDI = "XIG04";
    private static final String DEVICE_TYPE_M9 = "yuechu";
    private static final String DEVICE_TYPE_N11A = "duchamp";
    private static final String DEVICE_TYPE_N12 = "rothko";
    private static final String DEVICE_TYPE_N12A = "degas";
    public static String EXTRA_WIFI_DATA_STATE = "android.net.wifi.extra.WIFI_DATA_STATE";
    private static final String[] FCCList;
    public static final int HALL_STATE_CLOSE = 0;
    public static final int HALL_STATE_OPEN = 2;
    public static final int HOTSPOT_STATE_OFF = 0;
    public static final int HOTSPOT_STATE_ON = 1;
    private static final String INTENT_EXTRA_AIRPLANE_STATE = "state";
    public static final int MCC_CE = 1;
    public static final int MCC_DEFAULT = 3;
    public static final int MCC_FCC = 2;
    private static final String MODEM_SAR_STATE = "modem_sar_state";
    public static final int MODEM_STATE_ACTIVE = 1;
    public static final int MODEM_STATE_INACTIVE = 0;
    private static final String PROPERTY_OPERATOR_ISO_COUNTRY = "gsm.operator.iso-country";
    private static final String PROPERTY_PRODUCT_MOD_DEVICE = "ro.product.mod_device";
    private static final String PROPERTY_RECEIVER_STATUS = "vendor.audio.voice.receiver.status";
    private static final String PROPERTY_USECASE_TEST = "persist.vendor.radio.usecase.test";
    private static final String PROPERTY_WLAN_DISABLE_CHINA_SAR = "persist.vendor.wlan.disable.chinasar";
    public static final int SAR_DISTANCE_LONG = 0;
    public static final int SAR_DISTANCE_MID = 1;
    public static final int SAR_DISTANCE_SHORT = 1;
    private static final int SENSOR_TYPE_HALLSENSOR = 33171002;
    private static final int SENSOR_TYPE_SARSENSOR = 33171028;
    private static final int SENSOR_TYPE_SARSENSOR2 = 33171059;
    private static final int SENSOR_TYPE_SARSENSOR2_SUBSTANCE = 33171016;
    private static final int SENSOR_TYPE_SARSENSOR_SUBSTANCE = 33171015;
    private static final String SET_SENSOR_VALUES_INTENT = "com.android.set.sensor.values";
    private static final String SET_SENSOR_VALUES_INTENT_ZORE = "com.android.set.sensor.values.zero";
    private static final String SET_SENSOR_VALUES_STATE = "sensor_values_set";
    private static final String SET_SENSOR_VALUES_STATE_ZORE = "sensor_values_set_zero";
    public static final int TYPE_AUDIO_RECEIVER_STATE = 5;
    public static final int TYPE_BLUETOOTH_PAN_STATE = 7;
    public static final int TYPE_BLUETOOTH_STATE = 8;
    public static final int TYPE_CABLE_STATE = 10;
    public static final int TYPE_HALL_SENSOR_STATE = 4;
    public static final int TYPE_HOTSPOT_STATE = 1;
    public static final int TYPE_MCC_STATE = 6;
    public static final int TYPE_MODEM_STATE = 2;
    public static final int TYPE_RADIOPOWER_STATE = 13;
    public static final int TYPE_SAR_SENSOR2_STATE = 9;
    public static final int TYPE_SAR_SENSOR_STATE = 3;
    public static final int TYPE_SIMCARD_STATE = 14;
    public static final int TYPE_USB_STATE = 11;
    public static final int TYPE_USE_CASE_TEST = 12;
    public static final int TYPE_WIFI_STATE = 0;
    public static final int WIFI_DATA_STATE_AP_MAC_ADDR = 2;
    public static final int WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY = 128;
    public static final int WIFI_DATA_STATE_AP_VENDOR_IE_NUM_ONE = 1;
    public static final int WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER = 256;
    public static final int WIFI_DATA_STATE_DHCP_HOSTNAME = 8;
    public static final int WIFI_DATA_STATE_DHCP_PADDING_STA = 16;
    public static final int WIFI_DATA_STATE_DHCP_VENDOR = 4;
    public static final int WIFI_DATA_STATE_EXTRA_APP_STATE = 64;
    public static final int WIFI_DATA_STATE_TCP_STATE = 32;
    public static final int WIFI_STATE_CONNECTED = 1;
    public static final int WIFI_STATE_DISCONNECTED = 0;
    private static final List mCEList;
    private static final List mFCCList;
    private static DynamicSarService mInstance;
    private boolean mCallStateActive;
    private Context mContext;
    private int mCurrentAudioReceiverState;
    private int mCurrentHallSensorState;
    private int mCurrentHotspotState;
    private int mCurrentModemState;
    private int mCurrentSarSensor2State;
    private int mCurrentSarSensorState;
    private String mCurrentSensorArray;
    private int mCurrentWifiState;
    private boolean mDataStateActive;
    private Sensor mHallSensor;
    private final PhoneStateListener mPhoneStateListener;
    private Sensor mSensor;
    private Sensor mSensor2Substance;
    private boolean mSensorEnable;
    private final SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstance;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;
    private String sensor1;
    private String sensor2;
    private String sensor3;
    private String sensor4;
    private String sensor5;
    private final String TAG = "DynamicService";
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
    private int mWiFiDataSarState = 0;
    private int mSenorDetectTime = 3;
    private ArrayList mWifiListeners = new ArrayList();
    private ArrayList mHotspotListeners = new ArrayList();
    private ArrayList mModemListeners = new ArrayList();
    private ArrayList mSarSensorListeners = new ArrayList();
    private ArrayList mSarSensor2Listeners = new ArrayList();
    private ArrayList mCableListeners = new ArrayList();
    private ArrayList mHallSensorListeners = new ArrayList();
    private ArrayList mAudioReceiverListeners = new ArrayList();
    private ArrayList mMccListeners = new ArrayList();
    private ArrayList mBluetoothPanListeners = new ArrayList();
    private ArrayList mBluetoothListeners = new ArrayList();
    private ArrayList mCardStateListeners = new ArrayList();
    private ArrayList mRadioPowerListeners = new ArrayList();
    private ArrayList mUseCaseTestListeners = new ArrayList();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.DynamicSarService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int i;
            String action = intent.getAction();
            int i2 = 1;
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo.isConnected()) {
                    i = 1;
                } else {
                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        DynamicSarService.this.mWiFiDataSarState = 0;
                    }
                    i = 0;
                }
                boolean z = i == 1 && ((DynamicSarService.this.mWiFiDataSarState & 65535) == 269 || (DynamicSarService.this.mWiFiDataSarState & 65535) == 142 || (DynamicSarService.this.mWiFiDataSarState & 65535) == 15);
                Log.d("DynamicService", "wifiState:" + i + ", lastWifiState:" + DynamicSarService.this.mCurrentWifiState);
                Log.d("DynamicService", "recordData: " + DynamicSarService.this.mWiFiDataSarState + ", need do? = " + z);
                if (SystemProperties.get("ro.product.mod_device", "").endsWith("_global")) {
                    if (i != DynamicSarService.this.mCurrentWifiState) {
                        DynamicSarService dynamicSarService = DynamicSarService.this;
                        dynamicSarService.callChangeListeners(dynamicSarService.mWifiListeners, 0, i);
                        DynamicSarService.this.mCurrentWifiState = i;
                        return;
                    }
                    return;
                }
                if (i == 1) {
                    DynamicSarService dynamicSarService2 = DynamicSarService.this;
                    dynamicSarService2.callChangeListeners(dynamicSarService2.mWifiListeners, 0, 0);
                }
                if (SystemProperties.get(DynamicSarService.PROPERTY_WLAN_DISABLE_CHINA_SAR, "").equals("true")) {
                    return;
                }
                if (((z && i == 1) || i == 0) && i != DynamicSarService.this.mCurrentWifiState) {
                    DynamicSarService.this.mWiFiDataSarState = 0;
                    DynamicSarService dynamicSarService3 = DynamicSarService.this;
                    dynamicSarService3.callChangeListeners(dynamicSarService3.mWifiListeners, 0, i);
                }
                DynamicSarService.this.mCurrentWifiState = i;
                return;
            }
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                int i3 = intent.getIntExtra("wifi_state", 0) == 13 ? 1 : 0;
                if (i3 != DynamicSarService.this.mCurrentHotspotState) {
                    Log.d("DynamicService", "WIFI HOTSPOT STATE CHANGED");
                    DynamicSarService dynamicSarService4 = DynamicSarService.this;
                    dynamicSarService4.callChangeListeners(dynamicSarService4.mHotspotListeners, 1, i3);
                    DynamicSarService.this.mCurrentHotspotState = i3;
                    DynamicSarService.this.OnSenesorStateChange();
                    return;
                }
                return;
            }
            if ("android.bluetooth.action.STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("android.bluetooth.extra.LOCAL_STATE", -1);
                if (intExtra != 1) {
                    i2 = intExtra == 0 ? 0 : -1;
                }
                if (i2 == DynamicSarService.this.mCurrentBluetoothPanState || i2 == -1) {
                    return;
                }
                DynamicSarService dynamicSarService5 = DynamicSarService.this;
                dynamicSarService5.callChangeListeners(dynamicSarService5.mBluetoothPanListeners, 7, i2);
                DynamicSarService.this.mCurrentBluetoothPanState = i2;
                DynamicSarService.this.OnSenesorStateChange();
                return;
            }
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                if (intExtra2 != 12) {
                    if (intExtra2 != 10) {
                        i2 = -1;
                    } else if (intent.getBooleanExtra("android.xiaomi.bluetooth.BLUETOOTH_RESTRICT_STATE", false)) {
                        Log.d("DynamicService", "ignore bluetooth fake off state");
                    } else {
                        i2 = 0;
                    }
                }
                if (i2 == DynamicSarService.this.mCurrentBluetoothState || i2 == -1) {
                    return;
                }
                DynamicSarService dynamicSarService6 = DynamicSarService.this;
                dynamicSarService6.callChangeListeners(dynamicSarService6.mBluetoothListeners, 8, i2);
                DynamicSarService.this.mCurrentBluetoothState = i2;
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                Log.d("DynamicService", "iccState = " + stringExtra);
                if ("LOADED".equals(stringExtra)) {
                    Log.d("DynamicService", "iccState is loaded ");
                    DynamicSarService dynamicSarService7 = DynamicSarService.this;
                    dynamicSarService7.callChangeListeners(dynamicSarService7.mCardStateListeners, 14, 0);
                    return;
                }
                return;
            }
            if (DynamicSarService.ACTION_MODEM_SAR_STATE_CHANGE.equals(action)) {
                DynamicSarService.this.mModemSarEnable = Integer.parseInt(intent.getStringExtra(DynamicSarService.MODEM_SAR_STATE));
                Log.d("DynamicService", "set sar state " + DynamicSarService.this.mModemSarEnable);
                return;
            }
            if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(action)) {
                int mccState = DynamicSarService.this.getMccState();
                if (mccState != DynamicSarService.this.mCurrentMccState) {
                    DynamicSarService dynamicSarService8 = DynamicSarService.this;
                    dynamicSarService8.callChangeListeners(dynamicSarService8.mMccListeners, 6, mccState);
                    DynamicSarService.this.mCurrentMccState = mccState;
                    return;
                }
                return;
            }
            if (DynamicSarService.ACTION_WIFI_DATA_DISTINGUISH.equals(action)) {
                int intExtra3 = intent.getIntExtra(DynamicSarService.EXTRA_WIFI_DATA_STATE, 0);
                DynamicSarService.this.mWiFiDataSarState |= intExtra3;
                Log.d("DynamicService", "WIFI_DATA_DISTINGUISH_ACTION: extra data =" + intExtra3 + ", mWiFiDataSarState: " + DynamicSarService.this.mWiFiDataSarState);
            }
        }
    };

    interface SarControllerClient {
        void onStateChanged(int i, int i2);
    }

    static {
        String[] strArr = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "ph", "kh", "pk", "my", "au", "sg", "hk", "bd", "np", "lk", "jp"};
        CEList = strArr;
        String[] strArr2 = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar", "tw"};
        FCCList = strArr2;
        mCEList = Arrays.asList(strArr);
        mFCCList = Arrays.asList(strArr2);
    }

    private DynamicSarService(Context context) {
        this.mSensorEnable = true;
        SensorEventListener sensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.DynamicSarService.2
            @Override // android.hardware.SensorEventListener
            public final void onAccuracyChanged(Sensor sensor, int i) {
            }

            @Override // android.hardware.SensorEventListener
            public final void onSensorChanged(SensorEvent sensorEvent) {
                int type = sensorEvent.sensor.getType();
                int i = 0;
                if (type == DynamicSarService.SENSOR_TYPE_HALLSENSOR) {
                    float f = sensorEvent.values[0];
                    if (f != 0.0f && f == 1.0f) {
                        i = 1;
                    }
                    Log.d("DynamicService", "onSensorChanged hallstate = " + i);
                    if (i != DynamicSarService.this.mCurrentHallSensorState) {
                        DynamicSarService dynamicSarService = DynamicSarService.this;
                        dynamicSarService.callChangeListeners(dynamicSarService.mHallSensorListeners, 4, i);
                        DynamicSarService.this.mCurrentHallSensorState = i;
                        return;
                    }
                    return;
                }
                if (type == DynamicSarService.SENSOR_TYPE_SARSENSOR) {
                    int i2 = (int) sensorEvent.values[0];
                    Log.d("DynamicService", "onSensorChanged distance = " + i2);
                    if (i2 != DynamicSarService.this.mCurrentSarSensorState) {
                        DynamicSarService dynamicSarService2 = DynamicSarService.this;
                        dynamicSarService2.callChangeListeners(dynamicSarService2.mSarSensorListeners, 3, i2);
                        DynamicSarService.this.mCurrentSarSensorState = i2;
                        DynamicSarService.this.OnSenesorStateChange();
                        return;
                    }
                    return;
                }
                if (type == DynamicSarService.SENSOR_TYPE_SARSENSOR2) {
                    int i3 = (int) sensorEvent.values[0];
                    Log.d("DynamicService", "onSensorChanged sensor2 distance = " + i3);
                    if (i3 != DynamicSarService.this.mCurrentSarSensor2State) {
                        DynamicSarService dynamicSarService3 = DynamicSarService.this;
                        dynamicSarService3.callChangeListeners(dynamicSarService3.mSarSensor2Listeners, 9, i3);
                        DynamicSarService.this.mCurrentSarSensor2State = i3;
                        DynamicSarService.this.OnSenesorStateChange();
                        return;
                    }
                    return;
                }
                Log.d("DynamicService", "onSensorChanged unknown event for sensor: " + sensorEvent.sensor.getType());
            }
        };
        this.mSensorEventListener = sensorEventListener;
        this.mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.DynamicSarService.5
            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int i, String str) {
                Log.d("DynamicService", "PhoneStateListener.onCallStateChanged: state=" + i);
                DynamicSarService.this.refreshSensorListener(32, i);
            }

            @Override // android.telephony.PhoneStateListener
            public void onDataActivity(int i) {
                Log.d("DynamicService", "PhoneStateListener.onDataActivity: direction=" + i);
                DynamicSarService.this.refreshSensorListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, i);
            }

            public void onRadioPowerStateChanged(int i) {
                Log.d("DynamicService", "PhoneStateListener.onRadioPowerStateChanged: direction=" + i);
                DynamicSarService.this.refreshSensorListener(8388608, i);
            }
        };
        Log.d("DynamicService", "DynamicSarService init...");
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction(ACTION_WIFI_DATA_DISTINGUISH);
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction(ACTION_MODEM_SAR_STATE_CHANGE);
        intentFilter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        if (sensorManager == null) {
            Log.d("DynamicService", "obtain sensor service failed ");
            this.mSensorEnable = false;
        } else {
            Sensor defaultSensor = sensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, true);
            this.mHallSensor = defaultSensor;
            this.mSensorManager.registerListener(sensorEventListener, defaultSensor, 3);
            do {
                Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                this.mSensorSubstance = defaultSensor2;
                if (defaultSensor2 == null) {
                    Log.d("DynamicService", "mSensorSubstance is null!");
                }
                boolean zRegisterListener = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstance, 3);
                Log.d("DynamicService", "mSensorFlag = " + zRegisterListener);
                Sensor sensor = this.mSensorSubstance;
                if (sensor == null || !zRegisterListener) {
                    this.mSensorEnable = false;
                } else {
                    this.mSensorEnable = true;
                }
                if (this.mSensorEnable) {
                    this.mSensorManager.unregisterListener(this.mSensorEventListener, sensor);
                    Log.d("DynamicService", "substance sensor register successfully, unregister immediately!!!");
                } else {
                    SystemClock.sleep(1000L);
                    this.mSenorDetectTime--;
                    Log.d("DynamicService", "mSenorDetectTime = " + this.mSenorDetectTime);
                }
                if (this.mSenorDetectTime <= 0) {
                    break;
                }
            } while (!this.mSensorEnable);
        }
        Log.d("DynamicService", "mSensorEnable = " + this.mSensorEnable);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        startPhoneListener();
        startPollAudioReceiverStatus();
        startPollUseCaseTest();
        Log.d("DynamicService", "DynamicSarService init done");
    }

    public static DynamicSarService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DynamicSarService(context);
        }
        return mInstance;
    }

    private void checkWifiApStatus() {
        int wifiApState = getWifiApState();
        if (wifiApState != this.mCurrentHotspotState) {
            callChangeListeners(this.mHotspotListeners, 1, wifiApState);
            this.mCurrentHotspotState = wifiApState;
            OnSenesorStateChange();
        }
    }

    private int getWifiApState() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return 0;
        }
        int wifiApState = wifiManager.getWifiApState();
        Log.d("DynamicService", "wifiApState = " + wifiApState);
        return wifiApState == 13 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x0060  */
    public int getMccState() {
        String str = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        int i = 3;
        if (str.length() < 2) {
            Log.d("DynamicService", "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] strArrSplit = str.split(",");
        int i2 = 0;
        for (int i3 = 0; i3 < strArrSplit.length; i3++) {
            Log.d("DynamicService", "mCountryCode[" + i3 + "] = " + strArrSplit[i3]);
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
            Log.d("DynamicService", "mMccState is 0,can't get network countryCode, use MCC_DEFAULT");
        } else {
            i = i2;
        }
        Log.d("DynamicService", "mNetworkCountryCode: " + str + ", mMccState: " + i);
        return i;
    }

    private final void startPollAudioReceiverStatus() {
        new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService.this.pollReceiver();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollReceiver() {
        int i = 0;
        while (true) {
            String str = SystemProperties.get(PROPERTY_RECEIVER_STATUS);
            if (str.equals(SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON)) {
                i = 1;
            } else if (str.equals(SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF)) {
                i = 0;
            }
            if (i != this.mCurrentAudioReceiverState) {
                Log.d("DynamicService", "AudioReceiverState: " + i);
                callChangeListeners(this.mAudioReceiverListeners, 5, i);
                this.mCurrentAudioReceiverState = i;
                OnSenesorStateChange();
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startPollUseCaseTest() {
        new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarService.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService.this.pollTest();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollTest() {
        int i = 0;
        while (true) {
            int i2 = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (i != i2) {
                Log.d("DynamicService", "mTestPropValue: " + i2);
                callChangeListeners(this.mUseCaseTestListeners, 12, i2);
                i = i2;
            }
            SystemClock.sleep(1000L);
        }
    }

    private void startPhoneListener() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 8388768);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSensorListener(int i, int i2) {
        Log.d("DynamicService", "modem or radio power state change,messageType: " + i + " state: " + i2);
        if (i == 128) {
            if (i2 == 1 || i2 == 2 || i2 == 3) {
                this.mDataStateActive = true;
            } else if (i2 == 0 || i2 == 4) {
                this.mDataStateActive = false;
            }
        } else if (i == 32) {
            if (i2 == 1 || i2 == 2) {
                this.mCallStateActive = true;
            } else if (i2 == 0) {
                this.mCallStateActive = false;
            }
        } else if (i == 8388608 && i2 == 1) {
            Log.d("DynamicService", "RADIO_POWER_ON ");
            callChangeListeners(this.mRadioPowerListeners, 13, i2);
        }
        int i3 = (this.mDataStateActive || this.mCallStateActive) ? 1 : 0;
        if (i3 != this.mCurrentModemState) {
            callChangeListeners(this.mModemListeners, 2, i3);
            this.mCurrentModemState = i3;
        }
    }

    private boolean sarRegExceHandle(int i) {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            return false;
        }
        return this.mSensorManager.registerListener(this.mSensorEventListener, sensorManager.getDefaultSensor(i, true), 3);
    }

    void registerStateChangeListener(int i, SarControllerClient sarControllerClient) {
        switch (i) {
            case 0:
                this.mWifiListeners.add(sarControllerClient);
                break;
            case 1:
                this.mHotspotListeners.add(sarControllerClient);
                checkWifiApStatus();
                break;
            case 2:
                this.mModemListeners.add(sarControllerClient);
                break;
            case 3:
                this.mSarSensorListeners.add(sarControllerClient);
                if (this.mSensorEnable) {
                    if (!sarRegExceHandle(SENSOR_TYPE_SARSENSOR)) {
                        callChangeListeners(this.mSarSensorListeners, 3, 3);
                        Log.d("DynamicService", "retSensor1 is abnormal, set default value 3");
                    }
                } else {
                    callChangeListeners(this.mSarSensorListeners, 3, 3);
                    Log.d("DynamicService", "mSensorEnable is abnormal, set sensor1 default value 3");
                }
                break;
            case 4:
                this.mHallSensorListeners.add(sarControllerClient);
                break;
            case 5:
                this.mAudioReceiverListeners.add(sarControllerClient);
                break;
            case 6:
                this.mMccListeners.add(sarControllerClient);
                break;
            case 7:
                this.mBluetoothPanListeners.add(sarControllerClient);
                break;
            case 8:
                this.mBluetoothListeners.add(sarControllerClient);
                break;
            case 9:
                this.mSarSensor2Listeners.add(sarControllerClient);
                if (this.mSensorEnable) {
                    if (!sarRegExceHandle(SENSOR_TYPE_SARSENSOR2)) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                        Log.d("DynamicService", "retSensor2 is abnormal, set default value 3");
                    }
                } else {
                    callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                    Log.d("DynamicService", "mSensorEnable is abnormal, set sensor2 default value 3");
                }
                break;
            case 10:
                this.mCableListeners.add(sarControllerClient);
                break;
            case 12:
                this.mUseCaseTestListeners.add(sarControllerClient);
                break;
            case 13:
                this.mRadioPowerListeners.add(sarControllerClient);
                break;
            case 14:
                this.mCardStateListeners.add(sarControllerClient);
                break;
        }
    }

    void callChangeListeners(ArrayList arrayList, int i, int i2) {
        if (arrayList.size() == 0 || this.mModemSarEnable != 0) {
            Log.d("DynamicService", "listeners is null or mModemSarEnable is disable!!!");
            return;
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            ((SarControllerClient) arrayList.get(i3)).onStateChanged(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void OnSenesorStateChange() {
        SystemClock.sleep(100L);
        this.sensor1 = Integer.toString(this.mCurrentAudioReceiverState);
        int i = 1;
        if (this.mCurrentHotspotState != 1 && this.mCurrentBluetoothPanState != 1) {
            i = 0;
        }
        this.sensor2 = Integer.toString(i);
        this.sensor3 = Integer.toString(this.mCurrentSarSensorState);
        this.sensor4 = Integer.toString(this.mCurrentSarSensor2State);
        this.sensor5 = Integer.toString(this.mCableGpioStatus);
        String str = this.sensor1 + this.sensor2 + this.sensor3 + this.sensor4 + this.sensor5;
        Log.d("DynamicService", "SendToCit  value = ".concat(str));
        SetSensorValues(str);
    }

    private void SetSensorValues(String str) {
        Intent intent = new Intent(SET_SENSOR_VALUES_INTENT);
        intent.putExtra(SET_SENSOR_VALUES_STATE, str);
        this.mContext.sendBroadcast(intent);
    }
}
