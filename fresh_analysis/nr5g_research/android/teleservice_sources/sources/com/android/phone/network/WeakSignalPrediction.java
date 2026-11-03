package com.android.phone.network;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkMetricsController;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.VideoPreloadScene;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class WeakSignalPrediction {
    private static boolean VDBG = false;
    private static WeakSignalPrediction mInstance = null;
    private static int mOnnxRuntimeLoadRetryCount = 0;
    private static boolean mOnnxRuntimeLoaded = false;
    private final BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private EvaluationCriteria mCriteria;
    private String mCurStateDataType;
    private int mDdsPhoneId;
    private int mInputWeakSignalSeqCount;
    private boolean mLastActualWeakSignal;
    private int mLastPci;
    private boolean mLastPredictionWeakSignal;
    private long mLastReportModemAiDataTime;
    private long mLastVideoPreloadTime;
    private long mModemAiStartMonitorTime;
    private NetworkMetricsController mNetworkMetricsController;
    private OrtEnvironment mOnnxEnv;
    private OrtSession mOnnxSession;
    private OrtSession.SessionOptions mOnnxSession_options;
    private ServiceState[] mServiceState;
    private SignalStrength mSignalStrength;
    private TelephonyManager mTelephonyManager;
    private float[][][] mWeakSignalInputData;
    private float[][][] mWeakSignalStatsData;
    public Handler mWorkHandler;
    private HandlerThread mWorkThread;
    private TeleCallBack mTeleCallBack = null;
    private boolean mIsExistOnnxRuntimeEnv = false;
    private float mTestOutput = -1.0f;
    private int WEAK_SIGNAL_BATCH_SIZE = 1;
    private int WEAK_SIGNAL_SEQUENCE_LENGTH = 10;
    private int WEAK_SIGNAL_FEATURE_COUNT = 5;

    public static synchronized WeakSignalPrediction make() {
        try {
            if (mInstance == null) {
                mInstance = new WeakSignalPrediction();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private WeakSignalPrediction() {
        Class cls = Float.TYPE;
        this.mWeakSignalInputData = (float[][][]) Array.newInstance((Class<?>) cls, 1, 10, 5);
        this.mInputWeakSignalSeqCount = 0;
        this.mWeakSignalStatsData = (float[][][]) Array.newInstance((Class<?>) cls, this.WEAK_SIGNAL_BATCH_SIZE, 20, 6);
        this.mSignalStrength = new SignalStrength();
        this.mLastPci = 0;
        this.mCriteria = new EvaluationCriteria(this);
        this.mLastPredictionWeakSignal = false;
        this.mLastActualWeakSignal = false;
        this.mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
        this.mDdsPhoneId = -1;
        this.mLastVideoPreloadTime = 0L;
        this.mModemAiStartMonitorTime = 0L;
        this.mCurStateDataType = "";
        this.mLastReportModemAiDataTime = 0L;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.network.WeakSignalPrediction.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                String action = intent.getAction();
                WeakSignalPrediction.log("receive action" + action);
                if (action.equals("com.android.phone.ACTION_WEAK_SIGNAL_PREDICTION_TEST")) {
                    WeakSignalPrediction.VDBG = true;
                    boolean booleanExtra = intent.getBooleanExtra("testSWeakSingal", false);
                    WeakSignalPrediction.this.mTestOutput = intent.getFloatExtra("testOutput", -1.0f);
                    if (booleanExtra) {
                        WeakSignalPrediction.this.runModemAiWeakSignalModel();
                        return;
                    }
                    return;
                }
                if (action.equals("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED")) {
                    if (WeakSignalPrediction.this.mWorkHandler != null) {
                        boolean booleanExtra2 = intent.getBooleanExtra("mIsHstMode", false);
                        WeakSignalPrediction.logD("ACTION_HST_MODE_STATUS_CHANGED mIsHstMode:" + booleanExtra2);
                        WeakSignalPrediction.this.mWorkHandler.obtainMessage(3, Boolean.valueOf(booleanExtra2)).sendToTarget();
                        return;
                    }
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    WeakSignalPrediction.this.mWorkHandler.obtainMessage(5).sendToTarget();
                }
            }
        };
        log("WeakSignalPrediction init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("WeakSignalPrediction", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new WeakSingalHandler(this.mWorkThread.getLooper());
        registerAllEvents();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.mDdsPhoneId = getDefaultDataSlotId();
        initWeakSignalModel();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
        }
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
            for (int i5 = 0; i5 < 20; i5++) {
                for (int i6 = 0; i6 < 6; i6++) {
                    this.mWeakSignalStatsData[i4][i5][i6] = 1.0f;
                }
            }
        }
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTeleCallBack == null) {
            log("new TeleCallBack");
            TeleCallBack teleCallBack = new TeleCallBack();
            this.mTeleCallBack = teleCallBack;
            TelephonyStatAdapter.registerCallBack(teleCallBack);
        }
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.ACTION_WEAK_SIGNAL_PREDICTION_TEST");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        registerTelephonyInfoCallBack();
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            if (i == WeakSignalPrediction.this.mDdsPhoneId) {
                WeakSignalPrediction.this.mWorkHandler.obtainMessage(1, i, 0, signalStrength).sendToTarget();
            }
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            WeakSignalPrediction.this.mWorkHandler.obtainMessage(6, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (i == WeakSignalPrediction.this.mDdsPhoneId) {
                WeakSignalPrediction.this.mWorkHandler.obtainMessage(2, i, 0, cellLocation).sendToTarget();
            }
        }
    }

    class WeakSingalHandler extends Handler {
        WeakSingalHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakSignalPrediction.logD("handleMessage event:" + message.what);
            switch (message.what) {
                case 1:
                    WeakSignalPrediction.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                    break;
                case 2:
                    WeakSignalPrediction.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                    break;
                case 3:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    if (zBooleanValue && !WeakSignalPrediction.this.mIsExistOnnxRuntimeEnv) {
                        WeakSignalPrediction.this.initModemAiEnv();
                        WeakSignalPrediction weakSignalPrediction = WeakSignalPrediction.this;
                        if (weakSignalPrediction.mWorkHandler != null && weakSignalPrediction.mIsExistOnnxRuntimeEnv) {
                            WeakSignalPrediction.this.mWorkHandler.obtainMessage(4).sendToTarget();
                            break;
                        }
                    } else if (!zBooleanValue && WeakSignalPrediction.this.mIsExistOnnxRuntimeEnv) {
                        WeakSignalPrediction.this.resetInputWeakSignalCount(true);
                        if (WeakSignalPrediction.this.mWorkHandler.hasMessages(4)) {
                            WeakSignalPrediction.this.mWorkHandler.removeMessages(4);
                        }
                        WeakSignalPrediction.this.recordModemAiWeakSignalMetrics();
                        if (WeakSignalPrediction.this.mModemAiStartMonitorTime != 0) {
                            WeakSignalPrediction.log("EVENT_STRAT_SIGNAL_PREDICTION_DELAY exit point collection!");
                            WeakSignalPrediction.this.stopMonitorTimerForCollectData(false);
                        }
                        break;
                    }
                    break;
                case 4:
                    if (WeakSignalPrediction.this.mIsExistOnnxRuntimeEnv && WeakSignalPrediction.this.updateWeakSignalInputData()) {
                        WeakSignalPrediction.this.runModemAiWeakSignalModel();
                    } else if (WeakSignalPrediction.this.mModemAiStartMonitorTime != 0) {
                        WeakSignalPrediction.log("EVENT_STRAT_SIGNAL_PREDICTION_DELAY exit point collection!");
                        WeakSignalPrediction.this.stopMonitorTimerForCollectData(false);
                    }
                    WeakSignalPrediction.this.periodicWeakSignalPredict();
                    break;
                case 5:
                    int defaultDataSlotId = WeakSignalPrediction.this.getDefaultDataSlotId();
                    if (defaultDataSlotId != WeakSignalPrediction.this.mDdsPhoneId) {
                        WeakSignalPrediction.this.resetInputWeakSignalCount(false);
                    }
                    WeakSignalPrediction.log("EVENT_DATA_SLOT_CHANGED: mDdsPhoneId=" + WeakSignalPrediction.this.mDdsPhoneId + " newDdsPhoneId=" + defaultDataSlotId);
                    WeakSignalPrediction.this.mDdsPhoneId = defaultDataSlotId;
                    break;
                case 6:
                    WeakSignalPrediction.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                    break;
                case 7:
                    WeakSignalPrediction.log("EVENT_RECORD_MODEMAI_DATA_TIMER");
                    WeakSignalPrediction.this.stopMonitorTimerForCollectData(true);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        if (signalStrength != null) {
            logD("onSignalStrengthsChanged");
            this.mSignalStrength = new SignalStrength(signalStrength);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        try {
            log("processServiceStateChangeEvent phoneId=" + i);
            if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                this.mServiceState[i] = new ServiceState(serviceState);
            } else {
                log("processServiceStateChangeEvent invalid params!!!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private static synchronized boolean loadOnnxRuntime() {
        if (mOnnxRuntimeLoaded) {
            return true;
        }
        if (mOnnxRuntimeLoadRetryCount >= 1) {
            return false;
        }
        try {
            log("loadOnnxRuntime");
            System.loadLibrary("onnxruntime4j_jni");
            mOnnxRuntimeLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            log("loadOnnxRuntime failed, retryCount=" + mOnnxRuntimeLoadRetryCount + " " + e);
            mOnnxRuntimeLoadRetryCount = mOnnxRuntimeLoadRetryCount + 1;
        }
        return mOnnxRuntimeLoaded;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initModemAiEnv() {
        try {
            if (!loadOnnxRuntime()) {
                log("initModemAiEnv: onnxRuntime load failed");
                return;
            }
            this.mOnnxEnv = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
            this.mOnnxSession_options = sessionOptions;
            this.mOnnxSession = this.mOnnxEnv.createSession("/system_ext/etc/weak_signal_v2.onnx", sessionOptions);
            this.mIsExistOnnxRuntimeEnv = true;
            log("initModemAiEnv: init model success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeModemAiEnv() {
        try {
            OrtSession ortSession = this.mOnnxSession;
            if (ortSession != null) {
                ortSession.close();
            }
            OrtEnvironment ortEnvironment = this.mOnnxEnv;
            if (ortEnvironment != null) {
                ortEnvironment.close();
            }
            this.mOnnxSession_options = null;
            this.mIsExistOnnxRuntimeEnv = false;
            log("closeModemAiEnv: close model success");
        } catch (Exception e) {
            e.printStackTrace();
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
        ServiceState serviceState;
        if (!SubscriptionManager.isValidPhoneId(this.mDdsPhoneId)) {
            log("updateSignalInfoForDds is invalid PhoneId");
            return null;
        }
        List<CellSignalStrength> cellSignalStrengths = this.mSignalStrength.getCellSignalStrengths();
        if (cellSignalStrengths == null || (serviceState = this.mServiceState[this.mDdsPhoneId]) == null) {
            log("updateSignalInfo signalStrength or serviceState is null");
            return null;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        if (!this.mNetworkMetricsController.isLteNrNwType(dataNetworkType)) {
            return null;
        }
        int pci = this.mNetworkMetricsController.getPci();
        int i = this.mLastPci == pci ? 0 : 1;
        this.mLastPci = pci;
        for (CellSignalStrength cellSignalStrength : cellSignalStrengths) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                return processLteSignal((CellSignalStrengthLte) cellSignalStrength, dataNetworkType, pci, i);
            }
            if (cellSignalStrength instanceof CellSignalStrengthNr) {
                return processNrSignal((CellSignalStrengthNr) cellSignalStrength, dataNetworkType, pci, i);
            }
        }
        return null;
    }

    private float[] processLteSignal(CellSignalStrengthLte cellSignalStrengthLte, int i, int i2, int i3) {
        if (!this.mNetworkMetricsController.isLteNwType(i) || cellSignalStrengthLte == null) {
            log("processLteSignal rat is not match lte signal");
            return null;
        }
        int rsrp = cellSignalStrengthLte.getRsrp();
        int rssnr = cellSignalStrengthLte.getRssnr();
        int rsrq = cellSignalStrengthLte.getRsrq();
        int iInRangeOrUnavailable = this.mNetworkMetricsController.inRangeOrUnavailable(rsrp, -140, -43);
        int iInRangeOrUnavailable2 = this.mNetworkMetricsController.inRangeOrUnavailable(rssnr, -20, 30);
        int iInRangeOrUnavailable3 = this.mNetworkMetricsController.inRangeOrUnavailable(rsrq, -34, 3);
        if (iInRangeOrUnavailable == Integer.MAX_VALUE || iInRangeOrUnavailable2 == Integer.MAX_VALUE || iInRangeOrUnavailable3 == Integer.MAX_VALUE) {
            return null;
        }
        if (i3 == -1) {
            i3 = i2;
        }
        float[] fArr = {1.0f, iInRangeOrUnavailable, iInRangeOrUnavailable3, iInRangeOrUnavailable2, i3};
        logD("processLteSignal ddsPhoneId=" + this.mDdsPhoneId + "LtePci=" + i2 + " pciFlag= " + i3 + " lteRsrp=" + iInRangeOrUnavailable + " lteRssnr=" + iInRangeOrUnavailable2 + " rat=" + i + " lteRsrq=" + iInRangeOrUnavailable3);
        return fArr;
    }

    private float[] processNrSignal(CellSignalStrengthNr cellSignalStrengthNr, int i, int i2, int i3) {
        if (!this.mNetworkMetricsController.isNrNwType(i) || cellSignalStrengthNr == null) {
            log("processNrSignal rat is not match nr signal");
            return null;
        }
        int ssRsrp = cellSignalStrengthNr.getSsRsrp();
        int ssSinr = cellSignalStrengthNr.getSsSinr();
        int ssRsrq = cellSignalStrengthNr.getSsRsrq();
        int iInRangeOrUnavailable = this.mNetworkMetricsController.inRangeOrUnavailable(ssRsrp, -156, -31);
        int iInRangeOrUnavailable2 = this.mNetworkMetricsController.inRangeOrUnavailable(ssSinr, -23, 40);
        int iInRangeOrUnavailable3 = this.mNetworkMetricsController.inRangeOrUnavailable(ssRsrq, -43, 20);
        if (iInRangeOrUnavailable == Integer.MAX_VALUE || iInRangeOrUnavailable2 == Integer.MAX_VALUE || iInRangeOrUnavailable3 == Integer.MAX_VALUE) {
            return null;
        }
        if (i3 == -1) {
            i3 = i2;
        }
        float[] fArr = {0.0f, iInRangeOrUnavailable, iInRangeOrUnavailable3, iInRangeOrUnavailable2, i3};
        logD("processNrSignal ddsPhoneId=" + this.mDdsPhoneId + "nrPci=" + i2 + " pciFlag= " + i3 + "nrRsrp=" + iInRangeOrUnavailable + " nrSinr=" + iInRangeOrUnavailable2 + " rat=" + i + " nrRsrq=" + iInRangeOrUnavailable3);
        return fArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean runModemAiWeakSignalModel() {
        if (this.mNetworkMetricsController.getDdsMobileDataOn() && this.mIsExistOnnxRuntimeEnv) {
            try {
                log("runModemAiWeakSignalModel V2.0: input data = " + Arrays.deepToString(this.mWeakSignalInputData));
                float[][] fArr = (float[][]) this.mOnnxSession.run(Collections.singletonMap("input1", OnnxTensor.createTensor(this.mOnnxEnv, this.mWeakSignalInputData))).get(0).getValue();
                if (fArr != null) {
                    float[] fArr2 = fArr[0];
                    if (fArr2.length == 2) {
                        this.mWeakSignalStatsData[this.WEAK_SIGNAL_BATCH_SIZE - 1][19][5] = fArr2[1];
                        logD("mWeakSignalStatsData:" + Arrays.deepToString(this.mWeakSignalStatsData));
                        if (VDBG) {
                            float f = this.mTestOutput;
                            if (f != -1.0f) {
                                fArr[0][1] = f;
                            }
                        }
                        float f2 = fArr[0][1];
                        boolean z = ((double) f2) > 0.5d;
                        this.mLastPredictionWeakSignal = z;
                        if (!this.mLastActualWeakSignal && z) {
                            tryDoNetworkOptimization(f2);
                        }
                        reportModemAiPerception(ModemAiScene.HST_WEAK_SIGNAL, fArr[0][1]);
                        log("runModemAiWeakSignalModel V2.0: output data = " + Arrays.deepToString(fArr) + " mTestOutput = " + this.mTestOutput);
                        return true;
                    }
                }
                log("runModemAiWeakSignalModel V2.0: error for outputValues");
                return false;
            } catch (OrtException e) {
                log("runModemAiWeakSignalModel V2.0: OrtException occurred");
                e.printStackTrace();
            } catch (Exception e2) {
                log("runModemAiWeakSignalModel V2.0: Exception occurred");
                e2.printStackTrace();
            }
        }
        return false;
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
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, 3000, -1, VideoPreloadScene.HST_WEAK_SIGNAL.getValue());
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

    public enum ModemAiScene {
        DUMMY(0),
        HST_WEAK_SIGNAL(1);

        private int value;

        ModemAiScene(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    private void reportModemAiPerception(ModemAiScene modemAiScene, float f) {
        log("reportModemAiPerception modemAiSceneL:" + modemAiScene.getValue() + " result:" + f);
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.MODEM_AI_WEAKSIGNAL_CHANGED");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        intent.putExtra("modemAiScene", modemAiScene.getValue());
        intent.putExtra(a.L, f);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void periodicWeakSignalPredict() {
        if (this.mWorkHandler == null) {
            return;
        }
        logD("periodicWeakSignalPredict");
        if (this.mWorkHandler.hasMessages(4)) {
            this.mWorkHandler.removeMessages(4);
        }
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(4), 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateWeakSignalInputData() {
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
                for (int i3 = 1; i3 < 20; i3++) {
                    float[][] fArr2 = this.mWeakSignalStatsData[i];
                    System.arraycopy(fArr2[i3], 0, fArr2[i3 - 1], 0, 6);
                }
                System.arraycopy(fArrUpdateSignalInfoForDds, 0, this.mWeakSignalInputData[i][this.WEAK_SIGNAL_SEQUENCE_LENGTH - 1], 0, this.WEAK_SIGNAL_FEATURE_COUNT);
                System.arraycopy(this.mWeakSignalInputData[i][this.WEAK_SIGNAL_SEQUENCE_LENGTH - 1], 0, this.mWeakSignalStatsData[i][19], 0, this.WEAK_SIGNAL_FEATURE_COUNT);
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
        if (this.mInputWeakSignalSeqCount > this.WEAK_SIGNAL_SEQUENCE_LENGTH + 1) {
            evaluationCriteriaUpdate(this.mLastActualWeakSignal, this.mLastPredictionWeakSignal, zIsActualWeakSignal);
            if (isShouldReportStatsData(this.mLastActualWeakSignal, this.mLastPredictionWeakSignal, zIsActualWeakSignal)) {
                startMonitorTimerForCollectData();
            }
        }
        this.mLastActualWeakSignal = zIsActualWeakSignal;
        logD("updateWeakSignalInputData mInputArray:" + Arrays.deepToString(this.mWeakSignalInputData));
        return true;
    }

    private boolean isFirstMonitorCollect() {
        Handler handler;
        return (this.mModemAiStartMonitorTime != 0 || (handler = this.mWorkHandler) == null || handler.hasMessages(7)) ? false : true;
    }

    private void startMonitorTimerForCollectData() {
        Handler handler = this.mWorkHandler;
        if (handler == null || !handler.hasMessages(7)) {
            log("startMonitorTimerForCollectData enter");
            this.mModemAiStartMonitorTime = SystemClock.elapsedRealtime();
            Handler handler2 = this.mWorkHandler;
            if (handler2 != null) {
                handler2.sendMessageDelayed(handler2.obtainMessage(7), 31000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopMonitorTimerForCollectData(boolean z) {
        log("stopMonitorTimerForCollectData exit isRecord:" + z);
        Handler handler = this.mWorkHandler;
        if (handler != null && handler.hasMessages(7)) {
            this.mWorkHandler.removeMessages(7);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInputWeakSignalCount(boolean z) {
        log("resetInputWeakSignalCount isCloseEnv=" + z + " mIsExistOnnxRuntimeEnv=" + this.mIsExistOnnxRuntimeEnv);
        this.mInputWeakSignalSeqCount = 0;
        if (z && this.mIsExistOnnxRuntimeEnv) {
            closeModemAiEnv();
        }
    }

    private boolean shouldRandomTriggerStatsData(double d) {
        Random random = new Random();
        logD("shouldRandomTriggerStatsData random: " + random);
        return random.nextDouble() < d;
    }

    public class EvaluationCriteria {
        private int truePositives = 0;
        private int falsePositives = 0;
        private int falseNegatives = 0;
        private int trueNegatives = 0;
        private int falsePositivesCase1 = 0;
        private int falsePositivesCase8 = 0;
        private int falseNegativesCase2 = 0;
        private int falseNegativesCase7 = 0;
        private int truePositivesCase4 = 0;
        private int truePositivesCase5 = 0;
        private int trueNegativesCase3 = 0;
        private int trueNegativesCase6 = 0;
        private int inferenceCount = 0;

        public EvaluationCriteria(WeakSignalPrediction weakSignalPrediction) {
        }

        public void update(boolean z, boolean z2, boolean z3) {
            if (z2 && z3) {
                this.truePositives++;
                if (z) {
                    this.truePositivesCase5++;
                } else {
                    this.truePositivesCase4++;
                }
            } else if (z2 && !z3) {
                this.falsePositives++;
                if (z) {
                    this.falsePositivesCase1++;
                } else {
                    this.falsePositivesCase8++;
                }
            } else if (!z2 && z3) {
                this.falseNegatives++;
                if (z) {
                    this.falseNegativesCase7++;
                } else {
                    this.falseNegativesCase2++;
                }
            } else if (!z2 && !z3) {
                this.trueNegatives++;
                if (z) {
                    this.trueNegativesCase3++;
                } else {
                    this.trueNegativesCase6++;
                }
            }
            this.inferenceCount++;
        }

        public double accuracy() {
            int i = this.inferenceCount;
            if (i == 0) {
                return 0.0d;
            }
            return ((double) ((i - this.falsePositives) - this.falseNegatives)) / ((double) i);
        }

        public double precision() {
            int i = this.truePositives;
            int i2 = this.falsePositives;
            if (i + i2 == 0) {
                return 0.0d;
            }
            return ((double) i) / ((double) (i + i2));
        }

        public double recall() {
            int i = this.truePositives;
            int i2 = this.falseNegatives;
            if (i + i2 == 0) {
                return 0.0d;
            }
            return ((double) i) / ((double) (i + i2));
        }

        public double f1Score() {
            double dPrecision = precision();
            double dRecall = recall();
            double d = dPrecision + dRecall;
            if (d == 0.0d) {
                return 0.0d;
            }
            return ((dPrecision * dRecall) * 2.0d) / d;
        }

        public void reset() {
            this.truePositives = 0;
            this.falsePositives = 0;
            this.falseNegatives = 0;
            this.trueNegatives = 0;
            this.falsePositivesCase1 = 0;
            this.falsePositivesCase8 = 0;
            this.falseNegativesCase2 = 0;
            this.falseNegativesCase7 = 0;
            this.truePositivesCase4 = 0;
            this.truePositivesCase5 = 0;
            this.trueNegativesCase3 = 0;
            this.trueNegativesCase6 = 0;
            this.inferenceCount = 0;
        }

        public String toString() {
            return "EvaluationCriteria{True Positives=" + this.truePositives + ", False Positives=" + this.falsePositives + ", False Negatives=" + this.falseNegatives + ", True Negatives=" + this.trueNegatives + ", falsePositivesCase1=" + this.falsePositivesCase1 + ", falsePositivesCase8 =" + this.falsePositivesCase8 + ", falseNegativesCase2 =" + this.falseNegativesCase2 + ", falseNegativesCase7 =" + this.falseNegativesCase7 + ", truePositivesCase4=" + this.truePositivesCase4 + ", truePositivesCase5=" + this.truePositivesCase5 + ", trueNegativesCase3=" + this.trueNegativesCase3 + ", trueNegativesCase6=" + this.trueNegativesCase6 + ", Accuracy=" + accuracy() + ", Precision=" + precision() + ", Recall=" + recall() + ", F1 Score=" + f1Score() + ", inferenceCount=" + this.inferenceCount + '}';
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

    /* JADX INFO: Access modifiers changed from: private */
    public void recordModemAiWeakSignalMetrics() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("WeakSignalPrediction", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logD(String str) {
        if (VDBG) {
            Log.d("WeakSignalPrediction", str);
        }
    }
}
