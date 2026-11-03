package com.android.phone.dsda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkMetricsController;
import com.android.phone.NetworkModeRecognition;
import com.android.phone.PhoneGlobals;
import com.android.phone.QmsClientBase;
import com.android.phone.QmsMsgTunnelControllerBase;
import com.android.phone.R;
import com.android.phone.RilHook;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.statistics.utils.Utils;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.b.a;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDualDataController {
    private static MiuiDualDataController sInstance;
    ContentObserver mAppDualDataEnableObserver;
    private boolean mAppDualDataEnabled;
    private String[] mBand;
    private int mBatteryLevel;
    private LinkedList mCellQueue;
    private volatile boolean mConcurrentLimitedWhiteListForground;
    private volatile boolean mConcurrentWhiteListForground;
    private final Context mContext;
    ContentObserver mDataEnabledObserver;
    private boolean mDppdCapability;
    private QmsClientBase.IDppdInterfaceListener mDppdInterfaceListener;
    private int mDppdSubMode;
    private int mDppdTxconcurrency;
    private List mDualDataConcurrentLimitedAppWhiteList;
    private List mDualDataConcurrentModeAppWhiteList;
    ContentObserver mDualDataEnabledObserver;
    private List mDualDataRedundantModeAppWhiteList;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private GameUpdateListener mGameUpdateListener;
    private long mGameUpdateStartTime;
    private MiuiDualDataHandler mHandler;
    private Set mHstDualDataPromoteApps;
    private SceneAwareCallback mHstStationCallback;
    private final BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsGameUpdating;
    private boolean mIsHstMode;
    private boolean mIsNeedDelay;
    private boolean mIsSensorStaticStatus;
    private boolean mIsStaticStatus;
    private boolean mIsWifiConnected;
    private Long mLastCellId;
    private long mLastStaticStatusTime;
    private int mLastSubwayState;
    private int mLinkTurboMode;
    private boolean mLowBatteryStatus;
    private int[] mLteRsrp;
    private int[] mLteSnr;
    private boolean mMasterWifiStatus;
    private MiuiDualDataTracker[] mMiuiDualDataTracker;
    private MiuiDualMobileTrafficStats[] mMobileTrafficStats;
    private boolean mNddsCapability;
    private NetworkModeRecognition mNetworkModeRecognition;
    private int[] mNrRsrp;
    private int[] mNrSnr;
    private ValidStatus[] mPhoneValidStatus;
    private Phone[] mPhones;
    private int[] mRat;
    private volatile boolean mRedundantWhiteListForground;
    private RilHook mRilHook;
    private String[] mSaBandCombsLimit;
    private String[] mSaBandCombsLimitless;
    private long mSensorStaticStatusTime;
    private ServiceState[] mServiceState;
    private SettingsObserver mSettingsObserver;
    private boolean mSlaveWifiStatus;
    ContentObserver mSlaveWifiStatusObserver;
    private boolean mSlmEnabled;
    ContentObserver mSubwayStateObserver;
    private TelephonyInfoCallBack mTelephonyInfoCallBack;
    private ArrayList mThermalList;
    private boolean mThermalStatus;
    private boolean mUserDataEnabled;
    private boolean mUserDualDataEnabled;
    private WifiManager mWifiManager;
    private final HandlerThread mWorkThread;
    private static final HashMap sLocalLogs = new HashMap();
    private static int mRetryCount = 0;

    private int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    private boolean isLteNetworkType(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrNetworkType(int i) {
        return i == 20 || i == 30;
    }

    public static synchronized MiuiDualDataController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiuiDualDataController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initValue() {
        log("DualDataController: init");
        this.mPhones = PhoneFactory.getPhones();
        this.mUserDualDataEnabled = isDualMobileDataSettingOn();
        this.mUserDataEnabled = isMobileDataSettingOn();
        this.mLinkTurboMode = getLinkTurboSetting();
        this.mSaBandCombsLimit = this.mContext.getResources().getStringArray(R.array.disable_sa_sa_combination_dppd);
        this.mSaBandCombsLimitless = this.mContext.getResources().getStringArray(R.array.enable_sa_sa_combination_dppd);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        for (Phone phone : this.mPhones) {
            if (phone != null) {
                this.mMiuiDualDataTracker[phone.getPhoneId()] = new MiuiDualDataTracker(this.mContext, phone);
                this.mMobileTrafficStats[phone.getPhoneId()] = new MiuiDualMobileTrafficStats(phone);
                this.mServiceState[phone.getPhoneId()] = new ServiceState();
                this.mNrRsrp[phone.getPhoneId()] = Integer.MAX_VALUE;
                this.mLteRsrp[phone.getPhoneId()] = Integer.MAX_VALUE;
                this.mNrSnr[phone.getPhoneId()] = Integer.MAX_VALUE;
                this.mLteSnr[phone.getPhoneId()] = Integer.MAX_VALUE;
                this.mRat[phone.getPhoneId()] = 0;
                this.mBand[phone.getPhoneId()] = "-1";
                this.mPhoneValidStatus[phone.getPhoneId()] = new ValidStatus(this);
            }
        }
        initDualDataConcurrentModeWhiteList();
        initDualDataConcurrentLimitedWhiteList();
        initDualDataRedundantModeWhiteList();
        if (this.mUserDualDataEnabled) {
            this.mHandler.sendEmptyMessage(1029);
        }
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
    }

    private MiuiDualDataController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mNrRsrp = new int[i];
        this.mLteRsrp = new int[i];
        this.mNrSnr = new int[i];
        this.mLteSnr = new int[i];
        this.mRedundantWhiteListForground = false;
        this.mConcurrentWhiteListForground = false;
        this.mConcurrentLimitedWhiteListForground = false;
        this.mThermalList = new ArrayList();
        this.mBatteryLevel = 0;
        this.mLowBatteryStatus = false;
        this.mThermalStatus = false;
        this.mServiceState = new ServiceState[i];
        this.mBand = new String[i];
        this.mRat = new int[i];
        this.mPhoneValidStatus = new ValidStatus[i];
        this.mUserDataEnabled = false;
        this.mUserDualDataEnabled = false;
        this.mAppDualDataEnabled = false;
        this.mNddsCapability = false;
        this.mSlaveWifiStatus = false;
        this.mMasterWifiStatus = false;
        this.mWifiManager = null;
        this.mMiuiDualDataTracker = new MiuiDualDataTracker[i];
        this.mMobileTrafficStats = new MiuiDualMobileTrafficStats[i];
        this.mDualDataConcurrentModeAppWhiteList = new ArrayList();
        this.mDualDataConcurrentLimitedAppWhiteList = new ArrayList();
        this.mDualDataRedundantModeAppWhiteList = new ArrayList();
        this.mTelephonyInfoCallBack = null;
        this.mIsWifiConnected = false;
        this.mSlmEnabled = false;
        this.mLinkTurboMode = 0;
        this.mNetworkModeRecognition = null;
        this.mLastSubwayState = 0;
        this.mIsSensorStaticStatus = false;
        this.mIsStaticStatus = false;
        this.mCellQueue = new LinkedList();
        this.mLastCellId = Long.MAX_VALUE;
        this.mSensorStaticStatusTime = -1L;
        this.mLastStaticStatusTime = -1L;
        this.mIsNeedDelay = false;
        this.mSaBandCombsLimit = null;
        this.mSaBandCombsLimitless = null;
        this.mDppdCapability = false;
        this.mIsHstMode = false;
        this.mHstDualDataPromoteApps = new HashSet();
        this.mIsGameUpdating = false;
        this.mGameUpdateStartTime = 0L;
        this.mGameUpdateListener = null;
        this.mDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsMobileDataSettingOn = MiuiDualDataController.this.isMobileDataSettingOn();
                if (MiuiDualDataController.this.mUserDataEnabled != zIsMobileDataSettingOn) {
                    MiuiDualDataController.this.mUserDataEnabled = zIsMobileDataSettingOn;
                    boolean z2 = MiuiDualDataController.this.mUserDataEnabled;
                    MiuiDualDataController miuiDualDataController = MiuiDualDataController.this;
                    if (z2) {
                        miuiDualDataController.mHandler.sendMessageDelayed(MiuiDualDataController.this.mHandler.obtainMessage(1022), 2000L);
                    } else {
                        miuiDualDataController.mHandler.obtainMessage(1022).sendToTarget();
                    }
                }
            }
        };
        this.mSubwayStateObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                int subwayState = MiuiDualDataController.this.getSubwayState();
                if (subwayState != -1) {
                    MiuiDualDataController.this.mHandler.obtainMessage(1032, Integer.valueOf(subwayState)).sendToTarget();
                }
            }
        };
        this.mDualDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsDualMobileDataSettingOn = MiuiDualDataController.this.isDualMobileDataSettingOn();
                MiuiDualDataController.log("curUserDualDataEnabled = " + zIsDualMobileDataSettingOn + ", mUserDualDataEnabled = " + MiuiDualDataController.this.mUserDualDataEnabled);
                if (MiuiDualDataController.this.mUserDualDataEnabled != zIsDualMobileDataSettingOn) {
                    MiuiDualDataController.this.mUserDualDataEnabled = zIsDualMobileDataSettingOn;
                    MiuiDualDataController.this.mHandler.obtainMessage(1012).sendToTarget();
                    MiuiDualDataController miuiDualDataController = MiuiDualDataController.this;
                    miuiDualDataController.updateDppdUiOption(miuiDualDataController.mUserDualDataEnabled);
                    if (MiuiDualDataController.this.mUserDualDataEnabled) {
                        MiuiDualDataController.log("begin check thermal!");
                        MiuiDualDataController.this.mHandler.sendEmptyMessage(1029);
                    } else if (MiuiDualDataController.this.mHandler.hasMessages(1029)) {
                        MiuiDualDataController.log("stop check thermal!");
                        MiuiDualDataController.this.mHandler.removeMessages(1029);
                    }
                }
            }
        };
        this.mAppDualDataEnableObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsAppDualDataEnableStateOn = MiuiDualDataController.this.isAppDualDataEnableStateOn();
                MiuiDualDataController.log("curAppDualDataEnabled = " + zIsAppDualDataEnableStateOn + ", mAppDualDataEnabled = " + MiuiDualDataController.this.mAppDualDataEnabled);
                if (MiuiDualDataController.this.mAppDualDataEnabled != zIsAppDualDataEnableStateOn) {
                    MiuiDualDataController.this.mAppDualDataEnabled = zIsAppDualDataEnableStateOn;
                    int defaultSlotId = MiuiDualDataController.getDefaultSlotId();
                    int viceSlotId = MiuiDualDataController.getViceSlotId();
                    if (MiuiDualDataController.this.isValidatePhoneId(defaultSlotId) && MiuiDualDataController.isSlotActivated(defaultSlotId) && MiuiDualDataController.this.isValidatePhoneId(viceSlotId) && MiuiDualDataController.isSlotActivated(viceSlotId)) {
                        if (!MiuiDualDataController.this.mAppDualDataEnabled || MiuiDualDataController.this.getCommonSignalPolicyState(defaultSlotId, viceSlotId) || MiuiDualDataController.this.mLastSubwayState == 1) {
                            if (MiuiDualDataController.this.mHandler.hasMessages(1020)) {
                                MiuiDualDataController.this.mHandler.removeMessages(1020);
                            }
                            MiuiDualDataController.this.mHandler.sendMessageDelayed(MiuiDualDataController.this.mHandler.obtainMessage(1020), 2000L);
                        }
                    }
                }
            }
        };
        this.mSlaveWifiStatusObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsSlaveWifiConnected = MiuiDualDataController.this.isSlaveWifiConnected();
                if (MiuiDualDataController.this.mSlaveWifiStatus != zIsSlaveWifiConnected) {
                    MiuiDualDataController.this.mSlaveWifiStatus = zIsSlaveWifiConnected;
                    MiuiDualDataController.this.mHandler.obtainMessage(1028).sendToTarget();
                }
            }
        };
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.dsda.MiuiDualDataController.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                MiuiDualDataController.log("IntentReceiver action = " + intent.getAction());
                if (action.equals("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED") && !MiuiPhoneUtils.isLicenseDualDataSupported()) {
                    int i2 = SystemProperties.getInt("ril.multisim.voice_capability", 0);
                    MiuiDualDataController.log("BroadcastReceiver dsda capability:" + i2);
                    MiuiDualDataController.this.mNddsCapability = 3 == i2;
                    MiuiDualDataController.this.mHandler.obtainMessage(1011).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                    MiuiDualDataController.log("ACTION_DEFAULT_DATA_SLOT_CHANGED or ACTION_DEFAULT_DATA_SLOT_READY");
                    MiuiDualDataController.this.mHandler.sendMessageDelayed(MiuiDualDataController.this.mHandler.obtainMessage(1013), 2000L);
                    for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                        MiuiDualDataController.this.mMobileTrafficStats[i3].updateInterfaceName();
                        MiuiDualDataController.this.mMobileTrafficStats[i3].updateDefaultDataSlotId();
                    }
                    return;
                }
                if (action.equals("com.android.phone.intent.action.DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DONE")) {
                    MiuiDualDataController.log("ACTION_DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DONE RECEIVED");
                    MiuiDualDataController.this.mHandler.obtainMessage(1014).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.DUAL_DATA_CONCURRENT_LIMITED_WHITE_LIST_DONE")) {
                    MiuiDualDataController.log("ACTION_DUAL_DATA_CONCURRENT_LIMITED_WHITE_LIST_DONE RECEIVED");
                    MiuiDualDataController.this.mHandler.obtainMessage(1030).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DONE")) {
                    MiuiDualDataController.log("ACTION_DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DONE RECEIVED");
                    MiuiDualDataController.this.mHandler.obtainMessage(1021).sendToTarget();
                    return;
                }
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo == null || networkInfo.getType() != 1) {
                        return;
                    }
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !MiuiDualDataController.this.mIsWifiConnected) {
                        MiuiDualDataController.this.mIsWifiConnected = true;
                        MiuiDualDataController.log("CONNECTIVITY_ACTION: WIFI is CONNECTED");
                        MiuiDualDataController.this.mHandler.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT).sendToTarget();
                        return;
                    } else {
                        if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && MiuiDualDataController.this.mIsWifiConnected) {
                            MiuiDualDataController.this.mIsWifiConnected = false;
                            MiuiDualDataController.log("CONNECTIVITY_ACTION: WIFI is DISCONNECTED");
                            MiuiDualDataController.this.mHandler.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT).sendToTarget();
                            return;
                        }
                        return;
                    }
                }
                if ("com.android.phone.intent.action.SLM_STATUS".equals(action)) {
                    MiuiDualDataController.this.mHandler.obtainMessage(1025, intent).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                    MiuiDualDataController.this.mHandler.obtainMessage(1026, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.GET_SUBMODE_INFO")) {
                    MiuiDualDataController.this.getMultiSimSubmodeInfo();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.GET_DPPD_CAPABILITY")) {
                    MiuiDualDataController.this.getDppdCapability();
                } else if (action.equals("com.android.phone.intent.action.GET_DPPD_RECOMMENDATION")) {
                    MiuiDualDataController.this.getDppdRecommendation();
                } else if (action.equals("com.android.phone.intent.action.UPDATE_DPPD_UI_OPTION")) {
                    MiuiDualDataController.this.updateDppdUiOption(true);
                }
            }
        };
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.dsda.MiuiDualDataController.8
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                    return;
                }
                MiuiDualDataController.this.mHandler.obtainMessage(1015, foregroundInfo).sendToTarget();
            }
        };
        this.mDppdInterfaceListener = new QmsClientBase.IDppdInterfaceListener() { // from class: com.android.phone.dsda.MiuiDualDataController.9
            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onUiOptionStatusChanged(int i2, boolean z) {
                MiuiDualDataController.log("ui result status = " + z);
                if (!z && MiuiDualDataController.mRetryCount < 3) {
                    MiuiDualDataController.mRetryCount++;
                    MiuiDualDataController.log("retry update ui option count = " + MiuiDualDataController.mRetryCount);
                    if (MiuiDualDataController.this.mHandler.hasMessages(1031)) {
                        MiuiDualDataController.this.mHandler.removeMessages(1031);
                    }
                    MiuiDualDataController.this.mHandler.sendEmptyMessageDelayed(1031, 2000L);
                    return;
                }
                if (z || MiuiDualDataController.mRetryCount < 3) {
                    return;
                }
                MiuiDualDataController.log("update ui option error close dual data");
                Settings.Global.putInt(MiuiDualDataController.this.mContext.getContentResolver(), "dual_mobile_data", 0);
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdStatusCapabilityChanged(int i2, boolean z) {
                MiuiDualDataController.log("dppd status status = " + i2 + ", capability = " + z);
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdStatusRecommendationChanged(int i2, int i3, int i4) {
                MiuiDualDataController.log("dppd status status = " + i2 + ", action = " + i3 + ", subscription = " + i4);
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdCapabilityChanged(boolean z) {
                MiuiDualDataController.log("dppd capability = " + z);
                MiuiDualDataController.this.mDppdCapability = z;
                if (z) {
                    MiuiDualDataController miuiDualDataController = MiuiDualDataController.this;
                    miuiDualDataController.updateDppdUiOption(miuiDualDataController.mUserDualDataEnabled);
                    MiuiDualDataController.this.getMultiSimSubmodeInfo();
                }
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdRecommendationChanged(int i2, int i3) {
                MiuiDualDataController.log("dppd action = " + i2 + ", subscription = " + i3);
                boolean z = MiuiDualDataController.this.mNddsCapability;
                if (i3 == 2) {
                    z = true;
                    if (i2 != 1) {
                        z = false;
                    }
                }
                if (z != MiuiDualDataController.this.mNddsCapability) {
                    MiuiDualDataController.this.mNddsCapability = z;
                    MiuiDualDataController.this.mHandler.obtainMessage(1011).sendToTarget();
                }
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdSubmodeInfoChanged(int i2, int i3) {
                MiuiDualDataController.log("dppd submode = " + i2 + ", txconcurrency = " + i3);
                if (i2 == MiuiDualDataController.this.mDppdSubMode && i3 == MiuiDualDataController.this.mDppdTxconcurrency) {
                    return;
                }
                MiuiDualDataController.this.mDppdSubMode = i2;
                MiuiDualDataController.this.mDppdTxconcurrency = i3;
                if (MiuiDualDataController.this.mHandler.hasMessages(1037)) {
                    MiuiDualDataController.this.mHandler.removeMessages(1037);
                }
                MiuiDualDataController.this.mHandler.sendMessageDelayed(MiuiDualDataController.this.mHandler.obtainMessage(1037), 2000L);
            }

            @Override // com.android.phone.QmsClientBase.IDppdInterfaceListener
            public void onDppdStatusSubmodeInfoChanged(int i2, int i3, int i4) {
                MiuiDualDataController.log("dppd status = " + i2 + ", submode = " + i3 + ", txconcurrency = " + i4);
                MiuiDualDataController.this.mDppdSubMode = i3;
                MiuiDualDataController.this.mDppdTxconcurrency = i4;
            }
        };
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        HandlerThread handlerThread = new HandlerThread("MiuiDualDataController", 10);
        this.mWorkThread = handlerThread;
        this.mRilHook = RilHook.init(phoneGlobals);
        handlerThread.start();
        MiuiDualDataHandler miuiDualDataHandler = new MiuiDualDataHandler(handlerThread.getLooper());
        this.mHandler = miuiDualDataHandler;
        miuiDualDataHandler.obtainMessage(1010).sendToTarget();
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int i, Map map) {
            MiuiDualDataController.this.mHandler.obtainMessage(1038, dataStatType.ordinal(), i, map).sendToTarget();
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            MiuiDualDataController.this.mHandler.obtainMessage(1018, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            MiuiDualDataController.this.mHandler.obtainMessage(1017, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            MiuiDualDataController.this.mHandler.obtainMessage(1034, i, 0, cellLocation).sendToTarget();
        }
    }

    class GameUpdateListener implements NetworkMetricsController.GameDelayListener {
        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(boolean z) {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameDelayChanged(int i) {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(int i, String str) {
        }

        GameUpdateListener() {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameUpdateStatusChanged(boolean z) {
            MiuiDualDataController.this.mHandler.obtainMessage(1039, Boolean.valueOf(z)).sendToTarget();
        }
    }

    class MiuiDualDataHandler extends Handler {
        public MiuiDualDataHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            switch (message.what) {
                case 1010:
                    MiuiDualDataController.this.initValue();
                    MiuiDualDataController.this.registerAllEvents();
                    return;
                case 1011:
                case 1012:
                case 1022:
                case SmartNetworkRelayScore.TRAFFIC_UNIT /* 1024 */:
                case 1037:
                    break;
                case 1013:
                    int viceSlotId = MiuiDualDataController.getViceSlotId();
                    if (!MiuiDualDataController.this.isValidatePhoneId(viceSlotId) || !MiuiDualDataController.isSlotActivated(viceSlotId)) {
                        return;
                    } else {
                        MiuiDualDataController.this.releaseViceNetworkByPolicy(5000, viceSlotId);
                    }
                    break;
                case 1014:
                    MiuiDualDataController.this.updateDualDataConcurrentModeAppWhiteList();
                    return;
                case 1015:
                    MiuiDualDataController.this.processForegroundInfoEvent((ForegroundInfo) message.obj);
                    return;
                case 1016:
                    MiuiDualDataController.this.processWhiteAppListForegroundStateChanged();
                    return;
                case 1017:
                    MiuiDualDataController.this.processServiceStateChanged(message.arg1, (ServiceState) message.obj);
                    return;
                case 1018:
                    MiuiDualDataController.this.processSignalStrengthChanged(message.arg1, (SignalStrength) message.obj);
                    return;
                case 1019:
                    MiuiDualDataController.this.updateViceNetworkByPolicy(((Integer) message.obj).intValue(), MiuiDualDataController.getDefaultSlotId(), MiuiDualDataController.getViceSlotId());
                    return;
                case 1020:
                    int defaultSlotId = MiuiDualDataController.getDefaultSlotId();
                    int viceSlotId2 = MiuiDualDataController.getViceSlotId();
                    if (MiuiDualDataController.this.isValidatePhoneId(defaultSlotId) && MiuiDualDataController.isSlotActivated(defaultSlotId) && MiuiDualDataController.this.isValidatePhoneId(viceSlotId2) && MiuiDualDataController.isSlotActivated(viceSlotId2)) {
                        if (!MiuiDualDataController.this.mAppDualDataEnabled || MiuiDualDataController.this.getCommonSignalPolicyState(defaultSlotId, viceSlotId2) || MiuiDualDataController.this.mLastSubwayState == 1) {
                            MiuiDualDataController.this.updateViceNetworkByPolicy(5004, defaultSlotId, viceSlotId2);
                            return;
                        }
                        return;
                    }
                    return;
                case 1021:
                    MiuiDualDataController.this.updateDualDataRedundantModeAppWhiteList();
                    return;
                case 1023:
                    MiuiDualDataController.this.processReleaseViceNetwork(((Integer) message.obj).intValue());
                    return;
                case 1025:
                    Intent intent = (Intent) message.obj;
                    MiuiDualDataController.this.mSlmEnabled = intent.getIntExtra("coexistense", -1) == 1;
                    MiuiDualDataController.this.updateViceNetworkByPolicy(5000, MiuiDualDataController.getDefaultSlotId(), MiuiDualDataController.getViceSlotId());
                    MiuiDualDataController.log("EVENT_SLM_STATUS_CHANGED status " + MiuiDualDataController.this.mSlmEnabled);
                    MiuiDualDataController.this.processNetworkBoostStatusChanged(1025, intent);
                    return;
                case 1026:
                    MiuiDualDataController.this.processBatteryLevelChanged(((Intent) message.obj).getIntExtra(a.d, 0));
                    return;
                case 1027:
                    MiuiDualDataController.this.processNetworkBoostStatusChanged(1027, null);
                    return;
                case 1028:
                    MiuiDualDataController.this.processNetworkBoostStatusChanged(1028, null);
                    return;
                case 1029:
                    MiuiDualDataController.this.checkThermal();
                    if (MiuiDualDataController.this.mHandler.hasMessages(1029)) {
                        return;
                    }
                    MiuiDualDataController.this.mHandler.sendEmptyMessageDelayed(1029, 15000L);
                    return;
                case 1030:
                    MiuiDualDataController.this.updateDualDataConcurrentLimitedWhiteList();
                    return;
                case 1031:
                    MiuiDualDataController miuiDualDataController = MiuiDualDataController.this;
                    miuiDualDataController.updateDppdUiOption(miuiDualDataController.mUserDualDataEnabled);
                    return;
                case 1032:
                    int iIntValue = ((Integer) message.obj).intValue();
                    if (MiuiDualDataController.this.mRedundantWhiteListForground && !MiuiDualDataController.this.isDualDataConnected() && iIntValue == 1) {
                        MiuiDualDataController.this.updateViceNetworkByPolicy(5000, MiuiDualDataController.getDefaultSlotId(), MiuiDualDataController.getViceSlotId());
                    }
                    if (MiuiDualDataController.this.mRedundantWhiteListForground && MiuiDualDataController.this.isDualDataConnected() && iIntValue == 0) {
                        int viceSlotId3 = MiuiDualDataController.getViceSlotId();
                        if (MiuiDualDataController.this.isValidatePhoneId(viceSlotId3) && MiuiDualDataController.isSlotActivated(viceSlotId3)) {
                            MiuiDualDataController.this.releaseViceNetworkByPolicy(5000, viceSlotId3);
                            return;
                        }
                        return;
                    }
                    return;
                case 1033:
                    MiuiDualDataController.this.processSensorExtenMoveStateChanged();
                    return;
                case 1034:
                    MiuiDualDataController.this.processCellLocationChanged(message.arg1, (CellLocation) message.obj);
                    return;
                case 1035:
                    MiuiDualDataController.this.processStaticStateDurationLinger();
                    return;
                case 1036:
                    MiuiDualDataController.this.processSensorChangeStaticLinger();
                    return;
                case 1038:
                    MiuiDualDataController.this.processDataStatisticChange(message.arg1, message.arg2, (HashMap) message.obj);
                    return;
                case 1039:
                    MiuiDualDataController.this.processGameUpdateStatusChanged(((Boolean) message.obj).booleanValue());
                    return;
                case 1040:
                    MiuiDualDataController.log("game update timeout, force exit");
                    MiuiDualDataController.this.processGameUpdateStatusChanged(false);
                    return;
                default:
                    return;
            }
            MiuiDualDataController.this.updateViceNetworkByPolicy(5000, MiuiDualDataController.getDefaultSlotId(), MiuiDualDataController.getViceSlotId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        intentFilter.addAction("com.android.phone.intent.action.DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.DUAL_DATA_CONCURRENT_LIMITED_WHITE_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DONE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.SLM_STATUS");
        intentFilter.addAction("com.android.phone.intent.action.GET_SUBMODE_INFO");
        intentFilter.addAction("com.android.phone.intent.action.GET_DPPD_CAPABILITY");
        intentFilter.addAction("com.android.phone.intent.action.GET_DPPD_RECOMMENDATION");
        intentFilter.addAction("com.android.phone.intent.action.UPDATE_DPPD_UI_OPTION");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, null, this.mHandler, 2);
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED"), "com.qti.permission.RECEIVE_MSIM_VOICE_CAPABILITY_CHANGED", this.mHandler, 2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(getMobileDataSettingName()), false, this.mDataEnabledObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("dual_mobile_data"), false, this.mDualDataEnabledObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("application_dual_mobile_data"), false, this.mAppDualDataEnableObserver);
        registerTelephonyInfoCallBack();
        registerForegroundInfoListener();
        registerDppdInterfaceCallback();
        initSLMUIObserver();
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("slave_wifi_ssid"), false, this.mSlaveWifiStatusObserver);
        registerSettingsCallbacks();
        registerGameUpdateListener();
        registerHstStationCallback();
    }

    private void registerGameUpdateListener() {
        if (this.mGameUpdateListener == null) {
            this.mGameUpdateListener = new GameUpdateListener();
            NetworkMetricsController.getInstance().registerGameDelayListener(this.mGameUpdateListener);
        }
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTelephonyInfoCallBack == null) {
            log("new TelephonyInfoCallBack");
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            this.mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    private void registerSettingsCallbacks() {
        SettingsObserver settingsObserver = new SettingsObserver(this.mContext, this.mHandler);
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe(Settings.Global.getUriFor("sensor_state_extent_move"), 1033);
    }

    private int getSensorExtenMoveState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_extent_move", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSensorExtenMoveStateChanged() {
        int sensorExtenMoveState = getSensorExtenMoveState();
        log("processSensorExtenMoveStateChanged: sensorState=" + sensorExtenMoveState);
        if (sensorExtenMoveState == 2) {
            this.mIsSensorStaticStatus = true;
            this.mSensorStaticStatusTime = SystemClock.elapsedRealtime();
            startMonitorTimerForSensorStatic();
        } else {
            this.mSensorStaticStatusTime = -1L;
            this.mIsSensorStaticStatus = false;
            if (this.mIsStaticStatus) {
                setStaticState(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSensorChangeStaticLinger() {
        log("processSensorChangeStaticLinger, sensorS=" + this.mIsSensorStaticStatus + ", S=" + this.mIsStaticStatus);
        updateMoveStateAndProcessChangeIfNeed(SystemClock.elapsedRealtime());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataStatisticChange(int i, int i2, HashMap map) {
        if (!isValidatePhoneId(i2)) {
            log("invalid phoneId: " + i2);
            return;
        }
        log("processDataStatisticChange dataType=" + i + ", phoneId" + i2);
        ValidStatus validStatus = this.mPhoneValidStatus[i2];
        int i3 = AnonymousClass10.$SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.values()[i].ordinal()];
        if (i3 == 1) {
            validStatus.setValid(false, SystemClock.elapsedRealtime());
        } else {
            if (i3 != 2) {
                return;
            }
            validStatus.setValid(true, SystemClock.elapsedRealtime());
        }
    }

    /* JADX INFO: renamed from: com.android.phone.dsda.MiuiDualDataController$10, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType;

        static {
            int[] iArr = new int[TelephonyStatAdapter.DataStatType.values().length];
            $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType = iArr;
            try {
                iArr[TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processStaticStateDurationLinger() {
        if (this.mConcurrentLimitedWhiteListForground && isDualDataConnected()) {
            int defaultSlotId = getDefaultSlotId();
            if (getReducePowerSiganlPolicyState(defaultSlotId, getViceSlotId()) && isSatifyStaticDemand() && !getSmartDualSimSwitchState()) {
                broadcastNotifySimSwitch(defaultSlotId);
            }
        }
    }

    private void startMonitorTimerForSensorStatic() {
        log("startMonitorTimerForSensorStatic");
        stopMonitorTimerForSensorStatic();
        this.mHandler.sendEmptyMessageDelayed(1036, 480000L);
    }

    private void stopMonitorTimerForSensorStatic() {
        if (this.mHandler.hasMessages(1036)) {
            log("stopMonitorTimerForSensorStatic");
            this.mHandler.removeMessages(1036);
        }
    }

    private void startMonitorTimerForStaticDuration() {
        log("startMonitorTimerForStaticDuration");
        stopMonitorTimerForStaticDuration();
        this.mHandler.sendEmptyMessageDelayed(1035, 120000L);
    }

    private void stopMonitorTimerForStaticDuration() {
        if (this.mHandler.hasMessages(1035)) {
            log("stopMonitorTimerForStaticDuration");
            this.mHandler.removeMessages(1035);
        }
    }

    private void broadcastNotifySimSwitch(int i) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.SIM_SWITCH");
        intent.putExtra("phoneId", i);
        intent.putExtra("switchScene", "DUAL_DATA");
        this.mContext.sendBroadcast(intent);
    }

    private String getMobileDataSettingName() {
        return MiuiPhoneUtils.PHONE_COUNT > 1 ? "mobile_data0" : "mobile_data";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataSettingOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), getMobileDataSettingName(), 0) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDualMobileDataSettingOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "dual_mobile_data", 0) != 0;
    }

    private int getLinkTurboSetting() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "link_turbo_mode", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDefaultSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAppDualDataEnableStateOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "application_dual_mobile_data", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDualDataConnected() {
        return SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
    }

    private boolean isSeamlessSwitching() {
        return "true".equals(SystemProperties.get("ril.temp.seamless_switch.state", "false"));
    }

    public static int getViceSlotId() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != DefaultSimManager.getDefaultDataSlotId()) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSlotActivated(int i) {
        SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
        return subscriptionInfoForSlot != null && subscriptionInfoForSlot.isActivated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    private boolean isValidateRsrp(int i) {
        if (!isValidatePhoneId(i)) {
            log("isValidateRsrp invalid slot id");
            return false;
        }
        if (isLteNetworkType(this.mRat[i])) {
            return this.mLteRsrp[i] != Integer.MAX_VALUE;
        }
        return isNrNetworkType(this.mRat[i]) && this.mNrRsrp[i] != Integer.MAX_VALUE;
    }

    private boolean isValidateSnr(int i) {
        if (!isValidatePhoneId(i)) {
            log("isValidateSnr invalid slot id");
            return false;
        }
        if (isLteNetworkType(this.mRat[i])) {
            return this.mLteSnr[i] != Integer.MAX_VALUE;
        }
        return isNrNetworkType(this.mRat[i]) && this.mNrSnr[i] != Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("MiuiDualDataController", str);
    }

    private void initSLMUIObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("link_turbo_mode"), false, new ContentObserver(this.mHandler) { // from class: com.android.phone.dsda.MiuiDualDataController.7
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                int i = Settings.System.getInt(MiuiDualDataController.this.mContext.getContentResolver(), "link_turbo_mode", 0);
                if (MiuiDualDataController.this.mLinkTurboMode != i) {
                    MiuiDualDataController.this.mLinkTurboMode = i;
                    MiuiDualDataController.this.mHandler.sendEmptyMessage(1027);
                }
            }
        });
    }

    private void initDualDataConcurrentModeWhiteList() {
        List dualDataConcurrentModeWhiteListByCloud = FiveGManagerBase.getDualDataConcurrentModeWhiteListByCloud(this.mContext);
        this.mDualDataConcurrentModeAppWhiteList = dualDataConcurrentModeWhiteListByCloud;
        if (dualDataConcurrentModeWhiteListByCloud.size() != 0) {
            log("mode white list = " + this.mDualDataConcurrentModeAppWhiteList);
        } else {
            log("mode white list cloud data fetch error!");
        }
        if (this.mIsHstMode) {
            for (String str : this.mHstDualDataPromoteApps) {
                if (!this.mDualDataConcurrentModeAppWhiteList.contains(str)) {
                    this.mDualDataConcurrentModeAppWhiteList.add(str);
                }
            }
        }
    }

    private void initDualDataConcurrentLimitedWhiteList() {
        List dualDataConcurrentLimitedWhiteListByCloud = FiveGManagerBase.getDualDataConcurrentLimitedWhiteListByCloud(this.mContext);
        this.mDualDataConcurrentLimitedAppWhiteList = dualDataConcurrentLimitedWhiteListByCloud;
        if (dualDataConcurrentLimitedWhiteListByCloud.size() != 0) {
            log("limited white list = " + this.mDualDataConcurrentLimitedAppWhiteList);
        } else {
            log("Limited white list cloud data fetch error!");
        }
        if (this.mIsHstMode) {
            for (String str : this.mHstDualDataPromoteApps) {
                if (this.mDualDataConcurrentLimitedAppWhiteList.contains(str)) {
                    this.mDualDataConcurrentLimitedAppWhiteList.remove(str);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDualDataConcurrentModeAppWhiteList() {
        this.mDualDataConcurrentModeAppWhiteList.clear();
        initDualDataConcurrentModeWhiteList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDualDataConcurrentLimitedWhiteList() {
        this.mDualDataConcurrentLimitedAppWhiteList.clear();
        initDualDataConcurrentLimitedWhiteList();
    }

    void initDualDataRedundantModeWhiteList() {
        List dualDataRedundantModeWhiteListByCloud = FiveGManagerBase.getDualDataRedundantModeWhiteListByCloud(this.mContext);
        this.mDualDataRedundantModeAppWhiteList = dualDataRedundantModeWhiteListByCloud;
        if (dualDataRedundantModeWhiteListByCloud.size() != 0) {
            log("mDualDataRedundantModeAppWhiteList=" + this.mDualDataRedundantModeAppWhiteList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDualDataRedundantModeAppWhiteList() {
        this.mDualDataRedundantModeAppWhiteList.clear();
        initDualDataRedundantModeWhiteList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameUpdateStatusChanged(boolean z) {
        if (!isDsdaEnhanceSupport()) {
            log("processGameUpdateStatusChanged: dsda enhance not supported, skip");
            return;
        }
        if (z == this.mIsGameUpdating) {
            log("processGameUpdateStatusChanged: same state, skip");
            return;
        }
        log("processGameUpdateStatusChanged isGameUpdating=" + z);
        if (z) {
            this.mGameUpdateStartTime = SystemClock.elapsedRealtime();
            this.mHandler.removeMessages(1040);
            this.mHandler.sendEmptyMessageDelayed(1040, 180000L);
        } else {
            this.mHandler.removeMessages(1040);
        }
        this.mIsGameUpdating = z;
        updateViceNetworkByPolicy(5001, getDefaultSlotId(), getViceSlotId());
    }

    private boolean isGameUpdatingValid() {
        return this.mIsGameUpdating && SystemClock.elapsedRealtime() - this.mGameUpdateStartTime < 180000;
    }

    private boolean isDsdaEnhanceSupport() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_dsda_enhance_support", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstModeChanged(boolean z) {
        if (!isDsdaEnhanceSupport()) {
            log("processHstModeChanged: dsda enhance not supported, skip");
            return;
        }
        if (this.mIsHstMode == z) {
            return;
        }
        this.mIsHstMode = z;
        log("processHstModeChanged isHstMode=" + z);
        Set<String> hstDualDataPromoteApps = getHstDualDataPromoteApps();
        this.mHstDualDataPromoteApps = hstDualDataPromoteApps;
        if (z) {
            for (String str : hstDualDataPromoteApps) {
                if (!this.mDualDataConcurrentModeAppWhiteList.contains(str)) {
                    this.mDualDataConcurrentModeAppWhiteList.add(str);
                }
                if (this.mDualDataConcurrentLimitedAppWhiteList.contains(str)) {
                    this.mDualDataConcurrentLimitedAppWhiteList.remove(str);
                }
            }
            return;
        }
        updateDualDataConcurrentModeAppWhiteList();
        updateDualDataConcurrentLimitedWhiteList();
    }

    private Set getHstDualDataPromoteApps() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("config_hst_dual_data_promote_apps");
        if (featureConfig.isEmpty()) {
            log("getHstDualDataPromoteApps fail");
            return new HashSet();
        }
        return new HashSet(featureConfig);
    }

    private void registerHstStationCallback() {
        HyperCommManager hyperCommManager = HyperCommManager.getDefault();
        if (hyperCommManager == null) {
            log("registerHstStationCallback: HyperCommManager not available");
            return;
        }
        HstStationCallback hstStationCallback = new HstStationCallback(new HandlerExecutor(this.mHandler));
        this.mHstStationCallback = hstStationCallback;
        boolean zRegisterSceneAwareCallback = hyperCommManager.registerSceneAwareCallback(hstStationCallback);
        log("registerHstStationCallback result: " + zRegisterSceneAwareCallback);
        if (zRegisterSceneAwareCallback) {
            return;
        }
        this.mHstStationCallback = null;
    }

    class HstStationCallback extends SceneAwareCallback implements SceneAwareCallback.HighSpeedTrainStationListener {
        HstStationCallback(Executor executor) {
            super(executor);
        }

        public void onHighSpeedTrainStationChanged(String str, boolean z, boolean z2) {
            MiuiDualDataController.log("onHighSpeedTrainStationChanged: name=" + str + ", isEnter=" + z + ", isDomestic=" + z2);
            MiuiDualDataController.this.processHstModeChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:33:0x006f  */
    /* JADX WARN: Code duplicated, block: B:35:0x0075  */
    /* JADX WARN: Code duplicated, block: B:40:0x0082  */
    /* JADX WARN: Code duplicated, block: B:55:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:57:0x00b1  */
    /* JADX WARN: Code duplicated, block: B:60:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:62:0x00bb  */
    /* JADX WARN: Code duplicated, block: B:67:? A[RETURN, SYNTHETIC] */
    public void processForegroundInfoEvent(ForegroundInfo foregroundInfo) {
        boolean z;
        String str = foregroundInfo.mForegroundPackageName;
        int defaultSlotId = getDefaultSlotId();
        int viceSlotId = getViceSlotId();
        if (isValidatePhoneId(defaultSlotId) && isSlotActivated(defaultSlotId) && isValidatePhoneId(viceSlotId) && isSlotActivated(viceSlotId)) {
            boolean z2 = true;
            if (isConcurrentWhiteListApp(str) && !this.mConcurrentWhiteListForground) {
                this.mConcurrentWhiteListForground = true;
            } else {
                if (isConcurrentWhiteListApp(str) || !this.mConcurrentWhiteListForground) {
                    z = false;
                } else {
                    this.mConcurrentWhiteListForground = false;
                }
                if (!isRedundantWhiteListApp(str) && !this.mRedundantWhiteListForground) {
                    this.mRedundantWhiteListForground = true;
                    log("processForegroundInfoEvent mLastSubwayState: " + this.mLastSubwayState);
                    if (!getCommonSignalPolicyState(defaultSlotId, viceSlotId) && this.mLastSubwayState != 1) {
                        return;
                    }
                } else {
                    if (!isRedundantWhiteListApp(str) && this.mRedundantWhiteListForground) {
                        this.mRedundantWhiteListForground = false;
                    }
                    if (!isConcurrentLimitedWhiteListApp(str) && !this.mConcurrentLimitedWhiteListForground) {
                        this.mConcurrentLimitedWhiteListForground = true;
                        if (!getCommonSignalPolicyState(defaultSlotId, viceSlotId)) {
                            return;
                        }
                        if (getReducePowerSiganlPolicyState(defaultSlotId, viceSlotId) && isSatifyStaticDemand() && !getSmartDualSimSwitchState() && !isSameServiceProvider(defaultSlotId, viceSlotId)) {
                            broadcastNotifySimSwitch(defaultSlotId);
                            return;
                        }
                    } else if (isConcurrentLimitedWhiteListApp(str) && this.mConcurrentLimitedWhiteListForground) {
                        this.mConcurrentLimitedWhiteListForground = false;
                    } else {
                        z2 = z;
                    }
                    if (z2) {
                        this.mHandler.obtainMessage(1016).sendToTarget();
                    }
                }
                z = true;
                if (!isConcurrentLimitedWhiteListApp(str)) {
                    if (isConcurrentLimitedWhiteListApp(str)) {
                        z2 = z;
                    } else {
                        z2 = z;
                    }
                } else if (isConcurrentLimitedWhiteListApp(str)) {
                    z2 = z;
                } else {
                    z2 = z;
                }
                if (z2) {
                    this.mHandler.obtainMessage(1016).sendToTarget();
                }
            }
            z = true;
            if (!isRedundantWhiteListApp(str)) {
                if (!isRedundantWhiteListApp(str)) {
                    this.mRedundantWhiteListForground = false;
                    z = true;
                }
            } else if (!isRedundantWhiteListApp(str)) {
                this.mRedundantWhiteListForground = false;
                z = true;
            }
            if (!isConcurrentLimitedWhiteListApp(str)) {
                if (isConcurrentLimitedWhiteListApp(str)) {
                    z2 = z;
                } else {
                    z2 = z;
                }
            } else if (isConcurrentLimitedWhiteListApp(str)) {
                z2 = z;
            } else {
                z2 = z;
            }
            if (z2) {
                this.mHandler.obtainMessage(1016).sendToTarget();
            }
        }
    }

    private boolean isConcurrentWhiteListApp(String str) {
        if (str != null && this.mDualDataConcurrentModeAppWhiteList.size() != 0) {
            Iterator it = this.mDualDataConcurrentModeAppWhiteList.iterator();
            while (it.hasNext()) {
                if (str.equals((String) it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRedundantWhiteListApp(String str) {
        if (str != null && this.mDualDataRedundantModeAppWhiteList.size() != 0) {
            Iterator it = this.mDualDataRedundantModeAppWhiteList.iterator();
            while (it.hasNext()) {
                if (str.equals((String) it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isConcurrentLimitedWhiteListApp(String str) {
        if (str != null && this.mDualDataConcurrentLimitedAppWhiteList.size() != 0) {
            Iterator it = this.mDualDataConcurrentLimitedAppWhiteList.iterator();
            while (it.hasNext()) {
                if (str.equals((String) it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processWhiteAppListForegroundStateChanged() {
        if (MiuiPhoneUtils.getInsertedSimCount() <= 1) {
            log("Single Or Absent Sim Card , Ignore!");
        } else if (isSuitBaseCondition()) {
            this.mHandler.obtainMessage(1019, 5001).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChanged(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            log("processServiceStateChanged invalid params");
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        int dataNetworkType2 = this.mServiceState[i].getDataNetworkType();
        this.mRat[i] = dataNetworkType;
        this.mBand[i] = Utils.getBand(i, serviceState);
        this.mServiceState[i] = serviceState;
        if (dataNetworkType2 == dataNetworkType || !getCommonPolicyState()) {
            return;
        }
        this.mHandler.obtainMessage(1019, 5002).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthChanged(int i, SignalStrength signalStrength) {
        if (!isValidatePhoneId(i) || signalStrength == null) {
            log("processSignalStrengthChanged invalid params!");
            return;
        }
        int i2 = this.mNrRsrp[i];
        int i3 = this.mLteRsrp[i];
        int i4 = this.mNrSnr[i];
        int i5 = this.mLteSnr[i];
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthNr) {
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                this.mNrRsrp[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsRsrp(), -140, -44);
                this.mNrSnr[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsSinr(), -23, 40);
            } else if (cellSignalStrength instanceof CellSignalStrengthLte) {
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                this.mLteRsrp[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRsrp(), -140, -44);
                this.mLteSnr[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRssnr(), -23, 40);
            }
        }
        if (!(this.mNrRsrp[i] == i2 && this.mLteRsrp[i] == i3 && ((this.mNrSnr[i] == i4 && this.mLteSnr[i] == i5) || i != getDefaultSlotId())) && getCommonPolicyState()) {
            log("CMASI: " + this.mConcurrentLimitedWhiteListForground + ", RAISI: " + this.mRedundantWhiteListForground);
            int defaultSlotId = getDefaultSlotId();
            int viceSlotId = getViceSlotId();
            if (isValidatePhoneId(defaultSlotId) && isSlotActivated(defaultSlotId) && isValidatePhoneId(viceSlotId) && isSlotActivated(viceSlotId)) {
                if (this.mConcurrentLimitedWhiteListForground && getConcurrentModeSignalPolicy(defaultSlotId, viceSlotId)) {
                    Message messageObtainMessage = this.mHandler.obtainMessage(1019, 5003);
                    if (this.mHandler.hasMessages(1019)) {
                        this.mHandler.removeMessages(1019);
                    }
                    if (this.mIsNeedDelay) {
                        log("delay disconnect in special move scene");
                        this.mHandler.sendMessageDelayed(messageObtainMessage, 120000L);
                    } else {
                        messageObtainMessage.sendToTarget();
                    }
                }
                if (this.mRedundantWhiteListForground && getRedundantModeSignalPolicy(defaultSlotId, viceSlotId)) {
                    this.mHandler.obtainMessage(1019, 5003).sendToTarget();
                }
            }
        }
    }

    private boolean getConcurrentModeSignalPolicy(int i, int i2) {
        if (!isValidateRsrp(i) || !isValidateRsrp(i2)) {
            log("getConcurrentModeSignalPolicy : SignalStrength Invalid");
            return false;
        }
        int rsrpBySlot = getRsrpBySlot(i);
        int rsrpBySlot2 = getRsrpBySlot(i2);
        if (rsrpBySlot == Integer.MAX_VALUE || rsrpBySlot2 == Integer.MAX_VALUE) {
            return false;
        }
        if (isDualDataConnected() && isSpecialMoveScene()) {
            boolean z = rsrpBySlot2 < -105;
            this.mIsNeedDelay = z;
            if (z) {
                return true;
            }
        } else {
            this.mIsNeedDelay = false;
        }
        return rsrpBySlot < -105 || rsrpBySlot2 < -105;
    }

    private boolean getRedundantModeSignalPolicy(int i, int i2) {
        if (!isValidateRsrp(i) || !isValidateRsrp(i2)) {
            log("getRedundantModeSignalPolicy : SignalStrength Invalid");
            return false;
        }
        int rsrpBySlot = getRsrpBySlot(i);
        int rsrpBySlot2 = getRsrpBySlot(i2);
        return rsrpBySlot != Integer.MAX_VALUE && rsrpBySlot2 != Integer.MAX_VALUE && rsrpBySlot < -105 && rsrpBySlot2 >= -105;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processReleaseViceNetwork(int i) {
        int viceSlotId = getViceSlotId();
        if (isValidatePhoneId(viceSlotId) && isSlotActivated(viceSlotId)) {
            setProcTypeInfo(0, i);
            this.mMiuiDualDataTracker[viceSlotId].releaseNetworkRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkBoostStatusChanged(int i, Intent intent) {
        if (i != 1025) {
            if (i == 1027) {
                log("EVENT_SLM_MODE_CHANGED mode: " + this.mLinkTurboMode);
            } else if (i == 1028) {
                log("EVENT_SLAVE_WIFI_CHANGED status: " + this.mSlaveWifiStatus);
            }
        } else {
            if (intent == null) {
                return;
            }
            this.mSlmEnabled = intent.getIntExtra("coexistense", -1) == 1;
            log("EVENT_SLM_STATUS_CHANGED status: " + this.mSlmEnabled);
        }
        if (this.mUserDualDataEnabled && this.mNddsCapability) {
            this.mHandler.obtainMessage(1019, 5007).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:21:0x018e A[Catch: all -> 0x017f, TryCatch #0 {all -> 0x017f, blocks: (B:4:0x0003, B:6:0x0009, B:9:0x0011, B:11:0x0174, B:21:0x018e, B:23:0x0194, B:25:0x019a, B:27:0x01a0, B:16:0x0181, B:18:0x0187, B:28:0x01a4), top: B:35:0x0003 }] */
    public synchronized void updateViceNetworkByPolicy(int i, int i2, int i3) {
        try {
            if (isValidatePhoneId(i3) && isSlotActivated(i3)) {
                localLog("updateViceNetworkByPolicy", ", type = " + i + ", dS = " + i2 + ", vS = " + i3 + ", mUD = " + this.mUserDataEnabled + ", mUE = " + this.mUserDualDataEnabled + ", mNC = " + this.mNddsCapability + ", dRt = " + this.mRat[i2] + ", nDRt = " + this.mRat[i3] + ", dBd = " + this.mBand[i2] + ", nBd = " + this.mBand[i3] + ", dNRp = " + this.mNrRsrp[i2] + ", nDNRp = " + this.mNrRsrp[i3] + ", dLRp = " + this.mLteRsrp[i2] + ", nDLRp = " + this.mLteRsrp[i3] + ", dNS = " + this.mNrSnr[i2] + ", nDNS = " + this.mLteSnr[i3] + ", mAE = " + this.mAppDualDataEnabled + ", mCF = " + this.mConcurrentWhiteListForground + ", mMF = " + this.mConcurrentLimitedWhiteListForground + ", mRF = " + this.mRedundantWhiteListForground + ", mSD = " + getSmartDualSimSwitchState() + ", mWC = " + this.mIsWifiConnected + ", mSE = " + this.mSlmEnabled + ", mSM = " + this.mLinkTurboMode + ", mDW = " + checkDoubleWifiState() + ", mLB = " + this.mLowBatteryStatus + ", mTS = " + this.mThermalStatus + ", mSLS =" + isSeamlessSwitching() + ", mVR =" + isViceSlotRoaming() + ", mGU =" + this.mIsGameUpdating + "(" + isGameUpdatingValid() + ")");
                if (!isDualDataConnected() || this.mHandler.hasMessages(1023)) {
                    if (getConcurrentModePolicyState(i2, i3) || getRedundantModePolicyState(i2, i3)) {
                        requestViceNetworkByPolicy(i, i3);
                    } else if (isDualDataConnected() && !getConcurrentModePolicyState(i2, i3) && !getRedundantModePolicyState(i2, i3)) {
                        releaseViceNetworkByPolicy(i, i3);
                    }
                } else if (isDualDataConnected()) {
                    releaseViceNetworkByPolicy(i, i3);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean getCommonPolicyState() {
        return (!this.mUserDataEnabled || !this.mUserDualDataEnabled || !this.mNddsCapability || getSmartDualSimSwitchState() || this.mLowBatteryStatus || this.mThermalStatus || isSeamlessSwitching() || isViceSlotRoaming()) ? false : true;
    }

    private boolean isViceSlotRoaming() {
        Phone phone = PhoneFactory.getPhone(getViceSlotId());
        if (phone != null) {
            return phone.getServiceState().getRoaming();
        }
        return false;
    }

    private boolean getFiveGAndFiveGPolicyState(int i, int i2) {
        if (isValidateRsrp(i) && isValidateRsrp(i2)) {
            return isNrNetworkType(this.mRat[i]) && isNrNetworkType(this.mRat[i2]) && this.mNrRsrp[i2] >= -105;
        }
        log("5+5: NR SignalStrength Invalid");
        return false;
    }

    private boolean getFourGAndFiveGPolicyState(int i, int i2) {
        if (isValidateRsrp(i2)) {
            return isLteNetworkType(this.mRat[i]) && isNrNetworkType(this.mRat[i2]) && this.mNrRsrp[i2] >= -105;
        }
        log("4+5: NR SignalStrength Invalid");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getCommonSignalPolicyState(int i, int i2) {
        if (!isValidateRsrp(i) || !isValidateRsrp(i2) || !isValidateSnr(i)) {
            log("getCommonSignalPolicyState : SignalStrength Invalid");
            return false;
        }
        int rsrpBySlot = getRsrpBySlot(i);
        int rsrpBySlot2 = getRsrpBySlot(i2);
        int snrBySlot = getSnrBySlot(i);
        log("cdS: " + rsrpBySlot + "  cvS: " + rsrpBySlot2 + "  cdSn: " + snrBySlot);
        return (rsrpBySlot == Integer.MAX_VALUE || rsrpBySlot2 == Integer.MAX_VALUE || snrBySlot == Integer.MAX_VALUE || (rsrpBySlot >= -105 && snrBySlot >= 0) || rsrpBySlot2 < -105) ? false : true;
    }

    private boolean getReducePowerSiganlPolicyState(int i, int i2) {
        if (isValidatePhoneId(i) && isSlotActivated(i) && isValidatePhoneId(i2) && isSlotActivated(i2)) {
            if (!isValidateRsrp(i) || !isValidateRsrp(i2) || !isValidateSnr(i)) {
                log("getReducePowerSiganlPolicyState : SignalStrength Invalid");
            } else {
                int rsrpBySlot = getRsrpBySlot(i);
                int rsrpBySlot2 = getRsrpBySlot(i2);
                log("getReducePowerSiganlPolicyState dS: " + rsrpBySlot + " vS: " + rsrpBySlot2);
                return rsrpBySlot != Integer.MAX_VALUE && rsrpBySlot2 != Integer.MAX_VALUE && rsrpBySlot < -105 && rsrpBySlot2 >= -95;
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x001a  */
    private boolean isSatifyStaticDemand() {
        boolean z;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsStaticStatus) {
            long j = this.mLastStaticStatusTime;
            if (j == -1 || jElapsedRealtime - j < 120000) {
                z = false;
            } else {
                z = true;
            }
        } else {
            z = false;
        }
        log("isSatifyStaticDemand rst=" + z);
        return z;
    }

    private int getRsrpBySlot(int i) {
        if (isValidateRsrp(i) && isValidatePhoneId(i)) {
            return isLteNetworkType(this.mRat[i]) ? this.mLteRsrp[i] : this.mNrRsrp[i];
        }
        log("getRsrpBySlot: SignalStrength Invalid");
        return Integer.MAX_VALUE;
    }

    private int getSnrBySlot(int i) {
        if (isValidateSnr(i) && isValidatePhoneId(i)) {
            return isLteNetworkType(this.mRat[i]) ? this.mLteSnr[i] : this.mNrSnr[i];
        }
        log("getSnrBySlot: Snr Invalid");
        return Integer.MAX_VALUE;
    }

    private boolean isSuitDualBandCondition(int i, int i2) {
        boolean zIsDsdaFullConcurrencyState;
        boolean zIsCompareBand = false;
        if (!isValidatePhoneId(i) || !isValidatePhoneId(i2)) {
            log("isSuitDualBandCondition invalid slot id");
            return false;
        }
        int[] iArr = this.mRat;
        int i3 = iArr[i];
        int i4 = iArr[i2];
        if (TelephonyManagerEx.getDefault().isPlatform8750()) {
            if (Build.IS_INTERNATIONAL_BUILD) {
                if (this.mDppdSubMode == 1 && this.mDppdTxconcurrency == 0) {
                    zIsCompareBand = true;
                }
                log("isSuitDualBandCondition international rst:" + zIsCompareBand);
                return zIsCompareBand;
            }
            if (i3 == 20 && i4 == 20) {
                int i5 = this.mDppdSubMode;
                if (i5 == 1 && this.mDppdTxconcurrency == 0) {
                    zIsCompareBand = !isCompareBand(i, i2, this.mSaBandCombsLimit);
                } else if (i5 == 1 && this.mDppdTxconcurrency == 1) {
                    zIsCompareBand = isCompareBand(i, i2, this.mSaBandCombsLimitless);
                }
                log("isSuitDualBandCondition cn nr+nr rst:" + zIsCompareBand);
                return zIsCompareBand;
            }
            if (this.mDppdSubMode == 1 && this.mDppdTxconcurrency == 0) {
                zIsCompareBand = true;
            }
            log("isSuitDualBandCondition cn !nr+nr rst:" + zIsCompareBand);
            return zIsCompareBand;
        }
        if (i3 == 20 && i4 == 20 && isDsdaFullConcurrencyState()) {
            zIsDsdaFullConcurrencyState = !isCompareBand(i, i2, this.mSaBandCombsLimit);
        } else {
            zIsDsdaFullConcurrencyState = isDsdaFullConcurrencyState();
        }
        log("isSuitDualBandCondition rst:" + zIsDsdaFullConcurrencyState);
        return zIsDsdaFullConcurrencyState;
    }

    private boolean isCompareBand(int i, int i2, String[] strArr) {
        String str;
        if (!isValidatePhoneId(i) || !isValidatePhoneId(i2) || strArr == null || strArr.length == 0) {
            log("isCompareBand invalid slot id or invalid band");
            return false;
        }
        String[] strArr2 = this.mBand;
        String str2 = strArr2[i];
        String str3 = strArr2[i2];
        boolean z = false;
        for (String str4 : strArr) {
            String[] strArrSplit = str4.split(",");
            if (strArrSplit == null || strArrSplit.length != 2) {
                log("isCompareBand band config exception");
                break;
            }
            String str5 = strArrSplit[0];
            if (str5 == null || (str = strArrSplit[1]) == null) {
                log("isCompareBand comb exception");
                return z;
            }
            if (str5.equals(str2) && str.equals(str3)) {
                log("isCompareBand band does not meet the requirements");
                z = true;
            }
        }
        return z;
    }

    private boolean getConcurrentModePolicyState(int i, int i2) {
        if (!getCommonPolicyState()) {
            return false;
        }
        if (!this.mConcurrentWhiteListForground && !this.mConcurrentLimitedWhiteListForground && !isGameUpdatingValid()) {
            return false;
        }
        if ((!getFiveGAndFiveGPolicyState(i, i2) && !getFourGAndFiveGPolicyState(i, i2)) || !isSuitDualBandCondition(i, i2)) {
            return false;
        }
        boolean z = this.mIsWifiConnected;
        return !z || (this.mSlmEnabled && z && checkDoubleWifiState() && this.mLinkTurboMode == 1);
    }

    private boolean getRedundantModePolicyState(int i, int i2) {
        return getCommonPolicyState() && this.mAppDualDataEnabled && this.mRedundantWhiteListForground && !this.mIsWifiConnected;
    }

    private void requestViceNetworkByPolicy(int i, int i2) {
        if (!isValidatePhoneId(i2) || !isSlotActivated(i2) || !this.mPhoneValidStatus[i2].isValid()) {
            log("ignore requestViceNetworkByPolicy");
            return;
        }
        if (this.mHandler.hasMessages(1023)) {
            this.mHandler.removeMessages(1023);
        }
        setProcTypeInfo(2, i2);
        this.mMiuiDualDataTracker[i2].requestNetworkRequest();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseViceNetworkByPolicy(int i, int i2) {
        if (this.mHandler.hasMessages(1023)) {
            this.mHandler.removeMessages(1023);
        }
        if (i == 5001 || i == 5004) {
            MiuiDualDataHandler miuiDualDataHandler = this.mHandler;
            miuiDualDataHandler.sendMessageDelayed(miuiDualDataHandler.obtainMessage(1023, Integer.valueOf(i2)), 30000L);
        } else {
            processReleaseViceNetwork(i2);
        }
    }

    private boolean getSmartDualSimSwitchState() {
        return "true".equals(SystemProperties.get("ril.temp.dds.state", "false"));
    }

    private void setProcTypeInfo(int i, int i2) {
        log("setProcTypeInfo type = " + i + ", phoneid = " + i2);
        this.mRilHook.setProcTypeInfo(i, i2);
    }

    public void requestNetworkBySlot(int i) {
        if (isValidatePhoneId(i)) {
            setProcTypeInfo(2, i);
            this.mMiuiDualDataTracker[i].requestNetworkRequest();
        }
    }

    public void releaseNetworkBySlot(int i) {
        if (isValidatePhoneId(i)) {
            setProcTypeInfo(0, i);
            this.mMiuiDualDataTracker[i].releaseNetworkRequest();
        }
    }

    private static void localLog(String str, String str2) {
        localLog("MiuiDualDataController", str, str2);
    }

    private static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(250));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MiuiDualDataController dump start------");
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
        printWriter.println("MiuiDualDataController dump end------");
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkThermal() throws Throwable {
        try {
            String line = readLine("/sys/class/thermal/thermal_message/board_sensor_temp");
            if (line != null) {
                this.mThermalList.add(Long.valueOf(Long.parseLong(line.trim())));
                if (this.mThermalList.size() > 4) {
                    this.mThermalList.remove(0);
                }
                ArrayList arrayList = this.mThermalList;
                int size = arrayList.size();
                long jLongValue = 0;
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    jLongValue += ((Long) obj).longValue();
                }
                long size2 = jLongValue / ((long) this.mThermalList.size());
                if (!this.mThermalStatus && !isPerformanceMode() && size2 > 45500) {
                    log("Thermal management disableDualData - " + size2 + "isPerfMode = " + isPerformanceMode());
                    this.mThermalStatus = true;
                    this.mHandler.obtainMessage(1019, 5006).sendToTarget();
                    return;
                }
                if (this.mThermalStatus) {
                    if (size2 < 41000 || isPerformanceMode()) {
                        log("Thermal management enableDualData- " + size2 + "isPerfMode = " + isPerformanceMode());
                        this.mThermalStatus = false;
                        this.mHandler.obtainMessage(1019, 5006).sendToTarget();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("MiuiDualDataController", "checkThermal:" + e);
        }
    }

    private boolean isPerformanceMode() throws Throwable {
        try {
            String line = readLine("/sys/class/thermal/thermal_message/balance_mode");
            return line != null && Integer.parseInt(line.trim()) == 1;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processBatteryLevelChanged(int i) {
        int i2 = this.mBatteryLevel;
        this.mBatteryLevel = i;
        boolean z = this.mLowBatteryStatus;
        if (i2 != i && i <= 20 && !z) {
            this.mLowBatteryStatus = true;
            log("Battery Level: " + i);
        } else if (i2 != i && i > 20 && z) {
            this.mLowBatteryStatus = false;
        }
        if (z != this.mLowBatteryStatus) {
            log("low battery changed");
            if (isSuitBaseCondition()) {
                this.mHandler.obtainMessage(1019, 5005).sendToTarget();
            }
        }
    }

    private boolean isSuitBaseCondition() {
        return this.mUserDualDataEnabled && this.mNddsCapability;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i, CellLocation cellLocation) {
        long ci;
        if (MiuiPhoneUtils.isValidatePhoneId(i) && isDefaultDataSlotId(i)) {
            CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
            if (cellLocation == null || cellLocation.isEmpty() || currentCellIdentity == null) {
                return;
            }
            if (currentCellIdentity instanceof CellIdentityNr) {
                ci = ((CellIdentityNr) currentCellIdentity).getNci();
            } else if (currentCellIdentity instanceof CellIdentityLte) {
                ci = ((CellIdentityLte) currentCellIdentity).getCi();
            } else {
                log("processCellLocationChange: ignore non 4/5G cell");
                return;
            }
            if (ci == this.mLastCellId.longValue() || ci == 2147483647L || ci == Long.MAX_VALUE) {
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            CellState cellState = (CellState) this.mCellQueue.peekFirst();
            while (cellState != null && jElapsedRealtime - cellState.getEntertime() > 480000) {
                this.mCellQueue.pollFirst();
                cellState = (CellState) this.mCellQueue.peekFirst();
            }
            this.mCellQueue.offerLast(new CellState(ci, jElapsedRealtime));
            log("processCellLocationChanged mCellQueue size=" + this.mCellQueue.size());
            updateMoveStateAndProcessChangeIfNeed(jElapsedRealtime);
        }
    }

    private synchronized void updateMoveStateByCell(long j) {
        if (this.mIsStaticStatus && isSpecialMoveScene()) {
            setStaticState(false);
            return;
        }
        if (this.mIsSensorStaticStatus) {
            long j2 = this.mSensorStaticStatusTime;
            if (j2 != -1 && j - j2 > 480000) {
                HashSet hashSet = new HashSet();
                for (int i = 0; i < this.mCellQueue.size(); i++) {
                    hashSet.add(Long.valueOf(((CellState) this.mCellQueue.get(i)).getCi()));
                }
                log("cellCiSet size=" + hashSet.size());
                if (this.mIsStaticStatus && hashSet.size() > 5) {
                    setStaticState(false);
                } else if (hashSet.size() <= 5) {
                    setStaticState(true);
                }
            }
        }
    }

    private void setStaticState(boolean z) {
        if (z) {
            this.mIsStaticStatus = true;
            this.mLastStaticStatusTime = SystemClock.elapsedRealtime();
            SystemProperties.set("ril.multisim.dualdata_static_status", "true");
            log("enter static status");
            return;
        }
        this.mIsStaticStatus = false;
        this.mLastStaticStatusTime = -1L;
        SystemProperties.set("ril.multisim.dualdata_static_status", "false");
        log("exit static status");
    }

    private void updateMoveStateAndProcessChangeIfNeed(long j) {
        boolean z = this.mIsStaticStatus;
        updateMoveStateByCell(j);
        if (!z && this.mIsStaticStatus && this.mConcurrentLimitedWhiteListForground && isDualDataConnected() && getReducePowerSiganlPolicyState(getDefaultSlotId(), getViceSlotId())) {
            startMonitorTimerForStaticDuration();
        }
    }

    private boolean isDefaultDataSlotId(int i) {
        return i == getDefaultSlotId();
    }

    /* JADX WARN: Code duplicated, block: B:43:0x0073 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private String readLine(String str) throws Throwable {
        BufferedReader bufferedReader;
        StringBuilder sb;
        BufferedReader bufferedReader2 = null;
        line = null;
        line = null;
        line = null;
        line = null;
        line = null;
        String line = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(str), SmartNetworkRelayScore.TRAFFIC_UNIT);
                try {
                    line = bufferedReader.readLine();
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("readLine fail:");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (IOException e2) {
                    e = e2;
                    log("readLine fail:" + e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("readLine fail:");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    log("readLine fail:" + e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            e = e5;
                            sb = new StringBuilder("readLine fail:");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                }
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e6) {
                        log("readLine fail:" + e6);
                    }
                }
                throw th;
            }
        } catch (IOException e7) {
            e = e7;
            bufferedReader = null;
        } catch (Exception e8) {
            e = e8;
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
        return line;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSlaveWifiConnected() {
        String dualWifiSsid = getDualWifiSsid();
        return (dualWifiSsid == null || dualWifiSsid.isEmpty()) ? false : true;
    }

    private String getDualWifiSsid() {
        return Settings.System.getString(this.mContext.getContentResolver(), "slave_wifi_ssid");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDppdCapability() {
        if (!MiuiPhoneUtils.isLicenseDualDataSupported()) {
            log("Not Support Lincense: getDppdCapability");
        } else {
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1008, 0, null, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDppdRecommendation() {
        if (!MiuiPhoneUtils.isLicenseDualDataSupported()) {
            log("Not Support Lincense: getDppdRecommendation");
        } else if (this.mDppdCapability) {
            log("getDppdRecommendation");
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1009, 0, null, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDppdUiOption(boolean z) {
        if (!MiuiPhoneUtils.isLicenseDualDataSupported()) {
            log("Not Support Lincense: updateDppdUiOption");
        } else if (this.mDppdCapability) {
            log("updateDppdUiOption option = " + z);
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1007, 0, Boolean.valueOf(z), "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getMultiSimSubmodeInfo() {
        if (!MiuiPhoneUtils.isLicenseDualDataSupported()) {
            log("Not Support Lincense: getMultiSimSubmodeInfo");
        } else if (this.mDppdCapability) {
            log("getMultiSimSubmodeInfo");
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1014, 0, null, "");
        }
    }

    private void registerDppdInterfaceCallback() {
        QmsClientBase qmsClientBase;
        if (!MiuiPhoneUtils.isLicenseDualDataSupported()) {
            log("Not Support Lincense: registerDppdInterfaceCallback");
            return;
        }
        try {
            if (this.mDppdInterfaceListener == null || (qmsClientBase = QmsClientBase.getInstance()) == null) {
                return;
            }
            qmsClientBase.registerDppdInterfaceListener(this.mDppdInterfaceListener);
        } catch (Exception e) {
            log("Exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubwayState() {
        int i;
        if (this.mNetworkModeRecognition == null || this.mLastSubwayState == (i = Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0) & 3)) {
            return -1;
        }
        this.mLastSubwayState = i;
        log("getSubwayState mLastSubwayState:" + this.mLastSubwayState + ", curState:" + i);
        NetworkModeRecognition networkModeRecognition = this.mNetworkModeRecognition;
        return (networkModeRecognition == null || !networkModeRecognition.isSubwayStartStopStateUpdate(i)) ? 0 : 1;
    }

    private boolean isSpecialMoveScene() {
        NetworkModeRecognition networkModeRecognition = this.mNetworkModeRecognition;
        if (networkModeRecognition == null) {
            return false;
        }
        boolean zIsHighSpeedTrain = networkModeRecognition.isHighSpeedTrain();
        boolean zIsSubwayStartStopStateUpdate = this.mNetworkModeRecognition.isSubwayStartStopStateUpdate(this.mLastSubwayState);
        if (!zIsHighSpeedTrain && !zIsSubwayStartStopStateUpdate) {
            return false;
        }
        log("isSpecialMoveScene isHst=" + zIsHighSpeedTrain + ", isSubway=" + zIsSubwayStartStopStateUpdate);
        return true;
    }

    private boolean isSameServiceProvider(int i, int i2) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !MiuiPhoneUtils.isValidatePhoneId(i2)) {
            return true;
        }
        Phone phone = PhoneFactory.getPhone(i);
        Phone phone2 = PhoneFactory.getPhone(i2);
        if (phone == null || phone2 == null) {
            return true;
        }
        String operatorNumeric = phone.getOperatorNumeric();
        String operatorNumeric2 = phone2.getOperatorNumeric();
        if (TextUtils.isEmpty(operatorNumeric) || TextUtils.isEmpty(operatorNumeric2)) {
            return true;
        }
        boolean zIsSameServiceProvider = ServiceProviderUtils.isSameServiceProvider(operatorNumeric, operatorNumeric2);
        if (zIsSameServiceProvider) {
            log("isSameServiceProvider rst=" + zIsSameServiceProvider);
        }
        return zIsSameServiceProvider;
    }

    public class CellState {
        private long ci;
        private long entertime;

        CellState(long j, long j2) {
            this.ci = j;
            this.entertime = j2;
        }

        public long getCi() {
            return this.ci;
        }

        public long getEntertime() {
            return this.entertime;
        }
    }

    public class ValidStatus {
        public boolean mIsValid = false;
        public long mStatusUpdateTime = -1;

        public ValidStatus(MiuiDualDataController miuiDualDataController) {
        }

        public boolean isValid() {
            return this.mIsValid || this.mStatusUpdateTime == -1 || SystemClock.elapsedRealtime() - this.mStatusUpdateTime > 3600000;
        }

        public void setValid(boolean z, long j) {
            this.mIsValid = z;
            this.mStatusUpdateTime = j;
        }

        public String toString() {
            return "ValidStatus{mIsValid=" + this.mIsValid + ", mStatusUpdateTime=" + this.mStatusUpdateTime + "}";
        }
    }

    private boolean isDsdaFullConcurrencyState() {
        boolean zIsDsdaFullConcurrencyState = TelephonyManagerEx.getDefault().isDsdaFullConcurrencyState();
        log("isDsdaFullConcurrencyState = " + zIsDsdaFullConcurrencyState);
        return zIsDsdaFullConcurrencyState;
    }

    private boolean checkDoubleWifiState() {
        if (!this.mSlaveWifiStatus) {
            return false;
        }
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        if (connectionInfo != null && connectionInfo.getBSSID() != null) {
            this.mMasterWifiStatus = true;
        } else {
            this.mMasterWifiStatus = false;
        }
        log("mMasterWifiStatus= " + this.mMasterWifiStatus + ", mSlaveWifiStatus= " + this.mSlaveWifiStatus);
        return this.mMasterWifiStatus;
    }
}
