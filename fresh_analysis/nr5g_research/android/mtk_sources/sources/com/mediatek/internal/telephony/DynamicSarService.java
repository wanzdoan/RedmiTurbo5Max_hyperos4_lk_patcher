package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
public class DynamicSarService {
    private static final String ACTION_MODEM_SAR_STATE_CHANGE = "com.android.phone.modemsar.state";
    public static final String ACTION_WIFI_DATA_DISTINGUISH = "android.net.wifi.action.WIFI_DATA_DISTINGUISH";
    private static final String AML_NETWORK_SLAVE_STATE_CHANGED_ACTION = "android.net.wifi.AML_NETWORK_SLAVE_STATE_CHANGE";
    public static final int AUDIO_RECEIVER_STATE_OFF = 0;
    public static final int AUDIO_RECEIVER_STATE_ON = 1;
    public static final int BAND_2_4_GHZ_END_FREQ_MHZ = 2484;
    public static final int BAND_2_4_GHZ_START_FREQ_MHZ = 2412;
    public static final int BAND_5_GHZ_END_FREQ_MHZ = 5885;
    public static final int BAND_5_GHZ_START_FREQ_MHZ = 5160;
    public static final int BAND_6_GHZ_END_FREQ_MHZ = 7115;
    public static final int BAND_6_GHZ_START_FREQ_MHZ = 5955;
    public static final int BLUETOOTH_PAN_STATE_OFF = 0;
    public static final int BLUETOOTH_PAN_STATE_ON = 1;
    public static final int BLUETOOTH_STATE_OFF = 0;
    public static final int BLUETOOTH_STATE_ON = 1;
    private static final String CABLE_DEVICE_PATH = "/sys/devices/platform/odm/odm:testing-mode/";
    private static final String CABLE_DEVICE_PATH_L11A = "/sys/devices/platform/soc/soc:odm/soc:odm:testing-mode/";
    private static final String CLOUD_WIFI_TA_SAR_ENABLE = "cloud_wifi_ta_sar_enable";
    public static final int DBS_STATE_OFF = 0;
    public static final int DBS_STATE_ON = 1;
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
    private static final int INVALID_CLOUD_VALUE = -1;
    public static final int MCC_CE = 1;
    public static final int MCC_DEFAULT = 3;
    public static final int MCC_FCC = 2;
    private static final String MODEM_SAR_STATE = "modem_sar_state";
    public static final int MODEM_STATE_ACTIVE = 1;
    public static final int MODEM_STATE_INACTIVE = 0;
    private static final String NETWORK_SLAVE_STATE_CHANGED_ACTION = "android.net.wifi.SLAVE_STATE_CHANGE";
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
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
    public static final int SLAVE_WIFI_STATE_CONNECTED = 1;
    public static final int SLAVE_WIFI_STATE_DISCONNECTED = 0;
    private static final String SUPPORT_80211D_DEVICE_LIST = "turner";
    public static final int TYPE_AUDIO_RECEIVER_STATE = 5;
    public static final int TYPE_BLUETOOTH_PAN_STATE = 7;
    public static final int TYPE_BLUETOOTH_STATE = 8;
    public static final int TYPE_CABLE_STATE = 10;
    public static final int TYPE_DBS_STATE = 15;
    public static final int TYPE_HALL_SENSOR_STATE = 4;
    public static final int TYPE_HOTSPOT_STATE = 1;
    public static final int TYPE_MCC_STATE = 6;
    public static final int TYPE_MODEM_STATE = 2;
    public static final int TYPE_RADIOPOWER_STATE = 13;
    public static final int TYPE_SAR_SENSOR2_STATE = 9;
    public static final int TYPE_SAR_SENSOR_STATE = 3;
    public static final int TYPE_SIMCARD_STATE = 14;
    public static final int TYPE_TA_SAR_STATE = 16;
    public static final int TYPE_USB_STATE = 11;
    public static final int TYPE_USE_CASE_TEST = 12;
    public static final int TYPE_WIFI_STATE = 0;
    public static final int WIFI_DATA_STATE_AP_MAC_ADDR = 2;
    public static final int WIFI_DATA_STATE_AP_SET_EMPTY = 65404;
    public static final int WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY = 128;
    public static final int WIFI_DATA_STATE_AP_VENDOR_IE_NUM_ONE = 1;
    public static final int WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER = 256;
    public static final int WIFI_DATA_STATE_DHCP_HOSTNAME = 8;
    public static final int WIFI_DATA_STATE_DHCP_PADDING_STA = 16;
    public static final int WIFI_DATA_STATE_DHCP_SET_EMPTY = 65251;
    public static final int WIFI_DATA_STATE_DHCP_VENDOR = 4;
    public static final int WIFI_DATA_STATE_EXTRA_APP_SET_EMPTY = 65471;
    public static final int WIFI_DATA_STATE_EXTRA_APP_STATE = 64;
    public static final int WIFI_DATA_STATE_MASK_AP = 16777216;
    public static final int WIFI_DATA_STATE_MASK_DHCP = 33554432;
    public static final int WIFI_DATA_STATE_MASK_EXTRA_APP = 134217728;
    public static final int WIFI_DATA_STATE_MASK_TCP = 67108864;
    public static final int WIFI_DATA_STATE_TCP_SET_EMPTY = 65503;
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
    private int mCurrentSlaveWifiState;
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
    public static String EXTRA_WIFI_DATA_STATE = "android.net.wifi.extra.WIFI_DATA_STATE";
    public static String EXTRA_IS_WIFI_INTERFACE = "android.net.wifi.extra.IS_WIFI_INTERFACE";
    private static final String[] CEList = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "ph", "kh", "pk", "my", "au", "sg", "hk", "bd", "np", "lk", "jp"};
    private static final String[] FCCList = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar", "tw"};
    private static final String[] SupportTaSarCCList = {"ie", "mt", "cy", "es", "it", "fr", "gb", "nl", "pt", "be", "lu", "de", "pl", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "se", "lt", "ee", "lv", "at", "ch", "no", "ua", "rs", "me", "hr", "al", "ru", "by", "kz", "uz", "az", "am", "md", "ge", "tm", "kg", "tj", "hk", "tw", "mo", "jp", "my", "th", "vn", "sg", "id", "mm", "la", "kh", "pg", "bn", "ph", "au", "nz", "sa", "pk", "ae", "tr", "om", "il", "qa", "bh", "lb", "iq", "jo", "kw", "il", "eg", "za", "ci", "ke", "ao", "tn", "ng", "dz", "bw", "gh", "ly", "mz", "so", "tz", "ug", "zm", "ma", "kr", "in", "np", "lk", "bd", "mx", "pe", "cr", "co", "cl", "uy", "py", "bo", "pa", "gt", "do", "ar", "ec", "br", "sv", "hn", "ni"};
    private static final List<String> mCEList = Arrays.asList(CEList);
    private static final List<String> mFCCList = Arrays.asList(FCCList);
    private static final List<String> mSupportTaSarCCList = Arrays.asList(SupportTaSarCCList);
    private final String TAG = "DynamicService";
    private final int isRoleUnknow = 0;
    private final int isRoleWifi = 1;
    private final int isRoleSlaveWifi = 2;
    private int chinaSarSetRole = 0;
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
    private int mSlaveWiFiDataSarState = 0;
    private int mWiFiDataSarState = 0;
    private int mCurrentWifiDbsState = 0;
    private int mCurIsCcSupportTaSar = 0;
    private int mDefaultTaSarEnable = 1;
    private int mCcSupportTaSar = 0;
    private int mCloudTaSarEnable = this.mDefaultTaSarEnable;
    private boolean mTaSarConfigEnable = false;
    private String[] mTaPlimitArray = null;
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
    private ArrayList<SarControllerClient> mWifiDbsListeners = new ArrayList<>();
    private ArrayList<SarControllerClient> mTaSarSupportListeners = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.DynamicSarService.1
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
                if (SystemProperties.get(DynamicSarService.PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
                    if (wifiState != DynamicSarService.this.mCurrentWifiState) {
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mWifiListeners, 0, wifiState);
                        DynamicSarService.this.mCurrentWifiState = wifiState;
                    }
                } else if ((wifiState == 1 || wifiState == 0) && (wifiState != DynamicSarService.this.mCurrentWifiState || wifiState == 1)) {
                    DynamicSarService.this.handlSpecialSecenWithWifiState(wifiState, DynamicSarService.this.mCurrentSlaveWifiState, true);
                    DynamicSarService.this.mCurrentWifiState = wifiState;
                }
                DynamicSarService.this.updateDbsState();
                return;
            }
            if (DynamicSarService.NETWORK_SLAVE_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo info2 = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                DynamicSarService.this.handleSlaveWifiSarByStateChange(Boolean.valueOf(info2.isConnected()));
                return;
            }
            if (DynamicSarService.AML_NETWORK_SLAVE_STATE_CHANGED_ACTION.equals(action)) {
                Boolean isNetworkSlaveWifiConnected = Boolean.valueOf(intent.getBooleanExtra("isConnected", false));
                DynamicSarService.this.handleSlaveWifiSarByStateChange(isNetworkSlaveWifiConnected);
                DynamicSarService.this.updateDbsState();
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
                }
                DynamicSarService.this.updateDbsState();
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
                return;
            }
            if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(action)) {
                int isCcSupportTaSar = 0;
                int mccState = DynamicSarService.this.getMccState("");
                if (DynamicSarService.this.mTaSarConfigEnable) {
                    isCcSupportTaSar = DynamicSarService.this.checkCcTaSarSupport("");
                }
                int i = DynamicSarService.this.mCurrentMccState;
                DynamicSarService dynamicSarService = DynamicSarService.this;
                if (mccState != i) {
                    dynamicSarService.callChangeListeners(DynamicSarService.this.mMccListeners, 6, mccState);
                    DynamicSarService.this.mCurrentMccState = mccState;
                    return;
                } else {
                    if (dynamicSarService.mTaSarConfigEnable && isCcSupportTaSar != DynamicSarService.this.mCurIsCcSupportTaSar) {
                        Log.d("DynamicService", "cc support changed");
                        DynamicSarService.this.callChangeListeners(DynamicSarService.this.mTaSarSupportListeners, 16, isCcSupportTaSar);
                        DynamicSarService.this.mCurIsCcSupportTaSar = isCcSupportTaSar;
                        return;
                    }
                    return;
                }
            }
            if (DynamicSarService.ACTION_WIFI_DATA_DISTINGUISH.equals(action)) {
                int state3 = intent.getIntExtra(DynamicSarService.EXTRA_WIFI_DATA_STATE, 0);
                int roleWifi = intent.getIntExtra(DynamicSarService.EXTRA_IS_WIFI_INTERFACE, 0);
                if (roleWifi == 1) {
                    DynamicSarService.this.mWiFiDataSarState = DynamicSarService.this.getRealWifiData(state3, DynamicSarService.this.mWiFiDataSarState);
                } else if (roleWifi == 2) {
                    DynamicSarService.this.mSlaveWiFiDataSarState = DynamicSarService.this.getRealWifiData(state3, DynamicSarService.this.mSlaveWiFiDataSarState);
                }
                Log.d("DynamicService", "WIFI_DATA_DISTINGUISH_ACTION: extra data=0x" + Integer.toHexString(state3) + ", WiFiDataSarState: 0x" + Integer.toHexString(DynamicSarService.this.mWiFiDataSarState) + ",0x" + Integer.toHexString(DynamicSarService.this.mSlaveWiFiDataSarState) + ", roleWifi:" + roleWifi);
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.mediatek.internal.telephony.DynamicSarService.2
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
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.mediatek.internal.telephony.DynamicSarService.6
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("DynamicService", "PhoneStateListener.onCallStateChanged: state=" + state);
            DynamicSarService.this.refreshSensorListener(32, state);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            Log.d("DynamicService", "PhoneStateListener.onDataActivity: direction=" + direction);
            DynamicSarService.this.refreshSensorListener(128, direction);
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
        long j;
        boolean z = false;
        this.mSensorEnable = true;
        Log.d("DynamicService", "DynamicSarService init...");
        this.mContext = context;
        this.mDevName = SystemProperties.get(PROPERTY_DEVICE_NAME);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction(NETWORK_SLAVE_STATE_CHANGED_ACTION);
        filter.addAction(AML_NETWORK_SLAVE_STATE_CHANGED_ACTION);
        filter.addAction(ACTION_WIFI_DATA_DISTINGUISH);
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction("android.bluetooth.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction(ACTION_MODEM_SAR_STATE_CHANGE);
        filter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        if (is80211DDevice()) {
            registerCountryCodeChangedCallBack();
        }
        registerTaSarEnableCloudObserver(this.mContext);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorManager == null) {
            Log.d("DynamicService", "obtain sensor service failed ");
            this.mSensorEnable = false;
        } else {
            this.mHallSensor = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, true);
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mHallSensor, 3);
            if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_L12A) || this.mDevName.contains(DEVICE_TYPE_M9) || this.mDevName.contains(DEVICE_TYPE_M12) || this.mDevName.contains(DEVICE_TYPE_M12A) || this.mDevName.contains(DEVICE_TYPE_M12A_KDDI) || this.mDevName.contains(DEVICE_TYPE_N11A) || this.mDevName.contains(DEVICE_TYPE_N12) || this.mDevName.contains(DEVICE_TYPE_N12A)) {
                do {
                    this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                    if (this.mSensorSubstance == null) {
                        Log.d("DynamicService", "mSensorSubstance is null!");
                    }
                    j = 1000;
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
            } else {
                j = 1000;
            }
            if (this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11)) {
                while (true) {
                    this.mSensorSubstance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
                    this.mSensor2Substance = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR2_SUBSTANCE);
                    if (this.mSensorSubstance == null) {
                        Log.d("DynamicService", "mSensorSubstance is null!");
                    }
                    if (this.mSensor2Substance == null) {
                        Log.d("DynamicService", "mSensor2Substance is null!");
                    }
                    boolean mSensorFlag2 = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstance, 3);
                    Log.d("DynamicService", "mSensorFlag = " + mSensorFlag2);
                    boolean mSensor2Flag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor2Substance, 3);
                    Log.d("DynamicService", "mSensor2Flag = " + mSensor2Flag);
                    if (this.mSensorSubstance == null || !mSensorFlag2 || this.mSensor2Substance == null || !mSensor2Flag) {
                        this.mSensorEnable = z;
                    } else {
                        this.mSensorEnable = true;
                    }
                    if (this.mSensorEnable) {
                        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstance);
                        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensor2Substance);
                        Log.d("DynamicService", "substance sensor register successfully, unregister immediately!!!");
                    } else {
                        SystemClock.sleep(j);
                        this.mSenorDetectTime--;
                        Log.d("DynamicService", "mSenorDetectTime = " + this.mSenorDetectTime);
                    }
                    if (this.mSenorDetectTime <= 0 || this.mSensorEnable) {
                        break;
                    } else {
                        z = false;
                    }
                }
            }
        }
        Log.d("DynamicService", "mSensorEnable = " + this.mSensorEnable);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        startPhoneListener();
        startPollAudioReceiverStatus();
        startPollUseCaseTest();
        loadTaPlimitArray(this.mContext);
        Log.d("DynamicService", "DynamicSarService init done");
    }

    public static DynamicSarService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DynamicSarService(context);
        }
        return mInstance;
    }

    private class ActiveCountryCodeChangedCallback implements WifiManager.ActiveCountryCodeChangedCallback {
        private ActiveCountryCodeChangedCallback() {
        }

        public void onActiveCountryCodeChanged(String countryCode) {
            int mccState = DynamicSarService.this.getMccState(countryCode);
            Log.d("DynamicService", "onActiveCountryCodeChanged, cc is " + countryCode);
            if (mccState != DynamicSarService.this.mCurrentMccState) {
                DynamicSarService.this.mCurrentMccState = mccState;
                DynamicSarService.this.callChangeListeners(DynamicSarService.this.mMccListeners, 6, DynamicSarService.this.mCurrentMccState);
            }
        }

        public void onCountryCodeInactive() {
            Log.d("DynamicService", "onCountryCodeInactive");
        }
    }

    public boolean isTaSarConfigEnable() {
        return this.mTaSarConfigEnable;
    }

    public int isCcSupportTaSar() {
        return this.mCcSupportTaSar;
    }

    public boolean isTaCloudEnable() {
        return this.mCloudTaSarEnable == 1;
    }

    public boolean isTaSarEnable() {
        Log.v("DynamicService", "cc:" + this.mCcSupportTaSar + ", config: " + this.mTaSarConfigEnable + ", cloud: " + this.mCloudTaSarEnable);
        return this.mCcSupportTaSar == 1 && this.mTaSarConfigEnable && this.mCloudTaSarEnable == 1;
    }

    public String[] getTaPlimitArray() {
        return this.mTaPlimitArray;
    }

    private void loadTaPlimitArray(Context context) {
        try {
            this.mTaSarConfigEnable = context.getResources().getBoolean(context.getResources().getIdentifier("config_support_ta_sar", "bool", "android.miui"));
            if (this.mTaSarConfigEnable) {
                Log.d("DynamicService", "ta config is enable");
                this.mTaPlimitArray = context.getResources().getStringArray(context.getResources().getIdentifier("config_ta_sar_plimit_params", "array", "android.miui"));
            }
        } catch (Exception e) {
            Log.e("DynamicService", "Failed to get ta plimit.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSlaveWifiSarByStateChange(Boolean isConnected) {
        int slaveWifiState;
        if (isConnected.booleanValue()) {
            slaveWifiState = 1;
        } else {
            slaveWifiState = 0;
        }
        if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
            if (slaveWifiState != this.mCurrentSlaveWifiState) {
                if (this.mCurrentWifiState == 0) {
                    callChangeListeners(this.mWifiListeners, 0, slaveWifiState);
                }
                this.mCurrentSlaveWifiState = slaveWifiState;
                return;
            }
            return;
        }
        if ((slaveWifiState == 1 || slaveWifiState == 0) && slaveWifiState != this.mCurrentSlaveWifiState) {
            handlSpecialSecenWithWifiState(this.mCurrentWifiState, slaveWifiState, false);
            this.mCurrentSlaveWifiState = slaveWifiState;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRealWifiData(int inputState, int wifiData) {
        int index = (-16777216) & inputState;
        switch (index) {
            case WIFI_DATA_STATE_MASK_AP /* 16777216 */:
                return (65404 & wifiData) | (65535 & inputState);
            case WIFI_DATA_STATE_MASK_DHCP /* 33554432 */:
                return (65251 & wifiData) | (65535 & inputState);
            case WIFI_DATA_STATE_MASK_TCP /* 67108864 */:
                return (65503 & wifiData) | (65535 & inputState);
            case WIFI_DATA_STATE_MASK_EXTRA_APP /* 134217728 */:
                return (65471 & wifiData) | (65535 & inputState);
            default:
                return wifiData;
        }
    }

    private void doCallChangeListeners(boolean neeeSet) {
        if (!neeeSet) {
            callChangeListeners(this.mWifiListeners, 0, 0);
        } else if (!SystemProperties.get(PROPERTY_WLAN_DISABLE_CHINA_SAR, "").equals("true")) {
            callChangeListeners(this.mWifiListeners, 0, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlSpecialSecenWithWifiState(int wifistate, int slaveWifiState, boolean isWifiEnter) {
        boolean isNeedSarByWifiData = false;
        Log.d("DynamicService", "wifistate:" + wifistate + ", slavewifistate:" + slaveWifiState + ",WiFiDataSar:0x" + Integer.toHexString(this.mWiFiDataSarState) + ",0x" + Integer.toHexString(this.mSlaveWiFiDataSarState));
        if (wifistate == 1) {
            if ((this.mWiFiDataSarState & 65535) == 269 || (this.mWiFiDataSarState & 65535) == 142 || (this.mWiFiDataSarState & 65535) == 15) {
                isNeedSarByWifiData = true;
            }
            this.mWiFiDataSarState = 0;
        }
        if (slaveWifiState == 1) {
            if ((this.mSlaveWiFiDataSarState & 65535) == 269 || (this.mSlaveWiFiDataSarState & 65535) == 142 || (65535 & this.mSlaveWiFiDataSarState) == 15) {
                isNeedSarByWifiData = true;
            }
            this.mSlaveWiFiDataSarState = 0;
        }
        Log.d("DynamicService", "needset:" + isNeedSarByWifiData + ", already set:" + this.chinaSarSetRole);
        if (wifistate == 1) {
            if (slaveWifiState == 1) {
                if (this.chinaSarSetRole == 0) {
                    if (isNeedSarByWifiData) {
                        this.chinaSarSetRole = (isWifiEnter ? 1 : 2) | this.chinaSarSetRole;
                        doCallChangeListeners(true);
                        return;
                    }
                    return;
                }
                if (isNeedSarByWifiData) {
                    this.chinaSarSetRole |= isWifiEnter ? 1 : 2;
                    return;
                }
                return;
            }
            if (slaveWifiState == 0) {
                int i = this.chinaSarSetRole;
                if (isNeedSarByWifiData) {
                    this.chinaSarSetRole = i | 1;
                    doCallChangeListeners(true);
                    return;
                } else {
                    if (i != 0) {
                        if ((this.chinaSarSetRole & 1) != 0) {
                            if ((2 & this.chinaSarSetRole) != 0) {
                                this.chinaSarSetRole &= -3;
                                return;
                            }
                            return;
                        } else {
                            this.chinaSarSetRole = 0;
                            doCallChangeListeners(false);
                            return;
                        }
                    }
                    doCallChangeListeners(false);
                    return;
                }
            }
            return;
        }
        if (wifistate == 0) {
            if (slaveWifiState == 1) {
                int i2 = this.chinaSarSetRole;
                if (isNeedSarByWifiData) {
                    this.chinaSarSetRole = 2 | i2;
                    doCallChangeListeners(true);
                    return;
                } else {
                    if (i2 != 0) {
                        if ((2 & this.chinaSarSetRole) == 0) {
                            this.chinaSarSetRole = 0;
                            doCallChangeListeners(false);
                            return;
                        } else {
                            if ((this.chinaSarSetRole & 1) != 0) {
                                this.chinaSarSetRole &= -2;
                                return;
                            }
                            return;
                        }
                    }
                    doCallChangeListeners(false);
                    return;
                }
            }
            if (slaveWifiState == 0 && this.chinaSarSetRole != 0) {
                this.chinaSarSetRole = 0;
                doCallChangeListeners(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkCcTaSarSupport(String countryCode) {
        String networkCountryCode = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (networkCountryCode.equals("")) {
            networkCountryCode = countryCode;
            Log.d("DynamicService", "sar cc change to " + countryCode);
        }
        if (networkCountryCode.length() < 2) {
            Log.d("DynamicService", "Can't get network countryCode, use lagacy sar");
            this.mCcSupportTaSar = 0;
            return 0;
        }
        String[] countryCodes = networkCountryCode.split(",");
        for (int i = 0; i < countryCodes.length; i++) {
            Log.d("DynamicService", "countryCodes[" + i + "] = " + countryCodes[i]);
            countryCodes[i] = countryCodes[i].toLowerCase();
            if (countryCodes[i].isEmpty() || !mSupportTaSarCCList.contains(countryCodes[i])) {
                Log.d("DynamicService", "cc not support ta");
                this.mCcSupportTaSar = 0;
                return 0;
            }
        }
        this.mCcSupportTaSar = 1;
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMccState(String country) {
        int mMccState = 0;
        String mNetworkCountryCode = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        if (mNetworkCountryCode.equals("")) {
            mNetworkCountryCode = country;
            Log.d("DynamicService", "sar country code change to " + country);
        }
        if (mNetworkCountryCode.length() < 2) {
            Log.d("DynamicService", "Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] mCountryCode = mNetworkCountryCode.split(",");
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

    public static int convertFreqToBand(int frequency) {
        if (frequency >= 2412 && frequency <= 2484) {
            return 1;
        }
        if (frequency < 5160 || frequency > 5885) {
            return (frequency < 5955 || frequency > 7115) ? 1 : 4;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDbsState() {
        int wifiDbsState = 0;
        int wifiFreq = 0;
        Log.d("DynamicService", "mCurrentWifiState = " + this.mCurrentWifiState + ", mCurrentSlaveWifiState = " + this.mCurrentSlaveWifiState + ", mCurrentHotspotState = " + this.mCurrentHotspotState);
        if (this.mCurrentWifiState == 1) {
            WifiInfo wifiInfo = this.mWifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                wifiFreq = wifiInfo.getFrequency();
            }
            if (this.mCurrentSlaveWifiState == 1) {
                wifiDbsState = 1;
            }
            if (this.mCurrentHotspotState == 1) {
                SoftApConfiguration softApConfig = this.mWifiManager.getSoftApConfiguration();
                int wifiStaBand = convertFreqToBand(wifiFreq);
                int softapBand = softApConfig.getBand();
                Log.d("DynamicService", "wifiStaBand = " + wifiStaBand + ", softapBand = " + softapBand);
                if (wifiStaBand != softapBand) {
                    wifiDbsState = 1;
                } else {
                    wifiDbsState = 0;
                }
            }
        } else {
            wifiDbsState = 0;
        }
        Log.d("DynamicService", "current wifiDbsState is : " + wifiDbsState);
        if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global") && wifiDbsState != this.mCurrentWifiDbsState) {
            callChangeListeners(this.mWifiDbsListeners, 15, wifiDbsState);
            this.mCurrentWifiDbsState = wifiDbsState;
        }
    }

    private final void startPollAudioReceiverStatus() {
        Thread ReceiverPollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService.3
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

    private final void startPollCableStatus() {
        Thread CablePollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws IllegalAccessException, InvocationTargetException {
                DynamicSarService.this.pollCableStatus();
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
        String str2 = CABLE_DEVICE_PATH_L11A;
        Log.d("DynamicService", "enter pollCableStatus ...");
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
                    Log.e("DynamicService", "erro= " + Log.getStackTraceString(e));
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
                        if (this.mDevName.contains(DEVICE_TYPE_L11A)) {
                            mIsExist3 = ((Boolean) isExists.invoke(mIMiSys, str2, MtkTelephony.MtkThreads.STATUS)).booleanValue();
                        } else {
                            mIsExist3 = ((Boolean) isExists.invoke(mIMiSys, CABLE_DEVICE_PATH, MtkTelephony.MtkThreads.STATUS)).booleanValue();
                        }
                        try {
                            Log.d("DynamicService", "mIsExist = " + mIsExist3);
                            mIsExist = mIsExist3;
                        } catch (Exception e3) {
                            e = e3;
                            mIsExist = mIsExist3;
                            Log.e("DynamicService", "erro= " + Log.getStackTraceString(e));
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
                                if (this.mDevName.contains(DEVICE_TYPE_L11A)) {
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
                                            Log.e("DynamicService", "erro= " + Log.getStackTraceString(e));
                                            mMaxReadTimes--;
                                            if (mMaxReadTimes == 0) {
                                                mState = mState2;
                                            } else {
                                                Log.d("DynamicService", " Try to read file max times, failed.");
                                                mMaxExistTimes = mMaxExistTimes2;
                                                if (mMaxExistTimes != 0) {
                                                    break;
                                                } else {
                                                    break;
                                                }
                                                if (mMaxExistTimes == 0) {
                                                    Log.d("DynamicService", "get failed for max times!");
                                                }
                                                return;
                                            }
                                        }
                                    } else {
                                        i = i2;
                                        Log.d("DynamicService", "err = " + IBufferReadResultClz.getField("value").get(mResult));
                                    }
                                    mState = mState2;
                                } catch (Exception e6) {
                                    e = e6;
                                    i = i2;
                                    Log.e("DynamicService", "erro= " + Log.getStackTraceString(e));
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
                                        Log.d("DynamicService", " Try to read file max times, failed.");
                                        mMaxExistTimes = mMaxExistTimes2;
                                        if (mMaxExistTimes != 0) {
                                            break;
                                            break;
                                        } else {
                                            break;
                                            break;
                                        }
                                        if (mMaxExistTimes == 0) {
                                            Log.d("DynamicService", "get failed for max times!");
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
                        Log.d("DynamicService", " Try to read file max times, failed.");
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
                    Log.d("DynamicService", "get failed for max times!");
                }
                return;
            }
            Log.d("DynamicService", "mIMiSysv is null!");
        } catch (ClassNotFoundException e8) {
            e8.printStackTrace();
        }
    }

    private final void startPollUseCaseTest() {
        Thread TestPollThread = new Thread() { // from class: com.mediatek.internal.telephony.DynamicSarService.5
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

    private boolean is80211DDevice() {
        return SUPPORT_80211D_DEVICE_LIST.indexOf(this.mDevName) != -1;
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
                if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11) || this.mDevName.contains(DEVICE_TYPE_L12A) || this.mDevName.contains(DEVICE_TYPE_M9) || this.mDevName.contains(DEVICE_TYPE_M12) || this.mDevName.contains(DEVICE_TYPE_M12A) || this.mDevName.contains(DEVICE_TYPE_M12A_KDDI) || this.mDevName.contains(DEVICE_TYPE_N11A) || this.mDevName.contains(DEVICE_TYPE_N12) || this.mDevName.contains(DEVICE_TYPE_N12A)) {
                    if (!this.mSensorEnable) {
                        callChangeListeners(this.mSarSensorListeners, 3, 3);
                        Log.d("DynamicService", "mSensorEnable is abnormal, set sensor1 default value 3");
                    } else {
                        boolean retSensor1 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR);
                        if (!retSensor1) {
                            callChangeListeners(this.mSarSensorListeners, 3, 3);
                            Log.d("DynamicService", "retSensor1 is abnormal, set default value 3");
                        }
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
                if (this.mDevName.contains(DEVICE_TYPE_K10A) || this.mDevName.contains(DEVICE_TYPE_K16) || this.mDevName.contains(DEVICE_TYPE_L11) || this.mDevName.contains(DEVICE_TYPE_L12A) || this.mDevName.contains(DEVICE_TYPE_M9) || this.mDevName.contains(DEVICE_TYPE_M12) || this.mDevName.contains(DEVICE_TYPE_M12A) || this.mDevName.contains(DEVICE_TYPE_M12A_KDDI) || this.mDevName.contains(DEVICE_TYPE_N11A) || this.mDevName.contains(DEVICE_TYPE_N12) || this.mDevName.contains(DEVICE_TYPE_N12A)) {
                    if (!this.mSensorEnable) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                        Log.d("DynamicService", "mSensorEnable is abnormal, set sensor2 default value 3");
                    } else {
                        boolean retSensor3 = sarRegExceHandle(SENSOR_TYPE_SARSENSOR2);
                        if (!retSensor3) {
                            callChangeListeners(this.mSarSensor2Listeners, 9, 3);
                            Log.d("DynamicService", "retSensor2 is abnormal, set default value 3");
                        }
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
            case 15:
                this.mWifiDbsListeners.add(listener);
                break;
            case 16:
                this.mTaSarSupportListeners.add(listener);
                break;
        }
    }

    private void registerCountryCodeChangedCallBack() {
        Log.d("DynamicService", "registerCountryCodeChangedCallBack");
        if (this.mWifiManager != null) {
            this.mWifiManager.registerActiveCountryCodeChangedCallback(new DynamicSarService$$ExternalSyntheticLambda0(), new ActiveCountryCodeChangedCallback());
        }
    }

    private void registerTaSarEnableCloudObserver(final Context context) {
        Handler cloudHandler = new Handler();
        ContentObserver observer = new ContentObserver(cloudHandler) { // from class: com.mediatek.internal.telephony.DynamicSarService.7
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                String setting = uri != null ? uri.getLastPathSegment() : null;
                if (!TextUtils.isEmpty(setting) && TextUtils.equals(setting, DynamicSarService.CLOUD_WIFI_TA_SAR_ENABLE)) {
                    DynamicSarService.this.updateTaSarEnableCloud(context);
                }
            }
        };
        context.getContentResolver().registerContentObserver(Settings.System.getUriFor(CLOUD_WIFI_TA_SAR_ENABLE), false, observer, -2);
        observer.onChange(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTaSarEnableCloud(Context context) {
        try {
            int cloudValue = Settings.System.getIntForUser(context.getContentResolver(), CLOUD_WIFI_TA_SAR_ENABLE, -1, -2);
            Log.d("DynamicService", "TA cloud value = " + cloudValue);
            if (cloudValue == -1 && this.mCloudTaSarEnable != cloudValue) {
                this.mCloudTaSarEnable = this.mDefaultTaSarEnable;
            } else {
                this.mCloudTaSarEnable = cloudValue;
            }
            callChangeListeners(this.mTaSarSupportListeners, 16, this.mCcSupportTaSar);
        } catch (Exception e) {
            Log.e("DynamicService", "updateTaSarEnableCloud: " + e);
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
