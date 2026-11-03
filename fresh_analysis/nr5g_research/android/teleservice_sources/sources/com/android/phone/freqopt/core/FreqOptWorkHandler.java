package com.android.phone.freqopt.core;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SignalStrength;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.debug.FreqOptDebugController;
import com.android.phone.freqopt.engine.ExecutionEngine;
import com.android.phone.freqopt.engine.HandoverMonitor;
import com.android.phone.freqopt.engine.PostExecutionMonitor;
import com.android.phone.freqopt.strategy.FddTddStrategy;
import com.android.phone.freqopt.strategy.NrLteStrategy;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
class FreqOptWorkHandler extends Handler {
    private final FreqOptController mController;
    private int mDdsPhoneId;
    private boolean mIsDdsChanging;
    private PendingMeasurement mPending;
    private CellIdentity mPreviousCell;
    private long mPreviousCellCampTime;
    private String mPreviousCellId;
    private int mPreviousConnectionState;

    FreqOptWorkHandler(Looper looper, FreqOptController freqOptController) {
        super(looper);
        this.mDdsPhoneId = -1;
        this.mPreviousConnectionState = -1;
        this.mController = freqOptController;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            handleCellLocationChanged(message.arg1);
            return;
        }
        if (i == 2) {
            handleDdsChanged();
            return;
        }
        if (i == 3) {
            exitDdsChangedMonitorStatus();
            return;
        }
        if (i == 4) {
            handleDelayedRateMeasure(message.arg1);
            return;
        }
        if (i != 100) {
            return;
        }
        FreqOptDebugController debugController = this.mController.getDebugController();
        if (debugController != null) {
            debugController.handleDebugCommand((Intent) message.obj);
        } else {
            LogUtils.w("Freq_Opt_WorkHandler", "handleMessage: debugController is null");
        }
    }

    int getDdsPhoneId() {
        return this.mDdsPhoneId;
    }

    boolean isDdsChanging() {
        return this.mIsDdsChanging;
    }

    private void handleDdsChanged() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (this.mDdsPhoneId != defaultDataSlotId) {
            enterDdsChangedMonitorStatus();
            this.mDdsPhoneId = defaultDataSlotId;
            this.mPreviousCell = null;
            this.mPreviousCellId = null;
            this.mPreviousCellCampTime = 0L;
            this.mPreviousConnectionState = -1;
            removeMessages(4);
            this.mPending = null;
            ExecutionEngine executionEngine = this.mController.getExecutionEngine();
            if (executionEngine != null) {
                executionEngine.cancelPendingIdleExecution();
                executionEngine.cancelPendingConnectedRateCheck();
            }
            HandoverMonitor handoverMonitor = this.mController.getHandoverMonitor();
            if (handoverMonitor != null) {
                handoverMonitor.cancelAll();
            }
            for (IFreqOptStrategy iFreqOptStrategy : this.mController.getStrategies().values()) {
                if (iFreqOptStrategy instanceof FddTddStrategy) {
                    ((FddTddStrategy) iFreqOptStrategy).cancelPendingActions();
                }
            }
            NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
            if (nrLteStrategy != null) {
                nrLteStrategy.onDdsChanged();
            }
            PostExecutionMonitor postExecutionMonitor = this.mController.getPostExecutionMonitor();
            if (postExecutionMonitor != null) {
                Iterator it = this.mController.getStrategies().keySet().iterator();
                while (it.hasNext()) {
                    postExecutionMonitor.cancelMonitoring((String) it.next());
                }
            }
        }
    }

    private void enterDdsChangedMonitorStatus() {
        this.mIsDdsChanging = true;
        startMonitorTimerForDdsChanged();
    }

    private void exitDdsChangedMonitorStatus() {
        stopMonitorTimerForDdsChanged();
        this.mIsDdsChanging = false;
        LogUtils.d("Freq_Opt_WorkHandler", "exitDdsChangedMonitorStatus");
    }

    private void startMonitorTimerForDdsChanged() {
        LogUtils.d("Freq_Opt_WorkHandler", "startMonitorTimerForDdsChanged");
        stopMonitorTimerForDdsChanged();
        sendMessageDelayed(obtainMessage(3), 10000L);
    }

    private void stopMonitorTimerForDdsChanged() {
        removeMessages(3);
    }

    private void handleCellLocationChanged(int i) {
        LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged, phoneId=" + i);
        CellIdentity currentCell = getCurrentCell(i);
        if (currentCell == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged: cellIdentity is null");
            this.mPreviousCell = null;
            this.mPreviousCellId = null;
            this.mPreviousConnectionState = -1;
            return;
        }
        String globalCellId = currentCell.getGlobalCellId();
        if (globalCellId == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged: globalCellId is null");
            return;
        }
        if (globalCellId.equals(this.mPreviousCellId)) {
            int rrcConnectionState = getRrcConnectionState(i);
            if (rrcConnectionState == this.mPreviousConnectionState) {
                LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged: same cell, same state");
                return;
            }
            LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged: same cell, state changed " + this.mPreviousConnectionState + " -> " + rrcConnectionState);
        }
        if (MiuiPhoneUtils.isWifiConnected()) {
            LogUtils.d("Freq_Opt_WorkHandler", "handleCellLocationChanged ignored: wifi connected");
            this.mPreviousCell = currentCell;
            this.mPreviousCellId = globalCellId;
            this.mPreviousCellCampTime = SystemClock.elapsedRealtime();
            this.mPreviousConnectionState = getRrcConnectionState(i);
            if (this.mPending != null) {
                removeMessages(4);
                this.mPending = null;
                return;
            }
            return;
        }
        LogUtils.i("Freq_Opt_WorkHandler", "cell changed: " + this.mPreviousCellId + " -> " + globalCellId);
        cancelPendingExecutionIfNeeded(globalCellId);
        notifyHandoverMonitor(globalCellId);
        boolean zCheckExecutionOpportunity = checkExecutionOpportunity(currentCell, globalCellId, i);
        checkLearningOpportunity(currentCell, globalCellId, i);
        checkNrLteIratSwitch(currentCell, globalCellId, i);
        if (zCheckExecutionOpportunity) {
            return;
        }
        this.mPreviousCell = currentCell;
        this.mPreviousCellId = globalCellId;
        this.mPreviousCellCampTime = SystemClock.elapsedRealtime();
        this.mPreviousConnectionState = getRrcConnectionState(i);
    }

    private CellIdentity getCurrentCell(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "getCurrentCell: phone is null");
            return null;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "getCurrentCell: cellIdentity is null");
        }
        return currentCellIdentity;
    }

    private void notifyHandoverMonitor(String str) {
        HandoverMonitor handoverMonitor = this.mController.getHandoverMonitor();
        PostExecutionMonitor postExecutionMonitor = this.mController.getPostExecutionMonitor();
        for (String str2 : this.mController.getStrategies().keySet()) {
            if (handoverMonitor != null) {
                handoverMonitor.onCellChanged(str2, str);
            }
            if (postExecutionMonitor != null) {
                postExecutionMonitor.onCellChanged(str2, str);
            }
        }
    }

    public void clearPreviousCellTracking() {
        this.mPreviousCell = null;
        this.mPreviousCellId = null;
        this.mPreviousConnectionState = -1;
        LogUtils.d("Freq_Opt_WorkHandler", "clearPreviousCellTracking: cleared after execution success");
    }

    private boolean checkExecutionOpportunity(CellIdentity cellIdentity, String str, int i) {
        int rrcConnectionState = getRrcConnectionState(i);
        for (Map.Entry entry : this.mController.getStrategies().entrySet()) {
            String str2 = (String) entry.getKey();
            IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) entry.getValue();
            if (this.mController.isStrategyActive(str2) && iFreqOptStrategy.isSourceCell(cellIdentity) && this.mController.tryExecute(str2, str, rrcConnectionState, i)) {
                clearPreviousCellTracking();
                return true;
            }
        }
        return false;
    }

    private void checkLearningOpportunity(CellIdentity cellIdentity, String str, int i) {
        if (this.mPreviousCell == null || this.mPreviousCellId == null) {
            return;
        }
        boolean z = false;
        for (Map.Entry entry : this.mController.getStrategies().entrySet()) {
            String str2 = (String) entry.getKey();
            IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) entry.getValue();
            if (this.mController.isStrategyActive(str2) && iFreqOptStrategy.isSourceCell(this.mPreviousCell) && iFreqOptStrategy.isTargetCell(cellIdentity)) {
                LogUtils.d("Freq_Opt_WorkHandler", "detected transition for " + str2 + ", source=" + this.mPreviousCellId + ", target=" + str);
                z = true;
            }
        }
        if (z) {
            scheduleRateMeasurement(cellIdentity, str, i);
        }
    }

    private boolean isValidNrToLteTransition(CellIdentity cellIdentity) {
        return this.mPreviousCell != null && this.mPreviousCellId != null && this.mController.isStrategyActive("nr_lte") && (this.mPreviousCell instanceof CellIdentityNr) && (cellIdentity instanceof CellIdentityLte);
    }

    private void checkNrLteIratSwitch(CellIdentity cellIdentity, String str, int i) {
        if (isValidNrToLteTransition(cellIdentity)) {
            NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
            if (nrLteStrategy == null) {
                LogUtils.w("Freq_Opt_WorkHandler", "checkNrLteIratSwitch: NrLteStrategy is null");
                return;
            }
            NrLteStrategy.TempFadingRecord tempFadingRecordOnIratSwitchTo4G = nrLteStrategy.onIratSwitchTo4G(str, cellIdentity);
            if (tempFadingRecordOnIratSwitchTo4G == null) {
                LogUtils.d("Freq_Opt_WorkHandler", "checkNrLteIratSwitch: no qualified temp record for target=" + str);
                return;
            }
            LogUtils.d("Freq_Opt_WorkHandler", "checkNrLteIratSwitch: qualified IRAT switch, source=" + tempFadingRecordOnIratSwitchTo4G.mSourceCellId + " -> target=" + str);
            FreqOptLearningRecord freqOptLearningRecordLearnIratSwitch = nrLteStrategy.learnIratSwitch(tempFadingRecordOnIratSwitchTo4G.mSourceCellId, str, cellIdentity, tempFadingRecordOnIratSwitchTo4G.mRsrpCurrent, tempFadingRecordOnIratSwitchTo4G.mSnrCurrent);
            if (freqOptLearningRecordLearnIratSwitch != null) {
                LogUtils.i("Freq_Opt_WorkHandler", "checkNrLteIratSwitch: learning success, count=" + freqOptLearningRecordLearnIratSwitch.getCount());
            }
        }
    }

    private void scheduleRateMeasurement(CellIdentity cellIdentity, String str, int i) {
        this.mPending = new PendingMeasurement(this.mPreviousCell, this.mPreviousCellId, cellIdentity, str, this.mPreviousCellCampTime, TrafficStats.getMobileRxBytes(), SystemClock.elapsedRealtime(), getRrcConnectionState(i));
        removeMessages(4);
        sendMessageDelayed(obtainMessage(4, i, 0), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private int getRrcConnectionState(int i) {
        return FreqOptUtils.getNr5gRrcState(i) == 2 ? 0 : 1;
    }

    private void handleDelayedRateMeasure(int i) {
        if (this.mPending == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "handleDelayedRateMeasure: no pending measurement");
            return;
        }
        if (MiuiPhoneUtils.isWifiConnected()) {
            LogUtils.i("Freq_Opt_WorkHandler", "handleDelayedRateMeasure ignored: wifi connected");
            this.mPending = null;
            return;
        }
        if (!verifyStillOnTargetCell(i)) {
            this.mPending = null;
            return;
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "handleDelayedRateMeasure: phone is null");
            this.mPending = null;
            return;
        }
        MeasurementResult measurementResultMeasureRateAndSignal = measureRateAndSignal(phone);
        LogUtils.d("Freq_Opt_WorkHandler", "handleDelayedRateMeasure: rateKbps=" + measurementResultMeasureRateAndSignal.rateKbps + ", rsrp=" + measurementResultMeasureRateAndSignal.rsrp + ", sinr=" + measurementResultMeasureRateAndSignal.sinr);
        triggerLearningForMatchingStrategies(measurementResultMeasureRateAndSignal, calculateDwellTime());
        this.mPending = null;
    }

    private boolean verifyStillOnTargetCell(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "verifyStillOnTargetCell: phone is null");
            return false;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            LogUtils.d("Freq_Opt_WorkHandler", "verifyStillOnTargetCell: currentCell is null");
            return false;
        }
        String globalCellId = currentCellIdentity.getGlobalCellId();
        boolean zEquals = this.mPending.targetCellId.equals(globalCellId);
        PendingMeasurement pendingMeasurement = this.mPending;
        if (!zEquals) {
            LogUtils.i("Freq_Opt_WorkHandler", "verifyStillOnTargetCell: cell changed, discard. expected=" + pendingMeasurement.targetCellId + ", actual=" + globalCellId);
            return false;
        }
        pendingMeasurement.targetCell = currentCellIdentity;
        return true;
    }

    private MeasurementResult measureRateAndSignal(Phone phone) {
        int ssRsrp;
        int ssSinr;
        List cellSignalStrengths;
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        PendingMeasurement pendingMeasurement = this.mPending;
        long j = jElapsedRealtime - pendingMeasurement.transitionTime;
        long j2 = j > 0 ? ((mobileRxBytes - pendingMeasurement.transitionRxBytes) * 8) / j : 0L;
        SignalStrength signalStrength = phone.getSignalStrength();
        if (signalStrength == null || (cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class)) == null || cellSignalStrengths.isEmpty()) {
            ssRsrp = Integer.MIN_VALUE;
            ssSinr = Integer.MIN_VALUE;
        } else {
            CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrengths.get(0);
            ssRsrp = cellSignalStrengthNr.getSsRsrp();
            ssSinr = cellSignalStrengthNr.getSsSinr();
        }
        return new MeasurementResult(j2, ssRsrp, ssSinr);
    }

    private long calculateDwellTime() {
        PendingMeasurement pendingMeasurement = this.mPending;
        long j = pendingMeasurement.sourceCampTime;
        if (j > 0) {
            return pendingMeasurement.transitionTime - j;
        }
        return -1L;
    }

    private void triggerLearningForMatchingStrategies(MeasurementResult measurementResult, long j) {
        for (Map.Entry entry : this.mController.getStrategies().entrySet()) {
            String str = (String) entry.getKey();
            IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) entry.getValue();
            if (this.mController.isStrategyActive(str) && iFreqOptStrategy.isSourceCell(this.mPending.sourceCell) && iFreqOptStrategy.isTargetCell(this.mPending.targetCell)) {
                FreqOptController freqOptController = this.mController;
                PendingMeasurement pendingMeasurement = this.mPending;
                freqOptController.onCellTransition(str, pendingMeasurement.sourceCellId, pendingMeasurement.targetCellId, pendingMeasurement.targetCell, measurementResult.rsrp, measurementResult.sinr, measurementResult.rateKbps, j, pendingMeasurement.connectionState);
            }
        }
    }

    private void cancelPendingExecutionIfNeeded(String str) {
        ExecutionEngine executionEngine = this.mController.getExecutionEngine();
        if (executionEngine == null) {
            LogUtils.w("Freq_Opt_WorkHandler", "cancelPendingExecutionIfNeeded: executionEngine is null");
            return;
        }
        if (executionEngine.hasPendingIdleExecution() && !str.equals(executionEngine.getPendingIdleSourceCellId())) {
            LogUtils.i("Freq_Opt_WorkHandler", "cell changed from pending idle source, cancelling delayed execution");
            executionEngine.cancelPendingIdleExecution();
        }
        if (!executionEngine.hasPendingConnectedRateCheck() || str.equals(executionEngine.getPendingConnectedSourceCellId())) {
            return;
        }
        LogUtils.i("Freq_Opt_WorkHandler", "cell changed from pending connected source, cancelling rate check");
        executionEngine.cancelPendingConnectedRateCheck();
    }

    class PendingMeasurement {
        final int connectionState;
        final long sourceCampTime;
        final CellIdentity sourceCell;
        final String sourceCellId;
        CellIdentity targetCell;
        final String targetCellId;
        final long transitionRxBytes;
        final long transitionTime;

        PendingMeasurement(CellIdentity cellIdentity, String str, CellIdentity cellIdentity2, String str2, long j, long j2, long j3, int i) {
            this.sourceCell = cellIdentity;
            this.sourceCellId = str;
            this.targetCell = cellIdentity2;
            this.targetCellId = str2;
            this.sourceCampTime = j;
            this.transitionRxBytes = j2;
            this.transitionTime = j3;
            this.connectionState = i;
        }
    }

    class MeasurementResult {
        final long rateKbps;
        final int rsrp;
        final int sinr;

        MeasurementResult(long j, int i, int i2) {
            this.rateKbps = j;
            this.rsrp = i;
            this.sinr = i2;
        }
    }
}
