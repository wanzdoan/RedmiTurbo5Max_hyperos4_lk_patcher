package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.userperception.IStateChangeListener;
import android.userperception.StateEvent;
import android.userperception.UserPerceptionManager;
import android.util.Log;
import com.android.phone.utils.MiuiThreadUtil;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyXiaoAiManager {
    private static TelephonyXiaoAiManager mInstance;
    private Context mContext;
    private XiaoAiHandler mXiaoAiHandler;
    private UserPerceptionManager userPerceptionManager;
    String TAG = "TelephonyXiaoAiManager";
    public boolean mAIRegister = false;
    private final ArrayList mXiaoAiListeners = new ArrayList();
    IStateChangeListener xiaoAiCallback = new IStateChangeListener.Stub() { // from class: com.android.phone.TelephonyXiaoAiManager.2
        public void onStateChanged(StateEvent stateEvent) {
            Log.d(TelephonyXiaoAiManager.this.TAG, "onStateChanged ");
            TelephonyXiaoAiManager.this.userPerceptionManager.getCurrentScene();
            Bundle event = stateEvent.getEvent();
            event.getInt("StateType");
            int i = event.getInt("PermissionCode");
            if (i != 0) {
                if (i == -1) {
                    TelephonyXiaoAiManager.this.reDegisterXiaoAI(true);
                }
            } else {
                Log.d(TelephonyXiaoAiManager.this.TAG, "onStateChanged bundle = " + event);
                TelephonyXiaoAiManager.this.notifyScenceStateChanged(event);
            }
        }
    };

    public interface XiaoAiListener {
        void onScenceStateChanged(Bundle bundle);
    }

    public static synchronized TelephonyXiaoAiManager make() {
        try {
            if (mInstance == null && MiuiPhoneUtils.isTelephonyXiaoAiSupported()) {
                mInstance = new TelephonyXiaoAiManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    public static TelephonyXiaoAiManager getInstance() {
        return mInstance;
    }

    private TelephonyXiaoAiManager() {
        this.userPerceptionManager = null;
        Log.d(this.TAG, "TelephonyXiaoAiManager init");
        this.mContext = PhoneGlobals.getInstance();
        this.userPerceptionManager = new UserPerceptionManager();
        this.mXiaoAiHandler = new XiaoAiHandler(MiuiThreadUtil.getWorkThreadLooper());
        registerState();
    }

    public void registerXiaoAiListener(XiaoAiListener xiaoAiListener) {
        if (xiaoAiListener == null) {
            return;
        }
        synchronized (this) {
            try {
                if (findXiaoAiListener(xiaoAiListener) < 0) {
                    this.mXiaoAiListeners.add(xiaoAiListener);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void registerState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TelephonyXiaoAi.intent.debugall");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.TelephonyXiaoAiManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                if ("TelephonyXiaoAi.intent.debugall".equals(intent.getAction())) {
                    TelephonyXiaoAiManager.this.mXiaoAiHandler.obtainMessage(101, intent).sendToTarget();
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                    TelephonyXiaoAiManager.this.mXiaoAiHandler.obtainMessage(102, intent).sendToTarget();
                }
            }
        }, intentFilter, 2);
    }

    private int findXiaoAiListener(XiaoAiListener xiaoAiListener) {
        int size = this.mXiaoAiListeners.size();
        for (int i = 0; i < size; i++) {
            if (this.mXiaoAiListeners.get(i) == xiaoAiListener) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScenceStateChanged(Bundle bundle) {
        int size = this.mXiaoAiListeners.size();
        for (int i = 0; i < size; i++) {
            ((XiaoAiListener) this.mXiaoAiListeners.get(i)).onScenceStateChanged(bundle);
        }
    }

    class XiaoAiHandler extends Handler {
        public XiaoAiHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 100:
                    TelephonyXiaoAiManager.this.reDegisterXiaoAI(true);
                    break;
                case 101:
                    TelephonyXiaoAiManager.this.reDegisterXiaoAI(((Intent) message.obj).getBooleanExtra("register", false));
                    break;
                case 102:
                    TelephonyXiaoAiManager.this.onSimStateChanged((Intent) message.obj);
                    break;
                default:
                    Log.d(TelephonyXiaoAiManager.this.TAG, "handleMessage: default");
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        String stringExtra = intent.getStringExtra("ss");
        int intExtra = intent.getIntExtra("phone", -1);
        Log.d(this.TAG, "onSimStateChanged: simState = " + stringExtra + " mAIRegister = " + this.mAIRegister + "phoneId = " + intExtra);
        if ("LOADED".equals(stringExtra) && !this.mAIRegister && MiuiPhoneUtils.getInsertedSimCount() > 0) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (jElapsedRealtime >= 900000) {
                reDegisterXiaoAI(true);
                return;
            } else {
                XiaoAiHandler xiaoAiHandler = this.mXiaoAiHandler;
                xiaoAiHandler.sendMessageDelayed(xiaoAiHandler.obtainMessage(100), 900000 - jElapsedRealtime);
                return;
            }
        }
        if ("ABSENT".equals(stringExtra) && MiuiPhoneUtils.getInsertedSimCount() == 0) {
            if (this.mXiaoAiHandler.hasMessages(100)) {
                this.mXiaoAiHandler.removeMessages(100);
            }
            if (this.mAIRegister) {
                reDegisterXiaoAI(false);
            }
        }
    }

    private boolean queryPermission(int i) {
        UserPerceptionManager userPerceptionManager = this.userPerceptionManager;
        Context context = this.mContext;
        return userPerceptionManager.queryPermission(i, false, context, context.getPackageName()) == 0;
    }

    public void reDegisterXiaoAI(boolean z) {
        int[] iArr = {10000001, 10000002, 10000003, 10000004, 10000005, 10000006, 10000007, 10000008};
        boolean zQueryPermission = true;
        for (int i = 0; i < 8; i++) {
            zQueryPermission &= queryPermission(iArr[i]);
        }
        if (zQueryPermission) {
            if (z && !this.mAIRegister) {
                for (int i2 = 0; i2 < 8; i2++) {
                    int iRegisterListener = this.userPerceptionManager.registerListener(iArr[i2], this.xiaoAiCallback, 0L);
                    Log.d(this.TAG, "registerScenes = " + iArr + " registerResult is " + iRegisterListener);
                }
                this.mAIRegister = true;
                return;
            }
            if (z || !this.mAIRegister || this.mContext == null) {
                return;
            }
            for (int i3 = 0; i3 < 8; i3++) {
                UserPerceptionManager userPerceptionManager = this.userPerceptionManager;
                int i4 = iArr[i3];
                IStateChangeListener iStateChangeListener = this.xiaoAiCallback;
                Context context = this.mContext;
                userPerceptionManager.unregisterListener(i4, iStateChangeListener, context, context.getPackageName());
            }
            Log.d(this.TAG, "unRegister XiaoAI");
            this.mAIRegister = false;
            return;
        }
        Log.d(this.TAG, "reDegisterXiaoAI: permission = " + zQueryPermission);
        if (this.mXiaoAiHandler.hasMessages(100)) {
            this.mXiaoAiHandler.removeMessages(100);
        }
        XiaoAiHandler xiaoAiHandler = this.mXiaoAiHandler;
        xiaoAiHandler.sendMessageDelayed(xiaoAiHandler.obtainMessage(100), 600000L);
    }
}
