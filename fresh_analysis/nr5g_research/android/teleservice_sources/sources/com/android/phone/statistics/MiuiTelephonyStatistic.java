package com.android.phone.statistics;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.devicestate.DeviceState;
import android.hardware.devicestate.DeviceStateManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.service.carrier.CarrierIdentifier;
import android.service.euicc.EuiccProfileInfo;
import android.telecom.VideoProfile;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellLocation;
import android.telephony.MiuiSignalStrengthUtils;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.android.id.IdentifierManager;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.datasub.SmartDualSimSwitch;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.internal.util.ArrayUtils;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import com.android.phone.RilHook;
import com.android.phone.TelephonyJobService;
import com.android.phone.TelephonySensorManager;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.ims.MiuiImsStatistics;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IMiConnectModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.settings.TelephonySettings;
import com.android.phone.statistics.policy.BaseRetryPolicy;
import com.android.phone.statistics.telmqs.MqsasTool;
import com.android.phone.statistics.ui.ModemLogDialog;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.DeviceUtil;
import com.android.phone.statistics.utils.DigestUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.streaming.StreamingOptimizationStats;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.FoldStateMonitor;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.MiuiCurrentStateMonitor;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.ot.pubsub.g.f;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemSarController;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.settingsdk.backup.SettingsBackupConsts;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiDisplayInfoController;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miuix.core.util.SystemProperties;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTelephonyStatistic {
    private static boolean DBG = false;
    private static final int DELAY_GET_MD_CALL_REPORT;
    private static final int[] DURATION_BUCKETS_MS;
    private static final List SENSITIVE_KEYS;
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private static final String VOIP_PACKAGENAME;
    private static final String[] mExcludedErrorCodes;
    private static NetworkCallback mNetworkCallback;
    private static TelephonyCallBack mTelephonyCallBack;
    private static volatile MiuiTelephonyStatistic sInstance;
    private static final HashMap sLocalLogs;
    ModemLogDialog.DialogCallBack MdlogCallBack;
    private HashSet historyCall;
    private boolean isDataStall;
    private long lastAllSceneDsiTime;
    private int lastDsi;
    private long lastGameSceneDsiTime;
    private long lastSingleHandSceneDsiTime;
    private long mAppStallDuration;
    private long mAppStallStartTime;
    private long mAppUsageDuration;
    private long mAppUsageStartTime;
    private int mAutoCaptureLogMask;
    private BaseRetryPolicy mBaseRetryPolicy;
    private MiuiCallStat mCallStat;
    private IccCardStatus.CardState[] mCardState;
    private CellIdentity[] mCellIdentity;
    private CellLocation[] mCellLocation;
    private Context mContext = PhoneGlobals.getInstance();
    private ArrayList mCumulativeHstRachDiff;
    private String mCurrentForegroundPackageName;
    private boolean mCurrentHstState;
    private final CurrentTopAppObserver mCurrentTopAppObserver;
    private long mDataConnectionStartTime;
    private long mDataConnectionStateDuration;
    private DataEvaluation mDataEvaluation;
    private ContentObserver mDataRecoveryDBObserver;
    private DataStat mDataStat;
    private long mDcBlockedStartTime;
    private long mDdsOosDuration;
    private final int[] mDefaultApnFailureDurationCounts;
    private int mDefaultApnRequestCount;
    private final int[] mDefaultApnSuccessDurationCounts;
    private DeviceStateManager mDeviceStateManager;
    public DriveModemStats mDriveModemStats;
    private ConcurrentHashMap mDsiStats;
    private int mDualDataActivateCount;
    private List mDualDataActivateDurations;
    private List mDualDataActivatePkgNames;
    private ArrayList mEnterHstRachData;
    private final BaseEvent.IEventListener mEventListener;
    private ArrayList mExitHstRachData;
    private HashMap mFakeDataSnapshot;
    private int mFakeDataSnapshotPhoneId;
    private long mFakeDataSnapshotSaveTime;
    private String mFakeDataTrigger;
    private ConcurrentHashMap mFcnStats;
    private FiveGServiceInfo[] mFiveGInfo;
    private FiveGServiceInfoListener mFiveGServiceInfoListener;
    private FoldStateListener mFoldStateListener;
    private int[] mForegroundCallState;
    private HandlerExecutor mHandlerExecutor;
    public HikingModemStats mHikingModemStats;
    public HstModemStats mHstModemStats;
    public HstTunnelModemStats mHstTunnelModemStats;
    private String mIMEI;
    private String mIMEIMD5;
    private String[] mIccidSS;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsAbnormalCell;
    private boolean mIsAirPlaneMode;
    private boolean mIsAutoLogPropVerified;
    private boolean mIsDcBlocked;
    private boolean mIsDualSimInserted;
    private boolean mIsEmmTimerAirPlaneModeOn;
    private boolean mIsHandSensorReady;
    private boolean mIsHotplug;
    private boolean mIsLLMEnabled;
    private boolean mIsLteEmmTimerStart;
    private Map mIsLteEmmTimerStartStatistic;
    private boolean[] mIsNoLteNrTimerStarted;
    private boolean mIsScreenOn;
    private boolean mIsSingleHandApp;
    private boolean mIsSleepMode;
    private boolean mIsStallRatePeriod;
    private boolean[] mIsVirtualSim;
    private boolean mIsWechatVideo;
    private boolean mIsWechatVoip;
    private boolean mIsmotionless;
    private boolean mIsshutdown;
    private int[] mLastArfcn;
    private int[] mLastBand;
    private long mLastDataCallErrorTime;
    private String[] mLastDataCallFailure;
    private String[] mLastDataDataEvaluationFailure;
    private int mLastDdsPhoneId;
    private long mLastDrop5GTime;
    private long mLastDualDataActivateTime;
    private int mLastDualDataStatus;
    private String mLastForegroundPackageName;
    private NetworkInfo mLastNetworkInfo;
    private int[] mLastNrArfcn;
    private int[] mLastNrBand;
    private long[] mLastOosTime;
    private long mLastSatelliteDisableTime;
    private String[] mLastSimStateWithNetworkState;
    private int mLastVideoQualityStatus;
    private String[] mLastVoLTERegisteredPlmn;
    private long[] mLastVoLTERegisteredTime;
    private long mLteEmmTimerStartTime;
    private MiuiAppStateMonitor mMiuiAppStateMonitor;
    private MiuiCurrentStateMonitor mMiuiCurrentStateMonitor;
    private int mMobileConnectedTime;
    private ModemPowerStatistic mModemPowerStatistic;
    private boolean mModemRegisterState;
    private ModemStats mModemStats;
    private Set mMtbTargetEvents;
    private int mNetworkType;
    private String mNetworkTypeName;
    private long[] mNoLteNrCounter;
    private long[] mNoLteNrDuration;
    private List mNoLteNrDurations;
    private String mNoLteNrRat;
    private String mNoLteNrVplmn;
    private long mNonDdsOosDuration;
    public NsaModemStats mNsaModemStats;
    private Map mNtnDataStatistic;
    private int[] mNwOosCount;
    private long[] mNwOosDuration;
    private List mNwOosDurations;
    private long[] mNwOosNoLimitOccurrenceTime;
    private long[] mNwOosStartTime;
    private long[] mNwSearchDuration;
    private List mNwSearchDurations;
    private int mNwShortDurationOosCount;
    private int mOldDataConnectionState;
    private String mOosBand;
    private String mOosRat;
    private int mOosRsrp;
    private int mOosSnr;
    private String[] mPlmn;
    private boolean mPreSatelliteEnable;
    private DefaultPreferenceHelper mPreference;
    private boolean mRecordRsrpAfterGameEnd;
    private int mRecoveryAction;
    private RoamingModemStats mRoamingModemStats;
    private ContentObserver mSatelliteObserver;
    private final SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private Sensor mSensorSubstanceHand;
    private ServiceState[] mServiceState;
    private long mSetupDataSucceedConnectedTime;
    private SignalStrength[] mSignalStrength;
    private int mSimAbsentId;
    private SimStatistic mSimStatistic;
    private long mStartUseNtnDataTime;
    private MiuiTelephonyStatisticHandler mStatisticHandler;
    private List mStatisticListener;
    public SubwayModemStats mSubwayModemStats;
    private boolean mSupportFiveG;
    private TeleDataCallBack mTeleDataCallBack;
    private TelephonyManager mTelephonyManager;
    private Dialog mUserPermissionDialog;
    private ConcurrentHashMap mVoLTERegisteredDuration;
    private VolteRegisterMonitor mVolteRegisterMonitor;
    private long mWeChatHangUpStartTime;
    private long mWeChatRestartVoipTime;
    private int mWifiConnectedTime;
    private HashMap mWifiToCellularDurations;
    private final HandlerThread mWorkThread;
    private long mlastNetworkInfoSwitchTime;
    private SignalStrengthMonitor[] signalStrengthMonitor;
    private ArrayList simAbnormalList;

    enum NumberType {
        UNKNOWN,
        ORDINARY,
        EMERGENCY,
        SERVICE
    }

    public interface StatisticListener {
        HashMap getStatisticListener();
    }

    private boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    static {
        DELAY_GET_MD_CALL_REPORT = ModemProvider.IS_QCOM ? 1000 : ResponseObject.RELAY_FAILED_CODE;
        mTelephonyCallBack = null;
        mNetworkCallback = null;
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
        sLocalLogs = new HashMap();
        VOIP_PACKAGENAME = HyperOSCustFeatureResolve.getString("config_voip_packages", "");
        DURATION_BUCKETS_MS = new int[]{2000, 5000, 10000, 15000, 20000, 25000, 30000, 35000, 40000};
        SENSITIVE_KEYS = Arrays.asList("c", "p", "CellLocation");
        mExcludedErrorCodes = new String[]{"0x88E", "0x1A", "0x840", "0x833", "0x848", "0x842"};
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MiuiTelephonyStatistic dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("MiuiTelephonyStatistic dump end------");
        printWriter.flush();
    }

    class HandlerExecutor implements Executor {
        private final Handler mHandler;

        HandlerExecutor(Handler handler) {
            this.mHandler = handler;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    class FoldStateListener implements DeviceStateManager.DeviceStateCallback {
        private int[] mFoldedDeviceStates;

        FoldStateListener(Context context) {
            Resources system = Resources.getSystem();
            this.mFoldedDeviceStates = system.getIntArray(system.getIdentifier("config_foldedDeviceStates", "array", "android"));
        }

        public final void onDeviceStateChanged(DeviceState deviceState) throws Throwable {
            ArrayUtils.contains(this.mFoldedDeviceStates, deviceState.getIdentifier());
            if (SubscriptionManager.isValidPhoneId(MiuiTelephonyStatistic.this.mLastDdsPhoneId)) {
                MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.this;
                miuiTelephonyStatistic.updateNwSignalInfo(miuiTelephonyStatistic.mLastDdsPhoneId, null, null, true);
                if (MiuiTelephonyStatistic.this.mFiveGInfo[MiuiTelephonyStatistic.this.mLastDdsPhoneId] != null) {
                    MiuiTelephonyStatistic miuiTelephonyStatistic2 = MiuiTelephonyStatistic.this;
                    miuiTelephonyStatistic2.updateFiveGInfo(miuiTelephonyStatistic2.mLastDdsPhoneId, MiuiTelephonyStatistic.this.mFiveGInfo[MiuiTelephonyStatistic.this.mLastDdsPhoneId], true);
                }
            }
        }
    }

    public static MiuiTelephonyStatistic make() {
        if (sInstance == null) {
            synchronized (MiuiTelephonyStatistic.class) {
                try {
                    if (sInstance == null && shouldStatistic()) {
                        sInstance = new MiuiTelephonyStatistic();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public static MiuiTelephonyStatistic getInstance() {
        return sInstance;
    }

    public static boolean shouldStatistic() {
        if (!DeviceUtil.isLegalReleaseVersion() || !DeviceUtil.isValidVersion()) {
            Utils.logW("MiuiTelephonyStatistic", "Version & Model return!");
            return false;
        }
        if (!Utils.isUserExperienceProgramEnabled(PhoneGlobals.getInstance()) && !DeviceUtil.isXmsRecordEnabled()) {
            Utils.logW("MiuiTelephonyStatistic", "User Setting return!");
            return false;
        }
        if (isStatisEnabled(PhoneGlobals.getInstance())) {
            return true;
        }
        Utils.logW("MiuiTelephonyStatistic", "Cloud Control return!");
        return false;
    }

    private void dispose() {
        Utils.logI("MiuiTelephonyStatistic", "dispose");
        UiccController.getInstance().unregisterForIccChanged(this.mStatisticHandler);
        BroadcastReceiver broadcastReceiver = this.mIntentReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
        unregisterTelephonyCallBack();
        unregisterNetworkCallback();
        if (this.mFiveGServiceInfoListener != null) {
            unregister5GServiceInfoListener();
        }
        DeviceStateManager deviceStateManager = this.mDeviceStateManager;
        if (deviceStateManager != null) {
            deviceStateManager.unregisterCallback(this.mFoldStateListener);
            this.mFoldStateListener = null;
            this.mHandlerExecutor = null;
        }
        clearTelephonyStatisticData();
        TelephonyJobService.cancelReportJobSchedule(PhoneGlobals.getInstance());
        unRegisterBlockedNwRecoveryActionObserver();
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            unRegisterSatelliteObserver();
        }
        unregisterModemRestart();
        unregisterTeleDataCallBack();
        regAppStateMonitor(false);
        BaseRetryPolicy baseRetryPolicy = this.mBaseRetryPolicy;
        if (baseRetryPolicy != null) {
            baseRetryPolicy.resetAll();
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            SignalStrengthMonitor signalStrengthMonitor = this.signalStrengthMonitor[i];
            if (signalStrengthMonitor != null) {
                signalStrengthMonitor.dispose();
            }
        }
        SimStatistic simStatistic = this.mSimStatistic;
        if (simStatistic != null) {
            simStatistic.dispose();
        }
        sInstance = null;
    }

    private MiuiTelephonyStatistic() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mCellLocation = new CellLocation[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mFiveGInfo = new FiveGServiceInfo[i];
        this.mLastVoLTERegisteredTime = new long[i];
        this.mLastVoLTERegisteredPlmn = new String[i];
        this.mVoLTERegisteredDuration = new ConcurrentHashMap();
        this.mNwOosCount = new int[i];
        this.mNwOosStartTime = new long[i];
        this.mLastDataCallFailure = new String[i];
        this.mLastDataDataEvaluationFailure = new String[i];
        this.mOldDataConnectionState = 0;
        this.mDataConnectionStartTime = 0L;
        this.mDataConnectionStateDuration = 0L;
        this.lastDsi = -1;
        this.lastAllSceneDsiTime = 0L;
        this.lastGameSceneDsiTime = 0L;
        this.lastSingleHandSceneDsiTime = 0L;
        this.mIsAirPlaneMode = false;
        this.mModemRegisterState = false;
        this.mUserPermissionDialog = null;
        this.mLastNetworkInfo = null;
        this.mWifiToCellularDurations = new HashMap();
        this.mForegroundCallState = new int[i];
        this.mPreference = null;
        this.mVolteRegisterMonitor = null;
        this.mCardState = new IccCardStatus.CardState[i];
        this.mPlmn = new String[i];
        this.mIsHotplug = false;
        this.mDcBlockedStartTime = 0L;
        this.mIsDcBlocked = false;
        this.mFakeDataSnapshot = null;
        this.mFakeDataSnapshotPhoneId = -1;
        this.mFakeDataSnapshotSaveTime = 0L;
        this.mDdsOosDuration = 0L;
        this.mNonDdsOosDuration = 0L;
        this.mLastOosTime = new long[i];
        this.mLastSimStateWithNetworkState = new String[i];
        this.mLastDdsPhoneId = -1;
        this.mRecoveryAction = -1;
        this.mFakeDataTrigger = "TELEPHONY_CALL_BACK";
        this.mIMEI = "";
        this.mIMEIMD5 = "";
        this.mSimAbsentId = -1;
        this.mModemStats = new ModemStats();
        this.mRoamingModemStats = new RoamingModemStats();
        this.mIsLLMEnabled = false;
        this.mIsWechatVoip = false;
        this.mIsWechatVideo = false;
        this.mRecordRsrpAfterGameEnd = false;
        this.mLastBand = new int[i];
        this.mLastNrBand = new int[i];
        this.mLastArfcn = new int[i];
        this.mLastNrArfcn = new int[i];
        this.mNetworkType = -1;
        this.mNetworkTypeName = null;
        this.mIccidSS = new String[i];
        this.mIsVirtualSim = new boolean[i];
        this.mIsDualSimInserted = false;
        this.mSupportFiveG = false;
        this.mNwOosDuration = new long[i];
        this.mNwOosNoLimitOccurrenceTime = new long[i];
        this.mNwOosDurations = new ArrayList();
        this.mNwSearchDuration = new long[i];
        this.mNwSearchDurations = new ArrayList();
        this.mOosRat = "";
        this.mOosRsrp = -141;
        this.mOosBand = "";
        this.mOosSnr = -24;
        this.mDualDataActivateCount = 0;
        this.mNwShortDurationOosCount = 0;
        this.mDualDataActivateDurations = new ArrayList();
        this.mDualDataActivatePkgNames = new ArrayList();
        this.mLastDualDataActivateTime = -1L;
        this.mLastDualDataStatus = 0;
        this.mLastDataCallErrorTime = -1L;
        this.mPreSatelliteEnable = false;
        this.mLastSatelliteDisableTime = -1L;
        this.mWeChatHangUpStartTime = 0L;
        this.mWeChatRestartVoipTime = 0L;
        this.mIsStallRatePeriod = false;
        this.mLastVideoQualityStatus = -1;
        this.mAppStallDuration = 0L;
        this.mAppUsageDuration = 0L;
        this.mAppStallStartTime = 0L;
        this.mAppUsageStartTime = 0L;
        this.mCurrentForegroundPackageName = "";
        this.mLastForegroundPackageName = "";
        this.mCellIdentity = new CellIdentity[i];
        this.mFcnStats = new ConcurrentHashMap();
        this.mDsiStats = new ConcurrentHashMap();
        this.mDataStat = null;
        this.mDataEvaluation = null;
        this.mMobileConnectedTime = 0;
        this.mWifiConnectedTime = 0;
        this.mIsshutdown = false;
        this.mlastNetworkInfoSwitchTime = -1L;
        this.mIsSleepMode = false;
        this.mStatisticListener = new ArrayList();
        this.historyCall = new HashSet();
        this.mTeleDataCallBack = null;
        this.mSetupDataSucceedConnectedTime = 0L;
        this.mIsAbnormalCell = false;
        this.mIsmotionless = true;
        this.simAbnormalList = new ArrayList();
        this.mIsNoLteNrTimerStarted = new boolean[i];
        this.mNoLteNrDurations = new ArrayList();
        this.mNoLteNrDuration = new long[i];
        this.mNoLteNrVplmn = "";
        this.mNoLteNrRat = "";
        this.mNoLteNrCounter = new long[i];
        this.mLastDrop5GTime = 0L;
        this.mIsAutoLogPropVerified = false;
        this.mAutoCaptureLogMask = -1;
        this.mMtbTargetEvents = new HashSet(Arrays.asList(915200010, 915000004, 915000006, 915100012, 915400002, 915700002, 915100015));
        this.mIsLteEmmTimerStart = false;
        this.mIsEmmTimerAirPlaneModeOn = false;
        this.mLteEmmTimerStartTime = 0L;
        this.mIsLteEmmTimerStartStatistic = new HashMap();
        this.mStartUseNtnDataTime = 0L;
        this.mNtnDataStatistic = new HashMap();
        this.mDefaultApnRequestCount = 0;
        int[] iArr = DURATION_BUCKETS_MS;
        this.mDefaultApnSuccessDurationCounts = new int[iArr.length + 1];
        this.mDefaultApnFailureDurationCounts = new int[iArr.length + 1];
        this.mCurrentHstState = false;
        this.mEnterHstRachData = null;
        this.mExitHstRachData = null;
        this.mCumulativeHstRachDiff = new ArrayList();
        this.signalStrengthMonitor = new SignalStrengthMonitor[i];
        this.mSimStatistic = null;
        this.mCurrentTopAppObserver = new CurrentTopAppObserver();
        this.mIsHandSensorReady = false;
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                Utils.logD("MiuiTelephonyStatistic", "mIntentReceiver action=".concat(action));
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(7, intent).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.ACTION_IMS_REGISTED")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(9, intent).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.this;
                    miuiTelephonyStatistic.mIsAirPlaneMode = Settings.Global.getInt(miuiTelephonyStatistic.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(8, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.CLOUD_MODEM_LOG")) {
                    int intExtra = intent.getIntExtra("ruleEventId", -1);
                    if (intExtra != -1) {
                        MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(15, Integer.valueOf(intExtra)).sendToTarget();
                        return;
                    }
                    return;
                }
                if (action.equals("telephonyStatistics.intent.debugall")) {
                    MiuiTelephonyStatistic.DBG = intent.getBooleanExtra("debug", false);
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(100, Boolean.valueOf(intent.getBooleanExtra("debug_all", false))).sendToTarget();
                    String stringExtra = intent.getStringExtra("dataStatType");
                    if (stringExtra != null) {
                        int intExtra2 = intent.getIntExtra("phoneId", 0);
                        try {
                            TelephonyStatAdapter.DataStatType dataStatTypeValueOf = TelephonyStatAdapter.DataStatType.valueOf(stringExtra);
                            Utils.logD("MiuiTelephonyStatistic", "debug_fake_data: type=" + stringExtra + " phoneId=" + intExtra2);
                            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(5, dataStatTypeValueOf.ordinal(), intExtra2, new HashMap()).sendToTarget();
                            return;
                        } catch (IllegalArgumentException unused) {
                            Utils.logE("MiuiTelephonyStatistic", "debug_fake_data: invalid dataStatType=".concat(stringExtra));
                            return;
                        }
                    }
                    return;
                }
                if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.sendEmptyMessage(19);
                    MiuiTelephonyStatistic.this.mIsshutdown = true;
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(24, intent).sendToTarget();
                    return;
                }
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(54, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.WECHAT_VOIP_SCENE")) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(55, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.CALL_TIME_OUT".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(62, intent).sendToTarget();
                    return;
                }
                if ("android.telecom.action.DEFAULT_DIALER_CHANGED".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(64, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.ACCESS_TELECOMM_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(65, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.STK_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(66, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.SS_OPTIMIZATION_SCENE_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(67, intent).sendToTarget();
                    return;
                }
                if ("com.anrdoid.internal.action.SEND_SMS_RESULT_EVENT".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(68, intent).sendToTarget();
                    return;
                }
                if ("com.android.internal.action.VERIFICATION_CODE".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(70, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.SIGNAL_STRENGTH_BIG_CHANGE".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(71, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.SIGNAL_SHAKE".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(72, intent).sendToTarget();
                    return;
                }
                if ("android.intent.action.ANY_DATA_STATE".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.sendEmptyMessage(74);
                    return;
                }
                if ("com.miui.powerkeeper_sleep_changed".equals(intent.getAction())) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(76, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.RIL_WAKE_LOCK_REQUEST".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(77, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.SEAMLEASS_SWITCH_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(78, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.ACTION_BADCELL_STATISTIC_DUMP".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(79, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.RADIO_STALL_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(81, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.NITZ_TIMEZONE_PREFERRED_OPTIMIZATION".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(82, intent).sendToTarget();
                    return;
                }
                if ("miui.intent.action.VIDEO_INFO".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(84, intent).sendToTarget();
                    return;
                }
                if (ModemSarController.BC_INTENT_ACTION_DSI_VALUE_CHANGED.equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(88, intent).sendToTarget();
                    return;
                }
                if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    MiuiTelephonyStatistic.this.mIsshutdown = false;
                } else if ("miui.intent.action.TIMEZONE_DETECTION_WITH_NULL_NITZ_STATISTIC".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(91, intent).sendToTarget();
                } else if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                    MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(92, intent).sendToTarget();
                }
            }
        };
        this.mSensorEventListener = new SensorEventListener() { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.2
            @Override // android.hardware.SensorEventListener
            public final void onAccuracyChanged(Sensor sensor, int i2) {
            }

            @Override // android.hardware.SensorEventListener
            public final void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == 33171120) {
                    int i2 = (int) sensorEvent.values[0];
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.this;
                    if (i2 == -1) {
                        miuiTelephonyStatistic.mIsmotionless = true;
                        if (MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime != 0) {
                            long j = (jElapsedRealtime - MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime) / 1000;
                            MiuiTelephonyStatistic miuiTelephonyStatistic2 = MiuiTelephonyStatistic.this;
                            miuiTelephonyStatistic2.recordDsiStats("single_hand_dsi_statistic", j, miuiTelephonyStatistic2.lastDsi);
                            MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime = 0L;
                            return;
                        }
                        return;
                    }
                    miuiTelephonyStatistic.mIsmotionless = false;
                    if (MiuiTelephonyStatistic.this.mIsSingleHandApp && MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime == 0) {
                        MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime = jElapsedRealtime;
                    }
                }
            }
        };
        this.MdlogCallBack = new ModemLogDialog.DialogCallBack() { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.3
            @Override // com.android.phone.statistics.ui.ModemLogDialog.DialogCallBack
            public void onPositivePressed(int i2) {
                MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(16, Integer.valueOf(i2)).sendToTarget();
            }

            @Override // com.android.phone.statistics.ui.ModemLogDialog.DialogCallBack
            public void onNagtivePressed() {
                MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(17).sendToTarget();
            }
        };
        this.mDataRecoveryDBObserver = new ContentObserver(this.mStatisticHandler) { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                int i2 = Settings.System.getInt(MiuiTelephonyStatistic.this.mContext.getContentResolver(), "radio.data.stall.recovery.action", -1);
                if (i2 != 0) {
                    MiuiTelephonyStatistic.this.mRecoveryAction = i2;
                }
                Utils.logD("MiuiTelephonyStatistic", "DataRecovery old=" + MiuiTelephonyStatistic.this.mRecoveryAction + ",new=" + i2);
            }
        };
        this.mSatelliteObserver = new ContentObserver(this.mStatisticHandler) { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean satelliteEnable = MiuiTelephonyStatistic.this.getSatelliteEnable();
                Utils.logD("MiuiTelephonyStatistic", "satelliteEnable = " + satelliteEnable + ", mPreSatelliteEnable = " + MiuiTelephonyStatistic.this.mPreSatelliteEnable);
                if (!satelliteEnable && MiuiTelephonyStatistic.this.mPreSatelliteEnable) {
                    MiuiTelephonyStatistic.this.mLastSatelliteDisableTime = SystemClock.elapsedRealtime();
                    MiuiTelephonyStatistic.this.mStatisticHandler.sendEmptyMessageDelayed(93, 180000L);
                } else if (satelliteEnable && !MiuiTelephonyStatistic.this.mPreSatelliteEnable) {
                    Utils.logD("MiuiTelephonyStatistic", "ignore satellite is disabled recently!");
                    MiuiTelephonyStatistic.this.mStatisticHandler.removeMessageWithCount(93);
                }
                MiuiTelephonyStatistic.this.mPreSatelliteEnable = satelliteEnable;
            }
        };
        this.mEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.MiuiTelephonyStatistic.6
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(89, displayEvent).sendToTarget();
                        return;
                    }
                    return;
                }
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                    return;
                }
                MiuiTelephonyStatistic.this.mStatisticHandler.sendMessageDelayed(MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(83, foregroundInfo), 1000L);
            }
        };
        HandlerThread handlerThread = new HandlerThread("MiuiTelephonyStatistic", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = new MiuiTelephonyStatisticHandler(handlerThread.getLooper());
        this.mStatisticHandler = miuiTelephonyStatisticHandler;
        miuiTelephonyStatisticHandler.obtainMessage(6).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mSupportFiveG = MiuiPhoneUtils.isFiveGCapable();
        this.mPreference = DefaultPreferenceHelper.getInstance();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mVolteRegisterMonitor = VolteRegisterMonitor.getInstance(this.mContext);
        CallManager.getInstance(this.mContext).registerForDisconnect(this.mStatisticHandler, 1, (Object) null);
        UiccController.getInstance().registerForIccChanged(this.mStatisticHandler, 18, (Object) null);
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        registerBlockedNwRecoveryActionObserver();
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            registerSatelliteObserver();
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            this.mCellIdentity[i] = phone.getCurrentCellIdentity();
            this.mLastVoLTERegisteredTime[i] = -1;
            this.mLastVoLTERegisteredPlmn[i] = "NoSim";
            this.mServiceState[i] = new ServiceState();
            this.mSignalStrength[i] = new SignalStrength();
            this.mCellLocation[i] = getEmptyCellLocation(phone.getPhoneType());
            initNwOosValue(i);
            this.mForegroundCallState[i] = 0;
            this.mCardState[i] = IccCardStatus.CardState.CARDSTATE_ERROR;
            this.mLastDataCallFailure[i] = "NA";
            this.mLastDataDataEvaluationFailure[i] = "NA";
            this.mLastOosTime[i] = -1;
            this.mLastSimStateWithNetworkState[i] = "NoSim-1";
            this.mLastBand[i] = -1;
            this.mLastNrBand[i] = -1;
            this.mLastArfcn[i] = -1;
            this.mLastNrArfcn[i] = -1;
            this.mIccidSS[i] = "";
            this.mPlmn[i] = "";
            this.mIsVirtualSim[i] = false;
            this.mNwOosDuration[i] = -1;
            this.mNwOosNoLimitOccurrenceTime[i] = -1;
            this.mNwSearchDuration[i] = -1;
            phone.mCi.registerForRadioStateChanged(this.mStatisticHandler, 59, Integer.valueOf(i));
            this.signalStrengthMonitor[i] = new SignalStrengthMonitor(i);
            initNoLteNrValue(i);
            this.mNoLteNrCounter[i] = 0;
        }
        this.mSimStatistic = new SimStatistic(this.mContext);
        SpecialNetworkStat.init(this.mContext);
        ModemComponentStats.init(this.mContext);
        MiuiImsStatistics.init();
        if (MiuiPhoneUtils.isHikingOptimizationSupported()) {
            this.mHikingModemStats = new HikingModemStats();
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mHstModemStats = new HstModemStats();
            this.mHstTunnelModemStats = new HstTunnelModemStats();
        }
        if (MiuiPhoneUtils.isSubwayOptimizationSupported()) {
            this.mSubwayModemStats = new SubwayModemStats();
        }
        if (MiuiPhoneUtils.isDriveOptimizationSupported()) {
            this.mDriveModemStats = new DriveModemStats();
        }
        reloadStatisticDataFromSP();
        TelephonyJobService.initReportJobSchedule(this.mContext);
        registerReceiver();
        registerTelephonyCallBack();
        registerNetworkCallback();
        registerModemRestart();
        registerTeleDataCallBack();
        registerForegroundInfoListener();
        regAppStateMonitor(true);
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mEventListener);
        if (MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) {
            this.mDeviceStateManager = (DeviceStateManager) this.mContext.getSystemService(DeviceStateManager.class);
            this.mFoldStateListener = new FoldStateListener(this.mContext);
            HandlerExecutor handlerExecutor = new HandlerExecutor(new Handler(Looper.getMainLooper()));
            this.mHandlerExecutor = handlerExecutor;
            DeviceStateManager deviceStateManager = this.mDeviceStateManager;
            if (deviceStateManager != null) {
                deviceStateManager.registerCallback(handlerExecutor, this.mFoldStateListener);
            }
        }
        this.mPreference.updateMdlogFromSP();
        this.mIsAirPlaneMode = isAirPlaneModeOn();
        this.mLastDdsPhoneId = getDefaultDataSlotId();
        this.mBaseRetryPolicy = new BaseRetryPolicy();
        this.mIMEIMD5 = getIMEIMD5();
        this.mIsLLMEnabled = false;
        if (this.mSupportFiveG) {
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                this.mFiveGInfo[i2] = new FiveGServiceInfo();
            }
            register5GServiceInfoListener();
            this.mNsaModemStats = new NsaModemStats();
        }
        this.mCallStat = MiuiCallStat.getInstance(this.mContext);
        this.mMiuiCurrentStateMonitor = MiuiCurrentStateMonitor.getInstance();
        int integer = this.mContext.getResources().getInteger(R.integer.config_hydra_init_delay);
        this.mStatisticHandler.sendEmptyMessageDelayed(4, integer);
        this.mModemPowerStatistic = new ModemPowerStatistic(this.mContext);
        Utils.logI("MiuiTelephonyStatistic", "hydra init delay: " + integer);
        Utils.logI("MiuiTelephonyStatistic", "init done");
    }

    protected void regAppStateMonitor(boolean z) {
        MiuiAppStateMonitor miuiAppStateMonitor;
        Utils.logI("MiuiTelephonyStatistic", "regAppStateMonitor " + z);
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

    private void registerModemRestart() {
        Phone phone = MiuiPhoneUtils.getPhone(0);
        if (phone == null || this.mModemRegisterState) {
            return;
        }
        phone.mCi.registerForModemReset(this.mStatisticHandler, 57, (Object) null);
        this.mModemRegisterState = true;
    }

    private void unregisterModemRestart() {
        Phone phone = MiuiPhoneUtils.getPhone(0);
        if (phone == null || !this.mModemRegisterState) {
            return;
        }
        phone.mCi.unregisterForModemReset(this.mStatisticHandler);
        this.mModemRegisterState = false;
    }

    private void register5GServiceInfoListener() {
        this.mFiveGServiceInfoListener = new FiveGServiceInfoListener();
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGServiceInfoListener);
    }

    private void unregister5GServiceInfoListener() {
        EventHubManager.getInstance().unregisterListener(this.mFiveGServiceInfoListener);
        this.mFiveGServiceInfoListener = null;
    }

    private void registerTelephonyCallBack() {
        if (mTelephonyCallBack == null) {
            Utils.logI("MiuiTelephonyStatistic", "new  TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (connectivityManager == null || mNetworkCallback != null) {
            return;
        }
        Utils.logI("MiuiTelephonyStatistic", "new  NetworkCallback");
        mNetworkCallback = new NetworkCallback();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(1);
        builder.addTransportType(0);
        builder.addCapability(12);
        builder.addCapability(16);
        connectivityManager.registerNetworkCallback(builder.build(), mNetworkCallback);
    }

    private void unregisterTelephonyCallBack() {
        TelephonyCallBack telephonyCallBack = mTelephonyCallBack;
        if (telephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
            mTelephonyCallBack = null;
        }
    }

    private void unregisterNetworkCallback() {
        NetworkCallback networkCallback;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (connectivityManager == null || (networkCallback = mNetworkCallback) == null) {
            return;
        }
        connectivityManager.unregisterNetworkCallback(networkCallback);
        mNetworkCallback = null;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_IMS_REGISTED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.CLOUD_MODEM_LOG");
        intentFilter.addAction("telephonyStatistics.intent.debugall");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("miui.intent.action.NETWORK_CONNECTED");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("miui.intent.action.ACTIVE_RE_REGISTRATION_NETWORK");
        intentFilter.addAction("miui.intent.action.CALL_TIME_OUT");
        intentFilter.addAction("android.telecom.action.DEFAULT_DIALER_CHANGED");
        intentFilter.addAction("miui.intent.action.ACCESS_TELECOMM_STATISTIC");
        intentFilter.addAction("miui.intent.action.STK_STATISTIC");
        intentFilter.addAction("miui.intent.action.SS_OPTIMIZATION_SCENE_STATISTIC");
        intentFilter.addAction("com.anrdoid.internal.action.SEND_SMS_RESULT_EVENT");
        intentFilter.addAction("com.android.internal.action.VERIFICATION_CODE");
        intentFilter.addAction("com.android.phone.intent.action.SIGNAL_STRENGTH_BIG_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.SIGNAL_SHAKE");
        intentFilter.addAction("android.intent.action.ANY_DATA_STATE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        intentFilter.addAction("com.android.phone.intent.action.RIL_WAKE_LOCK_REQUEST");
        intentFilter.addAction("com.android.phone.intent.action.SEAMLEASS_SWITCH_STATISTIC");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_BADCELL_STATISTIC_DUMP");
        intentFilter.addAction("com.android.phone.intent.action.RADIO_STALL_STATISTIC");
        intentFilter.addAction("miui.intent.action.NITZ_TIMEZONE_PREFERRED_OPTIMIZATION");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        intentFilter.addAction(ModemSarController.BC_INTENT_ACTION_DSI_VALUE_CHANGED);
        intentFilter.addAction("miui.intent.action.TIMEZONE_DETECTION_WITH_NULL_NITZ_STATISTIC");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSimStateChanged(Intent intent) {
        try {
            int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_SLOT_ID);
            if (SubscriptionManager.isRealSlotId(intExtra)) {
                String stringExtra = intent.getStringExtra("ss");
                Utils.logD("MiuiTelephonyStatistic", "processSimStateChanged iccState=" + stringExtra);
                if ("ABSENT".equals(stringExtra)) {
                    if (recordSimAbnormalIfNeed(intExtra)) {
                        recordSimAbnormalDelay(intExtra);
                    }
                    this.mIccidSS[intExtra] = "";
                    updateOutOfServiceDuration(intExtra, this.mLastSimStateWithNetworkState[intExtra]);
                    if (this.mIsDualSimInserted) {
                        forceUpdataModemStats(intExtra, true);
                    }
                    this.mIsDualSimInserted = false;
                } else if ("LOADED".equals(stringExtra)) {
                    TelephonyManager telephonyManager = this.mTelephonyManager;
                    if (telephonyManager != null) {
                        this.mPlmn[intExtra] = telephonyManager.getSimOperatorNumericForPhone(intExtra);
                    }
                    this.mIsDualSimInserted = MiuiPhoneUtils.getInsertedSimCount() == 2;
                    this.mIsVirtualSim[intExtra] = isVirtualSim(this.mContext, intExtra);
                    this.mIccidSS[intExtra] = getIccidSS(intExtra);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void recordSimAbnormalDelay(int i) {
        try {
            if (this.mStatisticHandler.hasMessages(69)) {
                if (this.mSimAbsentId != i) {
                    this.mStatisticHandler.removeMessageWithCount(69);
                    this.mSimAbsentId = 2;
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).getModemSimAbnormalInfo(i, this.simAbnormalList);
                    recordSimAbnormal();
                }
            } else {
                this.mSimAbsentId = i;
                MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
                miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(69), 30000L);
                ((IModemModule) ModuleDelegate.get(IModemModule.class)).getModemSimAbnormalInfo(i, this.simAbnormalList);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean recordSimAbnormalIfNeed(int i) {
        return !isEsimEnable() && isSimCardSlotInsertedStatus(i) && preSimLoadAndNotVirtualSim(i);
    }

    private boolean preSimLoadAndNotVirtualSim(int i) {
        return ("".equals(this.mIccidSS[i]) || this.mIsVirtualSim[i]) ? false : true;
    }

    private boolean isEsimEnable() {
        return TelephonyManagerEx.getDefault().getEsimGPIOState() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSimAbnormal() {
        HashMap map = new HashMap();
        map.put("SimOperator", String.join(",", this.mPlmn));
        map.put("abnormalId", Integer.valueOf(this.mSimAbsentId));
        map.put("sceneType", getSceneType());
        map.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
        this.simAbnormalList.add(map);
        ArrayList arrayList = new ArrayList(this.simAbnormalList);
        HashMap map2 = new HashMap();
        map2.put("Params", arrayList);
        Utils.logD("MiuiTelephonyStatistic", "recordSimAbnormal params= " + map + " simAbnormalParams= " + map2);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_sim_abnormal", map2);
        this.mSimAbsentId = -1;
        this.simAbnormalList.clear();
    }

    public String getSimOperator() {
        return String.join(",", this.mPlmn);
    }

    private boolean isSimCardSlotInsertedStatus(int i) {
        if (FiveGManagerBase.getInstance() != null) {
            return FiveGManagerBase.getInstance().isSimCardSlotInsertedStatus(i);
        }
        return false;
    }

    private void recordDataConnectionDuration(int i, boolean z) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i2 = this.mOldDataConnectionState;
        if (i2 != 2 && i == 2) {
            this.mDataConnectionStartTime = jElapsedRealtime;
            this.mOldDataConnectionState = i;
            return;
        }
        if (i2 != 2 || i == 2) {
            if (i2 == 2 && i == 2 && z) {
                long j = this.mDataConnectionStateDuration + (jElapsedRealtime - this.mDataConnectionStartTime);
                this.mDataConnectionStateDuration = j;
                try {
                    saveStringDataToSP("data_connection_state_duration", String.valueOf(j));
                } catch (Exception e) {
                    Rlog.d("MiuiTelephonyStatistic", "save KEY_DATA_CONNECTION_STATE_DURATION exception:" + e);
                }
                this.mDataConnectionStartTime = jElapsedRealtime;
                return;
            }
            return;
        }
        long j2 = jElapsedRealtime - this.mDataConnectionStartTime;
        this.mDataConnectionStateDuration += j2;
        if (DBG) {
            Rlog.d("MiuiTelephonyStatistic", "recordDataConnectionDuration duration:" + j2 + " total duration:" + this.mDataConnectionStateDuration);
        }
        try {
            saveStringDataToSP("data_connection_state_duration", String.valueOf(this.mDataConnectionStateDuration));
        } catch (Exception e2) {
            Rlog.d("MiuiTelephonyStatistic", "save KEY_DATA_CONNECTION_STATE_DURATION exception:" + e2);
        }
        this.mOldDataConnectionState = i;
    }

    public class MiuiTelephonyStatisticHandler extends CountableHandler {
        public MiuiTelephonyStatisticHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            ComponentName componentName;
            Object obj;
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                MiuiTelephonyStatistic.this.processCallDisconnectEvent((Connection) ((AsyncResult) message.obj).result);
                return;
            }
            if (i == 2) {
                MiuiTelephonyStatistic.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                return;
            }
            if (i == 3) {
                MiuiTelephonyStatistic.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                return;
            }
            if (i == 24) {
                Intent intent = (Intent) message.obj;
                long longExtra = intent.getLongExtra("Level_UL", 1L);
                long longExtra2 = intent.getLongExtra("Level_DL", 1L);
                MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.this;
                miuiTelephonyStatistic.getAvailableOperatorNumeric(null, miuiTelephonyStatistic.mLastDdsPhoneId);
                if (longExtra == longExtra2 && longExtra2 == 1) {
                    if (MiuiTelephonyStatistic.this.mIsLLMEnabled) {
                        MiuiTelephonyStatistic.this.mIsLLMEnabled = false;
                        MiuiTelephonyStatistic.this.onGameStatusChanged();
                    }
                } else {
                    MiuiTelephonyStatistic.this.mIsLLMEnabled = true;
                    MiuiTelephonyStatistic.this.onGameStatusChanged();
                }
                MiuiTelephonyStatistic.this.onGameStatusChangedForDsi();
                ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateModemHydraStat("game", MiuiTelephonyStatistic.this.mIsLLMEnabled, MiuiTelephonyStatistic.this.mLastDdsPhoneId);
                return;
            }
            if (i == 100) {
                MiuiTelephonyStatistic.this.processDebugAll(((Boolean) message.obj).booleanValue());
                return;
            }
            if (i != 101) {
                switch (i) {
                    case 5:
                        MiuiTelephonyStatistic.this.processDataStatisticChange(message.arg1, message.arg2, (HashMap) message.obj);
                        MiuiTelephonyStatistic.this.processNtnDataStatistic(message.arg1, message.arg2, (HashMap) message.obj);
                        break;
                    case 6:
                        MiuiTelephonyStatistic.this.init();
                        break;
                    case 7:
                        MiuiTelephonyStatistic.this.processSimStateChanged((Intent) message.obj);
                        break;
                    case 8:
                        Intent intent2 = (Intent) message.obj;
                        int i2 = SubscriptionManager.INVALID_SLOT_ID;
                        int intExtra = intent2.getIntExtra("old_data_slot", i2);
                        int intExtra2 = intent2.getIntExtra(SubscriptionManager.SLOT_KEY, i2);
                        Utils.logD("MiuiTelephonyStatistic", "ACTION_DEFAULT_DATA_SLOT_CHANGED oldSlotId=" + intExtra + ", newSlotId=" + intExtra2);
                        if (SubscriptionManager.isRealSlotId(intExtra)) {
                            MiuiTelephonyStatistic miuiTelephonyStatistic2 = MiuiTelephonyStatistic.this;
                            miuiTelephonyStatistic2.updateOutOfServiceDuration(intExtra, miuiTelephonyStatistic2.mLastSimStateWithNetworkState[intExtra]);
                            MiuiTelephonyStatistic.this.forceUpdataModemStats(intExtra, true);
                        }
                        if (SubscriptionManager.isRealSlotId(intExtra2)) {
                            MiuiTelephonyStatistic miuiTelephonyStatistic3 = MiuiTelephonyStatistic.this;
                            miuiTelephonyStatistic3.updateOutOfServiceDuration(intExtra2, miuiTelephonyStatistic3.mLastSimStateWithNetworkState[intExtra2]);
                        }
                        MiuiTelephonyStatistic miuiTelephonyStatistic4 = MiuiTelephonyStatistic.this;
                        miuiTelephonyStatistic4.mLastDdsPhoneId = miuiTelephonyStatistic4.getDefaultDataSlotId();
                        if (SubscriptionManager.isRealSlotId(intExtra2)) {
                            MiuiTelephonyStatistic.this.forceUpdataModemStats(intExtra2, true);
                        }
                        break;
                    case 9:
                        Intent intent3 = (Intent) message.obj;
                        int intExtra3 = intent3.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                        boolean booleanExtra = intent3.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                        String availableOperatorNumeric = MiuiTelephonyStatistic.this.getAvailableOperatorNumeric(null, intExtra3);
                        MiuiTelephonyStatistic.this.mModemStats.updateVolteStats(availableOperatorNumeric, booleanExtra, intExtra3);
                        boolean booleanExtra2 = intent3.getBooleanExtra("wfc_state", false);
                        if (Utils.isSupportVoWifiDevice(MiuiTelephonyStatistic.this.mContext, intExtra3)) {
                            MiuiTelephonyStatistic.this.mModemStats.updateVoWifiStats(availableOperatorNumeric, booleanExtra2, intExtra3);
                        }
                        MiuiImsStatistics.processVoWiFiRegisteredStateChange(intExtra3, booleanExtra2);
                        MiuiTelephonyStatistic.this.processVoLTERegisteredStateChange(intExtra3, booleanExtra, false);
                        if (CarrierConfigManagerEx.getBooleanConfigForSubId(MiuiTelephonyStatistic.this.mContext, intExtra3, "carrier_volte_globalization_enable") && !CarrierConfigManagerEx.getBooleanConfigForSubId(MiuiTelephonyStatistic.this.mContext, intExtra3, "carrier_volte_unavailable_bool")) {
                            MiuiTelephonyStatistic.this.mVolteRegisterMonitor.updateVolteGlobalizationStats(intExtra3, availableOperatorNumeric, booleanExtra);
                            break;
                        }
                        break;
                    case 10:
                        MiuiTelephonyStatistic.this.processDataDuration(message.arg1, message.arg2);
                        MiuiTelephonyStatistic.this.processBlockedNetworkDuration(message.arg1, message.arg2);
                        MiuiTelephonyStatistic.this.updateRatDataStateInfo(message.arg1, null, message.arg2, false);
                        break;
                    case 11:
                        MiuiTelephonyStatistic.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                        break;
                    case 12:
                        MiuiTelephonyStatistic.this.processNwOosStatEvent(((Integer) message.obj).intValue());
                        break;
                    default:
                        switch (i) {
                            case 14:
                                MiuiTelephonyStatistic.this.processPreciseCallStateEvent(message.arg1, message.arg2);
                                break;
                            case 15:
                                MiuiTelephonyStatistic.this.processUserUploadMdlogEvent(((Integer) message.obj).intValue());
                                break;
                            case 16:
                                MiuiTelephonyStatistic.this.processMdlogReportAgree(((Integer) message.obj).intValue());
                                break;
                            case 17:
                                MiuiTelephonyStatistic.this.processMdlogReportDisagree();
                                break;
                            case 18:
                                AsyncResult asyncResult = (AsyncResult) message.obj;
                                if (asyncResult != null && (obj = asyncResult.result) != null) {
                                    MiuiTelephonyStatistic.this.processOnIccChanged(((Integer) obj).intValue());
                                    break;
                                }
                                break;
                            case 19:
                                if (MiuiTelephonyStatistic.this.mIsDcBlocked) {
                                    MiuiTelephonyStatistic.this.recordFakeDataConnectionTime(4);
                                }
                                MiuiTelephonyStatistic.this.reportFakeDataSnapshot("shutdown");
                                MiuiTelephonyStatistic.this.forceUpdateStatisticData();
                                MiuiTelephonyStatistic.this.processShutDownEvent();
                                break;
                            default:
                                switch (i) {
                                    case 52:
                                        MiuiTelephonyStatistic.this.updateFiveGInfo(message.arg1, (FiveGServiceInfo) message.obj, false);
                                        break;
                                    case 53:
                                        Utils.logD("MiuiTelephonyStatistic", "EVENT_STATISTIC_GAME_RSRP_RECORD_TIMER timer end");
                                        MiuiTelephonyStatistic miuiTelephonyStatistic5 = MiuiTelephonyStatistic.this;
                                        miuiTelephonyStatistic5.updateSceneSignalInfo(1, miuiTelephonyStatistic5.mIsLLMEnabled, true);
                                        MiuiTelephonyStatistic.this.mRecordRsrpAfterGameEnd = false;
                                        break;
                                    case 54:
                                        MiuiTelephonyStatistic.this.processNetworkInfoChanged((Intent) message.obj);
                                        break;
                                    case 55:
                                        Intent intent4 = (Intent) message.obj;
                                        boolean z = MiuiTelephonyStatistic.this.mIsWechatVoip;
                                        MiuiTelephonyStatistic.this.mIsWechatVoip = intent4.getBooleanExtra("wechatVoipEnabled", false);
                                        if (DeviceUtil.isModemSceneStatEnabled()) {
                                            MiuiTelephonyStatistic miuiTelephonyStatistic6 = MiuiTelephonyStatistic.this;
                                            miuiTelephonyStatistic6.recordDurationOfRestartWeChatVoip(z, miuiTelephonyStatistic6.mIsWechatVoip);
                                            Utils.logD("MiuiTelephonyStatistic", "EVENT_STATISTIC_WECHAT_VOIP_CHANGED mIsWechatVoip=" + MiuiTelephonyStatistic.this.mIsWechatVoip);
                                            MiuiTelephonyStatistic miuiTelephonyStatistic7 = MiuiTelephonyStatistic.this;
                                            miuiTelephonyStatistic7.updateSceneSignalInfo(2, miuiTelephonyStatistic7.mIsWechatVoip, true ^ MiuiTelephonyStatistic.this.mIsWechatVoip);
                                            if (z != MiuiTelephonyStatistic.this.mIsWechatVoip && z && MiuiTelephonyStatistic.this.mIsWechatVideo) {
                                                ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateModemHydraStat("wechat", MiuiTelephonyStatistic.this.mIsWechatVoip, MiuiTelephonyStatistic.this.mLastDdsPhoneId);
                                                MiuiTelephonyStatistic.this.mIsWechatVideo = false;
                                            }
                                        }
                                        MiuiTelephonyStatistic.this.processVoipStallRateStatistics(z);
                                        break;
                                    default:
                                        switch (i) {
                                            case 57:
                                                MiuiTelephonyStatistic.this.mStatisticHandler.sendMessageDelayed(MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(58), 30000L);
                                                break;
                                            case 58:
                                                ModemComponentStats modemComponentStats = ModemComponentStats.getInstance();
                                                if (modemComponentStats != null) {
                                                    modemComponentStats.processModemReset();
                                                }
                                                break;
                                            case 59:
                                                int iIntValue = ((Integer) ((AsyncResult) message.obj).userObj).intValue();
                                                if (MiuiTelephonyStatistic.this.isSimReady(iIntValue) && MiuiTelephonyStatistic.this.isRadioPowerOn(iIntValue) && iIntValue == MiuiTelephonyStatistic.this.mLastDdsPhoneId) {
                                                    MiuiTelephonyStatistic.this.startNwSearchDurationTimer(iIntValue);
                                                    break;
                                                }
                                                break;
                                            case 60:
                                                int i3 = message.arg1;
                                                MiuiTelephonyStatistic miuiTelephonyStatistic8 = MiuiTelephonyStatistic.this;
                                                miuiTelephonyStatistic8.calcNwOosDurationIfNeed(i3, miuiTelephonyStatistic8.mServiceState[i3]);
                                                break;
                                            case 61:
                                                int i4 = message.arg1;
                                                MiuiTelephonyStatistic miuiTelephonyStatistic9 = MiuiTelephonyStatistic.this;
                                                miuiTelephonyStatistic9.calcNwSearchDurationIfNeed(i4, miuiTelephonyStatistic9.mServiceState[i4], true);
                                                break;
                                            case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                                                Intent intent5 = (Intent) message.obj;
                                                MiuiTelephonyStatistic.this.recordCallTimeOutEvent(intent5);
                                                MiuiTelephonyStatistic.this.mStatisticHandler.sendMessageDelayed(MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(63, intent5), 10000L);
                                                break;
                                            case 63:
                                                if (MiuiPhoneUtils.isPhoneInCall()) {
                                                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_time_out_retry_fail");
                                                }
                                                break;
                                            case DynamicSarService.WIFI_DATA_STATE_EXTRA_APP_STATE /* 64 */:
                                                MiuiTelephonyStatistic.this.recordEventIncallServiceChanged((Intent) message.obj);
                                                break;
                                            case 65:
                                                MiuiTelephonyStatistic.this.recordTelecommStatistic((Intent) message.obj);
                                                break;
                                            case 66:
                                                MiuiTelephonyStatistic.this.recordStkStatistic((Intent) message.obj);
                                                break;
                                            case 67:
                                                MiuiTelephonyStatistic.this.recordSsOptimizationStatistic((Intent) message.obj);
                                                break;
                                            case 68:
                                                MiuiTelephonyStatistic.this.recordSendSmsResult((Intent) message.obj);
                                                break;
                                            case 69:
                                                MiuiTelephonyStatistic.this.recordSimAbnormal();
                                                break;
                                            case 70:
                                                MiuiTelephonyStatistic.this.recordVerificationCode((Intent) message.obj);
                                                break;
                                            case 71:
                                                MiuiTelephonyStatistic.this.recordSignalStrengthEvent((Intent) message.obj);
                                                break;
                                            case 72:
                                                MiuiTelephonyStatistic.this.recordSignalStrengthShakeEvent((Intent) message.obj);
                                                break;
                                            default:
                                                switch (i) {
                                                    case 74:
                                                        MiuiTelephonyStatistic.this.calcDualDataStatistcIfNeed();
                                                        break;
                                                    case 75:
                                                        MiuiTelephonyStatistic.this.recordConsecutiveDataCallError();
                                                        break;
                                                    case 76:
                                                        MiuiTelephonyStatistic.this.mIsSleepMode = 1 == ((Intent) message.obj).getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                                                        MiuiTelephonyStatistic miuiTelephonyStatistic10 = MiuiTelephonyStatistic.this;
                                                        miuiTelephonyStatistic10.processSleepModeChangeEvent(miuiTelephonyStatistic10.mIsSleepMode);
                                                        break;
                                                    case 77:
                                                        MiuiTelephonyStatistic.this.recordRilWakeLockRequest((Intent) message.obj);
                                                        break;
                                                    case 78:
                                                        MiuiTelephonyStatistic.this.recordSeamlessSwitch((Intent) message.obj);
                                                        break;
                                                    case 79:
                                                        MiuiTelephonyStatistic.this.recordBadCellStatDump((Intent) message.obj);
                                                        break;
                                                    case 80:
                                                        MiuiTelephonyStatistic.this.recordUnsolDataCallError(message.arg1, message.arg2, (String) message.obj);
                                                        break;
                                                    case MiRilHookBase.EVENT_OEMHOOK_MTB_CLASS_MASK_GET /* 81 */:
                                                        MiuiTelephonyStatistic.this.recordRadioStallOptimization((Intent) message.obj);
                                                        break;
                                                    case MiRilHookBase.EVENT_OEMHOOK_XIAOMI_MODEM_HAL_CHIP_ID_GET /* 82 */:
                                                        MiuiTelephonyStatistic.this.recordNitzTimeZonePrefferredOptimization((Intent) message.obj);
                                                        break;
                                                    case 83:
                                                        MiuiTelephonyStatistic.this.processStallRateWhiteList((ForegroundInfo) message.obj);
                                                        break;
                                                    case SmartNetworkRelayScore.SCORE_PASS /* 84 */:
                                                        MiuiTelephonyStatistic.this.processMiuiVideoInfo((Intent) message.obj);
                                                        break;
                                                    case 85:
                                                        if (MiuiTelephonyStatistic.this.mIsAbnormalCell) {
                                                            MiuiTelephonyStatistic.this.recordAbnormalCell();
                                                        }
                                                        break;
                                                    case 86:
                                                        int iIntValue2 = ((Integer) message.obj).intValue();
                                                        MiuiTelephonyStatistic miuiTelephonyStatistic11 = MiuiTelephonyStatistic.this;
                                                        miuiTelephonyStatistic11.processNoLteNrStatEvent(iIntValue2, miuiTelephonyStatistic11.mServiceState[iIntValue2]);
                                                        break;
                                                    case 87:
                                                        int iIntValue3 = ((Integer) message.obj).intValue();
                                                        MiuiTelephonyStatistic miuiTelephonyStatistic12 = MiuiTelephonyStatistic.this;
                                                        miuiTelephonyStatistic12.calcNoLteNrDurationIfNeed(iIntValue3, miuiTelephonyStatistic12.mServiceState[iIntValue3]);
                                                        MiuiTelephonyStatistic.this.initNoLteNrValue(iIntValue3);
                                                        break;
                                                    case 88:
                                                        MiuiTelephonyStatistic.this.processDsiValueChanged((Intent) message.obj);
                                                        break;
                                                    case 89:
                                                        boolean zIsScreenOn = ((DeviceStateEvent.DisplayEvent) message.obj).isScreenOn();
                                                        if (zIsScreenOn != MiuiTelephonyStatistic.this.mIsScreenOn) {
                                                            MiuiTelephonyStatistic.this.mIsScreenOn = zIsScreenOn;
                                                            MiuiTelephonyStatistic.this.processScreenStateChangedForDsi();
                                                            break;
                                                        }
                                                        break;
                                                    case 90:
                                                        MiuiTelephonyStatistic.this.recordLteEmmTimerStartStatisticDelay();
                                                        break;
                                                    case SystemProperties.PROP_VALUE_MAX /* 91 */:
                                                        MiuiTelephonyStatistic.this.recordDoTimeZoneDetectionWithNullNitz((Intent) message.obj);
                                                        break;
                                                    case 92:
                                                        boolean booleanExtra3 = ((Intent) message.obj).getBooleanExtra("mIsHstMode", false);
                                                        if (MiuiTelephonyStatistic.this.mCurrentHstState != booleanExtra3) {
                                                            MiuiTelephonyStatistic.this.mCurrentHstState = booleanExtra3;
                                                            if (MiuiTelephonyStatistic.shouldProcessHstRachStats()) {
                                                                if (MiuiTelephonyStatistic.this.mCurrentHstState) {
                                                                    MiuiTelephonyStatistic.this.resetHstRachData();
                                                                    if (ModuleDelegate.get(IModemModule.class) != null) {
                                                                        MiuiTelephonyStatistic.this.mEnterHstRachData = ((IModemModule) ModuleDelegate.get(IModemModule.class)).getRachFailRateInfo();
                                                                        MiuiTelephonyStatistic.this.mCumulativeHstRachDiff.clear();
                                                                        Utils.logD("MiuiTelephonyStatistic", "Enter HST mode, record initial RACH data");
                                                                    }
                                                                } else if (ModuleDelegate.get(IModemModule.class) != null) {
                                                                    MiuiTelephonyStatistic.this.mExitHstRachData = ((IModemModule) ModuleDelegate.get(IModemModule.class)).getRachFailRateInfo();
                                                                    if (MiuiTelephonyStatistic.this.mEnterHstRachData != null) {
                                                                        MiuiTelephonyStatistic miuiTelephonyStatistic13 = MiuiTelephonyStatistic.this;
                                                                        miuiTelephonyStatistic13.accumulateHstRachDiff(miuiTelephonyStatistic13.mExitHstRachData, MiuiTelephonyStatistic.this.mEnterHstRachData);
                                                                    }
                                                                    MiuiTelephonyStatistic.this.recordHstRachStats();
                                                                }
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 93:
                                                        if (MiuiTelephonyStatistic.this.isNormalCaseForUseCelluar()) {
                                                            MiuiTelephonyStatistic.this.recordDataNetworkFailBySatellite();
                                                        }
                                                        break;
                                                    case 94:
                                                        Utils.logD("MiuiTelephonyStatistic", "EVENT_FAKE_DATA_SNAPSHOT_TIMEOUT triggered");
                                                        MiuiTelephonyStatistic.this.reportFakeDataSnapshot(ResponseObject.DEFAULT_MESSAGE_TIMEOUT);
                                                        break;
                                                    case 95:
                                                        MiuiTelephonyStatistic.this.saveFakeDataSnapshot((HashMap) message.obj, message.arg1);
                                                        break;
                                                    case 96:
                                                        MiuiTelephonyStatistic.this.reportFakeDataSnapshot("data_ok");
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
                return;
            }
            if (message.arg1 > 0) {
                Utils.logD("MiuiTelephonyStatistic", "Not supported under Split Screen");
                return;
            }
            ArrayList arrayList = (ArrayList) message.obj;
            if (arrayList == null || arrayList.size() < 0 || (componentName = (ComponentName) arrayList.get(0)) == null) {
                return;
            }
            String packageName = componentName.getPackageName();
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            boolean zContains = ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("config_single_hand_app"), packageName);
            MiuiTelephonyStatistic miuiTelephonyStatistic14 = MiuiTelephonyStatistic.this;
            if (zContains) {
                miuiTelephonyStatistic14.mIsSingleHandApp = true;
                if (MiuiTelephonyStatistic.this.mIsmotionless) {
                    return;
                }
                MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime = jElapsedRealtime;
                return;
            }
            if (miuiTelephonyStatistic14.lastSingleHandSceneDsiTime != 0) {
                long j = (jElapsedRealtime - MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime) / 1000;
                MiuiTelephonyStatistic miuiTelephonyStatistic15 = MiuiTelephonyStatistic.this;
                miuiTelephonyStatistic15.recordDsiStats("single_hand_dsi_statistic", j, miuiTelephonyStatistic15.lastDsi);
                MiuiTelephonyStatistic.this.lastSingleHandSceneDsiTime = 0L;
            }
            MiuiTelephonyStatistic.this.mIsSingleHandApp = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDurationOfRestartWeChatVoip(boolean z, boolean z2) {
        Rlog.d("MiuiTelephonyStatistic", "preWechatVoip=" + z + "curWechatVoip=" + z2);
        HashMap map = new HashMap();
        if (z && !z2) {
            if (MiuiPhoneUtils.isDuringCall()) {
                this.mWeChatHangUpStartTime = SystemClock.elapsedRealtime();
                map.put("weChatInterruptedByCall", Boolean.TRUE);
            } else {
                map.put("weChatInterruptedByCall", Boolean.FALSE);
            }
        }
        Rlog.d("MiuiTelephonyStatistic", "mWeChatHangUpStartTime=" + this.mWeChatHangUpStartTime);
        if (this.mWeChatHangUpStartTime != 0 && !z && z2) {
            this.mWeChatRestartVoipTime = SystemClock.elapsedRealtime();
            Rlog.d("MiuiTelephonyStatistic", "mWeChatRestartVoipTime=" + this.mWeChatRestartVoipTime);
            long j = this.mWeChatRestartVoipTime;
            long j2 = this.mWeChatHangUpStartTime;
            if (j > j2) {
                if (j - j2 < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
                    map.put("restartWechatVoipIntervalTime", Long.valueOf((j - j2) / 1000));
                }
                this.mWeChatHangUpStartTime = 0L;
                this.mWeChatRestartVoipTime = 0L;
            }
        }
        if (map.isEmpty()) {
            return;
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_wechat_voip_hangup_due_to_call", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordBadCellStatDump(Intent intent) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject = new JSONObject(intent.getBundleExtra("badCellDump").getString("bad_cell_stat_info"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        localLog("event_badcell_statistic", jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordTelecommStatistic(Intent intent) {
        String stringExtra = intent.getStringExtra("category_name");
        if (TextUtils.isEmpty(stringExtra)) {
            Rlog.e("MiuiTelephonyStatistic", "no category");
            return;
        }
        String stringExtra2 = intent.getStringExtra(f.a.l);
        if (TextUtils.isEmpty(stringExtra2)) {
            Rlog.e("MiuiTelephonyStatistic", "no event");
            return;
        }
        Rlog.d("MiuiTelephonyStatistic", "category: " + stringExtra + ", event: " + stringExtra2);
        Bundle bundleExtra = intent.getBundleExtra("param_name");
        if (bundleExtra != null) {
            if ("event_app_txrx_transmission".equals(stringExtra2) && Build.IS_STABLE_VERSION) {
                localLog(stringExtra2, bundleExtra.getString("app_txrx_info"));
                return;
            } else {
                MiStatInterfaceUtil.recordObjectEvent(stringExtra, stringExtra2, bundleToHashMap(stringExtra2, bundleExtra));
                return;
            }
        }
        MiStatInterfaceUtil.recordCountEvent(stringExtra, stringExtra2);
    }

    public void recordRadioStallOptimization(Intent intent) {
        HashMap map = new HashMap();
        map.put("result_code", intent.getExtra("resultCode"));
        map.put("failed_code", intent.getExtra("failedCode"));
        Rlog.d("MiuiTelephonyStatistic", "recordRadioStallOptimization :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "radio_stall_optimization", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordNitzTimeZonePrefferredOptimization(Intent intent) {
        HashMap map = new HashMap();
        map.put("country", intent.getExtra("countryIso"));
        map.put("operator", intent.getExtra("operator"));
        map.put("nitz_offset", intent.getExtra("nitz_offset"));
        map.put("cache_zoneid", intent.getExtra("cache_zoneid"));
        map.put("suggestion_zoneid", intent.getExtra("suggestion_zoneid"));
        map.put("suggestion_offset", intent.getExtra("suggestion_offset"));
        map.put("current_zoneid", intent.getExtra("current_zoneid"));
        map.put("reason", intent.getExtra("reason"));
        Rlog.d("MiuiTelephonyStatistic", "recordNitzTimeZonePrefferredOptimization :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nitz_timezone_preferred", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDoTimeZoneDetectionWithNullNitz(Intent intent) {
        HashMap map = new HashMap();
        map.put("country", intent.getExtra("countryIso"));
        map.put("suggestion_zoneid", intent.getExtra("suggestion_zoneid"));
        Rlog.d("MiuiTelephonyStatistic", "recordDoTimeZoneDetectionWithNullNitz :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "timezone_detection_with_null_nitz", map);
    }

    private void localLog(String str, String str2) {
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str)) {
                    map.put(str, new LocalLog(50));
                }
                ((LocalLog) map.get(str)).log(str2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void localLog(String str, Map map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                sb.append(((String) entry.getKey()) + " : " + entry.getValue() + "; ");
            }
        }
        HashMap map2 = sLocalLogs;
        synchronized (map2) {
            try {
                if (!map2.containsKey(str)) {
                    map2.put(str, new LocalLog(50));
                }
                ((LocalLog) map2.get(str)).log(sb.toString());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordStkStatistic(Intent intent) {
        String stringExtra = intent.getStringExtra("stk_event_name");
        if (TextUtils.isEmpty(stringExtra)) {
            Rlog.e("MiuiTelephonyStatistic", "no event");
            return;
        }
        Rlog.d("MiuiTelephonyStatistic", "recordStkStatistic-event: " + stringExtra);
        Bundle bundleExtra = intent.getBundleExtra("stk_param_name");
        if (bundleExtra != null) {
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, stringExtra, bundleToHashMap(stringExtra, bundleExtra));
        } else {
            Rlog.e("MiuiTelephonyStatistic", "param is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordEventIncallServiceChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME");
        boolean z = "com.android.contacts".equals(stringExtra) || "com.google.android.dialer".equals(stringExtra);
        HashMap map = new HashMap();
        map.put("isSystem", Boolean.valueOf(z));
        if (!z) {
            map.put(SettingsBackupConsts.EXTRA_PACKAGE_NAME, stringExtra);
            map.put("countryIso", MiuiPhoneUtils.getCountryIso(this.mContext));
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "incall_service_info_changed", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordRilWakeLockRequest(Intent intent) {
        HashMap map = new HashMap();
        map.put("operator", intent.getExtra("operator"));
        map.put("requestCode", intent.getExtra("requestCode"));
        Utils.logD("MiuiTelephonyStatistic", "recordRilWakeLockRequest :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ril_wake_lock_request", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSeamlessSwitch(Intent intent) {
        HashMap map = new HashMap();
        map.put("Scene", intent.getExtra("switchScene"));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_seamless_switch", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSendSmsResult(Intent intent) {
        HashMap map = new HashMap();
        boolean booleanExtra = intent.getBooleanExtra("is_satellite_sms", false);
        int intExtra = intent.getIntExtra("error_Code", -1);
        boolean booleanExtra2 = intent.getBooleanExtra("is_Sent_SMS_Success", false);
        String stringExtra = intent.getStringExtra("sms_packagename");
        map.put("isSentSmsSuccess", booleanExtra2 ? "1" : "0");
        map.put(SettingsBackupConsts.EXTRA_PACKAGE_NAME, stringExtra);
        map.put("errorCode", String.valueOf(intExtra));
        map.put("sceneType", getSceneType());
        map.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
        ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
        if (iSatelliteModule != null && iSatelliteModule.isSatelliteEnabled() && booleanExtra) {
            String stringExtra2 = intent.getStringExtra("sms_send_duration");
            boolean booleanExtra3 = intent.getBooleanExtra("is_mult_sms", false);
            map.put("isSatelliteDataEnable", iSatelliteModule.isSatelliteDataEnabled() ? "1" : "0");
            map.put("isSatelliteEnabled", "1");
            map.put("smsSendDuration", stringExtra2);
            map.put("isMultSms", booleanExtra3 ? "1" : "0");
            Bundle ntnLocationInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnLocationInfo();
            if (ntnLocationInfo != null) {
                map.put("p", String.valueOf(ntnLocationInfo.getString("province")));
                map.put("city", String.valueOf(ntnLocationInfo.getString("city")));
                map.put("altitude", String.valueOf(ntnLocationInfo.getDouble("altitude")));
                map.put("speed", String.valueOf(ntnLocationInfo.getFloat("speed")));
            }
            FoldStateMonitor.getInstance();
            addSatelliteInfo(map);
            if (!booleanExtra2) {
                recordDFSSatelliteSMSEvent();
            }
        } else {
            int intExtra2 = intent.getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, 0);
            if (!isValidatePhoneId(intExtra2)) {
                Utils.logE("MiuiTelephonyStatistic", "recordSendSmsResult exception phoneId!");
                return;
            }
            int intExtra3 = intent.getIntExtra("sms_Optimization_Status", 0);
            String stringExtra3 = intent.getStringExtra("sms_Optimization_List");
            String stringExtra4 = intent.getStringExtra("smsc_Address");
            String stringExtra5 = intent.getStringExtra("save_Smsc");
            int dataRat = getDataRat(this.mServiceState[intExtra2]);
            int voiceRat = getVoiceRat(this.mServiceState[intExtra2]);
            if (TextUtils.isEmpty(stringExtra4)) {
                stringExtra4 = SmsManager.getSmsManagerForSubscriptionId(SubscriptionManager.getDefault().getSubscriptionIdForSlot(intExtra2)).getSmscAddress();
            }
            map.put("smscAddress", stringExtra4);
            map.put("saveSmsc", stringExtra5);
            map.put("smsOptimizationStatus", String.valueOf(intExtra3));
            map.put("smsOptimizationList", stringExtra3);
            map.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            map.put("Network", getAvailableOperatorNumeric(null, intExtra2));
            if (20 == voiceRat || 14 == voiceRat) {
                map.put("VoLTE", "1");
            } else {
                map.put("VoLTE", "0");
            }
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, intExtra2)) {
                map.put("VoiceLink", "1");
            } else {
                map.put("VoiceLink", "0");
            }
            map.put("dsda", String.valueOf(Utils.getDsdaState().ordinal()));
            updateRoamingParams(intExtra2, this.mServiceState[intExtra2], map);
            map.put("simType", String.valueOf(Utils.getCustomSimTypeForStatistic(intExtra2)));
        }
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_send_sms_result", map);
        Utils.logD("MiuiTelephonyStatistic", "recordSendSmsResult data= " + map);
        if (booleanExtra2) {
            return;
        }
        recordDFSEventStatistic(915700002, map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordVerificationCode(Intent intent) {
        int intExtra = intent.getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
        if (!isValidatePhoneId(intExtra)) {
            Utils.logE("MiuiTelephonyStatistic", "recordVerificationCode exception phoneId!");
            return;
        }
        HashMap map = new HashMap();
        String stringExtra = intent.getStringExtra("app_name");
        String stringExtra2 = intent.getStringExtra("app_package_name");
        long longExtra = intent.getLongExtra("long_time", -1L);
        int dataRat = getDataRat(this.mServiceState[intExtra]);
        int voiceRat = getVoiceRat(this.mServiceState[intExtra]);
        map.put("isVerificationSuccess", intent.getBooleanExtra("is_verification_success", false) ? "1" : "0");
        map.put("appName", stringExtra);
        map.put("appPackageName", stringExtra2);
        map.put("verificationLongTime", String.valueOf(longExtra));
        map.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
        map.put("Network", getAvailableOperatorNumeric(null, intExtra));
        map.put("rsrp", String.valueOf(getDbm(this.mSignalStrength[intExtra])));
        if (20 == voiceRat || 14 == voiceRat) {
            map.put("VoLTE", "1");
        } else {
            map.put("VoLTE", "0");
        }
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_sms_verification_code", map);
        Utils.logD("MiuiTelephonyStatistic", "recordVerificationCode data= " + map);
    }

    public static void recordSatelliteEnabled(HashMap map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_satellite_enabled", map);
        Utils.logD("MiuiTelephonyStatistic", "recordSatelliteEnabled data= " + map);
    }

    public static void recordSatelliteHighPenetration(HashMap map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_satellite_high_penetration", map);
        Utils.logD("MiuiTelephonyStatistic", "recordSatelliteHighPenetration data= " + map);
    }

    public static void recordSatelliteBusinessEntrance(HashMap map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_satellite_business_entrance", map);
        Utils.logD("MiuiTelephonyStatistic", "recordSatelliteBusinessEntrance data= " + map);
    }

    public static void recordSatelliteOta(HashMap map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_satellite_ota", map);
        Utils.logD("MiuiTelephonyStatistic", "recordSatelliteOta data= " + map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNwSearchDurationTimer(int i) {
        if (this.mNwSearchDurations.size() < 20) {
            this.mNwSearchDuration[i] = SystemClock.elapsedRealtime();
            sendMaxCalcDurationEvent(61, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRadioPowerOn(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return phone != null && phone.mCi.getRadioState() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordCallTimeOutEvent(Intent intent) {
        HashMap map = new HashMap();
        int intExtra = intent.getIntExtra("phone_index", SubscriptionManager.INVALID_PHONE_ID);
        if (isValidatePhoneId(intExtra)) {
            int voiceRat = getVoiceRat(this.mServiceState[intExtra]);
            int dbm = getDbm(this.mSignalStrength[intExtra]);
            map.put("RAT", ServiceState.rilRadioTechnologyToString(voiceRat));
            map.put("rsrp", String.valueOf(dbm));
            map.put("Band", Utils.getBand(intExtra, this.mServiceState[intExtra]));
            map.put("isDds", intExtra == this.mLastDdsPhoneId ? "1" : "0");
        }
        map.put("RetryAction", intent.getStringExtra("retry_action"));
        map.put("RetryTimes", Integer.valueOf(intent.getIntExtra("retry_times", 0)));
        map.put("board_thermal_temp", String.valueOf(Utils.getBoardThermalTemp()));
        Utils.logD("MiuiTelephonyStatistic", "recordCallTimeOutEvent :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_time_out", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSignalStrengthEvent(Intent intent) {
        HashMap map = new HashMap();
        map.put("slotId", intent.getExtra("slotId"));
        map.put("operator", intent.getExtra("operator"));
        map.put("rat", intent.getExtra("rat"));
        map.put("band", intent.getExtra("band"));
        map.put("levelChange", intent.getExtra("levelChange"));
        map.put("foregroundActivity", intent.getExtra("foregroundActivity"));
        Utils.logD("MiuiTelephonyStatistic", "recordSignalStrengthEvent :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "signal_strength_big_change", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSignalStrengthShakeEvent(Intent intent) {
        HashMap map = new HashMap();
        map.put("slotId", intent.getExtra("slotId"));
        map.put("signalShake", intent.getExtra("signalShake"));
        Utils.logD("MiuiTelephonyStatistic", "recordSignalStrengthShakeEvent :" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "signal_strength_shake", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processDataStatisticChange(int i, int i2, HashMap map) {
        if (!isValidatePhoneId(i2)) {
            Utils.logE("MiuiTelephonyStatistic", "processDataStatisticChange exception phoneId!");
            return;
        }
        TelephonyStatAdapter.DataStatType dataStatType = TelephonyStatAdapter.DataStatType.values()[i];
        Utils.logD("MiuiTelephonyStatistic", "processDataStatisticChange dataStatType=" + dataStatType);
        this.mFakeDataTrigger = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getFakeDataTrigger();
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).processDataStatisticChange(dataStatType);
        switch (AnonymousClass7.$SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[dataStatType.ordinal()]) {
            case 1:
                if (!this.mIsDcBlocked) {
                    doFakeDataConnectionEvent(i2);
                }
                this.isDataStall = true;
                int dataRat = getDataRat(this.mServiceState[i2]);
                if (MiuiPhoneUtils.isModemLogUploadSupported()) {
                    if (dataRat == 13 || dataRat == 19) {
                        ((IModemModule) ModuleDelegate.get(IModemModule.class)).startCaptureModemLog("dataStall", "modem_data_lte");
                    } else if (dataRat == 20) {
                        ((IModemModule) ModuleDelegate.get(IModemModule.class)).startCaptureModemLog("dataStall", "modem_data_sa");
                    }
                }
                break;
            case 2:
                doDataCallEvent(true, i2, map);
                break;
            case 3:
                doDataCallEvent(false, i2, map);
                break;
            case 4:
                this.mDefaultApnRequestCount++;
                break;
            case 5:
                doApnRecoveryEvent(i2, map);
                break;
            case 6:
                doDataEvaluationFailed(false, i2, map);
                break;
            case 7:
                doRecordDataInterruptionStatus(i2, map);
                break;
            case 8:
                doRecordDataInterruptionCause(i2, map);
                break;
            case 9:
                if (this.isDataStall) {
                    this.isDataStall = false;
                }
                if (isTurnOffSatelliteRecently()) {
                    Utils.logD("MiuiTelephonyStatistic", "ignore satellite is disabled recently!");
                    this.mStatisticHandler.removeMessageWithCount(93);
                }
                MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
                miuiTelephonyStatisticHandler.sendMessage(miuiTelephonyStatisticHandler.obtainMessage(96, Long.valueOf(this.mDcBlockedStartTime)));
                if (this.mIsDcBlocked) {
                    recordFakeDataConnectionTime(2);
                }
                break;
            default:
                Utils.logE("MiuiTelephonyStatistic", "default data statistic type!");
                break;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.statistics.MiuiTelephonyStatistic$7, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType;

        static {
            int[] iArr = new int[TelephonyStatAdapter.DataStatType.values().length];
            $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType = iArr;
            try {
                iArr[TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_SETUP_OK_CN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_SETUP_FAIL_CN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_SETUP_REQUEST_CN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_APN_RECOVERY_CN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_EVALUATION_FAILED_CN.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_INTERRUPTION_RECOVERY_ACTION.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_INTERRUPTION_CAUSE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        try {
            if (isValidatePhoneId(i) && signalStrength != null) {
                this.mSignalStrength[i] = new SignalStrength(signalStrength);
                updateNwSignalInfo(i, signalStrength, null, false);
                updateFiveGInfo(i, this.mFiveGInfo[i], false);
                updateNonDdsInfo(i, null);
                onSceneStatsInfoChanged();
            } else {
                Utils.logE("MiuiTelephonyStatistic", "processSignalStrengthEvent-invalid phone id!");
            }
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, Integer.valueOf(i));
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataDuration(int i, int i2) {
        if (isValidatePhoneId(i)) {
            recordDataConnectionDuration(i2, false);
        }
    }

    private boolean isVirtualSim(Context context, int i) {
        return MiuiPhoneUtils.isVirtualSim(i) || MiuiPhoneUtils.isVirtualSeedSimBySlot(context, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        boolean zIsUserFiveGEnabled;
        boolean zIsUserFiveGSaEnabled;
        try {
            Utils.logD("MiuiTelephonyStatistic", "processServiceStateChangeEvent phoneId=" + i);
            if (serviceState != null && isValidatePhoneId(i)) {
                Utils.logD("MiuiTelephonyStatistic", "processServiceStateChangeEvent manual=" + serviceState.getIsManualSelection());
                if (!serviceState.getIsManualSelection()) {
                    monitorVoiceNwChange(i, serviceState);
                    if (ModemProvider.IS_MTK && FiveGManagerBase.isFiveGCapable()) {
                        monitorNwChange(i, serviceState);
                        int dataNetworkType = serviceState.getDataNetworkType();
                        ServiceState serviceState2 = this.mServiceState[i];
                        int dataNetworkType2 = serviceState2 != null ? serviceState2.getDataNetworkType() : dataNetworkType;
                        if (FiveGManagerBase.getInstance() != null) {
                            zIsUserFiveGEnabled = FiveGManagerBase.getInstance().isUserFiveGEnabled();
                            zIsUserFiveGSaEnabled = FiveGManagerBase.getInstance().isUserFiveGSaEnabled();
                        } else {
                            zIsUserFiveGEnabled = false;
                            zIsUserFiveGSaEnabled = false;
                        }
                        if (zIsUserFiveGSaEnabled && zIsUserFiveGEnabled && Utils.isNr(dataNetworkType2) && Utils.isNonNr(dataNetworkType) && !isNormalCaseForVoiceDetach(i)) {
                            long jElapsedRealtime = SystemClock.elapsedRealtime();
                            long j = this.mLastDrop5GTime;
                            if (j == 0 || jElapsedRealtime - j >= 20000) {
                                this.mLastDrop5GTime = jElapsedRealtime;
                                ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(124, Integer.valueOf(i));
                            }
                        }
                    }
                }
                monitorNoLteNrVoiceRatState(i, serviceState);
                this.mServiceState[i] = new ServiceState(serviceState);
                updateSimStateWithNwState(i, serviceState);
                updateNwSignalInfo(i, null, serviceState, false);
                updateDualSimNwBandInfo(i, serviceState, false);
                updateNwBandInfo(i, serviceState, false);
                updateNsaNwBandInfo(i, serviceState, false);
                updateFiveGInfo(i, this.mFiveGInfo[i], false);
                updateNonDdsInfo(i, serviceState);
                updateRatDataStateInfo(i, serviceState, -1, false);
                calcNwSearchDurationIfNeed(i, serviceState, false);
                updateHstArfcnInfo(i, null, false);
                if (i == this.mLastDdsPhoneId) {
                    this.mRoamingModemStats.updateRoamingState(serviceState.getRoaming());
                }
                if (isNormalCardStatus(i)) {
                    if (ServiceProviderUtils.isChinaTelecom(serviceState.getOperatorNumeric())) {
                        this.mVolteRegisterMonitor.processCheckVolteCdma1xState(i, serviceState, this.mLastVoLTERegisteredTime[i] != -1);
                    }
                } else {
                    this.mVolteRegisterMonitor.clearAndRecordVolteFailedEvent(i, serviceState);
                    this.mVolteRegisterMonitor.clearAndRecordCdma1xFailedEvent(i, serviceState);
                }
                updateLteEmmTimerInfo(i, serviceState);
            } else {
                Utils.logE("MiuiTelephonyStatistic", "processServiceStateChangeEvent invalid params!!!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void calcNwSearchDurationIfNeed(int i, ServiceState serviceState, boolean z) {
        try {
            int voiceNetworkType = serviceState.getVoiceNetworkType();
            boolean z2 = Utils.isLte(voiceNetworkType) || Utils.isNr(voiceNetworkType);
            if (this.mNwSearchDuration[i] != -1 && (z2 || z)) {
                this.mStatisticHandler.removeMessageWithCount(61);
                String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
                long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mNwSearchDuration[i]) / 1000;
                if (jElapsedRealtime >= 60) {
                    jElapsedRealtime = 60;
                }
                HashMap map = new HashMap();
                map.put("Network", availableOperatorNumeric);
                map.put("Value", Long.valueOf(jElapsedRealtime));
                addRoamingParams(i, serviceState, map);
                map.put("simType", String.valueOf(Utils.getCustomSimTypeForStatistic(i)));
                this.mNwSearchDurations.add(map);
                this.mNwSearchDuration[i] = -1;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void addRoamingParams(int i, ServiceState serviceState, Map map) {
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String operatorNumeric = serviceState.getOperatorNumeric();
        String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        boolean roaming = serviceState.getRoaming();
        map.put("vplmn", operatorNumeric);
        map.put("hplmn", simOperatorForSlot);
        map.put("roaming", Boolean.valueOf(roaming));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSleepModeChangeEvent(boolean z) {
        this.mModemPowerStatistic.onSleepModeChange(z);
    }

    class CurrentTopAppObserver implements MiuiAppStateMonitor.TopAppObserver {
        private CurrentTopAppObserver() {
        }

        @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
        public void onTopAppChanged(List list, int i) {
            MiuiTelephonyStatistic.this.mStatisticHandler.sendMessageDelayed(MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(101, i, 0, list), 500L);
        }
    }

    public void regHandsensor() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            Utils.logI("MiuiTelephonyStatistic", "obtain sensor service failed ");
            return;
        }
        if (this.mIsHandSensorReady) {
            return;
        }
        Sensor defaultSensor = sensorManager.getDefaultSensor(33171120);
        this.mSensorSubstanceHand = defaultSensor;
        boolean zRegisterListener = this.mSensorManager.registerListener(this.mSensorEventListener, defaultSensor, 3);
        if (this.mSensorSubstanceHand == null || !zRegisterListener) {
            this.mIsHandSensorReady = false;
        } else {
            this.mIsHandSensorReady = true;
        }
    }

    public void unRegHandsensor() {
        if (this.mIsHandSensorReady) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mSensorSubstanceHand);
            this.mIsHandSensorReady = false;
        }
    }

    private void updateRoamingParams(int i, ServiceState serviceState, Map map) {
        if (serviceState == null) {
            return;
        }
        String operatorNumeric = serviceState.getOperatorNumeric();
        String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        map.put("vplmn", operatorNumeric);
        map.put("hplmn", simOperatorForSlot);
        map.put("roaming", serviceState.getRoaming() ? "1" : "0");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:35:0x008c  */
    public void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
        int nrarfcn;
        if (!isValidatePhoneId(i) || cellLocation == null || cellLocation.isEmpty() || cellLocation.equals(this.mCellLocation[i])) {
            return;
        }
        this.mCellLocation[i] = cellLocation;
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).updatePingpongCellStat(i);
        Phone phone = MiuiPhoneUtils.getPhone(i);
        int dataRat = getDataRat(this.mServiceState[i]);
        if (phone == null || phone.getCurrentCellIdentity() == null) {
            return;
        }
        this.mCellIdentity[i] = phone.getCurrentCellIdentity();
        if (DeviceUtil.IS_INTERNATIONAL_BUILD) {
            if (!Utils.isNr(dataRat)) {
                return;
            }
            CellIdentity cellIdentity = this.mCellIdentity[i];
            if (cellIdentity instanceof CellIdentityNr) {
                nrarfcn = ((CellIdentityNr) cellIdentity).getNrarfcn();
            } else {
                nrarfcn = -1;
            }
        } else {
            if (!Utils.isLte(dataRat) && !Utils.isNr(dataRat)) {
                return;
            }
            CellIdentity cellIdentity2 = this.mCellIdentity[i];
            if (cellIdentity2 instanceof CellIdentityLte) {
                nrarfcn = ((CellIdentityLte) cellIdentity2).getEarfcn();
            } else if (cellIdentity2 instanceof CellIdentityNr) {
                nrarfcn = ((CellIdentityNr) cellIdentity2).getNrarfcn();
            } else {
                nrarfcn = -1;
            }
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(this.mServiceState[i], i);
        if (nrarfcn == -1 || availableOperatorNumeric == null || "NoSim".equals(availableOperatorNumeric)) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mFcnStats.get(availableOperatorNumeric);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mFcnStats.put(availableOperatorNumeric, concurrentHashMap);
        }
        concurrentHashMap.put(Integer.valueOf(nrarfcn), Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(Integer.valueOf(nrarfcn), 0)).intValue() + 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processVoLTERegisteredStateChange(int i, boolean z, boolean z2) {
        if (i == SubscriptionManager.INVALID_PHONE_ID) {
            return;
        }
        if (((!z2 && !z) || (z2 && z)) && this.mLastVoLTERegisteredTime[i] != -1 && this.mLastVoLTERegisteredPlmn[i] != null) {
            long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mLastVoLTERegisteredTime[i];
            Long l = (Long) this.mVoLTERegisteredDuration.get(this.mLastVoLTERegisteredPlmn[i]);
            if (l == null) {
                l = 0L;
            }
            this.mVoLTERegisteredDuration.put(this.mLastVoLTERegisteredPlmn[i], Long.valueOf(l.longValue() + jElapsedRealtime));
            try {
                saveStringDataToSP("volte_duration", ObjectSerializer.serialize(this.mVoLTERegisteredDuration));
            } catch (Exception e) {
                Rlog.d("MiuiTelephonyStatistic", "save KEY_VOLTE_DURATION exception:" + e);
            }
        }
        if (z) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                phone.getImsPhone();
            }
            this.mLastVoLTERegisteredTime[i] = SystemClock.elapsedRealtime();
            this.mLastVoLTERegisteredPlmn[i] = this.mServiceState[i].getOperatorNumeric();
        } else {
            this.mLastVoLTERegisteredTime[i] = -1;
            this.mLastVoLTERegisteredPlmn[i] = null;
        }
        ServiceState serviceState = this.mServiceState[i];
        if (serviceState == null || !isNormalCardStatus(i)) {
            return;
        }
        if (ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(this.mContext, i)) {
            this.mVolteRegisterMonitor.processCheckVolteState(i, serviceState, z);
        }
        if (ServiceProviderUtils.isChinaTelecom(serviceState.getOperatorNumeric())) {
            this.mVolteRegisterMonitor.processCheckVolteCdma1xState(i, serviceState, z);
        }
    }

    private boolean isNormalCardStatus(int i) {
        return (this.mIsVirtualSim[i] || this.mCardState[i] != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mIsAirPlaneMode) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x0034  */
    /* JADX WARN: Code duplicated, block: B:23:0x003f  */
    /* JADX WARN: Code duplicated, block: B:26:0x0084  */
    /* JADX WARN: Code duplicated, block: B:28:0x008a  */
    /* JADX WARN: Code duplicated, block: B:31:0x0096  */
    /* JADX WARN: Code duplicated, block: B:32:0x0098  */
    /* JADX WARN: Code duplicated, block: B:35:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:37:0x00be  */
    /* JADX WARN: Code duplicated, block: B:38:0x00c0  */
    /* JADX WARN: Code duplicated, block: B:40:0x00c3  */
    /* JADX WARN: Code duplicated, block: B:42:0x00c9  */
    /* JADX WARN: Code duplicated, block: B:43:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:45:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:51:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:54:0x00ee  */
    /* JADX WARN: Code duplicated, block: B:57:0x011f  */
    /* JADX WARN: Code duplicated, block: B:61:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    public void processCallDisconnectEvent(Connection connection) {
        int i;
        String str;
        boolean zIsVideo;
        ServiceState serviceState;
        ServiceState serviceState2;
        Phone phone = getPhone(connection);
        if (phone == null) {
            return;
        }
        int phoneId = phone.getPhoneId();
        if (isValidatePhoneId(phoneId)) {
            if (isCallOffhook(connection)) {
                if (recordCallUnexpectedDisconnectCountEvent(phoneId, connection)) {
                    i = 1;
                }
                if (MiuiDsdaStatistic.getInstance() != null) {
                    MiuiDsdaStatistic.getInstance().recordVideoCallStatistics(connection);
                }
                if (connection instanceof ImsPhoneConnection) {
                    MiDataChannelStatistic.getInstance().recordDataChannelInfoCallStatistics(getCommonRadioParamInfo(phoneId, true), connection.getTelecomCallId());
                }
                Utils.logD("MiuiTelephonyStatistic", "processCallDisconnectEvent phone.getPhoneType()=" + phone.getPhoneType() + ",conn.isIncoming()=" + connection.isIncoming());
                String availableOperatorNumeric = getAvailableOperatorNumeric(null, phoneId);
                if (phone.getPhoneType() != 5) {
                    str = "csfbCn";
                } else {
                    serviceState2 = this.mServiceState[phoneId];
                    if (serviceState2 == null && Utils.isNr(serviceState2.getDataNetworkType())) {
                        str = "vonrCn";
                    } else {
                        str = "volteCn";
                    }
                }
                zIsVideo = VideoProfile.isVideo(connection.getVideoState());
                if (connection.isWifi()) {
                    if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phoneId)) {
                        str = "vodataCn";
                    } else {
                        str = "vowifiCn";
                    }
                }
                if (zIsVideo) {
                    if (str.equals("vowifiCn")) {
                        str = "viwifiCn";
                    } else if (!str.equals("volteCn") || str.equals("vonrCn")) {
                        str = "vilteCn";
                    } else if (str.equals("vodataCn")) {
                        str = "vidataCn";
                    }
                }
                if (phone.getPhoneType() == 7) {
                    str = "vontnCn";
                }
                Utils.logD("MiuiTelephonyStatistic", "isVideoCall = " + zIsVideo + "isWifiCall = " + connection.isWifi());
                String str2 = str;
                this.mModemStats.updateCallStats(phoneId, availableOperatorNumeric, connection.isIncoming(), str2, i);
                serviceState = this.mServiceState[phoneId];
                if (serviceState == null && serviceState.getRoaming()) {
                    this.mRoamingModemStats.updateCallStats(phoneId, getHplmnAndVplmn(serviceState, phoneId), connection.isIncoming(), str2, i);
                    return;
                }
            }
            int i2 = recordCallConnFailEvent(phoneId, connection) ? 2 : 0;
            i = i2;
            if (MiuiDsdaStatistic.getInstance() != null) {
                MiuiDsdaStatistic.getInstance().recordVideoCallStatistics(connection);
            }
            if (connection instanceof ImsPhoneConnection) {
                MiDataChannelStatistic.getInstance().recordDataChannelInfoCallStatistics(getCommonRadioParamInfo(phoneId, true), connection.getTelecomCallId());
            }
            Utils.logD("MiuiTelephonyStatistic", "processCallDisconnectEvent phone.getPhoneType()=" + phone.getPhoneType() + ",conn.isIncoming()=" + connection.isIncoming());
            String availableOperatorNumeric2 = getAvailableOperatorNumeric(null, phoneId);
            if (phone.getPhoneType() != 5) {
                str = "csfbCn";
            } else {
                serviceState2 = this.mServiceState[phoneId];
                if (serviceState2 == null) {
                    str = "volteCn";
                } else {
                    str = "volteCn";
                }
            }
            zIsVideo = VideoProfile.isVideo(connection.getVideoState());
            if (connection.isWifi()) {
                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phoneId)) {
                    str = "vodataCn";
                } else {
                    str = "vowifiCn";
                }
            }
            if (zIsVideo) {
                if (str.equals("vowifiCn")) {
                    str = "viwifiCn";
                } else if (!str.equals("volteCn")) {
                    str = "vilteCn";
                } else {
                    str = "vilteCn";
                }
            }
            if (phone.getPhoneType() == 7) {
                str = "vontnCn";
            }
            Utils.logD("MiuiTelephonyStatistic", "isVideoCall = " + zIsVideo + "isWifiCall = " + connection.isWifi());
            String str3 = str;
            this.mModemStats.updateCallStats(phoneId, availableOperatorNumeric2, connection.isIncoming(), str3, i);
            serviceState = this.mServiceState[phoneId];
            if (serviceState == null) {
            }
            i = i2;
            if (MiuiDsdaStatistic.getInstance() != null) {
                MiuiDsdaStatistic.getInstance().recordVideoCallStatistics(connection);
            }
            if (connection instanceof ImsPhoneConnection) {
                MiDataChannelStatistic.getInstance().recordDataChannelInfoCallStatistics(getCommonRadioParamInfo(phoneId, true), connection.getTelecomCallId());
            }
            Utils.logD("MiuiTelephonyStatistic", "processCallDisconnectEvent phone.getPhoneType()=" + phone.getPhoneType() + ",conn.isIncoming()=" + connection.isIncoming());
            String availableOperatorNumeric3 = getAvailableOperatorNumeric(null, phoneId);
            if (phone.getPhoneType() != 5) {
                str = "csfbCn";
            } else {
                serviceState2 = this.mServiceState[phoneId];
                if (serviceState2 == null) {
                    str = "volteCn";
                } else {
                    str = "volteCn";
                }
            }
            zIsVideo = VideoProfile.isVideo(connection.getVideoState());
            if (connection.isWifi()) {
                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phoneId)) {
                    str = "vodataCn";
                } else {
                    str = "vowifiCn";
                }
            }
            if (zIsVideo) {
                if (str.equals("vowifiCn")) {
                    str = "viwifiCn";
                } else if (!str.equals("volteCn")) {
                    str = "vilteCn";
                } else {
                    str = "vilteCn";
                }
            }
            if (phone.getPhoneType() == 7) {
                str = "vontnCn";
            }
            Utils.logD("MiuiTelephonyStatistic", "isVideoCall = " + zIsVideo + "isWifiCall = " + connection.isWifi());
            String str4 = str;
            this.mModemStats.updateCallStats(phoneId, availableOperatorNumeric3, connection.isIncoming(), str4, i);
            serviceState = this.mServiceState[phoneId];
            if (serviceState == null) {
            }
        }
    }

    private int getNumType(Connection connection) {
        int iOrdinal = NumberType.UNKNOWN.ordinal();
        if (connection == null || connection.getAddress() == null) {
            return iOrdinal;
        }
        String address = connection.getAddress();
        if (PhoneNumberUtils.isEmergencyNumber(address)) {
            return NumberType.EMERGENCY.ordinal();
        }
        return (PhoneNumberUtils.isGlobalPhoneNumber(address) || PhoneNumberUtils.isChinaMobileNumber(address)) ? NumberType.ORDINARY.ordinal() : iOrdinal;
    }

    private String getMbnInfo(int i) {
        return i == 0 ? android.os.SystemProperties.get("persist.radio.mbn_sw_sub0", "") : android.os.SystemProperties.get("persist.radio.mbn_sw_sub1", "");
    }

    private HashMap getCommonParamInfo() {
        HashMap map = new HashMap();
        map.put("DeviceName", android.os.Build.DEVICE);
        return map;
    }

    private void addFoldStateInfo(HashMap map) {
        MiuiCurrentStateMonitor miuiCurrentStateMonitor;
        if (map == null) {
            return;
        }
        if ((MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) && (miuiCurrentStateMonitor = this.mMiuiCurrentStateMonitor) != null) {
            map.put("fold_state", String.valueOf(miuiCurrentStateMonitor.getFoldState()));
        }
    }

    public synchronized HashMap getCommonRadioParamInfo(int i, boolean z) {
        FiveGServiceInfo fiveGServiceInfo;
        try {
            if (!isValidatePhoneId(i)) {
                return null;
            }
            Phone phone = MiuiPhoneUtils.getPhone(i);
            int dataRat = getDataRat(this.mServiceState[i]);
            int voiceRat = getVoiceRat(this.mServiceState[i]);
            if (z) {
                dataRat = voiceRat;
            }
            String signalQuality = getSignalQuality(this.mSignalStrength[i], dataRat);
            if (Utils.isNr(dataRat) && (fiveGServiceInfo = this.mFiveGInfo[i]) != null) {
                signalQuality = String.valueOf(fiveGServiceInfo.getSnr());
            }
            String str = getDbm(this.mSignalStrength[i]) + "," + miui.telephony.TelephonyManager.getDefault().getMiuiLevel(this.mSignalStrength[i]);
            FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
            String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
            String province = fiveGCloudController != null ? fiveGCloudController.getProvince() : "";
            HashMap commonParamInfo = getCommonParamInfo();
            commonParamInfo.put("CellLocation", getCellLocation(phone));
            commonParamInfo.put("Network", getAvailableOperatorNumeric(null, i));
            commonParamInfo.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            commonParamInfo.put("SignalStrength", str);
            commonParamInfo.put("SignalQuality", signalQuality);
            commonParamInfo.put("Band", Utils.getBand(i, this.mServiceState[i]));
            commonParamInfo.put("dsda", String.valueOf(Utils.getDsdaState().ordinal()));
            commonParamInfo.put("simType", String.valueOf(Utils.getCustomSimTypeForStatistic(i)));
            addFoldStateInfo(commonParamInfo);
            if (DeviceUtil.IS_INTERNATIONAL_BUILD) {
                commonParamInfo.put("mbn", getMbnInfo(i));
            } else {
                commonParamInfo.put("c", city);
                commonParamInfo.put("p", province);
                commonParamInfo.put("SimIccidSS", this.mIccidSS[i]);
            }
            updatePoiParams(commonParamInfo);
            return commonParamInfo;
        } catch (Throwable th) {
            throw th;
        }
    }

    private String getCallType(Connection connection) {
        String str;
        Phone phone = getPhone(connection);
        if (phone == null) {
            return "NA";
        }
        int phoneId = phone.getPhoneId();
        if (!isValidatePhoneId(phoneId)) {
            return "NA";
        }
        if (phone.getPhoneType() == 7) {
            return "VoNTN";
        }
        if (phone.getPhoneType() != 5) {
            str = "CSFB";
        } else {
            ServiceState serviceState = this.mServiceState[phoneId];
            str = (serviceState == null || !Utils.isNr(serviceState.getDataNetworkType())) ? "VoLTE" : "VoNR";
        }
        boolean zIsVideo = VideoProfile.isVideo(connection.getVideoState());
        if (connection.isWifi()) {
            str = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phoneId) ? "VoDATA" : "VoWIFI";
        }
        if (zIsVideo) {
            if (str.equals("VoWIFI")) {
                str = "ViWIFI";
            } else if (str.equals("VoLTE") || str.equals("VoNR")) {
                str = "ViLTE";
            } else if (str.equals("VoDATA")) {
                str = "ViDATA";
            }
        }
        Utils.logD("MiuiTelephonyStatistic", "isVideoCall = " + zIsVideo + "isWifiCall = " + connection.isWifi());
        return str;
    }

    /* JADX WARN: Code duplicated, block: B:91:0x028c  */
    /* JADX WARN: Code duplicated, block: B:94:0x02a6  */
    /* JADX WARN: Code duplicated, block: B:97:0x02af  */
    /* JADX WARN: Instruction removed from duplicated block: B:97:0x02af, please report this as an issue */
    private boolean recordCallUnexpectedDisconnectCountEvent(int i, Connection connection) {
        boolean z;
        String str;
        if (isValidatePhoneId(i)) {
            int callDisconnectCause = getCallDisconnectCause(connection);
            String callDisconnectMessage = getCallDisconnectMessage(connection);
            String str2 = "";
            if (MiuiPhoneUtils.isSupportMtkCallDropStats() && (connection instanceof ImsPhoneConnection) && Utils.isLogicalFailureForDisconnectCall(callDisconnectCause)) {
                Object[] objArr = new Object[1];
                try {
                    if (ATCommandUtils.getInstance().atCmdRunSync(i, new String[]{"AT+EDMFAPP=20102,5,0", "+EDMFAPP"}, objArr)) {
                        Object obj = objArr[0];
                        if (obj instanceof String[]) {
                            String[] strArrSplit = ((String[]) obj)[0].split(",");
                            if (strArrSplit.length > 2 && "3".equals(strArrSplit[1]) && (str = strArrSplit[2]) != null && !"0".equals(str) && !"31".equals(str)) {
                                callDisconnectCause = 2025;
                                str2 = str;
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.logE("MiuiTelephonyStatistic", "Error processing AT command", e);
                }
            }
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, true);
            if (commonRadioParamInfo != null) {
                int voiceRat = getVoiceRat(this.mServiceState[i]);
                int dbm = getDbm(this.mSignalStrength[i]);
                String callType = getCallType(connection);
                commonRadioParamInfo.put("RAT", ServiceState.rilRadioTechnologyToString(voiceRat));
                commonRadioParamInfo.put("rsrp", String.valueOf(dbm));
                commonRadioParamInfo.put("Band", Utils.getBand(i, this.mServiceState[i]));
                commonRadioParamInfo.put("DiconnectCause", String.valueOf(callDisconnectCause));
                commonRadioParamInfo.put("DiconnectMessage", callDisconnectMessage);
                if (MiuiPhoneUtils.isSupportMtkCallDropStats() && !TextUtils.isEmpty(str2)) {
                    commonRadioParamInfo.put("Reason", str2);
                }
                commonRadioParamInfo.put("CallDirect", connection.isIncoming() ? "MT" : "MO");
                commonRadioParamInfo.put("CallType", callType);
                commonRadioParamInfo.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
                commonRadioParamInfo.put("numberType", String.valueOf(getNumType(connection)));
                commonRadioParamInfo.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
                commonRadioParamInfo.put("ConnDuration", String.valueOf(connection.getDurationMillis()));
                boolean z2 = connection instanceof ImsPhoneConnection;
                if (z2) {
                    commonRadioParamInfo.put("isDcCall", String.valueOf(MiDataChannelStatistic.getInstance().isDataChannelCall(connection.getTelecomCallId())));
                }
                commonRadioParamInfo.put("isEuicc", String.valueOf(isEuicc(i)));
                int dsdaUsageScene = Utils.getDsdaUsageScene(i);
                commonRadioParamInfo.put("dsdaUsageScene", String.valueOf(dsdaUsageScene));
                if (dsdaUsageScene != -1) {
                    commonRadioParamInfo.put("dsdaBandCombination", Utils.getBand(0, this.mServiceState[0]) + "_" + Utils.getBand(1, this.mServiceState[1]));
                }
                commonRadioParamInfo.put("sceneType", getSceneType());
                commonRadioParamInfo.put("networkType", this.mNetworkTypeName);
                boolean z3 = !Utils.isLogicalFailureForDisconnectCall(callDisconnectCause) || Utils.isDFXForDisconnectCall(callDisconnectCause);
                commonRadioParamInfo.put("UploadLog", String.valueOf(z3));
                ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
                if (iSatelliteModule != null && iSatelliteModule.isSatelliteEnabled() && callType.equals("VoNTN")) {
                    addSatelliteInfo(commonRadioParamInfo);
                    recordDFSSatelliteCallFailedEvent(connection);
                }
                updateRoamingParams(i, this.mServiceState[i], commonRadioParamInfo);
                Utils.logD("MiuiTelephonyStatistic", "recordCallUnexpectedDisconnectCountEvent data=" + commonRadioParamInfo);
                if (!Utils.isValidParams(commonRadioParamInfo)) {
                    Utils.logE("MiuiTelephonyStatistic", "recordCallUnexpectedDisconnectCountEvent invalid paramerters!!!");
                    return false;
                }
                Utils.logI("MiuiTelephonyStatistic", "Readyfor recordCallUnexpectedDisconnectCountEvent phoneId=" + i + ",cause=" + callDisconnectCause);
                if (!Utils.isLogicalFailureForDisconnectCall(callDisconnectCause) && z2) {
                    String telecomCallId = connection.getTelecomCallId();
                    if (this.historyCall.contains(telecomCallId)) {
                        Utils.logD("MiuiTelephonyStatistic", "recordCallUnexpectedDisconnectCountEvent: the call has collected modem data");
                    } else {
                        ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraSendDelayMessage(ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE, Integer.valueOf(callDisconnectCause), ResponseObject.RELAY_FAILED_CODE);
                        this.historyCall.add(telecomCallId);
                        Utils.logD("MiuiTelephonyStatistic", "recordCallUnexpectedDisconnectCountEvent: modemHydraStat ims call fail report");
                    }
                }
                if (!Utils.isLogicalFailureForDisconnectCall(callDisconnectCause)) {
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraSendDelayMessage(122, constructCallReportData(callDisconnectCause, i, connection), DELAY_GET_MD_CALL_REPORT);
                    Utils.logD("MiuiTelephonyStatistic", "recordCallUnexpectedDisconnectCountEvent: modemHydraStat get modem call report");
                }
                if (z3) {
                    recordDFSEventStatistic(915200010, commonRadioParamInfo);
                }
                if (DeviceUtil.IS_STABLE_VERSION) {
                    if (!Utils.isLogicalFailureForDisconnectCall(callDisconnectCause)) {
                        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_unexpected_disconnected", commonRadioParamInfo);
                        z = true;
                    } else {
                        Utils.logI("MiuiTelephonyStatistic", "CallUnexpectedDisconnect-LogicalFailure -ignore");
                    }
                    if (connection.isEmergencyCall()) {
                        recordEccCallInfo(commonRadioParamInfo, callDisconnectCause, connection);
                    }
                    HashMap map = new HashMap();
                    map.put("dsdaUsageScene", String.valueOf(dsdaUsageScene));
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_scene", map);
                    if (TextUtils.equals(callType, "VoNTN")) {
                        recordNtnCallInfo(commonRadioParamInfo, callDisconnectCause, connection);
                    }
                    if (!Utils.isLogicalFailureForDisconnectCall(callDisconnectCause)) {
                        Utils.logI("MiuiTelephonyStatistic", "MOD-TF-CALL: recordCallUnexpectedDisconnectCountEvent phoneId=" + i + ",cause=" + callDisconnectCause);
                        recordLogInfo(commonRadioParamInfo, "call_unexpected_disconnected");
                    }
                    return z;
                }
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_unexpected_disconnected", commonRadioParamInfo);
                z = false;
                if (connection.isEmergencyCall()) {
                    recordEccCallInfo(commonRadioParamInfo, callDisconnectCause, connection);
                }
                HashMap map2 = new HashMap();
                map2.put("dsdaUsageScene", String.valueOf(dsdaUsageScene));
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_scene", map2);
                if (TextUtils.equals(callType, "VoNTN")) {
                    recordNtnCallInfo(commonRadioParamInfo, callDisconnectCause, connection);
                }
                if (!Utils.isLogicalFailureForDisconnectCall(callDisconnectCause)) {
                    Utils.logI("MiuiTelephonyStatistic", "MOD-TF-CALL: recordCallUnexpectedDisconnectCountEvent phoneId=" + i + ",cause=" + callDisconnectCause);
                    recordLogInfo(commonRadioParamInfo, "call_unexpected_disconnected");
                }
                return z;
            }
        }
        return false;
    }

    /* JADX WARN: Failed to calculate best type for var: r4v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v10 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v6 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v6 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v7 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v6 ??, new type: boolean
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    private boolean recordCallConnFailEvent(int r19, com.android.internal.telephony.Connection r20) {
        /*
            Method dump skipped, instruction units count: 593
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.phone.statistics.MiuiTelephonyStatistic.recordCallConnFailEvent(int, com.android.internal.telephony.Connection):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6, types: [int] */
    private ConcurrentHashMap constructCallReportData(int i, int i2, Connection connection) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("cause", Integer.valueOf(i));
        concurrentHashMap.put("phoneId", Integer.valueOf(i2));
        concurrentHashMap.put("callType", Integer.valueOf((int) (connection.getConnectTime() == 0 ? connection.isIncoming() : 2)));
        if (connection.isEmergencyCall()) {
            concurrentHashMap.put("isEmergencyCall", 1);
            concurrentHashMap.put("eccNumber", connection.getAddress());
            return concurrentHashMap;
        }
        concurrentHashMap.put("isEmergencyCall", 0);
        concurrentHashMap.put("eccNumber", "null");
        return concurrentHashMap;
    }

    private void recordEccCallInfo(HashMap map, int i, Connection connection) {
        map.put("emergencyNumber", connection.getAddress());
        map.put("eccNormaDisconnect", String.valueOf(Utils.isLogicalFailureForDisconnectCall(i)));
        if (connection.getConnectTime() != 0) {
            map.put("eccConnectDuration", String.valueOf(connection.getConnectTime() - connection.getCreateTime()));
        } else {
            map.put("eccConnectDuration", String.valueOf(System.currentTimeMillis() - connection.getCreateTime()));
        }
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_ecc_info", map);
    }

    private void recordNtnCallInfo(HashMap map, int i, Connection connection) {
        Bundle ntnLocationInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnLocationInfo();
        if (ntnLocationInfo != null) {
            map.put("p", String.valueOf(ntnLocationInfo.getString("province")));
            map.put("city", String.valueOf(ntnLocationInfo.getString("city")));
        }
        map.put("callsetupduration", String.valueOf(TelephonyCommonUtils.getCallSetupDuration(connection)));
        map.put("callActive", isCallOffhook(connection) ? "1" : "0");
        if (connection != null && connection.getConnectionExtras() != null) {
            map.put("isCallBeforeSatelliteDataEnabled", connection.getConnectionExtras().getBoolean("isCallBeforeSatelliteDataEnabled") ? "1" : "0");
        }
        ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
        if (iSatelliteModule != null) {
            map.put("speechBitrate", String.valueOf(iSatelliteModule.getSpeechBitrate()));
            map.put("isSatelliteDataEnable", iSatelliteModule.isSatelliteDataEnabled() ? "1" : "0");
        }
        FoldStateMonitor.getInstance();
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "call_ntn_info", map);
    }

    private void addSatelliteInfo(HashMap map) {
        ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
        if (iSatelliteModule != null) {
            map.put("ntn_realTxPwr", String.valueOf(iSatelliteModule.getRealTxPwr()));
            map.put("ntn_tartxPwr", String.valueOf(iSatelliteModule.getTarTxPwr()));
            map.put("ntn_rssi", String.valueOf(iSatelliteModule.getRetRssi()));
            map.put("ntn_snr", String.valueOf(iSatelliteModule.getRetSnr()));
            map.put("altitude", String.valueOf(iSatelliteModule.getAltitude()));
            Bundle ntnLocationInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnLocationInfo();
            if (ntnLocationInfo != null) {
                map.put("speed", String.valueOf(ntnLocationInfo.getFloat("speed")));
            }
            map.put("ntn_vendor_info", TelephonyCommonUtils.getNtnVendorInfo());
            map.put("satid", String.valueOf(iSatelliteModule.getSatId()));
            map.put("offset", String.valueOf(iSatelliteModule.getFreqOff()));
            map.put("fer", String.valueOf(iSatelliteModule.getFer()));
            map.put("bandid", String.valueOf(iSatelliteModule.getNtnBandId()));
            map.put("ntnPosInfo", String.valueOf(iSatelliteModule.getNtnPosInfo()));
            map.put("error_calss", String.valueOf(iSatelliteModule.getErrorClass()));
            map.put("error_cause", String.valueOf(iSatelliteModule.getErrorCause()));
        }
    }

    private String getThermalThrottleInfo() {
        return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getThermalThrottleInfo();
    }

    private String getUlMimoInfo() {
        return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getUlMimoInfo();
    }

    private String getModemPowerSaveInfo() {
        return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getModemPowerSaveInfo();
    }

    private String getForce5GCloudInfo() {
        String str = android.os.SystemProperties.get("persist.radio.cloud.force.5g.version", "NA");
        String strConcat = "";
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (!TextUtils.isEmpty(strConcat)) {
                strConcat = strConcat.concat(", ");
            }
            strConcat = strConcat + "networkModeType" + i + " : " + String.valueOf(getPreferredNwModeFromDb(i));
        }
        if (!"NA".equals(str)) {
            strConcat = strConcat + ", cloudForce5GVersion : " + str;
        }
        if (!"NA".equals(str) || FiveGUtils.isFiveGInvisableByDefault()) {
            strConcat = strConcat + ", isFiveGCloudEnableBySecretCode : " + String.valueOf(FiveGUtils.isFiveGNetworkForceVisable());
        }
        if (!FiveGUtils.isFiveGInvisableByDefault()) {
            return strConcat;
        }
        return strConcat + ", isNetworkModeEnableByDevelop : " + String.valueOf(!FiveGUtils.isNetworkModeInVisableByDevelopOption());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getAvailableOperatorNumeric(ServiceState serviceState, int i) {
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : null;
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = this.mServiceState[i].getOperatorNumeric();
        }
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "NoSim" : operatorNumeric;
    }

    private String getCellLocation(Phone phone) {
        if (phone == null) {
            return "NA";
        }
        try {
            CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
            if (currentCellIdentity == null) {
                return "NA";
            }
            if (currentCellIdentity instanceof CellIdentityGsm) {
                CellIdentityGsm cellIdentityGsm = (CellIdentityGsm) currentCellIdentity;
                return cellIdentityGsm.getLac() + "," + cellIdentityGsm.getCid();
            }
            if (currentCellIdentity instanceof CellIdentityCdma) {
                CellIdentityCdma cellIdentityCdma = (CellIdentityCdma) currentCellIdentity;
                return cellIdentityCdma.getSystemId() + "," + cellIdentityCdma.getNetworkId() + "," + cellIdentityCdma.getBasestationId();
            }
            if (currentCellIdentity instanceof CellIdentityWcdma) {
                CellIdentityWcdma cellIdentityWcdma = (CellIdentityWcdma) currentCellIdentity;
                return cellIdentityWcdma.getLac() + "," + cellIdentityWcdma.getCid();
            }
            if (currentCellIdentity instanceof CellIdentityTdscdma) {
                CellIdentityTdscdma cellIdentityTdscdma = (CellIdentityTdscdma) currentCellIdentity;
                return cellIdentityTdscdma.getLac() + "," + cellIdentityTdscdma.getCid();
            }
            if (currentCellIdentity instanceof CellIdentityLte) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
                return cellIdentityLte.getTac() + "," + cellIdentityLte.getCi();
            }
            if (!(currentCellIdentity instanceof CellIdentityNr)) {
                return "NA";
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            return cellIdentityNr.getTac() + "," + cellIdentityNr.getNci();
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "getCellLocation e=" + e);
            return "NA";
        }
    }

    private long getNetworkClass(ServiceState serviceState) {
        return TelephonyManagerEx.getDefault().getNetworkClass(serviceState.getVoiceNetworkType());
    }

    private int getCallDisconnectCause(Connection connection) {
        int preciseDisconnectCause = connection.getPreciseDisconnectCause();
        if (preciseDisconnectCause != 0 && preciseDisconnectCause != 65535) {
            return preciseDisconnectCause;
        }
        if (connection instanceof ImsPhoneConnection) {
            int disconnectCause = connection.getDisconnectCause();
            ImsReasonInfo imsReasonInfo = ((ImsPhoneConnection) connection).getImsReasonInfo();
            return (disconnectCause != 36 || imsReasonInfo == null) ? disconnectCause + 10000 : imsReasonInfo.getCode() + 30000;
        }
        return connection.getDisconnectCause() + 20000;
    }

    private String getCallDisconnectMessage(Connection connection) {
        ImsReasonInfo imsReasonInfo;
        String extraMessage;
        if (!(connection instanceof ImsPhoneConnection) || (imsReasonInfo = ((ImsPhoneConnection) connection).getImsReasonInfo()) == null || (extraMessage = imsReasonInfo.getExtraMessage()) == null) {
            return null;
        }
        return extraMessage;
    }

    private Phone getPhone(Connection connection) {
        if (connection == null || connection.getCall() == null) {
            return null;
        }
        return connection.getCall().getPhone();
    }

    private void saveStringDataToSP(String str, String str2) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("telephony_statistic_data", 0).edit();
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    private void reloadStatisticDataFromSP() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("telephony_statistic_data", 0);
        try {
            String string = sharedPreferences.getString("volte_duration", "");
            if (!string.isEmpty()) {
                this.mVoLTERegisteredDuration = (ConcurrentHashMap) ObjectSerializer.deserialize(string);
            }
            Rlog.d("MiuiTelephonyStatistic", "reloadStatisticDataFromSP mVoLTERegisteredDuration=" + this.mVoLTERegisteredDuration);
            String string2 = sharedPreferences.getString("data_connection_state_duration", "");
            if (!string2.isEmpty()) {
                this.mDataConnectionStateDuration = Long.parseLong(string2);
            }
            Rlog.d("MiuiTelephonyStatistic", "reloadStatisticDataFromSP mDataConnectionStateDuration=" + this.mDataConnectionStateDuration);
        } catch (Exception e) {
            Rlog.d("MiuiTelephonyStatistic", "reloadStatisticDataFromSP exception=" + e);
        }
        reloadSignalStrengthBinDurationFromSP();
        if (SpecialNetworkStat.getInstance() != null) {
            SpecialNetworkStat.getInstance().reloadSpecialNetworkDataFromSp(sharedPreferences);
        }
        this.mModemStats.reloadModemStatsFromSP();
        reloadDualDataActivateStatistcFromSP();
        reloadNetworkConnectedTimeFromSp();
        if (MiuiPhoneUtils.isSubwayOptimizationSupported()) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reloadSubwayModeStatsFromSP();
        }
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reloadStatisticDataFromSP();
        if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
            MiuiQmsStats.getInstance().reloadStatsFromSP();
        }
        if (MiuiPhoneUtils.isFileUploadMonitorSupported()) {
            FileUploadStats.getInstance().reloadStatsFromSP();
        }
        this.mRoamingModemStats.reloadModemStatsFromSP();
        HikingModemStats hikingModemStats = this.mHikingModemStats;
        if (hikingModemStats != null) {
            hikingModemStats.reloadModemStatsFromSP();
        }
        HstModemStats hstModemStats = this.mHstModemStats;
        if (hstModemStats != null) {
            hstModemStats.reloadModemStatsFromSP();
        }
        SubwayModemStats subwayModemStats = this.mSubwayModemStats;
        if (subwayModemStats != null) {
            subwayModemStats.reloadModemStatsFromSP();
        }
        DriveModemStats driveModemStats = this.mDriveModemStats;
        if (driveModemStats != null) {
            driveModemStats.reloadModemStatsFromSP();
        }
        NsaModemStats nsaModemStats = this.mNsaModemStats;
        if (nsaModemStats != null) {
            nsaModemStats.reloadModemStatsFromSP();
        }
    }

    private Map bundleToHashMap(String str, Bundle bundle) {
        HashMap map = new HashMap();
        if ("start_ringer_info".equals(str)) {
            map.put("fire_wall_type", Integer.valueOf(bundle.getInt("fire_wall_type")));
            map.put("ringing_uri_is_null", Boolean.valueOf(bundle.getBoolean("ringing_uri_is_null")));
            map.put("is_ringing", Boolean.valueOf(bundle.getBoolean("is_ringing")));
            map.put("is_crs_call", Boolean.valueOf(bundle.getBoolean("is_crs_call")));
            map.put("is_colume_over_zero", Boolean.valueOf(bundle.getBoolean("is_colume_over_zero")));
            map.put("is_need_voice_report", Boolean.valueOf(bundle.getBoolean("is_need_voice_report")));
            map.put("audio_route", bundle.getString("audio_route"));
            map.put("is_DND", Boolean.valueOf(bundle.getBoolean("is_DND")));
            map.put("ringtone_played", Boolean.valueOf(bundle.getBoolean("ringtone_played")));
            map.put("ringtone_volume", Float.valueOf(bundle.getFloat("ringtone_volume")));
            localLog(str + " [callId:" + bundle.getString("callId") + "]", map);
            return map;
        }
        if ("start_vibration_info".equals(str)) {
            map.put("is_using_audio_coupled_haptics", Boolean.valueOf(bundle.getBoolean("is_using_audio_coupled_haptics")));
            map.put("has_external_control", Boolean.valueOf(bundle.getBoolean("has_external_control")));
            map.put("haptic_playback_supported", Boolean.valueOf(bundle.getBoolean("haptic_playback_supported")));
            map.put("is_vibrator_enabled", Boolean.valueOf(bundle.getBoolean("is_vibrator_enabled")));
            map.put("is_vibrating", Boolean.valueOf(bundle.getBoolean("is_vibrating")));
            map.put("should_ring_contact", Boolean.valueOf(bundle.getBoolean("should_ring_contact")));
            map.put("should_apply_ramping_ringer", Boolean.valueOf(bundle.getBoolean("should_apply_ramping_ringer")));
            map.put("is_audio_coupled_vibration", Boolean.valueOf(bundle.getBoolean("is_audio_coupled_vibration")));
            map.put("vibration_scene", bundle.getString("vibration_scene"));
            localLog(str + " [callId:" + bundle.getString("callId") + "]", map);
            return map;
        }
        if ("event_stk_idle_dialog".equals(str)) {
            map.put("numeric", bundle.getString("numeric"));
            map.put("stk_dialog_content", bundle.getString("stk_dialog_content"));
            return map;
        }
        if ("event_stk_idle_menu".equals(str)) {
            map.put("numeric", bundle.getString("numeric"));
            map.put("stk_menu_title", bundle.getString("stk_menu_title"));
            map.put("stk_menu_pop_count", Integer.valueOf(bundle.getInt("stk_menu_pop_count")));
            return map;
        }
        if ("event_app_txrx_transmission".equals(str)) {
            String string = bundle.getString("app_txrx_info");
            localLog(str, string);
            try {
                JSONArray jSONArray = new JSONArray(string);
                Rlog.d("MiuiTelephonyStatistic", "jsonArray: " + jSONArray.toString());
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = new JSONObject(jSONArray.get(i).toString());
                    Iterator<String> itKeys = jSONObject.keys();
                    HashMap map2 = new HashMap();
                    while (itKeys.hasNext()) {
                        String strValueOf = String.valueOf(itKeys.next());
                        map2.put(strValueOf, jSONObject.get(strValueOf));
                    }
                    arrayList.add(map2);
                }
                map.put("app_txrx_info", arrayList);
            } catch (Exception e) {
                Rlog.e("MiuiTelephonyStatistic", "json array exception");
                e.printStackTrace();
            }
        } else {
            if ("relaunch_incall_service".equals(str) || "detect_dialer_ui".equals(str)) {
                map.put("abnormal_scene", Integer.valueOf(bundle.getInt("abnormal_scene")));
                map.put("default_dialer_name", bundle.getString("default_dialer_name"));
                map.put("countryIso", bundle.getString("countryIso"));
                return map;
            }
            if ("event_yellow_page_info".equals(str)) {
                map.put("query_yellow_page_time", Double.valueOf(bundle.getDouble("query_yellow_page_time")));
                map.put("is_yellow_page_call", Boolean.valueOf(bundle.getBoolean("is_yellow_page_call")));
            }
        }
        return map;
    }

    public void reportTelephonyStatisticData() throws Throwable {
        StreamingOptimizationStats streamingOptimizationStats;
        Utils.logI("MiuiTelephonyStatistic", "reportTelephonyStatisticData enter");
        if (!shouldStatistic()) {
            TelephonyJobService.cancelReportJobSchedule(PhoneGlobals.getInstance());
            return;
        }
        Map statisticData = getStatisticData();
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_statistic_data", statisticData);
        Utils.logD("MiuiTelephonyStatistic", "reportTelephonyStatisticData data=" + statisticData.toString());
        if (SpecialNetworkStat.getInstance() != null) {
            SpecialNetworkStat.getInstance().reportSpecialNetworkEvent();
        }
        recordSignalStrengthBinDuration();
        reportDataSetupAfterWiFiEvent();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            String availableOperatorNumeric = getAvailableOperatorNumeric(null, i);
            if (CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), i, "carrier_volte_globalization_enable") && !CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), i, "carrier_volte_unavailable_bool")) {
                this.mVolteRegisterMonitor.recordVolteGlobalizationStats();
                this.mVolteRegisterMonitor.recordVolteGlobalizationVolteSwitch(i, phone, availableOperatorNumeric);
            }
        }
        MiuiImsStatistics.recordVoWiFiRedisteredDuration();
        recordModemStats();
        recordFcnStats();
        recordDsiStatistic();
        MiuiNetworkInfoStats.getInstance().reportNetworkInfoStats();
        clearTelephonyStatisticData();
        DefaultPreferenceHelper.getInstance().revokeUserPermission();
        MiuiCallStat miuiCallStat = this.mCallStat;
        if (miuiCallStat != null) {
            miuiCallStat.reportCallConnectTimeEvent();
        }
        recordMaxNetworkRate();
        ModemComponentStats modemComponentStats = ModemComponentStats.getInstance();
        if (modemComponentStats != null) {
            modemComponentStats.recordEfsData();
        }
        if (MiuiDsdaStatistic.getInstance() != null) {
            MiuiDsdaStatistic.getInstance().reportStatistics();
        }
        if (MiuiCallStat.getInstance(this.mContext) != null) {
            MiuiCallStat.getInstance(this.mContext).reportViceCardCallStats();
        }
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).performModemLogUploadDailyJob();
        if (MiuiPhoneUtils.isSubwayOptimizationSupported()) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportSubwayModeStatistics();
        }
        if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
            MiuiQmsStats.getInstance().reportStatistics();
        }
        if (MiuiPhoneUtils.isCellPreferOptSupported()) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportSubwayFdd2TddStatistic();
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportCellHdrStatistic();
        }
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).reportStatistics();
        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
        if (MiuiPhoneUtils.isSelfStudySupported() && MiuiPhoneUtils.isSupportVideoDataStallOptimization()) {
            iNetworkModule.reportVideoAndGameAiBenefitStatistic();
            iNetworkModule.reportVideoStatistic();
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                iNetworkModule.reportGameResidentCellStatistic();
                iNetworkModule.reportStreamMediaStatistic();
                iNetworkModule.reportSafStatistic();
                iNetworkModule.reportStallStatistic();
            }
        }
        if (MiuiPhoneUtils.isSelfStudySupported() && MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
            iNetworkModule.reportGameResidentCellStatistic();
        }
        if (MiuiPhoneUtils.isDeviceModemTcpKaSupported()) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).reportModemTcpKaStatistics();
        }
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportStatistics();
        if (TelephonyManagerEx.getDefault().isFiveGASupported() && FiveGAStatistics.getInstance() != null) {
            FiveGAStatistics.getInstance().reportStatistics();
        }
        Context context = this.mContext;
        if (context != null && SimStatistic.getInstance(context) != null) {
            SimStatistic.getInstance(this.mContext).recordMiSimData(this.mServiceState);
        }
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).reportStatistics();
        if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).reportCallOptimizeStatistic();
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportNetworkOptimizerGlobalSafStatistic();
        }
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportNetworkOptimizerGlobalSafStatistic();
        }
        if (MiuiPhoneUtils.isFileUploadMonitorSupported()) {
            FileUploadStats.getInstance().reportStatistics();
        }
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimizationV2()) {
            INetworkModule iNetworkModule2 = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
            iNetworkModule2.reportNetworkOptimizerGlobalVideoBenefitStatistic();
            iNetworkModule2.reportNetworkOptimizerGlobalSafDetailStatistic();
            iNetworkModule2.reportStreamMediaStatistic();
        }
        if (MiuiPhoneUtils.isStreamingOptimizationSupported() && (streamingOptimizationStats = StreamingOptimizationStats.getInstance()) != null) {
            streamingOptimizationStats.reportStreamingStats();
        }
        doRecordDataCallStatus();
        doRecordSarSensorException();
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).reportStatistics();
        reportQmiInfo();
        if (SmartDualSimSwitch.getInstance() != null) {
            SmartDualSimSwitch.getInstance().reportSmartDualSimSwitchStatistic();
            SmartDualSimSwitch.getInstance().reportUserDualSimSwitchStatistic();
        }
    }

    private void recordFcnStats() {
        if (this.mFcnStats.isEmpty()) {
            Utils.logD("MiuiTelephonyStatistic", "fcnStats is empty");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : this.mFcnStats.entrySet()) {
            String str = (String) entry.getKey();
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) entry.getValue();
            HashMap map = new HashMap();
            for (Map.Entry entry2 : concurrentHashMap.entrySet()) {
                map.put(String.valueOf(entry2.getKey()), entry2.getValue());
            }
            map.put("Network", str);
            arrayList.add(map);
        }
        Utils.logD("MiuiTelephonyStatistic", "recordFcnStats params= " + arrayList);
        recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_fcn", arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceUpdateStatisticData() throws Throwable {
        Utils.logD("MiuiTelephonyStatistic", "forceUpdateStatisticData");
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            processVoLTERegisteredStateChange(i, PhoneProxy.isVolteEnabled(MiuiPhoneUtils.getPhone(i)), true);
            updateOutOfServiceDuration(i, this.mLastSimStateWithNetworkState[i]);
            forceUpdataModemStats(i, true);
        }
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            recordDataConnectionDuration(telephonyManager.getDataState(), true);
        }
        if (SpecialNetworkStat.getInstance() != null) {
            SpecialNetworkStat.getInstance().recordSpecialNetworkData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processShutDownEvent() {
        MiuiCallStat miuiCallStat = this.mCallStat;
        if (miuiCallStat != null) {
            miuiCallStat.updateCallRecords();
        }
    }

    public void clearTelephonyStatisticData() {
        this.mVoLTERegisteredDuration.clear();
        this.mDataConnectionStateDuration = 0L;
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("telephony_statistic_data", 0).edit();
        editorEdit.clear();
        editorEdit.commit();
        DefaultPreferenceHelper.getInstance().setUserHotPlugCounter(0);
        clearBlockedNetworkDuration();
        clearSignalStrengthBinDuration();
        this.mModemStats.clearModemStats();
        this.mNwOosDurations.clear();
        this.mNwSearchDurations.clear();
        this.mFcnStats.clear();
        this.mNoLteNrDurations.clear();
        this.mDsiStats.clear();
        clearDualDataActiveStats();
        clearNetworkConnectedTime();
        this.mRoamingModemStats.clearModemStats();
        HikingModemStats hikingModemStats = this.mHikingModemStats;
        if (hikingModemStats != null) {
            hikingModemStats.clearModemStats();
        }
        HstModemStats hstModemStats = this.mHstModemStats;
        if (hstModemStats != null) {
            hstModemStats.clearModemStats();
        }
        SubwayModemStats subwayModemStats = this.mSubwayModemStats;
        if (subwayModemStats != null) {
            subwayModemStats.clearModemStats();
        }
        DriveModemStats driveModemStats = this.mDriveModemStats;
        if (driveModemStats != null) {
            driveModemStats.clearModemStats();
        }
        NsaModemStats nsaModemStats = this.mNsaModemStats;
        if (nsaModemStats != null) {
            nsaModemStats.clearModemStats();
        }
        MiuiNetworkInfoStats.getInstance().clearNetworkInfoStats();
    }

    private void getFiveGSwitch(Map map) {
        boolean zIs5GNetworkMode = Utils.is5GNetworkMode(Utils.getPreferredNwModeFromDb(getDefaultDataSlotId()));
        try {
            map.put("FiveGOn", zIs5GNetworkMode ? "1" : "0");
            Utils.logD("MiuiTelephonyStatistic", "fiveGStats fiveGSwitch = " + zIs5GNetworkMode);
            if (FiveGUtils.isDualNrSupported()) {
                FiveGManagerBase.getInstance();
                boolean zIsDualNrEnabled = FiveGManagerBase.isDualNrEnabled();
                map.put("FiveGDualNr", zIsDualNrEnabled ? "1" : "0");
                Utils.logD("MiuiTelephonyStatistic", "fiveGStats dual 5g = " + zIsDualNrEnabled);
            }
            if (MiuiPhoneUtils.is5gSaSupported()) {
                int fiveGNetworkMode = FiveGManagerBase.getInstance().getFiveGNetworkMode();
                map.put("FiveGNetworkMode", String.valueOf(fiveGNetworkMode));
                Utils.logD("MiuiTelephonyStatistic", "fiveGStats 5g network mode = " + fiveGNetworkMode);
                if (FiveGUtils.isDualSaSupported()) {
                    boolean zIsDualSaEnabled = FiveGManagerBase.getInstance().isDualSaEnabled();
                    map.put("FiveGDualSa", zIsDualSaEnabled ? "1" : "0");
                    Utils.logD("MiuiTelephonyStatistic", "fiveGStats dual sa = " + zIsDualSaEnabled);
                }
            }
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "fiveGStats exception:" + e);
        }
    }

    private String getIccidSS(int i) {
        TelephonyManager telephonyManager;
        if (isValidatePhoneId(i) && (telephonyManager = this.mTelephonyManager) != null) {
            String simOperatorNumericForPhone = telephonyManager.getSimOperatorNumericForPhone(i);
            Phone phone = MiuiPhoneUtils.getPhone(i);
            String iccSerialNumber = phone != null ? phone.getIccSerialNumber() : "";
            if (!TextUtils.isEmpty(iccSerialNumber) && !TextUtils.isEmpty(simOperatorNumericForPhone) && iccSerialNumber.length() == 20) {
                if (ServiceProviderUtils.isChinaMobile(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(8, 10);
                }
                if (ServiceProviderUtils.isChinaUnicom(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(9, 11);
                }
                if (ServiceProviderUtils.isChinaTelecom(simOperatorNumericForPhone)) {
                    return iccSerialNumber.substring(10, 13);
                }
            }
        }
        return "";
    }

    private String cloudCategoryToKey(String str) {
        str.hashCode();
        switch (str) {
            case "TelephonyBWPMode":
                return "CloudBwpMode";
            case "TelephonyN28Mode":
                return "CloudN28Mode";
            case "TelephonyN1Mode":
                return "CloudN1Mode";
            case "Telephony5GDsdaSwitch":
                return "Cloud5GDsdaSwitch";
            case "Telephony5GSaSwitch":
                return "Cloud5GSaSwitch";
            case "TelephonyVonrSwitch":
                return "CloudVonrSwitch";
            case "TelephonyViceSaSwitch":
                return "CloudViceSaSwitch";
            default:
                return "";
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Map getStatisticData() throws Throwable {
        Class<INetworkModule> cls;
        Class<ITelephonyModule> cls2;
        int i;
        TelephonyManager telephonyManager;
        String strNvReadItem;
        Class<INetworkModule> cls3 = INetworkModule.class;
        Class<ITelephonyModule> cls4 = ITelephonyModule.class;
        forceUpdateStatisticData();
        HashMap map = new HashMap();
        try {
            for (String str : this.mVoLTERegisteredDuration.keySet()) {
                map.put(str, String.valueOf(this.mVoLTERegisteredDuration.get(str)));
            }
            int i2 = 1;
            if (!MiuiPhoneUtils.shouldHideIntelligentDualSimButton()) {
                int i3 = (Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(this.mContext.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1 && Settings.Global.getInt(this.mContext.getContentResolver(), "dual_mobile_data", 0) == 1) ? 1 : 0;
                map.put("IntelligentDualSim", String.valueOf(i3));
                if (i3 != 0) {
                    map.put("DualDataActivateCount", String.valueOf(this.mDualDataActivateCount));
                    map.put("DualDataActivateDurations", String.valueOf(this.mDualDataActivateDurations));
                    map.put("DualDataActivatePkgNames", String.valueOf(this.mDualDataActivatePkgNames));
                }
            } else if (MiuiPhoneUtils.isSmartSwitchDualSimSupported() && !MiuiPhoneUtils.shouldHideSmartDualSimButton() && !MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) {
                map.put("SmartSwitchDualSim", String.valueOf((Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(this.mContext.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1) ? 1 : 0));
            }
            if (MiuiPhoneUtils.isFiveGHostedModeSupported()) {
                int i4 = Settings.Global.getInt(this.mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", 1);
                int i5 = Settings.System.getInt(this.mContext.getContentResolver(), "power_center_5g_save_mode", 1);
                map.put("SmartSwitch", String.valueOf(i4));
                map.put("FiveGPowerSave", String.valueOf(i5));
            }
            if (this.mSupportFiveG) {
                getFiveGSwitch(map);
                map.put("force5GCloudStatistic", getForce5GCloudInfo());
            }
            if (DeviceUtil.isModemOfflineSupported() && (telephonyManager = this.mTelephonyManager) != null && (strNvReadItem = telephonyManager.nvReadItem(6860)) != null) {
                String[] strArrSplit = strNvReadItem.split(";");
                if (strArrSplit.length > 10) {
                    map.put("ModemOffline", strArrSplit[10]);
                }
            }
            if (MiuiPhoneUtils.isRfExceptionStatsSupported()) {
                RilHook rilHookInit = RilHook.init(this.mContext);
                if (rilHookInit != null) {
                    String oemNvItem = rilHookInit.readOemNvItem(6860);
                    Rlog.d("MiuiTelephonyStatistic", "value: " + oemNvItem);
                    if (oemNvItem != null) {
                        String[] strArrSplit2 = oemNvItem.split(";");
                        if (strArrSplit2.length > 1 && !"0".equals(strArrSplit2[1])) {
                            map.put("NV1", strArrSplit2[1]);
                        }
                        if (strArrSplit2.length > 10 && !"0".equals(strArrSplit2[10])) {
                            map.put("NV10", strArrSplit2[10]);
                        }
                        if (strArrSplit2.length > 11 && !"0".equals(strArrSplit2[11])) {
                            map.put("NV11", strArrSplit2[11]);
                        }
                    }
                    map.put("hwLevel", android.os.SystemProperties.get("ro.boot.hwlevel", "-1"));
                }
                if (FiveGManagerBase.getInstance() != null) {
                    map.put("hwId", String.valueOf(FiveGManagerBase.getInstance().getHwId()));
                }
            }
            if (MiuiPhoneUtils.isDSDASupport()) {
                map.put("DsdaStatus", String.valueOf(FiveGManagerBase.getInstance().getDSDAStateBySetting()));
            }
            if (this.mTelephonyManager != null) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                StringBuilder sb3 = new StringBuilder();
                StringBuilder sb4 = new StringBuilder();
                StringBuilder sb5 = new StringBuilder();
                int i6 = 0;
                while (true) {
                    int i7 = MiuiPhoneUtils.PHONE_COUNT;
                    if (i6 >= i7) {
                        break;
                    }
                    String simOperatorNumericForPhone = this.mTelephonyManager.getSimOperatorNumericForPhone(i6);
                    int i8 = i2;
                    String networkOperatorForPhone = this.mTelephonyManager.getNetworkOperatorForPhone(i6);
                    String strValueOf = String.valueOf(isEuicc(i6));
                    sb.append(simOperatorNumericForPhone);
                    sb2.append(this.mIccidSS[i6]);
                    sb3.append(networkOperatorForPhone);
                    Class<INetworkModule> cls5 = cls3;
                    Class<ITelephonyModule> cls6 = cls4;
                    sb4.append(this.mNoLteNrCounter[i6]);
                    sb5.append(strValueOf);
                    if (i6 != i7 - 1) {
                        sb.append(",");
                        sb2.append(",");
                        sb3.append(",");
                        sb4.append(",");
                        sb5.append(",");
                    }
                    i6++;
                    cls3 = cls5;
                    i2 = i8;
                    cls4 = cls6;
                }
                cls = cls3;
                cls2 = cls4;
                i = i2;
                map.put("SimOperator", sb.toString());
                map.put("SimIccidSS", sb2.toString());
                map.put("DdsPhoneId", String.valueOf(getDefaultDataSlotId()));
                map.put("NetworkPlmn", sb3.toString());
                map.put("noltenrcounter", sb4.toString());
                map.put("isEuicc", sb5.toString());
            } else {
                cls = cls3;
                cls2 = cls4;
                i = 1;
            }
            map.put("VolteSwitch", MiuiPhoneUtils.getVolteEnabledStatus(this.mContext));
            String voWiFiEnabledStatus = MiuiPhoneUtils.getVoWiFiEnabledStatus(this.mContext);
            if (voWiFiEnabledStatus.length() != 0) {
                map.put("VoWiFiSwitch", voWiFiEnabledStatus);
            }
            map.put("DataConnectionStateDuration", String.valueOf(this.mDataConnectionStateDuration));
            map.put("UserHotplugCounter", String.valueOf(DefaultPreferenceHelper.getInstance().getUserHotPlugCounter()));
            map.put("PropertyOperatingMode", String.valueOf(Utils.getPropertyOperatingMode()));
            if (MiuiPhoneUtils.isSaPowerOptimizationSupported()) {
                map.put("SilentDisableSaCount", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSilentDisableSaCountForStatis());
                map.put("NightModeDurationSeconds", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getNightModeDuration());
                boolean z = HyperOSCustFeatureResolve.getBoolean("support_special_night_mode", false);
                Rlog.d("MiuiTelephonyStatistic", "isSupportSpecialNightMode: " + z);
                if (z) {
                    Rlog.d("MiuiTelephonyStatistic", "reportStatistics begin");
                    String reportedMessageIDs = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getReportedMessageIDs();
                    Rlog.d("MiuiTelephonyStatistic", reportedMessageIDs);
                    if (reportedMessageIDs != null && !reportedMessageIDs.isEmpty()) {
                        String[] strArrSplit3 = reportedMessageIDs.split("\\|");
                        map.put("ReportedWhiteRilIds", strArrSplit3[0]);
                        map.put("ReportedBlackRilIds", strArrSplit3[i]);
                        map.put("ReportedSleepIndicationRilIds", strArrSplit3[2]);
                    }
                    map.put("ReportedNightIndicationRilIds", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getNightReportedMessageIDs());
                    map.put("ReportedScreenOffIndicationRilIds", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getScreenOffReportedMessageIDs());
                    map.put("NightModeNoSleepDuration", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getNightModeNoSleepDuration());
                    map.put("ScreenOffDuration", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getScreenOffDuration());
                    map.put("AllModeModemSleepSeconds", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getAllModeModemSleepSeconds());
                    map.put("MaxMinAvgModemSleepSeconds", ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getAvgMinMaxModemSleepRatio());
                    Rlog.d("MiuiTelephonyStatistic", "reportStatistics FINISHED");
                }
            }
            if (MiuiPhoneUtils.isMimoSceneFiveGOptimizeSupported()) {
                map.put("MimoSceneChangeRxCount", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getMimoSceneChangeRxCount()));
            }
            if (TelephonyManagerEx.getDefault().isN28Supported()) {
                map.put("modemSSRCausedByN28Count", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getModemSSRCausedByN28Count()));
            }
            if (TelephonyManagerEx.getDefault().isBWPSupported()) {
                map.put("modemSSRCausedByBwpCount", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getModemSSRCausedByBwpCount()));
            }
            if (TelephonyManagerEx.getDefault().isN1Supported()) {
                map.put("modemSSRCausedByN1Count", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getModemSSRCausedByN1Count()));
            }
            if (FiveGCloudController.getInstance() != null && FiveGCloudController.getInstance().getCloudStatus() != null && FiveGCloudController.getInstance().getCloudStatus().keySet() != null) {
                for (String str2 : FiveGCloudController.getInstance().getCloudStatus().keySet()) {
                    map.put(cloudCategoryToKey(str2), (String) FiveGCloudController.getInstance().getCloudStatus().get(str2));
                }
            }
            if (MiuiPhoneUtils.isThermalThrottleSupported() && !MiuiPhoneUtils.isThermalThrottleDisableByCloud()) {
                map.put("thermalThrottleStatistic", getThermalThrottleInfo());
            }
            for (int i9 = 0; i9 < MiuiPhoneUtils.PHONE_COUNT; i9++) {
                map.put("SignalStrengthBigChangeCount" + i9, this.signalStrengthMonitor[i9].getSignalStrengthBigChangeCount());
                map.put("OosDuration" + i9, this.signalStrengthMonitor[i9].getOOSDuration());
                map.put("signalShakeCount" + i9, this.signalStrengthMonitor[i9].getSignalShakeCount());
                map.put("RatNetworkModeDuration" + i9, this.signalStrengthMonitor[i9].getRatNetworkModeDuration());
                map.put("nrBetterLteDuration" + i9, this.signalStrengthMonitor[i9].getNrBetterDuration());
            }
            if (TelephonyManagerEx.getDefault().isPlatform8550()) {
                map.put("modemSleep", MiuiPhoneUtils.getModemSleepCount("persist.radio.sleep"));
                map.put("modemAbnormalSleep", MiuiPhoneUtils.getModemSleepCount("persist.radio.abnormalsleep"));
            }
            if (!Build.IS_STABLE_VERSION) {
                map.put("always_receive_mms_switch_state", String.valueOf(Settings.System.getInt(this.mContext.getContentResolver(), "always_enable_mms", -1)));
            }
            HashMap signalStrengthOptimizationCountMap = SignalStrengthMonitor.getSignalStrengthOptimizationCountMap();
            for (String str3 : signalStrengthOptimizationCountMap.keySet()) {
                map.put(str3, (String) signalStrengthOptimizationCountMap.get(str3));
            }
            if (MiuiPhoneUtils.isUlMimoOptimizationEnabled()) {
                map.put("ulMimoDisabledStatistic", String.valueOf(getUlMimoInfo()));
            }
            if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
                map.put("specialNetworkOptimizationEnabled", String.valueOf(Settings.Global.getInt(this.mContext.getContentResolver(), "special_network_optimization", -1)));
            }
            map.put("volteDelayOptimization", MiuiDisplayInfoController.getVolteDelayOptimizationCountInCall());
            if (((ITelephonyModule) ModuleDelegate.get(cls2)).isSmartCallForwardSupported()) {
                map.put("SmartCallForwardSwitch", String.valueOf(MiuiSettings.Telephony.isSmartCallForwardEnabled(this.mContext.getContentResolver()) ? 1 : 0));
            }
            if (com.android.phone.utils.Utils.isSupportSensorAnswer(this.mContext)) {
                map.put("SensorAnswerSwitch", String.valueOf(TelephonySettings.isSensorAnswerEnabled(this.mContext.getContentResolver()) ? 1 : 0));
            }
            if (MiuiPhoneUtils.isSupportVoiceLinkFeature() && Utils.IS_INTERNATIONAL_BUILD && !TelephonyCloudController.CLOUD_VOICE_LINK_FEATURE_DISABLED) {
                for (int i10 = 0; i10 < MiuiPhoneUtils.PHONE_COUNT; i10++) {
                    if (i10 != DefaultSimManager.getDefaultDataSlotId()) {
                        map.put("enable_vocie_link_" + i10, String.valueOf(((ITelephonyModule) ModuleDelegate.get(cls2)).isCrossSimCallingEnabledByUser(this.mContext, i10)));
                    }
                }
            }
            if (MiuiPhoneUtils.isSupportPowerSaveV2() && (ModemProvider.IS_MTK || ModemProvider.IS_XRING)) {
                map.put("modemPowerSaveStatistic", getModemPowerSaveInfo());
            }
            if (MiuiPhoneUtils.isNsaScgPoorOptimizationSupported()) {
                map.put("nsaScgPoorOptimizationCount", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getNsaScgPoorOptimizationCounts()));
                map.put("nsaScgPoorFreezeRecoverCount", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getNsaScgPoorFreezeRecoverCounts()));
            }
            if (MiuiPhoneUtils.isEsimCapable() && !((ITelephonyModule) ModuleDelegate.get(cls2)).isRemoveEsimSwitch()) {
                int i11 = Settings.Secure.getInt(this.mContext.getContentResolver(), "is_enable_esim_for_user", -1);
                boolean z2 = i11 != -1 ? i : 0;
                map.put("eSIMEnabled", String.valueOf(i11));
                map.put("eSIMUsed", String.valueOf(z2));
                if (i11 == i) {
                    ArrayList arrayList = new ArrayList();
                    List<EuiccProfileInfo> allEsimProfiles = ((ITelephonyModule) ModuleDelegate.get(cls2)).getAllEsimProfiles();
                    if (allEsimProfiles != null && !allEsimProfiles.isEmpty()) {
                        int i12 = 0;
                        for (EuiccProfileInfo euiccProfileInfo : allEsimProfiles) {
                            CarrierIdentifier carrierIdentifier = euiccProfileInfo.getCarrierIdentifier();
                            if (carrierIdentifier != null) {
                                String mcc = carrierIdentifier.getMcc();
                                String mnc = carrierIdentifier.getMnc();
                                String serviceProviderName = euiccProfileInfo.getServiceProviderName();
                                if (mcc != null && mnc != null && serviceProviderName != null) {
                                    arrayList.add(mcc + "," + mnc + "," + serviceProviderName);
                                }
                                if (euiccProfileInfo.getState() == 1) {
                                    i12++;
                                }
                            }
                        }
                        map.put("eSIMProfileDetails", String.valueOf(arrayList));
                        map.put("eSIMProfileCounts", String.valueOf(i12));
                    }
                }
            }
            if (TelephonyManagerEx.getDefault().isFiveGASupported()) {
                boolean zIsFiveGARegistration = FiveGManagerBase.getInstance().isFiveGARegistration();
                map.put("5ga_registered", String.valueOf(zIsFiveGARegistration));
                if (zIsFiveGARegistration) {
                    FiveGManagerBase.getInstance().setFiveGARegistration(false);
                }
                map.put("5ga_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getFiveGAState()));
            }
            if (TelephonyManagerEx.getDefault().isHOMSupported()) {
                map.put("hom_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getHOMState()));
            }
            if (TelephonyManagerEx.getDefault().isSwulSupported()) {
                map.put("swul_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getSwulState()));
            }
            if (TelephonyManagerEx.getDefault().isR16PowerSupported()) {
                map.put("r16_power_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getR16PowerState()));
            }
            if (TelephonyManagerEx.getDefault().isNetSlicingSupported()) {
                map.put("net_slicing_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getNetSlicingState()));
            }
            if (TelephonyManagerEx.getDefault().isConditionalHandoverSupported()) {
                map.put("conditional_handover_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getConditionalHandoverState()));
            }
            if (TelephonyManagerEx.getDefault().isFullPowerModeSupported()) {
                map.put("full_power_mode_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getFullPowerModeState()));
            }
            if (TelephonyManagerEx.getDefault().isSulSupported()) {
                map.put("sul_switch_enable", String.valueOf(FiveGManagerBase.getInstance().getSulState()));
            }
            if (TelephonyManagerEx.getDefault().is3gppVersionSupported()) {
                map.put("3gpp_version_state", String.valueOf(FiveGManagerBase.getInstance().get3GppVersionSate()));
            }
            map.put("ULPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(cls)).getPeakUlRateBps()));
            map.put("DLPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(cls)).getPeakDlRateBps()));
            ((INetworkModule) ModuleDelegate.get(cls)).clearPeakRateBps();
            if (MiuiPhoneUtils.isDeviceSupportFlip()) {
                int flipAutoAnswerCallCount = TelephonySettings.getFlipAutoAnswerCallCount(this.mContext.getContentResolver());
                int flipAutoHangupCallCount = TelephonySettings.getFlipAutoHangupCallCount(this.mContext.getContentResolver());
                int flipMistakeHangupCallCount = TelephonySettings.getFlipMistakeHangupCallCount(this.mContext.getContentResolver());
                boolean zIsFlipAutoAnswerCall = TelephonySettings.isFlipAutoAnswerCall(this.mContext.getContentResolver());
                boolean zIsFlipAutoHangupCall = TelephonySettings.isFlipAutoHangupCall(this.mContext.getContentResolver());
                if (flipAutoAnswerCallCount > 0) {
                    map.put("flip_answer_call_count", String.valueOf(flipAutoAnswerCallCount));
                    TelephonySettings.setFlipAutoAnswerCallCount(this.mContext.getContentResolver(), 0);
                }
                if (flipAutoHangupCallCount > 0) {
                    map.put("flip_hangup_call_count", String.valueOf(flipAutoHangupCallCount));
                    TelephonySettings.setFlipAutoHangupCallCount(this.mContext.getContentResolver(), 0);
                }
                if (flipMistakeHangupCallCount > 0) {
                    map.put("flip_mistake_hangup_call_count", String.valueOf(flipMistakeHangupCallCount));
                    TelephonySettings.setFlipMistakeHangupCallCount(this.mContext.getContentResolver(), 0);
                }
                map.put("flip_answer_call_enable", String.valueOf(zIsFlipAutoAnswerCall));
                map.put("flip_hangup_call_enable", String.valueOf(zIsFlipAutoHangupCall));
            }
            String string = PhoneGlobals.getInstance().getResources().getString(android.R.string.device_storage_monitor_notification_channel);
            if (MiuiPhoneUtils.isPad() && RelayUtils.Relay.PRIMARY_LOCATION_TIMEZONE_PROVIDER.equals(string)) {
                int tzidChangedByLocationCount = TelephonySettings.getTzidChangedByLocationCount(this.mContext.getContentResolver());
                int tzidChangedByRelayCount = TelephonySettings.getTzidChangedByRelayCount(this.mContext.getContentResolver());
                boolean zIsLocationTzidDetcetion = TelephonySettings.isLocationTzidDetcetion(this.mContext.getContentResolver());
                int tzidWIFIProviderNullCount = TelephonySettings.getTzidWIFIProviderNullCount(this.mContext.getContentResolver());
                int tzidWIFICoordInvalidCount = TelephonySettings.getTzidWIFICoordInvalidCount(this.mContext.getContentResolver());
                int tzidTimeShapeResultNullCount = TelephonySettings.getTzidTimeShapeResultNullCount(this.mContext.getContentResolver());
                if (tzidChangedByLocationCount > 0) {
                    map.put("tzid_changed_location_count", String.valueOf(tzidChangedByLocationCount));
                    TelephonySettings.setTzidChangedByLocationCount(this.mContext.getContentResolver(), 0);
                }
                if (tzidChangedByRelayCount > 0) {
                    map.put("tzid_changed_relay_count", String.valueOf(tzidChangedByRelayCount));
                    TelephonySettings.setTzidChangedByRelayCount(this.mContext.getContentResolver(), 0);
                }
                if (tzidWIFIProviderNullCount > 0) {
                    map.put("tzid_wifi_provider_null_count", String.valueOf(tzidWIFIProviderNullCount));
                    TelephonySettings.setTzidWIFIProviderNullCount(this.mContext.getContentResolver(), 0);
                }
                if (tzidWIFICoordInvalidCount > 0) {
                    map.put("tzid_wifi_coord_invalid_count", String.valueOf(tzidWIFICoordInvalidCount));
                    TelephonySettings.setTzidWIFICoordInvalidCount(this.mContext.getContentResolver(), 0);
                }
                if (tzidTimeShapeResultNullCount > 0) {
                    map.put("tzid_timeshape_result_null_count", String.valueOf(tzidTimeShapeResultNullCount));
                    TelephonySettings.setTzidTimeShapeResultNullCount(this.mContext.getContentResolver(), 0);
                }
                map.put("location_tzid_detection_enable", String.valueOf(zIsLocationTzidDetcetion));
            }
            map.putAll(getExternalStatistic());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    public class DataStat {
        public String apn;
        public String apnType;
        public String cause;
        public String reason;
        public String setupDuration;

        public DataStat() {
            reset();
        }

        public void reset() {
            this.apn = "NA";
            this.apnType = "NA";
            this.reason = "NA";
            this.cause = "NA";
            this.setupDuration = "NA";
        }

        public String toString() {
            return "{DataStat apn=" + this.apn + " apnType=" + this.apnType + " reason=" + this.reason + " cause=" + this.cause + " setupDuration=" + this.setupDuration + "}";
        }

        public void updateDataStat(Map map) {
            if (map == null || map.isEmpty()) {
                return;
            }
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                String str2 = (String) entry.getValue();
                if (str != null && str2 != null) {
                    if (str.equals("apnName")) {
                        this.apn = str2;
                    } else if (str.equals("apnType")) {
                        this.apnType = str2;
                    } else if (str.equals("reasonCode")) {
                        this.reason = str2;
                    } else if (str.equals("errorCode")) {
                        this.cause = str2;
                    } else if (str.equals("setupDuration")) {
                        this.setupDuration = str2;
                    }
                }
            }
        }
    }

    public class DataEvaluation {
        public String dataEvaluation;
        public String evalutionReason;
        public String requestType;

        public DataEvaluation() {
            reset();
        }

        public void reset() {
            this.requestType = "NA";
            this.dataEvaluation = "NA";
            this.evalutionReason = "NA";
        }

        public String toString() {
            return "{DataEvaluation : requestType=" + this.requestType + " dataEvaluation=" + this.dataEvaluation + " evalutionReason=" + this.evalutionReason + "}";
        }

        public void updateDataEvaluation(Map map) {
            if (map == null || map.isEmpty()) {
                return;
            }
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                String str2 = (String) entry.getValue();
                if (str != null && str2 != null) {
                    if (str.equals("requestType")) {
                        this.requestType = str2;
                    } else if (str.equals("evalution")) {
                        this.dataEvaluation = str2;
                    } else if (str.equals("evalution_reason")) {
                        this.evalutionReason = str2;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordUnsolDataCallError(int i, int i2, String str) {
        String signalQuality;
        String strValueOf;
        FiveGServiceInfo fiveGServiceInfo;
        if (!isValidatePhoneId(i) || this.mServiceState[i] == null) {
            return;
        }
        HashMap map = new HashMap();
        map.put("ErrorCause", String.valueOf(i2));
        map.put("dnn", str);
        int dataRat = getDataRat(this.mServiceState[i]);
        if (Utils.isNr(this.mServiceState[i].getVoiceNetworkType()) && (fiveGServiceInfo = this.mFiveGInfo[i]) != null) {
            signalQuality = String.valueOf(fiveGServiceInfo.getSnr());
            strValueOf = String.valueOf(this.mFiveGInfo[i].getRsrp());
        } else {
            signalQuality = getSignalQuality(this.mSignalStrength[i], dataRat);
            strValueOf = String.valueOf(getDbm(this.mSignalStrength[i]));
        }
        String operatorNumeric = this.mServiceState[i].getOperatorNumeric();
        String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        boolean roaming = this.mServiceState[i].getRoaming();
        map.put("vplmn", operatorNumeric);
        map.put("hplmn", simOperatorForSlot);
        map.put("roaming", String.valueOf(roaming));
        map.put("isInCall", MiuiPhoneUtils.isDuringCall() ? "1" : "0");
        map.put("wifiActive", MiuiPhoneUtils.isWifiConnected() ? "1" : "0");
        FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
        String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
        map.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
        map.put("SignalStrength", strValueOf);
        map.put("SignalQuality", signalQuality);
        map.put("Band", Utils.getBand(i, this.mServiceState[i]));
        map.put("dsda", String.valueOf(Utils.getDsdaState().ordinal()));
        map.put("c", city);
        map.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
        int dataRegistrationState = getDataRegistrationState(this.mServiceState[i]);
        int voiceRegistrationState = getVoiceRegistrationState(this.mServiceState[i]);
        map.put("dataRegState", String.valueOf(dataRegistrationState));
        map.put("voiceRegState", String.valueOf(voiceRegistrationState));
        ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
        if (foregroundInfo != null) {
            map.put("foregroundPkg", foregroundInfo.mForegroundPackageName);
        }
        if (MiuiPhoneUtils.isSignalMapNOMLSupported()) {
            map.put("rr", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getTopOneRegion());
        }
        if (!str.equals("ims") && this.mSetupDataSucceedConnectedTime != 0) {
            map.put("setupDataSucceedDuration", String.valueOf((System.currentTimeMillis() - this.mSetupDataSucceedConnectedTime) / 1000));
            map.put("driveState", getSensorDriveState() == 10 ? "1" : "0");
            map.put("ULPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getUnsolPeakUlRateBps()));
            map.put("DLPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getUnsolPeakDlRateBps()));
        }
        map.put("simType", String.valueOf(Utils.getCustomSimTypeForStatistic(i)));
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "unsol_data_call_error", map);
        HashMap map2 = new HashMap();
        map2.putAll(map);
        map2.put("c", "");
        localLog("unsol_data_call_error", map2.toString());
        this.mSetupDataSucceedConnectedTime = 0L;
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).clearUnsolPeakRateBps();
    }

    private int getSensorDriveState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_drive_state", 0);
    }

    class NetworkCallback extends ConnectivityManager.NetworkCallback {
        NetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            MiuiTelephonyStatistic.this.updateNetworkInfo(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            ConnectivityManager connectivityManager;
            MiuiTelephonyStatistic.this.updateNetworkInfo(network);
            if (!MiuiPhoneUtils.isMobileDataEnabled(MiuiTelephonyStatistic.this.mContext) && (connectivityManager = (ConnectivityManager) MiuiTelephonyStatistic.this.mContext.getSystemService("connectivity")) != null) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
                if (!MiuiPhoneUtils.isWifiEnabled() || ((MiuiPhoneUtils.isWifiEnabled() && networkInfo != null && networkInfo.getState() != NetworkInfo.State.CONNECTED) || MiuiTelephonyStatistic.this.mIsAirPlaneMode)) {
                    MiuiTelephonyStatistic.this.mLastNetworkInfo = null;
                }
            }
            if (MiuiTelephonyStatistic.this.mIsAirPlaneMode) {
                MiuiTelephonyStatistic.this.mLastNetworkInfo = null;
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            MiuiTelephonyStatistic.this.updateNetworkInfo(network);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkInfo(Network network) {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (network == null || networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                return;
            }
            this.mLastNetworkInfo = networkInfo;
            Rlog.d("MiuiTelephonyStatistic", "NetworkCallback called, mLastNetworkInfo update to " + this.mLastNetworkInfo);
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int i, Map map) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(5, dataStatType.ordinal(), i, map).sendToTarget();
        }

        public void onDataConnectionChanged(int i, int i2) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(10, i, i2).sendToTarget();
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(11, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(2, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(3, i, 0, cellLocation).sendToTarget();
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(14, i, i2).sendToTarget();
        }
    }

    private void doApnRecoveryEvent(int i, Map map) {
        Utils.logD("MiuiTelephonyStatistic", "doApnRecoveryEvent phoneId=" + i);
        if (isValidatePhoneId(i)) {
            Rlog.d("MiuiTelephonyStatistic", "mDataStat " + map.toString());
            HashMap commonParamInfo = getCommonParamInfo();
            if (commonParamInfo == null) {
                return;
            }
            commonParamInfo.putAll(map);
            if (!Utils.isValidParams(commonParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "doApnRecoveryEvent invalid paramerters!!!");
            } else {
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "apn_recovery", commonParamInfo);
            }
        }
    }

    private synchronized void processConsecutiveDataCallError(boolean z) {
        MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
        try {
            if (z) {
                if (miuiTelephonyStatisticHandler.hasMessages(75)) {
                    this.mStatisticHandler.removeMessageWithCount(75);
                    recordConsecutiveDataCallError();
                }
            } else {
                if (miuiTelephonyStatisticHandler.hasMessages(75)) {
                    return;
                }
                this.mLastDataCallErrorTime = SystemClock.elapsedRealtime();
                MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler2 = this.mStatisticHandler;
                miuiTelephonyStatisticHandler2.sendMessageDelayed(miuiTelephonyStatisticHandler2.obtainMessage(75), 180000L);
                Utils.logD("MiuiTelephonyStatistic", "after 180 seconds, there is no connection data to trigger stats");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean isConsecutiveDataCallError() {
        if (!isValidatePhoneId(this.mLastDdsPhoneId)) {
            return false;
        }
        return isSimReady(this.mLastDdsPhoneId) && !(this.mNetworkType == 1) && MiuiPhoneUtils.isMobileDataEnabled(this.mContext) && !this.mIsAirPlaneMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordConsecutiveDataCallError() {
        HashMap commonRadioParamInfo;
        if (!isConsecutiveDataCallError()) {
            Utils.logD("MiuiTelephonyStatistic", "Does not meet the continuous data call error, return");
            return;
        }
        if (isValidatePhoneId(this.mLastDdsPhoneId) && (commonRadioParamInfo = getCommonRadioParamInfo(this.mLastDdsPhoneId, false)) != null) {
            dataCallErrorCommomParam(commonRadioParamInfo, this.mLastDdsPhoneId, Boolean.FALSE);
            commonRadioParamInfo.put("duration", String.valueOf((SystemClock.elapsedRealtime() - this.mLastDataCallErrorTime) / 1000));
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            if (foregroundInfo != null) {
                commonRadioParamInfo.put("foregroundPkg", foregroundInfo.mForegroundPackageName);
            }
            commonRadioParamInfo.put("ScreenStatus", MiuiPhoneUtils.isScreenOn(this.mContext) ? "On" : "Off");
            commonRadioParamInfo.put("isInCall", MiuiPhoneUtils.isDuringCall() ? "1" : "0");
            commonRadioParamInfo.put("wifiActive", MiuiPhoneUtils.isWifiConnected() ? "1" : "0");
            commonRadioParamInfo.put("sleepMode", String.valueOf(this.mIsSleepMode));
            Utils.logD("MiuiTelephonyStatistic", "recordConsecutiveDataCallError data= " + commonRadioParamInfo);
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_consecutive_data_call_error", commonRadioParamInfo);
        }
    }

    private void recordDataSetupAfterWiFiEvent(Map map) {
        String str;
        NetworkInfo networkInfo;
        NetworkInfo networkInfo2;
        String str2 = "";
        if (map == null || !map.containsKey("apnType")) {
            str = "";
        } else {
            str = (String) map.get("apnType");
        }
        if (str.contains("default")) {
            if (map != null && map.containsKey("setupDuration")) {
                str2 = (String) map.get("setupDuration");
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (TextUtils.isEmpty(str2) || TextUtils.equals(str2, "0") || !MiuiPhoneUtils.isMobileDataEnabled(this.mContext)) {
                    return;
                }
                if ((MiuiPhoneUtils.isWifiConnected() && activeNetworkInfo != null && activeNetworkInfo.getType() == 1 && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED && (networkInfo2 = this.mLastNetworkInfo) != null && networkInfo2.getType() == 1) || (activeNetworkInfo == null && (networkInfo = this.mLastNetworkInfo) != null && networkInfo.getType() == 1)) {
                    storeDataSetupDuration(Integer.parseInt(str2));
                }
            }
        }
    }

    private void storeDataSetupDuration(int i) {
        if (i > 0) {
            int iFloorDiv = Math.floorDiv(i, 1000) + 1;
            if (iFloorDiv > 4 && iFloorDiv <= 9) {
                iFloorDiv = 5;
            } else if (iFloorDiv > 9) {
                iFloorDiv = 6;
            }
            HashMap map = this.mWifiToCellularDurations;
            if (map != null) {
                Integer num = (Integer) map.get(Integer.valueOf(iFloorDiv));
                HashMap map2 = this.mWifiToCellularDurations;
                if (num != null) {
                    map2.put(Integer.valueOf(iFloorDiv), Integer.valueOf(num.intValue() + 1));
                } else {
                    map2.put(Integer.valueOf(iFloorDiv), 1);
                }
            }
        }
    }

    private void reportDataSetupAfterWiFiEvent() {
        String str;
        if (this.mWifiToCellularDurations != null) {
            HashMap map = new HashMap();
            for (Map.Entry entry : this.mWifiToCellularDurations.entrySet()) {
                switch (((Integer) entry.getKey()).intValue()) {
                    case 1:
                        str = "setupDurationExcellent";
                        break;
                    case 2:
                        str = "setupDurationGreat";
                        break;
                    case 3:
                        str = "setupDurationGood";
                        break;
                    case 4:
                        str = "setupDurationModerate";
                        break;
                    case 5:
                        str = "setupDurationPoor";
                        break;
                    case 6:
                        str = "setupDurationBad";
                        break;
                    default:
                        continue;
                }
                map.put(str, String.valueOf(entry.getValue()));
            }
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "setup_data_after_wifi_event", map);
        }
    }

    private void doDataCallEvent(boolean z, int i, Map map) {
        String str;
        Utils.logD("MiuiTelephonyStatistic", "doDataCallEvent status=" + z);
        recordDataSetupAfterWiFiEvent(map);
        if (isValidatePhoneId(i)) {
            processConsecutiveDataCallError(z);
            DataStat dataStat = new DataStat();
            this.mDataStat = dataStat;
            dataStat.updateDataStat(map);
            Rlog.d("MiuiTelephonyStatistic", "mDataStat " + this.mDataStat.toString());
            processDataCallDuration(z, this.mDataStat);
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, false);
            if (commonRadioParamInfo == null) {
                return;
            }
            dataCallErrorCommomParam(commonRadioParamInfo, i, Boolean.valueOf(z));
            if (MiuiPhoneUtils.isSignalMapNOMLSupported()) {
                commonRadioParamInfo.put("rr", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getTopOneRegion());
            }
            int wifiSignalStrength = getWifiSignalStrength(this.mContext);
            if (this.mNetworkType == 1) {
                str = "1," + String.valueOf(wifiSignalStrength);
            } else {
                str = "0";
            }
            commonRadioParamInfo.put("WiFiSignalStrength", str);
            boolean zShouldUploadDataCallErrorLog = shouldUploadDataCallErrorLog(commonRadioParamInfo);
            Utils.logD("MiuiTelephonyStatistic", "doDataCallEvent data =" + commonRadioParamInfo);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "doDataCallEvent invalid paramerters!!!");
                return;
            }
            Utils.logI("MiuiTelephonyStatistic", "Readyfor doDataCallEvent status=" + z + ",phoneId=" + i);
            if (!z) {
                Rlog.d("MiuiTelephonyStatistic", "MOD-TF-DATA - cause " + this.mDataStat.cause);
                if (this.mDataStat.cause.equals(this.mLastDataCallFailure[i]) && !MiuiPhoneUtils.isSupportQcCallEndReason()) {
                    Rlog.d("MiuiTelephonyStatistic", "duplicate data call failure with  " + this.mLastDataCallFailure[i]);
                } else {
                    if (this.mDataStat.apnType.contains("default") && MiuiPhoneUtils.isSupportQcCallEndReason() && ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getModemDataCollectorCallEndInfo()) {
                        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
                        DataStat dataStat2 = this.mDataStat;
                        iNetworkModule.updateCallEndReason(dataStat2.apnType, dataStat2.cause, commonRadioParamInfo);
                    }
                    this.mLastDataCallFailure[i] = this.mDataStat.cause;
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_setup_call_error", commonRadioParamInfo);
                    if (shouldRecordDFSEvent(i)) {
                        commonRadioParamInfo.put("UploadLog", String.valueOf(zShouldUploadDataCallErrorLog));
                        recordDFSEventStatistic(915000006, commonRadioParamInfo);
                    }
                    HashMap map2 = new HashMap();
                    map2.putAll(commonRadioParamInfo);
                    map2.put("c", "");
                    map2.put("p", "");
                    map2.put("CellLocation", "");
                    localLog("data_setup_call_error", map2.toString());
                }
            } else {
                this.mLastDataCallFailure[i] = "NA";
                if (DeviceUtil.IS_STABLE_VERSION) {
                    Utils.logI("MiuiTelephonyStatistic", "DataCall-OK -ignore");
                } else {
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_setup_call_error", commonRadioParamInfo);
                    if (shouldRecordDFSEvent(i)) {
                        commonRadioParamInfo.put("UploadLog", String.valueOf(zShouldUploadDataCallErrorLog));
                        recordDFSEventStatistic(915000006, commonRadioParamInfo);
                    }
                    HashMap map3 = new HashMap();
                    map3.putAll(commonRadioParamInfo);
                    map3.put("c", "");
                    map3.put("p", "");
                    map3.put("CellLocation", "");
                    localLog("data_setup_call_error", map3.toString());
                }
            }
            if (!z) {
                recordLogInfo(commonRadioParamInfo, "data_setup_call_error");
            }
            if (z) {
                this.mSetupDataSucceedConnectedTime = System.currentTimeMillis();
            }
        }
    }

    private boolean shouldUploadDataCallErrorLog(HashMap map) {
        String str = (String) map.get("dataRegState");
        return (str == null || "1".equals(str) || containsExcludedErrorCode((String) map.get("ErrorCause"))) ? false : true;
    }

    private boolean containsExcludedErrorCode(String str) {
        if (str != null && !str.isEmpty()) {
            for (String str2 : mExcludedErrorCodes) {
                if (str.contains(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void processDataCallDuration(boolean z, DataStat dataStat) {
        String str;
        if (dataStat == null || (str = dataStat.apnType) == null || !str.contains("default")) {
            Utils.logD("MiuiTelephonyStatistic", "processDataCallDuration, apnType is not default, skip");
            return;
        }
        try {
            int durationBucketIndex = getDurationBucketIndex(Integer.parseInt(dataStat.setupDuration));
            synchronized (this) {
                try {
                    if (z) {
                        int[] iArr = this.mDefaultApnSuccessDurationCounts;
                        iArr[durationBucketIndex] = iArr[durationBucketIndex] + 1;
                    } else {
                        int[] iArr2 = this.mDefaultApnFailureDurationCounts;
                        iArr2[durationBucketIndex] = iArr2[durationBucketIndex] + 1;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (NumberFormatException unused) {
            Utils.logE("MiuiTelephonyStatistic", "Invalid duration format: " + dataStat.setupDuration);
        }
    }

    private int getDurationBucketIndex(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = DURATION_BUCKETS_MS;
            if (i2 < iArr.length) {
                if (i < iArr[i2]) {
                    return i2;
                }
                i2++;
            } else {
                return iArr.length;
            }
        }
    }

    private void doRecordDataCallStatus() {
        int i;
        String strBuildDistributionString;
        String strBuildDistributionString2;
        HashMap map = new HashMap();
        synchronized (this) {
            i = this.mDefaultApnRequestCount;
            strBuildDistributionString = buildDistributionString(this.mDefaultApnSuccessDurationCounts);
            strBuildDistributionString2 = buildDistributionString(this.mDefaultApnFailureDurationCounts);
        }
        map.put("dataCallTotalRequests", String.valueOf(i));
        map.put("dataCallSuccessDistribution", strBuildDistributionString);
        map.put("dataCallFailureDistribution", strBuildDistributionString2);
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_data_call_status", map);
        resetDataCallStatus();
    }

    private void doRecordSarSensorException() {
        String str = android.os.SystemProperties.get("persist.radio.sar.excpt_cnt");
        Utils.logD("MiuiTelephonyStatistic", "doRecordSarSensorException current=" + str);
        if (str == null || str.isEmpty()) {
            return;
        }
        String[] strArrSplit = str.split(",");
        if (strArrSplit.length >= 2) {
            try {
                String str2 = strArrSplit[0];
                String str3 = strArrSplit[1];
                HashMap map = new HashMap();
                map.put("sarsensor_state3_exception", str2);
                map.put("sarsensor_state4_exception", str3);
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_sarsensor_exception", map);
                android.os.SystemProperties.set("persist.radio.sar.excpt_cnt", "0,0");
            } catch (Exception e) {
                Utils.logD("MiuiTelephonyStatistic", "recordSarException happens Exception");
                e.printStackTrace();
            }
        }
    }

    private String buildDistributionString(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iArr.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(iArr[i]);
        }
        return sb.toString();
    }

    private void resetDataCallStatus() {
        this.mDefaultApnRequestCount = 0;
        int[] iArr = this.mDefaultApnSuccessDurationCounts;
        if (iArr != null) {
            Arrays.fill(iArr, 0);
        }
        int[] iArr2 = this.mDefaultApnFailureDurationCounts;
        if (iArr2 != null) {
            Arrays.fill(iArr2, 0);
        }
    }

    private int getWifiSignalStrength(Context context) {
        WifiInfo connectionInfo;
        int rssi;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null || !wifiManager.isWifiEnabled() || (connectionInfo = wifiManager.getConnectionInfo()) == null || (rssi = connectionInfo.getRssi()) < -100 || rssi > -55) {
            return -1;
        }
        return rssi;
    }

    private void doDataEvaluationFailed(boolean z, int i, Map map) {
        Utils.logD("MiuiTelephonyStatistic", "doDataEvaluationFailed status=" + z);
        if (isValidatePhoneId(i)) {
            DataEvaluation dataEvaluation = new DataEvaluation();
            this.mDataEvaluation = dataEvaluation;
            dataEvaluation.updateDataEvaluation(map);
            Rlog.d("MiuiTelephonyStatistic", "mDataStat " + this.mDataEvaluation.toString());
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, false);
            if (commonRadioParamInfo == null) {
                return;
            }
            dataEvaluationFailedErrorCommomParam(commonRadioParamInfo, i, Boolean.valueOf(z));
            Utils.logD("MiuiTelephonyStatistic", "doDataEvaluationFailed data =" + commonRadioParamInfo);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "doDataEvaluationFailed invalid paramerters!!!");
                return;
            }
            Utils.logI("MiuiTelephonyStatistic", "Readyfor doDataEvaluationFailed status=" + z + ",phoneId=" + i);
            if (!z) {
                Rlog.d("MiuiTelephonyStatistic", "cause " + this.mDataEvaluation.dataEvaluation);
                boolean zEquals = this.mDataEvaluation.dataEvaluation.equals(this.mLastDataDataEvaluationFailure[i]);
                String[] strArr = this.mLastDataDataEvaluationFailure;
                if (zEquals) {
                    Rlog.d("MiuiTelephonyStatistic", "duplicate data call failure with  " + strArr[i]);
                } else {
                    strArr[i] = this.mDataEvaluation.dataEvaluation;
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_evaluation_error", commonRadioParamInfo);
                }
            }
            if (z) {
                return;
            }
            recordLogInfo(commonRadioParamInfo, "data_evaluation_error");
        }
    }

    private void doRecordDataInterruptionStatus(int i, Map map) {
        LinkProperties linkProperties;
        String interfaceName;
        String dnsServers;
        String strValueOf;
        if (isValidatePhoneId(i)) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 0) {
                Utils.logI("MiuiTelephonyStatistic", "startDiagnosticsInternal -networkInfo is not connected!!!");
                linkProperties = null;
            } else {
                linkProperties = connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork());
            }
            if (linkProperties != null && !TextUtils.isEmpty(linkProperties.getInterfaceName())) {
                interfaceName = linkProperties.getInterfaceName();
                strValueOf = String.valueOf(linkProperties.getMtu());
                dnsServers = getDnsServers(linkProperties.getDnsServers());
            } else {
                interfaceName = "NA";
                dnsServers = "NA";
                strValueOf = dnsServers;
            }
            String str = MiuiPhoneUtils.isScreenOn(this.mContext) ? "On" : "Off";
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, false);
            if (commonRadioParamInfo == null) {
                return;
            }
            int dataRat = getDataRat(this.mServiceState[i]);
            int dataRegistrationState = getDataRegistrationState(this.mServiceState[i]);
            int voiceRegistrationState = getVoiceRegistrationState(this.mServiceState[i]);
            int dbm = getDbm(this.mSignalStrength[i]);
            commonRadioParamInfo.put("wifiActive", String.valueOf(this.mNetworkType == 1));
            commonRadioParamInfo.put("userDataEnable", phone.isUserDataEnabled() ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
            commonRadioParamInfo.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            commonRadioParamInfo.put("rsrp", String.valueOf(dbm));
            commonRadioParamInfo.put("Band", Utils.getBand(i, this.mServiceState[i]));
            commonRadioParamInfo.put("Apn", String.valueOf(MiuiPhoneUtils.isVirtualSim(i)));
            commonRadioParamInfo.put("Interface", interfaceName);
            commonRadioParamInfo.put("Mtu", strValueOf);
            commonRadioParamInfo.put("DnsServers", dnsServers);
            commonRadioParamInfo.put("ScreenStatus", str);
            commonRadioParamInfo.put("Trigger", this.mFakeDataTrigger);
            commonRadioParamInfo.put("isInCall", String.valueOf(MiuiPhoneUtils.isDuringCall()));
            commonRadioParamInfo.put("dataRegState", String.valueOf(dataRegistrationState));
            commonRadioParamInfo.put("voiceRegState", String.valueOf(voiceRegistrationState));
            commonRadioParamInfo.put("isWechatVoip", String.valueOf(this.mIsWechatVoip));
            if (this.mSupportFiveG) {
                commonRadioParamInfo.put("NrConfigType", String.valueOf(getNrConfigType(i)));
            }
            commonRadioParamInfo.put("sleepMode", String.valueOf(this.mIsSleepMode));
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            if (foregroundInfo != null) {
                commonRadioParamInfo.put("foregroundPkg", foregroundInfo.mForegroundPackageName);
            }
            if (TelephonyManagerEx.getDefault().isFiveGASupported()) {
                boolean zIsFiveGARegistration = FiveGManagerBase.getInstance().isFiveGARegistration();
                commonRadioParamInfo.put("3CC_registered", String.valueOf(zIsFiveGARegistration));
                commonRadioParamInfo.put("2CC_registered", String.valueOf(isInNrcaState(this.mServiceState[i]) && !zIsFiveGARegistration));
            }
            commonRadioParamInfo.put("sceneType", getSceneType());
            commonRadioParamInfo.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
            commonRadioParamInfo.putAll(map);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "doRecordDataInterruptionStatus invalid paramerters!!!");
                return;
            }
            Utils.logD("MiuiTelephonyStatistic", "doRecordDataInterruptionStatus data =" + commonRadioParamInfo);
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_data_interruption_recovery", commonRadioParamInfo);
        }
    }

    private void doRecordDataInterruptionCause(int i, Map map) {
        HashMap commonRadioParamInfo;
        if (isValidatePhoneId(i) && (commonRadioParamInfo = getCommonRadioParamInfo(i, false)) != null) {
            commonRadioParamInfo.putAll(map);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "doRecordDataInterruptionCause invalid paramerters!!!");
            } else {
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_data_interruption_cause", commonRadioParamInfo);
            }
        }
    }

    private void dataCallErrorCommomParam(Map map, int i, Boolean bool) {
        DataStat dataStat = this.mDataStat;
        if (dataStat == null) {
            return;
        }
        map.put("Apn", dataStat.apn);
        map.put("ApnType", this.mDataStat.apnType);
        map.put("SetupDuration", this.mDataStat.setupDuration);
        map.put("ErrorCause", bool.booleanValue() ? "NA" : this.mDataStat.cause);
        if (this.mSupportFiveG) {
            map.put("NrConfigType", String.valueOf(getNrConfigType(i)));
        }
        map.put("PreferredApn", Utils.getPreferredApn(this.mContext, android.telephony.SubscriptionManager.getSubscriptionId(i)));
        ServiceState serviceState = this.mServiceState[i];
        if (serviceState != null) {
            String operatorNumeric = serviceState.getOperatorNumeric();
            String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
            boolean roaming = this.mServiceState[i].getRoaming();
            map.put("vplmn", operatorNumeric);
            map.put("hplmn", simOperatorForSlot);
            map.put("roaming", String.valueOf(roaming));
        }
        map.put("sceneType", getSceneType());
        map.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
    }

    private void dataEvaluationFailedErrorCommomParam(Map map, int i, Boolean bool) {
        DataEvaluation dataEvaluation = this.mDataEvaluation;
        if (dataEvaluation == null) {
            return;
        }
        map.put("dataRequestType", dataEvaluation.requestType);
        map.put("dataEvaluation", this.mDataEvaluation.dataEvaluation);
        map.put("dataEvaluationReason", bool.booleanValue() ? "NA" : this.mDataEvaluation.evalutionReason);
    }

    private void doFakeDataConnectionEvent(int i) {
        String str;
        LinkProperties linkProperties;
        String dnsServers;
        if (isValidatePhoneId(i)) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            String strValueOf = "NA";
            if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
                str = "NA";
            } else {
                str = "wifi active";
            }
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 0) {
                Utils.logI("MiuiTelephonyStatistic", "startDiagnosticsInternal -networkInfo is not connected!!!");
                linkProperties = null;
            } else {
                linkProperties = connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork());
            }
            if (linkProperties == null || TextUtils.isEmpty(linkProperties.getInterfaceName())) {
                dnsServers = "NA";
            } else {
                linkProperties.getInterfaceName();
                strValueOf = String.valueOf(linkProperties.getMtu());
                dnsServers = getDnsServers(linkProperties.getDnsServers());
            }
            String strValueOf2 = String.valueOf(this.mRecoveryAction);
            String str2 = MiuiPhoneUtils.isScreenOn(this.mContext) ? "On" : "Off";
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, false);
            if (commonRadioParamInfo == null) {
                return;
            }
            int dataRat = getDataRat(this.mServiceState[i]);
            int dataRegistrationState = getDataRegistrationState(this.mServiceState[i]);
            int voiceRegistrationState = getVoiceRegistrationState(this.mServiceState[i]);
            int dbm = getDbm(this.mSignalStrength[i]);
            commonRadioParamInfo.put("wifiActive", str);
            commonRadioParamInfo.put("userDataEnable", phone.isUserDataEnabled() ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
            commonRadioParamInfo.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            commonRadioParamInfo.put("rsrp", String.valueOf(dbm));
            commonRadioParamInfo.put("Band", Utils.getBand(i, this.mServiceState[i]));
            commonRadioParamInfo.put("Apn", String.valueOf(MiuiPhoneUtils.isVirtualSim(i)));
            commonRadioParamInfo.put("Interface", strValueOf2);
            commonRadioParamInfo.put("Mtu", strValueOf);
            commonRadioParamInfo.put("DnsServers", dnsServers);
            commonRadioParamInfo.put("ScreenStatus", str2);
            commonRadioParamInfo.put("Trigger", this.mFakeDataTrigger);
            commonRadioParamInfo.put("CameraMtpl", getCameraMtplAdjustState());
            commonRadioParamInfo.put("isInCall", String.valueOf(MiuiPhoneUtils.isDuringCall()));
            commonRadioParamInfo.put("dataRegState", String.valueOf(dataRegistrationState));
            commonRadioParamInfo.put("voiceRegState", String.valueOf(voiceRegistrationState));
            commonRadioParamInfo.put("isWechatVoip", String.valueOf(this.mIsWechatVoip));
            if (this.mSupportFiveG) {
                commonRadioParamInfo.put("NrConfigType", String.valueOf(getNrConfigType(i)));
            }
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            if (foregroundInfo != null) {
                commonRadioParamInfo.put("foregroundPkg", foregroundInfo.mForegroundPackageName);
            }
            if (TelephonyManagerEx.getDefault().isFiveGASupported()) {
                boolean zIsFiveGARegistration = FiveGManagerBase.getInstance().isFiveGARegistration();
                commonRadioParamInfo.put("3CC_registered", String.valueOf(zIsFiveGARegistration));
                commonRadioParamInfo.put("2CC_registered", String.valueOf(isInNrcaState(this.mServiceState[i]) && !zIsFiveGARegistration));
            }
            commonRadioParamInfo.put("sceneType", getSceneType());
            commonRadioParamInfo.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
            updateRoamingParams(i, this.mServiceState[i], commonRadioParamInfo);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Rlog.d("MiuiTelephonyStatistic", "doFakeDataConnectionEvent invalid paramerters!!!");
                return;
            }
            Utils.logI("MiuiTelephonyStatistic", "MOD-TF-DATA - Readyfor doFakeDataConnectionEvent  phoneId=" + i);
            updateBlockedNetworkInfo(i);
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).getDataStallInfoFromModem(i, commonRadioParamInfo);
            Message messageObtainMessage = this.mStatisticHandler.obtainMessage(95);
            messageObtainMessage.obj = new HashMap(commonRadioParamInfo);
            messageObtainMessage.arg1 = i;
            this.mStatisticHandler.sendMessage(messageObtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFakeDataSnapshot(String str) {
        HashMap map = this.mFakeDataSnapshot;
        if (map == null) {
            return;
        }
        int i = this.mFakeDataSnapshotPhoneId;
        this.mFakeDataSnapshot = null;
        this.mFakeDataSnapshotPhoneId = -1;
        this.mStatisticHandler.removeMessageWithCount(94);
        map.put("FakeDuration", String.valueOf(Math.min((SystemClock.elapsedRealtime() - this.mFakeDataSnapshotSaveTime) / 1000, 60L)));
        map.put("SnapshotTrigger", str);
        Rlog.d("MiuiTelephonyStatistic", "reportFakeDataSnapshot trigger=" + str + " data =" + map);
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_fake_connection", map);
        recordLogInfo(map, "data_fake_connection");
        if (shouldRecordDFSEvent(i)) {
            map.put("UploadLog", String.valueOf(shouldUploadDataFakeLog(map)));
            recordDFSEventStatistic(915000004, map);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveFakeDataSnapshot(HashMap map, int i) {
        if (this.mFakeDataSnapshot == null) {
            this.mFakeDataSnapshot = map;
            this.mFakeDataSnapshotPhoneId = i;
            this.mFakeDataSnapshotSaveTime = SystemClock.elapsedRealtime();
            MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
            miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(94), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            Rlog.d("MiuiTelephonyStatistic", "saveFakeDataSnapshot snapshot saved, waiting for report trigger");
            return;
        }
        Rlog.d("MiuiTelephonyStatistic", "saveFakeDataSnapshot snapshot already exists, skip");
    }

    private String getCameraMtplAdjustState() {
        try {
            return android.os.SystemProperties.get("persist.radio.camera_mtpl", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    private boolean shouldRecordDFSEvent(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return (phone == null || isAirPlaneModeOn() || !MiuiPhoneUtils.isScreenOn(this.mContext) || MiuiPhoneUtils.isWifiConnected() || !phone.isUserDataEnabled()) ? false : true;
    }

    private boolean shouldUploadDataFakeLog(HashMap map) {
        String str;
        String str2;
        if ("0".equals(map.get("dataRegState")) && SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON.equals(map.get("userDataEnable")) && (str = (String) map.get("Network")) != null && str.startsWith("460")) {
            String str3 = (String) map.get("RAT");
            if (("NR_SA".equals(str3) || "LTE".equals(str3)) && (str2 = (String) map.get("foregroundPkg")) != null && !str2.startsWith("com.miui.") && !str2.startsWith("com.android.")) {
                return true;
            }
        }
        return false;
    }

    private boolean isInNrcaState(ServiceState serviceState) {
        return ((getDataNetworkType(serviceState) == 20 || getVoiceNetworkType(serviceState) == 20) && serviceState.isUsingCarrierAggregation()) || getDataNetworkType(serviceState) == 30 || getVoiceNetworkType(serviceState) == 30;
    }

    private int getDataNetworkType(ServiceState serviceState) {
        if (serviceState != null) {
            return serviceState.getDataNetworkType();
        }
        return 0;
    }

    private int getVoiceNetworkType(ServiceState serviceState) {
        if (serviceState != null) {
            return serviceState.getVoiceNetworkType();
        }
        return 0;
    }

    private HashMap getCellParamInfo(Phone phone) {
        String string;
        String string2;
        HashMap map = new HashMap();
        String globalCellId = null;
        CellIdentity currentCellIdentity = phone != null ? phone.getCurrentCellIdentity() : null;
        if (currentCellIdentity != null) {
            if (currentCellIdentity instanceof CellIdentityLte) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
                globalCellId = cellIdentityLte.getGlobalCellId();
                string2 = Integer.toString(cellIdentityLte.getEarfcn());
                string = Integer.toString(cellIdentityLte.getPci());
            } else if (currentCellIdentity instanceof CellIdentityNr) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
                String string3 = Integer.toString(cellIdentityNr.getNrarfcn());
                String string4 = Integer.toString(cellIdentityNr.getPci());
                globalCellId = cellIdentityNr.getGlobalCellId();
                string = string4;
                string2 = string3;
            } else {
                string = null;
                string2 = null;
            }
            if (globalCellId == null) {
                globalCellId = "";
            }
            map.put("Gci", globalCellId);
            if (string == null) {
                string = "";
            }
            map.put("Pci", string);
            if (string2 == null) {
                string2 = "";
            }
            map.put("Arfcn", string2);
        }
        return map;
    }

    public void reportTcpRTTWorse(Map map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "tcp_rtt_worse", addLinkStatusParams(map, ""));
    }

    public void reportTcpLinkWorse(Map map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "tcp_link_worse", addLinkStatusParams(map, ""));
    }

    public Map reportSNANetworkRecoveryBefore(Map map) {
        return addLinkStatusParams(map, "_pre");
    }

    public void reportSNANetworkRecovery(Map map) {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "sna_network_recovery", addLinkStatusParams(map, ""));
    }

    private Map addLinkStatusParams(Map map, String str) {
        String interfaceName;
        String strValueOf;
        int defaultDataSlotId = getDefaultDataSlotId();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        LinkProperties linkProperties = (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 0) ? connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()) : null;
        if (linkProperties != null && !TextUtils.isEmpty(linkProperties.getInterfaceName())) {
            interfaceName = linkProperties.getInterfaceName();
            strValueOf = String.valueOf(linkProperties.getMtu());
        } else {
            interfaceName = "NA";
            strValueOf = "NA";
        }
        HashMap commonRadioParamInfo = getCommonRadioParamInfo(defaultDataSlotId, false);
        commonRadioParamInfo.putAll(map);
        int dataRat = getDataRat(this.mServiceState[defaultDataSlotId]);
        int dbm = getDbm(this.mSignalStrength[defaultDataSlotId]);
        commonRadioParamInfo.put("RAT" + str, ServiceState.rilRadioTechnologyToString(dataRat));
        commonRadioParamInfo.put("rsrp" + str, String.valueOf(dbm));
        commonRadioParamInfo.put("Band" + str, Utils.getBand(defaultDataSlotId, this.mServiceState[defaultDataSlotId]));
        commonRadioParamInfo.put("Interface" + str, interfaceName);
        commonRadioParamInfo.put("Mtu" + str, strValueOf);
        commonRadioParamInfo.put("Trigger" + str, this.mFakeDataTrigger);
        commonRadioParamInfo.putAll(getCellParamInfo(MiuiPhoneUtils.getPhone(defaultDataSlotId)));
        if (this.mSupportFiveG) {
            commonRadioParamInfo.put("NrConfigType" + str, String.valueOf(getNrConfigType(defaultDataSlotId)));
        }
        return commonRadioParamInfo;
    }

    private int getDbm(SignalStrength signalStrength) {
        if (signalStrength != null) {
            return signalStrength.getDbm();
        }
        return Integer.MAX_VALUE;
    }

    private int getSnr(int i) {
        FiveGServiceInfo fiveGServiceInfo;
        SignalStrength signalStrength;
        ServiceState serviceState = this.mServiceState[i];
        int voiceNetworkType = serviceState != null ? serviceState.getVoiceNetworkType() : 0;
        if (Utils.isLte(voiceNetworkType) && (signalStrength = this.mSignalStrength[i]) != null) {
            return signalStrength.getLteRssnr();
        }
        if (!Utils.isNr(voiceNetworkType) || (fiveGServiceInfo = this.mFiveGInfo[i]) == null) {
            return -24;
        }
        return fiveGServiceInfo.getSnr();
    }

    private int getVoiceRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilVoiceRadioTechnology();
    }

    private int getDataRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    private int getDataRegistrationState(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getDataRegState();
    }

    private int getVoiceRegistrationState(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getVoiceRegState();
    }

    private String getSignalQuality(SignalStrength signalStrength, int i) {
        if (signalStrength == null) {
            return "NA";
        }
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 9:
            case 10:
            case 11:
            case 15:
            case 16:
            case 20:
                return String.valueOf(signalStrength.getGsmBitErrorRate());
            case 4:
            case 5:
            case 6:
                return String.valueOf(signalStrength.getCdmaEcio());
            case 7:
            case 8:
            case 12:
            case 13:
                return String.valueOf(signalStrength.getEvdoEcio());
            case 14:
            case 19:
                return signalStrength.getLteRsrq() + "," + signalStrength.getLteRssnr();
            case 17:
            case 18:
            default:
                return "NA";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    private boolean isCallOffhook(Connection connection) {
        return connection.getConnectTime() != 0;
    }

    private String getDnsServers(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(((InetAddress) list.get(i)).getHostAddress());
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private synchronized void recordLogInfo(HashMap map, String str) {
        if (!DeviceUtil.isQcomPlatform()) {
            Utils.logE("MiuiTelephonyStatistic", "recordLogInfo: Dont catch log for non-Qc platform!!!");
            return;
        }
        if (map != null && !TextUtils.isEmpty(str)) {
            Utils.logI("MiuiTelephonyStatistic", "recordLogInfo:key=" + str);
            HashMap map2 = (HashMap) map.clone();
            map2.put("SubBussiness", str);
            int iConvertEventToBitIndex = Utils.convertEventToBitIndex(str);
            if (iConvertEventToBitIndex != -1 && Utils.hasRuleEvent(this.mPreference.getRuleEvent(), iConvertEventToBitIndex)) {
                map2.put("UserPermission", "Agree");
            }
            try {
                MqsasTool.reportTelephonyException(new JSONObject(map2).toString());
            } catch (Exception e) {
                Utils.logE("MiuiTelephonyStatistic", "recordLogInfo:e =" + e);
            }
            return;
        }
        Utils.logE("MiuiTelephonyStatistic", "recordLogInfo:invalid params");
    }

    private static CellLocation getEmptyCellLocation(int i) {
        if (i == 1) {
            return new GsmCellLocation();
        }
        if (i != 2) {
            return null;
        }
        return new CdmaCellLocation();
    }

    private void monitorNoLteNrVoiceRatState(int i, ServiceState serviceState) {
        String networkClassName;
        String networkClassName2 = "";
        if (serviceState == null) {
            networkClassName = "";
        } else {
            networkClassName = TelephonyManagerEx.getDefault().getNetworkClassName(serviceState.getVoiceNetworkType());
        }
        if (this.mServiceState[i] != null) {
            networkClassName2 = TelephonyManagerEx.getDefault().getNetworkClassName(this.mServiceState[i].getVoiceNetworkType());
        }
        Utils.logD("MiuiTelephonyStatistic", "newVoiceReg:" + networkClassName + ",oldVoiceReg:" + networkClassName2);
        boolean roaming = serviceState != null ? serviceState.getRoaming() : false;
        if (isAllowedForTrackNoLteNr(i, serviceState)) {
            if (isUnknownRat(networkClassName)) {
                if (this.mIsNoLteNrTimerStarted[i]) {
                    Utils.logD("MiuiTelephonyStatistic", "Unknown reg state, and NoLteNr timer has been started");
                    return;
                }
            } else if (roaming) {
                if (isLowerRat(networkClassName)) {
                    if (!isLowerRat(networkClassName2) && !this.mIsNoLteNrTimerStarted[i]) {
                        startNoLteNrTimer(i);
                    }
                    if (this.mIsNoLteNrTimerStarted[i]) {
                        updateNoLteNrPlmn(serviceState);
                        updateNoLteNrRat(networkClassName);
                        return;
                    }
                    return;
                }
                if (isHigherRat(networkClassName) && !isHigherRat(networkClassName2) && this.mIsNoLteNrTimerStarted[i]) {
                    calcNoLteNrDurationIfNeed(i, serviceState);
                }
            }
        }
        if (this.mIsNoLteNrTimerStarted[i]) {
            stopNoLteNrTimer(i);
            initNoLteNrValue(i);
        }
    }

    private boolean isUnknownRat(String str) {
        return TextUtils.isEmpty(str) || TextUtils.equals(str, "UNKNOWN");
    }

    private boolean isHigherRat(String str) {
        return TextUtils.equals(str, "4G") || TextUtils.equals(str, "5G");
    }

    private boolean isLowerRat(String str) {
        return TextUtils.equals(str, "2G") || TextUtils.equals(str, "3G");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initNoLteNrValue(int i) {
        this.mNoLteNrDuration[i] = -1;
        this.mNoLteNrVplmn = "";
        this.mNoLteNrRat = "";
        this.mIsNoLteNrTimerStarted[i] = false;
    }

    private boolean isAllowedForTrackNoLteNr(int i, ServiceState serviceState) {
        return serviceState != null && isPhoneStateIdle(i) && MiuiPhoneUtils.isLteNrNwMode(i) && !serviceState.getIsManualSelection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNoLteNrStatEvent(int i, ServiceState serviceState) {
        Utils.logD("MiuiTelephonyStatistic", "processNoLteNrStatEvent phoneId=" + i);
        if (serviceState == null || !serviceState.getRoaming()) {
            return;
        }
        long[] jArr = this.mNoLteNrCounter;
        jArr[i] = jArr[i] + 1;
        if (this.mNoLteNrDuration[i] != -1 || this.mNoLteNrDurations.size() >= 20) {
            return;
        }
        this.mNoLteNrDuration[i] = SystemClock.elapsedRealtime();
        removeStatisticMessages(87, i);
        sendMaxCalcDurationEvent(87, i, 480000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calcNoLteNrDurationIfNeed(int i, ServiceState serviceState) {
        Utils.logD("MiuiTelephonyStatistic", "calcNoLteNrDurationIfNeed phoneId=" + i);
        removeStatisticMessages(87, i);
        if (serviceState == null || !serviceState.getRoaming() || this.mNoLteNrDuration[i] == -1 || this.mNoLteNrDurations.size() >= 20) {
            return;
        }
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mNoLteNrDuration[i]) / 1000;
        if (jElapsedRealtime >= 480) {
            jElapsedRealtime = 480;
        }
        String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        String operatorNumeric = serviceState.getOperatorNumeric();
        HashMap map = new HashMap();
        map.put("hplmn", simOperatorForSlot);
        map.put("nowVplmn", operatorNumeric);
        map.put("nowRat", TelephonyManagerEx.getDefault().getNetworkClassName(serviceState.getVoiceNetworkType()));
        map.put("lastVplmn", this.mNoLteNrVplmn);
        map.put("lastRat", this.mNoLteNrRat);
        map.put("Value", Long.valueOf(jElapsedRealtime));
        map.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
        this.mNoLteNrDurations.add(map);
    }

    private void updateNoLteNrPlmn(ServiceState serviceState) {
        String operatorNumeric;
        if (serviceState == null || (operatorNumeric = serviceState.getOperatorNumeric()) == null || operatorNumeric.equals(this.mNoLteNrVplmn)) {
            return;
        }
        this.mNoLteNrVplmn = operatorNumeric;
    }

    private void updateNoLteNrRat(String str) {
        if (str.equals(this.mNoLteNrRat)) {
            return;
        }
        this.mNoLteNrRat = str;
    }

    private void stopNoLteNrTimer(int i) {
        removeStatisticMessages(86, i);
        removeStatisticMessages(87, i);
    }

    private void removeStatisticMessages(int i, int i2) {
        if (this.mStatisticHandler.hasMessages(i, Integer.valueOf(i2))) {
            Utils.logD("MiuiTelephonyStatistic", "remove static message: " + i + ", phoneId" + i2);
            this.mStatisticHandler.removeMessageWithCount(i, Integer.valueOf(i2));
        }
    }

    private void startNoLteNrTimer(int i) {
        this.mIsNoLteNrTimerStarted[i] = true;
        Utils.logD("MiuiTelephonyStatistic", "startNoLteNrTimer phoneId=" + i);
        stopNoLteNrTimer(i);
        sendMaxCalcDurationEvent(86, i, 30000);
    }

    private void monitorVoiceNwChange(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            Utils.logE("MiuiTelephonyStatistic", "monitorVoiceNwChange invalid params!");
            return;
        }
        int voiceRegState = serviceState.getVoiceRegState();
        int voiceRegState2 = this.mServiceState[i].getVoiceRegState();
        Utils.logD("MiuiTelephonyStatistic", "newVoiceReg:" + voiceRegState + ",oldVoiceReg=" + voiceRegState2);
        boolean z = false;
        boolean z2 = voiceRegState2 != 0 && voiceRegState == 0;
        if (voiceRegState2 == 0 && voiceRegState != 0) {
            z = true;
        }
        if (z2) {
            stopNwOosTimer();
            calcNwOosDurationIfNeed(i, serviceState);
            recordNwOosNoLimitDurationIfNeed(i, serviceState);
        } else {
            if (!z || isNormalCaseForVoiceDetach(i)) {
                return;
            }
            this.mOosBand = Utils.getBand(i, this.mServiceState[i]);
            this.mOosRat = ServiceState.rilRadioTechnologyToString(getVoiceRat(this.mServiceState[i]));
            this.mOosRsrp = getDbm(this.mSignalStrength[i]);
            this.mOosSnr = getSnr(i);
            startNwOosTimer(i);
        }
    }

    private void monitorNwChange(int i, ServiceState serviceState) {
        ServiceState serviceState2 = this.mServiceState[i];
        if (serviceState2 == null || serviceState == null) {
            Utils.logE("MiuiTelephonyStatistic", "monitorNwChange invalid params!");
            return;
        }
        getCombinedServiceState(serviceState2);
        getCombinedServiceState(serviceState);
        if (isInService(serviceState) || !isInService(serviceState2) || isNormalCaseForVoiceDetach(i) || this.mIsshutdown) {
            return;
        }
        if (!isPhoneStateIdle()) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(120, Integer.valueOf(i));
            return;
        }
        if (MiuiPhoneUtils.isScreenOn(this.mContext)) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(120, Integer.valueOf(i));
        } else {
            if (MiuiPhoneUtils.isMPDevice() || MiuiPhoneUtils.isWifiConnected()) {
                return;
            }
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(120, Integer.valueOf(i));
        }
    }

    private boolean isInService(ServiceState serviceState) {
        int combinedServiceState;
        return (serviceState == null || (combinedServiceState = getCombinedServiceState(serviceState)) == 3 || combinedServiceState == 1 || combinedServiceState == 2) ? false : true;
    }

    private int getCombinedServiceState(ServiceState serviceState) {
        if (serviceState == null) {
            return 1;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 2) && dataRegistrationState == 0 && (isNotInIwlan(serviceState) || isInPsWwan(serviceState))) {
            return 0;
        }
        return state;
    }

    private boolean isRegPsDomain(ServiceState serviceState, int i) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo == null) {
            return false;
        }
        return networkRegistrationInfo.getRegistrationState() == 1 || networkRegistrationInfo.getRegistrationState() == 5;
    }

    private boolean isInPsWwan(ServiceState serviceState) {
        return isRegPsDomain(serviceState, 1);
    }

    private boolean isNotInIwlan(ServiceState serviceState) {
        return !isRegPsDomain(serviceState, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void calcNwOosDurationIfNeed(int i, ServiceState serviceState) {
        try {
            this.mStatisticHandler.removeMessageWithCount(60);
            if (this.mNwOosDuration[i] != -1) {
                long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mNwOosDuration[i]) / 1000;
                if (jElapsedRealtime >= 60) {
                    jElapsedRealtime = 60;
                }
                String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
                int voiceRat = getVoiceRat(serviceState);
                int dbm = getDbm(this.mSignalStrength[i]);
                HashMap map = new HashMap();
                map.put("Network", availableOperatorNumeric);
                map.put("Value", Long.valueOf(jElapsedRealtime));
                map.put("RAT", ServiceState.rilRadioTechnologyToString(voiceRat));
                map.put("rsrp", String.valueOf(dbm));
                map.put("Band", Utils.getBand(i, this.mServiceState[i]));
                map.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
                addRoamingParams(i, serviceState, map);
                map.put("simType", String.valueOf(Utils.getCustomSimTypeForStatistic(i)));
                this.mNwOosDurations.add(map);
                this.mNwOosDuration[i] = -1;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void recordNwOosNoLimitDurationIfNeed(int i, ServiceState serviceState) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mNwOosNoLimitOccurrenceTime[i];
        long j2 = (jElapsedRealtime - j) / 1000;
        if (j2 >= 1200) {
            j2 = 1200;
        }
        if (j != -1) {
            if (j2 <= 15) {
                this.mNwShortDurationOosCount++;
                return;
            }
            String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
            getVoiceRat(this.mServiceState[i]);
            getDbm(this.mSignalStrength[i]);
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, true);
            if (commonRadioParamInfo == null) {
                return;
            }
            commonRadioParamInfo.put("Network", availableOperatorNumeric);
            commonRadioParamInfo.put("Value", String.valueOf(j2));
            commonRadioParamInfo.put("RAT", this.mOosRat);
            commonRadioParamInfo.put("rsrp", String.valueOf(this.mOosRsrp));
            commonRadioParamInfo.put("Band", this.mOosBand);
            commonRadioParamInfo.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "recordNwOosNoLimitDuration invalid paramerters!!!");
                return;
            }
            Utils.logE("MiuiTelephonyStatistic", "recordNwOosNoLimitDuration data=" + commonRadioParamInfo);
            Utils.logE("MiuiTelephonyStatistic", "MOD-TF-NT - OOS exception happened .");
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_nw_out_of_service_no_limit_duration", commonRadioParamInfo);
            this.mNwOosNoLimitOccurrenceTime[i] = -1;
        }
    }

    private void startNwOosTimer(int i) {
        Utils.logD("MiuiTelephonyStatistic", "startNwOosTimer phoneId=" + i);
        stopNwOosTimer();
        MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
        miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(12, Integer.valueOf(i)), 30000L);
        if (MiuiPhoneUtils.isModemLogUploadSupported()) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).startCaptureModemLog("oos", "modem_oos_nas");
        }
        processNwOosStatNoLimit(i);
    }

    private void stopNwOosTimer() {
        if (this.mStatisticHandler.hasMessages(12)) {
            Utils.logD("MiuiTelephonyStatistic", "stopNwOosTimer");
            this.mStatisticHandler.removeMessageWithCount(12);
        }
    }

    private boolean isNormalCaseForVoiceDetach(int i) {
        return this.mIsAirPlaneMode || !isSimReady(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSimReady(int i) {
        return TelephonyManager.getDefault().getSimState(i) == 5;
    }

    private void initNwOosValue(int i) {
        this.mNwOosCount[i] = 0;
        this.mNwOosStartTime[i] = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNwOosStatEvent(int i) {
        Utils.logD("MiuiTelephonyStatistic", "processNwOosStatEvent phoneId=" + i);
        if (isNormalCaseForVoiceDetach(i)) {
            return;
        }
        startNwOosDurationTimer(i);
        boolean zIsAllowedForTrackNwOos = isAllowedForTrackNwOos(i);
        if (zIsAllowedForTrackNwOos) {
            initNwOosValue(i);
        }
        recordNwOosEvent(i, zIsAllowedForTrackNwOos);
    }

    private void processNwOosStatNoLimit(int i) {
        if (isNormalCaseForVoiceDetach(i)) {
            return;
        }
        this.mNwOosNoLimitOccurrenceTime[i] = SystemClock.elapsedRealtime();
    }

    private void startNwOosDurationTimer(int i) {
        if (this.mNwOosDuration[i] != -1 || this.mNwOosDurations.size() >= 20) {
            return;
        }
        this.mNwOosDuration[i] = SystemClock.elapsedRealtime();
        sendMaxCalcDurationEvent(60, i);
    }

    private void sendMaxCalcDurationEvent(int i, int i2) {
        Message messageObtainMessage = this.mStatisticHandler.obtainMessage();
        messageObtainMessage.what = i;
        messageObtainMessage.arg1 = i2;
        this.mStatisticHandler.sendMessageDelayed(messageObtainMessage, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    private void sendMaxCalcDurationEvent(int i, int i2, int i3) {
        Message messageObtainMessage = this.mStatisticHandler.obtainMessage();
        messageObtainMessage.what = i;
        messageObtainMessage.obj = Integer.valueOf(i2);
        this.mStatisticHandler.sendMessageDelayed(messageObtainMessage, i3);
    }

    private boolean isAllowedForTrackNwOos(int i) {
        Utils.logD("MiuiTelephonyStatistic", "isTrackNwOosAllowed  phoneId=" + i);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mNwOosStartTime[i];
        if (j != -1 && jElapsedRealtime - j > v.b) {
            initNwOosValue(i);
        }
        int[] iArr = this.mNwOosCount;
        int i2 = iArr[i];
        if (i2 == 0) {
            this.mNwOosStartTime[i] = jElapsedRealtime;
        }
        iArr[i] = i2 + 1;
        Utils.logD("MiuiTelephonyStatistic", "isAllowedForTrackNwOos cn=" + this.mNwOosCount[i]);
        return ((long) this.mNwOosCount[i]) >= 5;
    }

    private void recordNwOosEvent(int i, boolean z) {
        if (isValidatePhoneId(i)) {
            HashMap commonRadioParamInfo = getCommonRadioParamInfo(i, true);
            if (commonRadioParamInfo == null) {
                return;
            }
            getVoiceRat(this.mServiceState[i]);
            getDbm(this.mSignalStrength[i]);
            String str = MiuiPhoneUtils.isScreenOn(this.mContext) ? "On" : "Off";
            commonRadioParamInfo.put("RAT", this.mOosRat);
            commonRadioParamInfo.put("rsrp", String.valueOf(this.mOosRsrp));
            commonRadioParamInfo.put("Band", this.mOosBand);
            commonRadioParamInfo.put("isDds", i == this.mLastDdsPhoneId ? "1" : "0");
            commonRadioParamInfo.put("ScreenStatus", str);
            commonRadioParamInfo.put("sleepMode", String.valueOf(this.mIsSleepMode));
            commonRadioParamInfo.put("sceneType", getSceneType());
            commonRadioParamInfo.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
            Utils.logD("MiuiTelephonyStatistic", "recordNwOosEvent data=" + commonRadioParamInfo);
            if (!Utils.isValidParams(commonRadioParamInfo)) {
                Utils.logE("MiuiTelephonyStatistic", "recordNwOosEvent invalid paramerters!!!");
                return;
            }
            Utils.logI("MiuiTelephonyStatistic", "Readyfor recordNwOosEvent phoneId=" + i);
            if (z) {
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nw_out_of_service", commonRadioParamInfo);
                recordLogInfo(commonRadioParamInfo, "nw_out_of_service");
            }
            commonRadioParamInfo.put("UploadLog", String.valueOf(this.mOosRsrp >= -110 && this.mOosSnr >= 0));
            recordDFSEventStatistic(915100012, commonRadioParamInfo);
        }
    }

    private int getPreferredNwModeFromDb(int i) {
        return ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
    }

    private boolean isPhoneStateIdle() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                Utils.logD("MiuiTelephonyStatistic", "isPhoneStateIdle: Voice call active on phone: " + i);
                return false;
            }
        }
        return true;
    }

    private boolean isPhoneStateIdle(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || phone.getState() == PhoneConstants.State.IDLE) {
            return true;
        }
        Utils.logD("MiuiTelephonyStatistic", "isPhoneStateIdle: Voice call active on phone: " + i);
        return false;
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private void showModemDialog(int i) {
        Utils.logD("MiuiTelephonyStatistic", "showModemDialog: Enter");
        Dialog dialog = this.mUserPermissionDialog;
        if ((dialog == null || !dialog.isShowing()) && this.mContext != null) {
            ModemLogDialog modemLogDialog = new ModemLogDialog(this.mContext, this.MdlogCallBack, i);
            this.mUserPermissionDialog = modemLogDialog;
            modemLogDialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPreciseCallStateEvent(int i, int i2) {
        if (!isValidatePhoneId(i) || i2 == this.mForegroundCallState[i]) {
            return;
        }
        Utils.logD("MiuiTelephonyStatistic", "processPreciseCallStateEvent phoneId=" + i + ",fc=" + i2 + ",oldfcstate=" + this.mForegroundCallState[i]);
        if (i2 == 0 || 1 == i2) {
            this.mForegroundCallState[i] = i2;
            if (1 == i2 && Utils.hasRuleEvent(this.mPreference.getRuleEvent(), 1)) {
                HashMap commonParamInfo = getCommonParamInfo();
                commonParamInfo.put("CallState", "CallOffhook");
                Utils.logI("MiuiTelephonyStatistic", "Readyfor Offhook Call");
                recordLogInfo(commonParamInfo, "call_unexpected_disconnected");
            }
        }
        if (5 == i2 && i != this.mLastDdsPhoneId && Utils.getDsdaUsageScene(i) != -1 && com.android.phone.utils.Utils.isGameMode(this.mContext)) {
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nondds_incoming_in_gamemode");
        }
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && TelephonyManagerEx.getDefault().isPlatformMtk4G()) {
            if (i2 == 5 || i2 == 3) {
                try {
                    ATCommandUtils.getInstance().atCmdRunSync(0, new String[]{"AT+EDMFAPP=20100,700", "+EDMFAPP"}, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processUserUploadMdlogEvent(int i) {
        try {
            Utils.logD("MiuiTelephonyStatistic", "processUserUploadMdlogEvent id=" + i);
            if (i == -1) {
                return;
            }
            int ruleEvent = this.mPreference.getRuleEvent();
            Utils.logD("MiuiTelephonyStatistic", "processUserUploadMdlogEvent ruleEvent=" + ruleEvent);
            if (!Utils.isPermanentUserDisagree(ruleEvent) && !Utils.hasRuleEvent(ruleEvent, i)) {
                if (!Utils.isAnyValidRuleEventExist(ruleEvent)) {
                    showModemDialog(i);
                } else {
                    this.mPreference.setRuleEvent(i, true);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMdlogReportAgree(int i) {
        Utils.logD("MiuiTelephonyStatistic", "processMdlogReportAgree id=" + i);
        this.mPreference.setRuleEvent(i, true);
        this.mPreference.setLastUserMdlogTime(Long.valueOf(System.currentTimeMillis()));
        this.mPreference.setUserDisagreeCounter(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMdlogReportDisagree() {
        int userDisagreeCounter = this.mPreference.getUserDisagreeCounter() + 1;
        if (userDisagreeCounter >= 3) {
            this.mPreference.setRuleEvent(0, true);
        }
        this.mPreference.setUserDisagreeCounter(userDisagreeCounter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDebugAll(boolean z) {
        Utils.logW("MiuiTelephonyStatistic", "processDebugAll :" + z);
        Utils.setDebugAll(z);
        MiStatInterfaceUtil.enableOneTrackDebugMode(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:14:0x0061  */
    public void processOnIccChanged(int i) {
        String str;
        String str2;
        String cardState = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCardState(i);
        StringBuilder sb = new StringBuilder("processOnIccChanged phoneId=");
        sb.append(i);
        sb.append("state is ");
        sb.append((Object) (cardState == null ? "NULL" : cardState));
        Utils.logD("MiuiTelephonyStatistic", sb.toString());
        if (cardState != null) {
            if (this.mIsHotplug) {
                str = this.mCardState[i];
                str2 = IccCardStatus.CardState.CARDSTATE_ABSENT;
                if (str != str2) {
                    Utils.logD("MiuiTelephonyStatistic", "processOnIccChanged sim remove");
                    this.mIsHotplug = false;
                    if (i == this.mLastDdsPhoneId) {
                        reportFakeDataSnapshot("sim_removed");
                    }
                }
            } else {
                String str3 = this.mCardState[i];
                String str4 = IccCardStatus.CardState.CARDSTATE_ABSENT;
                if (str3 == str4 && cardState != str4) {
                    this.mIsHotplug = true;
                    int userHotPlugCounter = this.mPreference.getUserHotPlugCounter() + 1;
                    this.mPreference.setUserHotPlugCounter(userHotPlugCounter);
                    Utils.logD("MiuiTelephonyStatistic", "processOnIccChanged sim insert hotplugCn=" + userHotPlugCounter);
                } else {
                    str = this.mCardState[i];
                    str2 = IccCardStatus.CardState.CARDSTATE_ABSENT;
                    if (str != str2 && cardState == str2) {
                        Utils.logD("MiuiTelephonyStatistic", "processOnIccChanged sim remove");
                        this.mIsHotplug = false;
                        if (i == this.mLastDdsPhoneId && this.mIsDcBlocked) {
                            reportFakeDataSnapshot("sim_removed");
                        }
                    }
                }
            }
            this.mCardState[i] = cardState;
        }
    }

    private static boolean isStatisEnabled(Context context) {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(context.getContentResolver(), "TelephonyStatisticsBlackList");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    Utils.logD("MiuiTelephonyStatistic", "isStatisEnabled data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("VersionType", "NA");
                    String versionType = DeviceUtil.getVersionType();
                    String str = android.os.Build.DEVICE;
                    Utils.logD("MiuiTelephonyStatistic", "isStatisEnabled cloudModel=" + string + ",cloudVerType=" + string2);
                    if ("All".equals(string) || str.equalsIgnoreCase(string)) {
                        if ("All".equals(string2) || versionType.equalsIgnoreCase(string2)) {
                            return false;
                        }
                    }
                }
                return true;
            }
            Utils.logW("MiuiTelephonyStatistic", "isStatisEnabled blackList is empty");
            return true;
        } catch (Exception e) {
            Utils.logW("MiuiTelephonyStatistic", "isStatisEnabled exception" + e);
            return true;
        }
    }

    public static synchronized void resetByCause(Context context, int i) {
        boolean zIsStatisEnabled;
        try {
            if (i == 0) {
                zIsStatisEnabled = Utils.isUserExperienceProgramEnabled(context);
            } else if (1 == i) {
                zIsStatisEnabled = isStatisEnabled(context);
            } else {
                Utils.logW("MiuiTelephonyStatistic", "reset  return by invalid cause!!!");
                return;
            }
            if (zIsStatisEnabled && getInstance() == null && shouldStatistic()) {
                Utils.logW("MiuiTelephonyStatistic", "reset -make by cause:" + i);
                make();
            } else if (!zIsStatisEnabled && getInstance() != null && !shouldStatistic()) {
                Utils.logW("MiuiTelephonyStatistic", "reset -dispose by cause:" + i);
                getInstance().dispose();
            } else {
                Utils.logW("MiuiTelephonyStatistic", "reset others- isEnabled=" + zIsStatisEnabled + ",cause:" + i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerBlockedNwRecoveryActionObserver() {
        Utils.logD("MiuiTelephonyStatistic", "registerBlockedNwRecoveryActionObserver Enter");
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("radio.data.stall.recovery.action"), true, this.mDataRecoveryDBObserver);
    }

    private void unRegisterBlockedNwRecoveryActionObserver() {
        if (this.mDataRecoveryDBObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDataRecoveryDBObserver);
        }
    }

    private void registerSatelliteObserver() {
        Utils.logD("MiuiTelephonyStatistic", "SatelliteObserver Enter");
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("satellite_state"), false, this.mSatelliteObserver);
    }

    private void unRegisterSatelliteObserver() {
        if (this.mSatelliteObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mSatelliteObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSatelliteEnable() {
        boolean z = Settings.System.getInt(this.mContext.getContentResolver(), "satellite_state", 0) == 1;
        Utils.logD("MiuiTelephonyStatistic", "isSatelliteEnabled = " + z);
        return z;
    }

    private synchronized boolean isTurnOffSatelliteRecently() {
        return (DBG || this.mLastSatelliteDisableTime == -1 || SystemClock.elapsedRealtime() - this.mLastSatelliteDisableTime > 180000) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNormalCaseForUseCelluar() {
        return isValidatePhoneId(this.mLastDdsPhoneId) && MiuiPhoneUtils.isDataRegistered(this.mLastDdsPhoneId) && !MiuiPhoneUtils.isWifiConnected() && !MiuiPhoneUtils.isDuringCall() && !this.mIsAirPlaneMode && MiuiPhoneUtils.isMobileDataEnabled(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDataNetworkFailBySatellite() {
        HashMap commonRadioParamInfo = getCommonRadioParamInfo(this.mLastDdsPhoneId, false);
        if (commonRadioParamInfo == null) {
            return;
        }
        Utils.logD("MiuiTelephonyStatistic", "recordDataNetworkFailBySatellite data= " + commonRadioParamInfo);
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_data_network_fail_by_satellite", commonRadioParamInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processBlockedNetworkDuration(int i, int i2) {
        Utils.logD("MiuiTelephonyStatistic", "processBlockedNetworkDuration dataState=" + i2 + ",mIsDcBlocked=" + this.mIsDcBlocked);
        if (isValidatePhoneId(i) && i2 != 2 && this.mIsDcBlocked) {
            recordFakeDataConnectionTime(3);
        }
    }

    private void updateBlockedNetworkInfo(int i) {
        this.mIsDcBlocked = true;
        this.mDcBlockedStartTime = SystemClock.elapsedRealtime();
    }

    private void clearBlockedNetworkDuration() {
        this.mIsDcBlocked = false;
        this.mDcBlockedStartTime = 0L;
    }

    private void recordSignalStrengthBinDuration() {
        HashMap commonParamInfo = getCommonParamInfo();
        commonParamInfo.put("MaxSignalBin", String.valueOf(MiuiSignalStrengthUtils.MIUI_NUM_SIGNAL_STRENGTH_BINS));
        commonParamInfo.put("DdsOosDuration", String.valueOf(this.mDdsOosDuration));
        commonParamInfo.put("NonDdsOosDuration", String.valueOf(this.mNonDdsOosDuration));
        Utils.logD("MiuiTelephonyStatistic", "recordSignalStrengthBinDuration data=" + commonParamInfo);
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "signal_strength_bin_duration", commonParamInfo);
    }

    private void clearSignalStrengthBinDuration() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            long[] jArr = this.mLastOosTime;
            if (jArr[i] != -1) {
                jArr[i] = SystemClock.elapsedRealtime();
            }
        }
        this.mDdsOosDuration = 0L;
        this.mNonDdsOosDuration = 0L;
        try {
            DefaultPreferenceHelper.getInstance().setDdsSsBinDuration(String.valueOf(0));
            DefaultPreferenceHelper.getInstance().setNonDdsSsBinDuration(String.valueOf(0));
            DefaultPreferenceHelper.getInstance().setBlockNetworkDuration(String.valueOf(0));
            DefaultPreferenceHelper.getInstance().setDdsOosDuration(this.mDdsOosDuration);
            DefaultPreferenceHelper.getInstance().setNonDdsOosDuration(this.mNonDdsOosDuration);
        } catch (Exception e) {
            Utils.logD("MiuiTelephonyStatistic", "save blocked network duration exception:" + e);
        }
    }

    private void reloadDualDataActivateStatistcFromSP() {
        DefaultPreferenceHelper defaultPreferenceHelper = DefaultPreferenceHelper.getInstance();
        try {
            this.mDualDataActivateCount = defaultPreferenceHelper.getDualDataActivateCount();
            String dualDataActivateDurations = defaultPreferenceHelper.getDualDataActivateDurations();
            if (!dualDataActivateDurations.isEmpty()) {
                this.mDualDataActivateDurations = (ArrayList) ObjectSerializer.deserialize(dualDataActivateDurations);
                Utils.logD("MiuiTelephonyStatistic", "reloadDualDataActivateStatistcFromSP mDualDataActivateDurations=" + this.mDualDataActivateDurations);
            }
            String dualDataActivatePkgNames = defaultPreferenceHelper.getDualDataActivatePkgNames();
            if (dualDataActivatePkgNames.isEmpty()) {
                return;
            }
            this.mDualDataActivatePkgNames = (ArrayList) ObjectSerializer.deserialize(dualDataActivatePkgNames);
            Utils.logD("MiuiTelephonyStatistic", "reloadDualDataActivateStatistcFromSP mDualDataActivatePkgNames=" + this.mDualDataActivatePkgNames);
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "reloadDualDataActivateStatistcFromSP exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void calcDualDataStatistcIfNeed() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i = android.os.SystemProperties.getInt("ril.multisim.dualdata_status", 0);
        int i2 = this.mLastDualDataStatus;
        if (i2 != i) {
            if (i == 1) {
                this.mDualDataActivateCount++;
                this.mLastDualDataActivateTime = jElapsedRealtime;
                Utils.logD("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed mDualDataActivateCount=" + this.mDualDataActivateCount);
                try {
                    DefaultPreferenceHelper.getInstance().setDualDataActivateCount(this.mDualDataActivateCount);
                } catch (Exception e) {
                    Utils.logE("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed exception:" + e);
                }
                ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
                if (foregroundInfo != null) {
                    this.mDualDataActivatePkgNames.add(foregroundInfo.mForegroundPackageName);
                    if (DBG) {
                        Utils.logD("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed mDualDataActivatePkgNames=" + this.mDualDataActivatePkgNames);
                    }
                    try {
                        DefaultPreferenceHelper.getInstance().setDualDataActivatePkgNames(ObjectSerializer.serialize((Serializable) this.mDualDataActivatePkgNames));
                    } catch (Exception e2) {
                        Utils.logE("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed exception:" + e2);
                    }
                }
                this.mLastDualDataStatus = i;
            } else {
                if (i == 2 && i2 == 1) {
                    this.mDualDataActivateDurations.add(Long.valueOf((jElapsedRealtime - this.mLastDualDataActivateTime) / 1000));
                    Utils.logD("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed mDualDataActivateDurations=" + this.mDualDataActivateDurations);
                    try {
                        DefaultPreferenceHelper.getInstance().setDualDataActivateDurations(ObjectSerializer.serialize((Serializable) this.mDualDataActivateDurations));
                    } catch (Exception e3) {
                        Utils.logE("MiuiTelephonyStatistic", "calcDualDataStatistcIfNeed exception:" + e3);
                    }
                }
                this.mLastDualDataStatus = i;
            }
        }
    }

    private void clearDualDataActiveStats() {
        this.mLastDualDataActivateTime = -1L;
        this.mDualDataActivateCount = 0;
        this.mDualDataActivateDurations.clear();
        this.mDualDataActivatePkgNames.clear();
        try {
            DefaultPreferenceHelper.getInstance().setDualDataActivateCount(this.mDualDataActivateCount);
            DefaultPreferenceHelper.getInstance().setDualDataActivateDurations(ObjectSerializer.serialize((Serializable) this.mDualDataActivateDurations));
            DefaultPreferenceHelper.getInstance().setDualDataActivatePkgNames(ObjectSerializer.serialize((Serializable) this.mDualDataActivatePkgNames));
        } catch (Exception e) {
            Utils.logD("MiuiTelephonyStatistic", "save dual data active stats exception:" + e);
        }
    }

    public String getSimState(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getIccCard() != null) {
            return phone.getIccCard().getState().name();
        }
        return "NA";
    }

    private void updateSimStateWithNwState(int i, ServiceState serviceState) {
        String str = getSimState(i) + "-" + serviceState.getState();
        if (str.equals(this.mLastSimStateWithNetworkState[i])) {
            return;
        }
        updateOutOfServiceDuration(i, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOutOfServiceDuration(int i, String str) {
        if (isValidatePhoneId(i) && !TextUtils.isEmpty(str)) {
            String[] strArrSplit = this.mLastSimStateWithNetworkState[i].split("-");
            if (strArrSplit.length != 2) {
                Utils.logD("MiuiTelephonyStatistic", "simWithNwState.length != 2");
                return;
            }
            String str2 = strArrSplit[0];
            String str3 = strArrSplit[1];
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (this.mLastOosTime[i] != -1 && "READY".equals(str2) && isRealOutOfService(str3)) {
                long j = jElapsedRealtime - this.mLastOosTime[i];
                Utils.logD("MiuiTelephonyStatistic", "updateOutOfServiceDuration duration=" + j);
                if (i == this.mLastDdsPhoneId) {
                    this.mDdsOosDuration += j;
                    Utils.logD("MiuiTelephonyStatistic", "updateOutOfServiceDuration mDdsOosDuration=" + this.mDdsOosDuration);
                    try {
                        DefaultPreferenceHelper.getInstance().setDdsOosDuration(this.mDdsOosDuration);
                    } catch (Exception e) {
                        Utils.logD("MiuiTelephonyStatistic", "updateOutOfServiceDuration ddsOosDuration exception:" + e);
                    }
                } else {
                    this.mNonDdsOosDuration += j;
                    Utils.logD("MiuiTelephonyStatistic", "updateOutOfServiceDuration mNonDdsOosDuration=" + this.mNonDdsOosDuration);
                    try {
                        DefaultPreferenceHelper.getInstance().setNonDdsOosDuration(this.mNonDdsOosDuration);
                    } catch (Exception e2) {
                        Utils.logD("MiuiTelephonyStatistic", "updateOutOfServiceDuration NonDdsOosDuration exception:" + e2);
                    }
                }
            }
            this.mLastOosTime[i] = jElapsedRealtime;
            this.mLastSimStateWithNetworkState[i] = str;
        }
    }

    private boolean isRealOutOfService(String str) {
        return "1".equals(str) || "2".equals(str);
    }

    private void reloadSignalStrengthBinDurationFromSP() {
        DefaultPreferenceHelper defaultPreferenceHelper = DefaultPreferenceHelper.getInstance();
        try {
            this.mDdsOosDuration = defaultPreferenceHelper.getDdsOosDuration();
            this.mNonDdsOosDuration = defaultPreferenceHelper.getNonDdsOosDuration();
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "reloadSignalStrengthBinDurationFromSP exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordFakeDataConnectionTime(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mDcBlockedStartTime;
        String str = MiuiPhoneUtils.isScreenOn(this.mContext) ? "On" : "Off";
        HashMap commonRadioParamInfo = getCommonRadioParamInfo(this.mLastDdsPhoneId, false);
        if (commonRadioParamInfo == null) {
            return;
        }
        int dataRat = getDataRat(this.mServiceState[this.mLastDdsPhoneId]);
        int dbm = getDbm(this.mSignalStrength[this.mLastDdsPhoneId]);
        commonRadioParamInfo.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
        commonRadioParamInfo.put("rsrp", String.valueOf(dbm));
        int i2 = this.mLastDdsPhoneId;
        commonRadioParamInfo.put("Band", Utils.getBand(i2, this.mServiceState[i2]));
        commonRadioParamInfo.put("RecoveryStage", String.valueOf(this.mRecoveryAction));
        commonRadioParamInfo.put("FakeDuration", String.valueOf(jElapsedRealtime));
        commonRadioParamInfo.put("ScreenStatus", str);
        commonRadioParamInfo.put("RecoveryMethod", String.valueOf(i));
        commonRadioParamInfo.put("sceneType", getSceneType());
        int i3 = this.mLastDdsPhoneId;
        updateRoamingParams(i3, this.mServiceState[i3], commonRadioParamInfo);
        Utils.logW("MiuiTelephonyStatistic", "recordFakeDataConnectionTime data =" + commonRadioParamInfo);
        if (!Utils.isValidParams(commonRadioParamInfo)) {
            Utils.logE("MiuiTelephonyStatistic", "recordFakeDataConnectionTime invalid paramerters!!!");
            return;
        }
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_fake_connection_time", commonRadioParamInfo);
        this.mRecoveryAction = -1;
        clearBlockedNetworkDuration();
        recordDFSEventStatistic(915000005, commonRadioParamInfo);
    }

    private String getIMEI() {
        List imeiList = miui.telephony.TelephonyManager.getDefault().getImeiList();
        if (imeiList != null && !imeiList.isEmpty()) {
            for (int i = 0; i < imeiList.size(); i++) {
                if (!TextUtils.isEmpty((CharSequence) imeiList.get(i))) {
                    return (String) imeiList.get(i);
                }
            }
            return "";
        }
        return "";
    }

    private String getIMEIMD5() {
        if (DeviceUtil.IS_RESTRICT_IMEI) {
            if (TextUtils.isEmpty(this.mIMEI)) {
                this.mIMEI = IdentifierManager.getOAID(this.mContext);
            }
            return TextUtils.isEmpty(this.mIMEI) ? "" : this.mIMEI;
        }
        if (!TextUtils.isEmpty(this.mIMEIMD5)) {
            return this.mIMEIMD5;
        }
        try {
            if (TextUtils.isEmpty(this.mIMEI)) {
                this.mIMEI = getIMEI();
            }
            if (TextUtils.isEmpty(this.mIMEI)) {
                this.mIMEI = miui.telephony.TelephonyManager.getDefault().getDeviceId();
            }
            if (TextUtils.isEmpty(this.mIMEI)) {
                return "";
            }
            String md5Digest = DigestUtils.getMd5Digest(this.mIMEI);
            this.mIMEIMD5 = md5Digest;
            return md5Digest;
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "getIMEIMD5 e=" + e);
            return "";
        }
    }

    private void onSceneStatsInfoChanged() {
        if (DeviceUtil.isModemSceneStatEnabled()) {
            boolean z = this.mIsLLMEnabled;
            if (z || this.mRecordRsrpAfterGameEnd) {
                updateSceneSignalInfo(1, z, false);
            }
            boolean z2 = this.mIsWechatVoip;
            if (z2) {
                updateSceneSignalInfo(2, z2, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGameStatusChanged() {
        if (DeviceUtil.isModemSceneStatEnabled()) {
            Utils.logD("MiuiTelephonyStatistic", "onGameStatusChanged status = " + this.mIsLLMEnabled);
            this.mStatisticHandler.removeMessageWithCount(53);
            if (this.mIsLLMEnabled) {
                this.mRecordRsrpAfterGameEnd = false;
            } else {
                this.mRecordRsrpAfterGameEnd = true;
                MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
                miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(53), 180000L);
            }
            updateSceneSignalInfo(1, this.mIsLLMEnabled, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGameStatusChangedForDsi() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsLLMEnabled) {
            if (isLandscape()) {
                this.lastGameSceneDsiTime = jElapsedRealtime;
            }
        } else {
            long j = this.lastGameSceneDsiTime;
            if (j != 0) {
                recordDsiStats("landscape_game_dsi_statistic", (jElapsedRealtime - j) / 1000, this.lastDsi);
            }
            this.lastGameSceneDsiTime = 0L;
        }
    }

    private boolean isLandscape() {
        try {
            Thread.sleep(500L);
            Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
            if (defaultDisplay.getRotation() != 1 && defaultDisplay.getRotation() != 3) {
                if (defaultDisplay.getRotation() != 0) {
                    defaultDisplay.getRotation();
                }
                return false;
            }
            return true;
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkInfoChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        int type = networkInfo.getType();
        int i = this.mNetworkType;
        this.mNetworkTypeName = networkInfo.getTypeName();
        NetworkInfo.State state = networkInfo.getState();
        Utils.logD("MiuiTelephonyStatistic", "networkType=" + type + ", connected=" + state);
        MiuiImsStatistics.processNetworkInfoChanged(type, state);
        if (type == 0 || type == 1) {
            if (state == NetworkInfo.State.CONNECTED) {
                this.mNetworkType = type;
                onSceneStatsInfoChanged();
                if (type == 1 && this.mIsDcBlocked) {
                    reportFakeDataSnapshot("wifi");
                }
            } else if (state == NetworkInfo.State.DISCONNECTED) {
                this.mNetworkType = -1;
                onSceneStatsInfoChanged();
            }
            recordNetworkTypeChangedStallRate(i);
            storeNetworkConnectedTime(i, false);
        }
    }

    private void storeNetworkConnectedTime(int i, boolean z) {
        if (i != this.mNetworkType || z) {
            Utils.logD("MiuiTelephonyStatistic", "network info switch, last network = " + i + ", current netwrok = " + this.mNetworkType);
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j = this.mlastNetworkInfoSwitchTime;
            if (j == -1) {
                this.mlastNetworkInfoSwitchTime = jElapsedRealtime;
                return;
            }
            long j2 = jElapsedRealtime - j;
            if (i == 0) {
                this.mMobileConnectedTime = (int) (((long) this.mMobileConnectedTime) + j2);
                DefaultPreferenceHelper.getInstance().setMobileConnectedTime(this.mMobileConnectedTime);
            } else if (i == 1) {
                this.mWifiConnectedTime = (int) (((long) this.mWifiConnectedTime) + j2);
                DefaultPreferenceHelper.getInstance().setWifiConnectedTime(this.mWifiConnectedTime);
            }
            this.mlastNetworkInfoSwitchTime = jElapsedRealtime;
        }
    }

    private void recordNetworkConnectedTime() {
        storeNetworkConnectedTime(this.mNetworkType, true);
        HashMap map = new HashMap();
        map.put("mobileConnectedTime", String.valueOf(this.mMobileConnectedTime / 1000));
        map.put("wifiConnectedTime", String.valueOf(this.mWifiConnectedTime / 1000));
        Utils.logD("MiuiTelephonyStatistic", "recordNetworkConnectedTime params=" + map);
        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_network_connected_time", map);
    }

    private void clearNetworkConnectedTime() {
        this.mMobileConnectedTime = 0;
        this.mWifiConnectedTime = 0;
        try {
            DefaultPreferenceHelper.getInstance().setWifiConnectedTime(0);
            DefaultPreferenceHelper.getInstance().setMobileConnectedTime(0);
        } catch (Exception e) {
            Utils.logD("MiuiTelephonyStatistic", "clear network connected time exception:" + e);
        }
    }

    private void reloadNetworkConnectedTimeFromSp() {
        this.mMobileConnectedTime = DefaultPreferenceHelper.getInstance().getMobileConnectedTime();
        this.mWifiConnectedTime = DefaultPreferenceHelper.getInstance().getWifiConnectedTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateSceneSignalInfo(int i, boolean z, boolean z2) {
        int rsrp;
        int snr;
        try {
            if (isValidatePhoneId(this.mLastDdsPhoneId)) {
                ServiceState[] serviceStateArr = this.mServiceState;
                int i2 = this.mLastDdsPhoneId;
                String availableOperatorNumeric = getAvailableOperatorNumeric(serviceStateArr[i2], i2);
                if (!TextUtils.isEmpty(availableOperatorNumeric) && !"NoSim".equals(availableOperatorNumeric)) {
                    SignalStrength[] signalStrengthArr = this.mSignalStrength;
                    int i3 = this.mLastDdsPhoneId;
                    SignalStrength signalStrength = signalStrengthArr[i3];
                    String networkType = MiuiPhoneUtils.getNetworkType(i3);
                    FiveGServiceInfo fiveGServiceInfo = this.mFiveGInfo[this.mLastDdsPhoneId];
                    if (fiveGServiceInfo == null || !fiveGServiceInfo.is5GConnected()) {
                        rsrp = Integer.MAX_VALUE;
                        snr = Integer.MAX_VALUE;
                    } else {
                        rsrp = this.mFiveGInfo[this.mLastDdsPhoneId].getRsrp();
                        snr = this.mFiveGInfo[this.mLastDdsPhoneId].getSnr();
                    }
                    Utils.logD("MiuiTelephonyStatistic", "updateSceneSignalInfo mNetworkType=" + this.mNetworkType);
                    this.mModemStats.updateSceneStats(new StatsSignalInfo(signalStrength.getLteRsrp(), signalStrength.getLteRssnr(), signalStrength.getLteRsrq(), rsrp, snr, networkType, this.mNetworkType, i, z, availableOperatorNumeric), z2);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void updateDualSimNwBandInfo(int i, ServiceState serviceState, boolean z) {
        if (!isValidatePhoneId(i) || MiuiPhoneUtils.PHONE_COUNT != 2) {
            Utils.logD("MiuiTelephonyStatistic", "updateDualSimNwBandInfo not allowed ,ignore");
            return;
        }
        if (this.mIsDualSimInserted || z) {
            if (serviceState == null) {
                serviceState = this.mServiceState[i];
            }
            String band = Utils.getBand(i, serviceState);
            Utils.logD("MiuiTelephonyStatistic", "updateDualSimNwBandInfo band: " + band + ", phoneId:" + i);
            this.mModemStats.updateDualSimNwBands(i, band, z);
        }
    }

    public void updateNwBandInfo(int i, ServiceState serviceState, boolean z) {
        if (!isValidatePhoneId(i) || i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateNwBandInfo not allowed ,ignore");
            return;
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        String band = Utils.getBand(i, serviceState);
        Utils.logD("MiuiTelephonyStatistic", "updateNwBandInfo, plmn=" + availableOperatorNumeric + ", band: " + band + ", phoneId:" + i);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        HikingModemStats hikingModemStats = this.mHikingModemStats;
        if (hikingModemStats != null) {
            hikingModemStats.updateNwBands(availableOperatorNumeric, band, jElapsedRealtime, z);
        }
        HstModemStats hstModemStats = this.mHstModemStats;
        if (hstModemStats != null) {
            hstModemStats.updateNwBands(availableOperatorNumeric, band, jElapsedRealtime, z);
        }
        SubwayModemStats subwayModemStats = this.mSubwayModemStats;
        if (subwayModemStats != null) {
            subwayModemStats.updateNwBands(availableOperatorNumeric, band, jElapsedRealtime, z);
        }
        DriveModemStats driveModemStats = this.mDriveModemStats;
        if (driveModemStats != null) {
            driveModemStats.updateNwBands(availableOperatorNumeric, band, jElapsedRealtime, z);
        }
    }

    public void updateNsaNwBandInfo(int i, ServiceState serviceState, boolean z) {
        if (!isValidatePhoneId(i) || i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateNsaNwBandInfo not allowed ,ignore");
            return;
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        String band = Utils.getBand(i, serviceState);
        Utils.logD("MiuiTelephonyStatistic", "updateNsaNwBandInfo, plmn=" + availableOperatorNumeric + ", band: " + band + ", phoneId:" + i);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        NsaModemStats nsaModemStats = this.mNsaModemStats;
        if (nsaModemStats != null) {
            nsaModemStats.updateNwBands(availableOperatorNumeric, band, jElapsedRealtime, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNwSignalInfo(int i, SignalStrength signalStrength, ServiceState serviceState, boolean z) throws Throwable {
        int rsrpBin;
        int operatingBandForEarfcn;
        int i2;
        if (!isValidatePhoneId(i) || i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateNwSignalInfo not allowed ,ignore");
            return;
        }
        if (signalStrength == null) {
            signalStrength = this.mSignalStrength[i];
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        int i3 = -1;
        int dataNetworkType = serviceState != null ? serviceState.getDataNetworkType() : -1;
        Utils.logD("MiuiTelephonyStatistic", "updateNwSignalInfo networkType " + dataNetworkType);
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        if (Utils.isLte(dataNetworkType)) {
            rsrpBin = Utils.getRsrpBin(signalStrength.getLteRsrp());
            int snrBin = Utils.getSnrBin(signalStrength.getLteRssnr());
            if (PhoneProxy.isBandForEarfcnEnabled() && !PhoneProxy.isChannelNumberValid(serviceState) && ServiceProviderUtils.isChinaTelecom(availableOperatorNumeric)) {
                Utils.logD("MiuiTelephonyStatistic", "updateNwSignalInfo CDMA mLastBand=" + this.mLastBand[i]);
                operatingBandForEarfcn = this.mLastBand[i];
                if (operatingBandForEarfcn == -1) {
                    operatingBandForEarfcn = PhoneProxy.getOperatingBandForEarfcn(PhoneProxy.getLteEarfcn(i));
                    this.mLastBand[i] = operatingBandForEarfcn;
                }
                i2 = this.mLastArfcn[i];
                if (i2 == -1) {
                    int lteEarfcn = PhoneProxy.getLteEarfcn(i);
                    this.mLastArfcn[i] = lteEarfcn;
                    i2 = lteEarfcn;
                }
            } else {
                Utils.logD("MiuiTelephonyStatistic", "updateNwSignalInfo NON-CDMA band");
                int operatingBandForEarfcn2 = PhoneProxy.getOperatingBandForEarfcn(serviceState);
                this.mLastBand[i] = operatingBandForEarfcn2;
                int channelNumber = serviceState.getChannelNumber();
                this.mLastArfcn[i] = channelNumber;
                i2 = channelNumber;
                operatingBandForEarfcn = operatingBandForEarfcn2;
            }
            i3 = snrBin;
        } else {
            rsrpBin = -1;
            operatingBandForEarfcn = -1;
            i2 = -1;
        }
        Utils.logD("MiuiTelephonyStatistic", "updateNwSignalInfo phoneId=" + i + ",plmn=" + availableOperatorNumeric + ",rsrpBin=" + rsrpBin + ",snrBin=" + i3 + ",band=" + operatingBandForEarfcn + ",arfcn=" + i2 + ",force=" + z);
        if (this.mModemStats.isNwSignalChanged(availableOperatorNumeric, rsrpBin) || z) {
            this.mModemStats.updateNwSignalStats(availableOperatorNumeric, rsrpBin, i3, operatingBandForEarfcn, i2, 1, z);
            this.mRoamingModemStats.updateBandStats(getHplmnAndVplmn(serviceState, i), rsrpBin, operatingBandForEarfcn);
            return;
        }
        int i4 = i3;
        if (this.mModemStats.isOnlySnrChanged(i3)) {
            this.mModemStats.updateNwSignalStats(availableOperatorNumeric, rsrpBin, i4, operatingBandForEarfcn, i2, 2, z);
        }
        if (this.mModemStats.isOnlyBandChanged(operatingBandForEarfcn)) {
            this.mModemStats.updateNwSignalStats(availableOperatorNumeric, rsrpBin, i4, operatingBandForEarfcn, i2, 3, z);
            this.mRoamingModemStats.updateBandStats(getHplmnAndVplmn(serviceState, i), rsrpBin, operatingBandForEarfcn);
        }
        if (this.mModemStats.isOnlyArfcnChanged(i2)) {
            this.mModemStats.updateNwSignalStats(availableOperatorNumeric, rsrpBin, i4, operatingBandForEarfcn, i2, 4, z);
        }
    }

    public void updateHstArfcnInfo(int i, ServiceState serviceState, boolean z) {
        if (!isValidatePhoneId(i) || i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateHstArfcnInfo not allowed, ignore");
            return;
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        int channelNumber = serviceState.getChannelNumber();
        Utils.logD("MiuiTelephonyStatistic", "updateHstArfcnInfo, plmn=" + availableOperatorNumeric + ", arfcn: " + channelNumber + ", phoneId:" + i);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        HstModemStats hstModemStats = this.mHstModemStats;
        if (hstModemStats != null) {
            if (z || hstModemStats.isNwArfcnChanged(availableOperatorNumeric, channelNumber)) {
                this.mHstModemStats.updateNwArfcn(availableOperatorNumeric, channelNumber, jElapsedRealtime, z);
            }
        }
    }

    private void recordGlobalModemStats() {
        ConcurrentHashMap callStats = this.mModemStats.getCallStats();
        for (String str : callStats.keySet()) {
            if (!"NoSim".equals(str)) {
                HashMap map = new HashMap();
                map.put("MO_volteCn", 0);
                map.put("MO_csfbCn", 0);
                map.put("MT_volteCn", 0);
                map.put("MT_csfbCn", 0);
                map.put("MO_vonrCn", 0);
                map.put("MT_vonrCn", 0);
                map.put("MO_vilteCn", 0);
                map.put("MT_vilteCn", 0);
                map.put("MO_vowifiCn", 0);
                map.put("MT_vowifiCn", 0);
                map.put("MO_viwifiCn", 0);
                map.put("MT_viwifiCn", 0);
                map.put("MO_vodataCn", 0);
                map.put("MT_vodataCn", 0);
                map.put("MO_vidataCn", 0);
                map.put("MT_vidataCn", 0);
                map.put("Network", str);
                map.putAll((Map) callStats.get(str));
                Utils.logD("MiuiTelephonyStatistic", "call data=" + map.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "Call", map);
            }
        }
        ConcurrentHashMap nonDdsStats = this.mModemStats.getNonDdsStats();
        if (nonDdsStats.size() > 0) {
            HashMap map2 = new HashMap();
            map2.put("Nondds_Network", 0);
            map2.put("Nondds_Rat", 0);
            map2.putAll(nonDdsStats);
            MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "NonDds", map2);
        }
        ConcurrentHashMap ratStats = this.mModemStats.getRatStats();
        for (String str2 : ratStats.keySet()) {
            if (!"NoSim".equals(str2)) {
                HashMap map3 = new HashMap();
                map3.put("Rat_2G", 0);
                map3.put("Rat_3G", 0);
                map3.put("Rat_4G", 0);
                map3.put("Rat_5G", 0);
                map3.put("Network", str2);
                map3.putAll((Map) ratStats.get(str2));
                Utils.logD("MiuiTelephonyStatistic", "rat data=" + map3.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "Rat", map3);
            }
        }
        recordDFSRatStatsEvent(ratStats);
        if (this.mSupportFiveG) {
            recordFiveGStatsEvent(this.mModemStats.getFiveGStats());
        }
        ConcurrentHashMap signalStats = this.mModemStats.getSignalStats();
        for (String str3 : signalStats.keySet()) {
            if (!"NoSim".equals(str3)) {
                HashMap map4 = new HashMap();
                map4.put("Network", str3);
                map4.putAll((Map) signalStats.get(str3));
                Utils.logD("MiuiTelephonyStatistic", "signal data=" + map4.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "Signal", map4);
            }
        }
        ConcurrentHashMap bandStats = this.mModemStats.getBandStats();
        for (String str4 : bandStats.keySet()) {
            if (!"NoSim".equals(str4)) {
                HashMap map5 = new HashMap();
                map5.put("Network", str4);
                map5.putAll((Map) bandStats.get(str4));
                Utils.logD("MiuiTelephonyStatistic", "band data=" + map5.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "Band", map5);
            }
        }
        ConcurrentHashMap voWifiStats = this.mModemStats.getVoWifiStats();
        for (String str5 : voWifiStats.keySet()) {
            if (!"NoSim".equals(str5)) {
                HashMap map6 = new HashMap();
                map6.put("Network", str5);
                map6.put("Value", voWifiStats.get(str5));
                Utils.logD("MiuiTelephonyStatistic", "vowifi data=" + map6.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_vowifi", map6);
            }
        }
        ConcurrentHashMap volteStats = this.mModemStats.getVolteStats();
        for (String str6 : volteStats.keySet()) {
            if (!"NoSim".equals(str6)) {
                HashMap map7 = new HashMap();
                map7.put("Network", str6);
                map7.put("Value", volteStats.get(str6));
                Utils.logD("MiuiTelephonyStatistic", "ims data=" + map7.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_volte", map7);
            }
        }
        ArrayList arrayList = new ArrayList(this.mNwSearchDurations);
        ArrayList arrayList2 = new ArrayList(this.mNwOosDurations);
        recordObjectEvent("modem_ue_statistic", "event_nw_search_duration", arrayList);
        recordObjectEvent("modem_ue_statistic", "event_nw_out_of_service_duration", arrayList2);
        recordDFSNwOosDurations(915100014, arrayList);
        recordDFSNwOosDurations(915100013, arrayList2);
        ConcurrentHashMap bandStats2 = this.mRoamingModemStats.getBandStats();
        for (String str7 : bandStats2.keySet()) {
            if (!"NoSim".equals(str7)) {
                HashMap map8 = new HashMap();
                map8.put("Network", str7);
                map8.putAll((Map) bandStats2.get(str7));
                Utils.logD("MiuiTelephonyStatistic", "raomingBand data=" + map8.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_roaming_band", map8);
            }
        }
        ConcurrentHashMap ratStats2 = this.mRoamingModemStats.getRatStats();
        for (String str8 : ratStats2.keySet()) {
            if (!"NoSim".equals(str8)) {
                HashMap map9 = new HashMap();
                map9.put("Rat_2G", 0);
                map9.put("Rat_3G", 0);
                map9.put("Rat_4G", 0);
                map9.put("Rat_5G", 0);
                map9.put("Network", str8);
                map9.putAll((Map) ratStats2.get(str8));
                Utils.logD("MiuiTelephonyStatistic", "roamingRat data=" + map9.toString());
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_roaming_rat", map9);
            }
        }
        recordStatsEvent("event_lte_fiveg_signal_loss", this.mModemStats.getLteNrSignalLossStats());
        recordStatsEvent("event_roaming_call", this.mRoamingModemStats.getCallStats());
        NsaModemStats nsaModemStats = this.mNsaModemStats;
        if (nsaModemStats != null) {
            recordStatsEvent("event_nsa_band", nsaModemStats.getBandStats());
        }
        if (this.mNoLteNrDurations != null) {
            recordObjectEvent("modem_ue_statistic", "event_roaming_no_ltenr_event", new ArrayList(this.mNoLteNrDurations));
        }
        if (this.mMobileConnectedTime == 0 && this.mWifiConnectedTime == 0) {
            return;
        }
        recordNetworkConnectedTime();
    }

    private List convertMapStatsToList(ConcurrentHashMap concurrentHashMap) {
        ArrayList arrayList = new ArrayList();
        for (String str : concurrentHashMap.keySet()) {
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str);
            if (concurrentHashMap2 != null) {
                Utils.logD("MiuiTelephonyStatistic", "convertMapStatsToList value:" + concurrentHashMap2);
                for (String str2 : concurrentHashMap2.keySet()) {
                    String[] strArrSplit = TextUtils.isEmpty(str2) ? null : str2.split("_");
                    if (strArrSplit != null && strArrSplit.length > 0) {
                        HashMap map = new HashMap();
                        int i = 0;
                        while (i < strArrSplit.length) {
                            StringBuilder sb = new StringBuilder("Key");
                            int i2 = i + 1;
                            sb.append(i2);
                            map.put(sb.toString(), strArrSplit[i]);
                            i = i2;
                        }
                        map.put("Network", str);
                        map.put("Value", concurrentHashMap2.get(str2));
                        arrayList.add(map);
                    }
                }
            }
        }
        return arrayList;
    }

    private void recordFiveGStatsEvent(ConcurrentHashMap concurrentHashMap) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (Map map : convertMapStatsToList(concurrentHashMap)) {
            Object obj = map.get("Key1");
            if (obj != null) {
                String str = (String) obj;
                Utils.logD("MiuiTelephonyStatistic", "recordFiveGStatsEvent key: ".concat(str));
                if (str.startsWith("Rsrp")) {
                    arrayList2.add(map);
                } else if (str.startsWith("SaRsrp")) {
                    arrayList.add(map);
                } else if (str.equals("FiveGIcon") || str.equals("NonFiveGIcon")) {
                    arrayList3.add(map);
                }
            }
        }
        Utils.logD("MiuiTelephonyStatistic", "recordFiveGStatsEvent sa size: " + arrayList.size());
        if (arrayList.size() > 0) {
            recordObjectEvent("modem_ue_statistic", "FiveGSa", arrayList);
        }
        if (arrayList2.size() > 0) {
            recordObjectEvent("modem_ue_statistic", "FiveGNsa", arrayList2);
        }
        if (arrayList3.size() > 0) {
            recordObjectEvent("modem_ue_statistic", "FiveG", arrayList3);
        }
    }

    private void recordObjectEvent(String str, String str2, List list) {
        if (list.size() == 0) {
            Utils.logD("MiuiTelephonyStatistic", "recordStatsEvent empty params");
            return;
        }
        HashMap map = new HashMap();
        map.put("Params", list);
        Utils.logD("MiuiTelephonyStatistic", str2 + ": " + map);
        MiStatInterfaceUtil.recordObjectEvent(str, str2, map);
    }

    private void recordStatsEvent(String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null || concurrentHashMap.isEmpty() || TextUtils.isEmpty(str)) {
            return;
        }
        if ("FiveG".equals(str)) {
            recordFiveGStatsEvent(concurrentHashMap);
        } else {
            recordObjectEvent("modem_ue_statistic", str, convertMapStatsToList(concurrentHashMap));
        }
        if ("Rat".equals(str)) {
            recordDFSRatStatsEvent(concurrentHashMap);
        }
    }

    private void recordDFSRatStatsEvent(ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        for (Map.Entry entry : concurrentHashMap.entrySet()) {
            String str = (String) entry.getKey();
            for (Map.Entry entry2 : ((ConcurrentHashMap) entry.getValue()).entrySet()) {
                String str2 = (String) entry2.getKey();
                long jLongValue = ((Long) entry2.getValue()).longValue();
                HashMap map = new HashMap();
                map.put("RAT", str2);
                map.put("Network", str);
                map.put("duration", String.valueOf(jLongValue));
                recordDFSEventStatistic(915100011, map);
            }
        }
    }

    private void recordChinaModemStats() {
        Utils.logE("MiuiTelephonyStatistic", "recordModemStats Enter");
        try {
            recordStatsEvent("Signal", this.mModemStats.getSignalStats());
            recordStatsEvent("Band", this.mModemStats.getBandStats());
            recordStatsEvent("Arfcn", this.mModemStats.getArfcnStats());
            recordStatsEvent("Call", this.mModemStats.getCallStats());
            recordStatsEvent("Game", this.mModemStats.getGameStats());
            recordStatsEvent("Rat", this.mModemStats.getRatStats());
            recordStatsEvent("event_lte_fiveg_signal_loss", this.mModemStats.getLteNrSignalLossStats());
            recordStatsEvent("Wechat", this.mModemStats.getWechatStats());
            recordStatsEvent("FiveG", this.mModemStats.getFiveGStats());
            ConcurrentHashMap nonDdsStats = this.mModemStats.getNonDdsStats();
            recordStatsEvent("event_roaming_rat", this.mRoamingModemStats.getRatStats());
            recordStatsEvent("event_roaming_band", this.mRoamingModemStats.getBandStats());
            HikingModemStats hikingModemStats = this.mHikingModemStats;
            if (hikingModemStats != null) {
                recordStatsEvent("event_hiking_band", hikingModemStats.getBandStats());
            }
            HstModemStats hstModemStats = this.mHstModemStats;
            if (hstModemStats != null) {
                recordStatsEvent("event_hst_band", hstModemStats.getBandStats());
                recordStatsEvent("event_hst_arfcn", this.mHstModemStats.getArfcnStats());
            }
            SubwayModemStats subwayModemStats = this.mSubwayModemStats;
            if (subwayModemStats != null) {
                recordStatsEvent("event_subway_band", subwayModemStats.getBandStats());
            }
            DriveModemStats driveModemStats = this.mDriveModemStats;
            if (driveModemStats != null) {
                recordStatsEvent("event_drive_band", driveModemStats.getBandStats());
            }
            if (!nonDdsStats.isEmpty()) {
                Utils.logD("MiuiTelephonyStatistic", "recordModemStats nondds=" + nonDdsStats);
                MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "NonDds", new HashMap(nonDdsStats));
            }
            ArrayList arrayList = new ArrayList(this.mNwSearchDurations);
            ArrayList arrayList2 = new ArrayList(this.mNwOosDurations);
            recordObjectEvent("modem_ue_statistic", "event_nw_search_duration", arrayList);
            recordObjectEvent("modem_ue_statistic", "event_nw_out_of_service_duration", arrayList2);
            recordDFSNwOosDurations(915100014, arrayList);
            recordDFSNwOosDurations(915100013, arrayList2);
            recordQcRilReportMessage(0);
            recordDualSimBands();
            recordNetworkConnectedTime();
            HashMap map = new HashMap();
            map.put("ShortDurationCount", String.valueOf(this.mNwShortDurationOosCount));
            if (!Utils.isValidParams(map)) {
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_nw_out_of_service_no_limit_duration", map);
            }
            this.mNwShortDurationOosCount = 0;
            recordStatsEvent("event_roaming_call", this.mRoamingModemStats.getCallStats());
            if (this.mNoLteNrDurations != null) {
                recordObjectEvent("modem_ue_statistic", "event_roaming_no_ltenr_event", new ArrayList(this.mNoLteNrDurations));
            }
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "recordModemStats error: " + e);
        }
    }

    private void recordDualSimBands() {
        int i;
        ConcurrentHashMap dualSimbands = this.mModemStats.getDualSimbands();
        if (dualSimbands == null || dualSimbands.isEmpty()) {
            return;
        }
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        Iterator it = dualSimbands.keySet().iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            String[] strArrSplit = TextUtils.isEmpty(str) ? null : str.split("_");
            if (strArrSplit != null && strArrSplit.length > 0) {
                HashMap map2 = new HashMap();
                while (i < strArrSplit.length) {
                    StringBuilder sb = new StringBuilder("Key");
                    int i2 = i + 1;
                    sb.append(i2);
                    map2.put(sb.toString(), strArrSplit[i]);
                    i = i2;
                }
                map2.put("Value", dualSimbands.get(str));
                arrayList.add(map2);
            }
        }
        if (arrayList.size() > 0) {
            map.put("Params", arrayList);
        }
        while (i < MiuiPhoneUtils.PHONE_COUNT) {
            map.put("Network" + i, this.mPlmn[i]);
            i++;
        }
        Utils.logD("MiuiTelephonyStatistic", "recordDualSimBands params=" + map);
        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_dual_sim_band", map);
    }

    private void recordQcRilReportMessage(int i) {
        RilHook rilHookInit = RilHook.init(this.mContext);
        if (rilHookInit == null) {
            return;
        }
        String qcRilReportMsg = rilHookInit.getQcRilReportMsg(i);
        if (TextUtils.isEmpty(qcRilReportMsg)) {
            return;
        }
        Utils.logD("MiuiTelephonyStatistic", "getQcRilReportMsg:" + qcRilReportMsg.trim());
        ArrayList arrayListConvertStringStatsToList = convertStringStatsToList(qcRilReportMsg.trim());
        if (arrayListConvertStringStatsToList == null || arrayListConvertStringStatsToList.size() <= 0) {
            return;
        }
        recordObjectEvent("modem_ue_statistic", "event_qcril_report_message", arrayListConvertStringStatsToList);
        rilHookInit.deleteQcRilReportMsg(i);
    }

    private ArrayList convertStringStatsToList(String str) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split(",")) {
            String[] strArrSplit = str2.split(":");
            if (strArrSplit.length == 2 && !TextUtils.isEmpty(strArrSplit[0]) && !TextUtils.isEmpty(strArrSplit[1])) {
                HashMap map = new HashMap();
                map.put(strArrSplit[0], strArrSplit[1]);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    private void recordModemStats() {
        ModemStats modemStats = this.mModemStats;
        if (modemStats == null || modemStats.getSignalStats() == null || this.mModemStats.getSignalStats().isEmpty()) {
            Utils.logD("MiuiTelephonyStatistic", "recordModemStats -invalid modem-return");
            return;
        }
        if (Utils.IS_INTERNATIONAL_BUILD) {
            recordGlobalModemStats();
        } else {
            recordChinaModemStats();
        }
        if (shouldProcessHstRachStats() && this.mCurrentHstState && this.mEnterHstRachData != null) {
            ArrayList rachFailRateInfo = ((IModemModule) ModuleDelegate.get(IModemModule.class)).getRachFailRateInfo();
            accumulateHstRachDiff(rachFailRateInfo, this.mEnterHstRachData);
            this.mEnterHstRachData = createZeroRachData(rachFailRateInfo);
        }
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).reportModemHydraStat();
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).reportModemAiStats();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceUpdataModemStats(int i, boolean z) throws Throwable {
        if (!isValidatePhoneId(i)) {
            Utils.logD("MiuiTelephonyStatistic", "forceUpdataModemStats not allowed ,ignore");
            return;
        }
        updateDualSimNwBandInfo(i, null, z);
        if (i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "forceUpdataModemStats not allowed on nodds");
            return;
        }
        updateNwSignalInfo(i, null, null, z);
        updateFiveGInfo(i, this.mFiveGInfo[i], z);
        updateRatDataStateInfo(i, null, -1, z);
        updateNwBandInfo(i, null, z);
        updateNsaNwBandInfo(i, null, z);
    }

    private void updateNonDdsInfo(int i, ServiceState serviceState) {
        if (!isValidatePhoneId(i) || i == this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateNonDdsInfo not allowed ,ignore");
            return;
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        TelephonyManagerEx.getDefault();
        String networkClassNameByNetworkClass = TelephonyManagerEx.getNetworkClassNameByNetworkClass(getNetworkClass(serviceState));
        Utils.logD("MiuiTelephonyStatistic", "updateNonDdsInfo phoneId=" + i + ",plmn=" + availableOperatorNumeric + ",nwClass=" + networkClassNameByNetworkClass);
        if (this.mModemStats.isNonDdsNwChanged(availableOperatorNumeric, networkClassNameByNetworkClass)) {
            this.mModemStats.updateNonDdsStats(availableOperatorNumeric, networkClassNameByNetworkClass);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRatDataStateInfo(int i, ServiceState serviceState, int i2, boolean z) {
        String ratDataState;
        if (!isValidatePhoneId(i) || i != this.mLastDdsPhoneId) {
            Utils.logD("MiuiTelephonyStatistic", "updateRatDataStateInfo not allowed ,ignore");
            return;
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        if (i2 == -1) {
            i2 = this.mOldDataConnectionState;
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(serviceState, i);
        int dataNetworkType = serviceState.getDataNetworkType();
        FiveGServiceInfo fiveGServiceInfo = this.mFiveGInfo[i];
        if (fiveGServiceInfo != null && fiveGServiceInfo.isShow5GIcon()) {
            dataNetworkType = 20;
        }
        if (Utils.IS_INTERNATIONAL_BUILD) {
            ratDataState = Utils.getNwClassDataState(dataNetworkType, i2);
        } else {
            ratDataState = Utils.getRatDataState(dataNetworkType, i2);
        }
        Utils.logD("MiuiTelephonyStatistic", "updateRatDataStateInfo phoneId=" + i + ",plmn=" + availableOperatorNumeric + ",ratDataState=" + ratDataState + ",force=" + z);
        if (this.mModemStats.isRatOrDataStateChanged(availableOperatorNumeric, ratDataState) || z) {
            this.mModemStats.updateRatStats(availableOperatorNumeric, ratDataState);
            this.mRoamingModemStats.updateRatState(getHplmnAndVplmn(serviceState, i), ratDataState);
            HstTunnelModemStats hstTunnelModemStats = this.mHstTunnelModemStats;
            if (hstTunnelModemStats != null) {
                hstTunnelModemStats.updateRatState(availableOperatorNumeric, ratDataState);
            }
            if (MiuiPhoneUtils.isLteNrNwMode(i) && !"NoSim".equals(availableOperatorNumeric) && isPhoneStateIdle(i)) {
                this.mModemStats.updateNoLteNrSignalStats(availableOperatorNumeric, ratDataState, Utils.isLte(dataNetworkType) || Utils.isNr(dataNetworkType));
            }
        }
        onSceneStatsInfoChanged();
    }

    private String getHplmnAndVplmn(ServiceState serviceState, int i) {
        if (!isValidatePhoneId(i)) {
            return "NoSim";
        }
        if (serviceState == null) {
            serviceState = this.mServiceState[i];
        }
        String simOperatorForSlot = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        String operatorNumeric = serviceState.getOperatorNumeric();
        if (simOperatorForSlot == null) {
            simOperatorForSlot = "";
        }
        if (operatorNumeric == null) {
            operatorNumeric = "";
        }
        return simOperatorForSlot + "_" + operatorNumeric;
    }

    class FiveGServiceInfoListener implements BaseEvent.IEventListener {
        FiveGServiceInfoListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            TelephonyEvent.FiveGEvent fiveGEvent;
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.FiveGEvent) || (bundle = (fiveGEvent = (TelephonyEvent.FiveGEvent) baseEvent).getBundle()) == null) {
                return;
            }
            int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID);
            FiveGServiceInfo fiveGServiceInfo = fiveGEvent.getFiveGServiceInfo();
            if (fiveGServiceInfo == null || !MiuiTelephonyStatistic.this.isValidatePhoneId(i)) {
                Utils.logD("MiuiTelephonyStatistic", "FiveGServiceListener info is null or invalid phone id");
                return;
            }
            Utils.logD("MiuiTelephonyStatistic", "info[" + i + "]= " + fiveGServiceInfo);
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(52, i, 0, fiveGServiceInfo).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFiveGInfo(int i, FiveGServiceInfo fiveGServiceInfo, boolean z) {
        try {
            if (this.mSupportFiveG && isValidatePhoneId(i) && i == this.mLastDdsPhoneId) {
                if (fiveGServiceInfo != null) {
                    this.mFiveGInfo[i].copyFrom(fiveGServiceInfo);
                    String availableOperatorNumeric = getAvailableOperatorNumeric(null, i);
                    boolean zIsShow5GIcon = fiveGServiceInfo.isShow5GIcon();
                    int rsrpLevel = zIsShow5GIcon ? fiveGServiceInfo.getRsrpLevel() : 0;
                    int snrLevel = zIsShow5GIcon ? fiveGServiceInfo.getSnrLevel() : 0;
                    this.mLastNrBand[i] = Utils.getOperatingBandForNrarfcn(this.mServiceState[i]);
                    this.mLastNrArfcn[i] = this.mServiceState[i].getChannelNumber();
                    Utils.logD("MiuiTelephonyStatistic", "showFiveGIcon = " + zIsShow5GIcon + ", plmn = " + availableOperatorNumeric + ", rsrpLevel =" + rsrpLevel + ", snrLevel = " + snrLevel + ", mLastNrBand = " + this.mLastNrBand[i] + ", mLastNrArfcn = " + this.mLastNrArfcn[i]);
                    this.mModemStats.updateFiveGStats(availableOperatorNumeric, new StatsFiveGInfo(availableOperatorNumeric, this.mFiveGInfo[i].getNrConfigType(), rsrpLevel, snrLevel, zIsShow5GIcon, this.mLastNrBand[i], this.mLastNrArfcn[i]), z);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private int getNrConfigType(int i) {
        FiveGServiceInfo fiveGServiceInfo;
        if (isValidatePhoneId(i) && (fiveGServiceInfo = this.mFiveGInfo[i]) != null && fiveGServiceInfo.is5GConnected()) {
            return this.mFiveGInfo[i].getNrConfigType();
        }
        return -1;
    }

    private void recordMaxNetworkRate() {
        if (DeviceUtil.IS_STABLE_VERSION) {
            return;
        }
        ArrayList maxNetworkByteList = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getMaxNetworkByteList();
        Utils.logD("MiuiTelephonyStatistic", "recordMaxNetworkRate records: " + maxNetworkByteList);
        if (maxNetworkByteList.size() > 0) {
            HashMap map = new HashMap();
            map.put("Params", maxNetworkByteList);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_max_network_rate", map);
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).clearMaxNetworkByteList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSsOptimizationStatistic(Intent intent) {
        HashMap map = new HashMap();
        String stringExtra = intent.getStringExtra("ss_optimization_extra");
        Utils.logD("MiuiTelephonyStatistic", "recordSsOptimizationStatistic: " + stringExtra);
        if (stringExtra != null) {
            map.put("Signal", stringExtra);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ssOptimization", map);
        }
    }

    private boolean isEuicc(int i) {
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot != null) {
            return availableSubscriptionInfoBySlot.isEmbedded();
        }
        return false;
    }

    public void addStatisticListener(StatisticListener statisticListener) {
        if (statisticListener != null) {
            this.mStatisticListener.add(statisticListener);
        }
    }

    private HashMap getExternalStatistic() {
        HashMap map = new HashMap();
        new HashMap();
        Iterator it = this.mStatisticListener.iterator();
        while (it.hasNext()) {
            HashMap statisticListener = ((StatisticListener) it.next()).getStatisticListener();
            if (statisticListener != null && !statisticListener.isEmpty()) {
                map.putAll(statisticListener);
            }
        }
        return map;
    }

    private void registerTeleDataCallBack() {
        Utils.logD("MiuiTelephonyStatistic", "registerTeleDataCallBack");
        if (this.mTeleDataCallBack == null) {
            Utils.logD("MiuiTelephonyStatistic", "new TeleDataCallBack");
            TeleDataCallBack teleDataCallBack = new TeleDataCallBack();
            this.mTeleDataCallBack = teleDataCallBack;
            TelephonyStatAdapter.registerDataCallBack(teleDataCallBack);
        }
    }

    private void unregisterTeleDataCallBack() {
        TeleDataCallBack teleDataCallBack = this.mTeleDataCallBack;
        if (teleDataCallBack != null) {
            TelephonyStatAdapter.unregisterDataCallBack(teleDataCallBack);
            this.mTeleDataCallBack = null;
        }
    }

    class TeleDataCallBack implements TelephonyStatAdapter.IDataCallBack {
        TeleDataCallBack() {
        }

        public void onDataCallListChanged(int i, int i2, String str) {
            MiuiTelephonyStatistic.this.mStatisticHandler.obtainMessage(80, i, i2, str).sendToTarget();
        }
    }

    public String getSceneType() {
        if (isHstState()) {
            return "hst";
        }
        if (isDriveState()) {
            return "drive";
        }
        if (isSubwayState()) {
            return "subway";
        }
        if (isHikingState()) {
            return "hiking";
        }
        if (isElevatorState()) {
            return "elevator";
        }
        return "NA";
    }

    private boolean isHstState() {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHighSpeedTrain();
    }

    private boolean isDriveState() {
        return getSensorDriveState() == 10;
    }

    private boolean isSubwayState() {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isInSubwayState();
    }

    private boolean isHikingState() {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isHikingState();
    }

    private boolean isElevatorState() {
        if (TelephonySensorManager.getInstance() != null) {
            return TelephonySensorManager.getInstance().isInElevator();
        }
        return false;
    }

    private void recordDFSNwOosDurations(int i, ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            Map map = (Map) obj;
            if (map != null) {
                HashMap map2 = new HashMap();
                for (Map.Entry entry : map.entrySet()) {
                    map2.put((String) entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
                }
                recordDFSEventStatistic(i, map2);
            }
        }
    }

    private void dfsEventForMtb(int i) {
        if (this.mMtbTargetEvents.contains(Integer.valueOf(i))) {
            if (!this.mIsAutoLogPropVerified) {
                this.mAutoCaptureLogMask = android.os.SystemProperties.getInt(ModemServiceMoniter.PROPERTY_AUTO_CAPTURE_LOG_MASK, -1);
                this.mIsAutoLogPropVerified = true;
            }
            if (1 == this.mAutoCaptureLogMask) {
                Intent intent = new Intent("android.intent.action.DFS_MODEM_EVENT_FOR_MTB");
                intent.setFlags(16777216);
                intent.putExtra("eventId", i);
                intent.setPackage("com.xiaomi.mtb");
                this.mContext.sendBroadcast(intent);
            }
        }
    }

    public void recordDFSEventStatistic(int i, HashMap map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        Object[] objArr = new Object[map.size() * 2];
        int i2 = 0;
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            if (!SENSITIVE_KEYS.contains(str)) {
                int i3 = i2 + 1;
                objArr[i2] = str;
                i2 += 2;
                objArr[i3] = entry.getValue();
            }
        }
        try {
            sendEvent(MiSight.constructEvent(i, objArr));
            dfsEventForMtb(i);
        } catch (Exception e) {
            Utils.logD("MiuiTelephonyStatistic", "recordDFSEventStatistic exception:" + e);
        }
    }

    private void recordDFSSatelliteSMSEvent() {
        HashMap mapAddDfsSatelliteInfo = addDfsSatelliteInfo();
        int i = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("ntn_rssi"));
        int i2 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("ntn_snr"));
        int i3 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("fer"));
        int i4 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("error_cause"));
        if (i4 == 17 || i4 == 38 || i4 == 41) {
            recordDFSEventStatistic(915910001, mapAddDfsSatelliteInfo);
        } else if ((i < -125 || i2 < 2) && i3 > 50) {
            recordDFSEventStatistic(915910002, mapAddDfsSatelliteInfo);
        }
    }

    private void recordDFSSatelliteCallFailedEvent(Connection connection) {
        HashMap mapAddDfsSatelliteInfo = addDfsSatelliteInfo();
        mapAddDfsSatelliteInfo.put("CallDirect", connection.isIncoming() ? "MT" : "MO");
        int i = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("ntn_rssi"));
        int i2 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("ntn_snr"));
        int i3 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("fer"));
        int i4 = Integer.parseInt((String) mapAddDfsSatelliteInfo.get("error_cause"));
        if (isCallOffhook(connection)) {
            if (i4 == 38 || i4 == 41) {
                recordDFSEventStatistic(915920003, mapAddDfsSatelliteInfo);
                return;
            } else {
                if ((i < -125 || i2 < 2) && i3 > 50) {
                    recordDFSEventStatistic(915920004, mapAddDfsSatelliteInfo);
                    return;
                }
                return;
            }
        }
        if (i4 == 38 || i4 == 41) {
            recordDFSEventStatistic(915920001, mapAddDfsSatelliteInfo);
        } else if ((i < -125 || i2 < 2) && i3 > 50) {
            recordDFSEventStatistic(915920002, mapAddDfsSatelliteInfo);
        }
    }

    public static HashMap addDfsSatelliteInfo() {
        HashMap map = new HashMap();
        ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
        if (iSatelliteModule != null) {
            map.put("ntn_rssi", String.valueOf(iSatelliteModule.getRetRssi()));
            map.put("ntn_snr", String.valueOf(iSatelliteModule.getRetSnr()));
            map.put("fer", String.valueOf(iSatelliteModule.getFer()));
            map.put("error_calss", String.valueOf(iSatelliteModule.getErrorClass()));
            map.put("error_cause", String.valueOf(iSatelliteModule.getErrorCause()));
        }
        return map;
    }

    private static void sendEvent(MiEvent miEvent) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSight.sendEvent(miEvent);
        }
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processStallRateWhiteList(ForegroundInfo foregroundInfo) {
        String str = foregroundInfo.mForegroundPackageName;
        this.mCurrentForegroundPackageName = str;
        if (TextUtils.equals(str, this.mLastForegroundPackageName)) {
            return;
        }
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).processTopAppChanged(this.mCurrentForegroundPackageName);
        if (isStallRateVideoWhiteList(this.mLastForegroundPackageName) || isStallRateMainpageWhiteList(this.mLastForegroundPackageName) || isStallRateVideoWhiteList(this.mCurrentForegroundPackageName)) {
            processStallRateStatistics();
        }
        this.mLastForegroundPackageName = this.mCurrentForegroundPackageName;
    }

    private void processStallRateStatistics() {
        MiuiTelephonyStatistic miuiTelephonyStatistic;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (isStallRateVideoWhiteList(this.mLastForegroundPackageName) || isStallRateMainpageWhiteList(this.mLastForegroundPackageName)) {
            if (isStallRateVideoWhiteList(this.mLastForegroundPackageName)) {
                this.mAppUsageDuration += jElapsedRealtime - this.mAppUsageStartTime;
            }
            if (this.mLastVideoQualityStatus == 0) {
                singleStallDuration(jElapsedRealtime);
            }
            long j = this.mAppUsageDuration;
            if (j > 28800000 || j < MirrorDesktopHelper.TIMEOUT_MILLIS) {
                miuiTelephonyStatistic = this;
            } else {
                miuiTelephonyStatistic = this;
                miuiTelephonyStatistic.recordStallRateStatistic(this.mLastForegroundPackageName, j, this.mAppStallDuration);
                Utils.logD("MiuiTelephonyStatistic", "record sucess package=" + miuiTelephonyStatistic.mLastForegroundPackageName + "mAppUsageDuration=" + miuiTelephonyStatistic.mAppUsageDuration + "mAppStallDuration=" + miuiTelephonyStatistic.mAppStallDuration);
            }
            miuiTelephonyStatistic.clearStallRateStatistic();
            miuiTelephonyStatistic.mLastVideoQualityStatus = -1;
            miuiTelephonyStatistic.mIsStallRatePeriod = false;
        } else {
            miuiTelephonyStatistic = this;
        }
        if (miuiTelephonyStatistic.isStallRateVideoWhiteList(miuiTelephonyStatistic.mCurrentForegroundPackageName)) {
            miuiTelephonyStatistic.mAppUsageStartTime = jElapsedRealtime;
            miuiTelephonyStatistic.mIsStallRatePeriod = true;
            Utils.logD("MiuiTelephonyStatistic", "mCurrentForegroundPackageName=" + miuiTelephonyStatistic.mCurrentForegroundPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processVoipStallRateStatistics(boolean z) {
        MiuiTelephonyStatistic miuiTelephonyStatistic;
        if (z == this.mIsWechatVoip) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsWechatVoip) {
            this.mLastForegroundPackageName = VOIP_PACKAGENAME;
            this.mAppUsageStartTime = jElapsedRealtime;
            this.mIsStallRatePeriod = true;
            Utils.logD("MiuiTelephonyStatistic", "processVoipStallRateStatistics mIsWechatVoip=" + this.mIsWechatVoip);
            return;
        }
        this.mAppUsageDuration += jElapsedRealtime - this.mAppUsageStartTime;
        if (this.mLastVideoQualityStatus == 0) {
            singleStallDuration(jElapsedRealtime);
        }
        long j = this.mAppUsageDuration;
        if (j > 28800000 || j < MirrorDesktopHelper.TIMEOUT_MILLIS) {
            miuiTelephonyStatistic = this;
        } else {
            miuiTelephonyStatistic = this;
            miuiTelephonyStatistic.recordStallRateStatistic(this.mLastForegroundPackageName, j, this.mAppStallDuration);
            Utils.logD("MiuiTelephonyStatistic", "record sucess package=" + miuiTelephonyStatistic.mLastForegroundPackageName + "mAppUsageDuration=" + miuiTelephonyStatistic.mAppUsageDuration + "mAppStallDuration=" + miuiTelephonyStatistic.mAppStallDuration);
        }
        miuiTelephonyStatistic.clearStallRateStatistic();
        miuiTelephonyStatistic.mLastVideoQualityStatus = -1;
        miuiTelephonyStatistic.mIsStallRatePeriod = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMiuiVideoInfo(Intent intent) {
        MiuiTelephonyStatistic miuiTelephonyStatistic;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (isStallRateMainpageWhiteList(this.mCurrentForegroundPackageName)) {
            int intExtra = intent.getIntExtra("render-start", -1);
            if (intExtra == 1) {
                this.mAppUsageStartTime = jElapsedRealtime;
                this.mIsStallRatePeriod = true;
                if (this.mIsWechatVoip) {
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).updateModemHydraStat("wechat", true, this.mLastDdsPhoneId);
                    this.mIsWechatVideo = true;
                }
            } else {
                if (intExtra == 0) {
                    this.mAppUsageDuration += jElapsedRealtime - this.mAppUsageStartTime;
                    if (this.mLastVideoQualityStatus == 0) {
                        singleStallDuration(jElapsedRealtime);
                    }
                    long j = this.mAppUsageDuration;
                    if (j > 28800000 || j < MirrorDesktopHelper.TIMEOUT_MILLIS) {
                        miuiTelephonyStatistic = this;
                    } else {
                        miuiTelephonyStatistic = this;
                        miuiTelephonyStatistic.recordStallRateStatistic(this.mLastForegroundPackageName, j, this.mAppStallDuration);
                        Utils.logD("MiuiTelephonyStatistic", "record sucess package=" + miuiTelephonyStatistic.mLastForegroundPackageName + "mAppUsageDuration=" + miuiTelephonyStatistic.mAppUsageDuration + "mAppStallDuration=" + miuiTelephonyStatistic.mAppStallDuration);
                    }
                    miuiTelephonyStatistic.mIsStallRatePeriod = false;
                    miuiTelephonyStatistic.mLastVideoQualityStatus = -1;
                }
                Utils.logD("MiuiTelephonyStatistic", "isVideoInFrontState=" + intExtra);
            }
            miuiTelephonyStatistic = this;
            Utils.logD("MiuiTelephonyStatistic", "isVideoInFrontState=" + intExtra);
        } else {
            miuiTelephonyStatistic = this;
        }
        if (miuiTelephonyStatistic.mIsStallRatePeriod) {
            int intExtra2 = intent.getIntExtra("render-quality", -1);
            Utils.logD("MiuiTelephonyStatistic", "mCurrentVideoQualityStatus=" + intExtra2);
            if (intExtra2 == 0 && miuiTelephonyStatistic.mLastVideoQualityStatus == 1) {
                miuiTelephonyStatistic.mAppStallStartTime = jElapsedRealtime;
                miuiTelephonyStatistic.mIsAbnormalCell = true;
                miuiTelephonyStatistic.AbnormalCellDelay();
            } else if (intExtra2 == 1 && miuiTelephonyStatistic.mLastVideoQualityStatus == 0) {
                miuiTelephonyStatistic.singleStallDuration(jElapsedRealtime);
                miuiTelephonyStatistic.mIsAbnormalCell = false;
            }
            miuiTelephonyStatistic.mLastVideoQualityStatus = intExtra2;
        }
    }

    private void singleStallDuration(long j) {
        long j2 = j - this.mAppStallStartTime;
        if (j2 < 2000 || j2 > 30000) {
            return;
        }
        this.mAppStallDuration += j2;
        Utils.logD("MiuiTelephonyStatistic", "mSingleStallDur=" + j2);
    }

    private void AbnormalCellDelay() {
        if (this.mNetworkType != 0) {
            return;
        }
        if (this.mStatisticHandler.hasMessages(85)) {
            this.mStatisticHandler.removeMessageWithCount(85);
        }
        MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
        miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(85), 2000L);
        Utils.logD("MiuiTelephonyStatistic", "singleStallDelay");
    }

    private void recordNetworkTypeChangedStallRate(int i) {
        MiuiTelephonyStatistic miuiTelephonyStatistic;
        if (!this.mIsStallRatePeriod || i == this.mNetworkType) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mAppUsageDuration += jElapsedRealtime - this.mAppUsageStartTime;
        this.mAppUsageStartTime = jElapsedRealtime;
        if (this.mLastVideoQualityStatus == 0) {
            singleStallDuration(jElapsedRealtime);
        }
        long j = this.mAppUsageDuration;
        if (j > 28800000 || j < MirrorDesktopHelper.TIMEOUT_MILLIS) {
            miuiTelephonyStatistic = this;
        } else {
            miuiTelephonyStatistic = this;
            miuiTelephonyStatistic.recordStallRateStatistic(this.mLastForegroundPackageName, j, this.mAppStallDuration);
            Utils.logD("MiuiTelephonyStatistic", "NetworkTypeChanged mNetworkTypeName=" + miuiTelephonyStatistic.mNetworkTypeName);
        }
        miuiTelephonyStatistic.clearStallRateStatistic();
    }

    private void recordStallRateStatistic(String str, long j, long j2) {
        int defaultDataSlotId = getDefaultDataSlotId();
        if (isValidatePhoneId(defaultDataSlotId)) {
            int dataRat = getDataRat(this.mServiceState[defaultDataSlotId]);
            FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
            String province = "";
            String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
            if (fiveGCloudController != null) {
                province = fiveGCloudController.getProvince();
            }
            HashMap map = new HashMap();
            map.put("Network", getAvailableOperatorNumeric(null, defaultDataSlotId));
            map.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            map.put("c", city);
            map.put("p", province);
            map.put("sceneType", getSceneType());
            map.put("networkType", this.mNetworkTypeName);
            map.put("packages", str);
            map.put("usage_duration", String.valueOf(j / 1000));
            map.put("stall_duration", String.valueOf(j2 / 1000));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_stall_rate_statistic", map);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordAbnormalCell() {
        String province;
        int defaultDataSlotId = getDefaultDataSlotId();
        if (isValidatePhoneId(defaultDataSlotId)) {
            Phone phone = MiuiPhoneUtils.getPhone(defaultDataSlotId);
            int dataRat = getDataRat(this.mServiceState[defaultDataSlotId]);
            String signalQuality = getSignalQuality(this.mSignalStrength[defaultDataSlotId], dataRat);
            String str = getDbm(this.mSignalStrength[defaultDataSlotId]) + "," + miui.telephony.TelephonyManager.getDefault().getMiuiLevel(this.mSignalStrength[defaultDataSlotId]);
            FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
            String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
            if (fiveGCloudController == null) {
                province = "";
            } else {
                province = fiveGCloudController.getProvince();
            }
            Utils.DsdaState dsdaState = Utils.getDsdaState();
            HashMap map = new HashMap();
            map.put("CellLocation", getCellLocation(phone));
            map.put("Network", getAvailableOperatorNumeric(null, defaultDataSlotId));
            map.put("RAT", ServiceState.rilRadioTechnologyToString(dataRat));
            map.put("SignalStrength", str);
            map.put("SignalQuality", signalQuality);
            map.put("Band", Utils.getBand(defaultDataSlotId, this.mServiceState[defaultDataSlotId]));
            map.put("dsda", String.valueOf(dsdaState.ordinal()));
            map.put("isInCall", MiuiPhoneUtils.isDuringCall() ? "1" : "0");
            map.put("c", city);
            map.put("p", province);
            map.put("sceneType", getSceneType());
            map.put("packages", this.mCurrentForegroundPackageName);
            map.put("ULPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getPeakUlRateBps()));
            map.put("DLPeakRate", String.valueOf(((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getPeakDlRateBps()));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_abnormal_cell", map);
            HashMap map2 = new HashMap();
            map2.putAll(map);
            map2.put("c", "");
            map2.put("p", "");
            map2.put("CellLocation", "");
            localLog("event_abnormal_cell", map2.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChangedForDsi() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsScreenOn) {
            this.lastAllSceneDsiTime = jElapsedRealtime;
            regHandsensor();
            return;
        }
        long j = this.lastAllSceneDsiTime;
        if (j != 0) {
            recordDsiStats("all_scene_dsi_statistic", (jElapsedRealtime - j) / 1000, this.lastDsi);
            this.lastAllSceneDsiTime = 0L;
        }
        long j2 = this.lastGameSceneDsiTime;
        if (j2 != 0) {
            recordDsiStats("landscape_game_dsi_statistic", (jElapsedRealtime - j2) / 1000, this.lastDsi);
            this.lastGameSceneDsiTime = 0L;
        }
        long j3 = this.lastSingleHandSceneDsiTime;
        if (j3 != 0) {
            recordDsiStats("single_hand_dsi_statistic", (jElapsedRealtime - j3) / 1000, this.lastDsi);
            this.lastSingleHandSceneDsiTime = 0L;
        }
        unRegHandsensor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDsiStats(String str, long j, int i) {
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mDsiStats.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mDsiStats.put(str, concurrentHashMap);
        }
        concurrentHashMap.put("dsi_" + i, Long.valueOf(((Long) concurrentHashMap.getOrDefault("dsi_" + i, 0L)).longValue() + j));
        Utils.logD("MiuiTelephonyStatistic", "recordDsiStats: eventName = " + str + ", dur = " + j + ", dsi = " + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDsiValueChanged(Intent intent) {
        int intExtra;
        if (intent == null || (intExtra = intent.getIntExtra(ModemSarController.BC_INTENT_EXTRA_DSI_VALUE, 0)) == this.lastDsi) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastAllSceneDsiTime;
        if (j != 0) {
            recordDsiStats("all_scene_dsi_statistic", (jElapsedRealtime - j) / 1000, this.lastDsi);
        }
        long j2 = this.lastGameSceneDsiTime;
        if (j2 != 0) {
            recordDsiStats("landscape_game_dsi_statistic", (jElapsedRealtime - j2) / 1000, this.lastDsi);
        }
        long j3 = this.lastSingleHandSceneDsiTime;
        if (j3 != 0) {
            recordDsiStats("single_hand_dsi_statistic", (jElapsedRealtime - j3) / 1000, this.lastDsi);
        }
        this.lastDsi = intExtra;
        if (this.mIsScreenOn) {
            this.lastAllSceneDsiTime = jElapsedRealtime;
            if (this.mIsLLMEnabled && isLandscape()) {
                this.lastGameSceneDsiTime = jElapsedRealtime;
            }
            if (this.mIsSingleHandApp && !this.mIsmotionless) {
                this.lastSingleHandSceneDsiTime = jElapsedRealtime;
            }
        }
        Utils.logD("MiuiTelephonyStatistic", "processDsiValueChanged: currentDsi = " + intExtra + ", lastDsi = " + this.lastDsi + ", lastDsiTime = " + this.lastAllSceneDsiTime);
    }

    private void recordDsiStatistic() {
        ConcurrentHashMap concurrentHashMap = this.mDsiStats;
        if (concurrentHashMap == null || concurrentHashMap.size() <= 0) {
            return;
        }
        for (String str : this.mDsiStats.keySet()) {
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, new HashMap((Map) this.mDsiStats.get(str)));
        }
    }

    private void clearStallRateStatistic() {
        this.mAppStallDuration = 0L;
        this.mAppUsageDuration = 0L;
    }

    private boolean isStallRateVideoWhiteList(String str) {
        if (str == null) {
            return false;
        }
        return ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("config_video_white_list"), str);
    }

    private boolean isStallRateMainpageWhiteList(String str) {
        if (str == null) {
            return false;
        }
        return ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("config_mainpage_white_list"), str);
    }

    private void updateLteEmmTimerInfo(int i, ServiceState serviceState) {
        if (!isValidatePhoneId(i) || serviceState == null) {
            return;
        }
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mLteEmmTimerStartTime) / 1000;
        Utils.logD("MiuiTelephonyStatistic", "updateLteEmmTimerInfo" + rilDataRadioTechnology + jElapsedRealtime);
        if (this.mIsLteEmmTimerStart && !isLteNrNwType(rilDataRadioTechnology)) {
            this.mIsLteEmmTimerStartStatistic.put("rat_trigger", ServiceState.rilRadioTechnologyToString(rilDataRadioTechnology));
            this.mIsLteEmmTimerStartStatistic.put("time_trigger", String.valueOf(jElapsedRealtime));
            this.mIsLteEmmTimerStart = false;
        }
        if (this.mIsEmmTimerAirPlaneModeOn && isLteNrNwType(rilDataRadioTechnology)) {
            this.mIsLteEmmTimerStartStatistic.put("rat_recovery", ServiceState.rilRadioTechnologyToString(rilDataRadioTechnology));
            this.mIsLteEmmTimerStartStatistic.put("time_recovery", String.valueOf(jElapsedRealtime));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "lte_emm_timer_start_statistic", this.mIsLteEmmTimerStartStatistic);
            this.mIsEmmTimerAirPlaneModeOn = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordLteEmmTimerStartStatisticDelay() {
        Utils.logD("MiuiTelephonyStatistic", "recordLteEmmDelay" + this.mIsLteEmmTimerStart + this.mIsEmmTimerAirPlaneModeOn);
        if (this.mIsLteEmmTimerStart) {
            this.mIsLteEmmTimerStartStatistic.put("rat_trigger", "NA");
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "lte_emm_timer_start_statistic", this.mIsLteEmmTimerStartStatistic);
            this.mIsLteEmmTimerStart = false;
        } else if (this.mIsEmmTimerAirPlaneModeOn) {
            this.mIsLteEmmTimerStartStatistic.put("rat_recovery", "NA");
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "lte_emm_timer_start_statistic", this.mIsLteEmmTimerStartStatistic);
            this.mIsEmmTimerAirPlaneModeOn = false;
        }
    }

    public void recordLteEmmTimerAirPlaneOnStatistic() {
        ServiceState serviceState;
        this.mIsEmmTimerAirPlaneModeOn = true;
        int defaultDataSlotId = getDefaultDataSlotId();
        this.mLteEmmTimerStartTime = SystemClock.elapsedRealtime();
        if (isValidatePhoneId(defaultDataSlotId) && (serviceState = this.mServiceState[defaultDataSlotId]) != null && this.mIsLteEmmTimerStart) {
            this.mIsLteEmmTimerStartStatistic.put("rat_trigger", ServiceState.rilRadioTechnologyToString(serviceState.getRilDataRadioTechnology()));
            this.mIsLteEmmTimerStartStatistic.put("time_trigger", "NA");
            this.mIsLteEmmTimerStart = false;
        }
        Utils.logD("MiuiTelephonyStatistic", "recordLteEmmTimerAirPlaneOnStatistic");
    }

    public void recordLteEmmTimerStartStatistic() {
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!isValidatePhoneId(defaultDataSlotId) || this.mServiceState[defaultDataSlotId] == null || this.mIsLteEmmTimerStart) {
            return;
        }
        if (this.mStatisticHandler.hasMessages(90)) {
            this.mStatisticHandler.removeMessageWithCount(90);
        }
        MiuiTelephonyStatisticHandler miuiTelephonyStatisticHandler = this.mStatisticHandler;
        miuiTelephonyStatisticHandler.sendMessageDelayed(miuiTelephonyStatisticHandler.obtainMessage(90), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        this.mIsLteEmmTimerStartStatistic.clear();
        this.mIsLteEmmTimerStart = true;
        this.mLteEmmTimerStartTime = SystemClock.elapsedRealtime();
        Phone phone = MiuiPhoneUtils.getPhone(defaultDataSlotId);
        int rilDataRadioTechnology = this.mServiceState[defaultDataSlotId].getRilDataRadioTechnology();
        String signalQuality = getSignalQuality(this.mSignalStrength[defaultDataSlotId], rilDataRadioTechnology);
        String str = getDbm(this.mSignalStrength[defaultDataSlotId]) + "," + miui.telephony.TelephonyManager.getDefault().getMiuiLevel(this.mSignalStrength[defaultDataSlotId]);
        FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
        String province = "";
        String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
        if (fiveGCloudController != null) {
            province = fiveGCloudController.getProvince();
        }
        this.mIsLteEmmTimerStartStatistic.put("CellLocation", getCellLocation(phone));
        this.mIsLteEmmTimerStartStatistic.put("Network", getAvailableOperatorNumeric(null, defaultDataSlotId));
        this.mIsLteEmmTimerStartStatistic.put("RAT", ServiceState.rilRadioTechnologyToString(rilDataRadioTechnology));
        this.mIsLteEmmTimerStartStatistic.put("SignalStrength", str);
        this.mIsLteEmmTimerStartStatistic.put("SignalQuality", signalQuality);
        this.mIsLteEmmTimerStartStatistic.put("Band", Utils.getBand(defaultDataSlotId, this.mServiceState[defaultDataSlotId]));
        this.mIsLteEmmTimerStartStatistic.put("c", city);
        this.mIsLteEmmTimerStartStatistic.put("p", province);
        this.mIsLteEmmTimerStartStatistic.put("sceneType", getSceneType());
        Utils.logD("MiuiTelephonyStatistic", "recordLteEmmTimerStartStatistic");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNtnDataStatistic(int i, int i2, HashMap map) {
        TelephonyStatAdapter.DataStatType dataStatType = TelephonyStatAdapter.DataStatType.values()[i];
        ISatelliteModule iSatelliteModule = (ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class);
        Phone ntnPhone = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnPhone();
        String str = map != null ? (String) map.getOrDefault("apnName", "unknown") : "unknown";
        Utils.logD("MiuiTelephonyStatistic", "processNtnDataStatistic : dataStatType = " + i + ", apnName = " + str + ", ntnPhone = " + ntnPhone);
        if (TelephonyStatAdapter.DataStatType.DATA_SETUP_FAIL_CN.equals(dataStatType) || TelephonyStatAdapter.DataStatType.DATA_SETUP_OK_CN.equals(dataStatType)) {
            if (!"dcnet".equals(str) || iSatelliteModule == null || ntnPhone == null) {
                return;
            }
            String str2 = (String) map.getOrDefault("errorCode", "");
            String str3 = (String) map.getOrDefault("setupDuration", "");
            this.mStartUseNtnDataTime = SystemClock.elapsedRealtime();
            this.mNtnDataStatistic.put("ntn_data_act_realTxPwr", String.valueOf(iSatelliteModule.getRealTxPwr()));
            this.mNtnDataStatistic.put("ntn_data_act_tartxPwr", String.valueOf(iSatelliteModule.getTarTxPwr()));
            this.mNtnDataStatistic.put("ntn_data_act_rssi", String.valueOf(iSatelliteModule.getRetRssi()));
            this.mNtnDataStatistic.put("ntn_data_act_snr", String.valueOf(iSatelliteModule.getRetSnr()));
            this.mNtnDataStatistic.put("ntn_data_act_satid", String.valueOf(iSatelliteModule.getSatId()));
            this.mNtnDataStatistic.put("ntn_data_act_beamid", String.valueOf(iSatelliteModule.getNtnBandId()));
            this.mNtnDataStatistic.put("ntn_data_act_bandid", String.valueOf(iSatelliteModule.getNtnBeamId()));
            this.mNtnDataStatistic.put("ntn_data_error_code", str2);
            this.mNtnDataStatistic.put("ntn_data_set_up_duration", str3);
            return;
        }
        if (!TelephonyStatAdapter.DataStatType.DATA_INTERRUPTION_CAUSE.equals(dataStatType) || !"dcnet".equals(str) || iSatelliteModule == null || ntnPhone == null) {
            return;
        }
        String str4 = (String) map.getOrDefault("dataInterruptionCause", "");
        this.mNtnDataStatistic.put("ntn_data_use_duration", String.valueOf(SystemClock.elapsedRealtime() - this.mStartUseNtnDataTime));
        this.mNtnDataStatistic.put("ntn_data_interruption_cause", str4);
        this.mNtnDataStatistic.put("ntn_data_deact_realTxPwr", String.valueOf(iSatelliteModule.getRealTxPwr()));
        this.mNtnDataStatistic.put("ntn_data_deact_tartxPwr", String.valueOf(iSatelliteModule.getTarTxPwr()));
        this.mNtnDataStatistic.put("ntn_data_deact_rssi", String.valueOf(iSatelliteModule.getRetRssi()));
        this.mNtnDataStatistic.put("ntn_data_deact_snr", String.valueOf(iSatelliteModule.getRetSnr()));
        this.mNtnDataStatistic.put("ntn_data_deact_satid", String.valueOf(iSatelliteModule.getSatId()));
        this.mNtnDataStatistic.put("ntn_data_deact_beamid", String.valueOf(iSatelliteModule.getNtnBandId()));
        this.mNtnDataStatistic.put("ntn_data_deact_bandid", String.valueOf(iSatelliteModule.getNtnBeamId()));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntn_data", new HashMap(this.mNtnDataStatistic));
        this.mNtnDataStatistic.clear();
    }

    private boolean isLteNrNwType(int i) {
        return isLteNwType(i) || isNrNwType(i);
    }

    private void updatePoiParams(Map map) {
        if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isInPoi()) {
            map.put("current_poi_id", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentPoiId());
            map.put("current_poi_name", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentPoiName());
        } else {
            map.put("last_poi_id", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getLastPoiId());
            map.put("last_poi_name", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getLastPoiName());
        }
        map.put("hst_line", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentHitLine());
    }

    private void reportQmiInfo() {
        Utils.logI("MiuiTelephonyStatistic", "reportQmiInfo enter");
        RilHook rilHookInit = RilHook.init(this.mContext);
        if (rilHookInit == null) {
            Utils.logE("MiuiTelephonyStatistic", "reportQmiInfo rilhook is null");
            return;
        }
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        byte b = -1;
        int i = 0;
        while (true) {
            ByteBuffer qmiIndInfo = rilHookInit.getQmiIndInfo();
            if (qmiIndInfo == null || i >= 50) {
                break;
            }
            b = qmiIndInfo.get();
            short s = qmiIndInfo.getShort();
            if (s <= 0) {
                break;
            }
            for (int i2 = 0; i2 < s; i2++) {
                HashMap map2 = new HashMap();
                map2.put("service_id", Integer.valueOf(qmiIndInfo.getInt()));
                map2.put("msg_id", Integer.valueOf(qmiIndInfo.getInt()));
                map2.put("sleep_on", Byte.valueOf(qmiIndInfo.get()));
                map2.put("screen_on", Byte.valueOf(qmiIndInfo.get()));
                map2.put("night_time", Byte.valueOf(qmiIndInfo.get()));
                map2.put("config_type", Byte.valueOf(qmiIndInfo.get()));
                qmiIndInfo.getInt();
                map2.put("qmi_ind_count", Integer.valueOf(qmiIndInfo.getInt()));
                arrayList.add(map2);
            }
            i++;
        }
        Utils.logI("MiuiTelephonyStatistic", "reportQmiInfo: info size = " + arrayList.size() + ", version = " + ((int) b));
        if (arrayList.size() > 0) {
            map.put("version", Integer.valueOf(b));
            map.put("data", new JSONArray((Collection) arrayList).toString());
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "qmi_ind_info", map);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean shouldProcessHstRachStats() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_hst_rach", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void accumulateHstRachDiff(ArrayList arrayList, ArrayList arrayList2) {
        MiuiTelephonyStatistic miuiTelephonyStatistic;
        if (arrayList == null || arrayList2 == null) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) obj;
            int iSafeToIntFromShort = this.safeToIntFromShort(concurrentHashMap.get("mcc"));
            int iSafeToIntFromShort2 = this.safeToIntFromShort(concurrentHashMap.get("mnc"));
            ConcurrentHashMap concurrentHashMapFindRachData = this.findRachData(arrayList2, iSafeToIntFromShort, iSafeToIntFromShort2);
            if (concurrentHashMapFindRachData != null) {
                miuiTelephonyStatistic = this;
                miuiTelephonyStatistic.accumulateToCumulativeDiff(iSafeToIntFromShort, iSafeToIntFromShort2, this.safeToIntFromShort(concurrentHashMap.get("rachTiggerNum")) - this.safeToIntFromShort(concurrentHashMapFindRachData.get("rachTiggerNum")), this.safeToIntFromShort(concurrentHashMap.get("rachOkNum")) - this.safeToIntFromShort(concurrentHashMapFindRachData.get("rachOkNum")), this.safeToIntFromShort(concurrentHashMap.get("rachFailNum")) - this.safeToIntFromShort(concurrentHashMapFindRachData.get("rachFailNum")), this.safeToIntFromShort(concurrentHashMap.get("rachUnknowNum")) - this.safeToIntFromShort(concurrentHashMapFindRachData.get("rachUnknowNum")));
            } else {
                miuiTelephonyStatistic = this;
            }
            this = miuiTelephonyStatistic;
        }
    }

    private void accumulateToCumulativeDiff(int i, int i2, int i3, int i4, int i5, int i6) {
        ConcurrentHashMap concurrentHashMapFindRachData = findRachData(this.mCumulativeHstRachDiff, i, i2);
        if (concurrentHashMapFindRachData != null) {
            concurrentHashMapFindRachData.put("rachTiggerNum", Integer.valueOf(safeToIntFromShort(concurrentHashMapFindRachData.get("rachTiggerNum")) + i3));
            concurrentHashMapFindRachData.put("rachOkNum", Integer.valueOf(safeToIntFromShort(concurrentHashMapFindRachData.get("rachOkNum")) + i4));
            concurrentHashMapFindRachData.put("rachFailNum", Integer.valueOf(safeToIntFromShort(concurrentHashMapFindRachData.get("rachFailNum")) + i5));
            concurrentHashMapFindRachData.put("rachUnknowNum", Integer.valueOf(safeToIntFromShort(concurrentHashMapFindRachData.get("rachUnknowNum")) + i6));
            return;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("mcc", Integer.valueOf(i));
        concurrentHashMap.put("mnc", Integer.valueOf(i2));
        concurrentHashMap.put("rachTiggerNum", Integer.valueOf(i3));
        concurrentHashMap.put("rachOkNum", Integer.valueOf(i4));
        concurrentHashMap.put("rachFailNum", Integer.valueOf(i5));
        concurrentHashMap.put("rachUnknowNum", Integer.valueOf(i6));
        this.mCumulativeHstRachDiff.add(concurrentHashMap);
    }

    private ArrayList createZeroRachData(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) obj;
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("mcc", Integer.valueOf(safeToIntFromShort(concurrentHashMap.get("mcc"))));
            concurrentHashMap2.put("mnc", Integer.valueOf(safeToIntFromShort(concurrentHashMap.get("mnc"))));
            concurrentHashMap2.put("rachTiggerNum", 0);
            concurrentHashMap2.put("rachOkNum", 0);
            concurrentHashMap2.put("rachFailNum", 0);
            concurrentHashMap2.put("rachUnknowNum", 0);
            arrayList2.add(concurrentHashMap2);
        }
        return arrayList2;
    }

    private ConcurrentHashMap findRachData(ArrayList arrayList, int i, int i2) {
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i3 = 0;
        while (i3 < size) {
            Object obj = arrayList.get(i3);
            i3++;
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) obj;
            if (i == safeToIntFromShort(concurrentHashMap.get("mcc")) && i2 == safeToIntFromShort(concurrentHashMap.get("mnc"))) {
                return concurrentHashMap;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordHstRachStats() {
        ArrayList arrayList = this.mCumulativeHstRachDiff;
        if (arrayList == null || arrayList.isEmpty()) {
            Utils.logE("MiuiTelephonyStatistic", "recordHstRachStats: No cumulative RACH data to report");
            return;
        }
        try {
            if (!this.mCumulativeHstRachDiff.isEmpty()) {
                HashMap map = new HashMap();
                map.put("Params", this.mCumulativeHstRachDiff);
                if (!map.isEmpty() && map.size() <= 50) {
                    MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hst_rach_statistic", map);
                    return;
                }
                Utils.logE("MiuiTelephonyStatistic", "recordHstRachStats invalid parameters!!!");
                return;
            }
            Utils.logE("MiuiTelephonyStatistic", "No HST RACH Stats to report");
        } catch (Exception e) {
            Utils.logE("MiuiTelephonyStatistic", "Error recording HST RACH stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetHstRachData() {
        this.mEnterHstRachData = null;
        this.mExitHstRachData = null;
        this.mCumulativeHstRachDiff.clear();
        Utils.logD("MiuiTelephonyStatistic", "HST RACH data reset");
    }

    private int safeToIntFromShort(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).intValue();
        }
        return 0;
    }
}
