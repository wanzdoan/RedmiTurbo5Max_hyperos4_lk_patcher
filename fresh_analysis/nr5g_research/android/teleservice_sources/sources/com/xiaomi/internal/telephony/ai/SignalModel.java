package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.modem.OemHookAgent;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private static Context mContext;
    private static int mHookType;
    private static OemHookAgent mOemHookAgent;
    private float[][][] input;
    private ScheduledExecutorService mExecutor;
    private AiModelLoader mLoader;
    private float[][] output;
    private int xiaomi_ard_touch_count;
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
    private List mOneDayIndicatorList = new ArrayList();
    private int mForecastCount = 0;
    private int mOneDayIndicatorCount = 0;
    private long mLastBroadcastTime = 0;
    private boolean isSignalModelRun = false;
    private final Runnable forecast = new Runnable() { // from class: com.xiaomi.internal.telephony.ai.SignalModel.1
        @Override // java.lang.Runnable
        public void run() {
            if (SignalModel.this.makeInput().booleanValue()) {
                SignalModel.this.filterInput();
                long jNanoTime = System.nanoTime();
                SignalModel.this.mLoader.run(SignalModel.this.input, SignalModel.this.output);
                SignalModel.log(String.format("forecast time %d us", Long.valueOf((System.nanoTime() - jNanoTime) / 1000)));
                SignalModel signalModel = SignalModel.this;
                signalModel.mForecastRes = signalModel.output[0][0] >= SignalModel.this.output[0][1] ? "WEAK" : "STRONG";
                SignalModel.log("forecast res:" + SignalModel.this.mForecastRes);
                SignalModel.this.updateConfusionMatrix();
                SignalModel signalModel2 = SignalModel.this;
                signalModel2.updateBroadcastState(signalModel2.mForecastRes);
                SignalModel.this.mForecastCount++;
                for (int i = 0; i < 100; i++) {
                    ModemAiDataCollector.getInstance(SignalModel.mContext).appendDebugStringToFile(SignalModel.this.dataFileName, String.format("\n%d \t%.1f  \t%.1f  \t%.1f  \t%.1f", Integer.valueOf(SignalModel.this.mForecastCount), Float.valueOf(SignalModel.this.input[0][i][0]), Float.valueOf(SignalModel.this.input[0][i + 100][0]), Float.valueOf(SignalModel.this.input[0][i + 200][0]), Float.valueOf(SignalModel.this.input[0][i + MiRilHook.HOOK_ID_START_URC_BY_AT][0])));
                }
                ModemAiDataCollector.getInstance(SignalModel.mContext).appendDebugStringToFile(SignalModel.this.resFileName, String.format("\n%d \t%s", Integer.valueOf(SignalModel.this.mForecastCount), SignalModel.this.mForecastRes));
            }
        }
    };

    public SignalModel(Context context) {
        this.mLoader = null;
        if (mOemHookAgent == null) {
            mOemHookAgent = OemHookAgent.getHook();
        }
        if (mHookType == 0) {
            mHookType = OemHookAgent.getHookType();
        }
        mContext = context;
        this.mLoader = new AiModelLoader("signal_model", mContext);
    }

    private void initParameters() {
        String str = "" + new Timestamp(System.currentTimeMillis());
        String str2 = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10) + "_" + str.substring(11, 13) + str.substring(14, 16) + str.substring(17, 19);
        Class cls = Float.TYPE;
        this.input = (float[][][]) Array.newInstance((Class<?>) cls, 1, 400, 1);
        this.output = (float[][]) Array.newInstance((Class<?>) cls, 1, 2);
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
        this.dataFileName = "Signal_" + str2 + "_log.txt";
        this.resFileName = "Signal_" + str2 + "_Res.txt";
        this.indicatorFileName = "Signal_" + str2 + "_Indicator.txt";
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.dataFileName, "ForecastCount  \tRSRP  \tRSRQ  \tSINR  \tPCI_SWITCH");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.resFileName, "ForecastCount  \tRES");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, "ForecastCount  \ttp  \tfp  \ttn  \tfn  \tPrecision  \tRecall  \tAcc");
    }

    private void calculateIndicator() {
        log("calculateIndicator:");
        float f = this.tp;
        float f2 = this.tn;
        float f3 = this.fp;
        float f4 = this.fn;
        if (f + f2 + f3 + f4 != 400.0f) {
            return;
        }
        this.acc = ((f + f2) * 100.0f) / (((f2 + f) + f3) + f4);
        if (f + f3 == 0.0f) {
            log("calculateIndicator error:tp+fp=0");
        } else {
            this.precision = (f * 100.0f) / (f + f3);
        }
        float f5 = this.tp;
        float f6 = this.fn;
        if (f5 + f6 == 0.0f) {
            log("calculateIndicator error:tp+fn=0");
        } else {
            this.recall = (100.0f * f5) / (f5 + f6);
        }
        log(String.format("calculateIndicator: signalModel indicators: \n precision:%f,recall:%f,acc:%f", Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, String.format("\n%d \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f", Integer.valueOf(this.mForecastCount), Float.valueOf(this.tp), Float.valueOf(this.fp), Float.valueOf(this.tn), Float.valueOf(this.fn), Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        this.mOneDayIndicatorCount++;
        HashMap map = new HashMap();
        map.put("index", Integer.valueOf(this.mOneDayIndicatorCount));
        map.put("tp", Float.valueOf(this.tp));
        map.put("fp", Float.valueOf(this.fp));
        map.put("tn", Float.valueOf(this.tn));
        map.put("fn", Float.valueOf(this.fn));
        map.put("precision", Float.valueOf(this.precision));
        map.put("recall", Float.valueOf(this.recall));
        map.put("acc", Float.valueOf(this.acc));
        this.mOneDayIndicatorList.add(map);
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
        String str = this.lastForecastRes;
        if (str == "WEAK" && actualSignal == "WEAK") {
            this.tp += 1.0f;
            log("calculateIndicator:tp++");
            calculateIndicator();
        } else if (str != "WEAK" || actualSignal != "STRONG") {
            if (str != "STRONG" || actualSignal != "WEAK") {
                if (str == "STRONG" && actualSignal == "STRONG") {
                    this.tn += 1.0f;
                    log("calculateIndicator:tn++");
                    calculateIndicator();
                } else {
                    log("calculateIndicator error:lastForecastRes=" + this.lastForecastRes + ",actualSignal=" + actualSignal);
                }
            } else {
                this.fn += 1.0f;
                log("calculateIndicator:fn++");
                calculateIndicator();
            }
        } else {
            this.fp += 1.0f;
            log("calculateIndicator:fp++");
            calculateIndicator();
        }
        this.lastForecastRes = this.mForecastRes;
    }

    private String getActualSignal() {
        float f = 0.0f;
        for (int i = 70; i < 90; i++) {
            f += this.input[0][i][0];
        }
        return f / 20.0f <= -95.0f ? "WEAK" : "STRONG";
    }

    private void sendSignalBroadcast(String str) {
        log("sendSignalBroadcast:" + str);
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.modemai.signal.FORECAST_RES");
        intent.putExtra("FORECAST_RES", str);
        mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBroadcastState(String str) {
        log("broadcastState  newState: " + str);
        if (this.mLastBroadcastTime > 0 && SystemClock.elapsedRealtime() - this.mLastBroadcastTime <= 120000) {
            log("broadcastState: less than broadcast period time , skip broadcast");
            return;
        }
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = str;
            return;
        }
        if ("STRONG".equals(str) && "WEAK".equals(this.mlastState)) {
            sendSignalBroadcast(str);
            this.mLastBroadcastTime = SystemClock.elapsedRealtime();
            this.mlastState = str;
        } else if ("WEAK".equals(str) && "STRONG".equals(this.mlastState)) {
            sendSignalBroadcast(str);
            this.mLastBroadcastTime = SystemClock.elapsedRealtime();
            this.mlastState = str;
        } else if ("WEAK".equals(str) && "WEAK".equals(this.mlastState)) {
            log("keep weak");
        } else {
            log("keep strong");
        }
    }

    private Boolean parseMtkSignalInfo(String str) {
        log("parseMtkSignalInfo:");
        if (str.equals("")) {
            log("at resp is null, modem signal info may not ready.");
            return Boolean.FALSE;
        }
        log(str);
        String strSubstring = str.split("\n")[1].substring(17);
        String strSubstring2 = str.split("\n")[2].substring(17);
        String[] strArrSplit = strSubstring.split(",");
        String[] strArrSplit2 = strSubstring2.split(",");
        for (int i = 0; i < 50; i++) {
            this.input[0][i][0] = Float.parseFloat(strArrSplit[i]) - 150.0f;
            int i2 = i + 50;
            this.input[0][i2][0] = Float.parseFloat(strArrSplit2[i]) - 150.0f;
            int i3 = i + 100;
            this.input[0][i3][0] = Float.parseFloat(strArrSplit[i2]) - 40.0f;
            int i4 = i + 150;
            this.input[0][i4][0] = Float.parseFloat(strArrSplit2[i2]) - 40.0f;
            this.input[0][i + 200][0] = Float.parseFloat(strArrSplit[i3]) - 20.0f;
            this.input[0][i + 250][0] = Float.parseFloat(strArrSplit2[i3]) - 20.0f;
            this.input[0][i + MiRilHook.HOOK_ID_START_URC_BY_AT][0] = Float.parseFloat(strArrSplit[i4]);
            this.input[0][i + 350][0] = Float.parseFloat(strArrSplit2[i4]);
        }
        return Boolean.TRUE;
    }

    private Boolean parseQcomSignalInfo(ByteBuffer byteBuffer) {
        log("parseQcomSignalInfo:");
        if (byteBuffer == null) {
            log("ByteBuffer is null");
            return Boolean.FALSE;
        }
        byteBuffer.get();
        for (int i = 0; i < 100; i++) {
            float[][] fArr = this.input[0];
            int i2 = i + MiRilHook.HOOK_ID_START_URC_BY_AT;
            fArr[i2][0] = byteBuffer.get();
            this.input[0][i][0] = byteBuffer.get() - 156.0f;
            int i3 = i + 100;
            this.input[0][i3][0] = byteBuffer.get() - 43.0f;
            int i4 = i + 200;
            this.input[0][i4][0] = byteBuffer.get() - 23.0f;
            log(String.format("sample %d: RSRP=%f , RSRQ=%f , SINR=%f , PCI=%f ", Integer.valueOf(i), Float.valueOf(this.input[0][i][0]), Float.valueOf(this.input[0][i3][0]), Float.valueOf(this.input[0][i4][0]), Float.valueOf(this.input[0][i2][0])));
        }
        return Boolean.TRUE;
    }

    private Boolean getDataFromMTK() {
        String strOnSendAtCommandByMtkTelephonyManagerEx;
        if (mOemHookAgent != null) {
            log("on send at command by mtk telephony manager ex");
            mOemHookAgent = OemHookAgent.getHook();
            try {
                log("try");
                strOnSendAtCommandByMtkTelephonyManagerEx = mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,13,0,0");
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
                log("Error:" + e.getMessage() + "\n" + sb.toString());
                strOnSendAtCommandByMtkTelephonyManagerEx = "";
            }
            return parseMtkSignalInfo(strOnSendAtCommandByMtkTelephonyManagerEx);
        }
        log("mOemHookAgent is null");
        return Boolean.FALSE;
    }

    private Boolean getDataFromQCOM() {
        if (mOemHookAgent != null) {
            log("on send command by QCOM mirilhook");
            return parseQcomSignalInfo(mOemHookAgent.getSignalAiInfo());
        }
        return Boolean.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean makeInput() {
        log("on signal_model makeInput");
        int i = mHookType;
        if (i == 1) {
            log("makeInput mHookType is MTK");
            return getDataFromMTK();
        }
        if (i == 2) {
            log("makeInput mHookType is QCOM");
            return getDataFromQCOM();
        }
        log("makeInput failed, mHookType is unknown");
        return Boolean.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterInput() {
        log("In filterInput");
        float[] fArr = new float[100];
        float[] fArr2 = new float[100];
        float[] fArr3 = new float[100];
        for (int i = 0; i < 100; i++) {
            float[][] fArr4 = this.input[0];
            fArr[i] = fArr4[i][0];
            fArr2[i] = fArr4[i + 100][0];
            fArr3[i] = fArr4[i + 200][0];
        }
        float[] fArrMedianFilter = medianFilter(fArr, 5);
        float[] fArrMedianFilter2 = medianFilter(fArr2, 5);
        float[] fArrMedianFilter3 = medianFilter(fArr3, 5);
        if (fArrMedianFilter == null || fArrMedianFilter2 == null || fArrMedianFilter3 == null) {
            return;
        }
        for (int i2 = 0; i2 < 100; i2++) {
            float[][] fArr5 = this.input[0];
            fArr5[i2][0] = fArrMedianFilter[i2];
            fArr5[i2 + 100][0] = fArrMedianFilter2[i2];
            fArr5[i2 + 200][0] = fArrMedianFilter3[i2];
        }
    }

    public static float[] medianFilter(float[] fArr, int i) {
        float[] fArrCopyOfRange;
        log("In medianFilter");
        int length = fArr.length;
        if (i > length) {
            return null;
        }
        int i2 = i / 2;
        float[] fArr2 = new float[length];
        for (int i3 = 0; i3 < length; i3++) {
            if (i3 <= i2) {
                fArrCopyOfRange = Arrays.copyOfRange(fArr, 0, i3 + i2 + 1);
            } else {
                int i4 = i3 + i2;
                if (i4 >= length) {
                    fArrCopyOfRange = Arrays.copyOfRange(fArr, i3 - i2, length);
                } else {
                    fArrCopyOfRange = Arrays.copyOfRange(fArr, i3 - i2, i4 + 1);
                }
            }
            fArr2[i3] = median(fArrCopyOfRange);
        }
        return fArr2;
    }

    public static float median(float[] fArr) {
        int length = fArr.length;
        int i = length / 2;
        quickSort(fArr, 0, length - 1);
        if (length % 2 == 0) {
            return (fArr[i - 1] + fArr[i]) * 0.5f;
        }
        return fArr[i];
    }

    public static void quickSort(float[] fArr, int i, int i2) {
        float f;
        float f2 = fArr[((i2 - i) / 2) + i];
        int i3 = i;
        int i4 = i2;
        while (i3 <= i4) {
            while (fArr[i3] < f2) {
                i3++;
            }
            while (true) {
                f = fArr[i4];
                if (f <= f2) {
                    break;
                } else {
                    i4--;
                }
            }
            if (i3 <= i4) {
                float f3 = fArr[i3];
                fArr[i3] = f;
                fArr[i4] = f3;
                i3++;
                i4--;
            }
        }
        if (i < i4) {
            quickSort(fArr, i, i4);
        }
        if (i3 < i2) {
            quickSort(fArr, i3, i2);
        }
    }

    public void startRun(OemHookAgent oemHookAgent) {
        log("signal_model startRun");
        if (this.isSignalModelRun) {
            return;
        }
        mOemHookAgent = oemHookAgent;
        if (oemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK start data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,10,0,0");
        }
        initParameters();
        ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(4);
        this.mExecutor = scheduledExecutorServiceNewScheduledThreadPool;
        scheduledExecutorServiceNewScheduledThreadPool.scheduleAtFixedRate(this.forecast, 0L, 3000L, TimeUnit.MILLISECONDS);
        this.isSignalModelRun = true;
    }

    public void stopRun(OemHookAgent oemHookAgent) {
        log("signal_model stopRun");
        if (this.isSignalModelRun) {
            mOemHookAgent = oemHookAgent;
            if (oemHookAgent == null) {
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
    }

    public List getSignalModelOneDayIndicator() {
        List arrayList = new ArrayList();
        log(String.format("mOneDayIndicatorCount: %d", Integer.valueOf(this.mOneDayIndicatorCount)));
        if (this.mOneDayIndicatorCount != 0) {
            arrayList = (List) this.mOneDayIndicatorList.stream().map(new Function() { // from class: com.xiaomi.internal.telephony.ai.SignalModel$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return SignalModel.$r8$lambda$kqPQAsMqXqGQAaAFMEGHqOaFz2I((HashMap) obj);
                }
            }).collect(Collectors.toList());
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
        }
        printList(arrayList);
        return arrayList;
    }

    public static /* synthetic */ HashMap $r8$lambda$kqPQAsMqXqGQAaAFMEGHqOaFz2I(HashMap map) {
        return new HashMap(map);
    }

    private void printList(List list) {
        log("printList:");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            if (map == null) {
                log("null");
            } else if (map.isEmpty()) {
                log("{} (empty)");
            } else {
                log(map.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String str) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, str);
    }
}
