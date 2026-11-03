package com.mediatek.internal.telephony;

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
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class DynamicSarService_M11R {
    private static final String ACTION_MODEM_SAR_STATE_CHANGE = "com.android.phone.modemsar.state";
    public static final int AUDIO_RECEIVER_STATE_OFF = 0;
    public static final int AUDIO_RECEIVER_STATE_ON = 1;
    public static final int BLUETOOTH_PAN_STATE_OFF = 0;
    public static final int BLUETOOTH_PAN_STATE_ON = 1;
    public static final int BLUETOOTH_STATE_OFF = 0;
    public static final int BLUETOOTH_STATE_ON = 1;
    private static final String CABLE_DEVICE_PATH = "/sys/devices/platform/odm/odm:testing-mode/";
    private static final String CABLE_DEVICE_PATH_M11R = "/sys/devices/platform/soc/soc:odm/soc:odm:testing-mode/";
    private static final String DEVICE_TYPE_K10 = "ares";
    private static final String DEVICE_TYPE_K10A = "chopin";
    private static final String DEVICE_TYPE_K16 = "pissarro";
    private static final String DEVICE_TYPE_L11 = "matisse";
    private static final String DEVICE_TYPE_L11A = "rubens";
    private static final String DEVICE_TYPE_L12A = "plato";
    private static final String DEVICE_TYPE_M11R = "rembrandt";
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
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_OPERATOR_ISO_COUNTRY = "gsm.operator.iso-country";
    private static final String PROPERTY_RECEIVER_STATUS = "vendor.audio.voice.receiver.status";
    private static final String PROPERTY_USECASE_TEST = "persist.vendor.radio.usecase.test";
    public static final int SAR_DISTANCE_LONG = 0;
    public static final int SAR_DISTANCE_MID = 1;
    public static final int SAR_DISTANCE_SHORT = 1;
    private static final int SENSOR_TYPE_HALLSENSOR = 33171002;
    private static final int SENSOR_TYPE_SARSENSOR = 33171028;
    private static final int SENSOR_TYPE_SARSENSOR2 = 33171059;
    private static final int SENSOR_TYPE_SARSENSOR2_SUBSTANCE = 33171016;
    private static final int SENSOR_TYPE_SARSENSOR_SUBSTANCE = 33171015;
    private static final String SET_SENSOR_VALUES_INTENT_ZORE = "com.android.set.sensor.values.zero";
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
    public static final int WIFI_STATE_CONNECTED = 1;
    public static final int WIFI_STATE_DISCONNECTED = 0;
    private static DynamicSarService_M11R mInstance;
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
    private String mDevName;
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
    private static final String[] CEList = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "sa", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "th", "ph", "kh", "pk", "my", "au", "sg", "hk", "tw", "bd", "np", "lk", "jp"};
    private static final String[] FCCList = {"co", "uy", "py", "cl", "bo", "pe", "kr", "in", "us", "ar"};
    private static final List<String> mCEList = Arrays.asList(CEList);
    private static final List<String> mFCCList = Arrays.asList(FCCList);
    private final String TAG = "DynamicSarService_M11R";
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
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
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int mccState;
            int hotspotState;
            int wifiState;
            String action = intent.getAction();
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (info.isConnected()) {
                    wifiState = 1;
                } else {
                    wifiState = 0;
                }
                if (wifiState != DynamicSarService_M11R.this.mCurrentWifiState) {
                    DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mWifiListeners, 0, wifiState);
                    DynamicSarService_M11R.this.mCurrentWifiState = wifiState;
                    return;
                }
                return;
            }
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                if (intent.getIntExtra("wifi_state", 0) == 13) {
                    hotspotState = 1;
                } else {
                    hotspotState = 0;
                }
                if (hotspotState != DynamicSarService_M11R.this.mCurrentHotspotState) {
                    Log.d("DynamicSarService_M11R", "WIFI HOTSPOT STATE CHANGED");
                    DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mHotspotListeners, 1, hotspotState);
                    DynamicSarService_M11R.this.mCurrentHotspotState = hotspotState;
                    DynamicSarService_M11R.this.OnSenesorStateChange();
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
                if (bluetoothPanState != DynamicSarService_M11R.this.mCurrentBluetoothPanState && bluetoothPanState != -1) {
                    DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mBluetoothPanListeners, 7, bluetoothPanState);
                    DynamicSarService_M11R.this.mCurrentBluetoothPanState = bluetoothPanState;
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
                        Log.d("DynamicSarService_M11R", "ignore bluetooth fake off state");
                    }
                }
                if (bluetoothState != DynamicSarService_M11R.this.mCurrentBluetoothState && bluetoothState != -1) {
                    DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mBluetoothListeners, 8, bluetoothState);
                    DynamicSarService_M11R.this.mCurrentBluetoothState = bluetoothState;
                    return;
                }
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String iccState = intent.getStringExtra("ss");
                Log.d("DynamicSarService_M11R", "iccState = " + iccState);
                if ("LOADED".equals(iccState)) {
                    Log.d("DynamicSarService_M11R", "iccState is loaded ");
                    DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mCardStateListeners, 14, 0);
                    return;
                }
                return;
            }
            if (DynamicSarService_M11R.ACTION_MODEM_SAR_STATE_CHANGE.equals(action)) {
                DynamicSarService_M11R.this.mModemSarEnable = Integer.parseInt(intent.getStringExtra(DynamicSarService_M11R.MODEM_SAR_STATE));
                Log.d("DynamicSarService_M11R", "set sar state " + DynamicSarService_M11R.this.mModemSarEnable);
            } else if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(action) && (mccState = DynamicSarService_M11R.this.getMccState()) != DynamicSarService_M11R.this.mCurrentMccState) {
                DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mMccListeners, 6, mccState);
                DynamicSarService_M11R.this.mCurrentMccState = mccState;
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case DynamicSarService_M11R.SENSOR_TYPE_HALLSENSOR /* 33171002 */:
                    int hallState = 0;
                    if (event.values[0] == 0.0f) {
                        hallState = 0;
                    } else if (event.values[0] == 1.0f) {
                        hallState = 1;
                    }
                    Log.d("DynamicSarService_M11R", "onSensorChanged hallstate = " + hallState);
                    if (hallState != DynamicSarService_M11R.this.mCurrentHallSensorState) {
                        DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mHallSensorListeners, 4, hallState);
                        DynamicSarService_M11R.this.mCurrentHallSensorState = hallState;
                    }
                    break;
                case DynamicSarService_M11R.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                    int sarState = (int) event.values[0];
                    Log.d("DynamicSarService_M11R", "onSensorChanged distance = " + sarState);
                    if (sarState != DynamicSarService_M11R.this.mCurrentSarSensorState) {
                        DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mSarSensorListeners, 3, sarState);
                        DynamicSarService_M11R.this.mCurrentSarSensorState = sarState;
                        DynamicSarService_M11R.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarService_M11R.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                    int sar2State = (int) event.values[0];
                    Log.d("DynamicSarService_M11R", "onSensorChanged sensor2 distance = " + sar2State);
                    if (sar2State != DynamicSarService_M11R.this.mCurrentSarSensor2State) {
                        DynamicSarService_M11R.this.callChangeListeners(DynamicSarService_M11R.this.mSarSensor2Listeners, 9, sar2State);
                        DynamicSarService_M11R.this.mCurrentSarSensor2State = sar2State;
                        DynamicSarService_M11R.this.OnSenesorStateChange();
                    }
                    break;
                default:
                    Log.d("DynamicSarService_M11R", "onSensorChanged unknown event for sensor: " + event.sensor.getType());
                    break;
            }
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.6
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("DynamicSarService_M11R", "PhoneStateListener.onCallStateChanged: state=" + state);
            DynamicSarService_M11R.this.refreshSensorListener(32, state);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            Log.d("DynamicSarService_M11R", "PhoneStateListener.onDataActivity: direction=" + direction);
            DynamicSarService_M11R.this.refreshSensorListener(128, direction);
        }

        public void onRadioPowerStateChanged(int direction) {
            Log.d("DynamicSarService_M11R", "PhoneStateListener.onRadioPowerStateChanged: direction=" + direction);
            DynamicSarService_M11R.this.refreshSensorListener(8388608, direction);
        }
    };

    interface SarControllerClient {
        void onStateChanged(int i, int i2);
    }

    private DynamicSarService_M11R(Context context) {
        long j;
        boolean z = false;
        this.mSensorEnable = true;
        Log.d("DynamicSarService_M11R", "DynamicSarService_M11R init...");
        this.mContext = context;
        this.mDevName = SystemProperties.get(PROPERTY_DEVICE_NAME);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
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
            Log.d("DynamicSarService_M11R", "obtain sensor service failed ");
            this.mSensorEnable = false;
        } else {
            this.mHallSensor = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, true);
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mHallSensor, 3);
            if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_L12A)) {
                do {
                    this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                    if (this.mSensorSubstance == null) {
                        Log.d("DynamicSarService_M11R", "mSensorSubstance is null!");
                    }
                    j = 1000;
                    boolean mSensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstance, 3);
                    Log.d("DynamicSarService_M11R", "mSensorFlag = " + mSensorFlag);
                    if (this.mSensorSubstance == null || !mSensorFlag) {
                        this.mSensorEnable = false;
                    } else {
                        this.mSensorEnable = true;
                    }
                    if (this.mSensorEnable) {
                        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstance);
                        Log.d("DynamicSarService_M11R", "substance sensor register successfully, unregister immediately!!!");
                    } else {
                        SystemClock.sleep(1000L);
                        this.mSenorDetectTime--;
                        Log.d("DynamicSarService_M11R", "mSenorDetectTime = " + this.mSenorDetectTime);
                    }
                    if (this.mSenorDetectTime <= 0) {
                        break;
                    }
                } while (!this.mSensorEnable);
            } else {
                j = 1000;
            }
            if (this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11)) {
                while (true) {
                    this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                    this.mSensor2Substance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR2_SUBSTANCE);
                    if (this.mSensorSubstance == null) {
                        Log.d("DynamicSarService_M11R", "mSensorSubstance is null!");
                    }
                    if (this.mSensor2Substance == null) {
                        Log.d("DynamicSarService_M11R", "mSensor2Substance is null!");
                    }
                    boolean mSensorFlag2 = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstance, 3);
                    Log.d("DynamicSarService_M11R", "mSensorFlag = " + mSensorFlag2);
                    boolean mSensor2Flag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor2Substance, 3);
                    Log.d("DynamicSarService_M11R", "mSensor2Flag = " + mSensor2Flag);
                    if (this.mSensorSubstance == null || !mSensorFlag2 || this.mSensor2Substance == null || !mSensor2Flag) {
                        this.mSensorEnable = z;
                    } else {
                        this.mSensorEnable = true;
                    }
                    if (this.mSensorEnable) {
                        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstance);
                        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensor2Substance);
                        Log.d("DynamicSarService_M11R", "substance sensor register successfully, unregister immediately!!!");
                    } else {
                        SystemClock.sleep(j);
                        this.mSenorDetectTime--;
                        Log.d("DynamicSarService_M11R", "mSenorDetectTime = " + this.mSenorDetectTime);
                    }
                    if (this.mSenorDetectTime <= 0 || this.mSensorEnable) {
                        break;
                    } else {
                        z = false;
                    }
                }
            }
        }
        Log.d("DynamicSarService_M11R", "mSensorEnable = " + this.mSensorEnable);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        startPhoneListener();
        startPollAudioReceiverStatus();
        startPollUseCaseTest();
        Log.d("DynamicSarService_M11R", "DynamicSarService_M11R init done");
    }

    public static DynamicSarService_M11R getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DynamicSarService_M11R(context);
        }
        return mInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMccState() {
        int mMccState = 0;
        String mNetworkCountryCode = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (mNetworkCountryCode.length() < 2) {
            Log.d("DynamicSarService_M11R", "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] mCountryCode = mNetworkCountryCode.split(",");
        for (int i = 0; i < mCountryCode.length; i++) {
            Log.d("DynamicSarService_M11R", "mCountryCode[" + i + "] = " + mCountryCode[i]);
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
            Log.d("DynamicSarService_M11R", "mMccState is 0,can't get network countryCode, use MCC_DEFAULT");
            mMccState = 3;
        }
        Log.d("DynamicSarService_M11R", "mNetworkCountryCode: " + mNetworkCountryCode + ", mMccState: " + mMccState);
        return mMccState;
    }

    private final void startPollAudioReceiverStatus() {
        Thread ReceiverPollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService_M11R.this.pollReceiver();
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
                Log.d("DynamicSarService_M11R", "AudioReceiverState: " + state);
                callChangeListeners(this.mAudioReceiverListeners, 5, state);
                this.mCurrentAudioReceiverState = state;
                OnSenesorStateChange();
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startPollCableStatus() {
        Thread CablePollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws IllegalAccessException, InvocationTargetException {
                DynamicSarService_M11R.this.pollCableStatus();
            }
        };
        CablePollThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:101:0x01d8 A[EDGE_INSN: B:101:0x01d8->B:64:0x01d8 BREAK  A[LOOP:1: B:37:0x011d->B:69:0x01f2], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:103:0x01f2 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:65:0x01e0  */
    /* JADX WARN: Code duplicated, block: B:68:0x01e6  */
    /* JADX WARN: Code duplicated, block: B:72:0x0210 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:76:0x021d  */
    /* JADX WARN: Code duplicated, block: B:99:0x021b A[ADDED_TO_REGION, EDGE_INSN: B:99:0x021b->B:75:0x021b BREAK  A[LOOP:0: B:20:0x00a1->B:74:0x0213], REMOVE, SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public final void pollCableStatus() throws IllegalAccessException, InvocationTargetException {
        char c;
        boolean mIsExist;
        String str;
        boolean mIsExist2;
        int i;
        int mState;
        Object mResult;
        boolean mIsExist3;
        String str2 = CABLE_DEVICE_PATH_M11R;
        Log.d("DynamicSarService_M11R", "enter pollCableStatus ...");
        PathClassLoader v2Loader = new PathClassLoader("/system/framework/vendor.xiaomi.hardware.misys-V2.0-java.jar", getClass().getClassLoader());
        PathClassLoader v1Loader = new PathClassLoader("/system/framework/vendor.xiaomi.hardware.misys-V1.0-java.jar", getClass().getClassLoader());
        try {
            Class IMiSysClz = v2Loader.loadClass("vendor.xiaomi.hardware.misys.V2_0.IMiSys");
            Class IBufferReadResultClz = v2Loader.loadClass("vendor.xiaomi.hardware.misys.V2_0.IBufferReadResult");
            Class IResultValueClz = v1Loader.loadClass("vendor.xiaomi.hardware.misys.V1_0.IResultValue");
            Object mIMiSys = null;
            int i2 = 0;
            try {
                Method getService = IMiSysClz.getMethod("getService", Boolean.TYPE);
                c = 1;
                try {
                    mIMiSys = getService.invoke(null, true);
                } catch (Exception e) {
                    e = e;
                    Log.e("DynamicSarService_M11R", "erro= " + Log.getStackTraceString(e));
                }
            } catch (Exception e2) {
                e = e2;
                c = 1;
            }
            if (mIMiSys != null) {
                int mState2 = 0;
                int mMaxReadTimes = 5;
                int mMaxExistTimes = 3;
                boolean mIsExist4 = false;
                while (true) {
                    boolean mIsExist5 = mIsExist4;
                    int mMaxExistTimes2 = mMaxExistTimes;
                    try {
                        Class<?>[] clsArr = new Class[2];
                        clsArr[i2] = String.class;
                        clsArr[c] = String.class;
                        Method isExists = IMiSysClz.getMethod("IsExists", clsArr);
                        if (this.mDevName.contains(DEVICE_TYPE_M11R)) {
                            mIsExist3 = ((Boolean) isExists.invoke(mIMiSys, str2, MtkTelephony.MtkThreads.STATUS)).booleanValue();
                        } else {
                            mIsExist3 = ((Boolean) isExists.invoke(mIMiSys, CABLE_DEVICE_PATH, MtkTelephony.MtkThreads.STATUS)).booleanValue();
                        }
                        try {
                            Log.d("DynamicSarService_M11R", "mIsExist = " + mIsExist3);
                            mIsExist = mIsExist3;
                        } catch (Exception e3) {
                            e = e3;
                            mIsExist = mIsExist3;
                            Log.e("DynamicSarService_M11R", "erro= " + Log.getStackTraceString(e));
                        }
                    } catch (Exception e4) {
                        e = e4;
                        mIsExist = mIsExist5;
                    }
                    if (mIsExist) {
                        while (true) {
                            mIsExist2 = mIsExist;
                            try {
                                Class<?>[] clsArr2 = new Class[2];
                                clsArr2[i2] = String.class;
                                clsArr2[c] = String.class;
                                Method MiSysReadBuffer = IMiSysClz.getMethod("MiSysReadBuffer", clsArr2);
                                if (this.mDevName.contains(DEVICE_TYPE_M11R)) {
                                    mResult = MiSysReadBuffer.invoke(mIMiSys, str2, MtkTelephony.MtkThreads.STATUS);
                                } else {
                                    mResult = MiSysReadBuffer.invoke(mIMiSys, CABLE_DEVICE_PATH, MtkTelephony.MtkThreads.STATUS);
                                }
                                str = str2;
                                try {
                                    if (IBufferReadResultClz.getField("value").get(mResult) == IResultValueClz.getField("MISYS_SUCCESS").get(null)) {
                                        ArrayList<Byte> mDataList = new ArrayList<>();
                                        mDataList.addAll((Collection) IBufferReadResultClz.getField("data").get(mResult));
                                        i = i2;
                                        try {
                                            mState2 = mDataList.get(i).byteValue() - 48;
                                        } catch (Exception e5) {
                                            e = e5;
                                            Log.e("DynamicSarService_M11R", "erro= " + Log.getStackTraceString(e));
                                            mMaxReadTimes--;
                                            if (mMaxReadTimes == 0) {
                                                mState = mState2;
                                            } else {
                                                Log.d("DynamicSarService_M11R", " Try to read file max times, failed.");
                                                mMaxExistTimes = mMaxExistTimes2;
                                                if (mMaxExistTimes != 0) {
                                                    break;
                                                } else {
                                                    break;
                                                }
                                                if (mMaxExistTimes == 0) {
                                                    Log.d("DynamicSarService_M11R", "get failed for max times!");
                                                }
                                                return;
                                            }
                                        }
                                    } else {
                                        i = i2;
                                        Log.d("DynamicSarService_M11R", "err = " + IBufferReadResultClz.getField("value").get(mResult));
                                    }
                                    mState = mState2;
                                } catch (Exception e6) {
                                    e = e6;
                                    i = i2;
                                    Log.e("DynamicSarService_M11R", "erro= " + Log.getStackTraceString(e));
                                    mMaxReadTimes--;
                                    if (mMaxReadTimes == 0) {
                                        mState = mState2;
                                        if (mState != this.mCableGpioStatus) {
                                            callChangeListeners(this.mCableListeners, 10, mState);
                                            this.mCableGpioStatus = mState;
                                            OnSenesorStateChange();
                                        }
                                        SystemClock.sleep(1000L);
                                        mState2 = mState;
                                        i2 = i;
                                        mIsExist = mIsExist2;
                                        str2 = str;
                                    } else {
                                        Log.d("DynamicSarService_M11R", " Try to read file max times, failed.");
                                        mMaxExistTimes = mMaxExistTimes2;
                                        if (mMaxExistTimes != 0) {
                                            break;
                                            break;
                                        } else {
                                            break;
                                            break;
                                        }
                                        if (mMaxExistTimes == 0) {
                                            Log.d("DynamicSarService_M11R", "get failed for max times!");
                                        }
                                        return;
                                    }
                                }
                            } catch (Exception e7) {
                                e = e7;
                                str = str2;
                            }
                            if (mState != this.mCableGpioStatus) {
                                callChangeListeners(this.mCableListeners, 10, mState);
                                this.mCableGpioStatus = mState;
                                OnSenesorStateChange();
                            }
                            SystemClock.sleep(1000L);
                            mState2 = mState;
                            i2 = i;
                            mIsExist = mIsExist2;
                            str2 = str;
                        }
                        Log.d("DynamicSarService_M11R", " Try to read file max times, failed.");
                        mMaxExistTimes = mMaxExistTimes2;
                    } else {
                        str = str2;
                        mIsExist2 = mIsExist;
                        i = i2;
                        SystemClock.sleep(5000L);
                        mMaxExistTimes = mMaxExistTimes2 - 1;
                    }
                    if (mMaxExistTimes != 0 || mIsExist2) {
                        break;
                        break;
                    } else {
                        i2 = i;
                        mIsExist4 = mIsExist2;
                        str2 = str;
                    }
                }
                if (mMaxExistTimes == 0) {
                    Log.d("DynamicSarService_M11R", "get failed for max times!");
                }
                return;
            }
            Log.d("DynamicSarService_M11R", "mIMiSysv is null!");
        } catch (ClassNotFoundException e8) {
            e8.printStackTrace();
        }
    }

    private final void startPollUseCaseTest() {
        Thread TestPollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService_M11R.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarService_M11R.this.pollTest();
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
                Log.d("DynamicSarService_M11R", "mTestPropValue: " + mTestPropValue);
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
        Log.d("DynamicSarService_M11R", "modem or radio power state change,messageType: " + messageType + " state: " + state);
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
            Log.d("DynamicSarService_M11R", "RADIO_POWER_ON ");
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
                break;
            case 2:
                this.mModemListeners.add(listener);
                break;
            case 3:
                this.mSarSensorListeners.add(listener);
                if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11) || this.mDevName.contains(DEVICE_TYPE_L12A)) {
                    if (this.mSensorEnable) {
                        boolean retSensor1 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR);
                        if (!retSensor1) {
                            callChangeListeners(this.mSarSensorListeners, 3, 3);
                            Log.d("DynamicSarService_M11R", "retSensor1 is abnormal, set default value 3");
                        }
                    } else {
                        callChangeListeners(this.mSarSensorListeners, 3, 3);
                        Log.d("DynamicSarService_M11R", "mSensorEnable is abnormal, set sensor1 default value 3");
                    }
                } else {
                    boolean retSensor2 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR);
                    if (!retSensor2) {
                        callChangeListeners(this.mSarSensorListeners, 3, 1);
                    }
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
                if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11) || this.mDevName.contains(DEVICE_TYPE_L12A)) {
                    if (this.mSensorEnable) {
                        boolean retSensor3 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR2);
                        if (!retSensor3) {
                            callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                            Log.d("DynamicSarService_M11R", "retSensor2 is abnormal, set default value 3");
                        }
                    } else {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                        Log.d("DynamicSarService_M11R", "mSensorEnable is abnormal, set sensor2 default value 3");
                    }
                } else {
                    boolean retSensor4 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR2);
                    if (!retSensor4) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 1);
                    }
                }
                break;
            case 10:
                this.mCableListeners.add(listener);
                startPollCableStatus();
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
            Log.d("DynamicSarService_M11R", "listeners is null or mModemSarEnable is disable!!!");
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
        this.sensor2 = Integer.toString(this.mCurrentHotspotState);
        this.sensor3 = Integer.toString(this.mCurrentSarSensorState);
        this.sensor4 = Integer.toString(this.mCurrentSarSensor2State);
        this.sensor5 = Integer.toString(this.mCableGpioStatus);
        String SendToCit = this.sensor1 + this.sensor2 + this.sensor3 + this.sensor4 + this.sensor5;
        Log.d("DynamicSarService_M11R", "SendToCit  value = " + SendToCit);
        SetSensorValues(SendToCit);
    }

    private void SetSensorValues(String mCurrentSensorArray) {
        Intent intent = new Intent(SET_SENSOR_VALUES_INTENT_ZORE);
        intent.putExtra(SET_SENSOR_VALUES_STATE_ZORE, mCurrentSensorArray);
        this.mContext.sendBroadcast(intent);
    }
}
