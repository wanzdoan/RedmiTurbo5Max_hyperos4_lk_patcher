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
import miui.telephony.CommenUtils;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSignalUAC {
    private static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
    private static final String ACTION_SCREEN_ORIENTATION_CHANGE = "android.intent.action.CONFIGURATION_CHANGED";
    private static String DEFAULT_DEVICE_UL_IMBALANCE_CFG = null;
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
    private Context mContext;
    private DisplayManager mDisplayManager;
    private boolean mIsHeadHandOnly;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private Handler mWorkerThreadHandler;
    private TelephonyManager teleManager;
    private static MEModuleSignalUAC mInstance = null;
    private static boolean mIsFeatureEnable = true;
    protected static OemHookAgent mHookAgent = null;
    private static int last_state = -1;
    private static String DEVICE_UAC_CFG = "";
    private static final Map<Integer, String> uacConfigs = new HashMap();
    private boolean mIsGamemode = false;
    private boolean mIsCallState = false;
    private boolean mIsHandSensorReady = false;
    private HandlerThread mWorkerThread = new HandlerThread("MEModuleSignalUAC_BgThread");
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == MEModuleSignalUAC.SENSOR_TYPE_HANDSENSOR) {
                int handSensorState = (int) event.values[0];
                switch (handSensorState) {
                    case 1:
                        if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(0);
                        }
                        if (!MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode && !MEModuleSignalUAC.this.isLandscape()) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(2);
                            break;
                        }
                        break;
                    case 2:
                        if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(1);
                        }
                        if (!MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode && !MEModuleSignalUAC.this.isLandscape()) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(3);
                            break;
                        }
                        break;
                    case 101:
                        if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(0);
                            break;
                        }
                        break;
                    case 102:
                        if (MEModuleSignalUAC.this.mIsCallState && !MEModuleSignalUAC.this.mIsGamemode) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(1);
                            break;
                        }
                        break;
                    default:
                        if (!MEModuleSignalUAC.this.mIsGamemode && !MEModuleSignalUAC.this.isLandscape()) {
                            MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                            break;
                        }
                        break;
                }
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(MEModuleSignalUAC.TAG, "ACTION= " + action);
            if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                long level_ul = intent.getLongExtra(MEModuleSignalUAC.LLM_LATENCY_KEY_LEVEL_UL, MEModuleSignalUAC.LLM_LEVEL1);
                long level_dl = intent.getLongExtra(MEModuleSignalUAC.LLM_LATENCY_KEY_LEVEL_DL, MEModuleSignalUAC.LLM_LEVEL1);
                Log.d(MEModuleSignalUAC.TAG, "level_ul = " + level_ul);
                Log.d(MEModuleSignalUAC.TAG, "level_dl = " + level_dl);
                if (level_ul == MEModuleSignalUAC.LLM_LEVEL2 && level_dl == MEModuleSignalUAC.LLM_LEVEL2) {
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
                if (MEModuleSignalUAC.this.isLandscape() && !MEModuleSignalUAC.this.mIsGamemode) {
                    MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                    return;
                }
                return;
            }
            if (action.equals("com.android.phone.intent.action.aol_gesture_state_changed")) {
                if (intent.getIntExtra("state", -1) == 0) {
                    MEModuleSignalUAC.this.sendAtCmdToModem(-1);
                    MEModuleSignalUAC.this.unRegHandsensor();
                    MEModuleSignalUAC.mIsFeatureEnable = false;
                } else if (1 == intent.getIntExtra("state", -1)) {
                    MEModuleSignalUAC.mIsFeatureEnable = true;
                    MEModuleSignalUAC.this.regHandsensor();
                }
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            if (2 == state || 1 == state) {
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
        uacConfigs.put(1, "12340");
        uacConfigs.put(2, "01012");
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
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager.registerDisplayListener(new DisplayManager.DisplayListener() { // from class: com.xiaomi.internal.telephony.MEModuleSignalUAC.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                if (MEModuleSignalUAC.this.mIsGamemode) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    MEModuleSignalUAC.this.mWorkerThreadHandler.sendMessage(msg);
                }
            }
        }, new Handler(Looper.getMainLooper()));
        this.teleManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.teleManager.listen(this.mPhoneStateListener, 32);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (!this.mIsHeadHandOnly) {
            regHandsensor();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        filter.addAction(ACTION_SCREEN_ORIENTATION_CHANGE);
        filter.addAction("com.android.phone.intent.action.aol_gesture_state_changed");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        Log.d(TAG, "MEModuleSignalUAC init done");
    }

    public static MEModuleSignalUAC getInstance(Context context, OemHookAgent hookAgent) {
        mHookAgent = hookAgent;
        if (mInstance == null) {
            mInstance = new MEModuleSignalUAC(context);
        }
        return mInstance;
    }

    public void regHandsensor() {
        if (this.mSensorManager == null) {
            Log.d(TAG, "obtain sensor service failed ");
        } else if (!this.mIsHandSensorReady && mIsFeatureEnable) {
            this.mSensorSubstanceHand = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HANDSENSOR);
            boolean mSensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstanceHand, 3);
            if (this.mSensorSubstanceHand == null || !mSensorFlag) {
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
        int featureSWVersion = MEFeatureConfig.getFeatureSWVersion(1);
        DEVICE_UAC_CFG = uacConfigs.getOrDefault(Integer.valueOf(featureSWVersion), "12340");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmdToModem(int scene) {
        Message msg = Message.obtain();
        msg.what = 2;
        Bundle bundle = new Bundle();
        bundle.putInt(CommenUtils.EXTRA_SCENE, scene);
        msg.setData(bundle);
        this.mWorkerThreadHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlesendAtCmdToModem(int scene) {
        if (last_state != scene && mIsFeatureEnable) {
            if (MEFeatureConfig.isFeatureSupported(1)) {
                String result = mHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+ERFIDX=0," + String.valueOf(getUacAT(scene)));
                Log.d(TAG, "AT cmd: AT+ERFIDX=0," + getUacAT(scene) + ", send success!!! Retunrn is " + result);
            }
            if (MEFeatureConfig.isFeatureSupported(2)) {
                mHookAgent.onMtkSendImbalanceAtCommand(getImbalanceAT(scene));
                Log.d(TAG, "AT cmd: MIPC_SET_DYNAMIC_ANT_BIAS_" + getImbalanceAT(scene) + ", send success!!!");
            }
            last_state = scene;
        }
    }

    private int getUacAT(int scene_index) {
        if (-1 == scene_index) {
            return -1;
        }
        return Character.getNumericValue(DEVICE_UAC_CFG.charAt(scene_index));
    }

    private int getImbalanceAT(int scene_index) {
        if (-1 == scene_index) {
            return 5;
        }
        return Character.getNumericValue(DEFAULT_DEVICE_UL_IMBALANCE_CFG.charAt(scene_index));
    }

    private void setLooper(Looper workerLooper) {
        this.mWorkerThreadHandler = new WorkerHandler(workerLooper);
    }

    public boolean isLandscape() {
        Display display = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        if (display.getRotation() == 1 || display.getRotation() == 3) {
            return true;
        }
        if (display.getRotation() == 0 || display.getRotation() == 2) {
            return false;
        }
        Log.d(TAG, "getRotation, failed!");
        return false;
    }

    private class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    Thread.sleep(500L);
                    if (MEModuleSignalUAC.this.isLandscape()) {
                        MEModuleSignalUAC.this.sendAtCmdToModem(4);
                        Log.d(MEModuleSignalUAC.TAG, "getRotation and Game, Success!");
                    }
                    Log.d(MEModuleSignalUAC.TAG, "Handle!");
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.d(MEModuleSignalUAC.TAG, "Thread interrupted!");
                    return;
                } catch (Exception e2) {
                    Log.d(MEModuleSignalUAC.TAG, "Handle EVENT_SCREEN_ORIENTATION_OBTAIN failed!");
                    return;
                }
            }
            if (msg.what == 2) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    int scene = bundle.getInt(CommenUtils.EXTRA_SCENE);
                    MEModuleSignalUAC.this.handlesendAtCmdToModem(scene);
                    return;
                }
                return;
            }
            Log.d(MEModuleSignalUAC.TAG, "invalid msg id: " + msg.what);
        }
    }
}
