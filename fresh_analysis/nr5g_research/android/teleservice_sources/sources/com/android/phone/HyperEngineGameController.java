package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.Rlog;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import java.util.ArrayDeque;

/* JADX INFO: loaded from: classes.dex */
public class HyperEngineGameController {
    private static boolean VDBG = false;
    private static HyperEngineGameController sInstance;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private int mCurrentGameDelay;
    private String mCurrentGamePackage;
    private boolean mHasGameDelayMonitorState;
    private long mHighDelayStartTime;
    private boolean mIsGameBattle;
    private boolean mIsGameDelayListenerRegistered;
    private NetworkMetricsController mNetworkMetricsController;
    private final Handler mHandler = new Handler(MiuiThreadUtil.getSmartNetworkWorkThread());
    private final ArrayDeque mHighDelayTimestamps = new ArrayDeque();
    private final ArrayDeque mHighDelayDurations = new ArrayDeque();
    private int mCurrentGameDelayThreshold = -1;
    private final BroadcastReceiver mDebugReceiver = new AnonymousClass1();
    private final NetworkMetricsController.GameDelayListener mGameDelayListener = new AnonymousClass2();
    private final Runnable mHighDelayOverTimeRunnable = new Runnable() { // from class: com.android.phone.HyperEngineGameController.3
        @Override // java.lang.Runnable
        public void run() {
            if (HyperEngineGameController.this.isCurrentGameDelayMonitorSupported() && HyperEngineGameController.this.mCurrentGameDelay >= HyperEngineGameController.this.mCurrentGameDelayThreshold && HyperEngineGameController.this.mIsGameBattle && HyperEngineGameController.this.isStrongSignalHyperEngineGameOptActive()) {
                HyperEngineGameController.this.disableHyperEngineGameOpt(DisableReason.REASON_HIGH_DELAY_OVER_TIME);
            }
        }
    };
    private final Runnable mHighDelayAccumulatedRunnable = new Runnable() { // from class: com.android.phone.HyperEngineGameController.4
        @Override // java.lang.Runnable
        public void run() {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (HyperEngineGameController.this.isCurrentGameDelayMonitorSupported() && HyperEngineGameController.this.mCurrentGameDelay >= HyperEngineGameController.this.mCurrentGameDelayThreshold && HyperEngineGameController.this.mIsGameBattle && HyperEngineGameController.this.isStrongSignalHyperEngineGameOptActive() && HyperEngineGameController.this.getAccumulatedHighDelayTime(jElapsedRealtime) >= 24000) {
                HyperEngineGameController.this.disableHyperEngineGameOpt(DisableReason.REASON_HIGH_DELAY_ACCUMULATED);
            }
        }
    };

    enum DisableReason {
        REASON_HIGH_DELAY_PULSE("high delay pulse count"),
        REASON_HIGH_DELAY_ACCUMULATED("high delay accumulated over 24s"),
        REASON_HIGH_DELAY_OVER_TIME("high delay over 15s"),
        REASON_SECRET_CODE("secret code");

        private final String mDescription;

        DisableReason(String str) {
            this.mDescription = str;
        }

        String getDescription() {
            return this.mDescription;
        }
    }

    public static synchronized HyperEngineGameController init(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new HyperEngineGameController(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public static HyperEngineGameController getInstance() {
        return sInstance;
    }

    private HyperEngineGameController(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        initGameOptimizationSetting();
        registerDebugReceiver();
        log("HyperEngineGameController init");
    }

    public void registerNetworkMetricsController(NetworkMetricsController networkMetricsController) {
        if (networkMetricsController == null || this.mNetworkMetricsController == networkMetricsController || this.mIsGameDelayListenerRegistered) {
            return;
        }
        this.mNetworkMetricsController = networkMetricsController;
        networkMetricsController.registerGameDelayListener(this.mGameDelayListener);
        this.mIsGameDelayListenerRegistered = true;
    }

    public void unregisterNetworkMetricsController() {
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController != null && this.mIsGameDelayListenerRegistered) {
            networkMetricsController.unregisterGameDelayListener(this.mGameDelayListener);
        }
        this.mIsGameDelayListenerRegistered = false;
        this.mNetworkMetricsController = null;
        this.mIsGameBattle = false;
        resetGameDelayMonitor();
        logD("unregister game delay listener for HyperEngine monitor");
    }

    private void initGameOptimizationSetting() {
        if (Settings.Secure.getInt(this.mContentResolver, "hyper_engine_game_optimization_enabled", -1) == -1) {
            Settings.Secure.putInt(this.mContentResolver, "hyper_engine_game_optimization_enabled", 1);
        }
    }

    private void registerDebugReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.HYPER_ENGINE_GAME_DEBUG");
        this.mContext.registerReceiver(this.mDebugReceiver, intentFilter, 2);
        log("register HyperEngine game debug receiver");
    }

    /* JADX INFO: renamed from: com.android.phone.HyperEngineGameController$1, reason: invalid class name */
    class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && "com.android.phone.intent.action.HYPER_ENGINE_GAME_DEBUG".equals(intent.getAction())) {
                HyperEngineGameController.this.mHandler.post(new Runnable() { // from class: com.android.phone.HyperEngineGameController$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            HyperEngineGameController.this.handleDebugIntent(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDebugIntent(Intent intent) {
        if (intent.hasExtra("vdbg")) {
            VDBG = intent.getBooleanExtra("vdbg", false);
            log("VDBG changed to " + VDBG);
        }
        if (intent.hasExtra("simulate_game_battle")) {
            boolean booleanExtra = intent.getBooleanExtra("simulate_game_battle", false);
            log("simulate game battle by debug broadcast, isBattle=" + booleanExtra);
            processGameBattleStatusChanged(booleanExtra);
        }
        if (intent.getBooleanExtra("simulate_460_delay", false)) {
            log("simulate 460ms game delay by debug broadcast");
            processGameDelayChanged(460);
        } else if (intent.hasExtra("simulate_game_delay")) {
            int intExtra = intent.getIntExtra("simulate_game_delay", 0);
            log("simulate game delay by debug broadcast, delay=" + intExtra);
            processGameDelayChanged(intExtra);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.HyperEngineGameController$2, reason: invalid class name */
    class AnonymousClass2 implements NetworkMetricsController.GameDelayListener {
        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameUpdateStatusChanged(boolean z) {
        }

        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGameDelayChanged$0(int i) {
            HyperEngineGameController.this.processGameDelayChanged(i);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameDelayChanged(final int i) {
            HyperEngineGameController.this.mHandler.post(new Runnable() { // from class: com.android.phone.HyperEngineGameController$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onGameDelayChanged$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGameStatusChanged$1(int i, String str) {
            HyperEngineGameController.this.processGameStatusChanged(i, str);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(final int i, final String str) {
            HyperEngineGameController.this.mHandler.post(new Runnable() { // from class: com.android.phone.HyperEngineGameController$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onGameStatusChanged$1(i, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGameBattleStatusChanged$2(boolean z) {
            HyperEngineGameController.this.processGameBattleStatusChanged(z);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(final boolean z) {
            HyperEngineGameController.this.mHandler.post(new Runnable() { // from class: com.android.phone.HyperEngineGameController$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onGameBattleStatusChanged$2(z);
                }
            });
        }
    }

    public void onCallStateChanged(final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.HyperEngineGameController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCallStateChanged$0(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: processCallStateChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onCallStateChanged$0(boolean z) {
        logD("processCallStateChanged isInCall=" + z + ", game=" + this.mCurrentGamePackage + ", threshold=" + this.mCurrentGameDelayThreshold + ", battle=" + this.mIsGameBattle + ", inHighDelay=" + isCurrentGameDelayHigh());
        if (z && isCurrentGameDelayHigh()) {
            resetGameDelayMonitor();
        }
    }

    private boolean isCurrentGameDelayHigh() {
        return this.mHighDelayStartTime != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatusChanged(int i, String str) {
        this.mCurrentGamePackage = str;
        this.mCurrentGameDelayThreshold = getGameHighDelayThreshold(str);
        logD("processGameStatusChanged gameType=" + i + ", topGame=" + str + ", threshold=" + this.mCurrentGameDelayThreshold + ", isBattle=" + this.mIsGameBattle);
        this.mIsGameBattle = false;
        resetGameDelayMonitor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameBattleStatusChanged(boolean z) {
        if (z && !isCurrentGameDelayMonitorSupported()) {
            logD("ignore battle status for unsupported game, topGame=" + this.mCurrentGamePackage + ", threshold=" + this.mCurrentGameDelayThreshold);
            this.mIsGameBattle = false;
            resetGameDelayMonitor();
            return;
        }
        if (this.mIsGameBattle == z) {
            return;
        }
        this.mIsGameBattle = z;
        logD("processGameBattleStatusChanged isBattle=" + this.mIsGameBattle);
        if (this.mIsGameBattle) {
            return;
        }
        resetGameDelayMonitor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCurrentGameDelayMonitorSupported() {
        return this.mCurrentGameDelayThreshold != -1;
    }

    private int getGameHighDelayThreshold(String str) {
        if ("com.tencent.jkchess".equals(str)) {
            return 9999;
        }
        if ("com.tencent.tmgp.sgame".equals(str) || "com.tencent.tmgp.pubgmhd".equals(str)) {
            return 460;
        }
        if ("com.tencent.tmgp.dfm".equals(str)) {
            return ResponseObject.RELAY_FAILED_CODE;
        }
        return "com.tencent.lolm".equals(str) ? 999 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameDelayChanged(int i) {
        this.mCurrentGameDelay = i;
        logD("processGameDelayChanged delay=" + i + ", highDelayStart=" + this.mHighDelayStartTime + ", pulseCount=" + this.mHighDelayTimestamps.size() + ", topGame=" + this.mCurrentGamePackage + ", threshold=" + this.mCurrentGameDelayThreshold + ", isBattle=" + this.mIsGameBattle);
        if (!isCurrentGameDelayMonitorSupported()) {
            resetGameDelayMonitor();
            return;
        }
        if (!this.mIsGameBattle) {
            resetGameDelayMonitor();
            return;
        }
        if (!isStrongSignalHyperEngineGameOptActive()) {
            resetGameDelayMonitor();
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (i < this.mCurrentGameDelayThreshold) {
            recordHighGameDelayDuration(jElapsedRealtime);
            logD("delay below threshold, reset current high-delay segment");
            this.mHandler.removeCallbacks(this.mHighDelayOverTimeRunnable);
            this.mHandler.removeCallbacks(this.mHighDelayAccumulatedRunnable);
            return;
        }
        if (this.mHighDelayStartTime == 0) {
            if (recordHighGameDelayPulse(jElapsedRealtime)) {
                return;
            }
            this.mHighDelayStartTime = jElapsedRealtime;
            this.mHasGameDelayMonitorState = true;
            logD("start high-delay segment at " + this.mHighDelayStartTime);
            scheduleHighDelayOverTimeCheck();
        }
        long accumulatedHighDelayTime = getAccumulatedHighDelayTime(jElapsedRealtime);
        logD("high delay accumulatedTime=" + accumulatedHighDelayTime);
        if (accumulatedHighDelayTime >= 24000) {
            disableHyperEngineGameOpt(DisableReason.REASON_HIGH_DELAY_ACCUMULATED);
        } else {
            scheduleHighDelayAccumulatedCheck(24000 - accumulatedHighDelayTime);
        }
    }

    private void scheduleHighDelayOverTimeCheck() {
        this.mHandler.removeCallbacks(this.mHighDelayOverTimeRunnable);
        this.mHandler.postDelayed(this.mHighDelayOverTimeRunnable, 15000L);
        this.mHasGameDelayMonitorState = true;
    }

    private void scheduleHighDelayAccumulatedCheck(long j) {
        this.mHandler.removeCallbacks(this.mHighDelayAccumulatedRunnable);
        this.mHandler.postDelayed(this.mHighDelayAccumulatedRunnable, j);
        this.mHasGameDelayMonitorState = true;
    }

    private boolean recordHighGameDelayPulse(long j) {
        this.mHasGameDelayMonitorState = true;
        this.mHighDelayTimestamps.addLast(Long.valueOf(j));
        while (!this.mHighDelayTimestamps.isEmpty() && j - ((Long) this.mHighDelayTimestamps.peekFirst()).longValue() > 30000) {
            this.mHighDelayTimestamps.removeFirst();
        }
        logD("high delay pulse count=" + this.mHighDelayTimestamps.size());
        if (this.mHighDelayTimestamps.size() < 20) {
            return false;
        }
        disableHyperEngineGameOpt(DisableReason.REASON_HIGH_DELAY_PULSE, this.mHighDelayTimestamps.size());
        return true;
    }

    private void recordHighGameDelayDuration(long j) {
        long j2 = this.mHighDelayStartTime;
        if (j2 == 0) {
            return;
        }
        this.mHasGameDelayMonitorState = true;
        this.mHighDelayDurations.addLast(new long[]{j2, j});
        logD("record high delay duration start=" + this.mHighDelayStartTime + ", end=" + j);
        this.mHighDelayStartTime = 0L;
        trimHighDelayDurations(j);
        if (getAccumulatedHighDelayTime(j) >= 24000) {
            disableHyperEngineGameOpt(DisableReason.REASON_HIGH_DELAY_ACCUMULATED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getAccumulatedHighDelayTime(long j) {
        trimHighDelayDurations(j);
        long j2 = j - 30000;
        long jMax = 0;
        for (long[] jArr : this.mHighDelayDurations) {
            jMax += Math.max(0L, jArr[1] - Math.max(jArr[0], j2));
        }
        long j3 = this.mHighDelayStartTime;
        if (j3 != 0) {
            jMax += j - Math.max(j3, j2);
        }
        logD("getAccumulatedHighDelayTime now=" + j + ", accumulated=" + jMax + ", segments=" + this.mHighDelayDurations.size());
        return jMax;
    }

    private void trimHighDelayDurations(long j) {
        long j2 = j - 30000;
        while (!this.mHighDelayDurations.isEmpty() && ((long[]) this.mHighDelayDurations.peekFirst())[1] < j2) {
            this.mHighDelayDurations.removeFirst();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStrongSignalHyperEngineGameOptActive() {
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        boolean z = false;
        if (networkMetricsController != null && networkMetricsController.isHyperEngineGameOptActive()) {
            if (!this.mNetworkMetricsController.getDdsMobileDataOn()) {
                logD("isStrongSignalHyperEngineGameOptActive mobile data off");
                return false;
            }
            if (this.mNetworkMetricsController.isInCall()) {
                logD("isStrongSignalHyperEngineGameOptActive in call, stop delay monitor");
                return false;
            }
            int hyperEngineGameRsrp = this.mNetworkMetricsController.getHyperEngineGameRsrp();
            if (hyperEngineGameRsrp != Integer.MAX_VALUE && hyperEngineGameRsrp > -100) {
                z = true;
            }
            logD("isStrongSignalHyperEngineGameOptActive rsrp=" + hyperEngineGameRsrp + ", active=" + z);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableHyperEngineGameOpt(DisableReason disableReason) {
        disableHyperEngineGameOpt(disableReason, -1);
    }

    private void disableHyperEngineGameOpt(DisableReason disableReason, int i) {
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController != null) {
            networkMetricsController.disableHyperEngineGameOptByTemp(disableReason.name());
        }
        resetGameDelayMonitor();
        String description = disableReason.getDescription();
        if (i >= 0) {
            description = description + "=" + i;
        }
        log("disable hyper engine game optimization by " + description);
    }

    private void resetGameDelayMonitor() {
        if (this.mHasGameDelayMonitorState) {
            logD("resetGameDelayMonitor currentDelay=" + this.mCurrentGameDelay + ", highDelayStart=" + this.mHighDelayStartTime + ", pulseCount=" + this.mHighDelayTimestamps.size() + ", durationCount=" + this.mHighDelayDurations.size() + ", topGame=" + this.mCurrentGamePackage + ", threshold=" + this.mCurrentGameDelayThreshold + ", isBattle=" + this.mIsGameBattle);
            this.mCurrentGameDelay = 0;
            this.mHighDelayStartTime = 0L;
            this.mHighDelayTimestamps.clear();
            this.mHighDelayDurations.clear();
            this.mHandler.removeCallbacks(this.mHighDelayOverTimeRunnable);
            this.mHandler.removeCallbacks(this.mHighDelayAccumulatedRunnable);
            this.mHasGameDelayMonitorState = false;
        }
    }

    private static void log(String str) {
        Rlog.d("HyperEngineGameController", str);
    }

    private static void logD(String str) {
        if (VDBG) {
            log(str);
        }
    }
}
