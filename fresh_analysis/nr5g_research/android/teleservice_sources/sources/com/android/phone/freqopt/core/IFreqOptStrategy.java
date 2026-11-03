package com.android.phone.freqopt.core;

import android.telephony.CellIdentity;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IFreqOptStrategy {
    Map buildExtFields(CellIdentity cellIdentity);

    void cancelOptimization(String str, int i);

    void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i);

    default long getHandoverTimeoutMs() {
        return -1L;
    }

    int getLearnCountThreshold(int i);

    String getStrategyId();

    long getTtlMs();

    boolean isSourceCell(CellIdentity cellIdentity);

    boolean isTargetCell(CellIdentity cellIdentity);

    default boolean needsConnectedRateCheck() {
        return false;
    }

    default boolean needsPostExecutionMonitoring() {
        return false;
    }

    default boolean requiresConnectedStateForLearning() {
        return false;
    }

    boolean shouldRemoveOnFail(FreqOptLearningRecord freqOptLearningRecord);

    default Map buildExtFields(CellIdentity cellIdentity, long j) {
        return buildExtFields(cellIdentity);
    }

    default void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i, int i2) {
        executeOptimization(freqOptLearningRecord, i);
    }
}
