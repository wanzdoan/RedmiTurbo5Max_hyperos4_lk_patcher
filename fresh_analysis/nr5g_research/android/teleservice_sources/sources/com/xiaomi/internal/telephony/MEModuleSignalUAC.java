package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.xiaomi.modem.OemHookAgent;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSignalUAC {
    private static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
    private static final String ACTION_SCREEN_ORIENTATION_CHANGE = "android.intent.action.CONFIGURATION_CHANGED";
    private static String DEFAULT_DEVICE_UL_IMBALANCE_CFG = null;
    private static String DEVICE_UAC_CFG = "";
    private static final int EVENT_GESTURE_STATE_CHANGE = 2;
    private static final int EVENT_SCREEN_ORIENTATION_OBTAIN = 1;
    private static final int EXIT_AOL = -1;
    private static final int INDEX_AOL_GAME = 4;
    private static final int INDEX_AOL_LH = 2;
    private static final int INDEX_AOL_LH_HEAD = 0;
    private static final int INDEX_AOL_RH = 3;
    private static final int INDEX_AOL_RH_HEAD = 1;
    private static final int LEFT_HAND_STATE = 1;
    private static final int LEFT_HEAD_HAND_STATE = 101;
    private static final String LLM_LATENCY_KEY_LEVEL_DL = "Level_DL";
    private static final String LLM_LATENCY_KEY_LEVEL_UL = "Level_UL";
    private static final long LLM_LEVEL1 = 1;
    private static final long LLM_LEVEL2 = 2;
    private static final String PROPERTY_UAC_SCENE_STATUS = "persist.vendor.radio.uac_headhand_only";
    private static final int RIGHT_HAND_STATE = 2;
    private static final int RIGHT_HEAD_HAND_STATE = 102;
    private static final int SENSOR_TYPE_HANDSENSOR = 33171120;
    private static final String TAG = "MEModuleSignalUAC";
    private static int last_state = -1;
    protected static OemHookAgent mHookAgent = null;
    private static MEModuleSignalUAC mInstance = null;
    private static boolean mIsFeatureEnable = true;
    private static final Map uacConfigs;
    private Context mContext;
    private DisplayManager mDisplayManager;
    private boolean mIsHeadHandOnly;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private Handler mWorkerThreadHandler;
    private TelephonyManager teleManager;
    private boolean mIsGamemode = false;
    private boolean mIsCallState = false;
    private boolean mIsHandSensorReady = false;
    private HandlerThread mWorkerThread = new HandlerThread("MEModuleSignalUAC_BgThread");
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == MEModuleSignalUAC.SENSOR_TYPE_HANDSENSOR) {
                int i = (int) sensorEvent.values[0];
                if (i == 1) {
                    if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                        MEModuleSignalUAC.this.sendAtCmdToModem(0);
                    }
                    if (MEModuleSignalUAC.this.mIsCallState || MEModuleSignalUAC.this.mIsGamemode || MEModuleSignalUAC.this.isLandscape()) {
                        return;
                    }
                    MEModuleSignalUAC.this.sendAtCmdToModem(2);
                    return;
                }
                if (i == 2) {
                    if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                        MEModuleSignalUAC.this.sendAtCmdToModem(1);
                    }
                    if (MEModuleSignalUAC.this.mIsCallState || MEModuleSignalUAC.this.mIsGamemode || MEModuleSignalUAC.this.isLandscape()) {
                        return;
                    }
                    MEModuleSignalUAC.this.sendAtCmdToModem(3);
                    return;
                }
                if (i == 101) {
                    if (!MEModuleSignalUAC.this.mIsCallState || MEModuleSignalUAC.this.mIsGamemode) {
                        return;
                    }
                    MEModuleSignalUAC.this.sendAtCmdToModem(0);
                    return;
                }
                MEModuleSignalUAC mEModuleSignalUAC = MEModuleSignalUAC.this;
                if (i == 102) {
                    if (!mEModuleSignalUAC.mIsCallState || MEModuleSignalUAC.this.mIsGamemode) {
                        return;
                    }
                    MEModuleSignalUAC.this.sendAtCmdToModem(1);
                    return;
                }
                if (mEModuleSignalUAC.mIsGamemode || MEModuleSignalUAC.this.isLandscape()) {
                    return;
                }
                MEModuleSignalUAC.this.sendAtCmdToModem(-1);
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(MEModuleSignalUAC.TAG, "ACTION= " + action);
            if (action.equals(MEModuleSignalUAC.ACTION_GAME_CHANGE_LEVEL)) {
                long longExtra = intent.getLongExtra(MEModuleSignalUAC.LLM_LATENCY_KEY_LEVEL_UL, 1L);
                long longExtra2 = intent.getLongExtra(MEModuleSignalUAC.LLM_LATENCY_KEY_LEVEL_DL, 1L);
                Log.d(MEModuleSignalUAC.TAG, "level_ul = " + longExtra);
                Log.d(MEModuleSignalUAC.TAG, "level_dl = " + longExtra2);
                if (longExtra == 2 && longExtra2 == 2) {
                    MEModuleSignalUAC.this.mIsGamemode = true;
                    Log.d(MEModuleSignalUAC.TAG, "mIsGamemode = true");
                    if (MEModuleSignalUAC.this.isLandscape()) {
                        MEModuleSignalUAC.this.sendAtCmdToModem(4);
                        return;
                    }
                    return;
                }
                Log.d(MEModuleSignalUAC.TAG, "mIsGamemode = false");
                MEModuleSignalUAC.this.mIsGamemode = false;
                MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                return;
            }
            if (action.equals("android.intent.action.SCREEN_ON") && !MEModuleSignalUAC.this.mIsHeadHandOnly) {
                MEModuleSignalUAC.this.regHandsensor();
                return;
            }
            if (action.equals("android.intent.action.SCREEN_OFF") && !MEModuleSignalUAC.this.mIsHeadHandOnly && !MEModuleSignalUAC.this.mIsCallState) {
                MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                MEModuleSignalUAC.this.unRegHandsensor();
                return;
            }
            if (action.equals(MEModuleSignalUAC.ACTION_SCREEN_ORIENTATION_CHANGE)) {
                if (!MEModuleSignalUAC.this.isLandscape() || MEModuleSignalUAC.this.mIsGamemode) {
                    return;
                }
                MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                return;
            }
            if (action.equals("com.android.phone.intent.action.aol_gesture_state_changed")) {
                if (intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1) == 0) {
                    MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                    MEModuleSignalUAC.this.unRegHandsensor();
                    MEModuleSignalUAC.mIsFeatureEnable = false;
                } else if (1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1)) {
                    MEModuleSignalUAC.mIsFeatureEnable = true;
                    MEModuleSignalUAC.this.regHandsensor();
                }
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            if (2 == i || 1 == i) {
                MEModuleSignalUAC.this.mIsCallState = true;
                if (MEModuleSignalUAC.this.mIsHeadHandOnly) {
                    MEModuleSignalUAC.this.regHandsensor();
                    return;
                }
                return;
            }
            if (MEModuleSignalUAC.this.mIsHeadHandOnly) {
                MEModuleSignalUAC.this.unRegHandsensor();
            }
            MEModuleSignalUAC.this.mIsCallState = false;
            MEModuleSignalUAC.this.sendAtCmdToModem(-1);
            Log.d(MEModuleSignalUAC.TAG, "Exit! LH and RH optimize is disabled!");
        }
    };

    static {
        HashMap map = new HashMap();
        uacConfigs = map;
        map.put(1, "12340");
        map.put(2, "01012");
        DEFAULT_DEVICE_UL_IMBALANCE_CFG = "01234";
    }

    private MEModuleSignalUAC(Context context) {
        this.mIsHeadHandOnly = false;
        Log.d(TAG, "MEModuleSignalUAC init...");
        initDeviceUACCfg();
        this.mContext = context;
        this.mIsHeadHandOnly = SystemProperties.getBoolean(PROPERTY_UAC_SCENE_STATUS, false);
        this.mWorkerThread.start();
        setLooper(this.mWorkerThread.getLooper());
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager = displayManager;
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                if (MEModuleSignalUAC.this.mIsGamemode) {
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 1;
                    MEModuleSignalUAC.this.mWorkerThreadHandler.sendMessage(messageObtain);
                }
            }
        }, new Handler(Looper.getMainLooper()));
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.teleManager = telephonyManager;
        telephonyManager.listen(this.mPhoneStateListener, 32);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (!this.mIsHeadHandOnly) {
            regHandsensor();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GAME_CHANGE_LEVEL);
        intentFilter.addAction(ACTION_SCREEN_ORIENTATION_CHANGE);
        intentFilter.addAction("com.android.phone.intent.action.aol_gesture_state_changed");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        Log.d(TAG, "MEModuleSignalUAC init done");
    }

    public static MEModuleSignalUAC getInstance(Context context, OemHookAgent oemHookAgent) {
        mHookAgent = oemHookAgent;
        if (mInstance == null) {
            mInstance = new MEModuleSignalUAC(context);
        }
        return mInstance;
    }

    public void regHandsensor() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            Log.d(TAG, "obtain sensor service failed ");
        } else if (!this.mIsHandSensorReady && mIsFeatureEnable) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(SENSOR_TYPE_HANDSENSOR);
            this.mSensorSubstanceHand = defaultSensor;
            boolean zRegisterListener = this.mSensorManager.registerListener(this.mSensorEventListener, defaultSensor, 3);
            if (this.mSensorSubstanceHand == null || !zRegisterListener) {
                this.mIsHandSensorReady = false;
            } else {
                this.mIsHandSensorReady = true;
            }
        }
        Log.d(TAG, "regHandsensor mSensorFlag = " + this.mIsHandSensorReady);
    }

    public void unRegHandsensor() {
        if (this.mIsHandSensorReady) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstanceHand);
            this.mIsHandSensorReady = false;
        }
        Log.d(TAG, "unRegHandsensor mSensorFlag = " + this.mIsHandSensorReady);
    }

    public static void initDeviceUACCfg() {
        DEVICE_UAC_CFG = (String) uacConfigs.getOrDefault(Integer.valueOf(MEFeatureConfig.getFeatureSWVersion(1)), "12340");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmdToModem(int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        Bundle bundle = new Bundle();
        bundle.putInt("scene", i);
        messageObtain.setData(bundle);
        this.mWorkerThreadHandler.sendMessage(messageObtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlesendAtCmdToModem(int i) {
        if (last_state == i || !mIsFeatureEnable) {
            return;
        }
        if (MEFeatureConfig.isFeatureSupported(1)) {
            Log.d(TAG, "AT cmd: AT+ERFIDX=0," + getUacAT(i) + ", send success!!! Retunrn is " + mHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+ERFIDX=0," + String.valueOf(getUacAT(i))));
        }
        if (MEFeatureConfig.isFeatureSupported(2)) {
            mHookAgent.onMtkSendImbalanceAtCommand(getImbalanceAT(i));
            Log.d(TAG, "AT cmd: MIPC_SET_DYNAMIC_ANT_BIAS_" + getImbalanceAT(i) + ", send success!!!");
        }
        last_state = i;
    }

    private int getUacAT(int i) {
        if (-1 == i) {
            return -1;
        }
        return Character.getNumericValue(DEVICE_UAC_CFG.charAt(i));
    }

    private int getImbalanceAT(int i) {
        if (-1 == i) {
            return 5;
        }
        return Character.getNumericValue(DEFAULT_DEVICE_UL_IMBALANCE_CFG.charAt(i));
    }

    private void setLooper(Looper looper) {
        this.mWorkerThreadHandler = new WorkerHandler(looper);
    }

    public boolean isLandscape() {
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        boolean z = true;
        if (defaultDisplay.getRotation() != 1 && defaultDisplay.getRotation() != 3) {
            z = false;
            if (defaultDisplay.getRotation() != 0 && defaultDisplay.getRotation() != 2) {
                Log.d(TAG, "getRotation, failed!");
            }
        }
        return z;
    }

    class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                try {
                    Thread.sleep(500L);
                    if (MEModuleSignalUAC.this.isLandscape()) {
                        MEModuleSignalUAC.this.sendAtCmdToModem(4);
                        Log.d(MEModuleSignalUAC.TAG, "getRotation and Game, Success!");
                    }
                    Log.d(MEModuleSignalUAC.TAG, "Handle!");
                    return;
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                    Log.d(MEModuleSignalUAC.TAG, "Thread interrupted!");
                    return;
                } catch (Exception unused2) {
                    Log.d(MEModuleSignalUAC.TAG, "Handle EVENT_SCREEN_ORIENTATION_OBTAIN failed!");
                    return;
                }
            }
            if (i == 2) {
                Bundle data = message.getData();
                if (data != null) {
                    MEModuleSignalUAC.this.handlesendAtCmdToModem(data.getInt("scene"));
                    return;
                }
                return;
            }
            Log.d(MEModuleSignalUAC.TAG, "invalid msg id: " + message.what);
        }
    }
}
