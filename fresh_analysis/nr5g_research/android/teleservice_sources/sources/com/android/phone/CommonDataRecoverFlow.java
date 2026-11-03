package com.android.phone;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.datasub.SmartDualSimSwitch;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyStatAdapter;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CommonDataRecoverFlow extends StateMachine implements TelephonyStatAdapter.ICallBack, PhoneDebug.Listener, MiuiAppStateMonitor.TopAppObserver {
    private static final String[] DATA_STALL_OPT_CMDS = {"MIPC_SET_DATA_STALL_OPT,1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,0,1000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,1,1000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final ThreadLocal TIME_FORMATTER = ThreadLocal.withInitial(new Supplier() { // from class: com.android.phone.CommonDataRecoverFlow$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return CommonDataRecoverFlow.$r8$lambda$uXTJN6FnMBptpxm6umdh5QcdyhM();
        }
    });
    private static CommonDataRecoverFlow sDataRecoverFlow;
    private boolean isSMStarted;
    private boolean isScoreGood;
    private final FixedSizeArray mBlacklistCellInfos;
    private final FixedSizeArray mCellChangeInterval;
    private long mCellChangeTime;
    private int mCellCntAfterOpt;
    private int mCommonDataExemptScene;
    private ContentResolver mContentResolver;
    private Context mContext;
    private volatile int mCurrentDataRat;
    private String mCurrentGci;
    private CloudCellDbHelper mDbHelper;
    private int mDebugCount;
    private DefaultState mDefaultState;
    private DisableSaEndcState mDisableSaEndcState;
    private TextView mFloatingView;
    private boolean mHasNr2Lte;
    private boolean mInCall;
    private boolean mIsViewFloating;
    private String mLastGci;
    private LteReselectState mLteReselectState;
    private Phone mPhone;
    private final BroadcastReceiver mReceiver;
    private SaReselectState mSaReselectState;
    private int mSlotId;
    private final List mSpecialScenePkgs;
    private final Map mState2ActionMap;
    private TeleCallback mTeleCallback;
    private WindowManager mWindowManager;

    private CommonDataRecoverFlow() {
        super("DataRecoverFlow");
        this.mSpecialScenePkgs = HyperOSCustFeatureResolve.getStringArray("config_special_scene_opt_packages");
        this.mBlacklistCellInfos = new FixedSizeArray(20);
        this.mCellChangeInterval = new FixedSizeArray(3);
        this.isScoreGood = true;
        this.mCellCntAfterOpt = -1;
        log("init");
        Context phoneGlobals = PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication();
        this.mContext = phoneGlobals;
        this.mContentResolver = phoneGlobals.getContentResolver();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mDbHelper = CloudCellDbHelper.getInstance(this.mContext);
        this.mDefaultState = new DefaultState();
        this.mSaReselectState = new SaReselectState();
        this.mLteReselectState = new LteReselectState();
        this.mDisableSaEndcState = new DisableSaEndcState();
        addState(this.mDefaultState);
        addState(this.mSaReselectState, this.mDefaultState);
        addState(this.mLteReselectState, this.mDefaultState);
        addState(this.mDisableSaEndcState, this.mDefaultState);
        setInitialState(this.mDefaultState);
        initBlacklistCells();
        this.mState2ActionMap = Map.of(this.mSaReselectState, 1, this.mLteReselectState, 5, this.mDisableSaEndcState, 2);
        PhoneDebug.addListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xiaomi.action.NETWORK_SCORE_BROADCAST");
        MiuiAppStateMonitor.init().registerAppObserver(this);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.CommonDataRecoverFlow.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Handler handler = CommonDataRecoverFlow.this.getHandler();
                if ("com.xiaomi.action.NETWORK_SCORE_BROADCAST".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("avgScore", 100);
                    int i = SystemProperties.getInt("persist.sys.network_score_threshold", 60);
                    boolean z = CommonDataRecoverFlow.this.isScoreGood;
                    CommonDataRecoverFlow.this.isScoreGood = intExtra >= i || intExtra == -1;
                    if (z == CommonDataRecoverFlow.this.isScoreGood || CommonDataRecoverFlow.this.isScoreGood) {
                        return;
                    }
                    CommonDataRecoverFlow.this.log("net score = " + intExtra);
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 13;
                    messageObtain.arg1 = intExtra;
                    messageObtain.obj = intent;
                    if (handler != null) {
                        handler.sendMessage(messageObtain);
                    }
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        this.mSlotId = activeDataPhoneId;
        boolean zIsMobileDataConnected = MiuiPhoneUtils.isMobileDataConnected(activeDataPhoneId);
        int i = this.mCommonDataExemptScene;
        this.mCommonDataExemptScene = !zIsMobileDataConnected ? i | 4 : i & (-5);
        start();
    }

    public static synchronized CommonDataRecoverFlow make() {
        if (!CloudHelper.initialize()) {
            Rlog.d("DataRecoverFlow", "feature not supported");
            return null;
        }
        if (sDataRecoverFlow == null && CloudHelper.mCloudEnable) {
            sDataRecoverFlow = new CommonDataRecoverFlow();
        } else {
            Rlog.d("DataRecoverFlow", "feature not enabled");
        }
        return sDataRecoverFlow;
    }

    class CloudHelper implements BaseEvent.IEventListener {
        private static boolean mCloudEnable;

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean initialize() {
            if (Build.IS_INTERNATIONAL_BUILD || !HyperOSCustFeatureResolve.getBoolean("config_common_data_recover_support", false)) {
                return false;
            }
            new CloudHelper();
            return true;
        }

        private static boolean isCloudEnabled() {
            try {
                List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "TelephonyCommonFeature");
                if (cloudDataList != null && cloudDataList.size() > 0) {
                    Iterator it = cloudDataList.iterator();
                    while (it.hasNext()) {
                        JSONObject jSONObjectJson = ((MiuiSettings.SettingsCloudData.CloudData) it.next()).json();
                        if (jSONObjectJson.has("com_data_enable")) {
                            return jSONObjectJson.getBoolean("com_data_enable");
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }

        private CloudHelper() {
            EventHubManager.getInstance().registerListener("telephony", "cloud_update", this);
            mCloudEnable = isCloudEnabled();
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            boolean zIsCloudEnabled;
            if (!(baseEvent instanceof TelephonyEvent.CloudEvent) || (zIsCloudEnabled = isCloudEnabled()) == mCloudEnable) {
                return;
            }
            mCloudEnable = zIsCloudEnabled;
            Rlog.d("DataRecoverFlow", "cloud enable updated:" + mCloudEnable);
            if (mCloudEnable) {
                if (CommonDataRecoverFlow.sDataRecoverFlow == null) {
                    CommonDataRecoverFlow.sDataRecoverFlow = new CommonDataRecoverFlow();
                }
            } else if (CommonDataRecoverFlow.sDataRecoverFlow != null) {
                CommonDataRecoverFlow.sDataRecoverFlow.sendMessage(16);
            }
        }
    }

    private void initBlacklistCells() {
        Cursor commonCellBlacklist = this.mDbHelper.getCommonCellBlacklist();
        if (commonCellBlacklist != null) {
            while (commonCellBlacklist.moveToNext()) {
                try {
                    CellData cellData = new CellData();
                    cellData.gci = commonCellBlacklist.getString(commonCellBlacklist.getColumnIndex("gci"));
                    cellData.action = commonCellBlacklist.getInt(commonCellBlacklist.getColumnIndex("act"));
                    cellData.launchTime = commonCellBlacklist.getLong(commonCellBlacklist.getColumnIndex("launch_time"));
                    cellData.doneTime = commonCellBlacklist.getLong(commonCellBlacklist.getColumnIndex("done_time"));
                    logV("init blacklist:" + cellData);
                    this.mBlacklistCellInfos.add(cellData);
                } catch (Throwable th) {
                    try {
                        commonCellBlacklist.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
        if (commonCellBlacklist != null) {
            commonCellBlacklist.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExpiredBlacklistCells() {
        log("updateExpiredBlacklistCells");
        long jCurrentTimeMillis = System.currentTimeMillis();
        Iterator it = this.mBlacklistCellInfos.iterator();
        boolean z = false;
        while (it.hasNext()) {
            CellData cellData = (CellData) it.next();
            long j = cellData.launchTime;
            if (j > 0 && jCurrentTimeMillis - j > 1209600000) {
                log("remove expired cell:" + cellData);
                it.remove();
                z = true;
            }
        }
        if (z) {
            updateCellsInDb();
        }
    }

    private void updateCellsInDb() {
        try {
            this.mDbHelper.clearCommonCellBlacklist();
            FixedSizeArray fixedSizeArray = this.mBlacklistCellInfos;
            int size = fixedSizeArray.size();
            int i = 0;
            while (i < size) {
                Object obj = fixedSizeArray.get(i);
                i++;
                CellData cellData = (CellData) obj;
                this.mDbHelper.updateCommonCellBlacklist(cellData.gci, cellData.action, cellData.launchTime, cellData.doneTime);
            }
        } catch (Exception e) {
            log("updateCellsInDb err:" + e);
        }
    }

    public void start() {
        super.start();
        this.isSMStarted = true;
        TelephonyManager telephonyManager = (TelephonyManager) PhoneGlobals.getInstance().getSystemService("phone");
        this.mTeleCallback = new TeleCallback();
        telephonyManager.registerTelephonyCallback(new HandlerExecutor(getHandler()), this.mTeleCallback);
        TelephonyStatAdapter.registerCallBack(this);
    }

    protected void onQuitting() {
        log("onQuitting");
        this.mDefaultState.enter();
        ((TelephonyManager) PhoneGlobals.getInstance().getSystemService("phone")).unregisterTelephonyCallback(this.mTeleCallback);
        TelephonyStatAdapter.unregisterCallBack(this);
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
        PhoneDebug.removeListener(this);
        MiuiAppStateMonitor.init().unregisterAppObserver(this);
        this.isSMStarted = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReturnToDefaultTimeout() {
        log("timeout, return to default");
        transitionTo(this.mDefaultState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCellDataAvailable(FlowState flowState, State state) {
        log("checkCellDataAvailable");
        removeMessages(6);
        if (isGoodNrCell() || isGoodLteCell()) {
            long pingDelaySync = getPingDelaySync("ping2");
            if (pingDelaySync > -1 && pingDelaySync < 2000) {
                onDataOK();
                flowState.mFixed = true;
                return;
            } else {
                if (limitNr2Lte(state)) {
                    return;
                }
                transitionTo(state);
                DefaultState defaultState = this.mDefaultState;
                if (state == defaultState) {
                    markCellOptFailed(defaultState.mLaunchGci);
                    return;
                }
                return;
            }
        }
        log("found no good cell");
        if (limitNr2Lte(state)) {
            return;
        }
        transitionTo(state);
        DefaultState defaultState2 = this.mDefaultState;
        if (state == defaultState2) {
            markCellOptFailed(defaultState2.mLaunchGci);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markCellOptFailed(String str) {
        log("markCellOptFailed:" + MiuiPhoneUtils.maskSensitive(str, 4));
        int iIndexOf = this.mBlacklistCellInfos.indexOf(fromGci(str));
        if (iIndexOf >= 0) {
            CellData cellData = (CellData) this.mBlacklistCellInfos.get(iIndexOf);
            cellData.doneTime = System.currentTimeMillis();
            cellData.action = -1;
            updateCellsInDb();
            return;
        }
        log(MiuiPhoneUtils.maskSensitive(str, 4) + " markCellOptFailed err!!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCellOptNotAllowed(String str) {
        boolean z;
        long jCurrentTimeMillis = System.currentTimeMillis();
        int iIndexOf = this.mBlacklistCellInfos.indexOf(fromGci(str));
        if (iIndexOf >= 0) {
            CellData cellData = (CellData) this.mBlacklistCellInfos.get(iIndexOf);
            z = cellData.action < 0 && jCurrentTimeMillis < cellData.doneTime + 172800000;
            if (z) {
                log(MiuiPhoneUtils.maskSensitive(str, 4) + " is bad cell");
            }
        } else {
            z = false;
        }
        return z || getCurrentState() != this.mDefaultState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPing1Callback(long j) {
        if (j < 0 || j >= 2000) {
            log("ping1 check failed");
            int iIndexOf = this.mBlacklistCellInfos.indexOf(fromGci(this.mCurrentGci));
            if (iIndexOf >= 0) {
                ((CellData) this.mBlacklistCellInfos.get(iIndexOf)).launchTime = System.currentTimeMillis();
            } else {
                CellData cellData = new CellData();
                cellData.gci = this.mCurrentGci;
                cellData.action = 0;
                cellData.launchTime = System.currentTimeMillis();
                log("blacklist add new:" + cellData);
                this.mBlacklistCellInfos.add(cellData);
            }
        } else {
            log(MiuiPhoneUtils.maskSensitive(this.mCurrentGci, 4) + " ping1 good");
            this.mBlacklistCellInfos.remove(fromGci(this.mCurrentGci));
        }
        updateCellsInDb();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionToState(int i) {
        if (i == 1) {
            transitionTo(this.mSaReselectState);
            return;
        }
        if (i == 2) {
            transitionTo(this.mDisableSaEndcState);
            return;
        }
        if (i != 3) {
            if (i == 5) {
                transitionTo(this.mLteReselectState);
                return;
            }
            log("invalid action:" + i);
        }
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        if (i == this.mSlotId) {
            sendMessage(3);
        }
    }

    public void onCellLocationChanged(int i, CellLocation cellLocation) {
        if (i == this.mSlotId) {
            removeMessages(5);
            sendMessage(5, cellLocation);
            removeMessages(1);
            sendMessage(1, cellLocation);
        }
    }

    public void onPreciseCallStateChanged(int i, int i2) {
        boolean z = this.mInCall;
        boolean zIsPhoneInCall = MiuiPhoneUtils.isPhoneInCall();
        this.mInCall = zIsPhoneInCall;
        if (z == zIsPhoneInCall || zIsPhoneInCall) {
            return;
        }
        getHandler().post(new Runnable() { // from class: com.android.phone.CommonDataRecoverFlow$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onPreciseCallStateChanged$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreciseCallStateChanged$1() {
        if (getCurrentState() != this.mDefaultState) {
            log("once call ended, return to default");
            transitionTo(this.mDefaultState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getPingDelaySync(String str) {
        final long[] jArr = {-1};
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        sendReachabilityIntent(new Messenger(new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.CommonDataRecoverFlow.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                jArr[0] = message.arg1;
                countDownLatch.countDown();
            }
        }), SystemClock.elapsedRealtime(), 7);
        try {
            try {
                try {
                    log("ping delay=" + jArr[0] + " ret:" + countDownLatch.await(MirrorDesktopHelper.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                e2.printStackTrace();
            }
            return jArr[0];
        } finally {
            handlerThread.quit();
        }
    }

    private void sendReachabilityIntent(Messenger messenger, long j, int i) {
        Intent intent = new Intent("com.xiaomi.hypercomm.intent.action.NETWORK_REACHABILITY");
        intent.setPackage("com.xiaomi.hypercomm");
        Bundle bundle = new Bundle();
        bundle.putParcelable("messenger", messenger);
        bundle.putLong("cookie", j);
        bundle.putInt("what", i);
        bundle.putString("cid", this.mCurrentGci);
        intent.putExtra("bundle", bundle);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    private boolean isGoodNrCell() {
        SignalStrength signalStrength = this.mPhone.getSignalStrength();
        if (signalStrength == null) {
            return false;
        }
        CellSignalStrengthNr nrSignalStrength = MiuiPhoneUtils.getNrSignalStrength(signalStrength);
        logV("isGoodNrCell:" + nrSignalStrength);
        return nrSignalStrength != null && nrSignalStrength.getSsRsrp() > -105 && nrSignalStrength.getSsRsrp() < Integer.MAX_VALUE && nrSignalStrength.getSsSinr() > -3 && nrSignalStrength.getSsSinr() < Integer.MAX_VALUE;
    }

    private boolean isGoodLteCell() {
        SignalStrength signalStrength = this.mPhone.getSignalStrength();
        if (signalStrength == null) {
            return false;
        }
        logV("isGoodLteCell:" + signalStrength);
        return signalStrength.getLteRsrp() > -110 && signalStrength.getLteRsrp() < Integer.MAX_VALUE && signalStrength.getLteRssnr() > -3 && signalStrength.getLteRssnr() < Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean skipDataCheck(FlowState flowState) {
        CellIdentity currentCellIdentity;
        if (!hasMessages(6) || (currentCellIdentity = this.mPhone.getCurrentCellIdentity()) == null) {
            return true;
        }
        String globalCellId = currentCellIdentity.getGlobalCellId();
        boolean zEquals = TextUtils.equals(flowState.mGciWhenOpt, globalCellId);
        logV("optEnter:" + MiuiPhoneUtils.maskSensitive(flowState.mGciWhenOpt, 4) + " now:" + MiuiPhoneUtils.maskSensitive(globalCellId, 4));
        return zEquals;
    }

    private void onDataOK() {
        this.mCellCntAfterOpt = 0;
        log("recover DONE for " + MiuiPhoneUtils.maskSensitive(this.mDefaultState.mLaunchGci, 4));
        int iIndexOf = this.mBlacklistCellInfos.indexOf(fromGci(this.mDefaultState.mLaunchGci));
        if (iIndexOf >= 0) {
            CellData cellData = (CellData) this.mBlacklistCellInfos.get(iIndexOf);
            DefaultState currentState = getCurrentState();
            if (currentState == this.mDefaultState) {
                log("err default state");
                return;
            }
            Integer num = (Integer) this.mState2ActionMap.get(currentState);
            cellData.action = num != null ? num.intValue() : 0;
            cellData.optCount++;
            cellData.doneTime = System.currentTimeMillis();
            log("update history action records");
            updateCellsInDb();
        } else {
            log("no history in list, err!!");
        }
        removeMessages(11);
        sendMessageDelayed(11, 360000L);
    }

    private boolean shouldIgnoreStallEvent(Message message) {
        Object obj = message.obj;
        if (obj == null) {
            return true;
        }
        Intent intent = (Intent) obj;
        if (PhoneDebug.VDBG) {
            dumpIntentExtras(intent);
        }
        if (cellChangeTooFrequently()) {
            log("ignore as cells change too frequently");
            return true;
        }
        if (intent.getIntExtra("dlCurBandwidth", -1) > 1000) {
            log("ignore on high speed network");
            return true;
        }
        if (this.mCommonDataExemptScene > 0) {
            log("ignore in exempt scene");
            return true;
        }
        if (this.mDefaultState.mLastStallTime > 0 && SystemClock.elapsedRealtime() - this.mDefaultState.mLastStallTime < 360000) {
            log("ignore in opt cooldown time");
            return true;
        }
        if (this.mBlacklistCellInfos.contains(fromGci(this.mCurrentGci))) {
            log("ignore since already in blacklist");
            return true;
        }
        if (!hasMessages(15)) {
            return false;
        }
        log("ignore in DDS change grace time");
        return true;
    }

    public void onCommonDataStall(Message message) {
        log("onCommonDataStall");
        if (shouldIgnoreStallEvent(message)) {
            return;
        }
        if (getCurrentState() != this.mDefaultState) {
            log("stall on err state:" + getCurrentState());
        } else {
            onPing1Callback(getPingDelaySync("ping1"));
            if (this.mBlacklistCellInfos.contains(fromGci(this.mCurrentGci))) {
                sendMessage(10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateViewFromBundle(Bundle bundle, final TextView textView) {
        String str;
        if (!this.mIsViewFloating || textView == null) {
            return;
        }
        int[] intArray = bundle.getIntArray("caState");
        String str2 = "";
        if (intArray == null || intArray.length <= 4) {
            str = "";
        } else {
            str = String.format("Disorder:%d, CWR:%d\nRecovery:%d, Loss:%d", Integer.valueOf(intArray[1]), Integer.valueOf(intArray[2]), Integer.valueOf(intArray[3]), Integer.valueOf(intArray[4]));
        }
        long[] longArray = bundle.getLongArray(a.P);
        if (longArray != null && longArray.length > 2) {
            str2 = String.format("DNS:%d OK:%d%% latency:%dms", Long.valueOf(longArray[0]), Long.valueOf(longArray[1]), Long.valueOf(longArray[2]));
        }
        final String str3 = String.format("Tx: %d Rx: %d\nTx: %dKB Rx: %dKB\nLost: %d Retrans: %d\nRtx: %d Total: %d\nDisorder: %d Dup: %d Bkf: %d\nRtt: %dms\nMinRtt: %dms RttVar: %d\nSsthre: %d Cwnd: %d\n%s\n%s", Integer.valueOf(bundle.getInt("tx", -1)), Integer.valueOf(bundle.getInt("rx", -1)), Long.valueOf(bundle.getLong("txBytes", -1L) / 1024), Long.valueOf(bundle.getLong("rxBytes", -1L) / 1024), Integer.valueOf(bundle.getInt("lost", -1)), Integer.valueOf(bundle.getInt("retrans", -1)), Integer.valueOf(bundle.getInt("retransmit", -1)), Integer.valueOf(bundle.getInt("totalTrans", -1)), Integer.valueOf(bundle.getInt("reorder", -1)), Integer.valueOf(bundle.getInt("dsackDup", -1)), Integer.valueOf(bundle.getInt("backoff", -1)), Integer.valueOf(bundle.getInt("rtt", -1) / 1000), Integer.valueOf(bundle.getInt("minRtt", -1) / 1000), Integer.valueOf(bundle.getInt("rttVar", -1) / 1000), Integer.valueOf(bundle.getInt("ssthresh", -1)), Integer.valueOf(bundle.getInt("cwnd", -1)), str, str2);
        textView.post(new Runnable() { // from class: com.android.phone.CommonDataRecoverFlow$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                textView.setText(str3);
            }
        });
    }

    public void onDebugChanged() {
        this.mDebugCount++;
        log("onDebugChanged:" + PhoneDebug.VDBG + this.mDebugCount);
        if (this.mDebugCount > 5 && PhoneDebug.VDBG) {
            setDbg(true);
        } else {
            setDbg(false);
        }
    }

    @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
    public void onTopAppChanged(List list, int i) {
        if (list == null || list.size() <= 0) {
            return;
        }
        removeMessages(14);
        sendMessageDelayed(14, i, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTopAppChangeConfirmed(Message message) {
        int i;
        int i2 = message.arg1;
        String topAppName = MiuiPhoneUtils.getTopAppName();
        boolean z = i2 == 0 && ArrayUtils.contains(this.mSpecialScenePkgs, topAppName);
        boolean z2 = Settings.Secure.getInt(this.mContentResolver, "gb_boosting", 0) == 1;
        logV("top app changed:" + topAppName + " mode:" + i2 + " isGame:" + z2);
        if (z || z2) {
            i = this.mCommonDataExemptScene | 1;
        } else {
            i = this.mCommonDataExemptScene & (-2);
        }
        this.mCommonDataExemptScene = i;
        logV("TOP_APP ExemptScene changed:" + this.mCommonDataExemptScene);
        if (this.mCommonDataExemptScene > 0) {
            DefaultState currentState = getCurrentState();
            DefaultState defaultState = this.mDefaultState;
            if (currentState != defaultState) {
                transitionTo(defaultState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logV(String str) {
        if (PhoneDebug.VDBG) {
            log(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CellData fromGci(String str) {
        CellData cellData = new CellData();
        cellData.gci = str;
        return cellData;
    }

    class FixedSizeArray extends ArrayList {
        private final int mMaxSize;

        FixedSizeArray(int i) {
            super(i);
            if (i < 1) {
                Folme$$ExternalSyntheticBUOutline0.m("size must be greater than 0");
                throw null;
            }
            this.mMaxSize = i;
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(Object obj) {
            if (obj == null) {
                Folme$$ExternalSyntheticBUOutline0.m("null element is not allowed");
                return false;
            }
            if (size() >= this.mMaxSize) {
                remove(0);
            }
            return super.add(obj);
        }
    }

    public static /* synthetic */ SimpleDateFormat $r8$lambda$uXTJN6FnMBptpxm6umdh5QcdyhM() {
        return new SimpleDateFormat("(MM-dd HH:mm)");
    }

    class CellData {
        int action;
        long doneTime;
        String gci;
        long launchTime;
        int optCount;

        CellData() {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof CellData) {
                return TextUtils.equals(this.gci, ((CellData) obj).gci);
            }
            return false;
        }

        public String toString() {
            return "CellData{gci=" + MiuiPhoneUtils.maskSensitive(this.gci, 4) + ", action=" + this.action + ", time=" + ((SimpleDateFormat) CommonDataRecoverFlow.TIME_FORMATTER.get()).format(new Date(this.launchTime)) + '}';
        }
    }

    abstract class FlowState extends State {
        protected long mEnterTime;
        protected boolean mFixed;
        protected String mGciWhenOpt;
        protected long mReachabilityCookie;

        FlowState() {
        }
    }

    class TeleCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.ActiveDataSubscriptionIdListener {
        TeleCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
            CommonDataRecoverFlow.this.log("onActiveDataSubscriptionIdChanged dds=" + activeDataPhoneId);
            if (activeDataPhoneId != CommonDataRecoverFlow.this.mSlotId) {
                if (CommonDataRecoverFlow.this.isSMStarted) {
                    CommonDataRecoverFlow commonDataRecoverFlow = CommonDataRecoverFlow.this;
                    commonDataRecoverFlow.transitionTo(commonDataRecoverFlow.mDefaultState);
                }
                CommonDataRecoverFlow.this.removeMessages(15);
                CommonDataRecoverFlow.this.sendMessageDelayed(15, MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            CommonDataRecoverFlow.this.log("onDataConnectionStateChanged state=" + i + " networkType=" + i2);
            CommonDataRecoverFlow.this.removeMessages(4);
            CommonDataRecoverFlow.this.sendMessageDelayed(4, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    class DefaultState extends FlowState {
        private long mLastStallTime;
        private String mLaunchGci;

        DefaultState() {
        }

        public void enter() {
            CommonDataRecoverFlow.this.log("enter default state");
            CommonDataRecoverFlow.this.mSlotId = MiuiPhoneUtils.getActiveDataPhoneId();
            CommonDataRecoverFlow commonDataRecoverFlow = CommonDataRecoverFlow.this;
            commonDataRecoverFlow.mPhone = PhoneFactory.getPhone(commonDataRecoverFlow.mSlotId);
            CellIdentity currentCellIdentity = CommonDataRecoverFlow.this.mPhone.getCurrentCellIdentity();
            if (currentCellIdentity != null) {
                CommonDataRecoverFlow.this.mCurrentGci = currentCellIdentity.getGlobalCellId();
            }
            CommonDataRecoverFlow commonDataRecoverFlow2 = CommonDataRecoverFlow.this;
            commonDataRecoverFlow2.mCurrentDataRat = commonDataRecoverFlow2.getCurrentRat(commonDataRecoverFlow2.mSlotId);
            CommonDataRecoverFlow.this.mCellCntAfterOpt = -1;
            this.mLaunchGci = null;
            if (CommonDataRecoverFlow.this.mHasNr2Lte) {
                if (NetworkRecoveryStrategyBase.getInstance() != null) {
                    NetworkRecoveryStrategyBase.getInstance().enableNsa(true, CommonDataRecoverFlow.this.mSlotId);
                    NetworkRecoveryStrategyBase.getInstance().enableSa(true, CommonDataRecoverFlow.this.mSlotId);
                }
                CommonDataRecoverFlow.this.mHasNr2Lte = false;
            }
            SmartDualSimSwitch smartDualSimSwitch = SmartDualSimSwitch.getInstance();
            if (smartDualSimSwitch != null) {
                smartDualSimSwitch.setDataRecoverStatus(false);
            }
        }

        public boolean processMessage(Message message) {
            int i;
            CommonDataRecoverFlow.this.logV("processMessage:" + message.what);
            int i2 = message.what;
            if (i2 == 1) {
                CellIdentity currentCellIdentity = CommonDataRecoverFlow.this.mPhone.getCurrentCellIdentity();
                if (currentCellIdentity != null && !TextUtils.isEmpty(currentCellIdentity.getGlobalCellId())) {
                    CommonDataRecoverFlow.this.mCurrentGci = currentCellIdentity.getGlobalCellId();
                    if (!TextUtils.equals(CommonDataRecoverFlow.this.mCurrentGci, CommonDataRecoverFlow.this.mLastGci)) {
                        long j = CommonDataRecoverFlow.this.mCellChangeTime;
                        CommonDataRecoverFlow.this.mCellChangeTime = SystemClock.elapsedRealtime();
                        if (j > 0) {
                            CommonDataRecoverFlow.this.mCellChangeInterval.add(Long.valueOf(CommonDataRecoverFlow.this.mCellChangeTime - j));
                        }
                        CommonDataRecoverFlow commonDataRecoverFlow = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow.mLastGci = commonDataRecoverFlow.mCurrentGci;
                        if (CommonDataRecoverFlow.this.mCellCntAfterOpt >= 0) {
                            CommonDataRecoverFlow.this.mCellCntAfterOpt++;
                        }
                        if (CommonDataRecoverFlow.this.mCellCntAfterOpt > 6 && CommonDataRecoverFlow.this.getCurrentState() != CommonDataRecoverFlow.this.mDefaultState) {
                            CommonDataRecoverFlow.this.log("has passed 6 cells");
                            CommonDataRecoverFlow.this.removeMessages(11);
                            CommonDataRecoverFlow commonDataRecoverFlow2 = CommonDataRecoverFlow.this;
                            commonDataRecoverFlow2.transitionTo(commonDataRecoverFlow2.mDefaultState);
                            return true;
                        }
                    }
                    CommonDataRecoverFlow.this.logV("cell changed:" + MiuiPhoneUtils.maskSensitive(CommonDataRecoverFlow.this.mCurrentGci, 4));
                    FixedSizeArray fixedSizeArray = CommonDataRecoverFlow.this.mBlacklistCellInfos;
                    CommonDataRecoverFlow commonDataRecoverFlow3 = CommonDataRecoverFlow.this;
                    int iIndexOf = fixedSizeArray.indexOf(commonDataRecoverFlow3.fromGci(commonDataRecoverFlow3.mCurrentGci));
                    if (iIndexOf >= 0) {
                        CommonDataRecoverFlow.this.log("enter suspect cell coverage");
                        CommonDataRecoverFlow commonDataRecoverFlow4 = CommonDataRecoverFlow.this;
                        if (commonDataRecoverFlow4.isCellOptNotAllowed(commonDataRecoverFlow4.mCurrentGci) || CommonDataRecoverFlow.this.mInCall || CommonDataRecoverFlow.this.mCommonDataExemptScene > 0) {
                            CommonDataRecoverFlow.this.log("return due to call or cell opt not allowed");
                            return true;
                        }
                        CommonDataRecoverFlow.this.onPing1Callback(CommonDataRecoverFlow.this.getPingDelaySync("ping1"));
                        FixedSizeArray fixedSizeArray2 = CommonDataRecoverFlow.this.mBlacklistCellInfos;
                        CommonDataRecoverFlow commonDataRecoverFlow5 = CommonDataRecoverFlow.this;
                        if (fixedSizeArray2.contains(commonDataRecoverFlow5.fromGci(commonDataRecoverFlow5.mCurrentGci))) {
                            int i3 = ((CellData) CommonDataRecoverFlow.this.mBlacklistCellInfos.get(iIndexOf)).action;
                            CommonDataRecoverFlow commonDataRecoverFlow6 = CommonDataRecoverFlow.this;
                            if (i3 > 0) {
                                commonDataRecoverFlow6.transitionToState(i3);
                            } else {
                                commonDataRecoverFlow6.sendMessage(10);
                            }
                        }
                    }
                    CommonDataRecoverFlow.this.updateExpiredBlacklistCells();
                }
            } else if (i2 == 7) {
                CommonDataRecoverFlow.this.log("EVENT_PING1_CALLBACK");
            } else if (i2 != 10) {
                if (i2 == 3) {
                    CommonDataRecoverFlow commonDataRecoverFlow7 = CommonDataRecoverFlow.this;
                    commonDataRecoverFlow7.mCurrentDataRat = commonDataRecoverFlow7.getCurrentRat(commonDataRecoverFlow7.mSlotId);
                    CommonDataRecoverFlow.this.log("EVENT_SERVICE_STATE_CHANGED rat=" + CommonDataRecoverFlow.this.mCurrentDataRat);
                } else if (i2 != 4) {
                    switch (i2) {
                        case 12:
                            Bundle bundle = (Bundle) message.obj;
                            CommonDataRecoverFlow commonDataRecoverFlow8 = CommonDataRecoverFlow.this;
                            commonDataRecoverFlow8.updateViewFromBundle(bundle, commonDataRecoverFlow8.mFloatingView);
                            break;
                        case 13:
                            CommonDataRecoverFlow.this.onCommonDataStall(message);
                            break;
                        case 14:
                            CommonDataRecoverFlow.this.onTopAppChangeConfirmed(message);
                            break;
                        case 15:
                            CommonDataRecoverFlow.this.mSlotId = MiuiPhoneUtils.getActiveDataPhoneId();
                            CommonDataRecoverFlow commonDataRecoverFlow9 = CommonDataRecoverFlow.this;
                            commonDataRecoverFlow9.mPhone = PhoneFactory.getPhone(commonDataRecoverFlow9.mSlotId);
                            break;
                        case 16:
                            CommonDataRecoverFlow.this.quit();
                            CommonDataRecoverFlow.sDataRecoverFlow = null;
                            break;
                        default:
                            CommonDataRecoverFlow.this.log("unhandled msg:" + message.what + " in default state");
                            break;
                    }
                } else {
                    CommonDataRecoverFlow commonDataRecoverFlow10 = CommonDataRecoverFlow.this;
                    boolean zIsMobileDataConnected = MiuiPhoneUtils.isMobileDataConnected(commonDataRecoverFlow10.mSlotId);
                    CommonDataRecoverFlow commonDataRecoverFlow11 = CommonDataRecoverFlow.this;
                    if (!zIsMobileDataConnected) {
                        i = commonDataRecoverFlow11.mCommonDataExemptScene | 4;
                    } else {
                        i = commonDataRecoverFlow11.mCommonDataExemptScene & (-5);
                    }
                    commonDataRecoverFlow10.mCommonDataExemptScene = i;
                    CommonDataRecoverFlow.this.logV("Data ExemptScene changed:" + CommonDataRecoverFlow.this.mCommonDataExemptScene);
                    if (CommonDataRecoverFlow.this.mCommonDataExemptScene > 0 && CommonDataRecoverFlow.this.getCurrentState() != CommonDataRecoverFlow.this.mDefaultState) {
                        CommonDataRecoverFlow commonDataRecoverFlow12 = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow12.transitionTo(commonDataRecoverFlow12.mDefaultState);
                    }
                }
            } else {
                if (CommonDataRecoverFlow.this.mInCall || TextUtils.isEmpty(CommonDataRecoverFlow.this.mCurrentGci)) {
                    CommonDataRecoverFlow.this.log("ignore bad data call:" + CommonDataRecoverFlow.this.mInCall);
                    return true;
                }
                this.mLastStallTime = SystemClock.elapsedRealtime();
                this.mLaunchGci = CommonDataRecoverFlow.this.mCurrentGci;
                CommonDataRecoverFlow.this.log("launch recover for " + MiuiPhoneUtils.maskSensitive(this.mLaunchGci, 4));
                CommonDataRecoverFlow commonDataRecoverFlow13 = CommonDataRecoverFlow.this;
                commonDataRecoverFlow13.mCurrentDataRat = commonDataRecoverFlow13.getCurrentRat(commonDataRecoverFlow13.mSlotId);
                int i4 = CommonDataRecoverFlow.this.mCurrentDataRat;
                CommonDataRecoverFlow commonDataRecoverFlow14 = CommonDataRecoverFlow.this;
                if (i4 == 1) {
                    boolean zIsNsaConnected = MiuiPhoneUtils.isNsaConnected(commonDataRecoverFlow14.mSlotId);
                    CommonDataRecoverFlow commonDataRecoverFlow15 = CommonDataRecoverFlow.this;
                    if (zIsNsaConnected) {
                        commonDataRecoverFlow15.log("NSA opt not supported");
                    } else {
                        commonDataRecoverFlow15.transitionTo(commonDataRecoverFlow15.mLteReselectState);
                    }
                } else {
                    int i5 = commonDataRecoverFlow14.mCurrentDataRat;
                    CommonDataRecoverFlow commonDataRecoverFlow16 = CommonDataRecoverFlow.this;
                    if (i5 == 2) {
                        commonDataRecoverFlow16.transitionTo(commonDataRecoverFlow16.mSaReselectState);
                    } else {
                        commonDataRecoverFlow16.log("current rat is not 4/5G:" + CommonDataRecoverFlow.this.mCurrentDataRat);
                    }
                }
            }
            return true;
        }

        public void exit() {
            SmartDualSimSwitch smartDualSimSwitch = SmartDualSimSwitch.getInstance();
            if (smartDualSimSwitch != null) {
                smartDualSimSwitch.setDataRecoverStatus(true);
            }
        }
    }

    class SaReselectState extends FlowState {
        SaReselectState() {
        }

        public void enter() {
            CommonDataRecoverFlow.this.log("enter sa reselect state");
            if (CommonDataRecoverFlow.this.mInCall) {
                return;
            }
            FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[5].split(","), null);
            this.mGciWhenOpt = CommonDataRecoverFlow.this.mCurrentGci;
            this.mEnterTime = SystemClock.elapsedRealtime();
            CommonDataRecoverFlow.this.sendMessageDelayed(6, 8000L);
        }

        /* JADX WARN: Code duplicated, block: B:16:0x0038  */
        /* JADX WARN: Code duplicated, block: B:18:0x0043  */
        /* JADX WARN: Code duplicated, block: B:20:0x0067  */
        /* JADX WARN: Instruction removed from duplicated block: B:18:0x0043, please report this as an issue */
        public boolean processMessage(Message message) {
            int i;
            CommonDataRecoverFlow commonDataRecoverFlow;
            CommonDataRecoverFlow.this.logV("processMessage:" + message.what);
            int i2 = message.what;
            if (i2 != 5) {
                if (i2 == 6) {
                    i = CommonDataRecoverFlow.this.mCurrentDataRat;
                    commonDataRecoverFlow = CommonDataRecoverFlow.this;
                    if (i != 2) {
                        commonDataRecoverFlow.log(CommonDataRecoverFlow.this.mCurrentDataRat + " not NR return");
                        CommonDataRecoverFlow commonDataRecoverFlow2 = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow2.transitionTo(commonDataRecoverFlow2.mDefaultState);
                        return true;
                    }
                    commonDataRecoverFlow.checkCellDataAvailable(this, commonDataRecoverFlow.mDisableSaEndcState);
                    FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[1].split(","), null);
                } else {
                    if (i2 != 11) {
                        return false;
                    }
                    CommonDataRecoverFlow.this.onReturnToDefaultTimeout();
                }
            } else if (!this.mFixed && !CommonDataRecoverFlow.this.skipDataCheck(this)) {
                i = CommonDataRecoverFlow.this.mCurrentDataRat;
                commonDataRecoverFlow = CommonDataRecoverFlow.this;
                if (i != 2) {
                    commonDataRecoverFlow.log(CommonDataRecoverFlow.this.mCurrentDataRat + " not NR return");
                    CommonDataRecoverFlow commonDataRecoverFlow3 = CommonDataRecoverFlow.this;
                    commonDataRecoverFlow3.transitionTo(commonDataRecoverFlow3.mDefaultState);
                    return true;
                }
                commonDataRecoverFlow.checkCellDataAvailable(this, commonDataRecoverFlow.mDisableSaEndcState);
                FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[1].split(","), null);
            }
            return true;
        }

        public void exit() {
            this.mEnterTime = 0L;
            this.mFixed = false;
            this.mReachabilityCookie = 0L;
            FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[1].split(","), null);
        }
    }

    class LteReselectState extends FlowState {
        LteReselectState() {
        }

        public void enter() {
            CommonDataRecoverFlow.this.log("enter lte reselect state");
            if (CommonDataRecoverFlow.this.mInCall) {
                return;
            }
            FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[4].split(","), null);
            this.mGciWhenOpt = CommonDataRecoverFlow.this.mCurrentGci;
            this.mEnterTime = SystemClock.elapsedRealtime();
            CommonDataRecoverFlow.this.sendMessageDelayed(6, 8000L);
        }

        /* JADX WARN: Code duplicated, block: B:15:0x0038  */
        /* JADX WARN: Code duplicated, block: B:17:0x0042  */
        /* JADX WARN: Code duplicated, block: B:19:0x0066  */
        /* JADX WARN: Instruction removed from duplicated block: B:17:0x0042, please report this as an issue */
        public boolean processMessage(Message message) {
            int i;
            CommonDataRecoverFlow commonDataRecoverFlow;
            CommonDataRecoverFlow.this.logV("processMessage:" + message.what);
            int i2 = message.what;
            if (i2 != 5) {
                if (i2 == 6) {
                    i = CommonDataRecoverFlow.this.mCurrentDataRat;
                    commonDataRecoverFlow = CommonDataRecoverFlow.this;
                    if (i != 1) {
                        commonDataRecoverFlow.log(CommonDataRecoverFlow.this.mCurrentDataRat + " not LTE return");
                        CommonDataRecoverFlow commonDataRecoverFlow2 = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow2.transitionTo(commonDataRecoverFlow2.mDefaultState);
                        return true;
                    }
                    commonDataRecoverFlow.checkCellDataAvailable(this, commonDataRecoverFlow.mDefaultState);
                    FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[0].split(","), null);
                } else {
                    if (i2 != 11) {
                        return false;
                    }
                    CommonDataRecoverFlow.this.onReturnToDefaultTimeout();
                }
            } else if (!this.mFixed && !CommonDataRecoverFlow.this.skipDataCheck(this)) {
                i = CommonDataRecoverFlow.this.mCurrentDataRat;
                commonDataRecoverFlow = CommonDataRecoverFlow.this;
                if (i != 1) {
                    commonDataRecoverFlow.log(CommonDataRecoverFlow.this.mCurrentDataRat + " not LTE return");
                    CommonDataRecoverFlow commonDataRecoverFlow3 = CommonDataRecoverFlow.this;
                    commonDataRecoverFlow3.transitionTo(commonDataRecoverFlow3.mDefaultState);
                    return true;
                }
                commonDataRecoverFlow.checkCellDataAvailable(this, commonDataRecoverFlow.mDefaultState);
                FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[0].split(","), null);
            }
            return true;
        }

        public void exit() {
            this.mEnterTime = 0L;
            this.mFixed = false;
            this.mReachabilityCookie = 0L;
            FiveGManagerBase.getInstance().sendAtCmdSync(CommonDataRecoverFlow.this.mSlotId, CommonDataRecoverFlow.DATA_STALL_OPT_CMDS[0].split(","), null);
        }
    }

    class DisableSaEndcState extends FlowState {
        DisableSaEndcState() {
        }

        public void enter() {
            CommonDataRecoverFlow.this.log("enter disable sa endc state");
            if (CommonDataRecoverFlow.this.mInCall) {
                return;
            }
            this.mGciWhenOpt = CommonDataRecoverFlow.this.mCurrentGci;
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().enableSa(false, CommonDataRecoverFlow.this.mSlotId);
                NetworkRecoveryStrategyBase.getInstance().enableNsa(false, CommonDataRecoverFlow.this.mSlotId);
            }
            CommonDataRecoverFlow.this.mHasNr2Lte = true;
            this.mEnterTime = SystemClock.elapsedRealtime();
            CommonDataRecoverFlow.this.sendMessageDelayed(6, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }

        public boolean processMessage(Message message) {
            CommonDataRecoverFlow.this.logV("processMessage:" + message.what);
            int i = message.what;
            if (i == 3 || i == 6) {
                CommonDataRecoverFlow commonDataRecoverFlow = CommonDataRecoverFlow.this;
                commonDataRecoverFlow.mCurrentDataRat = commonDataRecoverFlow.getCurrentRat(commonDataRecoverFlow.mSlotId);
                if (this.mFixed) {
                    return true;
                }
                int i2 = CommonDataRecoverFlow.this.mCurrentDataRat;
                CommonDataRecoverFlow commonDataRecoverFlow2 = CommonDataRecoverFlow.this;
                if (i2 != 1) {
                    commonDataRecoverFlow2.log("rat not downgraded");
                    if (message.what == 6) {
                        CommonDataRecoverFlow commonDataRecoverFlow3 = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow3.markCellOptFailed(commonDataRecoverFlow3.mCurrentGci);
                        CommonDataRecoverFlow commonDataRecoverFlow4 = CommonDataRecoverFlow.this;
                        commonDataRecoverFlow4.transitionTo(commonDataRecoverFlow4.mDefaultState);
                    }
                    return true;
                }
                commonDataRecoverFlow2.checkCellDataAvailable(this, commonDataRecoverFlow2.mLteReselectState);
            } else {
                if (i != 11) {
                    return false;
                }
                CommonDataRecoverFlow.this.onReturnToDefaultTimeout();
            }
            return true;
        }

        public void exit() {
            this.mEnterTime = 0L;
            this.mFixed = false;
            this.mReachabilityCookie = 0L;
        }
    }

    public static void reportCommonDataOptStatistics() {
        CommonDataRecoverFlow commonDataRecoverFlow = sDataRecoverFlow;
        if (commonDataRecoverFlow != null) {
            try {
                commonDataRecoverFlow.reportCommonDataRecoverData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reportCommonDataRecoverData() {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        ArrayList arrayList = new ArrayList();
        FixedSizeArray fixedSizeArray = this.mBlacklistCellInfos;
        int size = fixedSizeArray.size();
        int i = 0;
        while (i < size) {
            Object obj = fixedSizeArray.get(i);
            i++;
            CellData cellData = (CellData) obj;
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("gci", cellData.gci);
            concurrentHashMap2.put("action", Integer.valueOf(cellData.action));
            concurrentHashMap2.put("launch_time", Long.valueOf(cellData.launchTime));
            concurrentHashMap2.put("opt_count", Integer.valueOf(cellData.optCount));
            arrayList.add(concurrentHashMap2);
        }
        concurrentHashMap.put("data", arrayList);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "common_data_recover", concurrentHashMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentRat(int i) {
        ServiceState serviceState;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            return 0;
        }
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        if (rilDataRadioTechnology == 14 || rilDataRadioTechnology == 19) {
            return 1;
        }
        return (rilDataRadioTechnology == 20 || rilDataRadioTechnology == 30) ? 2 : 0;
    }

    private boolean cellChangeTooFrequently() {
        if (this.mCellChangeInterval.size() < this.mCellChangeInterval.mMaxSize) {
            return false;
        }
        FixedSizeArray fixedSizeArray = this.mCellChangeInterval;
        int size = fixedSizeArray.size();
        long jLongValue = 0;
        int i = 0;
        while (i < size) {
            Object obj = fixedSizeArray.get(i);
            i++;
            jLongValue += ((Long) obj).longValue();
        }
        logV("dump change interval:" + this.mCellChangeInterval);
        return jLongValue / ((long) this.mCellChangeInterval.mMaxSize) < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS && (isGoodNrCell() || isGoodLteCell());
    }

    private boolean limitNr2Lte(State state) {
        if (state != this.mDisableSaEndcState || !isGoodNrCell()) {
            return false;
        }
        log("signal is good, no Nr2Lte");
        transitionTo(this.mDefaultState);
        return true;
    }

    private void dumpIntentExtras(Intent intent) {
        Bundle extras;
        if (intent == null || (extras = intent.getExtras()) == null) {
            return;
        }
        for (String str : extras.keySet()) {
            Object obj = extras.get(str);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": ");
            sb.append(obj != null ? obj.toString() : "null");
            log(sb.toString());
        }
    }
}
