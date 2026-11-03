package com.android.phone.dsda;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGCloudController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.ModemCrashUtils;
import com.ot.pubsub.util.v;
import java.util.ArrayList;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDsdaSmartSwitcher {
    private static MiuiDsdaSmartSwitcher mInstance;
    private PendingIntent mAlarmIntent;
    private AlarmManager mAlarmManager;
    private DsdaInfoListener mDsdaInfoListener;
    private ArrayList mKeyWordsOfModemResetReason = new ArrayList();
    private LinkedList mSsrTimeListIntervalDay = new LinkedList();
    private LinkedList mSsrTimeListIntervalWeek = new LinkedList();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.dsda.MiuiDsdaSmartSwitcher.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MiuiDsdaSmartSwitcher.log("onReceive intent: " + action);
            if ("com.android.phone.alarm_disable_dsda_timeout".equals(action)) {
                MiuiDsdaSmartSwitcher.this.mHandler.sendEmptyMessageDelayed(4, 0L);
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                if (MiuiDsdaSmartSwitcher.this.mHandler.hasMessages(5)) {
                    MiuiDsdaSmartSwitcher.this.mHandler.removeMessages(5);
                }
                MiuiDsdaSmartSwitcher.this.mHandler.sendEmptyMessageDelayed(5, 2000L);
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();
    private Handler mHandler = new WorkHandler(BackgroundThread.get().getLooper());

    public static synchronized MiuiDsdaSmartSwitcher getInstance() {
        try {
            if (mInstance == null && MiuiPhoneUtils.isDeviceDsdaSupportedByQcom() && MiuiPhoneUtils.isPlatformDE30()) {
                mInstance = new MiuiDsdaSmartSwitcher();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private MiuiDsdaSmartSwitcher() {
        Phone phone;
        IntentFilter intentFilter = new IntentFilter("com.android.phone.alarm_disable_dsda_timeout");
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSwitchDsdaModeByResetHwMbn()) {
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        this.mKeyWordsOfModemResetReason.add("dsda");
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSwitchDsdaModeByResetHwMbn() || (phone = MiuiPhoneUtils.getPhone(0)) == null) {
            return;
        }
        phone.mCi.registerForModemReset(this.mHandler, 1, (Object) null);
    }

    public class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            MiuiDsdaSmartSwitcher.log("handleMessage  : " + message.what);
            int i = message.what;
            if (i == 1) {
                MiuiDsdaSmartSwitcher.log("EVENT_MODEM_RESET received");
                MiuiDsdaSmartSwitcher.this.mHandler.sendEmptyMessageDelayed(2, 30000L);
                return;
            }
            int i2 = 0;
            if (i != 2) {
                if (i == 3) {
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDsdaEnabled(false, 5)) {
                        MiuiDsdaSmartSwitcher.this.startTimeoutAlarm();
                        return;
                    }
                    return;
                } else if (i == 4) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDsdaEnabled(true, 5);
                    return;
                } else {
                    if (i != 5) {
                        return;
                    }
                    MiuiDsdaSmartSwitcher.this.handleDsdaCloudControlWhenScreenOff();
                    return;
                }
            }
            String modemCrashReason = ModemCrashUtils.getModemCrashReason();
            MiuiDsdaSmartSwitcher.log("modemCrashFileValue = " + modemCrashReason);
            ArrayList arrayList = MiuiDsdaSmartSwitcher.this.mKeyWordsOfModemResetReason;
            int size = arrayList.size();
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                String str = (String) obj;
                if (modemCrashReason != null && modemCrashReason.toLowerCase().contains(str)) {
                    MiuiDsdaSmartSwitcher.this.handleModemSsr();
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDsdaCloudControlWhenScreenOff() {
        if (FiveGCloudController.getInstance() != null) {
            FiveGCloudController.getInstance();
            if (!FiveGCloudController.is5GDsdaSwitchUnhandledByCloud()) {
                boolean zIsDsdaEnabled = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDsdaEnabled();
                FiveGCloudController.getInstance();
                boolean zIs5GDsdaEnabledByCloud = FiveGCloudController.is5GDsdaEnabledByCloud();
                if (zIsDsdaEnabled == zIs5GDsdaEnabledByCloud) {
                    log("handleDsdaCloudControlWhenScreenOff no changed, return.");
                    return;
                } else {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDsdaEnabled(zIs5GDsdaEnabledByCloud, 2);
                    return;
                }
            }
        }
        log("handleDsdaCloudControlWhenScreenOff cloud controller null or unhandled dsda, return.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleModemSsr() {
        try {
            if (MiuiPhoneUtils.getInsertedSimCount() >= 2 && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDsdaEnabled()) {
                this.mSsrTimeListIntervalDay.add(Long.valueOf(SystemClock.elapsedRealtime()));
                this.mSsrTimeListIntervalWeek.add(Long.valueOf(SystemClock.elapsedRealtime()));
                if (calculateDisableDsdaForModemSsr(this.mSsrTimeListIntervalDay, 3, v.b) || calculateDisableDsdaForModemSsr(this.mSsrTimeListIntervalWeek, 5, v.a)) {
                    this.mSsrTimeListIntervalDay.clear();
                    this.mSsrTimeListIntervalWeek.clear();
                    log("Modem ssr frequently, disable dsda");
                    if (this.mHandler.hasMessages(3)) {
                        this.mHandler.removeMessages(3);
                    }
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(3), 15000L);
                }
                return;
            }
            this.mSsrTimeListIntervalDay.clear();
            this.mSsrTimeListIntervalWeek.clear();
            log("two SIMs undetected, or not under DSDA  sub mode, return.");
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized boolean calculateDisableDsdaForModemSsr(LinkedList linkedList, int i, long j) {
        log("calculateDisableDsdaForModemSsr enter, ssrTimeList : " + linkedList.toString() + " , maxCount : " + i);
        int size = linkedList.size();
        boolean z = false;
        if (size < i) {
            log("ssr count not reached, return");
            return false;
        }
        long jLongValue = ((Long) linkedList.getLast()).longValue();
        if (jLongValue - ((Long) linkedList.getFirst()).longValue() <= j) {
            log("calculateDisableDsdaForModemSsr need disable dsda.");
            z = true;
        } else {
            for (int i2 = size - 2; i2 >= 0; i2--) {
                if (jLongValue - ((Long) linkedList.get(i2)).longValue() > j) {
                    log("invalid time node index : " + i2);
                    linkedList.remove(i2);
                }
            }
            log("calculateDisableDsdaForModemSsr exit, ssrTimeList : " + linkedList.toString());
        }
        return z;
    }

    class DsdaInfoListener implements BaseEvent.IEventListener {
        DsdaInfoListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.DsdaEvent) {
                TelephonyEvent.DsdaEvent dsdaEvent = (TelephonyEvent.DsdaEvent) baseEvent;
                boolean zIsDsdaEnabled = dsdaEvent.isDsdaEnabled();
                int dsdaOpCode = dsdaEvent.getDsdaOpCode();
                MiuiDsdaSmartSwitcher.log("onDsdaEnabledChanged enabled : " + zIsDsdaEnabled + " , opCode : " + dsdaOpCode);
                if (dsdaOpCode == 1 || dsdaOpCode == 2 || dsdaOpCode == 3) {
                    MiuiDsdaSmartSwitcher.this.cancelTimeoutAlarm();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTimeoutAlarm() {
        log("startTimeoutAlarm");
        this.mAlarmIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.phone.alarm_disable_dsda_timeout"), 335544320);
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + 1209600000, this.mAlarmIntent);
        this.mDsdaInfoListener = new DsdaInfoListener();
        EventHubManager.getInstance().registerListener("telephony", "dsda_state_changed", this.mDsdaInfoListener, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelTimeoutAlarm() {
        log("cancelTimeoutAlarm");
        PendingIntent pendingIntent = this.mAlarmIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
            this.mAlarmIntent = null;
        }
        if (this.mDsdaInfoListener != null) {
            EventHubManager.getInstance().unregisterListener(this.mDsdaInfoListener);
            this.mDsdaInfoListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("MiuiDsdaSmartSwitcher", str);
    }
}
