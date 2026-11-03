package com.android.internal.telephony.datasub;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.util.ArrayUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CommenUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class SmartDualSimSwitch extends Handler {
    private static final String ACTION_DEBUG_ALL = "smartDualSim.intent.debugall";
    private static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
    private static final String ACTION_HST_MODE_STATUS_CHANGED = "com.android.phone.intent.action.HST_MODE_STATUS_CHANGED";
    private static final String ACTION_MODEM_AI_WEAK_PERCEPTION = "com.android.phone.intent.action.MODEM_AI_WEAKSIGNAL_CHANGED";
    private static final String ACTION_NETWORK_TRAFFIC_LIMIT = "com.miui.phone.TRAFFIC_LIMIT";
    private static final String ACTION_NOTIFY_SIM_SWITCH = "com.android.phone.intent.action.SIM_SWITCH";
    private static final String ACTION_SEAMLEASS_SWITCH_STATISTIC = "com.android.phone.intent.action.SEAMLEASS_SWITCH_STATISTIC";
    private static final String ACTION_SMART_SWITCH_DUAL_SWITCH_ALERT_DIALOG = "com.android.phone.intent.action.SMART_SWITCH_DUAL_SWITCH_ALERT_DIALOG";
    private static final String COLUMN_NAME_TRAFFIC_LIMIT = "limit_status";
    private static final int COMMON_FEAURE_SMART_DUAL_SIM = 2;
    private static final boolean DBG = true;
    private static final int DELAY_DDS_CHANGED_TIMEROUT = 10000;
    private static final int DELAY_DRIVE_STATE_CHANGE_TIMEOUT = 60000;
    private static final int DELAY_FIVEG_CHANGED_TIMEROUT = 10000;
    private static final int DELAY_GAME_PINGPONG_TIMEROUT = 20000;
    private static final int DELAY_GAME_TIME_MILLISECOND = 600000;
    private static final int DELAY_NW_SIGNAL_DIFF_TIMEOUT = 1000;
    private static final int DELAY_NW_WEAK_SIGNAL_TIMEOUT = 1000;
    private static final int DELAY_NW_WITHOUT_LTENR_TIMEOUT = 5000;
    private static final int DELAY_NW_WITHOUT_LTENR_TIMEOUT_AIRPLANE = 15000;
    private static final int DELAY_PHONE_STATE_IDLE_TIMEOUT = 5000;
    private static final int DELAY_RELEASE_DUALDATA_TIMEROUT = 2000;
    private static final int DELAY_SEAMLESS_SWITCH_RELEASE_TIMEROUT = 5000;
    private static final int DELAY_SEAMLESS_SWITCH_TIMEROUT = 3000;
    private static final int DELAY_UPDATE_OOS_TIMEOUT;
    private static final String[] EF_DRIVING_MAP_PN;
    private static final int EVENT_AIRPLANE_MODE_CHANGED = 240;
    private static final int EVENT_CELL_LOCATION_CHANGED = 170;
    private static final int EVENT_DATA_STATISTIC_CHANGED = 120;
    private static final int EVENT_DATA_SWITCH_CHANGED = 110;
    private static final int EVENT_DDS_CHANGED_LINGER = 210;
    private static final int EVENT_DEBUG_ALL = 1000;
    private static final int EVENT_DEFAULT_DATA_SLOT_CHANGED = 160;
    private static final int EVENT_DEFAULT_DATA_SUB_CHANGED = 100;
    private static final int EVENT_DRIVE_STATE_CHANGED_LINGER = 350;
    private static final int EVENT_DUALDATA_RELEASE_LINGER = 370;
    private static final int EVENT_FIVEG_CHANGED_LINGER = 230;
    private static final int EVENT_GAME_CHANGE_LEVEL = 70;
    private static final int EVENT_GAME_RESTORE_DELAY = 310;
    private static final int EVENT_GAME_SWITCH_DELAY = 300;
    private static final int EVENT_HST_MODE_STATUS_CHANGED = 270;
    private static final int EVENT_ID_INTVL = 10;
    private static final int EVENT_LOW_SPEED_CALLBACK = 80;
    private static final int EVENT_MIUI_REGION_CHANGED = 340;
    private static final int EVENT_MODEM_AI_PREDICT = 360;
    private static final int EVENT_NETWORK_SIGNAL_STRENGTH_CHANGED = 140;
    private static final int EVENT_NETWORK_STATE_CHANGED = 30;
    private static final int EVENT_NETWORK_TRAFFIC_LIMIT_CHANGED = 130;
    private static final int EVENT_NOTI_SIM_SWITCH = 320;
    private static final int EVENT_NW_SIGNAL_DIFF_LINGER = 380;
    private static final int EVENT_NW_WEAK_SIGNAL_LINGER = 220;
    private static final int EVENT_NW_WITHOUT_LTENR_AIRPLANE_LINGER = 260;
    private static final int EVENT_NW_WITHOUT_LTENR_LINGER = 200;
    private static final int EVENT_PHONE_STATE_IDLE = 390;
    private static final int EVENT_SCREEN_STATE_CHANGED = 40;
    private static final int EVENT_SCREEN_STATE_CHECK_DELAY = 50;
    private static final int EVENT_SCREEN_STATE_CHECK_DELAY_TIMEOUT = 60;
    private static final int EVENT_SEAMLESS_SWITCH_LINGER = 280;
    private static final int EVENT_SEAMLESS_SWITCH_RELEASE_LINGER = 290;
    private static final int EVENT_SERVICE_STATE_CHANGED = 20;
    private static final int EVENT_SIM_STATE_CHANGED = 90;
    private static final int EVENT_SLM_STATUS_CHANGED = 150;
    private static final int EVENT_SMART_DUAL_SIM_SETTINGS = 10;
    private static final String EVENT_SMART_DUAL_SIM_SWITCH = "event_smart_dual_sim_switch";
    private static final int EVENT_UPDATE_OOS_STATE_LINGER = 250;
    private static final String EVENT_USER_DUAL_SIM_SWITCH = "event_user_dual_sim_switch";
    private static final int EVENT_VOIP_ENABLED_CHANGED = 330;
    private static final int HIGH_SPEED = 1;
    private static final String HST_MODE_STATUS = "mIsHstMode";
    private static final int HST_WEAK_SIGNAL_SAMPLING_RATE_FOR_POST = 10;
    private static final int HST_WEAK_SIGNAL_SAMPLING_RATE_FOR_PRE = 80;
    private static final boolean IS_CUST_SINGLE_SIM;
    private static final boolean IS_INDIA;
    private static final String KEY_HIKING_STATE = "network_mode_state";
    private static final String KEY_PHONE_ID = "phoneId";
    private static final String KEY_SEAMLESS_SWITCH_STATE = "ril.temp.seamless_switch.state";
    private static final String KEY_SENSOR_DRIVE = "sensor_drive_state";
    private static final String KEY_SLOT_NUM = "slotNum";
    private static final String KEY_STATUS = "limit_status";
    private static final String KEY_SUBWAY_STATE = "network_mode_state";
    private static final String KEY_SWITCH_SCENE = "switchScene";
    private static final String KEY_TEMP_DDS_STATE = "ril.temp.dds.state";
    private static final String LATENCY_KEY_LEVEL_DL = "Level_DL";
    private static final String LATENCY_KEY_LEVEL_UL = "Level_UL";
    private static final long LEVEL1 = 1;
    private static final long LEVEL2 = 2;
    private static final String LOG_TAG = "SmartDualSimSwitch";
    private static final int LOW_SPEED = 0;
    private static final int LTENR_RSRP_DIFF_THRESHOLD = 16;
    private static final int LTENR_RSRP_GOOD_THRESHOLD = -105;
    private static final int LTENR_RSRP_GOOD_THRESHOLD_BROADER = -100;
    private static final int LTENR_RSRP_GOOD_THRESHOLD_FAKE_CON = -115;
    private static final int LTENR_RSRP_GOOD_THRESHOLD_STREAM_DATASTALL = -105;
    private static final int LTENR_RSRP_WEAK_THRESHOLD = -115;
    private static final int LTE_RSRP_GOOD_THRESHOLD = -97;
    private static final int LTE_RSRP_GOOD_THRESHOLD_GAME = -100;
    private static final int LTE_RSRP_WEAK_THRESHOLD = -110;
    private static final int LTE_RSRQ_GOOD_THRESHOLD = -11;
    private static final int LTE_RSRQ_WEAK_THRESHOLD = -16;
    private static final int MAX_SIGNAL_SAMPLING_CN = 40;
    private static final String MODEM_AI_KEY_RESULT = "result";
    private static final String MODEM_AI_KEY_SCENE = "modemAiScene";
    private static final String MULTISIM_DUALDATA_STATUS = "ril.multisim.dualdata_status";
    private static final int NA_RAT = -1;
    private static final int NETWORK_CONNECTED = 0;
    private static final int NETWORK_DISCONNECTED = 1;
    private static final String NOTIFICATION_RECEIVER_PACKAGE = "com.android.phone";
    private static final int NO_LIMIT = 0;
    private static final int NR_RSRP_GOOD_THRESHOLD = -85;
    private static final int NR_RSRP_GOOD_THRESHOLD_GAME = -95;
    private static final int NR_RSRP_WEAK_THRESHOLD = -105;
    private static final String PARAM_KEY_ACTIVE_RESTORE_RECORD = "records";
    private static final String PARAM_KEY_RESTORE_COUNT = "restoreCnt";
    private static final String PARAM_KEY_SIGNAL_DIFF_SWITCH_COUNT_HST = "signalDiffSwitchCntHst";
    private static final String PARAM_KEY_SWITCH_COUNT = "switchCnt";
    private static final String PARAM_KEY_SWITCH_INTERVAL = "switchInterval";
    private static final String PARAM_KEY_USER_SWITCH_SIGNAL_INFO = "userSwitchSignalInfo";
    private static final int PREDICT_INTERVAL = 2000;
    private static final int QCOM_STATE_DSDA = 3;
    private static final int SCENE_DEFAULT_CODE = 0;
    private static final int SCENE_HST_CODE = 1;
    private static final int SCENE_SUBWAY_CODE = 2;
    private static final int SCREEN_DELAY_TIME = 4000;
    private static final int SCREEN_DELAY_TIMEOUT = 10000;
    private static final int SCREEN_STATE_OFF = 0;
    private static final int SCREEN_STATE_ON = 1;
    private static final String[] SEAMLEASS_SWITCH_WHITE_LIST;
    private static final int SEAMLESS_SWITCH_LIMIT_TIME = 4000;
    private static final int SENSOR_STATE_DRIVE = 10;
    private static final int SIGLA_WEAK_CNT_THRESHOLD = 5;
    private static final int SIGNAL_DIFF_NEED_SWITCH_MIN_CNT = 14;
    private static final int SIGNAL_DIFF_NEED_SWITCH_RATE = 70;
    private static final int SIGNAL_DIFF_SAMPLING_TIMEROUT = 20;
    private static final int SIGNAL_SAMPLING_TIMEROUT_DRIVING = 15;
    private static final int SIGNAL_SAMPLING_TIMEROUT_NORMAL = 30;
    private static final String SIGNAL_STRONG = "STRONG";
    private static final String SIGNAL_WAEK = "WEAK";
    private static final String SLM_FEATURE_STATUS = "com.android.phone.intent.action.SLM_STATUS";
    private static final int SLM_ON = 1;
    private static final String SMART_DUAL_SIM = "smart_dual_sim";
    private static final int SMART_DUAL_SIM_OFF = 0;
    private static final int SMART_DUAL_SIM_ON = 1;
    private static final int STATE_HIKING_IN = 4;
    private static final int STATE_SUBWAY = 3;
    private static final String TELEPHONY_STATISTIC_CATEGORY = "telephony_statistic";
    private static final int TRAFFIC_EXCEPTION = -1;
    private static final int TRAFFIC_LIMIT = 1;
    private static final int TRAFFIC_LIMIT_PROTECT_TIME = 43200000;
    private static final int UNAVAILABLE = Integer.MAX_VALUE;
    private static final int UNKNOWN = -1;
    private static final Uri URI_MOBILE_TRAFFIC_LIMIT_SUBID;
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private static final int WEAK_SIGNAL_PREDICTION = 1;
    private static final int WEAK_SIGNAL_SAMPLING_RATE_FOR_POST = 10;
    private static final int WEAK_SIGNAL_SAMPLING_RATE_FOR_PRE = 60;
    private static final String WECHAT_VOIP_ENABLED = "wechatVoipEnabled";
    private static final String WECHAT_VOIP_SCENE = "com.android.phone.intent.action.WECHAT_VOIP_SCENE";
    private static final int WIFI_RSSI_WEAK_THRESHOLD_GAME = -71;
    public static int mTempDataPhoneId;
    private static WifiManager mWifiManager;
    private boolean isSmartDualSimOn;
    private ActiveRestoreRecord mActiveRestoreRecord;
    private AudioManager mAudioManager;
    private String[] mCellId;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private final boolean mDualDataFeatureSupported;
    private boolean mIsAirPlaneMode;
    private boolean mIsDriving;
    private boolean mIsScreenOn;
    private boolean mIsWifiConnected;
    private int mLastInsertedSimCn;
    private String mLastRegion;
    private int[] mLteRsrp;
    private int mLteRsrpGoodThreshold;
    private int mLteRsrpWeakThreshold;
    private int[] mLteRsrq;
    private int mLteRsrqGoodThreshold;
    private int mLteRsrqWeakThreshold;
    private int[] mNrRsrp;
    private int mNrRsrpGoodThreshold;
    private int mNrRsrpWeakThreshold;
    private int mPhoneNum;
    private ValidStatus[] mPhoneValidStatus;
    private final Phone[] mPhones;
    private PowerManager mPowerManager;
    private final RegistrantList mReEvalueRegistrants;
    private ContentResolver mResolver;
    private long[] mRestoreByTrafficLimitTime;
    private ServiceState[] mServiceState;
    private int[] mServiceStateHashCode;
    private final SettingsObserver mSettingsObserver;
    private SignalDiffStatistic mSignalDiffStatistic;
    private SignalStatistic mSignalStatistic;
    private final boolean mSmartDualSimFeatureSupported;
    private SpeedRateEstimate mSpeedRateEstimate;
    private SpeedRateEstimateListener mSpeedRateEstimateListener;
    private boolean mUserDataEnabled;
    private PowerManager.WakeLock mWakeLock;
    private int mWifiRssiThreshold;
    private static boolean VDBG = false;
    private static SmartDualSimSwitch sSmartDualSimSwitch = null;
    private String mLastDualSimSwitchCellId = "";
    private int mDefaultDataPhoneId = -1;
    private TelephonyManager mTelephonyManager = null;
    private Object mRegistrantidentifier = new Object();
    private TelephonyInfoCallBack mTelephonyInfoCallBack = null;
    private long mLastGameSwitchTime = 0;
    private boolean mIsGaming = false;
    private boolean mIsMobileConnected = false;
    private int mDdsRat = -1;
    private int mNonDdsRat = -1;
    private int mLastDdsPhoneId = -1;
    private boolean mIsInTempDdsSwitchForSds = false;
    private boolean mIsDdsChanging = false;
    private boolean mIsSignalMonitorStatus = false;
    private boolean mIsSignalDiffMonitorStatus = false;
    private boolean mSlmEnabled = false;
    private boolean mIsFiveGChanging = false;
    private boolean mIsHighSpeedTrain = false;
    private long mRequestViceNetworkTime = -1;
    private boolean mIsWechatVoipForgroud = false;
    private boolean mIsRegServiceState = false;
    private boolean mIsSubwayMode = false;
    private int mSigalWeakCnt = 0;
    private long mLastPredictTime = 0;
    private boolean mIsAudioRecording = false;
    private int mSwitchCnt = 0;
    private int mRestoreCnt = 0;
    private List<Long> mSwitchIntervalList = new ArrayList();
    private long mLastOnActiveTime = 0;
    private List<ActiveRestoreRecord> mActiveRestoreRecordList = new ArrayList();
    private int mSignalDiffSwitchCntHst = 0;
    private List<String> mUserSwitchSignalInfoList = new ArrayList();
    private ActiveScene mLastScene = ActiveScene.DUMMY;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(100, intent).sendToTarget();
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(30, intent).sendToTarget();
                return;
            }
            if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(70, intent).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_AIRPLANE_MODE_CHANGED, intent).sendToTarget();
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_SIM_STATE_CHANGED, intent).sendToTarget();
                return;
            }
            if (SmartDualSimSwitch.ACTION_NETWORK_TRAFFIC_LIMIT.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(130, intent).sendToTarget();
                return;
            }
            if (SmartDualSimSwitch.SLM_FEATURE_STATUS.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_SLM_STATUS_CHANGED, intent).sendToTarget();
                return;
            }
            if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_DEFAULT_DATA_SLOT_CHANGED).sendToTarget();
                return;
            }
            if (SmartDualSimSwitch.ACTION_HST_MODE_STATUS_CHANGED.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_HST_MODE_STATUS_CHANGED, intent).sendToTarget();
                return;
            }
            if (SmartDualSimSwitch.ACTION_DEBUG_ALL.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(1000, intent).sendToTarget();
                return;
            }
            if (SmartDualSimSwitch.ACTION_NOTIFY_SIM_SWITCH.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_NOTI_SIM_SWITCH, intent).sendToTarget();
                return;
            }
            if ("com.android.phone.intent.action.WECHAT_VOIP_SCENE".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_VOIP_ENABLED_CHANGED, intent).sendToTarget();
            } else if ("miui.intent.action.MIUI_REGION_CHANGED".equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_MIUI_REGION_CHANGED).sendToTarget();
            } else if (SmartDualSimSwitch.ACTION_MODEM_AI_WEAK_PERCEPTION.equals(action)) {
                SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_MODEM_AI_PREDICT, intent).sendToTarget();
            }
        }
    };
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.2
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            int defaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
            SmartDualSimSwitch.this.updateDefaultDataPhoneId(defaultDataSubId, "SubscriptionsChanged");
        }
    };
    ContentObserver mDataEnabledObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.3
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            boolean curUserDataEnabled = SmartDualSimSwitch.this.isMobileDataSettingOn();
            SmartDualSimSwitch.logd("data state :" + curUserDataEnabled + ",mUserDataEnabled=" + SmartDualSimSwitch.this.mUserDataEnabled);
            if (SmartDualSimSwitch.this.mUserDataEnabled != curUserDataEnabled) {
                SmartDualSimSwitch.this.mUserDataEnabled = curUserDataEnabled;
                if ((SmartDualSimSwitch.this.mUserDataEnabled && SmartDualSimSwitch.this.checkIsSwitchAvailable()) || (!SmartDualSimSwitch.this.mUserDataEnabled && SmartDualSimSwitch.this.isAllowedToRestore(false) && SmartDualSimSwitch.this.isTempDdsSwitchEnabled())) {
                    SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_DATA_SWITCH_CHANGED).sendToTarget();
                }
            }
        }
    };
    ContentObserver mSubwayStateObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.4
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            int state = SmartDualSimSwitch.this.getSubwayState();
            SmartDualSimSwitch.this.mIsSubwayMode = SmartDualSimSwitch.this.isSubwayState(state);
        }
    };
    ContentObserver mDrivingStateObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.5
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            boolean isDriving = SmartDualSimSwitch.this.isDrivingState();
            if (SmartDualSimSwitch.this.mIsDriving != isDriving) {
                SmartDualSimSwitch.this.startMonitorTimerForDrive();
            }
        }
    };
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.6
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            if (displayId == 0) {
                boolean isScreenOn = SmartDualSimSwitch.this.isScreenOn();
                if (SmartDualSimSwitch.VDBG) {
                    SmartDualSimSwitch.logd("onDisplayChanged isScreenOn=" + isScreenOn + ",mIsScreenOn=" + SmartDualSimSwitch.this.mIsScreenOn);
                }
                if (isScreenOn == SmartDualSimSwitch.this.mIsScreenOn) {
                    return;
                }
                SmartDualSimSwitch.this.mIsScreenOn = isScreenOn;
                if (SmartDualSimSwitch.this.checkIsSwitchAvailable()) {
                    SmartDualSimSwitch.this.obtainMessage(40).sendToTarget();
                }
            }
        }
    };
    private volatile boolean mIsDataRecovering = false;
    private ContentObserver mFiveGEnabledObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.7
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            SmartDualSimSwitch.this.fiveGChangedMonitorStatus();
        }
    };
    private AudioManager.AudioRecordingCallback mAudioRecordingCallback = new AudioManager.AudioRecordingCallback() { // from class: com.android.internal.telephony.datasub.SmartDualSimSwitch.8
        @Override // android.media.AudioManager.AudioRecordingCallback
        public void onRecordingConfigChanged(List<AudioRecordingConfiguration> configs) {
            int size = configs.size();
            SmartDualSimSwitch smartDualSimSwitch = SmartDualSimSwitch.this;
            if (size != 0) {
                smartDualSimSwitch.mIsAudioRecording = SmartDualSimSwitch.DBG;
            } else {
                smartDualSimSwitch.mIsAudioRecording = false;
            }
        }
    };

    static {
        DELAY_UPDATE_OOS_TIMEOUT = Build.IS_INTERNATIONAL_BUILD ? 10000 : CommenUtils.DELAY_IMS_STATE_UPDATE_TIMER;
        mTempDataPhoneId = -1;
        IS_CUST_SINGLE_SIM = SystemProperties.getInt("ro.miui.singlesim", 0) == 1;
        URI_MOBILE_TRAFFIC_LIMIT_SUBID = Uri.parse("content://com.miui.networkassistant.provider/traffic_limit/");
        SEAMLEASS_SWITCH_WHITE_LIST = new String[]{"com.ss.android.ugc.aweme"};
        VICE_SLOT_VOLTE_DATA_ENABLED = (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) ? "data_service_enabled" : DefaultSimManager.VICE_SLOT_VOLTE_DATA_ENABLED;
        IS_INDIA = "in".equalsIgnoreCase(SystemProperties.get(ModemUtils.PROP_MIUI_BUILD_REGION));
        EF_DRIVING_MAP_PN = new String[]{"com.baidu.BaiduMap", "com.autonavi.minimap"};
    }

    public enum ActiveScene {
        DUMMY(0),
        RAT(1),
        SETUP_FAILURE(2),
        SIGNAL_QOS(3),
        SIGNAL_MAP(4),
        SIGNAL_GAME(5),
        DUAL_DATA(6),
        MODEM_AI(7),
        FAKE_CONNECTION(8),
        SIGNAL_DIFF(9),
        STREAM_DATA_STALL(10);

        private int value;

        ActiveScene(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum ExitCode {
        DUMMY(0),
        INVALID_SIGNAL(1),
        EXIT_LINGER(2),
        SAMPLING_TIMEOUT(3),
        NORMAL_EXIT(4);

        private int value;

        ExitCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private enum RestoreScene {
        DUMMY(0),
        RAT(1),
        SETUP_FAILURE(2),
        SIGNAL_QOS(3),
        SIGNAL_GAME(4),
        MODEM_AI(5),
        SIGNAL_DIFF(6),
        FEATURE_BUTTON(7),
        DATA_BUTTON(8),
        SCREEN_OFF(9),
        SCREEN_ON(10),
        PLUG_OUT_SIM(11),
        AIRPLANE(12),
        SWITCH_DDS(13),
        WIFI(14),
        TRAFFIC(15),
        STREAM_DATA_STALL(16);

        private int value;

        RestoreScene(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private void processGameChangeLevel(Intent intent) {
        long level_ul = intent.getLongExtra(LATENCY_KEY_LEVEL_UL, LEVEL1);
        long level_dl = intent.getLongExtra(LATENCY_KEY_LEVEL_DL, LEVEL1);
        boolean isGaming = (level_ul == LEVEL2 && level_dl == LEVEL2) ? DBG : false;
        if (isGaming == this.mIsGaming) {
            return;
        }
        if (hasMessages(300)) {
            removeMessages(300);
        }
        if (hasMessages(EVENT_GAME_RESTORE_DELAY)) {
            removeMessages(EVENT_GAME_RESTORE_DELAY);
        }
        if (isGaming) {
            if (isGameSceneTempSwitch(false)) {
                sendMessageDelayed(obtainMessage(300), 20000L);
            }
        } else if (isGameSceneTempRestore()) {
            sendMessageDelayed(obtainMessage(EVENT_GAME_RESTORE_DELAY), 20000L);
        }
        logd("processGameChangeLevel mIsGaming(lastState):" + this.mIsGaming + "isGaming(curtate):" + isGaming);
        this.mIsGaming = isGaming;
    }

    private boolean isGameSceneTempSwitch(boolean isMobileRequired) {
        if (checkIsSwitchAvailable() && this.mIsWifiConnected && isWeakWifiRssi() && isDdsPoorerThanNdds() && ((isMobileDataAlwaysOn() || !isMobileRequired) && !isGameSwitchLingerTime())) {
            return DBG;
        }
        return false;
    }

    private boolean isGameSceneTempRestore() {
        if (this.mLastScene == ActiveScene.SIGNAL_GAME && isTempDdsSwitchRequiredForSds() && this.mIsWifiConnected) {
            return DBG;
        }
        return false;
    }

    private void regSettingsObserver() {
        this.mSettingsObserver.unobserve();
        this.mSettingsObserver.observe(Settings.Global.getUriFor(SMART_DUAL_SIM), 10);
    }

    private void regMobileDataObserver() {
        String uri = getMobileDataSettingName();
        this.mResolver.registerContentObserver(Settings.Global.getUriFor(uri), false, this.mDataEnabledObserver);
    }

    private String getMobileDataSettingName() {
        return TelephonyManager.getDefault().getPhoneCount() > 1 ? "mobile_data0" : "mobile_data";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataSettingOn() {
        if (Settings.Global.getInt(this.mResolver, getMobileDataSettingName(), 0) != 0) {
            return DBG;
        }
        return false;
    }

    private void regSubwayStateOberver() {
        this.mResolver.registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
    }

    private void regDrivingStateObserver() {
        this.mResolver.registerContentObserver(Settings.Global.getUriFor(KEY_SENSOR_DRIVE), false, this.mDrivingStateObserver);
    }

    public static SmartDualSimSwitch makeSmartDualSimSwitch(Context context, Phone[] phones) {
        if (context == null || phones == null) {
            logd("makeSmartDualSimSwitch params is null !!!");
        }
        if (sSmartDualSimSwitch == null) {
            sSmartDualSimSwitch = new SmartDualSimSwitch(context, phones);
        }
        logd("makeSmartDualSimSwitch");
        return sSmartDualSimSwitch;
    }

    public static SmartDualSimSwitch getInstance() {
        if (sSmartDualSimSwitch == null) {
            logd("getInstance is null !!!");
        }
        return sSmartDualSimSwitch;
    }

    private SmartDualSimSwitch(Context context, Phone[] phones) {
        this.mContext = null;
        this.mLastInsertedSimCn = -1;
        this.mUserDataEnabled = false;
        this.mIsAirPlaneMode = false;
        this.mIsDriving = false;
        this.mAudioManager = null;
        this.mContext = context;
        this.mPhones = phones;
        this.mPhoneNum = phones.length;
        this.mServiceState = new ServiceState[this.mPhoneNum];
        this.mCellId = new String[this.mPhoneNum];
        this.mServiceStateHashCode = new int[this.mPhoneNum];
        this.mLteRsrp = new int[this.mPhoneNum];
        this.mLteRsrq = new int[this.mPhoneNum];
        this.mNrRsrp = new int[this.mPhoneNum];
        this.mRestoreByTrafficLimitTime = new long[this.mPhoneNum];
        this.mPhoneValidStatus = new ValidStatus[this.mPhoneNum];
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mServiceState[i] = new ServiceState();
            this.mCellId[i] = "";
            this.mServiceStateHashCode[i] = this.mServiceState[i].hashCode();
            this.mLteRsrp[i] = UNAVAILABLE;
            this.mLteRsrq[i] = UNAVAILABLE;
            this.mNrRsrp[i] = UNAVAILABLE;
            this.mRestoreByTrafficLimitTime[i] = -1;
        }
        initPhoneValidStatus();
        this.mSignalStatistic = new SignalStatistic();
        this.mSignalDiffStatistic = new SignalDiffStatistic();
        this.mReEvalueRegistrants = new RegistrantList();
        this.mResolver = this.mContext.getContentResolver();
        this.mSettingsObserver = new SettingsObserver(this.mContext, this);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mDualDataFeatureSupported = TelephonyManagerEx.getDefault().isDualDataSupported();
        this.mSmartDualSimFeatureSupported = isSmartDualSimSwitchSupport();
        logd("Enter mSmartDualSimFeatureSupported=" + this.mSmartDualSimFeatureSupported);
        if (this.mSmartDualSimFeatureSupported) {
            this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
            mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
            this.mWakeLock = this.mPowerManager.newWakeLock(1, "SmartDualSim");
            this.mActiveRestoreRecord = new ActiveRestoreRecord();
            this.mLastInsertedSimCn = getInsertedSimCount();
            this.mIsAirPlaneMode = isAirPlaneModeOn();
            this.mUserDataEnabled = isMobileDataSettingOn();
            this.mIsDriving = isDrivingState();
            this.mLastRegion = SystemProperties.get("ro.miui.region", "unkonw");
            logd("init mUserDataEnabled=" + this.mUserDataEnabled + ",mLastInsertedSimCn=" + this.mLastInsertedSimCn);
            this.mSpeedRateEstimate = SpeedRateEstimate.init(this.mContext);
            this.mSpeedRateEstimateListener = new SpeedRateEstimateListener();
            this.mIsScreenOn = isScreenOn();
            registerEvents();
            this.isSmartDualSimOn = isSmartDualSimSettingsOn();
            logd("init isSmartDualSimOn=" + this.isSmartDualSimOn);
            if (isAllowedToRegServiceState()) {
                regServiceStateChangedEvent();
                if (isAllowedToRegCoreEvents()) {
                    registerCoreEvents();
                }
            }
            SystemProperties.set(KEY_TEMP_DDS_STATE, ModemUtils.PROP_BOOL_VALUE_OFF);
        }
    }

    public void dispose() {
        logd("dispose");
        if (this.mSmartDualSimFeatureSupported) {
            unregisterAllEvents();
        }
    }

    private void registerEvents() {
        regSettingsObserver();
        regMobileDataObserver();
        registerFivegEvents();
        regDrivingStateObserver();
        regSubwayStateOberver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.AIRPLANE_MODE");
        filter.addAction(ACTION_NETWORK_TRAFFIC_LIMIT);
        filter.addAction(SLM_FEATURE_STATUS);
        filter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        filter.addAction(ACTION_HST_MODE_STATUS_CHANGED);
        filter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        if (!Build.IS_INTERNATIONAL_BUILD) {
            filter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
            filter.addAction(ACTION_NOTIFY_SIM_SWITCH);
            if (this.mDualDataFeatureSupported && TelephonyManagerEx.getDefault().isSupportModemAiSignalSupported()) {
                filter.addAction(ACTION_MODEM_AI_WEAK_PERCEPTION);
            }
        }
        if (Build.IS_INTERNATIONAL_BUILD) {
            filter.addAction("miui.intent.action.MIUI_REGION_CHANGED");
        }
        filter.addAction(ACTION_DEBUG_ALL);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
        dm.registerDisplayListener(this.mDisplayListener, null);
        SubscriptionManager.from(this.mContext).addOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener);
        registerAudioRecordingCallback();
    }

    private void unregisterAllEvents() {
        logd("unregisterEvents");
        try {
            if (this.mSettingsObserver != null) {
                this.mSettingsObserver.unobserve();
            }
            if (this.mResolver != null) {
                this.mResolver.unregisterContentObserver(this.mDataEnabledObserver);
                this.mResolver.unregisterContentObserver(this.mDrivingStateObserver);
            }
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
            dm.unregisterDisplayListener(this.mDisplayListener);
            SubscriptionManager.from(this.mContext).removeOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener);
            unregServiceStateChangedEvent();
            unregisterCoreEvents();
            unregisterAudioRecordingCallback();
        } catch (Exception e) {
            logd("unregisterEvents" + e.getMessage());
        }
    }

    private void registerCoreEvents() {
        logd("registerCoreEvents");
        registerTelephonyInfoCallBack();
        if (!Build.IS_INTERNATIONAL_BUILD) {
            initSignalStrength();
        }
    }

    private void unregisterCoreEvents() {
        logd("unregisterCoreEvents");
        removeMessages(EVENT_PHONE_STATE_IDLE);
        unregisterTelephonyInfoCallBack();
    }

    private void regServiceStateChangedEvent() {
        if (!this.mSmartDualSimFeatureSupported || this.mIsRegServiceState) {
            return;
        }
        logd("regServiceStateChangedEvent");
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].registerForServiceStateChanged(this, i + 20, (Object) null);
        }
        this.mIsRegServiceState = DBG;
    }

    private void unregServiceStateChangedEvent() {
        if (!this.mSmartDualSimFeatureSupported || !this.mIsRegServiceState) {
            return;
        }
        logd("unregServiceStateChangedEvent");
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].unregisterForServiceStateChanged(this);
            this.mServiceState[i] = new ServiceState();
        }
        this.mIsRegServiceState = false;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        int phoneId = msg.what % 10;
        int eventId = msg.what - phoneId;
        if (VDBG) {
            logd("handleMessage changed eventId=" + eventId);
        }
        boolean isNormalScene = false;
        boolean isLowSpeed = DBG;
        switch (eventId) {
            case 10:
                boolean last = this.isSmartDualSimOn;
                boolean current = isSmartDualSimSettingsOn();
                if (last != current) {
                    this.isSmartDualSimOn = current;
                    if (!this.isSmartDualSimOn) {
                        unregisterCoreEvents();
                        if (isTempDdsSwitchEnabled()) {
                            mTempDataPhoneId = -1;
                            onRestoreDdsData(DBG, RestoreScene.FEATURE_BUTTON);
                            return;
                        }
                        return;
                    }
                    if (isAllowedToRegCoreEvents()) {
                        registerCoreEvents();
                        return;
                    }
                    return;
                }
                return;
            case 20:
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar != null && ar.exception == null && ar.result != null) {
                    processServiceStateChangeEvent(phoneId, (ServiceState) ar.result);
                    return;
                }
                return;
            case 30:
                Intent intent = (Intent) msg.obj;
                processNetworkStateChanged(intent);
                return;
            case 40:
                processScreenStateChanged();
                return;
            case 50:
                if (isTempDdsSwitchRequiredForSds() && isLastDdsDataNwNormal()) {
                    if (isAllowedToMonitorLowSpeed()) {
                        startSpeedRateEstimate();
                        sendMessageDelayed(obtainMessage(60), 10000L);
                        return;
                    } else {
                        synchronized (this.mWakeLock) {
                            releaseWakeLock();
                            break;
                        }
                        return;
                    }
                }
                synchronized (this.mWakeLock) {
                    releaseWakeLock();
                    break;
                }
                return;
            case 60:
                synchronized (this.mWakeLock) {
                    releaseWakeLock();
                    break;
                }
                return;
            case 70:
                processGameChangeLevel((Intent) msg.obj);
                return;
            case 80:
                removeMessages(60);
                if (msg.arg1 != 0) {
                    isLowSpeed = false;
                }
                boolean isScreenOn = isScreenOn();
                boolean isTempSwitch = isTempDdsSwitchRequiredForSds();
                logd("EVENT_LOW_SPEED_CALLBACK, isLowSpeed: " + isLowSpeed + ", isScreenOn: " + isScreenOn + ", isTempSwitch: " + isTempSwitch);
                if (isLowSpeed && !isScreenOn && isTempSwitch) {
                    onRestoreDdsData(false, RestoreScene.SCREEN_OFF);
                }
                synchronized (this.mWakeLock) {
                    releaseWakeLock();
                    break;
                }
                return;
            case EVENT_SIM_STATE_CHANGED /* 90 */:
                processSimStateChanged((Intent) msg.obj);
                return;
            case 100:
                processDefaultDataSubChanged((Intent) msg.obj);
                return;
            case EVENT_DATA_SWITCH_CHANGED /* 110 */:
                processDataSwitchChanged();
                return;
            case EVENT_DATA_STATISTIC_CHANGED /* 120 */:
                processDataStatisticChange(msg.arg1, msg.arg2, (HashMap) msg.obj);
                return;
            case 130:
                if (checkIsSwitchAvailable() && isTempDdsSwitchEnabled()) {
                    processNetworkTrafficLimit((Intent) msg.obj);
                    return;
                }
                return;
            case EVENT_NETWORK_SIGNAL_STRENGTH_CHANGED /* 140 */:
                if (checkIsSwitchAvailable() && !Build.IS_INTERNATIONAL_BUILD) {
                    processSignalStrengthEvent(msg.arg1, (SignalStrength) msg.obj);
                    return;
                }
                return;
            case EVENT_SLM_STATUS_CHANGED /* 150 */:
                Intent intentSlm = (Intent) msg.obj;
                int slmStatus = intentSlm.getIntExtra("coexistense", -1);
                isNormalScene = slmStatus == 1;
                this.mSlmEnabled = isNormalScene;
                logd("EVENT_SLM_STATUS_CHANGED status " + this.mSlmEnabled);
                return;
            case EVENT_DEFAULT_DATA_SLOT_CHANGED /* 160 */:
                processDefaultDataSlotChanged();
                return;
            case EVENT_CELL_LOCATION_CHANGED /* 170 */:
                processCellLocationChanged((CellLocation) msg.obj, msg.arg1);
                return;
            case 200:
                processNwWithoutLteNrEvent(((Integer) msg.obj).intValue());
                return;
            case EVENT_DDS_CHANGED_LINGER /* 210 */:
                exitDdsChangedMonitorStatus();
                return;
            case EVENT_NW_WEAK_SIGNAL_LINGER /* 220 */:
                int ddsPhoneId = ((Integer) msg.obj).intValue();
                if (isSignalMonitorAllowed(ddsPhoneId)) {
                    this.mSignalStatistic.pollUpdateSignalMetrics(ddsPhoneId, getAnotherPhoneId(ddsPhoneId));
                    switchDdsIfNeeded(ddsPhoneId);
                    return;
                } else {
                    exitSignalMonitorStatus(ExitCode.EXIT_LINGER);
                    return;
                }
            case EVENT_FIVEG_CHANGED_LINGER /* 230 */:
                exitFiveGChangedMonitorStatus();
                return;
            case EVENT_AIRPLANE_MODE_CHANGED /* 240 */:
                processAirplaneModeChanged();
                return;
            case 250:
                if (!Build.IS_INTERNATIONAL_BUILD) {
                    processOosState(((Integer) msg.obj).intValue());
                    return;
                } else {
                    if (isCustForInternational(this.mLastRegion)) {
                        processOosStateForGlobal(((Integer) msg.obj).intValue());
                        return;
                    }
                    return;
                }
            case EVENT_NW_WITHOUT_LTENR_AIRPLANE_LINGER /* 260 */:
                if (!Build.IS_INTERNATIONAL_BUILD) {
                    processNwWithoutLteNrEvent(((Integer) msg.obj).intValue());
                    return;
                } else {
                    if (isCustForInternational(this.mLastRegion)) {
                        processOosStateForGlobal(((Integer) msg.obj).intValue());
                        return;
                    }
                    return;
                }
            case EVENT_HST_MODE_STATUS_CHANGED /* 270 */:
                Intent intent2 = (Intent) msg.obj;
                this.mIsHighSpeedTrain = intent2.getBooleanExtra(HST_MODE_STATUS, false);
                return;
            case EVENT_SEAMLESS_SWITCH_LINGER /* 280 */:
                processSeamlessSwitchLinger(msg.arg1, (ActiveScene) msg.obj);
                return;
            case EVENT_SEAMLESS_SWITCH_RELEASE_LINGER /* 290 */:
            case EVENT_DUALDATA_RELEASE_LINGER /* 370 */:
                TelephonyManagerEx.getDefault().releaseNetworkBySlot(((Integer) msg.obj).intValue());
                SystemProperties.set(KEY_SEAMLESS_SWITCH_STATE, ModemUtils.PROP_BOOL_VALUE_OFF);
                return;
            case 300:
                if (this.mIsGaming && isGameSceneTempSwitch(DBG) && !Build.IS_INTERNATIONAL_BUILD) {
                    processGameSceneSwitch();
                    return;
                }
                return;
            case EVENT_GAME_RESTORE_DELAY /* 310 */:
                if (!this.mIsGaming && isGameSceneTempRestore()) {
                    onRestoreDdsData(false, RestoreScene.SIGNAL_GAME);
                    return;
                }
                return;
            case EVENT_NOTI_SIM_SWITCH /* 320 */:
                Intent intent3 = (Intent) msg.obj;
                if (!Build.IS_INTERNATIONAL_BUILD) {
                    processNotifySimSwitch(intent3);
                    return;
                }
                return;
            case EVENT_VOIP_ENABLED_CHANGED /* 330 */:
                Intent intent4 = (Intent) msg.obj;
                boolean isVoipForgroud = intent4.getBooleanExtra("wechatVoipEnabled", false);
                logd("isLastVoipForgroud=" + this.mIsWechatVoipForgroud + ", isNowVoipForgroud=" + isVoipForgroud);
                if (this.mIsWechatVoipForgroud != isVoipForgroud) {
                    this.mIsWechatVoipForgroud = isVoipForgroud;
                    return;
                }
                return;
            case EVENT_MIUI_REGION_CHANGED /* 340 */:
                processMiuiRegionChanged();
                return;
            case EVENT_DRIVE_STATE_CHANGED_LINGER /* 350 */:
                processDriveStateChanged();
                return;
            case EVENT_MODEM_AI_PREDICT /* 360 */:
                if (this.mIsSubwayMode && checkIsSwitchAvailable() && this.mUserDataEnabled && isSeamlessWhiteList()) {
                    isNormalScene = true;
                }
                if (!isNormalScene) {
                    return;
                }
                Intent intent5 = (Intent) msg.obj;
                processModemAiPredict(intent5);
                return;
            case EVENT_NW_SIGNAL_DIFF_LINGER /* 380 */:
                int ddsPhoneId2 = ((Integer) msg.obj).intValue();
                if (isSignalDiffMonitorAllowed(ddsPhoneId2)) {
                    this.mSignalDiffStatistic.pollUpdateSignalDiffMetrics(ddsPhoneId2, getAnotherPhoneId(ddsPhoneId2));
                    switchDdsIfNeededBySignalDiff(ddsPhoneId2);
                    return;
                } else {
                    exitSignalDiffMonitorStatus(ExitCode.EXIT_LINGER);
                    return;
                }
            case EVENT_PHONE_STATE_IDLE /* 390 */:
                processPhoneStateIdle(((Integer) msg.obj).intValue());
                return;
            case 1000:
                VDBG = DBG;
                Intent intent6 = (Intent) msg.obj;
                this.mLteRsrpWeakThreshold = intent6.getIntExtra("weak_signal_rsrp", LTE_RSRP_WEAK_THRESHOLD);
                this.mLteRsrpGoodThreshold = intent6.getIntExtra("good_signal_rsrp", LTE_RSRP_GOOD_THRESHOLD);
                this.mLteRsrqWeakThreshold = intent6.getIntExtra("weak_signal_rsrq", LTE_RSRQ_WEAK_THRESHOLD);
                this.mLteRsrqGoodThreshold = intent6.getIntExtra("good_signal_rsrq", LTE_RSRQ_GOOD_THRESHOLD);
                this.mNrRsrpWeakThreshold = intent6.getIntExtra("nr_weak_signal_rsrp", -105);
                this.mNrRsrpGoodThreshold = intent6.getIntExtra("nr_good_signal_rsrp", NR_RSRP_GOOD_THRESHOLD);
                this.mWifiRssiThreshold = intent6.getIntExtra("wifi_weak_rssi", WIFI_RSSI_WEAK_THRESHOLD_GAME);
                logd("EVENT_DEBUG_ALL VDBG[" + VDBG + "],rsrpWeak[" + this.mLteRsrpWeakThreshold + "],rsrpGood[" + this.mLteRsrpGoodThreshold + "],rsrqWeak[" + this.mLteRsrqWeakThreshold + "],rsrqGood[" + this.mLteRsrqGoodThreshold + "],nrRsrpWeak[" + this.mNrRsrpWeakThreshold + "],nrRsrpGood[" + this.mNrRsrpGoodThreshold + "],mWifiRssiThreshold[" + this.mWifiRssiThreshold + "]");
                return;
            default:
                logd("Unhandled message with number:" + msg.what);
                return;
        }
    }

    private void processMiuiRegionChanged() {
        String region = SystemProperties.get("ro.miui.region", "unkonw");
        logd("processMiuiRegionChanged, lastRegion=" + this.mLastRegion + ", newRegion=" + region);
        if (this.mLastRegion.equals(region)) {
            return;
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), SMART_DUAL_SIM, 0);
        Settings.Global.putInt(this.mContext.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0);
        boolean mIsLastCustForInternational = isCustForInternational(this.mLastRegion);
        boolean mIsCurCustForInternational = isCustForInternational(region);
        if (mIsLastCustForInternational && !mIsCurCustForInternational) {
            unregServiceStateChangedEvent();
        } else if (!mIsLastCustForInternational && mIsCurCustForInternational && isAllowedToRegServiceState()) {
            regServiceStateChangedEvent();
        }
        this.mLastRegion = region;
    }

    private void processNotifySimSwitch(Intent intent) {
        int phoneId = intent.getIntExtra(KEY_PHONE_ID, -1);
        String scene = intent.getStringExtra(KEY_SWITCH_SCENE);
        if (!isValidatePhoneId(phoneId) || TextUtils.isEmpty(scene)) {
            return;
        }
        int ddsId = getDefaultDataSlotId();
        logd("processNotifySimSwitch phoneId=" + phoneId + ", ddsId=" + ddsId + ", scene=" + scene);
        if (!this.mIsDdsChanging && phoneId == ddsId && shouldTempSwitchDataToSlave(phoneId) && !isNr2LteSwitch(phoneId, getAnotherPhoneId(phoneId)) && !shoudAvoidOperatorScene(phoneId)) {
            if ("SIGNAL_MAP".equals(scene)) {
                onActiveNonDdsData(getAnotherPhoneId(ddsId), ActiveScene.SIGNAL_MAP);
            } else if ("DUAL_DATA".equals(scene)) {
                onActiveNonDdsData(getAnotherPhoneId(ddsId), ActiveScene.DUAL_DATA);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScreenOn() {
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
        Display[] displays = dm.getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    return DBG;
                }
            }
            return false;
        }
        logd("No displays found");
        return false;
    }

    private void processScreenStateChanged() {
        removeMessages(50);
        removeMessages(60);
        stopSpeedRateEstimate();
        if (this.mIsScreenOn) {
            logd("STATE ON, mTempDataSwitch: " + isTempDdsSwitchRequiredForSds() + ",mLastScene=" + this.mLastScene);
            synchronized (this.mWakeLock) {
                releaseWakeLock();
            }
            if (isTempDdsSwitchRequiredForSds() && isLastDdsDataNwNormal() && isBroaderGoodSignal(this.mLastDdsPhoneId) && !isHikingMode() && isLastDdsCellChanged()) {
                onRestoreDdsData(false, RestoreScene.SCREEN_ON);
                return;
            }
            return;
        }
        logd("STATE OFF, mTempDataSwitch: " + isTempDdsSwitchRequiredForSds() + ",mLastScene=" + this.mLastScene);
        if (!isTempDdsSwitchRequiredForSds() || !isLastDdsDataNwNormal() || !isLastDdsCellChanged()) {
            return;
        }
        synchronized (this.mWakeLock) {
            acquireWakeLock();
        }
        sendMessageDelayed(obtainMessage(50), 4000L);
    }

    public boolean isLastDdsCellChanged() {
        boolean rst = DBG;
        if (this.mLastScene == ActiveScene.SETUP_FAILURE || this.mLastScene == ActiveScene.FAKE_CONNECTION || this.mLastScene == ActiveScene.SIGNAL_MAP) {
            rst = (!isValidatePhoneId(this.mLastDdsPhoneId) || TextUtils.isEmpty(this.mLastDualSimSwitchCellId) || TextUtils.isEmpty(this.mCellId[this.mLastDdsPhoneId]) || this.mLastDualSimSwitchCellId.equals(this.mCellId[this.mLastDdsPhoneId])) ? false : DBG;
        }
        logd("isLastDdsCellChanged rst=" + rst);
        return rst;
    }

    public boolean isSmartDualSimSettingsOn() {
        int smartDualSimSettings = Settings.Global.getInt(this.mResolver, SMART_DUAL_SIM, 0);
        if (smartDualSimSettings == 1) {
            return DBG;
        }
        return false;
    }

    private void acquireWakeLock() {
        if (this.mWakeLock != null && !this.mWakeLock.isHeld()) {
            logd("acquire WakeLock.");
            this.mWakeLock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            logd("release WakeLock.");
            this.mWakeLock.release();
        }
    }

    public void updateDefaultDataPhoneId(int currDataSubId, String reason) {
        if (SubscriptionManager.isValidSubscriptionId(currDataSubId)) {
            int newDefaultDataPhoneId = SubscriptionManager.getPhoneId(currDataSubId);
            if (this.mLastDdsPhoneId == -1) {
                this.mLastDdsPhoneId = newDefaultDataPhoneId;
                logd("first time to update mLastDdsPhoneId=" + this.mLastDdsPhoneId + " reason:" + reason);
            } else if (newDefaultDataPhoneId != this.mLastDdsPhoneId) {
                removeMessages(EVENT_PHONE_STATE_IDLE);
                recordDualSimSignal(this.mLastDdsPhoneId);
                this.mLastDdsPhoneId = newDefaultDataPhoneId;
                logd("updateDefaultDataPhoneId() mLastDdsPhoneId=" + this.mLastDdsPhoneId + " reason:" + reason);
                enterDdsChangedMonitorStatus();
            }
        }
    }

    private void recordDualSimSignal(int phoneId) {
        if (this.mLastInsertedSimCn < 2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mPhoneNum; i++) {
            String str = getRsrpByPhoneId(i) + "|" + getCurRatByPhoneId(i) + "+";
            sb.append(str);
        }
        sb.append(phoneId);
        this.mUserSwitchSignalInfoList.add(sb.toString());
    }

    private ConnectivityManager getConnectivityManager() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        }
        return this.mConnectivityManager;
    }

    private TelephonyManager getTelephonyManager() {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        }
        return this.mTelephonyManager;
    }

    private synchronized void processServiceStateChangeEvent(int phoneId, ServiceState ss) {
        logd("processServiceStateChangeEvent phoneId=" + phoneId);
        if (ss != null && isValidatePhoneId(phoneId)) {
            logd("processServiceStateChangeEvent manual=" + ss.getIsManualSelection());
            if (!ss.getIsManualSelection() && isPhoneStateIdle()) {
                if (!Build.IS_INTERNATIONAL_BUILD) {
                    updateOosState(phoneId, ss);
                    monitorDataNwChange(phoneId, ss);
                } else if (isCustForInternational(this.mLastRegion)) {
                    updateOosStateForGlobal(phoneId, ss);
                    this.mServiceState[phoneId] = new ServiceState(ss);
                }
            } else {
                this.mServiceState[phoneId] = new ServiceState(ss);
            }
        } else {
            logd("processServiceStateChangeEvent invalid params!!!");
        }
        logd("processServiceStateChangeEvent exit phoneId=" + phoneId);
    }

    private void monitorDataNwChange(int phoneId, ServiceState ss) {
        if (this.mServiceState[phoneId] == null || ss == null) {
            logd("monitorDataNwChange invalid params!");
            return;
        }
        int newRat = ss.getDataNetworkType();
        int oldRat = this.mServiceState[phoneId].getDataNetworkType();
        if (!isValidRat(newRat, phoneId)) {
            logd("monitorDataNwChange ignore unknown rat!");
            return;
        }
        boolean isOldRatInitial = isServiceStateInitial(phoneId);
        boolean zIsLteNrNwType = isLteNrNwType(oldRat);
        boolean nwFirstDeregistered = DBG;
        boolean nwRegistered = !zIsLteNrNwType && isLteNrNwType(newRat);
        boolean nwDeregistered = isLteNrNwType(oldRat) && !isLteNrNwType(newRat);
        if (!isOldRatInitial || isLteNrNwType(newRat)) {
            nwFirstDeregistered = false;
        }
        if (nwRegistered || nwDeregistered || nwFirstDeregistered || VDBG || this.mIsDdsChanging || this.mIsFiveGChanging) {
            logd("monitorDataNwChange newRat:[" + newRat + "],oldRat:[" + oldRat + "],isOldRatInitial:[" + isOldRatInitial + "],nwRegistered:[" + nwRegistered + "],nwDeregistered:[" + nwDeregistered + "],mIsDdsChanging:[" + this.mIsDdsChanging + "],phoneid[" + phoneId + "],mIsFiveGChanging:[" + this.mIsFiveGChanging + "]");
        }
        this.mServiceState[phoneId] = new ServiceState(ss);
        if (phoneId == this.mLastDdsPhoneId) {
            this.mDdsRat = ss.getDataNetworkType();
            if (nwRegistered) {
                stopMonitorTimer();
                stopMonitorTimerForAirplane();
                if (isTempDdsSwitchRequiredForSds() && !isNwNormal(getAnotherPhoneId(phoneId))) {
                    onRestoreDdsData(false, RestoreScene.RAT);
                    return;
                }
                return;
            }
            if ((nwDeregistered || nwFirstDeregistered) && !this.mIsDdsChanging && !this.mIsFiveGChanging && shouldTempSwitchDataToSlave(phoneId)) {
                startMonitorTimer(phoneId);
                return;
            }
            return;
        }
        this.mNonDdsRat = ss.getDataNetworkType();
        if ((nwDeregistered || nwFirstDeregistered) && isTempDdsSwitchRequiredForSds() && isNwNormal(this.mLastDdsPhoneId)) {
            onRestoreDdsData(false, RestoreScene.RAT);
        }
    }

    private void updateOosState(int phoneId, ServiceState ss) {
        if (this.mServiceState[phoneId] == null || ss == null) {
            logd("updateOosState invalid params!");
            return;
        }
        int newRegisterState = ss.getDataRegistrationState();
        if (newRegisterState == 0) {
            stopMonitorTimerForOosUpdate(phoneId);
            return;
        }
        int oldRat = this.mServiceState[phoneId].getDataNetworkType();
        int oldRegisterState = this.mServiceState[phoneId].getDataRegistrationState();
        boolean isNewDeRegisterState = DBG;
        boolean isOldRegisterState = oldRegisterState == 0;
        if (newRegisterState != 1) {
            isNewDeRegisterState = false;
        }
        if (isOldRegisterState || isNewDeRegisterState || VDBG || this.mIsDdsChanging || this.mIsFiveGChanging) {
            logd("updateOosState oldRat:[" + oldRat + "],isOldRegisterState=" + isOldRegisterState + ",isNewDeRegisterState=" + isNewDeRegisterState + ",mIsDdsChanging=" + this.mIsDdsChanging + ",mIsFiveGChanging=" + this.mIsFiveGChanging);
        }
        if (isOldRegisterState && isLteNrNwType(oldRat) && isNewDeRegisterState) {
            if (phoneId == this.mLastDdsPhoneId) {
                if (!this.mIsDdsChanging && !this.mIsFiveGChanging && shouldTempSwitchDataToSlave(phoneId)) {
                    startMonitorTimerForOosUpdate(phoneId);
                    return;
                }
                return;
            }
            this.mNonDdsRat = ss.getDataNetworkType();
            if (isTempDdsSwitchRequiredForSds() && isNwNormal(this.mLastDdsPhoneId)) {
                onRestoreDdsData(false, RestoreScene.RAT);
            }
        }
    }

    private void updateOosStateForGlobal(int phoneId, ServiceState ss) {
        if (this.mServiceState[phoneId] == null || ss == null) {
            logd("updateOosStateForGlobal invalid params!");
            return;
        }
        int oldRat = this.mServiceState[phoneId].getDataNetworkType();
        boolean isOldRatInitial = isServiceStateInitial(phoneId);
        boolean isOldRegisterState = isNwInServiceForGlobal(this.mServiceState[phoneId]);
        boolean isNewDeRegisterState = isNwOosForGlobal(ss);
        boolean isOldDeRegisterState = isNwOosForGlobal(this.mServiceState[phoneId]);
        boolean isNewRegisterState = isNwInServiceForGlobal(ss);
        boolean nwFirstDeRegistered = (isOldRatInitial && isNewDeRegisterState) ? DBG : false;
        if (isOldRegisterState || nwFirstDeRegistered || isNewDeRegisterState || isNewRegisterState || isOldDeRegisterState || VDBG || this.mIsDdsChanging || this.mIsFiveGChanging) {
            logd("updateOosStateForGlobal oldRat:[" + oldRat + "],isOldRegisterState=" + isOldRegisterState + ",isOldRatInitial=" + isOldRatInitial + ",isNewDeRegisterState=" + isNewDeRegisterState + ",isOldDeRegisterState=" + isOldDeRegisterState + ",isNewRegisterState=" + isNewRegisterState + ",mIsDdsChanging=" + this.mIsDdsChanging + ",mIsFiveGChanging=" + this.mIsFiveGChanging);
        }
        if (phoneId == this.mLastDdsPhoneId) {
            if ((isOldRegisterState && isNewDeRegisterState) || nwFirstDeRegistered) {
                if (!this.mIsDdsChanging && !this.mIsFiveGChanging && shouldTempSwitchDataToSlave(phoneId)) {
                    startMonitorTimerForOosUpdate(phoneId);
                    return;
                }
                return;
            }
            if (isOldDeRegisterState && isNewRegisterState) {
                stopMonitorTimerForOosUpdate(phoneId);
                if (isTempDdsSwitchRequiredForSds()) {
                    onRestoreDdsData(false, RestoreScene.RAT);
                    return;
                }
                return;
            }
            return;
        }
        if (((isOldRegisterState && isNewDeRegisterState) || nwFirstDeRegistered) && isTempDdsSwitchRequiredForSds() && isNwNormal(this.mLastDdsPhoneId)) {
            onRestoreDdsData(false, RestoreScene.RAT);
        }
    }

    private void processOosState(int phoneId) {
        logd("processOosState phoneId=" + phoneId);
        if (isAllowedForSwitch(phoneId, DBG) && isNwNormal(getAnotherPhoneId(phoneId)) && isNwOos(phoneId) && !isTempDdsSwitchRequiredForSds()) {
            stopMonitorTimer();
            if (!this.isSmartDualSimOn) {
                if (this.mIsWifiConnected) {
                    return;
                }
                notifySmartSwitchDualSimAlertDialog();
                return;
            }
            onActiveNonDdsData(getAnotherPhoneId(phoneId), ActiveScene.RAT);
        }
    }

    private void processOosStateForGlobal(int phoneId) {
        logd("processOosStateForGlobal phoneId=" + phoneId);
        if (isAllowedForSwitch(phoneId, DBG) && isNwNormal(getAnotherPhoneId(phoneId))) {
            ServiceState ss = getServiceState(phoneId);
            if (isSimReady(phoneId) && isNwOosForGlobal(ss) && !isTempDdsSwitchRequiredForSds()) {
                if (!this.isSmartDualSimOn) {
                    if (this.mIsWifiConnected) {
                        return;
                    }
                    notifySmartSwitchDualSimAlertDialog();
                    return;
                }
                onActiveNonDdsData(getAnotherPhoneId(phoneId), ActiveScene.RAT);
            }
        }
    }

    private void processPhoneStateIdle(int phoneId) {
        logd("processPhoneStateIdle phoneId=" + phoneId);
        if (isValidatePhoneId(phoneId) && phoneId == this.mLastDdsPhoneId && isPhoneStateIdle() && !this.mIsDdsChanging && !this.mIsFiveGChanging && shouldTempSwitchDataToSlave(phoneId)) {
            if (!Build.IS_INTERNATIONAL_BUILD) {
                if (isNwOos(phoneId)) {
                    startMonitorTimerForOosUpdate(phoneId);
                    return;
                } else {
                    if (isNwOosOrWithoutLteNr(phoneId)) {
                        startMonitorTimer(phoneId);
                        return;
                    }
                    return;
                }
            }
            if (isCustForInternational(this.mLastRegion) && isNwOosForGlobal(getServiceState(phoneId))) {
                startMonitorTimerForOosUpdate(phoneId);
            }
        }
    }

    private boolean isNwOos(int phoneId) {
        ServiceState ss = getServiceState(phoneId);
        if (ss == null) {
            return false;
        }
        int registerState = ss.getDataRegistrationState();
        if (!isSimReady(phoneId) || registerState == 0) {
            return false;
        }
        return DBG;
    }

    private boolean isNwOosForGlobal(ServiceState ss) {
        if (ss == null) {
            return false;
        }
        int dataState = ss.getDataRegistrationState();
        int voiceState = ss.getVoiceRegState();
        if (dataState != 1 || voiceState != 1) {
            return false;
        }
        return DBG;
    }

    private boolean isNwInServiceForGlobal(ServiceState ss) {
        if (ss == null) {
            return false;
        }
        int dataState = ss.getDataRegistrationState();
        int voiceState = ss.getVoiceRegState();
        if (dataState == 0 || voiceState == 0) {
            return DBG;
        }
        return false;
    }

    private boolean isLteNwType(int rat) {
        if (rat == 13 || rat == 19) {
            return DBG;
        }
        return false;
    }

    private boolean isNrNwType(int rat) {
        if (rat == 20 || rat == 30) {
            return DBG;
        }
        return false;
    }

    private boolean isLteNrNwType(int rat) {
        if (rat == 13 || rat == 19 || rat == 20 || rat == 30) {
            return DBG;
        }
        return false;
    }

    private boolean isNr2LteSwitch(int curPhoneId, int otherPhoneId) {
        if (!isValidatePhoneId(curPhoneId) || !isValidatePhoneId(otherPhoneId)) {
            logd("isNr2LteSwitch invalid phoneid cur=" + curPhoneId + ",other=" + otherPhoneId);
            return false;
        }
        int curRat = getCurRatByPhoneId(curPhoneId);
        int otherRat = getCurRatByPhoneId(otherPhoneId);
        logd("isNr2LteSwitch curRat=" + curRat + ",otherRat=" + otherRat);
        if (VDBG) {
            logd("isNr2LteSwitch-ignore rst while VDBG is true");
            return false;
        }
        if (isNrNwType(curRat) && isLteNwType(otherRat)) {
            return DBG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidatePhoneId(int phoneId) {
        if (phoneId < 0 || phoneId >= this.mPhoneNum) {
            return false;
        }
        return DBG;
    }

    private void startMonitorTimer(int phoneId) {
        logd("startMonitorTimer phoneId=" + phoneId);
        stopMonitorTimer();
        sendMessageDelayed(obtainMessage(200, Integer.valueOf(phoneId)), CommenUtils.DELAY_IMS_STATE_UPDATE_TIMER);
    }

    private void stopMonitorTimer() {
        if (hasMessages(200)) {
            logd("stopLteMonitorTimer");
            removeMessages(200);
        }
    }

    private void startMonitorTimerForOosUpdate(int phoneId) {
        logd("startMonitorTimerForOosUpdate phoneId=" + phoneId);
        stopMonitorTimerForOosUpdate(phoneId);
        int delayInMs = DELAY_UPDATE_OOS_TIMEOUT;
        sendMessageDelayed(obtainMessage(250, Integer.valueOf(phoneId)), delayInMs);
    }

    private void stopMonitorTimerForOosUpdate(int phoneId) {
        if (hasMessages(250, Integer.valueOf(phoneId))) {
            logd("stopMonitorTimerForOosUpdate phoneId=" + phoneId);
            removeMessages(250, Integer.valueOf(phoneId));
        }
    }

    private void processNwWithoutLteNrEvent(int phoneId) {
        logd("processNwWithoutLteNrEvent phoneId=" + phoneId);
        if (isAllowedForSwitch(phoneId, DBG) && isNwNormal(getAnotherPhoneId(phoneId)) && isNwOosOrWithoutLteNr(phoneId) && !isTempDdsSwitchRequiredForSds()) {
            stopMonitorTimerForOosUpdate(phoneId);
            if (!this.isSmartDualSimOn) {
                if (this.mIsWifiConnected) {
                    return;
                }
                notifySmartSwitchDualSimAlertDialog();
                return;
            }
            onActiveNonDdsData(getAnotherPhoneId(phoneId), ActiveScene.RAT);
        }
    }

    private boolean isExceptionNwSceneForSwitch(int phoneId) {
        boolean rst = (this.mIsAirPlaneMode || isNonLteNrNwMode(phoneId)) ? DBG : false;
        if (rst) {
            logd("isExceptionNwScene: " + this.mIsAirPlaneMode + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + isNonLteNrNwMode(phoneId));
        }
        return rst;
    }

    private boolean isExceptionCommonSceneForSwitch(int phoneId) {
        boolean rst = (!isPhoneStateIdle() || (this.mIsWifiConnected && !isMobileDataAlwaysOn()) || !this.mUserDataEnabled || !isSimReady(phoneId) || isPreferDataApnUserEdited(getAnotherPhoneId(phoneId)) || this.mIsDataRecovering) ? DBG : false;
        if (rst) {
            logd("isExceptionCommonSceneForSwitch: " + isPhoneStateIdle() + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mIsWifiConnected + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mUserDataEnabled + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + isDefaultDataSlotId(phoneId) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mSlmEnabled + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + isPreferDataApnUserEdited(getAnotherPhoneId(phoneId)));
        }
        return rst;
    }

    public void setDataRecoverStatus(boolean isRecovering) {
        this.mIsDataRecovering = isRecovering;
    }

    private boolean isPreferDataApnUserEdited(int phoneId) {
        MiuiDataControllerImpl dataNetworkImpl = null;
        if (isValidatePhoneId(phoneId)) {
            dataNetworkImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phoneId);
        }
        if (dataNetworkImpl != null) {
            return dataNetworkImpl.isPreferDataApnUserEdited();
        }
        return false;
    }

    private boolean isAllowedForSwitch(int phoneId, boolean checkDefaultData) {
        boolean isException = isExceptionCommonSceneForSwitch(phoneId) || isExceptionNwSceneForSwitch(phoneId) || (checkDefaultData && !isDefaultDataSlotId(phoneId));
        if (isException) {
            logd("isAllowedForSwitch phoneId: " + phoneId + ",checkDefaultData:" + checkDefaultData + ",isException=" + isException);
        }
        if (isException) {
            return false;
        }
        return DBG;
    }

    private boolean isNwNormal(int phoneId) {
        boolean rst = false;
        ServiceState ss = getServiceState(phoneId);
        boolean zIsCustForInternational = isCustForInternational(this.mLastRegion);
        boolean z = DBG;
        if (zIsCustForInternational) {
            if (!isSimReady(phoneId) || !isNwInServiceForGlobal(ss)) {
                z = false;
            }
            rst = z;
        } else if (ss != null) {
            int rat = ss.getDataNetworkType();
            if (!isSimReady(phoneId) || isNwOos(phoneId) || !isLteNrNwType(rat) || !isAllowedDataEnabled(phoneId)) {
                z = false;
            }
            rst = z;
        }
        if (VDBG) {
            logd("isNwNormal phoneId=" + phoneId + ",rst=" + rst);
        }
        return rst;
    }

    private boolean isNwOosOrWithoutLteNr(int phoneId) {
        boolean rst = false;
        ServiceState ss = getServiceState(phoneId);
        boolean zIsCustForInternational = isCustForInternational(this.mLastRegion);
        boolean z = DBG;
        if (zIsCustForInternational) {
            if (!isSimReady(phoneId) || !isNwOosForGlobal(ss)) {
                z = false;
            }
            rst = z;
        } else if (ss != null) {
            if (!isSimReady(phoneId) || (!isNwOos(phoneId) && isLteNrNwType(ss.getDataNetworkType()))) {
                z = false;
            }
            rst = z;
        }
        if (VDBG) {
            logd("isNwOosOrWithoutLteNr phoneId=" + phoneId + ",rst=" + rst);
        }
        return rst;
    }

    private synchronized ServiceState getServiceState(int phoneId) {
        ServiceState ss = null;
        if (isValidatePhoneId(phoneId) && this.mServiceState[phoneId] != null) {
            if (this.mServiceStateHashCode[phoneId] == this.mServiceState[phoneId].hashCode()) {
                if (VDBG) {
                    logd("getDataRat mServiceState is original!!!");
                }
                if (this.mPhones[phoneId] != null) {
                    ss = this.mPhones[phoneId].getServiceState();
                }
            } else {
                ss = this.mServiceState[phoneId];
            }
        }
        if (ss != null) {
            return ss;
        }
        return new ServiceState();
    }

    private synchronized ServiceState getCurrentServiceState(int phoneId) {
        ServiceState ss;
        ss = null;
        if (isValidatePhoneId(phoneId) && this.mPhones[phoneId] != null) {
            ss = this.mPhones[phoneId].getServiceState();
        }
        return ss;
    }

    private boolean isServiceStateInitial(int phoneId) {
        if (this.mServiceState[phoneId] != null && this.mServiceStateHashCode[phoneId] == this.mServiceState[phoneId].hashCode()) {
            return DBG;
        }
        return false;
    }

    private boolean isAllowedDataEnabled(int phoneId) {
        if (isValidatePhoneId(phoneId) && this.mPhones[phoneId] != null) {
            return this.mPhones[phoneId].getDataSettingsManager().isDataEnabled();
        }
        return false;
    }

    private int getAnotherPhoneId(int phoneId) {
        if (isValidatePhoneId(phoneId)) {
            return phoneId == 0 ? 1 : 0;
        }
        return -1;
    }

    private boolean isAirPlaneModeOn() {
        if (Settings.Global.getInt(this.mResolver, "airplane_mode_on", 0) == 1) {
            return DBG;
        }
        return false;
    }

    private boolean isSimReady(int phoneId) {
        if (TelephonyManager.getDefault().getSimState(phoneId) == 5) {
            return DBG;
        }
        return false;
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private boolean isDefaultDataSlotId(int slotId) {
        if (slotId == getDefaultDataSlotId()) {
            return DBG;
        }
        return false;
    }

    private int getPreferredNwModeFromDb(int phoneId) {
        return DefaultSimManager.getPreferredNetworkModeFromDb(this.mContext, phoneId);
    }

    private void initSignalStrength() {
        logd("initSignalStrength");
        if (this.mPhones != null) {
            for (Phone phone : this.mPhones) {
                logd("initSignalStrength for phone = " + phone.getPhoneId());
                SignalStrength signalStrength = phone.getSignalStrength();
                processSignalStrengthEvent(phone.getPhoneId(), signalStrength);
            }
        }
    }

    private boolean isNonLteNrNwMode(int phoneId) {
        int nwMode = getPreferredNwModeFromDb(phoneId);
        boolean rst = false;
        if (nwMode == 0 || nwMode == 1 || nwMode == 2 || nwMode == 3 || nwMode == 5 || nwMode == 6 || nwMode == 7 || nwMode == 13 || nwMode == 14 || nwMode == 16 || nwMode == 18 || nwMode == 21) {
            rst = DBG;
        }
        if (VDBG) {
            logd("isNonLteNrNwMode nwMode:" + nwMode + "ignore rst while VDBG is true");
            return false;
        }
        return rst;
    }

    private boolean isLastDdsDataNwNormal() {
        boolean rst = false;
        if (mTempDataPhoneId != -1 && mTempDataPhoneId != this.mLastDdsPhoneId) {
            rst = isNwNormal(this.mLastDdsPhoneId);
        }
        if (VDBG) {
            logd("isLastDdsDataNwNormal :" + mTempDataPhoneId + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mLastDdsPhoneId + ",rst=" + rst);
        }
        return rst;
    }

    private boolean isRoamingState(int phoneId) {
        ServiceState ss = getServiceState(phoneId);
        if (ss != null) {
            return ss.getRoaming();
        }
        return false;
    }

    private boolean isSimNormal(int phoneId) {
        if (!isValidatePhoneId(phoneId)) {
            return false;
        }
        return this.mPhoneValidStatus[phoneId].isValid();
    }

    private synchronized void onActiveNonDdsData(int phoneId, ActiveScene scene) {
        if (isValidatePhoneId(phoneId) && isValidatePhoneId(this.mLastDdsPhoneId)) {
            if (isAllowActiveNonDdsData(phoneId, scene)) {
                logd("onActiveNonDdsData phoneId=" + phoneId + ",scene=" + scene);
                this.mLastDualSimSwitchCellId = this.mCellId[this.mLastDdsPhoneId];
                setTempDdsSwitch(DBG);
                this.mReEvalueRegistrants.notifyRegistrants(new AsyncResult((Object) null, new Integer(phoneId), (Throwable) null));
                this.mLastScene = scene;
                if (isDualDataConnected() && !isSeamlessSwitching()) {
                    sendMessageDelayed(obtainMessage(EVENT_DUALDATA_RELEASE_LINGER, Integer.valueOf(phoneId)), 2000L);
                }
                long curTime = SystemClock.elapsedRealtime();
                updateSwitchStatistic(scene, curTime);
                this.mLastOnActiveTime = curTime;
            }
            return;
        }
        logd("onActiveNonDdsData invalid phoneId=" + phoneId);
    }

    private void updateSwitchStatistic(ActiveScene type, long time) {
        this.mSwitchCnt++;
        this.mActiveRestoreRecord.setActiveInfo(type, time);
        int sceneCode = 0;
        if (this.mIsHighSpeedTrain) {
            sceneCode = 0 | 1;
        }
        if (this.mIsSubwayMode) {
            sceneCode |= 2;
        }
        this.mActiveRestoreRecord.setScene(sceneCode);
        if (this.mLastOnActiveTime != 0) {
            long interval = (time - this.mLastOnActiveTime) / 1000;
            this.mSwitchIntervalList.add(Long.valueOf(interval));
        }
    }

    private boolean isAllowActiveNonDdsData(int phoneId, ActiveScene scene) {
        boolean isSwitchAvailable = checkIsSwitchAvailable();
        boolean isRoaming = isRoamingState(phoneId);
        boolean isSimNormal = isSimNormal(phoneId);
        boolean rst = false;
        boolean isTrafficAvailable = getMobileTrafficLimitStatus(phoneId) < 1 && !isInTrafficLimitTime(phoneId);
        boolean isVirtualSim = isVirtualSimBySlot(this.mContext, getAnotherPhoneId(phoneId));
        boolean isVirtualSeedSim = isVirtualSeedSimBySlot(getAnotherPhoneId(phoneId));
        if (isSwitchAvailable && !isRoaming && isSimNormal && isTrafficAvailable && !isVirtualSim && !isVirtualSeedSim) {
            rst = true;
        }
        if (!rst) {
            logd("onActiveNonDdsData isSwitchAvailable=" + isSwitchAvailable + ",isRoaming=" + isRoaming + ",isSimNormal=" + isSimNormal + ",isTrafficAvailable=" + isTrafficAvailable + ",isInTrafficLimitTime=" + isInTrafficLimitTime(phoneId) + ",isVirtualSim=" + isVirtualSim + ",isVirtualSeedSim=" + isVirtualSeedSim + ",scene=" + scene);
        }
        return rst;
    }

    private boolean isInTrafficLimitTime(int phoneId) {
        long curTime = SystemClock.elapsedRealtime();
        if (this.mRestoreByTrafficLimitTime[phoneId] == -1 || curTime - this.mRestoreByTrafficLimitTime[phoneId] > 43200000) {
            return false;
        }
        return DBG;
    }

    private synchronized void onRestoreDdsData(boolean force, RestoreScene scene) {
        int defaultDataPhoneId = getDefaultDataSlotId();
        if (!isValidatePhoneId(defaultDataPhoneId)) {
            logd("onRestoreDdsData invalid defaultDataPhoneId=" + defaultDataPhoneId);
            return;
        }
        if (isAllowedToRestore(force)) {
            logd("onRestoreDdsData defaultDataPhoneId=" + defaultDataPhoneId);
            this.mReEvalueRegistrants.notifyRegistrants(new AsyncResult((Object) null, new Integer(defaultDataPhoneId), (Throwable) null));
            setTempDdsSwitch(false);
            this.mLastScene = ActiveScene.DUMMY;
            long curTime = SystemClock.elapsedRealtime();
            updateRestoreStatistic(scene, curTime);
        }
    }

    private void updateRestoreStatistic(RestoreScene scene, long time) {
        this.mRestoreCnt++;
        this.mActiveRestoreRecord.setRestoreInfo(scene, time);
        this.mActiveRestoreRecordList.add(this.mActiveRestoreRecord.createSnapshot());
        this.mActiveRestoreRecord.resetInfo();
    }

    public boolean isTempDdsSwitchRequiredForSds() {
        if (isTempDdsSwitchEnabled()) {
            int preferredDataPhoneId = PhoneSwitcher.getInstance() != null ? PhoneSwitcher.getInstance().getPreferredDataPhoneId() : -1;
            int userPreferredDataPhoneId = getDefaultDataSlotId();
            logd("isTempDdsSwitchRequiredForSds: user=" + userPreferredDataPhoneId + ", prefer=" + preferredDataPhoneId);
            if (preferredDataPhoneId != userPreferredDataPhoneId) {
                mTempDataPhoneId = preferredDataPhoneId;
                return DBG;
            }
            if (preferredDataPhoneId == userPreferredDataPhoneId) {
                mTempDataPhoneId = -1;
                onRestoreDdsData(false, RestoreScene.DUMMY);
            }
        }
        logd("isTempDdsSwitchRequiredForSds is " + isTempDdsSwitchEnabled());
        return false;
    }

    public void forceRestoreTempDataSwitch() {
        int preferredDataPhoneId = PhoneSwitcher.getInstance() != null ? PhoneSwitcher.getInstance().getPreferredDataPhoneId() : -1;
        int userPreferredDataPhoneId = getDefaultDataSlotId();
        logd("forceRestoreTempDataSwitch: user=" + userPreferredDataPhoneId + ", prefer=" + preferredDataPhoneId + ",mLastDdsPhoneId=" + this.mLastDdsPhoneId + "isTempDdsSwitchEnabled" + isTempDdsSwitchEnabled());
        if (preferredDataPhoneId != userPreferredDataPhoneId || (preferredDataPhoneId == userPreferredDataPhoneId && isTempDdsSwitchEnabled())) {
            logd("forceRestoreTempDataSwitch onRestoreDdsData");
            onRestoreDdsData(false, RestoreScene.SWITCH_DDS);
        }
    }

    public void registerReEvaluateEvent(Handler h, int what, Object obj) {
        if (!this.mSmartDualSimFeatureSupported) {
            logd("registerReEvaluateEvent:unsupport!!!");
            return;
        }
        Registrant r = new Registrant(h, what, obj);
        logd("registerReEvaluateEvent()");
        this.mReEvalueRegistrants.add(r);
    }

    public void unregisterReEvaluateEvent(Handler h) {
        if (!this.mSmartDualSimFeatureSupported) {
            logd("unregisterReEvaluateEvent:unsupport!!!");
        } else {
            logd("unregisterReEvaluateEvent()");
            this.mReEvalueRegistrants.remove(h);
        }
    }

    private int getDataRat(ServiceState ss) {
        if (ss == null) {
            return 0;
        }
        return ss.getRilDataRadioTechnology();
    }

    private boolean isPhoneStateIdle() {
        for (int i = 0; i < this.mPhoneNum; i++) {
            if (this.mPhones[i] != null && this.mPhones[i].getState() != PhoneConstants.State.IDLE) {
                logd("isPhoneStateIdle false: Voice call active on phone " + i);
                return false;
            }
        }
        return DBG;
    }

    private boolean isSameServicePrvider(int ddsPhoneId, int nonDdsPhoneId) {
        String ddsOperatorNum = getAvailableOperatorNumeric(ddsPhoneId);
        String nonDdsOperatorNum = getAvailableOperatorNumeric(nonDdsPhoneId);
        boolean rst = ServiceProviderUtils.isSameServiceProvider(ddsOperatorNum, nonDdsOperatorNum);
        logd("isSameServicePrvider rst[" + rst + "],dds[" + ddsPhoneId + "],ddsPlmn[" + ddsOperatorNum + "],NonDdsplmn[" + nonDdsOperatorNum + "]");
        if (VDBG) {
            logd("ignore isSameServicePrvider while VDBG is true");
            return false;
        }
        return rst;
    }

    private String getAvailableOperatorNumeric(int phoneId) {
        String operator = getServiceState(phoneId) != null ? getServiceState(phoneId).getOperatorNumeric() : null;
        if (TextUtils.isEmpty(operator)) {
            operator = getTelephonyManager().getSimOperatorNumericForPhone(phoneId);
        }
        if (TextUtils.isEmpty(operator)) {
            return "";
        }
        return operator;
    }

    private void startSpeedRateEstimate() {
        logd("startSpeedRateEstimate");
        if (SpeedRateEstimate.getInstance() != null) {
            SpeedRateEstimate.getInstance().startMonitor(getLooper());
            SpeedRateEstimate.getInstance().addSpeedRateEstimateListener(this.mSpeedRateEstimateListener);
        }
    }

    private void stopSpeedRateEstimate() {
        logd("stopSpeedRateEstimate");
        if (SpeedRateEstimate.getInstance() != null) {
            SpeedRateEstimate.getInstance().stopMonitor();
            SpeedRateEstimate.getInstance().removeSpeedRateEstimateListener(this.mSpeedRateEstimateListener);
        }
    }

    class SpeedRateEstimateListener implements SpeedRateEstimate.ISpeedRateEstimateListener {
        SpeedRateEstimateListener() {
        }

        @Override // com.android.internal.telephony.datasub.SpeedRateEstimate.ISpeedRateEstimateListener
        public void onSpeedRateChanged(boolean z) {
            SmartDualSimSwitch.this.obtainMessage(80, !z ? 1 : 0, -1).sendToTarget();
        }
    }

    private synchronized void processSimStateChanged(Intent intent) {
        int curInsertedSimCn = getInsertedSimCount();
        logd("processSimStateChanged mLast=" + this.mLastInsertedSimCn + ",cur=" + curInsertedSimCn);
        if (this.mLastInsertedSimCn > 1 && curInsertedSimCn < 2) {
            unregServiceStateChangedEvent();
            unregisterCoreEvents();
            if (isTempDdsSwitchEnabled()) {
                onRestoreDdsData(false, RestoreScene.PLUG_OUT_SIM);
            }
        } else if (this.mLastInsertedSimCn < 2 && curInsertedSimCn > 1) {
            this.mLastInsertedSimCn = curInsertedSimCn;
            if (isAllowedToRegServiceState()) {
                regServiceStateChangedEvent();
            }
            if (isAllowedToRegCoreEvents()) {
                registerCoreEvents();
            }
            enterDdsChangedMonitorStatus();
            initPhoneValidStatus();
        }
        this.mLastInsertedSimCn = curInsertedSimCn;
    }

    private int getInsertedSimCount() {
        int insertedSimCount = 0;
        if (this.mPhones != null) {
            for (Phone phone : this.mPhones) {
                if (phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                    insertedSimCount++;
                }
            }
        }
        return insertedSimCount;
    }

    private boolean isAllowedToRegServiceState() {
        if (this.mLastInsertedSimCn > 1) {
            if (!Build.IS_INTERNATIONAL_BUILD || isCustForInternational(SystemProperties.get("ro.miui.region", "unkonw"))) {
                return DBG;
            }
            return false;
        }
        return false;
    }

    private boolean isAllowedToRegCoreEvents() {
        if (this.mLastInsertedSimCn <= 1 || !this.isSmartDualSimOn) {
            return false;
        }
        return DBG;
    }

    private boolean isAllowedToMonitorLowSpeed() {
        boolean rst = (!this.mIsWifiConnected && this.mUserDataEnabled && this.mIsMobileConnected && isPhoneStateIdle()) ? DBG : false;
        if (!rst) {
            logd("isAllowedToMonitorLowSpeed " + this.mIsWifiConnected + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mUserDataEnabled + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mIsMobileConnected + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + isPhoneStateIdle());
        }
        return rst;
    }

    private void processAirplaneModeChanged() {
        int i = Settings.Global.getInt(this.mResolver, "airplane_mode_on", 0);
        boolean isNormalScene = DBG;
        this.mIsAirPlaneMode = i == 1;
        if (!checkIsSwitchAvailable() || !this.mUserDataEnabled) {
            isNormalScene = false;
        }
        if (!isNormalScene) {
            logd("processAirplaneModeChanged, isNormalScene=" + isNormalScene);
            return;
        }
        if (this.mIsAirPlaneMode) {
            removeMessages(EVENT_PHONE_STATE_IDLE);
            if (isTempDdsSwitchRequiredForSds()) {
                onRestoreDdsData(false, RestoreScene.AIRPLANE);
                return;
            }
            return;
        }
        if (!isTempDdsSwitchEnabled()) {
            int phoneId = getDefaultDataSlotId();
            startMonitorTimerForAirplane(phoneId);
        }
    }

    private void startMonitorTimerForAirplane(int phoneId) {
        logd("startMonitorTimerForAirplane phoneId=" + phoneId);
        stopMonitorTimerForAirplane();
        sendMessageDelayed(obtainMessage(EVENT_NW_WITHOUT_LTENR_AIRPLANE_LINGER, Integer.valueOf(phoneId)), DELAY_NW_WITHOUT_LTENR_TIMEOUT_AIRPLANE);
    }

    private void stopMonitorTimerForAirplane() {
        if (hasMessages(EVENT_NW_WITHOUT_LTENR_AIRPLANE_LINGER)) {
            logd("stopMonitorTimerForAirplane");
            removeMessages(EVENT_NW_WITHOUT_LTENR_AIRPLANE_LINGER);
        }
    }

    private void processDefaultDataSubChanged(Intent intent) {
        int defaultDataSubId = intent.getIntExtra("subscription", -1);
        logd("processDefaultDataSubChanged defaultDataSubId=" + defaultDataSubId);
        updateDefaultDataPhoneId(defaultDataSubId, "DataSubChanged");
    }

    private void processDefaultDataSlotChanged() {
        if (shouldRestoreAfterDataSlotChanged()) {
            forceRestoreTempDataSwitch();
        }
    }

    private void processDataSwitchChanged() {
        logd("processDataSwitchChanged mUserDataEnabled=" + this.mUserDataEnabled);
        if (this.mUserDataEnabled) {
            if (isNwOosOrWithoutLteNr(this.mLastDdsPhoneId) && shouldTempSwitchDataToSlave(this.mLastDdsPhoneId)) {
                onActiveNonDdsData(getAnotherPhoneId(this.mLastDdsPhoneId), ActiveScene.RAT);
                return;
            }
            return;
        }
        if (isTempDdsSwitchEnabled()) {
            onRestoreDdsData(false, RestoreScene.DATA_BUTTON);
        }
    }

    private boolean shouldRestoreAfterDataSlotChanged() {
        boolean rst = (this.mSmartDualSimFeatureSupported && this.isSmartDualSimOn && isTempDdsSwitchEnabled()) ? DBG : false;
        logd("shouldRestoreAfterDataSlotChanged rst=" + rst + ",sds=" + this.isSmartDualSimOn + ",enabled=" + isTempDdsSwitchEnabled());
        return rst;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllowedToRestore(boolean force) {
        boolean rst = this.mSmartDualSimFeatureSupported;
        if (!force) {
            rst = (rst && this.isSmartDualSimOn) ? DBG : false;
        }
        logd("isAllowedToRestore rst=" + rst + ",force=" + force + ",isSmartDualSimOn=" + this.isSmartDualSimOn);
        return rst;
    }

    public boolean isTempDdsSwitchEnabled() {
        return this.mIsInTempDdsSwitchForSds;
    }

    public void setTempDdsSwitch(boolean enabled) {
        String state = enabled ? "true" : ModemUtils.PROP_BOOL_VALUE_OFF;
        SystemProperties.set(KEY_TEMP_DDS_STATE, state);
        logd("state = " + state + ", enabled = " + enabled);
        this.mIsInTempDdsSwitchForSds = enabled;
    }

    private boolean shouldTempSwitchDataToSlave(int phoneId) {
        if (!isTempDdsSwitchRequiredForSds() && !isSameServicePrvider(phoneId, getAnotherPhoneId(phoneId)) && isAllowedForSwitch(phoneId, DBG) && isNwNormal(getAnotherPhoneId(phoneId))) {
            return DBG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkIsSwitchAvailable() {
        if (this.mSmartDualSimFeatureSupported && this.isSmartDualSimOn && this.mLastInsertedSimCn > 1) {
            return DBG;
        }
        return false;
    }

    private boolean isSmartDualSimSwitchSupport() {
        if (IS_CUST_SINGLE_SIM || HyperOSCustFeatureResolve.getBoolean("config_common_diabled_smart_dual_sim", false)) {
        }
        boolean isDeviceDisabled = isDevicesSupported() ^ DBG;
        if (IS_CUST_SINGLE_SIM || isDeviceDisabled) {
            return false;
        }
        return DBG;
    }

    private boolean isDevicesSupported() {
        int featuresSupport = SystemProperties.getInt("ro.vendor.radio.features_common", 0);
        boolean isSupport = HyperOSCustFeatureResolve.getBoolean("isSmartDualSimSwitchDevicesSupported", false);
        if (isSupport || (featuresSupport & 2) != 0) {
            return DBG;
        }
        return false;
    }

    private boolean isCustForInternational(String curRegion) {
        boolean isSupportSmartDualSim = HyperOSCustFeatureResolve.getBoolean("config_support_smart_dual_sim_by_sku", false);
        if (isSupportSmartDualSim) {
            return DBG;
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        boolean isSmartDualSimGolbalSupport = HyperOSCustFeatureResolve.getBoolean("config_is_smart_dual_sim_global_support", false);
        if ((!isSmartDualSimGolbalSupport && !TelephonyManagerEx.getDefault().isPlatform8650() && !TelephonyManagerEx.getDefault().isPlatform8635() && !TelephonyManagerEx.getDefault().isPlatform8735() && !TelephonyManagerEx.getDefault().isPlatform8750() && !TelephonyManagerEx.getDefault().isPlatform8850() && !MiuiTelephony.IS_MTK) || TextUtils.isEmpty(curRegion) || "unknow".equals(curRegion)) {
            return false;
        }
        try {
            List<String> regions = HyperOSCustFeatureResolve.getStringArray("config_support_smart_dual_sim_by_region");
            return ArrayUtils.contains(regions, curRegion);
        } catch (Exception e) {
            loge("isCustForInternational " + e);
            return false;
        }
    }

    private boolean isValidRat(int rat, int phoneId) {
        int curRat = 0;
        if (rat == 0) {
            ServiceState curSs = getCurrentServiceState(phoneId);
            if (curSs != null) {
                curRat = curSs.getDataNetworkType();
            }
            if (curRat != 0) {
                logd("isValidRat curRat:[" + curRat + "]  ,ignore!!!");
                return false;
            }
            return DBG;
        }
        return DBG;
    }

    private void registerTelephonyInfoCallBack() {
        logd("registerTelephonyInfoCallBack");
        if (this.mTelephonyInfoCallBack == null) {
            logd("new TelephonyInfoCallBack");
            this.mTelephonyInfoCallBack = new TelephonyInfoCallBack();
            TelephonyStatAdapter.registerCallBack(this.mTelephonyInfoCallBack);
        }
    }

    private void unregisterTelephonyInfoCallBack() {
        if (this.mTelephonyInfoCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyInfoCallBack);
            this.mTelephonyInfoCallBack = null;
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int phoneId, Map<String, String> params) {
            SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_DATA_STATISTIC_CHANGED, dataStatType.ordinal(), phoneId, params).sendToTarget();
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onDataConnectionChanged(int phoneId, int dataState) {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onSignalStrengthChanged(int phoneId, SignalStrength signal) {
            if (Build.IS_INTERNATIONAL_BUILD) {
                return;
            }
            Message msg = SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_NETWORK_SIGNAL_STRENGTH_CHANGED);
            msg.arg1 = phoneId;
            msg.arg2 = 0;
            msg.obj = signal;
            SmartDualSimSwitch.this.sendMessage(msg);
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onServiceStateChanged(int phoneId, ServiceState ss) {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onCellLocationChanged(int phoneId, CellLocation cellLocation) {
            Message msg = SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_CELL_LOCATION_CHANGED);
            msg.arg1 = phoneId;
            msg.arg2 = 0;
            msg.obj = cellLocation;
            SmartDualSimSwitch.this.sendMessage(msg);
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onPreciseCallStateChanged(int phoneId, int foregroundCallState) {
            SmartDualSimSwitch.logd("onPreciseCallStateChanged phoneId=" + phoneId + ",foregroundCallState=" + foregroundCallState);
            SmartDualSimSwitch.this.removeMessages(SmartDualSimSwitch.EVENT_PHONE_STATE_IDLE);
            SmartDualSimSwitch.this.sendMessageDelayed(SmartDualSimSwitch.this.obtainMessage(SmartDualSimSwitch.EVENT_PHONE_STATE_IDLE, Integer.valueOf(SmartDualSimSwitch.this.mLastDdsPhoneId)), 5000L);
        }
    }

    private void processDataStatisticChange(int dataType, int phoneId, HashMap<String, String> params) {
        if (!isValidatePhoneId(phoneId)) {
            logd("invalid phoneId: " + phoneId);
        }
        logd("processDataStatisticChange dataType=" + dataType + ", phoneId" + phoneId);
        ValidStatus validStatus = this.mPhoneValidStatus[phoneId];
        TelephonyStatAdapter.DataStatType dataStatType = TelephonyStatAdapter.DataStatType.values()[dataType];
        switch (dataStatType) {
            case FAKE_CONNECTION_CN:
                validStatus.setValid(false, SystemClock.elapsedRealtime());
                break;
            case DATA_CONNECTION_OK_CN:
                validStatus.setValid(DBG, SystemClock.elapsedRealtime());
                break;
        }
    }

    private void processNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        if (state != NetworkInfo.State.CONNECTED && state != NetworkInfo.State.DISCONNECTED) {
            return;
        }
        int networkType = networkInfo.getType();
        boolean connected = state == NetworkInfo.State.CONNECTED;
        logd("networkType=" + networkType + ",connected=" + connected);
        if (networkType == 0) {
            if (this.mIsMobileConnected == connected) {
                return;
            }
            this.mIsMobileConnected = connected;
            logd("mobile=" + this.mIsMobileConnected);
            return;
        }
        if (networkType != 1 || this.mIsWifiConnected == connected) {
            return;
        }
        this.mIsWifiConnected = connected;
        if (this.mIsWifiConnected) {
            logd("wifi connected, isTempDdsSwitchEnabled(): " + isTempDdsSwitchEnabled());
            if (isTempDdsSwitchEnabled() && isNwOosOrWithoutLteNr(this.mLastDdsPhoneId) && isMobileDataAlwaysOn()) {
                logd("ignore restore dds while dds is OOS or without LTE/NR and dataAlwaysOn");
                return;
            } else {
                if (isTempDdsSwitchRequiredForSds()) {
                    onRestoreDdsData(false, RestoreScene.WIFI);
                    return;
                }
                return;
            }
        }
        if (checkIsSwitchAvailable() && isNwOosOrWithoutLteNr(this.mLastDdsPhoneId) && shouldTempSwitchDataToSlave(this.mLastDdsPhoneId)) {
            onActiveNonDdsData(getAnotherPhoneId(this.mLastDdsPhoneId), ActiveScene.RAT);
        }
    }

    public void processDataNetworkFail(boolean isDdsPhoneId, ActiveScene scene) {
        if (!isValidatePhoneId(this.mLastDdsPhoneId)) {
            return;
        }
        logd("processDataNetworkFail isDdsPhoneId=" + isDdsPhoneId);
        if (isDdsPhoneId) {
            if (!this.mIsDdsChanging && shouldTempSwitchDataToSlave(this.mLastDdsPhoneId)) {
                onActiveNonDdsData(getAnotherPhoneId(this.mLastDdsPhoneId), scene);
                return;
            }
            return;
        }
        if (isTempDdsSwitchEnabled()) {
            onRestoreDdsData(false, RestoreScene.SETUP_FAILURE);
        }
    }

    public void processStreamDataStall(boolean isDdsPhoneId) {
        if (!isValidatePhoneId(this.mLastDdsPhoneId)) {
            return;
        }
        logd("processStreamDataStall isDdsPhoneId=" + isDdsPhoneId);
        if (isDdsPhoneId) {
            if (!this.mIsDdsChanging && shouldTempSwitchDataToSlave(this.mLastDdsPhoneId)) {
                onActiveNonDdsData(getAnotherPhoneId(this.mLastDdsPhoneId), ActiveScene.STREAM_DATA_STALL);
                return;
            }
            return;
        }
        if (isTempDdsSwitchEnabled()) {
            onRestoreDdsData(false, RestoreScene.STREAM_DATA_STALL);
        }
    }

    private void processNetworkTrafficLimit(Intent intent) {
        int slotId = intent.getIntExtra(KEY_SLOT_NUM, -1);
        int cause = intent.getIntExtra("limit_status", -1);
        int slaveSlotId = getAnotherPhoneId(this.mLastDdsPhoneId);
        logd("processNetworkTrafficLimit slotId=" + slotId + ",slaveSlotId=" + slaveSlotId + ",cause=" + cause);
        if (isValidatePhoneId(slotId) && isValidatePhoneId(slaveSlotId) && slotId == slaveSlotId && cause > 0) {
            onRestoreDdsData(false, RestoreScene.TRAFFIC);
            this.mRestoreByTrafficLimitTime[slotId] = SystemClock.elapsedRealtime();
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0048 A[PHI: r1 r2
  0x0048: PHI (r1v1 'limitStatus' int) = (r1v0 'limitStatus' int), (r1v3 'limitStatus' int) binds: [B:19:0x0046, B:13:0x003b] A[DONT_GENERATE, DONT_INLINE]
  0x0048: PHI (r2v4 'cursor' android.database.Cursor) = (r2v3 'cursor' android.database.Cursor), (r2v6 'cursor' android.database.Cursor) binds: [B:19:0x0046, B:13:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
    private int getMobileTrafficLimitStatus(int slotId) {
        int limitStatus = -1;
        if (!isValidatePhoneId(slotId)) {
            logd("getMobileTrafficLimitStatus Invalid!!!");
            return -1;
        }
        Cursor cursor = null;
        try {
            try {
                String subId = Integer.toString(slotId);
                Uri uri = Uri.withAppendedPath(URI_MOBILE_TRAFFIC_LIMIT_SUBID, subId);
                cursor = this.mContext.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    limitStatus = cursor.getInt(cursor.getColumnIndex("limit_status"));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                logd("getMobileTrafficLimitStatus Exception!");
                if (cursor != null) {
                    cursor.close();
                }
            }
            logd("getMobileTrafficLimitStatus limitStatus=" + limitStatus);
            return limitStatus;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private boolean isVirtualSimBySlot(Context context, int slotId) {
        if (context == null || !isValidatePhoneId(slotId)) {
            return false;
        }
        return VirtualSimUtils.isVirtualSim(context, slotId);
    }

    private boolean isVirtualSeedSimBySlot(int slotId) {
        SubscriptionInfo subInfo;
        String iccid = Settings.Global.getString(this.mContext.getContentResolver(), "virtual_seed_sim_iccid");
        if (TextUtils.isEmpty(iccid) || this.mPhones[slotId] == null || (subInfo = miui.telephony.SubscriptionManager.getDefault().getSubscriptionInfoForSubscription(this.mPhones[slotId].getSubId())) == null) {
            return false;
        }
        return iccid.equals(subInfo.getIccId());
    }

    private void startMonitorTimerForDdsChanged() {
        logd("startMonitorTimerForDdsChanged");
        stopMonitorTimerForDdsChanged();
        sendMessageDelayed(obtainMessage(EVENT_DDS_CHANGED_LINGER), 10000);
    }

    private void stopMonitorTimerForDdsChanged() {
        removeMessages(EVENT_DDS_CHANGED_LINGER);
    }

    private void enterDdsChangedMonitorStatus() {
        this.mIsDdsChanging = DBG;
        startMonitorTimerForDdsChanged();
    }

    private void exitDdsChangedMonitorStatus() {
        stopMonitorTimerForDdsChanged();
        this.mIsDdsChanging = false;
    }

    private synchronized void processSignalStrengthEvent(int phoneId, SignalStrength signal) {
        if (isValidatePhoneId(phoneId) && signal != null) {
            List<CellSignalStrength> cellSignalStrengths = signal.getCellSignalStrengths();
            for (CellSignalStrength cellSignal : cellSignalStrengths) {
                if (cellSignal instanceof CellSignalStrengthLte) {
                    CellSignalStrengthLte cellLteSignal = (CellSignalStrengthLte) cellSignal;
                    this.mLteRsrp[phoneId] = inRangeOrUnavailable(cellLteSignal.getRsrp(), -140, -43);
                    this.mLteRsrq[phoneId] = inRangeOrUnavailable(cellLteSignal.getRsrq(), -20, -3);
                    logd("updateSignalInfo lteRsrp=" + this.mLteRsrp[phoneId] + ",lteRsrq=" + this.mLteRsrq[phoneId] + ",phoneId=" + phoneId);
                } else if (cellSignal instanceof CellSignalStrengthNr) {
                    CellSignalStrengthNr cellNrSignal = (CellSignalStrengthNr) cellSignal;
                    this.mNrRsrp[phoneId] = inRangeOrUnavailable(cellNrSignal.getSsRsrp(), -140, -44);
                    logd("updateSignalInfo nrRsrp=" + this.mNrRsrp[phoneId] + ",phoneId=" + phoneId);
                }
            }
            int curRat = getCurRatByPhoneId(phoneId);
            if (VDBG) {
                logd("processSignalStrengthEvent mointor[" + this.mIsSignalMonitorStatus + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + this.mIsSignalDiffMonitorStatus + "],rat[" + curRat + "]");
            }
            if (isAllowedMonitorSignalStatus(phoneId) && isLteNrNwType(curRat) && !isSameServicePrvider(phoneId, getAnotherPhoneId(phoneId)) && !isNr2LteSwitch(phoneId, getAnotherPhoneId(phoneId))) {
                if (!this.mIsSignalMonitorStatus && isSignalBoundary(phoneId, getAnotherPhoneId(phoneId))) {
                    enterSignalMonitorStatus(phoneId);
                }
                if (!this.mIsSignalDiffMonitorStatus && isNeedSwitchBySignalDiff(phoneId, getAnotherPhoneId(phoneId))) {
                    enterSignalDiffMonitorStatus(phoneId);
                }
            }
            if (this.mIsSignalMonitorStatus && !isLteNrNwType(curRat)) {
                exitSignalMonitorStatus(ExitCode.INVALID_SIGNAL);
            }
            if (this.mIsSignalDiffMonitorStatus && !isLteNrNwType(curRat)) {
                exitSignalDiffMonitorStatus(ExitCode.INVALID_SIGNAL);
            }
            return;
        }
        logd("processSignalStrengthEvent-invalid params!");
    }

    private void processCellLocationChanged(CellLocation cellLocation, int phoneId) {
        Phone phone;
        CellIdentity newCi;
        String gci;
        if (!isValidatePhoneId(phoneId) || cellLocation == null || cellLocation.isEmpty() || (phone = PhoneFactory.getPhone(phoneId)) == null || (newCi = phone.getCurrentCellIdentity()) == null || (gci = newCi.getGlobalCellId()) == null) {
            return;
        }
        if (VDBG) {
            logd("gci=" + gci);
        }
        this.mCellId[phoneId] = gci;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedSwitchBySignalDiff(int prePhoneId, int postPhoneId) {
        if (!isValidatePhoneId(prePhoneId) || !isValidatePhoneId(postPhoneId)) {
            return false;
        }
        int prePhoneIdRsrp = getRsrpByPhoneId(prePhoneId);
        int postPhoneIdRsrp = getRsrpByPhoneId(postPhoneId);
        if (prePhoneIdRsrp == UNAVAILABLE || postPhoneIdRsrp == UNAVAILABLE) {
            return false;
        }
        boolean rst = false;
        if (prePhoneIdRsrp < -115 && postPhoneIdRsrp - prePhoneIdRsrp >= 16 && postPhoneIdRsrp >= -105) {
            rst = DBG;
        }
        logd("isNeedSwitchBySignalDiff rst=" + rst);
        return rst;
    }

    private void enterSignalDiffMonitorStatus(int phoneId) {
        logd("enterSignalDiffMonitorStatus");
        this.mIsSignalDiffMonitorStatus = DBG;
        this.mSignalDiffStatistic.clear();
        startMonitorTimerForSignalDiff(phoneId);
    }

    private void exitSignalDiffMonitorStatus(ExitCode code) {
        logd("exitSignalDiffMonitorStatus code=" + code);
        stopMonitorTimerForSignalDiff();
        this.mIsSignalDiffMonitorStatus = false;
    }

    private void startMonitorTimerForSignalDiff(int phoneId) {
        stopMonitorTimerForSignalDiff();
        sendMessageDelayed(obtainMessage(EVENT_NW_SIGNAL_DIFF_LINGER, Integer.valueOf(phoneId)), 1000);
    }

    private void stopMonitorTimerForSignalDiff() {
        removeMessages(EVENT_NW_SIGNAL_DIFF_LINGER);
    }

    private boolean isSignalDiffMonitorAllowed(int phoneId) {
        if (checkIsSwitchAvailable() && this.mIsSignalDiffMonitorStatus && isAllowedMonitorSignalStatus(phoneId)) {
            return DBG;
        }
        return false;
    }

    public class SignalDiffStatistic {
        public int needSwitchCn = 0;
        public int totalSignalCn = 0;

        public SignalDiffStatistic() {
        }

        public boolean isPrePhoneSignalPoor() {
            if (this.needSwitchCn >= 14) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public boolean isMonitorTimerOut() {
            if (this.totalSignalCn >= 20 || this.needSwitchCn >= 14) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public boolean isBeyondMonitorTime() {
            if (this.totalSignalCn >= 20) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public void pollUpdateSignalDiffMetrics(int prePhoneId, int postPhoneId) {
            if (SmartDualSimSwitch.this.isNeedSwitchBySignalDiff(prePhoneId, postPhoneId)) {
                this.needSwitchCn++;
            }
            this.totalSignalCn++;
            SmartDualSimSwitch.logd("pollUpdateSignalDiffMetrics needSwitchCn=" + this.needSwitchCn + ",totalSignalCn=" + this.totalSignalCn);
        }

        public void clear() {
            this.needSwitchCn = 0;
            this.totalSignalCn = 0;
        }
    }

    private void switchDdsIfNeededBySignalDiff(int phoneId) {
        logd("switchDdsIfNeededBySignalDiff totalSignalCn=" + this.mSignalDiffStatistic.totalSignalCn);
        if (this.mSignalDiffStatistic.isMonitorTimerOut() && this.mSignalDiffStatistic.isPrePhoneSignalPoor() && !shoudAvoidOperatorScene(phoneId) && isAllowedForSwitch(phoneId, false)) {
            logd("switchDdsIfNeededBySignalDiff TempDDSSwitch");
            if (this.mIsHighSpeedTrain) {
                this.mSignalDiffSwitchCntHst++;
                exitSignalDiffMonitorStatus(ExitCode.NORMAL_EXIT);
                return;
            }
            if (isDefaultDataSlotId(phoneId) && isSatisfySeamlessSwitch(phoneId)) {
                startSeamlessSwitch(phoneId, ActiveScene.SIGNAL_DIFF);
            } else if (isDefaultDataSlotId(phoneId)) {
                onActiveNonDdsData(getAnotherPhoneId(phoneId), ActiveScene.SIGNAL_DIFF);
            } else {
                onRestoreDdsData(false, RestoreScene.SIGNAL_DIFF);
            }
            exitSignalDiffMonitorStatus(ExitCode.NORMAL_EXIT);
            return;
        }
        if (this.mSignalDiffStatistic.isBeyondMonitorTime()) {
            exitSignalDiffMonitorStatus(ExitCode.SAMPLING_TIMEOUT);
        } else {
            startMonitorTimerForSignalDiff(phoneId);
        }
    }

    private boolean isSignalBoundary(int prePhoneId, int postPhoneId) {
        if (!isWeakSignal(prePhoneId) || !isGoodSignal(postPhoneId)) {
            return false;
        }
        return DBG;
    }

    private void startMonitorTimerForSignal(int phoneId) {
        stopMonitorTimerForSignal();
        sendMessageDelayed(obtainMessage(EVENT_NW_WEAK_SIGNAL_LINGER, Integer.valueOf(phoneId)), 1000);
    }

    private void stopMonitorTimerForSignal() {
        removeMessages(EVENT_NW_WEAK_SIGNAL_LINGER);
    }

    public class SignalStatistic {
        public int[] signalRatio;
        public int totalSignalCn;
        public int[] weakSignalCn;

        public SignalStatistic() {
            this.weakSignalCn = new int[SmartDualSimSwitch.this.mPhoneNum];
            this.signalRatio = new int[SmartDualSimSwitch.this.mPhoneNum];
            clear();
        }

        public boolean isPrePhoneSignalPoor(int prePhoneId, int postPhoneId) {
            if (!SmartDualSimSwitch.this.isValidatePhoneId(prePhoneId) || !SmartDualSimSwitch.this.isValidatePhoneId(postPhoneId)) {
                return false;
            }
            if (SmartDualSimSwitch.this.mIsHighSpeedTrain) {
                SmartDualSimSwitch.logd("isPrePhoneSignalPoor: mIsHighSpeedTrain=" + SmartDualSimSwitch.this.mIsHighSpeedTrain);
                if (this.signalRatio[prePhoneId] <= 80 || this.signalRatio[postPhoneId] >= 10) {
                    return false;
                }
                return SmartDualSimSwitch.DBG;
            }
            if (this.signalRatio[prePhoneId] <= 60 || this.signalRatio[postPhoneId] >= 10) {
                return false;
            }
            return SmartDualSimSwitch.DBG;
        }

        public boolean isMonitorTimerOut() {
            if (SmartDualSimSwitch.this.mIsDriving && (this.totalSignalCn == 15 || this.totalSignalCn == 16 || this.totalSignalCn == 17)) {
                return SmartDualSimSwitch.this.isNavigationWhiteList();
            }
            if (this.totalSignalCn >= 30) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public boolean isBeyondMonitorTime() {
            if (this.totalSignalCn > 40) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public void pollUpdateSignalMetrics(int prePhoneId, int postPhoneId) {
            if (SmartDualSimSwitch.this.isWeakSignal(prePhoneId)) {
                int[] iArr = this.weakSignalCn;
                iArr[prePhoneId] = iArr[prePhoneId] + 1;
            }
            if (SmartDualSimSwitch.this.isWeakSignal(postPhoneId)) {
                int[] iArr2 = this.weakSignalCn;
                iArr2[postPhoneId] = iArr2[postPhoneId] + 1;
            }
            this.totalSignalCn++;
            if (isMonitorTimerOut()) {
                for (int i = 0; i < SmartDualSimSwitch.this.mPhoneNum; i++) {
                    try {
                        this.signalRatio[i] = (this.weakSignalCn[i] * 100) / this.totalSignalCn;
                    } catch (ArithmeticException e) {
                        SmartDualSimSwitch.logd("ArithmeticException!!!");
                        this.signalRatio[i] = 0;
                    }
                }
            }
            SmartDualSimSwitch.logd("pollUpdateSignalMetrics weakSignalCn [" + prePhoneId + "]=" + this.weakSignalCn[prePhoneId] + ",weakSignalCn[" + postPhoneId + "]=" + this.weakSignalCn[postPhoneId] + ",totalSignalCn=" + this.totalSignalCn);
        }

        public void clear() {
            for (int i = 0; i < SmartDualSimSwitch.this.mPhoneNum; i++) {
                this.weakSignalCn[i] = 0;
                this.signalRatio[i] = 0;
            }
            this.totalSignalCn = 0;
        }
    }

    private int inRangeOrUnavailable(int value, int rangeMin, int rangeMax) {
        if (value < rangeMin || value > rangeMax) {
            return UNAVAILABLE;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWeakSignal(int phoneId) {
        boolean rst = false;
        if (isInvalidLteNrSignal(phoneId)) {
            return false;
        }
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            if (this.mLteRsrp[phoneId] <= LTE_RSRP_WEAK_THRESHOLD || this.mLteRsrq[phoneId] <= LTE_RSRQ_WEAK_THRESHOLD) {
                rst = DBG;
            } else if (VDBG && (this.mLteRsrp[phoneId] <= this.mLteRsrpWeakThreshold || this.mLteRsrq[phoneId] <= this.mLteRsrqWeakThreshold)) {
                rst = DBG;
            }
        } else if (isNrNwType(rat)) {
            if (this.mNrRsrp[phoneId] <= -105) {
                rst = DBG;
            } else if (VDBG && this.mNrRsrp[phoneId] <= this.mNrRsrpWeakThreshold) {
                rst = DBG;
            }
        }
        logd("isWeakSignal rat=" + rat + ",rst=" + rst);
        return rst;
    }

    private boolean isGoodSignal(int phoneId) {
        boolean rst = false;
        if (isInvalidLteNrSignal(phoneId)) {
            return false;
        }
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            if (this.mLteRsrp[phoneId] >= LTE_RSRP_GOOD_THRESHOLD && this.mLteRsrq[phoneId] >= LTE_RSRQ_GOOD_THRESHOLD) {
                rst = DBG;
            } else if (VDBG && this.mLteRsrp[phoneId] >= this.mLteRsrpGoodThreshold && this.mLteRsrq[phoneId] >= this.mLteRsrqGoodThreshold) {
                rst = DBG;
            }
        } else if (isNrNwType(rat)) {
            if (this.mNrRsrp[phoneId] >= NR_RSRP_GOOD_THRESHOLD) {
                rst = DBG;
            } else if (VDBG && this.mNrRsrp[phoneId] >= this.mNrRsrpGoodThreshold) {
                rst = DBG;
            }
        }
        logd("isGoodSignal rat=" + rat + ",rst=" + rst);
        return rst;
    }

    private boolean isBroaderGoodSignal(int phoneId) {
        if (isInvalidLteNrSignal(phoneId)) {
            return false;
        }
        boolean rst = false;
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            if (this.mLteRsrp[phoneId] >= -100) {
                rst = DBG;
            }
        } else if (isNrNwType(rat) && this.mNrRsrp[phoneId] >= -100) {
            rst = DBG;
        }
        logd("isBroaderGoodSignal rat=" + rat + ",rst=" + rst);
        return rst;
    }

    public boolean isGoodSignalForFakeConnection(int phoneId) {
        if (isInvalidLteNrSignal(phoneId)) {
            return false;
        }
        boolean rst = false;
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            if (this.mLteRsrp[phoneId] > -115) {
                rst = DBG;
            }
        } else if (isNrNwType(rat) && this.mNrRsrp[phoneId] > -115) {
            rst = DBG;
        }
        logd("isGoodSignalForFakeConnection rat=" + rat + ",rst=" + rst);
        return rst;
    }

    public boolean isGoodSignalForStreamDataStall(int phoneId) {
        if (isInvalidLteNrSignal(phoneId)) {
            return false;
        }
        boolean rst = false;
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            if (this.mLteRsrp[phoneId] > -105) {
                rst = DBG;
            }
        } else if (isNrNwType(rat) && this.mNrRsrp[phoneId] > -105) {
            rst = DBG;
        }
        logd("isGoodSignalForStreamDataStall rat=" + rat + ", rst=" + rst);
        return rst;
    }

    private boolean isInvalidLteNrSignal(int phoneId) {
        if (!isValidatePhoneId(phoneId)) {
            return DBG;
        }
        int rat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(rat)) {
            boolean rst = this.mLteRsrp[phoneId] == UNAVAILABLE || this.mLteRsrq[phoneId] == UNAVAILABLE;
            return rst;
        }
        if (isNrNwType(rat)) {
            boolean rst2 = this.mNrRsrp[phoneId] == UNAVAILABLE;
            return rst2;
        }
        logd("isInvalidLteNrSignal rat=" + rat);
        return DBG;
    }

    private boolean isAllowedMonitorSignalStatus(int phoneId) {
        boolean rst = false;
        if (isValidatePhoneId(phoneId) && this.mIsScreenOn) {
            int preferredDataPhoneId = PhoneSwitcher.getInstance() != null ? PhoneSwitcher.getInstance().getPreferredDataPhoneId() : -1;
            if (preferredDataPhoneId == phoneId && isPhoneStateIdle()) {
                rst = DBG;
            }
        }
        if (!rst) {
            logd("isAllowedMonitorSignalStatus screen=" + this.mIsScreenOn + ",phoneId=" + phoneId + "phoneState=" + isPhoneStateIdle());
        }
        return rst;
    }

    private void enterSignalMonitorStatus(int phoneId) {
        logd("enterSignalMonitorStatus");
        this.mIsSignalMonitorStatus = DBG;
        this.mSignalStatistic.clear();
        startMonitorTimerForSignal(phoneId);
    }

    private void exitSignalMonitorStatus(ExitCode code) {
        logd("exitSignalMonitorStatus code=" + code);
        stopMonitorTimerForSignal();
        this.mIsSignalMonitorStatus = false;
    }

    private boolean isSignalMonitorAllowed(int phoneId) {
        if (checkIsSwitchAvailable() && this.mIsSignalMonitorStatus && isAllowedMonitorSignalStatus(phoneId)) {
            return DBG;
        }
        return false;
    }

    private void switchDdsIfNeeded(int phoneId) {
        logd("switchDdsIfNeeded totalSignalCn=" + this.mSignalStatistic.totalSignalCn);
        if (this.mSignalStatistic.isMonitorTimerOut() && this.mSignalStatistic.isPrePhoneSignalPoor(phoneId, getAnotherPhoneId(phoneId)) && !shoudAvoidOperatorScene(phoneId) && isAllowedForSwitch(phoneId, false)) {
            logd("switchDdsIfNeeded TempDDSSwitch");
            if (isDefaultDataSlotId(phoneId) && isSatisfySeamlessSwitch(phoneId)) {
                startSeamlessSwitch(phoneId, ActiveScene.SIGNAL_QOS);
            } else if (isDefaultDataSlotId(phoneId)) {
                onActiveNonDdsData(getAnotherPhoneId(phoneId), ActiveScene.SIGNAL_QOS);
            } else {
                onRestoreDdsData(false, RestoreScene.SIGNAL_QOS);
            }
            exitSignalMonitorStatus(ExitCode.NORMAL_EXIT);
            return;
        }
        if (this.mSignalStatistic.isBeyondMonitorTime()) {
            exitSignalMonitorStatus(ExitCode.SAMPLING_TIMEOUT);
        } else {
            startMonitorTimerForSignal(phoneId);
        }
    }

    private void startSeamlessSwitch(int phoneId, ActiveScene scene) {
        int anotherPhoneId = getAnotherPhoneId(phoneId);
        if (!isValidatePhoneId(phoneId) || !isValidatePhoneId(anotherPhoneId)) {
            return;
        }
        if (!isAllowActiveNonDdsData(phoneId, scene)) {
            logd("startSeamlessSwitch exception");
            return;
        }
        logd("startSeamlessSwitch phoneId=" + phoneId);
        SystemProperties.set(KEY_SEAMLESS_SWITCH_STATE, "true");
        broadcastRecordSeamlessSwitch();
        TelephonyManagerEx.getDefault().requestNetworkBySlot(getAnotherPhoneId(phoneId));
        this.mRequestViceNetworkTime = SystemClock.elapsedRealtime();
        sendMessageDelayed(obtainMessage(EVENT_SEAMLESS_SWITCH_LINGER, getAnotherPhoneId(phoneId), 0, scene), 3000L);
        sendMessageDelayed(obtainMessage(EVENT_SEAMLESS_SWITCH_RELEASE_LINGER, Integer.valueOf(getAnotherPhoneId(phoneId))), 5000L);
    }

    private boolean shoudAvoidOperatorScene(int phoneId) {
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850()) {
            return false;
        }
        int anotherPhoneId = getAnotherPhoneId(phoneId);
        if (!isValidatePhoneId(anotherPhoneId)) {
            return false;
        }
        boolean rst = (!ServiceProviderUtils.isChinaTelecom(getAvailableOperatorNumeric(anotherPhoneId)) || isNrNwType(getCurRatByPhoneId(anotherPhoneId))) ? false : DBG;
        return rst;
    }

    private boolean isSatisfySeamlessSwitch(int phoneId) {
        boolean rst = false;
        boolean isScene = this.mIsGaming || this.mIsWechatVoipForgroud || isSeamlessWhiteList();
        boolean isDualDataAvailable = isDualDataAvailable();
        boolean isDualSimLteNrNwType = isDualSimLteNrNwType(phoneId, getAnotherPhoneId(phoneId));
        if (isScene && isDualDataAvailable && isDualSimLteNrNwType) {
            rst = true;
        }
        if (!rst) {
            logd("isSatisfySeamlessSwitch scene=" + isScene + ", isDualDataAvailable=" + isDualDataAvailable + ", isDualSimLteNrNwType=" + isDualSimLteNrNwType);
        }
        return rst;
    }

    private boolean isSeamlessWhiteList() {
        String topAppName = getTopAppName();
        for (String s : SEAMLEASS_SWITCH_WHITE_LIST) {
            if (topAppName.equals(s)) {
                return DBG;
            }
        }
        return false;
    }

    private int getCurRatByPhoneId(int phoneId) {
        ServiceState ss = getServiceState(phoneId);
        int rat = -1;
        if (ss != null) {
            rat = ss.getDataNetworkType();
        }
        if (VDBG) {
            logd("getCurRatByPhoneId phoneId=" + phoneId + ",rat" + rat);
        }
        return rat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fiveGChangedMonitorStatus() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        boolean fiveGEnabled = DBG;
        if (Settings.Global.getInt(contentResolver, "fiveg_user_enable", 1) != 1) {
            fiveGEnabled = false;
        }
        logd("fiveGChangedMonitorStatus fiveGEnabled=" + fiveGEnabled);
        this.mIsFiveGChanging = fiveGEnabled;
        removeMessages(EVENT_FIVEG_CHANGED_LINGER);
        if (fiveGEnabled) {
            sendMessageDelayed(obtainMessage(EVENT_FIVEG_CHANGED_LINGER), 10000L);
        }
    }

    private void exitFiveGChangedMonitorStatus() {
        removeMessages(EVENT_FIVEG_CHANGED_LINGER);
        this.mIsFiveGChanging = false;
    }

    private void registerFivegEvents() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("fiveg_user_enable"), false, this.mFiveGEnabledObserver);
    }

    private boolean isMobileDataAlwaysOn() {
        boolean isDataAlwaysOn = Settings.Global.getInt(this.mContext.getContentResolver(), "mobile_data_always_on", 0) == 1;
        logd("isMobileDataAlwaysOn " + isDataAlwaysOn);
        return isDataAlwaysOn;
    }

    private void notifySmartSwitchDualSimAlertDialog() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SMART_SWITCH_DUAL_SWITCH_ALERT_DIALOG);
        intent.setPackage(NOTIFICATION_RECEIVER_PACKAGE);
        this.mContext.sendBroadcast(intent);
        logd("send broadcast notify smart switch dual sim alert dialog");
    }

    private boolean isHikingMode() {
        boolean isHikingMode = false;
        boolean isHikingOptimizationSupported = TelephonyManagerEx.getDefault().isHikingOptimizationSupported();
        if (isHikingOptimizationSupported) {
            int hikingState = Settings.Global.getInt(this.mResolver, "network_mode_state", 0);
            isHikingMode = 4 == (hikingState & 4) ? DBG : false;
        }
        logd("isHikingMode: " + isHikingMode);
        return isHikingMode;
    }

    private void processGameSceneSwitch() {
        int ddsPhoneId = getDefaultDataSlotId();
        logd("processGameSceneSwitch ddsPhoneId = " + ddsPhoneId);
        if (!this.mIsDdsChanging && shouldTempSwitchDataToSlave(ddsPhoneId)) {
            onActiveNonDdsData(getAnotherPhoneId(ddsPhoneId), ActiveScene.SIGNAL_GAME);
            this.mLastGameSwitchTime = System.currentTimeMillis();
        }
    }

    private boolean isDdsPoorerThanNdds() {
        boolean rst = false;
        int ddsPhoneId = getDefaultDataSlotId();
        int nDdsPhoneId = getAnotherPhoneId(ddsPhoneId);
        int curRat = getCurRatByPhoneId(ddsPhoneId);
        if (!isValidatePhoneId(ddsPhoneId) || !isValidatePhoneId(nDdsPhoneId) || curRat != getCurRatByPhoneId(nDdsPhoneId)) {
            return false;
        }
        int ddsRsrp = getRsrpByPhoneId(ddsPhoneId);
        int nDdsRsrp = getRsrpByPhoneId(nDdsPhoneId);
        if (ddsRsrp == UNAVAILABLE || nDdsRsrp == UNAVAILABLE) {
            return false;
        }
        int weakThreshold = isNrNwType(curRat) ? -105 : LTE_RSRP_WEAK_THRESHOLD;
        int goodThreshold = isNrNwType(curRat) ? NR_RSRP_GOOD_THRESHOLD_GAME : -100;
        if ((ddsRsrp < weakThreshold && nDdsRsrp > goodThreshold) || (VDBG && ddsRsrp <= this.mNrRsrpWeakThreshold)) {
            rst = DBG;
        }
        logd("isWeakSignalisDdsPoorerThanNdds weakThreshold=" + weakThreshold + ",ddsRsrp=" + ddsRsrp + ",nDdsRsrp=" + nDdsRsrp + ",goodThreshold=" + goodThreshold);
        return rst;
    }

    private boolean isGameSwitchLingerTime() {
        if (this.mLastGameSwitchTime == 0 || System.currentTimeMillis() - this.mLastGameSwitchTime >= 600000) {
            return false;
        }
        logd("isGameSwitchLingerTime in the Linger Timer");
        return DBG;
    }

    private boolean isWeakWifiRssi() {
        int rssi = UNAVAILABLE;
        if (mWifiManager != null) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            rssi = wifiInfo != null ? wifiInfo.getRssi() : UNAVAILABLE;
        }
        logd("isWeakWifiRssi rssi=" + rssi);
        if (rssi < WIFI_RSSI_WEAK_THRESHOLD_GAME || (VDBG && rssi < this.mWifiRssiThreshold)) {
            return DBG;
        }
        return false;
    }

    private int getRsrpByPhoneId(int phoneId) {
        int curRat = getCurRatByPhoneId(phoneId);
        if (isLteNwType(curRat)) {
            int curRsrp = this.mLteRsrp[phoneId];
            return curRsrp;
        }
        if (!isNrNwType(curRat)) {
            return UNAVAILABLE;
        }
        int curRsrp2 = this.mNrRsrp[phoneId];
        return curRsrp2;
    }

    private void processSeamlessSwitchLinger(int phoneId, ActiveScene scene) {
        logd("processSeamlessSwitchLinger");
        if (isSeamlessTimeSatisfy() && !this.mIsDdsChanging && shouldTempSwitchDataToSlave(this.mLastDdsPhoneId)) {
            onActiveNonDdsData(phoneId, scene);
        }
    }

    private boolean isSeamlessTimeSatisfy() {
        long curTime = SystemClock.elapsedRealtime();
        if (this.mRequestViceNetworkTime == -1 || this.mRequestViceNetworkTime - curTime >= 4000) {
            return false;
        }
        return DBG;
    }

    private boolean isDualSimLteNrNwType(int prePhoneId, int postPhoneId) {
        if (!isValidatePhoneId(prePhoneId) || !isValidatePhoneId(postPhoneId)) {
            return false;
        }
        int preRat = getCurRatByPhoneId(prePhoneId);
        int postRat = getCurRatByPhoneId(postPhoneId);
        if (isLteNrNwType(preRat) && isLteNrNwType(postRat)) {
            return DBG;
        }
        return false;
    }

    private boolean isDualDataAvailable() {
        if (this.mDualDataFeatureSupported && !isDualDataConnected() && isDsdaState()) {
            return DBG;
        }
        return false;
    }

    private boolean isDualDataConnected() {
        if (SystemProperties.getInt(MULTISIM_DUALDATA_STATUS, 0) == 1) {
            return DBG;
        }
        return false;
    }

    private boolean isDsdaState() {
        if (SystemProperties.getInt("ril.multisim.voice_capability", 0) == 3) {
            return DBG;
        }
        return false;
    }

    private void broadcastRecordSeamlessSwitch() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SEAMLEASS_SWITCH_STATISTIC);
        if (this.mIsGaming) {
            intent.putExtra(KEY_SWITCH_SCENE, "game");
        } else if (this.mIsWechatVoipForgroud) {
            intent.putExtra(KEY_SWITCH_SCENE, "wechatVoip");
        } else {
            return;
        }
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNavigationWhiteList() {
        String topAppName = getTopAppName();
        for (String s : EF_DRIVING_MAP_PN) {
            if (topAppName.equals(s)) {
                return DBG;
            }
        }
        return false;
    }

    private String getTopAppName() {
        ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
        if (am == null) {
            return "";
        }
        try {
            if (am.getRunningTasks(1) != null && am.getRunningTasks(1).get(0) != null) {
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                return cn.getPackageName();
            }
        } catch (Exception e) {
            logd("getTopAppName exception:" + e.toString());
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMonitorTimerForDrive() {
        stopMonitorTimerForDrive();
        sendMessageDelayed(obtainMessage(EVENT_DRIVE_STATE_CHANGED_LINGER), DELAY_DRIVE_STATE_CHANGE_TIMEOUT);
    }

    private void stopMonitorTimerForDrive() {
        if (hasMessages(EVENT_DRIVE_STATE_CHANGED_LINGER)) {
            logd("stopMonitorTimerForDrive");
            removeMessages(EVENT_DRIVE_STATE_CHANGED_LINGER);
        }
    }

    private void processModemAiPredict(Intent intent) {
        int isWeakSignalPredict = intent.getIntExtra(MODEM_AI_KEY_SCENE, 0);
        if (isWeakSignalPredict != 1) {
            return;
        }
        long curTime = SystemClock.elapsedRealtime();
        if (this.mLastPredictTime != 0 && curTime - this.mLastPredictTime > 4000) {
            this.mSigalWeakCnt = 0;
        }
        this.mLastPredictTime = curTime;
        float result = intent.getFloatExtra(MODEM_AI_KEY_RESULT, 0.0f);
        String ret = ((double) result) > 0.5d ? SIGNAL_WAEK : SIGNAL_STRONG;
        logd("predict ret=" + ret);
        int preferredDataPhoneId = PhoneSwitcher.getInstance() != null ? PhoneSwitcher.getInstance().getPreferredDataPhoneId() : -1;
        int nPreferredDataPhoneId = getAnotherPhoneId(preferredDataPhoneId);
        logd("prefer=" + preferredDataPhoneId + ", nPrefer=" + nPreferredDataPhoneId);
        if (SIGNAL_WAEK.equals(ret)) {
            if (isGoodSignal(nPreferredDataPhoneId)) {
                this.mSigalWeakCnt++;
                logd("mSigalWeakCnt=" + this.mSigalWeakCnt);
            }
            if (this.mSigalWeakCnt < 5) {
                return;
            }
            this.mSigalWeakCnt = 0;
            onActiveNonDdsOrRestoreData(preferredDataPhoneId, nPreferredDataPhoneId);
            return;
        }
        if (SIGNAL_STRONG.equals(ret) && this.mSigalWeakCnt > 0) {
            this.mSigalWeakCnt = 0;
        }
    }

    private void onActiveNonDdsOrRestoreData(int preferredDataPhoneId, int nPreferredDataPhoneId) {
        logd("onActiveNonDdsOrRestoreData");
        if (!isValidatePhoneId(preferredDataPhoneId) || !isValidatePhoneId(nPreferredDataPhoneId)) {
            return;
        }
        if (isDefaultDataSlotId(preferredDataPhoneId)) {
            if (!this.mIsDdsChanging && shouldTempSwitchDataToSlave(preferredDataPhoneId) && !isNr2LteSwitch(preferredDataPhoneId, nPreferredDataPhoneId) && !shoudAvoidOperatorScene(preferredDataPhoneId) && checkIsSwitchAvailable() && !isAudioRecording()) {
                if (isSatisfySeamlessSwitch(preferredDataPhoneId)) {
                    startSeamlessSwitch(preferredDataPhoneId, ActiveScene.MODEM_AI);
                    return;
                } else {
                    if (!isDualDataConnected()) {
                        onActiveNonDdsData(nPreferredDataPhoneId, ActiveScene.MODEM_AI);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (isTempDdsSwitchEnabled()) {
            onRestoreDdsData(false, RestoreScene.MODEM_AI);
        }
    }

    private boolean isSeamlessSwitching() {
        return "true".equals(SystemProperties.get(KEY_SEAMLESS_SWITCH_STATE, ModemUtils.PROP_BOOL_VALUE_OFF));
    }

    private void registerAudioRecordingCallback() {
        if (this.mAudioManager != null) {
            logd("registerAudioRecordingCallback");
            this.mAudioManager.registerAudioRecordingCallback(this.mAudioRecordingCallback, null);
        }
    }

    private void unregisterAudioRecordingCallback() {
        if (this.mAudioManager != null) {
            logd("unregisterAudioRecordingCallback");
            this.mAudioManager.unregisterAudioRecordingCallback(this.mAudioRecordingCallback);
        }
    }

    private boolean isAudioRecording() {
        logd("isAudioRecording=" + this.mIsAudioRecording);
        return this.mIsAudioRecording;
    }

    private void processDriveStateChanged() {
        boolean isDriving = isDrivingState();
        if (this.mIsDriving != isDriving) {
            this.mIsDriving = isDriving;
            logd("driving state changed, mIsDriving=" + this.mIsDriving);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDrivingState() {
        if (getSensorDriveState() == 10) {
            return DBG;
        }
        return false;
    }

    private int getSensorDriveState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), KEY_SENSOR_DRIVE, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSubwayState(int state) {
        if ((state & 3) != 0) {
            return DBG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubwayState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0);
    }

    public void reportSmartDualSimSwitchStatistic() {
        if (this.mSwitchCnt == 0 && this.mSignalDiffSwitchCntHst == 0) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put(PARAM_KEY_SWITCH_COUNT, Integer.toString(this.mSwitchCnt));
        data.put(PARAM_KEY_RESTORE_COUNT, Integer.toString(this.mRestoreCnt));
        data.put(PARAM_KEY_SWITCH_INTERVAL, this.mSwitchIntervalList.toString());
        data.put(PARAM_KEY_ACTIVE_RESTORE_RECORD, this.mActiveRestoreRecordList.toString());
        data.put(PARAM_KEY_SIGNAL_DIFF_SWITCH_COUNT_HST, Integer.toString(this.mSignalDiffSwitchCntHst));
        logd("reportSmartDualSimSwitchStatistic: data=" + data);
        TelephonyManagerEx.getDefault().recordObjectEvent("telephony_statistic", EVENT_SMART_DUAL_SIM_SWITCH, data);
        clearSmartDualSimSwitchStatistic();
    }

    private void clearSmartDualSimSwitchStatistic() {
        this.mSwitchCnt = 0;
        this.mRestoreCnt = 0;
        this.mSwitchIntervalList.clear();
        this.mActiveRestoreRecordList.clear();
        this.mSignalDiffSwitchCntHst = 0;
    }

    public void reportUserDualSimSwitchStatistic() {
        if (this.mUserSwitchSignalInfoList.isEmpty()) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put(PARAM_KEY_USER_SWITCH_SIGNAL_INFO, this.mUserSwitchSignalInfoList.toString());
        logd("reportUserDualSimSwitchStatistic: data=" + data);
        TelephonyManagerEx.getDefault().recordObjectEvent("telephony_statistic", EVENT_USER_DUAL_SIM_SWITCH, data);
        clearUserDualSimSwitchStatistic();
    }

    private void clearUserDualSimSwitchStatistic() {
        this.mUserSwitchSignalInfoList.clear();
    }

    private void initPhoneValidStatus() {
        logd("initPhoneValidStatus");
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhoneValidStatus[i] = new ValidStatus(this);
        }
    }

    public class ValidStatus {
        private static final int INVALID_PUSHISH_TIME = 43200000;
        public boolean mIsValid = false;
        public long mStatusUpdateTime = -1;

        public ValidStatus(SmartDualSimSwitch this$0) {
        }

        public boolean isValid() {
            if (this.mIsValid || this.mStatusUpdateTime == -1 || SystemClock.elapsedRealtime() - this.mStatusUpdateTime > 43200000) {
                return SmartDualSimSwitch.DBG;
            }
            return false;
        }

        public void setValid(boolean isValid, long time) {
            this.mIsValid = isValid;
            this.mStatusUpdateTime = time;
        }

        public String toString() {
            return "ValidStatus{mIsValid=" + this.mIsValid + ", mStatusUpdateTime=" + this.mStatusUpdateTime + "}";
        }
    }

    private class ActiveRestoreRecord {
        private long activeTime;
        private long duration;
        private long restoreTime;
        private ActiveScene activeType = ActiveScene.DUMMY;
        private RestoreScene restoreType = RestoreScene.DUMMY;
        private int scene = 0;

        public ActiveRestoreRecord() {
        }

        public void setActiveInfo(ActiveScene type, long time) {
            this.activeType = type;
            this.activeTime = time;
        }

        public void setScene(int sceneCode) {
            this.scene = sceneCode;
        }

        public void setRestoreInfo(RestoreScene scene, long time) {
            this.restoreType = scene;
            this.restoreTime = time;
            this.duration = (this.restoreTime - this.activeTime) / 1000;
        }

        public void resetInfo() {
            this.activeType = ActiveScene.DUMMY;
            this.restoreType = RestoreScene.DUMMY;
            this.scene = 0;
        }

        public ActiveRestoreRecord createSnapshot() {
            ActiveRestoreRecord snapshot = SmartDualSimSwitch.this.new ActiveRestoreRecord();
            snapshot.activeType = this.activeType;
            snapshot.restoreType = this.restoreType;
            snapshot.restoreTime = this.restoreTime;
            snapshot.duration = this.duration;
            snapshot.scene = this.scene;
            return snapshot;
        }

        public String toString() {
            if (this.activeType == ActiveScene.DUMMY) {
                return "";
            }
            return this.activeType.getValue() + "-" + this.restoreType.getValue() + "-" + this.duration + "-" + this.scene;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String s) {
        Rlog.d(LOG_TAG, s);
    }

    private static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }
}
