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
import android.net.wifi.nl80211.WifiNl80211Manager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UEventObserver;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DynamicSarServiceSarV8550 {
    private static final String ACTION_MODEM_SAR_STATE_CHANGE = "com.android.phone.modemsar.state";
    public static final int AUDIO_RECEIVER_STATE_OFF = 0;
    public static final int AUDIO_RECEIVER_STATE_ON = 1;
    public static final int BLUETOOTH_PAN_STATE_OFF = 0;
    public static final int BLUETOOTH_PAN_STATE_ON = 1;
    public static final int BLUETOOTH_STATE_OFF = 0;
    public static final int BLUETOOTH_STATE_ON = 1;
    private static final String CABLE_DEVICE_PATH = "/sys/bus/platform/devices/soc/soc:md_testing_mode/";
    public static final int HALL_ABNORMAL_VALUE = 0;
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
    private static final String PROPERTY_DYNAMIC_SAR_LOG = "persist.vendor.radio.dynamic_sar_log";
    private static final String PROPERTY_OPERATOR_ISO_COUNTRY = "gsm.operator.iso-country";
    private static final String PROPERTY_RECEIVER_STATUS = "vendor.audio.voice.receiver.status";
    private static final String PROPERTY_USECASE_TEST = "persist.vendor.radio.usetest";
    public static final int SAR_DISTANCE_LONG = 0;
    public static final int SAR_DISTANCE_MID = 2;
    public static final int SAR_DISTANCE_SHORT = 1;
    private static final int SENSOR_TYPE_HALLSENSOR = 33171087;
    private static final int SENSOR_TYPE_SARSENSOR = 33171028;
    private static final int SENSOR_TYPE_SARSENSOR2 = 33171059;
    private static final int SENSOR_TYPE_SARSENSOR3 = 33171109;
    private static final int SENSOR_TYPE_SARSENSOR_SUBSTANCE = 33171015;
    private static final String SET_SENSOR_VALUES_INTENT = "com.android.set.sensor.values";
    private static final String SET_SENSOR_VALUES_STATE = "sensor_values_set";
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
    public static final int TYPE_SAR_SENSOR3_STATE = 15;
    public static final int TYPE_SAR_SENSOR_STATE = 3;
    public static final int TYPE_SIMCARD_STATE = 14;
    public static final int TYPE_USB_STATE = 11;
    public static final int TYPE_USE_CASE_TEST = 12;
    public static final int TYPE_WIFI_STATE = 0;
    private static final String WIFI_COUNTRY_CODE_CHANGE = "android.net.wifi.COUNTRY_CODE_CHANGED";
    public static final int WIFI_STATE_CONNECTED = 1;
    public static final int WIFI_STATE_DISCONNECTED = 0;
    private static DynamicSarServiceSarV8550 mInstance;
    private boolean mCallStateActive;
    private Context mContext;
    private int mCurrentAudioReceiverState;
    private int mCurrentHallSensorState;
    private int mCurrentHotspotState;
    private int mCurrentModemState;
    private int mCurrentSarSensor2State;
    private int mCurrentSarSensor3State;
    private int mCurrentSarSensorState;
    private int mCurrentWifiState;
    private boolean mDataStateActive;
    private Sensor mHallSensor;
    private SarCountryCodeChangeListener mSarCountryCodeChangeListener;
    private boolean mSarLogProState;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstance;
    private TelephonyManager mTelephonyManager;
    private WifiNl80211Manager mWifiCondManager;
    private WifiManager mWifiManager;
    private static final String[] CEList = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "sa", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "th", "ph", "kh", "pk", "my", "au", "sg", "hk", "tw", "bd", "np", "lk", "jp"};
    private static final String[] FCCList = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar"};
    private static final List<String> mCEList = Arrays.asList(CEList);
    private static final List<String> mFCCList = Arrays.asList(FCCList);
    private final String TAG = "DynamicServiceSarV8550";
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
    private int mCurrentUseCaseTest = 0;
    private int mCurrentUsbState = 0;
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
    private ArrayList<SarControllerClient> mUsbStateListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mSarSensor3Listeners = new ArrayList<>();
    private final Object mLock = new Object();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.1
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
                }
                if (wifiState != DynamicSarServiceSarV8550.this.mCurrentWifiState) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mWifiListeners, 0, wifiState);
                    DynamicSarServiceSarV8550.this.mCurrentWifiState = wifiState;
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
                if (hotspotState != DynamicSarServiceSarV8550.this.mCurrentHotspotState) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mHotspotListeners, 1, hotspotState);
                    DynamicSarServiceSarV8550.this.mCurrentHotspotState = hotspotState;
                    DynamicSarServiceSarV8550.this.OnSenesorStateChange();
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
                if (bluetoothPanState != DynamicSarServiceSarV8550.this.mCurrentBluetoothPanState && bluetoothPanState != -1) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mBluetoothPanListeners, 7, bluetoothPanState);
                    DynamicSarServiceSarV8550.this.mCurrentBluetoothPanState = bluetoothPanState;
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
                        DynamicSarServiceSarV8550.this.log("ignore bluetooth fake off state");
                    }
                }
                if (bluetoothState != DynamicSarServiceSarV8550.this.mCurrentBluetoothState && bluetoothState != -1) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mBluetoothListeners, 8, bluetoothState);
                    DynamicSarServiceSarV8550.this.mCurrentBluetoothState = bluetoothState;
                    return;
                }
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String iccState = intent.getStringExtra("ss");
                DynamicSarServiceSarV8550.this.log("iccState = " + iccState);
                if ("LOADED".equals(iccState)) {
                    DynamicSarServiceSarV8550.this.log("iccState is loaded ");
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mCardStateListeners, 14, 0);
                    return;
                }
                return;
            }
            if (DynamicSarServiceSarV8550.ACTION_MODEM_SAR_STATE_CHANGE.equals(action)) {
                DynamicSarServiceSarV8550.this.mModemSarEnable = Integer.parseInt(intent.getStringExtra(DynamicSarServiceSarV8550.MODEM_SAR_STATE));
                DynamicSarServiceSarV8550.this.log("set md controller state " + DynamicSarServiceSarV8550.this.mModemSarEnable);
                return;
            }
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action) || "android.intent.action.ACTION_POWER_CONNECTED".equals(action)) {
                DynamicSarServiceSarV8550.this.log("  Action： " + action);
                if (DynamicSarServiceSarV8550.this.mCurrentUsbState != 1) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mUsbStateListeners, 11, 1);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 1;
                    return;
                }
                return;
            }
            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) || "android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
                DynamicSarServiceSarV8550.this.log("  Action： " + action);
                if (DynamicSarServiceSarV8550.this.mCurrentUsbState != 0) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mUsbStateListeners, 11, 0);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 0;
                    return;
                }
                return;
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int plug = intent.getIntExtra("plugged", -1);
                DynamicSarServiceSarV8550.this.log(" ACTION_BATTERY_CHANGED change,USB plug = " + plug);
                if ((plug == 1 || plug == 2) && DynamicSarServiceSarV8550.this.mCurrentUsbState != 1) {
                    DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mUsbStateListeners, 11, 1);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 1;
                }
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                    int sarState = (int) event.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor1 distance = " + sarState);
                    if (sarState != DynamicSarServiceSarV8550.this.mCurrentSarSensorState) {
                        DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mSarSensorListeners, 3, sarState);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensorState = sarState;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                    int sar2State = (int) event.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor2 distance = " + sar2State);
                    if (sar2State != DynamicSarServiceSarV8550.this.mCurrentSarSensor2State) {
                        DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mSarSensor2Listeners, 9, sar2State);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensor2State = sar2State;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_HALLSENSOR /* 33171087 */:
                    int hallState = 0;
                    if (event.values[0] == 0.0f) {
                        hallState = 1;
                    } else if (event.values[0] == 1.0f) {
                        hallState = 2;
                    }
                    DynamicSarServiceSarV8550.this.log("onSensorChanged hallstate = " + hallState);
                    if (hallState != DynamicSarServiceSarV8550.this.mCurrentHallSensorState) {
                        DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mHallSensorListeners, 4, hallState);
                        DynamicSarServiceSarV8550.this.mCurrentHallSensorState = hallState;
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR3 /* 33171109 */:
                    int sar3State = (int) event.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor3 distance = " + sar3State);
                    if (sar3State != DynamicSarServiceSarV8550.this.mCurrentSarSensor3State) {
                        DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mSarSensor3Listeners, 15, sar3State);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensor3State = sar3State;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                default:
                    DynamicSarServiceSarV8550.this.log("onSensorChanged unknown event for sensor: " + event.sensor.getType());
                    break;
            }
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.7
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onCallStateChanged: state=" + state);
            DynamicSarServiceSarV8550.this.refreshSensorListener(32, state);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onDataActivity: direction=" + direction);
            DynamicSarServiceSarV8550.this.refreshSensorListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, direction);
        }

        public void onRadioPowerStateChanged(int direction) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onRadioPowerStateChanged: direction=" + direction);
            DynamicSarServiceSarV8550.this.refreshSensorListener(8388608, direction);
        }
    };

    interface SarControllerClient {
        void onStateChanged(int i, int i2);
    }

    private DynamicSarServiceSarV8550(Context context) {
        this.mSarLogProState = false;
        this.mSarLogProState = SystemProperties.getBoolean("persist.vendor.radio.dynamic_sar_log", true);
        log("DynamicServiceSarV8550 init...");
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction(WIFI_COUNTRY_CODE_CHANGE);
        filter.addAction("android.bluetooth.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction(ACTION_MODEM_SAR_STATE_CHANGE);
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorManager != null) {
            this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
            if (this.mSensorSubstance == null) {
                log("mSensorSubstance == null");
            }
        }
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        startPhoneListener();
        this.mWifiCondManager = (WifiNl80211Manager) this.mContext.getSystemService("wifinl80211");
        log("DynamicServiceSarV8550 init done");
    }

    public static DynamicSarServiceSarV8550 getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DynamicSarServiceSarV8550(context);
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
        Log.d("DynamicServiceSarV8550", "wifiApState = " + wifiApState);
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
            log("Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] mCountryCode = mNetworkCountryCode.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        for (int i = 0; i < mCountryCode.length; i++) {
            log("mCountryCode[" + i + "] = " + mCountryCode[i]);
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
            log("mMccState is 0,can't get network countryCode, use MCC_DEFAULT");
            mMccState = 3;
        }
        log("mNetworkCountryCode: " + mNetworkCountryCode + ", mMccState: " + mMccState);
        return mMccState;
    }

    private class SarCountryCodeChangeListener implements WifiNl80211Manager.CountryCodeChangedListener {
        private SarCountryCodeChangeListener() {
        }

        public void onCountryCodeChanged(String country) {
            DynamicSarServiceSarV8550.this.log("SAR Receive onCountryCodeChanged: " + country);
            int mccState = DynamicSarServiceSarV8550.this.getMccState();
            if (mccState != DynamicSarServiceSarV8550.this.mCurrentMccState) {
                DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mMccListeners, 6, mccState);
                DynamicSarServiceSarV8550.this.mCurrentMccState = mccState;
            }
        }
    }

    private final void startPollAudioReceiverStatus() {
        Thread ReceiverPollThread = new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollReceiver();
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
                log("AudioReceiverState: " + state);
                callChangeListeners(this.mAudioReceiverListeners, 5, state);
                this.mCurrentAudioReceiverState = state;
                OnSenesorStateChange();
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startUseCaseTest() {
        Thread UseCasePollThread = new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollUseCaseTest();
            }
        };
        UseCasePollThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollUseCaseTest() {
        while (true) {
            int usetest = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (usetest != this.mCurrentUseCaseTest) {
                log("usetest: " + usetest);
                callChangeListeners(this.mUseCaseTestListeners, 12, usetest);
                this.mCurrentUseCaseTest = usetest;
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startPollCableStatus() {
        Thread CablePollThread = new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollCableStatus();
            }
        };
        CablePollThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollCableStatus() {
        Log.d("DynamicServiceSarV8550", "enter pollCableStatus ...");
        if (new File("/sys/bus/platform/devices/soc/soc:md_testing_mode").exists()) {
            Log.d("DynamicServiceSarV8550", "enter pollCable obeserve ...");
            UEventObserver CableStatusObserver = new UEventObserver() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.6
                /* JADX WARN: Type inference fix 'apply assigned field type' failed
                java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
                	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
                	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
                	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
                 */
                public void onUEvent(UEventObserver.UEvent uEvent) {
                    boolean zEquals = "1".equals(uEvent.get("STATUS"));
                    Log.d("DynamicServiceSarV8550", "mCableStatus get");
                    synchronized (DynamicSarServiceSarV8550.this.mLock) {
                        if (zEquals != DynamicSarServiceSarV8550.this.mCableGpioStatus) {
                            DynamicSarServiceSarV8550.this.callChangeListeners(DynamicSarServiceSarV8550.this.mCableListeners, 10, zEquals ? 1 : 0);
                            DynamicSarServiceSarV8550.this.mCableGpioStatus = zEquals ? 1 : 0;
                        }
                    }
                    DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                }
            };
            CableStatusObserver.startObserving("DEVPATH=/bus/platform/devices/soc/soc:md_testing_mode");
        }
    }

    private void startPhoneListener() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 8388768);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSensorListener(int messageType, int state) {
        log("modem or radio power state change,messageType: " + messageType + " state: " + state);
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
            log("RADIO_POWER_ON ");
            callChangeListeners(this.mRadioPowerListeners, 13, state);
        }
        int modemState = (this.mDataStateActive || this.mCallStateActive) ? 1 : 0;
        if (modemState != this.mCurrentModemState) {
            callChangeListeners(this.mModemListeners, 2, modemState);
            this.mCurrentModemState = modemState;
        }
    }

    void registerStateChangeListener(int type, SarControllerClient listener) {
        Log.d("DynamicServiceSarV8550", "registerStateChangeListener type = " + type);
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
                if (this.mSensorManager == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensorListeners, 3, 1);
                    log("mSensorManager is null , set sensor1 valid");
                } else {
                    this.mSensor = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                    boolean sensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, 3);
                    if (!sensorFlag) {
                        callChangeListeners(this.mSarSensorListeners, 3, 1);
                        log("sensor1 registration failed , set sensor1 valid");
                    }
                }
                break;
            case 4:
                this.mHallSensorListeners.add(listener);
                if (this.mSensorManager == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mHallSensorListeners, 4, 0);
                    log("mSensorManager is null, set hall abnomal");
                } else {
                    this.mHallSensor = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, false);
                    boolean sensorFlag2 = this.mSensorManager.registerListener(this.mSensorEventListener, this.mHallSensor, 3);
                    if (!sensorFlag2) {
                        callChangeListeners(this.mHallSensorListeners, 4, 0);
                        log("hall sensor registration failed , set hall abnomal");
                    }
                }
                break;
            case 5:
                this.mAudioReceiverListeners.add(listener);
                startPollAudioReceiverStatus();
                break;
            case 6:
                this.mMccListeners.add(listener);
                if (this.mWifiCondManager == null) {
                    log("mWifiCondManager is null, CountryCodeChangedListener valid");
                } else {
                    this.mSarCountryCodeChangeListener = new SarCountryCodeChangeListener();
                    this.mWifiCondManager.registerCountryCodeChangedListener(new DynamicSarServiceSarV8550$$ExternalSyntheticLambda0(), this.mSarCountryCodeChangeListener);
                    log("registerCountryCodeChangedListener");
                }
                break;
            case 7:
                this.mBluetoothPanListeners.add(listener);
                break;
            case 8:
                this.mBluetoothListeners.add(listener);
                break;
            case 9:
                this.mSarSensor2Listeners.add(listener);
                if (this.mSensorManager == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensor2Listeners, 9, 1);
                    log("mSensorManager is null, set sensor2 valid");
                } else {
                    Sensor mSensor2 = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR2, true);
                    boolean sensorFlag3 = this.mSensorManager.registerListener(this.mSensorEventListener, mSensor2, 3);
                    if (!sensorFlag3) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 1);
                        log("sensor2 registration failed , set sensor2 valid");
                    }
                }
                break;
            case 10:
                this.mCableListeners.add(listener);
                startPollCableStatus();
                break;
            case 11:
                this.mUsbStateListeners.add(listener);
                break;
            case 12:
                this.mUseCaseTestListeners.add(listener);
                startUseCaseTest();
                break;
            case 13:
                this.mRadioPowerListeners.add(listener);
                break;
            case 14:
                this.mCardStateListeners.add(listener);
                break;
            case 15:
                this.mSarSensor3Listeners.add(listener);
                if (this.mSensorManager == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensor3Listeners, 15, 1);
                    log("mSensorManager is null, set sensor3 valid");
                } else {
                    Sensor mSensor3 = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR3, true);
                    boolean sensorFlag4 = this.mSensorManager.registerListener(this.mSensorEventListener, mSensor3, 3);
                    if (!sensorFlag4) {
                        callChangeListeners(this.mSarSensor3Listeners, 15, 1);
                        log("sensor3 registration failed , set sensor3 valid");
                    }
                }
                break;
        }
    }

    void callChangeListeners(ArrayList<SarControllerClient> listeners, int type, int value) {
        if (listeners.size() == 0 || this.mModemSarEnable != 0) {
            log("listeners is null or md controller is disable!!!");
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onStateChanged(type, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        if (this.mSarLogProState) {
            Log.d("DynamicServiceSarV8550", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void OnSenesorStateChange() {
        SystemClock.sleep(20L);
        String sensor1 = Integer.toString(this.mCurrentAudioReceiverState);
        String sensor2 = Integer.toString(this.mCurrentHotspotState);
        String sensor3 = Integer.toString(this.mCurrentSarSensorState);
        String sensor4 = Integer.toString(this.mCurrentSarSensor2State);
        String sensor5 = Integer.toString(this.mCurrentSarSensor3State);
        String sensor6 = Integer.toString(this.mCableGpioStatus);
        String SendToCit = sensor1 + sensor2 + sensor3 + sensor4 + sensor5 + sensor6;
        log("SendToCit  value = " + SendToCit);
        SetSensorValues(SendToCit);
    }

    private void SetSensorValues(String mCurrentSensorArray) {
        Intent intent = new Intent(SET_SENSOR_VALUES_INTENT);
        intent.putExtra(SET_SENSOR_VALUES_STATE, mCurrentSensorArray);
        this.mContext.sendBroadcast(intent);
    }
}
