package com.android.internal.telephony.utils;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class NtnBoxOptimization {
    private static volatile NtnBoxOptimization instance;
    private Context mContext;
    private final NtnBoxHandler mNtnBoxHandler;
    private final NtnPhone mPhone;
    private int mLastServiceState = 3;
    private PhoneConstants.State mLastPhoneState = PhoneConstants.State.IDLE;
    private boolean mLowPowerState = false;

    private NtnBoxOptimization(Context context) {
        NtnBoxHandler ntnBoxHandler = new NtnBoxHandler();
        this.mNtnBoxHandler = ntnBoxHandler;
        NtnPhone ntnPhone = (NtnPhone) NtnPhoneFactory.getDefaultPhone();
        this.mPhone = ntnPhone;
        this.mContext = context;
        ntnPhone.registerForPreciseCallStateChanged(ntnBoxHandler, 1, null);
        ntnPhone.registerForServiceStateChanged(ntnBoxHandler, 2, null);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new NtnBoxOptimization(context);
        }
    }

    public static NtnBoxOptimization getInstance() {
        if (instance == null) {
            loge("instance is Null");
        }
        return instance;
    }

    public void destroy() {
        this.mPhone.unregisterForPreciseCallStateChanged(this.mNtnBoxHandler);
        this.mPhone.unregisterForServiceStateChanged(this.mNtnBoxHandler);
        instance = null;
    }

    private class NtnBoxHandler extends Handler {
        public NtnBoxHandler() {
            super(Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            NtnBoxOptimization.logi("handleMessage " + i);
            if (i == 1) {
                NtnBoxOptimization.this.onCallStateChanged((Phone) ((AsyncResult) message.obj).result);
            } else if (i == 2) {
                NtnBoxOptimization.this.onServiceStateChanged((ServiceState) ((AsyncResult) message.obj).result);
            } else {
                if (i != 3) {
                    return;
                }
                NtnBoxOptimization.this.mPhone.setScreenState(0);
                NtnBoxOptimization.this.setLowPowerState(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceStateChanged(ServiceState serviceState) {
        int state = serviceState.getState();
        if (state == 0 && this.mLastServiceState != 0) {
            sendScreenOffMessage();
        } else if (state != 0 && this.mLastServiceState == 0) {
            removeSceneOffMessage();
        }
        logi("onServiceStateChanged: serviceState = " + state);
        this.mLastServiceState = state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallStateChanged(Phone phone) {
        PhoneConstants.State state = phone.getState();
        PhoneConstants.State state2 = PhoneConstants.State.IDLE;
        if (state == state2 && this.mLastPhoneState != state2) {
            sendScreenOffMessage();
            logi("onCallStateChanged: sendScreenOffMessage");
        }
        this.mLastPhoneState = state;
    }

    public void sendScreenOffMessage() {
        removeSceneOffMessage();
        this.mNtnBoxHandler.sendMessageDelayed(this.mNtnBoxHandler.obtainMessage(3), 5000L);
    }

    public void sendScreenOffMessageByLongTime() {
        removeSceneOffMessage();
        this.mNtnBoxHandler.sendMessageDelayed(this.mNtnBoxHandler.obtainMessage(3), SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS);
    }

    public void onSendSmsStatusReport(HashMap map) {
        if (map.isEmpty()) {
            sendScreenOffMessage();
            logi("onSendSmsStatusReport: deliveryPendingMapForNtn is empty");
        } else {
            logi("onSendSmsStatusReport:  is not empty");
        }
    }

    public void onReceiveNewSms() {
        sendScreenOffMessage();
        logi("onReceiveNewSms: Receive new");
    }

    public void removeSceneOffMessage() {
        if (this.mNtnBoxHandler.hasMessages(3)) {
            this.mNtnBoxHandler.removeMessages(3);
        }
    }

    public void setLowPowerState(boolean z) {
        this.mLowPowerState = z;
    }

    public boolean getLowPowerState() {
        logd("Low power state = " + String.valueOf(this.mLowPowerState));
        return this.mLowPowerState;
    }

    protected static void logi(String str) {
        Log.i("NtnBoxOptimization", str);
    }

    protected static void logd(String str) {
        Log.d("NtnBoxOptimization", str);
    }

    protected static void loge(String str) {
        Log.e("NtnBoxOptimization", str);
    }
}
