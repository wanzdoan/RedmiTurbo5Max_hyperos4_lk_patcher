package com.android.phone.freqopt.engine;

import android.os.Handler;
import android.os.SystemClock;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.LogUtils;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class HandoverMonitor {
    private OnFailureListener mFailureListener;
    private final Handler mHandler;
    private OnSuccessListener mSuccessListener;
    private long mTimeoutMs = 10000;
    private final ConcurrentHashMap mSessions = new ConcurrentHashMap();

    public interface OnFailureListener {
        void onHandoverFailed(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i);
    }

    public interface OnSuccessListener {
        void onHandoverSuccess(String str, String str2, FreqOptLearningRecord freqOptLearningRecord, int i);
    }

    class MonitoringSession {
        final int connectionState;
        final FreqOptLearningRecord record;
        final int slotId;
        final String sourceCellId;
        final long startTimeMs = SystemClock.elapsedRealtime();
        final String strategyId;
        final String targetCellId;
        Runnable timeoutRunnable;

        MonitoringSession(String str, String str2, String str3, FreqOptLearningRecord freqOptLearningRecord, int i, int i2) {
            this.strategyId = str;
            this.sourceCellId = str2;
            this.targetCellId = str3;
            this.record = freqOptLearningRecord;
            this.connectionState = i;
            this.slotId = i2;
        }
    }

    public HandoverMonitor(Handler handler) {
        this.mHandler = handler;
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        this.mFailureListener = onFailureListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.mSuccessListener = onSuccessListener;
    }

    public void setTimeoutMs(long j) {
        this.mTimeoutMs = j;
        LogUtils.d("Freq_Opt_HandoverMonitor", "setTimeoutMs " + j);
    }

    public long getTimeoutMs() {
        return this.mTimeoutMs;
    }

    public void startMonitoring(final String str, String str2, String str3, FreqOptLearningRecord freqOptLearningRecord, int i, int i2, long j) {
        cancelMonitoring(str);
        MonitoringSession monitoringSession = new MonitoringSession(str, str2, str3, freqOptLearningRecord, i, i2);
        if (j <= 0) {
            j = this.mTimeoutMs;
        }
        if (this.mHandler != null) {
            Runnable runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.HandoverMonitor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startMonitoring$0(str);
                }
            };
            monitoringSession.timeoutRunnable = runnable;
            this.mHandler.postDelayed(runnable, j);
        } else {
            LogUtils.w("Freq_Opt_HandoverMonitor", "startMonitoring: handler is null, no timeout for " + str);
        }
        this.mSessions.put(str, monitoringSession);
        LogUtils.d("Freq_Opt_HandoverMonitor", "startMonitoring strategyId=" + str + ", source=" + str2 + ", target=" + str3 + ", timeout=" + j + "ms");
    }

    public void onCellChanged(String str, String str2) {
        MonitoringSession monitoringSession = (MonitoringSession) this.mSessions.get(str);
        if (monitoringSession == null) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime() - monitoringSession.startTimeMs;
        if (monitoringSession.targetCellId.equals(str2)) {
            clearSession(str);
            LogUtils.i("Freq_Opt_HandoverMonitor", "onCellChanged SUCCESS strategyId=" + str + ", duration=" + jElapsedRealtime + "ms");
            notifyResult(monitoringSession, 0, jElapsedRealtime);
            return;
        }
        if (monitoringSession.sourceCellId.equals(str2)) {
            return;
        }
        clearSession(str);
        LogUtils.i("Freq_Opt_HandoverMonitor", "onCellChanged FAILED strategyId=" + str + ", actual=" + str2 + ", duration=" + jElapsedRealtime + "ms");
        notifyResult(monitoringSession, 1, jElapsedRealtime);
    }

    public void cancelMonitoring(String str) {
        Runnable runnable;
        MonitoringSession monitoringSession = (MonitoringSession) this.mSessions.remove(str);
        if (monitoringSession != null) {
            Handler handler = this.mHandler;
            if (handler != null && (runnable = monitoringSession.timeoutRunnable) != null) {
                handler.removeCallbacks(runnable);
            }
            LogUtils.d("Freq_Opt_HandoverMonitor", "cancelMonitoring strategyId=" + str);
        }
    }

    public void cancelAll() {
        Iterator it = this.mSessions.keySet().iterator();
        while (it.hasNext()) {
            cancelMonitoring((String) it.next());
        }
    }

    public boolean isMonitoring(String str) {
        return this.mSessions.containsKey(str);
    }

    public String getMonitoringTargetCell(String str) {
        MonitoringSession monitoringSession = (MonitoringSession) this.mSessions.get(str);
        if (monitoringSession != null) {
            return monitoringSession.targetCellId;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$startMonitoring$0(String str) {
        MonitoringSession monitoringSession = (MonitoringSession) this.mSessions.remove(str);
        if (monitoringSession == null) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime() - monitoringSession.startTimeMs;
        LogUtils.i("Freq_Opt_HandoverMonitor", "onTimeout strategyId=" + str + ", duration=" + jElapsedRealtime + "ms");
        notifyResult(monitoringSession, 2, jElapsedRealtime);
    }

    private void clearSession(String str) {
        Handler handler;
        Runnable runnable;
        MonitoringSession monitoringSession = (MonitoringSession) this.mSessions.remove(str);
        if (monitoringSession == null || (handler = this.mHandler) == null || (runnable = monitoringSession.timeoutRunnable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }

    private void notifyResult(MonitoringSession monitoringSession, int i, long j) {
        String str;
        if (i == 0) {
            str = "freq_opt_success";
        } else if (i == 2) {
            str = "freq_opt_handover_timeout";
        } else {
            str = "freq_opt_fail";
        }
        String str2 = str;
        FreqOptReporter.reportStateEvent(str2, monitoringSession.strategyId, monitoringSession.sourceCellId, monitoringSession.targetCellId, monitoringSession.connectionState, "duration=" + j + ",result=" + i);
        reportFddTddDetailedSuccess(monitoringSession, i, j);
        notifyListeners(monitoringSession, i);
    }

    private void reportFddTddDetailedSuccess(MonitoringSession monitoringSession, int i, long j) {
        String str;
        if (i == 0 && "fdd_tdd".equals(monitoringSession.strategyId)) {
            int i2 = monitoringSession.connectionState;
            if (i2 == 0) {
                str = "fdd_tdd_connected_coverage_success";
            } else {
                str = "fdd_tdd_idle_coverage_success";
            }
            String str2 = str;
            FreqOptReporter.reportStateEvent(str2, monitoringSession.strategyId, monitoringSession.sourceCellId, monitoringSession.targetCellId, i2, "duration=" + j);
        }
    }

    private void notifyListeners(MonitoringSession monitoringSession, int i) {
        if (i != 0) {
            OnFailureListener onFailureListener = this.mFailureListener;
            if (onFailureListener != null) {
                onFailureListener.onHandoverFailed(monitoringSession.strategyId, monitoringSession.record, i == 2 ? "handover_timeout" : "handover_failed", monitoringSession.slotId);
            } else {
                LogUtils.w("Freq_Opt_HandoverMonitor", "notifyListeners: no failure listener for " + monitoringSession.strategyId);
            }
        }
        if (i == 0) {
            OnSuccessListener onSuccessListener = this.mSuccessListener;
            if (onSuccessListener != null) {
                onSuccessListener.onHandoverSuccess(monitoringSession.strategyId, monitoringSession.targetCellId, monitoringSession.record, monitoringSession.slotId);
                return;
            }
            LogUtils.w("Freq_Opt_HandoverMonitor", "notifyListeners: no success listener for " + monitoringSession.strategyId);
        }
    }

    public void simulateTimeout(String str) {
        lambda$startMonitoring$0(str);
    }
}
