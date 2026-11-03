package com.android.phone.freqopt.engine;

import android.telephony.CellIdentity;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.ThresholdValidator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LearningEngine {
    private final FreqOptDBManager mDbManager;
    private final ThresholdValidator mValidator;

    public LearningEngine(FreqOptDBManager freqOptDBManager, ThresholdValidator thresholdValidator) {
        this.mDbManager = freqOptDBManager;
        this.mValidator = thresholdValidator;
    }

    public FreqOptLearningRecord learn(IFreqOptStrategy iFreqOptStrategy, String str, String str2, CellIdentity cellIdentity, int i, int i2, long j, long j2, int i3) {
        FreqOptLearningRecord freqOptLearningRecordQueryRecord;
        int countForState;
        String strategyId = iFreqOptStrategy.getStrategyId();
        boolean z = i3 == 0;
        LogUtils.d("Freq_Opt_LearningEngine", "learn strategyId=" + strategyId + ", source=" + str + ", target=" + str2 + ", connected=" + z);
        if (iFreqOptStrategy.requiresConnectedStateForLearning() && i3 != 0) {
            LogUtils.d("Freq_Opt_LearningEngine", "learn skipped: strategy requires connected state");
            return null;
        }
        if (z) {
            if (!this.mValidator.validateSignal(i, i2)) {
                LogUtils.d("Freq_Opt_LearningEngine", "learn signal validation failed");
                return null;
            }
            if (!this.mValidator.validateRate(j)) {
                LogUtils.d("Freq_Opt_LearningEngine", "learn rate validation failed");
                return null;
            }
        }
        if (iFreqOptStrategy.requiresConnectedStateForLearning() && (freqOptLearningRecordQueryRecord = this.mDbManager.queryRecord(strategyId, str, str2)) != null && (countForState = freqOptLearningRecordQueryRecord.getCountForState(i3)) >= iFreqOptStrategy.getLearnCountThreshold(i3)) {
            LogUtils.d("Freq_Opt_LearningEngine", "learn skipped: count already meets threshold, count=" + countForState);
            return freqOptLearningRecordQueryRecord;
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(strategyId, str, str2);
        buildAndApplyExtFields(freqOptLearningRecord, iFreqOptStrategy, cellIdentity, j2, z);
        if (this.mDbManager.insertOrUpdate(freqOptLearningRecord, iFreqOptStrategy.getTtlMs(), z) < 0) {
            LogUtils.w("Freq_Opt_LearningEngine", "learn DB insert/update failed");
            return null;
        }
        return finalizeLearnResult(iFreqOptStrategy, strategyId, str, str2, z, i3);
    }

    private void buildAndApplyExtFields(FreqOptLearningRecord freqOptLearningRecord, IFreqOptStrategy iFreqOptStrategy, CellIdentity cellIdentity, long j, boolean z) {
        Map mapBuildExtFields;
        if (j > 0) {
            mapBuildExtFields = iFreqOptStrategy.buildExtFields(cellIdentity, j);
        } else {
            mapBuildExtFields = iFreqOptStrategy.buildExtFields(cellIdentity);
        }
        if (mapBuildExtFields != null && !z) {
            mergeTMinWithHistory(freqOptLearningRecord.getStrategyId(), freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId(), mapBuildExtFields);
        }
        if (mapBuildExtFields == null || mapBuildExtFields.isEmpty()) {
            return;
        }
        freqOptLearningRecord.setExtFields(mapBuildExtFields);
    }

    private FreqOptLearningRecord finalizeLearnResult(IFreqOptStrategy iFreqOptStrategy, String str, String str2, String str3, boolean z, int i) {
        int connectedCount;
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = this.mDbManager.queryRecord(str, str2, str3);
        if (freqOptLearningRecordQueryRecord != null) {
            connectedCount = z ? freqOptLearningRecordQueryRecord.getConnectedCount() : freqOptLearningRecordQueryRecord.getCount();
        } else {
            connectedCount = 1;
        }
        reportLearnEvent(str, str2, str3, connectedCount);
        if (freqOptLearningRecordQueryRecord != null && connectedCount >= iFreqOptStrategy.getLearnCountThreshold(i)) {
            this.mDbManager.clearOtherTargetsForState(str, str2, str3, z);
        }
        StringBuilder sb = new StringBuilder("learn success, ");
        sb.append(z ? "connectedCount=" : "count=");
        sb.append(connectedCount);
        LogUtils.i("Freq_Opt_LearningEngine", sb.toString());
        return freqOptLearningRecordQueryRecord;
    }

    private void mergeTMinWithHistory(String str, String str2, String str3, Map map) {
        Map extFields;
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = this.mDbManager.queryRecord(str, str2, str3);
        if (freqOptLearningRecordQueryRecord == null || (extFields = freqOptLearningRecordQueryRecord.getExtFields()) == null) {
            return;
        }
        Object obj = extFields.get("t_min");
        if (obj instanceof Number) {
            long jLongValue = ((Number) obj).longValue();
            Object obj2 = map.get("t_min");
            if (obj2 instanceof Number) {
                map.put("t_min", Long.valueOf(Math.min(jLongValue, ((Number) obj2).longValue())));
            } else {
                map.put("t_min", Long.valueOf(jLongValue));
            }
        }
    }

    private void reportLearnEvent(String str, String str2, String str3, int i) {
        FreqOptReporter.reportEvent("freq_opt_learn", str, str2, str3, i, null);
    }

    public FreqOptLearningRecord learnFromDebug(IFreqOptStrategy iFreqOptStrategy, String str, String str2, int i, int i2, long j, long j2, int i3, String str3) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        boolean z = i3 == 0;
        LogUtils.d("Freq_Opt_LearningEngine", "learnFromDebug strategyId=" + strategyId + ", source=" + str + ", target=" + str2 + ", connected=" + z);
        if (z) {
            if (!this.mValidator.validateSignal(i, i2)) {
                LogUtils.d("Freq_Opt_LearningEngine", "learnFromDebug signal validation failed");
                return null;
            }
            if (!this.mValidator.validateRate(j)) {
                LogUtils.d("Freq_Opt_LearningEngine", "learnFromDebug rate validation failed");
                return null;
            }
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(strategyId, str, str2);
        freqOptLearningRecord.setExtData(str3);
        if (this.mDbManager.insertOrUpdate(freqOptLearningRecord, iFreqOptStrategy.getTtlMs(), z) < 0) {
            LogUtils.d("Freq_Opt_LearningEngine", "learnFromDebug DB insert/update failed");
            return null;
        }
        return finalizeLearnResult(iFreqOptStrategy, strategyId, str, str2, z, i3);
    }
}
