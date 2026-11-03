package com.android.phone.ntn;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public class NtnNetworkRecoveryController {
    private NtnBoxServiceManager mNtnBoxServiceManager;
    private boolean mInprocress = false;
    private final Handler mHandler = new RecoveryHandler();

    class RecoveryHandler extends Handler {
        RecoveryHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NtnLogUtils.logd("NtnNetworkRecoveryController", "RecoveryHandler handleMessage:" + message.what);
            int i = message.what;
            if (i == 1) {
                NtnNetworkRecoveryController.this.mNtnBoxServiceManager.turnNtnRadioOn();
            } else if (i == 3) {
                NtnNetworkRecoveryController.this.mNtnBoxServiceManager.rebootNtnStack();
            } else {
                if (i != 4) {
                    return;
                }
                NtnNetworkRecoveryController.this.mInprocress = false;
            }
        }
    }

    public void setBoxServiceManager(NtnBoxServiceManager ntnBoxServiceManager) {
        this.mNtnBoxServiceManager = ntnBoxServiceManager;
    }

    public void recoverNtnNetwork() {
        recoverNtnNetwork(0);
    }

    public void recoverNtnNetwork(int i) {
        if (this.mInprocress) {
            NtnLogUtils.logd("NtnNetworkRecoveryController", "recoverNtnNetwork recovery is in progress, Skip!!!");
            return;
        }
        if (NtnPhoneProxyBase.getInstance().isNtnInService()) {
            NtnLogUtils.logd("NtnNetworkRecoveryController", "recoverNtnNetwork recovery already in Service, Skip!!!");
            return;
        }
        NtnLogUtils.logd("NtnNetworkRecoveryController", "recoverNtnNetwork scene:" + convertSceneIdToString(i));
        if (i == 0 || i == 1) {
            this.mNtnBoxServiceManager.turnNtnRadioOff();
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1), 1000L);
        } else if (i == 2) {
            this.mNtnBoxServiceManager.resetNtnStack();
            Handler handler2 = this.mHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(2), 2000L);
        } else if (i == 3) {
            this.mNtnBoxServiceManager.shutdownNtn();
            Handler handler3 = this.mHandler;
            handler3.sendMessageDelayed(handler3.obtainMessage(3), 3000L);
        }
        this.mInprocress = true;
        Handler handler4 = this.mHandler;
        handler4.sendMessageDelayed(handler4.obtainMessage(4), 120000L);
    }

    private String convertSceneIdToString(int i) {
        if (i == 0) {
            return "RECOVER_SCENE_DEFAULT";
        }
        if (i == 1) {
            return "RECOVER_SCENE_RADIO_POWER_RESET";
        }
        if (i == 2) {
            return "RECOVER_SCENE_STACK_RESET";
        }
        if (i == 3) {
            return "RECOVER_SCENE_STACK_REBOOT";
        }
        return String.valueOf(i);
    }
}
