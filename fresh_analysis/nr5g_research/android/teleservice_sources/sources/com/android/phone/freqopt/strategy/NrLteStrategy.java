package com.android.phone.freqopt.strategy;

import android.os.Handler;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.engine.NrLtePostExecVerifier;
import com.android.phone.freqopt.engine.WhitelistAppMonitor;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.RapidFadingCallback;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.PreferredFrequencyInfoS;

/* JADX INFO: loaded from: classes.dex */
public class NrLteStrategy {
    private final Handler mHandler;
    private final NrLtePostExecVerifier mPostExecVerifier;
    private RapidFadingCallback mRapidFadingCallback;
    private boolean mRapidFadingEnabled;
    private int mRapidFadingRegisterRetries;
    private boolean mRapidFadingRegistered;
    private Runnable mRegisterRetryRunnable;
    private TempFadingRecord mTempRecord;
    private Runnable mTempTimeoutRunnable;
    private final WhitelistAppMonitor.OnWhitelistAppStateChangedListener mWhitelistAppListener;
    private final WhitelistAppMonitor mWhitelistAppMonitor;
    private Runnable mWhitelistEnterRunnable;
    private static final String[] EVENT_TYPE_NAMES = {"FAST_FADING", "RECOVERY"};
    private static final String[] STATE_NAMES = {"NORMAL", "FAST_FADING"};
    private static final List NR_LTE_WHITELIST_APPS = MiuiPhoneUtils.getFeatureConfig("nr_lte_whitelist_apps");
    private long mTempRecordTimeoutMs = 30000;
    private long mTempRecordMinDurationMs = 2000;
    private int mLearnCountThreshold = 1;
    private long mTtlMs = 2592000000L;
    private long mMinExecIntervalMs = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    private int mRapidFadingRegisteredSlotId = -1;

    public class TempFadingRecord {
        public int mRsrpCurrent;
        public int mSnrCurrent;
        public CellIdentity mSourceCell;
        public String mSourceCellId;
        public long mTimestamp;
    }

    public NrLteStrategy(Handler handler) {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        this.mWhitelistAppListener = anonymousClass1;
        this.mHandler = handler;
        this.mPostExecVerifier = new NrLtePostExecVerifier(handler);
        List list = NR_LTE_WHITELIST_APPS;
        WhitelistAppMonitor whitelistAppMonitor = new WhitelistAppMonitor(handler, list.isEmpty() ? null : (String[]) list.toArray(new String[0]));
        this.mWhitelistAppMonitor = whitelistAppMonitor;
        whitelistAppMonitor.addListener(anonymousClass1);
        if (handler != null) {
            handler.post(new NrLteStrategy$$ExternalSyntheticLambda0(this));
        }
        LogUtils.i("Freq_Opt_NrLte", "init done");
    }

    /* JADX INFO: renamed from: com.android.phone.freqopt.strategy.NrLteStrategy$1, reason: invalid class name */
    class AnonymousClass1 implements WhitelistAppMonitor.OnWhitelistAppStateChangedListener {
        AnonymousClass1() {
        }

        @Override // com.android.phone.freqopt.engine.WhitelistAppMonitor.OnWhitelistAppStateChangedListener
        public void onWhitelistAppForeground(final String str) {
            FreqOptController freqOptController = FreqOptController.getInstance();
            if (freqOptController == null || !freqOptController.isStrategyActive("nr_lte")) {
                LogUtils.d("Freq_Opt_NrLte", "whitelist app foreground: strategy not active, skip");
                return;
            }
            LogUtils.d("Freq_Opt_NrLte", "whitelist app foreground: " + str + ", delay 3000ms");
            NrLteStrategy.this.cancelPendingWhitelistEnter();
            NrLteStrategy.this.mWhitelistEnterRunnable = new Runnable() { // from class: com.android.phone.freqopt.strategy.NrLteStrategy$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onWhitelistAppForeground$0(str);
                }
            };
            NrLteStrategy.this.mHandler.postDelayed(NrLteStrategy.this.mWhitelistEnterRunnable, 3000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWhitelistAppForeground$0(String str) {
            NrLteStrategy.this.mWhitelistEnterRunnable = null;
            if (!str.equals(NrLteStrategy.this.mWhitelistAppMonitor.getForegroundWhitelistApp())) {
                LogUtils.d("Freq_Opt_NrLte", "whitelist app no longer foreground, skip enter command, pkg=".concat(str));
                return;
            }
            LogUtils.d("Freq_Opt_NrLte", "whitelist app enter command, pkg=".concat(str));
            if (NrLteStrategy.this.mRapidFadingEnabled) {
                LogUtils.d("Freq_Opt_NrLte", "rapidFading already enabled, skip");
            } else {
                NrLteStrategy.this.mRapidFadingEnabled = true;
                FreqOptUtils.setRapidFadingEnabled(true, DefaultSimManager.getDefaultDataSlotId());
            }
        }

        @Override // com.android.phone.freqopt.engine.WhitelistAppMonitor.OnWhitelistAppStateChangedListener
        public void onWhitelistAppBackground(String str) {
            LogUtils.d("Freq_Opt_NrLte", "whitelist app background: " + str + ", send exit immediately");
            NrLteStrategy.this.cancelPendingWhitelistEnter();
            if (!NrLteStrategy.this.mRapidFadingEnabled) {
                LogUtils.d("Freq_Opt_NrLte", "rapidFading not enabled, skip disable");
            } else {
                NrLteStrategy.this.mRapidFadingEnabled = false;
                FreqOptUtils.setRapidFadingEnabled(false, DefaultSimManager.getDefaultDataSlotId());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingWhitelistEnter() {
        Runnable runnable;
        Handler handler = this.mHandler;
        if (handler == null || (runnable = this.mWhitelistEnterRunnable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
        this.mWhitelistEnterRunnable = null;
    }

    public void onDisabled() {
        LogUtils.i("Freq_Opt_NrLte", "onDisabled: cleaning up active state");
        this.mPostExecVerifier.cancelAll();
        if (this.mRapidFadingEnabled) {
            this.mRapidFadingEnabled = false;
            FreqOptUtils.setRapidFadingEnabled(false, DefaultSimManager.getDefaultDataSlotId());
        }
        cancelPendingWhitelistEnter();
        clearTempRecord();
        unregisterRapidFadingIndication();
    }

    public void onDdsChanged() {
        clearTempRecord();
        this.mPostExecVerifier.cancelAll();
        unregisterRapidFadingIndication();
        this.mRapidFadingRegisterRetries = 0;
        registerRapidFadingIndication();
        LogUtils.d("Freq_Opt_NrLte", "onDdsChanged: cleared runtime state and re-registered rapid fading");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRapidFadingIndication() {
        int i;
        if (this.mRapidFadingRegistered) {
            LogUtils.d("Freq_Opt_NrLte", "registerRapidFadingIndication: already registered");
            return;
        }
        if (this.mRapidFadingCallback == null) {
            this.mRapidFadingCallback = new AnonymousClass2();
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        boolean zRegister = RapidFadingCallback.register(defaultDataSlotId, this.mRapidFadingCallback);
        this.mRapidFadingRegistered = zRegister;
        if (zRegister) {
            this.mRapidFadingRegisteredSlotId = defaultDataSlotId;
        }
        LogUtils.d("Freq_Opt_NrLte", "registerRapidFadingIndication result=" + this.mRapidFadingRegistered + ", slotId=" + defaultDataSlotId + ", retries=" + this.mRapidFadingRegisterRetries);
        boolean z = this.mRapidFadingRegistered;
        if (z || (i = this.mRapidFadingRegisterRetries) >= 6) {
            if (z) {
                return;
            }
            LogUtils.w("Freq_Opt_NrLte", "registerRapidFadingIndication: max retries(6) exhausted, giving up");
            FreqOptReporter.reportEvent("nr_lte_register_failed", "nr_lte", null, null, this.mRapidFadingRegisterRetries, null);
            return;
        }
        this.mRapidFadingRegisterRetries = i + 1;
        NrLteStrategy$$ExternalSyntheticLambda0 nrLteStrategy$$ExternalSyntheticLambda0 = new NrLteStrategy$$ExternalSyntheticLambda0(this);
        this.mRegisterRetryRunnable = nrLteStrategy$$ExternalSyntheticLambda0;
        this.mHandler.postDelayed(nrLteStrategy$$ExternalSyntheticLambda0, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: renamed from: com.android.phone.freqopt.strategy.NrLteStrategy$2, reason: invalid class name */
    class AnonymousClass2 extends RapidFadingCallback {
        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRapidFadingEvent$0(RapidFadingCallback.RapidFadingInfo rapidFadingInfo) {
            NrLteStrategy.this.handleRapidFadingInd(rapidFadingInfo);
        }

        @Override // com.android.phone.freqopt.util.RapidFadingCallback
        public void onRapidFadingEvent(final RapidFadingCallback.RapidFadingInfo rapidFadingInfo) {
            NrLteStrategy.this.mHandler.post(new Runnable() { // from class: com.android.phone.freqopt.strategy.NrLteStrategy$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onRapidFadingEvent$0(rapidFadingInfo);
                }
            });
        }
    }

    private void unregisterRapidFadingIndication() {
        RapidFadingCallback rapidFadingCallback;
        int i;
        Runnable runnable;
        Handler handler = this.mHandler;
        if (handler != null && (runnable = this.mRegisterRetryRunnable) != null) {
            handler.removeCallbacks(runnable);
            this.mRegisterRetryRunnable = null;
        }
        if (!this.mRapidFadingRegistered || (rapidFadingCallback = this.mRapidFadingCallback) == null || (i = this.mRapidFadingRegisteredSlotId) < 0) {
            LogUtils.d("Freq_Opt_NrLte", "unregisterRapidFadingIndication: not registered");
            this.mRapidFadingRegistered = false;
            this.mRapidFadingRegisteredSlotId = -1;
            return;
        }
        LogUtils.d("Freq_Opt_NrLte", "unregisterRapidFadingIndication result=" + RapidFadingCallback.unregister(i, rapidFadingCallback) + ", slotId=" + this.mRapidFadingRegisteredSlotId);
        this.mRapidFadingRegistered = false;
        this.mRapidFadingRegisteredSlotId = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRapidFadingInd(RapidFadingCallback.RapidFadingInfo rapidFadingInfo) {
        FreqOptController freqOptController = FreqOptController.getInstance();
        if (freqOptController == null || !freqOptController.isStrategyActive("nr_lte")) {
            LogUtils.d("Freq_Opt_NrLte", "handleRapidFadingInd: strategy not active, skip");
            return;
        }
        if (MiuiPhoneUtils.isWifiConnected()) {
            LogUtils.d("Freq_Opt_NrLte", "handleRapidFadingInd: wifi connected, skip");
            return;
        }
        LogUtils.d("Freq_Opt_NrLte", "handleRapidFadingInd arfcn=" + rapidFadingInfo.arfcn + ", pci=" + rapidFadingInfo.pci + ", rsrp=" + rapidFadingInfo.rsrp + ", snr=" + rapidFadingInfo.snr + ", eventType=" + EVENT_TYPE_NAMES[rapidFadingInfo.eventType & 1] + ", state=" + STATE_NAMES[rapidFadingInfo.state & 1]);
        StringBuilder sb = new StringBuilder("nr_");
        sb.append(rapidFadingInfo.arfcn);
        sb.append("_");
        sb.append(rapidFadingInfo.pci);
        String string = sb.toString();
        StringBuilder sb2 = new StringBuilder("onModemFastFadingDetected sourceCellId=");
        sb2.append(string);
        sb2.append(", rsrp=");
        sb2.append(rapidFadingInfo.rsrp);
        sb2.append(", snr=");
        sb2.append(rapidFadingInfo.snr);
        LogUtils.d("Freq_Opt_NrLte", sb2.toString());
        FreqOptReporter.reportEvent("nr_lte_fast_fading_detected", "nr_lte", string, null, 0, String.valueOf((int) rapidFadingInfo.eventType));
        if (rapidFadingInfo.eventType != 0) {
            LogUtils.d("Freq_Opt_NrLte", "onSignalRecovered, clearing temp record");
            clearTempRecord();
        } else {
            onFastFadingDetected(string, null, (int) rapidFadingInfo.rsrp, (int) rapidFadingInfo.snr);
            tryExecuteOnFastFading(string);
        }
    }

    public void debugDispatch(String str, String str2) {
        LogUtils.d("Freq_Opt_NrLte", "debugDispatch action=" + str + ", source=" + str2);
        str.hashCode();
        switch (str) {
            case "execute":
                tryExecuteOnFastFading(str2);
                break;
            case "stop_stutter":
                this.mPostExecVerifier.debugStopStutterCheck();
                break;
            case "start_stutter":
                this.mPostExecVerifier.debugStartStutterCheck(str2);
                break;
            case "refresh":
                this.mPostExecVerifier.debugRefreshRecordValidity(str2);
                break;
            default:
                LogUtils.w("Freq_Opt_NrLte", "debugDispatch unknown action: ".concat(str));
                break;
        }
    }

    public void debugSimulateLteStutter(int i, int i2) {
        this.mPostExecVerifier.debugSimulateLteStutter(i, i2);
    }

    private FreqOptDBManager getDbManager() {
        FreqOptController freqOptController = FreqOptController.getInstance();
        if (freqOptController == null) {
            LogUtils.w("Freq_Opt_NrLte", "controller is null");
            return null;
        }
        FreqOptDBManager dbManager = freqOptController.getDbManager();
        if (dbManager == null) {
            LogUtils.w("Freq_Opt_NrLte", "dbManager is null");
        }
        return dbManager;
    }

    private void tryExecuteOnFastFading(String str) {
        FreqOptLearningRecord freqOptLearningRecord;
        int defaultDataSlotId;
        FreqOptDBManager dbManager = getDbManager();
        if (dbManager == null) {
            return;
        }
        List listQueryBySourceCell = dbManager.queryBySourceCell("nr_lte", str, this.mTtlMs);
        if (listQueryBySourceCell.isEmpty()) {
            LogUtils.d("Freq_Opt_NrLte", "no matching records");
            return;
        }
        int i = this.mLearnCountThreshold;
        Iterator it = listQueryBySourceCell.iterator();
        while (true) {
            if (!it.hasNext()) {
                freqOptLearningRecord = null;
                break;
            }
            freqOptLearningRecord = (FreqOptLearningRecord) it.next();
            if (freqOptLearningRecord.getCount() >= i && freqOptLearningRecord.getState() == 0) {
                break;
            }
        }
        if (freqOptLearningRecord == null) {
            LogUtils.d("Freq_Opt_NrLte", "no record meets threshold=" + i);
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - freqOptLearningRecord.getActionTime();
        if (jCurrentTimeMillis < this.mMinExecIntervalMs) {
            LogUtils.d("Freq_Opt_NrLte", "tryExecuteOnFastFading rate-limited, elapsed=" + jCurrentTimeMillis + "ms, minInterval=" + this.mMinExecIntervalMs + "ms");
            return;
        }
        try {
            defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        } catch (Exception e) {
            LogUtils.w("Freq_Opt_NrLte", "getDdsSlotId failed", e);
            defaultDataSlotId = 0;
        }
        executeOptimization(freqOptLearningRecord, defaultDataSlotId);
        dbManager.updateActionTime("nr_lte", freqOptLearningRecord.getId());
        LogUtils.i("Freq_Opt_NrLte", "executed, sourceCellId=" + str);
    }

    public Map buildExtFields(CellIdentity cellIdentity) {
        HashMap map = new HashMap();
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            int[] bands = cellIdentityLte.getBands();
            if (bands != null && bands.length > 0) {
                map.put("fast_fading_band", Integer.valueOf(bands[0]));
            }
            map.put("fast_fading_freq", Integer.valueOf(cellIdentityLte.getEarfcn()));
            int bandwidth = cellIdentityLte.getBandwidth();
            if (bandwidth > 0) {
                map.put("fast_fading_bw", Integer.valueOf(bandwidth));
            }
            String mccString = cellIdentityLte.getMccString();
            String mncString = cellIdentityLte.getMncString();
            if (mccString != null && mncString != null) {
                map.put("fast_fading_plmn", mccString.concat(mncString));
            }
        }
        return map;
    }

    public long getTtlMs() {
        return this.mTtlMs;
    }

    public void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i) {
        LogUtils.d("Freq_Opt_NrLte", "executeOptimization record=" + freqOptLearningRecord + ", slotId=" + i);
        Map extFields = freqOptLearningRecord.getExtFields();
        if (extFields.isEmpty()) {
            LogUtils.w("Freq_Opt_NrLte", "executeOptimization: no ext data");
            return;
        }
        int iIntValue = extFields.get("fast_fading_freq") instanceof Number ? ((Number) extFields.get("fast_fading_freq")).intValue() : 0;
        int iIntValue2 = extFields.get("fast_fading_band") instanceof Number ? ((Number) extFields.get("fast_fading_band")).intValue() : 0;
        int iConvertToActiveBand = FreqOptUtils.convertToActiveBand(iIntValue2);
        int iIntValue3 = extFields.get("fast_fading_bw") instanceof Number ? ((Number) extFields.get("fast_fading_bw")).intValue() : 0;
        Object obj = extFields.get("fast_fading_plmn");
        String str = obj instanceof String ? (String) obj : null;
        PreferredFrequencyInfoS preferredFrequencyInfoS = new PreferredFrequencyInfoS();
        preferredFrequencyInfoS.freq = iIntValue;
        preferredFrequencyInfoS.band = iConvertToActiveBand;
        preferredFrequencyInfoS.bandwidth = FreqOptUtils.convertToLteBandwidthE(iIntValue3);
        if (str != null && str.length() >= 5) {
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = Short.parseShort(str.substring(0, 3));
            plmnIdS.mnc = Short.parseShort(str.substring(3));
            plmnIdS.mncIncludesPcsDigit = str.length() - 3 == 3;
            ArrayList arrayList = new ArrayList();
            arrayList.add(plmnIdS);
            preferredFrequencyInfoS.plmn_list_len = 1;
            preferredFrequencyInfoS.plmn = arrayList;
        }
        FreqOptUtils.setPreferredFrequencyList(i, 2, 0, Collections.singletonList(preferredFrequencyInfoS), null, "FreqOpt_NrLte_Mobility", null);
        FreqOptReporter.reportEvent("nr_lte_opt_exec", "nr_lte", freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId(), 0, "band=" + iIntValue2 + ",freq=" + iIntValue);
        LogUtils.d("Freq_Opt_NrLte", "executeOptimization setPreferredFrequencyList done, band=" + iIntValue2 + ", activeBand=" + iConvertToActiveBand + ", freq=" + iIntValue);
        this.mPostExecVerifier.startVerification(i, freqOptLearningRecord.getSourceCellId(), iIntValue2, iIntValue);
    }

    public FreqOptLearningRecord learnIratSwitch(String str, String str2, CellIdentity cellIdentity, int i, int i2) {
        LogUtils.d("Freq_Opt_NrLte", "learnIratSwitch source=" + str + ", target=" + str2);
        FreqOptDBManager dbManager = getDbManager();
        if (dbManager == null) {
            return null;
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord("nr_lte", str, str2);
        Map mapBuildExtFields = buildExtFields(cellIdentity);
        mapBuildExtFields.put("fast_fading_rsrp", Integer.valueOf(i));
        mapBuildExtFields.put("fast_fading_snr", Integer.valueOf(i2));
        freqOptLearningRecord.setExtFields(mapBuildExtFields);
        if (dbManager.insertOrUpdate(freqOptLearningRecord, this.mTtlMs, false) < 0) {
            LogUtils.w("Freq_Opt_NrLte", "learnIratSwitch DB insert/update failed");
            FreqOptReporter.reportEvent("nr_lte_learn_fail", "nr_lte", str, str2, 0, "reason=db_fail");
            return null;
        }
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = dbManager.queryRecord("nr_lte", str, str2);
        if (freqOptLearningRecordQueryRecord != null) {
            LogUtils.i("Freq_Opt_NrLte", "learnIratSwitch success, count=" + freqOptLearningRecordQueryRecord.getCount());
            FreqOptReporter.reportEvent("nr_lte_learn_success", "nr_lte", str, str2, freqOptLearningRecordQueryRecord.getCount(), null);
            return freqOptLearningRecordQueryRecord;
        }
        LogUtils.i("Freq_Opt_NrLte", "learnIratSwitch success");
        FreqOptReporter.reportEvent("nr_lte_learn_success", "nr_lte", str, str2, 0, null);
        return freqOptLearningRecordQueryRecord;
    }

    public void onFastFadingDetected(String str, CellIdentity cellIdentity, int i, int i2) {
        LogUtils.d("Freq_Opt_NrLte", "onFastFadingDetected source=" + str + ", rsrp=" + i + ", snr=" + i2);
        clearTempRecord();
        TempFadingRecord tempFadingRecord = new TempFadingRecord();
        this.mTempRecord = tempFadingRecord;
        tempFadingRecord.mSourceCellId = str;
        tempFadingRecord.mSourceCell = cellIdentity;
        tempFadingRecord.mRsrpCurrent = i;
        tempFadingRecord.mSnrCurrent = i2;
        tempFadingRecord.mTimestamp = SystemClock.elapsedRealtime();
        if (this.mHandler != null) {
            this.mTempTimeoutRunnable = new Runnable() { // from class: com.android.phone.freqopt.strategy.NrLteStrategy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onFastFadingDetected$0();
                }
            };
            LogUtils.d("Freq_Opt_NrLte", "onFastFadingDetected temp record created: source=" + this.mTempRecord.mSourceCellId + ", rsrp=" + this.mTempRecord.mRsrpCurrent + ", snr=" + this.mTempRecord.mSnrCurrent + ", timestamp=" + this.mTempRecord.mTimestamp);
            this.mHandler.postDelayed(this.mTempTimeoutRunnable, this.mTempRecordTimeoutMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFastFadingDetected$0() {
        LogUtils.d("Freq_Opt_NrLte", "onFastFadingDetected temp record expired (30s timeout)");
        clearTempRecord();
    }

    public TempFadingRecord onIratSwitchTo4G(String str, CellIdentity cellIdentity) {
        if (this.mTempRecord == null) {
            LogUtils.d("Freq_Opt_NrLte", "onIratSwitchTo4G no temp record");
            return null;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mTempRecord.mTimestamp;
        LogUtils.d("Freq_Opt_NrLte", "onIratSwitchTo4G elapsed=" + jElapsedRealtime + "ms, target=" + str);
        if (jElapsedRealtime < this.mTempRecordMinDurationMs) {
            LogUtils.d("Freq_Opt_NrLte", "onIratSwitchTo4G elapsed=" + jElapsedRealtime + "ms < minDuration, keeping temp record");
            return null;
        }
        TempFadingRecord tempFadingRecord = this.mTempRecord;
        clearTempRecord();
        LogUtils.d("Freq_Opt_NrLte", "onIratSwitchTo4G qualified for learning, source=" + tempFadingRecord.mSourceCellId);
        return tempFadingRecord;
    }

    private void clearTempRecord() {
        Runnable runnable;
        this.mTempRecord = null;
        Handler handler = this.mHandler;
        if (handler == null || (runnable = this.mTempTimeoutRunnable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
        this.mTempTimeoutRunnable = null;
    }

    public void updateCloudConfig(Map map) {
        if (map == null) {
            LogUtils.d("Freq_Opt_NrLte", "updateCloudConfig: config is null, skip");
            return;
        }
        this.mTempRecordTimeoutMs = FreqOptUtils.parseLong(map, "freq_opt_nr_lte_temp_timeout", this.mTempRecordTimeoutMs);
        this.mTempRecordMinDurationMs = FreqOptUtils.parseLong(map, "freq_opt_nr_lte_temp_min_duration", this.mTempRecordMinDurationMs);
        this.mLearnCountThreshold = FreqOptUtils.parseInt(map, "freq_opt_nr_lte_learn_threshold", this.mLearnCountThreshold);
        this.mTtlMs = FreqOptUtils.parseLong(map, "freq_opt_nr_lte_ttl", this.mTtlMs);
        this.mMinExecIntervalMs = FreqOptUtils.parseLong(map, "freq_opt_nr_lte_min_exec_interval", this.mMinExecIntervalMs);
        this.mPostExecVerifier.updateConfig(FreqOptUtils.parseLong(map, "freq_opt_nr_lte_rat_check_delay", 10000L), FreqOptUtils.parseInt(map, "freq_opt_nr_lte_rat_check_retries", 3), FreqOptUtils.parseLong(map, "freq_opt_nr_lte_stutter_window", RelayUtils.Relay.RETRY_DELAY_IN_MILLIS), FreqOptUtils.parseInt(map, "freq_opt_nr_lte_qoe_threshold", 80));
    }
}
