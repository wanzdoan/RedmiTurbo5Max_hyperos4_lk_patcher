package com.android.phone.streaming;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class StreamingOptimizationStats {
    private static volatile StreamingOptimizationStats mInstance;
    private ContentObserver mCollectSwitchObserver;
    private Context mContext;
    private boolean mIsCollectingStallTime;
    private boolean mIsStalling;
    private boolean mIsWhitelistForeground;
    private long mStallStartTime;
    private long mStallTotalTime;
    private long mWhitelistForegroundStartTime;
    private long mWhitelistForegroundTotalTime;
    private final StatsData mStatsData = new StatsData();
    private final CurrentScene mCurrentScene = new CurrentScene();
    private final AtomicBoolean mStallTimeObserverRegistered = new AtomicBoolean(false);
    private int mLastNetworkScore = -1;

    private boolean isStallScore(int i) {
        return i >= 0 && i < 80;
    }

    class StatsData {
        int sceneCount = 0;
        long totalDuration = 0;
        int opt1Count = 0;
        int opt1Effective = 0;
        int opt2Count = 0;
        int opt2Effective = 0;
        int stallCount = 0;

        StatsData() {
        }

        void reset() {
            this.sceneCount = 0;
            this.totalDuration = 0L;
            this.opt1Count = 0;
            this.opt1Effective = 0;
            this.opt2Count = 0;
            this.opt2Effective = 0;
            this.stallCount = 0;
        }

        public String toString() {
            return "StatsData{sceneCount=" + this.sceneCount + ", totalDuration=" + this.totalDuration + ", opt1=" + this.opt1Effective + "/" + this.opt1Count + ", opt2=" + this.opt2Effective + "/" + this.opt2Count + ", stall=" + this.stallCount + '}';
        }
    }

    class CurrentScene {
        boolean isActive = false;
        long enterTime = 0;
        int opt1Count = 0;
        int opt1Effective = 0;
        int opt2Count = 0;
        int opt2Effective = 0;
        int stallCount = 0;

        CurrentScene() {
        }

        void reset() {
            this.isActive = false;
            this.enterTime = 0L;
            this.opt1Count = 0;
            this.opt1Effective = 0;
            this.opt2Count = 0;
            this.opt2Effective = 0;
            this.stallCount = 0;
        }
    }

    private StreamingOptimizationStats() {
        Log.i("StreamingOptimizationStats", "StreamingOptimizationStats initialized");
    }

    public static StreamingOptimizationStats getInstance() {
        if (mInstance == null) {
            synchronized (StreamingOptimizationStats.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new StreamingOptimizationStats();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private boolean isCdcCollectEnabled() {
        return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "hst_qos_cdc_collect_switch", 0) == 1;
    }

    public void registerStallTimeObserver(Context context, Handler handler) {
        if (context == null || handler == null) {
            Log.w("StreamingOptimizationStats", "registerStallTimeObserver failed: context or handler is null");
            return;
        }
        if (this.mStallTimeObserverRegistered.compareAndSet(false, true)) {
            if (context.getApplicationContext() != null) {
                context = context.getApplicationContext();
            }
            this.mContext = context;
            this.mCollectSwitchObserver = new ContentObserver(handler) { // from class: com.android.phone.streaming.StreamingOptimizationStats.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z, Uri uri) {
                    super.onChange(z, uri);
                    StreamingOptimizationStats.this.handleCollectSwitchChanged();
                }
            };
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("hst_qos_cdc_collect_switch"), false, this.mCollectSwitchObserver);
            handleCollectSwitchChanged();
            Log.i("StreamingOptimizationStats", "Stall time observer registered");
        }
    }

    public void unregisterStallTimeObserver() {
        if (this.mStallTimeObserverRegistered.compareAndSet(true, false)) {
            Context context = this.mContext;
            if (context != null && this.mCollectSwitchObserver != null) {
                context.getContentResolver().unregisterContentObserver(this.mCollectSwitchObserver);
            }
            this.mCollectSwitchObserver = null;
            this.mContext = null;
            synchronized (this) {
                resetStallTimeStatsLocked();
                this.mIsCollectingStallTime = false;
            }
            Log.i("StreamingOptimizationStats", "Stall time observer unregistered");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleCollectSwitchChanged() {
        try {
            boolean zIsCdcCollectEnabled = isCdcCollectEnabled();
            if (zIsCdcCollectEnabled == this.mIsCollectingStallTime) {
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (zIsCdcCollectEnabled) {
                resetStallTimeStatsLocked();
                this.mIsCollectingStallTime = true;
                if (this.mIsWhitelistForeground) {
                    this.mWhitelistForegroundStartTime = jCurrentTimeMillis;
                    if (isStallScore(this.mLastNetworkScore)) {
                        this.mIsStalling = true;
                        this.mStallStartTime = jCurrentTimeMillis;
                    }
                }
                Log.i("StreamingOptimizationStats", "Stall time collect started");
            } else {
                finishActiveDurationsLocked(jCurrentTimeMillis);
                publishStallTimeInfoLocked(jCurrentTimeMillis);
                resetStallTimeStatsLocked();
                this.mIsCollectingStallTime = false;
                Log.i("StreamingOptimizationStats", "Stall time collect stopped");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void resetStallTimeStatsLocked() {
        this.mWhitelistForegroundStartTime = 0L;
        this.mStallStartTime = 0L;
        this.mWhitelistForegroundTotalTime = 0L;
        this.mStallTotalTime = 0L;
        this.mIsStalling = false;
    }

    private void finishActiveDurationsLocked(long j) {
        long j2 = this.mWhitelistForegroundStartTime;
        if (j2 > 0) {
            this.mWhitelistForegroundTotalTime += j - j2;
            this.mWhitelistForegroundStartTime = 0L;
        }
        long j3 = this.mStallStartTime;
        if (j3 > 0) {
            this.mStallTotalTime += j - j3;
            this.mStallStartTime = 0L;
        }
        this.mIsStalling = false;
    }

    public synchronized void onWhitelistForegroundChanged(boolean z) {
        try {
            if (this.mIsWhitelistForeground == z) {
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.mIsWhitelistForeground = z;
            if (this.mIsCollectingStallTime) {
                if (z) {
                    this.mWhitelistForegroundStartTime = jCurrentTimeMillis;
                    if (isStallScore(this.mLastNetworkScore)) {
                        this.mIsStalling = true;
                        this.mStallStartTime = jCurrentTimeMillis;
                    }
                } else {
                    long j = this.mWhitelistForegroundStartTime;
                    if (j > 0) {
                        this.mWhitelistForegroundTotalTime += jCurrentTimeMillis - j;
                        this.mWhitelistForegroundStartTime = 0L;
                    }
                    long j2 = this.mStallStartTime;
                    if (j2 > 0) {
                        this.mStallTotalTime += jCurrentTimeMillis - j2;
                        this.mStallStartTime = 0L;
                    }
                    this.mIsStalling = false;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onNetworkScoreChanged(int i) {
        try {
            this.mLastNetworkScore = i;
            if (this.mIsCollectingStallTime && this.mIsWhitelistForeground) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                boolean zIsStallScore = isStallScore(i);
                if (zIsStallScore && !this.mIsStalling) {
                    this.mIsStalling = true;
                    this.mStallStartTime = jCurrentTimeMillis;
                } else if (!zIsStallScore && this.mIsStalling) {
                    this.mStallTotalTime += jCurrentTimeMillis - this.mStallStartTime;
                    this.mStallStartTime = 0L;
                    this.mIsStalling = false;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void publishStallTimeInfoLocked(long j) {
        try {
            long j2 = this.mWhitelistForegroundTotalTime / 1000;
            long j3 = this.mStallTotalTime / 1000;
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("appForgroundTime", j2);
            jSONObject.put("stallTime", j3);
            jSONObject.put("time", j / 1000);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("hst_streaming_stall_time", jSONObject);
            Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), "hst_streaming_stall_time", jSONObject2.toString());
            Log.d("StreamingOptimizationStats", "Published HST streaming stall time to DB: " + jSONObject2);
        } catch (JSONException e) {
            Log.w("StreamingOptimizationStats", "Failed to build HST streaming stall time JSON", e);
        }
    }

    private String buildHstQosOptInfo(int i, int i2, int i3) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("optType", i);
            jSONObject.put("preScore", i2);
            jSONObject.put("postScore", i3);
            jSONObject.put("time", System.currentTimeMillis());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("os4_streaming_opt", jSONObject);
            return jSONObject2.toString();
        } catch (JSONException e) {
            Log.w("StreamingOptimizationStats", "Failed to build HST QoS opt info JSON", e);
            return "{}";
        }
    }

    private void publishHstQosOptInfo(int i, int i2, int i3) {
        if (!isCdcCollectEnabled()) {
            Log.d("StreamingOptimizationStats", "Skip publishing HST QoS opt info, collect switch is off");
            return;
        }
        String strBuildHstQosOptInfo = buildHstQosOptInfo(i, i2, i3);
        Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), "os4_streaming_opt", strBuildHstQosOptInfo);
        Log.d("StreamingOptimizationStats", "Published HST QoS opt info to DB: " + strBuildHstQosOptInfo);
    }

    public synchronized void onEnterScene() {
        try {
            if (this.mCurrentScene.isActive) {
                Log.w("StreamingOptimizationStats", "Already in scene, force exit previous scene");
                onExitScene();
            }
            CurrentScene currentScene = this.mCurrentScene;
            currentScene.isActive = true;
            currentScene.enterTime = System.currentTimeMillis();
            Log.i("StreamingOptimizationStats", "Enter scene at " + this.mCurrentScene.enterTime);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onExitScene() {
        if (!this.mCurrentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore exit");
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        CurrentScene currentScene = this.mCurrentScene;
        long j = jCurrentTimeMillis - currentScene.enterTime;
        StatsData statsData = this.mStatsData;
        statsData.sceneCount++;
        statsData.totalDuration += j;
        statsData.opt1Count += currentScene.opt1Count;
        statsData.opt1Effective += currentScene.opt1Effective;
        statsData.opt2Count += currentScene.opt2Count;
        statsData.opt2Effective += currentScene.opt2Effective;
        statsData.stallCount += currentScene.stallCount;
        Log.i("StreamingOptimizationStats", "Exit scene, duration=" + j + "ms, current scene: opt1=" + this.mCurrentScene.opt1Effective + "/" + this.mCurrentScene.opt1Count + ", opt2=" + this.mCurrentScene.opt2Effective + "/" + this.mCurrentScene.opt2Count + ", stall=" + this.mCurrentScene.stallCount);
        StringBuilder sb = new StringBuilder("Stats: ");
        sb.append(this.mStatsData.toString());
        Log.i("StreamingOptimizationStats", sb.toString());
        this.mCurrentScene.reset();
    }

    public synchronized void onOpt1Executed() {
        CurrentScene currentScene = this.mCurrentScene;
        if (!currentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt1 executed");
            return;
        }
        currentScene.opt1Count++;
        Log.d("StreamingOptimizationStats", "Opt1 executed, current scene count=" + this.mCurrentScene.opt1Count);
    }

    public synchronized void onOpt1Effective(int i, int i2) {
        CurrentScene currentScene = this.mCurrentScene;
        if (!currentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt1 effective");
            return;
        }
        currentScene.opt1Effective++;
        publishHstQosOptInfo(1, i, i2);
        Log.d("StreamingOptimizationStats", "Opt1 effective, before=" + i + ", after=" + i2 + ", current scene effective=" + this.mCurrentScene.opt1Effective);
    }

    public synchronized void onOpt1NotEffective(int i, int i2) {
        if (!this.mCurrentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt1 ineffective");
            return;
        }
        publishHstQosOptInfo(1, i, i2);
        Log.d("StreamingOptimizationStats", "Opt1 ineffective, before=" + i + ", after=" + i2);
    }

    public synchronized void onOpt2Executed() {
        CurrentScene currentScene = this.mCurrentScene;
        if (!currentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt2 executed");
            return;
        }
        currentScene.opt2Count++;
        Log.d("StreamingOptimizationStats", "Opt2 executed, current scene count=" + this.mCurrentScene.opt2Count);
    }

    public synchronized void onOpt2Effective(int i, int i2) {
        CurrentScene currentScene = this.mCurrentScene;
        if (!currentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt2 effective");
            return;
        }
        currentScene.opt2Effective++;
        publishHstQosOptInfo(2, i, i2);
        Log.d("StreamingOptimizationStats", "Opt2 effective, before=" + i + ", after=" + i2 + ", current scene effective=" + this.mCurrentScene.opt2Effective);
    }

    public synchronized void onOpt2NotEffective(int i, int i2) {
        if (!this.mCurrentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore opt2 ineffective");
            return;
        }
        publishHstQosOptInfo(2, i, i2);
        Log.d("StreamingOptimizationStats", "Opt2 ineffective, before=" + i + ", after=" + i2);
    }

    public synchronized void onStall(int i, int i2) {
        CurrentScene currentScene = this.mCurrentScene;
        if (!currentScene.isActive) {
            Log.w("StreamingOptimizationStats", "Not in scene, ignore stall");
            return;
        }
        if (i == 0) {
            Log.d("StreamingOptimizationStats", "Not a stall event, reason=" + i + ", networkScore=" + i2);
            return;
        }
        currentScene.stallCount++;
        Log.d("StreamingOptimizationStats", "Stall detected, current scene count=" + this.mCurrentScene.stallCount + ", reason=" + i + ", networkScore=" + i2);
    }

    public synchronized void reportStreamingStats() {
        try {
            if (this.mStatsData.sceneCount == 0) {
                Log.i("StreamingOptimizationStats", "No scene data to report");
                return;
            }
            HashMap map = new HashMap();
            map.put("scene_count", Integer.valueOf(this.mStatsData.sceneCount));
            map.put("total_duration", Long.valueOf(this.mStatsData.totalDuration));
            map.put("opt1_count", Integer.valueOf(this.mStatsData.opt1Count));
            map.put("opt1_effective", Integer.valueOf(this.mStatsData.opt1Effective));
            map.put("opt2_count", Integer.valueOf(this.mStatsData.opt2Count));
            map.put("opt2_effective", Integer.valueOf(this.mStatsData.opt2Effective));
            map.put("stall_count", Integer.valueOf(this.mStatsData.stallCount));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "streaming_opt_daily_stats", map);
            Log.i("StreamingOptimizationStats", "Report stats: " + this.mStatsData.toString());
            this.mStatsData.reset();
        } catch (Exception e) {
            Log.e("StreamingOptimizationStats", "Failed to report streaming stats", e);
        }
    }
}
