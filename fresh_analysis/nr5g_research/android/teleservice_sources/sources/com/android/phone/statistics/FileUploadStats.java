package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class FileUploadStats {
    private static FileUploadStats sInstance;
    private MiuiFileUploadHandler mHandler;
    private HandlerThread mWorkThread;
    private ConcurrentHashMap mTriggerTimesStats = new ConcurrentHashMap();
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.FileUploadStats.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Utils.logD("FileUploadStats", "receive action".concat(action));
            if (action.equals("com.android.phone.intent.action.SET_QOS_STATUS")) {
                FileUploadStats.this.mHandler.obtainMessage(1, intent).sendToTarget();
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    private FileUploadStats() {
        HandlerThread handlerThread = new HandlerThread("FileUploadStats", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new MiuiFileUploadHandler(this.mWorkThread.getLooper());
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.SET_QOS_STATUS");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class MiuiFileUploadHandler extends Handler {
        MiuiFileUploadHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Utils.logD("FileUploadStats", "msg.what = " + message.what);
            if (message.what != 1) {
                return;
            }
            FileUploadStats.this.recordTriggerTimesForSetQos((Intent) message.obj);
        }
    }

    public static synchronized FileUploadStats getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new FileUploadStats();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public void recordTriggerTimes(String str) {
        ConcurrentHashMap concurrentHashMap = this.mTriggerTimesStats;
        concurrentHashMap.put(str, Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(str, 0)).intValue() + 1));
        Utils.logD("FileUploadStats", "recordTriggerTimes scenes: " + str);
        try {
            DefaultPreferenceHelper.getInstance().setFileUploadTriggerStats(ObjectSerializer.serialize(this.mTriggerTimesStats));
        } catch (Exception e) {
            Utils.logE("FileUploadStats", "record exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordTriggerTimesForSetQos(Intent intent) {
        if (intent.getBooleanExtra("setQosStatus", false)) {
            recordTriggerTimes("mSetQosForFileUploadCount");
        } else {
            recordTriggerTimes("mSetQosForFileUploadExitCount");
        }
    }

    public void reportStatistics() {
        report();
        clear();
    }

    public void reloadStatsFromSP() {
        try {
            String fileUploadTriggerStats = DefaultPreferenceHelper.getInstance().getFileUploadTriggerStats();
            if (fileUploadTriggerStats.isEmpty()) {
                return;
            }
            this.mTriggerTimesStats = (ConcurrentHashMap) ObjectSerializer.deserialize(fileUploadTriggerStats);
        } catch (Exception e) {
            Utils.logE("FileUploadStats", "reloadStatsFromSP exception:" + e);
        }
    }

    public void report() {
        if (this.mTriggerTimesStats.isEmpty()) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(this.mTriggerTimesStats);
        Utils.logD("FileUploadStats", "reportStatistics stats=" + concurrentHashMap);
        MiStatInterfaceUtil.recordObjectEvent("FILEUPLOAD", "event_file_upload_triggers", concurrentHashMap);
    }

    public void clear() {
        this.mTriggerTimesStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setFileUploadTriggerStats(ObjectSerializer.serialize(this.mTriggerTimesStats));
        } catch (Exception e) {
            Utils.logE("FileUploadStats", "clear exception:" + e);
        }
    }
}
