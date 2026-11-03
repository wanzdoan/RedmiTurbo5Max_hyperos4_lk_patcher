package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.modem.OemHookAgent;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import miui.telephony.CommenUtils;

/* JADX INFO: loaded from: classes.dex */
public class SignalModel {
    private static final int BROADCAST_PERIOD_TIME_MILLISECOND = 120000;
    public static final int HOOK_TYPE_MTK = 1;
    public static final int HOOK_TYPE_QCOM = 2;
    public static final int HOOK_TYPE_UNKNOW = 0;
    private static final String LOG_TAG = "signalModel";
    public static final int MTK_GET_SIGNAL_INFO_BY_AT = 13;
    private static final int RUN_SIGNAL_AI_TIME_MILLIS = 3000;
    private static final int XIAOMI_ARD_MAX_TOUCH_COUNT = 3;
    private float[][][] input;
    private ScheduledExecutorService mExecutor;
    private AiModelLoader mLoader;
    private float[][] output;
    private int xiaomi_ard_touch_count;
    private static Context mContext = null;
    private static OemHookAgent mOemHookAgent = null;
    private static int mHookType = 0;
    private String mForecastRes = "NOT Ready";
    private String lastForecastRes = "NOT Ready";
    private String mlastState = "NOT Ready";
    private float tp = 0.0f;
    private float fp = 0.0f;
    private float tn = 0.0f;
    private float fn = 0.0f;
    private float precision = 0.0f;
    private float recall = 0.0f;
    private float acc = 0.0f;
    private String dataFileName = "Signal_log.txt";
    private String resFileName = "Signal_Res.txt";
    private String indicatorFileName = "Signal_Indicator.txt";
    private List<HashMap<String, Object>> mOneDayIndicatorList = new ArrayList();
    private int mForecastCount = 0;
    private int mOneDayIndicatorCount = 0;
    private long mLastBroadcastTime = 0;
    private boolean isSignalModelRun = false;
    private final Runnable forecast = new Runnable() { // from class: com.xiaomi.internal.telephony.ai.SignalModel.1
        @Override // java.lang.Runnable
        public void run() {
            Boolean makeInputRes = SignalModel.this.makeInput();
            if (makeInputRes.booleanValue()) {
                SignalModel.this.filterInput();
                long startTime = System.nanoTime();
                SignalModel.this.mLoader.run(SignalModel.this.input, SignalModel.this.output);
                long endTime = System.nanoTime();
                long runTime = (endTime - startTime) / 1000;
                SignalModel.log(String.format("forecast time %d us", Long.valueOf(runTime)));
                char c = 0;
                SignalModel.this.mForecastRes = SignalModel.this.output[0][0] >= SignalModel.this.output[0][1] ? "WEAK" : "STRONG";
                SignalModel.log("forecast res:" + SignalModel.this.mForecastRes);
                SignalModel.this.updateConfusionMatrix();
                SignalModel.this.updateBroadcastState(SignalModel.this.mForecastRes);
                SignalModel.this.mForecastCount++;
                int i = 0;
                while (i < 100) {
                    char c2 = c;
                    ModemAiDataCollector.getInstance(SignalModel.mContext).appendDebugStringToFile(SignalModel.this.dataFileName, String.format("\n%d \t%.1f  \t%.1f  \t%.1f  \t%.1f", Integer.valueOf(SignalModel.this.mForecastCount), Float.valueOf(SignalModel.this.input[c][i + 0][c]), Float.valueOf(SignalModel.this.input[c][i + 100][c]), Float.valueOf(SignalModel.this.input[c][i + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][c2]), Float.valueOf(SignalModel.this.input[c2][i + MiRilHook.HOOK_ID_START_URC_BY_AT][c2])));
                    i++;
                    c = c2;
                    makeInputRes = makeInputRes;
                }
                ModemAiDataCollector.getInstance(SignalModel.mContext).appendDebugStringToFile(SignalModel.this.resFileName, String.format("\n%d \t%s", Integer.valueOf(SignalModel.this.mForecastCount), SignalModel.this.mForecastRes));
            }
        }
    };

    public SignalModel(Context cnt) {
        this.mLoader = null;
        if (mOemHookAgent == null) {
            mOemHookAgent = OemHookAgent.getHook();
        }
        if (mHookType == 0) {
            mHookType = OemHookAgent.getHookType();
        }
        mContext = cnt;
        this.mLoader = new AiModelLoader("signal_model", mContext);
    }

    private void initParameters() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String temp = "" + timestamp;
        String time_Init = temp.substring(0, 4) + temp.substring(5, 7) + temp.substring(8, 10) + "_" + temp.substring(11, 13) + temp.substring(14, 16) + temp.substring(17, 19);
        this.input = (float[][][]) Array.newInstance((Class<?>) Float.TYPE, 1, CommenUtils.REASON_RECOVERY_FAIL_UNKNOWN, 1);
        this.output = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 1, 2);
        this.mForecastRes = "NOT Ready";
        this.lastForecastRes = "NOT Ready";
        this.mlastState = "NOT Ready";
        this.mLastBroadcastTime = 0L;
        this.mForecastCount = 0;
        this.tp = 0.0f;
        this.fp = 0.0f;
        this.tn = 0.0f;
        this.fn = 0.0f;
        this.precision = 0.0f;
        this.recall = 0.0f;
        this.acc = 0.0f;
        this.dataFileName = "Signal_" + time_Init + "_log.txt";
        this.resFileName = "Signal_" + time_Init + "_Res.txt";
        this.indicatorFileName = "Signal_" + time_Init + "_Indicator.txt";
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.dataFileName, "ForecastCount  \tRSRP  \tRSRQ  \tSINR  \tPCI_SWITCH");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.resFileName, "ForecastCount  \tRES");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, "ForecastCount  \ttp  \tfp  \ttn  \tfn  \tPrecision  \tRecall  \tAcc");
    }

    private void calculateIndicator() {
        log("calculateIndicator:");
        if (this.tp + this.tn + this.fp + this.fn != 400.0f) {
            return;
        }
        this.acc = ((this.tp + this.tn) * 100.0f) / (((this.tp + this.tn) + this.fp) + this.fn);
        if (this.tp + this.fp == 0.0f) {
            log("calculateIndicator error:tp+fp=0");
        } else {
            this.precision = (this.tp * 100.0f) / (this.tp + this.fp);
        }
        if (this.tp + this.fn == 0.0f) {
            log("calculateIndicator error:tp+fn=0");
        } else {
            this.recall = (this.tp * 100.0f) / (this.tp + this.fn);
        }
        log(String.format("calculateIndicator: signalModel indicators: \n precision:%f,recall:%f,acc:%f", Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, String.format("\n%d \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f", Integer.valueOf(this.mForecastCount), Float.valueOf(this.tp), Float.valueOf(this.fp), Float.valueOf(this.tn), Float.valueOf(this.fn), Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        this.mOneDayIndicatorCount++;
        HashMap<String, Object> tempMap = new HashMap<>();
        tempMap.put("index", Integer.valueOf(this.mOneDayIndicatorCount));
        tempMap.put("tp", Float.valueOf(this.tp));
        tempMap.put("fp", Float.valueOf(this.fp));
        tempMap.put("tn", Float.valueOf(this.tn));
        tempMap.put("fn", Float.valueOf(this.fn));
        tempMap.put("precision", Float.valueOf(this.precision));
        tempMap.put("recall", Float.valueOf(this.recall));
        tempMap.put("acc", Float.valueOf(this.acc));
        this.mOneDayIndicatorList.add(tempMap);
        printList(this.mOneDayIndicatorList);
        this.lastForecastRes = "NOT Ready";
        this.tp = 0.0f;
        this.fp = 0.0f;
        this.tn = 0.0f;
        this.fn = 0.0f;
        this.precision = 0.0f;
        this.recall = 0.0f;
        this.acc = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrix() {
        if (this.lastForecastRes.equals("NOT Ready")) {
            this.lastForecastRes = this.mForecastRes;
            return;
        }
        String actualSignal = getActualSignal();
        if (this.lastForecastRes == "WEAK" && actualSignal == "WEAK") {
            this.tp += 1.0f;
            log("calculateIndicator:tp++");
            calculateIndicator();
        } else if (this.lastForecastRes == "WEAK" && actualSignal == "STRONG") {
            this.fp += 1.0f;
            log("calculateIndicator:fp++");
            calculateIndicator();
        } else if (this.lastForecastRes == "STRONG" && actualSignal == "WEAK") {
            this.fn += 1.0f;
            log("calculateIndicator:fn++");
            calculateIndicator();
        } else if (this.lastForecastRes == "STRONG" && actualSignal == "STRONG") {
            this.tn += 1.0f;
            log("calculateIndicator:tn++");
            calculateIndicator();
        } else {
            log("calculateIndicator error:lastForecastRes=" + this.lastForecastRes + ",actualSignal=" + actualSignal);
        }
        this.lastForecastRes = this.mForecastRes;
    }

    private String getActualSignal() {
        if (70 + 20 > 100) {
            return "WaitMoreInfo";
        }
        float sum = 0.0f;
        for (int i = 70; i < 70 + 20; i++) {
            sum += this.input[0][i][0];
        }
        float average = sum / 20.0f;
        return average <= -95.0f ? "WEAK" : "STRONG";
    }

    private void sendSignalBroadcast(String res) {
        log("sendSignalBroadcast:" + res);
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.modemai.signal.FORECAST_RES");
        intent.putExtra("FORECAST_RES", res);
        mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBroadcastState(String newState) {
        log("broadcastState  newState: " + newState);
        if (this.mLastBroadcastTime > 0 && SystemClock.elapsedRealtime() - this.mLastBroadcastTime <= 120000) {
            log("broadcastState: less than broadcast period time , skip broadcast");
            return;
        }
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = newState;
            return;
        }
        if ("STRONG".equals(newState) && "WEAK".equals(this.mlastState)) {
            sendSignalBroadcast(newState);
            this.mLastBroadcastTime = SystemClock.elapsedRealtime();
            this.mlastState = newState;
        } else if ("WEAK".equals(newState) && "STRONG".equals(this.mlastState)) {
            sendSignalBroadcast(newState);
            this.mLastBroadcastTime = SystemClock.elapsedRealtime();
            this.mlastState = newState;
        } else if ("WEAK".equals(newState) && "WEAK".equals(this.mlastState)) {
            log("keep weak");
        } else {
            log("keep strong");
        }
    }

    private Boolean parseMtkSignalInfo(String result) {
        log("parseMtkSignalInfo:");
        if (result.equals("")) {
            log("at resp is null, modem signal info may not ready.");
            return false;
        }
        log(result);
        String data_s_1_200 = result.split("\n")[1].substring(17);
        String data_s_201_400 = result.split("\n")[2].substring(17);
        String[] data_s_1_200_array = data_s_1_200.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        String[] data_s_201_400_array = data_s_201_400.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        for (int i = 0; i < 50; i++) {
            this.input[0][i][0] = Float.parseFloat(data_s_1_200_array[i]) - 150.0f;
            this.input[0][i + 50][0] = Float.parseFloat(data_s_201_400_array[i]) - 150.0f;
            this.input[0][i + 100][0] = Float.parseFloat(data_s_1_200_array[i + 50]) - 40.0f;
            this.input[0][i + 150][0] = Float.parseFloat(data_s_201_400_array[i + 50]) - 40.0f;
            this.input[0][i + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][0] = Float.parseFloat(data_s_1_200_array[i + 100]) - 20.0f;
            this.input[0][i + CommenUtils.REFRESH_MBN_DELAY_TIMER][0] = Float.parseFloat(data_s_201_400_array[i + 100]) - 20.0f;
            this.input[0][i + MiRilHook.HOOK_ID_START_URC_BY_AT][0] = Float.parseFloat(data_s_1_200_array[i + 150]);
            this.input[0][i + 350][0] = Float.parseFloat(data_s_201_400_array[i + 150]);
        }
        return true;
    }

    private Boolean parseQcomSignalInfo(ByteBuffer result) {
        log("parseQcomSignalInfo:");
        if (result == null) {
            log("ByteBuffer is null");
            return false;
        }
        result.get();
        for (int i = 0; i < 100; i++) {
            this.input[0][i + MiRilHook.HOOK_ID_START_URC_BY_AT][0] = result.get();
            this.input[0][i + 0][0] = result.get() - 156.0f;
            this.input[0][i + 100][0] = result.get() - 43.0f;
            this.input[0][i + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][0] = result.get() - 23.0f;
            log(String.format("sample %d: RSRP=%f , RSRQ=%f , SINR=%f , PCI=%f ", Integer.valueOf(i), Float.valueOf(this.input[0][i + 0][0]), Float.valueOf(this.input[0][i + 100][0]), Float.valueOf(this.input[0][i + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][0]), Float.valueOf(this.input[0][i + MiRilHook.HOOK_ID_START_URC_BY_AT][0])));
        }
        return true;
    }

    private Boolean getDataFromMTK() {
        String atCmdResult = "";
        if (mOemHookAgent != null) {
            log("on send at command by mtk telephony manager ex");
            mOemHookAgent = OemHookAgent.getHook();
            try {
                log("try");
                atCmdResult = mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,13,0,0");
            } catch (Exception e) {
                StringBuilder stackTraceBuilder = new StringBuilder();
                for (StackTraceElement element : e.getStackTrace()) {
                    stackTraceBuilder.append(element.toString()).append("\n");
                }
                String stackTraceString = stackTraceBuilder.toString();
                log("Error:" + e.getMessage() + "\n" + stackTraceString);
            }
            return parseMtkSignalInfo(atCmdResult);
        }
        log("mOemHookAgent is null");
        return false;
    }

    private Boolean getDataFromQCOM() {
        if (mOemHookAgent != null) {
            log("on send command by QCOM mirilhook");
            ByteBuffer res = mOemHookAgent.getSignalAiInfo();
            return parseQcomSignalInfo(res);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean makeInput() {
        log("on signal_model makeInput");
        if (mHookType == 1) {
            log("makeInput mHookType is MTK");
            return getDataFromMTK();
        }
        if (mHookType == 2) {
            log("makeInput mHookType is QCOM");
            return getDataFromQCOM();
        }
        log("makeInput failed, mHookType is unknown");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterInput() {
        log("In filterInput");
        float[] mTmpRSRP = new float[100];
        float[] mTmpRSRQ = new float[100];
        float[] mTmpSINR = new float[100];
        for (int i = 0; i < 100; i++) {
            mTmpRSRP[i] = this.input[0][i + 0][0];
            mTmpRSRQ[i] = this.input[0][i + 100][0];
            mTmpSINR[i] = this.input[0][i + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][0];
        }
        float[] mTmpRSRP2 = medianFilter(mTmpRSRP, 5);
        float[] mTmpRSRQ2 = medianFilter(mTmpRSRQ, 5);
        float[] mTmpSINR2 = medianFilter(mTmpSINR, 5);
        if (mTmpRSRP2 == null || mTmpRSRQ2 == null || mTmpSINR2 == null) {
            return;
        }
        for (int i2 = 0; i2 < 100; i2++) {
            this.input[0][i2][0] = mTmpRSRP2[i2];
            this.input[0][i2 + 100][0] = mTmpRSRQ2[i2];
            this.input[0][i2 + SarDeviceStateListener.TYPE_SAVE_AND_BROADCAST_STATE][0] = mTmpSINR2[i2];
        }
    }

    public static float[] medianFilter(float[] array, int window) {
        float[] subarray;
        log("In medianFilter");
        int len = array.length;
        if (window > len) {
            return null;
        }
        int mid = window / 2;
        float[] result = new float[len];
        for (int i = 0; i < len; i++) {
            if (i <= mid) {
                subarray = Arrays.copyOfRange(array, 0, i + mid + 1);
            } else if (i + mid >= len) {
                subarray = Arrays.copyOfRange(array, i - mid, len);
            } else {
                subarray = Arrays.copyOfRange(array, i - mid, i + mid + 1);
            }
            result[i] = median(subarray);
        }
        return result;
    }

    public static float median(float[] array) {
        int len = array.length;
        int mid = len / 2;
        quickSort(array, 0, len - 1);
        if (len % 2 == 0) {
            return (array[mid - 1] + array[mid]) * 0.5f;
        }
        return array[mid];
    }

    public static void quickSort(float[] array, int low, int high) {
        int i = low;
        int j = high;
        float pivot = array[((high - low) / 2) + low];
        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                float exchange = array[i];
                array[i] = array[j];
                array[j] = exchange;
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSort(array, low, j);
        }
        if (i < high) {
            quickSort(array, i, high);
        }
    }

    public void startRun(OemHookAgent hookAgent) {
        log("signal_model startRun");
        if (this.isSignalModelRun) {
            return;
        }
        mOemHookAgent = hookAgent;
        if (mOemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK start data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,10,0,0");
        }
        initParameters();
        this.mExecutor = Executors.newScheduledThreadPool(4);
        this.mExecutor.scheduleAtFixedRate(this.forecast, 0L, 3000L, TimeUnit.MILLISECONDS);
        this.isSignalModelRun = true;
    }

    public void stopRun(OemHookAgent hookAgent) {
        log("signal_model stopRun");
        if (!this.isSignalModelRun) {
            return;
        }
        mOemHookAgent = hookAgent;
        if (mOemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK stop data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,11,0,0");
        }
        this.mExecutor.shutdown();
        this.isSignalModelRun = false;
    }

    public List<HashMap<String, Object>> getSignalModelOneDayIndicator() {
        List<HashMap<String, Object>> returnList = new ArrayList<>();
        log(String.format("mOneDayIndicatorCount: %d", Integer.valueOf(this.mOneDayIndicatorCount)));
        if (this.mOneDayIndicatorCount != 0) {
            returnList = (List) this.mOneDayIndicatorList.stream().map(new Function() { // from class: com.xiaomi.internal.telephony.ai.SignalModel$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return SignalModel.lambda$getSignalModelOneDayIndicator$0((HashMap) obj);
                }
            }).collect(Collectors.toList());
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
        }
        printList(returnList);
        return returnList;
    }

    static /* synthetic */ HashMap lambda$getSignalModelOneDayIndicator$0(HashMap map) {
        return new HashMap(map);
    }

    private void printList(List<HashMap<String, Object>> list) {
        log("printList:");
        for (HashMap<String, Object> innerMap : list) {
            if (innerMap == null) {
                log("null");
            } else if (innerMap.isEmpty()) {
                log("{} (empty)");
            } else {
                log(innerMap.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String msg) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, msg);
    }
}
