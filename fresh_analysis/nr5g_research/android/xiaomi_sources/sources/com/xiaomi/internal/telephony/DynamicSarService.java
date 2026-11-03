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
import com.xiaomi.modem.ModemUtils;
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
    private Sensor mSensor;
    private Sensor mSensor2Substance;
    private boolean mSensorEnable;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstance;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;
    private String sensor1;
    private String sensor2;
    private String sensor3;
    private String sensor4;
    private String sensor5;
    public static String EXTRA_WIFI_DATA_STATE = "android.net.wifi.extra.WIFI_DATA_STATE";
    private static final String[] CEList = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "ph", "kh", "pk", "my", "au", "sg", "hk", "bd", "np", "lk", "jp"};
    private static final String[] FCCList = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar", "tw"};
    private static final List<String> mCEList = Arrays.asList(CEList);
    private static final List<String> mFCCList = Arrays.asList(FCCList);
    private final String TAG = "DynamicService";
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
    private int mWiFiDataSarState = 0;
    private int mSenorDetectTime = 3;
    private ArrayList<SarControllerClient> mWifiListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mHotspotListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mModemListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mSarSensorListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mSarSensor2Listeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mCableListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mHallSensorListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mAudioReceiverListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mMccListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mBluetoothPanListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mBluetoothListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mCardStateListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mRadioPowerListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mUseCaseTestListeners = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.DynamicSarService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int hotspotState;
            int wifiState;
            String action = intent.getAction();
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (info.isConnected()) {
                    wifiState = 1;
                } else {
                    wifiState = 0;
                    if (info.getState() == NetworkInfo.State.DISCONNECTED) {
                        DynamicSarService.this.mWiFiDataSarState = 0;
                    }
                }
                boolean isNeedSarByWifiData = false;
                if (wifiState == 1 && ((DynamicSarService.this.mWiFiDataSarState & ModemUtils.MODEM_STATS_DATA_INVALID) == 269 || (DynamicSarService.this.mWiFiDataSarState & ModemUtils.MODEM_STATS_DATA_INVALID) == 142 || (DynamicSarService.this.mWiFiDataSarState & ModemUtils.MODEM_STATS_DATA_INVALID) == 15)) {
                    isNeedSarByWifiData = true;
                }
                Log.d("DynamicService", "wifiState:" + wifiState + ", lastWifiState:" + DynamicSarService.this.mCurrentWifiState);
                Log.d("DynamicService", "recordData: " + DynamicSarService.this.mWiFiDataSarState + ", need do? = " + isNeedSarByWifiData);
                if (SystemProperties.get("ro.product.mod_device", "").endsWith("_global")) {
                    if (wifiState != DynamicSarService.this.mCurrentWifiState) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mWifiListeners, 0, wifiState);
                        DynamicSarService.this.mCurrentWifiState = wifiState;
                        return;
                    }
                    return;
                }
                if (wifiState == 1) {
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mWifiListeners, 0, 0);
                }
                if (SystemProperties.get(DynamicSarService.PROPERTY_WLAN_DISABLE_CHINA_SAR, "").equals("true")) {
                    return;
                }
                if (((isNeedSarByWifiData && wifiState == 1) || wifiState == 0) && wifiState != DynamicSarService.this.mCurrentWifiState) {
                    DynamicSarService.this.mWiFiDataSarState = 0;
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mWifiListeners, 0, wifiState);
                }
                DynamicSarService.this.mCurrentWifiState = wifiState;
                return;
            }
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                if (intent.getIntExtra("wifi_state", 0) == 13) {
                    hotspotState = 1;
                } else {
                    hotspotState = 0;
                }
                if (hotspotState != DynamicSarService.this.mCurrentHotspotState) {
                    Log.d("DynamicService", "WIFI HOTSPOT STATE CHANGED");
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mHotspotListeners, 1, hotspotState);
                    DynamicSarService.this.mCurrentHotspotState = hotspotState;
                    DynamicSarService.this.OnSenesorStateChange();
                    return;
                }
                return;
            }
            if ("android.bluetooth.action.STATE_CHANGED".equals(action)) {
                int state = intent.getIntExtra("android.bluetooth.extra.LOCAL_STATE", -1);
                int bluetoothPanState = -1;
                if (state == 1) {
                    bluetoothPanState = 1;
                } else if (state == 0) {
                    bluetoothPanState = 0;
                }
                if (bluetoothPanState != DynamicSarService.this.mCurrentBluetoothPanState && bluetoothPanState != -1) {
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mBluetoothPanListeners, 7, bluetoothPanState);
                    DynamicSarService.this.mCurrentBluetoothPanState = bluetoothPanState;
                    DynamicSarService.this.OnSenesorStateChange();
                    return;
                }
                return;
            }
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int state2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                int bluetoothState = -1;
                if (state2 == 12) {
                    bluetoothState = 1;
                } else if (state2 == 10) {
                    bluetoothState = 0;
                    if (intent.getBooleanExtra("android.xiaomi.bluetooth.BLUETOOTH_RESTRICT_STATE", false)) {
                        bluetoothState = 1;
                        Log.d("DynamicService", "ignore bluetooth fake off state");
                    }
                }
                if (bluetoothState != DynamicSarService.this.mCurrentBluetoothState && bluetoothState != -1) {
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mBluetoothListeners, 8, bluetoothState);
                    DynamicSarService.this.mCurrentBluetoothState = bluetoothState;
                    return;
                }
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String iccState = intent.getStringExtra("ss");
                Log.d("DynamicService", "iccState = " + iccState);
                if ("LOADED".equals(iccState)) {
                    Log.d("DynamicService", "iccState is loaded ");
                    DynamicSarService.this.callChangeListeners(DynamicSarService.this.mCardStateListeners, 14, 0);
                    return;
                }
                return;
            }
            if (DynamicSarService.ACTION_MODEM_SAR_STATE_CHANGE.equals(action)) {
                DynamicSarService.this.mModemSarEnable = Integer.parseInt(intent.getStringExtra(DynamicSarService.MODEM_SAR_STATE));
                Log.d("DynamicService", "set sar state " + DynamicSarService.this.mModemSarEnable);
            } else {
                if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(action)) {
                    int mccState = DynamicSarService.this.getMccState();
                    if (mccState != DynamicSarService.this.mCurrentMccState) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mMccListeners, 6, mccState);
                        DynamicSarService.this.mCurrentMccState = mccState;
                        return;
                    }
                    return;
                }
                if (DynamicSarService.ACTION_WIFI_DATA_DISTINGUISH.equals(action)) {
                    int state3 = intent.getIntExtra(DynamicSarService.EXTRA_WIFI_DATA_STATE, 0);
                    DynamicSarService.this.mWiFiDataSarState |= state3;
                    Log.d("DynamicService", "WIFI_DATA_DISTINGUISH_ACTION: extra data =" + state3 + ", mWiFiDataSarState: " + DynamicSarService.this.mWiFiDataSarState);
                }
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.DynamicSarService.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case DynamicSarService.SENSOR_TYPE_HALLSENSOR /* 33171002 */:
                    int hallState = 0;
                    if (event.values[0] == 0.0f) {
                        hallState = 0;
                    } else if (event.values[0] == 1.0f) {
                        hallState = 1;
                    }
                    Log.d("DynamicService", "onSensorChanged hallstate = " + hallState);
                    if (hallState != DynamicSarService.this.mCurrentHallSensorState) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mHallSensorListeners, 4, hallState);
                        DynamicSarService.this.mCurrentHallSensorState = hallState;
                    }
                    break;
                case DynamicSarService.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                    int sarState = (int) event.values[0];
                    Log.d("DynamicService", "onSensorChanged distance = " + sarState);
                    if (sarState != DynamicSarService.this.mCurrentSarSensorState) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mSarSensorListeners, 3, sarState);
                        DynamicSarService.this.mCurrentSarSensorState = sarState;
                        DynamicSarService.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarService.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                    int sar2State = (int) event.values[0];
                    Log.d("DynamicService", "onSensorChanged sensor2 distance = " + sar2State);
                    if (sar2State != DynamicSarService.this.mCurrentSarSensor2State) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mSarSensor2Listeners, 9, sar2State);
                        DynamicSarService.this.mCurrentSarSensor2State = sar2State;
                        DynamicSarService.this.OnSenesorStateChange();
                    }
                    break;
                default:
                    Log.d("DynamicService", "onSensorChanged unknown event for sensor: " + event.sensor.getType());
                    break;
            }
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.DynamicSarService.5
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("DynamicService", "PhoneStateListener.onCallStateChanged: state=" + state);
            DynamicSarService.this.refreshSensorListener(32, state);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            Log.d("DynamicService", "PhoneStateListener.onDataActivity: direction=" + direction);
            DynamicSarService.this.refreshSensorListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, direction);
        }

        public void onRadioPowerStateChanged(int direction) {
            Log.d("DynamicService", "PhoneStateListener.onRadioPowerStateChanged: direction=" + direction);
            DynamicSarService.this.refreshSensorListener(8388608, direction);
        }
    };

    interface SarControllerClient {
        void onStateChanged(int i, int i2);
    }

    private DynamicSarService(Context context) {
        this.mSensorEnable = true;
        Log.d("DynamicService", "DynamicSarService init...");
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction(ACTION_WIFI_DATA_DISTINGUISH);
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction("android.bluetooth.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction(ACTION_MODEM_SAR_STATE_CHANGE);
        filter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorManager == null) {
            Log.d("DynamicService", "obtain sensor service failed ");
            this.mSensorEnable = false;
        } else {
            this.mHallSensor = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, true);
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mHallSensor, 3);
            do {
                this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                if (this.mSensorSubstance == null) {
                    Log.d("DynamicService", "mSensorSubstance is null!");
                }
                boolean mSensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstance, 3);
                Log.d("DynamicService", "mSensorFlag = " + mSensorFlag);
                if (this.mSensorSubstance == null || !mSensorFlag) {
                    this.mSensorEnable = false;
                } else {
                    this.mSensorEnable = true;
                }
                if (this.mSensorEnable) {
                    this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstance);
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
        if (this.mWifiManager == null) {
            return 0;
        }
        int wifiApState = this.mWifiManager.getWifiApState();
        Log.d("DynamicService", "wifiApState = " + wifiApState);
        if (wifiApState == 13) {
            return 1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMccState() {
        int mMccState = 0;
        String mNetworkCountryCode = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (mNetworkCountryCode.length() < 2) {
            Log.d("DynamicService", "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] mCountryCode = mNetworkCountryCode.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        for (int i = 0; i < mCountryCode.length; i++) {
            Log.d("DynamicService", "mCountryCode[" + i + "] = " + mCountryCode[i]);
            mCountryCode[i] = mCountryCode[i].toLowerCase();
            if (mCountryCode[i] != null && !mCountryCode[i].isEmpty()) {
                if (mFCCList.contains(mCountryCode[i])) {
                    if (i > 0) {
                        if (mMccState == 2 || mMccState == 0) {
                            mMccState = 2;
                        } else {
                            mMccState = 3;
                        }
                    } else {
                        mMccState = 2;
                    }
                } else if (mCEList.contains(mCountryCode[i])) {
                    if (i > 0) {
                        if (mMccState == 1 || mMccState == 0) {
                            mMccState = 1;
                        } else {
                            mMccState = 3;
                        }
                    } else {
                        mMccState = 1;
                    }
                } else {
                    mMccState = 3;
                    break;
                }
            }
        }
        if (mMccState == 0) {
            Log.d("DynamicService", "mMccState is 0,can't get network countryCode, use MCC_DEFAULT");
            mMccState = 3;
        }
        Log.d("DynamicService", "mNetworkCountryCode: " + mNetworkCountryCode + ", mMccState: " + mMccState);
        return mMccState;
    }

    private final void startPollAudioReceiverStatus() {
        Thread ReceiverPollThread = new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService.this.pollReceiver();
            }
        };
        ReceiverPollThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollReceiver() {
        int state = 0;
        while (true) {
            String AudioReceiverState = SystemProperties.get(PROPERTY_RECEIVER_STATUS);
            if (AudioReceiverState.equals("on")) {
                state = 1;
            } else if (AudioReceiverState.equals("off")) {
                state = 0;
            }
            if (state != this.mCurrentAudioReceiverState) {
                Log.d("DynamicService", "AudioReceiverState: " + state);
                callChangeListeners(this.mAudioReceiverListeners, 5, state);
                this.mCurrentAudioReceiverState = state;
                OnSenesorStateChange();
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startPollUseCaseTest() {
        Thread TestPollThread = new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarService.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService.this.pollTest();
            }
        };
        TestPollThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollTest() {
        int mHistory = 0;
        while (true) {
            int mTestPropValue = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (mHistory != mTestPropValue) {
                Log.d("DynamicService", "mTestPropValue: " + mTestPropValue);
                callChangeListeners(this.mUseCaseTestListeners, 12, mTestPropValue);
                mHistory = mTestPropValue;
            }
            SystemClock.sleep(1000L);
        }
    }

    private void startPhoneListener() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 8388768);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSensorListener(int messageType, int state) {
        Log.d("DynamicService", "modem or radio power state change,messageType: " + messageType + " state: " + state);
        if (messageType == 128) {
            if (state == 1 || state == 2 || state == 3) {
                this.mDataStateActive = true;
            } else if (state == 0 || state == 4) {
                this.mDataStateActive = false;
            }
        } else if (messageType == 32) {
            if (state == 1 || state == 2) {
                this.mCallStateActive = true;
            } else if (state == 0) {
                this.mCallStateActive = false;
            }
        } else if (messageType == 8388608 && state == 1) {
            Log.d("DynamicService", "RADIO_POWER_ON ");
            callChangeListeners(this.mRadioPowerListeners, 13, state);
        }
        int modemState = (this.mDataStateActive || this.mCallStateActive) ? 1 : 0;
        if (modemState != this.mCurrentModemState) {
            callChangeListeners(this.mModemListeners, 2, modemState);
            this.mCurrentModemState = modemState;
        }
    }

    private boolean sarRegExceHandle(int sensorType) {
        if (this.mSensorManager == null) {
            return false;
        }
        Sensor sensor = this.mSensorManager.getDefaultSensor(sensorType, true);
        boolean sensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, sensor, 3);
        return sensorFlag;
    }

    void registerStateChangeListener(int type, SarControllerClient listener) {
        switch (type) {
            case 0:
                this.mWifiListeners.add(listener);
                break;
            case 1:
                this.mHotspotListeners.add(listener);
                checkWifiApStatus();
                break;
            case 2:
                this.mModemListeners.add(listener);
                break;
            case 3:
                this.mSarSensorListeners.add(listener);
                if (this.mSensorEnable) {
                    boolean retSensor1 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR);
                    if (!retSensor1) {
                        callChangeListeners(this.mSarSensorListeners, 3, 3);
                        Log.d("DynamicService", "retSensor1 is abnormal, set default value 3");
                    }
                } else {
                    callChangeListeners(this.mSarSensorListeners, 3, 3);
                    Log.d("DynamicService", "mSensorEnable is abnormal, set sensor1 default value 3");
                }
                break;
            case 4:
                this.mHallSensorListeners.add(listener);
                break;
            case 5:
                this.mAudioReceiverListeners.add(listener);
                break;
            case 6:
                this.mMccListeners.add(listener);
                break;
            case 7:
                this.mBluetoothPanListeners.add(listener);
                break;
            case 8:
                this.mBluetoothListeners.add(listener);
                break;
            case 9:
                this.mSarSensor2Listeners.add(listener);
                if (this.mSensorEnable) {
                    boolean retSensor2 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR2);
                    if (!retSensor2) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                        Log.d("DynamicService", "retSensor2 is abnormal, set default value 3");
                    }
                } else {
                    callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                    Log.d("DynamicService", "mSensorEnable is abnormal, set sensor2 default value 3");
                }
                break;
            case 10:
                this.mCableListeners.add(listener);
                break;
            case 12:
                this.mUseCaseTestListeners.add(listener);
                break;
            case 13:
                this.mRadioPowerListeners.add(listener);
                break;
            case 14:
                this.mCardStateListeners.add(listener);
                break;
        }
    }

    void callChangeListeners(ArrayList<SarControllerClient> listeners, int type, int value) {
        if (listeners.size() == 0 || this.mModemSarEnable != 0) {
            Log.d("DynamicService", "listeners is null or mModemSarEnable is disable!!!");
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onStateChanged(type, value);
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
        String SendToCit = this.sensor1 + this.sensor2 + this.sensor3 + this.sensor4 + this.sensor5;
        Log.d("DynamicService", "SendToCit  value = " + SendToCit);
        SetSensorValues(SendToCit);
    }

    private void SetSensorValues(String mCurrentSensorArray) {
        Intent intent = new Intent(SET_SENSOR_VALUES_INTENT);
        intent.putExtra(SET_SENSOR_VALUES_STATE, mCurrentSensorArray);
        this.mContext.sendBroadcast(intent);
    }
}
