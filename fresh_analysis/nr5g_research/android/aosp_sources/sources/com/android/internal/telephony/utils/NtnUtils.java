package com.android.internal.telephony.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.ntnphone.NtnSmsDispatcher;
import com.android.internal.telephony.satellite.SatelliteSessionController;

/* JADX INFO: loaded from: classes.dex */
public class NtnUtils {
    private int mCurrentReportLevel;
    private Handler mHandler;
    private HandlerThread mWorkThread;
    private SignalStrength mCurrentShowSignalStrength = null;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.utils.NtnUtils.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.app.action.SATELLITE_STATE_CHANGE".equals(intent.getAction())) {
                boolean booleanExtra = intent.getBooleanExtra("is_enable", false);
                if (!booleanExtra && NtnUtils.this.mHandler.hasMessages(1)) {
                    NtnUtils.this.mHandler.removeMessages(1);
                }
                NtnUtils.this.log("NtnPhone changed, enabled = " + booleanExtra);
            }
        }
    };

    public NtnUtils() {
        HandlerThread handlerThread = new HandlerThread("NtnUtils");
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new WorkHandler(this.mWorkThread.getLooper());
        if (MiuiTelephonyManagerStub.get().isSupportSatelliteByDevice()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.app.action.SATELLITE_STATE_CHANGE");
            PhoneFactory.getDefaultPhone().getContext().registerReceiver(this.mIntentReceiver, intentFilter, 2);
        }
    }

    public SMSDispatcher getNtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        return new NtnSmsDispatcher(phone, smsDispatchersController);
    }

    public boolean ntnInboundSmsHandlerIsWaiting(Phone phone) {
        if (!TelephonyCommonUtilsStub.get().isSatelliteEnable(phone.getContext())) {
            return false;
        }
        try {
            InboundSmsHandler ntnInboundSmsHandler = ((NtnPhone) NtnPhoneFactory.getDefaultPhone()).getNtnInboundSmsHandler();
            if (ntnInboundSmsHandler != null && ntnInboundSmsHandler.getCurrentState().getName().equals("WaitingState")) {
                log("handlePartialSegmentTimerExpiry: ignoring timer expiry as NtnInboundSmsHandler is in WaitingState");
                return true;
            }
        } catch (Exception e) {
            loge("get ntnphones failed, exception: " + e);
        }
        return false;
    }

    public String getNtnImei(Context context, int i) {
        try {
            if (!TelephonyCommonUtilsStub.get().isSatelliteEnable(context)) {
                return null;
            }
            NtnPhone ntnPhone = (NtnPhone) NtnPhoneFactory.getDefaultPhone();
            if (ntnPhone.getPhoneId() == PhoneFactory.getPhone(i).getPhoneId()) {
                return ntnPhone.getImei();
            }
            return null;
        } catch (Exception e) {
            loge("getImei: " + e);
            return null;
        }
    }

    public boolean isDelayNotifySignalStrength(Phone phone) {
        boolean z;
        checkDelaySignalStrengthIfNeeded(phone);
        if (this.mHandler.hasMessages(1)) {
            z = false;
        } else {
            this.mCurrentShowSignalStrength = phone.getSignalStrength();
            z = true;
        }
        return !z;
    }

    private void checkDelaySignalStrengthIfNeeded(Phone phone) {
        this.mCurrentReportLevel = phone.getSignalStrength().getMiuiLevel();
        SignalStrength signalStrength = this.mCurrentShowSignalStrength;
        int miuiLevel = signalStrength == null ? 0 : signalStrength.getMiuiLevel();
        log("check if need update signal level delay, currentReportlevel = " + this.mCurrentReportLevel + ", currentShowLevel = " + miuiLevel);
        int i = this.mCurrentReportLevel;
        Handler handler = this.mHandler;
        if (i < miuiLevel) {
            if (handler.hasMessages(1)) {
                log("already have EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE msg, ignore");
                return;
            }
            if (this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, phone), SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS)) {
                log("send EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE msg success, timer = 3000");
                return;
            }
            return;
        }
        if (handler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
    }

    private class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            NtnUtils.this.log("msg.what = " + i);
            if (i == 1) {
                Phone phone = (Phone) message.obj;
                NtnUtils.this.log("EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE, update miuilevel ");
                NtnUtils.this.setNtnPhoneOptimizedLevel(phone);
            } else {
                NtnUtils.this.loge("unknown msg.what = " + i);
            }
        }
    }

    public void setNtnPhoneOptimizedLevel(Phone phone) {
        SignalStrength signalStrength = phone.getSignalStrength();
        if (signalStrength != null) {
            signalStrength.setMiuiLevel(this.mCurrentReportLevel);
            log("setNtnPhoneOptimizedLevel realSignalStrength : " + signalStrength);
            this.mCurrentShowSignalStrength = signalStrength;
            phone.notifySignalStrength();
        }
    }

    public int getShowMiuiLevel(int i) {
        if (!this.mHandler.hasMessages(1)) {
            return i;
        }
        int miuiLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
        log("need delay update , show currentShowLevel = " + miuiLevel);
        return miuiLevel;
    }

    protected void log(String str) {
        Rlog.d("NtnUtils", str);
    }

    protected void loge(String str) {
        Rlog.e("NtnUtils", str);
    }
}
