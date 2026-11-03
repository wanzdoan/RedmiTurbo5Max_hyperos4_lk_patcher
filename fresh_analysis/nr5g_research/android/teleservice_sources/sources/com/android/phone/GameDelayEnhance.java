package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.ModemUtils;

/* JADX INFO: loaded from: classes.dex */
public class GameDelayEnhance {
    private static Context mContext;
    protected static volatile GameDelayEnhance mInstance;
    private static boolean mMiRilHookReady;
    private ModemUtils mModemUtils;
    private TelephonyManager mTelephonyManager;
    private Handler mWorkerThreadHandler;
    private HandlerThread mWorkerThread = new HandlerThread("GameDelayEnhanceBgThread");
    private boolean mIsScreenOff = false;
    private boolean mIsMobileConnected = true;
    private boolean mIsGameDelayEnhance = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.GameDelayEnhance.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                GameDelayEnhance.this.mIsScreenOff = false;
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                GameDelayEnhance.this.mIsScreenOff = true;
                GameDelayEnhance.this.enableDelayEnhance(false);
                return;
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo != null && networkInfo.getType() == 0) {
                    NetworkInfo.State state = networkInfo.getState();
                    NetworkInfo.State state2 = NetworkInfo.State.DISCONNECTED;
                    GameDelayEnhance gameDelayEnhance = GameDelayEnhance.this;
                    if (state == state2) {
                        gameDelayEnhance.mIsMobileConnected = false;
                        GameDelayEnhance.this.enableDelayEnhance(false);
                    } else {
                        gameDelayEnhance.mIsMobileConnected = true;
                    }
                }
                Log.d("GameDelayEnhance", "CONNECTIVITY_ACTION, mobileConnected = " + GameDelayEnhance.this.mIsMobileConnected);
                return;
            }
            if (action.equals("com.android.phone.intent.action.GAME_STATUS_SCENE")) {
                GameDelayEnhance.this.enableDelayEnhance(intent.getBooleanExtra("game_status", false));
            } else {
                Log.d("GameDelayEnhance", "UNKNOWN action");
            }
        }
    };

    class RilHookListener implements BaseEvent.IEventListener {
        private RilHookListener(GameDelayEnhance gameDelayEnhance) {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            GameDelayEnhance.mMiRilHookReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
            if (GameDelayEnhance.mMiRilHookReady) {
                Log.d("GameDelayEnhance", "MIRIL Hook connected.");
                GameDelayEnhance.mMiRilHookReady = true;
            } else {
                Log.d("GameDelayEnhance", "MIRIL Hook not onnected.");
                GameDelayEnhance.mMiRilHookReady = false;
            }
        }
    }

    class WorkerHandler extends Handler {
        public WorkerHandler(GameDelayEnhance gameDelayEnhance, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
        }
    }

    public static GameDelayEnhance make() {
        if (mInstance == null) {
            synchronized (GameDelayEnhance.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new GameDelayEnhance();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private GameDelayEnhance() {
        this.mTelephonyManager = null;
        this.mModemUtils = null;
        mContext = PhoneGlobals.getInstance();
        this.mWorkerThread.start();
        setLooper(this.mWorkerThread.getLooper());
        this.mModemUtils = ModemUtils.init(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("com.android.phone.intent.action.GAME_STATUS_SCENE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        this.mTelephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookListener(), this.mWorkerThreadHandler);
    }

    private void setLooper(Looper looper) {
        this.mWorkerThreadHandler = new WorkerHandler(this, looper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableDelayEnhance(boolean z) {
        boolean zIsDataEnabled = this.mTelephonyManager.isDataEnabled();
        boolean z2 = SystemProperties.getBoolean("debug.offline_log.enable", false);
        Log.d("GameDelayEnhance", "enableDelayEnhance, current = " + this.mIsGameDelayEnhance + ", new = " + z);
        boolean z3 = this.mIsGameDelayEnhance;
        if (!z) {
            if (z3) {
                try {
                    Intent intent = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://9959951"));
                    intent.putExtra("CTRL", "stop");
                    intent.addFlags(16777216);
                    mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
                    this.mIsGameDelayEnhance = false;
                    return;
                } catch (RuntimeException unused) {
                    Log.d("GameDelayEnhance", "Exception");
                    return;
                }
            }
            return;
        }
        if (!z3 && !z2 && this.mIsMobileConnected && zIsDataEnabled) {
            try {
                Intent intent2 = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://9959951"));
                intent2.putExtra("CTRL", "start");
                intent2.addFlags(16777216);
                mContext.sendBroadcast(intent2, "android.permission.READ_PRIVILEGED_PHONE_STATE");
                this.mIsGameDelayEnhance = true;
                return;
            } catch (RuntimeException unused2) {
                Log.d("GameDelayEnhance", "Exception");
                return;
            }
        }
        Log.d("GameDelayEnhance", "enableDelayEnhance, connected = " + this.mIsMobileConnected + ", MobileDataEnabled = " + zIsDataEnabled + ", offlineRunning = " + z2);
    }
}
