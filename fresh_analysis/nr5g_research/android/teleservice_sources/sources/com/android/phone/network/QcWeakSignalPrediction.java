package com.android.phone.network;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtException;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.VideoPreloadScene;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class QcWeakSignalPrediction extends ModemAiQoeController {
    private static QcWeakSignalPrediction mInstance;
    private static ModemQoeMetrics mModemQoeMetrics = ModemQoeMetrics.getInstance();
    private int WEAK_SIGNAL_BATCH_SIZE;
    private int WEAK_SIGNAL_FEATURE_COUNT;
    private int WEAK_SIGNAL_SEQUENCE_LENGTH;
    private boolean mActualWeakSignalFor6s;
    private ModemAiQoeController.EvaluationCriteria mCriteria;
    private String mCurStateDataType;
    private int mInputWeakSignalSeqCount;
    private boolean mLast2ActualWeakSignal;
    private boolean mLast2PredictionWeakSignal;
    private boolean mLast3ActualWeakSignal;
    private boolean mLast3PredictionWeakSignal;
    private boolean mLastActualWeakSignal;
    private boolean mLastPredictionWeakSignal;
    private long mLastReportModemAiDataTime;
    private long mLastVideoPreloadTime;
    private long mModemAiStartMonitorTime;
    private float[][][] mWeakSignalInputData;
    private float[][][] mWeakSignalStatsData;

    public static synchronized QcWeakSignalPrediction make() {
        try {
            if (mInstance == null) {
                mInstance = new QcWeakSignalPrediction();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private QcWeakSignalPrediction() {
        super("/system_ext/etc/weak_signal_v3.onnx");
        this.WEAK_SIGNAL_BATCH_SIZE = 1;
        this.WEAK_SIGNAL_SEQUENCE_LENGTH = 20;
        this.WEAK_SIGNAL_FEATURE_COUNT = 10;
        Class cls = Float.TYPE;
        this.mWeakSignalInputData = (float[][][]) Array.newInstance((Class<?>) cls, 1, 20, 10);
        this.mInputWeakSignalSeqCount = 0;
        this.mWeakSignalStatsData = (float[][][]) Array.newInstance((Class<?>) cls, this.WEAK_SIGNAL_BATCH_SIZE, 25, 11);
        this.mCriteria = new ModemAiQoeController.EvaluationCriteria(this);
        this.mLastPredictionWeakSignal = false;
        this.mLast2PredictionWeakSignal = false;
        this.mLast3PredictionWeakSignal = false;
        this.mLastActualWeakSignal = false;
        this.mLast2ActualWeakSignal = false;
        this.mLast3ActualWeakSignal = false;
        this.mActualWeakSignalFor6s = false;
        this.mLastVideoPreloadTime = 0L;
        this.mModemAiStartMonitorTime = 0L;
        this.mCurStateDataType = "";
        this.mLastReportModemAiDataTime = 0L;
        log("QcWeakSignalPrediction init");
        initWeakSignalModel();
    }

    private void initWeakSignalModel() {
        this.mInputWeakSignalSeqCount = 0;
        for (int i = 0; i < this.WEAK_SIGNAL_BATCH_SIZE; i++) {
            for (int i2 = 0; i2 < this.WEAK_SIGNAL_SEQUENCE_LENGTH; i2++) {
                for (int i3 = 0; i3 < this.WEAK_SIGNAL_FEATURE_COUNT; i3++) {
                    this.mWeakSignalInputData[i][i2][i3] = 1.0f;
                }
            }
        }
        for (int i4 = 0; i4 < this.WEAK_SIGNAL_BATCH_SIZE; i4++) {
            for (int i5 = 0; i5 < 25; i5++) {
                for (int i6 = 0; i6 < 11; i6++) {
                    this.mWeakSignalStatsData[i4][i5][i6] = 1.0f;
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
        if (message.what != 1) {
            return;
        }
        log("EVENT_RECORD_MODEMAI_DATA_TIMER");
        stopMonitorTimerForCollectData(true);
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        super.processServiceStateChangeEvent(i, serviceState);
        if (this.mIsRegsiterLteNr) {
            return;
        }
        resetInputWeakSignalCount(false);
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processMovementEnterChange() {
        super.processMovementEnterChange();
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processMovementExitChange() {
        super.processMovementExitChange();
        resetInputWeakSignalCount(true);
        recordModemAiWeakSignalMetrics();
        if (this.mModemAiStartMonitorTime != 0) {
            log("Exit point collection!");
            stopMonitorTimerForCollectData(false);
        }
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processDefaultDataSlotChange(int i) {
        if (i != this.mDdsPhoneId) {
            resetInputWeakSignalCount(false);
        }
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
    }

    @Override // com.android.phone.network.ModemAiQoeController
    protected void processModemQoeMetricsForPeriod() {
        super.processModemQoeMetricsForPeriod();
        if (this.mIsExistOnnxRuntimeEnv && updateWeakSignalInputData()) {
            runQoeAiModel();
        } else if (this.mModemAiStartMonitorTime != 0) {
            log("Exit point collection!");
            stopMonitorTimerForCollectData(false);
        }
    }

    private void normalizeArrayForLineN(float[][][] fArr, int i) {
        try {
            float[][] fArr2 = fArr[0];
            int length = fArr2.length;
            int length2 = fArr2[0].length;
            if (i > length) {
                log("normalizeArrayForLineN: all arrays must have the same length.");
                return;
            }
            for (float[][] fArr3 : fArr) {
                for (int i2 = 0; i2 < length2; i2++) {
                    int i3 = i - 2;
                    if (i3 >= 0) {
                        float[] fArr4 = fArr3[i - 1];
                        if (fArr4[i2] > 1.0E9f) {
                            fArr4[i2] = fArr3[i3][i2];
                        }
                    }
                }
            }
        } catch (ArithmeticException e) {
            resetInputWeakSignalCount(false);
            log("normalizeArrayForLineN ArithmeticException:" + e.toString());
        } catch (ArrayIndexOutOfBoundsException e2) {
            resetInputWeakSignalCount(false);
            log("normalizeArrayForLineN ArrayIndexOutOfBoundsException:" + e2.toString());
        } catch (Exception e3) {
            log("normalizeArrayForLineN Exception:" + e3.toString());
        }
    }

    private float[] updateSignalInfoForDds() {
        if (!SubscriptionManager.isValidPhoneId(this.mDdsPhoneId) || this.mNetworkMetricsController == null) {
            log("updateSignalInfoForDds is invalid PhoneId");
            return null;
        }
        float rat = mModemQoeMetrics.getRat();
        float rsrp = mModemQoeMetrics.getRsrp();
        float rsrq = mModemQoeMetrics.getRsrq();
        float snr = mModemQoeMetrics.getSnr();
        int pci = mModemQoeMetrics.getPci();
        long cellId = mModemQoeMetrics.getCellId();
        long band = mModemQoeMetrics.getBand();
        float[] fArr = {rat, rsrp, rsrq, snr, snr, snr, snr, pci, cellId, band};
        logD("updateSignalInfoForDds ddsPhoneId = " + this.mDdsPhoneId + ", rat = " + rat + ", rsrp = " + rsrp + ", rsrq = " + rsrq + ", snr = " + snr + ", cqi = , pci = " + pci + ", cellId = " + cellId + ", band = " + band);
        return fArr;
    }

    protected boolean runQoeAiModel() {
        if (this.mNetworkMetricsController != null && this.mIsExistOnnxRuntimeEnv) {
            try {
                log("runModemAiWeakSignalModel V3.0: input data = " + Arrays.deepToString(this.mWeakSignalInputData));
                float[][] fArr = (float[][]) this.mOnnxSession.run(Collections.singletonMap("input1", OnnxTensor.createTensor(this.mOnnxEnv, this.mWeakSignalInputData))).get(0).getValue();
                if (fArr != null) {
                    float[] fArr2 = fArr[0];
                    if (fArr2.length == 2) {
                        this.mWeakSignalStatsData[this.WEAK_SIGNAL_BATCH_SIZE - 1][24][10] = fArr2[1];
                        logD("mWeakSignalStatsData:" + Arrays.deepToString(this.mWeakSignalStatsData));
                        if (ModemAiQoeController.VDBG) {
                            float f = this.mTestOutput;
                            if (f != -1.0f) {
                                fArr[0][1] = f;
                            }
                        }
                        updatePredictionState(((double) fArr[0][1]) > 0.5d);
                        if (!this.mLastActualWeakSignal && this.mLastPredictionWeakSignal) {
                            tryDoNetworkOptimization(fArr[0][1]);
                        }
                        reportModemAiPerception(ModemAiQoeController.ModemAiScene.WEAK_SIGNAL_PREDICTION, fArr[0][1]);
                        log("runModemAiWeakSignalModel V3.0: output data = " + Arrays.deepToString(fArr) + " mTestOutput = " + this.mTestOutput);
                        return true;
                    }
                }
                log("runModemAiWeakSignalModel V3.0: error for outputValues");
                return false;
            } catch (OrtException e) {
                log("runModemAiWeakSignalModel V3.0: OrtException occurred");
                e.printStackTrace();
            } catch (Exception e2) {
                log("runModemAiWeakSignalModel V3.0: Exception occurred");
                e2.printStackTrace();
            }
        }
        return false;
    }

    private void updatePredictionState(boolean z) {
        this.mLast3PredictionWeakSignal = this.mLast2PredictionWeakSignal;
        this.mLast2PredictionWeakSignal = this.mLastPredictionWeakSignal;
        this.mLastPredictionWeakSignal = z;
    }

    private boolean isShouldReportStatsData(boolean z, boolean z2, boolean z3) {
        if (!isFirstMonitorCollect()) {
            logD("isShouldReportStatsData: in colloct monitor time");
            return false;
        }
        boolean z4 = true;
        if (z != z3 && shouldRandomTriggerStatsData(0.6d)) {
            this.mCurStateDataType = "STATE_CHANGE";
        } else if (z && z3 && !z2) {
            this.mCurStateDataType = "WEAK_PREDICTION_FAIL";
        } else if (!z && !z3 && z2) {
            this.mCurStateDataType = "STRONG_PREDICTION_FAIL";
        } else if (shouldRandomTriggerStatsData(0.01d)) {
            this.mCurStateDataType = "RANDOM_SAMPLE";
        } else {
            z4 = false;
        }
        if (z4) {
            log("isShouldReportStatsData type: " + this.mCurStateDataType);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (z4) {
            long j = this.mLastReportModemAiDataTime;
            if (j != 0 && jCurrentTimeMillis - j < 120000) {
                log("isShouldReportStatsData colddown period.");
                return false;
            }
        }
        return z4;
    }

    private void tryDoNetworkOptimization(float f) {
        if (isPreloadVideoWhiteList()) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j = this.mLastVideoPreloadTime;
            if (j != 0 && jCurrentTimeMillis - j < 120000) {
                log("tryDoNetworkOptimization skip : exist penalty time and do nothing!");
                return;
            }
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, 6000, -1, VideoPreloadScene.HST_WEAK_SIGNAL.getValue());
            }
            this.mLastVideoPreloadTime = System.currentTimeMillis();
        }
    }

    private boolean isPreloadVideoWhiteList() {
        logD("isPreloadVideoWhiteList topAppName:" + MiuiPhoneUtils.getTopAppName());
        return MiuiPhoneUtils.isTopAppForFeatureConfig("weak_signal_perception_optimization_white_list_default");
    }

    private void evaluationCriteriaUpdate(boolean z, boolean z2, boolean z3) {
        this.mCriteria.update(z, z2, z3);
        logD("evaluationCriteriaUpdate lastActual=" + z + " prediction=" + z2 + " actual=" + z3 + this.mCriteria.toString());
    }

    private void reportModemAiPerception(ModemAiQoeController.ModemAiScene modemAiScene, float f) {
        log("reportModemAiPerception modemAiSceneL:" + modemAiScene.getValue() + " result:" + f);
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.MODEM_AI_WEAKSIGNAL_CHANGED");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        intent.putExtra("modemAiScene", modemAiScene.getValue());
        intent.putExtra(a.L, f);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    private boolean updateWeakSignalInputData() {
        float[] fArrUpdateSignalInfoForDds = updateSignalInfoForDds();
        if (fArrUpdateSignalInfoForDds == null) {
            resetInputWeakSignalCount(false);
            return false;
        }
        this.mInputWeakSignalSeqCount++;
        logD("updateWeakSignalInputData mInputWeakSignalSeqCount=" + this.mInputWeakSignalSeqCount + " singalInfo: " + Arrays.toString(fArrUpdateSignalInfoForDds));
        for (int i = 0; i < this.WEAK_SIGNAL_BATCH_SIZE; i++) {
            try {
                for (int i2 = 1; i2 < this.WEAK_SIGNAL_SEQUENCE_LENGTH; i2++) {
                    float[][] fArr = this.mWeakSignalInputData[i];
                    System.arraycopy(fArr[i2], 0, fArr[i2 - 1], 0, this.WEAK_SIGNAL_FEATURE_COUNT);
                }
                for (int i3 = 1; i3 < 25; i3++) {
                    float[][] fArr2 = this.mWeakSignalStatsData[i];
                    System.arraycopy(fArr2[i3], 0, fArr2[i3 - 1], 0, 11);
                }
                System.arraycopy(fArrUpdateSignalInfoForDds, 0, this.mWeakSignalInputData[i][this.WEAK_SIGNAL_SEQUENCE_LENGTH - 1], 0, this.WEAK_SIGNAL_FEATURE_COUNT);
                System.arraycopy(this.mWeakSignalInputData[i][this.WEAK_SIGNAL_SEQUENCE_LENGTH - 1], 0, this.mWeakSignalStatsData[i][24], 0, this.WEAK_SIGNAL_FEATURE_COUNT);
            } catch (ArrayIndexOutOfBoundsException unused) {
                resetInputWeakSignalCount(false);
                log("updateWeakSignalInputData ArrayIndexOutOfBoundsException mInputArrays:" + Arrays.deepToString(this.mWeakSignalInputData));
                return false;
            } catch (NullPointerException unused2) {
                resetInputWeakSignalCount(false);
                log("updateWeakSignalInputData NullPointerException mInputArrays:" + Arrays.deepToString(this.mWeakSignalInputData));
                return false;
            }
        }
        normalizeArrayForLineN(this.mWeakSignalInputData, this.WEAK_SIGNAL_SEQUENCE_LENGTH);
        if (this.mInputWeakSignalSeqCount <= this.WEAK_SIGNAL_SEQUENCE_LENGTH) {
            logD("updateWeakSignalInputData count is less: mInputWeakSignalSeqCount:" + this.mInputWeakSignalSeqCount);
            return false;
        }
        boolean zIsActualWeakSignal = isActualWeakSignal(fArrUpdateSignalInfoForDds);
        updateActualState(zIsActualWeakSignal);
        boolean z = this.mLast2ActualWeakSignal || this.mLastActualWeakSignal || zIsActualWeakSignal;
        this.mActualWeakSignalFor6s = z;
        if (this.mInputWeakSignalSeqCount > this.WEAK_SIGNAL_SEQUENCE_LENGTH + 2) {
            evaluationCriteriaUpdate(this.mLast3ActualWeakSignal, this.mLast3PredictionWeakSignal, z);
            if (isShouldReportStatsData(this.mLast3ActualWeakSignal, this.mLast3PredictionWeakSignal, this.mActualWeakSignalFor6s)) {
                startMonitorTimerForCollectData();
            }
        }
        logD("updateWeakSignalInputData mInputArray:" + Arrays.deepToString(this.mWeakSignalInputData));
        return true;
    }

    private void updateActualState(boolean z) {
        this.mLast3ActualWeakSignal = this.mLast2ActualWeakSignal;
        this.mLast2ActualWeakSignal = this.mLastActualWeakSignal;
        this.mLastActualWeakSignal = z;
    }

    private boolean isFirstMonitorCollect() {
        Handler handler;
        return (this.mModemAiStartMonitorTime != 0 || (handler = this.mWorkHandler) == null || handler.hasMessages(1)) ? false : true;
    }

    private void startMonitorTimerForCollectData() {
        Handler handler = this.mWorkHandler;
        if (handler == null || !handler.hasMessages(1)) {
            log("startMonitorTimerForCollectData enter");
            this.mModemAiStartMonitorTime = SystemClock.elapsedRealtime();
            Handler handler2 = this.mWorkHandler;
            if (handler2 != null) {
                handler2.sendMessageDelayed(handler2.obtainMessage(1), 41000L);
            }
        }
    }

    private void stopMonitorTimerForCollectData(boolean z) {
        log("stopMonitorTimerForCollectData exit isRecord:" + z);
        Handler handler = this.mWorkHandler;
        if (handler != null && handler.hasMessages(1)) {
            this.mWorkHandler.removeMessages(1);
        }
        if (z) {
            recordModemAiWeakSignalData();
        }
        this.mModemAiStartMonitorTime = 0L;
    }

    private boolean isActualWeakSignal(float[] fArr) {
        if (fArr == null || fArr.length != this.WEAK_SIGNAL_FEATURE_COUNT) {
            return false;
        }
        if (this.mInputWeakSignalSeqCount <= this.WEAK_SIGNAL_SEQUENCE_LENGTH) {
            logD("isActualWeakSignal count is less: mInputWeakSignalSeqCount:" + this.mInputWeakSignalSeqCount);
            return false;
        }
        return fArr[1] < -110.0f || fArr[3] < 0.0f || fArr[2] < -16.0f;
    }

    private synchronized void resetInputWeakSignalCount(boolean z) {
        log("resetInputWeakSignalCount isCloseEnv=" + z + " mIsExistOnnxRuntimeEnv=" + this.mIsExistOnnxRuntimeEnv);
        this.mInputWeakSignalSeqCount = 0;
        if (z && this.mIsExistOnnxRuntimeEnv) {
            closeModemAiEnv();
        }
    }

    private void recordModemAiWeakSignalData() {
        log("recordModemAiWeakSignalStatistic mCurStateDataType=" + this.mCurStateDataType);
        long jCurrentTimeMillis = System.currentTimeMillis();
        HashMap map = new HashMap();
        map.put("time", Long.toString(jCurrentTimeMillis));
        map.put("inputStats_type", this.mCurStateDataType);
        map.put("inputStats_data", Arrays.deepToString(this.mWeakSignalStatsData));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modemAi_weaksignal", map);
        this.mLastReportModemAiDataTime = jCurrentTimeMillis;
    }

    private void recordModemAiWeakSignalMetrics() {
        log("recordModemAiWeakSignalMetrics mCriteria " + this.mCriteria.toString());
        long jCurrentTimeMillis = System.currentTimeMillis();
        HashMap map = new HashMap();
        map.put("time", Long.toString(jCurrentTimeMillis));
        map.put("evaluation_criteria", this.mCriteria.toString());
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modemAi_weaksignal", map);
        clearModemAiStatisticMetrics();
    }

    private void clearModemAiStatisticMetrics() {
        this.mCriteria.reset();
    }

    protected static void log(String str) {
        Log.d("QcWeakSignalPrediction", str);
    }

    protected static void logD(String str) {
        if (ModemAiQoeController.VDBG) {
            Log.d("QcWeakSignalPrediction", str);
        }
    }
}
