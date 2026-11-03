package com.android.phone.network;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtException;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class ModemQoePrediction extends ModemAiQoeController {
    private static ModemQoePrediction mInstance;
    private int MODEM_QOE_BATCH_SIZE;
    private int MODEM_QOE_FEATURE_COUNT;
    private int MODEM_QOE_FEATURE_COUNT_STATS;
    private int MODEM_QOE_SEQUENCE_LENGTH;
    private int MODEM_QOE_SEQUENCE_LENGTH_STATS;
    private final String MODEM_STATS_VALUES_ERROR;
    private ModemAiQoeController.EvaluationCriteria mCriteria;
    private String mCurStateDataType;
    private int mCurStatsArrayIndex;
    private int mInputModemQoeSeqCount;
    private boolean mIsVideoFront;
    private boolean mLastActualVideoStall;
    private long mLastCid;
    private long mLastMcsUpdateTime;
    private int mLastPredictionLevel;
    private float mLastPredictionResult;
    private int mLastQuality;
    private long mLastReportModemAiDataTime;
    private final Queue mMcsQueue;
    private long mModemAiStartMonitorTime;
    private float[][][] mModemQoeInputData;
    private ModemAiQoeController.RecoveryPolicyInfo mRecoveryPolicyInfo;
    private float mSecondLastPredictionResult;
    private float[][][] mStatsValues;
    private float mThirdLastPredictionResult;
    private long mVideoStallEndTime;
    private long mVideoStallStartTime;

    public static synchronized ModemQoePrediction getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new ModemQoePrediction();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private ModemQoePrediction() {
        super("/system_ext/etc/model_modem_0805.onnx");
        this.MODEM_QOE_BATCH_SIZE = 1;
        this.MODEM_QOE_SEQUENCE_LENGTH = 10;
        this.MODEM_QOE_FEATURE_COUNT = 30;
        this.MODEM_QOE_FEATURE_COUNT_STATS = 49;
        this.MODEM_QOE_SEQUENCE_LENGTH_STATS = 31;
        this.MODEM_STATS_VALUES_ERROR = "modem stats values error";
        Class cls = Float.TYPE;
        this.mModemQoeInputData = (float[][][]) Array.newInstance((Class<?>) cls, 1, 10, 30);
        this.mStatsValues = (float[][][]) Array.newInstance((Class<?>) cls, this.MODEM_QOE_BATCH_SIZE, this.MODEM_QOE_SEQUENCE_LENGTH_STATS, this.MODEM_QOE_FEATURE_COUNT_STATS);
        this.mCurStatsArrayIndex = 0;
        this.mInputModemQoeSeqCount = 0;
        this.mIsVideoFront = false;
        this.mLastQuality = 0;
        this.mVideoStallStartTime = 0L;
        this.mVideoStallEndTime = 0L;
        this.mLastActualVideoStall = false;
        this.mLastReportModemAiDataTime = 0L;
        this.mModemAiStartMonitorTime = 0L;
        this.mCriteria = new ModemAiQoeController.EvaluationCriteria(this);
        this.mRecoveryPolicyInfo = new ModemAiQoeController.RecoveryPolicyInfo(this);
        this.mCurStateDataType = "";
        this.mLastCid = 0L;
        this.mMcsQueue = new LinkedList();
        this.mLastMcsUpdateTime = 0L;
        initModemQoeModel();
        log("ModemQoePrediction init");
    }

    private void initModemQoeModel() {
        this.mInputModemQoeSeqCount = 0;
        for (int i = 0; i < this.MODEM_QOE_BATCH_SIZE; i++) {
            for (int i2 = 0; i2 < this.MODEM_QOE_SEQUENCE_LENGTH; i2++) {
                for (int i3 = 0; i3 < this.MODEM_QOE_FEATURE_COUNT; i3++) {
                    this.mModemQoeInputData[i][i2][i3] = 1.0f;
                }
            }
        }
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected boolean initModemAiEnv() {
        boolean zInitModemAiEnv = super.initModemAiEnv();
        if (zInitModemAiEnv) {
            log("initModemAiEnv: init modem model success");
        }
        return zInitModemAiEnv;
    }

    @Override // com.android.phone.network.ModemAiQoeController
    public void handleSubModelMessage(Message message) {
        logD("handleSubModelMessage event:" + message.what);
        int i = message.what;
        if (i == 12) {
            processVideoInfoEvent(message.arg1, message.arg2);
            return;
        }
        if (i != 13) {
            if (i != 1001) {
                return;
            }
            log("EVENT_RECORD_MODEMAI_DATA_TIMER");
            stopMonitorTimerForCollectData(true);
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mModemAiStartMonitorTime;
        if (j == 0 || jElapsedRealtime - j >= 2000.0d) {
            return;
        }
        this.mRecoveryPolicyInfo.updateRecoveryPolicyInfo(message.arg1, message.arg2, jElapsedRealtime);
        log("EVENT_RECOVERY_POLICY_CHANGED mRecoveryPolicyInfo:" + this.mRecoveryPolicyInfo.toString());
    }

    private void processVideoInfoEvent(int i, int i2) {
        log("processVideoInfoEvent video quality:" + i + " isVideoInFront:" + i2);
        if (i2 == 1) {
            this.mIsVideoFront = true;
        } else if (i2 == 0) {
            this.mIsVideoFront = false;
            this.mVideoStallStartTime = 0L;
        }
        int i3 = this.mLastQuality;
        if (i3 != i && this.mIsVideoFront) {
            calcVideoQualityDuration(i3, i);
        }
        if (i2 != -1) {
            this.mLastQuality = 1;
        }
        if (i != -1) {
            this.mLastQuality = i;
        }
    }

    private int getExistStallFlag(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        logD("getExistStallFlag mVideoStallEndTime:" + this.mVideoStallEndTime + " mVideoStallStartTime:" + this.mVideoStallStartTime + " curTime:" + jElapsedRealtime);
        long j = this.mVideoStallEndTime;
        if (j == 0) {
            return 1;
        }
        return (this.mVideoStallStartTime == 0 && jElapsedRealtime - j >= ((long) i)) ? 1 : 0;
    }

    private void calcVideoQualityDuration(int i, int i2) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        log("calcVideoQualityDuration lastQuality=" + i + " mVideoStallStartTime=" + this.mVideoStallStartTime);
        if (i == 1 && i2 == -1) {
            return;
        }
        if (i == 0 && this.mVideoStallStartTime != 0) {
            this.mVideoStallStartTime = 0L;
            this.mVideoStallEndTime = jElapsedRealtime;
        } else if (i == 1 && i2 == 0) {
            this.mVideoStallStartTime = jElapsedRealtime;
        }
        log("calcVideoQualityDuration mVideoStallStartTime=" + this.mVideoStallStartTime + " mVideoStallEndTime=" + this.mVideoStallEndTime);
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        super.processServiceStateChangeEvent(i, serviceState);
        if (this.mIsRegsiterLteNr) {
            return;
        }
        resetInputModemQoeCount(false);
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processMovementEnterChange() {
        super.processMovementEnterChange();
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processMovementExitChange() {
        super.processMovementExitChange();
        resetInputModemQoeCount(true);
        recordModemQoeMetrics();
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processModemQoeMetricsForPeriod() {
        super.processModemQoeMetricsForPeriod();
        if (this.mIsExistOnnxRuntimeEnv && ((this.mTestDataUpdate || this.mIsWhiteListForground) && updateModemQoeInputData())) {
            runQoeAiModel();
        } else if (this.mModemAiStartMonitorTime != 0) {
            log("processModemQoeMetricsForPeriod discontinuous data appears, exit collection!");
            stopMonitorTimerForCollectData(false);
        }
        processMcsExceptionModel();
    }

    protected void processMcsExceptionModel() {
        logD("processMcsExceptionModel");
        if (this.mModemQoeMetrics != null && isScreenOn()) {
            updateMcsQueueWithLatestValue();
            checkZeroValueThresholdAndNotify();
        } else {
            this.mMcsQueue.clear();
        }
    }

    private void updateMcsQueueWithLatestValue() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.mLastMcsUpdateTime >= 3000) {
            log("updateMcsQueueWithLatestValue clear mcsQue");
            this.mMcsQueue.clear();
        }
        if (this.mMcsQueue.size() == 5) {
            this.mMcsQueue.poll();
        }
        this.mMcsQueue.offer(Integer.valueOf(this.mModemQoeMetrics.getDlMcs() + (isUlSpeedNormal() ? 0 : 100000)));
        logD("processMcsExceptionModel mMcsQueue:" + this.mMcsQueue.toString());
        this.mLastMcsUpdateTime = jCurrentTimeMillis;
    }

    private void checkZeroValueThresholdAndNotify() {
        if (this.mMcsQueue.size() == 5) {
            Iterator it = this.mMcsQueue.iterator();
            int i = 0;
            while (it.hasNext()) {
                if (((Integer) it.next()).intValue() == 0) {
                    i++;
                }
            }
            if (i >= 4) {
                notifyMcsDropException();
                this.mMcsQueue.clear();
            }
        }
    }

    private void notifyMcsDropException() {
        logD("notifyMcsDropException");
        Intent intent = new Intent();
        intent.setAction("miui.intent.action.ACTION_MODEM_MCS_DROP_EXCEPTION");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    private boolean isUlSpeedNormal() {
        NetworkSpeedTracker networkSpeedTracker = this.mNetworkSpeedTracker;
        if (networkSpeedTracker == null) {
            return false;
        }
        long rateBps = networkSpeedTracker.getRateBpsInfo().getRateBps() / 1024;
        long updateTimes = this.mNetworkSpeedTracker.getRateBpsInfo().getUpdateTimes();
        logD("isUlSpeedNormal ulRateKBps=" + rateBps + " timeStamp=" + updateTimes);
        return rateBps > 1 && updateTimes > 0 && System.currentTimeMillis() - updateTimes < MirrorDesktopHelper.TIMEOUT_MILLIS;
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processDefaultDataSlotChange(int i) {
        super.processDefaultDataSlotChange(i);
        if (i != this.mDdsPhoneId) {
            resetInputModemQoeCount(false);
        }
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected synchronized void processForegroundInfoEvent() {
        super.processForegroundInfoEvent();
        if (!this.mIsWhiteListForground) {
            resetInputModemQoeCount(false);
        }
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processMobileDataChangeEvent() {
        super.processMobileDataChangeEvent();
        if (this.mIsMobileDataOn) {
            return;
        }
        resetInputModemQoeCount(false);
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processWifiDataChangeEvent() {
        super.processWifiDataChangeEvent();
        if (this.mIsWifiDataOn) {
            return;
        }
        resetInputModemQoeCount(false);
    }

    protected boolean runQoeAiModel() {
        if (this.mNetworkMetricsController != null && this.mIsExistOnnxRuntimeEnv) {
            try {
                logD("runModemAiModemQoeModel V1.0: input data = " + Arrays.deepToString(this.mModemQoeInputData));
                logD("runModemAiModemQoeModel V1.0: input mAppScene = " + Arrays.toString(this.mAppScene));
                OnnxTensor onnxTensorCreateTensor = OnnxTensor.createTensor(this.mOnnxEnv, this.mModemQoeInputData);
                OnnxTensor onnxTensorCreateTensor2 = OnnxTensor.createTensor(this.mOnnxEnv, this.mAppScene);
                HashMap map = new HashMap();
                map.put("input", onnxTensorCreateTensor);
                map.put("app", onnxTensorCreateTensor2);
                float[][] fArr = (float[][]) this.mOnnxSession.run(map).get(0).getValue();
                if (fArr != null) {
                    float[] fArr2 = fArr[0];
                    if (fArr2.length == 2) {
                        if (ModemAiQoeController.VDBG) {
                            float f = this.mTestOutput;
                            if (f != -1.0f) {
                                fArr2[1] = f;
                            }
                        }
                        this.mThirdLastPredictionResult = this.mSecondLastPredictionResult;
                        this.mSecondLastPredictionResult = this.mLastPredictionResult;
                        this.mLastPredictionResult = fArr2[1];
                        logD("runModemAiModemQoeModel mLastPredictionResult:" + this.mLastPredictionResult + " mSecondLastPredictionResult:" + this.mSecondLastPredictionResult + " mThirdLastPredictionResult:" + this.mThirdLastPredictionResult);
                        int iFromProbability = PredictionLevel.fromProbability(this.mLastPredictionResult);
                        if (this.mLastPredictionLevel != iFromProbability) {
                            reportModemAiPerception(ModemAiQoeController.ModemAiScene.MOVEMENT_MODEM_QOE_PREDICTION, this.mLastPredictionResult, this.mAppScene[0]);
                        }
                        logD("runModemAiModemQoeModel V1.0: output data = " + Arrays.deepToString(fArr) + " mLastPredictionLevel = " + this.mLastPredictionLevel + " curLevel = " + iFromProbability + " mTestOutput = " + this.mTestOutput);
                        this.mLastPredictionLevel = iFromProbability;
                        return true;
                    }
                }
                log("runModemAiModemQoeModel V1.0: error for outputValues");
                return false;
            } catch (OrtException e) {
                log("runModemAiModemQoeModel V1.0: OrtException occurred");
                e.printStackTrace();
            } catch (Exception e2) {
                log("runModemAiModemQoeModel V1.0: Exception occurred");
                e2.printStackTrace();
            }
        }
        return false;
    }

    private void evaluationCriteriaUpdate(boolean z, boolean z2, boolean z3) {
        this.mCriteria.update(z, z2, z3);
        logD("evaluationCriteriaUpdate lastActual=" + z + " prediction=" + z2 + " actual=" + z3 + " mCriteria:" + this.mCriteria.toString());
    }

    private boolean isShouldReportStatsData(boolean z, boolean z2, boolean z3) {
        if (!isFirstMonitorCollect()) {
            logD("isShouldReportStatsData: in colloct monitor time");
            return false;
        }
        boolean z4 = true;
        if (z3 != z2 && shouldRandomTriggerStatsData(0.5d)) {
            this.mCurStateDataType = "FAIL_SAMPLE_TYPE";
        } else if (shouldRandomTriggerStatsData(0.1d)) {
            this.mCurStateDataType = "RANDOM_SAMPLE";
        } else {
            z4 = false;
        }
        if (z4) {
            log("isShouldReportStatsData type: " + this.mCurStateDataType);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (!z4 || jCurrentTimeMillis - this.mLastReportModemAiDataTime >= 120000) {
            return z4;
        }
        log("isShouldReportStatsData colddown period.");
        return false;
    }

    private boolean isFirstMonitorCollect() {
        Handler handler;
        return (this.mModemAiStartMonitorTime != 0 || (handler = this.mWorkHandler) == null || handler.hasMessages(1001)) ? false : true;
    }

    private void updateStatsValues(int i, float[] fArr, float[] fArr2) {
        int i2 = this.mCurStatsArrayIndex + 1;
        this.mCurStatsArrayIndex = i2;
        if (i2 > this.MODEM_QOE_SEQUENCE_LENGTH_STATS - 1) {
            this.mCurStatsArrayIndex = 0;
        }
        System.arraycopy(fArr, 0, this.mStatsValues[i][this.mCurStatsArrayIndex], 0, fArr.length);
        System.arraycopy(fArr2, 0, this.mStatsValues[i][this.mCurStatsArrayIndex], fArr.length, fArr2.length);
    }

    private boolean updateModemQoeInputData() {
        int i;
        float[][][] fArr;
        float[] fArrUpdateModemQoeInfoForDds = updateModemQoeInfoForDds(this.mModemQoeMetrics);
        float[] fArr2 = {getExistStallFlag(2000), this.mIsVideoFront ? 1.0f : 0.0f};
        if (fArrUpdateModemQoeInfoForDds == null) {
            resetInputModemQoeCount(false);
            return false;
        }
        this.mInputModemQoeSeqCount++;
        logD("updateModemQoeInputData mInputModemQoeSeqCount=" + this.mInputModemQoeSeqCount + " modemInfo: " + Arrays.toString(fArrUpdateModemQoeInfoForDds) + " recordInfo:" + Arrays.toString(fArr2));
        for (int i2 = 0; i2 < this.MODEM_QOE_BATCH_SIZE; i2++) {
            try {
                int i3 = 1;
                while (true) {
                    i = this.MODEM_QOE_SEQUENCE_LENGTH;
                    fArr = this.mModemQoeInputData;
                    if (i3 >= i) {
                        break;
                    }
                    float[][] fArr3 = fArr[i2];
                    System.arraycopy(fArr3[i3], 0, fArr3[i3 - 1], 0, this.MODEM_QOE_FEATURE_COUNT);
                    i3++;
                }
                System.arraycopy(fArrUpdateModemQoeInfoForDds, 0, fArr[i2][i - 1], 0, this.MODEM_QOE_FEATURE_COUNT);
                updateStatsValues(i2, this.mModemQoeMetrics.getAllData(), fArr2);
            } catch (ArrayIndexOutOfBoundsException unused) {
                resetInputModemQoeCount(false);
                log("updateModemQoeInputData ArrayIndexOutOfBoundsException mInputArrays:" + Arrays.deepToString(this.mModemQoeInputData) + " mStatsValues:" + Arrays.deepToString(this.mStatsValues));
                return false;
            } catch (NullPointerException unused2) {
                resetInputModemQoeCount(false);
                log("updateModemQoeInputData NullPointerException mInputArrays:" + Arrays.deepToString(this.mModemQoeInputData) + " mStatsValues:" + Arrays.deepToString(this.mStatsValues));
                return false;
            }
        }
        if (this.mInputModemQoeSeqCount <= this.MODEM_QOE_SEQUENCE_LENGTH) {
            logD("updateModemQoeInputData count is less: mInputModemQoeSeqCount:" + this.mInputModemQoeSeqCount);
            return false;
        }
        boolean zIsActualVideoStall = isActualVideoStall();
        if (this.mInputModemQoeSeqCount > this.MODEM_QOE_SEQUENCE_LENGTH + 1) {
            evaluationCriteriaUpdate(this.mLastActualVideoStall, ((double) this.mThirdLastPredictionResult) > 0.5d, zIsActualVideoStall);
            if (isShouldReportStatsData(this.mLastActualVideoStall, ((double) this.mThirdLastPredictionResult) > 0.5d, zIsActualVideoStall)) {
                startMonitorTimerForCollectData();
            }
        }
        this.mLastActualVideoStall = zIsActualVideoStall;
        logD("updateModemQoeInputData mInputArray:" + Arrays.deepToString(this.mModemQoeInputData));
        logD("updateModemQoeInputData mStatsValues:" + Arrays.deepToString(this.mStatsValues));
        return true;
    }

    private boolean isActualVideoStall() {
        return getExistStallFlag(6000) == 0;
    }

    private float[] updateModemQoeInfoForDds(ModemQoeMetrics modemQoeMetrics) {
        long cellId = modemQoeMetrics.getCellId();
        float f = this.mLastCid == cellId ? 0.0f : 1.0f;
        this.mLastCid = cellId;
        return new float[]{plmnParseFloat(this.mPlmn), f, modemQoeMetrics.getDlEarfcn(), modemQoeMetrics.getUlEarfcn(), modemQoeMetrics.getBand(), modemQoeMetrics.getRat(), modemQoeMetrics.getRsrp(), modemQoeMetrics.getRsrq(), modemQoeMetrics.getDlBler(), modemQoeMetrics.getUlBler(), modemQoeMetrics.getDlMcs(), modemQoeMetrics.getUlMcs(), modemQoeMetrics.getLteTxPusch(), modemQoeMetrics.getLteMaxPower(), modemQoeMetrics.getNr5gTargetTxPower(), modemQoeMetrics.getNr5gMtplTx(), modemQoeMetrics.getNasRejectCause(), modemQoeMetrics.getCqi(), modemQoeMetrics.getRi(), modemQoeMetrics.getNumLayers(), modemQoeMetrics.getUlGrantRatio(), modemQoeMetrics.getPdcpDlNumMissPdu(), modemQoeMetrics.getPdcpDlNumDataPduRcv(), modemQoeMetrics.getNr5gPdcpUlTotalNumRxBytes(), modemQoeMetrics.getLtePdcpUlNumPdcpUiBufBytes(), modemQoeMetrics.getRachResult(), modemQoeMetrics.getRlcUlTotalTxPdus(), modemQoeMetrics.getRlcDlNumDataPdu(), modemQoeMetrics.getRlcDlNumDroppedPdu(), modemQoeMetrics.getSnr()};
    }

    public float plmnParseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException unused) {
            return -1.0f;
        }
    }

    private synchronized void resetInputModemQoeCount(boolean z) {
        try {
            log("resetInputModemQoeCount isCloseEnv=" + z + " mIsExistOnnxRuntimeEnv=" + this.mIsExistOnnxRuntimeEnv);
            this.mInputModemQoeSeqCount = 0;
            if (z && this.mIsExistOnnxRuntimeEnv) {
                closeModemAiEnv();
            }
            if (this.mModemAiStartMonitorTime != 0) {
                stopMonitorTimerForCollectData(false);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void startMonitorTimerForCollectData() {
        Handler handler = this.mWorkHandler;
        if (handler == null || !handler.hasMessages(1001)) {
            log("startMonitorTimerForCollectData enter");
            this.mModemAiStartMonitorTime = SystemClock.elapsedRealtime();
            Handler handler2 = this.mWorkHandler;
            if (handler2 != null) {
                handler2.sendMessageDelayed(handler2.obtainMessage(1001), 33000L);
            }
        }
    }

    private void stopMonitorTimerForCollectData(boolean z) {
        log("stopMonitorTimerForCollectData exit isRecord:" + z);
        Handler handler = this.mWorkHandler;
        if (handler != null && handler.hasMessages(1001)) {
            this.mWorkHandler.removeMessages(1001);
        }
        if (z) {
            recordModemQoeData();
        }
        this.mModemAiStartMonitorTime = 0L;
    }

    private void reportModemAiPerception(ModemAiQoeController.ModemAiScene modemAiScene, float f, float f2) {
        log("reportModemAiPerception modemAiSceneL:" + modemAiScene.getValue() + " result:" + f + " appType:" + f2);
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.MODEM_AI_MODEMQOE_CHANGED");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        intent.putExtra("modemAiScene", modemAiScene.getValue());
        intent.putExtra(a.L, f);
        intent.putExtra("appType", f2);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    private void recordModemQoeData() {
        log("recordModemQoeData mCurStateDataType=" + this.mCurStateDataType);
        long jCurrentTimeMillis = System.currentTimeMillis();
        HashMap map = new HashMap();
        map.put("time", Long.toString(jCurrentTimeMillis));
        map.put("inputStats_type", "");
        map.put("recovery_policy", this.mRecoveryPolicyInfo.toString());
        map.put("foreground_scene_info", Arrays.toString(this.mAppScene));
        if (moveStatsValues()) {
            map.put("inputStats_data", Arrays.deepToString(this.mStatsValues));
        } else {
            map.put("inputStats_data", "modem stats values error");
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modemAi_modemqoe", map);
        this.mLastReportModemAiDataTime = jCurrentTimeMillis;
    }

    private boolean moveStatsValues() {
        int i = this.MODEM_QOE_FEATURE_COUNT_STATS;
        float[] fArr = new float[i];
        float[] fArr2 = new float[i];
        int i2 = this.MODEM_QOE_SEQUENCE_LENGTH_STATS;
        boolean[] zArr = new boolean[i2];
        int i3 = (i2 - 1) - this.mCurStatsArrayIndex;
        if (i3 == 0) {
            return true;
        }
        for (int i4 = 0; i4 < this.MODEM_QOE_BATCH_SIZE; i4++) {
            try {
                logD("mCurStatsArrayIndex =" + this.mCurStatsArrayIndex);
                logD("before move mStatsValues:" + Arrays.deepToString(this.mStatsValues));
                float[] fArr3 = this.mStatsValues[i4][0];
                int i5 = i3;
                int i6 = 0;
                while (i6 < this.MODEM_QOE_SEQUENCE_LENGTH_STATS) {
                    while (true) {
                        int i7 = this.MODEM_QOE_SEQUENCE_LENGTH_STATS;
                        if (i5 >= i7 || !zArr[i5]) {
                            break;
                        }
                        i5++;
                        int i8 = i5 - i3;
                        if (i8 <= 0) {
                            i8 = (i8 + i7) - 1;
                        }
                        fArr3 = this.mStatsValues[i4][i8];
                    }
                    logD("moveStatsValues saveIndex=" + i5);
                    float[][] fArr4 = this.mStatsValues[i4];
                    float[] fArr5 = fArr4[i5];
                    fArr4[i5] = fArr3;
                    zArr[i5] = true;
                    i5 += i3;
                    int i9 = this.MODEM_QOE_SEQUENCE_LENGTH_STATS;
                    if (i5 >= i9) {
                        i5 -= i9;
                    }
                    i6++;
                    fArr3 = fArr5;
                }
                logD("after move mStatsValues:" + Arrays.deepToString(this.mStatsValues));
            } catch (ArrayIndexOutOfBoundsException unused) {
                log("updateStatsValues ArrayIndexOutOfBoundsException mStatsValues:" + Arrays.deepToString(this.mStatsValues));
                return false;
            } catch (NullPointerException unused2) {
                log("updateStatsValues NullPointerException mStatsValues:" + Arrays.deepToString(this.mStatsValues));
                return false;
            }
        }
        this.mCurStatsArrayIndex = this.MODEM_QOE_SEQUENCE_LENGTH_STATS - 1;
        return true;
    }

    private void recordModemQoeMetrics() {
        log("recordModemQoeMetrics mCriteria " + this.mCriteria.toString());
        long jCurrentTimeMillis = System.currentTimeMillis();
        HashMap map = new HashMap();
        map.put("time", Long.toString(jCurrentTimeMillis));
        map.put("evaluation_criteria", this.mCriteria.toString());
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modemAi_modemqoe", map);
        clearModemQoeMetrics();
    }

    private void clearModemQoeMetrics() {
        this.mRecoveryPolicyInfo.clear();
        this.mCriteria.reset();
    }

    public enum PredictionLevel {
        LEVEL_1(1, 0.0f, 0.25f),
        LEVEL_2(2, 0.25f, 0.5f),
        LEVEL_3(3, 0.5f, 0.75f),
        LEVEL_4(4, 0.75f, 1.0f);

        private final int level;
        private final float max;
        private final float min;

        PredictionLevel(int i, float f, float f2) {
            this.level = i;
            this.min = f;
            this.max = f2;
        }

        public static int fromProbability(float f) {
            if (f < 0.0f || f > 1.0f) {
                ModemQoePrediction.log("PredictionLevel fromProbability: probability para is invalid!");
            }
            for (PredictionLevel predictionLevel : values()) {
                if (f >= predictionLevel.min && (f < predictionLevel.max || predictionLevel == LEVEL_4)) {
                    return predictionLevel.level;
                }
            }
            return LEVEL_1.level;
        }
    }

    protected static void log(String str) {
        Log.d("ModemQoePrediction", str);
    }

    protected static void logD(String str) {
        if (ModemAiQoeController.VDBG) {
            Log.d("ModemQoePrediction", str);
        }
    }
}
