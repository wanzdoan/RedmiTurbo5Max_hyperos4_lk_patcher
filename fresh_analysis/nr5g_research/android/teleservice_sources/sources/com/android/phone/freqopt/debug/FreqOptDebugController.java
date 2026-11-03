package com.android.phone.freqopt.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.ArrayMap;
import com.android.phone.PhoneGlobals;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.engine.HandoverMonitor;
import com.android.phone.freqopt.strategy.FddTddStrategy;
import com.android.phone.freqopt.strategy.N79HotspotStrategy;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.ThresholdValidator;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class FreqOptDebugController {
    private final FreqOptController mController;
    private final DbDebugHelper mDbHelper;
    private final NrLteDebugHelper mNrLteHelper;
    private final Map mSimulateCommands = new ArrayMap();
    private final Map mNrLteCommands = new ArrayMap();
    private int mSavedRsrp = Integer.MIN_VALUE;
    private int mSavedSinr = Integer.MIN_VALUE;
    private long mSavedRate = Long.MIN_VALUE;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !"com.android.phone.freqopt.intent.DEBUG".equals(intent.getAction())) {
                return;
            }
            FreqOptDebugController.this.mController.dispatchDebugCommand(intent);
        }
    };

    public FreqOptDebugController(FreqOptController freqOptController) {
        this.mController = freqOptController;
        this.mNrLteHelper = new NrLteDebugHelper(freqOptController);
        this.mDbHelper = new DbDebugHelper(freqOptController);
        initCommandMaps();
    }

    private void initCommandMaps() {
        this.mSimulateCommands.put("simulate_learn", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleSimulateLearn((Intent) obj);
            }
        });
        this.mSimulateCommands.put("simulate_execute", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleSimulateExecute((Intent) obj);
            }
        });
        this.mSimulateCommands.put("simulate_handover", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleSimulateHandover((Intent) obj);
            }
        });
        this.mSimulateCommands.put("e2e_test", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleE2eTest((Intent) obj);
            }
        });
        this.mSimulateCommands.put("threshold_override", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleThresholdOverride((Intent) obj);
            }
        });
        this.mSimulateCommands.put("threshold_restore", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$initCommandMaps$0((Intent) obj);
            }
        });
        this.mSimulateCommands.put("ratelimiter_reset", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleRateLimiterReset((Intent) obj);
            }
        });
        this.mSimulateCommands.put("ratelimiter_reset_all", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleRateLimiterResetAll((Intent) obj);
            }
        });
        this.mSimulateCommands.put("log_debug", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleLogDebug((Intent) obj);
            }
        });
        this.mSimulateCommands.put("query_rrc_state", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleQueryRrcState((Intent) obj);
            }
        });
        this.mSimulateCommands.put("test_build_ext_fields", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.handleTestBuildExtFields((Intent) obj);
            }
        });
        Map map = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper);
        map.put("simulate_learn_nrlte", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper.handleSimulateLearnNrLte((Intent) obj);
            }
        });
        Map map2 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper2 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper2);
        map2.put("simulate_fast_fading", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper2.handleSimulateFastFading((Intent) obj);
            }
        });
        Map map3 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper3 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper3);
        map3.put("simulate_irat_switch", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper3.handleSimulateIratSwitch((Intent) obj);
            }
        });
        Map map4 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper4 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper4);
        map4.put("e2e_test_nrlte", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper4.handleE2eTestNrLte((Intent) obj);
            }
        });
        Map map5 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper5 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper5);
        map5.put("simulate_nrlte_rat_result", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper5.handleSimulateNrLteRatResult((Intent) obj);
            }
        });
        Map map6 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper6 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper6);
        map6.put("simulate_nrlte_stutter_result", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper6.handleSimulateNrLteStutterResult((Intent) obj);
            }
        });
        Map map7 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper7 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper7);
        map7.put("nrlte_config_override", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper7.handleNrLteConfigOverride((Intent) obj);
            }
        });
        Map map8 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper8 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper8);
        map8.put("simulate_lte_stutter", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper8.handleSimulateLteStutter((Intent) obj);
            }
        });
        Map map9 = this.mNrLteCommands;
        final NrLteDebugHelper nrLteDebugHelper9 = this.mNrLteHelper;
        Objects.requireNonNull(nrLteDebugHelper9);
        map9.put("simulate_post_execution", new Consumer() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nrLteDebugHelper9.handleSimulatePostExecution((Intent) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initCommandMaps$0(Intent intent) {
        handleThresholdRestore();
    }

    public void init() {
        if (!isDebugEnabled()) {
            LogUtils.d("Freq_Opt_Debug", "init: debug not enabled, skip receiver registration");
            return;
        }
        PhoneGlobals.getInstance().registerReceiver(this.mReceiver, new IntentFilter("com.android.phone.freqopt.intent.DEBUG"), 4);
        LogUtils.i("Freq_Opt_Debug", "init done");
    }

    public void handleDebugCommand(Intent intent) {
        String stringExtra = intent.getStringExtra("command");
        if (stringExtra == null) {
            LogUtils.w("Freq_Opt_Debug", "missing 'command' extra");
            return;
        }
        LogUtils.d("Freq_Opt_Debug", "handleDebugCommand: ".concat(stringExtra));
        if (!isDebugEnabled()) {
            LogUtils.w("Freq_Opt_Debug", "debug not enabled, set: adb shell setprop persist.sys.freqopt.debug 1");
        } else {
            if (dispatchNrLteCommand(stringExtra, intent) || this.mDbHelper.dispatch(stringExtra, intent) || dispatchSimulateCommand(stringExtra, intent)) {
                return;
            }
            LogUtils.w("Freq_Opt_Debug", "unknown command: ".concat(stringExtra));
        }
    }

    private boolean dispatchNrLteCommand(String str, Intent intent) {
        Consumer consumer = (Consumer) this.mNrLteCommands.get(str);
        if (consumer == null) {
            return false;
        }
        consumer.accept(intent);
        return true;
    }

    private boolean dispatchSimulateCommand(String str, Intent intent) {
        Consumer consumer = (Consumer) this.mSimulateCommands.get(str);
        if (consumer == null) {
            return false;
        }
        consumer.accept(intent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRateLimiterResetAll(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        FreqOptDBManager dbManager = this.mController.getDbManager();
        if (stringExtra == null) {
            stringExtra = "fdd_tdd";
        }
        dbManager.resetActionTime(stringExtra, null);
        LogUtils.d("Freq_Opt_Debug", "ratelimiter_reset_all done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogDebug(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("enable", true);
        LogUtils.setDebugEnabled(booleanExtra);
        LogUtils.i("Freq_Opt_Debug", "log_debug: " + booleanExtra);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimulateLearn(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra("source_cell");
        String stringExtra3 = intent.getStringExtra("target_cell");
        int intExtra = intent.getIntExtra("rsrp", -90);
        int intExtra2 = intent.getIntExtra("sinr", 10);
        long longExtra = intent.getLongExtra("rate_kbps", MirrorDesktopHelper.TIMEOUT_MILLIS);
        long longExtra2 = intent.getLongExtra("dwell_time_ms", 8000L);
        String stringExtra4 = intent.getStringExtra("connection_state");
        String stringExtra5 = intent.getStringExtra("ext_data");
        if (stringExtra == null || stringExtra2 == null || stringExtra3 == null) {
            LogUtils.w("Freq_Opt_Debug", "simulate_learn: missing required params (strategy/source_cell/target_cell)");
            return;
        }
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mController.getStrategies().get(stringExtra);
        if (iFreqOptStrategy == null) {
            LogUtils.w("Freq_Opt_Debug", "simulate_learn: unknown strategy: ".concat(stringExtra));
            return;
        }
        FreqOptLearningRecord freqOptLearningRecordLearnFromDebug = this.mController.getLearningEngine().learnFromDebug(iFreqOptStrategy, stringExtra2, stringExtra3, intExtra, intExtra2, longExtra, longExtra2, !"connected".equals(stringExtra4) ? 1 : 0, stringExtra5);
        if (freqOptLearningRecordLearnFromDebug == null) {
            LogUtils.w("Freq_Opt_Debug", "simulate_learn failed (check signal/rate thresholds or DB debounce)");
            return;
        }
        LogUtils.d("Freq_Opt_Debug", "simulate_learn success: " + freqOptLearningRecordLearnFromDebug);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleThresholdOverride(Intent intent) {
        ThresholdValidator validator = this.mController.getValidator();
        if (this.mSavedRsrp == Integer.MIN_VALUE) {
            this.mSavedRsrp = validator.getRsrpThreshold();
            this.mSavedSinr = validator.getSinrThreshold();
            this.mSavedRate = validator.getRateThresholdKbps();
        }
        int intExtra = intent.getIntExtra("rsrp", -200);
        int intExtra2 = intent.getIntExtra("sinr", -100);
        long longExtra = intent.getLongExtra("rate_kbps", 0L);
        validator.setRsrpThreshold(intExtra);
        validator.setSinrThreshold(intExtra2);
        validator.setRateThresholdKbps(longExtra);
        LogUtils.d("Freq_Opt_Debug", "threshold_override: rsrp=" + intExtra + ", sinr=" + intExtra2 + ", rate=" + longExtra);
    }

    private void handleThresholdRestore() {
        if (this.mSavedRsrp == Integer.MIN_VALUE) {
            LogUtils.w("Freq_Opt_Debug", "threshold_restore: no saved values (override first)");
            return;
        }
        ThresholdValidator validator = this.mController.getValidator();
        validator.setRsrpThreshold(this.mSavedRsrp);
        validator.setSinrThreshold(this.mSavedSinr);
        validator.setRateThresholdKbps(this.mSavedRate);
        LogUtils.d("Freq_Opt_Debug", "threshold_restore: rsrp=" + this.mSavedRsrp + ", sinr=" + this.mSavedSinr + ", rate=" + this.mSavedRate);
        this.mSavedRsrp = Integer.MIN_VALUE;
        this.mSavedSinr = Integer.MIN_VALUE;
        this.mSavedRate = Long.MIN_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRateLimiterReset(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra("source_cell");
        if (stringExtra == null) {
            stringExtra = "fdd_tdd";
        }
        this.mController.getDbManager().resetActionTime(stringExtra, stringExtra2);
        LogUtils.d("Freq_Opt_Debug", "ratelimiter_reset: strategy=" + stringExtra + ", source=" + stringExtra2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleQueryRrcState(Intent intent) {
        String str;
        int intExtra = intent.getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, 0);
        int nr5gRrcState = FreqOptUtils.getNr5gRrcState(intExtra);
        if (nr5gRrcState == 0) {
            str = "INACTIVE(0)";
        } else if (nr5gRrcState == 2) {
            str = "CONNECTED(2)";
        } else if (nr5gRrcState == 5) {
            str = "IRAT(5)";
        } else if (nr5gRrcState == 6) {
            str = "INVALID(6)";
        } else {
            str = "UNKNOWN(" + nr5gRrcState + ")";
        }
        LogUtils.i("Freq_Opt_Debug", "query_rrc_state: phoneId=" + intExtra + ", state=" + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTestBuildExtFields(Intent intent) {
        String stringExtra = intent.getStringExtra("bands");
        if (stringExtra == null || stringExtra.isEmpty()) {
            LogUtils.w("Freq_Opt_Debug", "test_build_ext_fields: missing 'bands' param (comma-separated, e.g. \"78,79\")");
            return;
        }
        String[] strArrSplit = stringExtra.split(",");
        int[] iArr = new int[strArrSplit.length];
        for (int i = 0; i < strArrSplit.length; i++) {
            try {
                iArr[i] = Integer.parseInt(strArrSplit[i].trim());
            } catch (NumberFormatException unused) {
                LogUtils.w("Freq_Opt_Debug", "test_build_ext_fields: invalid bands format: ".concat(stringExtra));
                return;
            }
        }
        int iFindTargetBand = N79HotspotStrategy.findTargetBand(iArr);
        if (iFindTargetBand == 79) {
            LogUtils.d("Freq_Opt_Debug", "test_build_ext_fields: bands=" + Arrays.toString(iArr) + ", selectedBand=" + iFindTargetBand);
            return;
        }
        LogUtils.w("Freq_Opt_Debug", "test_build_ext_fields: bands=" + Arrays.toString(iArr) + ", selectedBand=" + iFindTargetBand + " (N79 not found, fallback to bands[0])");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimulateExecute(Intent intent) {
        final String stringExtra = intent.getStringExtra("strategy");
        final String stringExtra2 = intent.getStringExtra("source_cell");
        final String stringExtra3 = intent.getStringExtra("connection_state");
        final int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        final boolean booleanExtra = intent.getBooleanExtra("skip_rate_check", false);
        final boolean booleanExtra2 = intent.getBooleanExtra("skip_source_check", false);
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_Debug", "simulate_execute: missing strategy or source_cell");
            return;
        }
        final int i = !"connected".equals(stringExtra3) ? 1 : 0;
        this.mController.getDbManager().resetActionTime(stringExtra, null);
        withDebugFlags((IFreqOptStrategy) this.mController.getStrategies().get(stringExtra), booleanExtra, booleanExtra2, new Runnable() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleSimulateExecute$1(stringExtra, stringExtra2, i, intExtra, stringExtra3, booleanExtra, booleanExtra2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSimulateExecute$1(String str, String str2, int i, int i2, String str3, boolean z, boolean z2) {
        LogUtils.d("Freq_Opt_Debug", "simulate_execute: strategy=" + str + ", source=" + str2 + ", state=" + str3 + ", skipRateCheck=" + z + ", skipSourceCheck=" + z2 + ", result=" + this.mController.tryExecute(str, str2, i, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimulateHandover(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra(a.L);
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_Debug", "simulate_handover: missing strategy or result");
            return;
        }
        HandoverMonitor handoverMonitor = this.mController.getHandoverMonitor();
        if (handoverMonitor == null || !handoverMonitor.isMonitoring(stringExtra)) {
            LogUtils.w("Freq_Opt_Debug", "simulate_handover: no active monitoring for ".concat(stringExtra));
            return;
        }
        stringExtra2.hashCode();
        switch (stringExtra2) {
            case "success":
                String monitoringTargetCell = handoverMonitor.getMonitoringTargetCell(stringExtra);
                if (monitoringTargetCell != null) {
                    handoverMonitor.onCellChanged(stringExtra, monitoringTargetCell);
                }
                LogUtils.d("Freq_Opt_Debug", "simulate_handover: SUCCESS for ".concat(stringExtra));
                break;
            case "timeout":
                handoverMonitor.simulateTimeout(stringExtra);
                LogUtils.d("Freq_Opt_Debug", "simulate_handover: TIMEOUT for ".concat(stringExtra));
                break;
            case "failed":
                handoverMonitor.onCellChanged(stringExtra, "DEBUG_OTHER_CELL");
                LogUtils.d("Freq_Opt_Debug", "simulate_handover: FAILED for ".concat(stringExtra));
                break;
            default:
                LogUtils.w("Freq_Opt_Debug", "simulate_handover: unknown result: ".concat(stringExtra2));
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleE2eTest(Intent intent) {
        String stringExtra = intent.getStringExtra("source_cell");
        String stringExtra2 = intent.getStringExtra("target_cell");
        String stringExtra3 = intent.getStringExtra("connection_state");
        int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        String stringExtra4 = intent.getStringExtra("ext_data");
        String stringExtra5 = intent.getStringExtra("handover_result");
        String stringExtra6 = intent.getStringExtra("strategy");
        if (stringExtra6 == null) {
            stringExtra6 = "fdd_tdd";
        }
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_Debug", "e2e_test: missing source_cell or target_cell");
            return;
        }
        int i = !"connected".equals(stringExtra3) ? 1 : 0;
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mController.getStrategies().get(stringExtra6);
        if (iFreqOptStrategy == null) {
            LogUtils.w("Freq_Opt_Debug", "e2e_test: unknown strategy: ".concat(stringExtra6));
            return;
        }
        LogUtils.d("Freq_Opt_Debug", "e2e_test START: strategy=" + stringExtra6 + ", source=" + stringExtra + ", target=" + stringExtra2 + ", state=" + stringExtra3);
        ThresholdValidator validator = this.mController.getValidator();
        int[] iArr = {validator.getRsrpThreshold(), validator.getSinrThreshold(), (int) validator.getRateThresholdKbps()};
        validator.setRsrpThreshold(-200);
        validator.setSinrThreshold(-100);
        validator.setRateThresholdKbps(0L);
        prepareE2eDbRecord(stringExtra6, stringExtra, stringExtra2, stringExtra4, iFreqOptStrategy);
        String str = stringExtra6;
        boolean zExecuteE2eTest = executeE2eTest(iFreqOptStrategy, str, stringExtra, i, intExtra);
        if (stringExtra5 != null && zExecuteE2eTest) {
            scheduleE2eHandover(str, stringExtra5, validator, iArr[0], iArr[1], iArr[2]);
            return;
        }
        validator.setRsrpThreshold(iArr[0]);
        validator.setSinrThreshold(iArr[1]);
        validator.setRateThresholdKbps(iArr[2]);
        LogUtils.d("Freq_Opt_Debug", "e2e_test DONE (no handover simulation)");
    }

    private void prepareE2eDbRecord(String str, String str2, String str3, String str4, IFreqOptStrategy iFreqOptStrategy) {
        this.mController.getDbManager().resetActionTime(str, null);
        FreqOptDBManager dbManager = this.mController.getDbManager();
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = dbManager.queryRecord(str, str2, str3);
        if (freqOptLearningRecordQueryRecord != null) {
            dbManager.deleteRecord(str, freqOptLearningRecordQueryRecord.getId());
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(str, str2, str3);
        freqOptLearningRecord.setCount(iFreqOptStrategy.getLearnCountThreshold(1));
        freqOptLearningRecord.setConnectedCount(iFreqOptStrategy.getLearnCountThreshold(0));
        freqOptLearningRecord.setExtData(str4);
        dbManager.insertOrUpdate(freqOptLearningRecord, iFreqOptStrategy.getTtlMs(), false);
    }

    private boolean executeE2eTest(IFreqOptStrategy iFreqOptStrategy, final String str, final String str2, final int i, final int i2) {
        final boolean[] zArr = {false};
        withDebugFlags(iFreqOptStrategy, true, i == 0, new Runnable() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeE2eTest$2(zArr, str, str2, i, i2);
            }
        });
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeE2eTest$2(boolean[] zArr, String str, String str2, int i, int i2) {
        zArr[0] = this.mController.tryExecute(str, str2, i, i2);
        LogUtils.d("Freq_Opt_Debug", "e2e_test: execute result=" + zArr[0]);
    }

    private void scheduleE2eHandover(final String str, final String str2, final ThresholdValidator thresholdValidator, int i, int i2, long j) {
        final int[] iArr = {i, i2, (int) j};
        this.mController.getWorkThreadHandler().postDelayed(new Runnable() { // from class: com.android.phone.freqopt.debug.FreqOptDebugController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleE2eHandover$3(str, str2, thresholdValidator, iArr);
            }
        }, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleE2eHandover$3(String str, String str2, ThresholdValidator thresholdValidator, int[] iArr) {
        if (this.mController.getHandoverMonitor().isMonitoring(str)) {
            Intent intent = new Intent();
            intent.putExtra("strategy", str);
            intent.putExtra(a.L, str2);
            handleSimulateHandover(intent);
        } else {
            LogUtils.d("Freq_Opt_Debug", "e2e_test: no active monitoring, skip handover simulation");
        }
        thresholdValidator.setRsrpThreshold(iArr[0]);
        thresholdValidator.setSinrThreshold(iArr[1]);
        thresholdValidator.setRateThresholdKbps(iArr[2]);
        LogUtils.d("Freq_Opt_Debug", "e2e_test DONE: thresholds restored");
    }

    private void withDebugFlags(IFreqOptStrategy iFreqOptStrategy, boolean z, boolean z2, Runnable runnable) {
        this.mController.getExecutionEngine().setDebugSkipConnectedRateCheck(z);
        boolean z3 = iFreqOptStrategy instanceof FddTddStrategy;
        if (z3) {
            ((FddTddStrategy) iFreqOptStrategy).setDebugSkipSourceCellCheck(z2);
        }
        try {
            runnable.run();
        } finally {
            this.mController.getExecutionEngine().setDebugSkipConnectedRateCheck(false);
            if (z3) {
                ((FddTddStrategy) iFreqOptStrategy).setDebugSkipSourceCellCheck(false);
            }
        }
    }

    private boolean isDebugEnabled() {
        return "1".equals(SystemProperties.get("persist.sys.freqopt.debug", "0"));
    }
}
