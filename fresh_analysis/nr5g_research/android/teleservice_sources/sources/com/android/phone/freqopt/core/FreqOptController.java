package com.android.phone.freqopt.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SignalStrength;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.debug.FreqOptDebugController;
import com.android.phone.freqopt.engine.ExecutionEngine;
import com.android.phone.freqopt.engine.HandoverMonitor;
import com.android.phone.freqopt.engine.LearningEngine;
import com.android.phone.freqopt.engine.PostExecutionMonitor;
import com.android.phone.freqopt.strategy.FddTddStrategy;
import com.android.phone.freqopt.strategy.N79HotspotStrategy;
import com.android.phone.freqopt.strategy.NrLteStrategy;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.ThresholdValidator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class FreqOptController {
    private static volatile FreqOptController sInstance;
    private FreqOptConfigManager mConfigManager;
    private FreqOptDBManager mDbManager;
    private FreqOptDebugController mDebugController;
    private ExecutionEngine mExecutionEngine;
    private FreqOptCellLocationListener mFreqOptCellLocationListener;
    private HandoverMonitor mHandoverMonitor;
    private LearningEngine mLearningEngine;
    private NrLteStrategy mNrLteStrategy;
    private PostExecutionMonitor mPostExecutionMonitor;
    private ThresholdValidator mValidator;
    private FreqOptWorkHandler mWorkHandler;
    private HandlerThread mWorkThread;
    private final Map mStrategies = new ConcurrentHashMap();
    private final Map mStrategyEnabled = new ConcurrentHashMap();
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.freqopt.core.FreqOptController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                LogUtils.w("Freq_Opt_Controller", "onReceive: null intent or action");
                return;
            }
            String action = intent.getAction();
            LogUtils.d("Freq_Opt_Controller", "receive action " + action);
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                FreqOptController.this.mWorkHandler.obtainMessage(2).sendToTarget();
            }
        }
    };

    private FreqOptController() {
        LogUtils.i("Freq_Opt_Controller", "init.");
    }

    public static synchronized void init() {
        if (sInstance == null) {
            FreqOptController freqOptController = new FreqOptController();
            if (freqOptController.initInternal()) {
                sInstance = freqOptController;
            }
        }
    }

    public static FreqOptController getInstance() {
        return sInstance;
    }

    private boolean initInternal() {
        HandlerThread handlerThread = new HandlerThread("Freq_Opt_Controller", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        Looper looper = this.mWorkThread.getLooper();
        if (looper == null) {
            LogUtils.e("Freq_Opt_Controller", "initInternal: failed to get looper from work thread");
            return false;
        }
        this.mWorkHandler = new FreqOptWorkHandler(looper, this);
        this.mValidator = new ThresholdValidator();
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        FreqOptDBManager freqOptDBManager = FreqOptDBManager.getInstance(phoneGlobals);
        this.mDbManager = freqOptDBManager;
        this.mLearningEngine = new LearningEngine(freqOptDBManager, this.mValidator);
        this.mExecutionEngine = new ExecutionEngine(this.mDbManager, this.mWorkHandler);
        HandoverMonitor handoverMonitor = new HandoverMonitor(this.mWorkHandler);
        this.mHandoverMonitor = handoverMonitor;
        this.mExecutionEngine.setHandoverMonitor(handoverMonitor);
        this.mPostExecutionMonitor = new PostExecutionMonitor(this.mWorkHandler);
        initMonitorCallbacks();
        this.mConfigManager = new FreqOptConfigManager(this.mStrategyEnabled, this.mValidator, this.mExecutionEngine, this.mHandoverMonitor, this.mPostExecutionMonitor, this.mStrategies, this.mWorkHandler);
        LogUtils.d("Freq_Opt_Controller", "registerFreqOptCellLocationListener");
        FreqOptCellLocationListener freqOptCellLocationListener = new FreqOptCellLocationListener(this);
        this.mFreqOptCellLocationListener = freqOptCellLocationListener;
        TelephonyStatAdapter.registerCallBack(freqOptCellLocationListener);
        LogUtils.d("Freq_Opt_Controller", "registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        PhoneGlobals.getInstance().registerReceiver(this.mIntentReceiver, intentFilter, 2);
        if (FreqOptUtils.isN79HostOptEnabled()) {
            registerStrategy(new N79HotspotStrategy());
        }
        if (FreqOptUtils.isFddTddOptEnabled()) {
            registerStrategy(new FddTddStrategy(this.mWorkHandler, phoneGlobals));
        }
        if (FreqOptUtils.isNrLteOptEnabled()) {
            NrLteStrategy nrLteStrategy = new NrLteStrategy(this.mWorkHandler);
            this.mNrLteStrategy = nrLteStrategy;
            this.mConfigManager.setNrLteStrategy(nrLteStrategy);
            this.mStrategyEnabled.put("nr_lte", Boolean.TRUE);
        }
        FreqOptDebugController freqOptDebugController = new FreqOptDebugController(this);
        this.mDebugController = freqOptDebugController;
        freqOptDebugController.init();
        LogUtils.i("Freq_Opt_Controller", "init done");
        return true;
    }

    private void initMonitorCallbacks() {
        this.mPostExecutionMonitor.setSignalProvider(new PostExecutionMonitor.SignalProvider() { // from class: com.android.phone.freqopt.core.FreqOptController$$ExternalSyntheticLambda0
            @Override // com.android.phone.freqopt.engine.PostExecutionMonitor.SignalProvider
            public final int[] getCurrentNrSignal(int i) {
                return FreqOptController.m2532$r8$lambda$hJmH4Nb5NwmYsRYUMW4yXBh8AQ(i);
            }
        });
        this.mPostExecutionMonitor.setOnPostFailureListener(new PostExecutionMonitor.OnPostFailureListener() { // from class: com.android.phone.freqopt.core.FreqOptController$$ExternalSyntheticLambda1
            @Override // com.android.phone.freqopt.engine.PostExecutionMonitor.OnPostFailureListener
            public final void onPostExecutionFailed(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i) {
                this.f$0.lambda$initMonitorCallbacks$1(str, freqOptLearningRecord, str2, i);
            }
        });
        this.mPostExecutionMonitor.setOnLeaveTargetListener(new PostExecutionMonitor.OnLeaveTargetListener() { // from class: com.android.phone.freqopt.core.FreqOptController$$ExternalSyntheticLambda2
            @Override // com.android.phone.freqopt.engine.PostExecutionMonitor.OnLeaveTargetListener
            public final void onLeaveTargetCell(String str, String str2, int i) {
                this.f$0.lambda$initMonitorCallbacks$2(str, str2, i);
            }
        });
        this.mHandoverMonitor.setOnFailureListener(new HandoverMonitor.OnFailureListener() { // from class: com.android.phone.freqopt.core.FreqOptController$$ExternalSyntheticLambda3
            @Override // com.android.phone.freqopt.engine.HandoverMonitor.OnFailureListener
            public final void onHandoverFailed(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i) {
                this.f$0.lambda$initMonitorCallbacks$3(str, freqOptLearningRecord, str2, i);
            }
        });
        this.mHandoverMonitor.setOnSuccessListener(new HandoverMonitor.OnSuccessListener() { // from class: com.android.phone.freqopt.core.FreqOptController$$ExternalSyntheticLambda4
            @Override // com.android.phone.freqopt.engine.HandoverMonitor.OnSuccessListener
            public final void onHandoverSuccess(String str, String str2, FreqOptLearningRecord freqOptLearningRecord, int i) {
                this.f$0.lambda$initMonitorCallbacks$4(str, str2, freqOptLearningRecord, i);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$hJmH4Nb5NwmYsRYUM-W4yXBh8AQ, reason: not valid java name */
    public static /* synthetic */ int[] m2532$r8$lambda$hJmH4Nb5NwmYsRYUMW4yXBh8AQ(int i) {
        SignalStrength signalStrength;
        List cellSignalStrengths;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null || (cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class)) == null || cellSignalStrengths.isEmpty()) {
            return null;
        }
        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrengths.get(0);
        return new int[]{cellSignalStrengthNr.getSsRsrp(), cellSignalStrengthNr.getSsSinr()};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initMonitorCallbacks$1(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i) {
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mStrategies.get(str);
        if (iFreqOptStrategy != null) {
            this.mExecutionEngine.onExecutionFailed(iFreqOptStrategy, freqOptLearningRecord, str2, i);
            return;
        }
        LogUtils.w("Freq_Opt_Controller", "onPostFailure: strategy not found: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initMonitorCallbacks$3(String str, FreqOptLearningRecord freqOptLearningRecord, String str2, int i) {
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mStrategies.get(str);
        if (iFreqOptStrategy != null) {
            this.mExecutionEngine.handleHandoverFailure(iFreqOptStrategy, freqOptLearningRecord, str2, i);
            return;
        }
        LogUtils.w("Freq_Opt_Controller", "onHandoverFailure: strategy not found: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initMonitorCallbacks$4(String str, String str2, FreqOptLearningRecord freqOptLearningRecord, int i) {
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mStrategies.get(str);
        if (iFreqOptStrategy == null || !iFreqOptStrategy.needsPostExecutionMonitoring()) {
            return;
        }
        this.mPostExecutionMonitor.startMonitoring(str, str2, freqOptLearningRecord, i);
    }

    public void registerStrategy(IFreqOptStrategy iFreqOptStrategy) {
        String strategyId = iFreqOptStrategy.getStrategyId();
        this.mStrategies.put(strategyId, iFreqOptStrategy);
        this.mStrategyEnabled.put(strategyId, Boolean.TRUE);
        LogUtils.i("Freq_Opt_Controller", "registerStrategy: " + strategyId);
    }

    class FreqOptCellLocationListener implements TelephonyStatAdapter.ICallBack {
        private final FreqOptController mController;

        FreqOptCellLocationListener(FreqOptController freqOptController) {
            this.mController = freqOptController;
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            FreqOptWorkHandler workHandler = this.mController.getWorkHandler();
            if (workHandler == null || i != workHandler.getDdsPhoneId() || workHandler.isDdsChanging()) {
                return;
            }
            if (MiuiPhoneUtils.isPhoneInCall()) {
                LogUtils.d("Freq_Opt_Controller", "onCellLocationChanged ignored: phone in call");
            } else {
                if (cellLocation == null || cellLocation.isEmpty()) {
                    return;
                }
                workHandler.obtainMessage(1, i, 0).sendToTarget();
            }
        }
    }

    public FreqOptLearningRecord onCellTransition(String str, String str2, String str3, CellIdentity cellIdentity, int i, int i2, long j, long j2, int i3) {
        if (!isStrategyActive(str)) {
            return null;
        }
        return this.mLearningEngine.learn((IFreqOptStrategy) this.mStrategies.get(str), str2, str3, cellIdentity, i, i2, j, j2, i3);
    }

    public boolean tryExecute(String str, String str2, int i, int i2) {
        if (!isStrategyActive(str)) {
            return false;
        }
        return this.mExecutionEngine.execute((IFreqOptStrategy) this.mStrategies.get(str), str2, i, i2);
    }

    /* JADX INFO: renamed from: cancelExecution, reason: merged with bridge method [inline-methods] */
    public void lambda$initMonitorCallbacks$2(String str, String str2, int i) {
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mStrategies.get(str);
        if (iFreqOptStrategy != null) {
            this.mExecutionEngine.cancel(iFreqOptStrategy, str2, i);
            return;
        }
        LogUtils.w("Freq_Opt_Controller", "cancelExecution: strategy not found: " + str);
    }

    public void updateFreqOptCloudConfig(Map map) {
        this.mConfigManager.updateCloudConfig(map);
    }

    public boolean isStrategyActive(String str) {
        return this.mConfigManager.isStrategyActive(str);
    }

    public FreqOptDBManager getDbManager() {
        return this.mDbManager;
    }

    public ExecutionEngine getExecutionEngine() {
        return this.mExecutionEngine;
    }

    public HandoverMonitor getHandoverMonitor() {
        return this.mHandoverMonitor;
    }

    public PostExecutionMonitor getPostExecutionMonitor() {
        return this.mPostExecutionMonitor;
    }

    public LearningEngine getLearningEngine() {
        return this.mLearningEngine;
    }

    public ThresholdValidator getValidator() {
        return this.mValidator;
    }

    FreqOptWorkHandler getWorkHandler() {
        return this.mWorkHandler;
    }

    public Map getStrategies() {
        return this.mStrategies;
    }

    public NrLteStrategy getNrLteStrategy() {
        return this.mNrLteStrategy;
    }

    public FreqOptDebugController getDebugController() {
        return this.mDebugController;
    }

    public Handler getWorkThreadHandler() {
        return this.mWorkHandler;
    }

    public void dispatchDebugCommand(Intent intent) {
        FreqOptWorkHandler freqOptWorkHandler = this.mWorkHandler;
        if (freqOptWorkHandler != null) {
            freqOptWorkHandler.obtainMessage(100, intent).sendToTarget();
        }
    }
}
