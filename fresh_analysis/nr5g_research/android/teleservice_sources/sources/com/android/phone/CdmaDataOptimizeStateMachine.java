package com.android.phone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class CdmaDataOptimizeStateMachine extends StateMachine {
    public static int AUTO_TIMEOUT_DELAY_ALARM = 480000;
    protected static String TAG = "CdmaDataOptimizeStateMachine";
    private static CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine;
    protected static String[] sCmdToString = {"EVENT_INITIAL_TIMER", "EVENT_START_TIMER", "EVENT_TIMER_OUT_DONE", "EVENT_SERVICE_INFO_1X_CHANGED", "EVENT_INTENT_VALUE_ICC_LOADED", "EVENT_DATA_CONNECTED_STATE_CHANGED", "EVENT_WIFI_DISCONNECTED_STATE_CHANGED", "EVENT_END_CALL_STATE_CHANGED", "EVENT_DATA_SLOT_CHANGED", "EVENT_ON_AIRPLANE_MODE_CHANGED", "EVENT_INTENT_VALUE_ICC_ABSENT_CHANGED", "EVENT_SERVICE_INFO_OOS_CHANGED", "EVENT_WIFI_CONNECTED_STATE_CHANGED", "EVENT_SERVICE_INFO_LTE_OR_NR_CHANGED", "EVENT_DATA_DISABLED_STATE_CHANGED", "EVENT_DATA_ENABLED_STATE_CHANGED"};
    protected PendingIntent mAlarmIntent;
    protected AlarmManager mAlarmManager;
    protected Context mContext;
    protected CdmaDataOptimizeManager.CdmaDataOptimizeHandler mHandler;
    private State mInitialState;
    private State mTimerOutState;
    private State mTimerWorkingState;

    public static String cmdToString(int i) {
        if (i < 0) {
            return null;
        }
        String[] strArr = sCmdToString;
        if (i < strArr.length) {
            return strArr[i];
        }
        return null;
    }

    public static CdmaDataOptimizeStateMachine makeStateMachine(Context context, Handler handler) {
        CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine2 = new CdmaDataOptimizeStateMachine(context, handler);
        cdmaDataOptimizeStateMachine = cdmaDataOptimizeStateMachine2;
        cdmaDataOptimizeStateMachine2.start();
        return cdmaDataOptimizeStateMachine;
    }

    protected CdmaDataOptimizeStateMachine(Context context, Handler handler) {
        super(TAG);
        this.mInitialState = new InitialState();
        this.mTimerWorkingState = new TimerWorkingState();
        this.mTimerOutState = new TimerOutState();
        this.mContext = context;
        this.mHandler = (CdmaDataOptimizeManager.CdmaDataOptimizeHandler) handler;
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        initCdmaDataOptimizeStateMachine();
    }

    private void initCdmaDataOptimizeStateMachine() {
        addState(this.mInitialState);
        addState(this.mTimerWorkingState);
        addState(this.mTimerOutState);
        setInitialState(this.mInitialState);
        if (FiveGCloudController.isCdmaDataOptimizeTimeByCloud()) {
            AUTO_TIMEOUT_DELAY_ALARM = FiveGCloudController.getCloudCdmaDataOptimizeTime();
            log("CdmaDataOptimizeTimeByCloud  timeout = " + AUTO_TIMEOUT_DELAY_ALARM);
        }
    }

    class InitialState extends State {
        InitialState() {
        }

        public boolean processMessage(Message message) {
            CdmaDataOptimizeStateMachine.this.log("InitialState msg = " + CdmaDataOptimizeStateMachine.cmdToString(message.what));
            int i = message.what;
            if ((i == 3 || i == 4 || i == 5 || i == 6 || i == 7) && CdmaDataOptimizeStateMachine.this.isStartTimerWorking()) {
                CdmaDataOptimizeStateMachine.this.sendMessage(1);
                CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine = CdmaDataOptimizeStateMachine.this;
                cdmaDataOptimizeStateMachine.transitionTo(cdmaDataOptimizeStateMachine.mTimerWorkingState);
            }
            return true;
        }
    }

    class TimerWorkingState extends State {
        TimerWorkingState() {
        }

        public boolean processMessage(Message message) {
            CdmaDataOptimizeStateMachine.this.log("TimerWorkingState msg = " + CdmaDataOptimizeStateMachine.cmdToString(message.what));
            int i = message.what;
            if (i == 1) {
                CdmaDataOptimizeStateMachine.this.startTimeoutAlarm();
            } else if (i != 2) {
                switch (i) {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        CdmaDataOptimizeStateMachine.this.cancelTimeoutAlarm();
                        CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine = CdmaDataOptimizeStateMachine.this;
                        cdmaDataOptimizeStateMachine.transitionTo(cdmaDataOptimizeStateMachine.mInitialState);
                        CdmaDataOptimizeStateMachine.this.sendMessage(0);
                    default:
                        return true;
                }
            } else {
                CdmaDataOptimizeStateMachine.this.deferMessage(message);
                CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine2 = CdmaDataOptimizeStateMachine.this;
                cdmaDataOptimizeStateMachine2.transitionTo(cdmaDataOptimizeStateMachine2.mTimerOutState);
            }
            return true;
        }
    }

    class TimerOutState extends State {
        TimerOutState() {
        }

        public boolean processMessage(Message message) {
            CdmaDataOptimizeStateMachine.this.log("TimerOutState msg = " + CdmaDataOptimizeStateMachine.cmdToString(message.what));
            int i = message.what;
            if (i == 2) {
                CdmaDataOptimizeStateMachine.this.setDataEnabled(false);
            } else if (i == 13) {
                CdmaDataOptimizeStateMachine.this.setDataEnabled(true);
                CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine = CdmaDataOptimizeStateMachine.this;
                cdmaDataOptimizeStateMachine.sendMessage(cdmaDataOptimizeStateMachine.obtainMessage(0));
                CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine2 = CdmaDataOptimizeStateMachine.this;
                cdmaDataOptimizeStateMachine2.transitionTo(cdmaDataOptimizeStateMachine2.mInitialState);
            } else if (i == 15) {
                CdmaDataOptimizeManager.getInstance().cleanup();
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStartTimerWorking() {
        localLog("isStartTimerWorking", "mInCdmaState = " + CdmaDataOptimizeManager.mInCdmaState + ", isChinaTelecomSim = " + CdmaDataOptimizeManager.isChinaTelecomSim() + ", isMobileDataEnabled = " + MiuiPhoneUtils.isMobileDataEnabled(this.mContext) + ", mIsWifiConnected = " + CdmaDataOptimizeManager.mIsWifiConnected + ", mIsDataConnected = " + CdmaDataOptimizeManager.mIsDataConnected + ", mOnVoiceCall = " + CdmaDataOptimizeManager.mOnVoiceCall);
        return CdmaDataOptimizeManager.mInCdmaState && CdmaDataOptimizeManager.isChinaTelecomSim() && MiuiPhoneUtils.isMobileDataEnabled(this.mContext) && !CdmaDataOptimizeManager.mIsWifiConnected && CdmaDataOptimizeManager.mIsDataConnected && !CdmaDataOptimizeManager.mOnVoiceCall;
    }

    protected void setDataEnabled(boolean z) {
        localLog("setDataEnabled", "enabled = " + z);
        if (!z && CdmaDataOptimizeManager.isChinaTelecomSim()) {
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "cdma_data_disable_optimize");
            TelephonyManager.from(this.mContext).setDataEnabled(z);
        } else {
            TelephonyManager.from(this.mContext).setDataEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTimeoutAlarm() {
        log("startTimeoutAlarm");
        this.mAlarmIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.phone.timeout.alarm"), 335544320);
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + ((long) AUTO_TIMEOUT_DELAY_ALARM), this.mAlarmIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelTimeoutAlarm() {
        log("cancelTimeoutAlarm");
        PendingIntent pendingIntent = this.mAlarmIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
            this.mAlarmIntent = null;
        }
    }

    public void quitStateMachine() {
        log("quitStateMachine");
        cancelTimeoutAlarm();
        cdmaDataOptimizeStateMachine.quitNow();
    }

    protected void log(String str) {
        Log.d(TAG, str);
    }

    protected void loge(String str) {
        Log.e(TAG, str);
    }

    public static void localLog(String str, String str2) {
        FiveGManagerBase.getInstance();
        FiveGManagerBase.localLog(TAG, str, str2);
    }
}
