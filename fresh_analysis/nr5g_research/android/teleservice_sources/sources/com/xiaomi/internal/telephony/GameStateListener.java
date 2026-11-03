package com.xiaomi.internal.telephony;

import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.xiaomi.modem.ModemUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import miui.process.ForegroundInfo;
import miui.process.IForegroundInfoListener;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class GameStateListener {
    private static final String BC_INTENT_ACTION_SCREEN_ORIENTATION_CHANGE = "android.intent.action.CONFIGURATION_CHANGED";
    public static final int HORIZON_GAME_OFF = 0;
    public static final int HORIZON_GAME_ON = 1;
    private static final String LOG_TAG = "GameStateListener";
    private static final int STATE_LISTENER_STATE_IDLE = 0;
    private static final int STATE_LISTENER_STATE_RUNING = 1;
    public static final int TYPE_HORIZONTAL_GAME_LIST_STATE = 2;
    public static final int TYPE_LANDSCAPE_GAME_STATE = 1;
    private static Map mListenerStateMap = new HashMap();
    private static Map mRegRequestInfoMap = new HashMap();
    private static Context mContext = null;
    private static GameStateListener mInstance = null;
    private static int mCurrentLandScapeGameState = 0;
    private static MotionDetector mMotionDetector = null;
    private static volatile int mCurrentHorizontalGameListState = 0;

    public static void registerGameStateListener(Context context, int i, Handler handler, int i2, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerGameStateListener, context = " + context + ", eventType = " + i + ", handler = " + handler + ", what = " + i2 + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    RegistrantList registrantList = new RegistrantList();
                    registrantList.addUnique(handler, i2, obj);
                    mRegRequestInfoMap.put(Integer.valueOf(i), registrantList);
                } else {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).addUnique(handler, i2, obj);
                }
                startStateChangeListener(i, new Registrant(handler, i2, obj));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void unregisterGameStateChangeEvent(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterGameStateChangeEvent, eventType = " + i + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    return;
                }
                ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).remove(handler);
                if (((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).size() == 0) {
                    stopDeviceStateChangeListener(i, handler);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static synchronized void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GameStateListener(context);
            ModemSarLog.d(LOG_TAG, "initInstance done");
        }
    }

    private GameStateListener(Context context) {
        mContext = context;
    }

    private static void startStateChangeListener(int i, Registrant registrant) {
        ModemSarLog.d(LOG_TAG, "startStateChangeListener, eventType = " + i + ", r = " + registrant);
        synchronized (mListenerStateMap) {
            try {
                if (i != 1) {
                    if (i == 2) {
                        if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                            HorizontalGameListListener.startGameListListenner();
                            mListenerStateMap.put(Integer.valueOf(i), 1);
                        } else {
                            notifyDeviceStateChangeEvent(2, registrant, mCurrentHorizontalGameListState);
                        }
                    }
                } else if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                    if (mMotionDetector == null) {
                        mMotionDetector = new MotionDetector();
                    }
                    MotionDetector motionDetector = mMotionDetector;
                    if (motionDetector != null) {
                        motionDetector.start();
                        mListenerStateMap.put(Integer.valueOf(i), 1);
                    }
                } else {
                    notifyDeviceStateChangeEvent(1, registrant, mCurrentLandScapeGameState);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void stopDeviceStateChangeListener(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + i + ", h = " + handler);
        synchronized (mListenerStateMap) {
            try {
                if (i == 1) {
                    MotionDetector motionDetector = mMotionDetector;
                    if (motionDetector != null) {
                        motionDetector.stop();
                        mListenerStateMap.put(Integer.valueOf(i), 0);
                        mCurrentLandScapeGameState = 0;
                    }
                } else if (i == 2 && ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 1) {
                    HorizontalGameListListener.stopGameListListenner();
                    mListenerStateMap.put(Integer.valueOf(i), 0);
                    mCurrentHorizontalGameListState = 0;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + i + ", intValue = " + i2);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) != null) {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).notifyRegistrants(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void notifyDeviceStateChangeEvent(int i, Registrant registrant, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + i + ", r = " + registrant + ", intValue = " + i2);
        if (registrant == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
        }
    }

    class MotionDetector implements SensorEventListener {
        private static final float ACCEL_THRESHOLD = 0.5f;
        private static final float ACC_CHANGE_THRESHOLD = 0.008f;
        private static final float ALPHA = 0.1f;
        private static final long ANOMALY_DURATION_MS = 5000;
        private static final float ANOMALY_THRESHOLD = 19.6f;
        private static final int DYNAMIC_COUNT_THRESHOLD = 10;
        private static final float GRAVITY_EARTH = 9.8f;
        private static final float GYRO_THRESHOLD = 0.005f;
        private static final int STATIC_COUNT_THRESHOLD = 40;
        private Sensor accelerometer;
        private Handler backgroundHandler;
        private Display display;
        private Sensor gyroscope;
        private HandlerThread handlerThread;
        private long lastSensorUpdateTimestamp;
        private SensorManager sensorManager;
        private Runnable sensorTimeoutTask;
        private LinkedList accelVectorSumQueue = new LinkedList();
        private long lastAnomalyTimestamp = 0;
        private boolean isCurStationary = false;
        private boolean isLastStationary = false;
        private boolean isListeningToMotion = false;
        private boolean isFirstCall = true;
        private boolean isReceiverRegistered = false;
        private float[] accelValues = new float[3];
        private float[] accelValues_last = new float[3];
        private float[] gyroValues = new float[3];
        private int staticCnt = 0;
        private int dynamicCnt = 0;
        private int mLastLandScapeGameState = -1;
        private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.GameStateListener.MotionDetector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (GameStateListener.BC_INTENT_ACTION_SCREEN_ORIENTATION_CHANGE.equals(intent.getAction())) {
                    MotionDetector.this.updateLandScapeGameState();
                }
            }
        };

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        class VectorSumWithTimestamp {
            final float sum;
            final long timestamp;

            VectorSumWithTimestamp(float f, long j) {
                this.sum = f;
                this.timestamp = j;
            }
        }

        public MotionDetector() {
            SensorManager sensorManager = (SensorManager) GameStateListener.mContext.getSystemService("sensor");
            this.sensorManager = sensorManager;
            this.accelerometer = sensorManager.getDefaultSensor(1);
            this.gyroscope = this.sensorManager.getDefaultSensor(4);
            this.display = ((WindowManager) GameStateListener.mContext.getSystemService("window")).getDefaultDisplay();
            this.lastSensorUpdateTimestamp = System.currentTimeMillis();
        }

        public void start() {
            startBcIntentListen();
            HandlerThread handlerThread = this.handlerThread;
            if (handlerThread == null || !handlerThread.isAlive()) {
                HandlerThread handlerThread2 = new HandlerThread("MotionDetectorThread");
                this.handlerThread = handlerThread2;
                handlerThread2.start();
                this.backgroundHandler = new Handler(this.handlerThread.getLooper());
            }
            if (!this.isListeningToMotion) {
                startMotionDetection();
            }
            updateLandScapeGameState();
        }

        private void startBcIntentListen() {
            if (this.isReceiverRegistered) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GameStateListener.BC_INTENT_ACTION_SCREEN_ORIENTATION_CHANGE);
            GameStateListener.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
            this.isReceiverRegistered = true;
        }

        private void stopBcIntentListen() {
            if (this.isReceiverRegistered) {
                try {
                    GameStateListener.mContext.unregisterReceiver(this.mReceiver);
                    this.isReceiverRegistered = false;
                } catch (IllegalArgumentException e) {
                    ModemSarLog.e(GameStateListener.LOG_TAG, "Failed to unregister receiver: " + e.getMessage());
                }
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            this.lastSensorUpdateTimestamp = System.currentTimeMillis();
            int type = sensorEvent.sensor.getType();
            if (type == 1) {
                lowPassFilter(sensorEvent.values, this.accelValues);
                detectStationary();
                detectAnomaly();
            } else {
                if (type != 4) {
                    return;
                }
                lowPassFilter(sensorEvent.values, this.gyroValues);
                detectStationary();
            }
        }

        private void startMotionDetection() {
            if (this.isListeningToMotion) {
                return;
            }
            boolean zRegisterListener = this.sensorManager.registerListener(this, this.accelerometer, 3);
            boolean zRegisterListener2 = this.sensorManager.registerListener(this, this.gyroscope, 3);
            if (zRegisterListener && zRegisterListener2) {
                this.isListeningToMotion = true;
                ModemSarLog.d(GameStateListener.LOG_TAG, "Accelerometer and Gyroscope Start for Motion Detection, isListeningToMotion = true.");
                startSensorTimeout();
            } else {
                if (!zRegisterListener || !zRegisterListener2) {
                    ModemSarLog.e(GameStateListener.LOG_TAG, "Failed to register Accelerometer or Gyroscope listener.");
                }
                this.isListeningToMotion = false;
            }
        }

        private void detectStationary() {
            float[] fArr = this.accelValues;
            float f = fArr[0];
            float f2 = fArr[1];
            float f3 = fArr[2];
            float fSqrt = (float) Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
            float fAbs = Math.abs(this.accelValues[0] - this.accelValues_last[0]) + Math.abs(this.accelValues[1] - this.accelValues_last[1]) + Math.abs(this.accelValues[2] - this.accelValues_last[2]);
            float[] fArr2 = this.gyroValues;
            float f4 = fArr2[0];
            float f5 = fArr2[1];
            float f6 = fArr2[2];
            boolean z = Math.abs(fSqrt - GRAVITY_EARTH) < 0.5f && fAbs < ACC_CHANGE_THRESHOLD && ((float) Math.sqrt((double) (((f4 * f4) + (f5 * f5)) + (f6 * f6)))) < GYRO_THRESHOLD;
            this.isCurStationary = z;
            if (this.isFirstCall) {
                this.isFirstCall = false;
                updateLandScapeGameState();
                this.isLastStationary = this.isCurStationary;
                ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
            } else if (!z) {
                this.staticCnt = 0;
                if (this.isLastStationary) {
                    int i = this.dynamicCnt + 1;
                    this.dynamicCnt = i;
                    if (i > 10) {
                        this.isLastStationary = z;
                        updateLandScapeGameState();
                        ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
                    }
                }
            } else {
                this.dynamicCnt = 0;
                if (!this.isLastStationary) {
                    int i2 = this.staticCnt + 1;
                    this.staticCnt = i2;
                    if (i2 > STATIC_COUNT_THRESHOLD) {
                        this.isLastStationary = z;
                        updateLandScapeGameState();
                        ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
                    }
                }
            }
            float[] fArr3 = this.accelValues;
            System.arraycopy(fArr3, 0, this.accelValues_last, 0, fArr3.length);
        }

        private void startSensorTimeout() {
            if (this.sensorTimeoutTask == null) {
                this.sensorTimeoutTask = new Runnable() { // from class: com.xiaomi.internal.telephony.GameStateListener.MotionDetector.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (System.currentTimeMillis() - MotionDetector.this.lastSensorUpdateTimestamp > 5000 && !MotionDetector.this.isCurStationary) {
                            MotionDetector.this.isCurStationary = true;
                            MotionDetector.this.isLastStationary = true;
                            MotionDetector.this.updateLandScapeGameState();
                            ModemSarLog.d(GameStateListener.LOG_TAG, "No sensor data received for 5 seconds, assuming stationary.");
                        }
                        if (MotionDetector.this.backgroundHandler != null) {
                            MotionDetector.this.backgroundHandler.postDelayed(this, 5000L);
                        }
                    }
                };
            }
            Handler handler = this.backgroundHandler;
            if (handler != null) {
                handler.removeCallbacks(this.sensorTimeoutTask);
                this.backgroundHandler.postDelayed(this.sensorTimeoutTask, 5000L);
            }
        }

        private void detectAnomaly() {
            while (!this.accelVectorSumQueue.isEmpty() && System.currentTimeMillis() - ((VectorSumWithTimestamp) this.accelVectorSumQueue.peek()).timestamp > 5000) {
                this.accelVectorSumQueue.poll();
            }
            float[] fArr = this.accelValues;
            float f = fArr[0];
            float f2 = fArr[1];
            float f3 = fArr[2];
            float fSqrt = (float) Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
            if (fSqrt > ANOMALY_THRESHOLD) {
                this.accelVectorSumQueue.offer(new VectorSumWithTimestamp(fSqrt, System.currentTimeMillis()));
            }
            if (this.accelVectorSumQueue.isEmpty() || System.currentTimeMillis() - ((VectorSumWithTimestamp) this.accelVectorSumQueue.peek()).timestamp < 5000 || !this.accelVectorSumQueue.stream().allMatch(new Predicate() { // from class: com.xiaomi.internal.telephony.GameStateListener$MotionDetector$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return GameStateListener.MotionDetector.$r8$lambda$ywqychkGnuumcg_L5GM3ItC80gg((GameStateListener.MotionDetector.VectorSumWithTimestamp) obj);
                }
            })) {
                return;
            }
            stop();
        }

        public static /* synthetic */ boolean $r8$lambda$ywqychkGnuumcg_L5GM3ItC80gg(VectorSumWithTimestamp vectorSumWithTimestamp) {
            return vectorSumWithTimestamp.sum > ANOMALY_THRESHOLD;
        }

        public void stop() {
            Runnable runnable;
            stopBcIntentListen();
            Handler handler = this.backgroundHandler;
            if (handler != null && (runnable = this.sensorTimeoutTask) != null) {
                handler.removeCallbacks(runnable);
                this.sensorTimeoutTask = null;
            }
            HandlerThread handlerThread = this.handlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
                this.handlerThread = null;
            }
            this.backgroundHandler = null;
            this.sensorManager.unregisterListener(this);
            ModemSarLog.d(GameStateListener.LOG_TAG, "All MotionDetector Related Sensors Stop, isListeningToMotion = false.");
            this.isListeningToMotion = false;
            updateLandScapeGameState();
        }

        public int getLastLandScapeGameState() {
            return this.mLastLandScapeGameState;
        }

        public void setLastLandScapeGameState(int i) {
            this.mLastLandScapeGameState = i;
        }

        public boolean isStationary() {
            return this.isCurStationary;
        }

        public boolean isListeningMotion() {
            return this.isListeningToMotion;
        }

        private void lowPassFilter(float[] fArr, float[] fArr2) {
            for (int i = 0; i < fArr.length; i++) {
                float f = fArr2[i];
                fArr2[i] = f + ((fArr[i] - f) * 0.1f);
            }
        }

        private boolean isLandscape() {
            boolean z = true;
            if (this.display.getRotation() != 1 && this.display.getRotation() != 3) {
                z = false;
                if (this.display.getRotation() != 0 && this.display.getRotation() != 2) {
                    ModemSarLog.d(GameStateListener.LOG_TAG, "getRotation, failed!");
                }
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateLandScapeGameState() {
            ModemSarLog.d(GameStateListener.LOG_TAG, "is Landscape = " + isLandscape() + ", is ListeningMotion = " + isListeningMotion() + ", is Stationary = " + isStationary() + ".");
            if (isLandscape() && !isStationary() && isListeningMotion()) {
                GameStateListener.mCurrentLandScapeGameState = 1;
            } else if (!isLandscape() && !isStationary() && isListeningMotion()) {
                GameStateListener.mCurrentLandScapeGameState = 2;
            } else {
                GameStateListener.mCurrentLandScapeGameState = 0;
            }
            if (GameStateListener.mCurrentLandScapeGameState != getLastLandScapeGameState()) {
                GameStateListener.notifyDeviceStateChangeEvent(1, GameStateListener.mCurrentLandScapeGameState);
                setLastLandScapeGameState(GameStateListener.mCurrentLandScapeGameState);
            }
        }
    }

    public class HorizontalGameListListener {
        private static final int EVENT_GAME_LIST_FOREGROUND_STATE = 1;
        private static final int EVENT_TOUCH_STATE = 2;
        private static final int GAME_LIST_IN_BACKGROUND = 0;
        private static final int GAME_LIST_IN_FOREGROUND = 1;
        private static final String LOG_TAG = "HorizontalGameListListener";
        private static final int TOUCH_DOWN = 1;
        private static final int TOUCH_UP = 0;
        private static final String TOUCH_UP_TAG = "TOUCH_UP_TAG";
        private static final int TOUCH_UP_TIMEOUT = 30000;
        private static volatile String mCurrentTopGame = null;
        private static Display mDisplay = null;
        private static Handler mHandler = null;
        private static HandlerThread mHandlerThread = null;
        private static boolean mIsGameForegroundRegistered = false;
        private static volatile boolean mIsGameInForeground = false;
        private static boolean mIsMotionEventListenerRegistered = false;
        private static volatile boolean mIsTouchActionDown = false;
        private static final Object mMotionEventListenerLock = new Object();
        private static final Set HORIZON_GAME_LIST = new HashSet(Arrays.asList("com.tencent.tmgp.sgame", "com.tencent.tmgp.pubgmhd", "com.tencent.jkchess", "com.tencent.KiHan", "com.tencent.tmgp.dfm", "com.netease.party.mi", "com.tencent.tmgp.cf", "com.tencent.mf.uam", "com.tencent.lolm", "com.miHoYo.hkrpg", "com.miHoYo.Yuanshen", "com.netease.mc.mi", "com.tencent.tmgp.supercell.clashofclans", "com.netease.dwrg.mi", "com.tencent.tmgp.dnf", "com.tencent.tmgp.cod", "com.qqgame.hlddz", "com.minitech.miniworld.TMobile.mi", "com.netease.sky.mi", "com.hypergryph.arknights", "cn.jj", "com.tencent.tmgp.speedmobile", "com.miHoYo.Nap", "com.kurogame.mingchao", "com.tencent.qqgame.xq", "com.xiaomi.tinygame", "com.netease.dwrg", "com.tencent.tmgp.supercell.brawlstars", "com.pwrd.steam.esports", "com.tencent.letsgo", "com.netease.yyslscn", "com.tencent.ig", "com.netease.sky", "com.xxmddz.mi", "com.papegames.lysk.cn", "com.blizzard.wtcg.hearthstone", "com.pi.czrxdfirst", "com.bf.sgs.hdexp.mi", "com.qqgame.happymj", "com.tencent.nikke", "com.wepie.snake.new.mi", "com.netease.yhtj.mi", "com.feiyu.luobo4.mi", "com.bilibili.azurlane", "com.popcap.pvz2cthdxm", "com.tencent.tmgp.supercell.boombeach", "com.netease.l22", "com.hypergryph.arknights.bilibili", "com.sofunny.Sausage", "com.netease.nshm", "com.shenlan.m.reverse1999", "com.tencent.fiftyone.yc", "com.hortor.juliancysj", "com.tencent.hhw", "com.papegames.lysk.cn.mi", "com.tencent.nfsonline", "com.netease.party", "com.tencent.peng", "com.knight.union.mi", "com.netease.l22.mi", "com.tencent.nba2kx", "com.xd.xdt", "com.miniworldroyale.sparkgame.mi", "com.bf.sgs.hdexp", "com.netease.dfjs.mi", "com.papegames.infinitynikki"));
        private static IForegroundInfoListener mIForegroundInfoListener = new IForegroundInfoListener.Stub() { // from class: com.xiaomi.internal.telephony.GameStateListener.HorizontalGameListListener.1
            public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) throws RemoteException {
                String str = foregroundInfo.mForegroundPackageName;
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "onForegroundInfoChanged: " + str);
                if (TextUtils.isEmpty(str)) {
                    ModemSarLog.d(HorizontalGameListListener.LOG_TAG, "onForegroundInfoChanged: packageName is empty");
                } else {
                    HorizontalGameListListener.mHandler.sendMessage(HorizontalGameListListener.mHandler.obtainMessage(1, foregroundInfo));
                }
            }
        };
        private static MiuiDeviceMotionEventListener.MiuiMotionEventListener mMotionEventListenner = new MiuiDeviceMotionEventListener.MiuiMotionEventListener() { // from class: com.xiaomi.internal.telephony.GameStateListener.HorizontalGameListListener.2
            @Override // com.xiaomi.internal.telephony.MiuiDeviceMotionEventListener.MiuiMotionEventListener
            public void onMotionEvent(MotionEvent motionEvent) {
                if (motionEvent != null) {
                    ModemSarLog.d(HorizontalGameListListener.LOG_TAG, "onMotionEvent action = " + motionEvent.getAction());
                    if (motionEvent.getAction() == 0) {
                        HorizontalGameListListener.removeTouchEvent(0);
                        if (HorizontalGameListListener.mHandler != null) {
                            HorizontalGameListListener.mHandler.sendMessage(HorizontalGameListListener.mHandler.obtainMessage(2, 1, 0));
                        }
                    } else if ((1 == motionEvent.getAction() || 3 == motionEvent.getAction()) && HorizontalGameListListener.mHandler != null) {
                        HorizontalGameListListener.mHandler.sendMessageDelayed(HorizontalGameListListener.mHandler.obtainMessage(2, 0, 0, HorizontalGameListListener.TOUCH_UP_TAG), 30000L);
                    }
                    ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "onMotionEvent mIsTouchActionDown = " + HorizontalGameListListener.mIsTouchActionDown);
                }
            }
        };

        public static void startGameListListenner() {
            init();
            registerForegroundInfoListener();
        }

        public static void stopGameListListenner() {
            unregisterMotionEventListener();
            unregisterForegroundInfoListener();
            destroy();
            updateHorizontalGameListState(0);
        }

        public static String getCurrentTopGame() {
            return mCurrentTopGame;
        }

        private static void init() {
            if (GameStateListener.mContext != null && mDisplay == null) {
                mDisplay = ((WindowManager) GameStateListener.mContext.getSystemService("window")).getDefaultDisplay();
            }
            if (mHandlerThread == null) {
                HandlerThread handlerThread = new HandlerThread("HorizontalGameListListenerBgThread");
                mHandlerThread = handlerThread;
                handlerThread.start();
                if (mHandler == null) {
                    mHandler = new GameStateListenerHandler(mHandlerThread.getLooper());
                }
            }
            ModemSarLog.i(LOG_TAG, "init");
        }

        private static void destroy() {
            Handler handler = mHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            HandlerThread handlerThread = mHandlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
                mHandlerThread = null;
                mHandler = null;
            }
            mIsTouchActionDown = false;
            mIsGameInForeground = false;
            mDisplay = null;
            ModemSarLog.i(LOG_TAG, "destroy");
        }

        private static void registerForegroundInfoListener() {
            if (mIsGameForegroundRegistered) {
                return;
            }
            ProcessManager.registerForegroundInfoListener(mIForegroundInfoListener);
            mIsGameForegroundRegistered = true;
        }

        private static void unregisterForegroundInfoListener() {
            if (mIsGameForegroundRegistered) {
                ProcessManager.unregisterForegroundInfoListener(mIForegroundInfoListener);
                mIsGameForegroundRegistered = false;
                mIsGameInForeground = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void registerMotionEventListener() {
            synchronized (mMotionEventListenerLock) {
                try {
                    if (!mIsMotionEventListenerRegistered) {
                        MiuiDeviceMotionEventListener.RegisterMiuiMotionEventListener(GameStateListener.mContext, mMotionEventListenner);
                        mIsMotionEventListenerRegistered = true;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void unregisterMotionEventListener() {
            synchronized (mMotionEventListenerLock) {
                try {
                    if (mIsMotionEventListenerRegistered) {
                        MiuiDeviceMotionEventListener.UnregisterMiuiMotionEventListener(mMotionEventListenner);
                        mIsMotionEventListenerRegistered = false;
                        mIsTouchActionDown = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void removeTouchEvent(int i) {
            if (i == 0) {
                Handler handler = mHandler;
                if (handler != null) {
                    handler.removeMessages(2, TOUCH_UP_TAG);
                    return;
                }
                return;
            }
            Handler handler2 = mHandler;
            if (handler2 != null) {
                handler2.removeMessages(2);
            }
        }

        private static boolean isGameInForeground() {
            ModemSarLog.i(LOG_TAG, "isGameInForeground = " + mIsGameInForeground);
            return mIsGameInForeground;
        }

        private static boolean isTouchActionDown() {
            ModemSarLog.i(LOG_TAG, "isTouchActionDown = " + mIsTouchActionDown);
            return mIsTouchActionDown;
        }

        private static boolean isScreenHorizontal() {
            Display display = mDisplay;
            boolean z = false;
            if (display != null && (display.getRotation() == 1 || mDisplay.getRotation() == 3)) {
                z = true;
            }
            ModemSarLog.i(LOG_TAG, "isScreenHorizontal: " + z);
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isTopPackage(String str) {
            String[] strArr;
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfoOnDisplay = ActivityTaskManager.getService().getRootTaskInfoOnDisplay(1, 0, 0);
                if (rootTaskInfoOnDisplay == null || (strArr = rootTaskInfoOnDisplay.childTaskNames) == null) {
                    ModemSarLog.i(LOG_TAG, "rootTaskinfo is null");
                } else {
                    for (String str2 : strArr) {
                        String packageName = ComponentName.unflattenFromString(str2).getPackageName();
                        ModemSarLog.i(LOG_TAG, "rootPackageName: " + packageName);
                        if (packageName.equals(str)) {
                            ModemSarLog.i(LOG_TAG, str + " is top package");
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                ModemSarLog.e(LOG_TAG, "isTopPackage: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        class GameStateListenerHandler extends Handler {
            public GameStateListenerHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "handleMessage: what = " + message.what);
                int i = message.what;
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        HorizontalGameListListener.mIsTouchActionDown = false;
                    } else if (i2 == 1) {
                        HorizontalGameListListener.mIsTouchActionDown = true;
                    }
                    HorizontalGameListListener.updateHorizontalGameListState();
                    return;
                }
                String str = ((ForegroundInfo) message.obj).mForegroundPackageName;
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "EVENT_GAME_LIST_FOREGROUND_STATE: packageName = " + str);
                if (HorizontalGameListListener.isTopPackage(str)) {
                    if (HorizontalGameListListener.HORIZON_GAME_LIST.contains(str)) {
                        HorizontalGameListListener.mIsGameInForeground = true;
                        HorizontalGameListListener.registerMotionEventListener();
                        HorizontalGameListListener.mCurrentTopGame = str;
                        return;
                    } else {
                        HorizontalGameListListener.mIsGameInForeground = false;
                        HorizontalGameListListener.unregisterMotionEventListener();
                        HorizontalGameListListener.removeTouchEvent(1);
                        HorizontalGameListListener.updateHorizontalGameListState();
                        return;
                    }
                }
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "EVENT_GAME_LIST_FOREGROUND_STATE: package not top");
            }
        }

        private static void updateHorizontalGameListState(int i) {
            GameStateListener.notifyDeviceStateChangeEvent(2, i);
            GameStateListener.mCurrentHorizontalGameListState = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void updateHorizontalGameListState() {
            int i = (isGameInForeground() && isTouchActionDown() && isScreenHorizontal()) ? 1 : 0;
            ModemSarLog.i(LOG_TAG, "updateHorizontalGameListState: " + i);
            if (GameStateListener.mCurrentHorizontalGameListState != i) {
                ModemSarLog.d(LOG_TAG, "updateHorizontalGameListState: new state = " + i);
                GameStateListener.mCurrentHorizontalGameListState = i;
                GameStateListener.notifyDeviceStateChangeEvent(2, GameStateListener.mCurrentHorizontalGameListState);
            }
        }
    }
}
