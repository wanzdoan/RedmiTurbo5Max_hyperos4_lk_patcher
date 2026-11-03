package com.android.phone;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.OutcomeReceiver;
import android.telephony.ModemActivityInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Slog;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class ModemPowerEstimate implements MiuiAppStateMonitor.TopAppObserver {
    private static ModemPowerEstimate mInstance = null;
    private static int mOnnxRuntimeLoadRetryCount = 0;
    protected static boolean mOnnxRuntimeLoaded = false;
    private Context mContext;
    private ScheduledExecutorService mForcastExecutor;
    private ScheduledExecutorService mGetMddataExecutor;
    public Handler mHandler;
    private volatile boolean mIsGetModemPower;
    private boolean mIsMobileDataOn;
    private volatile boolean mIsModelRunning;
    private boolean mIsScreenOn;
    private OrtEnvironment mOrtEnv;
    private OrtSession mOrtSession;
    private OrtSession.SessionOptions mOrtSessionOptions;
    private TelephonyManager mTelephonyManager;
    private HandlerThread mWorkThread;
    private final int mInputLen = 1;
    private final int mTimeLen = 5;
    private final int mFeatureLen = 29;
    private float[][] mInputValues = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 1, 29);
    private final int mOriginParamLen = 100;
    private Deque mFixedSizeQueue = new ArrayDeque(5);
    private final int MSG_MODEM_ACTIVITY_RETURNED = 3;
    private final int MSG_IDEL_CHANGE = 4;
    private final int DELAY_TIME = 10000;
    private ModemActivityInfo mLastModemActivityInfo = null;
    private ModemActivityInfo mDeltaModemActivityInfo = null;
    private boolean mIsInWhilteList = false;
    private final OutcomeReceiver mOutcomeReceiver = new OutcomeReceiver() { // from class: com.android.phone.ModemPowerEstimate.1
        @Override // android.os.OutcomeReceiver
        public void onResult(ModemActivityInfo modemActivityInfo) {
            ModemPowerEstimate.this.mHandler.obtainMessage(3, modemActivityInfo).sendToTarget();
        }

        @Override // android.os.OutcomeReceiver
        public void onError(TelephonyManager.ModemActivityInfoException modemActivityInfoException) {
            ModemPowerEstimate.this.log("error reading modem stats:" + modemActivityInfoException);
            ModemPowerEstimate.this.mHandler.obtainMessage(3, null).sendToTarget();
        }
    };
    private final Runnable getModemPowerParam = new Runnable() { // from class: com.android.phone.ModemPowerEstimate$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final Runnable mForecast = new Runnable() { // from class: com.android.phone.ModemPowerEstimate$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$1();
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.ModemPowerEstimate.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                boolean zIsMobileDataConnected = MiuiPhoneUtils.isMobileDataConnected();
                if (ModemPowerEstimate.this.mIsMobileDataOn != zIsMobileDataConnected) {
                    ModemPowerEstimate.this.mIsMobileDataOn = zIsMobileDataConnected;
                    ModemPowerEstimate.this.log("Connectivity changed");
                    ModemPowerEstimate.this.onScenenChanged();
                    return;
                }
                return;
            }
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                ModemPowerEstimate.this.onScenenChanged();
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ModemPowerEstimate.3
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Handler handler;
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (!displayEvent.isDefaultDisplay() || (handler = ModemPowerEstimate.this.mHandler) == null) {
                    return;
                }
                handler.sendMessage(handler.obtainMessage(5, displayEvent));
            }
        }
    };

    public static synchronized ModemPowerEstimate getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new ModemPowerEstimate();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private ModemPowerEstimate() {
        log("ModemPowerEstimate init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("ModemPowerEstimate", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new ModemPowerHandler(this.mWorkThread.getLooper());
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        registerAllEvents();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (!isSholudRunModel()) {
            stopRun();
            return;
        }
        this.mTelephonyManager.requestModemActivityInfo(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), this.mOutcomeReceiver);
        log("Delta ModemActivityInfo=" + this.mDeltaModemActivityInfo);
        if (this.mDeltaModemActivityInfo == null) {
            return;
        }
        Object[] objArr = new Object[1];
        String[] strArr = {"AT+EDMFAPP=20107,200,0,0,0,0,0", "+EDMFAPP"};
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone != null) {
            FiveGManagerBase.getInstance().sendAtCmdSync(dataPhone.getPhoneId(), strArr, objArr);
            processInputData((String[]) objArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.getModemPowerParam.run();
        if (this.mFixedSizeQueue.size() == 5) {
            getMeanValue();
            normalization();
            log("forecast results=" + Arrays.deepToString(runPowerAiModel()));
        }
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        registerDeviceState();
    }

    private void registerDeviceState() {
        MiuiAppStateMonitor.init().registerAppObserver(this);
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
    }

    class ModemPowerHandler extends Handler {
        ModemPowerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemPowerEstimate.this.log("handleMessage msg=" + message.what);
            int i = message.what;
            if (i == 3) {
                ModemActivityInfo modemActivityInfo = (ModemActivityInfo) message.obj;
                ModemPowerEstimate modemPowerEstimate = ModemPowerEstimate.this;
                if (modemActivityInfo != null) {
                    modemPowerEstimate.log("handleMessage ModemActivityInfo=" + modemActivityInfo);
                    if (ModemPowerEstimate.this.mLastModemActivityInfo != null) {
                        ModemPowerEstimate modemPowerEstimate2 = ModemPowerEstimate.this;
                        modemPowerEstimate2.mDeltaModemActivityInfo = modemPowerEstimate2.mLastModemActivityInfo.getDelta(modemActivityInfo);
                    }
                    ModemPowerEstimate.this.mLastModemActivityInfo = modemActivityInfo;
                    return;
                }
                modemPowerEstimate.log("handleMessage ModemActivityInfo is null");
                return;
            }
            if (i == 4) {
                ModemPowerEstimate.this.startRun();
                return;
            }
            if (i != 5) {
                if (i != 101) {
                    return;
                }
                ModemPowerEstimate.this.onScenenChanged();
                return;
            }
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) message.obj;
            boolean z = ModemPowerEstimate.this.mIsScreenOn;
            ModemPowerEstimate.this.mIsScreenOn = displayEvent.isScreenOn();
            ModemPowerEstimate.this.log("onDisplayChanged mIsScreenOn=" + ModemPowerEstimate.this.mIsScreenOn);
            if (z != ModemPowerEstimate.this.mIsScreenOn) {
                boolean z2 = ModemPowerEstimate.this.mIsScreenOn;
                ModemPowerEstimate modemPowerEstimate3 = ModemPowerEstimate.this;
                if (z2) {
                    modemPowerEstimate3.mHandler.sendEmptyMessageDelayed(101, 10000L);
                    return;
                }
                if (modemPowerEstimate3.mHandler.hasMessages(101)) {
                    ModemPowerEstimate.this.mHandler.removeMessages(101);
                }
                ModemPowerEstimate.this.onScenenChanged();
            }
        }
    }

    @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
    public void onTopAppChanged(final List list, int i) {
        log("onTopAppChanged mode=" + i);
        if (i > 0) {
            log("onTopAppChanged ignore another mode");
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemPowerEstimate$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTopAppChanged$3(list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopAppChanged$3(List list) {
        ArrayList arrayList = (ArrayList) list;
        printList(arrayList);
        if (arrayList != null) {
            final String str = "config_powerAi_packages";
            if (arrayList.stream().allMatch(new Predicate() { // from class: com.android.phone.ModemPowerEstimate$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ModemPowerEstimate.$r8$lambda$fJfDcMfDM_68X7fUqS1Xb_tKVUk(str, (ComponentName) obj);
                }
            })) {
                this.mIsInWhilteList = true;
            } else {
                this.mIsInWhilteList = false;
            }
            log("onTopAppChanged mIsInWhilteList=" + this.mIsInWhilteList);
            boolean z = this.mIsInWhilteList;
            Handler handler = this.mHandler;
            if (z) {
                handler.sendEmptyMessageDelayed(101, 10000L);
                return;
            }
            if (handler.hasMessages(101)) {
                this.mHandler.removeMessages(101);
            }
            onScenenChanged();
        }
    }

    public static /* synthetic */ boolean $r8$lambda$fJfDcMfDM_68X7fUqS1Xb_tKVUk(String str, ComponentName componentName) {
        return componentName != null && MiuiPhoneUtils.isContainsPackage(str, componentName.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScenenChanged() {
        if (isSholudRunModel()) {
            startRun();
        } else {
            stopRun();
        }
    }

    private boolean isSholudRunModel() {
        boolean z = MiuiPhoneUtils.getInsertedSimCount() == 1;
        this.mIsMobileDataOn = MiuiPhoneUtils.isMobileDataConnected();
        log("mIsInWhilteList=" + this.mIsInWhilteList + " mIsMobileDataOn=" + this.mIsMobileDataOn + " mIsSingleSim=" + z + " mIsScreenOn=" + this.mIsScreenOn);
        return this.mIsInWhilteList && this.mIsMobileDataOn && z && this.mIsScreenOn;
    }

    private static synchronized boolean loadOnnxRuntime() {
        if (mOnnxRuntimeLoaded) {
            return true;
        }
        if (mOnnxRuntimeLoadRetryCount >= 1) {
            return false;
        }
        try {
            sLog("loadOnnxRuntime");
            System.loadLibrary("onnxruntime4j_jni");
            mOnnxRuntimeLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            sLog("loadOnnxRuntime failed, retryCount=" + mOnnxRuntimeLoadRetryCount + " " + e);
            mOnnxRuntimeLoadRetryCount = mOnnxRuntimeLoadRetryCount + 1;
        }
        return mOnnxRuntimeLoaded;
    }

    private void initAiEnv() {
        try {
            if (!loadOnnxRuntime()) {
                sLog("initAiEnv: onnxRuntime load failed");
                return;
            }
            this.mOrtEnv = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
            this.mOrtSessionOptions = sessionOptions;
            this.mOrtSession = this.mOrtEnv.createSession("system_ext/etc/best_model_green_area_Both.onnx", sessionOptions);
            log("initAiEnv success");
        } catch (Exception e) {
            log("initAiEnv Exception failed " + e);
        }
    }

    private void closeAiEnv() {
        try {
            OrtSession ortSession = this.mOrtSession;
            if (ortSession != null) {
                ortSession.close();
            }
            OrtEnvironment ortEnvironment = this.mOrtEnv;
            if (ortEnvironment != null) {
                ortEnvironment.close();
            }
            this.mOrtSessionOptions = null;
            log("closeAiEnv success");
        } catch (Exception e) {
            log("closeAiEnv Exception failed " + e);
        }
    }

    public void startRun() {
        if (!isSholudRunModel()) {
            log("is no need to start");
            return;
        }
        log("start run + mIsModelRunning" + this.mIsModelRunning);
        if (this.mIsModelRunning) {
            log("already start");
            return;
        }
        initAiEnv();
        this.mFixedSizeQueue.clear();
        this.mInputValues = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 1, 29);
        ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(4);
        this.mForcastExecutor = scheduledExecutorServiceNewScheduledThreadPool;
        scheduledExecutorServiceNewScheduledThreadPool.scheduleAtFixedRate(this.mForecast, 0L, 1L, TimeUnit.SECONDS);
        this.mIsModelRunning = true;
    }

    public void stopRun() {
        log("stop run + mIsModelRunning" + this.mIsModelRunning);
        stopGetParms();
        if (!this.mIsModelRunning) {
            log("already stop");
            return;
        }
        ScheduledExecutorService scheduledExecutorService = this.mForcastExecutor;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        closeAiEnv();
        this.mIsModelRunning = false;
    }

    public void stopGetParms() {
        ScheduledExecutorService scheduledExecutorService;
        if (!this.mIsGetModemPower || (scheduledExecutorService = this.mGetMddataExecutor) == null) {
            return;
        }
        scheduledExecutorService.shutdown();
        log("stopGetParms ");
        this.mIsGetModemPower = false;
    }

    private float[][] runPowerAiModel() {
        log("runPowerAiModel input values=" + Arrays.deepToString(this.mInputValues));
        float[][] fArr = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 1, 1);
        try {
            fArr = (float[][]) this.mOrtSession.run(Collections.singletonMap("input", OnnxTensor.createTensor(this.mOrtEnv, this.mInputValues))).get(0).getValue();
        } catch (OrtException e) {
            log("runPowerAiModel OrtException failed " + e);
        }
        log("runPowerAiModel success");
        return fArr;
    }

    private void processInputData(String[] strArr) {
        log("processInputData data=" + Arrays.toString(strArr));
        String[] strArrSplit = (strArr != null ? String.join(",", strArr) : null).replaceAll("[^0-9,-]", "").split(",");
        try {
            if (strArrSplit.length == 100) {
                float[] fArr = new float[100];
                for (int i = 0; i < 100; i++) {
                    fArr[i] = Float.parseFloat(strArrSplit[i]);
                }
                log("processInputData floatParams=" + Arrays.toString(fArr));
                Arrays.copyOfRange(fArr, 8, 23);
                float[] fArrCopyOfRange = Arrays.copyOfRange(fArr, 23, 38);
                float[] fArrCopyOfRange2 = Arrays.copyOfRange(fArr, 38, 53);
                float[] fArrCopyOfRange3 = Arrays.copyOfRange(fArr, 53, 68);
                float[] fArrCopyOfRange4 = Arrays.copyOfRange(fArr, 68, 73);
                float[] fArrCopyOfRange5 = Arrays.copyOfRange(fArr, 73, 78);
                ArrayList arrayList = new ArrayList();
                arrayList.add(Float.valueOf(getBandWidth(fArrCopyOfRange, 0)));
                float f = fArr[1];
                arrayList.add(Float.valueOf(0.0f == f ? getLastValue(1) : fArr[87] / f));
                float f2 = fArr[2];
                arrayList.add(Float.valueOf(0.0f == f2 ? getLastValue(2) : fArr[88] / f2));
                arrayList.add(Float.valueOf(getModemValue(fArrCopyOfRange2, 3)));
                arrayList.add(Float.valueOf(fArr[78]));
                arrayList.add(Float.valueOf(getModemValue(fArrCopyOfRange5, 5)));
                float f3 = fArr[2];
                arrayList.add(Float.valueOf(0.0f == f3 ? getLastValue(6) : fArr[89] / f3));
                arrayList.add(Float.valueOf(getModemValue(fArrCopyOfRange4, 7)));
                arrayList.add(Float.valueOf(getModemValue(fArrCopyOfRange3, 8)));
                float f4 = fArr[4];
                arrayList.add(Float.valueOf(0.0f == f4 ? getLastValue(9) : fArr[85] / f4));
                float f5 = fArr[4];
                arrayList.add(Float.valueOf(0.0f == f5 ? getLastValue(10) : fArr[83] / f5));
                float f6 = fArr[3];
                arrayList.add(Float.valueOf(0.0f == f6 ? getLastValue(11) : fArr[90] / f6));
                float f7 = fArr[3];
                arrayList.add(Float.valueOf(0.0f == f7 ? getLastValue(12) : fArr[91] / f7));
                float f8 = fArr[5];
                arrayList.add(Float.valueOf(0.0f == f8 ? getLastValue(13) : fArr[81] / f8));
                arrayList.add(Float.valueOf(fArr[80]));
                arrayList.add(Float.valueOf(fArr[92]));
                float f9 = fArr[7];
                arrayList.add(Float.valueOf(0.0f == f9 ? getLastValue(16) : fArr[86] / f9));
                float f10 = fArr[6];
                arrayList.add(Float.valueOf(0.0f == f10 ? getLastValue(17) : fArr[82] / f10));
                float f11 = fArr[95];
                arrayList.add(Float.valueOf(0.0f == f11 ? getLastValue(18) : fArr[93] / f11));
                float f12 = fArr[95];
                arrayList.add(Float.valueOf(0.0f == f12 ? getLastValue(19) : fArr[94] / f12));
                if (fArr[96] != 4.0f) {
                    log("nr_rrc_state is not 4");
                    if (!this.mIsGetModemPower) {
                        stopRun();
                        log("enter idel state, begin to collect modem power data every 10 seconds");
                        ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(1);
                        this.mGetMddataExecutor = scheduledExecutorServiceNewScheduledThreadPool;
                        scheduledExecutorServiceNewScheduledThreadPool.scheduleAtFixedRate(this.getModemPowerParam, 0L, 10L, TimeUnit.SECONDS);
                        this.mIsGetModemPower = true;
                    }
                    if (this.mHandler.hasMessages(4)) {
                        log("conect state is less 10s, remove MSG_IDEL_CHANGE");
                        this.mHandler.removeMessages(4);
                    }
                } else {
                    log("nr_rrc_state is 4");
                    if (this.mIsGetModemPower) {
                        stopGetParms();
                    }
                    if (!this.mIsModelRunning) {
                        this.mHandler.sendEmptyMessageDelayed(4, 10000L);
                    }
                }
                arrayList.add(Float.valueOf(fArr[98]));
                arrayList.add(Float.valueOf(fArr[99]));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getSleepTimeMillis()));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getIdleTimeMillis()));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getTransmitTimeMillis()[0]));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getTransmitTimeMillis()[1]));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getTransmitTimeMillis()[2]));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getTransmitTimeMillis()[3]));
                arrayList.add(Float.valueOf(this.mDeltaModemActivityInfo.getReceiveTimeMillis()));
                log("processInputData currentValue=" + arrayList);
                addElement(arrayList);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
    }

    private void addElement(ArrayList arrayList) {
        synchronized (this.mFixedSizeQueue) {
            try {
                log("addElement");
                if (this.mFixedSizeQueue.size() == 5) {
                    this.mFixedSizeQueue.clear();
                }
                this.mFixedSizeQueue.offerLast(arrayList);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void getMeanValue() {
        synchronized (this.mFixedSizeQueue) {
            try {
                log("getInputMeanValue");
                for (int i = 0; i < 29; i++) {
                    Iterator it = this.mFixedSizeQueue.iterator();
                    float fFloatValue = 0.0f;
                    while (it.hasNext()) {
                        fFloatValue += ((Float) ((List) it.next()).get(i)).floatValue();
                    }
                    this.mInputValues[0][i] = fFloatValue / 5.0f;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void normalization() {
        double[] dArr = {7.101891822609576d, 49.19721609843609d, 53.23848244039971d, 12.988843886182872d, 11.31853370930523d, 1.3418918226096093d, 36.915719046398266d, 0.4510176877723673d, 11.008141502178912d, -70.55103604328116d, 16.565368613395293d, 2174.411581645753d, 3.9861471417584817d, 8.784649892342836d, 89.69843629838503d, 187.84781338118282d, 15.803702383442683d, -1.6266704548139383d, 21.457165888701446d, -81.84507518904927d, 183444.15589848722d, 1507757.2029326071d, 127.34418866957205d, 213.07383747756901d, 25.317139195078294d, 4.385306331709773d, 20.76724942322506d, 7.569484747500587d, 659.2888797744104d};
        double[] dArr2 = {3.637129585571411d, 39.20976532626786d, 48.77709230692732d, 0.05321377838846615d, 8.564276717063862d, 1.171787036909517d, 25.874131886376883d, 0.469385888119843d, 1.0044654752780375d, 13.971667170928642d, 11.387159192372069d, 1762.831035167504d, 4.6719885382121955d, 8.355552309973046d, 81.85545503640276d, 255.19913812738787d, 8.735972015161328d, 17.394398129123662d, 13.962429955347085d, 19.15525084103539d, 165010.76936226175d, 1600105.3665880628d, 193.55037932348824d, 124.40591405309092d, 43.80253186182881d, 11.22334109006896d, 29.825733517345945d, 12.2712905606402d, 242.11476341779687d};
        synchronized (this.mFixedSizeQueue) {
            try {
                log("normalization");
                for (int i = 0; i < 29; i++) {
                    float[] fArr = this.mInputValues[0];
                    fArr[i] = (float) ((((double) fArr[i]) - dArr[i]) / dArr2[i]);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private float getBandWidth(float[] fArr, int i) {
        float[] fArr2 = {5.0f, 10.0f, 15.0f, 20.0f, 25.0f, 30.0f, 40.0f, 50.0f, 60.0f, 80.0f, 90.0f, 100.0f, 70.0f, 200.0f, 400.0f};
        int i2 = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i3 = 0; i3 < fArr.length; i3++) {
            float f3 = fArr[i3];
            f2 += fArr2[i3] * f3;
            f += f3;
        }
        if (0.0f == f) {
            return getLastValue(i);
        }
        float f4 = f2 / f;
        float f5 = Float.MAX_VALUE;
        int i4 = -1;
        for (int i5 = 0; i5 < 15; i5++) {
            float fAbs = Math.abs(fArr2[i5] - f4);
            if (fAbs < f5) {
                i4 = i5;
                f5 = fAbs;
            }
        }
        if (i4 < 0) {
            log("getBandWidth failed. closestIndex < 0");
        } else {
            i2 = i4;
        }
        return i2;
    }

    private float getModemValue(float[] fArr, int i) {
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i2 = 0; i2 < fArr.length; i2++) {
            float f3 = fArr[i2];
            f2 += i2 * f3;
            f += f3;
        }
        return 0.0f == f ? getLastValue(i) : f2 / f;
    }

    private float getLastValue(int i) {
        ArrayList arrayList;
        if (this.mFixedSizeQueue.isEmpty() || (arrayList = (ArrayList) this.mFixedSizeQueue.peekLast()) == null || arrayList.size() <= 0) {
            return 0.0f;
        }
        return ((Float) arrayList.get(i)).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("ModemPowerEstimate", str);
    }

    private static void sLog(String str) {
        Slog.d("ModemPowerEstimate", str);
    }

    private void printList(ArrayList arrayList) {
        log("printList");
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            log("printList name=" + ((ComponentName) obj).getPackageName());
        }
    }
}
