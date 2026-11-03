package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.SubscriptionManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class OemDataEnhance {
    private static Context mContext;
    private static OemDataEnhance sInstance;
    private OemDataEnhanceHandler mHandler;
    private HandlerThread mWorkThread;
    private long mTimeLastSetImsHPDDMs = 0;
    private int mLastInsertedSimCn = -1;
    private boolean mIsGameEnabled = false;
    private boolean mIsGameOptEnabled = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.OemDataEnhance.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Log.d("OemDataEnhance", "mIntentReceiver action=".concat(action));
            if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(action)) {
                OemDataEnhance.this.mHandler.obtainMessage(2, intent).sendToTarget();
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                OemDataEnhance.this.mHandler.obtainMessage(1).sendToTarget();
            }
        }
    };

    public static OemDataEnhance init(Context context) {
        mContext = context;
        if (sInstance == null) {
            sInstance = new OemDataEnhance(context);
        } else {
            Log.d("OemDataEnhance", "init() called multiple times! sInstance = " + sInstance);
        }
        return sInstance;
    }

    private OemDataEnhance(Context context) {
        Log.d("OemDataEnhance", "OemDataEnhance");
        mContext = context;
        HandlerThread handlerThread = new HandlerThread("OemDataEnhance", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        OemDataEnhanceHandler oemDataEnhanceHandler = new OemDataEnhanceHandler(this.mWorkThread.getLooper());
        this.mHandler = oemDataEnhanceHandler;
        oemDataEnhanceHandler.obtainMessage(0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    class OemDataEnhanceHandler extends Handler {
        public OemDataEnhanceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Log.d("OemDataEnhance", "EVENT_INIT_ACTION");
                OemDataEnhance.this.initAction();
            } else {
                if (i == 1) {
                    OemDataEnhance.this.processSimStateChanged();
                    return;
                }
                if (i == 2) {
                    OemDataEnhance.this.processGameScene((Intent) message.obj);
                } else {
                    if (i != 3) {
                        return;
                    }
                    OemDataEnhance oemDataEnhance = OemDataEnhance.this;
                    oemDataEnhance.setImsHighPriorityDataDuration(1200, oemDataEnhance.getDdsPhoneId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSimStateChanged() {
        this.mLastInsertedSimCn = MiuiPhoneUtils.getInsertedSimCount();
        Log.d("OemDataEnhance", "processSimStateChanged simCn=" + this.mLastInsertedSimCn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDdsPhoneId() {
        int phoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        if (phoneId != -1) {
            return phoneId;
        }
        Log.d("OemDataEnhance", "getDdsPhoneId ddsPhoneId is -1");
        return 0;
    }

    private int getVicePhoneId() {
        int i = getDdsPhoneId() == 0 ? 1 : 0;
        Log.d("OemDataEnhance", "getVicePhoneId vicePhoneId=" + i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameScene(Intent intent) {
        long longExtra = intent.getLongExtra("Level_UL", 1L);
        long longExtra2 = intent.getLongExtra("Level_DL", 1L);
        this.mIsGameEnabled = false;
        if (longExtra == 2 && longExtra2 == 2) {
            this.mIsGameEnabled = true;
            Log.d("OemDataEnhance", "processGameScene enable[" + this.mIsGameEnabled + "],simCn[" + this.mLastInsertedSimCn + "]");
            if (!this.mIsGameEnabled || this.mLastInsertedSimCn <= 1) {
                return;
            }
            if (isSetImsHPDDTimer()) {
                setImsHighPriorityDataDuration(1200, getVicePhoneId());
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3), 100L);
            }
            this.mIsGameOptEnabled = true;
            setGamingModeStatus(0, getDdsPhoneId());
            return;
        }
        if (this.mIsGameOptEnabled) {
            this.mIsGameOptEnabled = false;
            setGamingModeStatus(1, getDdsPhoneId());
        }
    }

    private boolean isSetImsHPDDTimer() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mTimeLastSetImsHPDDMs;
        if (j != 0 && jElapsedRealtime - j < 1200000) {
            Log.d("OemDataEnhance", "isSetImsHPDDTimer false");
            return false;
        }
        this.mTimeLastSetImsHPDDMs = jElapsedRealtime;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setImsHighPriorityDataDuration(int i, int i2) {
        Log.d("OemDataEnhance", "setImsHighPriorityDataDuration duration=" + i + ",phoneId=" + i2);
        RilHook rilHookInit = RilHook.init(mContext);
        if (rilHookInit != null) {
            rilHookInit.setImsHighPriorityDataDuration(i2, i);
        }
    }

    private synchronized void setGamingModeStatus(int i, int i2) {
        Log.d("OemDataEnhance", "setGamingModeStatus gameStatus=" + i + ",phoneId=" + i2);
        RilHook rilHookInit = RilHook.init(mContext);
        if (rilHookInit != null) {
            rilHookInit.setGamingModeStatus(i2, i);
        }
    }
}
