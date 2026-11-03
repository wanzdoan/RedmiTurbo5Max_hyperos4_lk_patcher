package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.android.internal.os.BackgroundThread;
import com.xiaomi.modem.OemHookAgent;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
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
    private static Context mContext;
    private static int mHookType;
    private static OemHookAgent mOemHookAgent;
    private float[] input;
    private ScheduledExecutorService mExecutor;
    private AiModelLoader mLoader;
    private float[] output;
    private final List GAME_HAS_DELAY_LIST = Arrays.asList("com.tencent.tmgp.sgame", "com.tencent.jkchess", "com.tencent.lolm");
    private final List GAME_NO_DELAY_LIST = Arrays.asList("com.netease.party", "com.netease.party.mi", "com.tencent.tmgp.dfm", "com.tencent.KiHan", "com.tencent.tmgp.cf", "com.tencent.mf.uam", "com.miHoYo.Yuanshen");
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
    private List mOneDayIndicatorList = new ArrayList();
    private ConcurrentHashMap mOneDayGameMdParamList = new ConcurrentHashMap();
    private ConcurrentHashMap tempGameDataMap = new ConcurrentHashMap();
    private ConcurrentHashMap tempGameCountMap = new ConcurrentHashMap();
    private long lastTransitionToLagTime = 0;
    private long lastTransitionToNoLagTime = 0;
    private boolean isGameLagging = false;
    private boolean startCalculateLag = false;
    private boolean shouldForceLag = false;
    private boolean isGameModelRun = false;
    private final Runnable forecast = new Runnable() { // from class: com.xiaomi.internal.telephony.ai.GameModel.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (GameModel.this.makeInput().booleanValue()) {
                    long jNanoTime = System.nanoTime();
                    GameModel.this.forecastModel();
                    boolean zIsGameLaggingForTraditionalMethod = GameModel.this.new gameDataProcessor().isGameLaggingForTraditionalMethod(GameModel.this.inputData);
                    GameModel.log(String.format("forecast time %d us", Long.valueOf((System.nanoTime() - jNanoTime) / 1000)));
                    GameModel gameModel = GameModel.this;
                    gameModel.mForecastRes = gameModel.parseGameAiOutput();
                    String str = "HIGH";
                    GameModel.log("forecast res: " + GameModel.this.mForecastRes + "  traditionalRes: " + zIsGameLaggingForTraditionalMethod + " mixRes: " + (zIsGameLaggingForTraditionalMethod && "HIGH".equals(GameModel.this.mForecastRes)));
                    if (GameModel.this.isHasDelayWhiteListGame().booleanValue()) {
                        GameModel.this.updateConfusionMatrix();
                        GameModel gameModel2 = GameModel.this;
                        gameModel2.getGamAiTpFor2S(gameModel2.mForecastRes);
                        GameModel gameModel3 = GameModel.this;
                        gameModel3.getGameAiTpIn1S(gameModel3.mForecastRes);
                        GameModel.this.updateConfusionMatrixForTraditionalModelIn1S(zIsGameLaggingForTraditionalMethod);
                        GameModel.this.updateConfusionMatrixForTraditionalModelFor2S(zIsGameLaggingForTraditionalMethod);
                    }
                    if (GameModel.this.isNoDelayWhiteListGame().booleanValue()) {
                        GameModel gameModel4 = GameModel.this;
                        gameModel4.updateAiAndTraState(gameModel4.mForecastRes, GameModel.this.mAiModel);
                        GameModel gameModel5 = GameModel.this;
                        if (!zIsGameLaggingForTraditionalMethod) {
                            str = "LOW";
                        }
                        gameModel5.updateAiAndTraState(str, gameModel5.mTraditionModel);
                    }
                    GameModel.this.mForecastCount++;
                    for (int i = 0; i < 20; i++) {
                        int i2 = i + 20;
                        int i3 = i + 40;
                        int i4 = i + 60;
                        int i5 = i + 80;
                        ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.dataFileName, String.format("\n%d \t%f  \t%f  \t%f  \t%f  \t%f ", Integer.valueOf(GameModel.this.mForecastCount), Float.valueOf(GameModel.this.input[i]), Float.valueOf(GameModel.this.input[i2]), Float.valueOf(GameModel.this.input[i3]), Float.valueOf(GameModel.this.input[i4]), Float.valueOf(GameModel.this.input[i5])));
                        ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.originalDataFileName, String.format("\n%d \t%d  \t%d  \t%d  \t%d  \t%d", Integer.valueOf(GameModel.this.mForecastCount), Integer.valueOf(GameModel.this.inputAiModelData[i]), Integer.valueOf(GameModel.this.inputAiModelData[i2]), Integer.valueOf(GameModel.this.inputAiModelData[i3]), Integer.valueOf(GameModel.this.inputAiModelData[i4]), Integer.valueOf(GameModel.this.inputAiModelData[i5])));
                    }
                    ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.resFileName, String.format("\n%d \t%s \t%f \t%f \t%d", Integer.valueOf(GameModel.this.mForecastCount), GameModel.this.mForecastRes, Float.valueOf(GameModel.this.output[0]), Float.valueOf(GameModel.this.output[1]), Integer.valueOf(GameModel.this.mDelay)));
                    ModemAiDataCollector.getInstance(GameModel.mContext).appendDebugStringToFile(GameModel.this.delayFileName, String.format("\n%d \t%d", Integer.valueOf(GameModel.this.mForecastCount), Integer.valueOf(GameModel.this.mDelay)));
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
                GameModel.log("Error:" + e.getMessage() + "\n" + sb.toString());
            }
        }
    };
    private final Handler mHandler = new Handler(BackgroundThread.get().getLooper()) { // from class: com.xiaomi.internal.telephony.ai.GameModel.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            int i = message.arg1;
            int size = GameModel.this.tempGameDataMap.size();
            GameModel gameModel = GameModel.this;
            if (size >= 3) {
                ArrayList arrayList = (ArrayList) gameModel.mOneDayGameMdParamList.get("MdParam");
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    GameModel.this.mOneDayGameMdParamList.put("MdParam", arrayList);
                }
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(GameModel.this.tempGameDataMap);
                concurrentHashMap.put("game", GameModel.this.mCurrentGame);
                arrayList.add(concurrentHashMap);
                GameModel.this.tempGameDataMap.clear();
                return;
            }
            gameModel.tempGameDataMap.put("delayParam" + i, Arrays.toString(GameModel.this.inputData));
            GameModel.this.mHandler.sendMessageDelayed(GameModel.this.mHandler.obtainMessage(1, Integer.valueOf(i + 1)), 1000L);
        }
    };

    public GameModel(Context context) {
        this.mLoader = null;
        if (mOemHookAgent == null) {
            mOemHookAgent = OemHookAgent.getHook();
        }
        if (mHookType == 0) {
            mHookType = OemHookAgent.getHookType();
        }
        mContext = context;
        this.mLoader = new AiModelLoader("game_model", mContext);
    }

    private void initParameters() {
        String str = "" + new Timestamp(System.currentTimeMillis());
        String str2 = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10) + "_" + str.substring(11, 13) + str.substring(14, 16) + str.substring(17, 19);
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
        this.dataFileName = "Game_" + str2 + "_log.txt";
        this.resFileName = "Game_" + str2 + "_Res.txt";
        this.indicatorFileName = "Game_" + str2 + "_Indicator.txt";
        this.delayFileName = "Game_" + str2 + "_Delay.txt";
        this.originalDataFileName = "Game_" + str2 + "_original.txt";
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
            int[] iArr = {1, 100, 1};
            int[] iArr2 = {1, 2};
            for (int i = 0; i < 3; i++) {
                log("inputShape[" + i + "]:" + iArr[i]);
            }
            for (int i2 = 0; i2 < 2; i2++) {
                log("outputShape[" + i2 + "]:" + iArr2[i2]);
            }
            DataType dataType = DataType.FLOAT32;
            TensorBuffer tensorBufferCreateFixedSize = TensorBuffer.createFixedSize(iArr, dataType);
            TensorBuffer tensorBufferCreateFixedSize2 = TensorBuffer.createFixedSize(iArr2, dataType);
            tensorBufferCreateFixedSize.loadArray(this.input);
            this.mLoader.run(tensorBufferCreateFixedSize.getBuffer(), tensorBufferCreateFixedSize2.getBuffer());
            log("outputBufferString:\n" + bytesToHexString(tensorBufferCreateFixedSize2.getBuffer().array()));
            this.output = tensorBufferCreateFixedSize2.getFloatArray();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(String.format("0x%02x ", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    private Boolean parseMtkGameInfo(String str) {
        log("parseMtkGameInfo:");
        if (isHasDelayWhiteListGame().booleanValue() && !this.startCalculateLag) {
            log(" startCalculateLag false return ");
            return Boolean.FALSE;
        }
        if (str.equals("")) {
            log("at resp is null, modem game info may not ready.");
            return Boolean.FALSE;
        }
        log(str);
        String[] strArrSplit = str.split("\n")[1].trim().split(",");
        if (strArrSplit[1].equals("0")) {
            return Boolean.FALSE;
        }
        if (strArrSplit[1].equals("3")) {
            this.shouldForceLag = true;
            log("shouldForceLag is true");
        }
        for (int i = 0; i < 20; i++) {
            try {
                int i2 = i + 2;
                this.inputData[i] = Integer.parseInt(strArrSplit[i2].trim());
                this.inputAiModelData[i] = Integer.parseInt(strArrSplit[i2].trim());
                int i3 = i + 20;
                int i4 = i + 22;
                this.inputData[i3] = Integer.parseInt(strArrSplit[i4].trim());
                this.inputAiModelData[i3] = Integer.parseInt(strArrSplit[i4].trim());
                int i5 = i / 10;
                int[] iArr = this.inputData;
                if (i5 >= 1) {
                    iArr[i + 40] = Integer.parseInt(strArrSplit[43].trim());
                    int i6 = i + 60;
                    this.inputAiModelData[i6] = Integer.parseInt(strArrSplit[43].trim());
                    this.inputData[i6] = Integer.parseInt(strArrSplit[45].trim());
                    int i7 = i + 80;
                    this.inputData[i7] = Integer.parseInt(strArrSplit[47].trim());
                    this.inputAiModelData[i7] = Integer.parseInt(strArrSplit[47].trim());
                } else {
                    iArr[i + 40] = Integer.parseInt(strArrSplit[42].trim());
                    int i8 = i + 60;
                    this.inputAiModelData[i8] = Integer.parseInt(strArrSplit[42].trim());
                    this.inputData[i8] = Integer.parseInt(strArrSplit[44].trim());
                    int i9 = i + 80;
                    this.inputData[i9] = Integer.parseInt(strArrSplit[46].trim());
                    this.inputAiModelData[i9] = Integer.parseInt(strArrSplit[46].trim());
                }
                int i10 = i + 48;
                this.inputData[i + 100] = Integer.parseInt(strArrSplit[i10].trim());
                int i11 = i + 68;
                this.inputData[i + 120] = Integer.parseInt(strArrSplit[i11].trim());
                int i12 = Integer.parseInt(strArrSplit[i11].trim());
                int[] iArr2 = this.inputAiModelData;
                if (i12 == 0) {
                    iArr2[i + 40] = 2;
                } else {
                    iArr2[i + 40] = Integer.parseInt(strArrSplit[i10].trim()) / Integer.parseInt(strArrSplit[i11].trim());
                }
                this.inputData[i + 140] = Integer.parseInt(strArrSplit[i + 88].trim());
                this.inputData[i + 160] = Integer.parseInt(strArrSplit[i + 108].trim());
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
                log("Error:" + e.getMessage() + "\n" + sb.toString());
            }
        }
        for (int i13 = 0; i13 < 100; i13++) {
            this.input[i13] = DataNormalizer.normalizeSingleValue(this.inputAiModelData[i13], i13 / 20);
        }
        log("input: " + Arrays.toString(this.input));
        log("parseMtkGameInfo:over");
        return Boolean.TRUE;
    }

    public class DataNormalizer {

        public class NormalizationConfig {
            public static final float[] MEANS = {0.0f, 0.0f, 0.0f, 0.0f, 59.71711f};
            public static final float[] SCALES = {100.0f, 100.0f, 2.0f, 100.0f, 837.0f};
        }

        public DataNormalizer(GameModel gameModel) {
        }

        public static float normalizeSingleValue(int i, int i2) {
            if (i2 < 0 || i2 > 4) {
                Folme$$ExternalSyntheticBUOutline0.m("索引必须在0-4之间");
                return 0.0f;
            }
            float f = NormalizationConfig.MEANS[i2];
            float f2 = NormalizationConfig.SCALES[i2];
            float f3 = i;
            if (f3 >= f2) {
                f3 = f2;
            }
            return (f3 - f) / f2;
        }
    }

    public class gameDataProcessor {
        public gameDataProcessor() {
        }

        class DataRow {
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

        public boolean isGameLaggingForTraditionalMethod(int[] iArr) {
            boolean z;
            int i;
            int i2;
            if (iArr != null && iArr.length >= 180) {
                GameModel.log("inputdata: " + Arrays.toString(iArr));
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < 20; i3++) {
                    DataRow dataRow = new DataRow();
                    dataRow.nrUlBler = iArr[i3];
                    dataRow.nrDlBler = iArr[i3 + 20];
                    dataRow.pdcpDlAbnormalityRate = iArr[i3 + 40];
                    dataRow.nrMacDlThroughput = iArr[i3 + 60];
                    dataRow.nrRlcDelay = iArr[i3 + 80];
                    dataRow.nrReceivedGrantBytes = iArr[i3 + 100];
                    dataRow.nrUplinkPendingData = iArr[i3 + 120];
                    dataRow.nrPdcpUlThroughput = iArr[i3 + 140];
                    dataRow.nrPdcpDlThroughput = iArr[i3 + 160];
                    arrayList.add(dataRow);
                }
                int size = arrayList.size();
                int i4 = 0;
                while (i4 < size) {
                    Object obj = arrayList.get(i4);
                    i4++;
                    DataRow dataRow2 = (DataRow) obj;
                    int i5 = dataRow2.nrUplinkPendingData;
                    if (i5 != 0) {
                        int i6 = dataRow2.nrReceivedGrantBytes;
                        if (i6 / i5 < 0.5d && i6 < 800) {
                            dataRow2.tempFlagForMacUl = 1;
                        }
                    }
                    dataRow2.tempFlagForMacUl = 0;
                }
                for (int i7 = 0; i7 < arrayList.size(); i7++) {
                    DataRow dataRow3 = (DataRow) arrayList.get(i7);
                    GameModel.log("orig-: " + dataRow3);
                    if (GameModel.this.isNoDelayWhiteListGame().booleanValue()) {
                        dataRow3.nrMacUlFlag = 0;
                    } else if (i7 >= 9) {
                        int i8 = 0;
                        int i9 = 0;
                        for (int i10 = i7 - 9; i10 <= i7; i10++) {
                            i8 += ((DataRow) arrayList.get(i10)).tempFlagForMacUl;
                            if (((DataRow) arrayList.get(i10)).nrPdcpUlThroughput == 0) {
                                i9++;
                            }
                        }
                        dataRow3.nrMacUlFlag = (i8 < 6 || i9 < 3) ? 0 : 1;
                    } else {
                        dataRow3.nrMacUlFlag = 0;
                    }
                }
                int size2 = arrayList.size();
                int i11 = 0;
                int i12 = 0;
                while (i12 < size2) {
                    Object obj2 = arrayList.get(i12);
                    i12++;
                    DataRow dataRow4 = (DataRow) obj2;
                    int i13 = dataRow4.nrReceivedGrantBytes;
                    if (i13 < 300 && dataRow4.nrRlcDelay < 150 && (i2 = dataRow4.nrUplinkPendingData) < 200) {
                        i11 = (i13 == 0 && i2 == 0) ? i11 + 3 : i11 + 1;
                    }
                }
                for (int i14 = 0; i14 < arrayList.size(); i14++) {
                    DataRow dataRow5 = (DataRow) arrayList.get(i14);
                    if (GameModel.this.isNoDelayWhiteListGame().booleanValue() || (GameModel.this.isHasDelayWhiteListGame().booleanValue() && i11 >= 4)) {
                        dataRow5.nrPdcpUlFlag = 0;
                    } else if (i14 >= 2) {
                        int i15 = i14 - 2;
                        while (true) {
                            if (i15 > i14) {
                                i = 1;
                                break;
                            }
                            if (((DataRow) arrayList.get(i15)).nrPdcpUlThroughput >= 500) {
                                i = 0;
                                break;
                            }
                            i15++;
                        }
                        dataRow5.nrPdcpUlFlag = i;
                    } else {
                        dataRow5.nrPdcpUlFlag = 0;
                    }
                }
                for (int i16 = 0; i16 < arrayList.size(); i16++) {
                    DataRow dataRow6 = (DataRow) arrayList.get(i16);
                    if (GameModel.this.isNoDelayWhiteListGame().booleanValue() || (GameModel.this.isHasDelayWhiteListGame().booleanValue() && i11 >= 4)) {
                        dataRow6.nrPdcpDlFlag = 0;
                    } else if (i16 >= 9) {
                        int i17 = 0;
                        for (int i18 = i16 - 9; i18 <= i16; i18++) {
                            if (((DataRow) arrayList.get(i18)).nrPdcpDlThroughput < 700) {
                                i17++;
                            }
                        }
                        dataRow6.nrPdcpDlFlag = (((DataRow) arrayList.get(i16)).nrPdcpDlThroughput == 0 || i17 >= 2) ? 1 : 0;
                    } else {
                        dataRow6.nrPdcpDlFlag = 0;
                    }
                }
                for (int i19 = 0; i19 < arrayList.size(); i19++) {
                    DataRow dataRow7 = (DataRow) arrayList.get(i19);
                    if (i19 >= 9) {
                        int i20 = 0;
                        double d = 0.0d;
                        for (int i21 = i19 - 9; i21 <= i19; i21++) {
                            if (((DataRow) arrayList.get(i21)).nrUlBler >= 70) {
                                i20++;
                            }
                            d += (double) (((DataRow) arrayList.get(i21)).nrUlBler / 10);
                        }
                        dataRow7.nrUlBlerFlag = (i20 >= 8 || d >= 70.0d) ? 1 : 0;
                    } else {
                        dataRow7.nrUlBlerFlag = 0;
                    }
                }
                for (int i22 = 0; i22 < arrayList.size(); i22++) {
                    DataRow dataRow8 = (DataRow) arrayList.get(i22);
                    if (i22 >= 9) {
                        int i23 = 0;
                        double d2 = 0.0d;
                        for (int i24 = i22 - 9; i24 <= i22; i24++) {
                            if (((DataRow) arrayList.get(i24)).nrDlBler >= 60) {
                                i23++;
                            }
                            d2 += (double) (((DataRow) arrayList.get(i24)).nrDlBler / 10);
                        }
                        dataRow8.nrDlBlerFlag = (i23 >= 7 || d2 >= 60.0d) ? 1 : 0;
                    } else {
                        dataRow8.nrDlBlerFlag = 0;
                    }
                }
                for (int i25 = 0; i25 < arrayList.size(); i25++) {
                    DataRow dataRow9 = (DataRow) arrayList.get(i25);
                    if (i25 >= 14) {
                        int i26 = 0;
                        for (int i27 = i25 - 9; i27 <= i25; i27++) {
                            if (((DataRow) arrayList.get(i27)).nrRlcDelay > 180) {
                                i26++;
                            }
                        }
                        dataRow9.nrRlcDelayFlag = i26 >= 4 ? 1 : 0;
                    } else {
                        dataRow9.nrRlcDelayFlag = 0;
                    }
                }
                for (int i28 = 0; i28 < arrayList.size(); i28++) {
                    DataRow dataRow10 = (DataRow) arrayList.get(i28);
                    if (i28 >= 9) {
                        int i29 = 0;
                        for (int i30 = i28 - 9; i30 <= i28; i30++) {
                            if (((DataRow) arrayList.get(i30)).pdcpDlAbnormalityRate > 4) {
                                i29++;
                            }
                        }
                        dataRow10.pdcpDlAbnormalityFlag = i29 >= 5 ? 1 : 0;
                    } else {
                        dataRow10.pdcpDlAbnormalityFlag = 0;
                    }
                }
                int size3 = arrayList.size();
                int i31 = 0;
                while (true) {
                    if (i31 >= size3) {
                        z = false;
                        break;
                    }
                    Object obj3 = arrayList.get(i31);
                    i31++;
                    if (((DataRow) obj3).nrMacDlThroughput > 20480) {
                        z = true;
                        break;
                    }
                }
                int size4 = arrayList.size();
                int i32 = 0;
                int i33 = 0;
                int i34 = 0;
                while (i34 < size4) {
                    Object obj4 = arrayList.get(i34);
                    i34++;
                    DataRow dataRow11 = (DataRow) obj4;
                    dataRow11.finalFlag = dataRow11.nrMacUlFlag | dataRow11.nrPdcpUlFlag | dataRow11.nrPdcpDlFlag | dataRow11.nrUlBlerFlag | dataRow11.nrDlBlerFlag | dataRow11.nrRlcDelayFlag | dataRow11.pdcpDlAbnormalityFlag;
                    GameModel.log("count: " + i33 + " finalFlag: " + dataRow11.finalFlag + " nrMacUlFlag: " + dataRow11.nrMacUlFlag + " nrPdcpUlFlag: " + dataRow11.nrPdcpUlFlag + " nrPdcpDlFlag: " + dataRow11.nrPdcpDlFlag + " nrUlBlerFlag: " + dataRow11.nrUlBlerFlag + " nrRlcDelayFlag: " + dataRow11.nrRlcDelayFlag + " nrDlBlerFlag: " + dataRow11.nrDlBlerFlag + " pdcpDlAbnormalityFlag: " + dataRow11.pdcpDlAbnormalityFlag);
                    if (i33 >= 10 && dataRow11.finalFlag == 1) {
                        i32++;
                    }
                    i33++;
                }
                GameModel.log("gameLagCount:" + i32 + " gameNoLagCount:" + i11 + " shouldForceLag: " + GameModel.this.shouldForceLag + " hasLowThroughput: " + z);
                if (GameModel.this.shouldForceLag) {
                    return true;
                }
                if (i32 >= 6 && !z) {
                    return true;
                }
            }
            return false;
        }
    }

    private Boolean parseQcomGameInfo(ByteBuffer byteBuffer) {
        log("parseQcomGameInfo:");
        if (byteBuffer == null) {
            log("ByteBuffer is null");
            return Boolean.FALSE;
        }
        try {
            byteBuffer.get();
            for (int i = 0; i < 4; i++) {
                this.input[i + 4] = byteBuffer.get();
                this.input[i] = byteBuffer.get();
                this.input[i + 16] = byteBuffer.get();
                this.input[i + 20] = byteBuffer.get();
                this.input[i + 32] = byteBuffer.get();
                this.input[i + 40] = byteBuffer.get();
                this.input[i + 12] = byteBuffer.get();
                this.input[i + 8] = byteBuffer.get();
                this.input[i + 24] = byteBuffer.get();
                this.input[i + 28] = byteBuffer.get();
                this.input[i + 36] = byteBuffer.get();
                this.input[i + 44] = byteBuffer.get();
            }
            log("parseQcomGameInfo:over");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
        return Boolean.TRUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseGameAiOutput() {
        for (int i = 0; i < 2; i++) {
            log("output[" + i + "]:" + this.output[i]);
        }
        boolean z = this.output[1] >= gameTh;
        if (this.shouldForceLag) {
            log("parseGameAiOutput shouldForceLag");
            this.shouldForceLag = false;
            return "HIGH";
        }
        if (z) {
            return z ? "HIGH" : "NOT Ready";
        }
        return "LOW";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAiAndTraState(String str, String str2) {
        log("updateAiAndTraState:  newState: " + str + " model: " + str2 + " mlastAiState: " + this.mlastAiState + " mLastTraditonState: " + this.mLastTraditonState);
        if ("AI".equals(str2)) {
            if ("NOT Ready".equals(this.mlastAiState)) {
                sendResToController(str, "AI");
                this.mlastAiState = str;
                return;
            }
            if ("LOW".equals(str) && "HIGH".equals(this.mlastAiState)) {
                log("ai high to low");
                sendResToController(str, "AI");
                this.mlastAiState = str;
                return;
            } else {
                if ("HIGH".equals(str) && "LOW".equals(this.mlastAiState)) {
                    log("ai low to high");
                    sendResToController(str, "AI");
                    this.mlastAiState = str;
                    return;
                }
                return;
            }
        }
        if ("TRADITIONAL".equals(str2)) {
            if ("NOT Ready".equals(this.mLastTraditonState)) {
                this.mLastTraditonState = str;
                sendResToController(str, "TRADITIONAL");
                return;
            }
            if ("LOW".equals(str) && "HIGH".equals(this.mLastTraditonState)) {
                log("tra high to low");
                sendResToController(str, "TRADITIONAL");
                this.mLastTraditonState = str;
            } else if ("HIGH".equals(str) && "LOW".equals(this.mLastTraditonState)) {
                log("tra low to high");
                sendResToController(str, "TRADITIONAL");
                this.mLastTraditonState = str;
            }
        }
    }

    private void updateState(String str) {
        log("updateState:");
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = str;
            return;
        }
        if ("LOW".equals(str) && "HIGH".equals(this.mlastState)) {
            sendResToController(str, "AI");
            this.mlastState = str;
            this.highCounter = 0;
            return;
        }
        if ("HIGH".equals(str) && "LOW".equals(this.mlastState)) {
            this.mlastState = str;
            this.highCounter = 1;
        } else {
            if ("HIGH".equals(str) && "HIGH".equals(this.mlastState)) {
                log("keep high");
                int i = this.highCounter + 1;
                this.highCounter = i;
                if (i == 3) {
                    sendResToController(str, "AI");
                    return;
                }
                return;
            }
            log("keep low");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getGamAiTpFor2S(String str) {
        DelayState delayStateCalculateDelayState = calculateDelayState();
        if ("NOT Ready".equals(this.mlastState)) {
            this.mlastState = str;
            return;
        }
        if ("LOW".equals(str) && "HIGH".equals(this.mlastState)) {
            this.mlastState = str;
            this.highCounter = 0;
            this.lowCounter = 1;
            return;
        }
        if ("HIGH".equals(str) && "LOW".equals(this.mlastState)) {
            this.mlastState = str;
            this.highCounter = 1;
            this.lowCounter = 0;
            return;
        }
        if ("HIGH".equals(str) && "HIGH".equals(this.mlastState)) {
            int i = this.highCounter + 1;
            this.highCounter = i;
            if (i >= 2) {
                boolean z = delayStateCalculateDelayState.isDelay;
                ConcurrentHashMap concurrentHashMap = this.tempGameCountMap;
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
        if ("LOW".equals(str) && "LOW".equals(this.mlastState)) {
            this.lowCounter++;
            log("keep low");
            if (this.lowCounter >= 2) {
                boolean z2 = delayStateCalculateDelayState.isNotDelay;
                ConcurrentHashMap concurrentHashMap2 = this.tempGameCountMap;
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
    public void getGameAiTpIn1S(String str) {
        DelayState delayStateCalculateDelayState = calculateDelayState();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if ("HIGH".equals(str)) {
            boolean z = delayStateCalculateDelayState.isDelay;
            ConcurrentHashMap concurrentHashMap = this.tempGameCountMap;
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
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("fp", Arrays.toString(this.inputData));
            concurrentHashMap2.put("game", this.mCurrentGame);
            concurrentHashMap2.put("time", Long.valueOf(jCurrentTimeMillis));
            concurrentHashMap2.put("delay", Integer.valueOf(this.mDelay));
            concurrentHashMap2.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList arrayList = (ArrayList) this.mOneDayGameMdParamList.get("fpAiParam");
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mOneDayGameMdParamList.put("fpAiParam", arrayList);
            }
            if (arrayList.size() < 5) {
                arrayList.add(concurrentHashMap2);
            }
            log("calculateIndicator:fp++ couter=" + this.mForecastCount);
            return;
        }
        if ("LOW".equals(str)) {
            boolean z2 = delayStateCalculateDelayState.isNotDelay;
            ConcurrentHashMap concurrentHashMap3 = this.tempGameCountMap;
            if (!z2) {
                concurrentHashMap3.compute("fn_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda19
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        Integer num = (Integer) obj2;
                        return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                    }
                });
                log("fn_AiIn1S++ ");
                ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
                concurrentHashMap4.put("game", this.mCurrentGame);
                concurrentHashMap4.put("fn", Arrays.toString(this.inputData));
                concurrentHashMap4.put("time", Long.valueOf(jCurrentTimeMillis));
                concurrentHashMap4.put("delay", Integer.valueOf(this.mDelay));
                concurrentHashMap4.put("rat", Integer.valueOf(this.mCurRat));
                ArrayList arrayList2 = (ArrayList) this.mOneDayGameMdParamList.get("fnAiParam");
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                    this.mOneDayGameMdParamList.put("fnAiParam", arrayList2);
                }
                if (arrayList2.size() < 5) {
                    arrayList2.add(concurrentHashMap4);
                }
                log("calculateIndicator:fn++ counter=" + this.mForecastCount);
                return;
            }
            concurrentHashMap3.compute("tn_AiIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda20
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("tn_AiIn1S++ ");
        }
    }

    private void sendResToController(String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.modemai.game.FORECAST_RES");
        intent.putExtra("FORECAST_RES", str);
        intent.putExtra("FORECAST_MODEL", str2);
        mContext.sendBroadcast(intent);
    }

    private void calculateIndicator() {
        log("calculateIndicator:");
        float f = this.tp;
        float f2 = this.tn;
        float f3 = this.fp;
        this.acc = ((f + f2) * 100.0f) / (((f2 + f) + f3) + this.fn);
        if (f + f3 == 0.0f) {
            log("calculateIndicator error:tp+fp=0");
        } else {
            this.precision = (f * 100.0f) / (f + f3);
        }
        float f4 = this.tp;
        float f5 = this.fn;
        if (f4 + f5 == 0.0f) {
            log("calculateIndicator error:tp+fn=0");
        } else {
            this.recall = (100.0f * f4) / (f4 + f5);
        }
        log(String.format("calculateIndicator: gameModel indicators: \n precision:%f,recall:%f,acc:%f", Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
        ModemAiDataCollector.getInstance(mContext).appendDebugStringToFile(this.indicatorFileName, String.format("\n%d  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f  \t%.2f", Integer.valueOf(this.mForecastCount), Float.valueOf(this.tp), Float.valueOf(this.fp), Float.valueOf(this.tn), Float.valueOf(this.fn), Float.valueOf(this.precision), Float.valueOf(this.recall), Float.valueOf(this.acc)));
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
        map.put("game", this.mCurrentGame);
        map.put("scene", Integer.valueOf(this.mCurGameScene));
        map.put("tp_AiIn1S", Integer.valueOf(this.tp_AiIn1S));
        map.put("fp_AiIn1S", Integer.valueOf(this.fp_AiIn1S));
        map.put("tn_AiIn1S", Integer.valueOf(this.tn_AiIn1S));
        map.put("fn_AiIn1S", Integer.valueOf(this.fn_AiIn1S));
        map.put("tp_AiFor2S", Integer.valueOf(this.tp_AiFor2S));
        map.put("fp_AiFor2S", Integer.valueOf(this.fp_AiFor2S));
        map.put("fn_AiFor2S", Integer.valueOf(this.fn_AiFor2S));
        map.put("tn_AiFor2S", Integer.valueOf(this.tn_AiFor2S));
        map.put("tp_traditionnalIn1S", Integer.valueOf(this.tpTraditionalIn1S));
        map.put("fp_traditionnalIn1S", Integer.valueOf(this.fpTraditionalIn1S));
        map.put("tn_traditionnalIn1S", Integer.valueOf(this.tnTraditionalIn1S));
        map.put("fn_traditionnalIn1S", Integer.valueOf(this.fnTraditionalIn1S));
        map.put("tp_traditionnalFor2S", Integer.valueOf(this.tpTraditionalFor2S));
        map.put("fp_traditionnalFor2S", Integer.valueOf(this.fpTraditionalFor2S));
        map.put("tn_traditionnalFor2S", Integer.valueOf(this.tnTraditionalFor2S));
        map.put("fn_traditionnalFor2S", Integer.valueOf(this.fnTraditionalFor2S));
        map.put("curGameRat", Integer.valueOf(this.mCurRat));
        this.mOneDayIndicatorList.add(map);
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
            return;
        }
        if ("LOW".equals(this.mForecastRes) && "LOW".equals(actualSignal)) {
            this.tempGameCountMap.compute("tn", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda16
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log(" updateConfusionMatrix tn+  " + this.tn);
            return;
        }
        log("calculateIndicator error:lastForecastRes=" + this.lastForecastRes + ",actualSignal=" + actualSignal);
    }

    private void updateConfusionMatrixForMixModelIn2S(boolean z) {
        String actualSignal = getActualSignal();
        long jCurrentTimeMillis = System.currentTimeMillis();
        log("lastTransitionToNoLagTime= " + this.lastTransitionToNoLagTime + "lastTransitionToLagTime=" + this.lastTransitionToLagTime + " curTime: " + jCurrentTimeMillis + "actualSignal: " + actualSignal);
        long j = this.lastTransitionToNoLagTime;
        long j2 = this.lastTransitionToLagTime;
        boolean z2 = j <= j2 ? !(j == 0 && j2 == 0) : jCurrentTimeMillis - j <= 3000;
        boolean z3 = j > j2 || (j == 0 && j2 == 0) || jCurrentTimeMillis - j2 <= 3000;
        if (z) {
            if (z2) {
                this.tpMixIn2s++;
                log("updateConfusionMatrixForMixModelIn2S:tp++ " + this.tpMixIn2s);
                return;
            }
            if (z2) {
                return;
            }
            this.fpMixIn2s++;
            log("updateConfusionMatrixForMixModelIn2S:fp++ " + this.fpMixIn2s);
            return;
        }
        if (!z3) {
            this.fnMixIn2s++;
            log("updateConfusionMatrixForMixModelIn2S:fn++ " + this.fnMixIn2s);
            return;
        }
        if (z3) {
            this.tnMixIn2s++;
            log("updateConfusionMatrixForMixModelIn2S:tn++ " + this.tnMixIn2s);
        }
    }

    private void updateConfusionMatrixForMixModel(boolean z) {
        String actualSignal = getActualSignal();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (z && "LOW".equals(actualSignal)) {
            log("updateConfusionMatrixForMixModelfp++ couter=" + this.mForecastCount);
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("fp", Arrays.toString(this.inputData));
            concurrentHashMap.put("game", this.mCurrentGame);
            concurrentHashMap.put("time", Long.valueOf(jCurrentTimeMillis));
            ArrayList arrayList = (ArrayList) this.mOneDayGameMdParamList.get("fpMixParam");
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mOneDayGameMdParamList.put("fpMixParam", arrayList);
            }
            if (arrayList.size() < 20) {
                arrayList.add(concurrentHashMap);
                return;
            }
            return;
        }
        if (z || !"HIGH".equals(actualSignal)) {
            return;
        }
        log("updateConfusionMatrixForMixModel:fn++ counter=" + this.mForecastCount);
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        concurrentHashMap2.put("fn", Arrays.toString(this.inputData));
        concurrentHashMap2.put("game", this.mCurrentGame);
        concurrentHashMap2.put("time", Long.valueOf(jCurrentTimeMillis));
        ArrayList arrayList2 = (ArrayList) this.mOneDayGameMdParamList.get("fnMixParam");
        if (arrayList2 == null) {
            arrayList2 = new ArrayList();
            this.mOneDayGameMdParamList.put("fnMixParam", arrayList2);
        }
        if (arrayList2.size() < 20) {
            arrayList2.add(concurrentHashMap2);
        }
    }

    private void updateConfusionMatrixForTraditionalModel(boolean z) {
        log("updateConfusionMatrixForTraditionalModel:traditionalRes=" + z);
        String actualSignal = getActualSignal();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (z && "HIGH".equals(actualSignal)) {
            this.tpTradionnal++;
            log("updateConfusionMatrixForTraditionalModel:tp++");
            return;
        }
        if (z && "LOW".equals(actualSignal)) {
            this.fpTraditionnal++;
            log("updateConfusionMatrixForTraditionalModel couter=" + this.mForecastCount);
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("fp", Arrays.toString(this.inputData));
            concurrentHashMap.put("game", this.mCurrentGame);
            concurrentHashMap.put("time", Long.valueOf(jCurrentTimeMillis));
            ArrayList arrayList = (ArrayList) this.mOneDayGameMdParamList.get("fpTraditionalParam");
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mOneDayGameMdParamList.put("fpTraditionalParam", arrayList);
            }
            if (arrayList.size() < 20) {
                arrayList.add(concurrentHashMap);
                return;
            }
            return;
        }
        if (!z && "HIGH".equals(actualSignal)) {
            this.fnTradionnal++;
            log("updateConfusionMatrixForTraditionalModel:fn++");
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("fn", Arrays.toString(this.inputData));
            concurrentHashMap2.put("game", this.mCurrentGame);
            concurrentHashMap2.put("time", Long.valueOf(jCurrentTimeMillis));
            ArrayList arrayList2 = (ArrayList) this.mOneDayGameMdParamList.get("fnTraditionalParam");
            if (arrayList2 == null) {
                arrayList2 = new ArrayList();
                this.mOneDayGameMdParamList.put("fnTraditionalParam", arrayList2);
            }
            if (arrayList2.size() < 20) {
                arrayList2.add(concurrentHashMap2);
                return;
            }
            return;
        }
        if (z || !"LOW".equals(actualSignal)) {
            return;
        }
        this.tnTradionnal++;
        log("updateConfusionMatrixForTraditionalModel:tn++");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrixForTraditionalModelIn1S(boolean z) {
        log("lastTransitionToNoLagTime:  " + this.lastTransitionToNoLagTime + " lastTransitionToLagTime: " + this.lastTransitionToLagTime);
        DelayState delayStateCalculateDelayState = calculateDelayState();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (z) {
            boolean z2 = delayStateCalculateDelayState.isDelay;
            ConcurrentHashMap concurrentHashMap = this.tempGameCountMap;
            if (z2) {
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
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("fp", Arrays.toString(this.inputData));
            concurrentHashMap2.put("game", this.mCurrentGame);
            concurrentHashMap2.put("time", Long.valueOf(jCurrentTimeMillis));
            concurrentHashMap2.put("delay", Integer.valueOf(this.mDelay));
            concurrentHashMap2.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList arrayList = (ArrayList) this.mOneDayGameMdParamList.get("fpTraditionalParam");
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mOneDayGameMdParamList.put("fpTraditionalParam", arrayList);
            }
            if (arrayList.size() < 5) {
                arrayList.add(concurrentHashMap2);
                return;
            }
            return;
        }
        boolean z3 = delayStateCalculateDelayState.isNotDelay;
        ConcurrentHashMap concurrentHashMap3 = this.tempGameCountMap;
        if (!z3) {
            concurrentHashMap3.compute("fnTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda6
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer num = (Integer) obj2;
                    return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
                }
            });
            log("updateConfusionMatrixForTraditionalModelIn1S:fn++");
            ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
            concurrentHashMap4.put("fn", Arrays.toString(this.inputData));
            concurrentHashMap4.put("game", this.mCurrentGame);
            concurrentHashMap4.put("time", Long.valueOf(jCurrentTimeMillis));
            concurrentHashMap4.put("delay", Integer.valueOf(this.mDelay));
            concurrentHashMap4.put("rat", Integer.valueOf(this.mCurRat));
            ArrayList arrayList2 = (ArrayList) this.mOneDayGameMdParamList.get("fnTraditionalParam");
            if (arrayList2 == null) {
                arrayList2 = new ArrayList();
                this.mOneDayGameMdParamList.put("fnTraditionalParam", arrayList2);
            }
            if (arrayList2.size() < 5) {
                arrayList2.add(concurrentHashMap4);
                return;
            }
            return;
        }
        concurrentHashMap3.compute("tnTraditionalIn1S", new BiFunction() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda7
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer num = (Integer) obj2;
                return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
            }
        });
        log("updateConfusionMatrixForTraditionalModelIn1S:tn++");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfusionMatrixForTraditionalModelFor2S(boolean z) {
        String str = z ? "HIGH" : "LOW";
        DelayState delayStateCalculateDelayState = calculateDelayState();
        if ("NOT Ready".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = str;
            return;
        }
        if ("LOW".equals(str) && "HIGH".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = str;
            this.highCounterForTra = 0;
            this.lowCounterForTra = 1;
            return;
        }
        if ("HIGH".equals(str) && "LOW".equals(this.mLastTraditonStateForCnt)) {
            this.mLastTraditonStateForCnt = str;
            this.highCounterForTra = 1;
            this.lowCounterForTra = 0;
            return;
        }
        if ("HIGH".equals(str) && "HIGH".equals(this.mLastTraditonStateForCnt)) {
            log("keep high");
            int i = this.highCounterForTra + 1;
            this.highCounterForTra = i;
            if (i >= 2) {
                boolean z2 = delayStateCalculateDelayState.isDelay;
                ConcurrentHashMap concurrentHashMap = this.tempGameCountMap;
                if (z2) {
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
        if ("LOW".equals(str) && "LOW".equals(this.mLastTraditonStateForCnt)) {
            this.lowCounterForTra++;
            log("keep low");
            if (this.lowCounterForTra >= 2) {
                boolean z3 = delayStateCalculateDelayState.isNotDelay;
                ConcurrentHashMap concurrentHashMap2 = this.tempGameCountMap;
                if (!z3) {
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

    class DelayState {
        public final boolean isDelay;
        public final boolean isNotDelay;

        public DelayState(boolean z, boolean z2) {
            this.isDelay = z;
            this.isNotDelay = z2;
        }
    }

    private DelayState calculateDelayState() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.lastTransitionToNoLagTime;
        long j2 = this.lastTransitionToLagTime;
        boolean z = true;
        boolean z2 = j <= j2 ? !(j == 0 && j2 == 0) : jCurrentTimeMillis - j <= 2000;
        if (j <= j2 && ((j != 0 || j2 != 0) && jCurrentTimeMillis - j2 > 2000)) {
            z = false;
        }
        return new DelayState(z2, z);
    }

    private String getActualSignal() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = jCurrentTimeMillis - 2000;
        long j2 = jCurrentTimeMillis - 1000;
        long j3 = this.lastTransitionToLagTime;
        long j4 = this.lastTransitionToNoLagTime;
        if (j3 <= j4) {
            jCurrentTimeMillis = j4;
        }
        boolean z = j3 <= j2 && jCurrentTimeMillis >= j;
        log("inTargetWindow: " + z + "lagStart: " + j3 + " targetEnd: " + j2 + " lagEnd: " + jCurrentTimeMillis + " targetStart: " + j);
        return z ? "HIGH" : "LOW";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean isHasDelayWhiteListGame() {
        return Boolean.valueOf(this.GAME_HAS_DELAY_LIST.contains(this.mCurrentGame));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean isNoDelayWhiteListGame() {
        return Boolean.valueOf(this.GAME_NO_DELAY_LIST.contains(this.mCurrentGame));
    }

    public void setDelayBuf(int i) {
        if (this.isGameModelRun) {
            if (i > 460) {
                i = 460;
            }
            if (i < 0) {
                i = 0;
            }
            if (!this.startCalculateLag) {
                this.startCalculateLag = true;
            }
            getTempGameCount();
            this.lastDelay = this.mDelay;
            this.mDelay = i;
            this.lastDelayTime = System.currentTimeMillis();
            log("setDelayBuf " + i + " isGameLagging = " + this.isGameLagging);
            int i2 = this.mDelay;
            if (i2 > 200 && !this.isGameLagging) {
                this.lastTransitionToLagTime = System.currentTimeMillis();
                this.lastTransitionToNoLagTime = 0L;
                this.isGameLagging = true;
            } else {
                if (i2 >= 200 || !this.isGameLagging) {
                    return;
                }
                this.isGameLagging = false;
                this.lastTransitionToLagTime = 0L;
                this.lastTransitionToNoLagTime = System.currentTimeMillis();
            }
        }
    }

    public void setGameRat(int i) {
        if (this.isGameModelRun) {
            this.mCurRat = i;
        }
    }

    private void getTempGameCount() {
        this.tp += ((Integer) this.tempGameCountMap.getOrDefault("tp", 0)).intValue();
        this.fp += ((Integer) this.tempGameCountMap.getOrDefault("fp", 0)).intValue();
        this.fn += ((Integer) this.tempGameCountMap.getOrDefault("fn", 0)).intValue();
        this.tn += ((Integer) this.tempGameCountMap.getOrDefault("tn", 0)).intValue();
        this.tp_AiIn1S += ((Integer) this.tempGameCountMap.getOrDefault("tp_AiIn1S", 0)).intValue();
        this.fp_AiIn1S += ((Integer) this.tempGameCountMap.getOrDefault("fp_AiIn1S", 0)).intValue();
        this.fn_AiIn1S += ((Integer) this.tempGameCountMap.getOrDefault("fn_AiIn1S", 0)).intValue();
        this.tn_AiIn1S += ((Integer) this.tempGameCountMap.getOrDefault("tn_AiIn1S", 0)).intValue();
        this.tpTraditionalIn1S += ((Integer) this.tempGameCountMap.getOrDefault("tpTraditionalIn1S", 0)).intValue();
        this.fpTraditionalIn1S += ((Integer) this.tempGameCountMap.getOrDefault("fpTraditionalIn1S", 0)).intValue();
        this.fnTraditionalIn1S += ((Integer) this.tempGameCountMap.getOrDefault("fnTraditionalIn1S", 0)).intValue();
        this.tnTraditionalIn1S += ((Integer) this.tempGameCountMap.getOrDefault("tnTraditionalIn1S", 0)).intValue();
        this.tp_AiFor2S += ((Integer) this.tempGameCountMap.getOrDefault("tp_AiFor2S", 0)).intValue();
        this.fp_AiFor2S += ((Integer) this.tempGameCountMap.getOrDefault("fp_AiFor2S", 0)).intValue();
        this.fn_AiFor2S += ((Integer) this.tempGameCountMap.getOrDefault("fn_AiFor2S", 0)).intValue();
        this.tn_AiFor2S += ((Integer) this.tempGameCountMap.getOrDefault("tn_AiFor2S", 0)).intValue();
        this.tpTraditionalFor2S += ((Integer) this.tempGameCountMap.getOrDefault("tpTraditionalFor2S", 0)).intValue();
        this.fpTraditionalFor2S += ((Integer) this.tempGameCountMap.getOrDefault("fpTraditionalFor2S", 0)).intValue();
        this.fnTraditionalFor2S += ((Integer) this.tempGameCountMap.getOrDefault("fnTraditionalFor2S", 0)).intValue();
        this.tnTraditionalFor2S += ((Integer) this.tempGameCountMap.getOrDefault("tnTraditionalFor2S", 0)).intValue();
        log("tp: " + this.tp + " fp: " + this.fp + " fn: " + this.fn + " tn: " + this.tn);
        log("tp_AiIn1S: " + this.tp_AiIn1S + " fp_AiIn1S: " + this.fp_AiIn1S + " fn_AiIn1S: " + this.fn_AiIn1S + " tn_AiIn1S: " + this.tn_AiIn1S);
        log("tpTraditionalIn1S: " + this.tpTraditionalIn1S + " fpTraditionalIn1S: " + this.fpTraditionalIn1S + " fnTraditionalIn1S: " + this.fnTraditionalIn1S + " tnTraditionalIn1S: " + this.tnTraditionalIn1S);
        log("tp_AiFor2S: " + this.tp_AiFor2S + " fp_AiFor2S: " + this.fp_AiFor2S + " fn_AiFor2S: " + this.fn_AiFor2S + " tn_AiFor2S: " + this.tn_AiFor2S);
        log("tpTraditionalFor2S: " + this.tpTraditionalFor2S + " fpTraditionalFor2S: " + this.fpTraditionalFor2S + " fnTraditionalFor2S: " + this.fnTraditionalFor2S + " tnTraditionalFor2S: " + this.tnTraditionalFor2S);
        this.tempGameCountMap.clear();
    }

    public ConcurrentHashMap getGameModelOneDayParam() {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        log("getGameModelOneDayParam: " + this.mOneDayGameMdParamList.size());
        try {
            int i = 0;
            for (Map.Entry entry : this.mOneDayGameMdParamList.entrySet()) {
                if (i >= 2) {
                    break;
                }
                if (entry.getValue() == null || ((ArrayList) entry.getValue()).isEmpty()) {
                    log("Skipping null value for key: " + ((String) entry.getKey()));
                } else {
                    concurrentHashMap.put((String) entry.getKey(), new ArrayList((Collection) entry.getValue()));
                    i++;
                }
            }
        } catch (Exception e) {
            log("Error processing game model parameters: " + e.getMessage());
            concurrentHashMap.clear();
        } finally {
            this.mOneDayGameMdParamList.clear();
        }
        return concurrentHashMap;
    }

    private Boolean getDataFromMTK() {
        String strOnSendAtCommandByMtkTelephonyManagerEx;
        if (mOemHookAgent != null) {
            log("on send at command by mtk telephony manager ex");
            mOemHookAgent = OemHookAgent.getHook();
            try {
                log("try");
                strOnSendAtCommandByMtkTelephonyManagerEx = mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,20,0,0");
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
                log("Error:" + e.getMessage() + "\n" + sb.toString());
                strOnSendAtCommandByMtkTelephonyManagerEx = "";
            }
            return parseMtkGameInfo(strOnSendAtCommandByMtkTelephonyManagerEx);
        }
        log("mOemHookAgent is null");
        return Boolean.FALSE;
    }

    private Boolean getDataFromQCOM() {
        if (mOemHookAgent != null) {
            log("on send command by QCOM mirilhook");
            return parseQcomGameInfo(mOemHookAgent.getGameAiInfo());
        }
        return Boolean.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean makeInput() {
        log("on game_model makeInput");
        if (!isHasDelayWhiteListGame().booleanValue() && !isNoDelayWhiteListGame().booleanValue()) {
            return Boolean.FALSE;
        }
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

    public void startRun(OemHookAgent oemHookAgent, String str, int i, int i2) {
        log("game_model startRun");
        if (this.isGameModelRun) {
            return;
        }
        mOemHookAgent = oemHookAgent;
        if (oemHookAgent == null) {
            log("ERROR: can't get OemHookAgent, return.");
            return;
        }
        if (mHookType == 1) {
            log("MTK start data info collection");
            mOemHookAgent.onSendAtCommandByMtkTelephonyManagerEx("AT+EDMFAPP=20005,0,0,0");
        }
        this.mCurrentGame = str;
        this.mCurGameScene = i;
        this.mCurRat = i2;
        initParameters();
        ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(4);
        this.mExecutor = scheduledExecutorServiceNewScheduledThreadPool;
        scheduledExecutorServiceNewScheduledThreadPool.scheduleAtFixedRate(this.forecast, 0L, 1000L, TimeUnit.MILLISECONDS);
        this.isGameModelRun = true;
    }

    public void stopRun(OemHookAgent oemHookAgent) {
        log("game_model stopRun");
        if (this.isGameModelRun) {
            mOemHookAgent = oemHookAgent;
            if (oemHookAgent == null) {
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
    }

    public List getGameModelOneDayIndicator() {
        List arrayList = new ArrayList();
        log(String.format("mOneDayIndicatorCount: %d", Integer.valueOf(this.mOneDayIndicatorCount)));
        try {
            try {
                int iMin = Math.min(this.mOneDayIndicatorList.size(), 2);
                if (iMin > 0) {
                    arrayList = (List) this.mOneDayIndicatorList.stream().limit(iMin).map(new Function() { // from class: com.xiaomi.internal.telephony.ai.GameModel$$ExternalSyntheticLambda12
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return GameModel.m5364$r8$lambda$xavmOXd7kwD9mXSsge2N_rOQ98((HashMap) obj);
                        }
                    }).collect(Collectors.toList());
                }
            } catch (Exception e) {
                log("Error processing indicators: " + e.getMessage());
                arrayList.clear();
            }
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
            printList(arrayList);
            return arrayList;
        } catch (Throwable th) {
            this.mOneDayIndicatorList.clear();
            this.mOneDayIndicatorCount = 0;
            throw th;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$xavmOXd7kwD9-mXSsge2N_rOQ98, reason: not valid java name */
    public static /* synthetic */ HashMap m5364$r8$lambda$xavmOXd7kwD9mXSsge2N_rOQ98(HashMap map) {
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
    public static final void log(String str) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, str);
    }
}
