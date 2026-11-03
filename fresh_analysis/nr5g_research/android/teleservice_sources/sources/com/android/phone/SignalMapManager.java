package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.phone.signalMap.SignalMapCloudController;
import com.android.phone.signalMap.SignalMapController;
import com.android.phone.signalMap.SignalMapHSTCloudController;
import com.android.phone.signalMap.SignalMapHSTController;
import com.android.phone.signalMap.SignalMapNOMLController;
import com.android.phone.signalMap.SignalMapSnsControllerBase;
import com.android.phone.signalMap.SignalMapSubwayController;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapManager {
    private static SignalMapManager sInstance;
    private SignalMapManagerHandler mHandler;
    private HandlerThread mWorkThread;
    private boolean mIsInitialized = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.SignalMapManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                SignalMapManager.this.mHandler.obtainMessage(2, intent).sendToTarget();
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    private SignalMapManager() {
        HandlerThread handlerThread = new HandlerThread("SignalMapManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapManagerHandler signalMapManagerHandler = new SignalMapManagerHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapManagerHandler;
        signalMapManagerHandler.obtainMessage(1).sendToTarget();
    }

    public static synchronized SignalMapManager getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new SignalMapManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        log("init");
        registerReceiver();
    }

    class SignalMapManagerHandler extends Handler {
        SignalMapManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SignalMapManager.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                SignalMapManager.this.init();
                return;
            }
            if (i != 2) {
                return;
            }
            Intent intent = (Intent) message.obj;
            String stringExtra = intent.getStringExtra("ss");
            int intExtra = intent.getIntExtra("phone", -1);
            SignalMapManager.this.log("EVENT_SIM_STATE_CHANGED = phoneId" + intExtra + ", simState=" + stringExtra);
            SignalMapManager.this.initializeControllersOnSimReady(stringExtra);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeControllersOnSimReady(String str) {
        if (!"READY".equals(str) || this.mIsInitialized) {
            log("initializeControllersOnSimReady fail, simState = " + str + ",mIsInitialized =" + this.mIsInitialized);
            return;
        }
        initializeControllers();
    }

    private void initializeControllers() {
        if (MiuiPhoneUtils.isSignalMapOptimizationSupported()) {
            SignalMapController.getInstance();
            SignalMapCloudController.getInstance();
            SignalMapHSTCloudController.getInstance();
            SignalMapSubwayController.getInstance();
            SignalMapHSTController.getInstance();
            if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
                SignalMapSnsControllerBase.getInstance();
            }
        }
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() && MiuiPhoneUtils.isSignalMapNOMLSupported()) {
            SignalMapNOMLController.getInstance();
        }
        this.mIsInitialized = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapManager", str);
    }
}
