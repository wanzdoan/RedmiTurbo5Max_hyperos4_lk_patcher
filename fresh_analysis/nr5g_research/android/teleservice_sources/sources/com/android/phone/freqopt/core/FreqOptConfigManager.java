package com.android.phone.freqopt.core;

import com.android.phone.freqopt.engine.ExecutionEngine;
import com.android.phone.freqopt.engine.HandoverMonitor;
import com.android.phone.freqopt.engine.PostExecutionMonitor;
import com.android.phone.freqopt.strategy.N79HotspotStrategy;
import com.android.phone.freqopt.strategy.NrLteStrategy;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.ThresholdValidator;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
class FreqOptConfigManager {
    private boolean mEnabled = true;
    private final ExecutionEngine mExecutionEngine;
    private final HandoverMonitor mHandoverMonitor;
    private NrLteStrategy mNrLteStrategy;
    private final PostExecutionMonitor mPostExecutionMonitor;
    private final Map mStrategies;
    private final Map mStrategyEnabled;
    private final ThresholdValidator mValidator;
    private final FreqOptWorkHandler mWorkHandler;

    FreqOptConfigManager(Map map, ThresholdValidator thresholdValidator, ExecutionEngine executionEngine, HandoverMonitor handoverMonitor, PostExecutionMonitor postExecutionMonitor, Map map2, FreqOptWorkHandler freqOptWorkHandler) {
        this.mStrategyEnabled = map;
        this.mValidator = thresholdValidator;
        this.mExecutionEngine = executionEngine;
        this.mHandoverMonitor = handoverMonitor;
        this.mPostExecutionMonitor = postExecutionMonitor;
        this.mStrategies = map2;
        this.mWorkHandler = freqOptWorkHandler;
    }

    void setNrLteStrategy(NrLteStrategy nrLteStrategy) {
        this.mNrLteStrategy = nrLteStrategy;
    }

    void updateCloudConfig(Map map) {
        if (map == null) {
            LogUtils.w("Freq_Opt_ConfigManager", "updateCloudConfig: config is null");
            return;
        }
        if (map.containsKey("freq_opt_framework_enable")) {
            this.mEnabled = "true".equals(map.get("freq_opt_framework_enable"));
        }
        updateFrameworkThresholds(map);
        updateStrategyEnableFlags(map);
        NrLteStrategy nrLteStrategy = this.mNrLteStrategy;
        if (nrLteStrategy != null) {
            nrLteStrategy.updateCloudConfig(map);
        }
        LogUtils.i("Freq_Opt_ConfigManager", "updateCloudConfig applied");
    }

    private void updateFrameworkThresholds(Map map) {
        ThresholdValidator thresholdValidator = this.mValidator;
        thresholdValidator.setRsrpThreshold(FreqOptUtils.parseInt(map, "freq_opt_rsrp_threshold", thresholdValidator.getRsrpThreshold()));
        ThresholdValidator thresholdValidator2 = this.mValidator;
        thresholdValidator2.setSinrThreshold(FreqOptUtils.parseInt(map, "freq_opt_sinr_threshold", thresholdValidator2.getSinrThreshold()));
        ThresholdValidator thresholdValidator3 = this.mValidator;
        thresholdValidator3.setRateThresholdKbps(FreqOptUtils.parseLong(map, "freq_opt_rate_threshold", thresholdValidator3.getRateThresholdKbps()));
        ExecutionEngine executionEngine = this.mExecutionEngine;
        executionEngine.setMinIntervalMs(FreqOptUtils.parseLong(map, "freq_opt_min_interval", executionEngine.getMinIntervalMs()));
        HandoverMonitor handoverMonitor = this.mHandoverMonitor;
        handoverMonitor.setTimeoutMs(FreqOptUtils.parseLong(map, "freq_opt_handover_timeout", handoverMonitor.getTimeoutMs()));
        PostExecutionMonitor postExecutionMonitor = this.mPostExecutionMonitor;
        postExecutionMonitor.setPostRateThresholdKbps(FreqOptUtils.parseLong(map, "freq_opt_post_rate_threshold", postExecutionMonitor.getPostRateThresholdKbps()));
        PostExecutionMonitor postExecutionMonitor2 = this.mPostExecutionMonitor;
        postExecutionMonitor2.setPostSignalThresholds(FreqOptUtils.parseInt(map, "freq_opt_post_rsrp_threshold", postExecutionMonitor2.getPostRsrpThreshold()), FreqOptUtils.parseInt(map, "freq_opt_post_sinr_threshold", this.mPostExecutionMonitor.getPostSinrThreshold()));
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mStrategies.get("n79_hotspot");
        if (iFreqOptStrategy instanceof N79HotspotStrategy) {
            N79HotspotStrategy n79HotspotStrategy = (N79HotspotStrategy) iFreqOptStrategy;
            n79HotspotStrategy.setHandoverTimeoutMs(FreqOptUtils.parseLong(map, "freq_opt_n79_handover_timeout", n79HotspotStrategy.getHandoverTimeoutMs()));
        }
    }

    private void updateStrategyEnableFlags(Map map) {
        for (String str : this.mStrategies.keySet()) {
            String str2 = "freq_opt_" + str + "_enable";
            if (map.containsKey(str2)) {
                setStrategyEnabled(str, "true".equals(map.get(str2)));
            }
        }
        if (map.containsKey("freq_opt_nr_lte_enable")) {
            setStrategyEnabled("nr_lte", "true".equals(map.get("freq_opt_nr_lte_enable")));
        }
    }

    void setStrategyEnabled(String str, boolean z) {
        final NrLteStrategy nrLteStrategy;
        this.mStrategyEnabled.put(str, Boolean.valueOf(z));
        LogUtils.i("Freq_Opt_ConfigManager", "setStrategyEnabled " + str + "=" + z);
        if (z || !"nr_lte".equals(str) || (nrLteStrategy = this.mNrLteStrategy) == null) {
            return;
        }
        FreqOptWorkHandler freqOptWorkHandler = this.mWorkHandler;
        Objects.requireNonNull(nrLteStrategy);
        freqOptWorkHandler.post(new Runnable() { // from class: com.android.phone.freqopt.core.FreqOptConfigManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                nrLteStrategy.onDisabled();
            }
        });
    }

    boolean isStrategyActive(String str) {
        if (!this.mEnabled) {
            LogUtils.d("Freq_Opt_ConfigManager", "framework disabled");
            return false;
        }
        if ("nr_lte".equals(str)) {
            if (this.mNrLteStrategy == null) {
                LogUtils.d("Freq_Opt_ConfigManager", "NrLteStrategy not initialized");
                return false;
            }
        } else if (!this.mStrategies.containsKey(str)) {
            LogUtils.d("Freq_Opt_ConfigManager", "strategy not registered: " + str);
            return false;
        }
        Boolean bool = (Boolean) this.mStrategyEnabled.get(str);
        if (bool == null || bool.booleanValue()) {
            return true;
        }
        LogUtils.d("Freq_Opt_ConfigManager", "strategy disabled: " + str);
        return false;
    }
}
