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
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
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
    private static final String[] CEList;
    private static final String[] FCCList;
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
    private static final List mCEList;
    private static final List mFCCList;
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
    private final String TAG = "DynamicServiceSarV8550";
    private int mCableGpioStatus = 0;
    private int mCurrentMccState = 0;
    private int mCurrentBluetoothPanState = -1;
    private int mCurrentBluetoothState = -1;
    private int mCurrentAirplaneState = -1;
    private int mModemSarEnable = 0;
    private int mCurrentUseCaseTest = 0;
    private int mCurrentUsbState = 0;
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
    private ArrayList mUsbStateListeners = new ArrayList();
    private ArrayList mSarSensor3Listeners = new ArrayList();
    private final Object mLock = new Object();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.1
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
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int i = 0;
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                boolean zIsConnected = ((NetworkInfo) intent.getParcelableExtra("networkInfo")).isConnected();
                if (zIsConnected != DynamicSarServiceSarV8550.this.mCurrentWifiState) {
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8550 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8550.callChangeListeners(dynamicSarServiceSarV8550.mWifiListeners, 0, zIsConnected ? 1 : 0);
                    DynamicSarServiceSarV8550.this.mCurrentWifiState = zIsConnected ? 1 : 0;
                    return;
                }
                return;
            }
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                i = intent.getIntExtra("wifi_state", 0) == 13 ? 1 : 0;
                if (i != DynamicSarServiceSarV8550.this.mCurrentHotspotState) {
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8551 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8551.callChangeListeners(dynamicSarServiceSarV8551.mHotspotListeners, 1, i);
                    DynamicSarServiceSarV8550.this.mCurrentHotspotState = i;
                    DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    return;
                }
                return;
            }
            if ("android.bluetooth.action.STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("android.bluetooth.extra.LOCAL_STATE", -1);
                if (intExtra == 1) {
                    i = 1;
                } else if (intExtra != 0) {
                    i = -1;
                }
                if (i == DynamicSarServiceSarV8550.this.mCurrentBluetoothPanState || i == -1) {
                    return;
                }
                DynamicSarServiceSarV8550 dynamicSarServiceSarV8552 = DynamicSarServiceSarV8550.this;
                dynamicSarServiceSarV8552.callChangeListeners(dynamicSarServiceSarV8552.mBluetoothPanListeners, 7, i);
                DynamicSarServiceSarV8550.this.mCurrentBluetoothPanState = i;
                return;
            }
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                if (intExtra2 == 12) {
                    i = 1;
                } else if (intExtra2 != 10) {
                    i = -1;
                } else if (intent.getBooleanExtra("android.xiaomi.bluetooth.BLUETOOTH_RESTRICT_STATE", false)) {
                    DynamicSarServiceSarV8550.this.log("ignore bluetooth fake off state");
                    i = 1;
                }
                if (i == DynamicSarServiceSarV8550.this.mCurrentBluetoothState || i == -1) {
                    return;
                }
                DynamicSarServiceSarV8550 dynamicSarServiceSarV8553 = DynamicSarServiceSarV8550.this;
                dynamicSarServiceSarV8553.callChangeListeners(dynamicSarServiceSarV8553.mBluetoothListeners, 8, i);
                DynamicSarServiceSarV8550.this.mCurrentBluetoothState = i;
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                DynamicSarServiceSarV8550.this.log("iccState = " + stringExtra);
                if ("LOADED".equals(stringExtra)) {
                    DynamicSarServiceSarV8550.this.log("iccState is loaded ");
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8554 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8554.callChangeListeners(dynamicSarServiceSarV8554.mCardStateListeners, 14, 0);
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
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8555 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8555.callChangeListeners(dynamicSarServiceSarV8555.mUsbStateListeners, 11, 1);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 1;
                    return;
                }
                return;
            }
            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) || "android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
                DynamicSarServiceSarV8550.this.log("  Action： " + action);
                if (DynamicSarServiceSarV8550.this.mCurrentUsbState != 0) {
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8556 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8556.callChangeListeners(dynamicSarServiceSarV8556.mUsbStateListeners, 11, 0);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 0;
                    return;
                }
                return;
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int intExtra3 = intent.getIntExtra("plugged", -1);
                DynamicSarServiceSarV8550.this.log(" ACTION_BATTERY_CHANGED change,USB plug = " + intExtra3);
                if ((intExtra3 == 1 || intExtra3 == 2) && DynamicSarServiceSarV8550.this.mCurrentUsbState != 1) {
                    DynamicSarServiceSarV8550 dynamicSarServiceSarV8557 = DynamicSarServiceSarV8550.this;
                    dynamicSarServiceSarV8557.callChangeListeners(dynamicSarServiceSarV8557.mUsbStateListeners, 11, 1);
                    DynamicSarServiceSarV8550.this.mCurrentUsbState = 1;
                }
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            int i = 0;
            switch (sensorEvent.sensor.getType()) {
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR /* 33171028 */:
                    int i2 = (int) sensorEvent.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor1 distance = " + i2);
                    if (i2 != DynamicSarServiceSarV8550.this.mCurrentSarSensorState) {
                        DynamicSarServiceSarV8550 dynamicSarServiceSarV8550 = DynamicSarServiceSarV8550.this;
                        dynamicSarServiceSarV8550.callChangeListeners(dynamicSarServiceSarV8550.mSarSensorListeners, 3, i2);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensorState = i2;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR2 /* 33171059 */:
                    int i3 = (int) sensorEvent.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor2 distance = " + i3);
                    if (i3 != DynamicSarServiceSarV8550.this.mCurrentSarSensor2State) {
                        DynamicSarServiceSarV8550 dynamicSarServiceSarV8551 = DynamicSarServiceSarV8550.this;
                        dynamicSarServiceSarV8551.callChangeListeners(dynamicSarServiceSarV8551.mSarSensor2Listeners, 9, i3);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensor2State = i3;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_HALLSENSOR /* 33171087 */:
                    float f = sensorEvent.values[0];
                    if (f == 0.0f) {
                        i = 1;
                    } else if (f == 1.0f) {
                        i = 2;
                    }
                    DynamicSarServiceSarV8550.this.log("onSensorChanged hallstate = " + i);
                    if (i != DynamicSarServiceSarV8550.this.mCurrentHallSensorState) {
                        DynamicSarServiceSarV8550 dynamicSarServiceSarV8552 = DynamicSarServiceSarV8550.this;
                        dynamicSarServiceSarV8552.callChangeListeners(dynamicSarServiceSarV8552.mHallSensorListeners, 4, i);
                        DynamicSarServiceSarV8550.this.mCurrentHallSensorState = i;
                    }
                    break;
                case DynamicSarServiceSarV8550.SENSOR_TYPE_SARSENSOR3 /* 33171109 */:
                    int i4 = (int) sensorEvent.values[0];
                    DynamicSarServiceSarV8550.this.log("onSensorChanged sensor3 distance = " + i4);
                    if (i4 != DynamicSarServiceSarV8550.this.mCurrentSarSensor3State) {
                        DynamicSarServiceSarV8550 dynamicSarServiceSarV8553 = DynamicSarServiceSarV8550.this;
                        dynamicSarServiceSarV8553.callChangeListeners(dynamicSarServiceSarV8553.mSarSensor3Listeners, 15, i4);
                        DynamicSarServiceSarV8550.this.mCurrentSarSensor3State = i4;
                        DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                    }
                    break;
                default:
                    DynamicSarServiceSarV8550.this.log("onSensorChanged unknown event for sensor: " + sensorEvent.sensor.getType());
                    break;
            }
        }
    };
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.7
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onCallStateChanged: state=" + i);
            DynamicSarServiceSarV8550.this.refreshSensorListener(32, i);
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int i) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onDataActivity: direction=" + i);
            DynamicSarServiceSarV8550.this.refreshSensorListener(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, i);
        }

        public void onRadioPowerStateChanged(int i) {
            DynamicSarServiceSarV8550.this.log("PhoneStateListener.onRadioPowerStateChanged: direction=" + i);
            DynamicSarServiceSarV8550.this.refreshSensorListener(8388608, i);
        }
    };

    interface SarControllerClient {
        void onStateChanged(int i, int i2);
    }

    static {
        String[] strArr = {"es", "it", "fr", "gb", "nl", "pt", "ch", "be", "lu", "de", "pl", "ua", "cz", "bg", "ro", "sk", "si", "hu", "gr", "dk", "fi", "no", "se", "lt", "ee", "lv", "at", "hr", "sa", "eg", "ir", "qa", "ma", "tr", "il", "ae", "ng", "th", "ph", "kh", "pk", "my", "au", "sg", "hk", "tw", "bd", "np", "lk", "jp"};
        CEList = strArr;
        String[] strArr2 = {"co", "uy", "py", "cl", "bo", "pe", "kr", "us", "ar"};
        FCCList = strArr2;
        mCEList = Arrays.asList(strArr);
        mFCCList = Arrays.asList(strArr2);
    }

    private DynamicSarServiceSarV8550(Context context) {
        this.mSarLogProState = false;
        this.mSarLogProState = SystemProperties.getBoolean(PROPERTY_DYNAMIC_SAR_LOG, true);
        log("DynamicServiceSarV8550 init...");
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        intentFilter.addAction(WIFI_COUNTRY_CODE_CHANGE);
        intentFilter.addAction("android.bluetooth.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction(ACTION_MODEM_SAR_STATE_CHANGE);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        if (sensorManager != null) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR_SUBSTANCE);
            this.mSensorSubstance = defaultSensor;
            if (defaultSensor == null) {
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
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return 0;
        }
        int wifiApState = wifiManager.getWifiApState();
        Log.d("DynamicServiceSarV8550", "wifiApState = " + wifiApState);
        return wifiApState == 13 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x005e  */
    public int getMccState() {
        String str = SystemProperties.get(PROPERTY_OPERATOR_ISO_COUNTRY, "");
        int i = 3;
        if (str.length() < 2) {
            log("Can't get network countryCode, use MCC_DEFAULT");
            return 3;
        }
        String[] strArrSplit = str.split(",");
        int i2 = 0;
        for (int i3 = 0; i3 < strArrSplit.length; i3++) {
            log("mCountryCode[" + i3 + "] = " + strArrSplit[i3]);
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
            log("mMccState is 0,can't get network countryCode, use MCC_DEFAULT");
        } else {
            i = i2;
        }
        log("mNetworkCountryCode: " + str + ", mMccState: " + i);
        return i;
    }

    class SarCountryCodeChangeListener implements WifiNl80211Manager.CountryCodeChangedListener {
        private SarCountryCodeChangeListener() {
        }

        public void onCountryCodeChanged(String str) {
            DynamicSarServiceSarV8550.this.log("SAR Receive onCountryCodeChanged: " + str);
            int mccState = DynamicSarServiceSarV8550.this.getMccState();
            if (mccState != DynamicSarServiceSarV8550.this.mCurrentMccState) {
                DynamicSarServiceSarV8550 dynamicSarServiceSarV8550 = DynamicSarServiceSarV8550.this;
                dynamicSarServiceSarV8550.callChangeListeners(dynamicSarServiceSarV8550.mMccListeners, 6, mccState);
                DynamicSarServiceSarV8550.this.mCurrentMccState = mccState;
            }
        }
    }

    private final void startPollAudioReceiverStatus() {
        new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollReceiver();
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
                log("AudioReceiverState: " + i);
                callChangeListeners(this.mAudioReceiverListeners, 5, i);
                this.mCurrentAudioReceiverState = i;
                OnSenesorStateChange();
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startUseCaseTest() {
        new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollUseCaseTest();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollUseCaseTest() {
        while (true) {
            int i = SystemProperties.getInt(PROPERTY_USECASE_TEST, 0);
            if (i != this.mCurrentUseCaseTest) {
                log("usetest: " + i);
                callChangeListeners(this.mUseCaseTestListeners, 12, i);
                this.mCurrentUseCaseTest = i;
            }
            SystemClock.sleep(1000L);
        }
    }

    private final void startPollCableStatus() {
        new Thread() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DynamicSarServiceSarV8550.this.pollCableStatus();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pollCableStatus() {
        Log.d("DynamicServiceSarV8550", "enter pollCableStatus ...");
        if (new File("/sys/bus/platform/devices/soc/soc:md_testing_mode").exists()) {
            Log.d("DynamicServiceSarV8550", "enter pollCable obeserve ...");
            new UEventObserver() { // from class: com.xiaomi.internal.telephony.DynamicSarServiceSarV8550.6
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
                        try {
                            if (zEquals != DynamicSarServiceSarV8550.this.mCableGpioStatus) {
                                DynamicSarServiceSarV8550 dynamicSarServiceSarV8550 = DynamicSarServiceSarV8550.this;
                                dynamicSarServiceSarV8550.callChangeListeners(dynamicSarServiceSarV8550.mCableListeners, 10, zEquals ? 1 : 0);
                                DynamicSarServiceSarV8550.this.mCableGpioStatus = zEquals ? 1 : 0;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    DynamicSarServiceSarV8550.this.OnSenesorStateChange();
                }
            }.startObserving("DEVPATH=/bus/platform/devices/soc/soc:md_testing_mode");
        }
    }

    private void startPhoneListener() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 8388768);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSensorListener(int i, int i2) {
        log("modem or radio power state change,messageType: " + i + " state: " + i2);
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
            log("RADIO_POWER_ON ");
            callChangeListeners(this.mRadioPowerListeners, 13, i2);
        }
        int i3 = (this.mDataStateActive || this.mCallStateActive) ? 1 : 0;
        if (i3 != this.mCurrentModemState) {
            callChangeListeners(this.mModemListeners, 2, i3);
            this.mCurrentModemState = i3;
        }
    }

    void registerStateChangeListener(int i, SarControllerClient sarControllerClient) {
        Log.d("DynamicServiceSarV8550", "registerStateChangeListener type = " + i);
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
                SensorManager sensorManager = this.mSensorManager;
                if (sensorManager == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensorListeners, 3, 1);
                    log("mSensorManager is null , set sensor1 valid");
                } else {
                    Sensor defaultSensor = sensorManager.getDefaultSensor(SENSOR_TYPE_SARSENSOR, true);
                    this.mSensor = defaultSensor;
                    if (!this.mSensorManager.registerListener(this.mSensorEventListener, defaultSensor, 3)) {
                        callChangeListeners(this.mSarSensorListeners, 3, 1);
                        log("sensor1 registration failed , set sensor1 valid");
                    }
                }
                break;
            case 4:
                this.mHallSensorListeners.add(sarControllerClient);
                SensorManager sensorManager2 = this.mSensorManager;
                if (sensorManager2 == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mHallSensorListeners, 4, 0);
                    log("mSensorManager is null, set hall abnomal");
                } else {
                    Sensor defaultSensor2 = sensorManager2.getDefaultSensor(SENSOR_TYPE_HALLSENSOR, false);
                    this.mHallSensor = defaultSensor2;
                    if (!this.mSensorManager.registerListener(this.mSensorEventListener, defaultSensor2, 3)) {
                        callChangeListeners(this.mHallSensorListeners, 4, 0);
                        log("hall sensor registration failed , set hall abnomal");
                    }
                }
                break;
            case 5:
                this.mAudioReceiverListeners.add(sarControllerClient);
                startPollAudioReceiverStatus();
                break;
            case 6:
                this.mMccListeners.add(sarControllerClient);
                if (this.mWifiCondManager == null) {
                    log("mWifiCondManager is null, CountryCodeChangedListener valid");
                } else {
                    this.mSarCountryCodeChangeListener = new SarCountryCodeChangeListener();
                    this.mWifiCondManager.registerCountryCodeChangedListener(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), this.mSarCountryCodeChangeListener);
                    log("registerCountryCodeChangedListener");
                }
                break;
            case 7:
                this.mBluetoothPanListeners.add(sarControllerClient);
                break;
            case 8:
                this.mBluetoothListeners.add(sarControllerClient);
                break;
            case 9:
                this.mSarSensor2Listeners.add(sarControllerClient);
                SensorManager sensorManager3 = this.mSensorManager;
                if (sensorManager3 == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensor2Listeners, 9, 1);
                    log("mSensorManager is null, set sensor2 valid");
                } else {
                    if (!this.mSensorManager.registerListener(this.mSensorEventListener, sensorManager3.getDefaultSensor(SENSOR_TYPE_SARSENSOR2, true), 3)) {
                        callChangeListeners(this.mSarSensor2Listeners, 9, 1);
                        log("sensor2 registration failed , set sensor2 valid");
                    }
                }
                break;
            case 10:
                this.mCableListeners.add(sarControllerClient);
                startPollCableStatus();
                break;
            case 11:
                this.mUsbStateListeners.add(sarControllerClient);
                break;
            case 12:
                this.mUseCaseTestListeners.add(sarControllerClient);
                startUseCaseTest();
                break;
            case 13:
                this.mRadioPowerListeners.add(sarControllerClient);
                break;
            case 14:
                this.mCardStateListeners.add(sarControllerClient);
                break;
            case 15:
                this.mSarSensor3Listeners.add(sarControllerClient);
                SensorManager sensorManager4 = this.mSensorManager;
                if (sensorManager4 == null || this.mSensorSubstance == null) {
                    callChangeListeners(this.mSarSensor3Listeners, 15, 1);
                    log("mSensorManager is null, set sensor3 valid");
                } else {
                    if (!this.mSensorManager.registerListener(this.mSensorEventListener, sensorManager4.getDefaultSensor(SENSOR_TYPE_SARSENSOR3, true), 3)) {
                        callChangeListeners(this.mSarSensor3Listeners, 15, 1);
                        log("sensor3 registration failed , set sensor3 valid");
                    }
                }
                break;
        }
    }

    void callChangeListeners(ArrayList arrayList, int i, int i2) {
        if (arrayList.size() == 0 || this.mModemSarEnable != 0) {
            log("listeners is null or md controller is disable!!!");
            return;
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            ((SarControllerClient) arrayList.get(i3)).onStateChanged(i, i2);
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
        String str = Integer.toString(this.mCurrentAudioReceiverState) + Integer.toString(this.mCurrentHotspotState) + Integer.toString(this.mCurrentSarSensorState) + Integer.toString(this.mCurrentSarSensor2State) + Integer.toString(this.mCurrentSarSensor3State) + Integer.toString(this.mCableGpioStatus);
        log("SendToCit  value = ".concat(str));
        SetSensorValues(str);
    }

    private void SetSensorValues(String str) {
        Intent intent = new Intent(SET_SENSOR_VALUES_INTENT);
        intent.putExtra(SET_SENSOR_VALUES_STATE, str);
        this.mContext.sendBroadcast(intent);
    }
}
