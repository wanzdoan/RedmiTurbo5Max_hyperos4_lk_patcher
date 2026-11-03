package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.OutcomeReceiver;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ModemActivityInfo;
import android.telephony.PhoneStateListener;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.NetworkSpeedMonitor;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.android.phone.utils.SignalUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miuix.preference.flexible.AbstractBaseTemplate;
import xiaomi.hypercomm.HyperCommManager;

/* JADX INFO: loaded from: classes.dex */
public class FiveGPowerController extends Handler implements TelephonyStatAdapter.ICallBack {
    private static int CDRX_TOO_FREQUENCY_DELAY_TIME = 300000;
    private static String LOG_TAG = "FiveGPowerController";
    protected static volatile FiveGPowerController mInstance;
    private static long mModemSleepInNightNoSleep;
    private static long mModemSleepInScreenOff;
    private static long mModemSleepInSleep;
    private int DL_THROTTLE_LEVEL_ON;
    private int IDLE_NEI_MEA_MULTIPLIER;
    private int MAX_TX_POWER_ON;
    private int SMART_IDLE_ON;
    private int UL_THROTTLE_STATE_ON;
    private int currentIndex;
    public final int[] currentOosType;
    protected boolean isSupportFakeSaAction;
    private boolean isSupportSleepProtectedMode;
    ContentObserver m5GEnabledObserver;
    ContentObserver m5GPowerObserver;
    private boolean mApmOnForPS;
    private String mArdSwitchCondition;
    private String mArdSwitchTo4RXCondition;
    private long mArdSwitchTo4RXTime;
    protected int mBatteryLevel;
    private final BatteryStateManager.BatteryStateListener mBatteryStateChangeListener;
    private int[] mCallFailedCounts;
    private CdrxConfig mCdrxConfig;
    private long mCdrxSceneDisableSaCount;
    private int mCoexistenceStatus;
    private ConnectivityManager mConnectivityManager;
    protected Context mContext;
    ContentObserver mDataEnabledObserver;
    private boolean mDebugPowerSaveMode;
    private FiveGPowerDelegateImpl mDelegate;
    private final BroadcastReceiver mDeviceStateReceiver;
    private final DisplayManager.DisplayListener mDisplayListener;
    private long[] mElapsedRealtimeForSignalChange;
    public boolean[] mEndcPowerEnabled;
    protected volatile boolean mEndcSceneChangedInCall;
    private boolean mFiveGHostedModeEnabled;
    ContentObserver mFiveGHostedModeEnabledObserver;
    private final BroadcastReceiver mFiveGOptimizationReceiver;
    public NrSceneConstants$Scene mFiveGOptimizeScene;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private boolean mGameResidentCellOptimizationRst;
    private boolean mGameRst;
    private boolean mGoodSignalForPS;
    public boolean mHasPowerSaveOpt;
    ContentObserver mHikingSubwayStateObserver;
    private ModemUtils mHydra;
    protected boolean mIdleNeiMea;
    protected long[] mIdleNeiMeaModemOptCount;
    protected long mIdleNeiMeaOptCount;
    protected volatile boolean mInCall;
    private TieredIratPingpongListener mIratPingpongListener;
    private boolean mIsBalanceModeSupport;
    public boolean mIsBwpClosed;
    public boolean[] mIsCallFailedSaDisabled;
    protected boolean mIsCharging;
    private boolean mIsFiveGHostedModeSupported;
    private boolean mIsGameForgroud;
    private int[] mIsInDeSa;
    boolean mIsInGame;
    private boolean mIsInit;
    private boolean mIsLastScreenOn;
    protected boolean mIsLimitCASupported;
    private boolean[] mIsLteCaChangedFailedForSlot;
    private boolean mIsMobileConnected;
    private boolean mIsModemSaveLevel;
    private boolean mIsNightMode;
    protected boolean[] mIsNrBGSearch;
    protected boolean mIsNsaScgPoorOptimizationSupported;
    protected boolean mIsNsaScgPoorStatusFlag;
    private boolean mIsPowerSaveMode;
    private boolean mIsPowerSaveModeSupport;
    protected boolean mIsRecFreeze;
    protected volatile boolean mIsRilHookReady;
    public volatile boolean mIsSilentSaEndcDisabled;
    private boolean mIsSsrEndcDisabled;
    private boolean mIsTestApp;
    private boolean mIsThrottleForPS;
    private boolean mIsWechatVoipForgroud;
    private boolean mIsWechatVoipPreferForLte;
    private boolean mIsWechatVoipPreferForLteBySa;
    private boolean mIsWhiteAppForNsaScgPoorOptimization;
    private volatile boolean mIsWifiConnected;
    private long[] mLastDepriSaTime;
    private long mLastDisableUlMimoTime;
    public NrSceneConstants$Scene[] mLastOptScene;
    private final long[] mLastRadioRestartTime;
    private final int[] mLastRegState;
    private int[] mLastRilRat;
    NrSceneConstants$Scene mLastScene;
    private boolean mLastWifiConnected;
    protected boolean mLimitCA;
    protected int mLimitCaBitMask;
    private boolean mLowBatteryForPS;
    private boolean mLowBatteryStatus;
    private boolean mLteCaChangedInCall;
    protected long mMaxTxPowerOptCount;
    protected long mMimoSceneChangeRxCount;
    private MiuiNetworkRecoveryListener mMiuiNetworkRecoveryListener;
    private int mModeAction;
    private CdrxConfig mModemCdrxConfig;
    protected long mModemSSRCausedByBwpCount;
    protected long mModemSSRCausedByN1Count;
    protected long mModemSSRCausedByN28Count;
    private boolean mNeedReportNsaScgPoor;
    private NetworkSpeedListener mNetworkSpeedListener;
    private long mNightModeSceneDisableSaCount;
    private boolean mNormalSignalForPS;
    protected long mNsaScgPoorFreezeRecoverCounts;
    protected long mNsaScgPoorOptimizationCounts;
    public boolean mNsaThermalFlag;
    protected long mNwCaLimitOptCount;
    private final BroadcastReceiver mOOSPowerReceiver;
    private final long[][] mOOSTimestamp;
    private int mOrientationType;
    private CdrxConfig mPendingModemCdrxConfig;
    private final PhoneStateListener mPhoneStateListener;
    private FiveGPowerFileStateListener mPowerLevelListener;
    private PowerManager mPowerManager;
    protected boolean mPowerOptimizationEnabled;
    private final BaseEvent.IEventListener mPowerSaveCloudChangeListener;
    private FiveGPowerFileStateListener mPowerSaveListener;
    private boolean mPreIsNightMode;
    private boolean mPreIsNightNoSleepMode;
    private final boolean[] mPreviousServiceState;
    private long mRecordEntryTimestampMillis;
    protected int mSASnr;
    public boolean[] mSaPowerEnabled;
    private boolean mSaPowerOptimizationEnabled;
    private boolean mSaPowerOptimizationSupported;
    private int mSaPowerScene;
    public volatile boolean mSaThermalFlag;
    private volatile boolean mSaThermalNodeLimited;
    private volatile boolean mSaThermalPendingForNonWifi;
    private Handler mScreenHandler;
    private boolean mScreenOnForPS;
    private Runnable mScreenRunnable;
    private Sensor mSensor;
    private SensorEventListener mSensorListener;
    private SensorManager mSensorManager;
    private ServiceState[] mServiceState;
    protected long mSmartIdleOptCount;
    protected int mSmartIdleTimer;
    private boolean mSmartNetworkOptimizationRst;
    private final BaseEvent.IEventListener mSpecialSimStateListener;
    private int mSsRsrpForUlMimo;
    private int mSsSnrForUlMimo;
    private LinkedList mSsrTimeList;
    private boolean mStreamMediaOptimizationRst;
    public int[] mSwitchSceneCount;
    private long mThermalModeSceneDisableSaCount;
    private FiveGPowerFileStateListener mThermalStateListenerForNSA;
    private FiveGPowerFileStateListener mThermalStateListenerForSA;
    protected long mThermalThrottle2RXCount;
    protected long mThermalThrottleDL1Count;
    protected long mThermalThrottleDL2Count;
    protected long mThermalThrottleDSDACount;
    private FiveGPowerFileStateListener mThermalThrottleListener;
    private TieredPowerSavingListener mTieredPowerSavingListener;
    protected int mTxPower;
    private long mULMimoDisabledCount;
    private long mULMimoEnabledCount;
    protected long mULThrottleOptCount;
    protected int mULThrottleState;
    private boolean mUserDataEnabled;
    private PowerManager.WakeLock mWakeLock;
    private boolean mWechatOptimizationRst;
    private long mWechatSceneDisableSaCount;
    private boolean mWhiteAppForPS;
    private boolean mWhiteListEnabled;
    private boolean misArdWhiteApp;
    private boolean misFivegModeEnabled;
    private boolean misFivegSaEnabled;
    private boolean misTaskScheduled;
    private boolean modemMeasureRelaxStatus;
    protected long startDataStallTime;
    private static final ThreadLocal sCrashFileDateFormat = ThreadLocal.withInitial(new Supplier() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return FiveGPowerController.m682$r8$lambda$eBYktXZ7MXq3GVlszCX79q6oxU();
        }
    });
    protected static String COOLONG_DEVICE_PATH = null;
    public static int mThrottleLevel = 0;
    protected static Map sThermalThrottleFGAppMap = new HashMap();
    protected static Map sThermalThrottleSASnrMap = new HashMap();
    public static int mGoodSnrForSA = 10;
    public static boolean mIsThrottleWhiteApp = false;
    protected static Map mHstThermalThrottleCount = new HashMap();
    protected static Map mSubwayThermalThrottleCount = new HashMap();
    private static final List NSA_SCG_POOR_OPTIMIZATION_WHITE_LIST = Arrays.asList("com.tencent.ig", "com.mobile.legends", "com.alibaba.aliexpresshd", "com.google.android.youtube", "com.ss.android.ugc.trill", "com.zhiliaoapp.musically", "com.snapchat.android", "com.dts.freefiremax", "in.mohalla.video", "com.pubg.imobile", "com.google.android.apps.maps", "com.google.android.gm", "com.instagram.android", "com.whatsapp", "com.android.chrome", "com.twitter.android");
    private static final String[] ENABLE_MODEM_MEASURE_RELAX = {"AT+EDMFAPP=20107,107,5,1,0,0,0", "+EDMFAPP"};
    private static final String[] DISABLE_MODEM_MEASURE_RELAX = {"AT+EDMFAPP=20107,107,5,0,0,0,0", "+EDMFAPP"};
    private static int mPowerSaveOpt = 0;
    private static long mNightModeDuration = 0;
    private static int mPreScene = 0;
    private static int mModemScene = 0;
    private static long mScreenOffDuration = 0;
    private static long mNightModeNoSleepDuration = 0;
    private static long mEnterNightModeNoSleepTimeStamp = 0;
    private static long mEnterScreenOffTimeStamp = 0;
    private static long mEnterSleepingModemSleep = 0;
    private static long mEnterNightNoSleepingModemSleep = 0;
    private static long mEnterScreenOffModemSleep = 0;
    public static ArrayList mSleepingModemInfo = new ArrayList();
    public static ArrayList mNightModemInfo = new ArrayList();
    public static ArrayList mScreenOffModemInfo = new ArrayList();
    private static String mSleepReportedMessageIds = "";
    private static String mNightReportedMessageIds = "";
    private static String mScreenOffReportedMessageIds = "";
    public static boolean mIsThermalSpecialApp = false;
    public static boolean mIsCurrentHstState = false;
    protected static int[] mThermalSpecialLevel = new int[0];
    protected static boolean mIsInSubway = false;
    public static boolean mIsThrottleSpecialApp = false;
    protected static int[] mThrottleSpecialLevel = new int[0];
    private static boolean mHikingState = false;
    private static boolean m4RXActivateFlag = false;
    private static boolean mEnhanced4RXFlag = false;
    private static boolean mIsDeactivatedBySignal = false;
    private static int mDeactivatedPhoneId = -1;
    private static boolean mNeedResetUlMimoBySignal = false;
    private static boolean mNeedResetUlMimoByExpired = false;
    private static boolean mNeedResetUlMimoByCall = false;
    protected static Map sUlMimoRsrpMap = new HashMap();
    protected static Map sUlMimoSnrMap = new HashMap();

    public enum CdrxState {
        Unknown,
        Enabled,
        Disabled,
        Expired
    }

    public enum CdrxType {
        Unknown,
        SA,
        NSA
    }

    private int initMtkPlatformScene() {
        return 94;
    }

    private int initQcomPlatformScene() {
        return 254;
    }

    private boolean is5GMatched(int i) {
        return i == 20 || i == 30;
    }

    private boolean isCommonReason(int i) {
        return i == 0 || i == 5 || i == 10;
    }

    private boolean isNon4GNetworkType(int i) {
        return (i == 0 || i == 14 || i == 19) ? false : true;
    }

    private boolean isRatMatched(int i) {
        return (i == 20 || i == 30 || i == 0) ? false : true;
    }

    private static boolean isSignalQosOptimizedSupported() {
        return true;
    }

    protected void adjustNetworkSearchTimer(boolean z) {
    }

    protected void handleDmfUrcInd(Message message) {
    }

    protected boolean isResidentCell() {
        return false;
    }

    protected void notifyModemIdelNeiMea(int i, int i2) {
    }

    protected void notifyModemNeedReportNsaScgPoorStatus(boolean z, int[] iArr) {
    }

    protected void notifyModemNsaScgPoorSuccess(boolean z) {
    }

    protected void notifyModemRegisterIcdForNrBgSearch(boolean z) {
    }

    protected void notifyModemRegisterIcdForNsaScgPoor() {
    }

    protected void notifyModemReset() {
    }

    protected void recoverFakestation(int i) {
    }

    public void reportStatistics() {
    }

    protected void resetBGSearch() {
    }

    public void resetFakeStationList() {
    }

    protected void resetLimitNwCA() {
    }

    protected void resetNetworkSearchTimer() {
    }

    /* JADX INFO: renamed from: $r8$lambda$eB-YktXZ7MXq3GVlszCX79q6oxU, reason: not valid java name */
    public static /* synthetic */ SimpleDateFormat m682$r8$lambda$eBYktXZ7MXq3GVlszCX79q6oxU() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
    }

    public enum LimitCaScene {
        POWER_SAVE(1),
        SCREEN(2),
        NSA_SCG_POOR_MODE(4);

        protected int value;

        LimitCaScene(int i) {
            this.value = i;
        }
    }

    public class CdrxConfig {
        public static final int CDRX_CONFIG_LEN = getLength();
        int drxInactivityTimer;
        int drxInactivityTimerValid;
        int drxLongCycle;
        int drxLongCycleValid;
        int drxShortCycle;
        int drxShortCycleValid;
        CdrxState status;
        CdrxType type;

        private static int getLength() {
            return 24;
        }

        public CdrxConfig() {
            this.drxInactivityTimerValid = -1;
            this.drxInactivityTimer = -1;
            this.drxLongCycleValid = -1;
            this.drxLongCycle = -1;
            this.drxShortCycleValid = -1;
            this.drxShortCycle = -1;
            this.type = CdrxType.Unknown;
            this.status = CdrxState.Unknown;
        }

        public CdrxConfig(CdrxConfig cdrxConfig) {
            this.drxInactivityTimerValid = -1;
            this.drxInactivityTimer = -1;
            this.drxLongCycleValid = -1;
            this.drxLongCycle = -1;
            this.drxShortCycleValid = -1;
            this.drxShortCycle = -1;
            this.type = CdrxType.Unknown;
            this.status = CdrxState.Unknown;
            this.drxInactivityTimerValid = cdrxConfig.drxInactivityTimerValid;
            this.drxInactivityTimer = cdrxConfig.drxInactivityTimer;
            this.drxLongCycleValid = cdrxConfig.drxLongCycleValid;
            this.drxShortCycleValid = cdrxConfig.drxShortCycleValid;
            this.drxShortCycle = cdrxConfig.drxShortCycle;
            this.type = cdrxConfig.type;
            this.status = cdrxConfig.status;
        }

        public CdrxConfig(byte[] bArr) {
            this.drxInactivityTimerValid = -1;
            this.drxInactivityTimer = -1;
            this.drxLongCycleValid = -1;
            this.drxLongCycle = -1;
            this.drxShortCycleValid = -1;
            this.drxShortCycle = -1;
            this.type = CdrxType.Unknown;
            this.status = CdrxState.Unknown;
            if (bArr.length == CDRX_CONFIG_LEN) {
                try {
                    this.drxInactivityTimerValid = MiuiPhoneUtils.bytesToInt(bArr, 0, 4);
                    this.drxInactivityTimer = MiuiPhoneUtils.bytesToInt(bArr, 4, 4);
                    this.drxLongCycleValid = MiuiPhoneUtils.bytesToInt(bArr, 8, 4);
                    this.drxLongCycle = MiuiPhoneUtils.bytesToInt(bArr, 0, 4);
                    this.drxShortCycleValid = MiuiPhoneUtils.bytesToInt(bArr, 12, 4);
                    this.drxShortCycle = MiuiPhoneUtils.bytesToInt(bArr, 20, 4);
                    this.status = isContentEmpty() ? CdrxState.Disabled : CdrxState.Enabled;
                    this.type = MiuiPhoneUtils.is5GSaRegistered() ? CdrxType.SA : CdrxType.NSA;
                } catch (Exception e) {
                    FiveGPowerController.log("CdrxConfig parse modem date failed, Exception=" + e);
                }
            }
        }

        public void setStatus(CdrxState cdrxState) {
            this.status = cdrxState;
        }

        public CdrxState getStatus() {
            return this.status;
        }

        private boolean isContentEmpty() {
            return this.drxInactivityTimerValid == 0 && this.drxLongCycleValid == 0 && this.drxShortCycleValid == 0;
        }

        public void setContentEmpty() {
            this.drxInactivityTimerValid = 0;
            this.drxInactivityTimer = 0;
            this.drxLongCycleValid = 0;
            this.drxLongCycle = 0;
            this.drxShortCycleValid = 0;
            this.drxShortCycle = 0;
        }

        public void setType(CdrxType cdrxType) {
            this.type = cdrxType;
        }

        public CdrxType getType() {
            return this.type;
        }

        public boolean equals(Object obj) {
            if (obj instanceof CdrxConfig) {
                CdrxConfig cdrxConfig = (CdrxConfig) obj;
                if (this.status == cdrxConfig.status && this.type == cdrxConfig.type && this.drxInactivityTimer == cdrxConfig.drxInactivityTimer && this.drxLongCycle == cdrxConfig.drxLongCycle && this.drxShortCycle == cdrxConfig.drxShortCycle) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "CdrxConfig[" + this.status + "], " + this.type + ", inactivityTimerValid=" + this.drxInactivityTimerValid + ", inactivityTimer=" + this.drxInactivityTimer + ", longCycleValid=" + this.drxLongCycleValid + ",longCycle=" + this.drxLongCycle + ", shortCycleValid=" + this.drxShortCycleValid + ",shortCycle=" + this.drxShortCycle;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAnyPhoneInUse() {
        for (Phone phone : PhoneFactory.getPhones()) {
            log("phone state = " + phone.getState() + " phoneid = " + phone.getPhoneId());
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStateToRil(final String str, String str2) {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone != null) {
            HandlerThread handlerThread = new HandlerThread("getReportedMessageIDs");
            handlerThread.start();
            try {
                FiveGManagerBase.getInstance().sendAtCommand(dataPhone, new String[]{str, str2}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.FiveGPowerController.2
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                String[] strArr = (String[]) asyncResult.result;
                                FiveGPowerController.log("sendStateToRil: " + str + " getReportedMessageID: " + strArr[0]);
                                String str3 = strArr[0];
                                if (str3 == null || !"set success".equals(str3)) {
                                    if (str.equals("MIPC_GET_SLEEP_STATE")) {
                                        FiveGPowerController.mSleepReportedMessageIds = strArr[0];
                                    } else if (str.equals("MIPC_NIGHT_IND_STATE")) {
                                        FiveGPowerController.mNightReportedMessageIds += strArr[0];
                                    } else if (str.equals("MIPC_SCREEN_IND_MSG_IDS")) {
                                        FiveGPowerController.mScreenOffReportedMessageIds += strArr[0];
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getModemSleepMs(final int i, final boolean z) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        log("getModemSleepMs ENTER");
        try {
            telephonyManager.requestModemActivityInfo(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new OutcomeReceiver(this) { // from class: com.android.phone.FiveGPowerController.3
                @Override // android.os.OutcomeReceiver
                public void onResult(ModemActivityInfo modemActivityInfo) {
                    boolean z2 = z;
                    int i2 = i;
                    if (z2) {
                        if (i2 == 1) {
                            FiveGPowerController.mModemSleepInSleep += modemActivityInfo.getSleepTimeMillis() - FiveGPowerController.mEnterSleepingModemSleep;
                        } else if (i2 == 2) {
                            FiveGPowerController.mModemSleepInNightNoSleep += modemActivityInfo.getSleepTimeMillis() - FiveGPowerController.mEnterNightNoSleepingModemSleep;
                        } else if (i2 == 3) {
                            FiveGPowerController.mModemSleepInScreenOff += modemActivityInfo.getSleepTimeMillis() - FiveGPowerController.mEnterScreenOffModemSleep;
                        }
                    } else if (i2 == 1) {
                        FiveGPowerController.mEnterSleepingModemSleep = modemActivityInfo.getSleepTimeMillis();
                    } else if (i2 == 2) {
                        FiveGPowerController.mEnterNightNoSleepingModemSleep = modemActivityInfo.getSleepTimeMillis();
                    } else if (i2 == 3) {
                        FiveGPowerController.mEnterScreenOffModemSleep = modemActivityInfo.getSleepTimeMillis();
                    }
                    FiveGPowerController.log("getModemSleepMs FINISH");
                }

                @Override // android.os.OutcomeReceiver
                public void onError(TelephonyManager.ModemActivityInfoException modemActivityInfoException) {
                    FiveGPowerController.log("getModemSleepMs onError:" + modemActivityInfoException);
                }
            });
        } catch (Exception e) {
            log("getModemSleepMs ERROR:" + e);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.FiveGPowerController$4, reason: invalid class name */
    class AnonymousClass4 extends BroadcastReceiver {
        AnonymousClass4() {
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return;
            }
            if (action.equals("com.android.phone.intent.action.TPUT_OPTIMIZATION")) {
                FiveGPowerController.this.mIsTestApp = intent.getBooleanExtra("optimizationEnabled", false);
                FiveGPowerController.log("tput test app is " + FiveGPowerController.this.mIsTestApp + ", force enable ENDC");
                FiveGPowerController.this.obtainMessage(14).sendToTarget();
            } else if (action.equals("com.miui.securitycenter.intent.action.SMART_5G")) {
                FiveGPowerController.this.obtainMessage(8, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.WECHAT_VOIP_SCENE")) {
                FiveGPowerController.log("WECHAT_VOIP_SCENE mFiveGHostedModeEnabled =" + FiveGPowerController.this.mFiveGHostedModeEnabled);
                FiveGPowerController.this.mIsWechatVoipForgroud = intent.getBooleanExtra("wechatVoipEnabled", false);
                FiveGPowerController.this.obtainMessage(12, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.GAME_SCENE")) {
                FiveGPowerController.this.mIsGameForgroud = intent.getBooleanExtra("gameEnabled", false);
                FiveGPowerController.log("GAME_SCENE forgroud=" + FiveGPowerController.this.mIsGameForgroud);
                if (MiuiPhoneUtils.isSupportDatFeature()) {
                    FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                    fiveGPowerController.setDatConfig(fiveGPowerController.mIsGameForgroud, FiveGPowerController.this.mOrientationType);
                }
            } else if (action.equals("com.android.phone.intent.action.SPEED_WHITE_LIST")) {
                FiveGPowerController.this.mWhiteListEnabled = intent.getBooleanExtra("whiteListStateTop", false);
                FiveGPowerController.log("WHITE_LIST_APP forgroud=" + FiveGPowerController.this.mWhiteListEnabled);
            } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null) {
                    return;
                }
                int type = networkInfo.getType();
                FiveGPowerController.log("networkType=" + type + ",mPowerOptimizationEnabled=" + FiveGPowerController.this.mPowerOptimizationEnabled);
                if (type == 0 || (type == 1 && FiveGPowerController.this.mPowerOptimizationEnabled)) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        FiveGPowerController.this.obtainMessage(3, networkInfo.getType(), 0).sendToTarget();
                    } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        FiveGPowerController.this.obtainMessage(3, networkInfo.getType(), 1).sendToTarget();
                    }
                }
            } else if ("com.miui.modem_sleep_ratio".equals(intent.getAction())) {
                FiveGPowerController.log("MODEM_SLEEP_RATIO get modem_sleep_ratio, mModemScene: " + FiveGPowerController.mModemScene);
                ModemActivityInfo parcelableExtra = intent.getParcelableExtra("modem_sleep_ratio");
                if (parcelableExtra == null) {
                    FiveGPowerController.log("MODEM_SLEEP_RATIO modemActivityInfo is null");
                    return;
                }
                if (FiveGPowerController.mModemScene == 1) {
                    if (FiveGPowerController.mSleepingModemInfo.size() >= 100) {
                        FiveGPowerController.mSleepingModemInfo.remove(0);
                    }
                    FiveGPowerController.mSleepingModemInfo.add(parcelableExtra);
                } else if (FiveGPowerController.mModemScene == 2) {
                    if (FiveGPowerController.mNightModemInfo.size() >= 100) {
                        FiveGPowerController.mNightModemInfo.remove(0);
                    }
                    FiveGPowerController.mNightModemInfo.add(parcelableExtra);
                } else if (FiveGPowerController.mModemScene == 3) {
                    if (FiveGPowerController.mScreenOffModemInfo.size() >= 100) {
                        FiveGPowerController.mScreenOffModemInfo.remove(0);
                    }
                    FiveGPowerController.mScreenOffModemInfo.add(parcelableExtra);
                }
                if (FiveGPowerController.mPreScene == 0) {
                    FiveGPowerController.mModemScene = 0;
                    FiveGPowerController.log("MODEM_SLEEP_RATIO mModemScene = SCENE_NONE");
                }
            } else if ("com.miui.powerkeeper_sleep_changed".equals(intent.getAction())) {
                FiveGPowerController.this.mIsNightMode = 1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                boolean z = HyperOSCustFeatureResolve.getBoolean("support_special_night_mode", false);
                boolean zIsMobileDataEnabled = FiveGPowerController.this.isMobileDataEnabled();
                int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
                FiveGPowerController.log("ACTION_SLEEP_CHANGED mIsNightMode:" + FiveGPowerController.this.mIsNightMode + ", isMobileDataEnabled():" + zIsMobileDataEnabled + ", getInsertedSimCount():" + insertedSimCount);
                FiveGPowerController fiveGPowerController2 = FiveGPowerController.this;
                fiveGPowerController2.takeActionOnSleepModeChange(fiveGPowerController2.mIsNightMode, insertedSimCount);
                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                    Phone phone = MiuiPhoneUtils.getPhone(i);
                    if (phone != null && phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                        FiveGManagerBase.getInstance().setSleepModeState(i, FiveGPowerController.this.mIsNightMode ? 1 : 0);
                    }
                }
                if (FiveGPowerController.this.mIsNightMode && ((!zIsMobileDataEnabled || insertedSimCount == 0) && !FiveGPowerController.this.mPreIsNightMode)) {
                    if (z) {
                        FiveGPowerController.this.sendStateToRil("MIPC_GET_SLEEP_STATE", "true");
                        FiveGPowerController.this.getModemSleepMs(1, false);
                        FiveGPowerController.mPreScene = 1;
                        FiveGPowerController.mModemScene = 1;
                    }
                    FiveGPowerController.this.notifyModemIdelNeiMea(1, 1);
                    FiveGPowerController.this.mRecordEntryTimestampMillis = System.currentTimeMillis();
                    FiveGPowerController.this.mPreIsNightMode = true;
                } else if (FiveGPowerController.this.mIsNightMode && zIsMobileDataEnabled && z && !FiveGPowerController.this.mPreIsNightNoSleepMode) {
                    FiveGPowerController.log("ACTION_SLEEP_CHANGED NIGHT_STAND_MODE");
                    FiveGPowerController.this.sendStateToRil("MIPC_NIGHT_IND_STATE", "true");
                    FiveGPowerController.mPreScene = 2;
                    FiveGPowerController.mModemScene = 2;
                    FiveGPowerController.this.getModemSleepMs(2, false);
                    FiveGPowerController.mEnterNightModeNoSleepTimeStamp = System.currentTimeMillis();
                    FiveGPowerController.this.mPreIsNightNoSleepMode = true;
                } else if (!FiveGPowerController.this.mIsNightMode && FiveGPowerController.this.mPreIsNightMode) {
                    if (z) {
                        FiveGPowerController.this.getModemSleepMs(1, true);
                        FiveGPowerController.this.sendStateToRil("MIPC_GET_SLEEP_STATE", "false");
                    }
                    FiveGPowerController.this.notifyModemIdelNeiMea(1, 0);
                    FiveGPowerController.mNightModeDuration += System.currentTimeMillis() - FiveGPowerController.this.mRecordEntryTimestampMillis;
                    FiveGPowerController.this.mPreIsNightMode = false;
                    FiveGPowerController.mPreScene = 0;
                } else if (!FiveGPowerController.this.mIsNightMode && FiveGPowerController.this.mPreIsNightNoSleepMode) {
                    FiveGPowerController.log("ACTION_SLEEP_CHANGED exit NIGHT_STAND_MODE");
                    FiveGPowerController.this.sendStateToRil("MIPC_NIGHT_IND_STATE", "false");
                    FiveGPowerController.mPreScene = 0;
                    FiveGPowerController.this.getModemSleepMs(2, true);
                    FiveGPowerController.mNightModeNoSleepDuration += System.currentTimeMillis() - FiveGPowerController.mEnterNightModeNoSleepTimeStamp;
                    FiveGPowerController.this.mPreIsNightNoSleepMode = false;
                }
                if (MiuiPhoneUtils.isOOSPowerOptimizeSupported()) {
                    FiveGPowerController fiveGPowerController3 = FiveGPowerController.this;
                    fiveGPowerController3.processOOSPowerOptimize(fiveGPowerController3.mIsNightMode);
                }
                if (FiveGPowerController.this.mSaPowerOptimizationEnabled && FiveGPowerController.this.isSaPowerNightModeEnabled() && !MiuiPhoneUtils.shouldDeprioSaDuringPingPongErrorInNightMode()) {
                    FiveGPowerController.this.processNightModePowerOptimize(DefaultSimManager.getDefaultDataSlotId());
                }
                if (FiveGPowerController.this.mSaPowerOptimizationEnabled && FiveGPowerController.this.isSaPowerNightModeEnabled() && MiuiPhoneUtils.shouldDeprioSaDuringPingPongErrorInNightMode() && !FiveGPowerController.this.mIsNightMode) {
                    FiveGPowerController.this.processNightModePowerOptimize(DefaultSimManager.getDefaultDataSlotId());
                }
                if (FiveGPowerController.this.mIsPowerSaveModeSupport && !TelephonyCloudController.getCloudControllerMeasureRelaxOptDisabled()) {
                    FiveGPowerController fiveGPowerController4 = FiveGPowerController.this;
                    fiveGPowerController4.reduceIdleNeighborMea(fiveGPowerController4.mIsNightMode, FiveGPowerController.this.IDLE_NEI_MEA_MULTIPLIER);
                    FiveGPowerController fiveGPowerController5 = FiveGPowerController.this;
                    fiveGPowerController5.notifyModemIdelNeiMea(3, fiveGPowerController5.mIsNightMode ? 1 : 0);
                }
            } else if (action.equals("com.android.phone.intent.action.PINGPONG_EXCEPTION") || action.equals("com.android.phone.intent.action.PINGPONG_BY_MODEM")) {
                FiveGPowerController.log("ACTION_PINGPONG_EXCEPTION mIsNightMode: " + FiveGPowerController.this.mIsNightMode);
                if (FiveGPowerController.this.mSaPowerOptimizationEnabled && FiveGPowerController.this.isSaPowerNightModeEnabled() && MiuiPhoneUtils.shouldDeprioSaDuringPingPongErrorInNightMode() && FiveGPowerController.this.mIsNightMode) {
                    FiveGPowerController.this.processNightModePowerOptimize(intent.getIntExtra("phoneId", DefaultSimManager.getDefaultDataSlotId()));
                }
            } else if (action.equals("com.android.phone.intent.action.ACTIVITY_STATUS")) {
                if (MiuiPhoneUtils.isMimoSceneFiveGOptimizeSupported()) {
                    FiveGPowerController.this.obtainMessage(30, intent).sendToTarget();
                }
            } else if (action.equals("com.android.updater.action.COTA_CARRIER")) {
                FiveGPowerController.log("onReceive: COTA config");
                if (intent.getBooleanExtra("carrierName", false) && miui.telephony.TelephonyManager.isCustForFrOrange() && MiuiPhoneUtils.isNsaThermalControlSupported() && FiveGPowerController.this.mThermalStateListenerForNSA == null) {
                    FiveGPowerController fiveGPowerController6 = FiveGPowerController.this;
                    fiveGPowerController6.mThermalStateListenerForNSA = fiveGPowerController6.new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_limit", 2);
                    FiveGPowerController.this.mThermalStateListenerForNSA.startWatching();
                }
                FiveGPowerController.this.post(new Runnable() { // from class: com.android.phone.FiveGPowerController$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0();
                    }
                });
            } else if (action.equals("com.modemenhance.ACTION_SHARE_DATA")) {
                FiveGPowerController.log("ACTION_ARD_INTERFACE_OPTIMIZATION, modeAction: " + FiveGPowerController.this.mModeAction);
                FiveGPowerController.this.mModeAction = intent.getIntExtra("shared_data", 0);
            }
            if (MiuiPhoneUtils.isDynamicBwpSupported() && "com.android.phone.intent.action.TPUT_OPTIMIZATION".equals(intent.getAction())) {
                FiveGPowerController.log("TPUT_TEST_APP_OPTIMIZATION mIsTestApp: " + FiveGPowerController.this.mIsTestApp + "; mIsWifiConnected = " + FiveGPowerController.this.mIsWifiConnected + "; is5GSaRegistered = " + MiuiPhoneUtils.is5GSaRegistered() + "; mIsBwpClosed = " + FiveGPowerController.this.mIsBwpClosed + "; isAnyPhoneInUse() = " + FiveGPowerController.this.isAnyPhoneInUse());
                if ((FiveGPowerController.this.mIsWifiConnected || !MiuiPhoneUtils.is5GSaRegistered() || FiveGPowerController.this.isAnyPhoneInUse()) && FiveGPowerController.this.mIsTestApp) {
                    return;
                }
                boolean z2 = FiveGPowerController.this.mIsTestApp;
                FiveGPowerController fiveGPowerController7 = FiveGPowerController.this;
                if (z2) {
                    fiveGPowerController7.removeMessages(27);
                    FiveGPowerController.this.obtainMessage(27).sendToTarget();
                    FiveGPowerController.this.mIsBwpClosed = true;
                } else if (fiveGPowerController7.mIsBwpClosed) {
                    fiveGPowerController7.sendEmptyMessageDelayed(27, 30000L);
                    FiveGPowerController.this.mIsBwpClosed = false;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            String str = SystemProperties.get("persist.sys.cota.carrier", "");
            FiveGPowerController.log("cotaCarrier = " + str);
            if (FiveGPowerController.this.mIsNsaScgPoorOptimizationSupported) {
                if ("DT".equalsIgnoreCase(str) || "OR".equalsIgnoreCase(str)) {
                    FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                    if (fiveGPowerController.mIsNsaScgPoorStatusFlag) {
                        fiveGPowerController.mIsNsaScgPoorStatusFlag = false;
                        boolean zIsXmRadioSupported = FiveGUtils.isXmRadioSupported();
                        FiveGPowerController fiveGPowerController2 = FiveGPowerController.this;
                        if (zIsXmRadioSupported) {
                            fiveGPowerController2.changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                        } else {
                            fiveGPowerController2.execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                        }
                    }
                    FiveGPowerController.this.limitNwCA(false, LimitCaScene.NSA_SCG_POOR_MODE.value);
                    FiveGPowerController.this.mIsNsaScgPoorOptimizationSupported = false;
                }
            }
        }
    }

    public static void init() {
        if (mInstance == null) {
            synchronized (FiveGPowerController.class) {
                try {
                    if (mInstance == null) {
                        HandlerThread handlerThread = new HandlerThread("FiveGPowerThread");
                        handlerThread.start();
                        if (ModemProvider.IS_MTK) {
                            mInstance = new MtkFiveGPowerController(handlerThread.getLooper());
                        } else if (ModemProvider.IS_QCOM) {
                            mInstance = new QcFiveGPowerController(handlerThread.getLooper());
                        } else if (ModemProvider.IS_SPRD) {
                            mInstance = new SprdFiveGPowerController(handlerThread.getLooper());
                        } else {
                            mInstance = new FiveGPowerController(handlerThread.getLooper());
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static FiveGPowerController getInstance() {
        return mInstance;
    }

    protected FiveGPowerController(Looper looper) {
        ModemUtils modemUtils;
        super(looper);
        this.mIsCallFailedSaDisabled = new boolean[]{false, false};
        this.mCallFailedCounts = new int[]{0, 0};
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mPowerOptimizationEnabled = false;
        this.mSaPowerOptimizationEnabled = false;
        this.mSaPowerScene = initSaPowerScene();
        this.mSaPowerOptimizationSupported = MiuiPhoneUtils.isSaPowerOptimizationSupported();
        this.mCoexistenceStatus = -1;
        this.mInCall = false;
        this.mEndcSceneChangedInCall = false;
        this.mEndcPowerEnabled = new boolean[]{true, true};
        this.mSaPowerEnabled = new boolean[]{true, true};
        this.mIsInDeSa = new int[]{-1, -1};
        this.mIsInGame = false;
        this.mGameRst = true;
        this.mIsTestApp = false;
        this.mIsWechatVoipPreferForLte = false;
        this.mIsWechatVoipPreferForLteBySa = false;
        this.mIsInit = false;
        this.mIsLastScreenOn = false;
        this.mCdrxConfig = null;
        this.mPendingModemCdrxConfig = null;
        this.mModemCdrxConfig = null;
        this.mIsWechatVoipForgroud = false;
        this.mIsGameForgroud = false;
        this.mWhiteListEnabled = false;
        this.mIsNightMode = false;
        this.mLteCaChangedInCall = false;
        this.mIsLteCaChangedFailedForSlot = new boolean[i];
        this.mIsSilentSaEndcDisabled = false;
        this.mIsNrBGSearch = new boolean[i];
        this.mCdrxSceneDisableSaCount = 0L;
        this.mWechatSceneDisableSaCount = 0L;
        this.mNightModeSceneDisableSaCount = 0L;
        this.mThermalModeSceneDisableSaCount = 0L;
        this.mModemSSRCausedByN28Count = 0L;
        this.mModemSSRCausedByBwpCount = 0L;
        this.mModemSSRCausedByN1Count = 0L;
        this.mThermalStateListenerForSA = null;
        this.mThermalStateListenerForNSA = null;
        this.mSaThermalNodeLimited = false;
        this.mSaThermalPendingForNonWifi = false;
        this.mSaThermalFlag = false;
        this.mNsaThermalFlag = false;
        this.mModeAction = 0;
        this.mThermalThrottleListener = null;
        this.mThermalThrottle2RXCount = 0L;
        this.mThermalThrottleDL1Count = 0L;
        this.mThermalThrottleDL2Count = 0L;
        this.mThermalThrottleDSDACount = 0L;
        this.mSASnr = 0;
        this.mIsPowerSaveModeSupport = false;
        this.mIsBalanceModeSupport = false;
        this.mIsLimitCASupported = false;
        this.mPowerSaveListener = null;
        this.mPowerLevelListener = null;
        this.mHasPowerSaveOpt = false;
        this.mIsPowerSaveMode = false;
        this.mIsModemSaveLevel = false;
        this.mIsThrottleForPS = false;
        this.mWhiteAppForPS = false;
        this.modemMeasureRelaxStatus = false;
        this.mGoodSignalForPS = false;
        this.mNormalSignalForPS = false;
        this.mElapsedRealtimeForSignalChange = new long[]{0, 0, 0};
        this.mScreenOnForPS = true;
        this.mLowBatteryForPS = false;
        this.mApmOnForPS = false;
        this.UL_THROTTLE_STATE_ON = 3;
        this.mULThrottleState = 0;
        this.MAX_TX_POWER_ON = 6;
        this.mTxPower = 0;
        this.SMART_IDLE_ON = 5;
        this.mSmartIdleTimer = 60;
        this.DL_THROTTLE_LEVEL_ON = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? 1 : 9;
        this.IDLE_NEI_MEA_MULTIPLIER = 16;
        this.mIdleNeiMea = false;
        this.mLimitCA = false;
        this.mLimitCaBitMask = 0;
        this.mDebugPowerSaveMode = false;
        this.mULThrottleOptCount = 0L;
        this.mMaxTxPowerOptCount = 0L;
        this.mSmartIdleOptCount = 0L;
        this.mIdleNeiMeaOptCount = 0L;
        this.mIdleNeiMeaModemOptCount = new long[i];
        this.mNwCaLimitOptCount = 0L;
        this.mRecordEntryTimestampMillis = 0L;
        this.mScreenHandler = new Handler();
        this.misTaskScheduled = false;
        this.mIsBwpClosed = false;
        this.mMimoSceneChangeRxCount = 0L;
        this.mLowBatteryStatus = false;
        this.misArdWhiteApp = false;
        this.mArdSwitchCondition = "";
        this.mArdSwitchTo4RXCondition = "";
        this.mArdSwitchTo4RXTime = 0L;
        this.mSmartNetworkOptimizationRst = true;
        this.mWechatOptimizationRst = true;
        this.mStreamMediaOptimizationRst = true;
        this.mGameResidentCellOptimizationRst = true;
        this.isSupportFakeSaAction = MiuiPhoneUtils.isSupportFakeStationOptimization();
        this.mULMimoDisabledCount = 0L;
        this.mULMimoEnabledCount = 0L;
        this.mLastDepriSaTime = new long[]{0, 0};
        this.mSsSnrForUlMimo = Integer.MAX_VALUE;
        this.mSsRsrpForUlMimo = Integer.MAX_VALUE;
        this.isSupportSleepProtectedMode = MiuiPhoneUtils.isSupportSleepProtectOptimization();
        this.mOrientationType = 0;
        this.mFiveGOptimizeScene = NrSceneConstants$Scene.UNKNOWN;
        this.mLastRilRat = new int[i];
        this.mLastOptScene = new NrSceneConstants$Scene[i];
        this.mSwitchSceneCount = new int[i];
        this.mIsRilHookReady = false;
        this.mLastScene = NrSceneConstants$Scene.INIT;
        this.mDeviceStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.FiveGPowerController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if (action.equals("com.android.phone.intent.action.SLM_STATUS")) {
                    FiveGPowerController.this.obtainMessage(16, intent).sendToTarget();
                    return;
                }
                if (action.equals("qualcomm.intent.action.ACTION_NR5G_DRX_CONFIG_CHANGED")) {
                    FiveGPowerController.this.obtainMessage(18, intent).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    FiveGPowerController.this.obtainMessage(23, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    FiveGPowerController.this.obtainMessage(15);
                    FiveGPowerController.this.obtainMessage(56).sendToTarget();
                    FiveGPowerController.mIsDeactivatedBySignal = false;
                    FiveGPowerController.mDeactivatedPhoneId = -1;
                    return;
                }
                if (action.equals("com.android.phone.intent.action.THROTTLE_WHITE_APP_SCENE")) {
                    FiveGPowerController.mIsThrottleWhiteApp = intent.getBooleanExtra("throttleWhiteAppExtra", false);
                    return;
                }
                if (action.equals("com.android.phone.intent.action.THERMAL_SPECIAL_APP_SCENE")) {
                    FiveGPowerController.mIsThermalSpecialApp = intent.getBooleanExtra("thermalSpecialAppExtra", false);
                    return;
                }
                if (action.equals("com.android.phone.intent.action.THROTTLE_SPECIAL_APP_SCENE")) {
                    FiveGPowerController.mIsThrottleSpecialApp = intent.getBooleanExtra("throttleSpecialAppExtra", false);
                    return;
                }
                if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                    FiveGPowerController.this.obtainMessage(61, intent).sendToTarget();
                } else if ("android.os.action.CHARGING".equals(action)) {
                    FiveGPowerController.this.obtainMessage(55, Boolean.TRUE).sendToTarget();
                } else if ("android.os.action.DISCHARGING".equals(action)) {
                    FiveGPowerController.this.obtainMessage(55, Boolean.FALSE).sendToTarget();
                }
            }
        };
        this.mFiveGOptimizationReceiver = new AnonymousClass4();
        this.m5GPowerObserver = new ContentObserver(this) { // from class: com.android.phone.FiveGPowerController.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGPowerController.this.enableFiveGPowerOptimization();
            }
        };
        this.m5GEnabledObserver = new ContentObserver(this) { // from class: com.android.phone.FiveGPowerController.6
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGPowerController.this.clearCdrxConfigCache("5G switch");
                if (FiveGPowerController.isFivegModeEnabled()) {
                    FiveGPowerController.this.obtainMessage(15).sendToTarget();
                }
                if (MiuiPhoneUtils.isThermalThrottleSupported()) {
                    FiveGPowerController.this.removeMessages(35);
                    FiveGPowerController.this.sendMessageDelayed(FiveGPowerController.this.obtainMessage(35), 10000L);
                }
            }
        };
        this.mHikingSubwayStateObserver = new ContentObserver(this) { // from class: com.android.phone.FiveGPowerController.7
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                int i2 = Settings.Global.getInt(FiveGPowerController.this.mContext.getContentResolver(), "network_mode_state", 0);
                if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHikingStateUpdate(i2)) {
                    if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHikingIn(i2)) {
                        FiveGPowerController.mHikingState = true;
                    } else if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHikingOut(i2)) {
                        FiveGPowerController.mHikingState = false;
                    }
                    FiveGPowerController.log("Hiking state change to " + FiveGPowerController.mHikingState);
                }
                boolean zIsSubwayStartStopStateUpdate = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isSubwayStartStopStateUpdate(i2);
                if (FiveGPowerController.mIsInSubway != zIsSubwayStartStopStateUpdate) {
                    FiveGPowerController.mIsInSubway = zIsSubwayStartStopStateUpdate;
                    FiveGPowerController.this.sendEmptyMessage(62);
                }
            }
        };
        this.mDataEnabledObserver = new ContentObserver(this) { // from class: com.android.phone.FiveGPowerController.8
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                fiveGPowerController.mUserDataEnabled = fiveGPowerController.isMobileDataEnabled();
                FiveGPowerController.log("data enable state changed to ：" + FiveGPowerController.this.mUserDataEnabled + ", start check delay.");
                FiveGPowerController.this.removeMessages(4);
                Message messageObtainMessage = FiveGPowerController.this.obtainMessage(4);
                messageObtainMessage.arg1 = FiveGPowerController.this.mUserDataEnabled ? 1 : 0;
                FiveGPowerController.this.sendMessageDelayed(messageObtainMessage, 4000L);
            }
        };
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.phone.FiveGPowerController.9
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i2) {
                if (MiuiPhoneUtils.getInsertedSimCount() == 0) {
                    synchronized (FiveGPowerController.this.mWakeLock) {
                        FiveGPowerController.this.releaseWakeLock();
                    }
                    FiveGPowerController.log("onDisplayChanged Sim not inserted, not processed!");
                    return;
                }
                if (i2 == 0) {
                    Display display = ((DisplayManager) FiveGPowerController.this.mContext.getSystemService("display")).getDisplay(0);
                    if (display.getState() == 2) {
                        FiveGPowerController.log("onDisplayChanged to STATE ON");
                        if (MiuiPhoneUtils.isSupportDatFeature()) {
                            FiveGPowerController.log("register sensor TYPE_DEVICE_ORIENTATION");
                            FiveGPowerController.this.mSensorManager.registerListener(FiveGPowerController.this.mSensorListener, FiveGPowerController.this.mSensor, 3);
                        }
                        if (FiveGPowerController.this.mIsLastScreenOn) {
                            FiveGPowerController.log("onDisplayChanged isLastScreenOn is still on,ignore current displayChanged!!!");
                            return;
                        }
                        FiveGPowerController.this.mIsLastScreenOn = true;
                        synchronized (FiveGPowerController.this.mWakeLock) {
                            FiveGPowerController.this.releaseWakeLock();
                        }
                        FiveGPowerController.this.removeMessages(1);
                        FiveGPowerController.this.obtainMessage(1, 1, -1).sendToTarget();
                        return;
                    }
                    if (display.getState() == 1 && FiveGPowerController.this.mIsLastScreenOn) {
                        if (MiuiPhoneUtils.isSupportDatFeature()) {
                            FiveGPowerController.log("unregister sensor TYPE_DEVICE_ORIENTATION");
                            FiveGPowerController.this.mSensorManager.unregisterListener(FiveGPowerController.this.mSensorListener, FiveGPowerController.this.mSensor);
                        }
                        FiveGPowerController.this.mIsLastScreenOn = false;
                        boolean zIsFivegModeEnabled = FiveGPowerController.isFivegModeEnabled();
                        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                        FiveGPowerController.log("onDisplayChanged to STATE OFF, isFivegEnabled: " + zIsFivegModeEnabled + ", mEndcPowerEnabled[" + defaultDataSlotId + "]: " + FiveGPowerController.this.mEndcPowerEnabled[defaultDataSlotId] + ", mSaPowerEnabled[" + defaultDataSlotId + "]: " + FiveGPowerController.this.mSaPowerEnabled[defaultDataSlotId]);
                        if (FiveGPowerController.m4RXActivateFlag) {
                            FiveGPowerController.this.mArdSwitchCondition = "ardSwitchScreenOff";
                            FiveGPowerController.this.processDeactivate4RX(defaultDataSlotId);
                        }
                        FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                        if (fiveGPowerController.mIsNsaScgPoorOptimizationSupported || zIsFivegModeEnabled) {
                            if (fiveGPowerController.mEndcPowerEnabled[defaultDataSlotId] || fiveGPowerController.mSaPowerEnabled[defaultDataSlotId]) {
                                synchronized (fiveGPowerController.mWakeLock) {
                                    FiveGPowerController.this.acquireWakeLock();
                                }
                                FiveGPowerController.this.removeMessages(1);
                                FiveGPowerController.this.obtainMessage(1, 0, -1).sendToTarget();
                            }
                        }
                    }
                }
            }
        };
        this.mSensorListener = new SensorEventListener() { // from class: com.android.phone.FiveGPowerController.11
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int i2) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent sensorEvent) {
                int i2 = (int) sensorEvent.values[0];
                FiveGPowerController.log("onSensorChanged type=" + i2);
                FiveGPowerController.this.mOrientationType = i2;
                if (MiuiPhoneUtils.isSupportDatFeature()) {
                    FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                    fiveGPowerController.setDatConfig(fiveGPowerController.mIsGameForgroud, FiveGPowerController.this.mOrientationType);
                }
            }
        };
        this.mPhoneStateListener = new PhoneStateListener() { // from class: com.android.phone.FiveGPowerController.12
            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int i2, String str) {
                super.onCallStateChanged(i2, str);
                Message messageObtainMessage = FiveGPowerController.this.obtainMessage(7);
                messageObtainMessage.arg1 = i2;
                FiveGPowerController.this.sendMessage(messageObtainMessage);
            }
        };
        this.mIsSsrEndcDisabled = false;
        this.mSsrTimeList = new LinkedList();
        this.mFiveGHostedModeEnabledObserver = new ContentObserver(this) { // from class: com.android.phone.FiveGPowerController.16
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                FiveGPowerController.log("registerFiveGHostedModeObserver onChange");
                FiveGPowerController.this.sendMessage(FiveGPowerController.this.obtainMessage(11));
            }
        };
        this.currentOosType = new int[i];
        this.mPreviousServiceState = new boolean[i];
        this.mOOSTimestamp = (long[][]) Array.newInstance((Class<?>) Long.TYPE, i, 3);
        this.mLastRadioRestartTime = new long[i];
        this.mLastRegState = new int[i];
        this.currentIndex = 0;
        this.mOOSPowerReceiver = new BroadcastReceiver() { // from class: com.android.phone.FiveGPowerController.17
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                FiveGPowerController.log(intent.getAction());
                if ("android.intent.action.SERVICE_STATE".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("slot", -1);
                    Bundle extras = intent.getExtras();
                    ServiceState serviceStateNewFromBundle = extras != null ? ServiceState.newFromBundle(extras) : null;
                    if (intExtra == -1 || serviceStateNewFromBundle == null) {
                        return;
                    }
                    boolean z = serviceStateNewFromBundle.getState() == 0;
                    if (z != FiveGPowerController.this.mPreviousServiceState[intExtra]) {
                        FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                        if (fiveGPowerController.currentOosType[0] != 3) {
                            if (!z) {
                                fiveGPowerController.mOOSTimestamp[intExtra][FiveGPowerController.this.currentIndex % 3] = SystemClock.elapsedRealtime();
                                FiveGPowerController.this.currentIndex++;
                                if (FiveGPowerController.this.isFrequentOOS(intExtra)) {
                                    FiveGPowerController.this.obtainMessage(22, 2, intExtra).sendToTarget();
                                }
                            } else if (fiveGPowerController.isNetworkStable(intExtra)) {
                                FiveGPowerController.this.obtainMessage(22, 1, intExtra).sendToTarget();
                            }
                            FiveGPowerController.this.mPreviousServiceState[intExtra] = z;
                            return;
                        }
                        fiveGPowerController.obtainMessage(22, 3, intExtra).sendToTarget();
                    }
                }
            }
        };
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGPowerController.20
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                    return;
                }
                FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                fiveGPowerController.sendMessageDelayed(fiveGPowerController.obtainMessage(46, foregroundInfo), 1000L);
            }
        };
        this.mPowerSaveCloudChangeListener = new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$4(baseEvent);
            }
        };
        this.mBatteryStateChangeListener = new BatteryStateManager.BatteryStateListener() { // from class: com.android.phone.FiveGPowerController.22
            @Override // com.android.phone.BatteryStateManager.BatteryStateListener
            public void notifyBatteryStateChanged(int i2) {
                FiveGPowerController fiveGPowerController = FiveGPowerController.this;
                fiveGPowerController.sendMessageDelayed(fiveGPowerController.obtainMessage(51, Integer.valueOf(i2)), 1000L);
            }
        };
        this.mSpecialSimStateListener = new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda3
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$5(baseEvent);
            }
        };
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mConnectivityManager = (ConnectivityManager) phoneGlobals.getSystemService("connectivity");
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, "FiveG");
        FiveGPowerDelegateImpl fiveGPowerDelegateImpl = new FiveGPowerDelegateImpl();
        this.mDelegate = fiveGPowerDelegateImpl;
        ModuleDelegate.register(IFiveGPowerModule.class, fiveGPowerDelegateImpl);
        this.mScreenRunnable = new Runnable() { // from class: com.android.phone.FiveGPowerController.10
            @Override // java.lang.Runnable
            public void run() {
                if (FiveGPowerController.mPreScene != 0) {
                    FiveGPowerController.this.misTaskScheduled = false;
                    return;
                }
                FiveGPowerController.log("mScreenRunnable_ENTER_SCREEN_OFF");
                FiveGPowerController.this.sendStateToRil("MIPC_SCREEN_IND_MSG_IDS", "true");
                FiveGPowerController.mPreScene = 3;
                FiveGPowerController.mModemScene = 3;
                FiveGPowerController.this.misTaskScheduled = false;
                FiveGPowerController.mEnterScreenOffTimeStamp = System.currentTimeMillis();
                FiveGPowerController.this.getModemSleepMs(3, false);
            }
        };
        if (MiuiPhoneUtils.isOpTestVersion()) {
            log("op test version, return");
            return;
        }
        register5GPowerObserver();
        registerModemRestart();
        registerAirplaneState();
        CallManager.getInstance(this.mContext).registerForDisconnect(this, 17, (Object) null);
        this.mIsFiveGHostedModeSupported = MiuiPhoneUtils.isFiveGHostedModeSupported();
        log("FiveGPowerController mIsFiveGHostedModeSupported: " + this.mIsFiveGHostedModeSupported);
        if (this.mIsFiveGHostedModeSupported) {
            this.mFiveGHostedModeEnabled = isUserFiveGHostedModeEnabled();
            registerFiveGHostedModeListener();
            if (isSignalQosOptimizedSupported()) {
                registerRatSwitchEngineListener();
            }
        }
        if (MiuiPhoneUtils.isSupportTieredPowerSaving()) {
            registerTieredPowerSavingListener();
        }
        if (isSaPowerThermalModeEnabled()) {
            this.mIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
            FiveGPowerFileStateListener fiveGPowerFileStateListener = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_limit", 1);
            this.mThermalStateListenerForSA = fiveGPowerFileStateListener;
            fiveGPowerFileStateListener.startWatching();
        }
        if (MiuiPhoneUtils.isNsaThermalControlSupported()) {
            FiveGPowerFileStateListener fiveGPowerFileStateListener2 = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_limit", 2);
            this.mThermalStateListenerForNSA = fiveGPowerFileStateListener2;
            fiveGPowerFileStateListener2.startWatching();
        }
        if (MiuiPhoneUtils.isThermalThrottleSupported() && !MiuiPhoneUtils.isThermalThrottleDisableByCloud()) {
            FiveGPowerFileStateListener fiveGPowerFileStateListener3 = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_level", 3);
            this.mThermalThrottleListener = fiveGPowerFileStateListener3;
            fiveGPowerFileStateListener3.startWatching();
        }
        if (!TelephonyCloudController.CLOUD_IRAT_PINGPONG_OPT_DISABLED && MiuiPhoneUtils.isSupportIratPingpongOptimization()) {
            log("registerIratPingpongListener");
            registerIratPingpongListener();
        }
        register5GPowerOptimization();
        registerPhoneState();
        register5GEnabled();
        initOosReceiver();
        registerHikingSubwayState();
        updateThermalOptSpecialLevel();
        if (MiuiPhoneUtils.isSupportModemFeature() && !TextUtils.isEmpty(SystemProperties.get("persist.radio.modem_crash_feature", ""))) {
            sendMessageDelayed(obtainMessage(33), 30000L);
        }
        TelephonyStatAdapter.registerCallBack(this);
        if (MiuiPhoneUtils.isModemNonDdsRrcSupported() && !MiuiPhoneUtils.isModemNonddsRrcDisabledByCloud() && (modemUtils = this.mHydra) != null) {
            modemUtils.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SWITCH_XIAOMI_NONDDS_RRC.getType()), ModemUtils.modem_power_enable);
        }
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mSensor = sensorManager.getDefaultSensor(27);
        if (isScreenOn() && MiuiPhoneUtils.isSupportDatFeature()) {
            log("register sensor TYPE_DEVICE_ORIENTATION");
            this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 3);
        }
        post(new Runnable() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        });
        postDelayed(new Runnable() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$2();
            }
        }, 1000L);
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mSwitchSceneCount[i2] = 0;
            this.mLastOptScene[i2] = NrSceneConstants$Scene.UNKNOWN;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        boolean zIsSupportPowerSaveV2 = MiuiPhoneUtils.isSupportPowerSaveV2();
        this.mIsPowerSaveModeSupport = zIsSupportPowerSaveV2;
        if (zIsSupportPowerSaveV2) {
            FiveGPowerFileStateListener fiveGPowerFileStateListener = new FiveGPowerFileStateListener("/sys/class/thermal/power_save/powersave_mode", 4);
            this.mPowerSaveListener = fiveGPowerFileStateListener;
            fiveGPowerFileStateListener.startWatching();
            FiveGPowerFileStateListener fiveGPowerFileStateListener2 = new FiveGPowerFileStateListener("/sys/class/thermal/power_save/power_level", 5);
            this.mPowerLevelListener = fiveGPowerFileStateListener2;
            fiveGPowerFileStateListener2.startWatching();
            registerPowerSaveCloudChangeListener();
            registerBatteryStateChangeListener();
            registerDebugState();
            initPowerSaveOpt();
            this.mIsLimitCASupported = MiuiPhoneUtils.isSupportFivegaOptimization();
        }
        boolean zIsNsaScgPoorOptimizationSupported = MiuiPhoneUtils.isNsaScgPoorOptimizationSupported();
        this.mIsNsaScgPoorOptimizationSupported = zIsNsaScgPoorOptimizationSupported;
        if (zIsNsaScgPoorOptimizationSupported) {
            notifyModemRegisterIcdForNsaScgPoor();
            this.mIsCharging = isDeviceCharging();
        }
        if (this.mIsPowerSaveModeSupport || this.mIsNsaScgPoorOptimizationSupported) {
            registerForegroundInfoListener();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            registerSpecialSimStateListener();
        }
    }

    private void startTimer() {
        if (this.misTaskScheduled) {
            return;
        }
        log("startTimer30min: " + this.misTaskScheduled);
        this.mScreenHandler.postDelayed(this.mScreenRunnable, 1800000L);
        this.misTaskScheduled = true;
    }

    private void resetTimer() {
        if (this.misTaskScheduled) {
            log("resetTimer30min: " + this.misTaskScheduled);
            this.mScreenHandler.removeCallbacks(this.mScreenRunnable);
            this.misTaskScheduled = false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v10, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v9 */
    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i;
        ServiceState serviceState;
        DefaultSimManager.getDefaultDataSlotId();
        int i2 = message.what;
        if (i2 == 100) {
            if (MiuiPhoneUtils.isSupportModemFeature()) {
                String modemCrashReasonByCloud = FiveGCloudController.getModemCrashReasonByCloud();
                String str = TextUtils.isEmpty(modemCrashReasonByCloud) ? "bwp" : modemCrashReasonByCloud;
                log("modemCrashReasons  = " + str);
                if (!TextUtils.isEmpty(str)) {
                    sendMessageDelayed(obtainMessage(31, str), 30000L);
                }
            }
            ModemUtils modemUtils = this.mHydra;
            if (modemUtils != null) {
                modemUtils.callQcrilHookSet("onHookNotifyScreenStatusSync", Integer.valueOf(this.mIsLastScreenOn ? 1 : 0));
            }
            notifyModemReset();
            return;
        }
        if (i2 != 1000) {
            switch (i2) {
                case 1:
                    removeMessages(26);
                    removeMessages(2);
                    cancelNetworkSpeedMonitor();
                    ?? r3 = message.arg1 == 1 ? 1 : 0;
                    log("EVENT_SCREEN_STATE_CHANGED screen state changed to: " + ((boolean) r3));
                    if (r3 == 0) {
                        startTimer();
                    } else {
                        if (mPreScene == 3) {
                            mScreenOffDuration += System.currentTimeMillis() - mEnterScreenOffTimeStamp;
                            getModemSleepMs(3, true);
                            sendStateToRil("MIPC_SCREEN_IND_MSG_IDS", "false");
                            mPreScene = 0;
                        }
                        resetTimer();
                    }
                    if (isSaPowerWechatVoipEnabled() && r3 == 0 && this.mIsWechatVoipPreferForLteBySa) {
                        log("WeChat is in the foreground, after 30s off the screen, the sa capability");
                        Message messageObtainMessage = obtainMessage(26);
                        messageObtainMessage.arg1 = message.arg1;
                        sendMessageDelayed(messageObtainMessage, 30000L);
                    } else {
                        log("screen state changed to: " + ((boolean) r3) + ", start check delay.");
                        Message messageObtainMessage2 = obtainMessage(2);
                        messageObtainMessage2.arg1 = message.arg1;
                        sendMessageDelayed(messageObtainMessage2, 4000L);
                    }
                    if (MiuiPhoneUtils.isUlMimoOptimizationEnabled()) {
                        removeMessages(40);
                        if (r3 == 0) {
                            Message messageObtainMessage3 = obtainMessage(40);
                            messageObtainMessage3.arg1 = message.arg1;
                            sendMessageDelayed(messageObtainMessage3, 3000L);
                        }
                    }
                    if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && this.mIsPowerSaveModeSupport) {
                        processScreenForPowerSave(r3);
                    }
                    notifyModemIdelNeiMea(4, r3);
                    return;
                case 2:
                    boolean z = message.arg1 == 1;
                    boolean zIsScreenOn = isScreenOn();
                    log("screen state delay, oldScreenOn: " + z + ", currentScreenOn: " + zIsScreenOn);
                    if (z != zIsScreenOn) {
                        return;
                    }
                    boolean zIsFivegModeEnabled = isFivegModeEnabled();
                    boolean zIsMobileDataEnabled = isMobileDataEnabled();
                    log("screen state delay, isFivegModeEnabled: " + zIsFivegModeEnabled + ", isMobileDataEnabled: " + zIsMobileDataEnabled + ", mIsMobileConnected: " + this.mIsMobileConnected);
                    ModemUtils modemUtils2 = this.mHydra;
                    if (modemUtils2 != null) {
                        modemUtils2.callQcrilHookSet("onHookNotifyScreenStatusSync", Integer.valueOf(zIsScreenOn ? 1 : 0));
                    }
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateScreenState(zIsScreenOn);
                    if (zIsScreenOn) {
                        changeEnableEndcStatus(true, NrSceneConstants$Scene.SCREEN);
                        if (this.mIsNsaScgPoorOptimizationSupported || (isSatisfiedlimitCA() && !this.mWhiteAppForPS)) {
                            limitNwCA(false, LimitCaScene.SCREEN.value);
                            return;
                        }
                        return;
                    }
                    if (this.mIsNsaScgPoorOptimizationSupported) {
                        processScreenOffForNsaScgPoorOptimization(zIsMobileDataEnabled);
                        return;
                    }
                    if (zIsFivegModeEnabled) {
                        if (!this.mIsWifiConnected && zIsMobileDataEnabled && this.mIsMobileConnected) {
                            if (Build.IS_INTERNATIONAL_BUILD || isSatisfiedlimitCA()) {
                                startNetworkSpeedMonitor();
                                return;
                            }
                            synchronized (this.mWakeLock) {
                                releaseWakeLock();
                                break;
                            }
                            return;
                        }
                        changeEnableEndcStatus(false, NrSceneConstants$Scene.SCREEN);
                        if (this.mIsNsaScgPoorOptimizationSupported && this.mPowerOptimizationEnabled) {
                            limitNwCA(true, LimitCaScene.SCREEN.value);
                        }
                        synchronized (this.mWakeLock) {
                            releaseWakeLock();
                            break;
                        }
                        return;
                    }
                    synchronized (this.mWakeLock) {
                        releaseWakeLock();
                        break;
                    }
                    return;
                case 3:
                    this.mLastWifiConnected = this.mIsWifiConnected;
                    int i3 = message.arg1;
                    boolean z2 = message.arg2 == 0;
                    log("network state changed, networkType is " + i3 + ", new state: " + z2);
                    if (i3 == 0) {
                        if (z2 && z2 != this.mIsMobileConnected) {
                            log("Mobile network connected enable endc");
                            if (hasMessages(4)) {
                                removeMessages(4);
                            }
                            changeEnableEndcStatus(true, NrSceneConstants$Scene.WWAN);
                        }
                        this.mIsMobileConnected = z2;
                        return;
                    }
                    if (i3 == 1) {
                        log("Wifi old state is " + this.mLastWifiConnected);
                        if (this.mIsWifiConnected == z2) {
                            return;
                        }
                        this.mIsWifiConnected = z2;
                        boolean zIsFivegModeEnabled2 = isFivegModeEnabled();
                        log("Wifi network connect state is " + this.mIsWifiConnected);
                        if (this.mIsWifiConnected) {
                            log("Wifi network connected, fivegEnabled: " + zIsFivegModeEnabled2);
                            log("Wifi network connected, mCoexistenceStatus: " + this.mCoexistenceStatus);
                            changeEnableEndcStatus(false, NrSceneConstants$Scene.WLAN);
                            return;
                        }
                        boolean zIsMobileDataEnabled2 = isMobileDataEnabled();
                        log("Wifi network disconnected, isMobileDataEnabled: " + zIsMobileDataEnabled2);
                        if (zIsMobileDataEnabled2) {
                            changeEnableEndcStatus(true, NrSceneConstants$Scene.WLAN);
                        }
                        if (isSaPowerThermalModeEnabled()) {
                            if (this.mSaThermalPendingForNonWifi || this.mSaThermalNodeLimited) {
                                processThermalPowerOptimizeForSA();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                case 4:
                    boolean z3 = message.arg1 == 1;
                    boolean zIsMobileDataEnabled3 = isMobileDataEnabled();
                    if (z3 != zIsMobileDataEnabled3) {
                        return;
                    }
                    log("data enable state delay, data enabled: " + zIsMobileDataEnabled3);
                    if (zIsMobileDataEnabled3) {
                        changeEnableEndcStatus(true, NrSceneConstants$Scene.WWAN);
                        return;
                    } else {
                        changeEnableEndcStatus(false, NrSceneConstants$Scene.WWAN);
                        return;
                    }
                case 5:
                    boolean z4 = message.arg1 == 0;
                    log("continual low speed callback, isLowSpeed: " + z4);
                    if (z4) {
                        if (this.misFivegModeEnabled) {
                            changeEnableEndcStatus(false, NrSceneConstants$Scene.SCREEN);
                        }
                        if (this.mIsNsaScgPoorOptimizationSupported && this.mPowerOptimizationEnabled) {
                            limitNwCA(true, LimitCaScene.SCREEN.value);
                        }
                        if (isSatisfiedlimitCA() && this.mHasPowerSaveOpt) {
                            limitNwCA(true, LimitCaScene.SCREEN.value);
                        }
                    }
                    synchronized (this.mWakeLock) {
                        releaseWakeLock();
                        break;
                    }
                    return;
                case 6:
                    changeEnableEndcStatus(true, NrSceneConstants$Scene.SSR);
                    return;
                case 7:
                    int i4 = message.arg1;
                    this.mInCall = i4 != 0;
                    log("call state change to " + i4 + ", inCall state is " + this.mInCall + ", mEndcSceneChangedInCall: " + this.mEndcSceneChangedInCall);
                    if (this.mInCall) {
                        return;
                    }
                    if (this.mEndcSceneChangedInCall) {
                        changeEnableEndcStatus(true, NrSceneConstants$Scene.CALL);
                    }
                    this.mEndcSceneChangedInCall = false;
                    if (this.mIsPowerSaveModeSupport) {
                        removeMessages(42);
                        sendMessageDelayed(obtainMessage(42), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    }
                    if (MiuiPhoneUtils.isThermalThrottleSupported()) {
                        removeMessages(35);
                        sendMessageDelayed(obtainMessage(35), 10000L);
                    }
                    if (this.mLteCaChangedInCall && MiuiPhoneUtils.isLimitLTECAForNightModeSupported() && !MiuiPhoneUtils.isLimitLTECAForNightModeDisabledByCloud()) {
                        this.mLteCaChangedInCall = false;
                        sendMessageDelayed(obtainMessage(37), 10000L);
                        return;
                    }
                    return;
                case 8:
                    onGameStatusChanged((Intent) message.obj);
                    return;
                case 9:
                    changeEnableEndcStatus(true, NrSceneConstants$Scene.UPDATE);
                    return;
                default:
                    switch (i2) {
                        case 11:
                            recordFiveGHostedModeButton();
                            this.mFiveGHostedModeEnabled = isUserFiveGHostedModeEnabled();
                            log("EVENT_FIVEG_HOSTED_MODE_CHANGED mFiveGHostedModeEnabled=" + this.mFiveGHostedModeEnabled);
                            obtainMessage(15).sendToTarget();
                            if (isSignalQosOptimizedSupported()) {
                                unregisterRatSwitchEngineListener();
                                if (this.mFiveGHostedModeEnabled) {
                                    registerRatSwitchEngineListener();
                                    return;
                                }
                                return;
                            }
                            return;
                        case 12:
                            onSpecialAppWhiteListChanged((Intent) message.obj);
                            return;
                        case 13:
                            changeEnableEndcStatus(message.arg1 == 1, NrSceneConstants$Scene.FIVEG_HOSTED_SPEED_MODE);
                            return;
                        case 14:
                            if (this.mIsTestApp) {
                                if (!FiveGUtils.isXmRadioSupported()) {
                                    changeEnableEndcStatus(true, NrSceneConstants$Scene.SPEED_TEST);
                                }
                                resetThermalThrottle();
                                if (!MiuiPhoneUtils.isUlMimoOptimizationEnabled() || getUlMimoStatus() == 0) {
                                    return;
                                }
                                setUlMimoStatus(true);
                                return;
                            }
                            sendMessageDelayed(obtainMessage(9), 4000L);
                            if (MiuiPhoneUtils.isThermalThrottleSupported()) {
                                removeMessages(35);
                                sendMessageDelayed(obtainMessage(35), 10000L);
                                return;
                            }
                            return;
                        case 15:
                            resetEndcStatus();
                            return;
                        case 16:
                            onSlmStatusChanged((Intent) message.obj);
                            return;
                        case 17:
                            Connection connection = (Connection) ((AsyncResult) message.obj).result;
                            if (connection == null || connection.getCall() == null || connection.getCall().getPhone() == null) {
                                return;
                            }
                            int phoneId = connection.getCall().getPhone().getPhoneId();
                            if (this.mIsCallFailedSaDisabled[phoneId]) {
                                return;
                            }
                            boolean zIsNrSaNetworkType = isNrSaNetworkType(connection.getCall().getPhone().getSubId());
                            int disconnectCause = connection.getDisconnectCause();
                            log("isNrSaNetworkType = " + zIsNrSaNetworkType + ", disconnectCause = " + disconnectCause);
                            if (zIsNrSaNetworkType) {
                                int[] iArr = this.mCallFailedCounts;
                                if (disconnectCause == 36) {
                                    iArr[phoneId] = iArr[phoneId] + 1;
                                    log("mCallFailedCounts [" + phoneId + "] = " + this.mCallFailedCounts[phoneId]);
                                    if (this.mCallFailedCounts[phoneId] >= 5) {
                                        this.mIsCallFailedSaDisabled[phoneId] = true;
                                        changeEnableEndcStatus(false, NrSceneConstants$Scene.CALL_FAILED, phoneId);
                                        this.mCallFailedCounts[phoneId] = 0;
                                        return;
                                    }
                                    return;
                                }
                                iArr[phoneId] = 0;
                                return;
                            }
                            return;
                        case 18:
                            onCdrxConfigChanged((Intent) message.obj);
                            return;
                        case 19:
                            if (MiuiPhoneUtils.is5GSaRegistered()) {
                                CdrxConfig cdrxConfig = this.mModemCdrxConfig;
                                CdrxConfig cdrxConfig2 = this.mCdrxConfig;
                                if (cdrxConfig == null) {
                                    if (cdrxConfig2 == null) {
                                        this.mCdrxConfig = new CdrxConfig();
                                    }
                                    this.mCdrxConfig.setStatus(CdrxState.Disabled);
                                    this.mCdrxConfig.setType(CdrxType.SA);
                                    this.mCdrxConfig.setContentEmpty();
                                    localLog("SaCDRX", "CDRX not received, treat as DISABLED");
                                    return;
                                }
                                if (!cdrxConfig.equals(cdrxConfig2) && this.mModemCdrxConfig.getType() == CdrxType.SA) {
                                    this.mCdrxConfig = new CdrxConfig(this.mModemCdrxConfig);
                                    localLog("SaCDRX", "CDRX not received, use last modem cdrx config");
                                    return;
                                } else {
                                    log("CDRX not received, end");
                                    return;
                                }
                            }
                            return;
                        case 20:
                            this.mModemCdrxConfig = this.mPendingModemCdrxConfig;
                            this.mPendingModemCdrxConfig = null;
                            localLog("SaCDRX", "CDRX check end, " + this.mModemCdrxConfig);
                            CdrxConfig cdrxConfig3 = this.mModemCdrxConfig;
                            if (cdrxConfig3 == null || cdrxConfig3.getType() != CdrxType.SA) {
                                return;
                            }
                            this.mCdrxConfig = new CdrxConfig(this.mModemCdrxConfig);
                            localLog("SaCDRX", "CDRX is SA, update config");
                            removeMessages(21);
                            return;
                        case 21:
                            clearCdrxConfigCache("CDRX expired");
                            return;
                        case 22:
                            updateOosType(message.arg1, message.arg2);
                            return;
                        case 23:
                            onSimStateChanged((Intent) message.obj);
                            return;
                        case 24:
                            int i5 = message.arg1;
                            log("EVENT_UPDATE_NIGHT_MODE pingPongPhoneId=" + i5);
                            Object[] objArr = MiuiPhoneUtils.isLimitLTECAForNightModeSupported() && !MiuiPhoneUtils.isLimitLTECAForNightModeDisabledByCloud();
                            if (this.mIsNightMode) {
                                if (objArr != false) {
                                    if (i5 != DefaultSimManager.getDefaultDataSlotId()) {
                                        processLimitLteCaOptimizeBySlot(i5);
                                    } else {
                                        processLimitLteCaOptimize();
                                    }
                                }
                                removeMessages(36);
                                sendMessageDelayed(obtainMessage(36, i5, 0), objArr == true ? 10000L : 0L);
                                return;
                            }
                            for (int i6 = 0; i6 < MiuiPhoneUtils.PHONE_COUNT; i6++) {
                                changeEnableEndcStatus(!this.mIsNightMode, NrSceneConstants$Scene.SA_NIGHT_MODE, i6);
                            }
                            if (objArr == true) {
                                removeMessages(37);
                                sendMessageDelayed(obtainMessage(i5 != -1 ? 38 : 37, i5, 0), 10000L);
                                return;
                            }
                            return;
                        case 25:
                            this.mIsSsrEndcDisabled = true;
                            changeEnableEndcStatus(false, NrSceneConstants$Scene.SSR);
                            enableFiveGPowerOptimization();
                            return;
                        case 26:
                            log("WeChat is in the foreground, and the screen has been off for 30s, immediately turn off the sa capability");
                            changeEnableEndcStatus(false, NrSceneConstants$Scene.SA_WECHAT_VOIP);
                            return;
                        case 27:
                            FiveGManagerBase.getInstance().setModemFeature(2, !this.mIsTestApp ? 1 : 0, DefaultSimManager.getDefaultDataSlotId());
                            removeMessages(28);
                            sendMessageDelayed(obtainMessage(28), 250L);
                            return;
                        case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                            FiveGManagerBase.getInstance().refreshMbn(DefaultSimManager.getDefaultDataSlotId());
                            sendMessage(obtainMessage(29));
                            return;
                        case 29:
                            MiuiPhoneUtils.hideOOS("REFRESH_MBN");
                            return;
                        case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                            if (((Intent) message.obj).getBooleanExtra("mimo_scene", false)) {
                                int nrRsrp = MiuiPhoneUtils.getNrRsrp();
                                if (this.mIsWifiConnected || nrRsrp < -100) {
                                    return;
                                }
                                switchRxMode(true);
                                return;
                            }
                            switchRxMode(false);
                            return;
                        case 31:
                            handleModemFeatureCrash((String) message.obj);
                            return;
                        case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                            changeModemSsrFeatureStatus((String) message.obj, false);
                            return;
                        case 33:
                            changeModemSsrFeatureStatus(true);
                            return;
                        case 34:
                            processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                            if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && this.mIsPowerSaveModeSupport) {
                                processSignalStrengthForPowerSave(message.arg1, (SignalStrength) message.obj);
                                return;
                            }
                            return;
                        case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                            processThermalThrottle();
                            return;
                        case 36:
                            changeEnableEndcStatus(!this.mIsNightMode, NrSceneConstants$Scene.SA_NIGHT_MODE, message.arg1);
                            return;
                        case 37:
                            processLimitLteCaOptimize();
                            return;
                        case 38:
                            processLimitLteCaOptimizeBySlot(message.arg1);
                            return;
                        case MiRilHookBase.EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY /* 39 */:
                            if (MiuiPhoneUtils.isUlMimoOptimizationEnabled()) {
                                mNeedResetUlMimoByExpired = true;
                                return;
                            }
                            return;
                        case 40:
                            if (!isNeedResetUlMimo() || getUlMimoStatus() == 0) {
                                return;
                            }
                            setUlMimoStatus(true);
                            return;
                        case 41:
                            boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                            if (zBooleanValue != this.mSmartNetworkOptimizationRst) {
                                this.mSmartNetworkOptimizationRst = zBooleanValue;
                                changeEnableEndcStatus(zBooleanValue, NrSceneConstants$Scene.SMART_NETWORK);
                                return;
                            }
                            return;
                        case 42:
                            if (isDLThrottleOptEnabled() && this.mHasPowerSaveOpt && !this.mIsThrottleForPS && !isNetworkOptimizationForSa()) {
                                if (this.mInCall || this.mIsTestApp) {
                                    return;
                                }
                                logForPowerSave("updateThrottleLevel");
                                this.mIsThrottleForPS = true;
                                updateThrottleLevel(this.DL_THROTTLE_LEVEL_ON);
                                return;
                            }
                            if (this.mHasPowerSaveOpt || !this.mIsThrottleForPS) {
                                return;
                            }
                            logForPowerSave("resetThermalThrottle");
                            resetThermalThrottle();
                            return;
                        case 43:
                            boolean z5 = message.arg1 == 1;
                            int i7 = message.arg2;
                            if (this.mInCall) {
                                log("changeEnableEndcStatus stoped by InCall, scene=" + NrSceneConstants$Scene.NETWORK_RECOVERY_MODE);
                                return;
                            }
                            changeEnableEndcStatus(z5, NrSceneConstants$Scene.NETWORK_RECOVERY_MODE, i7);
                            return;
                        case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                            StringBuilder sb = new StringBuilder("Smart Tiered Power Saving, isStrategyApply=");
                            sb.append(message.arg1 == 1);
                            sb.append(", phoneId=");
                            sb.append(message.arg2);
                            log(sb.toString());
                            changeEnableEndcStatus(message.arg1 == 1, NrSceneConstants$Scene.SMART_TIERED_POWER_SAVING, message.arg2);
                            return;
                        case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                            StringBuilder sb2 = new StringBuilder("do irat pingpong optimization enable sa: ");
                            sb2.append(message.arg1 == 1);
                            sb2.append(" phoneId=");
                            sb2.append(message.arg2);
                            log(sb2.toString());
                            changeEnableEndcStatus(message.arg1 == 1, NrSceneConstants$Scene.IRAT_PINGPONG_MODE, message.arg2);
                            return;
                        case 46:
                            if (this.mIsPowerSaveModeSupport) {
                                processForegroundForPowerSave((ForegroundInfo) message.obj);
                            }
                            if (this.mIsNsaScgPoorOptimizationSupported) {
                                processForegroundChangedForNsaScgPoorOptimization((ForegroundInfo) message.obj);
                            }
                            processForegroundForARD((ForegroundInfo) message.obj);
                            return;
                        case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                            if (message.arg1 == 1) {
                                updateForPowerSaveOn(3);
                                return;
                            } else {
                                resetForPowerSaveOff(3);
                                return;
                            }
                        case ModemServiceMoniter.EVENT_OEMHOOK_DDS_CHANGE_NOTIFY /* 48 */:
                            if (message.arg1 == 0) {
                                updateForPowerSaveOn(4);
                                return;
                            } else {
                                resetForPowerSaveOff(4);
                                return;
                            }
                        case 49:
                            updatePowerSaveOptStatus(FiveGCloudController.getPowerSaveOptStatus());
                            this.mIsBalanceModeSupport = FiveGCloudController.isBalanceModeSupport();
                            resetForPowerSaveOff(10);
                            updateModemMeasureRelaxByCloud();
                            logForPowerSave("CloudPowerSaveData update");
                            return;
                        case 50:
                            boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                            log("EVENT_WECHAT_VOIP_OPTIMIZATION enableWechatOptimization = " + zBooleanValue2 + " mWechatOptimizationRst =  " + this.mWechatOptimizationRst);
                            if (zBooleanValue2 != this.mWechatOptimizationRst) {
                                this.mWechatOptimizationRst = zBooleanValue2;
                                changeEnableEndcStatus(zBooleanValue2, NrSceneConstants$Scene.WECHAT_VIDEO_MTK);
                                log("EVENT_WECHAT_VOIP_OPTIMIZATION changeEnableEndcStatus");
                                return;
                            }
                            return;
                        case 51:
                            int iIntValue = ((Integer) message.obj).intValue();
                            this.mLowBatteryStatus = iIntValue <= 20;
                            processBatteryStateChange(iIntValue);
                            if (this.mLowBatteryStatus && m4RXActivateFlag) {
                                int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                                this.mArdSwitchCondition = "ardSwitchLowBattery";
                                processDeactivate4RX(defaultDataSlotId);
                                return;
                            }
                            return;
                        case 52:
                            processResidentCellChange();
                            return;
                        case 53:
                            if (this.mNeedReportNsaScgPoor) {
                                this.mNeedReportNsaScgPoor = false;
                                notifyModemNeedReportNsaScgPoorStatus(false, TelephonyCloudController.mNsaScgPoorThreshold);
                            }
                            if (this.mIsNsaScgPoorStatusFlag) {
                                this.mIsNsaScgPoorStatusFlag = false;
                                if (FiveGUtils.isXmRadioSupported()) {
                                    changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                                } else {
                                    execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                                }
                                if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                                    notifyModemNsaScgPoorSuccess(false);
                                }
                            }
                            limitNwCA(false, LimitCaScene.NSA_SCG_POOR_MODE.value);
                            return;
                        case 54:
                            handleDmfUrcInd(message);
                            return;
                        case 55:
                            processChargingStateChange(((Boolean) message.obj).booleanValue());
                            return;
                        case 56:
                            if (this.mIsNsaScgPoorOptimizationSupported || this.mIsPowerSaveModeSupport) {
                                resetLimitNwCA();
                                if (this.mIsNsaScgPoorStatusFlag) {
                                    this.mIsNsaScgPoorStatusFlag = false;
                                    if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                                        notifyModemNsaScgPoorSuccess(false);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        case 57:
                            Bundle bundle = (Bundle) message.obj;
                            int i8 = bundle.getInt("slotId");
                            boolean z6 = bundle.getBoolean("enableSA");
                            changeEnableEndcStatus(z6, NrSceneConstants$Scene.SPECIAL_SIM, i8);
                            log("special sim scene enable sa: " + z6 + " phoneId=" + i8);
                            return;
                        case 58:
                            boolean zBooleanValue3 = ((Boolean) message.obj).booleanValue();
                            log("EVENT_STREAM_MEDIA_OPTIMIZATION enableStreamMediaOptimization = " + zBooleanValue3 + " mStreamMediaOptimizationRst =  " + this.mStreamMediaOptimizationRst);
                            if (zBooleanValue3 != this.mStreamMediaOptimizationRst) {
                                this.mStreamMediaOptimizationRst = zBooleanValue3;
                                changeEnableEndcStatus(zBooleanValue3, NrSceneConstants$Scene.STREAM_MEDIA_MTK);
                                log("EVENT_STREAM_MEDIA_OPTIMIZATION changeEnableEndcStatus");
                                return;
                            }
                            return;
                        case 59:
                            obtainMessage(15);
                            obtainMessage(56).sendToTarget();
                            return;
                        case 60:
                            boolean zBooleanValue4 = ((Boolean) message.obj).booleanValue();
                            if (zBooleanValue4 != this.mGameResidentCellOptimizationRst) {
                                this.mGameResidentCellOptimizationRst = zBooleanValue4;
                                changeEnableEndcStatus(zBooleanValue4, NrSceneConstants$Scene.GAME_RESIDENT_CELL);
                                return;
                            }
                            return;
                        case 61:
                            boolean booleanExtra = ((Intent) message.obj).getBooleanExtra("mIsHstMode", false);
                            log("EVENT_HST_MODE_STATUS_CHANGED isCurrentHstState: " + booleanExtra + " mIsCurrentHstState=" + mIsCurrentHstState);
                            if (mIsCurrentHstState == booleanExtra) {
                                return;
                            }
                            mIsCurrentHstState = booleanExtra;
                            if (booleanExtra) {
                                resetThermalThrottle();
                                if (this.mSaThermalFlag && isHstDisableSaPowerModeScene()) {
                                    enableSaAsync(true, NrSceneConstants$Scene.RECOVERY_NR);
                                    this.mSaThermalFlag = false;
                                }
                                if (m4RXActivateFlag || !mIsDeactivatedBySignal || (i = mDeactivatedPhoneId) < 0 || (serviceState = this.mServiceState[i]) == null) {
                                    return;
                                }
                                int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
                                if (rilDataRadioTechnology == 14 || rilDataRadioTechnology == 19) {
                                    log("HST enter, restore 4RX from signal-based deactivation, phoneId=" + mDeactivatedPhoneId);
                                    processActivate4RX(mDeactivatedPhoneId);
                                    return;
                                }
                                return;
                            }
                            return;
                        case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                            if (mIsInSubway) {
                                resetThermalThrottle();
                                return;
                            }
                            return;
                        case 63:
                            if (((AsyncResult) message.obj).exception == null) {
                                log("phone = " + message.arg1 + ", enabled = " + message.arg2);
                                this.mEndcPowerEnabled[message.arg1] = message.arg2 == 1;
                                return;
                            }
                            log("set nr dual connectivity error.");
                            return;
                        default:
                            return;
                    }
            }
        }
        Intent intent = (Intent) message.obj;
        updatePowerSaveOptStatus(intent.getStringExtra("PowerSaveTest"));
        this.mIsBalanceModeSupport = intent.getBooleanExtra("BalanceMode", false);
        this.mDebugPowerSaveMode = intent.getBooleanExtra("DebugMode", false);
        logForPowerSave("PowerSaveTest set BalanceMode=" + this.mIsBalanceModeSupport + " DebugMode=" + this.mDebugPowerSaveMode);
        resetForPowerSaveOff(10);
    }

    private void onSimStateChanged(Intent intent) {
        int intExtra = intent.getIntExtra("phone", -1);
        if (SubscriptionManager.isValidPhoneId(intExtra)) {
            String stringExtra = intent.getStringExtra("ss");
            log("onSimStateChanged iccState=" + stringExtra + ", phoneId=" + intExtra);
            if ("ABSENT".equals(stringExtra)) {
                clearCdrxConfigCache("Sim absent");
                obtainMessage(56).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void takeActionOnSleepModeChange(boolean z, int i) {
        if ((i == 0 && MiuiPhoneUtils.isSupportNoSimPowerOptimization()) || !this.isSupportSleepProtectedMode || TelephonyCloudController.getCloudControllerSleepProtectOptDisabled()) {
            return;
        }
        log("takeActionOnSleepModeChange isInSleepMode=" + z + " insertedSimCount=" + i);
        notifyModemRegisterIcdForNrBgSearch(z);
        if (z) {
            adjustNetworkSearchTimer(i == 0);
        } else {
            resetNetworkSearchTimer();
            resetBGSearch();
        }
    }

    public void resetSleepProtectModeByCloud() {
        notifyModemRegisterIcdForNrBgSearch(false);
        resetNetworkSearchTimer();
        resetBGSearch();
    }

    public static boolean isFivegModeEnabled() {
        return FiveGManagerBase.getInstance().isUserFiveGEnabled();
    }

    public static boolean isFiveGSaModeEnabled() {
        return FiveGManagerBase.getInstance().isUserFiveGSaEnabled();
    }

    private boolean isScreenOn() {
        Display display = ((DisplayManager) this.mContext.getSystemService("display")).getDisplay(0);
        if (display != null) {
            if (display.getState() == 2) {
                log("Screen is on");
                return true;
            }
            log("Screen is off");
            return false;
        }
        log("Screen state: No displays found");
        return false;
    }

    public void enableFiveGPowerOptimization() {
        int i;
        FiveGPowerFileStateListener fiveGPowerFileStateListener;
        if (MiuiPhoneUtils.isOpTestVersion()) {
            return;
        }
        boolean zIsCustForEsVodafone = miui.telephony.TelephonyManager.isCustForEsVodafone();
        Context context = this.mContext;
        boolean z = false;
        if (zIsCustForEsVodafone) {
            i = Settings.System.getInt(context.getContentResolver(), "power_center_5g_save_mode", -1);
            log("EsVodafone version status is: " + i);
            if (i == -1) {
                Settings.System.putInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", 0);
                return;
            }
        } else {
            i = Settings.System.getInt(context.getContentResolver(), "power_center_5g_save_mode", 1);
        }
        boolean z2 = i == 1 || i != 0;
        log("enableFiveGPowerOptimization enable is: " + z2);
        if (this.mIsSsrEndcDisabled) {
            log("Modem ssr, ssr endc has disabled, close optimization");
            z2 = false;
        }
        if (FiveGManagerBase.getInstance() != null && !this.mIsInit) {
            FiveGManagerBase.getInstance().isGameFiveGOptimizeSupported();
            FiveGManagerBase.getInstance().getMobileNetworkCapability(-1);
            FiveGManagerBase.getInstance().setMobileNetworkCapability("");
            this.mIsInit = true;
        }
        if (z2) {
            log("enable 5G power optimization");
            this.mPowerOptimizationEnabled = true;
            if (this.mSaPowerOptimizationSupported) {
                if (FiveGCloudController.getInstance() != null) {
                    FiveGCloudController.getInstance().fetchSAPowerCloudStatus();
                }
                if (!MiuiPhoneUtils.isSaPowerDisabledBySecretCode() && !FiveGCloudController.is5GSaPowerDisabledByCloud()) {
                    z = true;
                }
                this.mSaPowerOptimizationEnabled = z;
                int sAPowerCloudEnableScene = FiveGCloudController.getSAPowerCloudEnableScene();
                if (sAPowerCloudEnableScene != 0) {
                    this.mSaPowerScene = sAPowerCloudEnableScene;
                }
            }
            if (isSaPowerThermalModeEnabled() && this.mThermalStateListenerForSA == null) {
                this.mIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
                FiveGPowerFileStateListener fiveGPowerFileStateListener2 = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_limit", 1);
                this.mThermalStateListenerForSA = fiveGPowerFileStateListener2;
                fiveGPowerFileStateListener2.startWatching();
            }
            if (MiuiPhoneUtils.isNsaThermalControlSupported() && !miui.telephony.TelephonyManager.isCustForFrOrange() && this.mThermalStateListenerForNSA == null) {
                FiveGPowerFileStateListener fiveGPowerFileStateListener3 = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_limit", 2);
                this.mThermalStateListenerForNSA = fiveGPowerFileStateListener3;
                fiveGPowerFileStateListener3.startWatching();
            }
            if (MiuiPhoneUtils.isThermalThrottleSupported() && !MiuiPhoneUtils.isThermalThrottleDisableByCloud() && this.mThermalThrottleListener == null) {
                FiveGPowerFileStateListener fiveGPowerFileStateListener4 = new FiveGPowerFileStateListener("/sys/class/thermal/thermal_message/modem_level", 3);
                this.mThermalThrottleListener = fiveGPowerFileStateListener4;
                fiveGPowerFileStateListener4.startWatching();
            }
            registerDeviceState();
            NetworkSpeedMonitor.init(this.mContext);
            TelephonyStatAdapter.registerCallBack(this);
        } else {
            log("disable 5G power optimization, mPowerOptimizationEnabled: " + this.mPowerOptimizationEnabled);
            if (this.mPowerOptimizationEnabled) {
                unregisterDeviceState();
                cancelNetworkSpeedMonitor();
                this.mPowerOptimizationEnabled = false;
            }
            if (this.mSaPowerOptimizationEnabled) {
                enableSaPowerOptimization(false);
            }
            if (isSaPowerThermalModeEnabled()) {
                FiveGPowerFileStateListener fiveGPowerFileStateListener5 = this.mThermalStateListenerForSA;
                if (fiveGPowerFileStateListener5 != null) {
                    fiveGPowerFileStateListener5.stopWatching();
                    this.mThermalStateListenerForSA = null;
                }
                this.mSaThermalFlag = false;
                this.mSaThermalNodeLimited = false;
                this.mSaThermalPendingForNonWifi = false;
            }
            if (MiuiPhoneUtils.isNsaThermalControlSupported() && !miui.telephony.TelephonyManager.isCustForFrOrange()) {
                FiveGPowerFileStateListener fiveGPowerFileStateListener6 = this.mThermalStateListenerForNSA;
                if (fiveGPowerFileStateListener6 != null) {
                    fiveGPowerFileStateListener6.stopWatching();
                    this.mThermalStateListenerForNSA = null;
                }
                this.mNsaThermalFlag = false;
            }
            if (MiuiPhoneUtils.isUlMimoOptimizationEnabled() && getUlMimoStatus() != 0) {
                setUlMimoStatus(true);
            }
            TelephonyStatAdapter.unregisterCallBack(this);
            if (MiuiPhoneUtils.isThermalThrottleSupported() && (fiveGPowerFileStateListener = this.mThermalThrottleListener) != null) {
                fiveGPowerFileStateListener.stopWatching();
                this.mThermalThrottleListener = null;
            }
        }
        obtainMessage(15).sendToTarget();
        obtainMessage(56).sendToTarget();
    }

    public void updateFiveGPowerSaveEnabled() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", -1) == -1) {
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            int subId = PhoneFactory.getPhone(defaultDataSlotId).getSubId();
            boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(this.mContext, subId, "config_power_center_5g_save_mode", true);
            log("updateFiveGPowerSaveEnabled slotId:" + defaultDataSlotId + ", subId:" + subId + ", isFiveGPowerSaveEnabled:" + booleanConfigForSubId);
            if (booleanConfigForSubId) {
                return;
            }
            Settings.System.putInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", 0);
        }
    }

    public void updateFiveGPowerDisabled() {
        int i = Settings.System.getInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", -1);
        int i2 = Settings.Global.getInt(this.mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", -1);
        if (i == -1) {
            Settings.System.putInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", 0);
        }
        if (i2 == -1) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", 0);
        }
    }

    private void register5GPowerObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("power_center_5g_save_mode"), false, this.m5GPowerObserver);
    }

    private void register5GEnabled() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("fiveg_user_enable"), false, this.m5GEnabledObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("fiveg_network_mode"), false, this.m5GEnabledObserver);
    }

    private void registerHikingSubwayState() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mHikingSubwayStateObserver);
    }

    public boolean getHikingState() {
        return mHikingState;
    }

    private void registerDeviceState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.SLM_STATUS");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        if (this.mSaPowerOptimizationEnabled && isSaPowerCdrxEnabled()) {
            intentFilter.addAction("qualcomm.intent.action.ACTION_NR5G_DRX_CONFIG_CHANGED");
            if (FiveGCloudController.getSAPowerCloudCdrxFequencyCheckTime() != -1) {
                CDRX_TOO_FREQUENCY_DELAY_TIME = FiveGCloudController.getSAPowerCloudCdrxFequencyCheckTime();
            }
        }
        if (MiuiPhoneUtils.isThermalThrottleSupported() && !MiuiPhoneUtils.isThermalThrottleDisableByCloud()) {
            intentFilter.addAction("com.android.phone.intent.action.THROTTLE_WHITE_APP_SCENE");
        }
        intentFilter.addAction("com.android.phone.intent.action.THERMAL_SPECIAL_APP_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.THROTTLE_SPECIAL_APP_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("android.os.action.CHARGING");
        intentFilter.addAction("android.os.action.DISCHARGING");
        this.mContext.registerReceiver(this.mDeviceStateReceiver, intentFilter, 2);
        ((DisplayManager) this.mContext.getSystemService("display")).registerDisplayListener(this.mDisplayListener, null);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(MiuiPhoneUtils.isMultiSimEnabled() ? "mobile_data1" : "mobile_data"), false, this.mDataEnabledObserver);
    }

    private void register5GPowerOptimization() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.TPUT_OPTIMIZATION");
        if (MiuiPhoneUtils.isGameFiveGOptimizeSupported()) {
            intentFilter.addAction("com.miui.securitycenter.intent.action.SMART_5G");
        }
        intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.GAME_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.SPEED_WHITE_LIST");
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        intentFilter.addAction("com.miui.modem_sleep_ratio");
        intentFilter.addAction("com.android.phone.intent.action.ACTIVITY_STATUS");
        intentFilter.addAction("com.android.updater.action.COTA_CARRIER");
        intentFilter.addAction("com.android.phone.intent.action.PINGPONG_EXCEPTION");
        if (HyperOSCustFeatureResolve.getBoolean("support_pingpong_report_by_BP_bool", false)) {
            intentFilter.addAction("com.android.phone.intent.action.PINGPONG_BY_MODEM");
        }
        intentFilter.addAction("com.modemenhance.ACTION_SHARE_DATA");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mFiveGOptimizationReceiver, intentFilter, 2);
    }

    private void unregisterDeviceState() {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        try {
            context.unregisterReceiver(this.mDeviceStateReceiver);
            ((DisplayManager) this.mContext.getSystemService("display")).unregisterDisplayListener(this.mDisplayListener);
            this.mContext.getContentResolver().unregisterContentObserver(this.mDataEnabledObserver);
        } catch (Exception e) {
            log("unregisterDeviceState" + e.getMessage());
        }
    }

    private void registerPhoneState() {
        log("register call state.");
        ((TelephonyManager) this.mContext.getSystemService("phone")).listen(this.mPhoneStateListener, 32);
    }

    class NetworkSpeedListener implements BaseEvent.IEventListener {
        NetworkSpeedListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.DataEvent) || (bundle = ((TelephonyEvent.DataEvent) baseEvent).getBundle()) == null) {
                return;
            }
            FiveGPowerController.this.obtainMessage(5, !bundle.getBoolean("low_speed", false) ? 1 : 0, -1).sendToTarget();
        }
    }

    private void startNetworkSpeedMonitor() {
        log("start network speed monitor");
        NetworkSpeedMonitor.getInstance().startMonitor(getLooper());
        if (this.mNetworkSpeedListener == null) {
            this.mNetworkSpeedListener = new NetworkSpeedListener();
            EventHubManager.getInstance().registerListener("telephony", "continual_low_network_speed", this.mNetworkSpeedListener);
        }
    }

    private void cancelNetworkSpeedMonitor() {
        log("cancel network speed monitor");
        if (NetworkSpeedMonitor.getInstance() != null) {
            NetworkSpeedMonitor.getInstance().stopMonitor();
        }
        EventHubManager.getInstance().unregisterListener(this.mNetworkSpeedListener);
        this.mNetworkSpeedListener = null;
    }

    private synchronized void changeEnableEndcStatus(final boolean z, final NrSceneConstants$Scene nrSceneConstants$Scene, final int i) {
        if (FiveGUtils.isXmRadioSupported()) {
            int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
            String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
            boolean z2 = !z;
            if (shouldIgnoreNrOptimization(typeByScene, nrSceneConstants$Scene)) {
                log("return, the current version does not support " + nrSceneConstants$Scene + " optimization");
                return;
            }
            localLog("changeEnableEndcStatus", "requestNrOperation featureType=" + typeByScene + "; featureId=" + strValueOf + "; depriNr=" + z2 + "; phoneId=" + i);
            HyperCommManager.getDefault().requestNrOperation(i, strValueOf, typeByScene, z2, new ResultReceiver(this) { // from class: com.android.phone.FiveGPowerController.13
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i2, Bundle bundle) {
                    FiveGPowerController.this.localLog("changeEnableEndcStatus", "requestNrOperation result=" + i2 + "; scene=" + nrSceneConstants$Scene + "; phoneId=" + i);
                    if (i2 == 0) {
                        FiveGPowerController.this.mFiveGOptimizeScene = z ? NrSceneConstants$Scene.UNKNOWN : nrSceneConstants$Scene;
                    }
                }
            });
            return;
        }
        if (this.mInCall) {
            this.mEndcSceneChangedInCall = true;
            log("changeEnableEndcStatus stoped by InCall,scene=" + nrSceneConstants$Scene);
            return;
        }
        localLog("changeEnableEndcStatus", "enabled=" + z + "; scene=" + nrSceneConstants$Scene + "; phoneId=" + i);
        execSaOptimization(z, nrSceneConstants$Scene, i);
        execNsaOptimization(z, nrSceneConstants$Scene, i);
    }

    protected synchronized void changeEnableEndcStatus(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene) {
        try {
            INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
            if (NrSceneConstants$Scene.STREAM_MEDIA_MTK == nrSceneConstants$Scene && z && iNetworkModule.getMIsDdsChange() && MiuiPhoneUtils.getInsertedSimCount() > 1) {
                log("changeEnableEndcStatus force enable sa for stream dds");
                changeEnableEndcStatus(true, nrSceneConstants$Scene, 0);
                changeEnableEndcStatus(true, nrSceneConstants$Scene, 1);
                iNetworkModule.setMIsDdsChange(false);
            } else {
                changeEnableEndcStatus(z, nrSceneConstants$Scene, DefaultSimManager.getDefaultDataSlotId());
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0023  */
    /* JADX WARN: Code duplicated, block: B:60:0x00ec A[Catch: all -> 0x0027, Exception -> 0x00ea, TRY_LEAVE, TryCatch #0 {Exception -> 0x00ea, blocks: (B:55:0x00cf, B:57:0x00d5, B:60:0x00ec), top: B:68:0x00cf, outer: #1 }] */
    private synchronized void execNsaOptimization(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene, int i) {
        try {
            boolean z2 = Build.IS_INTERNATIONAL_BUILD;
            boolean z3 = this.mIsSsrEndcDisabled;
            boolean z4 = true;
            if (z2) {
                if (z3 || this.mIsSilentSaEndcDisabled) {
                    z4 = false;
                } else if (!forceEnableEndcByPolicy()) {
                    if (shouldEnableEndcByPolicy()) {
                        z4 = z;
                    } else {
                        z4 = false;
                    }
                }
            } else if (z3 || this.mIsSilentSaEndcDisabled || ((isSmartNetworkOptimizationSupported() && !this.mSmartNetworkOptimizationRst) || ((MiuiPhoneUtils.isSupportWechatVideoOptimization() && !this.mWechatOptimizationRst) || ((MiuiPhoneUtils.isSupportMtkDataOptimization() && !this.mStreamMediaOptimizationRst) || ((MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() && !this.mGameResidentCellOptimizationRst) || isNetworkOptimizationForNsa()))))) {
                z4 = false;
            }
            localLog("execNsaOptimization", "enableEndc=" + z4 + "; enabled=" + z + "; scene=" + nrSceneConstants$Scene + "; mEndcPowerEnabled[" + i + "]=" + this.mEndcPowerEnabled[i] + "; ssr=" + this.mIsSsrEndcDisabled + "; silentSa=" + this.mIsSilentSaEndcDisabled + "; phoneId=" + i);
            if (this.mEndcPowerEnabled[i] == z4 && nrSceneConstants$Scene != NrSceneConstants$Scene.INIT) {
                log("execNsaOptimization return because the current endc status is the same");
                return;
            }
            if (hasMessages(9)) {
                removeMessages(9);
            }
            if (!z4) {
                try {
                    if (shouldIgnoreNsaOptimization(nrSceneConstants$Scene)) {
                        log("Ignore NSA optimization for scene :" + nrSceneConstants$Scene + " due to DSDA function.");
                    } else {
                        bindServiceAndEnableEndc(z4, i);
                    }
                } catch (Exception e) {
                    log("execNsaOptimization: Exception = " + e);
                }
            } else {
                bindServiceAndEnableEndc(z4, i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void execNsaOptimization(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene) {
        execNsaOptimization(z, nrSceneConstants$Scene, DefaultSimManager.getDefaultDataSlotId());
    }

    private boolean shouldIgnoreNrOptimization(int i, NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (i == 1) {
            return !isSaPowerOptimizationScene(nrSceneConstants$Scene);
        }
        return (i != 2 || Build.IS_INTERNATIONAL_BUILD || nrSceneConstants$Scene == NrSceneConstants$Scene.NSA_SCG_POOR_MODE || nrSceneConstants$Scene == NrSceneConstants$Scene.NSA_NETWORK_RECOVERY_STRATEGY || nrSceneConstants$Scene == NrSceneConstants$Scene.NSA_THERMAL_MODE) ? false : true;
    }

    private boolean shouldIgnoreNsaOptimization(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return (NrSceneConstants$Scene.SCREEN == nrSceneConstants$Scene || NrSceneConstants$Scene.WWAN == nrSceneConstants$Scene || NrSceneConstants$Scene.WLAN == nrSceneConstants$Scene || NrSceneConstants$Scene.WHITE_LIST == nrSceneConstants$Scene || NrSceneConstants$Scene.FIVEG_HOSTED_SPEED_MODE == nrSceneConstants$Scene || NrSceneConstants$Scene.SA_WECHAT_VOIP == nrSceneConstants$Scene || NrSceneConstants$Scene.SLM_WLAN == nrSceneConstants$Scene || NrSceneConstants$Scene.CALL == nrSceneConstants$Scene) && MiuiPhoneUtils.shouldDisalbeNsaOptimization();
    }

    /* JADX WARN: Code duplicated, block: B:60:0x011b  */
    /* JADX WARN: Code duplicated, block: B:62:0x011e A[Catch: all -> 0x0015, Exception -> 0x00e8, TryCatch #1 {Exception -> 0x00e8, blocks: (B:47:0x00cb, B:49:0x00d3, B:52:0x00eb, B:53:0x00ee, B:55:0x00f4, B:58:0x00fc, B:59:0x0100, B:62:0x011e, B:64:0x0124, B:66:0x0130, B:67:0x0134), top: B:75:0x00cb, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:66:0x0130 A[Catch: all -> 0x0015, Exception -> 0x00e8, TryCatch #1 {Exception -> 0x00e8, blocks: (B:47:0x00cb, B:49:0x00d3, B:52:0x00eb, B:53:0x00ee, B:55:0x00f4, B:58:0x00fc, B:59:0x0100, B:62:0x011e, B:64:0x0124, B:66:0x0130, B:67:0x0134), top: B:75:0x00cb, outer: #0 }] */
    private synchronized void execSaOptimization(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene, int i) {
        boolean z2;
        boolean z3;
        if (isSaStabilityScene(nrSceneConstants$Scene) || isSaPowerOptimizationScene(nrSceneConstants$Scene)) {
            boolean z4 = false;
            boolean z5 = FiveGManagerBase.getInstance().getFiveGNetworkMode() == 2;
            if (!this.mIsSsrEndcDisabled && !this.mIsCallFailedSaDisabled[i] && !isSpecialSimSaDisabledForSa(i)) {
                if (forceEnableSaByPolicy()) {
                    z2 = true;
                } else {
                    z2 = !shouldEnableSaByPolicy() ? false : z;
                }
            }
            if (!this.mSaPowerEnabled[i] && MiuiPhoneUtils.is5GSaRegistered(i)) {
                z4 = true;
            }
            localLog("execSaOptimization", "enableSa=" + z2 + "; enabled=" + z + "; scene=" + nrSceneConstants$Scene + "; mSaPowerEnabled[" + i + "]:" + this.mSaPowerEnabled[i] + "; sync=" + z4 + "; isSaModeEnabled = " + z5 + "; phoneId=" + i + "; specialSim=" + isSpecialSimSaDisabledForSa(i));
            if (this.mSaPowerEnabled[i] == z2 && !z4) {
                log("execSaOptimization return because the current sa status is the same");
                return;
            }
            if (!z2) {
                silentDisableSaCounts(nrSceneConstants$Scene);
            }
            if (!z2 && z5) {
                try {
                    this.mIsSilentSaEndcDisabled = true;
                    if (shouldIgnoreNsaOptimization(nrSceneConstants$Scene)) {
                        log("Ignore NSA optimization for scene :" + nrSceneConstants$Scene + " due to DSDA function.");
                    } else {
                        bindServiceAndEnableEndc(z2, i);
                    }
                    enableSaAsync(z2, i, nrSceneConstants$Scene);
                    if (!z2) {
                        z3 = z2;
                        if (!z3) {
                            this.mLastDepriSaTime[i] = System.currentTimeMillis();
                            this.mIsInDeSa[i] = nrSceneConstants$Scene != null ? nrSceneConstants$Scene.getValue() : -2;
                        }
                    } else {
                        z3 = z2;
                        if (!z3) {
                            this.mLastDepriSaTime[i] = System.currentTimeMillis();
                            this.mIsInDeSa[i] = nrSceneConstants$Scene != null ? nrSceneConstants$Scene.getValue() : -2;
                        }
                    }
                } catch (Exception e) {
                    log("execSaOptimization: Exception = " + e);
                }
            } else {
                enableSaAsync(z2, i, nrSceneConstants$Scene);
                if (!z2 && z2 == this.mSaPowerEnabled[i]) {
                    recordDeprioritizeOrRestoreSa(z2, nrSceneConstants$Scene != null ? nrSceneConstants$Scene.getValue() : -2, this.mIsInDeSa[i], i, System.currentTimeMillis() - this.mLastDepriSaTime[i]);
                    this.mIsInDeSa[i] = -1;
                } else {
                    z3 = z2;
                    if (!z3 && z3 == this.mSaPowerEnabled[i]) {
                        this.mLastDepriSaTime[i] = System.currentTimeMillis();
                        this.mIsInDeSa[i] = nrSceneConstants$Scene != null ? nrSceneConstants$Scene.getValue() : -2;
                    }
                }
            }
        }
    }

    protected synchronized void switchRxMode(boolean z) {
        try {
            log("switchRxMode changed = " + z);
            String[] strArr = {"AT+EGMC = 1, \"nr_rx_path\", 1, 1, 3, 3, 3, 3", "+EGMC"};
            String[] strArr2 = {"AT+EGMC = 1, \"nr_rx_path\", 0", "+EGMC"};
            if (!z) {
                strArr = strArr2;
            }
            int i = z ? 2 : 4;
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            if (MiuiTelephony.IS_MTK) {
                FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone, strArr, (Message) null);
            } else if (MiuiTelephony.IS_XRING) {
                HyperRadioManager.getInstance(this.mContext).switchRxMode(activeDataPhone.getPhoneId(), i, (Executor) null, (HyperRadioManager.GenericResponse) null);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void updateThrottleLevel(final int i) {
        try {
            log("updateThrottleLevel throttleLevel = " + i);
            if (i != 0) {
                if (i == 8) {
                    this.mThermalThrottleDL1Count++;
                    i = 1;
                } else {
                    if (i != 9) {
                        return;
                    }
                    this.mThermalThrottleDL2Count++;
                    i = 2;
                }
            }
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            String str = "";
            if (foregroundInfo != null) {
                str = foregroundInfo.mForegroundPackageName;
            }
            sThermalThrottleFGAppMap.put(str, Integer.valueOf((sThermalThrottleFGAppMap.containsKey(str) ? ((Integer) sThermalThrottleFGAppMap.get(str)).intValue() : 1) + 1));
            sThermalThrottleSASnrMap.put(Integer.valueOf(this.mSASnr), Integer.valueOf((sThermalThrottleSASnrMap.containsKey(Integer.valueOf(this.mSASnr)) ? ((Integer) sThermalThrottleSASnrMap.get(Integer.valueOf(this.mSASnr))).intValue() : 1) + 1));
            HyperRadioManager.getInstance(this.mContext).setThermalState(1, i, new Executor() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda1
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGPowerController.14
                public void onResponse(int i2) {
                    FiveGPowerController.log("setThermalState thermalState=" + i + " errorCode=" + i2);
                }
            });
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void updateULThrottleState(int i) {
    }

    protected synchronized void reduceMaxTxPower(int i) {
    }

    protected synchronized void execSmartIdle(int i) {
    }

    public synchronized void reduceIdleNeighborMea(boolean z, int i) {
    }

    public synchronized void limitNwCA(boolean z, int i) {
    }

    private boolean isSaPowerSensitiveScene() {
        boolean z = this.mIsGameForgroud || this.mWhiteListEnabled || this.mInCall;
        localLog("execSaOptimization", "Sensitive scene:" + z + ", game:" + this.mIsGameForgroud + ", whitelist:" + this.mWhiteListEnabled + ", incall:" + this.mInCall);
        return z;
    }

    private boolean isSaPowerOptimizationScene(NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (!this.mSaPowerOptimizationSupported) {
            return false;
        }
        if (Build.IS_INTERNATIONAL_BUILD) {
            return isInternationalSceneEnabled(nrSceneConstants$Scene);
        }
        return isSaPowerOptimizationSceneBasedCdrxEnabled(nrSceneConstants$Scene) || isSaPowerBaseSceneEnabled(nrSceneConstants$Scene) || NrSceneConstants$Scene.INIT == nrSceneConstants$Scene || isSaPowerSceneEnabled(nrSceneConstants$Scene) || isMtkWlanDisSaSceneEnabled(nrSceneConstants$Scene) || NrSceneConstants$Scene.SMART_NETWORK == nrSceneConstants$Scene || NrSceneConstants$Scene.NETWORK_RECOVERY_MODE == nrSceneConstants$Scene || NrSceneConstants$Scene.SMART_TIERED_POWER_SAVING == nrSceneConstants$Scene || NrSceneConstants$Scene.IRAT_PINGPONG_MODE == nrSceneConstants$Scene || NrSceneConstants$Scene.WECHAT_VIDEO_MTK == nrSceneConstants$Scene || NrSceneConstants$Scene.STREAM_MEDIA_MTK == nrSceneConstants$Scene || NrSceneConstants$Scene.GAME_RESIDENT_CELL == nrSceneConstants$Scene || NrSceneConstants$Scene.SPECIAL_SIM == nrSceneConstants$Scene;
    }

    private boolean isSaPowerOptimizationSceneBasedCdrxEnabled(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return ((NrSceneConstants$Scene.SCREEN == nrSceneConstants$Scene && !isSaPowerSensitiveScene()) || ((isSaPowerWwanEnabled() && NrSceneConstants$Scene.WWAN == nrSceneConstants$Scene) || (isSaPowerWlanEnabled() && NrSceneConstants$Scene.WLAN == nrSceneConstants$Scene))) && isSaPowerCdrxEnabled();
    }

    private boolean isSaPowerBaseSceneEnabled(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return isSaPowerSpeedTestEnabled() && NrSceneConstants$Scene.SPEED_TEST == nrSceneConstants$Scene;
    }

    private boolean isSaStabilityScene(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return NrSceneConstants$Scene.CALL == nrSceneConstants$Scene || NrSceneConstants$Scene.SSR == nrSceneConstants$Scene;
    }

    private boolean isSaPowerSceneEnabled(NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (isSaPowerWechatVoipEnabled() && NrSceneConstants$Scene.SA_WECHAT_VOIP == nrSceneConstants$Scene) {
            return true;
        }
        if (isSaPowerNightModeEnabled() && NrSceneConstants$Scene.SA_NIGHT_MODE == nrSceneConstants$Scene) {
            return true;
        }
        return isSaPowerThermalModeEnabled() && NrSceneConstants$Scene.SA_THERMAL_MODE == nrSceneConstants$Scene;
    }

    private boolean isMtkWlanDisSaSceneEnabled(NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (MiuiPhoneUtils.isWlanDisSASupportedForMtk()) {
            return NrSceneConstants$Scene.INIT == nrSceneConstants$Scene || NrSceneConstants$Scene.WLAN == nrSceneConstants$Scene;
        }
        return false;
    }

    /* JADX INFO: renamed from: com.android.phone.FiveGPowerController$23, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass23 {
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$utils$NrSceneConstants$Scene;

        static {
            int[] iArr = new int[NrSceneConstants$Scene.values().length];
            $SwitchMap$com$android$phone$utils$NrSceneConstants$Scene = iArr;
            try {
                iArr[NrSceneConstants$Scene.SA_THERMAL_MODE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NrSceneConstants$Scene[NrSceneConstants$Scene.SA_NIGHT_MODE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private boolean isInternationalSceneEnabled(NrSceneConstants$Scene nrSceneConstants$Scene) {
        int i = AnonymousClass23.$SwitchMap$com$android$phone$utils$NrSceneConstants$Scene[nrSceneConstants$Scene.ordinal()];
        if (i == 1) {
            return HyperOSCustFeatureResolve.getBoolean("is_international_sa_thermal_mode_enabled", false);
        }
        if (i != 2) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("is_international_sa_night_mode_enabled", false);
    }

    protected synchronized void bindServiceAndEnableEndc(boolean z, int i) {
        try {
            if (z) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    Phone phone = MiuiPhoneUtils.getPhone(i2);
                    if (phone != null) {
                        log("bindServiceAndEnableEndc enableEndc: " + z + ", phone = " + i2);
                        phone.setNrDualConnectivityState(z ? 1 : 0, obtainMessage(63, i2, 1), (WorkSource) null);
                    }
                }
            } else {
                Phone phone2 = MiuiPhoneUtils.getPhone(i);
                if (phone2 != null) {
                    log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i);
                    phone2.setNrDualConnectivityState(z ? 1 : 0, obtainMessage(63, i, 0), (WorkSource) null);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void bindServiceAndEnableEndc(boolean z) {
        bindServiceAndEnableEndc(z, DefaultSimManager.getDefaultDataSlotId());
    }

    protected synchronized void enableSaAsync(boolean z, int i, NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled()) {
            log("enableSaAsync return because inCall");
            return;
        }
        NrSceneConstants$Scene nrSceneConstants$Scene2 = this.mLastOptScene[i];
        if (nrSceneConstants$Scene2 != null && nrSceneConstants$Scene2 != NrSceneConstants$Scene.UNKNOWN && nrSceneConstants$Scene2 != nrSceneConstants$Scene) {
            int[] iArr = this.mSwitchSceneCount;
            iArr[i] = iArr[i] + 1;
        }
        if (z) {
            this.mIsSilentSaEndcDisabled = false;
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                deprioritizeSaAsync(z, i2);
                this.mSaPowerEnabled[i2] = z;
            }
            this.mFiveGOptimizeScene = NrSceneConstants$Scene.UNKNOWN;
        } else {
            if (FiveGManagerBase.getInstance().getFiveGNetworkMode() == 2) {
                this.mIsSilentSaEndcDisabled = true;
            }
            deprioritizeSaAsync(z, i);
            this.mSaPowerEnabled[i] = z;
            this.mFiveGOptimizeScene = nrSceneConstants$Scene;
        }
        this.mLastOptScene[i] = nrSceneConstants$Scene;
    }

    protected synchronized void enableSaAsync(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene) {
        enableSaAsync(z, DefaultSimManager.getDefaultDataSlotId(), nrSceneConstants$Scene);
    }

    protected synchronized void deprioritizeSaAsync(boolean z, int i) {
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled()) {
            log("deprioritizeSaAsync return because inCall");
            return;
        }
        if (this.mSaPowerEnabled[i] != z) {
            HyperRadioManager.getInstance(this.mContext).deprioritizeSa(i, z, (Executor) null, (HyperRadioManager.GenericResponse) null);
            log("enableSaAsync enableSa: " + z + " phone" + i);
        }
    }

    protected synchronized void setDatConfig(boolean z, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataEnabled() {
        return PhoneFactory.getPhone(DefaultSimManager.getDefaultDataSlotId()).isUserDataEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acquireWakeLock() {
        PowerManager.WakeLock wakeLock;
        if (!Build.IS_INTERNATIONAL_BUILD || (wakeLock = this.mWakeLock) == null || wakeLock.isHeld()) {
            return;
        }
        log("acquire WakeLock.");
        this.mWakeLock.acquire();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock;
        if (Build.IS_INTERNATIONAL_BUILD && (wakeLock = this.mWakeLock) != null && wakeLock.isHeld()) {
            log("release WakeLock.");
            this.mWakeLock.release();
        }
    }

    private void resetEndcStatus() {
        log("reset endc to true");
        changeEnableEndcStatus(true, NrSceneConstants$Scene.INIT);
    }

    public boolean forceEnableEndcByPolicy() {
        boolean zIsFivegModeEnabled = isFivegModeEnabled();
        this.misFivegModeEnabled = zIsFivegModeEnabled;
        boolean z = zIsFivegModeEnabled && (this.mIsTestApp || !(this.mPowerOptimizationEnabled || isNetworkOptimizationForNsa() || isNsaThermalOptimization()));
        localLog("forceEnableEndcByPolicy", "rst[" + z + "], misFivegModeEnabled[" + this.misFivegModeEnabled + "], mIsTestApp[" + this.mIsTestApp + "]], isNetworkOptimization[" + isNetworkOptimizationForNsa() + "], mPowerOptimizationEnabled[" + this.mPowerOptimizationEnabled + "]");
        return z;
    }

    public boolean shouldEnableEndcByPolicy() {
        boolean zIsMobileDataEnabled = isMobileDataEnabled();
        boolean zIsScreenOn = isScreenOn();
        this.misFivegModeEnabled = isFivegModeEnabled();
        boolean zIsNetworkOptimizationForNsa = isNetworkOptimizationForNsa();
        boolean z = false;
        boolean z2 = !this.mPowerOptimizationEnabled || (zIsMobileDataEnabled && zIsScreenOn && wifiCompositeStatus() && !this.mIsNsaScgPoorStatusFlag);
        boolean z3 = (this.mFiveGHostedModeEnabled || isSaPowerWechatVoipEnabled()) ? !this.mIsWechatVoipPreferForLte : true;
        if (this.misFivegModeEnabled && z2 && z3 && this.mGameRst && !zIsNetworkOptimizationForNsa && !this.mNsaThermalFlag) {
            z = true;
        }
        localLog("shouldEnableEndc", "rst[" + z + "], mPowerOptimizationEnabled[" + this.mPowerOptimizationEnabled + "], mFiveGHostedModeEnabled[" + this.mFiveGHostedModeEnabled + "], deviceRst[" + z2 + "], appRst[" + z3 + "], dataEnable[" + zIsMobileDataEnabled + "], screenOn[" + zIsScreenOn + "], wifi[" + this.mIsWifiConnected + "], coexistence[" + this.mCoexistenceStatus + "], misFivegModeEnabled[" + this.misFivegModeEnabled + "], mGameRst[" + this.mGameRst + "], isNetworkOptimization[" + zIsNetworkOptimizationForNsa + "], mNsaThermalFlag[" + this.mNsaThermalFlag + "], mIsWechatVoipPreferForLte[" + this.mIsWechatVoipPreferForLte + "]");
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:58:0x008d  */
    /* JADX WARN: Code duplicated, block: B:63:0x0095  */
    public boolean forceEnableSaByPolicy() {
        boolean z;
        boolean z2;
        this.misFivegModeEnabled = isFivegModeEnabled();
        this.misFivegSaEnabled = isFiveGSaModeEnabled();
        boolean z3 = this.mIsTestApp;
        CdrxState cdrxConfigStatus = getCdrxConfigStatus();
        boolean z4 = false;
        if (this.mSaPowerOptimizationEnabled) {
            if (isSaPowerCdrxEnabled()) {
                z2 = cdrxConfigStatus != CdrxState.Disabled;
                if (isSaPowerWechatVoipEnabled()) {
                    z2 = z2 && !this.mIsWechatVoipForgroud;
                }
                if (isSaPowerNightModeEnabled()) {
                    z2 = z2 && !this.mIsNightMode;
                }
                if (isSaPowerThermalModeEnabled()) {
                    z2 = z2 && !this.mSaThermalFlag;
                }
                if (isSmartNetworkOptimizationSupported()) {
                    z2 = z2 && this.mSmartNetworkOptimizationRst;
                }
                if (MiuiPhoneUtils.isSupportWechatVideoOptimization()) {
                    z2 = z2 && this.mWechatOptimizationRst;
                }
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    z2 = z2 && this.mStreamMediaOptimizationRst;
                }
                if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
                    if (z2 && this.mGameResidentCellOptimizationRst) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                }
            } else {
                z2 = false;
            }
            if (z3 || z2) {
                z = true;
            } else {
                z = false;
            }
        } else {
            z = true;
        }
        if (this.misFivegModeEnabled && this.misFivegSaEnabled && z) {
            z4 = true;
        }
        boolean z5 = this.mSaPowerOptimizationEnabled ? z4 : true;
        localLog("forceEnableSaByPolicy", "rst[" + z5 + "], mSaPowerScene[" + this.mSaPowerScene + "], mSaPowerOptimizationEnabled[" + this.mSaPowerOptimizationEnabled + "], misFivegModeEnabled[" + this.misFivegModeEnabled + "], misFivegSaEnabled[" + this.misFivegSaEnabled + "], mIsTestApp[" + this.mIsTestApp + "], mIsWechatVoipForgroud[" + this.mIsWechatVoipForgroud + "], mIsNightMode[" + this.mIsNightMode + "], cdrxStatus[" + cdrxConfigStatus + "]");
        return z5;
    }

    public boolean shouldEnableSaByPolicy() {
        boolean z;
        boolean z2;
        boolean zIsMobileDataEnabled = isMobileDataEnabled();
        boolean zIsScreenOn = isScreenOn();
        boolean zIsNetworkOptimizationForSa = isNetworkOptimizationForSa();
        boolean zIsSmsOptimizationForSa = isSmsOptimizationForSa();
        CdrxState cdrxConfigStatus = getCdrxConfigStatus();
        this.misFivegModeEnabled = isFivegModeEnabled();
        this.misFivegSaEnabled = isFiveGSaModeEnabled();
        boolean z3 = false;
        if (this.mSaPowerOptimizationEnabled) {
            z = (isSaPowerCdrxEnabled() && cdrxConfigStatus == CdrxState.Disabled) ? false : true;
            if (isSaPowerNightModeEnabled()) {
                z = z && !this.mIsNightMode;
            }
            z2 = (!isSaPowerWechatVoipEnabled() || (MiuiPhoneUtils.isSupportWechatVideoOptimization() && ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isProcessedWechatVideoEnter())) ? true : !this.mIsWechatVoipForgroud;
            if (isSaPowerThermalModeEnabled()) {
                z = z && !this.mSaThermalFlag;
            }
            if (isSmartNetworkOptimizationSupported()) {
                z = z && this.mSmartNetworkOptimizationRst;
            }
            if (MiuiPhoneUtils.isSupportWechatVideoOptimization()) {
                z = z && this.mWechatOptimizationRst;
            }
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                z = z && this.mStreamMediaOptimizationRst;
            }
            if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
                z = z && this.mGameResidentCellOptimizationRst;
            }
        } else {
            z = true;
            z2 = true;
        }
        if (z && this.misFivegModeEnabled && this.misFivegSaEnabled && z2 && !zIsNetworkOptimizationForSa && !zIsSmsOptimizationForSa) {
            z3 = true;
        }
        StringBuilder sb = new StringBuilder("rst[");
        sb.append(!z3);
        sb.append("], deviceRst[");
        sb.append(z);
        sb.append("], appRst[");
        sb.append(z2);
        sb.append("], dataEnable[");
        sb.append(zIsMobileDataEnabled);
        sb.append("], screenOn[");
        sb.append(zIsScreenOn);
        sb.append("], wifi[");
        sb.append(this.mIsWifiConnected);
        sb.append("], coexistence[");
        sb.append(this.mCoexistenceStatus);
        sb.append("], mPowerOptimizationEnabled[");
        sb.append(this.mPowerOptimizationEnabled);
        sb.append("], misFivegModeEnabled[");
        sb.append(this.misFivegModeEnabled);
        sb.append("], misFivegSaEnabled[");
        sb.append(this.misFivegSaEnabled);
        sb.append("], mIsWechatVoipForgroud[");
        sb.append(this.mIsWechatVoipForgroud);
        sb.append("], mIsNightMode[");
        sb.append(this.mIsNightMode);
        sb.append("], mSaThermalFlag[");
        sb.append(this.mSaThermalFlag);
        sb.append("], isNetworkOptimization[");
        sb.append(zIsNetworkOptimizationForSa);
        sb.append("], isSMSOptimization[");
        sb.append(zIsSmsOptimizationForSa);
        sb.append("], cdrxStatus[");
        sb.append(cdrxConfigStatus);
        sb.append("]");
        localLog("shouldDisableSaByPolicy", sb.toString());
        return z3;
    }

    public CdrxState getCdrxConfigStatus() {
        CdrxState cdrxState = CdrxState.Unknown;
        CdrxConfig cdrxConfig = this.mCdrxConfig;
        return cdrxConfig == null ? cdrxState : cdrxConfig.getStatus();
    }

    public String getSilentDisableSaCountForStatis() {
        return "CDRX: " + getCdrxSilentDisableSaCount() + "; NIGHT MODE: " + getNightModeSilentDisableSaCount() + "; WECHAT MODE: " + getWechatSilentDisableSaCount() + "; THERMAL MODE: " + getThermalSilentDisableSaCount();
    }

    public long getCdrxSilentDisableSaCount() {
        long j = this.mCdrxSceneDisableSaCount;
        this.mCdrxSceneDisableSaCount = 0L;
        return j;
    }

    public long getWechatSilentDisableSaCount() {
        long j = this.mWechatSceneDisableSaCount;
        this.mWechatSceneDisableSaCount = 0L;
        return j;
    }

    public long getNightModeSilentDisableSaCount() {
        long j = this.mNightModeSceneDisableSaCount;
        this.mNightModeSceneDisableSaCount = 0L;
        return j;
    }

    public long getThermalSilentDisableSaCount() {
        long j = this.mThermalModeSceneDisableSaCount;
        this.mThermalModeSceneDisableSaCount = 0L;
        return j;
    }

    public Long getMimoSceneChangeRxCount() {
        long j = this.mMimoSceneChangeRxCount;
        this.mMimoSceneChangeRxCount = 0L;
        return Long.valueOf(j);
    }

    public long getModemSSRCausedByN28Count() {
        long j = this.mModemSSRCausedByN28Count;
        this.mModemSSRCausedByN28Count = 0L;
        return j;
    }

    public long getModemSSRCausedByBwpCount() {
        long j = this.mModemSSRCausedByBwpCount;
        this.mModemSSRCausedByBwpCount = 0L;
        return j;
    }

    public long getModemSSRCausedByN1Count() {
        long j = this.mModemSSRCausedByN1Count;
        this.mModemSSRCausedByN1Count = 0L;
        return j;
    }

    private void addCountCausedModemCrash(String str) {
        if (str.contains("n28")) {
            this.mModemSSRCausedByN28Count++;
        } else if (str.contains("bwp")) {
            this.mModemSSRCausedByBwpCount++;
        } else if (str.contains("n1")) {
            this.mModemSSRCausedByN1Count++;
        }
    }

    public void enableSaPowerOptimization(boolean z) {
        if (this.mSaPowerOptimizationSupported || !z) {
            log("enableSaPowerOptimization " + z);
            if (!z) {
                if (!this.mIsSsrEndcDisabled) {
                    enableSaAsync(true, NrSceneConstants$Scene.INIT);
                }
                this.mIsSilentSaEndcDisabled = false;
                clearCdrxConfigCache("SA power optimization disabled");
            }
            this.mSaPowerOptimizationEnabled = z;
        }
    }

    private boolean wifiCompositeStatus() {
        return !this.mIsWifiConnected || (this.mIsWifiConnected && this.mCoexistenceStatus == 1);
    }

    private void registerModemRestart() {
        if (MiuiPhoneUtils.isDeviceSupportModemRestartDisableEndc()) {
            PhoneFactory.getPhone(0).mCi.registerForModemReset(this, 100, (Object) null);
        }
    }

    private void handleModemFeatureCrash(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String modemCrashByFeatureValue = "";
        for (String str2 : str.split(",")) {
            if (reasonToResult(str2) != -1) {
                boolean z = MiuiPhoneUtils.getPlatformModemFeatureStatus(reasonToResult(str2), DefaultSimManager.getDefaultDataSlotId()) == 1;
                log("reason = " + str2 + ", isSupportFeature = " + z);
                if (z) {
                    if (TextUtils.isEmpty(modemCrashByFeatureValue)) {
                        modemCrashByFeatureValue = getModemCrashByFeatureValue();
                    }
                    log("modemCrashFileValue = " + modemCrashByFeatureValue);
                    if (modemCrashByFeatureValue != null && modemCrashByFeatureValue.toLowerCase().contains(str2)) {
                        addCountCausedModemCrash(str2);
                        Message messageObtainMessage = obtainMessage(32);
                        messageObtainMessage.obj = str2;
                        sendMessage(messageObtainMessage);
                        return;
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void onSlmStatusChanged(Intent intent) {
        this.mCoexistenceStatus = intent.getIntExtra("coexistense", -1);
        log("SLM mCoexistenceStatus is " + this.mCoexistenceStatus + ", force enable ENDC");
        int i = this.mCoexistenceStatus;
        if (i == 1) {
            changeEnableEndcStatus(true, NrSceneConstants$Scene.SLM_WLAN);
        } else if (i == 0) {
            sendMessageDelayed(obtainMessage(9), 4000L);
        }
    }

    private void onGameStatusChanged(Intent intent) {
        if (MiuiPhoneUtils.isSupportSmartNetworkGameOptimization()) {
            return;
        }
        boolean booleanExtra = intent.getBooleanExtra("game_status", false);
        boolean booleanExtra2 = intent.getBooleanExtra("smart_five_g_status", false);
        log("gameStatus[" + booleanExtra + "], smart5GStatus[" + booleanExtra2 + "], mGameRst[" + this.mGameRst + "]");
        boolean z = booleanExtra && booleanExtra2;
        this.mIsInGame = z;
        if (z) {
            if (switchLteIfNeedForGame()) {
                this.mGameRst = false;
                changeEnableEndcStatus(false, NrSceneConstants$Scene.GAME);
                return;
            }
            return;
        }
        log("delay update endc");
        this.mGameRst = true;
        sendMessageDelayed(obtainMessage(9), 4000L);
    }

    private void onCdrxConfigChanged(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("BROADCAST_CONTEXT");
        if (byteArrayExtra == null) {
            log("onCdrxConfigChanged: Invalid extra!");
            return;
        }
        if (!isCdrxConfigValid(byteArrayExtra)) {
            log("onCdrxConfigChanged: Invalid format!");
            return;
        }
        CdrxConfig cdrxConfig = new CdrxConfig(byteArrayExtra);
        removeMessages(19);
        localLog("SaCDRX", "Received new=" + cdrxConfig + ", old=" + this.mModemCdrxConfig);
        CdrxConfig cdrxConfig2 = this.mModemCdrxConfig;
        if (cdrxConfig2 == null || !cdrxConfig.equals(cdrxConfig2)) {
            log("CDRX changed");
            this.mPendingModemCdrxConfig = cdrxConfig;
            if (hasMessages(20)) {
                removeMessages(20);
                log("CDRX restart timer");
            } else {
                log("CDRX start timer");
            }
            sendEmptyMessageDelayed(20, CDRX_TOO_FREQUENCY_DELAY_TIME);
            return;
        }
        log("CDRX no change");
        if (hasMessages(20)) {
            removeMessages(20);
            log("CDRX cancel timer");
        }
    }

    private boolean isCdrxConfigValid(byte[] bArr) {
        return bArr.length == CdrxConfig.CDRX_CONFIG_LEN;
    }

    public void clearCdrxConfigCache(String str) {
        if (this.mSaPowerOptimizationEnabled) {
            localLog("SaCDRX", "clearCdrxConfigCache reason:" + str);
            this.mCdrxConfig = null;
            this.mModemCdrxConfig = null;
            this.mPendingModemCdrxConfig = null;
            removeMessages(19);
            removeMessages(21);
            removeMessages(20);
        }
    }

    private boolean switchLteIfNeedForGame() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        int nRRsrp = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getNRRsrp(defaultDataSlotId);
        log("fivegRsrp:" + nRRsrp);
        return nRRsrp < -102 || SignalUtils.isLteSignalMoreModerate(defaultDataSlotId);
    }

    private boolean switchLteIfNeed() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        int nRRsrp = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getNRRsrp(defaultDataSlotId);
        log("fivegRsrp:" + nRRsrp);
        return nRRsrp < -102 && SignalUtils.isLteSignalMoreModerate(defaultDataSlotId);
    }

    private void changeModemSsrFeatureStatus(boolean z) {
        changeModemSsrFeatureStatus("", z);
    }

    private void changeModemSsrFeatureStatus(String str, boolean z) {
        log("changeModemSsrFeatureStatus dds = " + DefaultSimManager.getDefaultDataSlotId() + ", reason = " + str + ", enable = " + z);
        String str2 = SystemProperties.get("persist.radio.modem_crash_feature", "");
        if (z) {
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            boolean z2 = false;
            for (String str3 : str2.split(",")) {
                int iReasonToResult = reasonToResult(str3);
                if (iReasonToResult != -1) {
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        FiveGManagerBase.getInstance().setModemFeatureBySlot(new FiveGManagerBase.ModemFeatureStatus(i, iReasonToResult, z ? 1 : 0));
                    }
                    z2 = true;
                }
            }
            if (z2) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    FiveGManagerBase.getInstance().needRefreshMbn(i2);
                }
            }
            str = "";
        } else {
            int iReasonToResult2 = reasonToResult(str);
            if (iReasonToResult2 == -1) {
                return;
            }
            FiveGManagerBase.getInstance().setModemFeatureBySlot(new FiveGManagerBase.ModemFeatureStatus(DefaultSimManager.getDefaultDataSlotId(), iReasonToResult2, z ? 1 : 0));
            FiveGManagerBase.getInstance().needRefreshMbn(DefaultSimManager.getDefaultDataSlotId());
            if (!TextUtils.isEmpty(str2)) {
                str = str2.contains(str) ? str2 : str2 + "," + str;
            }
        }
        log("reasonProp  = " + str);
        SystemProperties.set("persist.radio.modem_crash_feature", str);
    }

    private int reasonToResult(String str) {
        if (str.contains("n28")) {
            return 1;
        }
        if (str.contains("bwp")) {
            return 2;
        }
        if (str.contains("n1")) {
            return 3;
        }
        log("reasonToResult no match");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCallFailedSaDisabled() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mIsCallFailedSaDisabled[i]) {
                return true;
            }
        }
        return false;
    }

    private void registerAirplaneState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.FiveGPowerController.15
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                FiveGPowerController fiveGPowerController;
                if (intent != null && "android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                    boolean z = Settings.Global.getInt(FiveGPowerController.this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
                    if (!z && (FiveGPowerController.this.mIsSsrEndcDisabled || FiveGPowerController.this.isCallFailedSaDisabled())) {
                        FiveGPowerController.this.localLog("setEndcEnabled", "Modem ssr, airplane is off, enable endc");
                        int i = 0;
                        while (true) {
                            int i2 = MiuiPhoneUtils.PHONE_COUNT;
                            fiveGPowerController = FiveGPowerController.this;
                            if (i >= i2) {
                                break;
                            }
                            fiveGPowerController.mIsCallFailedSaDisabled[i] = false;
                            fiveGPowerController.mCallFailedCounts[i] = 0;
                            i++;
                        }
                        fiveGPowerController.mIsSsrEndcDisabled = false;
                        FiveGPowerController.this.obtainMessage(6).sendToTarget();
                        FiveGPowerController.this.enableFiveGPowerOptimization();
                    }
                    if (z && FiveGPowerController.this.mIsNsaScgPoorStatusFlag) {
                        FiveGPowerController.log("reset NSA SCG poor status flag for airplane mode off");
                        FiveGPowerController.this.resetNsascgPoorOptimization();
                    }
                    if (z) {
                        FiveGPowerController fiveGPowerController2 = FiveGPowerController.this;
                        if (fiveGPowerController2.mHasPowerSaveOpt) {
                            fiveGPowerController2.resetForPowerSaveOff(5);
                        }
                    }
                }
            }
        }, intentFilter, 2);
    }

    private void registerTieredPowerSavingListener() {
        this.mTieredPowerSavingListener = new TieredPowerSavingListener();
        EventHubManager.getInstance().registerListener("telephony", "smart_power_saving_state_changed", this.mTieredPowerSavingListener);
    }

    class TieredPowerSavingListener implements BaseEvent.IEventListener {
        TieredPowerSavingListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.PowerEvent) || (bundle = ((TelephonyEvent.PowerEvent) baseEvent).getBundle()) == null) {
                return;
            }
            int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID);
            boolean z = bundle.getBoolean("power_save_mode");
            FiveGPowerController.log("onApplyStrategyL1 enabled=" + z + ", phoneId=" + i);
            Message messageObtainMessage = FiveGPowerController.this.obtainMessage(44);
            messageObtainMessage.arg1 = !z ? 1 : 0;
            messageObtainMessage.arg2 = i;
            FiveGPowerController.this.sendMessage(messageObtainMessage);
        }
    }

    private void registerIratPingpongListener() {
        this.mIratPingpongListener = new TieredIratPingpongListener();
        EventHubManager.getInstance().registerListener("telephony", "nr_2_lte_ping_pong_info_changed", this.mIratPingpongListener);
    }

    class TieredIratPingpongListener implements BaseEvent.IEventListener {
        TieredIratPingpongListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.NetworkEvent) || (bundle = ((TelephonyEvent.NetworkEvent) baseEvent).getBundle()) == null) {
                return;
            }
            boolean z = bundle.getBoolean("ping_pong_opt", false);
            int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
            FiveGPowerController.log("doPingpongOptimization enabled=" + z + ", phoneId=" + i);
            Message messageObtainMessage = FiveGPowerController.this.obtainMessage(45);
            messageObtainMessage.arg1 = !z ? 1 : 0;
            messageObtainMessage.arg2 = i;
            FiveGPowerController.this.sendMessage(messageObtainMessage);
        }
    }

    private void registerRatSwitchEngineListener() {
        log("registerMiuiNetworkRecoveryListener mFiveGHostedModeEnabled=" + this.mFiveGHostedModeEnabled);
        if (this.mFiveGHostedModeEnabled) {
            this.mMiuiNetworkRecoveryListener = new MiuiNetworkRecoveryListener();
            EventHubManager.getInstance().registerListener("telephony", "enable_sa_state_changed", this.mMiuiNetworkRecoveryListener);
        }
    }

    private synchronized void unregisterRatSwitchEngineListener() {
        log("unregisterRatSwitchEngineListener");
        if (this.mMiuiNetworkRecoveryListener != null) {
            EventHubManager.getInstance().unregisterListener(this.mMiuiNetworkRecoveryListener);
            this.mMiuiNetworkRecoveryListener = null;
        }
    }

    class MiuiNetworkRecoveryListener implements BaseEvent.IEventListener {
        MiuiNetworkRecoveryListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.NetworkEvent) {
                Bundle bundle = ((TelephonyEvent.NetworkEvent) baseEvent).getBundle();
                if (bundle == null) {
                    FiveGPowerController.log("onMiuiNetworkRecoveryEnableSA bundle is null");
                    return;
                }
                int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
                boolean z = bundle.getBoolean("sa_status", false);
                FiveGPowerController.log("onMiuiNetworkRecoveryEnableSA enableSA=" + z + ", phoneId=" + i);
                Message messageObtainMessage = FiveGPowerController.this.obtainMessage(43);
                messageObtainMessage.arg1 = z ? 1 : 0;
                messageObtainMessage.arg2 = i;
                FiveGPowerController.this.sendMessage(messageObtainMessage);
            }
        }
    }

    private void registerFiveGHostedModeListener() {
        this.mContext.getContentResolver().registerContentObserver(getFiveGHostedModeUri(), false, this.mFiveGHostedModeEnabledObserver);
    }

    private Uri getFiveGHostedModeUri() {
        return Settings.Global.getUriFor("fiveg_hosted_mode_user_enable");
    }

    private boolean isUserFiveGHostedModeEnabled() {
        boolean zIsFivegHostedModeIndiaDefaultOff = MiuiPhoneUtils.isFivegHostedModeIndiaDefaultOff();
        Context context = this.mContext;
        return 1 == (zIsFivegHostedModeIndiaDefaultOff ? Settings.Global.getInt(context.getContentResolver(), "fiveg_hosted_mode_user_enable", 0) : Settings.Global.getInt(context.getContentResolver(), "fiveg_hosted_mode_user_enable", 1));
    }

    public boolean getFiveGHostedModeStatus() {
        return this.mFiveGHostedModeEnabled;
    }

    private int initSaPowerScene() {
        return (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? initMtkPlatformScene() : initQcomPlatformScene();
    }

    private boolean isSaPowerWwanEnabled() {
        return 32 == (this.mSaPowerScene & 32);
    }

    private boolean isSaPowerWlanEnabled() {
        return 64 == (this.mSaPowerScene & 64);
    }

    private boolean isSaPowerSpeedTestEnabled() {
        return 128 == (this.mSaPowerScene & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
    }

    private boolean isSaPowerCdrxEnabled() {
        return !Build.IS_STABLE_VERSION && 1 == (this.mSaPowerScene & 1);
    }

    private boolean isSaPowerWechatVoipEnabled() {
        return 2 == (this.mSaPowerScene & 2) && MiuiPhoneUtils.isSaPowerWechatVoipSupported();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSaPowerNightModeEnabled() {
        return 4 == (this.mSaPowerScene & 4) && MiuiPhoneUtils.shouldDeprioSaInNightMode();
    }

    private boolean isSaPowerThermalModeEnabled() {
        return 8 == (this.mSaPowerScene & 8) && MiuiPhoneUtils.isSaThermalControlSupported() && !TelephonyCloudController.CLOUD_SA_POWER_SCENE_THERMAL_MODE_DISABLED && !isHstDisableSaPowerModeScene();
    }

    private boolean isHstDisableSaPowerModeScene() {
        boolean z = false;
        boolean z2 = HyperOSCustFeatureResolve.getBoolean("config_is_hst_disable_sa_power_opt_supported", false);
        if (mIsCurrentHstState && (MiuiPhoneUtils.isPlatformDE90() || z2)) {
            z = true;
        }
        localLog("hstDisableSaPowerModeScene", "rst=" + z + ", isConfigSupported=" + z2);
        return z;
    }

    private boolean isSmartNetworkOptimizationSupported() {
        return MiuiPhoneUtils.isSupportSmartNetworkGameOptimization();
    }

    private void onSpecialAppWhiteListChanged(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("wechatVoipEnabled", false);
        boolean z = FiveGManagerBase.getInstance().getFiveGNetworkMode() == 2;
        log("wechat is " + booleanExtra + ", isSaEnabled is" + z);
        if (z && isSaPowerWechatVoipEnabled()) {
            onSpecialAppWhiteListChangedForSaPower(booleanExtra);
        } else if (this.mIsFiveGHostedModeSupported && this.mFiveGHostedModeEnabled) {
            onSpecialAppWhiteListChangedForFiveGHost(booleanExtra);
        }
    }

    private void onSpecialAppWhiteListChangedForSaPower(boolean z) {
        if (z) {
            this.mIsWechatVoipPreferForLteBySa = true;
            return;
        }
        removeMessages(26);
        this.mIsWechatVoipPreferForLteBySa = false;
        changeEnableEndcStatus(true, NrSceneConstants$Scene.SA_WECHAT_VOIP);
    }

    private void onSpecialAppWhiteListChangedForFiveGHost(boolean z) {
        if (z) {
            boolean zSwitchLteIfNeed = switchLteIfNeed();
            log("onSpecialAppWhiteListChangedForFiveGHost switchToLTE=" + zSwitchLteIfNeed);
            if (zSwitchLteIfNeed) {
                this.mIsWechatVoipPreferForLte = true;
                changeEnableEndcStatus(false, NrSceneConstants$Scene.WHITE_LIST);
                return;
            }
            return;
        }
        log("delay update endc");
        this.mIsWechatVoipPreferForLte = false;
        sendMessageDelayed(obtainMessage(9), 4000L);
    }

    public void setNetworkCapability(String str) {
        log("setNetworkCapability: " + str);
        if (!this.mIsInGame) {
            log("smart 5G for game is disabled");
            return;
        }
        if ("4G".equalsIgnoreCase(str)) {
            this.mGameRst = false;
            changeEnableEndcStatus(false, NrSceneConstants$Scene.GAME);
        } else if ("NSA".equalsIgnoreCase(str)) {
            this.mGameRst = true;
            changeEnableEndcStatus(true, NrSceneConstants$Scene.GAME);
        }
    }

    private boolean isNrSaNetworkType(int i) {
        int networkType = ((TelephonyManager) this.mContext.getSystemService("phone")).getNetworkType(i);
        boolean z = networkType == 20;
        log("networkType=" + networkType + ", subId=" + i + ", isNrSaNetworkType=" + z);
        return z;
    }

    private void silentDisableSaCounts(NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (isSaPowerCdrxEnabled() && getCdrxConfigStatus() == CdrxState.Disabled) {
            localLog("SaCDRX", "start expired check!");
            this.mCdrxSceneDisableSaCount++;
            removeMessages(21);
            sendMessageDelayed(obtainMessage(21), 3600000L);
        }
        if (NrSceneConstants$Scene.SA_NIGHT_MODE == nrSceneConstants$Scene && isSaPowerNightModeEnabled() && this.mIsNightMode) {
            this.mNightModeSceneDisableSaCount++;
        }
        if (NrSceneConstants$Scene.SA_WECHAT_VOIP == nrSceneConstants$Scene && isSaPowerWechatVoipEnabled() && this.mIsWechatVoipForgroud) {
            this.mWechatSceneDisableSaCount++;
        }
        if (NrSceneConstants$Scene.SA_THERMAL_MODE == nrSceneConstants$Scene && isSaPowerThermalModeEnabled() && this.mSaThermalFlag) {
            this.mThermalModeSceneDisableSaCount++;
        }
    }

    protected void localLog(String str, String str2) {
        FiveGManagerBase.getInstance();
        FiveGManagerBase.localLog(LOG_TAG, str, str2);
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    protected static void logForPowerSave(String str) {
        log("PowerSaveOpt: " + str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("FiveGPowerController: ".concat(" SaOptimization=" + this.mSaPowerOptimizationEnabled + "\n SaPower=" + this.mSaPowerEnabled + "\n Cdrx=" + this.mCdrxConfig + "\n Cdrx DisableSaCount=" + this.mCdrxSceneDisableSaCount + "\n NightMode DisableSaCount=" + this.mNightModeSceneDisableSaCount + "\n WechatMode DisableSaCount=" + this.mWechatSceneDisableSaCount + "\n ThermalMode DisableSaCount=" + this.mThermalModeSceneDisableSaCount + "\n SaPowerSetting=" + Settings.System.getInt(this.mContext.getContentResolver(), "sa_power_save_mode", -1) + "\n SaPowerCloud=" + FiveGCloudController.getSAPowerCloudStatus() + "\n"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processOOSPowerOptimize(boolean z) {
        int i = 0;
        if (z) {
            while (i < this.currentOosType.length) {
                obtainMessage(22, 3, i).sendToTarget();
                i++;
            }
        } else {
            while (true) {
                int[] iArr = this.currentOosType;
                if (i >= iArr.length) {
                    return;
                }
                if (iArr[i] != 2) {
                    obtainMessage(22, 1, i).sendToTarget();
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNightModePowerOptimize(int i) {
        obtainMessage(24, i, 0).sendToTarget();
    }

    public void processLimitLteCaOptimizeByCloud() {
        obtainMessage(37).sendToTarget();
    }

    private void processLimitLteCaOptimize() {
        TelephonyManagerEx.getDefault();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            processLimitLteCaOptimizeBySlot(i);
        }
    }

    private void processLimitLteCaOptimizeBySlot(int i) {
        if (this.mInCall) {
            log("LTE-CA optimization stoped by InCall");
            this.mLteCaChangedInCall = true;
            return;
        }
        TelephonyManagerEx telephonyManagerEx = TelephonyManagerEx.getDefault();
        if (MiuiPhoneUtils.isValidatePhoneId(i) && telephonyManagerEx.getNetworkTypeForSlot(i) != 0 && telephonyManagerEx.getNetworkTypeForSlot(i) != 13 && telephonyManagerEx.getNetworkTypeForSlot(i) != 19) {
            FiveGManagerBase.getInstance().setModemFeature(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, this.mIsNightMode ? 1 : 0, i);
            log("processLimitLteCaOptimize, slotId:" + i + ",LTE-CA optimization completed");
            this.mIsLteCaChangedFailedForSlot[i] = false;
            return;
        }
        this.mIsLteCaChangedFailedForSlot[i] = true;
        log("processLimitLteCaOptimize, slotId:" + i + ",LTE-CA optimization failed");
    }

    private void updateOosType(int i, int i2) {
        byte[] bArrOnGetModemMsgBytes = ModemUtils.onGetModemMsgBytes(new int[]{i, i2});
        String id = ZoneId.systemDefault().getId();
        if (("Asia/Shanghai".equals(id) || "Asia/Kolkata".equals(id)) && this.currentOosType[i2] != i) {
            log("updateOosType:" + i + " on phone:" + i2 + ". [1:day,2:repeatOOS,3:night]");
            ModemUtils modemUtils = this.mHydra;
            if (modemUtils != null && modemUtils.callQcrilHookSet("onHookModemOosBaseTypeSet", Integer.valueOf(i), Integer.valueOf(i2))) {
                this.currentOosType[i2] = i;
            } else {
                ModemUtils modemUtils2 = this.mHydra;
                if (modemUtils2 == null) {
                    log("updateOosType failed due to mHydra null");
                } else if (!modemUtils2.callQcrilHookSet("onHookModemOosBaseTypeSet", Integer.valueOf(i), Integer.valueOf(i2))) {
                    log("updateOosType failed due to callQcrilHookSet onHookModemOosBaseTypeSet return false");
                }
            }
            ModemUtils modemUtils3 = this.mHydra;
            if (modemUtils3 != null && modemUtils3.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SET_OOS_TYPE.getType()), bArrOnGetModemMsgBytes)) {
                this.currentOosType[i2] = i;
                return;
            }
            ModemUtils modemUtils4 = this.mHydra;
            if (modemUtils4 == null) {
                log("updateOosType failed due to mHydra null");
            } else {
                if (modemUtils4.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SET_OOS_TYPE.getType()), bArrOnGetModemMsgBytes)) {
                    return;
                }
                log("updateOosType failed due to callQcrilHookSet onHookModemPowerAPCommand return false");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFrequentOOS(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = 0;
        for (int i2 = 0; i2 < 3; i2++) {
            long j2 = this.mOOSTimestamp[i][i2];
            if (j2 < 1) {
                log("ignore previous 2 OOS");
                return false;
            }
            if (j2 > j) {
                j = j2;
            }
            if (j2 < jElapsedRealtime) {
                jElapsedRealtime = j2;
            }
        }
        return j - jElapsedRealtime < 1800000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNetworkStable(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        for (int i2 = 0; i2 < 3; i2++) {
            if (jElapsedRealtime - this.mOOSTimestamp[i][i2] < 3600000) {
                return false;
            }
        }
        return true;
    }

    private void initOosReceiver() {
        if (!MiuiPhoneUtils.isOOSPowerOptimizeSupported()) {
            log("oos update not supported!");
            return;
        }
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        this.mHydra = modemUtilsInit;
        if (modemUtilsInit != null) {
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda6
                @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
                public final void onEvent(BaseEvent baseEvent) {
                    this.f$0.lambda$initOosReceiver$3(baseEvent);
                }
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SERVICE_STATE");
            this.mContext.registerReceiver(this.mOOSPowerReceiver, intentFilter, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initOosReceiver$3(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.RilEvent) {
            boolean zIsRilReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
            this.mIsRilHookReady = zIsRilReady;
            if (zIsRilReady) {
                initOosTypeConfig();
                ModemUtils modemUtils = this.mHydra;
                if (modemUtils != null) {
                    modemUtils.callQcrilHookSet("onHookNotifyScreenStatusSync", Integer.valueOf(isScreenOn() ? 1 : 0));
                }
                sendEmptyMessage(0);
            }
        }
    }

    private void initOosTypeConfig() {
        int i = 0;
        while (true) {
            int[] iArr = this.currentOosType;
            if (i >= iArr.length) {
                return;
            }
            if (iArr[i] != 2) {
                updateOosType(1, i);
            }
            i++;
        }
    }

    private boolean isNsaThermalOptimization() {
        return miui.telephony.TelephonyManager.isCustForFrOrange() && this.mNsaThermalFlag;
    }

    private boolean isNetworkOptimizationForNsa() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isNsaOffInRecoveryAction(defaultDataSlotId) || ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isSaOffInRecoveryAction(defaultDataSlotId);
    }

    private boolean isNetworkOptimizationForSa() {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isSaOffInRecoveryAction(DefaultSimManager.getDefaultDataSlotId());
    }

    private boolean isSpecialSimSaDisabledForSa(int i) {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isSpecialSimSaDisabled(i);
    }

    private boolean isSmsOptimizationForSa() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (MiuiSmsControllerStub.get(i).isSmsSaOptimization()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:38:0x0098 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public String getModemCrashByNodeFile() throws Throwable {
        Throwable th;
        BufferedReader bufferedReader;
        String line = null;
        try {
            File file = new File("/proc/last_mcrash");
            if (!file.exists()) {
                return null;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j = jCurrentTimeMillis - RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
            long jLastModified = file.lastModified();
            log("fileName = " + file.getName() + ", creatime = " + ((SimpleDateFormat) sCrashFileDateFormat.get()).format(new Date(jLastModified)) + ", currentTime = " + jCurrentTimeMillis + ", beforeMinuteTime = " + j + ", fileUpdateTime = " + jLastModified);
            if (jLastModified <= jCurrentTimeMillis && jLastModified >= j) {
                bufferedReader = new BufferedReader(new FileReader(file));
                try {
                    try {
                        try {
                            line = bufferedReader.readLine();
                        } catch (IOException e) {
                            e = e;
                            log("getModemCrashByNode is error");
                            e.printStackTrace();
                            if (bufferedReader != null) {
                            }
                            return line;
                        }
                        bufferedReader.close();
                    } catch (IOException unused) {
                    }
                    return line;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException unused2) {
                        }
                    }
                    throw th;
                }
            }
            return null;
        } catch (IOException e2) {
            e = e2;
            bufferedReader = null;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
        log("getModemCrashByNode is error");
        e.printStackTrace();
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        return line;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public String getModemCrashByFeatureValue() throws Throwable {
        Throwable th;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                File modemCrashFileIfExists = getModemCrashFileIfExists();
                if (modemCrashFileIfExists != null) {
                    fileInputStream = new FileInputStream(modemCrashFileIfExists);
                    try {
                        String strTrim = new String(readInputStream(fileInputStream)).trim();
                        try {
                            fileInputStream.close();
                            return strTrim;
                        } catch (IOException e) {
                            log("can not get modem crash reason " + e);
                            return strTrim;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        log("getModemCrashByFeatureValue is error");
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                                log("can not get modem crash reason " + e3);
                            }
                        }
                        return null;
                    }
                }
                log("getModemCrashByFeatureValue file is null");
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e4) {
                        log("can not get modem crash reason " + e4);
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                fileInputStream2.close();
            }
            throw th;
        }
    }

    private static File getModemCrashFileIfExists() {
        File file = new File("/sdcard/ramdump");
        try {
            if (file.exists()) {
                new ArrayList();
                File[] fileArrListFiles = file.listFiles(new FileFilter() { // from class: com.android.phone.FiveGPowerController.18
                    @Override // java.io.FileFilter
                    public boolean accept(File file2) {
                        return file2.getName().contains("_dmesg.log");
                    }
                });
                if (fileArrListFiles.length == 0) {
                    log("There are no documents that meet the requirements");
                    return null;
                }
                Arrays.sort(fileArrListFiles, new Comparator() { // from class: com.android.phone.FiveGPowerController.19
                    @Override // java.util.Comparator
                    public int compare(File file2, File file3) {
                        long jLastModified = file2.lastModified() - file3.lastModified();
                        if (jLastModified > 0) {
                            return 1;
                        }
                        return jLastModified == 0 ? 0 : -1;
                    }
                });
                long jCurrentTimeMillis = System.currentTimeMillis();
                long j = jCurrentTimeMillis - RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
                long jLastModified = fileArrListFiles[fileArrListFiles.length - 1].lastModified();
                log("fileName = " + fileArrListFiles[fileArrListFiles.length - 1].getName() + ", creatime = " + ((SimpleDateFormat) sCrashFileDateFormat.get()).format(new Date(jLastModified)) + ", currentTime = " + jCurrentTimeMillis + ", beforeMinuteTime = " + j + ", fileUpdateTime = " + jLastModified);
                if (jLastModified <= jCurrentTimeMillis && jLastModified >= j) {
                    return fileArrListFiles[fileArrListFiles.length - 1];
                }
                return null;
            }
        } catch (Exception e) {
            log("getModemCrashFileIfExists e = " + e);
        }
        return null;
    }

    private void processThermalPowerOptimizeForSA() throws Throwable {
        StringBuilder sb;
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(new File("/sys/class/thermal/thermal_message/modem_limit"));
                try {
                    String strTrim = new String(readInputStream(fileInputStream2)).trim();
                    boolean zIsSaThermalLimited = isSaThermalLimited(strTrim);
                    boolean z = this.mSaThermalFlag;
                    this.mSaThermalNodeLimited = zIsSaThermalLimited;
                    if (zIsSaThermalLimited) {
                        if (this.mIsWifiConnected && !this.mSaThermalFlag) {
                            this.mSaThermalPendingForNonWifi = true;
                        } else if (!this.mIsWifiConnected) {
                            this.mSaThermalFlag = true;
                            this.mSaThermalPendingForNonWifi = false;
                        }
                    } else {
                        this.mSaThermalFlag = false;
                        this.mSaThermalPendingForNonWifi = false;
                    }
                    if (z != this.mSaThermalFlag) {
                        log("value = " + strTrim + " ; mSaThermalFlag = " + this.mSaThermalFlag + " ; mSaThermalNodeLimited = " + this.mSaThermalNodeLimited + " ; mSaThermalPendingForNonWifi = " + this.mSaThermalPendingForNonWifi + " ; mIsWifiConnected = " + this.mIsWifiConnected + " ; malSpecialApp = " + mIsThermalSpecialApp);
                        changeEnableEndcStatus(this.mSaThermalFlag ^ true, NrSceneConstants$Scene.SA_THERMAL_MODE);
                    } else if (this.mSaThermalPendingForNonWifi) {
                        log("value = " + strTrim + " ; sa thermal pending for non-wifi ; mIsWifiConnected = " + this.mIsWifiConnected + " ; malSpecialApp = " + mIsThermalSpecialApp);
                    }
                    try {
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("can not get temp state ");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    log("read temp state failed" + e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("can not get temp state ");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            log("can not get temp state " + e4);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    private boolean isSaThermalLimited(String str) {
        int i = Integer.parseInt(str);
        if (!mIsThermalSpecialApp) {
            return i != 0;
        }
        if (i == 0) {
            return mThermalSpecialLevel[0] != 0;
        }
        if (i != 1) {
            return this.mSaThermalNodeLimited;
        }
        return mThermalSpecialLevel[1] != 0;
    }

    private void updateThermalOptSpecialLevel() {
        mThermalSpecialLevel = this.mContext.getResources().getIntArray(285409494);
        mThrottleSpecialLevel = this.mContext.getResources().getIntArray(285409496);
    }

    private void processThermalPowerOptimizeForNSA() throws Throwable {
        StringBuilder sb;
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(new File("/sys/class/thermal/thermal_message/modem_limit"));
                try {
                    String strTrim = new String(readInputStream(fileInputStream2)).trim();
                    boolean z = this.mNsaThermalFlag;
                    boolean z2 = Integer.parseInt(strTrim) != 0;
                    this.mNsaThermalFlag = z2;
                    if (z != z2) {
                        log("value = " + strTrim + " ; mNsaThermalFlag = " + this.mNsaThermalFlag);
                        boolean zIsXmRadioSupported = FiveGUtils.isXmRadioSupported();
                        boolean z3 = this.mNsaThermalFlag;
                        if (zIsXmRadioSupported) {
                            changeEnableEndcStatus(!z3, NrSceneConstants$Scene.NSA_THERMAL_MODE);
                        } else {
                            execNsaOptimization(!z3, NrSceneConstants$Scene.NSA_THERMAL_MODE);
                        }
                    }
                    try {
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("can not get temp state ");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    log("read temp state failed" + e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("can not get temp state ");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            log("can not get temp state " + e4);
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void processThermalThrottle() throws Throwable {
        StringBuilder sb;
        log("mIsThrottleWhiteApp = " + mIsThrottleWhiteApp);
        if (mIsThrottleWhiteApp) {
            log("white app, do not exec thermal throttle");
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(new File("/sys/class/thermal/thermal_message/modem_level"));
                try {
                    String strTrim = new String(readInputStream(fileInputStream2)).trim();
                    if (mIsThrottleSpecialApp) {
                        int i = Integer.parseInt(strTrim);
                        if (i == 0) {
                            mThrottleLevel = mThrottleSpecialLevel[0];
                        } else if (i == 253) {
                            mThrottleLevel = mThrottleSpecialLevel[4];
                        } else if (i == 7) {
                            mThrottleLevel = mThrottleSpecialLevel[1];
                        } else if (i == 8) {
                            mThrottleLevel = mThrottleSpecialLevel[2];
                        } else if (i == 9) {
                            mThrottleLevel = mThrottleSpecialLevel[3];
                        }
                    } else {
                        mThrottleLevel = Integer.parseInt(strTrim);
                    }
                    int nrRsrp = MiuiPhoneUtils.getNrRsrp();
                    log("value = " + strTrim + ", isWifiConnected = " + this.mIsWifiConnected + ", isFivegModeEnabled = " + isFivegModeEnabled() + ", mInCall = " + this.mInCall + ", mIsTestApp = " + this.mIsTestApp + ", mIsThrottleSpecialApp = " + mIsThrottleSpecialApp + ", nrRsrp = " + nrRsrp + ", SNR = " + this.mSASnr + ", mThrottleLevel = " + mThrottleLevel + ", mIsCurrentHstState = " + mIsCurrentHstState);
                    if (!this.mIsWifiConnected && !mIsCurrentHstState && !mIsInSubway && isFivegModeEnabled() && nrRsrp >= -100 && this.mSASnr >= mGoodSnrForSA) {
                        if (!this.mInCall && !this.mIsTestApp && !this.mHasPowerSaveOpt) {
                            updateThrottleLevel(mThrottleLevel);
                        }
                    } else if (this.mIsWifiConnected || mIsCurrentHstState || mIsInSubway || !isFivegModeEnabled()) {
                        if (!this.mIsWifiConnected) {
                            updateHstSubwayThermalThrottleRecord(mThrottleLevel);
                        }
                        resetThermalThrottle();
                    }
                    try {
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("can not get thermal throttle state ");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    log("read thermal throttle state failed" + e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("can not get thermal throttle state ");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            log("can not get thermal throttle state " + e4);
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void updateThermalThrottleSASnr(int i) {
        mGoodSnrForSA = i;
    }

    public void resetThermalThrottle() {
        mThrottleLevel = 0;
        log("resetThermalThrottle to state " + mThrottleLevel + ", mInCall = " + this.mInCall + ", mHasPowerSaveOpt=" + this.mHasPowerSaveOpt);
        if (this.mInCall || this.mHasPowerSaveOpt) {
            return;
        }
        this.mIsThrottleForPS = false;
        updateThrottleLevel(mThrottleLevel);
    }

    protected void updateHstSubwayThermalThrottleRecord(int i) {
        if (mIsCurrentHstState) {
            mHstThermalThrottleCount.put(Integer.valueOf(i), Integer.valueOf(((Integer) mHstThermalThrottleCount.getOrDefault(Integer.valueOf(i), 0)).intValue() + 1));
        }
        if (mIsInSubway) {
            mSubwayThermalThrottleCount.put(Integer.valueOf(i), Integer.valueOf(((Integer) mSubwayThermalThrottleCount.getOrDefault(Integer.valueOf(i), 0)).intValue() + 1));
        }
    }

    private void processPowerSave() throws Throwable {
        StringBuilder sb;
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(new File("/sys/class/thermal/power_save/powersave_mode"));
                try {
                    String strTrim = new String(readInputStream(fileInputStream2)).trim();
                    boolean z = false;
                    if (!"-1".equals(strTrim) && ((Integer.parseInt(strTrim) & 1) == 1 || Integer.parseInt(strTrim) == 2)) {
                        z = true;
                    }
                    if (this.mIsPowerSaveMode != z) {
                        logForPowerSave("processPowerSave value=" + strTrim);
                        this.mIsPowerSaveMode = z;
                        execPowerSaveOptimization();
                    }
                    try {
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("can not get power save state ");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    log("read power save state failed" + e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("can not get power save state ");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            log("can not get power save state " + e4);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    private void execPowerSaveOptimization() {
        localLog("execPowerSaveOptimization", "mIsPowerSaveMode=" + this.mIsPowerSaveMode + ", mIsModemSaveLevel=" + this.mIsModemSaveLevel + ", mHasPowerSaveOpt=" + this.mHasPowerSaveOpt);
        boolean z = this.mIsPowerSaveMode && this.mIsModemSaveLevel;
        if (z && !this.mHasPowerSaveOpt) {
            this.mHasPowerSaveOpt = true;
            this.mApmOnForPS = false;
            notifyPowerSaveModeChangeListener();
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).execPowerSaveOptimization(true);
            removeMessages(42);
            sendMessageDelayed(obtainMessage(42), MirrorDesktopHelper.TIMEOUT_MILLIS);
            updateForPowerSaveOn(0);
            removeMessages(52);
            sendMessageDelayed(obtainMessage(52), 3600000L);
            notifyModemIdelNeiMea(2, 1);
            return;
        }
        if (z || !this.mHasPowerSaveOpt) {
            return;
        }
        this.mHasPowerSaveOpt = false;
        logForPowerSave("do recovery");
        notifyPowerSaveModeChangeListener();
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).execPowerSaveOptimization(false);
        resetThermalThrottle();
        if (this.mIsBalanceModeSupport) {
            return;
        }
        removeMessages(52);
        resetForPowerSaveOff(0);
        notifyModemIdelNeiMea(2, 0);
    }

    private void updatePowerSaveOptStatus(String str) {
        if (str != null) {
            logForPowerSave("updatePowerSaveOptStatus status=".concat(str));
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length == 7) {
                try {
                    mPowerSaveOpt = Integer.parseInt(strArrSplit[0].trim(), 2);
                    this.UL_THROTTLE_STATE_ON = Integer.parseInt(strArrSplit[1].trim());
                    this.MAX_TX_POWER_ON = Integer.parseInt(strArrSplit[2].trim());
                    this.SMART_IDLE_ON = Integer.parseInt(strArrSplit[3].trim());
                    this.DL_THROTTLE_LEVEL_ON = Integer.parseInt(strArrSplit[4].trim());
                    this.IDLE_NEI_MEA_MULTIPLIER = Integer.parseInt(strArrSplit[5].trim());
                } catch (NumberFormatException e) {
                    log("updatePowerSaveOptStatus not a number for " + e);
                }
                logForPowerSave("updatePowerSaveOptStatus set PowerSaveOpt=" + mPowerSaveOpt + ", UL_THROTTLE=" + this.UL_THROTTLE_STATE_ON + ", TX_POWER=" + this.MAX_TX_POWER_ON + ", SMART_IDLE=" + this.SMART_IDLE_ON + ", DL_THROTTLE=" + this.DL_THROTTLE_LEVEL_ON + ", IDLE_NEI_MEA=" + this.IDLE_NEI_MEA_MULTIPLIER);
            }
        }
    }

    private void updateModemMeasureRelaxByCloud() {
        int modemMeasureRelaxCloudStatus = FiveGCloudController.getModemMeasureRelaxCloudStatus();
        if (modemMeasureRelaxCloudStatus == -1) {
            return;
        }
        boolean z = modemMeasureRelaxCloudStatus == 1;
        if (this.modemMeasureRelaxStatus != z) {
            log("updateCloudFeatureStatus enable= ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
            if (z) {
                FiveGManagerBase.getInstance().sendAtCmdSync(0, ENABLE_MODEM_MEASURE_RELAX, null);
            } else {
                FiveGManagerBase.getInstance().sendAtCmdSync(0, DISABLE_MODEM_MEASURE_RELAX, null);
            }
            this.modemMeasureRelaxStatus = z;
        }
    }

    private void updateForPowerSaveOn(int i) {
        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            if ((!this.mIsBalanceModeSupport && !this.mHasPowerSaveOpt) || this.mApmOnForPS || this.mInCall) {
                return;
            }
            int i2 = this.mULThrottleState;
            int i3 = this.mTxPower;
            int i4 = this.mSmartIdleTimer;
            boolean z = this.mIdleNeiMea;
            boolean z2 = this.mLimitCA;
            if (isUpdateULOptEnabled() && isUpdateULReason(i) && this.mGoodSignalForPS && this.mWhiteAppForPS) {
                logForPowerSave("updateForPowerSaveOn ul throttle");
                updateULThrottleState(this.UL_THROTTLE_STATE_ON);
            }
            if (isMaxTxPowerOptEnabled() && isReduceTxPowerReason(i) && this.mNormalSignalForPS && this.mWhiteAppForPS) {
                logForPowerSave("updateForPowerSaveOn tx power");
                reduceMaxTxPower(this.MAX_TX_POWER_ON);
            }
            if (isSmartIdleOptEnabled() && isSmartIdleReason(i) && !this.mScreenOnForPS) {
                logForPowerSave("updateForPowerSaveOn smart idle");
                execSmartIdle(this.SMART_IDLE_ON);
            }
            if (isIdleNeiMeaOptEnabled() && isIdleNeiMeaReason(i) && ((this.mGoodSignalForPS && isResidentCell()) || this.mDebugPowerSaveMode)) {
                logForPowerSave("updateForPowerSaveOn idle neighbor measure");
                if (!TelephonyCloudController.getCloudControllerMeasureRelaxOptDisabled()) {
                    reduceIdleNeighborMea(true, this.IDLE_NEI_MEA_MULTIPLIER);
                }
            }
            if (isSatisfiedlimitCA() && this.misFivegModeEnabled && this.mWhiteAppForPS && this.mHasPowerSaveOpt) {
                logForPowerSave("updateForPowerSaveOn limit nw ca for 5GA");
                limitNwCA(true, LimitCaScene.POWER_SAVE.value);
            }
            if (isLimitCAOptEnabled() && isLimitCAReason(i) && ((!this.mGoodSignalForPS && this.mLowBatteryForPS) || this.mDebugPowerSaveMode)) {
                logForPowerSave("updateForPowerSaveOn limit nw ca");
                limitNwCA(true, LimitCaScene.POWER_SAVE.value);
            }
            if (i2 == this.mULThrottleState && i3 == this.mTxPower && i4 == this.mSmartIdleTimer && z == this.mIdleNeiMea && z2 == this.mLimitCA) {
                return;
            }
            localLog("updateForPowerSaveOn", "reason = " + i + "; isGoodSignal = " + this.mGoodSignalForPS + "; isNormalSignal = " + this.mNormalSignalForPS + "; isWhiteApp = " + this.mWhiteAppForPS + "; isScreenOn = " + this.mScreenOnForPS + "; isLowBattery = " + this.mLowBatteryForPS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetForPowerSaveOff(int i) {
        boolean z;
        boolean z2 = true;
        if (i == 5) {
            this.mApmOnForPS = true;
        }
        if (!isUpdateULReason(i) || this.mULThrottleState == 0) {
            z = false;
        } else {
            logForPowerSave("resetForPowerSaveOff ul throttle");
            updateULThrottleState(0);
            z = true;
        }
        if (isReduceTxPowerReason(i) && this.mTxPower != 0) {
            logForPowerSave("resetForPowerSaveOff tx power");
            reduceMaxTxPower(0);
            z = true;
        }
        if (isSmartIdleReason(i) && this.mSmartIdleTimer != 60) {
            logForPowerSave("resetForPowerSaveOff smart idle");
            execSmartIdle(60);
            z = true;
        }
        if (isIdleNeiMeaReason(i) && this.mIdleNeiMea) {
            logForPowerSave("resetForPowerSaveOff idle neighbor measure");
            if (!TelephonyCloudController.getCloudControllerMeasureRelaxOptDisabled()) {
                reduceIdleNeighborMea(false, this.IDLE_NEI_MEA_MULTIPLIER);
            }
            z = true;
        }
        if (isLimitCAReason(i)) {
            logForPowerSave("resetForPowerSaveOff limit ca");
            limitNwCA(false, LimitCaScene.POWER_SAVE.value);
        } else {
            z2 = z;
        }
        if (z2) {
            localLog("resetForPowerSaveOff", "reason = " + i);
        }
    }

    private boolean isUpdateULReason(int i) {
        return isCommonReason(i) || i == 1 || i == 3;
    }

    private boolean isReduceTxPowerReason(int i) {
        return isCommonReason(i) || i == 2 || i == 3;
    }

    private boolean isSmartIdleReason(int i) {
        return isCommonReason(i) || i == 4;
    }

    private boolean isIdleNeiMeaReason(int i) {
        return isCommonReason(i) || i == 6 || i == 1;
    }

    private boolean isLimitCAReason(int i) {
        return isCommonReason(i) || i == 8 || i == 7 || i == 3;
    }

    private void initPowerSaveOpt() {
        mPowerSaveOpt = 60;
        FiveGManagerBase.getInstance().sendAtCmdSync(0, ENABLE_MODEM_MEASURE_RELAX, null);
        this.modemMeasureRelaxStatus = true;
    }

    private boolean isUpdateULOptEnabled() {
        return 1 == (mPowerSaveOpt & 1);
    }

    private boolean isMaxTxPowerOptEnabled() {
        return 2 == (mPowerSaveOpt & 2);
    }

    private boolean isSmartIdleOptEnabled() {
        return 4 == (mPowerSaveOpt & 4);
    }

    private boolean isDLThrottleOptEnabled() {
        return 8 == (mPowerSaveOpt & 8);
    }

    private boolean isIdleNeiMeaOptEnabled() {
        return 16 == (mPowerSaveOpt & 16);
    }

    private boolean isLimitCAOptEnabled() {
        return 32 == (mPowerSaveOpt & 32);
    }

    private void processSignalStrengthForPowerSave(int i, SignalStrength signalStrength) {
        boolean z;
        boolean z2;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || signalStrength == null) {
            z = false;
            z2 = false;
        } else {
            z = false;
            z2 = false;
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                    int ssRsrp = cellSignalStrengthNr.getSsRsrp();
                    int ssSinr = cellSignalStrengthNr.getSsSinr();
                    logForPowerSave("processSignalStrengthForPowerSave nr rsrp=" + ssRsrp + " sinr=" + ssSinr);
                    if (ssRsrp != Integer.MAX_VALUE && ssRsrp >= -102 && ssSinr >= 0) {
                        z = true;
                    }
                    if (ssRsrp != Integer.MAX_VALUE && ssRsrp >= -120 && ssSinr >= 0) {
                        z2 = true;
                    }
                } else if (cellSignalStrength instanceof CellSignalStrengthLte) {
                    CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                    int rssnr = cellSignalStrengthLte.getRssnr();
                    int rsrp = cellSignalStrengthLte.getRsrp();
                    logForPowerSave("processSignalStrengthForPowerSave lte rsrp=" + rsrp + " rssnr=" + rssnr);
                    if (rsrp != Integer.MAX_VALUE && rsrp >= -102 && rssnr >= 0) {
                        z = true;
                    }
                }
            }
        }
        if (z && !this.mGoodSignalForPS) {
            this.mGoodSignalForPS = true;
            resetForPowerSaveOff(8);
            if (shouldAvoidPingPongForSignalChange(0, 1)) {
                logForPowerSave("processSignalStrengthForPowerSave avoid pingpong for good signal");
                return;
            } else {
                this.mElapsedRealtimeForSignalChange[0] = SystemClock.elapsedRealtime();
                updateForPowerSaveOn(1);
            }
        } else if (!z && this.mGoodSignalForPS) {
            this.mGoodSignalForPS = false;
            resetForPowerSaveOff(1);
            if (shouldAvoidPingPongForSignalChange(2, 1)) {
                logForPowerSave("processSignalStrengthForPowerSave avoid pingpong for weak signal");
                return;
            } else {
                this.mElapsedRealtimeForSignalChange[2] = SystemClock.elapsedRealtime();
                updateForPowerSaveOn(8);
            }
        }
        if (z2 && !this.mNormalSignalForPS) {
            this.mNormalSignalForPS = true;
            if (shouldAvoidPingPongForSignalChange(1, 1)) {
                logForPowerSave("processSignalStrengthForPowerSave avoid pingpong for normal signal");
                return;
            } else {
                this.mElapsedRealtimeForSignalChange[1] = SystemClock.elapsedRealtime();
                updateForPowerSaveOn(2);
                return;
            }
        }
        if (z2 || !this.mNormalSignalForPS) {
            return;
        }
        this.mNormalSignalForPS = false;
        resetForPowerSaveOff(2);
    }

    private boolean shouldAvoidPingPongForSignalChange(int i, int i2) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mElapsedRealtimeForSignalChange[i];
        return j > 0 && jElapsedRealtime - j < TimeUnit.MINUTES.toMillis((long) i2);
    }

    private void processForegroundChangedForNsaScgPoorOptimization(ForegroundInfo foregroundInfo) {
        boolean zContains = NSA_SCG_POOR_OPTIMIZATION_WHITE_LIST.contains(foregroundInfo.mForegroundPackageName);
        boolean z = this.mIsWhiteAppForNsaScgPoorOptimization;
        if (z && !zContains) {
            sendMessageDelayed(obtainMessage(53), 30000L);
        } else if (!z && zContains) {
            removeMessages(53);
            if (!this.mNeedReportNsaScgPoor && !TelephonyCloudController.CLOUD_NSA_SCG_POOR_OPT_DISABLED) {
                this.mNeedReportNsaScgPoor = true;
                notifyModemNeedReportNsaScgPoorStatus(true, TelephonyCloudController.mNsaScgPoorThreshold);
            }
        }
        this.mIsWhiteAppForNsaScgPoorOptimization = zContains;
    }

    private void processScreenOffForNsaScgPoorOptimization(boolean z) {
        if (this.mInCall) {
            synchronized (this.mWakeLock) {
                releaseWakeLock();
            }
        } else {
            if (!this.mIsWifiConnected && z && this.mIsMobileConnected) {
                startNetworkSpeedMonitor();
                return;
            }
            if (this.misFivegModeEnabled) {
                changeEnableEndcStatus(false, NrSceneConstants$Scene.SCREEN);
            }
            if (!MiuiPhoneUtils.is5GSaRegistered() && this.mPowerOptimizationEnabled) {
                limitNwCA(true, LimitCaScene.SCREEN.value);
            }
            synchronized (this.mWakeLock) {
                releaseWakeLock();
            }
        }
    }

    public boolean isDeviceCharging() {
        boolean zIsCharging = ((BatteryManager) this.mContext.getSystemService("batterymanager")).isCharging();
        log("isDeviceCharging=" + zIsCharging);
        return zIsCharging;
    }

    private void processChargingStateChange(boolean z) {
        if (this.mIsNsaScgPoorOptimizationSupported || isSatisfiedlimitCA()) {
            if (z && !this.mIsCharging && this.mBatteryLevel >= 25) {
                if (this.mIsNsaScgPoorOptimizationSupported) {
                    if (this.mIsNsaScgPoorStatusFlag) {
                        this.mIsNsaScgPoorStatusFlag = false;
                        if (FiveGUtils.isXmRadioSupported()) {
                            changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                        } else {
                            execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                        }
                        if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                            notifyModemNsaScgPoorSuccess(false);
                        }
                    }
                    limitNwCA(false, LimitCaScene.NSA_SCG_POOR_MODE.value);
                }
                if (isSatisfiedlimitCA()) {
                    limitNwCA(false, LimitCaScene.POWER_SAVE.value);
                }
            }
            this.mIsCharging = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetNsascgPoorOptimization() {
        if (this.mIsNsaScgPoorStatusFlag) {
            this.mIsNsaScgPoorStatusFlag = false;
            if (FiveGUtils.isXmRadioSupported()) {
                changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            } else {
                execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            }
            limitNwCA(false, LimitCaScene.NSA_SCG_POOR_MODE.value);
            if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                notifyModemNsaScgPoorSuccess(false);
            }
        }
    }

    public long getNsaScgPoorOptimizationCounts() {
        long j = this.mNsaScgPoorOptimizationCounts;
        this.mNsaScgPoorOptimizationCounts = 0L;
        return j;
    }

    public long getNsaScgPoorFreezeRecoverCounts() {
        long j = this.mNsaScgPoorFreezeRecoverCounts;
        this.mNsaScgPoorFreezeRecoverCounts = 0L;
        return j;
    }

    private void processForegroundForPowerSave(ForegroundInfo foregroundInfo) {
        String str = foregroundInfo.mForegroundPackageName;
        boolean zIsContainsPackage = MiuiPhoneUtils.isContainsPackage("config_powersave_white_list_packages", str);
        if ((!zIsContainsPackage || this.mWhiteAppForPS) && (zIsContainsPackage || !this.mWhiteAppForPS)) {
            return;
        }
        logForPowerSave("processForegroundForPowerSave " + str + ", isWhiteApp=" + zIsContainsPackage);
        this.mWhiteAppForPS = zIsContainsPackage;
        removeMessages(47);
        Message messageObtainMessage = obtainMessage(47);
        messageObtainMessage.arg1 = zIsContainsPackage ? 1 : 0;
        sendMessageDelayed(messageObtainMessage, zIsContainsPackage ? 30000 : 5000);
    }

    private void processScreenForPowerSave(boolean z) {
        if ((!z || this.mScreenOnForPS) && (z || !this.mScreenOnForPS)) {
            return;
        }
        logForPowerSave("processScreenForPowerSave screenOn=" + z);
        this.mScreenOnForPS = z;
        removeMessages(48);
        Message messageObtainMessage = obtainMessage(48);
        messageObtainMessage.arg1 = z ? 1 : 0;
        sendMessageDelayed(messageObtainMessage, z ? 5000 : 30000);
    }

    private void processBatteryStateChange(int i) {
        if (this.mBatteryLevel != i) {
            this.mBatteryLevel = i;
            logForPowerSave("processBatteryStateChange batteryLevel=" + i);
        }
        boolean z = i < 20;
        boolean z2 = i > 25;
        if (z && !this.mLowBatteryForPS) {
            this.mLowBatteryForPS = true;
            updateForPowerSaveOn(7);
        } else if (z2 && this.mLowBatteryForPS) {
            this.mLowBatteryForPS = false;
            resetForPowerSaveOff(7);
        }
        if (i >= 25 && this.mIsNsaScgPoorOptimizationSupported && this.mIsCharging) {
            if (this.mIsNsaScgPoorStatusFlag) {
                this.mIsNsaScgPoorStatusFlag = false;
                if (FiveGUtils.isXmRadioSupported()) {
                    changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                } else {
                    execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
                }
                if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                    notifyModemNsaScgPoorSuccess(false);
                }
            }
            limitNwCA(false, LimitCaScene.NSA_SCG_POOR_MODE.value);
        }
    }

    private boolean isSatisfiedlimitCA() {
        return !TelephonyCloudController.getCloudControllerFivegaOptDisabled() && this.mIsLimitCASupported;
    }

    private void processResidentCellChange() {
        if (!this.mIdleNeiMea) {
            updateForPowerSaveOn(6);
        }
        removeMessages(52);
        sendMessageDelayed(obtainMessage(52), 3600000L);
    }

    private void processPowerLevel() throws Throwable {
        StringBuilder sb;
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(new File("/sys/class/thermal/power_save/power_level"));
                try {
                    byte[] inputStream = readInputStream(fileInputStream2);
                    String strTrim = new String(inputStream).trim();
                    boolean z = false;
                    if (!"-1".equals(strTrim) && inputStream.length >= 9 && Integer.parseInt(String.valueOf(strTrim.charAt(4))) > 0) {
                        z = true;
                    }
                    if (this.mIsModemSaveLevel != z) {
                        logForPowerSave("processPowerLevel value=" + strTrim);
                        this.mIsModemSaveLevel = z;
                        execPowerSaveOptimization();
                    }
                    try {
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("can not get power level state ");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    log("read power level state failed" + e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("can not get power level state ");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            log("can not get power level state " + e4);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
    }

    private void registerDebugState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("fiveGPowerController.intent.debugall");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.FiveGPowerController.21
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null && "fiveGPowerController.intent.debugall".equals(intent.getAction())) {
                    FiveGPowerController.this.obtainMessage(1000, intent).sendToTarget();
                }
            }
        }, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(BaseEvent baseEvent) {
        sendEmptyMessageDelayed(49, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void registerPowerSaveCloudChangeListener() {
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", this.mPowerSaveCloudChangeListener);
    }

    private void registerBatteryStateChangeListener() {
        BatteryStateManager.getInstance().registerBatteryStateListener(this.mBatteryStateChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.SimEvent) {
            obtainMessage(57, ((TelephonyEvent.SimEvent) baseEvent).getBundle()).sendToTarget();
        }
    }

    private void registerSpecialSimStateListener() {
        EventHubManager.getInstance().registerListener("telephony", "special_sim_state_changed", this.mSpecialSimStateListener);
    }

    class FiveGPowerFileStateListener extends FileEventListener {
        private int fileType;

        public FiveGPowerFileStateListener(String str, int i) throws Throwable {
            super(str);
            this.fileType = i;
            FiveGPowerController.this.checkFileListener(i);
        }

        @Override // com.android.phone.FileEventListener
        public void onEvent(int i, String str) throws Throwable {
            if (i != 2) {
                return;
            }
            FiveGPowerController.this.checkFileListener(this.fileType);
        }

        public void startWatching() {
            FileObserverController.getInstance().register(this);
        }

        public void stopWatching() {
            FileObserverController.getInstance().unregister(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkFileListener(int i) throws Throwable {
        if (i == 1) {
            processThermalPowerOptimizeForSA();
            return;
        }
        if (i == 2) {
            processThermalPowerOptimizeForNSA();
            return;
        }
        if (i == 3) {
            removeMessages(35);
            sendMessageDelayed(obtainMessage(35), 10000L);
        } else if (i == 4) {
            processPowerSave();
        } else {
            if (i != 5) {
                return;
            }
            processPowerLevel();
        }
    }

    private void notifyPowerSaveModeChangeListener() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("power_save_mode", this.mHasPowerSaveOpt);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.PowerEvent("power_save_mode_changed", bundle));
    }

    public String getSaOptimizeScene() {
        return this.mFiveGOptimizeScene.toString();
    }

    public long getThermalThrottle2RXCount() {
        long j = this.mThermalThrottle2RXCount;
        this.mThermalThrottle2RXCount = 0L;
        return j;
    }

    public long getThermalThrottleDL1Count() {
        long j = this.mThermalThrottleDL1Count;
        this.mThermalThrottleDL1Count = 0L;
        return j;
    }

    public long getThermalThrottleDL2Count() {
        long j = this.mThermalThrottleDL2Count;
        this.mThermalThrottleDL2Count = 0L;
        return j;
    }

    public long getThermalThrottleDSDACount() {
        long j = this.mThermalThrottleDSDACount;
        this.mThermalThrottleDSDACount = 0L;
        return j;
    }

    public String getThermalThrottleSASnr() {
        Map map = sThermalThrottleSASnrMap;
        if (map == null) {
            return null;
        }
        String str = "";
        for (Integer num : map.keySet()) {
            str = str + num + ":" + sThermalThrottleSASnrMap.get(num) + " ";
        }
        sThermalThrottleSASnrMap.clear();
        return str;
    }

    public String getThermalThrottleFGApp() {
        Map map = sThermalThrottleFGAppMap;
        String str = null;
        if (map == null) {
            return null;
        }
        for (String str2 : map.keySet()) {
            str = str + str2 + ":" + sThermalThrottleFGAppMap.get(str2) + " ";
        }
        sThermalThrottleFGAppMap.clear();
        return str;
    }

    public String getHstThermalThrottleCount() {
        Map map = mHstThermalThrottleCount;
        if (map == null) {
            return null;
        }
        String str = "";
        for (Integer num : map.keySet()) {
            str = str + num + ":" + mHstThermalThrottleCount.get(num) + " ";
        }
        mHstThermalThrottleCount.clear();
        return str;
    }

    public String getSubwayThermalThrottleCount() {
        Map map = mSubwayThermalThrottleCount;
        if (map == null) {
            return null;
        }
        String str = "";
        for (Integer num : map.keySet()) {
            str = str + num + ":" + mSubwayThermalThrottleCount.get(num) + " ";
        }
        mSubwayThermalThrottleCount.clear();
        return str;
    }

    public long getULThrottleOptCount() {
        long j = this.mULThrottleOptCount;
        this.mULThrottleOptCount = 0L;
        return j;
    }

    public long getMaxTxPowerOptCount() {
        long j = this.mMaxTxPowerOptCount;
        this.mMaxTxPowerOptCount = 0L;
        return j;
    }

    public long getSmartIdleOptCount() {
        long j = this.mSmartIdleOptCount;
        this.mSmartIdleOptCount = 0L;
        return j;
    }

    public long getIdleNeiMeaOptCount() {
        long j = this.mIdleNeiMeaOptCount;
        this.mIdleNeiMeaOptCount = 0L;
        return j;
    }

    public long[] getIdleNeiMeaModemOptCount() {
        long[] jArr = new long[MiuiPhoneUtils.PHONE_COUNT];
        Object[] objArr = new Object[1];
        String[] strArr = {"AT+EDMFAPP=20107,300,0,0,0,0,0", "+EDMFAPP"};
        int i = 0;
        while (true) {
            int i2 = MiuiPhoneUtils.PHONE_COUNT;
            if (i < i2) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                if (phone != null) {
                    FiveGManagerBase.getInstance().sendAtCmdSync(phone.getPhoneId(), strArr, objArr);
                    String[] strArr2 = (String[]) objArr[0];
                    if (strArr2 != null) {
                        String[] strArrSplit = strArr2[0].split(",");
                        if (strArrSplit.length >= 2) {
                            this.mIdleNeiMeaModemOptCount[i] = Long.parseLong(strArrSplit[1].trim());
                        }
                    }
                }
                i++;
            } else {
                long[] jArr2 = this.mIdleNeiMeaModemOptCount;
                long[] jArrCopyOf = Arrays.copyOf(jArr2, jArr2.length);
                log("getIdleNeiMeaModemOptCount: " + Arrays.toString(jArrCopyOf));
                this.mIdleNeiMeaModemOptCount = new long[i2];
                return jArrCopyOf;
            }
        }
    }

    public long getNwCaLimitOptCount() {
        long j = this.mNwCaLimitOptCount;
        this.mNwCaLimitOptCount = 0L;
        return j;
    }

    private static byte[] readInputStream(FileInputStream fileInputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[512];
        while (true) {
            try {
                try {
                    int i = fileInputStream.read(bArr, 0, 512);
                    if (i > 0) {
                        byteArrayOutputStream.write(bArr, 0, i);
                    } else {
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        try {
                            byteArrayOutputStream.close();
                            return byteArray;
                        } catch (IOException e) {
                            log("readInputStream " + e);
                            return byteArray;
                        }
                    }
                } catch (Exception e2) {
                    log("readInputStream " + e2);
                    try {
                        byteArrayOutputStream.close();
                        return null;
                    } catch (IOException e3) {
                        log("readInputStream " + e3);
                        return null;
                    }
                }
            } catch (Throwable th) {
                byteArrayOutputStream.close();
                throw th;
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e4) {
                log("readInputStream " + e4);
            }
            throw th;
        }
    }

    private void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null) {
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                Rlog.d(LOG_TAG, "cellSignal = " + cellSignalStrength + ", phoneId = " + i);
                if (cellSignalStrength instanceof CellSignalStrengthLte) {
                    CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                    int rssnr = cellSignalStrengthLte.getRssnr();
                    int rsrp = cellSignalStrengthLte.getRsrp();
                    if (this.mModeAction == 1 || !MiuiPhoneUtils.isARDOptimizationSupported() || MiuiPhoneUtils.isARDOptimizationDisabledByCloud() || rsrp == Integer.MAX_VALUE || rssnr == Integer.MAX_VALUE) {
                        return;
                    }
                    if (enhancedSwitchTo4RX()) {
                        boolean z = m4RXActivateFlag;
                        if (z && rssnr >= 9 && rsrp >= -90) {
                            if (!mIsCurrentHstState) {
                                mIsDeactivatedBySignal = true;
                                mDeactivatedPhoneId = i;
                            }
                            processDeactivate4RX(i);
                        } else {
                            if (z) {
                                return;
                            }
                            if ((rssnr > 6 && rsrp > -100) || !isScreenOn() || this.mLowBatteryStatus) {
                                return;
                            }
                            processActivate4RX(i);
                            mEnhanced4RXFlag = true;
                        }
                    } else {
                        this.mArdSwitchCondition = "ardSwitchNormal";
                        boolean z2 = m4RXActivateFlag;
                        if (z2 && ((rssnr >= 9 && rsrp >= -95) || (mEnhanced4RXFlag && rssnr > 6 && rsrp > -105))) {
                            if (!mIsCurrentHstState) {
                                mIsDeactivatedBySignal = true;
                                mDeactivatedPhoneId = i;
                            }
                            processDeactivate4RX(i);
                        } else {
                            if (z2) {
                                return;
                            }
                            if ((rssnr > 6 && rsrp > -105) || !isScreenOn() || this.mLowBatteryStatus) {
                                return;
                            } else {
                                processActivate4RX(i);
                            }
                        }
                    }
                } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                    this.mSASnr = cellSignalStrengthNr.getSsSinr();
                    Rlog.d(LOG_TAG, "mSASnr = " + this.mSASnr);
                    if (MiuiPhoneUtils.isUlMimoOptimizationEnabled()) {
                        handleUlMimoLogic(cellSignalStrengthNr.getSsSinr(), cellSignalStrengthNr.getSsRsrp());
                    }
                }
            }
            return;
        }
        Rlog.d(LOG_TAG, "processSignalStrengthEvent-invalid phone id!");
    }

    private boolean enhancedSwitchTo4RX() {
        if (mHikingState) {
            this.mArdSwitchCondition = "ardSwitchHiking";
            return true;
        }
        if (this.misArdWhiteApp) {
            this.mArdSwitchCondition = "ardSwitchWhiteApp";
            return true;
        }
        if (isHstTunnelEnter()) {
            this.mArdSwitchCondition = "ardSwitchHstTunnel";
            return true;
        }
        if (!isSubwayTunnelEnter()) {
            return false;
        }
        this.mArdSwitchCondition = "ardSwitchSubwayTunnel";
        return true;
    }

    private void processActivate4RX(int i) {
        m4RXActivateFlag = true;
        mIsDeactivatedBySignal = false;
        mDeactivatedPhoneId = -1;
        localLog("processARDSwitch", "ARDSwitchTo4RX, mArdSwitchCondition =" + this.mArdSwitchCondition);
        FiveGManagerBase.getInstance().setModemFeature(11, 1, i);
        this.mArdSwitchTo4RXTime = SystemClock.elapsedRealtime();
        this.mArdSwitchTo4RXCondition = this.mArdSwitchCondition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDeactivate4RX(int i) {
        if (mIsCurrentHstState) {
            localLog("processARDSwitch", "processDeactivate4RX skip in HST mode");
            return;
        }
        mEnhanced4RXFlag = false;
        m4RXActivateFlag = false;
        localLog("processARDSwitch", "ARDSwitchTo2RX, mArdSwitchCondition =" + this.mArdSwitchCondition);
        FiveGManagerBase.getInstance().setModemFeature(11, 0, i);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mArdSwitchTo4RXTime;
        long j2 = (jElapsedRealtime - j) / 1000;
        if (j == 0 || j2 <= 0) {
            return;
        }
        recordArdSwitchStatus(j2);
    }

    private void recordArdSwitchStatus(long j) {
        HashMap map = new HashMap();
        map.put("ARDSwitchTo4RXCondition", this.mArdSwitchTo4RXCondition);
        map.put("ARDSwitchTo2RXCondition", this.mArdSwitchCondition);
        map.put("duration", String.valueOf(j));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_ard_switch", map);
        this.mArdSwitchTo4RXCondition = "";
        this.mArdSwitchCondition = "";
        this.mArdSwitchTo4RXTime = 0L;
    }

    private boolean isSubwayTunnelEnter() {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isSubwayStartStopStateUpdate(getNetworkModeState());
    }

    private int getNetworkModeState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0);
    }

    private boolean isHstTunnelEnter() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "modem_scene_recognition_state", 0) == 3;
    }

    private void processForegroundForARD(ForegroundInfo foregroundInfo) {
        boolean z = false;
        if (foregroundInfo == null || foregroundInfo.mForegroundPackageName == null) {
            this.misArdWhiteApp = false;
            return;
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray("config_ard_packages");
        if (stringArray != null && stringArray.contains(foregroundInfo.mForegroundPackageName)) {
            z = true;
        }
        this.misArdWhiteApp = z;
    }

    private void handleUlMimoLogic(int i, int i2) {
        int ulMimoStatus = getUlMimoStatus();
        Rlog.d(LOG_TAG, "curUlMimoStatus = " + ulMimoStatus + ", ssRsrp = " + i2 + ", ssSnr =" + i);
        if (i2 == Integer.MAX_VALUE || i == Integer.MAX_VALUE) {
            return;
        }
        if (i2 < -100 || i < 6) {
            if (ulMimoStatus != 0) {
                if (isScreenOn()) {
                    mNeedResetUlMimoBySignal = true;
                    return;
                } else {
                    setUlMimoStatus(true);
                    return;
                }
            }
            return;
        }
        boolean z = SystemClock.elapsedRealtime() - this.mLastDisableUlMimoTime <= 600000;
        Rlog.d(LOG_TAG, "isInUlMimoPingpangCheck = " + z + ", mIsTestApp = " + this.mIsTestApp);
        if (ulMimoStatus == 1 || z || this.mIsTestApp) {
            return;
        }
        setUlMimoStatus(false);
        this.mSsSnrForUlMimo = i;
        this.mSsRsrpForUlMimo = i2;
    }

    private boolean isNeedResetUlMimo() {
        return mNeedResetUlMimoBySignal || mNeedResetUlMimoByExpired || mNeedResetUlMimoByCall;
    }

    private void cleanResetUlMimoFlag() {
        mNeedResetUlMimoBySignal = false;
        mNeedResetUlMimoByCall = false;
        mNeedResetUlMimoByExpired = false;
    }

    private void recordUlMimoStatistic(boolean z) {
        if (z) {
            this.mULMimoEnabledCount++;
            return;
        }
        sUlMimoRsrpMap.put(Integer.valueOf(this.mSsRsrpForUlMimo), Integer.valueOf(sUlMimoRsrpMap.containsKey(Integer.valueOf(this.mSsRsrpForUlMimo)) ? ((Integer) sUlMimoRsrpMap.get(Integer.valueOf(this.mSsRsrpForUlMimo))).intValue() : 1));
        sUlMimoSnrMap.put(Integer.valueOf(this.mSsSnrForUlMimo), Integer.valueOf(sUlMimoSnrMap.containsKey(Integer.valueOf(this.mSsSnrForUlMimo)) ? ((Integer) sUlMimoSnrMap.get(Integer.valueOf(this.mSsSnrForUlMimo))).intValue() : 1));
        this.mULMimoDisabledCount++;
    }

    private void startDisableUlMimoExpiredCheck() {
        removeMessages(39);
        sendMessageDelayed(obtainMessage(39), 14400000L);
    }

    protected synchronized void setUlMimoStatus(boolean z) {
        try {
            localLog("setUlMimoStatus", "isEnableUlMimo = " + z + "; mInCall = " + this.mInCall + "; isScreenOn = " + isScreenOn());
            if (!z || (!this.mInCall && !this.mIsWechatVoipForgroud)) {
                FiveGManagerBase.getInstance().setModemFeature(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, z ? 1 : 0, DefaultSimManager.getDefaultDataSlotId());
                cleanResetUlMimoFlag();
                recordUlMimoStatistic(z);
                if (!z) {
                    this.mLastDisableUlMimoTime = SystemClock.elapsedRealtime();
                    startDisableUlMimoExpiredCheck();
                }
                return;
            }
            mNeedResetUlMimoByCall = true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int getUlMimoStatus() {
        return MiuiPhoneUtils.getPlatformModemFeatureStatus(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, DefaultSimManager.getDefaultDataSlotId());
    }

    public long getULMimoDisabledCount() {
        long j = this.mULMimoDisabledCount;
        this.mULMimoDisabledCount = 0L;
        return j;
    }

    public long getULMimoEnabledCount() {
        long j = this.mULMimoEnabledCount;
        this.mULMimoEnabledCount = 0L;
        return j;
    }

    public String getUlMimoRsrp() {
        Map map = sUlMimoRsrpMap;
        if (map == null) {
            return null;
        }
        String str = "";
        for (Integer num : map.keySet()) {
            str = str + num + ":" + sUlMimoRsrpMap.get(num) + " ";
        }
        sUlMimoRsrpMap.clear();
        return str;
    }

    public String getUlMimoSnr() {
        Map map = sUlMimoSnrMap;
        if (map == null) {
            return null;
        }
        String str = "";
        for (Integer num : map.keySet()) {
            str = str + num + ":" + sUlMimoSnrMap.get(num) + " ";
        }
        sUlMimoSnrMap.clear();
        return str;
    }

    public int getSwitchSceneCount(int i) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            return this.mSwitchSceneCount[i];
        }
        return 0;
    }

    public void resetARDOptimizationToDefault() {
        Rlog.d(LOG_TAG, "Reset ard optimization!");
        if (m4RXActivateFlag) {
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            this.mArdSwitchCondition = "ardSwitchReset";
            processDeactivate4RX(defaultDataSlotId);
        }
    }

    public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        if (SystemClock.elapsedRealtime() >= RelayUtils.Relay.RETRY_DELAY_IN_MILLIS && i == DefaultSimManager.getDefaultDataSlotId() && !this.mIsWifiConnected) {
            obtainMessage(34, i, 0, signalStrength).sendToTarget();
        }
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        int i2;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        ServiceState serviceState2 = this.mServiceState[i];
        int dataNetworkType2 = serviceState2 != null ? serviceState2.getDataNetworkType() : dataNetworkType;
        int rilVoiceRadioTechnology = serviceState.getRilVoiceRadioTechnology();
        int i3 = this.mIsInDeSa[i];
        if (i3 == -1 || !((dataNetworkType2 == 13 || dataNetworkType2 == 19) && (dataNetworkType == 20 || dataNetworkType == 30))) {
            i2 = i;
        } else {
            i2 = i;
            recordDeprioritizeOrRestoreSa(false, -2, i3, i2, System.currentTimeMillis() - this.mLastDepriSaTime[i]);
            this.mIsInDeSa[i2] = -1;
        }
        if (i2 == mDeactivatedPhoneId && ((dataNetworkType2 == 13 || dataNetworkType2 == 19) && (dataNetworkType == 20 || dataNetworkType == 30))) {
            mIsDeactivatedBySignal = false;
            mDeactivatedPhoneId = -1;
        }
        this.mServiceState[i2] = serviceState;
        if (MiuiPhoneUtils.isLimitLTECAForNightModeSupported()) {
            boolean[] zArr = this.mIsLteCaChangedFailedForSlot;
            if (i2 < zArr.length && zArr[i2] && dataNetworkType != 0 && dataNetworkType != 13 && dataNetworkType != 19) {
                obtainMessage(38, i2, 0).sendToTarget();
            }
        }
        int i4 = this.mLastRilRat[i2];
        if ((i4 == 20 || i4 == 30) && isRatMatched(rilVoiceRadioTechnology)) {
            resetOptSceneData(i);
        }
        if (this.mIsNsaScgPoorOptimizationSupported && this.mIsNsaScgPoorStatusFlag && i2 == MiuiPhoneUtils.getDataPhone().getPhoneId() && (dataNetworkType == 20 || dataNetworkType == 30 || isNon4GNetworkType(rilVoiceRadioTechnology))) {
            log("reset NSA SCG poor status flag for rat change");
            resetNsascgPoorOptimization();
        }
        if (isSatisfiedFeature(rilVoiceRadioTechnology)) {
            recoverFakestation(i);
        }
        this.mLastRilRat[i2] = rilVoiceRadioTechnology;
    }

    private boolean isSatisfiedFeature(int i) {
        return this.isSupportFakeSaAction && !TelephonyCloudController.getCloudControllerFakeStationOptDisable() && is5GMatched(i);
    }

    public static String getReportedMessageIDs() {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            return "";
        }
        String str = mSleepReportedMessageIds;
        log("getReportedMessageIDs(): " + str);
        FiveGManagerBase.getInstance().sendAtCommand(dataPhone, new String[]{"MIPC_SET_RIL_IDS_CLEAR"}, (Message) null);
        return str;
    }

    public static String getNightReportedMessageIDs() {
        String str = mNightReportedMessageIds;
        log("getNightReportedMessageIDs(): " + mNightReportedMessageIds);
        return str;
    }

    public static String getScreenOffReportedMessageIDs() {
        String str = mScreenOffReportedMessageIds;
        log("getScreenOffReportedMessageIDs(): " + mScreenOffReportedMessageIds);
        return str;
    }

    public static String getRILMessageIDs(int i) {
        if (i != 1) {
            if (i == 2) {
                return getNightReportedMessageIDs();
            }
            if (i == 3) {
                return getScreenOffReportedMessageIDs();
            }
            return "";
        }
        String reportedMessageIDs = getReportedMessageIDs();
        if (reportedMessageIDs != null && !reportedMessageIDs.isEmpty()) {
            String[] strArrSplit = reportedMessageIDs.split("\\|");
            if (strArrSplit.length >= 3) {
                return strArrSplit[2];
            }
            return "";
        }
        return "";
    }

    public static String getNightModeDuration() {
        double d = mNightModeDuration / 1000.0d;
        log("getNightModeDuration(): " + d);
        mNightModeDuration = 0L;
        return String.format("%.2f", Double.valueOf(d));
    }

    public static String getNightModeNoSleepDuration() {
        double d = mNightModeNoSleepDuration / 1000.0d;
        log("getNightModeNoSleepDuration(): " + d);
        mNightModeNoSleepDuration = 0L;
        return String.format("%.2f", Double.valueOf(d));
    }

    public static String getScreenOffDuration() {
        double d = mScreenOffDuration / 1000.0d;
        log("getScreenOffDuration(): " + d);
        mScreenOffDuration = 0L;
        return String.format("%.2f", Double.valueOf(d));
    }

    public static String getAllModeModemSleepSeconds() {
        double d = mModemSleepInSleep / 1000.0d;
        double d2 = mModemSleepInNightNoSleep / 1000.0d;
        double d3 = mModemSleepInScreenOff / 1000.0d;
        mModemSleepInSleep = 0L;
        mModemSleepInNightNoSleep = 0L;
        mModemSleepInScreenOff = 0L;
        String str = String.format("sleep: %s, nightNoSleep: %s, screenOff: %s", Double.valueOf(d), Double.valueOf(d2), Double.valueOf(d3));
        log("getAllModeModemSleepSeconds(): " + str.toString());
        return str;
    }

    private static String getMaxMinAvgFromList(ArrayList arrayList) {
        Double dValueOf = Double.valueOf(0.0d);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 1; i < arrayList.size(); i++) {
            int i2 = i - 1;
            arrayList2.add(Double.valueOf(((((ModemActivityInfo) arrayList.get(i)).getSleepTimeMillis() - ((ModemActivityInfo) arrayList.get(i2)).getSleepTimeMillis()) * 1.0d) / (((ModemActivityInfo) arrayList.get(i)).getTimestampMillis() - ((ModemActivityInfo) arrayList.get(i2)).getTimestampMillis())));
        }
        arrayList.clear();
        return String.format("min_(%.4f), max(%.4f), avg(%.4f)", (Double) arrayList2.stream().min(new Comparator() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda7
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Double.compare(((Double) obj).doubleValue(), ((Double) obj2).doubleValue());
            }
        }).orElse(dValueOf), (Double) arrayList2.stream().max(new Comparator() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda7
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Double.compare(((Double) obj).doubleValue(), ((Double) obj2).doubleValue());
            }
        }).orElse(dValueOf), Double.valueOf(arrayList2.stream().mapToDouble(new ToDoubleFunction() { // from class: com.android.phone.FiveGPowerController$$ExternalSyntheticLambda8
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((Double) obj).doubleValue();
            }
        }).average().orElse(0.0d)));
    }

    public static String getAvgMinMaxModemSleepRatio() {
        StringBuilder sb = new StringBuilder("sleeping:");
        sb.append(getMaxMinAvgFromList(mSleepingModemInfo));
        sb.append(", nightNoSleep:");
        sb.append(getMaxMinAvgFromList(mNightModemInfo));
        sb.append(", screenOff:");
        sb.append(getMaxMinAvgFromList(mScreenOffModemInfo));
        log("getAvgMinMaxModemSleepRatio(): ".concat(sb.toString()));
        return sb.toString();
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    private void recordDeprioritizeOrRestoreSa(boolean z, int i, int i2, int i3, long j) {
        log("recordDepriSa enabled=" + z + "; sceneCurr=" + i + "; scenePre=" + i2 + "; phoneId=" + i3 + ",depriTimeMs=" + j);
        HashMap map = new HashMap();
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i3);
        map.put("UpdateType", Boolean.valueOf(z));
        map.put("TypePowerCurr", Integer.valueOf(i));
        map.put("ScenePowerPre", Integer.valueOf(i2));
        map.put("DdsPhoneId", Integer.valueOf(i3));
        map.put("DepriTimeMs", Long.valueOf(j));
        map.put("Network", simOperatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_deprioritize_or_restore_sa", map);
    }

    private void recordFiveGHostedModeButton() {
        HashMap map = new HashMap();
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", -1);
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(DefaultSimManager.getDefaultDataSlotId());
        log("recordFiveGHostedModeButton enabled=" + i + "simOperatorNumeric=" + simOperatorNumeric);
        map.put("FivegHostedButton", Integer.valueOf(i));
        map.put("Network", simOperatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_fiveg_hosted_button", map);
    }

    private void resetOptSceneData(int i) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            this.mSwitchSceneCount[i] = 0;
        }
    }

    class FiveGPowerDelegateImpl implements IFiveGPowerModule {
        private FiveGPowerDelegateImpl() {
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public int getUlMimoStatus() {
            return FiveGPowerController.this.getUlMimoStatus();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void setUlMimoStatus(boolean z) {
            FiveGPowerController.this.setUlMimoStatus(z);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void resetARDOptimizationToDefault() {
            FiveGPowerController.this.resetARDOptimizationToDefault();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void processLimitLteCaOptimizeByCloud() {
            FiveGPowerController.this.processLimitLteCaOptimizeByCloud();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void resetThermalThrottle() {
            FiveGPowerController.this.resetThermalThrottle();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void updateThermalThrottleSASnr(int i) {
            FiveGPowerController.this.updateThermalThrottleSASnr(i);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getSaOptimizeScene() {
            return FiveGPowerController.this.getSaOptimizeScene();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public int getSwitchSceneCount(int i) {
            return FiveGPowerController.this.getSwitchSceneCount(i);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getModemCrashByNodeFile() {
            return FiveGPowerController.this.getModemCrashByNodeFile();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public boolean[] getEndcPowerEnable() {
            return FiveGPowerController.this.mEndcPowerEnabled;
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public boolean[] getSaPowerEnable() {
            return FiveGPowerController.this.mSaPowerEnabled;
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getNsaScgPoorOptimizationCounts() {
            return FiveGPowerController.this.getNsaScgPoorOptimizationCounts();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getNsaScgPoorFreezeRecoverCounts() {
            return FiveGPowerController.this.getNsaScgPoorFreezeRecoverCounts();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getSilentDisableSaCountForStatis() {
            return FiveGPowerController.this.getSilentDisableSaCountForStatis();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getNightModeDuration() {
            return FiveGPowerController.getNightModeDuration();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getReportedMessageIDs() {
            return FiveGPowerController.getReportedMessageIDs();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getNightReportedMessageIDs() {
            return FiveGPowerController.getNightReportedMessageIDs();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getScreenOffReportedMessageIDs() {
            return FiveGPowerController.getScreenOffReportedMessageIDs();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getNightModeNoSleepDuration() {
            return FiveGPowerController.getNightModeNoSleepDuration();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getScreenOffDuration() {
            return FiveGPowerController.getScreenOffDuration();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getAllModeModemSleepSeconds() {
            return FiveGPowerController.getAllModeModemSleepSeconds();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getAvgMinMaxModemSleepRatio() {
            return FiveGPowerController.getAvgMinMaxModemSleepRatio();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getMimoSceneChangeRxCount() {
            return FiveGPowerController.this.getMimoSceneChangeRxCount().longValue();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getModemSSRCausedByN28Count() {
            return FiveGPowerController.this.getModemSSRCausedByN28Count();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getModemSSRCausedByBwpCount() {
            return FiveGPowerController.this.getModemSSRCausedByBwpCount();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public long getModemSSRCausedByN1Count() {
            return FiveGPowerController.this.getModemSSRCausedByN1Count();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void reportStatistics() {
            FiveGPowerController.this.reportStatistics();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void enableSaAsync(boolean z, int i) {
            FiveGPowerController.this.enableSaAsync(z, NrSceneConstants$Scene.values()[i]);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void switchRxMode(boolean z) {
            FiveGPowerController.this.switchRxMode(z);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getThermalThrottleInfo() {
            return "thermalThrottle2RXCount : " + String.valueOf(FiveGPowerController.this.getThermalThrottle2RXCount()) + ", thermalThrottleDL1Count : " + String.valueOf(FiveGPowerController.this.getThermalThrottleDL1Count()) + ", thermalThrottleDL2Count : " + String.valueOf(FiveGPowerController.this.getThermalThrottleDL2Count()) + ", thermalThrottleDSDACount : " + String.valueOf(FiveGPowerController.this.getThermalThrottleDSDACount()) + ", thermalThrottleSASnr : " + FiveGPowerController.this.getThermalThrottleSASnr() + ", thermalThrottleFGApp : " + FiveGPowerController.this.getThermalThrottleFGApp() + ", hstThermalThrottleCount : " + FiveGPowerController.this.getHstThermalThrottleCount() + ", subwayThermalThrottleCount : " + FiveGPowerController.this.getSubwayThermalThrottleCount();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getUlMimoInfo() {
            return "ulMimoEnabledCount : " + String.valueOf(FiveGPowerController.this.getULMimoEnabledCount()) + ", ulMimoDisabledCount : " + String.valueOf(FiveGPowerController.this.getULMimoDisabledCount()) + ", ulMimoRsrp : " + FiveGPowerController.this.getUlMimoRsrp() + ", ulMimoSnr : " + FiveGPowerController.this.getUlMimoSnr();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getModemPowerSaveInfo() {
            long[] idleNeiMeaModemOptCount = FiveGPowerController.this.getIdleNeiMeaModemOptCount();
            return "ulThrottleOptCount : " + FiveGPowerController.this.getULThrottleOptCount() + ", maxTxPowerCountCount : " + FiveGPowerController.this.getMaxTxPowerOptCount() + ", smartIdleOptCount : " + FiveGPowerController.this.getSmartIdleOptCount() + ", idleNeiMeaOptCount : " + FiveGPowerController.this.getIdleNeiMeaOptCount() + ", nwCaLimitOptCount : " + FiveGPowerController.this.getNwCaLimitOptCount() + ", idleNeiMeaModemOptCountSim1 : " + idleNeiMeaModemOptCount[0] + ", idleNeiMeaModemOptCountSim2 : " + idleNeiMeaModemOptCount[1];
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void setNetworkCapability(String str) {
            FiveGPowerController.this.setNetworkCapability(str);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public boolean getFiveGHostedModeStatus() {
            return FiveGPowerController.this.getFiveGHostedModeStatus();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void triggerSmartNetworkOptimization(boolean z) {
            FiveGPowerController.this.obtainMessage(41, Boolean.valueOf(z)).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public boolean getSaThermalFlag() {
            return FiveGPowerController.this.mSaThermalFlag;
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void enableSaForWechat(boolean z) {
            FiveGPowerController.this.obtainMessage(50, Boolean.valueOf(z)).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void enableSaForStreamMedia(boolean z) {
            FiveGPowerController.this.obtainMessage(58, Boolean.valueOf(z)).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void enableSaForNetRecovery(boolean z, int i) {
            FiveGPowerController.this.enableSaAsync(z, i, NrSceneConstants$Scene.NETWORK_RECOVERY_STRATEGY);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void bindServiceAndEnableEndc(boolean z, int i) {
            try {
                FiveGPowerController.this.bindServiceAndEnableEndc(z, i);
            } catch (RemoteException e) {
                FiveGPowerController.log(e.toString());
            }
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void sendMessage(int i, Object obj) {
            FiveGPowerController.this.obtainMessage(i, obj).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public int getCurrentOosType(int i) {
            return FiveGPowerController.this.currentOosType[i];
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public int getBatteryLevel() {
            return FiveGPowerController.this.getBatteryLevel();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void triggerGameResidentNetworkOptimization(boolean z) {
            FiveGPowerController.this.obtainMessage(60, Boolean.valueOf(z)).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void resetGameResidentNetworkOptimization(boolean z) {
            FiveGPowerController.this.obtainMessage(60, Boolean.valueOf(z)).sendToTarget();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void limitNwCA(boolean z, int i) {
            FiveGPowerController.this.limitNwCA(z, i);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void resetFakeStationList() {
            FiveGPowerController.this.resetFakeStationList();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void resetSleepProtectModeByCloud() {
            FiveGPowerController.this.resetSleepProtectModeByCloud();
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public void reduceIdleNeighborMea(boolean z, int i) {
            FiveGPowerController.this.reduceIdleNeighborMea(z, i);
        }

        @Override // com.android.phone.module.IFiveGPowerModule
        public String getRILMessageIDs(int i) {
            return FiveGPowerController.getRILMessageIDs(i);
        }
    }
}
