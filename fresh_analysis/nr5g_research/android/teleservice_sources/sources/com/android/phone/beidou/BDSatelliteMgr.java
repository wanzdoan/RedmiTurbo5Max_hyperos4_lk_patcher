package com.android.phone.beidou;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import miui.telephony.SubscriptionManager;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class BDSatelliteMgr {
    private static final String TAG = "BDSatelliteMgr";
    private static volatile BDSatelliteMgr sInstance;
    private BDSatelliteDynInfo mBDSatelliteDynInfo;
    private Context mContext;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private boolean mIsCalibrating;
    private long mLastCalibrateSuccessTime;
    private long mLastUnreliableTime;
    private SatInfoListener mListener;
    private Sensor mOrientation;
    private SensorManager mSensorManager;
    private final int CALIBRATE_INTERVAL = 3000;
    public int mSatMode = 10;
    public int mSlotId = SubscriptionManager.INVALID_SLOT_ID;
    private boolean mIgnoreSensorAdjust = false;
    private final BdSmsController.IBdStatusListener mBdStatusListener = new BdSmsController.IBdStatusListener() { // from class: com.android.phone.beidou.BDSatelliteMgr.1
        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdServiceStatus(boolean z, int i) {
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateAuthKey(boolean z, int i) {
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateEphData(boolean z, int i) {
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSysStatus(int i, int i2) {
            Log.i(BDSatelliteMgr.TAG, "notifyBdSysStatus -> status: " + i + ", countdown: " + i2);
            BDSysStatus byValue = BDSysStatus.getByValue(i);
            if (byValue == null) {
                Log.i(BDSatelliteMgr.TAG, "unknown bd sys status.");
            } else if (BDSatelliteMgr.this.mBDSatelliteDynInfo != null) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mSysStatus = byValue;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mCountdown = i2;
            }
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSatelliteStatus(int i, int i2, int i3, int i4, int i5) {
            Log.i(BDSatelliteMgr.TAG, "notifyBdSysStatus -> azimuthDelta: " + i + ", pitchAngleDelta: " + i2 + ", strength: " + i3);
            if (BDSatelliteMgr.this.mBDSatelliteDynInfo != null) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mSateId = i4;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mInOpenSpace = true;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mAzimuthDelta = -i;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mTargetAzimuth = i + BDSatelliteMgr.this.mBDSatelliteDynInfo.mTrueAzimuth;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mElevationDelta = i2;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mTargetElevation = i2 + BDSatelliteMgr.this.mBDSatelliteDynInfo.mTrueElevation;
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mSignalStrength = i3;
            }
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSendSmsStatus(int i, int i2, int i3, int i4) {
            if (BDSmsSendStatus.getByValue(i3) == null) {
                Log.i(BDSatelliteMgr.TAG, "unknown bd sms status.");
            } else if (BDSatelliteMgr.this.mBDSatelliteDynInfo != null) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mSendInfo = new BDSatelliteDynInfo.BDSmsSendInfo(BDSmsSendStatus.getByValue(i3), BDSmsSendFailedCause.getByValue(i4));
            }
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdReceiveSmsStatus(boolean z, int i, String str, String str2) {
            Log.i(BDSatelliteMgr.TAG, "notifyBdReceiveSmsStatus -> status: " + z + ", cause: " + i);
            boolean zIsEmpty = TextUtils.isEmpty(str2) ^ true;
            if (BDSatelliteMgr.this.mBDSatelliteDynInfo != null) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mRecInfo = new BDSatelliteDynInfo.BDSmsRecInfo(z, BDSmsRecFailedCause.getByValue(i), zIsEmpty);
            }
        }
    };
    private final SensorEventListener mOrientationEventListener = new SensorEventListener() { // from class: com.android.phone.beidou.BDSatelliteMgr.2
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (BDSatelliteMgr.this.mBDSatelliteDynInfo != null) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mSensorAccuracy = sensorEvent.accuracy;
            }
            BDSatelliteMgr bDSatelliteMgr = BDSatelliteMgr.this;
            int i = sensorEvent.accuracy;
            float[] fArr = sensorEvent.values;
            bDSatelliteMgr.checkMagneSensorAccuracy(i, (int) fArr[1], (int) fArr[2], (int) fArr[0]);
            if (sensorEvent.accuracy < 3 || BDSatelliteMgr.this.mBDSatelliteDynInfo == null) {
                return;
            }
            if (sensorEvent.sensor.getType() == 3) {
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mTrueAzimuth = sensorEvent.values[0];
                BDSatelliteMgr.this.mBDSatelliteDynInfo.mTrueElevation = sensorEvent.values[1];
                BDSatelliteMgr bDSatelliteMgr2 = BDSatelliteMgr.this;
                bDSatelliteMgr2.notifyInfoChanged(bDSatelliteMgr2.mBDSatelliteDynInfo);
                return;
            }
            Log.i(BDSatelliteMgr.TAG, "onSensorChange invalid sensor type!");
        }
    };

    public interface SatInfoListener {
        void onAccuracyChanged(boolean z, int i, int i2, int i3);

        void onSatInfoChanged(BDSatelliteDynInfo bDSatelliteDynInfo);
    }

    private BDSatelliteMgr() {
    }

    public static BDSatelliteMgr getInstance() {
        if (sInstance == null) {
            sInstance = new BDSatelliteMgr();
        }
        return sInstance;
    }

    public BDSatelliteMgr setUp(Context context, int i, int i2) {
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("The context must be non-null.");
            return null;
        }
        this.mContext = context;
        this.mSlotId = i;
        this.mSatMode = i2;
        return sInstance;
    }

    public BDSatelliteMgr setUp(Context context, int i) {
        return setUp(context, i, 10);
    }

    public void init() {
        if (this.mContext == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("Before initializing Satellite Manager, please call the method setup to set the context field.");
            return;
        }
        createThreadHandler();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(0);
        }
        Log.i(TAG, "init align satellite service!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInit() {
        Log.i(TAG, "handleInit!");
        this.mIgnoreSensorAdjust = false;
        this.mBDSatelliteDynInfo = new BDSatelliteDynInfo();
        BDSatellitePresenter.getInstance().setUp(this.mContext);
        BDSatellitePresenter.getInstance().showAliSatellite();
        registerBdListener();
        initSensor();
    }

    public void stop() {
        Log.i(TAG, "destroy align satellite service!");
        BDSatellitePresenter.getInstance().closeAliSatellite();
        stopSensor();
        unregisterBdListener();
        clearThreadHandler();
        this.mBDSatelliteDynInfo = null;
    }

    private void registerBdListener() {
        Log.i(TAG, "registerBdListener!");
        BdSmsController.getInstance();
        BdSmsController.registerBdStatusListener(this.mBdStatusListener);
    }

    private void unregisterBdListener() {
        Log.i(TAG, "unregisterBdListener.");
        BdSmsController.getInstance();
        BdSmsController.unregisterBdStatusListener(this.mBdStatusListener);
    }

    private void createThreadHandler() {
        HandlerThread handlerThread = new HandlerThread("bd_sate_thread_handler");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.android.phone.beidou.BDSatelliteMgr.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Object obj = message.obj;
                int i = message.what;
                if (i == 0) {
                    BDSatelliteMgr.this.handleInit();
                    return;
                }
                if (i == 1) {
                    if (obj instanceof BDSatelliteDynInfo) {
                        BDSatelliteMgr.this.notifyInfoChanged((BDSatelliteDynInfo) obj);
                    }
                } else {
                    Log.i(BDSatelliteMgr.TAG, "handleMessage invalid msg " + message.what);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInfoChanged(BDSatelliteDynInfo bDSatelliteDynInfo) {
        Log.i(TAG, bDSatelliteDynInfo.toString());
        SatInfoListener satInfoListener = this.mListener;
        if (satInfoListener != null) {
            satInfoListener.onSatInfoChanged(bDSatelliteDynInfo);
        }
    }

    private void initSensor() {
        String str = TAG;
        Log.i(str, "initSensor enter!");
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        Sensor defaultSensor = sensorManager.getDefaultSensor(3);
        this.mOrientation = defaultSensor;
        if (defaultSensor == null) {
            Log.e(str, "initSensor failed to get sensor!");
        } else {
            Log.i(str, "initSensor success!");
            this.mSensorManager.registerListener(this.mOrientationEventListener, this.mOrientation, 2);
        }
    }

    private void stopSensor() {
        Log.i(TAG, "stopSensor enter!");
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this.mOrientationEventListener);
            this.mOrientation = null;
            this.mSensorManager = null;
        }
    }

    private void clearThreadHandler() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
    }

    public void registerSatInfoListener(SatInfoListener satInfoListener) {
        this.mListener = satInfoListener;
        satInfoListener.onSatInfoChanged(this.mBDSatelliteDynInfo);
    }

    public void unregisterSatInfoListener(SatInfoListener satInfoListener) {
        if (this.mListener == satInfoListener) {
            this.mListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMagneSensorAccuracy(int i, int i2, int i3, int i4) {
        if (this.mBDSatelliteDynInfo == null || this.mListener == null || this.mIgnoreSensorAdjust || BDSatellitePresenter.getInstance().getActivity() == null || (i >= 3 && !this.mIsCalibrating)) {
            if (i >= 3 && !this.mIgnoreSensorAdjust) {
                Log.d(TAG, "trigger sms action, mode = " + BdSmsController.getInstance().getBdSmsMode());
                this.mIgnoreSensorAdjust = true;
                BdSmsController.getInstance().triggerSmsAction();
                return;
            }
            if (this.mIgnoreSensorAdjust) {
                Log.d(TAG, "ignore sensor adjust.");
                return;
            }
            return;
        }
        if (i < 3 && System.currentTimeMillis() - this.mLastCalibrateSuccessTime > 3000) {
            if (!this.mIsCalibrating) {
                this.mIsCalibrating = true;
                this.mLastUnreliableTime = System.currentTimeMillis();
                Log.i(TAG, "accuracy is not trusted, require calibration");
            }
        } else if (this.mIsCalibrating && System.currentTimeMillis() - this.mLastUnreliableTime > 3000) {
            Log.i(TAG, "accuracy is trusted, calibrated completly!");
            HapticFeedbackTool.getInstance().performCalibration();
            this.mIsCalibrating = false;
            this.mLastCalibrateSuccessTime = System.currentTimeMillis();
        }
        this.mListener.onAccuracyChanged(this.mIsCalibrating, i2, i3, i4);
    }

    public float getPhoneAzimuth() {
        BDSatelliteDynInfo bDSatelliteDynInfo = this.mBDSatelliteDynInfo;
        if (bDSatelliteDynInfo == null) {
            return 2.1474836E9f;
        }
        return bDSatelliteDynInfo.mTrueAzimuth;
    }

    public float getPhoneElevation() {
        BDSatelliteDynInfo bDSatelliteDynInfo = this.mBDSatelliteDynInfo;
        if (bDSatelliteDynInfo == null) {
            return 2.1474836E9f;
        }
        return bDSatelliteDynInfo.mTrueElevation;
    }

    public boolean isSensorReady() {
        return this.mIgnoreSensorAdjust;
    }
}
