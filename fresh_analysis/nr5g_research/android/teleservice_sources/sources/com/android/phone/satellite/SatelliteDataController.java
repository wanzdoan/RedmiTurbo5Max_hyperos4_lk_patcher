package com.android.phone.satellite;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PreciseCallState;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.SatelliteManager;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteDataController extends Handler {
    private static final String PACKAGE_NAME_DINGDING = HyperOSCustFeatureResolve.getString("config_dingding_packages", "");
    private static boolean mIsSatelliteDataEnabled = false;
    private static SatelliteDataController sInstance;
    private TelephonyCallback mCallStateListener;
    private Context mContext;
    private boolean mDataStart;
    private int mExcuteChangeFrequencyCount;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private boolean mIsDingdingForeground;
    private boolean mIsRegisterForegroundInfo;
    private boolean mIsSatelliteDuringCall;
    private SatelliteManager mSatelliteManager;

    public static SatelliteDataController getInstance() {
        if (sInstance == null) {
            loge("SatelliteDataController was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteDataController make(SatelliteManager satelliteManager, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteDataController(satelliteManager, context);
        }
        return sInstance;
    }

    public SatelliteDataController(SatelliteManager satelliteManager, Context context) {
        super(SatelliteManager.getNtnLooper());
        this.mIsDingdingForeground = false;
        this.mIsSatelliteDuringCall = false;
        this.mIsRegisterForegroundInfo = false;
        this.mExcuteChangeFrequencyCount = 0;
        this.mDataStart = false;
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.satellite.SatelliteDataController.1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                    return;
                }
                SatelliteDataController satelliteDataController = SatelliteDataController.this;
                satelliteDataController.sendMessage(satelliteDataController.obtainMessage(2, foregroundInfo));
            }
        };
        this.mSatelliteManager = satelliteManager;
        this.mContext = context;
        logd("dingdingPkg=" + PACKAGE_NAME_DINGDING);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        logd("handleMessage :" + message.what);
        int i = message.what;
        if (i == 1) {
            processPreciseCallStateChanged(((Integer) message.obj).intValue());
            return;
        }
        if (i == 2) {
            processForegroundInfoEvent((ForegroundInfo) message.obj);
            return;
        }
        if (i == 3) {
            changeFrequencyForDataDisable();
        } else {
            if (i != 6) {
                return;
            }
            if (isDingdingInTop()) {
                startDelayForReleaseNtnData();
            } else {
                processSatelliteDataDisable();
            }
        }
    }

    public boolean isDingdingForeground(String str) {
        return !TextUtils.isEmpty(str) && PACKAGE_NAME_DINGDING.equals(str);
    }

    public boolean isDingdingInTop() {
        return PACKAGE_NAME_DINGDING.equals(getTopAppName());
    }

    private String getTopAppName() {
        ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService("activity");
        if (activityManager == null) {
            logd("getTopAppName activityManager is null");
            return "";
        }
        try {
            if (activityManager.getRunningTasks(1) != null && activityManager.getRunningTasks(1).get(0) != null) {
                ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                logd("getTopAppName packageName =" + componentName.getPackageName());
                return componentName.getPackageName();
            }
        } catch (Exception e) {
            logd("getTopAppName exception:" + e.toString());
        }
        return "";
    }

    public void setSatelliteDataEnableByUser(Context context, boolean z) {
        if (isSatelliteDataEnabled() == z) {
            return;
        }
        logd("setSatelliteDataEnableByUser isEnabled = " + z);
        Settings.System.putInt(this.mContext.getContentResolver(), "satellite_data_enabled", z ? 1 : 0);
        setSatelliteDataEnable(z);
        if (isSatelliteDataEnabled()) {
            return;
        }
        startDelayForDisableDataFrequency();
    }

    public void registerForDataChangedListener(int i) {
        registerCallStateListener(i);
        registerForegroundInfoListener();
    }

    public void unRegisterForDataChangedListener(int i) {
        unRegisterCallStateListener(i);
        unregisterForegroundInfoListener();
    }

    public void registerCallStateListener(int i) {
        if (MiuiPhoneUtils.isTiantongSatelliteDataSupported() && MiuiPhoneUtils.isSatelliteVersionDingTalkInstalled(this.mContext) && this.mCallStateListener == null) {
            this.mCallStateListener = new SatelliteCallStateListener();
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
            if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                telephonyManager.createForSubscriptionId(subscriptionIdForSlot).registerTelephonyCallback(this.mContext.getMainExecutor(), this.mCallStateListener);
            } else {
                telephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mCallStateListener);
            }
            logd("registerCallStateListener subId=" + subscriptionIdForSlot);
        }
    }

    public void unRegisterCallStateListener(int i) {
        if (this.mCallStateListener != null) {
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
            if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                telephonyManager.createForSubscriptionId(subscriptionIdForSlot).unregisterTelephonyCallback(this.mCallStateListener);
            } else {
                telephonyManager.unregisterTelephonyCallback(this.mCallStateListener);
            }
            logd("unRegisterCallStateListener");
            this.mCallStateListener = null;
        }
    }

    private void registerForegroundInfoListener() {
        if (MiuiPhoneUtils.isTiantongSatelliteDataSupported() && MiuiPhoneUtils.isSatelliteVersionDingTalkInstalled(this.mContext) && !this.mIsRegisterForegroundInfo) {
            logd("registerForegroundInfoListener");
            EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
            this.mIsRegisterForegroundInfo = true;
        }
    }

    private void unregisterForegroundInfoListener() {
        if (this.mIsRegisterForegroundInfo) {
            logd("unregisterForegroundInfoListener");
            EventHubManager.getInstance().unregisterListener(this.mForegroundInfoListener);
            this.mIsRegisterForegroundInfo = false;
        }
    }

    private void processForegroundInfoEvent(ForegroundInfo foregroundInfo) {
        String str = foregroundInfo.mForegroundPackageName;
        logd("processForegroundInfoEvent pkgName=" + str + ", mIsDingdingForeground=" + this.mIsDingdingForeground);
        if (isDingdingForeground(str) && !this.mIsDingdingForeground) {
            this.mIsDingdingForeground = true;
            stopDelayForReleaseNtnData();
            stopDelayForDisableDataFrequency();
            processSatelliteDataEnable();
            return;
        }
        if (isDingdingForeground(str) || !this.mIsDingdingForeground) {
            return;
        }
        startDelayForReleaseNtnData();
        this.mIsDingdingForeground = false;
    }

    class SatelliteCallStateListener extends TelephonyCallback implements TelephonyCallback.PreciseCallStateListener {
        SatelliteCallStateListener() {
        }

        public void onPreciseCallStateChanged(PreciseCallState preciseCallState) {
            int foregroundCallState = preciseCallState.getForegroundCallState();
            if (foregroundCallState == -1) {
                return;
            }
            SatelliteDataController.logd("onPreciseCallStateChanged currentState=" + foregroundCallState);
            SatelliteDataController satelliteDataController = SatelliteDataController.this;
            satelliteDataController.sendMessage(satelliteDataController.obtainMessage(1, Integer.valueOf(foregroundCallState)));
        }
    }

    private void processPreciseCallStateChanged(int i) {
        boolean zIsSatelliteDuringCall = MiuiPhoneUtils.isSatelliteDuringCall(this.mContext);
        logd("processPreciseCallStateChanged isSatelliteDuringCall=" + zIsSatelliteDuringCall + ", mIsSatelliteDuringCall=" + this.mIsSatelliteDuringCall);
        if (this.mIsSatelliteDuringCall == zIsSatelliteDuringCall) {
            return;
        }
        logd("mIsSatelliteDataEnabled=" + mIsSatelliteDataEnabled + ", mIsDingdingForeground=" + this.mIsDingdingForeground);
        boolean z = this.mIsSatelliteDuringCall;
        if (!z && zIsSatelliteDuringCall) {
            stopDelayForDisableDataFrequency();
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            if (defaultPhone != null) {
                logd("callState=" + defaultPhone.getState());
            }
            if (mIsSatelliteDataEnabled && defaultPhone != null && defaultPhone.getState() == PhoneConstants.State.RINGING) {
                try {
                    if (defaultPhone.getRingingCall() != null) {
                        defaultPhone.getRingingCall().hangup();
                    }
                } catch (Exception e) {
                    logd("processPreciseCallStateChanged hangup error: " + e);
                }
            }
        } else if (z && !zIsSatelliteDuringCall) {
            startDelayForDisableDataFrequency();
        }
        this.mIsSatelliteDuringCall = zIsSatelliteDuringCall;
    }

    public void stopDelayForDisableDataFrequency() {
        if (hasMessages(3)) {
            logd("stopDelayForDisableDataFrequency");
            removeMessages(3);
        }
    }

    public void changeFrequencyForDataDisable() {
        NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
        logd("changeFrequencyForDataDisable mIsSatelliteDataEnabled=" + mIsSatelliteDataEnabled + ", bandId=" + defaultPhone.getNtnBandId());
        if (mIsSatelliteDataEnabled || isSatelliteDuringCall()) {
            logd("can not changeFrequencyForDataDisable");
            return;
        }
        this.mExcuteChangeFrequencyCount = 0;
        if (defaultPhone.getNtnBandId() != 565) {
            return;
        }
        this.mDataStart = false;
        restartSatelliteForDataEnable();
    }

    public void startDelayForDisableDataFrequency() {
        logd("startDelayForDisableDataFrequency");
        stopDelayForDisableDataFrequency();
        sendMessageDelayed(obtainMessage(3), 120000L);
    }

    public void startDelayForReleaseNtnData() {
        logd("startDelayForReleaseNtnData");
        stopDelayForReleaseNtnData();
        sendMessageDelayed(obtainMessage(6), 120000L);
    }

    public void stopDelayForReleaseNtnData() {
        if (hasMessages(6)) {
            logd("stopDelayForReleaseNtnData");
            removeMessages(6);
        }
    }

    public void processSatelliteDataEnable() {
        logd("processSatelliteDataEnable: , mIsSatelliteDataEnabled = " + mIsSatelliteDataEnabled + ", mIsDingdingForeground = " + isDingdingForeground() + ", mIsSatelliteDuringCall = " + isSatelliteDuringCall() + ", isIdleForChannelState = " + isIdleForChannelState());
        if (mIsSatelliteDataEnabled || !isDingdingForeground() || isSatelliteDuringCall() || !isIdleForChannelState()) {
            return;
        }
        processFrequencyForDataEnable();
    }

    public void processSatelliteDataDisable() {
        logd("processSatelliteDataDisable: , mIsSatelliteDataEnabled = " + mIsSatelliteDataEnabled + ", mIsDingdingForeground = " + isDingdingForeground() + ", mIsSatelliteDuringCall = " + isSatelliteDuringCall());
        if (!mIsSatelliteDataEnabled || isDingdingForeground() || isSatelliteDuringCall()) {
            return;
        }
        setSatelliteDataEnableByUser(this.mContext, false);
    }

    public void updateNtnChannelTypePreferredIfNeed() {
        if (MiuiPhoneUtils.isTiantongSatelliteDataSupported() && MiuiPhoneUtils.isSatelliteVersionDingTalkInstalled(this.mContext)) {
            SystemProperties.set("persist.radio.ntn_channel_support", "1");
            logd("update ntn channel type preferred to uart and mux ");
        } else {
            SystemProperties.set("persist.radio.ntn_channel_support", "0");
            logd("update ntn channel type preferred to uart only");
        }
    }

    public void processFrequencyForDataEnable() {
        if (this.mSatelliteManager.getController().isNeedOpenSatelliteData()) {
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            logd("processFrequencyForDataEnable bandId=" + defaultPhone.getNtnBandId());
            stopDelayForDisableDataFrequency();
            if (defaultPhone.getNtnBandId() == 565) {
                setSatelliteDataEnableByUser(this.mContext, true);
                return;
            }
            int i = this.mExcuteChangeFrequencyCount;
            if (i > 0) {
                logd("already excute changeFrequencyForDataEnable");
                return;
            }
            this.mExcuteChangeFrequencyCount = i + 1;
            this.mDataStart = true;
            restartSatelliteForDataEnable();
        }
    }

    public boolean isIdleForChannelState() {
        return this.mSatelliteManager.getController().getChannelState() == 0;
    }

    public void resetSatelliteDataState() {
        logd("resetSatelliteDataState");
        setDingdingForeground(false);
        setSatelliteDuringCall(false);
        setExcuteChangeFrequencyCount(0);
        setDataStart(false);
        stopDelayForReleaseNtnData();
        stopDelayForDisableDataFrequency();
    }

    private void restartSatelliteForDataEnable() {
        this.mSatelliteManager.getController().restareSatelliteForData();
    }

    public boolean isSatelliteDataEnabled() {
        return mIsSatelliteDataEnabled;
    }

    public void setSatelliteDataEnable(boolean z) {
        mIsSatelliteDataEnabled = z;
    }

    public boolean isDataStart() {
        return this.mDataStart;
    }

    public void setDataStart(boolean z) {
        this.mDataStart = z;
    }

    public void setExcuteChangeFrequencyCount(int i) {
        this.mExcuteChangeFrequencyCount = i;
    }

    public boolean isDingdingForeground() {
        return this.mIsDingdingForeground;
    }

    public void setDingdingForeground(boolean z) {
        this.mIsDingdingForeground = z;
    }

    public boolean isSatelliteDuringCall() {
        return this.mIsSatelliteDuringCall;
    }

    public void setSatelliteDuringCall(boolean z) {
        this.mIsSatelliteDuringCall = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteDataController", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteDataController", str);
    }
}
