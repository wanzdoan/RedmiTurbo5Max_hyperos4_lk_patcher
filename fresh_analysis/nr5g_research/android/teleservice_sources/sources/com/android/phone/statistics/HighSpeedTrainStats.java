package com.android.phone.statistics;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.utils.Utils;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class HighSpeedTrainStats {
    private static volatile HighSpeedTrainStats sInstance;
    private ContentObserver mCollectSwitchObserver;
    private volatile boolean mCollectSwitchOn;
    private int mFileUploadCount;
    private final Handler mHandler;
    private int mPreferCellCount;
    private int mPreferFreqCount;
    private SceneAwareCallback mSceneAwareCallback;
    private long mSmartSwitchStartTime;
    private final HandlerThread mWorkThread;
    private final Object mLock = new Object();
    private final CopyOnWriteArrayList mPendingRecords = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList mCollectSwitchListeners = new CopyOnWriteArrayList();
    private final Deque mQoeSamples = new ArrayDeque();
    private int mSmartSwitchBeforeScore = -1;
    private int mCaDynamicOptBeforeScore = -1;

    public interface CollectSwitchListener {
        void onCollectSwitchChanged(boolean z);
    }

    private HighSpeedTrainStats() {
        HandlerThread handlerThread = new HandlerThread("HighSpeedTrainStats", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new HstStatsHandler(handlerThread.getLooper());
        this.mCollectSwitchOn = isCollectSwitchOn();
        registerCollectSwitchObserver();
        registerSceneAwareCallback();
    }

    private static HighSpeedTrainStats getInstance() {
        if (sInstance == null) {
            synchronized (HighSpeedTrainStats.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new HighSpeedTrainStats();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public static void init() {
        getInstance();
    }

    public static void registerCollectSwitchListener(CollectSwitchListener collectSwitchListener) {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats == null || collectSwitchListener == null || highSpeedTrainStats.mCollectSwitchListeners.contains(collectSwitchListener)) {
            return;
        }
        highSpeedTrainStats.mCollectSwitchListeners.add(collectSwitchListener);
        if (highSpeedTrainStats.mCollectSwitchOn) {
            try {
                collectSwitchListener.onCollectSwitchChanged(true);
            } catch (Exception e) {
                Utils.logD("HighSpeedTrainStats", "registerCollectSwitchListener exception:" + e);
            }
        }
    }

    public static void recordPreferCell() {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats != null) {
            synchronized (highSpeedTrainStats.mLock) {
                try {
                    if (highSpeedTrainStats.mCollectSwitchOn) {
                        highSpeedTrainStats.mPreferCellCount++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static void recordPreferFreq() {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats != null) {
            synchronized (highSpeedTrainStats.mLock) {
                try {
                    if (highSpeedTrainStats.mCollectSwitchOn) {
                        highSpeedTrainStats.mPreferFreqCount++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static void recordFileUpload() {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats != null) {
            synchronized (highSpeedTrainStats.mLock) {
                try {
                    if (highSpeedTrainStats.mCollectSwitchOn) {
                        highSpeedTrainStats.mFileUploadCount++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static void recordHstStateEnhance(long j, int i) {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats == null || j <= 0) {
            return;
        }
        highSpeedTrainStats.appendRecord(createStateEnhanceRecord(j / 1000, i));
    }

    public static void beginSmartSwitchOpt() {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats != null) {
            synchronized (highSpeedTrainStats.mLock) {
                try {
                    if (highSpeedTrainStats.mSmartSwitchBeforeScore < 0) {
                        highSpeedTrainStats.mSmartSwitchBeforeScore = highSpeedTrainStats.getAverageNetworkScore();
                        highSpeedTrainStats.mSmartSwitchStartTime = SystemClock.elapsedRealtime();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static void finishSmartSwitchOptIfNeeded(boolean z) {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats == null) {
            return;
        }
        synchronized (highSpeedTrainStats.mLock) {
            try {
                if (highSpeedTrainStats.mSmartSwitchBeforeScore < 0) {
                    return;
                }
                if (z || SystemClock.elapsedRealtime() - highSpeedTrainStats.mSmartSwitchStartTime >= 30000) {
                    if (z) {
                        int averageNetworkScore = highSpeedTrainStats.getAverageNetworkScore();
                        if (averageNetworkScore < 0) {
                            return;
                        } else {
                            highSpeedTrainStats.appendQoeRecord("hst_smart_switch", highSpeedTrainStats.mSmartSwitchBeforeScore, averageNetworkScore);
                        }
                    }
                    highSpeedTrainStats.mSmartSwitchBeforeScore = -1;
                    highSpeedTrainStats.mSmartSwitchStartTime = 0L;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void beginCaDynamicOpt() {
        HighSpeedTrainStats highSpeedTrainStats = getInstance();
        if (highSpeedTrainStats == null) {
            return;
        }
        synchronized (highSpeedTrainStats.mLock) {
            highSpeedTrainStats.mCaDynamicOptBeforeScore = highSpeedTrainStats.getAverageNetworkScore();
        }
        highSpeedTrainStats.mHandler.removeMessages(2);
        highSpeedTrainStats.mHandler.sendEmptyMessageDelayed(2, 15000L);
    }

    private void appendRecord(PendingRecord pendingRecord) {
        if (pendingRecord == null) {
            return;
        }
        synchronized (this.mLock) {
            try {
                if (!this.mCollectSwitchOn) {
                    Utils.logD("HighSpeedTrainStats", "appendRecord skipped feature=" + pendingRecord.feature + ", collectOff");
                } else {
                    this.mPendingRecords.add(pendingRecord);
                    Utils.logD("HighSpeedTrainStats", "appendRecord success feature=" + pendingRecord.feature);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void appendQoeRecord(String str, int i, int i2) {
        if (i < 0 || i2 < 0) {
            Utils.logD("HighSpeedTrainStats", "appendQoeRecord skipped feature=" + str + ", before=" + i + ", after=" + i2);
            return;
        }
        appendRecord(createQoeRecord(str, i, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishCaDynamicOpt() {
        synchronized (this.mLock) {
            try {
                if (this.mCaDynamicOptBeforeScore < 0) {
                    return;
                }
                int averageNetworkScore = getAverageNetworkScore();
                if (averageNetworkScore < 0) {
                    return;
                }
                appendQoeRecord("hst_ca_dynamic_opt", this.mCaDynamicOptBeforeScore, averageNetworkScore);
                this.mCaDynamicOptBeforeScore = -1;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static PendingRecord createRecord(String str, JSONObject jSONObject) {
        return new PendingRecord(str, jSONObject);
    }

    private static PendingRecord createCountRecord(String str, int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("count", i);
        } catch (JSONException e) {
            Utils.logD("HighSpeedTrainStats", "createCountRecord exception:" + e);
        }
        return createRecord(str, jSONObject);
    }

    private static PendingRecord createQoeRecord(String str, int i, int i2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("preQoeAvg", i);
            jSONObject.put("postQoeAvg", i2);
        } catch (JSONException e) {
            Utils.logD("HighSpeedTrainStats", "createQoeRecord exception:" + e);
        }
        return createRecord(str, jSONObject);
    }

    private static PendingRecord createStateEnhanceRecord(long j, int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("duration_s", j);
            jSONObject.put("count", i);
        } catch (JSONException e) {
            Utils.logD("HighSpeedTrainStats", "createStateEnhanceRecord exception:" + e);
        }
        return createRecord("hst_state_enhance", jSONObject);
    }

    private static String toJsonString(PendingRecord pendingRecord, long j) {
        if (pendingRecord != null && !TextUtils.isEmpty(pendingRecord.feature)) {
            JSONObject jSONObject = new JSONObject();
            try {
                JSONObject jSONObject2 = pendingRecord.data == null ? new JSONObject() : new JSONObject(pendingRecord.data.toString());
                jSONObject2.put("time", j);
                jSONObject.put(pendingRecord.feature, jSONObject2);
                return jSONObject.toString();
            } catch (JSONException e) {
                Utils.logD("HighSpeedTrainStats", "toJsonString exception:" + e);
            }
        }
        return "";
    }

    class PendingRecord {
        final JSONObject data;
        final String feature;

        PendingRecord(String str, JSONObject jSONObject) {
            this.feature = str;
            this.data = jSONObject;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkQoeChanged(int i) {
        if (i < 0) {
            return;
        }
        synchronized (this.mLock) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            this.mQoeSamples.addLast(new QoeSample(jElapsedRealtime, i));
            removeExpiredQoeSamples(jElapsedRealtime);
        }
    }

    private int getAverageNetworkScore() {
        synchronized (this.mLock) {
            try {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                removeExpiredQoeSamples(jElapsedRealtime);
                QoeSample qoeSample = (QoeSample) this.mQoeSamples.peekLast();
                if (qoeSample != null && jElapsedRealtime - qoeSample.timestamp <= 20000) {
                    long j = jElapsedRealtime - MirrorDesktopHelper.TIMEOUT_MILLIS;
                    long j2 = 0;
                    QoeSample qoeSample2 = null;
                    for (QoeSample qoeSample3 : this.mQoeSamples) {
                        if (qoeSample2 != null) {
                            long jMax = Math.max(qoeSample2.timestamp, j);
                            long j3 = qoeSample3.timestamp;
                            if (j3 > jMax) {
                                j2 += ((long) qoeSample2.score) * (j3 - jMax);
                            }
                        }
                        qoeSample2 = qoeSample3;
                    }
                    if (qoeSample2 != null) {
                        long jMax2 = Math.max(qoeSample2.timestamp, j);
                        if (jElapsedRealtime > jMax2) {
                            j2 += ((long) qoeSample2.score) * (jElapsedRealtime - jMax2);
                        }
                    }
                    return (int) (j2 / MirrorDesktopHelper.TIMEOUT_MILLIS);
                }
                return -1;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void removeExpiredQoeSamples(long j) {
        QoeSample qoeSample;
        long j2 = j - MirrorDesktopHelper.TIMEOUT_MILLIS;
        while (this.mQoeSamples.size() > 1 && (qoeSample = (QoeSample) this.mQoeSamples.peekFirst()) != null && qoeSample.timestamp < j2) {
            this.mQoeSamples.removeFirst();
        }
    }

    class QoeSample {
        final int score;
        final long timestamp;

        QoeSample(long j, int i) {
            this.timestamp = j;
            this.score = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCollectSwitchOn() {
        try {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            return (phoneGlobals == null || phoneGlobals.getContentResolver() == null || Settings.Global.getInt(phoneGlobals.getContentResolver(), "hst_qos_cdc_collect_switch", 0) != 1) ? false : true;
        } catch (Exception e) {
            Utils.logD("HighSpeedTrainStats", "isCollectSwitchOn exception:" + e);
            return false;
        }
    }

    private void registerCollectSwitchObserver() {
        try {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            if (phoneGlobals != null && phoneGlobals.getContentResolver() != null) {
                ContentResolver contentResolver = phoneGlobals.getContentResolver();
                Uri uriFor = Settings.Global.getUriFor("hst_qos_cdc_collect_switch");
                ContentObserver contentObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.statistics.HighSpeedTrainStats.1
                    @Override // android.database.ContentObserver
                    public void onChange(boolean z) {
                        HighSpeedTrainStats highSpeedTrainStats = HighSpeedTrainStats.this;
                        highSpeedTrainStats.processCollectSwitchChanged(highSpeedTrainStats.isCollectSwitchOn());
                    }
                };
                this.mCollectSwitchObserver = contentObserver;
                contentResolver.registerContentObserver(uriFor, false, contentObserver);
            }
        } catch (Exception e) {
            Utils.logD("HighSpeedTrainStats", "registerCollectSwitchObserver exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCollectSwitchChanged(boolean z) {
        synchronized (this.mLock) {
            try {
                if (this.mCollectSwitchOn == z) {
                    return;
                }
                if (z) {
                    this.mPendingRecords.clear();
                    this.mPreferCellCount = 0;
                    this.mPreferFreqCount = 0;
                    this.mFileUploadCount = 0;
                    this.mCollectSwitchOn = true;
                } else {
                    this.mCollectSwitchOn = false;
                }
                if (z) {
                    notifyCollectSwitchChanged(true);
                } else {
                    notifyCollectSwitchChanged(false);
                    flushPendingRecords();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void notifyCollectSwitchChanged(boolean z) {
        for (CollectSwitchListener collectSwitchListener : this.mCollectSwitchListeners) {
            if (collectSwitchListener != null) {
                try {
                    collectSwitchListener.onCollectSwitchChanged(z);
                } catch (Exception e) {
                    Utils.logD("HighSpeedTrainStats", "notifyCollectSwitchChanged exception:" + e);
                }
            }
        }
    }

    private void flushPendingRecords() {
        synchronized (this.mLock) {
            try {
                if (this.mPendingRecords.isEmpty() && this.mPreferCellCount <= 0 && this.mPreferFreqCount <= 0 && this.mFileUploadCount <= 0) {
                    Utils.logD("HighSpeedTrainStats", "flushPendingRecords skipped, no pending data");
                    return;
                }
                int i = this.mPreferCellCount;
                if (i > 0) {
                    this.mPendingRecords.add(createCountRecord("hst_prefer_cell", i));
                }
                int i2 = this.mPreferFreqCount;
                if (i2 > 0) {
                    this.mPendingRecords.add(createCountRecord("hst_prefer_freq", i2));
                }
                int i3 = this.mFileUploadCount;
                if (i3 > 0) {
                    this.mPendingRecords.add(createCountRecord("hst_file_upload", i3));
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                Utils.logD("HighSpeedTrainStats", "flushPendingRecords start, pendingCount=" + this.mPendingRecords.size() + ", time=" + jCurrentTimeMillis);
                for (PendingRecord pendingRecord : this.mPendingRecords) {
                    String jsonString = toJsonString(pendingRecord, jCurrentTimeMillis);
                    if (TextUtils.isEmpty(jsonString)) {
                        Utils.logD("HighSpeedTrainStats", "flushPendingRecords skip empty record feature=" + pendingRecord.feature);
                    } else {
                        Utils.logD("HighSpeedTrainStats", "flushPendingRecords build feature=" + pendingRecord.feature + ", json=" + jsonString);
                        writeStoredRecordLocked(pendingRecord.feature, jsonString);
                    }
                }
                this.mPendingRecords.clear();
                this.mPreferCellCount = 0;
                this.mPreferFreqCount = 0;
                this.mFileUploadCount = 0;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void registerSceneAwareCallback() {
        try {
            HyperCommManager hyperCommManager = HyperCommManager.getDefault();
            if (hyperCommManager == null) {
                return;
            }
            HstSceneAwareCallback hstSceneAwareCallback = new HstSceneAwareCallback(new HandlerExecutor(this.mHandler));
            if (hyperCommManager.registerSceneAwareCallback(hstSceneAwareCallback)) {
                this.mSceneAwareCallback = hstSceneAwareCallback;
            }
        } catch (Exception e) {
            Utils.logD("HighSpeedTrainStats", "registerSceneAwareCallback exception:" + e);
        }
    }

    private void writeStoredRecordLocked(String str, String str2) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (phoneGlobals == null) {
            Utils.logD("HighSpeedTrainStats", "writeStoredRecordLocked skipped, context null feature=" + str);
            return;
        }
        ContentResolver contentResolver = phoneGlobals.getContentResolver();
        if (contentResolver == null) {
            Utils.logD("HighSpeedTrainStats", "writeStoredRecordLocked skipped, resolver null feature=" + str);
            return;
        }
        if (str2 == null) {
            str2 = "";
        }
        Settings.Global.putString(contentResolver, str, str2);
        Utils.logD("HighSpeedTrainStats", "writeStoredRecordLocked success feature=" + str + ", length=" + str2.length());
    }

    class HstStatsHandler extends Handler {
        HstStatsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                HighSpeedTrainStats.this.processNetworkQoeChanged(message.arg1);
            } else {
                if (i != 2) {
                    return;
                }
                HighSpeedTrainStats.this.finishCaDynamicOpt();
            }
        }
    }

    class HstSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.NetworkQoeListener {
        HstSceneAwareCallback(HandlerExecutor handlerExecutor) {
            super(handlerExecutor);
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            HighSpeedTrainStats.this.mHandler.obtainMessage(2, i5, 0).sendToTarget();
        }
    }
}
