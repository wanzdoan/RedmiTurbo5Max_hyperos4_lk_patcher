package com.android.phone;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MtkNetworkOptimizer extends NetworkOptimizerBase implements TelephonyStatAdapter.ICallBack {
    private static final Map TOAST_MSG_MAP;
    public static boolean mCloudGlobalStreamMediaOn = true;
    public static boolean mCloudGlobalStreamMediaOnV2 = true;
    private static int mCurrentNwOpt = 0;
    private static boolean mIsEnableDebugGlobalDataOpt = false;
    private boolean VDBG;
    private boolean enterWechat;
    private final A2ThreshOpt mA2ThreshOpt;
    private final AbnormalNddsOpt mAbNddsOpt;
    private CloudModemController mCloudMdCtrl;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private int mCurrentScene;
    private final CurrentTopAppObserver mCurrentTopAppObserver;
    protected int mDataType;
    private FastRecovery mFastRecovery;
    private final GameNddsOpt mGameNddsOpt;
    private int mHomeUid;
    private final HstModeOpt mHstOpt;
    private boolean mIsHst;
    private boolean mIsMobileDataConnected;
    private boolean mMdGameModeOptOngoing;
    private MiuiAppStateMonitor mMiuiAppStateMonitor;
    private PackageManager mPackageManager;
    private final TelCallback mTelCallback;
    private TelephonyManager mTelephonyManager;
    private Toast mToast;
    private boolean isStreaming = false;
    private final Set OPTIMIZATION_MCC_SET = new HashSet(Arrays.asList("452", "454", "502", "510", "520", "460"));
    protected boolean mIsScreenOn = false;
    protected int mLastToModemDataType = 0;
    private boolean isBeginVideoOptimization = false;
    private boolean isBeginGameOptimization = false;
    private boolean mIsEnableMdGameOpt = false;
    private boolean mIsEnableSbpOpt = false;
    private boolean mIsEnableGameSafOpt = false;
    private boolean mIsEnableVideoSafOpt = false;
    private boolean mIsEnableLiveSafOpt = false;
    protected ConcurrentHashMap mGlobalStreamStats = new ConcurrentHashMap();
    private HashSet lastLteModes = new HashSet();
    private HashSet lastNrModes = new HashSet();
    private ArrayList lastLteStreamModes = new ArrayList();
    private ArrayList lastNrStreamModes = new ArrayList();
    private ArrayList lastColdLteStreamModes = new ArrayList();
    private ArrayList lastColdNrStreamModes = new ArrayList();
    private boolean hasSendDlDataStallAction = false;
    private boolean hasSendColdDlDataStallAction = false;
    private Runnable mAutoStopColdDataStallTask = null;
    private Runnable mSafValidityVerityTask = null;
    private boolean mIsEnableLargeBW = false;
    private boolean mIsEnableLLM = false;
    private boolean mIsStreamStall = false;
    private boolean mIsEnableModemPerceptionModel = false;
    private int mDataStallSourceType = 0;
    private boolean mIsNeedDoDataReconnectAction = true;
    private boolean mIsNeedDoPsAttachAction = false;
    public boolean isVideoOpt = false;
    public int[] durationStatistic = new int[10];
    public int lastQuality = -1;
    private final ConcurrentHashMap mVideoStatsList = new ConcurrentHashMap();
    private ArrayList mGlobalSafDetailList = new ArrayList();
    private int mCurrentAppUid = -1;
    private int mLastPrioUid = -1;
    public ArrayList mGlobalSafDataList = new ArrayList();
    public int mLastDataType = 0;
    private final BroadcastReceiver mReceiver = new AnonymousClass1();
    private int[] mActiveDataSubInfo = {-1, -1};
    private final Handler mWorkHandler = new AnonymousClass2(MiuiThreadUtil.getWorkThreadLooper());
    private final BaseEvent.IEventListener mDisplayListener = new AnonymousClass3();

    interface BaseOpt {
        void updateActionPhase(int i);
    }

    static {
        HashMap map = new HashMap();
        TOAST_MSG_MAP = map;
        Locale locale = Locale.ROOT;
        map.put("beginVideoOpt".toLowerCase(locale), "beginVideoOpt");
        map.put("exitVideoOpt".toLowerCase(locale), "exitVideoOpt");
        map.put("beginGameOpt".toLowerCase(locale), "beginGameOpt");
        map.put("exitGameOpt".toLowerCase(locale), "exitGameOpt");
        map.put("enterLive".toLowerCase(locale), "enterLive");
        map.put("exitLive".toLowerCase(locale), "exitLive");
        map.put("triggerSafOpt".toLowerCase(locale), "triggerSafOpt");
    }

    enum MdOptimizationScene {
        Default(0),
        ShortVideo(1);

        private int value;

        MdOptimizationScene(int i) {
            this.value = i;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.MtkNetworkOptimizer$1, reason: invalid class name */
    class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(103, intent).sendToTarget();
                return;
            }
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(intent.getAction())) {
                MtkNetworkOptimizer.this.mWorkHandler.sendMessage(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(107, intent));
                return;
            }
            if ("com.android.phone.intent.action.WECHAT_VOIP_SCENE".equals(intent.getAction())) {
                MtkNetworkOptimizer.this.enterWechat = intent.getBooleanExtra("wechatVoipEnabled", false);
                MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(109, Boolean.valueOf(MtkNetworkOptimizer.this.enterWechat)).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                if (Settings.Global.getInt(MtkNetworkOptimizer.this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1 || MtkNetworkOptimizer.this.mFastRecovery == null) {
                    return;
                }
                MtkNetworkOptimizer.this.mFastRecovery.mIsAPMOffLingering = true;
                MtkNetworkOptimizer.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0();
                    }
                }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                return;
            }
            if ("com.android.phone.miui.MtkNetworkOptimizer.debug".equals(intent.getAction())) {
                MtkNetworkOptimizer.this.VDBG = true;
                if (MtkNetworkOptimizer.this.mFastRecovery != null) {
                    MtkNetworkOptimizer.this.mFastRecovery.T1 = intent.getIntExtra("T1", 5);
                    MtkNetworkOptimizer.this.mFastRecovery.T2 = intent.getIntExtra("T2", 3);
                    MtkNetworkOptimizer.this.mFastRecovery.T3 = intent.getIntExtra("T3", 180);
                    MtkNetworkOptimizer.this.mFastRecovery.RSRP_THRES = intent.getIntExtra("rsrp_threshold", -105);
                    MtkNetworkOptimizer.this.log("FastRecovery t1:" + MtkNetworkOptimizer.this.mFastRecovery.T1 + " t2:" + MtkNetworkOptimizer.this.mFastRecovery.T2 + " t3:" + MtkNetworkOptimizer.this.mFastRecovery.T3 + " rsrp:" + MtkNetworkOptimizer.this.mFastRecovery.RSRP_THRES);
                }
                MtkNetworkOptimizer.mCurrentNwOpt = intent.getIntExtra(a.n, 255);
                intent.getIntExtra("testNotifyHstFlag", -1);
                return;
            }
            if ("com.android.phone.miui.MtkNetworkOptimizer.globalDataOpt.debug".equals(intent.getAction())) {
                if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
                    String stringExtra = intent.getStringExtra("debugType");
                    MtkNetworkOptimizer.this.log("DBG_GLOBAL_DATA_OPT_ACTION debugType = " + stringExtra);
                    if ("showToast".equals(stringExtra)) {
                        boolean booleanExtra = intent.getBooleanExtra("enable", false);
                        MtkNetworkOptimizer.this.log("DBG_GLOBAL_DATA_OPT_ACTION enable = " + booleanExtra);
                        MtkNetworkOptimizer.this.setEnableDebugGlobalDataOptState(booleanExtra);
                        return;
                    }
                    if ("ucr".equals(stringExtra)) {
                        String stringExtra2 = intent.getStringExtra("urcData");
                        MtkNetworkOptimizer.this.log("DBG_GLOBAL_DATA_OPT_ACTION modemUrc data = " + stringExtra2);
                        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
                        if (phone == null) {
                            MtkNetworkOptimizer.this.log("modemUrc dataPhone is null.");
                            return;
                        } else {
                            MtkNetworkOptimizer.this.mWorkHandler.sendMessage(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(102, new AsyncResult(phone, stringExtra2, (Throwable) null)));
                            return;
                        }
                    }
                    if ("stopBenefit".equals(stringExtra)) {
                        MtkNetworkOptimizer.this.log("DBG_GLOBAL_DATA_OPT_ACTION stopBenefit mDataType = " + MtkNetworkOptimizer.this.mDataType + " , isVideoOpt = " + MtkNetworkOptimizer.this.isVideoOpt);
                        MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                        int i = mtkNetworkOptimizer.mDataType;
                        if ((i == 1 || i == 4) && mtkNetworkOptimizer.isVideoOpt) {
                            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                            MtkNetworkOptimizer mtkNetworkOptimizer2 = MtkNetworkOptimizer.this;
                            MdOptimizationScene mdOptimizationScene = MdOptimizationScene.ShortVideo;
                            mtkNetworkOptimizer2.getTotalDuration(concurrentHashMap, mdOptimizationScene.value, "Before");
                            MtkNetworkOptimizer.this.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(118, mdOptimizationScene.value, 0, concurrentHashMap), 10000L);
                            MtkNetworkOptimizer.this.isVideoOpt = false;
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if ("com.android.phone.intent.action.LINK_RELIABILITY".equals(intent.getAction())) {
                if (MtkNetworkOptimizer.mCloudGlobalStreamMediaOn) {
                    MtkNetworkOptimizer mtkNetworkOptimizer3 = MtkNetworkOptimizer.this;
                    int i2 = mtkNetworkOptimizer3.mDataType;
                    if (i2 != 1 && i2 != 4) {
                        mtkNetworkOptimizer3.log("ignore linkStall with type mismatch.");
                        return;
                    }
                    int intExtra = intent.getIntExtra("netId", -1);
                    int intExtra2 = intent.getIntExtra("metricsCause", -1);
                    MtkNetworkOptimizer.this.log("receiver ACTION_LINK_RELIABILITY netId:" + intExtra + " cause:" + intExtra2 + " , mIsStreamStall = " + MtkNetworkOptimizer.this.mIsStreamStall + " , mDataType = " + MtkNetworkOptimizer.this.mDataType + " , mDataStallSourceType = " + MtkNetworkOptimizer.this.mDataStallSourceType);
                    MtkNetworkOptimizer mtkNetworkOptimizer4 = MtkNetworkOptimizer.this;
                    mtkNetworkOptimizer4.mIsStreamStall = mtkNetworkOptimizer4.mIsStreamStall ^ true;
                    boolean z = MtkNetworkOptimizer.this.mIsStreamStall;
                    MtkNetworkOptimizer mtkNetworkOptimizer5 = MtkNetworkOptimizer.this;
                    if (z) {
                        mtkNetworkOptimizer5.mWorkHandler.sendMessage(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(120, MtkNetworkOptimizer.this.mDataType, 0));
                        MtkNetworkOptimizer.this.processModemStallState();
                        MtkNetworkOptimizer.this.mDataStallSourceType = 1;
                        MtkNetworkOptimizer.this.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET, 1), 10000L);
                        return;
                    }
                    if (mtkNetworkOptimizer5.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET)) {
                        MtkNetworkOptimizer.this.mWorkHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET);
                        return;
                    }
                    return;
                }
                return;
            }
            if ("miui.intent.action.VIDEO_INFO".equals(intent.getAction()) && MtkNetworkOptimizer.mCloudGlobalStreamMediaOn) {
                MtkNetworkOptimizer.this.processVideoInfoChangeAction(intent.getIntExtra("render-start", -1), intent.getIntExtra("render-quality", -1));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            MtkNetworkOptimizer.this.mFastRecovery.mIsAPMOffLingering = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processVideoInfoChangeAction(int i, int i2) {
        logV("processVideoInfoChangeAction quality:" + i2 + " isVideoInFront:" + i + ",mDataType:" + this.mDataType);
        if (i != -1) {
            this.lastQuality = 1;
        }
        if (i2 != -1) {
            this.lastQuality = i2;
        }
        int i3 = this.lastQuality;
        if (i3 == 0) {
            if (this.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET)) {
                return;
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET), 3000L);
            return;
        }
        if (i3 == 1) {
            if (this.mIsStreamStall) {
                this.mIsStreamStall = false;
            }
            if (this.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET)) {
                this.mWorkHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET);
            }
            if (this.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET)) {
                this.mWorkHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.phone.MtkNetworkOptimizer$2, reason: invalid class name */
    class AnonymousClass2 extends Handler {
        AnonymousClass2(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Code duplicated, block: B:131:0x03c6 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:132:0x03c8  */
        /* JADX WARN: Code duplicated, block: B:134:0x03d0  */
        /* JADX WARN: Code duplicated, block: B:135:0x03d2  */
        /* JADX WARN: Code duplicated, block: B:137:0x03d8 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:138:0x03da  */
        /* JADX WARN: Code duplicated, block: B:140:0x03e2  */
        /* JADX WARN: Code duplicated, block: B:141:0x03e4  */
        /* JADX WARN: Code duplicated, block: B:145:0x03f1  */
        /* JADX WARN: Code duplicated, block: B:149:0x0401 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:150:0x0403  */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i;
            int i2;
            int i3;
            int i4;
            switch (message.what) {
                case 100:
                case 109:
                    MtkNetworkOptimizer.this.onStateChanged(((Boolean) message.obj).booleanValue(), message.what);
                    break;
                case 101:
                    try {
                        MtkNetworkOptimizer.this.onAtCmdResponse(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case 102:
                    try {
                        MtkNetworkOptimizer.this.onAtUrcInd(message.obj);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                    break;
                case 103:
                    NetworkInfo networkInfo = (NetworkInfo) ((Intent) message.obj).getParcelableExtra("networkInfo");
                    if (networkInfo != null && networkInfo.getType() == 0) {
                        MtkNetworkOptimizer.this.onStateChanged(MiuiPhoneUtils.isMobileDataConnected(), message.what);
                    }
                    break;
                case 106:
                    MtkNetworkOptimizer.this.updateCloudFeatureStatus();
                    MtkNetworkOptimizer.this.updateCloudState();
                    break;
                case 107:
                    int intExtra = ((Intent) message.obj).getIntExtra("android.telephony.extra.SIM_STATE", 1);
                    if (MtkNetworkOptimizer.this.mFastRecovery != null) {
                        MtkNetworkOptimizer.this.mFastRecovery.onSimStateChanged(intExtra);
                    }
                    break;
                case 108:
                    boolean z = message.arg1 > 0;
                    int i5 = message.arg2;
                    int i6 = MtkNetworkOptimizer.this.mCurrentScene;
                    boolean z2 = MtkNetworkOptimizer.this.mIsMobileDataConnected;
                    if (i5 == 100) {
                        if (i5 == 100) {
                            MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                            i3 = mtkNetworkOptimizer.mCurrentScene;
                            if (z) {
                                i4 = i3 | 1;
                            } else {
                                i4 = i3 & (-2);
                            }
                            mtkNetworkOptimizer.mCurrentScene = i4;
                        } else if (i5 == 109) {
                            MtkNetworkOptimizer mtkNetworkOptimizer2 = MtkNetworkOptimizer.this;
                            i = mtkNetworkOptimizer2.mCurrentScene;
                            if (z) {
                                i2 = i | 2;
                            } else {
                                i2 = i & (-3);
                            }
                            mtkNetworkOptimizer2.mCurrentScene = i2;
                        }
                        if (i6 != MtkNetworkOptimizer.this.mCurrentScene) {
                            if (!MtkNetworkOptimizer.this.mIsMobileDataConnected && z) {
                                MtkNetworkOptimizer.this.enterSceneAction();
                            } else if (!z) {
                                MtkNetworkOptimizer.this.exitSceneAction(false);
                            }
                        }
                    } else if (i5 == 103) {
                        MtkNetworkOptimizer.this.mIsMobileDataConnected = z;
                        if (z2 != MtkNetworkOptimizer.this.mIsMobileDataConnected) {
                            boolean z3 = MtkNetworkOptimizer.this.mIsMobileDataConnected;
                            MtkNetworkOptimizer mtkNetworkOptimizer3 = MtkNetworkOptimizer.this;
                            if (!z3) {
                                mtkNetworkOptimizer3.exitSceneAction(true);
                            } else if (mtkNetworkOptimizer3.mCurrentScene > 0) {
                                MtkNetworkOptimizer.this.enterSceneAction();
                            }
                            MtkNetworkOptimizer mtkNetworkOptimizer4 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer4.processMobileNetworkChange(mtkNetworkOptimizer4.mIsMobileDataConnected);
                        }
                    } else if (i5 == 109) {
                        if (i5 == 100) {
                            MtkNetworkOptimizer mtkNetworkOptimizer5 = MtkNetworkOptimizer.this;
                            i3 = mtkNetworkOptimizer5.mCurrentScene;
                            if (z) {
                                i4 = i3 | 1;
                            } else {
                                i4 = i3 & (-2);
                            }
                            mtkNetworkOptimizer5.mCurrentScene = i4;
                        } else if (i5 == 109) {
                            MtkNetworkOptimizer mtkNetworkOptimizer6 = MtkNetworkOptimizer.this;
                            i = mtkNetworkOptimizer6.mCurrentScene;
                            if (z) {
                                i2 = i | 2;
                            } else {
                                i2 = i & (-3);
                            }
                            mtkNetworkOptimizer6.mCurrentScene = i2;
                        }
                        if (i6 != MtkNetworkOptimizer.this.mCurrentScene) {
                            if (!MtkNetworkOptimizer.this.mIsMobileDataConnected) {
                                if (!z) {
                                    MtkNetworkOptimizer.this.exitSceneAction(false);
                                }
                            } else if (!z) {
                                MtkNetworkOptimizer.this.exitSceneAction(false);
                            }
                        }
                    }
                    MtkNetworkOptimizer.this.log("current scene:" + MtkNetworkOptimizer.this.mCurrentScene);
                    break;
                case 110:
                    if (MtkNetworkOptimizer.this.mAbNddsOpt.mStarted) {
                        MtkNetworkOptimizer.this.mAbNddsOpt.updateActionPhase(3);
                    }
                    break;
                case 111:
                    MtkNetworkOptimizer.this.log("Receiver UID_CHANGED mIsMobileDataConnected = " + MtkNetworkOptimizer.this.mIsMobileDataConnected + " , mLastToModemDataType = " + MtkNetworkOptimizer.this.mLastToModemDataType);
                    if (MtkNetworkOptimizer.this.mIsMobileDataConnected || MtkNetworkOptimizer.this.mLastToModemDataType != 0) {
                        if (message.arg1 > 0) {
                            MtkNetworkOptimizer.this.log("Not supported under Split Screen");
                            break;
                        } else {
                            ArrayList arrayList = (ArrayList) message.obj;
                            if (arrayList != null && arrayList.size() >= 0) {
                                ComponentName componentName = (ComponentName) arrayList.get(0);
                                int i7 = MtkNetworkOptimizer.this.mDataType;
                                if (componentName != null) {
                                    String packageName = componentName.getPackageName();
                                    if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2()) {
                                        try {
                                            MtkNetworkOptimizer mtkNetworkOptimizer7 = MtkNetworkOptimizer.this;
                                            mtkNetworkOptimizer7.mCurrentAppUid = mtkNetworkOptimizer7.mPackageManager.getPackageUid(packageName, 0);
                                        } catch (Exception unused) {
                                            MtkNetworkOptimizer.this.log(componentName + " NOT FOUND!");
                                            return;
                                        }
                                    }
                                    if (MiuiPhoneUtils.isContainsPackage("config_global_data_opt_video_whitelist_pkgs", packageName)) {
                                        MtkNetworkOptimizer.this.mDataType = 4;
                                    } else {
                                        boolean zIsContainsPackage = MiuiPhoneUtils.isContainsPackage("config_global_data_opt_game_whitelist_pkgs", packageName);
                                        MtkNetworkOptimizer mtkNetworkOptimizer8 = MtkNetworkOptimizer.this;
                                        if (zIsContainsPackage) {
                                            mtkNetworkOptimizer8.mDataType = 2;
                                        } else {
                                            mtkNetworkOptimizer8.mDataType = 0;
                                        }
                                    }
                                    MtkNetworkOptimizer.this.log("tempDataType = " + i7 + " , mDataType = " + MtkNetworkOptimizer.this.mDataType);
                                    MtkNetworkOptimizer mtkNetworkOptimizer9 = MtkNetworkOptimizer.this;
                                    if (i7 != mtkNetworkOptimizer9.mDataType) {
                                        mtkNetworkOptimizer9.processStreamBenefitStatistic();
                                        MtkNetworkOptimizer mtkNetworkOptimizer10 = MtkNetworkOptimizer.this;
                                        mtkNetworkOptimizer10.processDataAppTypeChange(i7, mtkNetworkOptimizer10.mDataType);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 112:
                    int iIntValue = ((Integer) message.obj).intValue();
                    MtkNetworkOptimizer.this.log("EVENT_DATA_APP_CHANGE_TYPE type:" + iIntValue + " , isBeginVideoOptimization = " + MtkNetworkOptimizer.this.isBeginVideoOptimization + " , isBeginGameOptimization = " + MtkNetworkOptimizer.this.isBeginGameOptimization);
                    if (4 == iIntValue) {
                        MtkNetworkOptimizer.this.notifySceneChangeToModem(iIntValue);
                        MtkNetworkOptimizer.this.beginVideoOptimization();
                        if (MtkNetworkOptimizer.this.isBeginGameOptimization) {
                            MtkNetworkOptimizer.this.exitGameOptimization();
                        }
                    } else if (2 == iIntValue) {
                        if (MtkNetworkOptimizer.this.isBeginVideoOptimization) {
                            MtkNetworkOptimizer mtkNetworkOptimizer11 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer11.mLastDataType = 4;
                            mtkNetworkOptimizer11.exitVideoOptimization();
                            MtkNetworkOptimizer.this.notifySceneChangeToModem(iIntValue);
                        }
                        MtkNetworkOptimizer.this.beginGameOptimization();
                    } else if (iIntValue == 0) {
                        if (MtkNetworkOptimizer.this.isBeginVideoOptimization) {
                            MtkNetworkOptimizer mtkNetworkOptimizer12 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer12.mLastDataType = 4;
                            mtkNetworkOptimizer12.exitVideoOptimization();
                        }
                        if (MtkNetworkOptimizer.this.isBeginGameOptimization) {
                            MtkNetworkOptimizer mtkNetworkOptimizer13 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer13.mLastDataType = 2;
                            mtkNetworkOptimizer13.exitGameOptimization();
                        }
                        MtkNetworkOptimizer.this.resetVideoAndGameOptState();
                        MtkNetworkOptimizer.this.notifySceneChangeToModem(iIntValue);
                    }
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                    if (((Boolean) message.obj).booleanValue()) {
                        if (MiuiPhoneUtils.isTopAppForFeatureConfig("config_global_data_opt_video_whitelist_pkgs")) {
                            MtkNetworkOptimizer.this.log("isScreenOn true.  mDataType = " + MtkNetworkOptimizer.this.mDataType);
                            MtkNetworkOptimizer.this.notifySceneChangeToModem(4);
                            MtkNetworkOptimizer.this.beginVideoOptimization();
                        } else if (MiuiPhoneUtils.isTopAppForFeatureConfig("config_global_data_opt_game_whitelist_pkgs")) {
                            MtkNetworkOptimizer.this.beginGameOptimization();
                        }
                        MtkNetworkOptimizer.this.processStreamBenefitStatistic();
                    } else {
                        MtkNetworkOptimizer.this.notifySceneChangeToModem(0);
                        if (MtkNetworkOptimizer.this.isBeginVideoOptimization) {
                            MtkNetworkOptimizer.this.log("isScreenOn false.  mDataType = " + MtkNetworkOptimizer.this.mDataType);
                            MtkNetworkOptimizer mtkNetworkOptimizer14 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer14.mLastDataType = 4;
                            mtkNetworkOptimizer14.exitVideoOptimization();
                        }
                        if (MtkNetworkOptimizer.this.isBeginGameOptimization) {
                            MtkNetworkOptimizer mtkNetworkOptimizer15 = MtkNetworkOptimizer.this;
                            mtkNetworkOptimizer15.mLastDataType = 2;
                            mtkNetworkOptimizer15.exitGameOptimization();
                        }
                        MtkNetworkOptimizer.this.resetBenefitStatisticList();
                    }
                    break;
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                    int iIntValue2 = ((Integer) message.obj).intValue();
                    MtkNetworkOptimizer.this.log("receiver EVENT_CHECK_VIDEO_STALL_STATE LinkQos keep 10s source = " + iIntValue2 + " , mIsStreamStall = " + MtkNetworkOptimizer.this.mIsStreamStall);
                    if (MtkNetworkOptimizer.this.mIsStreamStall && iIntValue2 == 1) {
                        MtkNetworkOptimizer.this.mIsStreamStall = false;
                        break;
                    }
                    break;
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                    MtkNetworkOptimizer.this.log("receiver VIDEO_INFO_STALL keep 3s mIsStreamStall = " + MtkNetworkOptimizer.this.mIsStreamStall);
                    if (!MtkNetworkOptimizer.this.mIsStreamStall) {
                        MtkNetworkOptimizer.this.mIsStreamStall = true;
                        MtkNetworkOptimizer.this.mDataStallSourceType = 2;
                        MtkNetworkOptimizer.this.mWorkHandler.sendMessage(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(120, MtkNetworkOptimizer.this.mDataType, 0));
                        MtkNetworkOptimizer.this.processModemStallState();
                        MtkNetworkOptimizer.this.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET), 3000L);
                        MtkNetworkOptimizer.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$2$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$handleMessage$0();
                            }
                        }, 30000L);
                    }
                    break;
                case 117:
                    int i8 = message.arg1;
                    int i9 = message.arg2;
                    MtkNetworkOptimizer mtkNetworkOptimizer16 = MtkNetworkOptimizer.this;
                    mtkNetworkOptimizer16.durationStatistic[i9] = 0;
                    mtkNetworkOptimizer16.logV("EVENT_BEGIN_VIDEO_BENEFIT scene = " + i8 + " , lastQuality = " + MtkNetworkOptimizer.this.lastQuality + " , mIsStreamStall = " + MtkNetworkOptimizer.this.mIsStreamStall);
                    if (i8 == MdOptimizationScene.ShortVideo.value) {
                        MtkNetworkOptimizer mtkNetworkOptimizer17 = MtkNetworkOptimizer.this;
                        if (mtkNetworkOptimizer17.lastQuality == 0 || mtkNetworkOptimizer17.mIsStreamStall) {
                            int[] iArr = MtkNetworkOptimizer.this.durationStatistic;
                            iArr[i9] = iArr[i9] | 1;
                        }
                    }
                    MtkNetworkOptimizer.this.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(117, i8, (i9 + 1) % 10), 1000L);
                    break;
                case 118:
                    ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) message.obj;
                    int i10 = message.arg1;
                    int i11 = MtkNetworkOptimizer.this.mDataType;
                    if ((i11 == 1 || i11 == 4) && i10 == MdOptimizationScene.ShortVideo.value) {
                        MtkNetworkOptimizer mtkNetworkOptimizer18 = MtkNetworkOptimizer.this;
                        mtkNetworkOptimizer18.updateVideoAndGameAiStats("global_short_video_benefit", mtkNetworkOptimizer18.getTotalDuration(concurrentHashMap, i10, "After"));
                    }
                    break;
                case 119:
                    MtkNetworkOptimizer.this.processCellLocationChangeForBenefitStatistic((CellLocation) message.obj, message.arg1);
                    break;
                case 120:
                    MtkNetworkOptimizer.this.beginStreamDataStall(message.arg1, message.arg2);
                    break;
                case 121:
                    MtkNetworkOptimizer.this.stopStreamDataStall(((Integer) message.obj).intValue());
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            MtkNetworkOptimizer.this.log("receiver VIDEO_INFO_STALL keep 30s mIsStreamStall = " + MtkNetworkOptimizer.this.mIsStreamStall + " , mDataStallSourceType = " + MtkNetworkOptimizer.this.mDataStallSourceType);
            if (MtkNetworkOptimizer.this.mIsStreamStall && MtkNetworkOptimizer.this.mDataStallSourceType == 2) {
                MtkNetworkOptimizer.this.mIsStreamStall = false;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.phone.MtkNetworkOptimizer$3, reason: invalid class name */
    class AnonymousClass3 implements BaseEvent.IEventListener {
        private boolean mLastScreenOn;

        AnonymousClass3() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public synchronized void onEvent(BaseEvent baseEvent) {
            try {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        MtkNetworkOptimizer.this.mWorkHandler.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                        final boolean zIsScreenOn = displayEvent.isScreenOn();
                        MtkNetworkOptimizer.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$3$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onEvent$0(zIsScreenOn);
                            }
                        }, MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, MirrorDesktopHelper.TIMEOUT_MILLIS);
                        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
                            if (!MtkNetworkOptimizer.mCloudGlobalStreamMediaOn) {
                                return;
                            }
                            MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                            if (mtkNetworkOptimizer.mIsScreenOn != zIsScreenOn) {
                                mtkNetworkOptimizer.mIsScreenOn = zIsScreenOn;
                                if (mtkNetworkOptimizer.mWorkHandler.hasMessages(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX)) {
                                    MtkNetworkOptimizer mtkNetworkOptimizer2 = MtkNetworkOptimizer.this;
                                    if (!mtkNetworkOptimizer2.mIsScreenOn) {
                                        return;
                                    } else {
                                        mtkNetworkOptimizer2.mWorkHandler.removeMessages(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX);
                                    }
                                }
                                MtkNetworkOptimizer mtkNetworkOptimizer3 = MtkNetworkOptimizer.this;
                                mtkNetworkOptimizer3.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, Boolean.valueOf(MtkNetworkOptimizer.this.mIsScreenOn)), mtkNetworkOptimizer3.mIsScreenOn ? 0 : 60000);
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$0(boolean z) {
            if (this.mLastScreenOn != z) {
                MtkNetworkOptimizer.this.log("last screen on:" + this.mLastScreenOn);
                this.mLastScreenOn = z;
                MtkNetworkOptimizer.this.checkIfEFD(z);
                MtkNetworkOptimizer.this.notifyScreenToModem(this.mLastScreenOn);
            }
        }
    }

    private MtkNetworkOptimizer() {
        this.mTelCallback = new TelCallback();
        this.mHomeUid = -1;
        this.mCurrentTopAppObserver = new CurrentTopAppObserver();
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mContentResolver = phoneGlobals.getContentResolver();
        registerAtUrc();
        observeGameStatus();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("com.android.phone.miui.MtkNetworkOptimizer.debug");
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            intentFilter.addAction("com.android.phone.miui.MtkNetworkOptimizer.globalDataOpt.debug");
            PreferenceUtils preferenceUtils = PreferenceUtils.getInstance();
            Boolean bool = Boolean.TRUE;
            mCloudGlobalStreamMediaOn = preferenceUtils.getBooleanPreference("mtkGlobalStreamState", bool).booleanValue();
            mCloudGlobalStreamMediaOnV2 = PreferenceUtils.getInstance().getBooleanPreference("mtkGlobalStreamStateV2", bool).booleanValue();
            if (mCloudGlobalStreamMediaOn) {
                intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
                intentFilter.addAction("miui.intent.action.VIDEO_INFO");
                regAppStateMonitor(true);
            }
        }
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() && mCloudGlobalStreamMediaOnV2) {
            this.mPackageManager = phoneGlobals.getPackageManager();
            ComponentName homeActivities = this.mPackageManager.getHomeActivities(new ArrayList());
            if (homeActivities != null) {
                try {
                    this.mHomeUid = this.mPackageManager.getPackageUid(homeActivities.getPackageName(), 0);
                    log("mHomeUid=" + this.mHomeUid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reloadGlobalVideoStatisticFromSp();
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        initNwOptMask();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
        this.mGameNddsOpt = new GameNddsOpt();
        this.mHstOpt = new HstModeOpt();
        this.mAbNddsOpt = new AbnormalNddsOpt();
        this.mA2ThreshOpt = new A2ThreshOpt();
        log("MtkNetworkOptimizer()");
        if (MiuiPhoneUtils.PHONE_COUNT == 2) {
            if (isSubFeatureEnabled(32)) {
                this.mFastRecovery = new FastRecovery("FastRecovery", MiuiThreadUtil.getWorkThreadLooper());
            }
            EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener, this.mWorkHandler);
            TelephonyStatAdapter.registerCallBack(this);
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        telephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mWorkHandler), this.mTelCallback);
        new MiuiAppStateMonitor.AppScene(this.mWorkHandler) { // from class: com.android.phone.MtkNetworkOptimizer.4
            private boolean mIsGaming;

            @Override // com.android.phone.utils.MiuiAppStateMonitor.AppScene
            public void sceneEnter(String str) {
                str.hashCode();
                int i = 5;
                switch (str) {
                    case "social":
                        i = 7;
                        break;
                    case "wechat":
                        i = 3;
                        break;
                    case "shopping":
                        i = 9;
                        break;
                    case "streaming":
                        MtkNetworkOptimizer.this.isStreaming = true;
                        break;
                    case "game":
                        this.mIsGaming = true;
                        MtkNetworkOptimizer.this.setMdGameModeOpt(true);
                        i = 1;
                        break;
                    case "wifi":
                        if (this.mIsGaming) {
                            MtkNetworkOptimizer.this.setMdGameModeOpt(false);
                        }
                        i = 11;
                        break;
                    default:
                        MtkNetworkOptimizer.this.log("invalid scene arg");
                        return;
                }
                FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{"AT+EDMFAPP=20102,1," + i, "+EDMFAPP"}, (Message) null);
                ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateAppScene(str, true);
            }

            @Override // com.android.phone.utils.MiuiAppStateMonitor.AppScene
            public void sceneExit(String str) {
                str.hashCode();
                int i = 4;
                switch (str) {
                    case "social":
                        i = 8;
                        break;
                    case "wechat":
                        break;
                    case "shopping":
                        i = 10;
                        break;
                    case "streaming":
                        MtkNetworkOptimizer.this.isStreaming = false;
                        i = 6;
                        break;
                    case "game":
                        this.mIsGaming = false;
                        MtkNetworkOptimizer.this.setMdGameModeOpt(false);
                        i = 2;
                        break;
                    case "wifi":
                        if (this.mIsGaming) {
                            MtkNetworkOptimizer.this.setMdGameModeOpt(true);
                        }
                        i = 12;
                        break;
                    default:
                        MtkNetworkOptimizer.this.log("invalid exit scene arg");
                        return;
                }
                FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{"AT+EDMFAPP=20102,1," + i, "+EDMFAPP"}, (Message) null);
                ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateAppScene(str, false);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mWorkHandler.sendEmptyMessage(106);
        }
    }

    class CurrentTopAppObserver implements MiuiAppStateMonitor.TopAppObserver {
        private CurrentTopAppObserver() {
        }

        @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
        public void onTopAppChanged(List list, int i) {
            MtkNetworkOptimizer.this.mWorkHandler.sendMessageDelayed(MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(111, i, 0, list), 500L);
        }
    }

    public static boolean isMtkNetworkOptimizeSupported() {
        return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_is_mtk_network_optimize_supported);
    }

    public static NetworkOptimizerBase make() {
        if (NetworkOptimizerBase.sInstance instanceof MtkNetworkOptimizer) {
            return NetworkOptimizerBase.sInstance;
        }
        if ((!Build.IS_INTERNATIONAL_BUILD && MiuiPhoneUtils.isMtkDx1Platform()) || isMtkNetworkOptimizeSupported()) {
            NetworkOptimizerBase.sInstance = new MtkNetworkOptimizer();
        }
        return NetworkOptimizerBase.sInstance;
    }

    private void initNwOptMask() {
        int i = ArrayUtils.contains(this.mContext.getResources().getStringArray(R.array.config_md_gamemode_opt_country), SystemProperties.get("ro.miui.region", "")) ? 184 : 56;
        int intPreference = PreferenceUtils.getInstance().getIntPreference("cloud_nw_opt_mask");
        if (!Build.IS_INTERNATIONAL_BUILD) {
            i = Integer.MAX_VALUE;
        }
        if (intPreference <= 0) {
            intPreference = i;
        }
        mCurrentNwOpt = intPreference;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMdGameModeOpt(boolean z) {
        int i;
        Phone phone;
        if ((MiuiPhoneUtils.isMobileDataConnected() || !z) && z != this.mMdGameModeOptOngoing) {
            if (isSubFeatureEnabled(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) || !z) {
                int[] activeDataPhoneInfo = getActiveDataPhoneInfo();
                if (activeDataPhoneInfo[0] == -1 || (i = activeDataPhoneInfo[1]) == -1 || (phone = MiuiPhoneUtils.getPhone(i)) == null) {
                    return;
                }
                this.mMdGameModeOptOngoing = z;
                FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_GAME_MODE_OPT", z ? "1" : "0"}, (Message) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message getRespMessage(MtkAtCmd mtkAtCmd, BaseOpt baseOpt) {
        return this.mWorkHandler.obtainMessage(101, new Object[]{mtkAtCmd, baseOpt});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStateChanged(boolean z, int i) {
        this.mWorkHandler.removeMessages(108);
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(108, z ? 1 : 0, i), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enterSceneAction() {
        if ((this.mCurrentScene & 1) != 0 && !this.mGameNddsOpt.mStarted) {
            this.mGameNddsOpt.updateActionPhase(1);
            this.mGameNddsOpt.mStarted = true;
        }
        this.mA2ThreshOpt.onConditionChanged(this.mActiveDataSubInfo[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitSceneAction(boolean z) {
        int i = this.mCurrentScene;
        if (i == 0 || z || i == 2) {
            this.mGameNddsOpt.updateActionPhase(4);
            this.mGameNddsOpt.mStarted = false;
        }
        this.mA2ThreshOpt.onConditionChanged(this.mActiveDataSubInfo[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAtCmdResponse(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult == null || asyncResult.exception != null) {
            return;
        }
        Object[] objArr = (Object[]) asyncResult.userObj;
        MtkAtCmd mtkAtCmd = (MtkAtCmd) objArr[0];
        BaseOpt baseOpt = (BaseOpt) objArr[1];
        String[] strArr = (String[]) asyncResult.result;
        log("onAtCmdResponse " + mtkAtCmd);
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            log("AT RESP:" + strArr[i]);
        }
        int iOrdinal = mtkAtCmd.ordinal();
        if (iOrdinal != 3) {
            if (iOrdinal != 12) {
                if (iOrdinal != 13) {
                    if (iOrdinal != 17) {
                        if (iOrdinal != 18) {
                            return;
                        }
                    }
                }
                baseOpt.updateActionPhase(3);
                return;
            }
            if ("0".equals(strArr[0].split(":")[1].trim())) {
                baseOpt.updateActionPhase(2);
                return;
            } else {
                baseOpt.updateActionPhase(3);
                return;
            }
        }
        strArr[0].split(",")[1].equals("0");
        baseOpt.updateActionPhase(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAtUrcInd(Object obj) {
        AsyncResult asyncResult = (AsyncResult) obj;
        if (asyncResult.exception != null) {
            return;
        }
        int phoneId = ((Phone) asyncResult.userObj).getPhoneId();
        String str = (String) asyncResult.result;
        log(phoneId + " onAtUrcInd:" + str);
        if (str.startsWith("+EDMFAPP:")) {
            String[] strArrSplit = str.split(",");
            String strTrim = strArrSplit[0].split(":")[1].trim();
            logV("onAtUrcInd data:" + Arrays.toString(strArrSplit));
            int i = 3;
            if ("20002".equals(strTrim)) {
                int i2 = Integer.parseInt(strArrSplit[1]);
                int i3 = Integer.parseInt(strArrSplit[2]);
                if (i2 == 300) {
                    boolean z = i3 == 1;
                    this.mIsHst = z;
                    HstModeOpt hstModeOpt = this.mHstOpt;
                    if (z) {
                        hstModeOpt.updateActionPhase(1);
                    } else {
                        hstModeOpt.updateActionPhase(4);
                    }
                }
                if (i2 == 206) {
                    log("onPsAttachInd slot:" + phoneId + " attach:" + i3);
                }
                if (i2 == 301) {
                    if (strArrSplit.length < 4) {
                        return;
                    }
                    int i4 = Integer.parseInt(strArrSplit[3]);
                    if (i3 == 68 && i4 == 15 && phoneId == this.mActiveDataSubInfo[1]) {
                        enterAbnormalNddsScene();
                    }
                }
                if (i2 == 302 && this.mAbNddsOpt.mStarted) {
                    this.mAbNddsOpt.updateActionPhase(4);
                    unregisterDataRatChanged();
                    this.mAbNddsOpt.mStarted = false;
                    return;
                }
                return;
            }
            if ("20100".equals(strTrim)) {
                if (!isSubFeatureEnabled(16)) {
                    log("AFR not enabled");
                    return;
                }
                if (!this.isStreaming && !this.enterWechat) {
                    int i5 = Integer.parseInt(strArrSplit[1]);
                    int i6 = Integer.parseInt(strArrSplit[2]);
                    if (i6 == 502) {
                        log("NR_MEAS_RESULT_INFO sub:" + i5 + " evt:" + i6 + " pci:" + Integer.parseInt(strArrSplit[3]) + " narfcn:" + Integer.parseInt(strArrSplit[4]));
                        FiveGManagerBase.getInstance().sendAtCommand(i5, new String[]{"AT+ERETNR", ""}, (Message) null);
                        i = 5;
                    }
                    if (i6 == 503) {
                        log("HST_ENHANCEMENTS_RESULT_INFO sub:" + i5 + " evt:" + i6 + " version:" + Integer.parseInt(strArrSplit[i]) + " ue_moving_in_high_speed:" + Integer.parseInt(strArrSplit[i + 1]) + " hsr_cell_count:" + Integer.parseInt(strArrSplit[i + 2]) + " hsr_cell_count_threshold:" + Integer.parseInt(strArrSplit[i + 3]) + " non_hsr_cell_count:" + Integer.parseInt(strArrSplit[i + 4]) + " non_hsr_cell_count_threshold:" + Integer.parseInt(strArrSplit[i + 5]) + " inactivity_timer_ongoing:" + Integer.parseInt(strArrSplit[i + 6]) + " ue_in_hsr_mode:" + Integer.parseInt(strArrSplit[i + 7]) + " highspeedflag:" + Integer.parseInt(strArrSplit[i + 8]));
                        return;
                    }
                    return;
                }
                log("Scene is streaming, skip AFR");
                return;
            }
            if ("20006".equals(strTrim)) {
                if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
                    log("ignore process 20006 urc msg for not support.");
                    return;
                }
                int i7 = this.mDataType;
                if (i7 != 1 && i7 != 4) {
                    log("ignore process 20006 urc msg for dataType mismatch.");
                    return;
                }
                if (strArrSplit.length < 3) {
                    return;
                }
                int i8 = Integer.parseInt(strArrSplit[1]);
                if (i8 == 501) {
                    boolean z2 = Integer.parseInt(strArrSplit[2]) == 1;
                    logV("receiver ModemUrcMessage isStall = " + z2 + " , mIsStreamStall = " + this.mIsStreamStall + " , mDataType = " + this.mDataType);
                    boolean z3 = this.mIsStreamStall;
                    if (!z3 && z2) {
                        Handler handler = this.mWorkHandler;
                        handler.sendMessage(handler.obtainMessage(120, this.mDataType, 0));
                        this.mIsStreamStall = true;
                        this.mDataStallSourceType = 3;
                    } else if (z3 && !z2) {
                        this.mIsStreamStall = false;
                        if (this.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET)) {
                            this.mWorkHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET);
                        }
                        if (this.mWorkHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET)) {
                            this.mWorkHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET);
                        }
                    }
                    if (strArrSplit.length == 4) {
                        int i9 = Integer.parseInt(strArrSplit[3]);
                        if (i9 == 2) {
                            if (this.mDataType != 1) {
                                logV("receiver ModemUrcMessage notifySceneChangeToModem live mDataType = " + this.mDataType);
                                this.mDataType = 1;
                                notifySceneChangeToModem(1);
                                enableSafOpt(1, true);
                                showToast("enterLive");
                                return;
                            }
                            showToast("enterLive");
                            return;
                        }
                        if (i9 == 1) {
                            if (this.mDataType != 4) {
                                logV("receiver ModemUrcMessage notifySceneChangeToModem shortVideo mDataType = " + this.mDataType);
                                this.mDataType = 4;
                                notifySceneChangeToModem(4);
                                enableSafOpt(4, true);
                                showToast("enterShortVideo");
                                return;
                            }
                            showToast("enterShortVideo");
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (i8 == 502 && strArrSplit.length == 4) {
                    if (this.mSafValidityVerityTask != null) {
                        logV("ignore process 502 msg for saf is verification in progress");
                        return;
                    }
                    final int i10 = Integer.parseInt(strArrSplit[2]);
                    final int i11 = Integer.parseInt(strArrSplit[3]);
                    final AtomicInteger atomicInteger = new AtomicInteger();
                    logV("receiver saf ModemUrcMessage mode = " + i10 + " ,cause = " + i11);
                    if (this.mSafValidityVerityTask == null) {
                        Runnable runnable = new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onAtUrcInd$1(atomicInteger, i10, i11);
                            }
                        };
                        this.mSafValidityVerityTask = runnable;
                        this.mWorkHandler.postDelayed(runnable, 10000L);
                    }
                    showToast("triggerSafOpt");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAtUrcInd$1(AtomicInteger atomicInteger, int i, int i2) {
        if (!this.mIsStreamStall) {
            atomicInteger.set(1);
        }
        this.mSafValidityVerityTask = null;
        logV("record saf whether valid mode:" + i + ",cause:" + i2 + ",isValid:" + atomicInteger.get() + ",mGlobalSafDetailList:" + this.mGlobalSafDetailList);
        recordGlobalDetail(i, i2, atomicInteger.get(), this.mGlobalSafDetailList);
    }

    private void registerAtUrc() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.registerForOemIndication(this.mWorkHandler, 102, phone);
            }
        }
    }

    private void observeGameStatus() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("gb_boosting"), false, new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.MtkNetworkOptimizer.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                MtkNetworkOptimizer.this.mWorkHandler.obtainMessage(100, Boolean.valueOf(Settings.Secure.getInt(MtkNetworkOptimizer.this.mContentResolver, "gb_boosting", 0) == 1)).sendToTarget();
            }
        });
    }

    private void registerDataRatChanged() {
        Phone phone = PhoneFactory.getPhone(this.mActiveDataSubInfo[1]);
        if (phone == null) {
            return;
        }
        phone.getServiceStateTracker().registerForDataRegStateOrRatChanged(1, this.mWorkHandler, 110, (Object) null);
    }

    private void unregisterDataRatChanged() {
        for (int i : this.mActiveDataSubInfo) {
            if (i == -1) {
                return;
            }
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.getServiceStateTracker().unregisterForDataRegStateOrRatChanged(1, this.mWorkHandler);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("MtkNetworkOptimizer", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("MtkNetworkOptimizer", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmd(int i, MtkAtCmd mtkAtCmd, Message message) {
        if (i < 0) {
            return;
        }
        log("sendAtCmd " + mtkAtCmd);
        FiveGManagerBase.getInstance().sendAtCommand(i, mtkAtCmd.cmd(), message);
    }

    private void sendAtCmd(String[] strArr, Message message, int i) {
        if (MiuiPhoneUtils.getPhone(i) == null) {
            log("get phone failed and error");
        } else {
            FiveGManagerBase.getInstance().sendAtCommand(i, strArr, message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getActiveDataPhoneInfo() {
        int[] iArr = {-1, -1};
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        int phoneId = SubscriptionManager.getPhoneId(activeDataSubscriptionId);
        if (phoneId == -1) {
            log("error activeSubId=" + activeDataSubscriptionId);
            phoneId = 0;
        }
        iArr[0] = phoneId;
        if (MiuiPhoneUtils.getInsertedSimCount() > 1) {
            iArr[1] = phoneId == 0 ? 1 : 0;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentRat(int i) {
        ServiceState serviceState;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            log("null phone/ServiceState");
            return 0;
        }
        if (serviceState.getRilDataRadioTechnology() == 20) {
            return 2;
        }
        if (serviceState.getRilDataRadioTechnology() == 14) {
            return 1;
        }
        log("unavailable HST RAT " + serviceState.getRilDataRadioTechnology());
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudFeatureStatus() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyMtkFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        int i = cloudData.getInt("cloud_nw_opt_mask", Integer.MAX_VALUE);
                        log("updateCloudFeatureStatus MtkNetworkOptimizer=" + i);
                        if (i != mCurrentNwOpt) {
                            this.mGameNddsOpt.tryRecovery();
                            this.mHstOpt.tryRecovery();
                            this.mAbNddsOpt.tryRecovery();
                            mCurrentNwOpt = i;
                            PreferenceUtils.getInstance().setIntPreference("cloud_nw_opt_mask", i);
                            FastRecovery fastRecovery = this.mFastRecovery;
                            if (fastRecovery != null) {
                                fastRecovery.initFastRecovery();
                            }
                        }
                    }
                }
                return;
            }
            log("updateCloudFeatureStatus cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudState() {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            log("updateCloudState is not support");
            return;
        }
        if (FiveGCloudController.getInstance() == null) {
            log("updateCloudState return for FiveGCloud is no Instantiation.");
            return;
        }
        try {
            List listFetchSelfStudyCloudStatus = FiveGCloudController.getInstance().fetchSelfStudyCloudStatus();
            if (listFetchSelfStudyCloudStatus != null && !listFetchSelfStudyCloudStatus.isEmpty()) {
                Iterator it = listFetchSelfStudyCloudStatus.iterator();
                while (it.hasNext()) {
                    processSingleCloudData((MiuiSettings.SettingsCloudData.CloudData) it.next());
                }
                return;
            }
            log("updateCloudState return for dataList is isEmpty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSingleCloudData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        String string = cloudData.getString("DataMode", "NA");
        log("updateCloudState cloudDataMode = " + string);
        if ("MtkGlobalDataOpt".equalsIgnoreCase(string)) {
            handleModeV1(cloudData);
        } else if ("MtkGlobalDataOptV2".equalsIgnoreCase(string)) {
            handleModeV2(cloudData);
        }
    }

    private void handleModeV1(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        int i = cloudData.getInt("mtkGlobalStreamOptStatus", 1);
        boolean z = i != 0;
        log("updateCloudState globalDataOptStatus = " + i + " , streamMediaCloudStatus = " + z);
        if (z != mCloudGlobalStreamMediaOn) {
            mCloudGlobalStreamMediaOn = z;
            regAppStateMonitor(z);
            if (!mCloudGlobalStreamMediaOn) {
                resetVideoAndGameOptState();
            }
            PreferenceUtils.getInstance().setBooleanPreference("mtkGlobalStreamState", mCloudGlobalStreamMediaOn);
        }
    }

    private void handleModeV2(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        int i = cloudData.getInt("mtkGlobalStreamOptStatus", 1);
        boolean z = i != 0;
        log("updateCloudState globalDataOptStatusV2 = " + i + " , streamMediaCloudStatus = " + z);
        if (z != mCloudGlobalStreamMediaOnV2) {
            mCloudGlobalStreamMediaOnV2 = z;
            if (!z) {
                resetVideoAndGameOptStateV2();
            }
            PreferenceUtils.getInstance().setBooleanPreference("mtkGlobalStreamStateV2", mCloudGlobalStreamMediaOnV2);
        }
    }

    @Override // com.android.phone.NetworkOptimizerBase
    public boolean isCloudGlobalStreamMediaOn() {
        return mCloudGlobalStreamMediaOn;
    }

    enum MtkAtCmd {
        ENABLE_DMF("AT+EDMFAPP=20002,6,1,1", "+EDMFAPP"),
        ENTER_GAME("AT+EDMFAPP=20002,2,1,1", "+EDMFAPP"),
        EXIT_GAME("AT+EDMFAPP=20002,2,1,0", "+EDMFAPP"),
        QUERY_ENDC("AT+EGMC=0,\"endc_deactivation\"", "+EGMC"),
        ENABLE_ENDC("AT+EGMC=1,\"endc_deactivation\",0,1", "+EGMC"),
        DISABLE_ENDC("AT+EGMC=1,\"endc_deactivation\",1,1", "+EGMC"),
        HST_UNDER_LTE("AT+EGMC=1,\"customize_q_rxlv_min\",1,-50,-65,-65", "+EGMC"),
        ENABLE_NR_SKIP_RPT("AT+ESBP=5,\"SBP_ERRC_THRESH_BASED_SKIP_NR_CELL_REPORT\",1", "+ESBP"),
        DISABLE_NR_SKIP_RPT("AT+ESBP=5,\"SBP_ERRC_THRESH_BASED_SKIP_NR_CELL_REPORT\",0", "+ESBP"),
        SET_NR_ERRC_THR("AT+EGMC=1,\"errc_nr_threshold\",1, -456,-55,0,-424,-48,0", "+EGMC"),
        ENABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE("AT+ESBP=6,\"SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE\",15", "+ESBP"),
        DISABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE("AT+ESBP=6,\"SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE\",36", "+ESBP"),
        QUERY_LTE_QRXLV_SBP("AT+ESBP=7,\"SBP_CUSTOM_QRXLEVL_QQUAL\"", "+ESBP"),
        ENABLE_LTE_QRXLV_SBP("AT+ESBP=5,\"SBP_CUSTOM_QRXLEVL_QQUAL\",1", "+ESBP"),
        DISABLE_LTE_QRXLV_SBP("AT+ESBP=5,\"SBP_CUSTOM_QRXLEVL_QQUAL\",0", "+ESBP"),
        DECREASE_LTE_RXLV_MIN("AT+EGMC=1,\"customize_q_rxlv_min\",1,-60,-70,-60", "+EGMC"),
        RESOTRE_LTE_RXLV_MIN("AT+EGMC=1,\"customize_q_rxlv_min\",0,-60,-70,-60", "+EGMC"),
        QUERY_NR_CAMP_SBP("AT+ESBP=7,\"SBP_NRRC_CAMP_ON_CRITERIA\"", "+ESBP"),
        ENABLE_NR_CAMP_SBP("AT+ESBP=5,\"SBP_NRRC_CAMP_ON_CRITERIA\",1", "+ESBP"),
        DISABLE_NR_CAMP_SBP("AT+ESBP=5,\"SBP_NRRC_CAMP_ON_CRITERIA\",0", "+ESBP"),
        INCR_SA_CAMP_CRITERIA("AT+EGMC=1,\"EGMC_NRRC_CAMP_ON_CRITERIA\",-53,-22,-53,-43,-12,-43", "+EGMC"),
        RESTORE_SA_CAMP_CRITERIA("AT+EGMC=1,\"EGMC_NRRC_CAMP_ON_CRITERIA\",-70,-22,-70,-43,-12,-43", "+EGMC"),
        IGNORE_NDDS_PS_PAGING("AT+EGMC=1,\"non_data_sim_ignore_ps_paging\",1", "+EGMC"),
        RESTORE_NDDS_PS_PAGING("AT+EGMC=1,\"non_data_sim_ignore_ps_paging\",0", "+EGMC"),
        DISABLE_IMS("AT+EIMSCFG=0,0,0,0,1,1", "+EIMSCFG"),
        ENABLE_IMS("AT+EIMSCFG=1,1,0,0,1,1", "+EIMSCFG");

        String atCmd0;
        String atCmd1;

        MtkAtCmd(String str, String str2) {
            this.atCmd0 = str;
            this.atCmd1 = str2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String[] cmd() {
            String str = this.atCmd1;
            String str2 = this.atCmd0;
            if (str != null) {
                return new String[]{str2, str};
            }
            return new String[]{str2};
        }
    }

    class GameNddsOpt implements BaseOpt {
        boolean mNddsDecrRxlv;
        boolean mOrigSbpEnable = true;
        private boolean mStarted;

        GameNddsOpt() {
        }

        @Override // com.android.phone.MtkNetworkOptimizer.BaseOpt
        public void updateActionPhase(int i) {
            if (MtkNetworkOptimizer.isSubFeatureEnabled(1)) {
                if (i == 1) {
                    MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                    int i2 = mtkNetworkOptimizer.mActiveDataSubInfo[1];
                    MtkAtCmd mtkAtCmd = MtkAtCmd.QUERY_LTE_QRXLV_SBP;
                    mtkNetworkOptimizer.sendAtCmd(i2, mtkAtCmd, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd, this));
                    return;
                }
                if (i == 2) {
                    if (!this.mOrigSbpEnable) {
                        MtkNetworkOptimizer mtkNetworkOptimizer2 = MtkNetworkOptimizer.this;
                        int i3 = mtkNetworkOptimizer2.mActiveDataSubInfo[1];
                        MtkAtCmd mtkAtCmd2 = MtkAtCmd.ENABLE_LTE_QRXLV_SBP;
                        mtkNetworkOptimizer2.sendAtCmd(i3, mtkAtCmd2, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd2, this));
                    }
                    this.mOrigSbpEnable = false;
                    return;
                }
                if (i == 3) {
                    MtkNetworkOptimizer mtkNetworkOptimizer3 = MtkNetworkOptimizer.this;
                    int i4 = mtkNetworkOptimizer3.mActiveDataSubInfo[1];
                    MtkAtCmd mtkAtCmd3 = MtkAtCmd.DECREASE_LTE_RXLV_MIN;
                    mtkNetworkOptimizer3.sendAtCmd(i4, mtkAtCmd3, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd3, this));
                    this.mNddsDecrRxlv = true;
                    PreferenceUtils.getInstance().setBooleanPreference("game_ndds_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[1], true);
                    return;
                }
                if (i != 4) {
                    return;
                }
                if (!this.mOrigSbpEnable) {
                    MtkNetworkOptimizer mtkNetworkOptimizer4 = MtkNetworkOptimizer.this;
                    int i5 = mtkNetworkOptimizer4.mActiveDataSubInfo[1];
                    MtkAtCmd mtkAtCmd4 = MtkAtCmd.DISABLE_LTE_QRXLV_SBP;
                    mtkNetworkOptimizer4.sendAtCmd(i5, mtkAtCmd4, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd4, this));
                    this.mOrigSbpEnable = true;
                }
                if (this.mNddsDecrRxlv) {
                    MtkNetworkOptimizer mtkNetworkOptimizer5 = MtkNetworkOptimizer.this;
                    int i6 = mtkNetworkOptimizer5.mActiveDataSubInfo[1];
                    MtkAtCmd mtkAtCmd5 = MtkAtCmd.RESOTRE_LTE_RXLV_MIN;
                    mtkNetworkOptimizer5.sendAtCmd(i6, mtkAtCmd5, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd5, this));
                    PreferenceUtils.getInstance().setBooleanPreference("game_ndds_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[1], false);
                    this.mNddsDecrRxlv = false;
                }
            }
        }

        public void tryRecovery() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (PreferenceUtils.getInstance().getBooleanPreference("game_ndds_opt" + i, Boolean.FALSE).booleanValue()) {
                    MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                    MtkAtCmd mtkAtCmd = MtkAtCmd.RESOTRE_LTE_RXLV_MIN;
                    mtkNetworkOptimizer.sendAtCmd(i, mtkAtCmd, mtkNetworkOptimizer.getRespMessage(mtkAtCmd, this));
                    PreferenceUtils.getInstance().setBooleanPreference("game_ndds_opt" + i, false);
                    this.mNddsDecrRxlv = false;
                }
            }
            this.mOrigSbpEnable = true;
            this.mNddsDecrRxlv = false;
            this.mStarted = false;
        }
    }

    class HstModeOpt implements BaseOpt {
        boolean mImproveDdsRxlv;
        boolean mOrigSbpEnable = true;

        HstModeOpt() {
        }

        @Override // com.android.phone.MtkNetworkOptimizer.BaseOpt
        public void updateActionPhase(int i) {
            if (MtkNetworkOptimizer.isSubFeatureEnabled(4)) {
                if (i == 1) {
                    if (!MtkNetworkOptimizer.this.mIsHst || this.mImproveDdsRxlv) {
                        return;
                    }
                    MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                    int currentRat = mtkNetworkOptimizer.getCurrentRat(mtkNetworkOptimizer.mActiveDataSubInfo[0]);
                    if (currentRat > 1) {
                        MtkNetworkOptimizer mtkNetworkOptimizer2 = MtkNetworkOptimizer.this;
                        int i2 = mtkNetworkOptimizer2.mActiveDataSubInfo[0];
                        MtkAtCmd mtkAtCmd = MtkAtCmd.QUERY_NR_CAMP_SBP;
                        mtkNetworkOptimizer2.sendAtCmd(i2, mtkAtCmd, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd, this));
                        return;
                    }
                    if (currentRat > 0) {
                        MtkNetworkOptimizer mtkNetworkOptimizer3 = MtkNetworkOptimizer.this;
                        int i3 = mtkNetworkOptimizer3.mActiveDataSubInfo[0];
                        MtkAtCmd mtkAtCmd2 = MtkAtCmd.QUERY_LTE_QRXLV_SBP;
                        mtkNetworkOptimizer3.sendAtCmd(i3, mtkAtCmd2, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd2, this));
                        return;
                    }
                    return;
                }
                if (i == 2) {
                    this.mOrigSbpEnable = false;
                    if (MtkNetworkOptimizer.this.mIsHst) {
                        MtkNetworkOptimizer mtkNetworkOptimizer4 = MtkNetworkOptimizer.this;
                        int i4 = mtkNetworkOptimizer4.mActiveDataSubInfo[0];
                        MtkAtCmd mtkAtCmd3 = MtkAtCmd.ENABLE_LTE_QRXLV_SBP;
                        mtkNetworkOptimizer4.sendAtCmd(i4, mtkAtCmd3, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd3, this));
                        return;
                    }
                    return;
                }
                if (i == 3) {
                    if (MtkNetworkOptimizer.this.mIsHst) {
                        MtkNetworkOptimizer mtkNetworkOptimizer5 = MtkNetworkOptimizer.this;
                        int i5 = mtkNetworkOptimizer5.mActiveDataSubInfo[0];
                        MtkAtCmd mtkAtCmd4 = MtkAtCmd.HST_UNDER_LTE;
                        mtkNetworkOptimizer5.sendAtCmd(i5, mtkAtCmd4, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd4, this));
                        this.mImproveDdsRxlv = true;
                        PreferenceUtils.getInstance().setBooleanPreference("hst_mode_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[0], true);
                        return;
                    }
                    return;
                }
                if (i != 4) {
                    return;
                }
                if (!this.mOrigSbpEnable) {
                    MtkNetworkOptimizer mtkNetworkOptimizer6 = MtkNetworkOptimizer.this;
                    int i6 = mtkNetworkOptimizer6.mActiveDataSubInfo[0];
                    MtkAtCmd mtkAtCmd5 = MtkAtCmd.DISABLE_LTE_QRXLV_SBP;
                    mtkNetworkOptimizer6.sendAtCmd(i6, mtkAtCmd5, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd5, this));
                    this.mOrigSbpEnable = true;
                }
                if (this.mImproveDdsRxlv) {
                    MtkNetworkOptimizer mtkNetworkOptimizer7 = MtkNetworkOptimizer.this;
                    int i7 = mtkNetworkOptimizer7.mActiveDataSubInfo[0];
                    MtkAtCmd mtkAtCmd6 = MtkAtCmd.RESOTRE_LTE_RXLV_MIN;
                    mtkNetworkOptimizer7.sendAtCmd(i7, mtkAtCmd6, MtkNetworkOptimizer.this.getRespMessage(mtkAtCmd6, this));
                    PreferenceUtils.getInstance().setBooleanPreference("hst_mode_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[0], false);
                    this.mImproveDdsRxlv = false;
                }
            }
        }

        public void tryRecovery() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (PreferenceUtils.getInstance().getBooleanPreference("hst_mode_opt" + i, Boolean.FALSE).booleanValue()) {
                    MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
                    MtkAtCmd mtkAtCmd = MtkAtCmd.RESOTRE_LTE_RXLV_MIN;
                    mtkNetworkOptimizer.sendAtCmd(i, mtkAtCmd, mtkNetworkOptimizer.getRespMessage(mtkAtCmd, this));
                    PreferenceUtils.getInstance().setBooleanPreference("hst_mode_opt" + i, false);
                    this.mImproveDdsRxlv = false;
                }
            }
            this.mOrigSbpEnable = true;
            this.mImproveDdsRxlv = false;
        }
    }

    class AbnormalNddsOpt implements BaseOpt {
        private int mCount;
        private boolean mHasSetNetType;
        private boolean mRadioOff;
        private boolean mStarted;
        private long mLastOptTime = -1;
        private int mRat = -1;

        AbnormalNddsOpt() {
        }

        @Override // com.android.phone.MtkNetworkOptimizer.BaseOpt
        public void updateActionPhase(int i) {
            ServiceState serviceState;
            if (MtkNetworkOptimizer.isSubFeatureEnabled(64)) {
                if (i == 3) {
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    if (!this.mHasSetNetType) {
                        long j = this.mLastOptTime;
                        if (j > 0 && jElapsedRealtime - j < 1800000) {
                            MtkNetworkOptimizer.this.log("AbnormalNddsOpt linger time");
                            return;
                        }
                    }
                    HashMap map = new HashMap();
                    Phone phone = PhoneFactory.getPhone(MtkNetworkOptimizer.this.mActiveDataSubInfo[1]);
                    if (phone == null || (serviceState = phone.getServiceState()) == null) {
                        MtkNetworkOptimizer.this.log("AbnormalNddsOpt null phone/ServiceState");
                        return;
                    }
                    int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
                    MtkNetworkOptimizer.this.log("AbnormalNddsOpt ndds phone id is " + MtkNetworkOptimizer.this.mActiveDataSubInfo[1] + ", rat is " + rilDataRadioTechnology);
                    if (!this.mHasSetNetType && ServiceState.rilRadioTechnologyToAccessNetworkType(rilDataRadioTechnology) == 4) {
                        int rafFromNetworkType = RadioAccessFamily.getRafFromNetworkType(7);
                        phone.setAllowedNetworkTypes(0, rafFromNetworkType, (Message) null);
                        this.mHasSetNetType = true;
                        this.mLastOptTime = SystemClock.elapsedRealtime();
                        MtkNetworkOptimizer.this.log("AbnormalNddsOpt set network type is" + rafFromNetworkType);
                        map.put("AbNddsOpt", "disableLteOrNr");
                        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "abnormal_ndds_opt", map);
                        PreferenceUtils.getInstance().setBooleanPreference("abnormal_ndds_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[1], true);
                        return;
                    }
                    if (this.mRadioOff || !this.mHasSetNetType || rilDataRadioTechnology == this.mRat) {
                        return;
                    }
                    int i2 = this.mCount + 1;
                    this.mCount = i2;
                    this.mRat = rilDataRadioTechnology;
                    if (i2 == 3) {
                        phone.setRadioPower(false, false, false, false);
                        this.mRadioOff = true;
                        this.mCount = 0;
                        MtkNetworkOptimizer.this.log("AbnormalNddsOpt radio off");
                        map.put("AbNddsOpt", "radioPowerOff");
                        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "abnormal_ndds_opt", map);
                        PreferenceUtils.getInstance().setBooleanPreference("abnormal_ndds_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[1], true);
                    }
                    MtkNetworkOptimizer.this.log("AbnormalNddsOpt Rat Change Count is " + this.mCount);
                    return;
                }
                if (i == 4 && this.mHasSetNetType) {
                    recoverPhoneNetworkType(MtkNetworkOptimizer.this.mActiveDataSubInfo[1]);
                    PreferenceUtils.getInstance().setBooleanPreference("abnormal_ndds_opt" + MtkNetworkOptimizer.this.mActiveDataSubInfo[1], false);
                }
            }
        }

        public void tryRecovery() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (PreferenceUtils.getInstance().getBooleanPreference("abnormal_ndds_opt" + i, Boolean.FALSE).booleanValue()) {
                    recoverPhoneNetworkType(i);
                    PreferenceUtils.getInstance().setBooleanPreference("abnormal_ndds_opt" + i, false);
                }
            }
            this.mStarted = false;
            this.mCount = 0;
            this.mRat = -1;
        }

        private void recoverPhoneNetworkType(int i) {
            MtkNetworkOptimizer.this.log("recoverPhoneNetworkType for slot=" + i);
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                MtkNetworkOptimizer.this.log("recoverPhoneNetworkType phone is null");
                return;
            }
            if (this.mRadioOff) {
                MtkNetworkOptimizer.this.log("recoverPhoneNetworkType radio on");
                phone.setRadioPower(true, false, false, false);
                this.mRadioOff = false;
            }
            if (this.mHasSetNetType) {
                MtkNetworkOptimizer.this.log("recoverPhoneNetworkType set network type");
                phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(24), (Message) null);
                this.mHasSetNetType = false;
            }
        }
    }

    class TelCallback extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        private int mLastDdsSub;

        private TelCallback() {
            this.mLastDdsSub = Integer.MAX_VALUE;
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            MtkNetworkOptimizer.this.log("onActiveDataSubscriptionIdChanged " + i);
            int i2 = this.mLastDdsSub;
            if (i2 == Integer.MAX_VALUE || (i2 != i && i2 != Integer.MAX_VALUE)) {
                MtkNetworkOptimizer.this.mGameNddsOpt.tryRecovery();
                MtkNetworkOptimizer.this.mHstOpt.tryRecovery();
                MtkNetworkOptimizer.this.mAbNddsOpt.tryRecovery();
            }
            MtkNetworkOptimizer mtkNetworkOptimizer = MtkNetworkOptimizer.this;
            mtkNetworkOptimizer.mActiveDataSubInfo = mtkNetworkOptimizer.getActiveDataPhoneInfo();
            if (this.mLastDdsSub != i && SubscriptionManager.isValidPhoneId(MtkNetworkOptimizer.this.mActiveDataSubInfo[0])) {
                FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{"AT+EDMFAPP=20102,2," + MtkNetworkOptimizer.this.mActiveDataSubInfo[0], "+EDMFAPP"}, (Message) null);
                MtkNetworkOptimizer.this.mA2ThreshOpt.onConditionChanged(MtkNetworkOptimizer.this.mActiveDataSubInfo[0]);
            }
            this.mLastDdsSub = i;
            if (MtkNetworkOptimizer.this.mCloudMdCtrl != null) {
                MtkNetworkOptimizer.this.mCloudMdCtrl.onActiveDataPhoneSlotChanged(MtkNetworkOptimizer.this.mActiveDataSubInfo[0]);
            }
        }
    }

    private void enterAbnormalNddsScene() {
        if (!isSubFeatureEnabled(64) || !HyperOSCustFeatureResolve.getBoolean("support_abnormal_ndds_bool", false)) {
            log("enterAbnormalNddsScene: not enabled!");
            return;
        }
        int i = this.mActiveDataSubInfo[1];
        if (i == -1) {
            log("enterAbnormalNddsScene: one sim card!");
            return;
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (!ServiceProviderUtils.isChinaTelecom(MiuiPhoneUtils.getOperatorNumeric(phone)) || isCtVoLTEOnlySupport(phone)) {
            log("enterAbnormalNddsScene: not support!");
            return;
        }
        if (this.mAbNddsOpt.mStarted) {
            return;
        }
        log("enterAbnormalNddsScene: reset register for " + i);
        unregisterDataRatChanged();
        registerDataRatChanged();
        this.mAbNddsOpt.mStarted = true;
    }

    public void onServiceStateChanged(final int i, final ServiceState serviceState) {
        if (serviceState == null || !SubscriptionManager.isValidPhoneId(i)) {
            return;
        }
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onServiceStateChanged$2(i, serviceState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceStateChanged$2(int i, ServiceState serviceState) {
        FastRecovery fastRecovery = this.mFastRecovery;
        if (fastRecovery != null) {
            fastRecovery.onServiceStateChanged(i, serviceState);
        }
        String operatorNumeric = serviceState.getOperatorNumeric();
        if (!TextUtils.isEmpty(operatorNumeric) && !operatorNumeric.startsWith("460")) {
            mCurrentNwOpt &= -17;
        }
        this.mA2ThreshOpt.onConditionChanged(i);
    }

    public void onCellLocationChanged(int i, CellLocation cellLocation) {
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() && mCloudGlobalStreamMediaOnV2) {
            if (this.mDataType == 0) {
                log("ignore onCellLocationChanged for dataType mismatch.");
            } else {
                this.mWorkHandler.obtainMessage(119, i, 0, cellLocation).sendToTarget();
            }
        }
    }

    class FastRecovery extends StateMachine {
        private int RSRP_THRES;
        private int T1;
        private int T2;
        private int T3;
        private final State mDefaultState;
        private boolean mIsAPMOffLingering;
        private final ConcurrentHashMap mLast45GTime;
        private int mLastRSCFGSlot;
        private final State mOosState;
        private final int[] mPhoneRegState;
        private String[] mRegOperatorNumeric;

        private FastRecovery(String str, Looper looper) {
            super(str, looper);
            this.T1 = 5;
            this.T2 = 3;
            this.T3 = 180;
            this.RSRP_THRES = -105;
            this.mLastRSCFGSlot = -1;
            this.mPhoneRegState = new int[]{3, 3};
            DefaultState defaultState = new DefaultState();
            this.mDefaultState = defaultState;
            OosState oosState = new OosState();
            this.mOosState = oosState;
            this.mLast45GTime = new ConcurrentHashMap();
            this.mRegOperatorNumeric = new String[MiuiPhoneUtils.PHONE_COUNT];
            addState(defaultState);
            addState(oosState);
            setInitialState(defaultState);
            start();
            initFastRecovery();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (MiuiPhoneUtils.getInsertedSimCount() < 2) {
                return;
            }
            if (!TextUtils.isEmpty(serviceState.getOperatorNumeric())) {
                this.mRegOperatorNumeric[i] = serviceState.getOperatorNumeric();
            }
            if (isNwConditionMatched()) {
                sendMessage(100, i, 0, serviceState);
                return;
            }
            State currentState = getCurrentState();
            State state = this.mDefaultState;
            if (currentState != state) {
                transitionTo(state);
            }
        }

        private boolean isNwConditionMatched() {
            if (Build.IS_INTERNATIONAL_BUILD && !TextUtils.isEmpty(this.mRegOperatorNumeric[0]) && !TextUtils.isEmpty(this.mRegOperatorNumeric[1]) && !TextUtils.equals(this.mRegOperatorNumeric[0].substring(0, 3), this.mRegOperatorNumeric[1].substring(0, 3))) {
                return false;
            }
            Phone[] phones = PhoneFactory.getPhones();
            if (phones != null) {
                for (Phone phone : phones) {
                    ServiceState serviceState = phone.getServiceState();
                    if (serviceState != null && serviceState.getRoaming()) {
                        return false;
                    }
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forceScan(int i, boolean z) {
            int i2;
            if (MiuiPhoneUtils.isPhoneInCall()) {
                log("skip forceScan due to ongoing voice call");
                return;
            }
            log("forceScan on phone:" + i + " fallback2Lte:" + z);
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            int subscriptionIdForSlot = SubscriptionManagerEx.getDefault().getSubscriptionIdForSlot(i);
            Pair pair = new Pair(Integer.valueOf(subscriptionIdForSlot), 14);
            Pair pair2 = new Pair(Integer.valueOf(subscriptionIdForSlot), 20);
            if (this.mLast45GTime.containsKey(pair2) && jElapsedRealtime - ((Long) this.mLast45GTime.get(pair2)).longValue() < 172800000) {
                i2 = 11;
            } else {
                if (!this.mLast45GTime.containsKey(pair) || jElapsedRealtime - ((Long) this.mLast45GTime.get(pair)).longValue() >= 172800000) {
                    log("no reg history found for:" + i);
                    return;
                }
                i2 = 7;
            }
            int i3 = z ? 7 : i2;
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                String operatorNumeric = TextUtils.isEmpty(this.mRegOperatorNumeric[i]) ? phone.getOperatorNumeric() : this.mRegOperatorNumeric[i];
                if (!TextUtils.isEmpty(operatorNumeric)) {
                    FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{"AT+EOPS=7,2,\"" + operatorNumeric + "\"," + i3 + ",1,-118", ""}, (Message) null);
                    return;
                }
                log("phone numeric is empty");
                return;
            }
            logw("null phone on slot:" + i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enableRapidScanConfig(boolean z, int i) {
            String str;
            log("enableRapidScanConfig: " + z + " on phone:" + i);
            if (z) {
                this.mLastRSCFGSlot = i;
            } else if (this.mLastRSCFGSlot < 0) {
                log("not ERSCFG yet, skip disable");
                return;
            }
            if (z) {
                str = "AT+ERSCFG=20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20";
            } else {
                str = "AT+ERSCFG=20, 20, 60, 60, 60, 60, 90, 90, 90, 90, 90, 90, 180, 180, 180, 180, 180, 180, 360, 360, 360, 360, 360";
            }
            FiveGManagerBase.getInstance().sendAtCommand(this.mLastRSCFGSlot, new String[]{str, ""}, obtainMessage(106, this.mLastRSCFGSlot, z ? 1 : 0));
            if (z) {
                return;
            }
            this.mLastRSCFGSlot = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSimStateChanged(int i) {
            log("onSimStateChanged " + i);
            if (i == 1) {
                this.mRegOperatorNumeric = new String[]{"", ""};
            }
            transitionTo(this.mDefaultState);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateServiceStateInfo(int i, ServiceState serviceState) {
            this.mPhoneRegState[i] = serviceState.getDataRegState();
            log("updateServiceStateInfo:" + this.mPhoneRegState[0] + " " + this.mPhoneRegState[1]);
            int subscriptionIdForSlot = SubscriptionManagerEx.getDefault().getSubscriptionIdForSlot(i);
            if (!SubscriptionManager.isUsableSubIdValue(subscriptionIdForSlot) || this.mLast45GTime.size() > 20) {
                return;
            }
            if (serviceState.getRilDataRadioTechnology() == 14) {
                this.mLast45GTime.put(new Pair(Integer.valueOf(subscriptionIdForSlot), 14), Long.valueOf(SystemClock.elapsedRealtime()));
            }
            if (serviceState.getRilDataRadioTechnology() == 20) {
                this.mLast45GTime.put(new Pair(Integer.valueOf(subscriptionIdForSlot), 20), Long.valueOf(SystemClock.elapsedRealtime()));
            }
        }

        class DefaultState extends State {
            DefaultState() {
            }

            public void enter() {
                FastRecovery.this.log("enter DefaultState");
                FastRecovery.this.mPhoneRegState[0] = 3;
                FastRecovery.this.mPhoneRegState[1] = 3;
            }

            public boolean processMessage(Message message) {
                if (MtkNetworkOptimizer.this.VDBG) {
                    FastRecovery.this.log("DefaultState processMessage " + message.what);
                }
                int i = message.what;
                if (i == 100) {
                    ServiceState serviceState = (ServiceState) message.obj;
                    int i2 = message.arg1;
                    char c = i2 == 0 ? (char) 1 : (char) 0;
                    if (serviceState.getDataRegState() == 3) {
                        enter();
                        return true;
                    }
                    if (FastRecovery.this.mPhoneRegState[0] != 0 || FastRecovery.this.mPhoneRegState[1] != 0 || serviceState.getDataRegState() != 1) {
                        if (FastRecovery.this.hasMessages(101)) {
                            if (serviceState.getDataRegState() == 1 && FastRecovery.this.mPhoneRegState[c] == 1) {
                                FastRecovery.this.removeMessages(101);
                                if (MiuiPhoneUtils.getInsertedSimCount() > 1 && !FastRecovery.this.mIsAPMOffLingering) {
                                    FastRecovery fastRecovery = FastRecovery.this;
                                    fastRecovery.transitionTo(fastRecovery.mOosState);
                                }
                            }
                            if (serviceState.getDataRegState() == 0 && FastRecovery.this.mPhoneRegState[i2] == 1) {
                                FastRecovery.this.removeMessages(101);
                            }
                        }
                        FastRecovery.this.updateServiceStateInfo(i2, serviceState);
                    } else {
                        FastRecovery.this.removeMessages(101);
                        FastRecovery fastRecovery2 = FastRecovery.this;
                        fastRecovery2.sendMessageDelayed(101, TimeUnit.SECONDS.toMillis(fastRecovery2.T1));
                        FastRecovery.this.updateServiceStateInfo(i2, serviceState);
                        return true;
                    }
                } else if (i == 101) {
                    FastRecovery.this.log("2 subs OOS checking expired");
                    FastRecovery.this.removeMessages(101);
                }
                return true;
            }
        }

        class OosState extends State {
            OosState() {
            }

            private boolean isSignalAbovePoor(int i) {
                List cellSignalStrengths;
                CellSignalStrengthNr cellSignalStrengthNr;
                Phone phone = PhoneFactory.getPhone(i);
                boolean z = false;
                if (phone != null) {
                    ServiceState serviceState = phone.getServiceState();
                    SignalStrength signalStrength = phone.getSignalStrength();
                    if (serviceState != null && signalStrength != null && (serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19 ? signalStrength.getLteRsrp() > FastRecovery.this.RSRP_THRES : !(serviceState.getRilDataRadioTechnology() != 20 || (cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class)) == null || cellSignalStrengths.size() <= 0 || (cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrengths.get(0)) == null || cellSignalStrengthNr.getSsRsrp() <= FastRecovery.this.RSRP_THRES))) {
                        z = true;
                    }
                }
                FastRecovery.this.log("isSignalGood:" + z);
                return z;
            }

            public void enter() {
                FastRecovery.this.log("enter OosState");
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            public boolean processMessage(Message message) {
                ServiceState serviceState;
                AsyncResult asyncResult;
                if (MtkNetworkOptimizer.this.VDBG) {
                    FastRecovery.this.log("OosState processMessage " + message.what);
                }
                switch (message.what) {
                    case 100:
                        ServiceState serviceState2 = (ServiceState) message.obj;
                        int i = message.arg1;
                        int i2 = i == 0 ? 1 : 0;
                        int dataRegState = serviceState2.getDataRegState();
                        FastRecovery fastRecovery = FastRecovery.this;
                        if (dataRegState == 3) {
                            fastRecovery.transitionTo(fastRecovery.mDefaultState);
                            return true;
                        }
                        if (fastRecovery.mPhoneRegState[i] == 1 && serviceState2.getDataRegState() == 0) {
                            FastRecovery.this.removeMessages(102);
                            FastRecovery fastRecovery2 = FastRecovery.this;
                            long j = fastRecovery2.T2;
                            TimeUnit timeUnit = TimeUnit.SECONDS;
                            fastRecovery2.sendMessageDelayed(102, i2, timeUnit.toMillis(j));
                            FastRecovery.this.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                            FastRecovery fastRecovery3 = FastRecovery.this;
                            fastRecovery3.sendMessageDelayed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i2, timeUnit.toMillis(fastRecovery3.T3));
                            FastRecovery.this.updateServiceStateInfo(i, serviceState2);
                            return true;
                        }
                        FastRecovery.this.log("ss change:" + FastRecovery.this.mPhoneRegState[i2] + serviceState2.getDataRegState());
                        if (FastRecovery.this.mPhoneRegState[i2] == 0 && serviceState2.getDataRegState() == 0) {
                            FastRecovery.this.removeMessages(102);
                            FastRecovery.this.enableRapidScanConfig(false, message.arg1);
                            FastRecovery.this.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                            FastRecovery.this.updateServiceStateInfo(i, serviceState2);
                            FastRecovery fastRecovery4 = FastRecovery.this;
                            fastRecovery4.transitionTo(fastRecovery4.mDefaultState);
                        }
                        return true;
                    case 101:
                    default:
                        return true;
                    case 102:
                        FastRecovery.this.removeMessages(102);
                        int i3 = message.arg1;
                        int i4 = i3 == 0 ? 1 : 0;
                        if (FastRecovery.this.mPhoneRegState[i3] == 1 && isSignalAbovePoor(i4)) {
                            FastRecovery.this.enableRapidScanConfig(true, i3);
                        } else {
                            Phone phone = PhoneFactory.getPhone(i3);
                            if (phone != null && (serviceState = phone.getServiceState()) != null) {
                                int[] iArr = {16, 6, 3};
                                if (serviceState.getVoiceRegState() == 0 && ArrayUtils.contains(iArr, serviceState.getRilVoiceRadioTechnology())) {
                                    FastRecovery.this.forceScan(i3, false);
                                }
                            }
                        }
                        return true;
                    case 103:
                        int i5 = message.arg1;
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        if (asyncResult2 != null && asyncResult2.exception != null && FastRecovery.this.mPhoneRegState[i5] == 1) {
                            FastRecovery.this.forceScan(message.arg1, true);
                        }
                        return true;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        FastRecovery.this.log("EVENT_EOPS_LTE_RESP");
                        return true;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        FastRecovery.this.enableRapidScanConfig(false, message.arg1);
                        FastRecovery fastRecovery5 = FastRecovery.this;
                        fastRecovery5.transitionTo(fastRecovery5.mDefaultState);
                        return true;
                    case 106:
                        int i6 = message.arg1;
                        if (message.arg2 == 1 && FastRecovery.this.mPhoneRegState[i6] == 1) {
                            FastRecovery.this.forceScan(i6, false);
                        } else if (message.arg2 == 0 && (asyncResult = (AsyncResult) message.obj) != null && asyncResult.exception != null) {
                            FastRecovery.this.log("AT+ERSCFG error occurs");
                        }
                        return true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initFastRecovery() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{"AT+ERSCFG=20, 20, 60, 60, 60, 60, 90, 90, 90, 90, 90, 90, 180, 180, 180, 180, 180, 180, 360, 360, 360, 360, 360", ""}, (Message) null);
            }
        }
    }

    public static boolean isSubFeatureEnabled(int i) {
        return (i & mCurrentNwOpt) != 0;
    }

    protected void checkIfEFD(boolean z) {
        ServiceState serviceState;
        if (z) {
            if (!isSubFeatureEnabled(8)) {
                log("EFD not supported");
                return;
            }
            String[] strArr = {"AT+EFD=3,1", ""};
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                Phone phone = PhoneFactory.getPhone(i);
                if (phone != null && isSimApplicationReady(i) && (serviceState = phone.getServiceState()) != null && (serviceState.getVoiceRegState() != 0 || serviceState.getDataRegState() != 0)) {
                    FiveGManagerBase.getInstance().sendAtCommand(i, strArr, (Message) null);
                }
            }
        }
    }

    private boolean isSimApplicationReady(int i) {
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(i);
        return !PhoneFactory.getPhone(i).getIccCard().isEmptyProfile() && (activeSubscriptionInfoForSimSlotIndex != null && activeSubscriptionInfoForSimSlotIndex.areUiccApplicationsEnabled());
    }

    protected void notifyScreenToModem(boolean z) {
        if (!isSubFeatureEnabled(16)) {
            log("AFR not supported");
            return;
        }
        log("notifyScreenToModem:" + z);
        FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{z ? "AT+EDMFAPP=20100,500" : "AT+EDMFAPP=20100,501", "+EDMFAPP"}, (Message) null);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    private boolean isCtVoLTEOnlySupport(Phone phone) {
        boolean z;
        if (MiuiPhoneUtils.isCtVoLTEOnlySupport(phone)) {
            z = ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(phone.getContext(), this.mActiveDataSubInfo[1]);
        }
        log("isCtVoLTEOnlySupport " + z);
        return z;
    }

    class A2ThreshOpt {
        boolean mEnabled;

        A2ThreshOpt() {
        }

        private void enableA2ThreshSetOpt(boolean z) {
            String str;
            String str2;
            if (z == this.mEnabled) {
                if (MtkNetworkOptimizer.this.VDBG) {
                    MtkNetworkOptimizer.this.log("A2 thresh has no change " + z);
                    return;
                }
                return;
            }
            if (z) {
                str = "AT+ESBP=6,\"SBP_NR_CUSTOM_A2_THRESH_SET_LOWER\",1";
            } else {
                str = "AT+ESBP=6,\"SBP_NR_CUSTOM_A2_THRESH_SET_LOWER\",0";
            }
            if (z) {
                str2 = "AT+EGMC=1,\"set_nr_a2_lower_thresh\",-464,32768,0,-464,32768";
            } else {
                str2 = "AT+EGMC=1,\"set_nr_a2_lower_thresh\",32768,32768,32768,32768,32768";
            }
            if (FiveGManagerBase.getInstance().atCmdRunSync(MtkNetworkOptimizer.this.mActiveDataSubInfo[0], new String[]{str, "+ESBP"}, null)) {
                FiveGManagerBase.getInstance().atCmdRunSync(MtkNetworkOptimizer.this.mActiveDataSubInfo[0], new String[]{str2, "+EGMC"}, null);
                this.mEnabled = z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onConditionChanged(int i) {
            if (i == MtkNetworkOptimizer.this.mActiveDataSubInfo[0]) {
                if (!TextUtils.equals(MiuiPhoneUtils.getNetworkNumeric(i), "46045") || MtkNetworkOptimizer.this.mCurrentScene == 0) {
                    enableA2ThreshSetOpt(false);
                } else {
                    enableA2ThreshSetOpt(true);
                }
            }
        }
    }

    protected void regAppStateMonitor(boolean z) {
        MiuiAppStateMonitor miuiAppStateMonitor;
        log("regAppStateMonitor " + z);
        if (z && this.mMiuiAppStateMonitor == null) {
            MiuiAppStateMonitor miuiAppStateMonitorInit = MiuiAppStateMonitor.init();
            this.mMiuiAppStateMonitor = miuiAppStateMonitorInit;
            miuiAppStateMonitorInit.registerAppObserver(this.mCurrentTopAppObserver);
        } else {
            if (z || (miuiAppStateMonitor = this.mMiuiAppStateMonitor) == null) {
                return;
            }
            miuiAppStateMonitor.unregisterAppObserver(this.mCurrentTopAppObserver);
            this.mMiuiAppStateMonitor = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMobileNetworkChange(boolean z) {
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            log("processMobileNetworkChange isConnect = " + z + " , mCloudGlobalStreamMediaOn = " + mCloudGlobalStreamMediaOn);
            if (mCloudGlobalStreamMediaOn) {
                if (!z) {
                    this.mLastDataType = this.mDataType;
                    notifySceneChangeToModem(0);
                    resetVideoAndGameOptState();
                    resetBenefitStatisticList();
                    return;
                }
                processCurrentScene();
            }
        }
    }

    public void processCurrentScene() {
        String topAppName = MiuiPhoneUtils.getTopAppName();
        int i = this.mDataType;
        if (topAppName == null) {
            return;
        }
        if (MiuiPhoneUtils.isContainsPackage("config_global_data_opt_video_whitelist_pkgs", topAppName)) {
            this.mDataType = 4;
        } else if (MiuiPhoneUtils.isContainsPackage("config_global_data_opt_game_whitelist_pkgs", topAppName)) {
            this.mDataType = 2;
        } else {
            this.mDataType = 0;
        }
        log("processCurrentScene tempDataType = " + i + " , mDataType = " + this.mDataType + " , mLastToModemDataType = " + this.mLastToModemDataType);
        if (this.mDataType != this.mLastToModemDataType) {
            processStreamBenefitStatistic();
            processDataAppTypeChange(i, this.mDataType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataAppTypeChange(int i, int i2) {
        log("processDataAppTypeChange tempType:" + i + " , dataType:" + i2 + " , mDataType = " + this.mDataType + " , mLastDataType = " + this.mLastDataType);
        if (this.mWorkHandler.hasMessages(112)) {
            if (i2 == 0) {
                log("ignore type change for has msg default");
                return;
            }
            if (i == 0 && i2 == 4) {
                this.mWorkHandler.removeMessages(112);
                if (this.isBeginGameOptimization) {
                    this.mLastDataType = 2;
                    exitGameOptimization();
                    log("remove msg for game is exiting while entering the video.");
                } else {
                    log("remove msg for video exit and enter again in less than 1min.");
                    return;
                }
            } else if (i == 0 && i2 == 2) {
                this.mWorkHandler.removeMessages(112);
                if (this.isBeginVideoOptimization) {
                    this.mLastDataType = 4;
                    exitVideoOptimization();
                    notifySceneChangeToModem(0);
                    log("remove msg for video is exiting while entering the game. mDataType:" + this.mDataType);
                } else {
                    log("remove msg for game exit and enter again in less than 1min.");
                    return;
                }
            }
        }
        if ((i == 4 || i == 1) && i2 == 2) {
            this.mLastDataType = i;
            notifySceneChangeToModem(i2);
            log("processDataAppTypeChange mDataType:" + this.mDataType);
            exitVideoOptimization();
        } else if (i == 2 && (i2 == 4 || i2 == 1)) {
            this.mLastDataType = i;
            notifySceneChangeToModem(i2);
            exitGameOptimization();
        }
        int i3 = i2 == 0 ? 60000 : 0;
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(112, Integer.valueOf(i2)), i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processModemStallState() {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
            log("return  processModemStallState for not support.");
            return;
        }
        try {
            log("processModemStallState");
            Object[] objArr = new Object[1];
            FiveGManagerBase.getInstance().sendAtCmdSync(MiuiPhoneUtils.getActiveDataPhoneId(), new String[]{"AT+EDMFAPP=20006,501,1,1", "+EDMFAPP"}, objArr);
            Object obj = objArr[0];
            if (obj == null) {
                log("queryModemStallState atResult is null.");
                return;
            }
            String[] strArrSplit = ((String[]) obj)[0].split(",");
            log("processModemStallState lineData = " + Arrays.toString(strArrSplit) + " , lineData.length = " + strArrSplit.length);
            if (strArrSplit.length < 3) {
                return;
            }
            int i = Integer.parseInt(strArrSplit[2]);
            log("processModemStallState stallState = " + i + " , mIsNeedDoPsAttachAction:" + this.mIsNeedDoPsAttachAction + " , mIsNeedDoDataReconnectAction:" + this.mIsNeedDoDataReconnectAction);
            if (i == 1) {
                if (this.mIsNeedDoPsAttachAction) {
                    doPsAttach();
                } else if (this.mIsNeedDoDataReconnectAction) {
                    doDataReConnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySceneChangeToModem(int i) {
        log("notifySceneChangeToModem type = " + i + " , mLastToModemDataType = " + this.mLastToModemDataType);
        if (i != this.mLastToModemDataType) {
            sendSelfStudyRequest(15, 1, new int[]{i});
            this.mLastToModemDataType = i;
            if (i != 0) {
                if (i == 4) {
                    enableModemDataPerceptionModel(true);
                    return;
                }
                return;
            }
            if (this.mWorkHandler.hasMessages(112)) {
                this.mWorkHandler.removeMessages(112);
            }
            if (this.mWorkHandler.hasMessages(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX)) {
                this.mWorkHandler.removeMessages(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX);
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(121, 0));
            enableModemDataPerceptionModel(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processStreamBenefitStatistic() {
        int i = this.mDataType;
        if (i == 4 || i == 1) {
            if (this.mWorkHandler.hasMessages(117)) {
                return;
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(117, MdOptimizationScene.ShortVideo.value, 0));
            return;
        }
        resetBenefitStatisticList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVideoAndGameAiStats(String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null) {
            log("return updateVideoAndGameAiStats for mStats is null.");
            return;
        }
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(concurrentHashMap);
        ArrayList arrayList = (ArrayList) this.mVideoStatsList.get(str);
        logV("return updateVideoAndGameAiStats eventName = " + str + " , mStats = " + concurrentHashMap + " , mVideoStatsList = " + this.mVideoStatsList + " , mDataType = " + this.mDataType + " , stats = " + concurrentHashMap2 + " , list = " + arrayList);
        if (arrayList == null) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(concurrentHashMap2);
            this.mVideoStatsList.put(str, arrayList2);
        } else {
            arrayList.add(concurrentHashMap2);
        }
        concurrentHashMap.clear();
    }

    @Override // com.android.phone.NetworkOptimizerBase
    public void reportGlobalVideoBenefitStatistic() {
        logV("reportGlobalVideoBenefitStatistic mVideoStatsList = " + this.mVideoStatsList);
        ConcurrentHashMap concurrentHashMap = this.mVideoStatsList;
        if (concurrentHashMap != null && !concurrentHashMap.isEmpty()) {
            for (String str : this.mVideoStatsList.keySet()) {
                HashMap map = new HashMap();
                map.put("Params", this.mVideoStatsList.get(str));
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, map);
            }
        }
        this.mVideoStatsList.clear();
    }

    private void recordGlobalDetail(int i, int i2, int i3, ArrayList arrayList) {
        if (arrayList != null && arrayList.size() >= 10) {
            arrayList.remove(0);
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("mode", Integer.valueOf(i));
        concurrentHashMap.put("cause", Integer.valueOf(i2));
        concurrentHashMap.put("isValid", Integer.valueOf(i3));
        if (arrayList != null) {
            arrayList.add(concurrentHashMap);
        }
    }

    @Override // com.android.phone.NetworkOptimizerBase
    public void reportGlobalSafDetailStatistic() {
        logV("reportGlobalSafDetailStatistic mGlobalSafDetailList = " + this.mGlobalSafDetailList);
        ArrayList arrayList = new ArrayList(this.mGlobalSafDetailList);
        if (!arrayList.isEmpty()) {
            HashMap map = new HashMap();
            map.put("Params", arrayList);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_global_saf_detail_statistic", map);
        }
        this.mGlobalSafDetailList.clear();
    }

    public void recordCount(String str, String str2) {
        ConcurrentHashMap concurrentHashMap;
        if (!"video".equalsIgnoreCase(str2) || (concurrentHashMap = this.mGlobalStreamStats) == null) {
            return;
        }
        int iIntValue = concurrentHashMap.get(str) != null ? ((Integer) this.mGlobalStreamStats.get(str)).intValue() : 0;
        logV("recordCount key:" + str + ",oldCount:" + iIntValue);
        this.mGlobalStreamStats.put(str, Integer.valueOf(iIntValue + 1));
        try {
            PreferenceUtils.getInstance().setStringPreference("globalStreamMediaStatisticStats", ObjectSerializer.serialize(this.mGlobalStreamStats));
        } catch (Exception e) {
            log("record global video counts stats exception:" + e);
        }
    }

    private void reloadGlobalVideoStatisticFromSp() {
        try {
            String stringPreference = PreferenceUtils.getInstance().getStringPreference("globalStreamMediaStatisticStats");
            if (stringPreference.isEmpty()) {
                return;
            }
            this.mGlobalStreamStats = (ConcurrentHashMap) ObjectSerializer.deserialize(stringPreference);
        } catch (Exception e) {
            log("reload global video statistic stats exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChangeForBenefitStatistic(CellLocation cellLocation, int i) {
        if (isValidCellLocation(cellLocation, i) && isValidCellIdentity(getCellIdentity(i))) {
            handleVideoBenefitLogic();
        }
    }

    private boolean isValidCellLocation(CellLocation cellLocation, int i) {
        return (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty()) ? false : true;
    }

    private CellIdentity getCellIdentity(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            return phone.getCurrentCellIdentity();
        }
        return null;
    }

    private boolean isValidCellIdentity(CellIdentity cellIdentity) {
        return (cellIdentity == null || cellIdentity.getGlobalCellId() == null) ? false : true;
    }

    private void handleVideoBenefitLogic() {
        logV("cellLocationChange mDataType:" + this.mDataType + " ,isVideoOpt:" + this.isVideoOpt);
        if (shouldTriggerVideoBenefitStop()) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            MdOptimizationScene mdOptimizationScene = MdOptimizationScene.ShortVideo;
            getTotalDuration(concurrentHashMap, mdOptimizationScene.value, "Before");
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(118, mdOptimizationScene.value, 0, concurrentHashMap), 10000L);
            this.isVideoOpt = false;
        }
    }

    private boolean shouldTriggerVideoBenefitStop() {
        int i = this.mDataType;
        return (i == 1 || i == 4) && this.isVideoOpt;
    }

    public ConcurrentHashMap getTotalDuration(ConcurrentHashMap concurrentHashMap, int i, String str) {
        log("getTotalDuration sceneType = " + i + " , suffix = " + str + " , map = " + concurrentHashMap);
        if (concurrentHashMap == null) {
            log("return getTotalDuration for map is null.");
            return null;
        }
        log("getTotalDuration map.isEmpty = " + concurrentHashMap.isEmpty());
        if (i == MdOptimizationScene.ShortVideo.value) {
            int i2 = 0;
            for (int i3 = 0; i3 < 10; i3++) {
                if (this.durationStatistic[i3] != 0) {
                    i2++;
                }
                concurrentHashMap.put("abnormal" + str, Integer.valueOf(i2));
                concurrentHashMap.put("normal" + str, Integer.valueOf(10 - i2));
            }
        }
        return concurrentHashMap;
    }

    public void resetBenefitStatisticList() {
        for (int i = 0; i < 10; i++) {
            this.durationStatistic[i] = 0;
        }
        this.mWorkHandler.removeMessages(117);
        this.mWorkHandler.removeMessages(118);
        this.isVideoOpt = false;
    }

    private void enableModemDataPerceptionModel(boolean z) {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
            StringBuilder sb = new StringBuilder("return ");
            sb.append(z ? "enable" : "disable");
            sb.append(" ModemPerceptionModel for not support.");
            log(sb.toString());
            return;
        }
        log("enableModemDataPerceptionModel enable is " + z + " ,mIsEnableModemPerceptionModel is " + this.mIsEnableModemPerceptionModel);
        boolean z2 = this.mIsEnableModemPerceptionModel;
        if (!z2 && z) {
            sendSelfStudyRequest(ResponseObject.RELAY_FAILED_CODE, 1, new int[]{1});
            this.mIsEnableModemPerceptionModel = true;
        } else {
            if (!z2 || z) {
                return;
            }
            sendSelfStudyRequest(ResponseObject.RELAY_FAILED_CODE, 1, new int[]{0});
            this.mIsEnableModemPerceptionModel = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void beginVideoOptimization() {
        log("beginVideoOptimization isBeginVideoOptimization = " + this.isBeginVideoOptimization);
        if (this.isBeginVideoOptimization || !shouldVideoAndGameOptimizationForMCC()) {
            return;
        }
        this.isBeginVideoOptimization = true;
        log("beginVideoOptimization start");
        enableSafOpt(4, true);
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() && mCloudGlobalStreamMediaOnV2) {
            if (isColdStart(MiuiPhoneUtils.getTopAppName())) {
                Handler handler = this.mWorkHandler;
                handler.sendMessage(handler.obtainMessage(120, 4, 1));
            }
            enableLargeBWOpt(true);
            enableLLMOpt(true);
        }
        showToast("beginVideoOpt");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitVideoOptimization() {
        log("exitVideoOptimization isBeginVideoOptimization = " + this.isBeginVideoOptimization + " , mDataType = " + this.mDataType);
        if (this.isBeginVideoOptimization) {
            this.isBeginVideoOptimization = false;
            enableSafOpt(this.mIsEnableLiveSafOpt ? 1 : 4, false);
            enableLargeBWOpt(false);
            enableLLMOpt(false);
            showToast("exitVideoOpt");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void beginGameOptimization() {
        log("beginGameOptimization isBeginGameOptimization = " + this.isBeginGameOptimization);
        if (this.isBeginGameOptimization || !shouldVideoAndGameOptimizationForMCC()) {
            return;
        }
        this.isBeginGameOptimization = true;
        enableModemGameOpt(true);
        enableSbpOpt(true);
        enableSafOpt(2, true);
        enableLLMOpt(true);
        doPowerHal(1, this.mCurrentAppUid);
        showToast("beginGameOpt");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitGameOptimization() {
        if (this.isBeginGameOptimization) {
            log("exitGameOptimization");
            this.isBeginGameOptimization = false;
            this.mIsEnableMdGameOpt = false;
            enableSbpOpt(false);
            enableSafOpt(2, false);
            enableLLMOpt(false);
            doPowerHal(2, this.mLastPrioUid);
            showToast("exitGameOpt");
        }
    }

    private void enableModemGameOpt(boolean z) {
        int i;
        Phone phone;
        log("enableModemGameOpt enable = " + z + " , mIsEnableMdGameOpt = " + this.mIsEnableMdGameOpt + " , mMdGameModeOptOngoing = " + this.mMdGameModeOptOngoing);
        if (z == this.mIsEnableMdGameOpt || this.mMdGameModeOptOngoing) {
            return;
        }
        int[] activeDataPhoneInfo = getActiveDataPhoneInfo();
        log("enableModemGameOpt slots = " + Arrays.toString(activeDataPhoneInfo));
        if (activeDataPhoneInfo.length <= 1 || activeDataPhoneInfo[0] == -1 || (i = activeDataPhoneInfo[1]) == -1 || (phone = MiuiPhoneUtils.getPhone(i)) == null) {
            return;
        }
        this.mIsEnableMdGameOpt = z;
        FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_GAME_MODE_OPT", z ? "1" : "0"}, (Message) null);
    }

    private void enableSbpOpt(boolean z) {
        log("enableSbpOpt enable = " + z + " , mIsEnableSbpOpt = " + this.mIsEnableSbpOpt);
        if (z == this.mIsEnableSbpOpt) {
            return;
        }
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if (z) {
            sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_NR_SKIP_RPT, (Message) null);
            sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_NR_ERRC_THR, (Message) null);
            sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE, (Message) null);
            this.mIsEnableSbpOpt = true;
            return;
        }
        sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_NR_SKIP_RPT, (Message) null);
        sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE, (Message) null);
        this.mIsEnableSbpOpt = false;
    }

    private void enableSafOpt(int i, boolean z) {
        int[] iArr;
        log("enableSafOpt type = " + i + " , enable = " + z + " , mLastDataType = " + this.mLastDataType + " , mIsEnableVideoSafOpt = " + this.mIsEnableVideoSafOpt + " , mIsEnableLiveSafOpt = " + this.mIsEnableLiveSafOpt + " , mIsEnableGameSafOpt = " + this.mIsEnableGameSafOpt);
        if (i == 2 && z == this.mIsEnableGameSafOpt) {
            return;
        }
        if (i == 4 && z == this.mIsEnableVideoSafOpt) {
            return;
        }
        if (i == 1 && z == this.mIsEnableLiveSafOpt) {
            return;
        }
        if (z) {
            if (!this.mIsEnableVideoSafOpt && !this.mIsEnableLiveSafOpt && !this.mIsEnableGameSafOpt) {
                clearSafData();
            }
            String[] dataStallCmd = getDataStallCmd(i);
            Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
            if (dataStallCmd == null || phone == null) {
                return;
            }
            int length = dataStallCmd.length;
            int i2 = 0;
            while (true) {
                iArr = null;
                if (i2 >= length) {
                    break;
                }
                String str = dataStallCmd[i2];
                if (!TextUtils.isEmpty(str)) {
                    String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                    log("enableSafOpt start result is sb ".concat(str2));
                    FiveGManagerBase.getInstance().sendAtCommand(phone, str2.split(","), (Message) null);
                }
                i2++;
            }
            if (i == 4) {
                iArr = new int[]{0, 4, 2, 3};
                this.mIsEnableVideoSafOpt = true;
                this.mIsEnableLiveSafOpt = false;
            } else if (i == 2) {
                iArr = new int[]{1, 0, 4, 2, 3};
                this.mIsEnableGameSafOpt = true;
            } else if (i == 1) {
                iArr = new int[]{0, 4, 2, 3};
                this.mIsEnableLiveSafOpt = true;
                this.mIsEnableVideoSafOpt = false;
            }
            int[] iArr2 = iArr;
            if (iArr != null && iArr2 != null) {
                IntStream intStreamStream = Arrays.stream(iArr);
                final HashSet hashSet = this.lastLteModes;
                Objects.requireNonNull(hashSet);
                intStreamStream.forEach(new IntConsumer() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda4
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i3) {
                        hashSet.add(Integer.valueOf(i3));
                    }
                });
                IntStream intStreamStream2 = Arrays.stream(iArr2);
                final HashSet hashSet2 = this.lastNrModes;
                Objects.requireNonNull(hashSet2);
                intStreamStream2.forEach(new IntConsumer() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda4
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i3) {
                        hashSet2.add(Integer.valueOf(i3));
                    }
                });
            }
            stopDataStallOnNddsPhoneId(phone.getPhoneId() != 0 ? 0 : 1);
            return;
        }
        HashSet hashSet3 = this.lastLteModes;
        if (hashSet3 != null && !hashSet3.isEmpty()) {
            Iterator it = this.lastLteModes.iterator();
            while (it.hasNext()) {
                stopDataStall(((Integer) it.next()).intValue(), 0);
            }
            this.lastLteModes.clear();
        }
        HashSet hashSet4 = this.lastNrModes;
        if (hashSet4 != null && !hashSet4.isEmpty()) {
            Iterator it2 = this.lastNrModes.iterator();
            while (it2.hasNext()) {
                stopDataStall(((Integer) it2.next()).intValue(), 1);
            }
            this.lastNrModes.clear();
        }
        if (i == 4) {
            this.mIsEnableVideoSafOpt = false;
            this.mIsEnableLiveSafOpt = false;
        } else if (i == 2) {
            this.mIsEnableGameSafOpt = false;
        }
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization() && this.mLastDataType != 0) {
            getModemSafData(MiuiPhoneUtils.getActiveDataPhoneId(), this.mGlobalSafDataList);
        }
        stopDataStallOnNddsPhoneId(MiuiPhoneUtils.getActiveDataPhoneId() != 0 ? 0 : 1);
    }

    public void stopDataStall(int i, int i2) {
        log("stopDataStall mode = " + i + " , rat = " + i2);
        String str = "MIPC_SET_DATA_STALL_OPT," + String.valueOf(i) + ",0,0," + String.valueOf(i2) + ",0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        log("stop result is sb ".concat(str));
        String[] strArrSplit = str.split(",");
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        if (phone != null) {
            FiveGManagerBase.getInstance().sendAtCommand(phone, strArrSplit, (Message) null);
        }
    }

    public void beginStreamDataStall(int i, int i2) {
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization() && mCloudGlobalStreamMediaOn) {
            if (i == 4 || i == 1) {
                String[] strArr = {"1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
                if (i2 == 0) {
                    if (this.hasSendDlDataStallAction || this.hasSendColdDlDataStallAction) {
                        return;
                    }
                } else if (i2 != 1 || this.hasSendColdDlDataStallAction) {
                    return;
                } else {
                    strArr = new String[]{"1,3,0,0,3000,8,3,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,3,0,1,3000,8,3,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
                }
                recordStreamModes(i2);
                executeCommand(strArr, i2);
            }
        }
    }

    public void stopStreamDataStall(int i) {
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            log("stopStreamDataStall hasSendDlDataStallAction = " + this.hasSendDlDataStallAction);
            log("stopStreamDataStall reason = " + i);
            resetHasSendDlDataStallActionForReason(i);
            handleStopCommandsForReason(i);
        }
    }

    private void resetHasSendDlDataStallActionForReason(int i) {
        log("resetHasSendDlDataStallActionForReason reason=" + i + " ,hasSendDlDataStallAction = " + this.hasSendDlDataStallAction + " ,hasSendColdDlDataStallAction = " + this.hasSendColdDlDataStallAction);
        if (i == 0) {
            if (this.hasSendDlDataStallAction) {
                this.hasSendDlDataStallAction = false;
            }
        } else if (1 == i && this.hasSendColdDlDataStallAction) {
            this.hasSendColdDlDataStallAction = false;
        }
    }

    private void handleStopCommandsForReason(int i) {
        if (shouldSendExitCommands(i)) {
            log("handleStopCommandsForReason sendExitCommands reason=" + i);
            sendExitCommands();
            return;
        }
        log("handleStopCommandsForReason sendGeneratedStopCommands reason=" + i);
        sendGeneratedStopCommands(i);
    }

    private boolean shouldSendExitCommands(int i) {
        ArrayList arrayList;
        ArrayList arrayList2;
        if (i == 0) {
            ArrayList arrayList3 = this.lastLteStreamModes;
            return (arrayList3 == null || arrayList3.isEmpty()) && ((arrayList2 = this.lastNrStreamModes) == null || arrayList2.isEmpty());
        }
        if (1 != i) {
            return false;
        }
        ArrayList arrayList4 = this.lastColdLteStreamModes;
        return (arrayList4 == null || arrayList4.isEmpty()) && ((arrayList = this.lastColdNrStreamModes) == null || arrayList.isEmpty());
    }

    private void sendGeneratedStopCommands(int i) {
        List listGenerateCommands = generateCommands(i);
        if (listGenerateCommands == null || listGenerateCommands.isEmpty()) {
            return;
        }
        Iterator it = listGenerateCommands.iterator();
        while (it.hasNext()) {
            sendCommand((String) it.next());
        }
    }

    private void sendExitCommands() {
        String[] strArr = {"1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        for (int i = 0; i < 2; i++) {
            String str = strArr[i];
            if (!TextUtils.isEmpty(str)) {
                sendCommand("MIPC_SET_DATA_STALL_OPT," + str);
            }
        }
    }

    private void sendCommand(String str) {
        log("sendCommand=" + str);
        if (str == null || TextUtils.isEmpty(str) || MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId()) == null) {
            return;
        }
        log("sendCommand stop result is sb ".concat(str));
        FiveGManagerBase.getInstance().sendAtCommand(MiuiPhoneUtils.getActiveDataPhoneId(), str.split(","), (Message) null);
    }

    private List generateCommands(int i) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3 = new ArrayList();
        if (i != 0) {
            if (1 == i) {
                arrayList = this.lastColdLteStreamModes;
                arrayList2 = this.lastColdNrStreamModes;
                arrayList.clear();
                this.lastColdNrStreamModes.clear();
            }
            return arrayList3;
        }
        arrayList = this.lastLteStreamModes;
        arrayList2 = this.lastNrStreamModes;
        arrayList.clear();
        this.lastNrStreamModes.clear();
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            for (int i3 : (int[]) obj) {
                arrayList3.add("MIPC_SET_DATA_STALL_OPT," + i3 + ",0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF");
            }
        }
        int size2 = arrayList2.size();
        int i4 = 0;
        while (i4 < size2) {
            Object obj2 = arrayList2.get(i4);
            i4++;
            for (int i5 : (int[]) obj2) {
                arrayList3.add("MIPC_SET_DATA_STALL_OPT," + i5 + ",0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF");
            }
        }
        return arrayList3;
    }

    private void recordStreamModes(int i) {
        log("recordStreamModes reason:" + i);
        int[] iArr = {1};
        if (i == 1) {
            addModesToSets(this.lastColdLteStreamModes, this.lastColdNrStreamModes, iArr);
        } else {
            addModesToSets(this.lastLteStreamModes, this.lastNrStreamModes, iArr);
        }
    }

    private void addModesToSets(ArrayList arrayList, ArrayList arrayList2, int[] iArr) {
        if (arrayList == null || arrayList2 == null || iArr == null) {
            return;
        }
        int[] iArr2 = (int[]) iArr.clone();
        arrayList.add(iArr2);
        arrayList2.add((int[]) iArr2.clone());
    }

    private void executeCommand(String[] strArr, int i) {
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        log("executeCommand reason =" + i);
        if (phone != null) {
            if (i == 0) {
                this.hasSendDlDataStallAction = true;
                this.isVideoOpt = true;
            } else if (i == 1) {
                this.hasSendColdDlDataStallAction = true;
                if (this.mAutoStopColdDataStallTask == null) {
                    Runnable runnable = new Runnable() { // from class: com.android.phone.MtkNetworkOptimizer$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$executeCommand$3();
                        }
                    };
                    this.mAutoStopColdDataStallTask = runnable;
                    this.mWorkHandler.postDelayed(runnable, 3000L);
                }
            }
            for (String str : strArr) {
                if (!TextUtils.isEmpty(str)) {
                    String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                    log("executeCommand:" + str2 + " ,reason = " + i);
                    FiveGManagerBase.getInstance().sendAtCommand(phone, str2.split(","), (Message) null);
                }
            }
            stopDataStallOnNddsPhoneId(phone.getPhoneId() != 0 ? 0 : 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeCommand$3() {
        log("force stop DataStall for ColdStart.");
        Handler handler = this.mWorkHandler;
        handler.sendMessage(handler.obtainMessage(121, 1));
        this.mAutoStopColdDataStallTask = null;
    }

    private void enableLargeBWOpt(boolean z) {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
            StringBuilder sb = new StringBuilder("return ");
            sb.append(z ? "enable" : "disable");
            sb.append(" LBW for not support.");
            log(sb.toString());
            return;
        }
        if (this.mIsEnableLargeBW != z) {
            for (String str : z ? new String[]{"1,0,100,-105,0x8000,0", "2,0,100,-105,0x8000,0"} : new String[]{"0,0,0xFFFF,0x8000,0x8000,0x80"}) {
                String[] strArrSplit = ("MIPC_SET_PREFER_LARGE_BW_REQ," + str).split(",");
                log("enableLargeBWOpt executeCmd :" + Arrays.toString(strArrSplit));
                Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
                if (phone != null) {
                    this.mIsEnableLargeBW = z;
                    FiveGManagerBase.getInstance().sendAtCommand(phone, strArrSplit, (Message) null);
                }
            }
        }
    }

    private void enableLLMOpt(boolean z) {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
            StringBuilder sb = new StringBuilder("return ");
            sb.append(z ? "enable" : "disable");
            sb.append(" LLM for not support.");
            log(sb.toString());
            return;
        }
        if (this.mIsEnableLLM != z) {
            String[] strArrSplit = "MIPC_SET_LOW_LATENCY_MODE_REQ,".concat(z ? "1" : "0").split(",");
            log("enableLLMOpt executeCmd :" + Arrays.toString(strArrSplit));
            Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
            if (phone != null) {
                this.mIsEnableLLM = z;
                FiveGManagerBase.getInstance().sendAtCommand(phone, strArrSplit, (Message) null);
            }
        }
    }

    private void doPsAttach() {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = NetworkRecoveryStrategyBase.getInstance();
        if (networkRecoveryStrategyBase != null) {
            logV("doPsAttach isVideoOpt:" + this.isVideoOpt + ",mIsNeedDoPsAttachAction:" + this.mIsNeedDoPsAttachAction + " ,mIsNeedDoDataReconnectAction:" + this.mIsNeedDoDataReconnectAction);
            networkRecoveryStrategyBase.reAttach(MiuiPhoneUtils.getActiveDataPhoneId());
            this.isVideoOpt = true;
            this.mIsNeedDoPsAttachAction = false;
            this.mIsNeedDoDataReconnectAction = false;
            recordCount("action2Cnt", "video");
        }
    }

    private void doDataReConnect() {
        logV("doDataReConnect isVideoOpt:" + this.isVideoOpt + ",mIsNeedDoPsAttachAction:" + this.mIsNeedDoPsAttachAction + " ,mIsNeedDoDataReconnectAction:" + this.mIsNeedDoDataReconnectAction);
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        if (phone != null) {
            try {
                MiuiDataControllerImpl miuiDataControllerImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
                if (miuiDataControllerImpl != null) {
                    DataNetwork dataNetworkFindActiveInternetDataNetwork = miuiDataControllerImpl.findActiveInternetDataNetwork();
                    logV("doDataReConnect dataNetwork = " + dataNetworkFindActiveInternetDataNetwork);
                    if (dataNetworkFindActiveInternetDataNetwork != null && phone.getServiceState() != null && dataNetworkFindActiveInternetDataNetwork.isInternetSupported()) {
                        log("doDataReConnect tearDown");
                        dataNetworkFindActiveInternetDataNetwork.tearDown(12);
                        recordCount("action1Cnt", "video");
                    }
                }
                this.mIsNeedDoPsAttachAction = true;
                this.mIsNeedDoDataReconnectAction = false;
                this.isVideoOpt = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doPowerHal(int i, int i2) {
        if (!MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2() || !mCloudGlobalStreamMediaOnV2) {
            log("return doPowerHal UID: " + i2 + "action:" + i + " for not support.");
            return;
        }
        if (!PowerHalReflector.isAvailable()) {
            log("Reflection method is not available.");
            return;
        }
        PowerHalReflector powerHalReflector = new PowerHalReflector();
        if (i != 1) {
            if (i == 2) {
                if (isValidApp(i2)) {
                    powerHalReflector.setPriorityByUid(2, i2);
                    logV("Reflect delete uid:" + i2);
                }
                this.mLastPrioUid = -1;
                return;
            }
            log("doPowerHal invalid call");
            return;
        }
        if (this.mLastPrioUid == i2 || !isValidApp(i2)) {
            return;
        }
        powerHalReflector.flushPriorityRules(1);
        if (powerHalReflector.setPriorityByUid(1, i2)) {
            logV("Reflect success. mLastPrioUid: " + this.mLastPrioUid + ",uid:" + i2);
            this.mLastPrioUid = i2;
        }
    }

    private boolean isValidApp(int i) {
        return UserHandle.isApp(i) && UserHandle.getAppId(i) != this.mHomeUid;
    }

    public class PowerHalReflector {
        private Method mFlushRulesMethod;
        private Object mPowerHalInstance;
        private Method mSetPriorityMethod;

        public PowerHalReflector() {
            try {
                Class<?> cls = Class.forName("com.mediatek.powerhalmgr.PowerHalMgrImpl");
                Class[] clsArr = new Class[0];
                this.mPowerHalInstance = cls.getMethod("getInstance", null).invoke(null, null);
                Class<?> cls2 = Integer.TYPE;
                this.mSetPriorityMethod = cls.getMethod("setPriorityByUid", cls2, cls2);
                this.mFlushRulesMethod = cls.getMethod("flushPriorityRules", cls2);
            } catch (Exception e) {
                MtkNetworkOptimizer.this.log("PowerHalReflector fail:" + e);
            }
        }

        public boolean setPriorityByUid(int i, int i2) {
            Method method;
            Object obj = this.mPowerHalInstance;
            if (obj == null || (method = this.mSetPriorityMethod) == null) {
                MtkNetworkOptimizer.this.log("setPriorityByUid PowerHal not init.");
                return false;
            }
            try {
                return ((Boolean) method.invoke(obj, Integer.valueOf(i), Integer.valueOf(i2))).booleanValue();
            } catch (Exception e) {
                MtkNetworkOptimizer.this.log("setPriorityByUid fail:" + e);
                return false;
            }
        }

        public boolean flushPriorityRules(int i) {
            Method method;
            Object obj = this.mPowerHalInstance;
            if (obj == null || (method = this.mFlushRulesMethod) == null) {
                MtkNetworkOptimizer.this.log("flushPriorityRules PowerHal not init.");
                return false;
            }
            try {
                return ((Boolean) method.invoke(obj, Integer.valueOf(i))).booleanValue();
            } catch (Exception e) {
                MtkNetworkOptimizer.this.log("flushPriorityRules fail:" + e);
                return false;
            }
        }

        public static boolean isAvailable() {
            try {
                Class.forName("com.mediatek.powerhalmgr.PowerHalMgrImpl");
                return true;
            } catch (ClassNotFoundException unused) {
                return false;
            }
        }
    }

    public void stopDataStallOnNddsPhoneId(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        String[] dataStallCmd = getDataStallCmd(0);
        if (phone == null || dataStallCmd == null) {
            return;
        }
        for (String str : dataStallCmd) {
            String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
            log("ndds phone stop result is sb ".concat(str2));
            FiveGManagerBase.getInstance().sendAtCommand(phone, str2.split(","), (Message) null);
        }
    }

    private String[] getDataStallCmd(int i) {
        log("getDataStallCmd type is:" + i);
        if (i == 0) {
            return new String[]{"0,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "0,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        }
        if (i == 1) {
            return new String[]{"0,1,0,0,5000,300,300,10000,0xFF,0xFF,0x7F,-105,0,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,60,60,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,0,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "0,1,0,1,5000,300,300,10000,0xFF,0xFF,0x7F,-105,0,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,60,60,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,1,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        }
        if (i == 2) {
            return new String[]{"0,1,0,0,5000,300,300,800,0xFF,90,0x7F,-110,0,-110,0,0xFF,0xFF,64,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,2000", "1,0,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-118,-5,0xFF,0xFF,150,6,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,50,50,0xFFFFFFFF,0xFFFFFFFF,40,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,0,20000,300,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "0,1,0,1,5000,300,300,800,0xFF,90,0x7F,-110,0,-110,0,0xFF,0xFF,64,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,2000", "1,0,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-118,-5,0xFF,0xFF,150,6,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,50,50,0xFFFFFFFF,0xFFFFFFFF,40,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,1,20000,300,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        }
        if (i != 4) {
            return null;
        }
        return new String[]{"0,1,0,0,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,0,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "0,1,0,1,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,4,0,1,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    }

    private void sendSelfStudyRequest(int i, int i2, int[] iArr) {
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        if (phone == null) {
            return;
        }
        if (i == 15) {
            sendAtCmd(constructionAtCmd(String.valueOf(15), 7, i2, iArr[0]), (Message) null, phone.getPhoneId());
        } else {
            if (i != 500) {
                return;
            }
            sendAtCmd(constructAtCmd(String.valueOf(ResponseObject.RELAY_FAILED_CODE), 7, i2, iArr), (Message) null, phone.getPhoneId());
        }
    }

    private String[] constructAtCmd(String str, int i, int i2, int[] iArr) {
        StringBuilder sb = new StringBuilder("AT+EDMFAPP=");
        if (i == 7) {
            sb.append(20006);
        }
        sb.append(",");
        sb.append(str);
        if (i2 > 0) {
            sb.append(",");
            sb.append(i2);
            for (int i3 : iArr) {
                sb.append(",");
                sb.append(i3);
            }
        }
        String[] strArr = {sb.toString(), "+EDMFAPP"};
        log("constructAtCmd atCmd is" + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    public String[] constructionAtCmd(String str, int i, int i2, int i3) {
        String[] strArr = {"", ""};
        if (i == 7) {
            strArr[0] = "AT+EDMFAPP=20006," + str + "," + i2 + "," + i3;
            strArr[1] = "+EDMFAPP";
        }
        log("constructionAtCmd atCmd is " + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    public boolean isColdStart(String str) {
        MiuiAppStateMonitor miuiAppStateMonitor = this.mMiuiAppStateMonitor;
        if (miuiAppStateMonitor != null) {
            MiuiAppStateMonitor.ForegroundAppInfo foregroundAppInfo = miuiAppStateMonitor.getForegroundAppInfo();
            log("DEBUGCOLD getTopActivityName foregroundAppInfo:" + foregroundAppInfo);
            if (foregroundAppInfo != null && !TextUtils.isEmpty(foregroundAppInfo.mPackageName) && foregroundAppInfo.mPackageName.equalsIgnoreCase(str) && foregroundAppInfo.mIsCold) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldVideoAndGameOptimizationForMCC() {
        if (!MiuiPhoneUtils.isMobileDataConnected()) {
            log("shouldVideoAndGameOptimizationForMCC no data");
            return false;
        }
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        if (phone == null) {
            log("shouldVideoOptimizationForMCC phone is null");
            return false;
        }
        String networkOperatorForPhone = this.mTelephonyManager.getNetworkOperatorForPhone(phone.getPhoneId());
        if (TextUtils.isEmpty(networkOperatorForPhone) || networkOperatorForPhone.length() < 3) {
            log("shouldVideoOptimizationForMCC plmn is empty");
            return false;
        }
        try {
            String strSubstring = networkOperatorForPhone.substring(0, 3);
            log("shouldVideoOptimizationForMCC mcc = ".concat(strSubstring));
            return this.OPTIMIZATION_MCC_SET.contains(strSubstring);
        } catch (StringIndexOutOfBoundsException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetVideoAndGameOptState() {
        log("resetVideoAndGameOptState isBeginVideoOptimization = " + this.isBeginVideoOptimization + " , isBeginGameOptimization = " + this.isBeginGameOptimization + " , mLastDataType = " + this.mLastDataType + " , mDataType = " + this.mDataType);
        if (this.isBeginVideoOptimization) {
            this.mLastDataType = 4;
            exitVideoOptimization();
        }
        if (this.isBeginGameOptimization) {
            this.mLastDataType = 2;
            exitGameOptimization();
        }
        this.mIsEnableLiveSafOpt = false;
        this.mIsEnableVideoSafOpt = false;
        this.mIsEnableGameSafOpt = false;
        this.mIsEnableSbpOpt = false;
        this.mIsEnableMdGameOpt = false;
        this.isBeginGameOptimization = false;
        this.isBeginVideoOptimization = false;
        this.mIsNeedDoDataReconnectAction = true;
        this.mIsNeedDoPsAttachAction = false;
    }

    private void resetVideoAndGameOptStateV2() {
        log("resetVideoAndGameOptStateV2 isBeginVideoOptimization = " + this.isBeginVideoOptimization + " , isBeginGameOptimization = " + this.isBeginGameOptimization + " , mLastPrioUid = " + this.mLastPrioUid + " , mDataType = " + this.mDataType);
        if (this.isBeginVideoOptimization) {
            enableLargeBWOpt(false);
            enableLLMOpt(false);
        }
        if (this.isBeginGameOptimization) {
            enableLLMOpt(false);
            doPowerHal(2, this.mLastPrioUid);
        }
        this.mIsNeedDoDataReconnectAction = true;
        this.mIsNeedDoPsAttachAction = false;
    }

    @Override // com.android.phone.NetworkOptimizerBase
    public synchronized void reportGlobalSafStatistic() {
        log("reportGlobalSafStatistic");
        ArrayList arrayList = new ArrayList(this.mGlobalSafDataList);
        if (arrayList.isEmpty()) {
            log("reportGlobalSafStatistic mGlobalSafDataList is empty");
            this.mGlobalSafDataList.clear();
        } else {
            HashMap map = new HashMap();
            map.put("Params", arrayList);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_global_saf_statistic", map);
            this.mGlobalSafDataList.clear();
        }
    }

    private void clearSafData() {
        String[] strArr = {"601", "603"};
        Phone phone = MiuiPhoneUtils.getPhone(MiuiPhoneUtils.getActiveDataPhoneId());
        if (phone == null) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            String[] strArr2 = {"AT+EDMFAPP=20100," + strArr[i], "+EDMFAPP"};
            try {
                logV("clearSafData fullCmd :" + Arrays.toString(strArr2));
                FiveGManagerBase.getInstance().sendAtCommand(phone, strArr2, (Message) null);
            } catch (Exception e) {
                log("clearSafData failed:" + e);
            }
        }
    }

    private void getModemSafData(int i, ArrayList arrayList) {
        String[] strArr = {"AT+EDMFAPP=20100,600", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        Object[] objArr2 = new Object[1];
        if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EDMFAPP=20100,602", "+EDMFAPP"}, objArr2)) {
            Object obj = objArr2[0];
            if (obj != null && (obj instanceof String[])) {
                String[] strArr2 = (String[]) obj;
                if (strArr2.length <= 0 || TextUtils.isEmpty(strArr2[0])) {
                    return;
                }
                String[] strArrSplit = strArr2[0].split(",");
                if (strArrSplit.length > 1) {
                    try {
                        parseSafResponse(objArr2[0], arrayList, Integer.parseInt(strArrSplit[1]));
                        return;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        log("getModemSafData version invalid response");
                        return;
                    }
                }
                return;
            }
            log("getModemSafData invalid response type");
            return;
        }
        if (FiveGManagerBase.getInstance().atCmdRunSync(i, strArr, objArr)) {
            parseSafResponse(objArr[0], arrayList, 10);
        }
    }

    private void parseSafResponse(Object obj, ArrayList arrayList, int i) {
        ConcurrentHashMap concurrentHashMapCreateMapWithDataForVersion;
        if (!(obj instanceof String[])) {
            log("parseSafResponse invalid response type");
            return;
        }
        String[] strArr = (String[]) obj;
        logV("parseSafResponse result:" + Arrays.toString(strArr));
        if (strArr.length <= 0 || TextUtils.isEmpty(strArr[0])) {
            return;
        }
        String[] strArrSplit = strArr[0].split(",");
        if (10 == i && strArrSplit.length < 40) {
            log("parseSafResponse SNS_VERSION_V1_0 data length:" + strArrSplit.length + " is invalid.");
            return;
        }
        if (isDataLengthMatched(strArrSplit, i)) {
            if (!isValidSafData(strArrSplit, i)) {
                log("parseSafResponse Invalid data version:" + i);
            } else {
                if (10 == i) {
                    concurrentHashMapCreateMapWithDataForVersion = createMapWithData(strArrSplit);
                } else {
                    concurrentHashMapCreateMapWithDataForVersion = createMapWithDataForVersion(strArrSplit, i);
                }
                arrayList.add(concurrentHashMapCreateMapWithDataForVersion);
            }
        }
    }

    private boolean isDataLengthMatched(String[] strArr, int i) {
        if (10 == i) {
            return true;
        }
        if (strArr.length > 7) {
            try {
                int i2 = Integer.parseInt(strArr[1]);
                int i3 = (Integer.parseInt(strArr[3]) * Integer.parseInt(strArr[4])) + (Integer.parseInt(strArr[5]) * (Integer.parseInt(strArr[6]) + 2)) + 7;
                logV("parseSafResponse " + i2 + " dataLength:" + i3 + ", lineDataLength:" + strArr.length);
                if (strArr.length >= i3) {
                    return true;
                }
                log("parseSafResponse " + i2 + " lineDataLength:" + strArr.length + " is invalid.");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isValidSafData(String[] strArr, int i) {
        int i2 = i == 10 ? 2 : 7;
        if (strArr != null && strArr.length >= i2) {
            while (i2 < strArr.length) {
                String str = strArr[i2];
                if (str != null && !str.trim().equals("0")) {
                    return true;
                }
                i2++;
            }
        }
        return false;
    }

    private ConcurrentHashMap createMapWithData(String[] strArr) {
        try {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("slotId", strArr[1]);
            concurrentHashMap.put("dataType", Integer.valueOf(this.mLastDataType));
            concurrentHashMap.put("ulDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, 2, 8)));
            concurrentHashMap.put("dlDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, 8, 14)));
            concurrentHashMap.put("highBler", TextUtils.join(",", Arrays.copyOfRange(strArr, 14, 20)));
            concurrentHashMap.put("highInterfer", TextUtils.join(",", Arrays.copyOfRange(strArr, 20, 26)));
            concurrentHashMap.put("nwAbnormal", TextUtils.join(",", Arrays.copyOfRange(strArr, 26, strArr.length)));
            return concurrentHashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ConcurrentHashMap createMapWithDataForVersion(String[] strArr, int i) {
        try {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            int i2 = Integer.parseInt(strArr[1]);
            concurrentHashMap.put("version", Integer.valueOf(i2));
            if (i2 != i) {
                concurrentHashMap.put("errMsg", "data version is not match.");
                return concurrentHashMap;
            }
            concurrentHashMap.put("slotId", strArr[2]);
            concurrentHashMap.put("modeNum", Integer.valueOf(Integer.parseInt(strArr[3])));
            int i3 = Integer.parseInt(strArr[4]);
            concurrentHashMap.put("cellChangeNum", Integer.valueOf(i3));
            int i4 = Integer.parseInt(strArr[5]);
            concurrentHashMap.put("iRatInfo", Integer.valueOf(i4));
            int i5 = Integer.parseInt(strArr[6]);
            concurrentHashMap.put("iRatCause", Integer.valueOf(i5));
            concurrentHashMap.put("dataType", Integer.valueOf(this.mLastDataType));
            int i6 = i3 + 7;
            concurrentHashMap.put("ulDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, 7, i6)));
            int i7 = i6 + i3;
            concurrentHashMap.put("dlDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, i6, i7)));
            int i8 = i7 + i3;
            concurrentHashMap.put("highBler", TextUtils.join(",", Arrays.copyOfRange(strArr, i7, i8)));
            int i9 = i8 + i3;
            concurrentHashMap.put("highInterfer", TextUtils.join(",", Arrays.copyOfRange(strArr, i8, i9)));
            int i10 = (i4 * (i5 + 2)) + i3 + i9;
            concurrentHashMap.put("nwAbnormal", TextUtils.join(",", Arrays.copyOfRange(strArr, i9, i10)));
            int i11 = i3 + i10;
            concurrentHashMap.put("goodLink", TextUtils.join(",", Arrays.copyOfRange(strArr, i10, i11)));
            concurrentHashMap.put("opOnly", TextUtils.join(",", Arrays.copyOfRange(strArr, i11, strArr.length)));
            logV("createMapWithDataForVersion map:" + concurrentHashMap);
            return concurrentHashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnableDebugGlobalDataOptState(boolean z) {
        if (mIsEnableDebugGlobalDataOpt != z) {
            mIsEnableDebugGlobalDataOpt = z;
        }
    }

    private void showToast(String str) {
        if (!Build.IS_STABLE_VERSION && mIsEnableDebugGlobalDataOpt) {
            Toast toast = this.mToast;
            if (toast != null) {
                toast.cancel();
            }
            try {
                String toastMessage = getToastMessage(str);
                logV("showToast msg = " + toastMessage);
                Toast toastMakeText = Toast.makeText(this.mContext.getApplicationContext(), toastMessage, 0);
                this.mToast = toastMakeText;
                toastMakeText.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getToastMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return (String) TOAST_MSG_MAP.getOrDefault(str.toLowerCase(), "");
    }
}
