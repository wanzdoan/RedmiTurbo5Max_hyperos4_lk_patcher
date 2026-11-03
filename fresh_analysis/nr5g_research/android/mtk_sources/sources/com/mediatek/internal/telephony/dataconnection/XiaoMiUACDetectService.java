package com.mediatek.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
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
import com.android.internal.telephony.CommandsInterface;

/* JADX INFO: loaded from: classes.dex */
public class XiaoMiUACDetectService {
    private static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
    private static final String ACTION_SCREEN_ORIENTATION_CHANGE = "android.intent.action.CONFIGURATION_CHANGED";
    private static final String DEVICE_AND_SKU_N11A = "duchamp, CN";
    private static final String DEVICE_AND_SKU_N12 = "rothko, CN, GLOBAL, JP";
    private static final String DEVICE_AND_SKU_N12A = "degas, GLOBAL";
    private static final String DEVICE_AND_SKU_N12_KDDI = "XIG06, JP";
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
    private static final String PROPERTY_FACTORY_BUILD = "ro.boot.factorybuild";
    private static final String PROPERTY_UAC_SCENE_STATUS = "persist.vendor.radio.uac_headhand_only";
    private static final int RIGHT_HAND_STATE = 2;
    private static final int RIGHT_HEAD_HAND_STATE = 102;
    private static final int SENSOR_TYPE_HANDSENSOR = 33171120;
    private static final String TAG = "XiaoMiUACDetectService";
    private Context mContext;
    private DisplayManager mDisplayManager;
    private boolean mIsHeadHandOnly;
    private boolean mIsULImbalanceEnable;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private Handler mWorkerThreadHandler;
    private TelephonyManager teleManager;
    private static XiaoMiUACDetectService mInstance = null;
    private static CommandsInterface[] sCi = null;
    private static int last_state = -1;
    private static String DEFAULT_DEVICE_UAC_CFG = "12340";
    private static String DEFAULT_DEVICE_UL_IMBALANCE_CFG = "23451";
    private static String UAC_AT = "AT+ERFIDX=0,";
    private static String mDeviceName = null;
    private static String mGlobalHW = null;
    private boolean mIsGamemode = false;
    private boolean mIsCallState = false;
    private boolean mIsHandSensorReady = false;
    private boolean mIsFeatureEnable = true;
    private HandlerThread mWorkerThread = new HandlerThread("XiaoMiUACDetectService_BgThread");
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.mediatek.internal.telephony.dataconnection.XiaoMiUACDetectService.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == XiaoMiUACDetectService.SENSOR_TYPE_HANDSENSOR) {
                int handSensorState = (int) event.values[0];
                switch (handSensorState) {
                    case 1:
                        if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(0);
                        }
                        if (!XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode && !XiaoMiUACDetectService.this.isLandscape()) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(2);
                            break;
                        }
                        break;
                    case 2:
                        if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(1);
                        }
                        if (!XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode && !XiaoMiUACDetectService.this.isLandscape()) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(3);
                            break;
                        }
                        break;
                    case 101:
                        if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(0);
                            break;
                        }
                        break;
                    case 102:
                        if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(1);
                            break;
                        }
                        break;
                    default:
                        if (!XiaoMiUACDetectService.this.mIsGamemode && !XiaoMiUACDetectService.this.isLandscape()) {
                            XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                            break;
                        }
                        break;
                }
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.dataconnection.XiaoMiUACDetectService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(XiaoMiUACDetectService.TAG, "ACTION= " + action);
            if (action.equals(XiaoMiUACDetectService.ACTION_GAME_CHANGE_LEVEL)) {
                long level_ul = intent.getLongExtra(XiaoMiUACDetectService.LLM_LATENCY_KEY_LEVEL_UL, 1L);
                long level_dl = intent.getLongExtra(XiaoMiUACDetectService.LLM_LATENCY_KEY_LEVEL_DL, 1L);
                Log.d(XiaoMiUACDetectService.TAG, "level_ul = " + level_ul);
                Log.d(XiaoMiUACDetectService.TAG, "level_dl = " + level_dl);
                if (level_ul == XiaoMiUACDetectService.LLM_LEVEL2 && level_dl == XiaoMiUACDetectService.LLM_LEVEL2) {
                    XiaoMiUACDetectService.this.mIsGamemode = true;
                    Log.d(XiaoMiUACDetectService.TAG, "mIsGamemode = true");
                    return;
                } else {
                    Log.d(XiaoMiUACDetectService.TAG, "mIsGamemode = false");
                    XiaoMiUACDetectService.this.mIsGamemode = false;
                    XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                    return;
                }
            }
            if (action.equals("android.intent.action.SCREEN_ON") && !XiaoMiUACDetectService.this.mIsHeadHandOnly) {
                XiaoMiUACDetectService.this.regHandsensor();
                return;
            }
            if (action.equals("android.intent.action.SCREEN_OFF") && !XiaoMiUACDetectService.this.mIsHeadHandOnly && !XiaoMiUACDetectService.this.mIsCallState) {
                XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                XiaoMiUACDetectService.this.unRegHandsensor();
                return;
            }
            if (action.equals(XiaoMiUACDetectService.ACTION_SCREEN_ORIENTATION_CHANGE)) {
                if (XiaoMiUACDetectService.this.isLandscape() && !XiaoMiUACDetectService.this.mIsGamemode) {
                    XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                    return;
                }
                return;
            }
            if (action.equals("com.android.phone.intent.action.aol_gesture_state_changed")) {
                if (intent.getIntExtra("state", -1) == 0) {
                    XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                    XiaoMiUACDetectService.this.unRegHandsensor();
                    XiaoMiUACDetectService.this.mIsFeatureEnable = false;
                } else if (1 == intent.getIntExtra("state", -1)) {
                    XiaoMiUACDetectService.this.mIsFeatureEnable = true;
                    XiaoMiUACDetectService.this.regHandsensor();
                }
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.mediatek.internal.telephony.dataconnection.XiaoMiUACDetectService.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            if (2 == state || 1 == state) {
                XiaoMiUACDetectService.this.mIsCallState = true;
                if (XiaoMiUACDetectService.this.mIsHeadHandOnly) {
                    XiaoMiUACDetectService.this.regHandsensor();
                    return;
                }
                return;
            }
            if (XiaoMiUACDetectService.this.mIsHeadHandOnly) {
                XiaoMiUACDetectService.this.unRegHandsensor();
            }
            XiaoMiUACDetectService.this.mIsCallState = false;
            XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
            Log.d(XiaoMiUACDetectService.TAG, "Exit! LH and RH optimize is disabled!");
        }
    };

    private XiaoMiUACDetectService(Context context) {
        this.mIsHeadHandOnly = false;
        this.mIsULImbalanceEnable = true;
        Log.d(TAG, "XiaoMiUACDetectService init...");
        if (1 == SystemProperties.getInt(PROPERTY_FACTORY_BUILD, 0) || (skuNotSuppport(DEVICE_AND_SKU_N11A) && skuNotSuppport(DEVICE_AND_SKU_N12) && skuNotSuppport(DEVICE_AND_SKU_N12_KDDI) && skuNotSuppport(DEVICE_AND_SKU_N12A))) {
            Log.d(TAG, "Factory build or Not support.");
            return;
        }
        if (DEVICE_AND_SKU_N12.contains(mDeviceName) || DEVICE_AND_SKU_N12_KDDI.contains(mDeviceName) || DEVICE_AND_SKU_N12A.contains(mDeviceName)) {
            DEFAULT_DEVICE_UAC_CFG = "01012";
            this.mIsULImbalanceEnable = false;
        }
        this.mContext = context;
        this.mIsHeadHandOnly = SystemProperties.getBoolean(PROPERTY_UAC_SCENE_STATUS, false);
        this.mWorkerThread.start();
        setLooper(this.mWorkerThread.getLooper());
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager.registerDisplayListener(new DisplayManager.DisplayListener() { // from class: com.mediatek.internal.telephony.dataconnection.XiaoMiUACDetectService.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                if (XiaoMiUACDetectService.this.mIsGamemode) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    XiaoMiUACDetectService.this.mWorkerThreadHandler.sendMessage(msg);
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
        filter.addAction(ACTION_GAME_CHANGE_LEVEL);
        filter.addAction(ACTION_SCREEN_ORIENTATION_CHANGE);
        filter.addAction("com.android.phone.intent.action.aol_gesture_state_changed");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        Log.d(TAG, "XiaoMiUACDetectService init done");
    }

    public static XiaoMiUACDetectService getInstance(Context context, CommandsInterface[] cis) {
        sCi = cis;
        if (mInstance == null) {
            mInstance = new XiaoMiUACDetectService(context);
        }
        return mInstance;
    }

    public void regHandsensor() {
        if (this.mSensorManager == null) {
            Log.d(TAG, "obtain sensor service failed ");
        } else if (!this.mIsHandSensorReady && this.mIsFeatureEnable) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmdToModem(int scene) {
        String uac_str = getUacAT(scene);
        if (last_state != scene && this.mIsFeatureEnable) {
            if (sCi != null) {
                for (int i = 0; i < sCi.length; i++) {
                    if (1 == sCi[i].getRadioState()) {
                        sCi[i].invokeOemRilRequestStrings(new String[]{uac_str, ""}, null);
                        Log.d(TAG, "AT cmd: " + uac_str + ", send success!!!");
                        if (!this.mIsULImbalanceEnable) {
                            break;
                        }
                        sCi[i].invokeOemRilRequestStrings(new String[]{"MIPC_SET_DYNAMIC_ANT_BIAS", getImbalanceAT(scene)}, null);
                        Log.d(TAG, "AT cmd: MIPC_SET_DYNAMIC_ANT_BIAS_" + getImbalanceAT(scene) + ", send success!!!");
                        break;
                    }
                    Log.d(TAG, "phone[" + i + "] radio is unavailable or off");
                }
                last_state = scene;
                return;
            }
            Log.e(TAG, "sCi[0] is NULL!!!");
        }
    }

    private String getUacAT(int scene_index) {
        if (-1 == scene_index) {
            return "AT+ERFIDX=0,-1";
        }
        return UAC_AT + DEFAULT_DEVICE_UAC_CFG.charAt(scene_index);
    }

    private String getImbalanceAT(int scene_index) {
        if (-1 == scene_index) {
            return "0";
        }
        return String.valueOf(DEFAULT_DEVICE_UL_IMBALANCE_CFG.charAt(scene_index));
    }

    private void setLooper(Looper workerLooper) {
        this.mWorkerThreadHandler = new WorkerHandler(workerLooper);
    }

    public boolean skuNotSuppport(String device_sku_cfg) {
        mDeviceName = SystemProperties.get("ro.product.device");
        mGlobalHW = SystemProperties.get("ro.boot.hwc");
        if (device_sku_cfg.contains(mDeviceName) && device_sku_cfg.contains(mGlobalHW)) {
            return false;
        }
        return true;
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
                    if (XiaoMiUACDetectService.this.isLandscape()) {
                        XiaoMiUACDetectService.this.sendAtCmdToModem(4);
                        Log.d(XiaoMiUACDetectService.TAG, "getRotation and Game, Success!");
                    }
                    Log.d(XiaoMiUACDetectService.TAG, "Handle!");
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.d(XiaoMiUACDetectService.TAG, "Thread interrupted!");
                    return;
                } catch (Exception e2) {
                    Log.d(XiaoMiUACDetectService.TAG, "Handle EVENT_SCREEN_ORIENTATION_OBTAIN failed!");
                    return;
                }
            }
            Log.d(XiaoMiUACDetectService.TAG, "invalid msg id: " + msg.what);
        }
    }
}
