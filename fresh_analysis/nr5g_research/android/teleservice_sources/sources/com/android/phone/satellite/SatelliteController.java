package com.android.phone.satellite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.ConnectivityManager;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.ntnphone.SatelliteConfig;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.BatteryStateManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeRecognition$$ExternalSyntheticLambda0;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SatelliteManager;
import com.android.phone.module.IUtilityModule;
import com.android.phone.ntn.NtnPhoneOptimization;
import com.android.phone.utils.FoldStateMonitor;
import com.android.phone.utils.MiuiSatelliteUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.sensor.aidl.Calibrate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteController extends Handler {
    private static ConnectivityManager mConnectivityManager = null;
    private static Context mContext = null;
    private static boolean mIsSatelliteEnabled = false;
    private static boolean mIsSatelliteXingwangEnabled = false;
    private static SatelliteController sInstance;
    private float mAltitude;
    private boolean mIsCalibrated;
    private Location mLocation;
    private LocationManager mLocationManager;
    private NtnPhoneOptimization mNtnPhoneOptimization;
    private int mOnHPRssi;
    private int mOnHPSnr;
    private long mOnHPTime;
    private SatelliteManager mSatelliteManager;
    private SatelliteSessionStateMachine mSessionStateMachine;
    private long mStartTime;
    private int mIsChannelState = -1;
    private int mEnableSatellitePhoneId = -1;
    private boolean mIsInServiceBefore = false;
    private boolean mHasCellInfoBefore = false;
    private boolean mIsNtnStackReady = false;
    private boolean mIsInCallUIReady = false;
    private boolean mIsSatelliteDisplay = false;
    private boolean mIsSatelliteInService = false;
    private boolean mIsHighPenetrationEnabled = false;
    private int mStartBattery = 0;
    private String mLocationType = "";
    private ArrayList mCalibrationInformation = new ArrayList();
    private volatile boolean mIsExitSatelliteInProgress = false;
    private int mOpenFlag = 0;
    private int mCloseFlag = 0;
    private int mPagingCount = 0;
    private boolean mBeamSwitched = false;
    private int mBeamSwitchedStatus = 0;
    private final BroadcastReceiver mMiuiSatelliteReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.SatelliteController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                SatelliteController.loge("received null intent or action");
                return;
            }
            SatelliteController.logd("onReceive action = " + intent.getAction());
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                if (intent.getIntExtra("android.intent.extra.user_handle", -10000) != 0) {
                    SatelliteController.this.closeSatellite(1004);
                    return;
                }
                return;
            }
            if ("com.android.satellite.action.OPEN_SATELLITE_MODE".equals(intent.getAction())) {
                if (SatelliteController.mIsSatelliteEnabled) {
                    if (SatelliteController.this.mIsNtnStackReady && !SatelliteController.this.mIsInCallUIReady) {
                        SatelliteController.this.startNtnRild();
                    }
                    SatelliteController.this.mIsInCallUIReady = true;
                    return;
                }
                return;
            }
            if ("android.intent.action.SERVICE_STATE".equals(intent.getAction())) {
                ServiceState serviceStateNewFromBundle = ServiceState.newFromBundle(intent.getExtras());
                int rilVoiceRadioTechnology = serviceStateNewFromBundle.getRilVoiceRadioTechnology();
                if (serviceStateNewFromBundle.getState() == 0 && rilVoiceRadioTechnology == 101) {
                    SatelliteController.this.mIsSatelliteInService = true;
                    SatelliteController.this.mSessionStateMachine.onSatelliteVoiceStateChanged(true);
                    SatelliteController.this.saveCityCoverInfo();
                    if (565 == serviceStateNewFromBundle.getChannelNumber()) {
                        SatelliteController.this.mSatelliteManager.getDataController().setDataStart(false);
                    }
                }
            }
        }
    };
    private final LocationListener mLocationListener = new LocationListener() { // from class: com.android.phone.satellite.SatelliteController.3
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.i("NTNSatelliteController", "No valid location need to be update! ");
            } else {
                SatelliteController.this.mLocation = location;
            }
        }
    };

    public static SatelliteController getInstance() {
        if (sInstance == null) {
            loge("SatelliteDataController was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteController make(SatelliteManager satelliteManager, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteController(satelliteManager, context);
        }
        return sInstance;
    }

    public SatelliteController(SatelliteManager satelliteManager, Context context) {
        this.mSatelliteManager = satelliteManager;
        mContext = context;
        mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mLocationManager = (LocationManager) mContext.getSystemService("location");
        this.mSessionStateMachine = SatelliteSessionStateMachine.make(this, context);
        init();
    }

    private void init() {
        resetSatelliteState();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        logd("handleMessage :" + message.what);
        switch (message.what) {
            case 0:
                this.mSessionStateMachine.onSatelliteRadioStateChanged(mIsSatelliteEnabled);
                break;
            case 1:
                this.mSatelliteManager.getDataController().updateNtnChannelTypePreferredIfNeed();
                this.mSessionStateMachine.handleStartNtnStack();
                break;
            case 2:
                if (!mIsSatelliteEnabled) {
                    TelephonyCommonUtils.resetNtnStackPropertyValue();
                } else {
                    if (!this.mIsNtnStackReady && this.mIsInCallUIReady) {
                        startNtnRild();
                    }
                    this.mIsNtnStackReady = true;
                }
                break;
            case 3:
                sendNtnGps((String) message.obj);
                break;
            case 4:
                final int iIntValue = ((Integer) message.obj).intValue();
                try {
                    new Thread(new Runnable(this) { // from class: com.android.phone.satellite.SatelliteController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Calibrate.nativeSetConfig(3, new float[]{iIntValue});
                            SatelliteController.logd("MSG_SENSOR_SET_CONFIG : type = " + iIntValue);
                        }
                    }).start();
                } catch (Exception e) {
                    loge("MSG_SENSOR_SET_CONFIG: " + e);
                    return;
                }
                break;
            case 5:
                if (mIsSatelliteXingwangEnabled) {
                    startNtnRild();
                    break;
                }
                break;
            case 9:
                NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
                if (defaultPhone != null) {
                    defaultPhone.setRadioPower(true, false, false, true);
                    break;
                }
                break;
            case 10:
                logd("Event MSG_CHANNEL_STATE_CHANGE Received. ");
                updateChannelState((AsyncResult) message.obj);
                break;
            case 12:
                sendMessageDelayed(obtainMessage(13), 4000L);
                break;
            case 13:
                startNtnRild();
                break;
            case 14:
                int iIntValue2 = ((Integer) ((AsyncResult) message.obj).result).intValue();
                highPenetrationDot(iIntValue2);
                if (iIntValue2 == 1 || iIntValue2 == 0) {
                    boolean z = iIntValue2 == 1;
                    this.mIsHighPenetrationEnabled = z;
                    sendHighPenetrationChanged(z);
                } else if (iIntValue2 == 2) {
                    this.mSatelliteManager.getUIController().sendFocusNotification();
                }
                logd("state = " + iIntValue2 + ", mIsHighPenetrationEnabled = " + this.mIsHighPenetrationEnabled);
                break;
            case 15:
                this.mSatelliteManager.unregisterSatelliteConfigState(this);
                if (isHardSimSwapForTT()) {
                    this.mSatelliteManager.getSIMController().setSimPowerState(1);
                }
                break;
        }
    }

    private void highPenetrationDot(int i) {
        if (i == 1 && !this.mIsHighPenetrationEnabled) {
            this.mPagingCount = 0;
            this.mOnHPTime = System.currentTimeMillis();
            this.mOnHPRssi = this.mSatelliteManager.getRetRssi();
            this.mOnHPSnr = this.mSatelliteManager.getRetSnr();
            return;
        }
        if (i == 2 && this.mIsHighPenetrationEnabled) {
            this.mPagingCount++;
            return;
        }
        if (i == 0 && this.mIsHighPenetrationEnabled) {
            HashMap map = new HashMap();
            map.put("timer", String.valueOf((System.currentTimeMillis() - this.mOnHPTime) / 1000));
            map.put("onHPRssi", String.valueOf(this.mOnHPRssi));
            map.put("onHPSnr", String.valueOf(this.mOnHPSnr));
            map.put("offHPRssi", String.valueOf(this.mSatelliteManager.getRetRssi()));
            map.put("offHPSnr", String.valueOf(this.mSatelliteManager.getRetSnr()));
            map.put("pagingCount", String.valueOf(this.mPagingCount));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordSatelliteHighPenetration(map);
        }
    }

    private void sendHighPenetrationChanged(boolean z) {
        Intent intent = new Intent("com.xiaomi.phone.satellite.action.HIGH_PENETRATION_CHANGED");
        intent.addFlags(268435456);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, z);
        intent.setPackage("com.xiaomi.phone");
        Log.d("NTNSatelliteController", "sendHighPenetrationChanged");
        mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    public void updateChannelState(AsyncResult asyncResult) {
        if (asyncResult.exception == null) {
            this.mIsChannelState = ((Integer) asyncResult.result).intValue();
        }
        this.mSatelliteManager.getDataController().processSatelliteDataEnable();
    }

    public boolean setSatelliteEnable(int i, boolean z) {
        this.mSessionStateMachine.onSatelliteEnabledStateChanged(z);
        if (z) {
            startSatellite();
        }
        return true;
    }

    private void startSatellite() {
        getLocationInformation();
        this.mNtnPhoneOptimization = new NtnPhoneOptimization(mContext);
        setSatelliteAirPlane(true);
        sendMessageDelayed(obtainMessage(1), mContext.getResources().getInteger(R.integer.config_start_ntn_stack_delay));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNtnRild() {
        logd("startNtnRild, hard Swip:" + isHardSimSwapForTT());
        if (isHardSimSwapForTT()) {
            this.mSatelliteManager.getSIMController().startCardDetectStatistics(true);
            this.mSatelliteManager.getSIMController().setSimPowerState(0);
        } else {
            handleStartNtnRild();
        }
    }

    public void handleStartNtnRild() {
        this.mSessionStateMachine.handleStartNtnRild();
    }

    public void setSatelliteAirPlane(boolean z) {
        mConnectivityManager.setAirplaneMode(z);
        if (z) {
            SystemProperties.set("persist.radio.ntn_airplane_on", "true");
        } else {
            SystemProperties.set("persist.radio.ntn_airplane_on", "false");
        }
    }

    public boolean isSatelliteEnabled() {
        return mIsSatelliteEnabled;
    }

    public int getEnabledSatelliteSlotId() {
        return this.mEnableSatellitePhoneId;
    }

    public boolean isHighPenetrationEnabled() {
        return this.mIsHighPenetrationEnabled;
    }

    public void setEnabledSatelliteSlotId(int i) {
        this.mEnableSatellitePhoneId = i;
    }

    public void setSatelliteEnableByUser(Context context, boolean z, int i) {
        if (TelephonyManagerEx.getDefault().isSatelliteBox()) {
            logd("setSatelliteEnableByUser: for satellite box, nothing to do!!!");
            return;
        }
        if (z) {
            this.mOpenFlag = i;
            if (SatelliteChecker.make(this, this.mSatelliteManager).handle()) {
                openSatellite(this.mEnableSatellitePhoneId, i);
                return;
            }
            return;
        }
        if (this.mIsSatelliteDisplay || i == 100101 || i == 1009 || i == 1010 || i == 1005 || i == 1101) {
            if (i != 100101) {
                this.mCloseFlag = i;
            }
            closeSatellite(i);
        } else {
            this.mCloseFlag = i;
            this.mSatelliteManager.getUIController().pullUpControlCenter(i);
            this.mSatelliteManager.getUIController().startDialogActivity(i, 13);
        }
    }

    public void openSatellite(int i, int i2) {
        logd("openSatellite phoneId = " + i + ", flag = " + i2);
        registerListener(true);
        if (!setSatelliteEnable(i, true)) {
            logd("modem start error, stop satellite " + i);
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0);
            this.mSatelliteManager.getDataController().unRegisterForDataChangedListener(this.mEnableSatellitePhoneId);
            return;
        }
        saveOpenSatelliteBeforeState();
        this.mEnableSatellitePhoneId = i;
        mIsSatelliteEnabled = true;
        this.mBeamSwitched = false;
        this.mIsHighPenetrationEnabled = false;
        this.mBeamSwitchedStatus = 0;
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 1);
        this.mSatelliteManager.getUIController().enableSatelliteIncallUi(true, i, i2);
        this.mSatelliteManager.getDataController().registerForDataChangedListener(this.mEnableSatellitePhoneId);
    }

    public void closeSatellite(int i, int i2) {
        if (i2 != this.mEnableSatellitePhoneId) {
            return;
        }
        closeSatellite(i);
    }

    public void closeSatellite(int i) {
        logd("closeSatellite flag = " + i);
        if (this.mIsSatelliteDisplay) {
            closeSatelliteDisplay(i);
            return;
        }
        if (this.mSatelliteManager.getDataController().isSatelliteDataEnabled()) {
            this.mSatelliteManager.getDataController().setSatelliteDataEnableByUser(mContext, false);
        }
        this.mSatelliteManager.getDataController().resetSatelliteDataState();
        if (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0) == 1 || mIsSatelliteEnabled) {
            this.mIsNtnStackReady = false;
            this.mIsInCallUIReady = false;
            this.mSatelliteManager.setSatelliteSignalBoostEnable(false, i);
            if (!isHardSimSwapForTT()) {
                registerListener(false);
                sendSatelliteDotBroadcast();
            } else {
                this.mSatelliteManager.getSIMController().startCardDetectStatistics(false);
            }
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0);
            mIsSatelliteEnabled = false;
            resetBeamSwitching();
            this.mNtnPhoneOptimization = null;
            this.mLocationManager.removeUpdates(this.mLocationListener);
            setSatelliteEnable(this.mEnableSatellitePhoneId, false);
            this.mSatelliteManager.getUIController().enableSatelliteIncallUi(false, SubscriptionManager.INVALID_SUBSCRIPTION_ID, i);
            MiuiSatelliteUtils.setupOnNtnBoot();
            sendMessage(obtainMessage(4, 0));
            highPenetrationDot(0);
            if (hasMessages(1)) {
                removeMessages(1);
            }
            if (hasMessages(2)) {
                removeMessages(2);
            }
            this.mSatelliteManager.getDataController().unRegisterForDataChangedListener(this.mEnableSatellitePhoneId);
            setSatelliteAirPlane(false);
        }
        post(new Runnable() { // from class: com.android.phone.satellite.SatelliteController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$closeSatellite$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeSatellite$0() {
        setExitSatelliteInProgress(false);
    }

    public void resetSatelliteState() {
        TelephonyCommonUtils.resetNtnStackPropertyValue();
        if (SystemProperties.getInt("persist.miui.cit.satellite_mode", -1) == 1) {
            SystemProperties.set("persist.miui.cit.satellite_mode", Integer.toString(0));
        }
        if ((Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0) == 1 || isSatelliteAirPlaneModeOn()) && !TelephonyManagerEx.getDefault().isSatelliteBox()) {
            logd("resetSatelliteState ");
            this.mSatelliteManager.getUIController().enableSatelliteIncallUi(false, SubscriptionManager.INVALID_SUBSCRIPTION_ID, 1011);
            NtnPhone defaultNtnPhone = getDefaultNtnPhone();
            logd("resetSatelliteState tiantong = " + defaultNtnPhone);
            if (defaultNtnPhone == null || defaultNtnPhone.getSatelliteConfig(SatelliteConfig.Type.TIANTONG_1) == null) {
                logd("registerSatelliteConfigState ");
                this.mSatelliteManager.registerSatelliteConfigState(this, 15, null);
            } else if (isHardSimSwapForTT() && this.mSatelliteManager.getSIMController() != null) {
                this.mSatelliteManager.getSIMController().setSimPowerState(1);
            }
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0);
            if (isSatelliteAirPlaneModeOn()) {
                setSatelliteAirPlane(false);
            }
            SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(this.mEnableSatellitePhoneId, false, false);
        }
        if (Settings.System.getInt(mContext.getContentResolver(), "satellite_data_enabled", 0) == 1) {
            Settings.System.putInt(mContext.getContentResolver(), "satellite_data_enabled", 0);
        }
        resetSatelliteXingwangState();
    }

    public void resetSatelliteXingwangState() {
        if (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "xingwang_satellite_state", 0) != 1 || TelephonyManagerEx.getDefault().isSatelliteBox()) {
            return;
        }
        logd("resetXingwangSatelliteState ");
        SystemProperties.set("persist.radio.ntn_stack_on", "36");
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "xingwang_satellite_state", 0);
        if (Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
            mConnectivityManager.setAirplaneMode(false);
        }
    }

    public void restoreSatellitestate() {
        this.mSessionStateMachine.onSatelliteSimStatusChanged(false);
    }

    public void restareSatelliteForData() {
        this.mSessionStateMachine.onSatelliteDataEnabledChanged(true);
    }

    private void saveOpenSatelliteBeforeState() {
        this.mIsInServiceBefore = false;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getServiceState().getState() == 0) {
                this.mIsInServiceBefore = true;
            }
        }
        this.mHasCellInfoBefore = false;
        for (Phone phone2 : PhoneFactory.getPhones()) {
            List allCellInfo = phone2.getAllCellInfo();
            if (allCellInfo != null && !allCellInfo.isEmpty()) {
                this.mHasCellInfoBefore = true;
                break;
            }
        }
        this.mStartTime = System.currentTimeMillis();
        this.mStartBattery = BatteryStateManager.getInstance().getmBatteryLevel();
    }

    public void sendSatelliteDotBroadcast() {
        HashMap map = new HashMap();
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric();
        int i = BatteryStateManager.getInstance().getmBatteryLevel();
        map.put("ntnVersion", SystemProperties.get("persist.radio.ntn_version", ""));
        map.put("isInService", this.mIsInServiceBefore ? "1" : "0");
        map.put("isSatelliteInService", this.mIsSatelliteInService ? "1" : "0");
        map.put("timer", String.valueOf((System.currentTimeMillis() - this.mStartTime) / 1000));
        map.put("startBattery", String.valueOf(this.mStartBattery));
        map.put("endBattery", String.valueOf(i));
        map.put("operatorNumber", simOperatorNumeric);
        map.put("openFlag", String.valueOf(this.mOpenFlag));
        map.put("mCloseFlag", String.valueOf(this.mCloseFlag));
        map.put("beamSwitchedStatus", String.valueOf(this.mBeamSwitchedStatus));
        if (TelephonyManagerEx.getDefault().isSupportSatelliteSignalBoost()) {
            map.put("signalBoost", this.mSatelliteManager.isSatelliteSignalBoostEnabled() ? "1" : "0");
        }
        FoldStateMonitor.getInstance();
        if (isHardSimSwapForTT()) {
            map.put("cardDetectedOnSatelliteTimer", String.valueOf(this.mSatelliteManager.getSIMController().getCardDetectedOnSatelliteTimer()));
            map.put("isCardDetectedOnSatellite", this.mSatelliteManager.getSIMController().isCardDetectedOnSatellite() ? "1" : "0");
            map.put("cardDetectedOnSatelliteShutdownTimer", String.valueOf(this.mSatelliteManager.getSIMController().getCardDetectedOnSatelliteShutdownTimer()));
            map.put("isCardDetectedOnSatelliteShutdown", this.mSatelliteManager.getSIMController().isCardDetectedOnSatelliteShutdown() ? "1" : "0");
            map.put("isHasIccLock", this.mSatelliteManager.getSIMController().hasIccLock() ? "1" : "0");
        }
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordSatelliteEnabled(map);
    }

    public void sendSatelliteBusinessEntranceDot() {
        HashMap map = new HashMap();
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric();
        boolean z = MiuiPhoneUtils.isWifiConnected() || MiuiPhoneUtils.isMobileDataConnected();
        map.put("operatorNumber", simOperatorNumeric);
        map.put("isNetworkConnected", z ? "1" : "0");
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordSatelliteBusinessEntrance(map);
    }

    public void registerListener(boolean z) {
        if (z) {
            satelliteReceiver();
        } else {
            unRegisterReceiver();
        }
        this.mSatelliteManager.getUIController().registerBatteryListener(z, mIsSatelliteEnabled);
        this.mSatelliteManager.getSIMController().registerSimStateChangedListener(z);
    }

    public void satelliteDisplay() {
        this.mIsSatelliteDisplay = true;
        registerListener(true);
        saveOpenSatelliteBeforeState();
        this.mEnableSatellitePhoneId = 0;
        mIsSatelliteEnabled = true;
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 1);
        this.mSatelliteManager.getUIController().enableSatelliteIncallUi(true, this.mEnableSatellitePhoneId, 1007);
        setSatelliteAirPlane(true);
        this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(this.mEnableSatellitePhoneId, true, true);
    }

    public void closeSatelliteDisplay(int i) {
        logd("closeSatelliteDisplay flag = " + i);
        this.mIsSatelliteDisplay = false;
        this.mSatelliteManager.setSatelliteSignalBoostEnable(false, i);
        sendSatelliteDotBroadcast();
        registerListener(false);
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0);
        mIsSatelliteEnabled = false;
        resetBeamSwitching();
        this.mSatelliteManager.getUIController().enableSatelliteIncallUi(false, SubscriptionManager.INVALID_SUBSCRIPTION_ID, i);
        setSatelliteAirPlane(false);
        this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(this.mEnableSatellitePhoneId, false, true);
        this.mEnableSatellitePhoneId = -1;
        if (this.mIsExitSatelliteInProgress) {
            setExitSatelliteInProgress(false);
        }
    }

    public boolean isExitSatelliteInProgress() {
        return this.mIsExitSatelliteInProgress;
    }

    public void setExitSatelliteInProgress(boolean z) {
        this.mIsExitSatelliteInProgress = z;
    }

    private boolean isSatelliteAirPlaneModeOn() {
        return SystemProperties.getBoolean("persist.radio.ntn_airplane_on", false);
    }

    private void satelliteReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("com.android.satellite.action.OPEN_SATELLITE_MODE");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        mContext.registerReceiver(this.mMiuiSatelliteReceiver, intentFilter, 2);
    }

    private void unRegisterReceiver() {
        BroadcastReceiver broadcastReceiver = this.mMiuiSatelliteReceiver;
        if (broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public void setCalibrationInformation(Bundle bundle) {
        this.mIsCalibrated = bundle.getBoolean(MiuiSatelliteUtils.IS_CALIBRATED);
        this.mCalibrationInformation.clear();
        this.mCalibrationInformation.add(Float.valueOf(bundle.getFloat(MiuiSatelliteUtils.REAL_AZIMUTH)));
        this.mCalibrationInformation.add(Float.valueOf(bundle.getFloat(MiuiSatelliteUtils.REAL_ELEVATION)));
        this.mCalibrationInformation.add(Float.valueOf(bundle.getFloat(MiuiSatelliteUtils.DESIRED_AZIMUTH)));
        this.mCalibrationInformation.add(Float.valueOf(bundle.getFloat(MiuiSatelliteUtils.DESIRED_ELEVATION)));
        float f = (float) bundle.getDouble(MiuiSatelliteUtils.ALTITUDE);
        this.mAltitude = f;
        this.mCalibrationInformation.add(Float.valueOf(f));
    }

    public void notifyStartBeamSwitching(float f, Bundle bundle) {
        int i = SystemProperties.getInt("debug.satellite.beam_switching_mode", 0);
        if (!mIsSatelliteEnabled) {
            logd("satellite closed");
            return;
        }
        if (this.mBeamSwitched) {
            logd("beam switched");
            return;
        }
        if (i == 0 && !mContext.getResources().getBoolean(R.bool.config_satellite_beam_switch_supported)) {
            logd("beam switching is not supported");
            return;
        }
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return;
        }
        int i2 = (-f) > 53.0f ? 1 : 0;
        if (i == 2) {
            i2 = 0;
        } else if (i == 1) {
            i2 = 1;
        }
        logd("notifyStartBeamswitching: type = 0, mode = " + i + ", status = " + i2 + ", beamSwitched = " + this.mBeamSwitched + ", elevation = " + f);
        defaultNtnPhone.notifyStartBeamswitching(0, i2);
        this.mBeamSwitchedStatus = i2;
        this.mBeamSwitched = true;
        bundle.putInt("beam_status", i2);
    }

    private void resetBeamSwitching() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return;
        }
        logd("resetBeamSwitching");
        defaultNtnPhone.notifyStartBeamswitching(0, 0);
        this.mBeamSwitched = false;
    }

    public boolean isCalibrated() {
        return this.mIsCalibrated;
    }

    public float getAltitude() {
        return this.mAltitude;
    }

    public void sendNtnGps(String str) {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            sendMessageDelayed(obtainMessage(3, str), 1000L);
        } else {
            defaultNtnPhone.setNtnGps(str);
        }
    }

    public ArrayList getCalibrationInformation() {
        return this.mCalibrationInformation;
    }

    public boolean isSatelliteXingwangEnabled() {
        return mIsSatelliteXingwangEnabled;
    }

    public void setSatelliteXingwangEnableByUser(Context context, boolean z, int i) {
        mIsSatelliteXingwangEnabled = z;
        if (z) {
            this.mEnableSatellitePhoneId = 0;
            SystemProperties.set("persist.radio.ntn_stack_on", "37");
            sendMessageDelayed(obtainMessage(5), mContext.getResources().getInteger(R.integer.config_start_ntn_rild_delay));
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "xingwang_satellite_state", 1);
            logd("startSatellite successfully!");
            return;
        }
        this.mSessionStateMachine.onSatelliteEnabledStateChanged(false);
        if (hasMessages(5)) {
            removeMessages(5);
        }
        SystemProperties.set("persist.radio.ntn_stack_on", "36");
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "xingwang_satellite_state", 0);
    }

    private void getLocationInformation() {
        if (this.mLocationManager.isProviderEnabled("gps")) {
            LocationRequest locationRequestBuild = new LocationRequest.Builder(1000L).setMinUpdateIntervalMillis(1000L).build();
            LocationManager locationManager = this.mLocationManager;
            Executor mainExecutor = mContext.getMainExecutor();
            LocationListener locationListener = this.mLocationListener;
            Objects.requireNonNull(locationListener);
            locationManager.getCurrentLocation("gps", locationRequestBuild, null, mainExecutor, new NetworkModeRecognition$$ExternalSyntheticLambda0(locationListener));
        }
    }

    public Location getLastKnownLocation() {
        Location location = this.mLocation;
        if (location != null) {
            this.mLocationType = "GPS";
            return location;
        }
        logd("try to get last known location");
        try {
            Location lastKnownLocation = this.mLocationManager.getLastKnownLocation("gps");
            if (lastKnownLocation != null) {
                this.mLocationType = "LAST_GPS";
                return lastKnownLocation;
            }
            Location lastKnownLocation2 = this.mLocationManager.getLastKnownLocation("network");
            if (lastKnownLocation2 != null) {
                this.mLocationType = "LAST_NETWORK";
                return lastKnownLocation2;
            }
            this.mLocationType = "LAST_PASSIVE";
            return this.mLocationManager.getLastKnownLocation("passive");
        } catch (Exception e) {
            this.mLocationType = "";
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCityCoverInfo() {
        int ntnBeamId = this.mSatelliteManager.getNtnBeamId();
        int ntnBandId = this.mSatelliteManager.getNtnBandId();
        if (this.mNtnPhoneOptimization == null || ntnBeamId == -1 || this.mSatelliteManager.getNtnBandId() == 565) {
            return;
        }
        logd("saveCityCoverInfo snr = " + this.mSatelliteManager.getRetSnr() + " rssi = " + this.mSatelliteManager.getRetRssi() + ", beam = " + ntnBeamId + ", freq = " + ntnBandId);
        if (this.mSatelliteManager.getRetRssi() >= -124 && this.mSatelliteManager.getRetRssi() < 0 && this.mSatelliteManager.getRetSnr() >= 30) {
            this.mNtnPhoneOptimization.saveCityCoverInfo(getLastKnownLocation(), ntnBeamId, ntnBandId);
            return;
        }
        int[] beamIdFromSharedPrefs = getBeamIdFromSharedPrefs();
        int[] freqIdFromSharedPrefs = getFreqIdFromSharedPrefs();
        if (beamIdFromSharedPrefs[0] == ntnBeamId && freqIdFromSharedPrefs[0] == ntnBandId) {
            this.mNtnPhoneOptimization.removeSaveCityCoverInfo();
        }
    }

    private int[] getBeamIdFromSharedPrefs() {
        return new int[]{PreferenceManager.getDefaultSharedPreferences(mContext).getInt("satellite.city.cover.save.beam", NtnPhoneOptimization.DEFAULT_BEAM[0])};
    }

    private int[] getFreqIdFromSharedPrefs() {
        return new int[]{PreferenceManager.getDefaultSharedPreferences(mContext).getInt("satellite.city.cover.save.frequency", NtnPhoneOptimization.DEFAULT_FREQUENCY[0])};
    }

    public int getUsedCityInfoIndex() {
        NtnPhoneOptimization ntnPhoneOptimization = this.mNtnPhoneOptimization;
        if (ntnPhoneOptimization == null || ntnPhoneOptimization.getUsedCityInfo() == null) {
            return -1;
        }
        return this.mNtnPhoneOptimization.getUsedCityInfo().getIndex();
    }

    public String getDataVersion() {
        NtnPhoneOptimization ntnPhoneOptimization = this.mNtnPhoneOptimization;
        if (ntnPhoneOptimization == null) {
            return "";
        }
        return ntnPhoneOptimization.getDataVersion();
    }

    public int getSaveBeamId() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("satellite.city.cover.save.beam", NtnPhoneOptimization.DEFAULT_BEAM[0]);
    }

    public String getLocationType() {
        return this.mLocationType;
    }

    public Location getCurrentLocationWhenTurnOnSatellite() {
        return getLastKnownLocation();
    }

    public void setSatelliteInService(boolean z) {
        this.mIsSatelliteInService = z;
    }

    public boolean isNeedOpenSatelliteData() {
        return this.mNtnPhoneOptimization.isNeedOpenSatelliteData(getLastKnownLocation());
    }

    public int getChannelState() {
        return this.mIsChannelState;
    }

    public int getOpenFlag() {
        return this.mOpenFlag;
    }

    public int getBeanSwitchedStatus() {
        return this.mBeamSwitchedStatus;
    }

    public static NtnPhone getDefaultNtnPhone() {
        try {
            return NtnPhoneFactory.getDefaultPhone();
        } catch (Exception e) {
            loge("getDefaultNtnPhone: " + e);
            return null;
        }
    }

    public boolean isHardSimSwapForTT() {
        return TelephonyCommonUtils.isNtnChipVendor_54() || this.mSatelliteManager.isHardSimSwapForTT();
    }

    public boolean hasCellInfoWhenTurnOnSatellite() {
        return this.mHasCellInfoBefore;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("NTNSatelliteController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("NTNSatelliteController", str);
    }
}
