package com.android.phone.streaming;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.b.a;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class StreamingOptimizationExecutor {
    private final ExecutorCallback mCallback;
    private final Context mContext;
    private long mLastOpt2ExecutedTime = -600000;
    private final StreamingOptimizationState mOptStateMgr;
    private final StreamingOptimizationStats mStats;
    private final Handler mWorkHandler;

    public interface ExecutorCallback {
        void executeDataStallEscape(int i);

        void executeDataStallEscapeNormal(int i);

        void resetDataStallEscape(int i);

        void set5GNetworkCapability(int i, boolean z);
    }

    public StreamingOptimizationExecutor(Context context, Handler handler, StreamingOptimizationState streamingOptimizationState, StreamingOptimizationStats streamingOptimizationStats, ExecutorCallback executorCallback) {
        this.mContext = context;
        Objects.requireNonNull(handler, "workHandler cannot be null");
        this.mWorkHandler = handler;
        Objects.requireNonNull(streamingOptimizationState, "optStateMgr cannot be null");
        this.mOptStateMgr = streamingOptimizationState;
        Objects.requireNonNull(streamingOptimizationStats, "stats cannot be null");
        this.mStats = streamingOptimizationStats;
        Objects.requireNonNull(executorCallback, "callback cannot be null");
        this.mCallback = executorCallback;
    }

    private boolean tryHandleScoreInOptState(int i, int i2) {
        boolean zIsCollectingScores = this.mOptStateMgr.getAuxState().isCollectingScores();
        StreamingOptimizationState streamingOptimizationState = this.mOptStateMgr;
        if (zIsCollectingScores) {
            streamingOptimizationState.getAuxState().addCollectedScore(i2);
            return true;
        }
        if (!streamingOptimizationState.getAuxState().isChecking()) {
            return false;
        }
        tryStartScoreCollectionOnCellChange(i, i2);
        return true;
    }

    public void evaluateStableCellByScore(int i) {
        if (!this.mOptStateMgr.getAuxState().isStableCellDecided() && MiuiPhoneUtils.isSupportCongestionOptimization() && StreamingCloudConfigHelper.isCongestionEnabled() && !MiuiPhoneUtils.isDataRoaming(this.mOptStateMgr.getSceneContext().getDdsSlot()) && this.mOptStateMgr.getOptState() == 0) {
            this.mOptStateMgr.getAuxState().setStableCellDecided(true);
            if (i > 80) {
                StreamingOptimizationController.localLog("stableCell", "score=" + i + ", enabled");
                Log.i("StreamingOptimizationExecutor", "First QoE score=" + i + " > threshold, enabling stable cell strategy");
                this.mWorkHandler.sendEmptyMessage(7);
                return;
            }
            StreamingOptimizationController.localLog("stableCell", "score=" + i + ", skipped");
            Log.i("StreamingOptimizationExecutor", "First QoE score=" + i + " <= threshold, skip stable cell strategy");
        }
    }

    public void handleCongestionReason(int i, int i2, int i3, int i4) {
        boolean z;
        if (tryHandleScoreInOptState(i, i4)) {
            return;
        }
        if (!MiuiPhoneUtils.isSupportCongestionOptimization() || !StreamingCloudConfigHelper.isCongestionEnabled()) {
            Log.d("StreamingOptimizationExecutor", "Congestion optimization is disabled, skip handling");
            return;
        }
        if (MiuiPhoneUtils.isDataRoaming(i)) {
            Log.d("StreamingOptimizationExecutor", "Data roaming is enabled on slot " + i + ", skip handling");
            return;
        }
        if (this.mOptStateMgr.getAuxState().isWaitingToRestore5G()) {
            Log.i("StreamingOptimizationExecutor", "New stall during 5G restore waiting period, canceling wait and restoring 5G immediately");
            cancelRestore5GWaiting(i);
        }
        this.mStats.onStall(i3, i4);
        this.mWorkHandler.removeMessages(7);
        if (this.mOptStateMgr.getAuxState().isStableCellEnabled()) {
            Log.i("StreamingOptimizationExecutor", "Stable cell strategy is enabled, disable it due to congestion");
            this.mWorkHandler.sendEmptyMessage(8);
            z = true;
        } else {
            z = false;
        }
        StreamingOptimizationController.localLog("congestion", "reason=" + i3 + ", score=" + i4 + ", stableCellDisabled=" + z);
        handleOptimizationByState(i4, i, i2);
    }

    public void handleNonCongestionReason(int i, int i2, int i3, int i4) {
        if (tryHandleScoreInOptState(i, i4)) {
            return;
        }
        if (!MiuiPhoneUtils.isSupportInternationalTravelDataOptimization() || !StreamingCloudConfigHelper.isOverseasTravelEnabled()) {
            Log.d("StreamingOptimizationExecutor", "Overseas travel feature is disabled, skip handling");
            return;
        }
        boolean zIsOverSeaSubway = this.mOptStateMgr.getSceneContext().isOverSeaSubway();
        boolean zIsDataRoaming = MiuiPhoneUtils.isDataRoaming(i);
        if (!zIsOverSeaSubway || !zIsDataRoaming) {
            Log.d("StreamingOptimizationExecutor", "Not in overseas subway or roaming state, skip handling");
            return;
        }
        if (this.mOptStateMgr.getAuxState().isWaitingToRestore5G()) {
            Log.i("StreamingOptimizationExecutor", "New stall during 5G restore waiting period, canceling wait and restoring 5G immediately");
            cancelRestore5GWaiting(i);
        }
        this.mStats.onStall(i3, i4);
        handleOptimizationByState(i4, i, i2);
    }

    private void handleOptimizationByState(int i, int i2, int i3) {
        if (this.mOptStateMgr.getOptState() == 0) {
            if (canStartNewRound()) {
                startOptimization1(i, i2, i3);
            } else {
                Log.d("StreamingOptimizationExecutor", "Cannot start new round");
            }
        }
    }

    private boolean canStartNewRound() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long roundEndTime = jElapsedRealtime - this.mOptStateMgr.getRoundEndTime();
        Log.d("StreamingOptimizationExecutor", "Time since last round: " + roundEndTime + "ms");
        if (roundEndTime < 20000) {
            Log.d("StreamingOptimizationExecutor", "Too soon to start new round, need at least 20000ms");
            return false;
        }
        long j = jElapsedRealtime - 3600000;
        long roundStartTime = this.mOptStateMgr.getRoundStartTime();
        int roundCount = this.mOptStateMgr.getRoundCount();
        if (roundStartTime > 0 && roundStartTime < j) {
            Log.d("StreamingOptimizationExecutor", "Reset round count, first round was more than 1 hour ago");
            this.mOptStateMgr.resetRoundCount();
            return true;
        }
        if (roundCount < 30) {
            return true;
        }
        Log.d("StreamingOptimizationExecutor", "Max rounds per hour reached: " + roundCount);
        return false;
    }

    private void startOptimization1(int i, int i2, int i3) {
        if (MiuiPhoneUtils.isDuringCall()) {
            Log.d("StreamingOptimizationExecutor", "Cannot start optimization 1 during call");
            return;
        }
        Log.i("StreamingOptimizationExecutor", "Starting optimization 1, networkScore=" + i + ", pci=" + i3);
        this.mOptStateMgr.startNewRound(i, i3);
        long servingCellId = getServingCellId(i2);
        this.mOptStateMgr.getAuxState().setLastCellId(servingCellId);
        StreamingOptimizationController.localLog("startOpt1", "slotId=" + i2 + ", score=" + i + ", cellId=" + TelephonyUtils.pii(String.valueOf(servingCellId)));
        if (this.mOptStateMgr.getAuxState().getLastQoeReason() == 3) {
            handleLowBandwidthCongestion(i2);
        } else {
            this.mCallback.executeDataStallEscape(i2);
        }
        this.mStats.onOpt1Executed();
        this.mWorkHandler.removeMessages(0);
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(0, i2, 0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    private void startScoreCollectionWindow(int i) {
        this.mOptStateMgr.getAuxState().clearCollectedScores();
        this.mOptStateMgr.getAuxState().setCollectingScores(true);
        this.mWorkHandler.removeMessages(11);
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(11, i, 0), 10000L);
        StreamingOptimizationController.localLog("scoreWindow", "slotId=" + i + ", lastScore=" + this.mOptStateMgr.getAuxState().getLastNetworkScore());
        Log.i("StreamingOptimizationExecutor", "Score collection window started (10000ms)");
    }

    public void evaluateCollectedScores(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !this.mOptStateMgr.getAuxState().isChecking()) {
            this.mOptStateMgr.getAuxState().setCollectingScores(false);
            this.mOptStateMgr.getAuxState().clearCollectedScores();
            return;
        }
        this.mOptStateMgr.getAuxState().setCollectingScores(false);
        List collectedScores = this.mOptStateMgr.getAuxState().getCollectedScores();
        List collectedTimestamps = this.mOptStateMgr.getAuxState().getCollectedTimestamps();
        int lastNetworkScore = this.mOptStateMgr.getAuxState().getLastNetworkScore();
        if (collectedScores.isEmpty()) {
            Log.i("StreamingOptimizationExecutor", "evaluateCollectedScores: no scores in 10s, waiting for 60s timeout");
            this.mOptStateMgr.getAuxState().clearCollectedScores();
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i2 = 0;
        long j = 0;
        long jIntValue = 0;
        while (i2 < collectedScores.size()) {
            int i3 = i2 + 1;
            long jLongValue = (i3 < collectedTimestamps.size() ? ((Long) collectedTimestamps.get(i3)).longValue() : jElapsedRealtime) - ((Long) collectedTimestamps.get(i2)).longValue();
            jIntValue += ((long) ((Integer) collectedScores.get(i2)).intValue()) * jLongValue;
            j += jLongValue;
            i2 = i3;
            collectedTimestamps = collectedTimestamps;
        }
        int iIntValue = j > 0 ? (int) (jIntValue / j) : ((Integer) collectedScores.get(collectedScores.size() - 1)).intValue();
        boolean z = iIntValue > lastNetworkScore;
        Log.i("StreamingOptimizationExecutor", "evaluateCollectedScores: scores=" + collectedScores + ", weightedAvg=" + iIntValue + ", totalDuration=" + j + "ms, baseline=" + lastNetworkScore + ", isEffective=" + z + ", state=" + this.mOptStateMgr.getOptState());
        StringBuilder sb = new StringBuilder("scoreCount=");
        sb.append(collectedScores.size());
        sb.append(", avg=");
        sb.append(iIntValue);
        sb.append(", baseline=");
        sb.append(lastNetworkScore);
        sb.append(", effective=");
        sb.append(z);
        StreamingOptimizationController.localLog("evalScores", sb.toString());
        this.mWorkHandler.removeMessages(0);
        this.mOptStateMgr.getAuxState().clearCollectedScores();
        handleEffectivenessResult(z, iIntValue, i, MiuiPhoneUtils.getPciByPhoneId(i));
    }

    private void handleLowBandwidthCongestion(int i) {
        Log.i("StreamingOptimizationExecutor", "handleLowBandwidthCongestion: slotId=" + i);
        if (!ModemProvider.IS_QCOM) {
            Log.d("StreamingOptimizationExecutor", "Not Qualcomm platform, use default strategy");
            this.mCallback.executeDataStallEscape(i);
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long coverageStateTime = this.mOptStateMgr.getAuxState().getCoverageStateTime();
        if (coverageStateTime > 0 && jElapsedRealtime - coverageStateTime < 120000) {
            Log.d("StreamingOptimizationExecutor", "Coverage state interval too short, use previous strategy");
            this.mCallback.executeDataStallEscape(i);
            return;
        }
        if (!this.mOptStateMgr.getSceneContext().isDynamicScene()) {
            Log.d("StreamingOptimizationExecutor", "Not in dynamic scene, use previous strategy");
            this.mCallback.executeDataStallEscape(i);
            return;
        }
        Log.i("StreamingOptimizationExecutor", "Opening coverage state for low bandwidth congestion in dynamic scene");
        Handler handler = this.mWorkHandler;
        handler.sendMessage(handler.obtainMessage(5, i, 0));
        this.mOptStateMgr.getAuxState().setCoverageStateTime(jElapsedRealtime);
        this.mWorkHandler.removeMessages(6);
        Handler handler2 = this.mWorkHandler;
        handler2.sendMessageDelayed(handler2.obtainMessage(6, i, 0), 2000L);
    }

    private void tryStartScoreCollectionOnCellChange(int i, int i2) {
        if (this.mOptStateMgr.getAuxState().isChecking()) {
            long lastCellId = this.mOptStateMgr.getAuxState().getLastCellId();
            long servingCellId = getServingCellId(i);
            boolean z = false;
            boolean z2 = (lastCellId == Long.MIN_VALUE || servingCellId == Long.MIN_VALUE) ? false : true;
            if (z2 && lastCellId != servingCellId) {
                z = true;
            }
            if (!z2) {
                Log.d("StreamingOptimizationExecutor", "tryStartScoreCollectionOnCellChange: cellId invalid, lastCellId=" + TelephonyUtils.pii(String.valueOf(lastCellId)) + ", currentCellId=" + TelephonyUtils.pii(String.valueOf(servingCellId)) + ", waiting for 60s timeout");
                return;
            }
            if (z) {
                Log.i("StreamingOptimizationExecutor", "Cell change detected, starting 10s score collection window, slotId=" + i + ", lastCellId=" + TelephonyUtils.pii(String.valueOf(lastCellId)) + ", currentCellId=" + TelephonyUtils.pii(String.valueOf(servingCellId)) + ", state=" + this.mOptStateMgr.getOptState());
                if (this.mOptStateMgr.getOptState() == 1) {
                    this.mCallback.executeDataStallEscapeNormal(i);
                }
                this.mOptStateMgr.getAuxState().setLastCellId(servingCellId);
                startScoreCollectionWindow(i);
                this.mOptStateMgr.getAuxState().addCollectedScore(i2);
            }
        }
    }

    private void handleEffectivenessResult(boolean z, int i, int i2, int i3) {
        int lastNetworkScore = this.mOptStateMgr.getAuxState().getLastNetworkScore();
        if (this.mOptStateMgr.getOptState() == 1) {
            StreamingOptimizationController.localLog("opt1Result", "effective=" + z + ", score=" + i);
            StreamingOptimizationStats streamingOptimizationStats = this.mStats;
            if (z) {
                streamingOptimizationStats.onOpt1Effective(lastNetworkScore, i);
                this.mOptStateMgr.finishRound();
                return;
            }
            streamingOptimizationStats.onOpt1NotEffective(lastNetworkScore, i);
            Log.i("StreamingOptimizationExecutor", "Starting optimization 2, evaluatedScore=" + i);
            this.mCallback.resetDataStallEscape(i2);
            if (isJingHuLine()) {
                this.mOptStateMgr.finishRound();
                openDedicatedBearer(i2, i3);
                Log.i("StreamingOptimizationExecutor", "Opening dedicated bearer for JingHu line");
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (!MiuiPhoneUtils.isNrNwType(MiuiPhoneUtils.getDataRat(i2)) && !MiuiPhoneUtils.isNsaConnected(i2)) {
                Log.i("StreamingOptimizationExecutor", "LTE network, optimization 1 not effective, finish round");
                openDedicatedBearer(i2, i3);
                this.mOptStateMgr.finishRound();
                return;
            }
            if (jElapsedRealtime - this.mLastOpt2ExecutedTime < 600000) {
                Log.i("StreamingOptimizationExecutor", "OPT2 interval not reached, skip 5G downgrade. elapsed=" + (jElapsedRealtime - this.mLastOpt2ExecutedTime) + "ms");
                this.mOptStateMgr.finishRound();
                return;
            }
            long servingCellId = getServingCellId(i2);
            StreamingOptimizationController.localLog("startOpt2", "score=" + i + ", cellId=" + TelephonyUtils.pii(String.valueOf(servingCellId)));
            this.mLastOpt2ExecutedTime = jElapsedRealtime;
            this.mCallback.set5GNetworkCapability(i2, false);
            this.mOptStateMgr.switchToOpt2(i);
            if (servingCellId != Long.MIN_VALUE) {
                this.mOptStateMgr.getAuxState().setLastCellId(servingCellId);
            }
            this.mStats.onOpt2Executed();
            this.mWorkHandler.removeMessages(0);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(0, i2, 0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            return;
        }
        if (this.mOptStateMgr.getOptState() == 2) {
            StreamingOptimizationController.localLog("opt2Result", "effective=" + z + ", score=" + i);
            if (!z) {
                Log.i("StreamingOptimizationExecutor", "Optimization 2 not effective, opening dedicated bearer");
                this.mStats.onOpt2NotEffective(lastNetworkScore, i);
                openDedicatedBearer(i2, i3);
                this.mCallback.set5GNetworkCapability(i2, true);
            } else {
                this.mStats.onOpt2Effective(lastNetworkScore, i);
                processOpt2IsEffective(i2);
            }
            this.mOptStateMgr.finishRound();
        }
    }

    private boolean isJingHuLine() {
        return this.mOptStateMgr.getSceneContext().getHstLineName() != null && this.mOptStateMgr.getSceneContext().getHstLineName().contains("G21");
    }

    private void processOpt2IsEffective(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            Log.i("StreamingOptimizationExecutor", "Optimization 2 effective, waiting 1 minute before restoring 5G");
            this.mOptStateMgr.getAuxState().setWaitingToRestore5G(true);
            this.mWorkHandler.removeMessages(9);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(9, i, 0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        }
    }

    public void checkOptEffectiveness(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && this.mOptStateMgr.getAuxState().isChecking()) {
            StreamingOptimizationController.localLog("timeout60s", "optState=" + this.mOptStateMgr.getOptState());
            Log.i("StreamingOptimizationExecutor", "Optimization check timeout, state=" + this.mOptStateMgr.getOptState());
            this.mOptStateMgr.getAuxState().clearCollectedScores();
            this.mOptStateMgr.getAuxState().setCollectingScores(false);
            this.mWorkHandler.removeMessages(11);
            if (this.mOptStateMgr.getOptState() == 1) {
                this.mCallback.resetDataStallEscape(i);
                this.mOptStateMgr.finishRound();
            } else if (this.mOptStateMgr.getOptState() == 2) {
                if (this.mOptStateMgr.getAuxState().is5GDisabled()) {
                    this.mCallback.set5GNetworkCapability(this.mOptStateMgr.getSceneContext().getDdsSlot(), true);
                }
                this.mOptStateMgr.finishRound();
            }
        }
    }

    public void restore5GAfterOpt2(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (!this.mOptStateMgr.getAuxState().isWaitingToRestore5G()) {
                Log.d("StreamingOptimizationExecutor", "restore5GAfterOpt2: not waiting, skip");
                return;
            }
            Log.i("StreamingOptimizationExecutor", "restore5GAfterOpt2: 1 minute passed, restoring 5G");
            if (this.mOptStateMgr.getAuxState().is5GDisabled()) {
                this.mCallback.set5GNetworkCapability(i, true);
            }
            this.mOptStateMgr.getAuxState().setWaitingToRestore5G(false);
        }
    }

    private void cancelRestore5GWaiting(int i) {
        this.mWorkHandler.removeMessages(9);
        if (this.mOptStateMgr.getAuxState().is5GDisabled()) {
            Log.i("StreamingOptimizationExecutor", "cancelRestore5GWaiting: restoring 5G immediately");
            this.mCallback.set5GNetworkCapability(i, true);
        }
        this.mOptStateMgr.getAuxState().setWaitingToRestore5G(false);
    }

    private void openDedicatedBearer(int i, int i2) {
        if (MiuiPhoneUtils.isDataRoaming(i)) {
            Log.i("StreamingOptimizationExecutor", "openDedicatedBearer: data roaming is enabled, skip");
            return;
        }
        if (!this.mOptStateMgr.getSceneContext().isHighSpeedTrain()) {
            Log.i("StreamingOptimizationExecutor", "openDedicatedBearer: not in high speed train scene, skip");
            return;
        }
        Log.i("StreamingOptimizationExecutor", "openDedicatedBearer: slotId=" + i + ", in high speed train scene");
        Intent intent = new Intent("com.android.phone.intent.action.QOS_CONGESTION_ABNOMAL");
        intent.putExtra(a.b, 1001);
        intent.putExtra("subId", SubscriptionManagerEx.getDefault().getSubscriptionIdForSlot(i));
        intent.putExtra("pci", i2);
        intent.setPackage("com.xiaomi.hypercomm");
        this.mContext.sendBroadcast(intent);
    }

    private long getServingCellId(int i) {
        try {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone == null) {
                return Long.MIN_VALUE;
            }
            CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
            if (currentCellIdentity instanceof CellIdentityLte) {
                int ci = ((CellIdentityLte) currentCellIdentity).getCi();
                if (ci == Integer.MAX_VALUE) {
                    return Long.MIN_VALUE;
                }
                return ci;
            }
            if (currentCellIdentity instanceof CellIdentityNr) {
                long nci = ((CellIdentityNr) currentCellIdentity).getNci();
                if (nci == Long.MAX_VALUE) {
                    return Long.MIN_VALUE;
                }
                return nci;
            }
            return Long.MIN_VALUE;
        } catch (Exception e) {
            Log.e("StreamingOptimizationExecutor", "getServingCellId failed", e);
        }
    }

    public void dump(PrintWriter printWriter) {
        String str;
        printWriter.println("[Executor]");
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mLastOpt2ExecutedTime;
        long j = 600000 - jElapsedRealtime;
        StringBuilder sb = new StringBuilder("  lastOpt2Time=");
        sb.append(this.mLastOpt2ExecutedTime);
        sb.append(" (elapsed ");
        sb.append(jElapsedRealtime / 1000);
        sb.append("s, cooldown ");
        if (j > 0) {
            str = (j / 1000) + "s remaining";
        } else {
            str = "ready";
        }
        sb.append(str);
        sb.append(")");
        printWriter.println(sb.toString());
    }
}
