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
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class XiaoMiUACDetectService {
    private static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
    private static final String ACTION_SCREEN_ORIENTATION_CHANGE = "android.intent.action.CONFIGURATION_CHANGED";
    private static String DEFAULT_DEVICE_UAC_CFG = "12340";
    private static String DEFAULT_DEVICE_UL_IMBALANCE_CFG = "23451";
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
    private static String UAC_AT = "AT+ERFIDX=0,";
    private static int last_state = -1;
    private static XiaoMiUACDetectService mInstance;
    private static CommandsInterface[] sCi;
    private Context mContext;
    private DisplayManager mDisplayManager;
    private boolean mIsHeadHandOnly;
    private boolean mIsULImbalanceEnable;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private Handler mWorkerThreadHandler;
    private TelephonyManager teleManager;
    private boolean mIsGamemode = false;
    private boolean mIsCallState = false;
    private boolean mIsHandSensorReady = false;
    private boolean mIsFeatureEnable = true;
    private HandlerThread mWorkerThread = new HandlerThread("XiaoMiUACDetectService_BgThread");
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.XiaoMiUACDetectService.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == XiaoMiUACDetectService.SENSOR_TYPE_HANDSENSOR) {
                int i = (int) sensorEvent.values[0];
                if (i == 1) {
                    if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                        XiaoMiUACDetectService.this.sendAtCmdToModem(0);
                    }
                    if (XiaoMiUACDetectService.this.mIsCallState || XiaoMiUACDetectService.this.mIsGamemode || XiaoMiUACDetectService.this.isLandscape()) {
                        return;
                    }
                    XiaoMiUACDetectService.this.sendAtCmdToModem(2);
                    return;
                }
                if (i == 2) {
                    if (XiaoMiUACDetectService.this.mIsCallState && !XiaoMiUACDetectService.this.mIsGamemode) {
                        XiaoMiUACDetectService.this.sendAtCmdToModem(1);
                    }
                    if (XiaoMiUACDetectService.this.mIsCallState || XiaoMiUACDetectService.this.mIsGamemode || XiaoMiUACDetectService.this.isLandscape()) {
                        return;
                    }
                    XiaoMiUACDetectService.this.sendAtCmdToModem(3);
                    return;
                }
                if (i == 101) {
                    if (!XiaoMiUACDetectService.this.mIsCallState || XiaoMiUACDetectService.this.mIsGamemode) {
                        return;
                    }
                    XiaoMiUACDetectService.this.sendAtCmdToModem(0);
                    return;
                }
                XiaoMiUACDetectService xiaoMiUACDetectService = XiaoMiUACDetectService.this;
                if (i == 102) {
                    if (!xiaoMiUACDetectService.mIsCallState || XiaoMiUACDetectService.this.mIsGamemode) {
                        return;
                    }
                    XiaoMiUACDetectService.this.sendAtCmdToModem(1);
                    return;
                }
                if (xiaoMiUACDetectService.mIsGamemode || XiaoMiUACDetectService.this.isLandscape()) {
                    return;
                }
                XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.XiaoMiUACDetectService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(XiaoMiUACDetectService.TAG, "ACTION= " + action);
            if (action.equals(XiaoMiUACDetectService.ACTION_GAME_CHANGE_LEVEL)) {
                long longExtra = intent.getLongExtra(XiaoMiUACDetectService.LLM_LATENCY_KEY_LEVEL_UL, 1L);
                long longExtra2 = intent.getLongExtra(XiaoMiUACDetectService.LLM_LATENCY_KEY_LEVEL_DL, 1L);
                Log.d(XiaoMiUACDetectService.TAG, "level_ul = " + longExtra);
                Log.d(XiaoMiUACDetectService.TAG, "level_dl = " + longExtra2);
                if (longExtra == 2 && longExtra2 == 2) {
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
                if (!XiaoMiUACDetectService.this.isLandscape() || XiaoMiUACDetectService.this.mIsGamemode) {
                    return;
                }
                XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                return;
            }
            if (action.equals("com.android.phone.intent.action.aol_gesture_state_changed")) {
                if (intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1) == 0) {
                    XiaoMiUACDetectService.this.sendAtCmdToModem(-1);
                    XiaoMiUACDetectService.this.unRegHandsensor();
                    XiaoMiUACDetectService.this.mIsFeatureEnable = false;
                } else if (1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1)) {
                    XiaoMiUACDetectService.this.mIsFeatureEnable = true;
                    XiaoMiUACDetectService.this.regHandsensor();
                }
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.XiaoMiUACDetectService.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            if (2 == i || 1 == i) {
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
        int i = HyperOSCustFeatureResolve.getInt("mtk_me_uac_option", -1);
        Integer numValueOf = Integer.valueOf(i);
        if (i == 1) {
            Log.d(TAG, "RF uac_option 1 loaded...");
            DEFAULT_DEVICE_UAC_CFG = "01012";
            this.mIsULImbalanceEnable = false;
        } else {
            Log.d(TAG, "RF uac_option is " + numValueOf + ", not 1. Using default behavior.");
        }
        if (1 == SystemProperties.getInt("ro.boot.factorybuild", 0)) {
            Log.d(TAG, "Factory build Not support.");
            return;
        }
        this.mContext = context;
        this.mIsHeadHandOnly = SystemProperties.getBoolean(PROPERTY_UAC_SCENE_STATUS, false);
        this.mWorkerThread.start();
        setLooper(this.mWorkerThread.getLooper());
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager = displayManager;
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() { // from class: com.xiaomi.internal.telephony.XiaoMiUACDetectService.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i2) {
                if (XiaoMiUACDetectService.this.mIsGamemode) {
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 1;
                    XiaoMiUACDetectService.this.mWorkerThreadHandler.sendMessage(messageObtain);
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
        Log.d(TAG, "XiaoMiUACDetectService init done");
    }

    public static XiaoMiUACDetectService getInstance(Context context, CommandsInterface[] commandsInterfaceArr) {
        sCi = commandsInterfaceArr;
        if (mInstance == null) {
            mInstance = new XiaoMiUACDetectService(context);
        }
        return mInstance;
    }

    public void regHandsensor() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            Log.d(TAG, "obtain sensor service failed ");
        } else if (!this.mIsHandSensorReady && this.mIsFeatureEnable) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmdToModem(int i) {
        String uacAT = getUacAT(i);
        if (last_state == i || !this.mIsFeatureEnable) {
            return;
        }
        if (sCi != null) {
            int i2 = 0;
            while (true) {
                CommandsInterface[] commandsInterfaceArr = sCi;
                if (i2 >= commandsInterfaceArr.length) {
                    break;
                }
                if (1 == commandsInterfaceArr[i2].getRadioState()) {
                    sCi[i2].invokeOemRilRequestStrings(new String[]{uacAT, ""}, (Message) null);
                    Log.d(TAG, "AT cmd: " + uacAT + ", send success!!!");
                    if (!this.mIsULImbalanceEnable) {
                        break;
                    }
                    sCi[i2].invokeOemRilRequestStrings(new String[]{"MIPC_SET_DYNAMIC_ANT_BIAS", getImbalanceAT(i)}, (Message) null);
                    Log.d(TAG, "AT cmd: MIPC_SET_DYNAMIC_ANT_BIAS_" + getImbalanceAT(i) + ", send success!!!");
                    break;
                }
                Log.d(TAG, "phone[" + i2 + "] radio is unavailable or off");
                i2++;
            }
            last_state = i;
            return;
        }
        Log.e(TAG, "sCi[0] is NULL!!!");
    }

    private String getUacAT(int i) {
        if (-1 == i) {
            return "AT+ERFIDX=0,-1";
        }
        return UAC_AT + DEFAULT_DEVICE_UAC_CFG.charAt(i);
    }

    private String getImbalanceAT(int i) {
        if (-1 == i) {
            return "0";
        }
        return String.valueOf(DEFAULT_DEVICE_UL_IMBALANCE_CFG.charAt(i));
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
            if (message.what == 1) {
                try {
                    Thread.sleep(500L);
                    if (XiaoMiUACDetectService.this.isLandscape()) {
                        XiaoMiUACDetectService.this.sendAtCmdToModem(4);
                        Log.d(XiaoMiUACDetectService.TAG, "getRotation and Game, Success!");
                    }
                    Log.d(XiaoMiUACDetectService.TAG, "Handle!");
                    return;
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                    Log.d(XiaoMiUACDetectService.TAG, "Thread interrupted!");
                    return;
                } catch (Exception unused2) {
                    Log.d(XiaoMiUACDetectService.TAG, "Handle EVENT_SCREEN_ORIENTATION_OBTAIN failed!");
                    return;
                }
            }
            Log.d(XiaoMiUACDetectService.TAG, "invalid msg id: " + message.what);
        }
    }
}
