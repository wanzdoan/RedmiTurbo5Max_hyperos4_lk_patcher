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
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

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
    protected static OemHookAgent mHookAgent = null;
    private static volatile MEModuleSmartTx mInstance = null;
    private static final Object mLock = new Object();
    private static final int status_off = 0;
    private static final int status_on = 1;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private TelephonyManager teleManager;
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
                int intExtra = intent.getIntExtra("phone", -1);
                String stringExtra = intent.getStringExtra("ss");
                MELog.d(MEModuleSmartTx.TAG, "phoneId = " + intExtra + ", sim state = " + stringExtra);
                MEModuleSmartTx.this.handleSimStateChanged(intExtra, stringExtra);
            }
        }
    };
    private final SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.3
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == MEModuleSmartTx.SENSOR_TYPE_HANDSENSOR) {
                int i = (int) sensorEvent.values[0];
                MEModuleSmartTx.this.isDeviceStatic = -1 == i;
                MEModuleSmartTx.this.updateSarState();
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.5
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            if (2 == i || 1 == i) {
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
        public void handleMessage(Message message) {
            Object obj = message.obj;
            if (!(obj instanceof ArrayList)) {
                MELog.d(MEModuleSmartTx.TAG, "Invalid message object type: ".concat(obj != null ? obj.getClass().getName() : "null"));
                return;
            }
            ArrayList arrayList = (ArrayList) obj;
            if (arrayList.size() < 2) {
                MELog.e(MEModuleSmartTx.TAG, "Message result too short: " + arrayList.size());
                return;
            }
            MELog.i(MEModuleSmartTx.TAG, "Receive urc: " + arrayList.toString());
            try {
                String str = (String) arrayList.get(1);
                String[] strArrSplit = str.split(":", 2);
                if (strArrSplit.length < 2) {
                    MELog.e(MEModuleSmartTx.TAG, "Invalid format in result[1], missing colon: ".concat(str));
                    return;
                }
                String[] strArrSplit2 = strArrSplit[1].trim().split(",");
                if (strArrSplit2.length < 2) {
                    MELog.e(MEModuleSmartTx.TAG, "cmdInfoArray too short after appId: " + strArrSplit2.length + ", content: " + Arrays.toString(strArrSplit2));
                    return;
                }
                int iSafeParseInt = MEModuleSmartTx.this.safeParseInt(strArrSplit2[0], -1);
                if (iSafeParseInt != MEModuleSmartTx.APP_ID_SAR_TEST) {
                    MELog.i(MEModuleSmartTx.TAG, "appId mismatch. Expected: 20108, got: " + iSafeParseInt);
                    return;
                }
                int iSafeParseInt2 = MEModuleSmartTx.this.safeParseInt(strArrSplit2[1], -1);
                if (iSafeParseInt2 == 102) {
                    MEModuleSmartTx.this.handleSarTestEnvironmentEvent(strArrSplit2);
                } else {
                    if (iSafeParseInt2 == 103) {
                        MEModuleSmartTx.this.handleUimStateEvent(strArrSplit2);
                        return;
                    }
                    MELog.d(MEModuleSmartTx.TAG, "Unknown eventId: " + iSafeParseInt2);
                }
            } catch (Exception unused) {
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
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.teleManager = telephonyManager;
        telephonyManager.listen(this.mPhoneStateListener, 32);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    public static MEModuleSmartTx getInstance(Context context, OemHookAgent oemHookAgent) {
        MEModuleSmartTx mEModuleSmartTx;
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context must not be null");
            return null;
        }
        if (oemHookAgent == null) {
            Folme$$ExternalSyntheticBUOutline0.m("OemHookAgent must not be null");
            return null;
        }
        mHookAgent = oemHookAgent;
        MEModuleSmartTx mEModuleSmartTx2 = mInstance;
        if (mEModuleSmartTx2 != null) {
            return mEModuleSmartTx2;
        }
        synchronized (mLock) {
            try {
                mEModuleSmartTx = mInstance;
                if (mEModuleSmartTx == null) {
                    mEModuleSmartTx = new MEModuleSmartTx(context.getApplicationContext());
                    mInstance = mEModuleSmartTx;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return mEModuleSmartTx;
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
    public void handleSimStateChanged(int i, String str) {
        synchronized (this.lock) {
            boolean z = true;
            try {
                if (i == 0) {
                    if ("LOADED".equals(str)) {
                        MELog.d(TAG, "SIM_STATE_LOADED for SUB_0");
                        this.mIsSim1Loaded = true;
                        this.testsim_prop_1 = SystemProperties.getInt("vendor.gsm.sim.ril.testsim", 0);
                        triggerSimAuthIfNeeded(i);
                    } else if ("ABSENT".equals(str)) {
                        MELog.d(TAG, "SIM_STATE_ABSENT for SUB_0");
                        resetCurrentState();
                        this.testsim_prop_1 = 0;
                        this.mIsSim1Loaded = false;
                    }
                } else if (i == 1) {
                    if ("LOADED".equals(str)) {
                        MELog.d(TAG, "SIM_STATE_LOADED for SUB_1");
                        this.mIsSim2Loaded = true;
                        this.testsim_prop_2 = SystemProperties.getInt("vendor.gsm.sim.ril.testsim.2", 0);
                        triggerSimAuthIfNeeded(i);
                    } else if ("ABSENT".equals(str)) {
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

    /* JADX WARN: Code duplicated, block: B:16:0x0017 A[Catch: all -> 0x000f, TryCatch #0 {all -> 0x000f, blocks: (B:4:0x0003, B:8:0x000a, B:16:0x0017, B:18:0x001d, B:20:0x0022, B:14:0x0013, B:21:0x0024), top: B:25:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:18:0x001d A[Catch: all -> 0x000f, TryCatch #0 {all -> 0x000f, blocks: (B:4:0x0003, B:8:0x000a, B:16:0x0017, B:18:0x001d, B:20:0x0022, B:14:0x0013, B:21:0x0024), top: B:25:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:19:0x0020 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:20:0x0022 A[Catch: all -> 0x000f, TryCatch #0 {all -> 0x000f, blocks: (B:4:0x0003, B:8:0x000a, B:16:0x0017, B:18:0x001d, B:20:0x0022, B:14:0x0013, B:21:0x0024), top: B:25:0x0003 }] */
    private void triggerSimAuthIfNeeded(int i) {
        synchronized (this.lock) {
            try {
                if (!this.mIsSimAuth) {
                    if (i == 0 && this.mIsSim1AuthNeeded) {
                        performSimAuthAsync(i);
                        if (i == 0) {
                            this.mIsSim1AuthNeeded = false;
                        } else if (i == 1) {
                            this.mIsSim2AuthNeeded = false;
                        }
                    } else if (i == 1 && this.mIsSim2AuthNeeded) {
                        performSimAuthAsync(i);
                        if (i == 0) {
                            this.mIsSim1AuthNeeded = false;
                        } else if (i == 1) {
                            this.mIsSim2AuthNeeded = false;
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void performSimAuthAsync(final int i) {
        new Thread(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSmartTx.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    MELog.d(MEModuleSmartTx.TAG, "Starting SIM authentication for phoneId: " + i);
                    boolean zStartSimAuth = MEModuleSmartTx.this.startSimAuth();
                    synchronized (MEModuleSmartTx.this.lock) {
                        try {
                            if (!zStartSimAuth) {
                                MELog.e(MEModuleSmartTx.TAG, "SIM authentication failed for phoneId: " + i);
                            } else {
                                MEModuleSmartTx.this.mIsSimAuth = true;
                                MELog.d(MEModuleSmartTx.TAG, "SIM authentication succeeded for phoneId: " + i);
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
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            MELog.d(TAG, "obtain sensor service failed ");
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
                    MELog.d(MEModuleSmartTx.TAG, "AT command result: " + MEModuleSmartTx.mHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20108,100"));
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
        synchronized (this.lock) {
            try {
                int i = 1;
                if (!this.mIsCallState ? !(isTestSim() || this.mIsTestEnv) : !(isTestSim() || (this.mIsTestEnv && this.isDeviceStatic))) {
                    i = 0;
                }
                if (this.mlaststate != i) {
                    Intent intent = new Intent("com.android.phone.intent.action.SMART_TX_STATE");
                    intent.putExtra("smart_tx_state", i);
                    this.mContext.sendBroadcast(intent);
                    this.mlaststate = i;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean isTestSim() {
        boolean z;
        synchronized (this.lock) {
            try {
                z = this.mIsSimAllowed && (this.mIsTestSim || this.mIsSimAuth);
                MELog.i(TAG, "isTestSim: " + z + ", mIsSimAllowed =" + this.mIsSimAllowed + ", mIsTestSim =" + this.mIsTestSim + ", mIsSimAuth =" + this.mIsSimAuth);
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
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
        int i = SystemProperties.getInt("persist.vendor.radio.sar_test_sim", 1);
        synchronized (this.lock) {
            try {
                if (1 == i) {
                    this.mIsSimAllowed = true;
                } else {
                    this.mIsSimAllowed = false;
                }
                MELog.d(TAG, "mIsSimAllowed set to: " + this.mIsSimAllowed + " from property state: " + i);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startSimAuth() {
        boolean zStartTestCardAuth = MEModuleUim.getInstance() != null ? MEModuleUim.getInstance().startTestCardAuth() : false;
        MELog.i(TAG, "startSimAuth: " + zStartTestCardAuth);
        return zStartTestCardAuth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:8:0x0019  */
    public void handleSarTestEnvironmentEvent(String[] strArr) {
        MELog.d(TAG, "SAR_TEST_ENV_CHANGE_DETECTED...");
        try {
            int length = strArr.length;
            boolean z = true;
            if (length == 3) {
                if (safeParseInt(strArr[2], 0) != 1) {
                    z = false;
                }
            } else if (length < 5) {
                MELog.e(TAG, "Invalid cmdInfoArray length for SAR event: " + length);
                return;
            } else {
                int iSafeParseInt = safeParseInt(strArr[3], 0);
                int iSafeParseInt2 = safeParseInt(strArr[4], 0);
                if (iSafeParseInt != 1 && iSafeParseInt2 != 1) {
                    z = false;
                }
            }
            MELog.d(TAG, "mIsTestEnv: " + z);
            synchronized (this.lock) {
                try {
                    if (this.mIsTestEnv != z) {
                        this.mIsTestEnv = z;
                        updateSarState();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception unused) {
            MELog.e(TAG, "Failed to parse SAR test environment state");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUimStateEvent(String[] strArr) {
        MELog.d(TAG, "UIM_STATE_IND...");
        if (strArr.length < 6) {
            MELog.e(TAG, "UIM_STATE_IND: Insufficient data length: " + strArr.length);
            return;
        }
        int iSafeParseInt = safeParseInt(strArr[2], -1);
        if (iSafeParseInt != 3) {
            MELog.d(TAG, "Unsupported UIM version: " + iSafeParseInt);
            return;
        }
        int iSafeParseInt2 = safeParseInt(strArr[5], -1);
        MELog.d(TAG, "uimAuthState: " + iSafeParseInt2);
        if (iSafeParseInt2 == 1) {
            handleUimAuthorized();
            return;
        }
        if (iSafeParseInt2 == 2) {
            resetCurrentState();
            setSimUpdateFlag();
            updateSarState();
        } else {
            MELog.d(TAG, "Unknown uim_auth_state: " + iSafeParseInt2);
        }
    }

    private void handleUimAuthorized() {
        synchronized (this.lock) {
            try {
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
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int safeParseInt(String str, int i) {
        if (str != null) {
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException unused) {
            }
        }
        return i;
    }
}
