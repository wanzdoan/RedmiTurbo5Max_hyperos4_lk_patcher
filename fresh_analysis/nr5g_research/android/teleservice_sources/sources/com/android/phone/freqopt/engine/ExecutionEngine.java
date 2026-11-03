package com.android.phone.freqopt.engine;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.LogUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ExecutionEngine {
    private final FreqOptDBManager mDbManager;
    private boolean mDebugSkipConnectedRateCheck;
    private final Handler mHandler;
    private HandoverMonitor mHandoverMonitor;
    private long mMinIntervalMs = 1800000;
    private final PendingTask mPendingConnected;
    private final PendingTask mPendingIdle;

    class PendingTask {
        long checkTimeBaseline;
        int connectionState;
        FreqOptLearningRecord record;
        Runnable runnable;
        long rxBytesBaseline;
        int slotId;
        String sourceCellId;
        IFreqOptStrategy strategy;

        private PendingTask() {
        }

        boolean isActive() {
            return this.runnable != null;
        }

        void clear() {
            this.strategy = null;
            this.record = null;
            this.sourceCellId = null;
            this.slotId = 0;
            this.connectionState = 0;
            this.runnable = null;
            this.rxBytesBaseline = 0L;
            this.checkTimeBaseline = 0L;
        }
    }

    public ExecutionEngine(FreqOptDBManager freqOptDBManager, Handler handler) {
        this.mPendingIdle = new PendingTask();
        this.mPendingConnected = new PendingTask();
        this.mDbManager = freqOptDBManager;
        this.mHandler = handler;
    }

    public void setMinIntervalMs(long j) {
        this.mMinIntervalMs = j;
        LogUtils.d("Freq_Opt_ExecutionEngine", "setMinIntervalMs " + j);
    }

    public long getMinIntervalMs() {
        return this.mMinIntervalMs;
    }

    public void setHandoverMonitor(HandoverMonitor handoverMonitor) {
        this.mHandoverMonitor = handoverMonitor;
    }

    public boolean execute(IFreqOptStrategy iFreqOptStrategy, String str, int i, int i2) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        LogUtils.d("Freq_Opt_ExecutionEngine", "execute strategyId=" + strategyId + ", source=" + str + ", state=" + i);
        List listQueryBySourceCellForState = this.mDbManager.queryBySourceCellForState(strategyId, str, iFreqOptStrategy.getTtlMs(), i == 0);
        if (listQueryBySourceCellForState.isEmpty()) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "execute no matching records");
            return false;
        }
        int learnCountThreshold = iFreqOptStrategy.getLearnCountThreshold(i);
        FreqOptLearningRecord freqOptLearningRecordFindBestRecord = findBestRecord(listQueryBySourceCellForState, i, learnCountThreshold);
        if (freqOptLearningRecordFindBestRecord == null) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "execute no record meets threshold=" + learnCountThreshold);
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - freqOptLearningRecordFindBestRecord.getActionTime();
        if (jCurrentTimeMillis < this.mMinIntervalMs) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "execute rate-limited by action_time, elapsed=" + jCurrentTimeMillis + "ms, minInterval=" + this.mMinIntervalMs + "ms");
            return false;
        }
        if (i == 0 && iFreqOptStrategy.needsConnectedRateCheck() && !this.mDebugSkipConnectedRateCheck) {
            if (this.mPendingConnected.isActive()) {
                LogUtils.d("Freq_Opt_ExecutionEngine", "execute: connectedRateCheck already pending, skip");
                return false;
            }
            LogUtils.d("Freq_Opt_ExecutionEngine", "execute: routing to connectedRateCheck for " + strategyId);
            return scheduleConnectedRateCheck(iFreqOptStrategy, freqOptLearningRecordFindBestRecord, str, i2);
        }
        if (i == 0) {
            this.mDbManager.updateActionTime(strategyId, freqOptLearningRecordFindBestRecord.getId());
            return doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecordFindBestRecord, str, 0, i2);
        }
        if (this.mPendingIdle.isActive()) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "execute: idle execution already pending, skip");
            return false;
        }
        return tryScheduleIdleExecution(iFreqOptStrategy, freqOptLearningRecordFindBestRecord, str, i2);
    }

    private FreqOptLearningRecord findBestRecord(List list, int i, int i2) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            FreqOptLearningRecord freqOptLearningRecord = (FreqOptLearningRecord) it.next();
            if (freqOptLearningRecord.getCountForState(i) >= i2 && freqOptLearningRecord.getState() == 0) {
                return freqOptLearningRecord;
            }
        }
        return null;
    }

    private boolean tryScheduleIdleExecution(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i) {
        Map extFields = freqOptLearningRecord.getExtFields();
        if (extFields != null && extFields.containsKey("t_min")) {
            Object obj = extFields.get("t_min");
            if (obj instanceof Number) {
                long jLongValue = ((Number) obj).longValue();
                if (jLongValue > 0) {
                    return scheduleDelayedIdleExecution(iFreqOptStrategy, freqOptLearningRecord, str, 1, i, jLongValue);
                }
            }
        }
        LogUtils.d("Freq_Opt_ExecutionEngine", "tryScheduleIdleExecution: no valid t_min, executing immediately");
        this.mDbManager.updateActionTime(iFreqOptStrategy.getStrategyId(), freqOptLearningRecord.getId());
        return doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecord, str, 1, i);
    }

    private boolean doExecuteAndMonitor(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i, int i2) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        try {
            iFreqOptStrategy.executeOptimization(freqOptLearningRecord, i2, i);
            reportExecuteEvent(strategyId, str, freqOptLearningRecord.getTargetCellId(), i);
            if (this.mHandoverMonitor == null) {
                LogUtils.w("Freq_Opt_ExecutionEngine", "doExecuteAndMonitor: handoverMonitor is null, skipping monitoring");
            } else {
                this.mHandoverMonitor.startMonitoring(strategyId, str, freqOptLearningRecord.getTargetCellId(), freqOptLearningRecord, i, i2, iFreqOptStrategy.getHandoverTimeoutMs());
            }
            LogUtils.i("Freq_Opt_ExecutionEngine", "doExecuteAndMonitor success, record=" + freqOptLearningRecord);
            return true;
        } catch (Exception e) {
            LogUtils.w("Freq_Opt_ExecutionEngine", "doExecuteAndMonitor exception", e);
            reportFailEvent(strategyId, str, "exception: " + e.getMessage());
            return false;
        }
    }

    private boolean scheduleDelayedIdleExecution(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i, int i2, long j) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        if (this.mHandler == null) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "scheduleDelayedIdleExecution: handler is null, execute immediately");
            this.mDbManager.updateActionTime(strategyId, freqOptLearningRecord.getId());
            return doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecord, str, i, i2);
        }
        cancelPendingIdleExecution();
        PendingTask pendingTask = this.mPendingIdle;
        pendingTask.strategy = iFreqOptStrategy;
        pendingTask.record = freqOptLearningRecord;
        pendingTask.sourceCellId = str;
        pendingTask.slotId = i2;
        pendingTask.connectionState = i;
        pendingTask.runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.ExecutionEngine$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.executePendingIdle();
            }
        };
        this.mHandler.postDelayed(this.mPendingIdle.runnable, j);
        LogUtils.d("Freq_Opt_ExecutionEngine", "scheduleDelayedIdleExecution: scheduled in " + j + "ms, source=" + str + ", target=" + freqOptLearningRecord.getTargetCellId());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executePendingIdle() {
        if (!this.mPendingIdle.isActive()) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingIdle: no pending execution");
            return;
        }
        PendingTask pendingTask = this.mPendingIdle;
        IFreqOptStrategy iFreqOptStrategy = pendingTask.strategy;
        FreqOptLearningRecord freqOptLearningRecord = pendingTask.record;
        String str = pendingTask.sourceCellId;
        int i = pendingTask.slotId;
        int i2 = pendingTask.connectionState;
        pendingTask.clear();
        if (!isStillOnSourceCell(i, str)) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingIdle: UE left source cell, skip execution");
            reportFailEvent(iFreqOptStrategy.getStrategyId(), str, "idle_source_cell_changed");
        } else {
            this.mDbManager.updateActionTime(iFreqOptStrategy.getStrategyId(), freqOptLearningRecord.getId());
            doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecord, str, i2, i);
        }
    }

    private boolean isStillOnSourceCell(int i, String str) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "isStillOnSourceCell: phone is null");
            return false;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "isStillOnSourceCell: currentCell is null");
            return false;
        }
        String globalCellId = currentCellIdentity.getGlobalCellId();
        if (str.equals(globalCellId)) {
            return true;
        }
        LogUtils.d("Freq_Opt_ExecutionEngine", "isStillOnSourceCell: cell changed, expected=" + str + ", actual=" + globalCellId);
        return false;
    }

    public void cancelPendingIdleExecution() {
        Handler handler;
        if (this.mPendingIdle.isActive() && (handler = this.mHandler) != null) {
            handler.removeCallbacks(this.mPendingIdle.runnable);
            LogUtils.d("Freq_Opt_ExecutionEngine", "cancelPendingIdleExecution: cancelled, source=" + this.mPendingIdle.sourceCellId);
        }
        this.mPendingIdle.clear();
    }

    public boolean hasPendingIdleExecution() {
        return this.mPendingIdle.isActive();
    }

    public String getPendingIdleSourceCellId() {
        return this.mPendingIdle.sourceCellId;
    }

    private boolean scheduleConnectedRateCheck(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        if (this.mHandler == null) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "scheduleConnectedRateCheck: handler is null, execute immediately");
            this.mDbManager.updateActionTime(strategyId, freqOptLearningRecord.getId());
            return doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecord, str, 0, i);
        }
        cancelPendingConnectedRateCheck();
        PendingTask pendingTask = this.mPendingConnected;
        pendingTask.strategy = iFreqOptStrategy;
        pendingTask.record = freqOptLearningRecord;
        pendingTask.sourceCellId = str;
        pendingTask.slotId = i;
        pendingTask.connectionState = 0;
        pendingTask.runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.ExecutionEngine$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.startRateMeasurement();
            }
        };
        this.mHandler.postDelayed(this.mPendingConnected.runnable, 30000L);
        LogUtils.d("Freq_Opt_ExecutionEngine", "scheduleConnectedRateCheck: stabilize wait 30000ms, then measure 3000ms, source=" + str + ", target=" + freqOptLearningRecord.getTargetCellId());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRateMeasurement() {
        if (!this.mPendingConnected.isActive()) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "startRateMeasurement: no pending execution");
            return;
        }
        PendingTask pendingTask = this.mPendingConnected;
        if (!isStillOnSourceCell(pendingTask.slotId, pendingTask.sourceCellId)) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "startRateMeasurement: UE left source cell, abort");
            reportFailEvent(this.mPendingConnected.strategy.getStrategyId(), this.mPendingConnected.sourceCellId, "connected_source_cell_changed_before_measure");
            this.mPendingConnected.clear();
            return;
        }
        this.mPendingConnected.rxBytesBaseline = TrafficStats.getMobileRxBytes();
        this.mPendingConnected.checkTimeBaseline = SystemClock.elapsedRealtime();
        this.mPendingConnected.runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.ExecutionEngine$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.executePendingConnected();
            }
        };
        this.mHandler.postDelayed(this.mPendingConnected.runnable, 3000L);
        LogUtils.d("Freq_Opt_ExecutionEngine", "startRateMeasurement: baseline captured, measuring for 3000ms");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executePendingConnected() {
        if (!this.mPendingConnected.isActive()) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingConnected: no pending execution");
            return;
        }
        PendingTask pendingTask = this.mPendingConnected;
        IFreqOptStrategy iFreqOptStrategy = pendingTask.strategy;
        FreqOptLearningRecord freqOptLearningRecord = pendingTask.record;
        String strategyId = iFreqOptStrategy.getStrategyId();
        PendingTask pendingTask2 = this.mPendingConnected;
        String str = pendingTask2.sourceCellId;
        int i = pendingTask2.slotId;
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        PendingTask pendingTask3 = this.mPendingConnected;
        long j = jElapsedRealtime - pendingTask3.checkTimeBaseline;
        long j2 = j > 0 ? ((mobileRxBytes - pendingTask3.rxBytesBaseline) * 8) / j : 0L;
        pendingTask3.clear();
        if (!isStillOnSourceCell(i, str)) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingConnected: UE left source cell, skip execution");
            reportFailEvent(strategyId, str, "connected_source_cell_changed");
            return;
        }
        LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingConnected: rateKbps=" + j2 + ", min=100, max=500");
        if (j2 < 100) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingConnected: rate too low, skip execution");
            reportFailEvent(strategyId, str, "connected_rate_low: " + j2 + "kbps");
            return;
        }
        if (j2 >= 500) {
            LogUtils.d("Freq_Opt_ExecutionEngine", "executePendingConnected: rate too high, FDD sufficient, skip");
            reportFailEvent(strategyId, str, "connected_rate_high: " + j2 + "kbps");
            return;
        }
        this.mDbManager.updateActionTime(strategyId, freqOptLearningRecord.getId());
        doExecuteAndMonitor(iFreqOptStrategy, freqOptLearningRecord, str, 0, i);
    }

    public void cancelPendingConnectedRateCheck() {
        Handler handler;
        if (this.mPendingConnected.isActive() && (handler = this.mHandler) != null) {
            handler.removeCallbacks(this.mPendingConnected.runnable);
            LogUtils.d("Freq_Opt_ExecutionEngine", "cancelPendingConnectedRateCheck: cancelled, source=" + this.mPendingConnected.sourceCellId);
        }
        this.mPendingConnected.clear();
    }

    public boolean hasPendingConnectedRateCheck() {
        return this.mPendingConnected.isActive();
    }

    public String getPendingConnectedSourceCellId() {
        return this.mPendingConnected.sourceCellId;
    }

    public void onExecutionFailed(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        LogUtils.i("Freq_Opt_ExecutionEngine", "onExecutionFailed strategyId=" + strategyId + ", reason=" + str);
        HandoverMonitor handoverMonitor = this.mHandoverMonitor;
        if (handoverMonitor != null) {
            handoverMonitor.cancelMonitoring(strategyId);
        }
        iFreqOptStrategy.cancelOptimization(str, i);
        if (freqOptLearningRecord != null && iFreqOptStrategy.shouldRemoveOnFail(freqOptLearningRecord)) {
            this.mDbManager.deleteRecord(strategyId, freqOptLearningRecord.getId());
            LogUtils.d("Freq_Opt_ExecutionEngine", "onExecutionFailed removed record id=" + freqOptLearningRecord.getId());
        }
        reportFailEvent(strategyId, freqOptLearningRecord != null ? freqOptLearningRecord.getSourceCellId() : "", str);
    }

    public void handleHandoverFailure(IFreqOptStrategy iFreqOptStrategy, FreqOptLearningRecord freqOptLearningRecord, String str, int i) {
        LogUtils.i("Freq_Opt_ExecutionEngine", "handleHandoverFailure strategyId=" + iFreqOptStrategy.getStrategyId() + ", reason=" + str);
        iFreqOptStrategy.cancelOptimization(str, i);
    }

    public void cancel(IFreqOptStrategy iFreqOptStrategy, String str, int i) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        LogUtils.i("Freq_Opt_ExecutionEngine", "cancel strategyId=" + strategyId + ", reason=" + str);
        HandoverMonitor handoverMonitor = this.mHandoverMonitor;
        if (handoverMonitor != null) {
            handoverMonitor.cancelMonitoring(strategyId);
        }
        iFreqOptStrategy.cancelOptimization(str, i);
        FreqOptReporter.reportEvent("freq_opt_cancel", strategyId, null, null, 0, str);
    }

    private void reportExecuteEvent(String str, String str2, String str3, int i) {
        FreqOptReporter.reportStateEvent("freq_opt_execute", str, str2, str3, i, null);
    }

    private void reportFailEvent(String str, String str2, String str3) {
        FreqOptReporter.reportEvent("freq_opt_fail", str, str2, null, 0, str3);
    }

    public void setDebugSkipConnectedRateCheck(boolean z) {
        this.mDebugSkipConnectedRateCheck = z;
    }
}
