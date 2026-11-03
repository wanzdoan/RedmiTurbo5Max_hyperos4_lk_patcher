package com.android.phone.ims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.recoveryPolicy.Policy;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsSceneControl {
    public static String LOG_TAG = "MiuiImsSceneControl";
    private static MiuiImsSceneControl sInstance;
    protected Context mContext;
    private boolean mIsMonitorStarted;
    protected boolean mIsScreenOn;
    private NetworkStats mNetworkStats;
    private MiuiImsSceneControlHandler mSceneControlHandler;
    private int mTotalCheckCount;
    private boolean misEvevator = false;
    protected boolean mIsUseMobileData = false;

    public class NetworkStats {
        public long rxBytes;
        public long time;
        public long txBytes;

        public NetworkStats(MiuiImsSceneControl miuiImsSceneControl) {
            clear();
        }

        public void updateTxRx() {
            this.txBytes = TrafficStats.getMobileTxBytes();
            this.rxBytes = TrafficStats.getMobileRxBytes();
            this.time = System.currentTimeMillis();
        }

        public NetworkStats(MiuiImsSceneControl miuiImsSceneControl, NetworkStats networkStats) {
            this.txBytes = networkStats.txBytes;
            this.rxBytes = networkStats.rxBytes;
            this.time = networkStats.time;
        }

        public void clear() {
            this.txBytes = 0L;
            this.rxBytes = 0L;
            this.time = 0L;
        }

        public String toString() {
            return "txBytes=" + this.txBytes + " rxBytes=" + this.rxBytes + " time=" + this.time;
        }
    }

    public static synchronized MiuiImsSceneControl getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new MiuiImsSceneControl(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public MiuiImsSceneControl(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("com.android.phone.intent.action.ELEVATOR_STATE_SCENE");
        this.mContext.registerReceiver(new SceneControlReceiver(), intentFilter, 2);
        this.mSceneControlHandler = new MiuiImsSceneControlHandler(this.mContext.getMainLooper());
        this.mNetworkStats = new NetworkStats(this);
    }

    class MiuiImsSceneControlHandler extends Handler {
        MiuiImsSceneControlHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiImsSceneControl.this.logd("handleMessage what=" + message.what);
            if (message.what != 1) {
                return;
            }
            MiuiImsSceneControl.this.handleUpdateNetworkSpeed();
        }
    }

    public class SceneControlReceiver extends BroadcastReceiver {
        public SceneControlReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            if (action.equals("android.intent.action.SCREEN_ON")) {
                MiuiImsSceneControl.this.mIsScreenOn = true;
                return;
            }
            if (action.equals("android.intent.action.SCREEN_OFF")) {
                MiuiImsSceneControl.this.mIsScreenOn = false;
                return;
            }
            if ("com.android.phone.intent.action.ELEVATOR_STATE_SCENE".equals(action)) {
                int intExtra = intent.getIntExtra("elevatorState", 1);
                MiuiImsSceneControl.this.logd("SceneControlReceiver: elevator state: " + intExtra);
                if (intExtra == 2 && !MiuiImsSceneControl.this.misEvevator) {
                    MiuiImsSceneControl.this.misEvevator = true;
                } else if (intExtra == 1 && MiuiImsSceneControl.this.misEvevator) {
                    MiuiImsSceneControl.this.misEvevator = false;
                }
            }
        }
    }

    private void postUpdateNetworkSpeedDelay(long j) {
        MiuiImsSceneControlHandler miuiImsSceneControlHandler = this.mSceneControlHandler;
        if (miuiImsSceneControlHandler != null) {
            miuiImsSceneControlHandler.removeMessages(1);
            this.mSceneControlHandler.sendEmptyMessageDelayed(1, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateNetworkSpeed() {
        long j;
        NetworkStats networkStats = new NetworkStats(this, this.mNetworkStats);
        this.mNetworkStats.updateTxRx();
        this.mTotalCheckCount++;
        NetworkStats networkStats2 = this.mNetworkStats;
        long j2 = networkStats2.txBytes - networkStats.txBytes;
        long j3 = networkStats2.rxBytes - networkStats.rxBytes;
        long j4 = networkStats.time;
        long j5 = 0;
        if (j4 == 0 || networkStats2.time <= j4 || j2 < 0 || j3 < 0) {
            j = 0;
        } else {
            j5 = (j2 * 1000) / 2000;
            j = (j3 * 1000) / 2000;
        }
        logd("update speed: deltaULBytes: " + j2 + " deltaDLBytes: " + j3 + " uLRateBps: " + j5 + " dLRateBps:" + j + " NetworkStats: " + this.mNetworkStats);
        StringBuilder sb = new StringBuilder("speed: UL-");
        sb.append(j5 / 1024);
        sb.append(" kb/s  DL-");
        sb.append(j / 1024);
        sb.append("kb/s");
        logi(sb.toString());
        boolean z = j5 > 1000 && j > 1000;
        this.mIsUseMobileData = z;
        if (z || this.mTotalCheckCount >= 4) {
            stopMonitor();
        } else {
            postUpdateNetworkSpeedDelay(2000L);
        }
    }

    public void stopMonitor() {
        logd("stop speed monitor.");
        MiuiImsSceneControlHandler miuiImsSceneControlHandler = this.mSceneControlHandler;
        if (miuiImsSceneControlHandler != null) {
            miuiImsSceneControlHandler.removeCallbacksAndMessages(null);
        }
        this.mIsMonitorStarted = false;
        this.mTotalCheckCount = 0;
    }

    public boolean canDoRecovery(Policy.Action action, ImsPhone imsPhone) {
        if (!MiuiPhoneUtils.isDuringCall() && !imsPhone.isImsRegistered()) {
            boolean zIsHighSpeedTrain = isHighSpeedTrain(this.mContext);
            if (this.misEvevator || zIsHighSpeedTrain) {
                logd("canDoRecovery false, misEvevator: " + this.misEvevator + " isHsTrain: " + zIsHighSpeedTrain);
            } else {
                boolean zIsGameBooster = isGameBooster(this.mContext);
                boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
                boolean zHavaForegroundApp = havaForegroundApp();
                boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
                logd("canDoRecovery: isScreenOn: " + zIsScreenOn + " isGameBoosting: " + zIsGameBooster + " isHaveAcvityFg: " + zHavaForegroundApp + " isWifiConn: " + zIsWifiConnected);
                if (zIsGameBooster || (zIsScreenOn && zHavaForegroundApp && !zIsWifiConnected)) {
                    return false;
                }
                logi("canDoRecovery true");
                return true;
            }
        }
        return false;
    }

    public static boolean isGameBooster(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "gb_boosting", 0) == 1;
    }

    public boolean havaForegroundApp() {
        ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
        if (foregroundInfo == null || TextUtils.equals("com.miui.home", foregroundInfo.mForegroundPackageName)) {
            return false;
        }
        logi("current fore ground app: " + foregroundInfo.mForegroundPackageName);
        return true;
    }

    public boolean isHighSpeedTrain(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "sensor_state_highspeedtrain", 0) == 1;
    }

    protected void logd(String str) {
        Rlog.d(LOG_TAG, str);
    }

    protected void logi(String str) {
        Rlog.i(LOG_TAG, str);
    }
}
