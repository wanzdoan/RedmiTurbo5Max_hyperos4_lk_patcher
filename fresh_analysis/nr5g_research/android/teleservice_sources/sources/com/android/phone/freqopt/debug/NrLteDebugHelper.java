package com.android.phone.freqopt.debug;

import android.content.Intent;
import android.os.Handler;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.engine.PostExecutionMonitor;
import com.android.phone.freqopt.strategy.NrLteStrategy;
import com.android.phone.freqopt.util.LogUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class NrLteDebugHelper {
    private final FreqOptController mController;

    NrLteDebugHelper(FreqOptController freqOptController) {
        this.mController = freqOptController;
    }

    void handleSimulateLearnNrLte(Intent intent) {
        FreqOptDBManager dbManager;
        FreqOptLearningRecord freqOptLearningRecordQueryRecord;
        String stringExtra = intent.getStringExtra("source_cell");
        String stringExtra2 = intent.getStringExtra("target_cell");
        int intExtra = intent.getIntExtra("rsrp", -90);
        int intExtra2 = intent.getIntExtra("snr", 10);
        String stringExtra3 = intent.getStringExtra("ext_data");
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_learn_nrlte: missing source_cell or target_cell");
            return;
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_learn_nrlte: NrLteStrategy not initialized");
            return;
        }
        FreqOptLearningRecord freqOptLearningRecordLearnIratSwitch = nrLteStrategy.learnIratSwitch(stringExtra, stringExtra2, null, intExtra, intExtra2);
        if (freqOptLearningRecordLearnIratSwitch != null && stringExtra3 != null && (freqOptLearningRecordQueryRecord = (dbManager = this.mController.getDbManager()).queryRecord("nr_lte", stringExtra, stringExtra2)) != null) {
            freqOptLearningRecordQueryRecord.setExtData(stringExtra3);
            dbManager.deleteRecord("nr_lte", freqOptLearningRecordQueryRecord.getId());
            dbManager.insertOrUpdate(freqOptLearningRecordQueryRecord, nrLteStrategy.getTtlMs(), false);
        }
        StringBuilder sb = new StringBuilder("simulate_learn_nrlte: result=");
        sb.append(freqOptLearningRecordLearnIratSwitch != null ? "success" : "failed");
        sb.append(", source=");
        sb.append(stringExtra);
        sb.append(", target=");
        sb.append(stringExtra2);
        LogUtils.d("Freq_Opt_NrLte_Debug", sb.toString());
    }

    void handleSimulateFastFading(Intent intent) {
        String stringExtra = intent.getStringExtra("source_cell");
        int intExtra = intent.getIntExtra("rsrp", -90);
        int intExtra2 = intent.getIntExtra("snr", 10);
        if (stringExtra == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_fast_fading: missing source_cell");
            return;
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_fast_fading: NrLteStrategy not initialized");
            return;
        }
        nrLteStrategy.onFastFadingDetected(stringExtra, null, intExtra, intExtra2);
        nrLteStrategy.debugDispatch("execute", stringExtra);
        LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_fast_fading: source=" + stringExtra + ", rsrp=" + intExtra + ", snr=" + intExtra2);
    }

    void handleSimulateIratSwitch(Intent intent) {
        String str;
        String stringExtra = intent.getStringExtra("target_cell");
        if (stringExtra == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_irat_switch: missing target_cell");
            return;
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_irat_switch: NrLteStrategy not initialized");
            return;
        }
        NrLteStrategy.TempFadingRecord tempFadingRecordOnIratSwitchTo4G = nrLteStrategy.onIratSwitchTo4G(stringExtra, null);
        if (tempFadingRecordOnIratSwitchTo4G == null) {
            LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_irat_switch: no qualified temp record (need simulate_fast_fading first, wait >= 2s)");
            return;
        }
        FreqOptLearningRecord freqOptLearningRecordLearnIratSwitch = nrLteStrategy.learnIratSwitch(tempFadingRecordOnIratSwitchTo4G.mSourceCellId, stringExtra, null, tempFadingRecordOnIratSwitchTo4G.mRsrpCurrent, tempFadingRecordOnIratSwitchTo4G.mSnrCurrent);
        StringBuilder sb = new StringBuilder("simulate_irat_switch: result=");
        if (freqOptLearningRecordLearnIratSwitch != null) {
            str = "success, count=" + freqOptLearningRecordLearnIratSwitch.getCount();
        } else {
            str = "failed";
        }
        sb.append(str);
        sb.append(", source=");
        sb.append(tempFadingRecordOnIratSwitchTo4G.mSourceCellId);
        sb.append(", target=");
        sb.append(stringExtra);
        LogUtils.d("Freq_Opt_NrLte_Debug", sb.toString());
    }

    void handleE2eTestNrLte(Intent intent) {
        final String stringExtra = intent.getStringExtra("source_cell");
        String stringExtra2 = intent.getStringExtra("target_cell");
        int intExtra = intent.getIntExtra("rsrp", -90);
        int intExtra2 = intent.getIntExtra("snr", 10);
        String stringExtra3 = intent.getStringExtra("ext_data");
        String stringExtra4 = intent.getStringExtra("rat_result");
        String stringExtra5 = intent.getStringExtra("stutter_result");
        final String str = stringExtra4 == null ? "pass" : stringExtra4;
        final String str2 = stringExtra5 == null ? "pass" : stringExtra5;
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "e2e_test_nrlte: missing source_cell or target_cell");
            return;
        }
        final NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "e2e_test_nrlte: NrLteStrategy not initialized");
            return;
        }
        LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte START: source=" + stringExtra + ", target=" + stringExtra2);
        final FreqOptDBManager dbManager = this.mController.getDbManager();
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = dbManager.queryRecord("nr_lte", stringExtra, stringExtra2);
        if (freqOptLearningRecordQueryRecord != null) {
            dbManager.deleteRecord("nr_lte", freqOptLearningRecordQueryRecord.getId());
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord("nr_lte", stringExtra, stringExtra2);
        freqOptLearningRecord.setCount(1);
        freqOptLearningRecord.setExtData(stringExtra3);
        dbManager.insertOrUpdate(freqOptLearningRecord, nrLteStrategy.getTtlMs(), false);
        LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte: DB prepared");
        nrLteStrategy.onFastFadingDetected(stringExtra, null, intExtra, intExtra2);
        nrLteStrategy.debugDispatch("execute", stringExtra);
        LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte: fast fading simulated");
        final Handler workThreadHandler = this.mController.getWorkThreadHandler();
        workThreadHandler.postDelayed(new Runnable() { // from class: com.android.phone.freqopt.debug.NrLteDebugHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleE2eTestNrLte$0(nrLteStrategy, dbManager, workThreadHandler, str, str2, stringExtra);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: simulateRatAndStutter, reason: merged with bridge method [inline-methods] */
    public void lambda$handleE2eTestNrLte$0(final NrLteStrategy nrLteStrategy, final FreqOptDBManager freqOptDBManager, Handler handler, String str, final String str2, final String str3) {
        boolean zEquals = "pass".equals(str);
        if (zEquals) {
            nrLteStrategy.debugDispatch("start_stutter", str3);
        } else {
            freqOptDBManager.deleteBySourceCell("nr_lte", str3);
        }
        LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte: RAT check simulated, result=" + str);
        if (zEquals) {
            handler.postDelayed(new Runnable() { // from class: com.android.phone.freqopt.debug.NrLteDebugHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$simulateRatAndStutter$1(nrLteStrategy, freqOptDBManager, str2, str3);
                }
            }, 1000L);
        } else {
            LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte DONE: RAT timeout, no stutter check");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: simulateStutterResult, reason: merged with bridge method [inline-methods] */
    public void lambda$simulateRatAndStutter$1(NrLteStrategy nrLteStrategy, FreqOptDBManager freqOptDBManager, String str, String str2) {
        nrLteStrategy.debugDispatch("stop_stutter", null);
        if ("pass".equals(str)) {
            nrLteStrategy.debugDispatch("refresh", str2);
        } else {
            freqOptDBManager.deleteBySourceCell("nr_lte", str2);
        }
        LogUtils.d("Freq_Opt_NrLte_Debug", "e2e_test_nrlte DONE: stutter result=" + str);
    }

    void handleSimulateNrLteRatResult(Intent intent) {
        String stringExtra = intent.getStringExtra(a.L);
        String stringExtra2 = intent.getStringExtra("source_cell");
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_nrlte_rat_result: missing result or source_cell");
            return;
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_nrlte_rat_result: NrLteStrategy not initialized");
            return;
        }
        if ("pass".equals(stringExtra)) {
            nrLteStrategy.debugDispatch("start_stutter", stringExtra2);
        } else {
            this.mController.getDbManager().deleteBySourceCell("nr_lte", stringExtra2);
        }
        LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_nrlte_rat_result: result=" + stringExtra + ", source=" + stringExtra2);
    }

    void handleSimulateNrLteStutterResult(Intent intent) {
        String stringExtra = intent.getStringExtra(a.L);
        String stringExtra2 = intent.getStringExtra("source_cell");
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_nrlte_stutter_result: missing result or source_cell");
            return;
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_nrlte_stutter_result: NrLteStrategy not initialized");
            return;
        }
        nrLteStrategy.debugDispatch("stop_stutter", null);
        if ("pass".equals(stringExtra)) {
            nrLteStrategy.debugDispatch("refresh", stringExtra2);
        } else {
            this.mController.getDbManager().deleteBySourceCell("nr_lte", stringExtra2);
        }
        LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_nrlte_stutter_result: result=" + stringExtra + ", source=" + stringExtra2);
    }

    void handleSimulateLteStutter(Intent intent) {
        int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        int intExtra2 = intent.getIntExtra("score", 60);
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_lte_stutter: NrLteStrategy not initialized");
            return;
        }
        nrLteStrategy.debugSimulateLteStutter(intExtra, intExtra2);
        LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_lte_stutter: slotId=" + intExtra + ", score=" + intExtra2);
    }

    void handleNrLteConfigOverride(Intent intent) {
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "nrlte_config_override: NrLteStrategy not initialized");
            return;
        }
        HashMap map = new HashMap();
        putIfNonNegative(map, "freq_opt_nr_lte_temp_timeout", intent.getLongExtra("temp_timeout", -1L));
        putIfNonNegative(map, "freq_opt_nr_lte_temp_min_duration", intent.getLongExtra("temp_min_duration", -1L));
        putIfNonNegative(map, "freq_opt_nr_lte_learn_threshold", intent.getIntExtra("learn_threshold", -1));
        putIfNonNegative(map, "freq_opt_nr_lte_ttl", intent.getLongExtra("ttl", -1L));
        putIfNonNegative(map, "freq_opt_nr_lte_rat_check_delay", intent.getLongExtra("rat_check_delay", -1L));
        putIfNonNegative(map, "freq_opt_nr_lte_rat_check_retries", intent.getIntExtra("rat_check_retries", -1));
        putIfNonNegative(map, "freq_opt_nr_lte_stutter_window", intent.getLongExtra("stutter_window", -1L));
        putIfNonNegative(map, "freq_opt_nr_lte_qoe_threshold", intent.getIntExtra("qoe_threshold", -1));
        nrLteStrategy.updateCloudConfig(map);
        LogUtils.d("Freq_Opt_NrLte_Debug", "nrlte_config_override done");
    }

    private static void putIfNonNegative(Map map, String str, long j) {
        if (j >= 0) {
            map.put(str, String.valueOf(j));
        }
    }

    void handleSimulatePostExecution(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra(com.ot.pubsub.b.a.b);
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_post_execution: missing strategy or event");
            return;
        }
        PostExecutionMonitor postExecutionMonitor = this.mController.getPostExecutionMonitor();
        if (postExecutionMonitor == null) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_post_execution: PostExecutionMonitor not initialized");
            return;
        }
        if (!postExecutionMonitor.isMonitoring(stringExtra)) {
            LogUtils.w("Freq_Opt_NrLte_Debug", "simulate_post_execution: no active monitoring for ".concat(stringExtra));
            return;
        }
        postExecutionMonitor.debugSimulatePostEvent(stringExtra, stringExtra2);
        LogUtils.d("Freq_Opt_NrLte_Debug", "simulate_post_execution: strategy=" + stringExtra + ", event=" + stringExtra2);
    }
}
