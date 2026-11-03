package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.android.internal.os.BackgroundThread;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.OemHookAgent;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import miui.telephony.CommenUtils;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public class GameModel {
    private static final int EVENT_GET_MODEM_DATA = 1;
    public static final int HOOK_TYPE_MTK = 1;
    public static final int HOOK_TYPE_QCOM = 2;
    public static final int HOOK_TYPE_UNKNOW = 0;
    private static final String LOG_TAG = "gameModel";
    public static final int MTK_GET_GAME_INFO_BY_AT = 13;
    private static final int RUN_GAME_AI_TIME_MILLIS = 1000;
    private static final float gameTh = 0.95f;
    private float[] input;
    private ScheduledExecutorService mExecutor;
    private AiModelLoader mLoader;
    private float[] output;
    private static Context mContext = null;
    private static OemHookAgent mOemHookAgent = null;
    private static int mHookType = 0;
    private final List<String> GAME_HAS_DELAY_LIST = Arrays.asList("com.tencent.tmgp.sgame", "com.tencent.jkchess", "com.tencent.lolm");
    private final List<String> GAME_NO_DELAY_LIST = Arrays.asList("com.netease.party", "com.netease.party.mi", "com.tencent.tmgp.dfm", "com.tencent.KiHan", "com.tencent.tmgp.cf", "com.tencent.mf.uam", "com.miHoYo.Yuanshen");
    private int mDelay = 0;
    private String mCurrentGame = "";
    private int mCurGameScene = 2;
    private String mForecastRes = "NOT Ready";
    private String lastForecastRes = "NOT Ready";
    private String mlastState = "NOT Ready";
    private String mLastTraditonStateForCnt = "NOT Ready";
    private String mlastAiState = "NOT Ready";
    private String mLastTraditonState = "NOT Ready";
    private String mAiModel = "AI";
    private String mTraditionModel = "TRADITIONAL";
    private int mForecastCount = 0;
    private int mOneDayIndicatorCount = 0;
    private int highCounter = 0;
    private int highCounterForTra = 0;
    private float tp = 0.0f;
    private float fp = 0.0f;
    private float tn = 0.0f;
    private float fn = 0.0f;
    private int tp_AiFor2S = 0;
    private int tn_AiFor2S = 0;
    private int fp_AiFor2S = 0;
    private int fn_AiFor2S = 0;
    private int lowCounter = 0;
    private int lowCounterForTra = 0;
    private int tpMixIn2s = 0;
    private int fpMixIn2s = 0;
    private int tnMixIn2s = 0;
    private int fnMixIn2s = 0;
    private int tpTradionnal = 0;
    private int fpTraditionnal = 0;
    private int tnTradionnal = 0;
    private int fnTradionnal = 0;
    private int tpTraditionalIn1S = 0;
    private int fpTraditionalIn1S = 0;
    private int tnTraditionalIn1S = 0;
    private int fnTraditionalIn1S = 0;
    private int tpTraditionalFor2S = 0;
    private int fpTraditionalFor2S = 0;
    private int tnTraditionalFor2S = 0;
    private int fnTraditionalFor2S = 0;
    private int tp_AiIn1S = 0;
    private int fp_AiIn1S = 0;
    private int tn_AiIn1S = 0;
    private int fn_AiIn1S = 0;
    private float precision = 0.0f;
    private float recall = 0.0f;
    private float acc = 0.0f;
    private int lastDelay = 0;
    private long lastDelayTime = 0;
    private int mCurRat = 0;
    int[] inputData = new int[180];
    int[] inputAiModelData = new int[100];
    private String dataFileName = "Game_log.txt";
    private String resFileName = "Game_Res.txt";
    private String indicatorFileName = "Game_Indicator.txt";
    private String delayFileName = "Game_Delay.txt";
    private String originalDataFileName = "Game_original.txt";
    private List<HashMap<String, Object>> mOneDayIndicatorList = new ArrayList();
    private ConcurrentHashMap<String, ArrayList> mOneDayGameMdParamList = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> tempGameDataMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> tempGameCountMap = new ConcurrentHashMap<>();
    private long lastTransitionToLagTime = 0;
    private long lastTransitionToNoLagTime = 0;
    private boolean isGameLagging = false;
    private boolean startCalculateLag = false;
    private boolean shouldForceLag = false;
    private boolean isGameModelRun = false;
    private final Runnable forecast = new Runnable() { // from class: com.xiaomi.internal.telephony.ai.GameModel.1
        @Override // java.lang.Runnable
        public void run() {
            int i;
            try {
                if (GameModel.this.makeInput().booleanValue()) {
                    long jNanoTime = System.nanoTime();
                    GameModel.this.forecastModel();
                    boolean zIsGameLaggingForTraditionalMethod = GameModel.this.new gameDataProcessor().isGameLaggingForTraditionalMethod(GameModel.this.inputData);
                    GameModel.log(String.format("forecast time %d us", Long.valueOf((System.nanoTime() - jNanoTime) / 1000)));
                    GameModel.this.mForecastRes = GameModel.this.parseGameAiOutput();
                    String str = "HIGH";
                    boolean z = true;
                    GameModel.log("forecast res: " + GameModel.this.mForecastRes + "  traditionalRes: " + zIsGameLaggingForTraditionalMethod + " mixRes: " + (zIsGameLaggingForTraditionalMethod && "HIGH".equals(GameModel.this.mForecastRes)));
                    if (GameModel.this.isHasDelayWhiteListGame().booleanValue()) {
                        GameModel.this.updateConfusionMatrix();
                        GameModel.this.getGamAiTpFor2S(GameModel.this.mForecastRes);
                        GameModel.this.getGameAiTpIn1S(GameModel.this.mForecastRes);
                        GameModel.this.updateConfusionMatrixForTraditionalModelIn1S(zIsGameLaggingForTraditionalMethod);
                        GameModel.this.updateConfusionMatrixForTraditionalModelFor2S(zIsGameLaggingForTraditionalMethod);
                    }
                    if (GameModel.this.isNoDelayWhiteListGame().booleanValue()) {
                        i = 0;
                        try {
                            GameModel.this.updateAiAndTraState(GameModel.this.mForecastRes, GameModel.this.mAiModel);
                            GameModel gameModel = GameModel.this;
                            if (!zIsGameLaggingForTraditionalMethod) {
                                str = "LOW";
                            }
                            gameModel.updateAiAndTraState(str, GameModel.this.mTraditionModel);
                        } catch (Exception e) {
                            e = e;
                        }
                    } else {
                        i = 0;
                    }
                    GameModel.this.mForecastCount++;
                    int i2 = 0;
                    while (i2 < 20) {
                        ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.dataFileName, String.format("\n%d \t%f  \t%f  \t%f  \t%f  \t%f ", Integer.valueOf(GameModel.this.mForecastCount), Float.valueOf(GameModel.this.input[i2]), Float.valueOf(GameModel.this.input[i2 + 20]), Float.valueOf(GameModel.this.input[i2 + 40]), Float.valueOf(GameModel.this.input[i2 + 60]), Float.valueOf(GameModel.this.input[i2 + 80])));
                        ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.originalDataFileName, String.format("\n%d \t%d  \t%d  \t%d  \t%d  \t%d", Integer.valueOf(GameModel.this.mForecastCount), Integer.valueOf(GameModel.this.inputAiModelData[i2]), Integer.valueOf(GameModel.this.inputAiModelData[i2 + 20]), Integer.valueOf(GameModel.this.inputAiModelData[i2 + 40]), Integer.valueOf(GameModel.this.inputAiModelData[i2 + 60]), Integer.valueOf(GameModel.this.inputAiModelData[i2 + 80])));
                        i2++;
                        z = z;
                    }
                    ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.resFileName, String.format("\n%d \t%s \t%f \t%f \t%d", Integer.valueOf(GameModel.this.mForecastCount), GameModel.this.mForecastRes, Float.valueOf(GameModel.this.output[i]), Float.valueOf(GameModel.this.output[z ? 1 : 0]), Integer.valueOf(GameModel.this.mDelay)));
                    ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.delayFileName, String.format("\n%d \t%d", Integer.valueOf(GameModel.this.mForecastCount), Integer.valueOf(GameModel.this.mDelay)));
                    return;
                }
                return;
            } catch (Exception e2) {
                e = e2;
                i = 0;
            }
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = e.getStackTrace();
            int length = stackTrace.length;
            for (int i3 = i; i3 < length; i3++) {
                sb.append(stackTrace[i3].toString()).append("\n");
            }
            GameModel.log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
    };
    private final Handler mHandler = new Handler(BackgroundThread.get().getLooper()) { // from class: com.xiaomi.internal.telephony.ai.GameModel.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int count = msg.arg1;
                    int size = GameModel.this.tempGameDataMap.size();
                    GameModel gameModel = GameModel.this;
                    if (size >= 3) {
                        ArrayList<ConcurrentHashMap> list = (ArrayList) gameModel.mOneDayGameMdParamList.get("MdParam");
                        if (list == null) {
                            list = new ArrayList<>();
                            GameModel.this.mOneDayGameMdParamList.put("MdParam", list);
                        }
                        ConcurrentHashMap<String, Object> gameDataMap = new ConcurrentHashMap<>(GameModel.this.tempGameDataMap);
                        gameDataMap.put("game", GameModel.this.mCurrentGame);
                        list.add(gameDataMap);
                        GameModel.this.tempGameDataMap.clear();
                    } else {
                        gameModel.tempGameDataMap.put("delayParam" + count, Arrays.toString(GameModel.this.inputData));
                        GameModel.this.mHandler.sendMessageDelayed(GameModel.this.mHandler.obtainMessage(1, Integer.valueOf(count + 1)), 1000L);
                    }
                    break;
            }
        }
    };

    public GameModel(Context cnt) {
        this.mLoader = null;
        if (mOemHookAgent == null) {
            mOemHookAgent = OemHookAgent.getHook();
        }
        if (mHookType == 0) {
            mHookType = OemHookAgent.getHookType();
        }
        mContext = cnt;
        this.mLoader = new AiModelLoader("game_model", mContext);
    }

    private void initParameters() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String temp = "" + timestamp;
        String time_Init = temp.substring(0, 4) + temp.substring(5, 7) + temp.substring(8, 10) + "_" + temp.substring(11, 13) + temp.substring(14, 16) + temp.substring(17, 19);
        this.input = new float[100];
        this.output = new float[2];
        this.mForecastRes = "NOT Ready";
        this.lastForecastRes = "NOT Ready";
        this.mlastState = "NOT Ready";
        this.mlastAiState = "NOT Ready";
        this.mLastTraditonState = "NOT Ready";
        this.mLastTraditonStateForCnt = "NOT Ready";
        this.mForecastCount = 0;
        this.highCounter = 0;
        this.highCounterForTra = 0;
        this.lowCounter = 0;
        this.lowCounterForTra = 0;
        this.lastDelay = 0;
        this.lastDelayTime = 0L;
        this.tp = 0.0f;
        this.fp = 0.0f;
        this.tn = 0.0f;
        this.fn = 0.0f;
        this.precision = 0.0f;
        this.recall = 0.0f;
        this.acc = 0.0f;
        initMetricsParameters();
        this.dataFileName = "Game_" + time_Init + "_log.txt";
        this.resFileName = "Game_" + time_Init + "_Res.txt";
        this.indicatorFileName = "Game_" + time_Init + "_Indicator.txt";
        this.delayFileName = "Game_" + time_Init + "_Delay.txt";
        this.originalDataFileName = "Game_" + time_Init + "_original.txt";
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.dataFileName, "ForecastCount  \tNR_UL_BLER  \tNR_DL_BLER  \tNR_Received_Grant_Bytes(Byte)/NR_Uplink_Pending_Data(Byte)   \tNL2_PDCP_DL_Stats  \tNR_RLC_Delay  ");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.resFileName, "ForecastCount  \tRES \tLow \tHigh \tDelay");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, "ForecastCount  \ttp  \tfp  \ttn  \tfn  \tPrecision  \tRecall  \tAcc");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.delayFileName, "ForecastCount   \tDelay");
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.originalDataFileName, "ForecastCount  \tNR_UL_BLER  \tNR_DL_BLER  \tNR_Received_Grant_Bytes(Byte)/NR_Uplink_Pending_Data(Byte)  \tNL2_PDCP_DL_Stats  \tNR_RLC_Delay  ");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forecastModel() {
        if (this.mLoader == null) {
            return;
        }
        log("forecastModel:");
        try {
            int[] inputShape = {1, 100, 1};
            int[] outputShape = {1, 2};
            for (int i = 0; i < inputShape.length; i++) {
                log("inputShape[" + i + "]:" + inputShape[i]);
            }
            for (int i2 = 0; i2 < outputShape.length; i2++) {
                log("outputShape[" + i2 + "]:" + outputShape[i2]);
            }
            TensorBuffer inputBuffer = TensorBuffer.createFixedSize(inputShape, DataType.FLOAT32);
            TensorBuffer outputBuffer = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32);
            inputBuffer.loadArray(this.input);
            this.mLoader.run(inputBuffer.getBuffer(), outputBuffer.getBuffer());
            String outputBufferString = bytesToHexString(outputBuffer.getBuffer().array());
            log("outputBufferString:\n" + outputBufferString);
            this.output = outputBuffer.getFloatArray();
        } catch (Exception e) {
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("0x%02x ", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    private Boolean parseMtkGameInfo(String result) {
        log("parseMtkGameInfo:");
        if (isHasDelayWhiteListGame().booleanValue() && !this.startCalculateLag) {
            log(" startCalculateLag false return ");
            return false;
        }
        if (result.equals("")) {
            log("at resp is null, modem game info may not ready.");
            return false;
        }
        log(result);
        String data_s_1_180 = result.split("\n")[1].trim();
        String[] data_s_1_180_array = data_s_1_180.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        if (data_s_1_180_array[1].equals("0")) {
            return false;
        }
        if (data_s_1_180_array[1].equals("3")) {
            this.shouldForceLag = true;
            log("shouldForceLag is true");
        }
        for (int i = 0; i < 20; i++) {
            try {
                this.inputData[i] = Integer.parseInt(data_s_1_180_array[i + 2].trim());
                this.inputAiModelData[i] = Integer.parseInt(data_s_1_180_array[i + 2].trim());
                this.inputData[i + 20] = Integer.parseInt(data_s_1_180_array[i + 22].trim());
                this.inputAiModelData[i + 20] = Integer.parseInt(data_s_1_180_array[i + 22].trim());
                int i2 = i / 10;
                int[] iArr = this.inputData;
                if (i2 >= 1) {
                    iArr[i + 40] = Integer.parseInt(data_s_1_180_array[43].trim());
                    this.inputAiModelData[i + 60] = Integer.parseInt(data_s_1_180_array[43].trim());
                    this.inputData[i + 60] = Integer.parseInt(data_s_1_180_array[45].trim());
                    this.inputData[i + 80] = Integer.parseInt(data_s_1_180_array[47].trim());
                    this.inputAiModelData[i + 80] = Integer.parseInt(data_s_1_180_array[47].trim());
                } else {
                    iArr[i + 40] = Integer.parseInt(data_s_1_180_array[42].trim());
                    this.inputAiModelData[i + 60] = Integer.parseInt(data_s_1_180_array[42].trim());
                    this.inputData[i + 60] = Integer.parseInt(data_s_1_180_array[44].trim());
                    this.inputData[i + 80] = Integer.parseInt(data_s_1_180_array[46].trim());
                    this.inputAiModelData[i + 80] = Integer.parseInt(data_s_1_180_array[46].trim());
                }
                this.inputData[i + 100] = Integer.parseInt(data_s_1_180_array[i + 48].trim());
                this.inputData[i + 120] = Integer.parseInt(data_s_1_180_array[i + 68].trim());
                int i3 = Integer.parseInt(data_s_1_180_array[i + 68].trim());
                int[] iArr2 = this.inputAiModelData;
                if (i3 == 0) {
                    iArr2[i + 40] = 2;
                } else {
                    iArr2[i + 40] = Integer.parseInt(data_s_1_180_array[i + 48].trim()) / Integer.parseInt(data_s_1_180_array[i + 68].trim());
                }
                this.inputData[i + 140] = Integer.parseInt(data_s_1_180_array[i + 88].trim());
                this.inputData[i + 160] = Integer.parseInt(data_s_1_180_array[i + 108].trim());
            } catch (Exception e) {
                StringBuilder stackTraceBuilder = new StringBuilder();
                for (StackTraceElement element : e.getStackTrace()) {
                    stackTraceBuilder.append(element.toString()).append("\n");
                }
                String stackTraceString = stackTraceBuilder.toString();
                log("Error:" + e.getMessage() + "\n" + stackTraceString);
            }
        }
        for (int i4 = 0; i4 < 100; i4++) {
            this.input[i4] = DataNormalizer.normalizeSingleValue(this.inputAiModelData[i4], i4 / 20);
        }
        log("input: " + Arrays.toString(this.input));
        log("parseMtkGameInfo:over");
        return true;
    }

    public class DataNormalizer {

        public static class NormalizationConfig {
            public static final float[] MEANS = {0.0f, 0.0f, 0.0f, 0.0f, 59.71711f};
            public static final float[] SCALES = {100.0f, 100.0f, 2.0f, 100.0f, 837.0f};
        }

        public DataNormalizer(GameModel this$0) {
        }

        public static float normalizeSingleValue(int dataValue, int index) {
            if (index < 0 || index > 4) {
                throw new IllegalArgumentException("索引必须在0-4之间");
            }
            float mean = NormalizationConfig.MEANS[index];
            float scale = NormalizationConfig.SCALES[index];
            float data = scale;
            if (dataValue < scale) {
                data = dataValue;
            }
            float res = (data - mean) / scale;
            return res;
        }
    }

    public class gameDataProcessor {
        public gameDataProcessor() {
        }

        static class DataRow {
            int finalFlag;
            int nrDlBler;
            int nrDlBlerFlag;
            int nrMacDlThroughput;
            int nrMacUlFlag;
            int nrPdcpDlFlag;
            int nrPdcpDlThroughput;
            int nrPdcpUlFlag;
            int nrPdcpUlThroughput;
            int nrReceivedGrantBytes;
            int nrRlcDelay;
            int nrRlcDelayFlag;
            int nrUlBler;
            int nrUlBlerFlag;
            int nrUplinkPendingData;
            int pdcpDlAbnormalityFlag;
            int pdcpDlAbnormalityRate;
            int tempFlagForMacUl;

            DataRow() {
            }

            public String toString() {
                return "DataRow{nrUlBler=" + this.nrUlBler + ", nrDlBler=" + this.nrDlBler + ", pdcpDlAbnormalityRate=" + this.pdcpDlAbnormalityRate + ", nrMacDlThroughput=" + this.nrMacDlThroughput + ", nrRlcDelay=" + this.nrRlcDelay + ", nrReceivedGrantBytes=" + this.nrReceivedGrantBytes + ", nrUplinkPendingData=" + this.nrUplinkPendingData + ", nrPdcpUlThroughput=" + this.nrPdcpUlThroughput + ", nrPdcpDlThroughput=" + this.nrPdcpDlThroughput + "}";
            }
        }

        public boolean isGameLaggingForTraditionalMethod(int[] inputData) {
            int i;
            int i2;
            List<DataRow> dataRows;
            if (inputData == null || inputData.length < 180) {
                return false;
            }
            GameModel.log("inputdata: " + Arrays.toString(inputData));
            List<DataRow> dataRows2 = new ArrayList<>();
            for (int i3 = 0; i3 < 20; i3++) {
                DataRow row = new DataRow();
                row.nrUlBler = inputData[i3];
                row.nrDlBler = inputData[i3 + 20];
                row.pdcpDlAbnormalityRate = inputData[i3 + 40];
                row.nrMacDlThroughput = inputData[i3 + 60];
                row.nrRlcDelay = inputData[i3 + 80];
                row.nrReceivedGrantBytes = inputData[i3 + 100];
                row.nrUplinkPendingData = inputData[i3 + 120];
                row.nrPdcpUlThroughput = inputData[i3 + 140];
                row.nrPdcpDlThroughput = inputData[i3 + 160];
                dataRows2.add(row);
            }
            Iterator<DataRow> it = dataRows2.iterator();
            while (true) {
                i = 1;
                if (!it.hasNext()) {
                    break;
                }
                DataRow row2 = it.next();
                if (row2.nrUplinkPendingData != 0 && row2.nrReceivedGrantBytes / row2.nrUplinkPendingData < 0.5d && row2.nrReceivedGrantBytes < 800) {
                    row2.tempFlagForMacUl = 1;
                } else {
                    row2.tempFlagForMacUl = 0;
                }
            }
            for (int i4 = 0; i4 < dataRows2.size(); i4++) {
                DataRow current = dataRows2.get(i4);
                GameModel.log("orig-: " + current);
                if (GameModel.this.isNoDelayWhiteListGame().booleanValue()) {
                    current.nrMacUlFlag = 0;
                } else if (i4 >= 10 - 1) {
                    int start = (i4 - 10) + 1;
                    int sumTemp = 0;
                    int sumPdcpZero = 0;
                    for (int j = start; j <= i4; j++) {
                        sumTemp += dataRows2.get(j).tempFlagForMacUl;
                        if (dataRows2.get(j).nrPdcpUlThroughput == 0) {
                            sumPdcpZero++;
                        }
                    }
                    current.nrMacUlFlag = (sumTemp < 6 || sumPdcpZero < 3) ? 0 : 1;
                } else {
                    current.nrMacUlFlag = 0;
                }
            }
            int gameNoLagCount = 0;
            for (DataRow row3 : dataRows2) {
                if (row3.nrReceivedGrantBytes < 300 && row3.nrRlcDelay < 150 && row3.nrUplinkPendingData < 200) {
                    if (row3.nrReceivedGrantBytes == 0 && row3.nrUplinkPendingData == 0) {
                        gameNoLagCount += 3;
                    } else {
                        gameNoLagCount++;
                    }
                }
            }
            int i5 = 0;
            while (true) {
                i2 = 4;
                if (i5 >= dataRows2.size()) {
                    break;
                }
                DataRow current2 = dataRows2.get(i5);
                if (GameModel.this.isNoDelayWhiteListGame().booleanValue() || (GameModel.this.isHasDelayWhiteListGame().booleanValue() && gameNoLagCount >= 4)) {
                    current2.nrPdcpUlFlag = 0;
                } else if (i5 >= 3 - 1) {
                    int start2 = (i5 - 3) + 1;
                    int i6 = 1;
                    for (int j2 = start2; j2 <= i5; j2++) {
                        if (dataRows2.get(j2).nrPdcpUlThroughput >= 500) {
                            i6 = 0;
                            break;
                        }
                    }
                    current2.nrPdcpUlFlag = i6;
                } else {
                    current2.nrPdcpUlFlag = 0;
                }
                i5++;
            }
            int i7 = 0;
            while (i7 < dataRows2.size()) {
                DataRow current3 = dataRows2.get(i7);
                if (GameModel.this.isNoDelayWhiteListGame().booleanValue() || (GameModel.this.isHasDelayWhiteListGame().booleanValue() && gameNoLagCount >= i2)) {
                    current3.nrPdcpDlFlag = 0;
                } else if (i7 >= 10 - 1) {
                    int start3 = (i7 - 10) + i;
                    int countDl = 0;
                    int j3 = start3;
                    while (j3 <= i7) {
                        int i8 = i;
                        if (dataRows2.get(j3).nrPdcpDlThroughput < 700) {
                            countDl++;
                        }
                        j3++;
                        i = i8;
                    }
                    i = i;
                    current3.nrPdcpDlFlag = (dataRows2.get(i7).nrPdcpDlThroughput == 0 || countDl >= 2) ? i : 0;
                } else {
                    i = i;
                    current3.nrPdcpDlFlag = 0;
                }
                i7++;
                i = i;
                i2 = 4;
            }
            int i9 = i;
            int i10 = 0;
            while (i10 < dataRows2.size()) {
                DataRow current4 = dataRows2.get(i10);
                if (i10 >= 10 - 1) {
                    int start4 = (i10 - 10) + 1;
                    int countUl = 0;
                    double sumUlDiv10 = 0.0d;
                    int j4 = start4;
                    while (j4 <= i10) {
                        if (dataRows2.get(j4).nrUlBler >= 70) {
                            countUl++;
                        }
                        sumUlDiv10 += (double) (dataRows2.get(j4).nrUlBler / 10);
                        j4++;
                        dataRows2 = dataRows2;
                    }
                    dataRows = dataRows2;
                    current4.nrUlBlerFlag = (countUl >= 8 || sumUlDiv10 >= 70.0d) ? i9 : 0;
                } else {
                    dataRows = dataRows2;
                    current4.nrUlBlerFlag = 0;
                }
                i10++;
                dataRows2 = dataRows;
            }
            List<DataRow> dataRows3 = dataRows2;
            int i11 = 10;
            int i12 = 0;
            while (i12 < dataRows3.size()) {
                List<DataRow> dataRows4 = dataRows3;
                DataRow current5 = dataRows4.get(i12);
                if (i12 >= 10 - 1) {
                    int start5 = (i12 - 10) + 1;
                    int countUl2 = 0;
                    double sumUlDiv11 = 0.0d;
                    for (int j5 = start5; j5 <= i12; j5++) {
                        if (dataRows4.get(j5).nrDlBler >= 60) {
                            countUl2++;
                        }
                        sumUlDiv11 += (double) (dataRows4.get(j5).nrDlBler / 10);
                    }
                    current5.nrDlBlerFlag = (countUl2 >= 7 || sumUlDiv11 >= 60.0d) ? i9 : 0;
                } else {
                    current5.nrDlBlerFlag = 0;
                }
                i12++;
                dataRows3 = dataRows4;
            }
            List<DataRow> dataRows5 = dataRows3;
            for (int i13 = 0; i13 < dataRows5.size(); i13++) {
                DataRow current6 = dataRows5.get(i13);
                if (i13 >= 10 + 4) {
                    int start6 = (i13 - 10) + 1;
                    int count = 0;
                    for (int j6 = start6; j6 <= i13; j6++) {
                        if (dataRows5.get(j6).nrRlcDelay > 180) {
                            count++;
                        }
                    }
                    current6.nrRlcDelayFlag = count >= 4 ? i9 : 0;
                } else {
                    current6.nrRlcDelayFlag = 0;
                }
            }
            for (int i14 = 0; i14 < dataRows5.size(); i14++) {
                DataRow current7 = dataRows5.get(i14);
                if (i14 >= 10 - 1) {
                    int start7 = (i14 - 10) + 1;
                    int count2 = 0;
                    for (int j7 = start7; j7 <= i14; j7++) {
                        if (dataRows5.get(j7).pdcpDlAbnormalityRate > 4) {
                            count2++;
                        }
                    }
                    current7.pdcpDlAbnormalityFlag = count2 >= 5 ? i9 : 0;
                } else {
                    current7.pdcpDlAbnormalityFlag = 0;
                }
            }
            boolean hasLowThroughput = false;
            Iterator<DataRow> it2 = dataRows5.iterator();
            while (it2.hasNext()) {
                if (it2.next().nrMacDlThroughput > 20480) {
                    hasLowThroughput = true;
                    break;
                }
            }
            int count3 = 0;
            int gameLagCount = 0;
            for (DataRow row4 : dataRows5) {
                row4.finalFlag = row4.nrMacUlFlag | row4.nrPdcpUlFlag | row4.nrPdcpDlFlag | row4.nrUlBlerFlag | row4.nrDlBlerFlag | row4.nrRlcDelayFlag | row4.pdcpDlAbnormalityFlag;
                GameModel.log("count: " + count3 + " finalFlag: " + row4.finalFlag + " nrMacUlFlag: " + row4.nrMacUlFlag + " nrPdcpUlFlag: " + row4.nrPdcpUlFlag + " nrPdcpDlFlag: " + row4.nrPdcpDlFlag + " nrUlBlerFlag: " + row4.nrUlBlerFlag + " nrRlcDelayFlag: " + row4.nrRlcDelayFlag + " nrDlBlerFlag: " + row4.nrDlBlerFlag + " pdcpDlAbnormalityFlag: " + row4.pdcpDlAbnormalityFlag);
                int i15 = i11;
                if (count3 >= i15 && row4.finalFlag == i9) {
                    gameLagCount++;
                }
                count3++;
                i11 = i15;
                i9 = 1;
            }
            GameModel.log("gameLagCount:" + gameLagCount + " gameNoLagCount:" + gameNoLagCount + " shouldForceLag: " + GameModel.this.shouldForceLag + " hasLowThroughput: " + hasLowThroughput);
            if (GameModel.this.shouldForceLag) {
                return true;
            }
            return gameLagCount >= 6 && !hasLowThroughput;
        }
    }

    private Boolean parseQcomGameInfo(ByteBuffer result) {
        log("parseQcomGameInfo:");
        if (result == null) {
            log("ByteBuffer is null");
            return false;
        }
        try {
            result.get();
            for (int i = 0; i < 4; i++) {
                this.input[i + 4] = result.get();
                this.input[i] = result.get();
                this.input[i + 16] = result.get();
                this.input[i + 20] = result.get();
                this.input[i + 32] = result.get();
                this.input[i + 40] = result.get();
                this.input[i + 12] = result.get();
                this.input[i + 8] = result.get();
                this.input[i + 24] = result.get();
                this.input[i + 28] = result.get();
                this.input[i + 36] = result.get();
                this.input[i + 44] = result.get();
            }
            log("parseQcomGameInfo:over");
        } catch (Exception e) {
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseGameAiOutput() {
        int res = 0;
        for (int i = 0; i < 2; i++) {
            log("output[" + i + "]:" + this.output[i]);
        }
        if (this.output[1] >= gameTh) {
            res = 1;
        }
        if (this.shouldForceLag) {
            log("parseGameAiOutput shouldForceLag");
            this.shouldForceLag = false;
            return "HIGH";
        }
        if (res == 0) {
            return "LOW";
        }
        return res == 1 ? "HIGH" : "NOT Ready";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAiAndTraState(String newState, String model) {
        log("updateAiAndTraState:  newState: " + newState + " model: " + model + " mlastAiState: " + this.mlastAiState + " mLastTraditonState: " + this.mLastTraditonState);
        if ("AI".equals(model)) {
            if ("NOT Ready".equals(this.mlastAiState)) {
                sendResToController(newState, "AI");
                this.mlastAiState = newState;
                return;
            }
            if ("LOW".equals(newState) && "HIGH".equals(this.mlastAiState)) {
                log("ai high to low");
                sendResToController(newState, "AI");
                this.mlastAiState = newState;
                return;
            } else {
                if ("HIGH".equals(newState) && "LOW".equals(this.mlastAiState)) {
                    log("ai low to high");
                    sendResToController(newState, "AI");
                    this.mlastAiState = newState;
                    return;
                }
                return;
            }
        }
        if ("TRADITIONAL".equals(model)) {
            if ("NOT Ready".equals(this.mLastTraditonState)) {
                this.mLastTraditonState = newState;
                sendResToController(newState, "TRADITIONAL");
                return;
            }
            if ("LOW".equals(newState) && "HIGH".equals(this.mLastTraditonState)) {
                log("tra high to low");
                sendResToController(newState, "TRADITIONAL");
                this.mLastTraditonState = newState;
            } else if ("HIGH".equals(newState) && "LOW".equals(this.mLastTraditonState)) {
                log("tra low to high");
                sendResToController(newState, "TRADITIONAL");
                this.mLastTraditonState = newState;
            }
        }
    }

    private void updateState(String newState) {
        log("updateState:");
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = newState;
            return;
        }
        if ("LOW".equals(newState) && "HIGH".equals(this.mlastState)) {
            sendResToController(newState, "AI");
            this.mlastState = newState;
            this.highCounter = 0;
        } else if ("HIGH".equals(newState) && "LOW".equals(this.mlastState)) {
            this.mlastState = newState;
            this.highCounter = 1;
        } else {
            if ("HIGH".equals(newState) && "HIGH".equals(this.mlastState)) {
                log("keep high");
                this.highCounter++;
                if (this.highCounter == 3) {
                    sendResToController(newState, "AI");
                    return;
                }
                return;
            }
            log("keep low");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getGamAiTpFor2S(String newState) {
        DelayState delayState = calculateDelayState();
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = newState;
            return;
        }
        if ("LOW".equals(newState) && "HIGH".equals(this.mlastState)) {
            this.mlastState = newState;
            this.highCounter = 0;
            this.lowCounter = 1;
            return;
        }
        if ("HIGH".equals(newState) && "LOW".equals(this.mlastState)) {
            this.mlastState = newState;
            this.highCounter = 1;
            this.lowCounter = 0;
            return;
        }
        if ("HIGH".equals(newState) && "HIGH".equals(this.mlastState)) {
            this.highCounter++;
            if (this.highCounter >= 2) {
                boolean z = delayState.isDelay;
                ConcurrentHashMap<String, Integer> concurrentHashMap = this.tempGameCountMap;
                if (z) {
                    concurrentHashMap.compute("tp_AiFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda0
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("getGamAiTpFor2S tp_AiFor2S ++");
                    return;
                } else {
                    concurrentHashMap.compute("fp_AiFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda1
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("getGamAiTpFor2S fp_AiFor2S++");
                    return;
                }
            }
            return;
        }
        if ("LOW".equals(newState) && "LOW".equals(this.mlastState)) {
            this.lowCounter++;
            log("keep low");
            if (this.lowCounter >= 2) {
                boolean z2 = delayState.isNotDelay;
                ConcurrentHashMap<String, Integer> concurrentHashMap2 = this.tempGameCountMap;
                if (!z2) {
                    concurrentHashMap2.compute("fn_AiFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda2
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("getGamAiTpFor2S fn_AiFor2S++");
                } else {
                    concurrentHashMap2.compute("tn_AiFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda3
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("getGamAiTpFor2S tn_AiFor2S++");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getGameAiTpIn1S(String newState) {
        DelayState delayState = calculateDelayState();
        long curTime = System.currentTimeMillis();
        if ("HIGH".equals(newState)) {
            boolean z = delayState.isDelay;
            ConcurrentHashMap<String, Integer> concurrentHashMap = this.tempGameCountMap;
            if (z) {
                concurrentHashMap.compute("tp_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda17
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        Integer num = (Integer) obj2;
                        return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                    }
                });
                log("tp_AiIn1S++ ");
                return;
            }
            concurrentHashMap.compute("fp_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda18
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("fp_AiIn1S++ ");
            ConcurrentHashMap<String, Object> fpMap = new ConcurrentHashMap<>();
            fpMap.put("fp", Arrays.toString(this.inputData));
            fpMap.put("game", this.mCurrentGame);
            fpMap.put("time", Long.valueOf(curTime));
            fpMap.put("delay", Integer.valueOf(this.mDelay));
            fpMap.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList<ConcurrentHashMap<String, Object>> fpParamList = this.mOneDayGameMdParamList.get("fpAiParam");
            if (fpParamList == null) {
                fpParamList = new ArrayList<>();
                this.mOneDayGameMdParamList.put("fpAiParam", fpParamList);
            }
            if (fpParamList.size() < 5) {
                fpParamList.add(fpMap);
            }
            log("calculateIndicator:fp++ couter=" + this.mForecastCount);
            return;
        }
        if ("LOW".equals(newState)) {
            boolean z2 = delayState.isNotDelay;
            ConcurrentHashMap<String, Integer> concurrentHashMap2 = this.tempGameCountMap;
            if (!z2) {
                concurrentHashMap2.compute("fn_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda19
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        Integer num = (Integer) obj2;
                        return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                    }
                });
                log("fn_AiIn1S++ ");
                ConcurrentHashMap<String, Object> fnMap = new ConcurrentHashMap<>();
                fnMap.put("game", this.mCurrentGame);
                fnMap.put("fn", Arrays.toString(this.inputData));
                fnMap.put("time", Long.valueOf(curTime));
                fnMap.put("delay", Integer.valueOf(this.mDelay));
                fnMap.put("rat", Integer.valueOf(this.mCurRat));
                ArrayList<ConcurrentHashMap<String, Object>> fnParamList = this.mOneDayGameMdParamList.get("fnAiParam");
                if (fnParamList == null) {
                    fnParamList = new ArrayList<>();
                    this.mOneDayGameMdParamList.put("fnAiParam", fnParamList);
                }
                if (fnParamList.size() < 5) {
                    fnParamList.add(fnMap);
                }
                log("calculateIndicator:fn++ counter=" + this.mForecastCount);
                return;
            }
            concurrentHashMap2.compute("tn_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda20
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("tn_AiIn1S++ ");
        }
    }

    private void sendResToController(String res, String model) {
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.modemai.game.FORECAST_RES");
        intent.putExtra("FORECAST_RES", res);
        intent.putExtra("FORECAST_MODEL", model);
        mContext.sendBroadcast(intent);
    }

    private void calculateIndicator() {
        log("calculateIndicator:");
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
        log(String.format("calculateIndicator: gameModel indicators: \n precision:%f,recall:%f,acc:%f", Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, String.format("\n%d  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f", Integer.valueOf(this.mForecastCount), Float.valueOf(this.tp), Float.valueOf(this.fp), Float.valueOf(this.tn), Float.valueOf(this.fn), Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
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
        tempMap.put("game", this.mCurrentGame);
        tempMap.put(CommenUtils.EXTRA_SCENE, Integer.valueOf(this.mCurGameScene));
        tempMap.put("tp_AiIn1S", Integer.valueOf(this.tp_AiIn1S));
        tempMap.put("fp_AiIn1S", Integer.valueOf(this.fp_AiIn1S));
        tempMap.put("tn_AiIn1S", Integer.valueOf(this.tn_AiIn1S));
        tempMap.put("fn_AiIn1S", Integer.valueOf(this.fn_AiIn1S));
        tempMap.put("tp_AiFor2S", Integer.valueOf(this.tp_AiFor2S));
        tempMap.put("fp_AiFor2S", Integer.valueOf(this.fp_AiFor2S));
        tempMap.put("fn_AiFor2S", Integer.valueOf(this.fn_AiFor2S));
        tempMap.put("tn_AiFor2S", Integer.valueOf(this.tn_AiFor2S));
        tempMap.put("tp_traditionnalIn1S", Integer.valueOf(this.tpTraditionalIn1S));
        tempMap.put("fp_traditionnalIn1S", Integer.valueOf(this.fpTraditionalIn1S));
        tempMap.put("tn_traditionnalIn1S", Integer.valueOf(this.tnTraditionalIn1S));
        tempMap.put("fn_traditionnalIn1S", Integer.valueOf(this.fnTraditionalIn1S));
        tempMap.put("tp_traditionnalFor2S", Integer.valueOf(this.tpTraditionalFor2S));
        tempMap.put("fp_traditionnalFor2S", Integer.valueOf(this.fpTraditionalFor2S));
        tempMap.put("tn_traditionnalFor2S", Integer.valueOf(this.tnTraditionalFor2S));
        tempMap.put("fn_traditionnalFor2S", Integer.valueOf(this.fnTraditionalFor2S));
        tempMap.put("curGameRat", Integer.valueOf(this.mCurRat));
        this.mOneDayIndicatorList.add(tempMap);
        printList(this.mOneDayIndicatorList);
        this.lastForecastRes = "NOT Ready";
        this.tp = 0.0f;
        this.fp = 0.0f;
        this.tn = 0.0f;
        this.fn = 0.0f;
        this.tp_AiFor2S = 0;
        this.fp_AiFor2S = 0;
        this.tn_AiFor2S = 0;
        this.fn_AiFor2S = 0;
        this.tpMixIn2s = 0;
        this.fpMixIn2s = 0;
        this.fnMixIn2s = 0;
        this.tnMixIn2s = 0;
        this.tpTradionnal = 0;
        this.tnTradionnal = 0;
        this.fpTraditionnal = 0;
        this.fnTradionnal = 0;
        this.tpTraditionalIn1S = 0;
        this.fpTraditionalIn1S = 0;
        this.tnTraditionalIn1S = 0;
        this.fnTraditionalIn1S = 0;
        this.tpTraditionalFor2S = 0;
        this.fpTraditionalFor2S = 0;
        this.tnTraditionalFor2S = 0;
        this.fnTraditionalFor2S = 0;
        this.tp_AiIn1S = 0;
        this.fp_AiIn1S = 0;
        this.tn_AiIn1S = 0;
        this.fn_AiIn1S = 0;
        this.precision = 0.0f;
        this.recall = 0.0f;
        this.acc = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrix() {
        String actualSignal = getActualSignal();
        log("actualSignal: " + actualSignal);
        System.currentTimeMillis();
        if ("HIGH".equals(this.mForecastRes) && "HIGH".equals(actualSignal)) {
            this.tempGameCountMap.compute("tp", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda13
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log(" updateConfusionMatrix tp+  " + this.tp);
            return;
        }
        if ("HIGH".equals(this.mForecastRes) && "LOW".equals(actualSignal)) {
            this.tempGameCountMap.compute("fp", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda14
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log(" updateConfusionMatrix fp+  " + this.fp);
            return;
        }
        if ("LOW".equals(this.mForecastRes) && "HIGH".equals(actualSignal)) {
            this.tempGameCountMap.compute("fn", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda15
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log(" updateConfusionMatrix fn+  " + this.fn);
        } else if ("LOW".equals(this.mForecastRes) && "LOW".equals(actualSignal)) {
            this.tempGameCountMap.compute("tn", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda16
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log(" updateConfusionMatrix tn+  " + this.tn);
        } else {
            log("calculateIndicator error:lastForecastRes=" + this.lastForecastRes + ",actualSignal=" + actualSignal);
        }
    }

    private void updateConfusionMatrixForMixModelIn2S(boolean mixRes) {
        String actualSignal = getActualSignal();
        long curTime = System.currentTimeMillis();
        boolean isDelay = false;
        boolean isNotDelay = false;
        log("lastTransitionToNoLagTime= " + this.lastTransitionToNoLagTime + "lastTransitionToLagTime=" + this.lastTransitionToLagTime + " curTime: " + curTime + "actualSignal: " + actualSignal);
        long j = this.lastTransitionToNoLagTime;
        long j2 = this.lastTransitionToLagTime;
        long j3 = this.lastTransitionToNoLagTime;
        if (j > j2) {
            if (curTime - j3 <= 3000) {
                isDelay = true;
            }
        } else if (j3 != 0 || this.lastTransitionToLagTime != 0) {
            isDelay = true;
        }
        if (this.lastTransitionToNoLagTime > this.lastTransitionToLagTime) {
            isNotDelay = true;
        } else if ((this.lastTransitionToNoLagTime == 0 && this.lastTransitionToLagTime == 0) || curTime - this.lastTransitionToLagTime <= 3000) {
            isNotDelay = true;
        }
        if (mixRes) {
            if (isDelay) {
                this.tpMixIn2s++;
                log("updateConfusionMatrixForMixModelIn2S:tp++ " + this.tpMixIn2s);
                return;
            } else {
                if (!isDelay) {
                    this.fpMixIn2s++;
                    log("updateConfusionMatrixForMixModelIn2S:fp++ " + this.fpMixIn2s);
                    return;
                }
                return;
            }
        }
        if (!isNotDelay) {
            this.fnMixIn2s++;
            log("updateConfusionMatrixForMixModelIn2S:fn++ " + this.fnMixIn2s);
        } else if (isNotDelay) {
            this.tnMixIn2s++;
            log("updateConfusionMatrixForMixModelIn2S:tn++ " + this.tnMixIn2s);
        }
    }

    private void updateConfusionMatrixForMixModel(boolean mixRes) {
        String actualSignal = getActualSignal();
        long cuTime = System.currentTimeMillis();
        if (mixRes && "LOW".equals(actualSignal)) {
            log("updateConfusionMatrixForMixModelfp++ couter=" + this.mForecastCount);
            ConcurrentHashMap<String, Object> fpMap = new ConcurrentHashMap<>();
            fpMap.put("fp", Arrays.toString(this.inputData));
            fpMap.put("game", this.mCurrentGame);
            fpMap.put("time", Long.valueOf(cuTime));
            ArrayList<ConcurrentHashMap<String, Object>> fpParamList = this.mOneDayGameMdParamList.get("fpMixParam");
            if (fpParamList == null) {
                fpParamList = new ArrayList<>();
                this.mOneDayGameMdParamList.put("fpMixParam", fpParamList);
            }
            if (fpParamList.size() < 20) {
                fpParamList.add(fpMap);
                return;
            }
            return;
        }
        if (!mixRes && "HIGH".equals(actualSignal)) {
            log("updateConfusionMatrixForMixModel:fn++ counter=" + this.mForecastCount);
            ConcurrentHashMap<String, Object> fnMap = new ConcurrentHashMap<>();
            fnMap.put("fn", Arrays.toString(this.inputData));
            fnMap.put("game", this.mCurrentGame);
            fnMap.put("time", Long.valueOf(cuTime));
            ArrayList<ConcurrentHashMap<String, Object>> fnParamList = this.mOneDayGameMdParamList.get("fnMixParam");
            if (fnParamList == null) {
                fnParamList = new ArrayList<>();
                this.mOneDayGameMdParamList.put("fnMixParam", fnParamList);
            }
            if (fnParamList.size() < 20) {
                fnParamList.add(fnMap);
            }
        }
    }

    private void updateConfusionMatrixForTraditionalModel(boolean traditionalRes) {
        log("updateConfusionMatrixForTraditionalModel:traditionalRes=" + traditionalRes);
        String actualSignal = getActualSignal();
        long cuTime = System.currentTimeMillis();
        if (traditionalRes && "HIGH".equals(actualSignal)) {
            this.tpTradionnal++;
            log("updateConfusionMatrixForTraditionalModel:tp++");
            return;
        }
        if (!traditionalRes || !"LOW".equals(actualSignal)) {
            if (!traditionalRes && "HIGH".equals(actualSignal)) {
                this.fnTradionnal++;
                log("updateConfusionMatrixForTraditionalModel:fn++");
                ConcurrentHashMap<String, Object> fnMap = new ConcurrentHashMap<>();
                fnMap.put("fn", Arrays.toString(this.inputData));
                fnMap.put("game", this.mCurrentGame);
                fnMap.put("time", Long.valueOf(cuTime));
                ArrayList<ConcurrentHashMap<String, Object>> fnParamList = this.mOneDayGameMdParamList.get("fnTraditionalParam");
                if (fnParamList == null) {
                    fnParamList = new ArrayList<>();
                    this.mOneDayGameMdParamList.put("fnTraditionalParam", fnParamList);
                }
                if (fnParamList.size() < 20) {
                    fnParamList.add(fnMap);
                    return;
                }
                return;
            }
            if (!traditionalRes && "LOW".equals(actualSignal)) {
                this.tnTradionnal++;
                log("updateConfusionMatrixForTraditionalModel:tn++");
                return;
            }
            return;
        }
        this.fpTraditionnal++;
        log("updateConfusionMatrixForTraditionalModel couter=" + this.mForecastCount);
        ConcurrentHashMap<String, Object> fpMap = new ConcurrentHashMap<>();
        fpMap.put("fp", Arrays.toString(this.inputData));
        fpMap.put("game", this.mCurrentGame);
        fpMap.put("time", Long.valueOf(cuTime));
        ArrayList<ConcurrentHashMap<String, Object>> fpParamList = this.mOneDayGameMdParamList.get("fpTraditionalParam");
        if (fpParamList == null) {
            fpParamList = new ArrayList<>();
            this.mOneDayGameMdParamList.put("fpTraditionalParam", fpParamList);
        }
        if (fpParamList.size() < 20) {
            fpParamList.add(fpMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrixForTraditionalModelIn1S(boolean traditionalRes) {
        log("lastTransitionToNoLagTime:  " + this.lastTransitionToNoLagTime + " lastTransitionToLagTime: " + this.lastTransitionToLagTime);
        DelayState delayState = calculateDelayState();
        long curTime = System.currentTimeMillis();
        if (traditionalRes) {
            boolean z = delayState.isDelay;
            ConcurrentHashMap<String, Integer> concurrentHashMap = this.tempGameCountMap;
            if (z) {
                concurrentHashMap.compute("tpTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda4
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        Integer num = (Integer) obj2;
                        return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                    }
                });
                log("updateConfusionMatrixForTraditionalModelIn1S:tp++");
                return;
            }
            concurrentHashMap.compute("fpTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda5
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("updateConfusionMatrixForTraditionalModelIn1S fp++  ");
            ConcurrentHashMap<String, Object> fpMap = new ConcurrentHashMap<>();
            fpMap.put("fp", Arrays.toString(this.inputData));
            fpMap.put("game", this.mCurrentGame);
            fpMap.put("time", Long.valueOf(curTime));
            fpMap.put("delay", Integer.valueOf(this.mDelay));
            fpMap.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList<ConcurrentHashMap<String, Object>> fpParamList = this.mOneDayGameMdParamList.get("fpTraditionalParam");
            if (fpParamList == null) {
                fpParamList = new ArrayList<>();
                this.mOneDayGameMdParamList.put("fpTraditionalParam", fpParamList);
            }
            if (fpParamList.size() < 5) {
                fpParamList.add(fpMap);
                return;
            }
            return;
        }
        boolean z2 = delayState.isNotDelay;
        ConcurrentHashMap<String, Integer> concurrentHashMap2 = this.tempGameCountMap;
        if (!z2) {
            concurrentHashMap2.compute("fnTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda6
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("updateConfusionMatrixForTraditionalModelIn1S:fn++");
            ConcurrentHashMap<String, Object> fnMap = new ConcurrentHashMap<>();
            fnMap.put("fn", Arrays.toString(this.inputData));
            fnMap.put("game", this.mCurrentGame);
            fnMap.put("time", Long.valueOf(curTime));
            fnMap.put("delay", Integer.valueOf(this.mDelay));
            fnMap.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList<ConcurrentHashMap<String, Object>> fnParamList = this.mOneDayGameMdParamList.get("fnTraditionalParam");
            if (fnParamList == null) {
                fnParamList = new ArrayList<>();
                this.mOneDayGameMdParamList.put("fnTraditionalParam", fnParamList);
            }
            if (fnParamList.size() < 5) {
                fnParamList.add(fnMap);
                return;
            }
            return;
        }
        concurrentHashMap2.compute("tnTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda7
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer num = (Integer) obj2;
                return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
            }
        });
        log("updateConfusionMatrixForTraditionalModelIn1S:tn++");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrixForTraditionalModelFor2S(boolean state) {
        String newState = state ? "HIGH" : "LOW";
        DelayState delayState = calculateDelayState();
        if ("NOT Ready".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = newState;
            return;
        }
        if ("LOW".equals(newState) && "HIGH".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = newState;
            this.highCounterForTra = 0;
            this.lowCounterForTra = 1;
            return;
        }
        if ("HIGH".equals(newState) && "LOW".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = newState;
            this.highCounterForTra = 1;
            this.lowCounterForTra = 0;
            return;
        }
        if ("HIGH".equals(newState) && "HIGH".equals(this.mLastTraditonStateForCnt)) {
            log("keep high");
            this.highCounterForTra++;
            if (this.highCounterForTra >= 2) {
                boolean z = delayState.isDelay;
                ConcurrentHashMap<String, Integer> concurrentHashMap = this.tempGameCountMap;
                if (z) {
                    concurrentHashMap.compute("tpTraditionalFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda8
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("tpTraditionalFor2S ++");
                    return;
                } else {
                    concurrentHashMap.compute("fpTraditionalFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda9
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("fpTraditionalFor2S ++");
                    return;
                }
            }
            return;
        }
        if ("LOW".equals(newState) && "LOW".equals(this.mLastTraditonStateForCnt)) {
            this.lowCounterForTra++;
            log("keep low");
            if (this.lowCounterForTra >= 2) {
                boolean z2 = delayState.isNotDelay;
                ConcurrentHashMap<String, Integer> concurrentHashMap2 = this.tempGameCountMap;
                if (!z2) {
                    concurrentHashMap2.compute("fnTraditionalFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda10
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                    log("fnTraditionalFor2S ++");
                } else {
                    concurrentHashMap2.compute("tnTraditionalFor2S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda11
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer num = (Integer) obj2;
                            return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                        }
                    });
                }
            }
        }
    }

    private static class DelayState {
        public final boolean isDelay;
        public final boolean isNotDelay;

        public DelayState(boolean isDelay, boolean isNotDelay) {
            this.isDelay = isDelay;
            this.isNotDelay = isNotDelay;
        }
    }

    private DelayState calculateDelayState() {
        long curTime = System.currentTimeMillis();
        boolean isDelay = false;
        boolean isNotDelay = false;
        long j = this.lastTransitionToNoLagTime;
        long j2 = this.lastTransitionToLagTime;
        long j3 = this.lastTransitionToNoLagTime;
        if (j > j2) {
            if (curTime - j3 <= 2000) {
                isDelay = true;
            }
        } else if (j3 != 0 || this.lastTransitionToLagTime != 0) {
            isDelay = true;
        }
        if (this.lastTransitionToNoLagTime > this.lastTransitionToLagTime) {
            isNotDelay = true;
        } else if ((this.lastTransitionToNoLagTime == 0 && this.lastTransitionToLagTime == 0) || curTime - this.lastTransitionToLagTime <= 2000) {
            isNotDelay = true;
        }
        return new DelayState(isDelay, isNotDelay);
    }

    private String getActualSignal() {
        long now = System.currentTimeMillis();
        long targetStart = now - 2000;
        long targetEnd = now - 1000;
        long lagStart = this.lastTransitionToLagTime;
        long lagEnd = this.lastTransitionToNoLagTime;
        if (lagStart > lagEnd) {
            lagEnd = now;
        }
        boolean inTargetWindow = lagStart <= targetEnd && lagEnd >= targetStart;
        log("inTargetWindow: " + inTargetWindow + "lagStart: " + lagStart + " targetEnd: " + targetEnd + " lagEnd: " + lagEnd + " targetStart: " + targetStart);
        return inTargetWindow ? "HIGH" : "LOW";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean isHasDelayWhiteListGame() {
        return Boolean.valueOf(this.GAME_HAS_DELAY_LIST.contains(this.mCurrentGame));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean isNoDelayWhiteListGame() {
        return Boolean.valueOf(this.GAME_NO_DELAY_LIST.contains(this.mCurrentGame));
    }

    public void setDelayBuf(int delay) {
        if (!this.isGameModelRun) {
            return;
        }
        if (delay > 460) {
            delay = 460;
        }
        if (delay < 0) {
            delay = 0;
        }
        if (!this.startCalculateLag) {
            this.startCalculateLag = true;
        }
        getTempGameCount();
        this.lastDelay = this.mDelay;
        this.mDelay = delay;
        this.lastDelayTime = System.currentTimeMillis();
        log("setDelayBuf " + delay + " isGameLagging = " + this.isGameLagging);
        if (this.mDelay > 200 && !this.isGameLagging) {
            this.lastTransitionToLagTime = System.currentTimeMillis();
            this.lastTransitionToNoLagTime = 0L;
            this.isGameLagging = true;
        } else if (this.mDelay < 200 && this.isGameLagging) {
            this.isGameLagging = false;
            this.lastTransitionToLagTime = 0L;
            this.lastTransitionToNoLagTime = System.currentTimeMillis();
        }
    }

    public void setGameRat(int rat) {
        if (this.isGameModelRun) {
            this.mCurRat = rat;
        }
    }

    private void getTempGameCount() {
        this.tp += this.tempGameCountMap.getOrDefault("tp", 0).intValue();
        this.fp += this.tempGameCountMap.getOrDefault("fp", 0).intValue();
        this.fn += this.tempGameCountMap.getOrDefault("fn", 0).intValue();
        this.tn += this.tempGameCountMap.getOrDefault("tn", 0).intValue();
        this.tp_AiIn1S += this.tempGameCountMap.getOrDefault("tp_AiIn1S", 0).intValue();
        this.fp_AiIn1S += this.tempGameCountMap.getOrDefault("fp_AiIn1S", 0).intValue();
        this.fn_AiIn1S += this.tempGameCountMap.getOrDefault("fn_AiIn1S", 0).intValue();
        this.tn_AiIn1S += this.tempGameCountMap.getOrDefault("tn_AiIn1S", 0).intValue();
        this.tpTraditionalIn1S += this.tempGameCountMap.getOrDefault("tpTraditionalIn1S", 0).intValue();
        this.fpTraditionalIn1S += this.tempGameCountMap.getOrDefault("fpTraditionalIn1S", 0).intValue();
        this.fnTraditionalIn1S += this.tempGameCountMap.getOrDefault("fnTraditionalIn1S", 0).intValue();
        this.tnTraditionalIn1S += this.tempGameCountMap.getOrDefault("tnTraditionalIn1S", 0).intValue();
        this.tp_AiFor2S += this.tempGameCountMap.getOrDefault("tp_AiFor2S", 0).intValue();
        this.fp_AiFor2S += this.tempGameCountMap.getOrDefault("fp_AiFor2S", 0).intValue();
        this.fn_AiFor2S += this.tempGameCountMap.getOrDefault("fn_AiFor2S", 0).intValue();
        this.tn_AiFor2S += this.tempGameCountMap.getOrDefault("tn_AiFor2S", 0).intValue();
        this.tpTraditionalFor2S += this.tempGameCountMap.getOrDefault("tpTraditionalFor2S", 0).intValue();
        this.fpTraditionalFor2S += this.tempGameCountMap.getOrDefault("fpTraditionalFor2S", 0).intValue();
        this.fnTraditionalFor2S += this.tempGameCountMap.getOrDefault("fnTraditionalFor2S", 0).intValue();
        this.tnTraditionalFor2S += this.tempGameCountMap.getOrDefault("tnTraditionalFor2S", 0).intValue();
        log("tp: " + this.tp + " fp: " + this.fp + " fn: " + this.fn + " tn: " + this.tn);
        log("tp_AiIn1S: " + this.tp_AiIn1S + " fp_AiIn1S: " + this.fp_AiIn1S + " fn_AiIn1S: " + this.fn_AiIn1S + " tn_AiIn1S: " + this.tn_AiIn1S);
        log("tpTraditionalIn1S: " + this.tpTraditionalIn1S + " fpTraditionalIn1S: " + this.fpTraditionalIn1S + " fnTraditionalIn1S: " + this.fnTraditionalIn1S + " tnTraditionalIn1S: " + this.tnTraditionalIn1S);
        log("tp_AiFor2S: " + this.tp_AiFor2S + " fp_AiFor2S: " + this.fp_AiFor2S + " fn_AiFor2S: " + this.fn_AiFor2S + " tn_AiFor2S: " + this.tn_AiFor2S);
        log("tpTraditionalFor2S: " + this.tpTraditionalFor2S + " fpTraditionalFor2S: " + this.fpTraditionalFor2S + " fnTraditionalFor2S: " + this.fnTraditionalFor2S + " tnTraditionalFor2S: " + this.tnTraditionalFor2S);
        this.tempGameCountMap.clear();
    }

    public ConcurrentHashMap<String, ArrayList> getGameModelOneDayParam() {
        ConcurrentHashMap<String, ArrayList> returnMap = new ConcurrentHashMap<>();
        log("getGameModelOneDayParam: " + this.mOneDayGameMdParamList.size());
        int count = 0;
        try {
            for (Map.Entry<String, ArrayList> entry : this.mOneDayGameMdParamList.entrySet()) {
                if (count >= 2) {
                    break;
                }
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    log("Skipping null value for key: " + entry.getKey());
                } else {
                    returnMap.put(entry.getKey(), new ArrayList(entry.getValue()));
                    count++;
                }
            }
        } catch (Exception e) {
            log("Error processing game model parameters: " + e.getMessage());
            returnMap.clear();
        } finally {
            this.mOneDayGameMdParamList.clear();
        }
        return returnMap;
    }

    private Boolean getDataFromMTK() {
        String atCmdResult = "";
        if (mOemHookAgent != null) {
            log("on send at command by mtk telephony manager ex");
            mOemHookAgent = OemHookAgent.getHook();
            try {
                log("try");
                atCmdResult = mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,20,0,0");
            } catch (Exception e) {
                StringBuilder stackTraceBuilder = new StringBuilder();
                for (StackTraceElement element : e.getStackTrace()) {
                    stackTraceBuilder.append(element.toString()).append("\n");
                }
                String stackTraceString = stackTraceBuilder.toString();
                log("Error:" + e.getMessage() + "\n" + stackTraceString);
            }
            return parseMtkGameInfo(atCmdResult);
        }
        log("mOemHookAgent is null");
        return false;
    }

    private Boolean getDataFromQCOM() {
        if (mOemHookAgent != null) {
            log("on send command by QCOM mirilhook");
            ByteBuffer res = mOemHookAgent.getGameAiInfo();
            return parseQcomGameInfo(res);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean makeInput() {
        log("on game_model makeInput");
        if (!isHasDelayWhiteListGame().booleanValue() && !isNoDelayWhiteListGame().booleanValue()) {
            return false;
        }
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

    public void startRun(OemHookAgent hookAgent, String game_pkg_name, int curGameScene, int curGameRat) {
        log("game_model startRun");
        if (this.isGameModelRun) {
            return;
        }
        mOemHookAgent = hookAgent;
        if (mOemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK start data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,0,0,0");
        }
        this.mCurrentGame = game_pkg_name;
        this.mCurGameScene = curGameScene;
        this.mCurRat = curGameRat;
        initParameters();
        this.mExecutor = Executors.newScheduledThreadPool(4);
        this.mExecutor.scheduleAtFixedRate(this.forecast, 0L, 1000L, TimeUnit.MILLISECONDS);
        this.isGameModelRun = true;
    }

    public void stopRun(OemHookAgent hookAgent) {
        log("game_model stopRun");
        if (!this.isGameModelRun) {
            return;
        }
        mOemHookAgent = hookAgent;
        if (mOemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK stop data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,1,0,0");
        }
        calculateIndicator();
        if (isNoDelayWhiteListGame().booleanValue()) {
            updateAiAndTraState("LOW", this.mAiModel);
            updateAiAndTraState("LOW", this.mTraditionModel);
        }
        this.mExecutor.shutdown();
        this.isGameModelRun = false;
        this.isGameLagging = false;
        this.startCalculateLag = false;
        this.shouldForceLag = false;
        this.lastTransitionToLagTime = 0L;
        this.lastTransitionToNoLagTime = 0L;
        this.tempGameCountMap.clear();
        this.mCurRat = 0;
    }

    public List<HashMap<String, Object>> getGameModelOneDayIndicator() {
        List<HashMap<String, Object>> returnList = new ArrayList<>();
        log(String.format("mOneDayIndicatorCount: %d", Integer.valueOf(this.mOneDayIndicatorCount)));
        try {
            try {
                int limit = Math.min(this.mOneDayIndicatorList.size(), 2);
                if (limit > 0) {
                    returnList = (List) this.mOneDayIndicatorList.stream().limit(limit).map(new Function() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda12
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return GameModel.lambda$getGameModelOneDayIndicator$20((HashMap) obj);
                        }
                    }).collect(Collectors.toList());
                }
            } catch (Exception e) {
                log("Error processing indicators: " + e.getMessage());
                returnList.clear();
            }
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
            printList(returnList);
            return returnList;
        } catch (Throwable th) {
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
            throw th;
        }
    }

    static /* synthetic */ HashMap lambda$getGameModelOneDayIndicator$20(HashMap map) {
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

    private void initMetricsParameters() {
        this.tp_AiIn1S = 0;
        this.fp_AiIn1S = 0;
        this.tn_AiIn1S = 0;
        this.fn_AiIn1S = 0;
        this.tp_AiFor2S = 0;
        this.tn_AiFor2S = 0;
        this.fp_AiFor2S = 0;
        this.fn_AiFor2S = 0;
        this.tpMixIn2s = 0;
        this.fpMixIn2s = 0;
        this.tnMixIn2s = 0;
        this.fnMixIn2s = 0;
        this.tpTradionnal = 0;
        this.fpTraditionnal = 0;
        this.tnTradionnal = 0;
        this.fnTradionnal = 0;
        this.tpTraditionalIn1S = 0;
        this.fpTraditionalIn1S = 0;
        this.tnTraditionalIn1S = 0;
        this.fnTraditionalIn1S = 0;
        this.tpTraditionalFor2S = 0;
        this.fpTraditionalFor2S = 0;
        this.tnTraditionalFor2S = 0;
        this.fnTraditionalFor2S = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String msg) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, msg);
    }
}
