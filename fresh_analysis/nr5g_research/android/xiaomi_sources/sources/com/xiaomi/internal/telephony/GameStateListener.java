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
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
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
    private static Map<Integer, Integer> mListenerStateMap = new HashMap();
    private static Map<Integer, RegistrantList> mRegRequestInfoMap = new HashMap();
    private static Context mContext = null;
    private static GameStateListener mInstance = null;
    private static int mCurrentLandScapeGameState = 0;
    private static MotionDetector mMotionDetector = null;
    private static volatile int mCurrentHorizontalGameListState = 0;

    public static void registerGameStateListener(Context context, int eventType, Handler handler, int what, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerGameStateListener, context = " + context + ", eventType = " + eventType + ", handler = " + handler + ", what = " + what + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                RegistrantList registrants = new RegistrantList();
                registrants.addUnique(handler, what, obj);
                mRegRequestInfoMap.put(Integer.valueOf(eventType), registrants);
            } else {
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).addUnique(handler, what, obj);
            }
            startStateChangeListener(eventType, new Registrant(handler, what, obj));
        }
    }

    public static void unregisterGameStateChangeEvent(int eventType, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterGameStateChangeEvent, eventType = " + eventType + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                return;
            }
            mRegRequestInfoMap.get(Integer.valueOf(eventType)).remove(handler);
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)).size() == 0) {
                stopDeviceStateChangeListener(eventType, handler);
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

    private static void startStateChangeListener(int eventType, Registrant r) {
        ModemSarLog.d(LOG_TAG, "startStateChangeListener, eventType = " + eventType + ", r = " + r);
        synchronized (mListenerStateMap) {
            try {
                switch (eventType) {
                    case 1:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) != null && mListenerStateMap.get(Integer.valueOf(eventType)).intValue() != 0) {
                            notifyDeviceStateChangeEvent(1, r, mCurrentLandScapeGameState);
                        } else {
                            if (mMotionDetector == null) {
                                mMotionDetector = new MotionDetector();
                            }
                            if (mMotionDetector != null) {
                                mMotionDetector.start();
                                mListenerStateMap.put(Integer.valueOf(eventType), 1);
                            }
                        }
                        break;
                    case 2:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                            HorizontalGameListListener.startGameListListenner();
                            mListenerStateMap.put(Integer.valueOf(eventType), 1);
                        } else {
                            notifyDeviceStateChangeEvent(2, r, mCurrentHorizontalGameListState);
                        }
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void stopDeviceStateChangeListener(int eventType, Handler h) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + eventType + ", h = " + h);
        synchronized (mListenerStateMap) {
            try {
                switch (eventType) {
                    case 1:
                        if (mMotionDetector != null) {
                            mMotionDetector.stop();
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            mCurrentLandScapeGameState = 0;
                        }
                        break;
                    case 2:
                        if (mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 1) {
                            HorizontalGameListListener.stopGameListListenner();
                            mListenerStateMap.put(Integer.valueOf(eventType), 0);
                            mCurrentHorizontalGameListState = 0;
                        }
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyDeviceStateChangeEvent(int eventType, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + eventType + ", intValue = " + intValue);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) != null) {
                byte[] bytes = ModemUtils.getBytes(intValue);
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).notifyRegistrants(new AsyncResult((Object) null, bytes, (Throwable) null));
            }
        }
    }

    private static void notifyDeviceStateChangeEvent(int eventType, Registrant r, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + eventType + ", r = " + r + ", intValue = " + intValue);
        if (r == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            byte[] bytes = ModemUtils.getBytes(intValue);
            r.notifyRegistrant(new AsyncResult((Object) null, bytes, (Throwable) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MotionDetector implements SensorEventListener {
        private static final float ACCEL_THRESHOLD = 0.5f;
        private static final float ACC_CHANGE_THRESHOLD = 0.008f;
        private static final float ALPHA = 0.1f;
        private static final long ANOMALY_DURATION_MS = 5000;
        private static final float ANOMALY_THRESHOLD = 19.6f;
        private static final int DYNAMIC_COUNT_THRESHOLD = 10;
        private static final float GRAVITY_EARTH = 9.8f;
        private static final float GYRO_THRESHOLD = 0.005f;
        private static final int STATIC_COUNT_THRESHOLD = 40;
        private Handler backgroundHandler;
        private HandlerThread handlerThread;
        private Runnable sensorTimeoutTask;
        private LinkedList<VectorSumWithTimestamp> accelVectorSumQueue = new LinkedList<>();
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
                String action = intent.getAction();
                if (GameStateListener.BC_INTENT_ACTION_SCREEN_ORIENTATION_CHANGE.equals(action)) {
                    MotionDetector.this.updateLandScapeGameState();
                }
            }
        };
        private SensorManager sensorManager = (SensorManager) GameStateListener.mContext.getSystemService("sensor");
        private Sensor accelerometer = this.sensorManager.getDefaultSensor(1);
        private Sensor gyroscope = this.sensorManager.getDefaultSensor(4);
        private Display display = ((WindowManager) GameStateListener.mContext.getSystemService("window")).getDefaultDisplay();
        private long lastSensorUpdateTimestamp = System.currentTimeMillis();

        /* JADX INFO: Access modifiers changed from: private */
        static class VectorSumWithTimestamp {
            final float sum;
            final long timestamp;

            VectorSumWithTimestamp(float sum, long timestamp) {
                this.sum = sum;
                this.timestamp = timestamp;
            }
        }

        public void start() {
            startBcIntentListen();
            if (this.handlerThread == null || !this.handlerThread.isAlive()) {
                this.handlerThread = new HandlerThread("MotionDetectorThread");
                this.handlerThread.start();
                this.backgroundHandler = new Handler(this.handlerThread.getLooper());
            }
            if (!this.isListeningToMotion) {
                startMotionDetection();
            }
            updateLandScapeGameState();
        }

        private void startBcIntentListen() {
            if (!this.isReceiverRegistered) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(GameStateListener.BC_INTENT_ACTION_SCREEN_ORIENTATION_CHANGE);
                GameStateListener.mContext.registerReceiver(this.mReceiver, filter, 2);
                this.isReceiverRegistered = true;
            }
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
        public void onSensorChanged(SensorEvent event) {
            this.lastSensorUpdateTimestamp = System.currentTimeMillis();
            switch (event.sensor.getType()) {
                case 1:
                    lowPassFilter(event.values, this.accelValues);
                    detectStationary();
                    detectAnomaly();
                    break;
                case 4:
                    lowPassFilter(event.values, this.gyroValues);
                    detectStationary();
                    break;
            }
        }

        private void startMotionDetection() {
            if (!this.isListeningToMotion) {
                boolean accelerometerRegistered = this.sensorManager.registerListener(this, this.accelerometer, 3);
                boolean gyroscopeRegistered = this.sensorManager.registerListener(this, this.gyroscope, 3);
                if (accelerometerRegistered && gyroscopeRegistered) {
                    this.isListeningToMotion = true;
                    ModemSarLog.d(GameStateListener.LOG_TAG, "Accelerometer and Gyroscope Start for Motion Detection, isListeningToMotion = true.");
                    startSensorTimeout();
                } else {
                    if (!accelerometerRegistered || !gyroscopeRegistered) {
                        ModemSarLog.e(GameStateListener.LOG_TAG, "Failed to register Accelerometer or Gyroscope listener.");
                    }
                    this.isListeningToMotion = false;
                }
            }
        }

        private void detectStationary() {
            float acceleration = (float) Math.sqrt((this.accelValues[0] * this.accelValues[0]) + (this.accelValues[1] * this.accelValues[1]) + (this.accelValues[2] * this.accelValues[2]));
            float accChange = Math.abs(this.accelValues[0] - this.accelValues_last[0]) + Math.abs(this.accelValues[1] - this.accelValues_last[1]) + Math.abs(this.accelValues[2] - this.accelValues_last[2]);
            float angularVelocity = (float) Math.sqrt((this.gyroValues[0] * this.gyroValues[0]) + (this.gyroValues[1] * this.gyroValues[1]) + (this.gyroValues[2] * this.gyroValues[2]));
            this.isCurStationary = Math.abs(acceleration - GRAVITY_EARTH) < ACCEL_THRESHOLD && accChange < ACC_CHANGE_THRESHOLD && angularVelocity < GYRO_THRESHOLD;
            if (this.isFirstCall) {
                this.isFirstCall = false;
                updateLandScapeGameState();
                this.isLastStationary = this.isCurStationary;
                ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
            } else if (!this.isCurStationary) {
                this.staticCnt = 0;
                if (this.isLastStationary) {
                    int i = this.dynamicCnt + 1;
                    this.dynamicCnt = i;
                    if (i > 10) {
                        this.isLastStationary = this.isCurStationary;
                        updateLandScapeGameState();
                        ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
                    }
                }
            } else {
                this.dynamicCnt = 0;
                if (!this.isLastStationary) {
                    int i2 = this.staticCnt + 1;
                    this.staticCnt = i2;
                    if (i2 > 40) {
                        this.isLastStationary = this.isCurStationary;
                        updateLandScapeGameState();
                        ModemSarLog.d(GameStateListener.LOG_TAG, "detectStationary, isCurStationary = " + this.isCurStationary + ".");
                    }
                }
            }
            System.arraycopy(this.accelValues, 0, this.accelValues_last, 0, this.accelValues.length);
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        private void startSensorTimeout() {
            if (this.sensorTimeoutTask == null) {
                this.sensorTimeoutTask = new Runnable() { // from class: com.xiaomi.internal.telephony.GameStateListener.MotionDetector.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (System.currentTimeMillis() - MotionDetector.this.lastSensorUpdateTimestamp > MotionDetector.ANOMALY_DURATION_MS && !MotionDetector.this.isCurStationary) {
                            MotionDetector.this.isCurStationary = true;
                            MotionDetector.this.isLastStationary = true;
                            MotionDetector.this.updateLandScapeGameState();
                            ModemSarLog.d(GameStateListener.LOG_TAG, "No sensor data received for 5 seconds, assuming stationary.");
                        }
                        if (MotionDetector.this.backgroundHandler != null) {
                            MotionDetector.this.backgroundHandler.postDelayed(this, MotionDetector.ANOMALY_DURATION_MS);
                        }
                    }
                };
            }
            if (this.backgroundHandler != null) {
                this.backgroundHandler.removeCallbacks(this.sensorTimeoutTask);
                this.backgroundHandler.postDelayed(this.sensorTimeoutTask, ANOMALY_DURATION_MS);
            }
        }

        private void detectAnomaly() {
            while (!this.accelVectorSumQueue.isEmpty() && System.currentTimeMillis() - this.accelVectorSumQueue.peek().timestamp > ANOMALY_DURATION_MS) {
                this.accelVectorSumQueue.poll();
            }
            float acceleration = (float) Math.sqrt((this.accelValues[0] * this.accelValues[0]) + (this.accelValues[1] * this.accelValues[1]) + (this.accelValues[2] * this.accelValues[2]));
            if (acceleration > ANOMALY_THRESHOLD) {
                this.accelVectorSumQueue.offer(new VectorSumWithTimestamp(acceleration, System.currentTimeMillis()));
            }
            if (!this.accelVectorSumQueue.isEmpty() && System.currentTimeMillis() - this.accelVectorSumQueue.peek().timestamp >= ANOMALY_DURATION_MS && this.accelVectorSumQueue.stream().allMatch(new Predicate() { // from class: com.xiaomi.internal.telephony.GameStateListener$MotionDetector$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return GameStateListener.MotionDetector.lambda$detectAnomaly$0((GameStateListener.MotionDetector.VectorSumWithTimestamp) obj);
                }
            })) {
                stop();
            }
        }

        static /* synthetic */ boolean lambda$detectAnomaly$0(VectorSumWithTimestamp entry) {
            return entry.sum > ANOMALY_THRESHOLD;
        }

        public void stop() {
            stopBcIntentListen();
            if (this.backgroundHandler != null && this.sensorTimeoutTask != null) {
                this.backgroundHandler.removeCallbacks(this.sensorTimeoutTask);
                this.sensorTimeoutTask = null;
            }
            if (this.handlerThread != null) {
                this.handlerThread.quitSafely();
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

        public void setLastLandScapeGameState(int state) {
            this.mLastLandScapeGameState = state;
        }

        public boolean isStationary() {
            return this.isCurStationary;
        }

        public boolean isListeningMotion() {
            return this.isListeningToMotion;
        }

        private void lowPassFilter(float[] input, float[] output) {
            for (int i = 0; i < input.length; i++) {
                output[i] = output[i] + ((input[i] - output[i]) * ALPHA);
            }
        }

        private boolean isLandscape() {
            if (this.display.getRotation() == 1 || this.display.getRotation() == 3) {
                return true;
            }
            if (this.display.getRotation() == 0 || this.display.getRotation() == 2) {
                return false;
            }
            ModemSarLog.d(GameStateListener.LOG_TAG, "getRotation, failed!");
            return false;
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

    public static class HorizontalGameListListener {
        private static final int EVENT_GAME_LIST_FOREGROUND_STATE = 1;
        private static final int EVENT_TOUCH_STATE = 2;
        private static final int GAME_LIST_IN_BACKGROUND = 0;
        private static final int GAME_LIST_IN_FOREGROUND = 1;
        private static final String LOG_TAG = "HorizontalGameListListener";
        private static final int TOUCH_DOWN = 1;
        private static final int TOUCH_UP = 0;
        private static final String TOUCH_UP_TAG = "TOUCH_UP_TAG";
        private static final int TOUCH_UP_TIMEOUT = 30000;
        private static volatile String mCurrentTopGame;
        private static Handler mHandler = null;
        private static HandlerThread mHandlerThread = null;
        private static Display mDisplay = null;
        private static boolean mIsGameForegroundRegistered = false;
        private static boolean mIsMotionEventListenerRegistered = false;
        private static final Object mMotionEventListenerLock = new Object();
        private static volatile boolean mIsTouchActionDown = false;
        private static volatile boolean mIsGameInForeground = false;
        private static final Set<String> HORIZON_GAME_LIST = new HashSet(Arrays.asList("com.tencent.tmgp.sgame", "com.tencent.tmgp.pubgmhd", "com.tencent.jkchess", "com.tencent.KiHan", "com.tencent.tmgp.dfm", "com.netease.party.mi", "com.tencent.tmgp.cf", "com.tencent.mf.uam", "com.tencent.lolm", "com.miHoYo.hkrpg", "com.miHoYo.Yuanshen", "com.netease.mc.mi", "com.tencent.tmgp.supercell.clashofclans", "com.netease.dwrg.mi", "com.tencent.tmgp.dnf", "com.tencent.tmgp.cod", "com.qqgame.hlddz", "com.minitech.miniworld.TMobile.mi", "com.netease.sky.mi", "com.hypergryph.arknights", "cn.jj", "com.tencent.tmgp.speedmobile", "com.miHoYo.Nap", "com.kurogame.mingchao", "com.tencent.qqgame.xq", "com.xiaomi.tinygame", "com.netease.dwrg", "com.tencent.tmgp.supercell.brawlstars", "com.pwrd.steam.esports", "com.tencent.letsgo", "com.netease.yyslscn", "com.tencent.ig", "com.netease.sky", "com.xxmddz.mi", "com.papegames.lysk.cn", "com.blizzard.wtcg.hearthstone", "com.pi.czrxdfirst", "com.bf.sgs.hdexp.mi", "com.qqgame.happymj", "com.tencent.nikke", "com.wepie.snake.new.mi", "com.netease.yhtj.mi", "com.feiyu.luobo4.mi", "com.bilibili.azurlane", "com.popcap.pvz2cthdxm", "com.tencent.tmgp.supercell.boombeach", "com.netease.l22", "com.hypergryph.arknights.bilibili", "com.sofunny.Sausage", "com.netease.nshm", "com.shenlan.m.reverse1999", "com.tencent.fiftyone.yc", "com.hortor.juliancysj", "com.tencent.hhw", "com.papegames.lysk.cn.mi", "com.tencent.nfsonline", "com.netease.party", "com.tencent.peng", "com.knight.union.mi", "com.netease.l22.mi", "com.tencent.nba2kx", "com.xd.xdt", "com.miniworldroyale.sparkgame.mi", "com.bf.sgs.hdexp", "com.netease.dfjs.mi", "com.papegames.infinitynikki"));
        private static IForegroundInfoListener mIForegroundInfoListener = new IForegroundInfoListener.Stub() { // from class: com.xiaomi.internal.telephony.GameStateListener.HorizontalGameListListener.1
            public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) throws RemoteException {
                String packageName = foregroundInfo.mForegroundPackageName;
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "onForegroundInfoChanged: " + packageName);
                if (TextUtils.isEmpty(packageName)) {
                    ModemSarLog.d(HorizontalGameListListener.LOG_TAG, "onForegroundInfoChanged: packageName is empty");
                } else {
                    HorizontalGameListListener.mHandler.sendMessage(HorizontalGameListListener.mHandler.obtainMessage(1, foregroundInfo));
                }
            }
        };
        private static MiuiDeviceMotionEventListener.MiuiMotionEventListener mMotionEventListenner = new MiuiDeviceMotionEventListener.MiuiMotionEventListener() { // from class: com.xiaomi.internal.telephony.GameStateListener.HorizontalGameListListener.2
            @Override // com.xiaomi.internal.telephony.MiuiDeviceMotionEventListener.MiuiMotionEventListener
            public void onMotionEvent(MotionEvent event) {
                if (event != null) {
                    ModemSarLog.d(HorizontalGameListListener.LOG_TAG, "onMotionEvent action = " + event.getAction());
                    if (event.getAction() == 0) {
                        HorizontalGameListListener.removeTouchEvent(0);
                        if (HorizontalGameListListener.mHandler != null) {
                            HorizontalGameListListener.mHandler.sendMessage(HorizontalGameListListener.mHandler.obtainMessage(2, 1, 0));
                        }
                    } else if ((1 == event.getAction() || 3 == event.getAction()) && HorizontalGameListListener.mHandler != null) {
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
                mHandlerThread = new HandlerThread("HorizontalGameListListenerBgThread");
                mHandlerThread.start();
                if (mHandler == null) {
                    mHandler = new GameStateListenerHandler(mHandlerThread.getLooper());
                }
            }
            ModemSarLog.i(LOG_TAG, "init");
        }

        private static void destroy() {
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mHandlerThread != null) {
                mHandlerThread.quitSafely();
                mHandlerThread = null;
                mHandler = null;
            }
            mIsTouchActionDown = false;
            mIsGameInForeground = false;
            mDisplay = null;
            ModemSarLog.i(LOG_TAG, "destroy");
        }

        private static void registerForegroundInfoListener() {
            if (!mIsGameForegroundRegistered) {
                ProcessManager.registerForegroundInfoListener(mIForegroundInfoListener);
                mIsGameForegroundRegistered = true;
            }
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
                if (!mIsMotionEventListenerRegistered) {
                    MiuiDeviceMotionEventListener.RegisterMiuiMotionEventListener(GameStateListener.mContext, mMotionEventListenner);
                    mIsMotionEventListenerRegistered = true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void unregisterMotionEventListener() {
            synchronized (mMotionEventListenerLock) {
                if (mIsMotionEventListenerRegistered) {
                    MiuiDeviceMotionEventListener.UnregisterMiuiMotionEventListener(mMotionEventListenner);
                    mIsMotionEventListenerRegistered = false;
                    mIsTouchActionDown = false;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void removeTouchEvent(int eventType) {
            if (eventType == 0) {
                if (mHandler != null) {
                    mHandler.removeMessages(2, TOUCH_UP_TAG);
                }
            } else if (mHandler != null) {
                mHandler.removeMessages(2);
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
            boolean result = false;
            if (mDisplay != null) {
                boolean z = true;
                if (mDisplay.getRotation() != 1 && mDisplay.getRotation() != 3) {
                    z = false;
                }
                result = z;
            }
            ModemSarLog.i(LOG_TAG, "isScreenHorizontal: " + result);
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isTopPackage(String packageName) {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskinfo = ActivityTaskManager.getService().getRootTaskInfoOnDisplay(1, 0, 0);
                if (rootTaskinfo == null || rootTaskinfo.childTaskNames == null) {
                    ModemSarLog.i(LOG_TAG, "rootTaskinfo is null");
                } else {
                    for (String name : rootTaskinfo.childTaskNames) {
                        String rootPackageName = ComponentName.unflattenFromString(name).getPackageName();
                        ModemSarLog.i(LOG_TAG, "rootPackageName: " + rootPackageName);
                        if (rootPackageName.equals(packageName)) {
                            ModemSarLog.i(LOG_TAG, packageName + " is top package");
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

        private static class GameStateListenerHandler extends Handler {
            public GameStateListenerHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "handleMessage: what = " + msg.what);
                switch (msg.what) {
                    case 1:
                        ForegroundInfo foregroundInfo = (ForegroundInfo) msg.obj;
                        String packageName = foregroundInfo.mForegroundPackageName;
                        ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "EVENT_GAME_LIST_FOREGROUND_STATE: packageName = " + packageName);
                        if (HorizontalGameListListener.isTopPackage(packageName)) {
                            if (HorizontalGameListListener.HORIZON_GAME_LIST.contains(packageName)) {
                                HorizontalGameListListener.mIsGameInForeground = true;
                                HorizontalGameListListener.registerMotionEventListener();
                                HorizontalGameListListener.mCurrentTopGame = packageName;
                            } else {
                                HorizontalGameListListener.mIsGameInForeground = false;
                                HorizontalGameListListener.unregisterMotionEventListener();
                                HorizontalGameListListener.removeTouchEvent(1);
                                HorizontalGameListListener.updateHorizontalGameListState();
                            }
                        } else {
                            ModemSarLog.i(HorizontalGameListListener.LOG_TAG, "EVENT_GAME_LIST_FOREGROUND_STATE: package not top");
                        }
                        break;
                    case 2:
                        if (msg.arg1 == 0) {
                            HorizontalGameListListener.mIsTouchActionDown = false;
                        } else if (msg.arg1 == 1) {
                            HorizontalGameListListener.mIsTouchActionDown = true;
                        }
                        HorizontalGameListListener.updateHorizontalGameListState();
                        break;
                }
            }
        }

        private static void updateHorizontalGameListState(int state) {
            GameStateListener.notifyDeviceStateChangeEvent(2, state);
            GameStateListener.mCurrentHorizontalGameListState = state;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void updateHorizontalGameListState() {
            int gameListState = 0;
            if (isGameInForeground() && isTouchActionDown() && isScreenHorizontal()) {
                gameListState = 1;
            }
            ModemSarLog.i(LOG_TAG, "updateHorizontalGameListState: " + gameListState);
            if (GameStateListener.mCurrentHorizontalGameListState != gameListState) {
                ModemSarLog.d(LOG_TAG, "updateHorizontalGameListState: new state = " + gameListState);
                GameStateListener.mCurrentHorizontalGameListState = gameListState;
                GameStateListener.notifyDeviceStateChangeEvent(2, GameStateListener.mCurrentHorizontalGameListState);
            }
        }
    }
}
