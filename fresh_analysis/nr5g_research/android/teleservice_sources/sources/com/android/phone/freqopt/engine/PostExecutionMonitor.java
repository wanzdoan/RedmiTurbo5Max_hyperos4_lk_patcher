package com.android.phone.freqopt.engine;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.SystemClock;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.LogUtils;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class PostExecutionMonitor {
    private OnPostFailureListener mFailureListener;
    private final Handler mHandler;
    private OnLeaveTargetListener mLeaveListener;
    private SignalProvider mSignalProvider;
    private int mPostRsrpThreshold = -105;
    private int mPostSinrThreshold = 0;
    private long mPostRateThresholdKbps = 30;
    private final ConcurrentHashMap mSessions = new ConcurrentHashMap();

    public interface OnLeaveTargetListener {
        void onLeaveTargetCell(String str, String str2, int i);
    }

    public interface OnPostFailureListener {
        void onPostExecutionFailed(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i);
    }

    public interface SignalProvider {
        int[] getCurrentNrSignal(int i);
    }

    class PostSession {
        long checkStartTime;
        boolean leftTargetDuringRateCheck;
        Runnable rateCheckRunnable;
        final FreqOptLearningRecord record;
        long rsrpSum;
        long rxBytesBaseline;
        int signalSampleCount;
        Runnable signalSampleRunnable;
        long sinrSum;
        final int slotId;
        int state = 0;
        final String strategyId;
        final String targetCellId;

        PostSession(String str, String str2, FreqOptLearningRecord freqOptLearningRecord, int i) {
            this.strategyId = str;
            this.targetCellId = str2;
            this.record = freqOptLearningRecord;
            this.slotId = i;
        }
    }

    public PostExecutionMonitor(Handler handler) {
        this.mHandler = handler;
    }

    public void setOnPostFailureListener(OnPostFailureListener onPostFailureListener) {
        this.mFailureListener = onPostFailureListener;
    }

    public void setOnLeaveTargetListener(OnLeaveTargetListener onLeaveTargetListener) {
        this.mLeaveListener = onLeaveTargetListener;
    }

    public void startMonitoring(final String str, String str2, FreqOptLearningRecord freqOptLearningRecord, int i) {
        cancelMonitoring(str);
        PostSession postSession = new PostSession(str, str2, freqOptLearningRecord, i);
        postSession.rxBytesBaseline = TrafficStats.getMobileRxBytes();
        postSession.checkStartTime = SystemClock.elapsedRealtime();
        postSession.signalSampleCount = 0;
        postSession.rsrpSum = 0L;
        postSession.sinrSum = 0L;
        if (this.mHandler != null) {
            scheduleSignalSample(postSession);
            Runnable runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.PostExecutionMonitor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startMonitoring$0(str);
                }
            };
            postSession.rateCheckRunnable = runnable;
            this.mHandler.postDelayed(runnable, MirrorDesktopHelper.TIMEOUT_MILLIS);
        } else {
            LogUtils.w("Freq_Opt_PostExecMonitor", "startMonitoring: handler is null, rate check not scheduled for " + str);
        }
        this.mSessions.put(str, postSession);
        LogUtils.i("Freq_Opt_PostExecMonitor", "startMonitoring strategyId=" + str + ", target=" + str2);
    }

    public void onCellChanged(String str, String str2) {
        PostSession postSession = (PostSession) this.mSessions.get(str);
        if (postSession == null) {
            return;
        }
        boolean zEquals = postSession.targetCellId.equals(str2);
        int i = postSession.state;
        if (zEquals) {
            if (i == 0 && postSession.leftTargetDuringRateCheck) {
                postSession.leftTargetDuringRateCheck = false;
                LogUtils.i("Freq_Opt_PostExecMonitor", "onCellChanged: returned to target during rate check, strategyId=" + str);
                return;
            }
            return;
        }
        if (i == 0) {
            postSession.leftTargetDuringRateCheck = true;
            LogUtils.i("Freq_Opt_PostExecMonitor", "onCellChanged: left target during rate check, strategyId=" + str + ", current=" + str2);
            return;
        }
        if (i == 1) {
            LogUtils.i("Freq_Opt_PostExecMonitor", "onCellChanged: left target cell, strategyId=" + str + ", target=" + postSession.targetCellId + ", current=" + str2);
            clearSession(str);
            OnLeaveTargetListener onLeaveTargetListener = this.mLeaveListener;
            if (onLeaveTargetListener != null) {
                onLeaveTargetListener.onLeaveTargetCell(str, "left_target_cell", postSession.slotId);
                return;
            }
            LogUtils.w("Freq_Opt_PostExecMonitor", "onCellChanged: no leave listener for " + str);
        }
    }

    public void cancelMonitoring(String str) {
        Runnable runnable;
        PostSession postSession = (PostSession) this.mSessions.remove(str);
        if (postSession != null) {
            Handler handler = this.mHandler;
            if (handler != null && (runnable = postSession.rateCheckRunnable) != null) {
                handler.removeCallbacks(runnable);
            }
            stopSignalSampling(postSession);
            LogUtils.i("Freq_Opt_PostExecMonitor", "cancelMonitoring strategyId=" + str);
        }
    }

    public boolean isMonitoring(String str) {
        return this.mSessions.containsKey(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onRateCheckTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$startMonitoring$0(String str) {
        PostSession postSession = (PostSession) this.mSessions.get(str);
        if (postSession == null) {
            LogUtils.d("Freq_Opt_PostExecMonitor", "onRateCheckTimeout: session already cancelled for " + str);
            return;
        }
        if (postSession.state != 0) {
            LogUtils.w("Freq_Opt_PostExecMonitor", "onRateCheckTimeout: unexpected state=" + postSession.state + " for " + str);
            return;
        }
        if (postSession.leftTargetDuringRateCheck) {
            LogUtils.i("Freq_Opt_PostExecMonitor", "onRateCheckTimeout: left target during rate check, treat as leave");
            int i = postSession.slotId;
            clearSession(str);
            OnLeaveTargetListener onLeaveTargetListener = this.mLeaveListener;
            if (onLeaveTargetListener != null) {
                onLeaveTargetListener.onLeaveTargetCell(str, "left_target_during_rate_check", i);
                return;
            }
            return;
        }
        evaluateRate(str, postSession);
    }

    private void scheduleSignalSample(final PostSession postSession) {
        if (this.mSignalProvider == null || this.mHandler == null) {
            return;
        }
        Runnable runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.PostExecutionMonitor.1
            @Override // java.lang.Runnable
            public void run() {
                PostSession postSession2 = (PostSession) PostExecutionMonitor.this.mSessions.get(postSession.strategyId);
                if (postSession2 == null || postSession2.state != 0) {
                    return;
                }
                int[] currentNrSignal = PostExecutionMonitor.this.mSignalProvider.getCurrentNrSignal(postSession2.slotId);
                if (currentNrSignal != null) {
                    postSession2.rsrpSum += (long) currentNrSignal[0];
                    postSession2.sinrSum += (long) currentNrSignal[1];
                    postSession2.signalSampleCount++;
                }
                PostExecutionMonitor.this.mHandler.postDelayed(this, 1000L);
            }
        };
        postSession.signalSampleRunnable = runnable;
        this.mHandler.post(runnable);
    }

    private void stopSignalSampling(PostSession postSession) {
        Handler handler;
        Runnable runnable = postSession.signalSampleRunnable;
        if (runnable == null || (handler = this.mHandler) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
        postSession.signalSampleRunnable = null;
    }

    private void evaluateRate(String str, PostSession postSession) {
        stopSignalSampling(postSession);
        if (evaluateSignal(str, postSession)) {
            evaluateTrafficRate(str, postSession);
        }
    }

    private boolean evaluateSignal(String str, PostSession postSession) {
        if (postSession == null) {
            LogUtils.w("Freq_Opt_PostExecMonitor", "evaluateSignal: session is null, skip");
            return true;
        }
        int i = postSession.signalSampleCount;
        if (i <= 0) {
            LogUtils.w("Freq_Opt_PostExecMonitor", "evaluateSignal: no signal samples, skip signal check");
            return true;
        }
        int i2 = (int) (postSession.rsrpSum / ((long) i));
        int i3 = (int) (postSession.sinrSum / ((long) i));
        LogUtils.d("Freq_Opt_PostExecMonitor", "evaluateSignal: avgRsrp=" + i2 + ", avgSinr=" + i3 + ", samples=" + postSession.signalSampleCount);
        if (i2 > this.mPostRsrpThreshold && i3 > this.mPostSinrThreshold) {
            return true;
        }
        LogUtils.i("Freq_Opt_PostExecMonitor", "evaluateSignal: signal below threshold, cancel + remove");
        FreqOptLearningRecord freqOptLearningRecord = postSession.record;
        int i4 = postSession.slotId;
        clearSession(str);
        OnPostFailureListener onPostFailureListener = this.mFailureListener;
        if (onPostFailureListener == null) {
            return false;
        }
        onPostFailureListener.onPostExecutionFailed(str, freqOptLearningRecord, "post_signal_below_threshold: rsrp=" + i2 + " sinr=" + i3, i4);
        return false;
    }

    private void evaluateTrafficRate(String str, PostSession postSession) {
        if (postSession == null) {
            LogUtils.w("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: session is null, skip");
            return;
        }
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long jElapsedRealtime = SystemClock.elapsedRealtime() - postSession.checkStartTime;
        if (mobileRxBytes >= 0) {
            long j = postSession.rxBytesBaseline;
            if (j >= 0) {
                long j2 = mobileRxBytes - j;
                if (j2 < 10240) {
                    LogUtils.i("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: insufficient traffic, deltaBytes=" + j2);
                    enterMonitoringState(postSession);
                    return;
                }
                long j3 = jElapsedRealtime > 0 ? (j2 * 8) / jElapsedRealtime : 0L;
                LogUtils.d("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: rateKbps=" + j3 + ", postThreshold=" + this.mPostRateThresholdKbps);
                if (j3 > this.mPostRateThresholdKbps) {
                    LogUtils.i("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: rate OK, entering monitoring state");
                    enterMonitoringState(postSession);
                    return;
                }
                LogUtils.i("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: rate below threshold, cancel + remove");
                FreqOptLearningRecord freqOptLearningRecord = postSession.record;
                int i = postSession.slotId;
                clearSession(str);
                OnPostFailureListener onPostFailureListener = this.mFailureListener;
                if (onPostFailureListener != null) {
                    onPostFailureListener.onPostExecutionFailed(str, freqOptLearningRecord, "post_rate_below_threshold: " + j3 + "kbps", i);
                    return;
                }
                return;
            }
        }
        LogUtils.w("Freq_Opt_PostExecMonitor", "evaluateTrafficRate: TrafficStats unavailable, current=" + mobileRxBytes + ", baseline=" + postSession.rxBytesBaseline);
        enterMonitoringState(postSession);
    }

    private void enterMonitoringState(PostSession postSession) {
        postSession.state = 1;
        postSession.rateCheckRunnable = null;
        stopSignalSampling(postSession);
    }

    private void clearSession(String str) {
        Handler handler;
        PostSession postSession = (PostSession) this.mSessions.remove(str);
        if (postSession == null || (handler = this.mHandler) == null) {
            return;
        }
        Runnable runnable = postSession.rateCheckRunnable;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        stopSignalSampling(postSession);
    }

    public void debugSimulatePostEvent(String str, String str2) {
        PostSession postSession = (PostSession) this.mSessions.get(str);
        if (postSession == null) {
            LogUtils.d("Freq_Opt_PostExecMonitor", "[DEBUG] no active session for " + str);
        }
        str2.hashCode();
        switch (str2) {
            case "rate_fail":
                LogUtils.d("Freq_Opt_PostExecMonitor", "[DEBUG] simulating rate fail for " + str);
                cancelPendingRateCheck(postSession);
                FreqOptLearningRecord freqOptLearningRecord = postSession.record;
                int i = postSession.slotId;
                clearSession(str);
                OnPostFailureListener onPostFailureListener = this.mFailureListener;
                if (onPostFailureListener != null) {
                    onPostFailureListener.onPostExecutionFailed(str, freqOptLearningRecord, "debug_rate_fail", i);
                    break;
                }
                break;
            case "rate_pass":
                LogUtils.d("Freq_Opt_PostExecMonitor", "[DEBUG] simulating rate pass for " + str);
                cancelPendingRateCheck(postSession);
                stopSignalSampling(postSession);
                postSession.state = 1;
                postSession.rateCheckRunnable = null;
                LogUtils.d("Freq_Opt_PostExecMonitor", "[DEBUG] entered monitoring state for " + str);
                break;
            case "leave_target":
                LogUtils.d("Freq_Opt_PostExecMonitor", "[DEBUG] simulating leave target for " + str);
                int i2 = postSession.slotId;
                clearSession(str);
                OnLeaveTargetListener onLeaveTargetListener = this.mLeaveListener;
                if (onLeaveTargetListener != null) {
                    onLeaveTargetListener.onLeaveTargetCell(str, "debug_leave_target", i2);
                    break;
                }
                break;
            default:
                LogUtils.w("Freq_Opt_PostExecMonitor", "[DEBUG] unknown event: ".concat(str2));
                break;
        }
    }

    private void cancelPendingRateCheck(PostSession postSession) {
        Handler handler;
        Runnable runnable = postSession.rateCheckRunnable;
        if (runnable == null || (handler = this.mHandler) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }

    public void setSignalProvider(SignalProvider signalProvider) {
        this.mSignalProvider = signalProvider;
    }

    public void setPostSignalThresholds(int i, int i2) {
        this.mPostRsrpThreshold = i;
        this.mPostSinrThreshold = i2;
    }

    public void setPostRateThresholdKbps(long j) {
        this.mPostRateThresholdKbps = j;
    }

    public long getPostRateThresholdKbps() {
        return this.mPostRateThresholdKbps;
    }

    public int getPostRsrpThreshold() {
        return this.mPostRsrpThreshold;
    }

    public int getPostSinrThreshold() {
        return this.mPostSinrThreshold;
    }
}
