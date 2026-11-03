package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiQmsStats {
    private static MiuiQmsStats sInstance;
    private MiuiQmsHandler mHandler;
    private HandlerThread mWorkThread;
    private long mDataStallReqTime = -1;
    private String mDataStallTriggerReason = "";
    private ConcurrentHashMap mTriggerTimesStats = new ConcurrentHashMap();
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver(this) { // from class: com.android.phone.statistics.MiuiQmsStats.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Utils.logD("MiuiQmsStats", "receive action".concat(action));
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    private MiuiQmsStats() {
        HandlerThread handlerThread = new HandlerThread("MiuiQmsStats", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new MiuiQmsHandler(this.mWorkThread.getLooper());
        registerBroadcastReceiver();
    }

    public static synchronized MiuiQmsStats getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiuiQmsStats();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private void registerBroadcastReceiver() {
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter(), 2);
    }

    class MiuiQmsHandler extends Handler {
        MiuiQmsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Utils.logD("MiuiQmsStats", "msg.what = " + message.what);
            if (message.what != 1) {
                return;
            }
            MiuiQmsStats.this.recordTriggerTimes("data_stall_effect," + MiuiQmsStats.this.mDataStallTriggerReason);
        }
    }

    public void linkReliabilityChange() {
        if (this.mHandler.hasMessages(1) && "linkqos".equals(this.mDataStallTriggerReason)) {
            this.mHandler.removeMessages(1);
        }
    }

    public void gameDelay() {
        if (this.mHandler.hasMessages(1) && "game".equals(this.mDataStallTriggerReason)) {
            this.mHandler.removeMessages(1);
        }
    }

    public void dataLinkStall() {
        if (this.mHandler.hasMessages(1) && "weChat".equals(this.mDataStallTriggerReason)) {
            this.mHandler.removeMessages(1);
        }
    }

    public void cellChange() {
        if (this.mDataStallReqTime != -1 && SystemClock.elapsedRealtime() - this.mDataStallReqTime < 30000) {
            this.mDataStallReqTime = -1L;
            this.mHandler.sendEmptyMessageDelayed(1, 30000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordTriggerTimes(String str) {
        ConcurrentHashMap concurrentHashMap = this.mTriggerTimesStats;
        concurrentHashMap.put(str, Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(str, 0)).intValue() + 1));
        Utils.logD("MiuiQmsStats", "recordTriggerTimes scenes: " + str);
        try {
            DefaultPreferenceHelper.getInstance().setQmsTriggerStats(ObjectSerializer.serialize(this.mTriggerTimesStats));
        } catch (Exception e) {
            Utils.logE("MiuiQmsStats", "record exception:" + e);
        }
    }

    public void recordTriggerTimes(int i, String str) {
        recordTriggerTimes(String.valueOf(i) + "," + str);
        if (i == 1001) {
            this.mDataStallReqTime = SystemClock.elapsedRealtime();
            this.mDataStallTriggerReason = str;
        }
    }

    public void reportStatistics() {
        report();
        clear();
    }

    public void reloadStatsFromSP() {
        try {
            String qmsTriggerStats = DefaultPreferenceHelper.getInstance().getQmsTriggerStats();
            if (qmsTriggerStats.isEmpty()) {
                return;
            }
            this.mTriggerTimesStats = (ConcurrentHashMap) ObjectSerializer.deserialize(qmsTriggerStats);
        } catch (Exception e) {
            Utils.logE("MiuiQmsStats", "reloadStatsFromSP exception:" + e);
        }
    }

    public void report() {
        if (this.mTriggerTimesStats.isEmpty()) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(this.mTriggerTimesStats);
        Utils.logI("MiuiQmsStats", "reportStatistics stats=" + concurrentHashMap);
        MiStatInterfaceUtil.recordObjectEvent("QMS", "event_qms_triggers", concurrentHashMap);
    }

    public void clear() {
        this.mTriggerTimesStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setAirportModeStats(ObjectSerializer.serialize(this.mTriggerTimesStats));
        } catch (Exception e) {
            Utils.logE("MiuiQmsStats", "clear exception:" + e);
        }
    }
}
