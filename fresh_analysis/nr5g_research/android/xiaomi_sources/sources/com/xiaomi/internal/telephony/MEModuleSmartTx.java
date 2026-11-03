package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSmartTx {
    private static final int APP_ID_SAR_TEST = 20108;
    private static final int DEVICE_STATE_STATIC = -1;
    private static final int EVENT_AT_URC_IND = 102;
    private static final int EVENT_AT_URC_UIM_STATE_IND = 103;
    private static final int SENSOR_TYPE_HANDSENSOR = 33171120;
    private static final int SUB_0 = 0;
    private static final int SUB_1 = 1;
    private static final int SUB_ALL = 2;
    private static final String TAG = "MEModuleSmartTx";
    private static final int UIM_AUTH_STATE_AUTHORIZED = 1;
    private static final int UIM_SIM_ABSENT = 2;
    private static final int UIM_STATE_OFF = 0;
    private static final int UIM_STATE_ON = 1;
    private static final int VERSION_UIM_V3 = 3;
    private static volatile MEModuleSmartTx mInstance = null;
    private static final int status_off = 0;
    private static final int status_on = 1;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private TelephonyManager teleManager;
    private static final Object mLock = new Object();
    protected static OemHookAgent mHookAgent = null;
    private boolean mIsTestSim = false;
    private boolean mIsTestEnv = false;
    private boolean isDeviceStatic = true;
    private boolean mIsCallState = false;
    private boolean mIsFeatureEnable = true;
    private boolean mIsHandSensorReady = false;
    private boolean mIsSimAuth = false;
    private boolean mIsSimAllowed = true;
    private int testsim_prop_1 = 0;
    private int testsim_prop_2 = 0;
    private volatile boolean mIsSim1Loaded = false;
    private volatile boolean mIsSim2Loaded = false;
    private volatile boolean mIsSim1AuthNeeded = false;
    private volatile boolean mIsSim2AuthNeeded = false;
    private int mlaststate = -1;
    private final Object lock = new Object();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                MELog.d(MEModuleSmartTx.TAG, "mBroadcastReceiver, intent is null");
                return;
            }
            String action = intent.getAction();
            MELog.d(MEModuleSmartTx.TAG, "BroadcastReceiver action: " + action);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                int phoneId = intent.getIntExtra("phone", -1);
                String simState = intent.getStringExtra("ss");
                MELog.d(MEModuleSmartTx.TAG, "phoneId = " + phoneId + ", sim state = " + simState);
                MEModuleSmartTx.this.handleSimStateChanged(phoneId, simState);
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.3
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == MEModuleSmartTx.SENSOR_TYPE_HANDSENSOR) {
                int handSensorState = (int) event.values[0];
                MEModuleSmartTx.this.isDeviceStatic = -1 == handSensorState;
                MEModuleSmartTx.this.updateSarState();
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.5
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            if (2 == state || 1 == state) {
                MEModuleSmartTx.this.mIsCallState = true;
                MELog.d(MEModuleSmartTx.TAG, "mIsCallState: " + MEModuleSmartTx.this.mIsCallState);
                MEModuleSmartTx.this.regHandsensor();
            } else {
                MEModuleSmartTx.this.unRegHandsensor();
                MEModuleSmartTx.this.mIsCallState = false;
                MELog.d(MEModuleSmartTx.TAG, "mIsCallState: " + MEModuleSmartTx.this.mIsCallState);
            }
            MEModuleSmartTx.this.updateSarState();
        }
    };
    public Handler mHandler = new Handler() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.6
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (!(msg.obj instanceof ArrayList)) {
                MELog.d(MEModuleSmartTx.TAG, "Invalid message object type: " + (msg.obj != null ? msg.obj.getClass().getName() : "null"));
                return;
            }
            ArrayList<String> result = (ArrayList) msg.obj;
            if (result.size() < 2) {
                MELog.e(MEModuleSmartTx.TAG, "Message result too short: " + result.size());
                return;
            }
            MELog.i(MEModuleSmartTx.TAG, "Receive urc: " + result.toString());
            try {
                String rawCmd = result.get(1);
                String[] parts = rawCmd.split(":", 2);
                if (parts.length < 2) {
                    MELog.e(MEModuleSmartTx.TAG, "Invalid format in result[1], missing colon: " + rawCmd);
                    return;
                }
                String cmdInfoStr = parts[1].trim();
                String[] cmdInfoArray = cmdInfoStr.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                if (cmdInfoArray.length < 2) {
                    MELog.e(MEModuleSmartTx.TAG, "cmdInfoArray too short after appId: " + cmdInfoArray.length + ", content: " + Arrays.toString(cmdInfoArray));
                    return;
                }
                int appId = MEModuleSmartTx.this.safeParseInt(cmdInfoArray[0], -1);
                if (appId == MEModuleSmartTx.APP_ID_SAR_TEST) {
                    int eventId = MEModuleSmartTx.this.safeParseInt(cmdInfoArray[1], -1);
                    switch (eventId) {
                        case 102:
                            MEModuleSmartTx.this.handleSarTestEnvironmentEvent(cmdInfoArray);
                            break;
                        case 103:
                            MEModuleSmartTx.this.handleUimStateEvent(cmdInfoArray);
                            break;
                        default:
                            MELog.d(MEModuleSmartTx.TAG, "Unknown eventId: " + eventId);
                            break;
                    }
                    return;
                }
                MELog.i(MEModuleSmartTx.TAG, "appId mismatch. Expected: 20108, got: " + appId);
            } catch (Exception e) {
                MELog.e(MEModuleSmartTx.TAG, "Failed to parse message header or appId");
            }
        }
    };

    private MEModuleSmartTx(Context context) {
        MELog.d(TAG, "MEModuleSmartTx init...");
        this.mContext = context;
        enableModemCB();
        initRegisterAtUrc();
        initMEModuleUim();
        this.teleManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.teleManager.listen(this.mPhoneStateListener, 32);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
    }

    public static MEModuleSmartTx getInstance(Context context, OemHookAgent hookAgent) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (hookAgent == null) {
            throw new IllegalArgumentException("OemHookAgent must not be null");
        }
        mHookAgent = hookAgent;
        MEModuleSmartTx instance = mInstance;
        if (instance == null) {
            synchronized (mLock) {
                instance = mInstance;
                if (instance == null) {
                    instance = new MEModuleSmartTx(context.getApplicationContext());
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private void initMEModuleUim() {
        if (MEModuleUim.getInstance() == null) {
            new MEModuleUim(this.mContext);
            MELog.d(TAG, "MEModuleUim initialized.");
        } else {
            MELog.d(TAG, "MEModuleUim already initialized.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimStateChanged(int phoneId, String simState) {
        synchronized (this.lock) {
            boolean z = true;
            try {
                if (phoneId == 0) {
                    if ("LOADED".equals(simState)) {
                        MELog.d(TAG, "SIM_STATE_LOADED for SUB_0");
                        this.mIsSim1Loaded = true;
                        this.testsim_prop_1 = SystemProperties.getInt("vendor.gsm.sim.ril.testsim", 0);
                        triggerSimAuthIfNeeded(phoneId);
                    } else if ("ABSENT".equals(simState)) {
                        MELog.d(TAG, "SIM_STATE_ABSENT for SUB_0");
                        resetCurrentState();
                        this.testsim_prop_1 = 0;
                        this.mIsSim1Loaded = false;
                    }
                } else if (phoneId == 1) {
                    if ("LOADED".equals(simState)) {
                        MELog.d(TAG, "SIM_STATE_LOADED for SUB_1");
                        this.mIsSim2Loaded = true;
                        this.testsim_prop_2 = SystemProperties.getInt("vendor.gsm.sim.ril.testsim.2", 0);
                        triggerSimAuthIfNeeded(phoneId);
                    } else if ("ABSENT".equals(simState)) {
                        MELog.d(TAG, "SIM_STATE_ABSENT for SUB_1");
                        resetCurrentState();
                        this.testsim_prop_2 = 0;
                        this.mIsSim2Loaded = false;
                    }
                }
                if (this.testsim_prop_1 != 1 && this.testsim_prop_2 != 1) {
                    z = false;
                }
                this.mIsTestSim = z;
                MELog.d(TAG, "testsim_prop_1 = " + this.testsim_prop_1 + ", testsim_prop_2 = " + this.testsim_prop_2);
                updateSarState();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void triggerSimAuthIfNeeded(int phoneId) {
        synchronized (this.lock) {
            if (!this.mIsSimAuth && ((phoneId == 0 && this.mIsSim1AuthNeeded) || (phoneId == 1 && this.mIsSim2AuthNeeded))) {
                performSimAuthAsync(phoneId);
                if (phoneId == 0) {
                    this.mIsSim1AuthNeeded = false;
                } else if (phoneId == 1) {
                    this.mIsSim2AuthNeeded = false;
                }
            }
        }
    }

    private void performSimAuthAsync(final int phoneId) {
        new Thread(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    MELog.d(MEModuleSmartTx.TAG, "Starting SIM authentication for phoneId: " + phoneId);
                    boolean authResult = MEModuleSmartTx.this.startSimAuth();
                    synchronized (MEModuleSmartTx.this.lock) {
                        try {
                            if (authResult) {
                                MEModuleSmartTx.this.mIsSimAuth = true;
                                MELog.d(MEModuleSmartTx.TAG, "SIM authentication succeeded for phoneId: " + phoneId);
                            } else {
                                MELog.e(MEModuleSmartTx.TAG, "SIM authentication failed for phoneId: " + phoneId);
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    MEModuleSmartTx.this.updateSarState();
                } catch (Exception e) {
                    MELog.e(MEModuleSmartTx.TAG, "Error during SIM authentication: " + e.getMessage());
                }
            }
        }).start();
    }

    public void regHandsensor() {
        if (this.mSensorManager == null) {
            MELog.d(TAG, "obtain sensor service failed ");
        } else if (!this.mIsHandSensorReady && this.mIsFeatureEnable) {
            this.mSensorSubstanceHand = this.mSensorManager.getDefaultSensor(SENSOR_TYPE_HANDSENSOR);
            boolean mSensorFlag = this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorSubstanceHand, 3);
            if (this.mSensorSubstanceHand == null || !mSensorFlag) {
                this.mIsHandSensorReady = false;
            } else {
                this.mIsHandSensorReady = true;
            }
        }
        MELog.d(TAG, "regHandsensor mSensorFlag = " + this.mIsHandSensorReady);
    }

    public void unRegHandsensor() {
        if (this.mIsHandSensorReady) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstanceHand);
            this.mIsHandSensorReady = false;
        }
        MELog.d(TAG, "unRegHandsensor mSensorFlag = " + this.mIsHandSensorReady);
    }

    private void enableModemCB() {
        new Thread(new Runnable(this) { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.4
            @Override // java.lang.Runnable
            public void run() {
                if (MEModuleSmartTx.mHookAgent != null) {
                    MELog.d(MEModuleSmartTx.TAG, "enableModemCB.");
                    String result = MEModuleSmartTx.mHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20108,100");
                    MELog.d(MEModuleSmartTx.TAG, "AT command result: " + result);
                    return;
                }
                MELog.e(MEModuleSmartTx.TAG, "mHookAgent is null, cannot send AT command.");
            }
        }).start();
    }

    private void initRegisterAtUrc() {
        mHookAgent.onRegisterAtUrcInd(2, 102, this.mHandler);
        mHookAgent.onRegisterAtUrcInd(2, 103, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSarState() {
        int mcurstate = 0;
        synchronized (this.lock) {
            if (this.mIsCallState) {
                if (isTestSim() || (this.mIsTestEnv && this.isDeviceStatic)) {
                    mcurstate = 1;
                }
            } else if (isTestSim() || this.mIsTestEnv) {
                mcurstate = 1;
            }
            if (this.mlaststate != mcurstate) {
                Intent intent = new Intent("com.android.phone.intent.action.SMART_TX_STATE");
                intent.putExtra("smart_tx_state", mcurstate);
                this.mContext.sendBroadcast(intent);
                this.mlaststate = mcurstate;
            }
        }
    }

    private boolean isTestSim() {
        boolean result;
        synchronized (this.lock) {
            result = false;
            if (this.mIsSimAllowed && (this.mIsTestSim || this.mIsSimAuth)) {
                result = true;
            }
            MELog.i(TAG, "isTestSim: " + result + ", mIsSimAllowed =" + this.mIsSimAllowed + ", mIsTestSim =" + this.mIsTestSim + ", mIsSimAuth =" + this.mIsSimAuth);
        }
        return result;
    }

    private void resetCurrentState() {
        synchronized (this.lock) {
            this.mIsTestSim = false;
            this.mIsSimAuth = false;
            this.mIsTestEnv = false;
            this.mIsSim1AuthNeeded = false;
            this.mIsSim2AuthNeeded = false;
        }
    }

    private void setSimUpdateFlag() {
        int state = SystemProperties.getInt("persist.vendor.radio.sar_test_sim", 1);
        synchronized (this.lock) {
            try {
                if (1 == state) {
                    this.mIsSimAllowed = true;
                } else {
                    this.mIsSimAllowed = false;
                }
                MELog.d(TAG, "mIsSimAllowed set to: " + this.mIsSimAllowed + " from property state: " + state);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startSimAuth() {
        boolean authResult = false;
        if (MEModuleUim.getInstance() != null) {
            authResult = MEModuleUim.getInstance().startTestCardAuth();
        }
        MELog.i(TAG, "startSimAuth: " + authResult);
        return authResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSarTestEnvironmentEvent(String[] cmdInfoArray) {
        MELog.d(TAG, "SAR_TEST_ENV_CHANGE_DETECTED...");
        try {
            int length = cmdInfoArray.length;
            boolean newState = true;
            if (length == 3) {
                if (safeParseInt(cmdInfoArray[2], 0) != 1) {
                    newState = false;
                }
            } else if (length >= 5) {
                int lteState = safeParseInt(cmdInfoArray[3], 0);
                int nrState = safeParseInt(cmdInfoArray[4], 0);
                if (lteState != 1 && nrState != 1) {
                    newState = false;
                }
            } else {
                MELog.e(TAG, "Invalid cmdInfoArray length for SAR event: " + length);
                return;
            }
            MELog.d(TAG, "mIsTestEnv: " + newState);
            synchronized (this.lock) {
                if (this.mIsTestEnv != newState) {
                    this.mIsTestEnv = newState;
                    updateSarState();
                }
            }
        } catch (Exception e) {
            MELog.e(TAG, "Failed to parse SAR test environment state");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUimStateEvent(String[] cmdInfoArray) {
        MELog.d(TAG, "UIM_STATE_IND...");
        if (cmdInfoArray.length < 6) {
            MELog.e(TAG, "UIM_STATE_IND: Insufficient data length: " + cmdInfoArray.length);
        }
        int version = safeParseInt(cmdInfoArray[2], -1);
        if (version != 3) {
            MELog.d(TAG, "Unsupported UIM version: " + version);
            return;
        }
        int uimAuthState = safeParseInt(cmdInfoArray[5], -1);
        MELog.d(TAG, "uimAuthState: " + uimAuthState);
        switch (uimAuthState) {
            case 1:
                handleUimAuthorized();
                break;
            case 2:
                resetCurrentState();
                setSimUpdateFlag();
                updateSarState();
                break;
            default:
                MELog.d(TAG, "Unknown uim_auth_state: " + uimAuthState);
                break;
        }
    }

    private void handleUimAuthorized() {
        synchronized (this.lock) {
            MELog.d(TAG, "handleUimAuthorized.");
            if (this.mIsSim1Loaded) {
                if (!this.mIsSimAuth) {
                    this.mIsSim1AuthNeeded = true;
                    triggerSimAuthIfNeeded(0);
                }
            } else {
                this.mIsSim1AuthNeeded = true;
            }
            if (this.mIsSim2Loaded) {
                if (!this.mIsSimAuth) {
                    this.mIsSim2AuthNeeded = true;
                    triggerSimAuthIfNeeded(1);
                }
            } else {
                this.mIsSim2AuthNeeded = true;
            }
            updateSarState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int safeParseInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
