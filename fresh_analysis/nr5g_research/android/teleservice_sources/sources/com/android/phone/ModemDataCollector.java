package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.network.ModemFiveGAScheduleInfo;
import com.android.phone.network.ModemQoeMetrics;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import miui.telephony.DefaultSimManager;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class ModemDataCollector extends ModemDataCollectorBase {
    private static boolean VDBG = false;
    private static boolean mIsNeedDump = false;
    private static int mMaxErrorCount = 3;
    private static int mReportErrorCount;
    private Context mContext;
    private int mDdsPhoneId;
    private final BroadcastReceiver mDebugReceiver;
    private volatile boolean mIsScreenOn;
    private Handler mMDCHandler;
    private HandlerThread mMDCThread;
    private ModemQoeMetrics mModemQoeMetrics;
    private ModemUtils mModemUtils;
    private final BroadcastReceiver mReceiver;
    private int mCollectGap = -1;
    private volatile boolean mMiRilHookReady = false;
    private volatile boolean mIsReporting = false;
    private String mSimState = "";
    private int mErrorCount = 0;
    private boolean mIsInErrorState = false;
    private BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ModemDataCollector$$ExternalSyntheticLambda0
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public final void onEvent(BaseEvent baseEvent) {
            this.f$0.lambda$new$1(baseEvent);
        }
    };

    class RilHookListener implements BaseEvent.IEventListener {
        private RilHookListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            ModemDataCollector.this.mMiRilHookReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
            boolean z = ModemDataCollector.this.mMiRilHookReady;
            ModemDataCollector modemDataCollector = ModemDataCollector.this;
            if (z) {
                modemDataCollector.mMDCHandler.sendEmptyMessage(1);
                ModemDataCollector.log("onMiRilHookReady");
            } else {
                modemDataCollector.mMDCHandler.sendEmptyMessage(2);
                ModemDataCollector.log("onMiRilHookDisconnected");
            }
        }
    }

    public static ModemDataCollectorBase make() {
        ModemDataCollectorBase modemDataCollectorBase;
        synchronized (ModemDataCollector.class) {
            try {
                if (ModemDataCollectorBase.mInstance == null) {
                    ModemDataCollectorBase.mInstance = new ModemDataCollector();
                } else {
                    log("ModemDataCollector is already init !");
                }
                modemDataCollectorBase = ModemDataCollectorBase.mInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return modemDataCollectorBase;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regHook() {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils != null) {
            if (modemUtils.registerOemHookRegistrant(this.mMDCHandler, 0, null, "xiaomi.intent.action.ACTION_MODEM_DATA_COLLECTOR", 0)) {
                log("registerOemHookRegistrant modem data collector success");
                this.mMDCHandler.sendEmptyMessageDelayed(3, 2000L);
            } else {
                log("registerOemHookRegistrant modem data collector fail");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegHook() {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils != null) {
            modemUtils.unregisterOemHookRegistrant(this.mMDCHandler, 0, null);
            log("unRegisterOemHookRegistrant");
        }
    }

    private ModemDataCollector() {
        this.mContext = null;
        this.mDdsPhoneId = -1;
        this.mModemUtils = null;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.ModemDataCollector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
                    ModemDataCollector.this.mMDCHandler.sendEmptyMessage(4);
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    ModemDataCollector.this.mMDCHandler.obtainMessage(6, intent).sendToTarget();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.android.phone.ModemDataCollector.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("com.android.phone.intent.action.MDC_DEBUG".equals(intent.getAction())) {
                    ModemDataCollector.VDBG = true;
                    ModemDataCollector.this.mMDCHandler.obtainMessage(100, intent.getIntExtra("testMsg", 0), intent.getIntExtra("testAdd", 0)).sendToTarget();
                }
            }
        };
        this.mDebugReceiver = broadcastReceiver2;
        log("ModemDataCollector init");
        HandlerThread handlerThread = new HandlerThread("ModemDataCollector", 10);
        this.mMDCThread = handlerThread;
        handlerThread.start();
        this.mMDCHandler = new MDCHandler(this.mMDCThread.getLooper());
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mModemUtils = ModemUtils.init(phoneGlobals);
        this.mDdsPhoneId = getDdsPhoneId();
        this.mModemQoeMetrics = ModemQoeMetrics.getInstance();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        if (Build.IS_DEBUGGABLE || "userroot".equals(Build.TYPE)) {
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.android.phone.intent.action.MDC_DEBUG");
            this.mContext.registerReceiver(broadcastReceiver2, intentFilter2, 2);
        }
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        mIsNeedDump = MiuiPhoneUtils.isSupportMDC();
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookListener());
        this.mMDCHandler.postDelayed(new Runnable() { // from class: com.android.phone.ModemDataCollector$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        }, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseEvent baseEvent) {
        if ((baseEvent instanceof DeviceStateEvent.DisplayEvent) && ((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
            this.mMDCHandler.obtainMessage(5).sendToTarget();
        }
    }

    class MDCHandler extends Handler {
        MDCHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 0:
                        Object obj = message.obj;
                        if (obj != null) {
                            ModemDataCollector.this.receiveModemData((AsyncResult) obj);
                        } else {
                            ModemDataCollector.log("modem data is null");
                        }
                        break;
                    case 1:
                        ModemDataCollector.this.regHook();
                        break;
                    case 2:
                        ModemDataCollector.this.unRegHook();
                        break;
                    case 3:
                        ModemDataCollector.this.startReportForPeriod();
                        break;
                    case 4:
                        ModemDataCollector.log("EVENT_DATA_SLOT_CHANGED");
                        ModemDataCollector.this.notifyDataSlotChanged();
                        if (ModemDataCollector.mIsNeedDump) {
                            ModemDataCollector modemDataCollector = ModemDataCollector.this;
                            modemDataCollector.notifyStartCollectData(4, 2, modemDataCollector.mDdsPhoneId);
                            ModemDataCollector.log("Data slot changed, notify BP reporter");
                        }
                        break;
                    case 5:
                        boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(ModemDataCollector.this.mContext);
                        if (ModemDataCollector.this.mIsScreenOn != zIsScreenOn) {
                            ModemDataCollector.this.mIsScreenOn = zIsScreenOn;
                            ModemDataCollector.this.processScreenStateChanged();
                        }
                        break;
                    case 6:
                        ModemDataCollector.this.processSimStateChanged((Intent) message.obj);
                        break;
                }
                return;
            }
            ModemDataCollector.this.processDebug(message.arg1, message.arg2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChanged() {
        if (mIsNeedDump) {
            if (!this.mIsScreenOn) {
                log("screen is off");
                stopReportForPeriod();
            } else {
                log("screen is on");
                if ("ABSENT".equals(this.mSimState)) {
                    return;
                }
                startReportForPeriod();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSimStateChanged(Intent intent) {
        if (mIsNeedDump) {
            String stringExtra = intent.getStringExtra("ss");
            if (stringExtra.equals(this.mSimState)) {
                return;
            }
            this.mSimState = stringExtra;
            int intExtra = intent.getIntExtra("phone", -1);
            if (SubscriptionManager.isValidPhoneId(intExtra)) {
                log("onSimStateChanged simState=" + stringExtra + ", phoneId=" + intExtra + ", mDdsPhoneId=" + this.mDdsPhoneId);
                if ("ABSENT".equals(stringExtra) && intExtra == this.mDdsPhoneId) {
                    stopReportForPeriod();
                } else if (this.mIsScreenOn) {
                    startReportForPeriod();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDebug(int i, int i2) {
        switch (i) {
            case 1:
                logD("Open register callback");
                startModemCallback();
                break;
            case 2:
                logD("Close register callback");
                stopModemCallback();
                break;
            case 3:
                logD("SUB1 Data");
                notifyStartCollectData(2, 999, 0);
                break;
            case 4:
                logD("SUB2 Data");
                notifyStartCollectData(3, 999, 1);
                break;
            case 5:
                logD("Report Period Data");
                startReportForPeriod();
                break;
            case 6:
                logD("STOP Report");
                stopReportForPeriod();
                break;
            case 7:
                logD("GET ModemAI Data Once");
                updateModemAiData();
                break;
            case 8:
                logD("Start Qos Monitor");
                startQosMonitor();
                break;
            case 9:
                logD("Stop Qos Monitor");
                stopQosMonitor();
                break;
            case 10:
                logD("Get Call End Info");
                getCallEndInfo();
                break;
            case 11:
                logD("Get 5GA Schedule Info");
                get5GAScheduleInfo();
                break;
            case 12:
                logD("Test 5GA Stats");
                process5GABandStatsTest(i2);
                break;
            case 13:
                logD("Test Wechat UL Enhance");
                processWechatUlEnhanceTest(i2);
                break;
        }
    }

    private void process5GABandStatsTest(int i) {
        if (i == 11) {
            logD("Start 5GA Stats Callback");
            lambda$new$0();
        } else if (i == 12) {
            logD("Stop 5GA Stats Callback");
            stop5GAStatsCallback();
        } else if (i == 10) {
            logD("Get 5GA Band Stats");
            get5GABandStats();
        }
    }

    private void processWechatUlEnhanceTest(int i) {
        if (i == 0) {
            logD("Start Wechat UL Enhance Callback");
            startWechatUlEnhanceCallback();
        } else if (i == 1) {
            logD("Stop Wechat UL Enhance Callback");
            stopWechatUlEnhanceCallback();
        } else if (i == 2) {
            logD("Get Wechat UL Enhance Status");
            isSupportWechatUlEnhance();
        }
    }

    private void handleModemAiData(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            log("handleModemAiData: byteBuffer is null");
            return;
        }
        byte modemAiCellAndSignal = parseModemAiCellAndSignal(byteBuffer);
        parseModemAiTxAndDlMetrics(byteBuffer, modemAiCellAndSignal);
        parseModemAiPdcpUlMetrics(byteBuffer, modemAiCellAndSignal);
        parseModemAiRlcMetrics(byteBuffer);
        dumpModemAiMetrics();
    }

    private byte parseModemAiCellAndSignal(ByteBuffer byteBuffer) {
        this.mModemQoeMetrics.setCellId(byteBuffer.getLong());
        this.mModemQoeMetrics.setDlEarfcn(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setUlEarfcn(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setBand(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setDlBw(byteBuffer.getShort() & 65535);
        this.mModemQoeMetrics.setUlBw(byteBuffer.getShort() & 65535);
        this.mModemQoeMetrics.setPci(byteBuffer.getShort() & 65535);
        byte b = byteBuffer.get();
        this.mModemQoeMetrics.setRat(b);
        this.mModemQoeMetrics.setRsrp(byteBuffer.getInt());
        this.mModemQoeMetrics.setRsrq(byteBuffer.getInt());
        int[] iArr = new int[4];
        int i = -40;
        for (int i2 = 0; i2 < 4; i2++) {
            int i3 = byteBuffer.getInt();
            iArr[i2] = i3;
            if (i3 != 65535 && i3 > i) {
                i = i3;
            }
        }
        this.mModemQoeMetrics.setSnr(i);
        this.mModemQoeMetrics.setDlBler(byteBuffer.getInt());
        this.mModemQoeMetrics.setUlBler(byteBuffer.getInt());
        this.mModemQoeMetrics.setDlMcs(byteBuffer.getInt());
        this.mModemQoeMetrics.setUlMcs(byteBuffer.getInt());
        return b;
    }

    private void parseModemAiTxAndDlMetrics(ByteBuffer byteBuffer, byte b) {
        short s = byteBuffer.getShort();
        ModemQoeMetrics modemQoeMetrics = this.mModemQoeMetrics;
        if (b != 14) {
            s = 0;
        }
        modemQoeMetrics.setLteTxPusch(s);
        int i = byteBuffer.getShort() & 65535;
        ModemQoeMetrics modemQoeMetrics2 = this.mModemQoeMetrics;
        if (b != 14) {
            i = 0;
        }
        modemQoeMetrics2.setLteMaxPower(i);
        this.mModemQoeMetrics.setNr5gTargetTxPower(b == 20 ? byteBuffer.getShort() : (short) 0);
        this.mModemQoeMetrics.setNr5gMtplTx(b == 20 ? ((long) byteBuffer.getInt()) & 4294967295L : 0L);
        this.mModemQoeMetrics.setNasRejectMsgType(byteBuffer.get());
        this.mModemQoeMetrics.setNasRejectCause(byteBuffer.get());
        this.mModemQoeMetrics.setPwrErrCode(byteBuffer.get());
        this.mModemQoeMetrics.setCqi(byteBuffer.get());
        this.mModemQoeMetrics.setRi(byteBuffer.get());
        this.mModemQoeMetrics.setNumLayers(byteBuffer.get());
        this.mModemQoeMetrics.setDlTbBytes(byteBuffer.getLong() & 4294967295L);
        this.mModemQoeMetrics.setDlGrantRatio(byteBuffer.getInt());
        this.mModemQoeMetrics.setUlGrantRatio(byteBuffer.getInt());
        this.mModemQoeMetrics.setPdcpDlNumMissPdu(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setPdcpDlNumDataPduRcv(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setPdcpDlNumOutOfWinPdu(((long) byteBuffer.getInt()) & 4294967295L);
        this.mModemQoeMetrics.setNumRbs(byteBuffer.get());
    }

    private void parseModemAiPdcpUlMetrics(ByteBuffer byteBuffer, byte b) {
        long j = byteBuffer.getLong() & 4294967295L;
        long j2 = byteBuffer.getLong() & 4294967295L;
        long j3 = byteBuffer.getLong() & 4294967295L;
        long j4 = byteBuffer.getLong() & 4294967295L;
        long j5 = ((long) byteBuffer.getInt()) & 4294967295L;
        long j6 = byteBuffer.getLong() & 4294967295L;
        long j7 = ((long) byteBuffer.getInt()) & 4294967295L;
        this.mModemQoeMetrics.setNr5gPdcpUlTotalNumDiscardBytes(b == 20 ? j : 0L);
        ModemQoeMetrics modemQoeMetrics = this.mModemQoeMetrics;
        if (b != 20) {
            j2 = 0;
        }
        modemQoeMetrics.setNr5gPdcpUlCurNumWmBytes(j2);
        ModemQoeMetrics modemQoeMetrics2 = this.mModemQoeMetrics;
        if (b != 20) {
            j3 = 0;
        }
        modemQoeMetrics2.setNr5gPdcpUlCurWmDropBytes(j3);
        ModemQoeMetrics modemQoeMetrics3 = this.mModemQoeMetrics;
        if (b != 20) {
            j4 = 0;
        }
        modemQoeMetrics3.setNr5gPdcpUlTotalNumRxBytes(j4);
        ModemQoeMetrics modemQoeMetrics4 = this.mModemQoeMetrics;
        if (b != 14) {
            j5 = 0;
        }
        modemQoeMetrics4.setLtePdcpUlNumPdcpUiBufBytes(j5);
        ModemQoeMetrics modemQoeMetrics5 = this.mModemQoeMetrics;
        if (b != 14) {
            j6 = 0;
        }
        modemQoeMetrics5.setLtePdcpUlDataBytesFromWm(j6);
        this.mModemQoeMetrics.setLtePdcpUlDiscardSduBytes(b == 14 ? j7 : 0L);
    }

    private void parseModemAiRlcMetrics(ByteBuffer byteBuffer) {
        this.mModemQoeMetrics.setRachNumAttempts(byteBuffer.getShort() & 65535);
        this.mModemQoeMetrics.setRachResult(byteBuffer.get());
        this.mModemQoeMetrics.setRlcUlTotalTxPdus(byteBuffer.getLong() & 4294967295L);
        this.mModemQoeMetrics.setRlcUlTotalRetxPdus(byteBuffer.getInt() & 65535);
        this.mModemQoeMetrics.setRlcDlNumDataPdu(byteBuffer.getInt() & 65535);
        this.mModemQoeMetrics.setRlcDlNumRetxPdu(byteBuffer.getInt() & 65535);
        this.mModemQoeMetrics.setRlcDlNumDroppedPdu(byteBuffer.getInt() & 65535);
        this.mModemQoeMetrics.setFullSchNum(byteBuffer.getInt() & 65535);
    }

    private void dumpModemAiMetrics() {
        if (mIsNeedDump) {
            ModemQoeMetrics.localLog("ModemDataCollector.Report", "," + this.mModemQoeMetrics.getCellId() + "," + this.mModemQoeMetrics.getDlEarfcn() + "," + this.mModemQoeMetrics.getUlEarfcn() + "," + this.mModemQoeMetrics.getBand() + "," + this.mModemQoeMetrics.getDlBw() + "," + this.mModemQoeMetrics.getUlBw() + "," + this.mModemQoeMetrics.getPci() + "," + this.mModemQoeMetrics.getRat() + "," + this.mModemQoeMetrics.getRsrp() + "," + this.mModemQoeMetrics.getRsrq() + "," + this.mModemQoeMetrics.getSnr() + "," + this.mModemQoeMetrics.getDlBler() + "," + this.mModemQoeMetrics.getUlBler() + "," + this.mModemQoeMetrics.getDlMcs() + "," + this.mModemQoeMetrics.getUlMcs() + "," + ((int) this.mModemQoeMetrics.getLteTxPusch()) + "," + this.mModemQoeMetrics.getLteMaxPower() + "," + ((int) this.mModemQoeMetrics.getNr5gTargetTxPower()) + "," + this.mModemQoeMetrics.getNr5gMtplTx() + "," + ((int) this.mModemQoeMetrics.getNasRejectMsgType()) + "," + ((int) this.mModemQoeMetrics.getNasRejectCause()) + "," + ((int) this.mModemQoeMetrics.getPwrErrCode()) + "," + ((int) this.mModemQoeMetrics.getCqi()) + "," + ((int) this.mModemQoeMetrics.getRi()) + "," + ((int) this.mModemQoeMetrics.getNumLayers()) + "," + this.mModemQoeMetrics.getDlTbBytes() + "," + this.mModemQoeMetrics.getDlGrantRatio() + "," + this.mModemQoeMetrics.getUlGrantRatio() + "," + this.mModemQoeMetrics.getPdcpDlNumMissPdu() + "," + this.mModemQoeMetrics.getPdcpDlNumDataPduRcv() + "," + this.mModemQoeMetrics.getPdcpDlNumOutOfWinPdu() + "," + ((int) this.mModemQoeMetrics.getNumRbs()) + "," + this.mModemQoeMetrics.getNr5gPdcpUlTotalNumDiscardBytes() + "," + this.mModemQoeMetrics.getNr5gPdcpUlCurNumWmBytes() + "," + this.mModemQoeMetrics.getNr5gPdcpUlCurWmDropBytes() + "," + this.mModemQoeMetrics.getNr5gPdcpUlTotalNumRxBytes() + "," + this.mModemQoeMetrics.getLtePdcpUlNumPdcpUiBufBytes() + "," + this.mModemQoeMetrics.getLtePdcpUlDataBytesFromWm() + "," + this.mModemQoeMetrics.getLtePdcpUlDiscardSduBytes() + "," + this.mModemQoeMetrics.getRachNumAttempts() + "," + ((int) this.mModemQoeMetrics.getRachResult()) + "," + this.mModemQoeMetrics.getRlcUlTotalTxPdus() + "," + this.mModemQoeMetrics.getRlcUlTotalRetxPdus() + "," + this.mModemQoeMetrics.getRlcDlNumDataPdu() + "," + this.mModemQoeMetrics.getRlcDlNumRetxPdu() + "," + this.mModemQoeMetrics.getRlcDlNumDroppedPdu() + "," + this.mModemQoeMetrics.getFullSchNum(), ResponseObject.RELAY_FAILED_CODE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void receiveModemData(AsyncResult asyncResult) {
        if (asyncResult.exception != null) {
            int i = mReportErrorCount + 1;
            mReportErrorCount = i;
            if (i == mMaxErrorCount) {
                log("modem data collector registrant fail too many times --> deregister hook, mReportErrorCount = " + mReportErrorCount);
                unRegHook();
                mReportErrorCount = 0;
                return;
            }
            log("modem data collector registrant fail: mReportErrorCount = " + mReportErrorCount + ", " + asyncResult.exception.getMessage());
            return;
        }
        try {
            ByteBuffer byteBufferOrder = ByteBuffer.wrap((byte[]) asyncResult.result).order(ByteOrder.LITTLE_ENDIAN);
            int i2 = byteBufferOrder.getInt();
            logD("receiveModemData: dataType = " + i2);
            if (i2 == 100) {
                handleModemAiData(byteBufferOrder);
            }
        } catch (Exception e) {
            log("Error casting result to byte[]: " + e.getMessage());
        }
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void startModemCallback() {
        notifyStartCollectData(0, 999, this.mDdsPhoneId);
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void stopModemCallback() {
        notifyStartCollectData(1, 999, this.mDdsPhoneId);
        resetErrorCount();
    }

    public void startQosMonitor() {
        notifyStartCollectData(6, 999, this.mDdsPhoneId);
    }

    public void stopQosMonitor() {
        notifyStartCollectData(7, 999, this.mDdsPhoneId);
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public boolean isInErrorState() {
        return this.mIsInErrorState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStartCollectData(int i, int i2, int i3) {
        ModemUtils modemUtils;
        boolean zBooleanValue;
        if (this.mMiRilHookReady && (modemUtils = this.mModemUtils) != null) {
            Object objCallHook = modemUtils.callHook("onHookStartCollectData", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            if (objCallHook instanceof Boolean) {
                zBooleanValue = ((Boolean) objCallHook).booleanValue();
            } else {
                log("notifyStartCollectData: return type error, actual: ".concat(objCallHook == null ? "null" : objCallHook.getClass().getName()));
                zBooleanValue = false;
            }
            if (zBooleanValue) {
                log("notifyStartCollectData success");
            } else {
                log("notifyStartCollectData fail");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startReportForPeriod() {
        try {
            if (mIsNeedDump && this.mMiRilHookReady && this.mIsScreenOn && !this.mIsReporting) {
                startModemCallback();
                this.mIsReporting = true;
                notifyStartCollectData(4, 0, this.mDdsPhoneId);
            } else {
                log("The build version is stable / mMiRilHookReady = " + this.mMiRilHookReady);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void stopReportForPeriod() {
        if (this.mIsReporting) {
            notifyStartCollectData(4, 1, this.mDdsPhoneId);
            this.mIsReporting = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDataSlotChanged() {
        this.mDdsPhoneId = getDdsPhoneId();
    }

    private int getDdsPhoneId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public boolean updateModemAiData() {
        if (this.mIsInErrorState) {
            return false;
        }
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("updateModemAiData failed, mModemUtils is null");
            return false;
        }
        Object objCallHook = modemUtils.callHook("onHookStartCollectDataSync", 5, 999, Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof ByteBuffer) {
            return updateModemAiDataInternal((ByteBuffer) objCallHook);
        }
        log("updateModemAiData: return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        return false;
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public boolean getCallEndInfo() {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("getCallEndInfo failed, mModemUtils is null");
            return false;
        }
        Object objCallHook = modemUtils.callHook("onHookStartCollectDataSync", 8, 999, 0);
        if (objCallHook instanceof ByteBuffer) {
            return getCallEndInfoInternal((ByteBuffer) objCallHook);
        }
        log("getCallEndInfo: return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        return false;
    }

    private void updateErrorState() {
        if (this.mErrorCount < 5 || this.mIsInErrorState) {
            return;
        }
        this.mIsInErrorState = true;
        logD("get in error time");
    }

    private void resetErrorCount() {
        this.mErrorCount = 0;
        this.mIsInErrorState = false;
        log("reset error count");
    }

    private boolean updateModemAiDataInternal(ByteBuffer byteBuffer) {
        boolean z;
        if (byteBuffer == null) {
            log("byteBuffer is null");
            this.mErrorCount++;
            updateErrorState();
            return false;
        }
        try {
            int i = byteBuffer.getInt();
            if (i != 0) {
                log("r != 0");
                this.mErrorCount++;
                updateErrorState();
                return false;
            }
            int i2 = byteBuffer.getInt();
            if (i2 != 186) {
                log("t != EVENT_OEMHOOK_START_COLLECT_DATA");
                this.mErrorCount++;
                updateErrorState();
                return false;
            }
            int i3 = byteBuffer.getInt();
            if (i3 != 191) {
                log("l != MODEM_AI_DATA_SIZE");
                this.mErrorCount++;
                updateErrorState();
                return false;
            }
            logD("r = " + i + " t = " + i2 + " l = " + i3);
            this.mModemQoeMetrics.setCellId(byteBuffer.getLong());
            this.mModemQoeMetrics.setDlEarfcn(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setUlEarfcn(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setBand(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setDlBw(byteBuffer.getShort() & 65535);
            this.mModemQoeMetrics.setUlBw(byteBuffer.getShort() & 65535);
            this.mModemQoeMetrics.setPci(byteBuffer.getShort() & 65535);
            byte b = byteBuffer.get();
            this.mModemQoeMetrics.setRat(b);
            this.mModemQoeMetrics.setRsrp(byteBuffer.getInt());
            this.mModemQoeMetrics.setRsrq(byteBuffer.getInt());
            int[] iArr = new int[4];
            int i4 = -40;
            for (int i5 = 0; i5 < 4; i5++) {
                int i6 = byteBuffer.getInt();
                iArr[i5] = i6;
                if (i6 != 65535 && i6 > i4) {
                    i4 = i6;
                }
            }
            this.mModemQoeMetrics.setSnr(i4);
            this.mModemQoeMetrics.setDlBler(byteBuffer.getInt());
            this.mModemQoeMetrics.setUlBler(byteBuffer.getInt());
            this.mModemQoeMetrics.setDlMcs(byteBuffer.getInt());
            this.mModemQoeMetrics.setUlMcs(byteBuffer.getInt());
            short s = byteBuffer.getShort();
            ModemQoeMetrics modemQoeMetrics = this.mModemQoeMetrics;
            if (b != 14) {
                s = 0;
            }
            modemQoeMetrics.setLteTxPusch(s);
            int i7 = byteBuffer.getShort() & 65535;
            ModemQoeMetrics modemQoeMetrics2 = this.mModemQoeMetrics;
            if (b != 14) {
                i7 = 0;
            }
            modemQoeMetrics2.setLteMaxPower(i7);
            short s2 = byteBuffer.getShort();
            ModemQoeMetrics modemQoeMetrics3 = this.mModemQoeMetrics;
            if (b != 20) {
                s2 = 0;
            }
            modemQoeMetrics3.setNr5gTargetTxPower(s2);
            this.mModemQoeMetrics.setNr5gMtplTx(b == 20 ? ((long) byteBuffer.getInt()) & 4294967295L : 0L);
            this.mModemQoeMetrics.setNasRejectMsgType(byteBuffer.get());
            this.mModemQoeMetrics.setNasRejectCause(byteBuffer.get());
            this.mModemQoeMetrics.setPwrErrCode(byteBuffer.get());
            this.mModemQoeMetrics.setCqi(byteBuffer.get());
            this.mModemQoeMetrics.setRi(byteBuffer.get());
            this.mModemQoeMetrics.setNumLayers(byteBuffer.get());
            this.mModemQoeMetrics.setDlTbBytes(byteBuffer.getLong() & 4294967295L);
            this.mModemQoeMetrics.setDlGrantRatio(byteBuffer.getInt());
            this.mModemQoeMetrics.setUlGrantRatio(byteBuffer.getInt());
            this.mModemQoeMetrics.setPdcpDlNumMissPdu(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setPdcpDlNumDataPduRcv(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setPdcpDlNumOutOfWinPdu(((long) byteBuffer.getInt()) & 4294967295L);
            this.mModemQoeMetrics.setNumRbs(byteBuffer.get());
            long j = byteBuffer.getLong() & 4294967295L;
            long j2 = byteBuffer.getLong() & 4294967295L;
            long j3 = byteBuffer.getLong() & 4294967295L;
            long j4 = byteBuffer.getLong() & 4294967295L;
            long j5 = ((long) byteBuffer.getInt()) & 4294967295L;
            try {
                long j6 = byteBuffer.getLong() & 4294967295L;
                z = false;
                long j7 = ((long) byteBuffer.getInt()) & 4294967295L;
                try {
                    ModemQoeMetrics modemQoeMetrics4 = this.mModemQoeMetrics;
                    if (b != 20) {
                        j = 0;
                    }
                    modemQoeMetrics4.setNr5gPdcpUlTotalNumDiscardBytes(j);
                    this.mModemQoeMetrics.setNr5gPdcpUlCurNumWmBytes(b == 20 ? j2 : 0L);
                    this.mModemQoeMetrics.setNr5gPdcpUlCurWmDropBytes(b == 20 ? j3 : 0L);
                    this.mModemQoeMetrics.setNr5gPdcpUlTotalNumRxBytes(b == 20 ? j4 : 0L);
                    ModemQoeMetrics modemQoeMetrics5 = this.mModemQoeMetrics;
                    if (b != 14) {
                        j5 = 0;
                    }
                    modemQoeMetrics5.setLtePdcpUlNumPdcpUiBufBytes(j5);
                    this.mModemQoeMetrics.setLtePdcpUlDataBytesFromWm(b == 14 ? j6 : 0L);
                    this.mModemQoeMetrics.setLtePdcpUlDiscardSduBytes(b == 14 ? j7 : 0L);
                    this.mModemQoeMetrics.setRachNumAttempts(byteBuffer.getShort() & 65535);
                    this.mModemQoeMetrics.setRachResult(byteBuffer.get());
                    this.mModemQoeMetrics.setRlcUlTotalTxPdus(byteBuffer.getLong() & 4294967295L);
                    this.mModemQoeMetrics.setRlcUlTotalRetxPdus(byteBuffer.getInt() & 65535);
                    this.mModemQoeMetrics.setRlcDlNumDataPdu(byteBuffer.getInt() & 65535);
                    this.mModemQoeMetrics.setRlcDlNumRetxPdu(byteBuffer.getInt() & 65535);
                    this.mModemQoeMetrics.setRlcDlNumDroppedPdu(byteBuffer.getInt() & 65535);
                    this.mModemQoeMetrics.setFullSchNum(byteBuffer.getInt() & 65535);
                    logD("byteBuffer remaining:" + byteBuffer.remaining());
                    logD("updateModemAiDataInternal modemQoeMetrics.toString():" + this.mModemQoeMetrics.toString());
                    return true;
                } catch (Exception e) {
                    e = e;
                    log("updateModemAiDataInternal Error casting: " + e.getMessage());
                    this.mErrorCount = this.mErrorCount + 1;
                    updateErrorState();
                    return z;
                }
            } catch (Exception e2) {
                e = e2;
                z = false;
            }
        } catch (Exception e3) {
            e = e3;
            z = false;
        }
    }

    private boolean getCallEndInfoInternal(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            log("byteBuffer is null");
            return false;
        }
        try {
            int i = byteBuffer.getInt();
            if (i != 0) {
                log("r != 0");
                return false;
            }
            int i2 = byteBuffer.getInt();
            if (i2 != 186) {
                log("t != EVENT_OEMHOOK_START_COLLECT_DATA");
                return false;
            }
            int i3 = byteBuffer.getInt();
            if (i3 != 12) {
                log("l != CALL_END_INFO_SIZE");
                return false;
            }
            logD("r = " + i + " t = " + i2 + " l = " + i3);
            long j = byteBuffer.getLong();
            int i4 = byteBuffer.getShort() & 65535;
            int i5 = byteBuffer.getShort() & 65535;
            if (j != 1) {
                log("Apn type is invalid");
            } else {
                this.mModemQoeMetrics.setDefaultCallEndReasonType(i4);
                this.mModemQoeMetrics.setDefaultCallEndReason(i5);
            }
            log("mdc -> apnType = " + j + ", callEndReasonType = " + i4 + ", callEndReason = " + i5);
            return true;
        } catch (Exception e) {
            log("updateCallEndInfo Error casting: " + e.getMessage());
            return false;
        }
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void startWechatUlEnhanceCallback() {
        notifyStartCollectData(13, 0, this.mDdsPhoneId);
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void stopWechatUlEnhanceCallback() {
        notifyStartCollectData(13, 1, this.mDdsPhoneId);
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public boolean isSupportWechatUlEnhance() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("mModemUtils is null");
            return false;
        }
        Object objCallHook = modemUtils.callHook("onHookStartCollectDataSync", 13, 2, Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof ByteBuffer) {
            byteBuffer = (ByteBuffer) objCallHook;
        } else {
            log("onHookStartCollectDataSync return is not ByteBuffer type, actual type: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
            byteBuffer = null;
        }
        if (byteBuffer == null) {
            log("byteBuffer is null");
            return false;
        }
        try {
            int i = byteBuffer.getInt();
            if (i != 0) {
                log("r != 0");
                return false;
            }
            int i2 = byteBuffer.getInt();
            if (i2 != 186) {
                log("t != EVENT_OEMHOOK_START_COLLECT_DATA");
                return false;
            }
            logD("r = " + i + " t = " + i2 + " l = " + byteBuffer.getInt());
            boolean z = byteBuffer.get() != 0;
            logD("isSupportWechatUlEnhance, isSupport = " + z);
            return z;
        } catch (Exception e) {
            log("isSupportWechatUlEnhance Error casting: " + e.getMessage());
            return false;
        }
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void startTest() {
        startReportForPeriod();
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public void stopTest() {
        stopReportForPeriod();
    }

    public int[][] get5GABandStats() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("mModemUtils is null");
            return null;
        }
        Object objCallHook = modemUtils.callHook("onHookStartCollectDataSync", 10, 999, Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof ByteBuffer) {
            byteBuffer = (ByteBuffer) objCallHook;
        } else {
            log("onHookStartCollectDataSync return is not ByteBuffer type, actual type: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
            byteBuffer = null;
        }
        if (byteBuffer == null) {
            log("byteBuffer is null");
            return null;
        }
        try {
            int i = byteBuffer.getInt();
            if (i != 0) {
                log("r != 0");
                return null;
            }
            int i2 = byteBuffer.getInt();
            if (i2 != 186) {
                log("t != EVENT_OEMHOOK_START_COLLECT_DATA");
                return null;
            }
            logD("r = " + i + " t = " + i2 + " l = " + byteBuffer.getInt());
            int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 20, 4);
            for (int i3 = 0; i3 < 20; i3++) {
                for (int i4 = 0; i4 < 3; i4++) {
                    iArr[i3][i4] = byteBuffer.get() & TransitionInfo.INIT;
                }
                iArr[i3][3] = byteBuffer.getInt();
                logD("bandStats[" + i3 + "]: " + Arrays.toString(iArr[i3]));
            }
            return iArr;
        } catch (Exception e) {
            log("get5GABandStats Error casting: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: renamed from: start5GAStatsCallback, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        notifyStartCollectData(11, 999, this.mDdsPhoneId);
    }

    public void stop5GAStatsCallback() {
        notifyStartCollectData(12, 999, this.mDdsPhoneId);
    }

    @Override // com.android.phone.ModemDataCollectorBase
    public ModemFiveGAScheduleInfo[] get5GAScheduleInfo() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("mModemUtils is null");
            return null;
        }
        Object objCallHook = modemUtils.callHook("onHookStartCollectDataSync", 9, 999, Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof ByteBuffer) {
            byteBuffer = (ByteBuffer) objCallHook;
        } else {
            log("onHookStartCollectDataSync return is not ByteBuffer type, actual type: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
            byteBuffer = null;
        }
        if (byteBuffer == null) {
            log("byteBuffer is null");
            return null;
        }
        try {
            int i = byteBuffer.getInt();
            if (i != 0) {
                log("r != 0");
                return null;
            }
            int i2 = byteBuffer.getInt();
            if (i2 != 186) {
                log("t != EVENT_OEMHOOK_START_COLLECT_DATA");
                return null;
            }
            logD("r = " + i + " t = " + i2 + " l = " + byteBuffer.getInt());
            ModemFiveGAScheduleInfo[] modemFiveGAScheduleInfoArr = new ModemFiveGAScheduleInfo[5];
            for (int i3 = 0; i3 < 5; i3++) {
                ModemFiveGAScheduleInfo modemFiveGAScheduleInfo = new ModemFiveGAScheduleInfo();
                modemFiveGAScheduleInfoArr[i3] = modemFiveGAScheduleInfo;
                modemFiveGAScheduleInfo.nrCaNum = byteBuffer.get();
                for (int i4 = 0; i4 < 3; i4++) {
                    modemFiveGAScheduleInfoArr[i3].arfcn[i4] = byteBuffer.getInt();
                    modemFiveGAScheduleInfoArr[i3].pci[i4] = byteBuffer.getShort() & 65535;
                    modemFiveGAScheduleInfoArr[i3].rsrp[i4] = byteBuffer.getInt();
                    modemFiveGAScheduleInfoArr[i3].snr[i4] = byteBuffer.getInt();
                    modemFiveGAScheduleInfoArr[i3].pdcpDecode[i4] = byteBuffer.getInt();
                    modemFiveGAScheduleInfoArr[i3].crcFail[i4] = byteBuffer.getInt();
                    modemFiveGAScheduleInfoArr[i3].tbBytes[i4] = byteBuffer.getLong();
                }
                logD("modem5GAScheduleInfos[" + i3 + "]: " + modemFiveGAScheduleInfoArr[i3].toString());
            }
            return modemFiveGAScheduleInfoArr;
        } catch (Exception e) {
            log("get5GAScheduleInfo Error casting: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("ModemDataCollector", str);
    }

    private static void logD(String str) {
        if (VDBG) {
            log(str);
        }
    }
}
