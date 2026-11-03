package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.ims.ProvisioningManager;
import android.text.TextUtils;
import com.android.id.IdentifierManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.OneTrackImpl;
import com.android.phone.utils.PubSubTrackImpl;
import com.xiaomi.hyperradio.HyperRadioCallbackBase;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.hyperradio.IRemoteConnectionCallback;
import com.xiaomi.internal.hyperradio.radio.NrIcon;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class FiveGManager extends FiveGManagerBase implements IRemoteConnectionCallback {
    private static String AT_CMD_BANDS = "+EPBSEH";
    private static String[] mDisableN28Cmd;
    private static String[] mDisableN5AndN8Cmd;
    private static String[] mEnableN28Cmd;
    private static String[] mEnableN5AndN8Cmd;
    private static RilHook mMiRilHook;
    private final int AT_CMD_5G_MODE_SA_OFF_AND_NSA_OFF;
    private final int AT_CMD_5G_MODE_SA_OFF_AND_NSA_ON;
    private final int AT_CMD_5G_MODE_SA_ON_AND_NSA_OFF;
    private final int AT_CMD_5G_MODE_SA_ON_AND_NSA_ON;
    private final int DELAY_TIMES_FOR_UPDATE_BANDS;
    private final int DISABLE_DATA_CHANNEL;
    private final int DISABLE_DATA_CHANNEL_AT_LEAST_V;
    private final int ENABLE_DATA_CHANNEL;
    private final int ENABLE_DATA_CHANNEL_AT_LEAST_V;
    private final int SA_ON_AND_NSA_ON_OPTION1;
    private final int SA_ON_AND_NSA_ON_OPTION2;
    private final int SA_ON_AND_NSA_ON_OPTION3;
    public List bandList;
    private int mAOLGestureEnable;
    private final HashSet mCarrierConfigListeners;
    private String[] mCurretSimState;
    private boolean mDataChannelInitialized;
    private boolean mFiveGA4CCRegistration;
    private volatile boolean mFiveGARegistration;
    private FiveGManagerBase.FiveGNetworkModeListener mFiveGNetworkModeListener;
    private volatile boolean mHandle5GNetworkModeCompleted;
    private HashSet mIFiveGCarrierConfigChangedListeners;
    private volatile boolean mMiRilHookReady;
    private List mModemFeatureList;
    private NrIcon[] mNrIcons;
    private boolean[] mNsaStatus;
    private boolean[] mSaStatus;
    private ServiceCallback[] mServiceCallbacks;
    private int mT1Enable;
    private boolean mWaitingRadioOff;
    private final int NR_DISABLE_VoNR = 0;
    private final int NR_ENABLE_VoNR = 1;
    private final int NR_ENABLE_SA_AND_NSA = 0;
    private final int NR_DISABLE_SA = 1;
    private final int NR_DISABLE_NSA = 2;
    private final int NR_DISABLE_NRCA = 0;
    private final int NR_ENABLE_NRCA = 1;
    private final int NR_NOT_SET_5GA = -1;
    private final int NR_DISABLE_5GA = 0;
    private final int NR_ENABLE_5GA = 1;
    private final int NR_DISABLE_5GA_4CC = 0;
    private final int NR_ENABLE_5GA_4CC = 1;
    private final int NR_DISABLE_ULCA = 0;
    private final int NR_ENABLE_ULCA = 1;
    private final int NR_DISABLE_SUL = 0;
    private final int NR_ENABLE_SUL = 1;
    private final int NR_DISABLE_FULL_POWER_MODE = 0;
    private final int NR_ENABLE_FULL_POWER_MODE = 1;
    private final int NR_DISABLE_WUS = 0;
    private final int NR_ENABLE_WUS = 1;
    private final int NR_DISABLE_RRC_SEGMENTATION = 0;
    private final int NR_ENABLE_RRC_SEGMENTATION = 1;
    private final int NR_DISABLE_RRC_INACTIVE = 1;
    private final int NR_ENABLE_RRC_INACTIVE = 0;
    private final int NR_DISABLE_BWP_SWITCH = 0;
    private final int NR_ENABLE_BWP_SWITCH = 1;
    private final int NR_DISABLE_CHO = 0;
    private final int NR_ENABLE_CHO = 1;
    private final int DISABLE = 0;
    private final int ENABLE = 1;

    private int msgWhatSet5GCMDForSlot(int i) {
        return i == 0 ? 13 : 14;
    }

    static {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        mEnableN28Cmd = new String[i];
        mDisableN28Cmd = new String[i];
        mEnableN5AndN8Cmd = new String[i];
        mDisableN5AndN8Cmd = new String[i];
    }

    class RilHookStateCallback implements BaseEvent.IEventListener {
        private RilHookStateCallback() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.RilEvent) {
                FiveGManager.this.mMiRilHookReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
                FiveGManagerBase.logi("mMiRilHookReady : " + FiveGManager.this.mMiRilHookReady);
            }
        }
    }

    public static FiveGManagerBase make() {
        if (FiveGManagerBase.mInstance instanceof FiveGManager) {
            return FiveGManagerBase.mInstance;
        }
        FiveGManagerBase.mInstance = new FiveGManager();
        return FiveGManagerBase.mInstance;
    }

    private FiveGManager() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mNsaStatus = new boolean[i];
        this.mSaStatus = new boolean[i];
        this.AT_CMD_5G_MODE_SA_OFF_AND_NSA_OFF = 1;
        this.AT_CMD_5G_MODE_SA_ON_AND_NSA_OFF = 3;
        this.AT_CMD_5G_MODE_SA_OFF_AND_NSA_ON = 5;
        this.AT_CMD_5G_MODE_SA_ON_AND_NSA_ON = 7;
        this.SA_ON_AND_NSA_ON_OPTION1 = 0;
        this.SA_ON_AND_NSA_ON_OPTION2 = 1;
        this.SA_ON_AND_NSA_ON_OPTION3 = 2;
        this.DELAY_TIMES_FOR_UPDATE_BANDS = 3000;
        this.mAOLGestureEnable = -1;
        this.mT1Enable = -1;
        this.bandList = Arrays.asList("n5_band", "n8_band", "n28_band");
        this.DISABLE_DATA_CHANNEL = 0;
        this.ENABLE_DATA_CHANNEL = 1;
        this.DISABLE_DATA_CHANNEL_AT_LEAST_V = 0;
        this.ENABLE_DATA_CHANNEL_AT_LEAST_V = 2;
        this.mDataChannelInitialized = false;
        this.mWaitingRadioOff = false;
        this.mHandle5GNetworkModeCompleted = true;
        this.mMiRilHookReady = false;
        this.mNrIcons = new NrIcon[i];
        this.mServiceCallbacks = new ServiceCallback[i];
        this.mFiveGARegistration = false;
        this.mFiveGA4CCRegistration = false;
        this.mFiveGNetworkModeListener = null;
        this.mIFiveGCarrierConfigChangedListeners = new HashSet();
        this.mCurretSimState = new String[]{"", ""};
        this.mCarrierConfigListeners = new HashSet();
        mMiRilHook = RilHook.init(FiveGManagerBase.mContext);
        this.mHandler.post(new Runnable() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            ServiceCallback[] serviceCallbackArr = this.mServiceCallbacks;
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            serviceCallbackArr[i2] = new ServiceCallback(i2, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler));
        }
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookStateCallback(), this.mHandler);
        HyperRadioManager.getInstance(FiveGManagerBase.mContext).connect(this);
        if (!ModemProvider.IS_QCOM) {
            this.mMiRilHookReady = true;
        }
        FiveGManagerBase.logi("FiveGManager inited!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        CallOptimizeManager.make();
        if (ModemProvider.IS_MTK) {
            registerSimStateReceiver();
            initMTKRFCloudManager();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void init() {
        super.init();
        FiveGPowerController.init();
        FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler != null) {
            fiveGHandler.sendEmptyMessage(0);
        }
        Settings.Global.putString(FiveGManagerBase.mContext.getContentResolver(), "miui_cpu_model", FiveGManagerBase.mContext.getResources().getString(R.string.config_miui_cpu_model));
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onInitFiveGData() {
        super.onInitFiveGData();
        if (ModemProvider.IS_MTK) {
            updateAll5GCmdStatus();
            querySupportedBandsStatus();
            if (FiveGCloudController.getInstance() != null) {
                FiveGCloudController.getInstance().getCurrentLocation();
                return;
            }
            return;
        }
        if (ModemProvider.IS_QCOM) {
            String[] strArr = {"persist.vendor.radio.5g_mode_pref", "persist.vendor.radio.5g_mode_pref_0", "persist.vendor.radio.5g_mode_pref_1"};
            for (int i = 0; i < 3; i++) {
                String str = strArr[i];
                if (SystemProperties.getInt(str, 0) != 0) {
                    try {
                        FiveGManagerBase.logi("set persist.vendor.radio.5g_mode_pref to 0");
                        SystemProperties.set(str, "0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onEnableFiveGUserAsync(Message message) {
        if (ModemProvider.IS_MTK || ModemProvider.IS_SPRD) {
            onSetUserFiveGEnabled((FiveGManagerBase.SwitchStatus) message.obj);
            return;
        }
        if (ModemProvider.IS_QCOM) {
            FiveGManagerBase.SwitchStatus switchStatus = (FiveGManagerBase.SwitchStatus) message.obj;
            if (MiuiPhoneUtils.shouldRadioOffBeforeChangeFiveGState(MiuiPhoneUtils.getPhone(switchStatus.slotId))) {
                FiveGManagerBase.log("radio power off before enable 5G.");
                this.mHandle5GNetworkModeCompleted = false;
                PhoneGlobals.getPhone().mCi.registerForOffOrNotAvailable(this.mHandler, 4, message.obj);
                this.mWaitingRadioOff = true;
                PhoneUtils.setRadioPower(false);
                return;
            }
            onSetUserFiveGEnabled(switchStatus);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onHandleRadioPowerOff(Message message) {
        Object obj = message.obj;
        if (obj == null) {
            FiveGManagerBase.log("onHandleRadioPowerOff msg.obj is null");
            this.mWaitingRadioOff = false;
            this.mHandle5GNetworkModeCompleted = true;
            PhoneGlobals.getPhone().mCi.unregisterForOffOrNotAvailable(this.mHandler);
            notifyFiveGNetworkModeCompleted();
            return;
        }
        FiveGManagerBase.SwitchStatus switchStatus = (FiveGManagerBase.SwitchStatus) ((AsyncResult) obj).userObj;
        Phone phone = MiuiPhoneUtils.getPhone(switchStatus.slotId);
        boolean z = Settings.Global.getInt(FiveGManagerBase.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        if (MiuiPhoneUtils.shouldRadioOffBeforeChangeFiveGState(phone) && this.mWaitingRadioOff) {
            FiveGManagerBase.log("radio power off done");
            this.mWaitingRadioOff = false;
            if (!z) {
                PhoneUtils.setRadioPower(true);
            }
            FiveGManagerBase.log("radio power off done, slotId: " + switchStatus.slotId + ", enable 5g: " + switchStatus.enabled);
            onSetUserFiveGEnabled(switchStatus);
            PhoneGlobals.getPhone().mCi.unregisterForOffOrNotAvailable(this.mHandler);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onSetPreferredNetworkModeDone(Message message) {
        FiveGManagerBase.SwitchStatus switchStatus = (FiveGManagerBase.SwitchStatus) ((AsyncResult) message.obj).userObj;
        FiveGManagerBase.log("set preferred network type done, slotId: " + switchStatus.slotId + ", enable 5g: " + switchStatus.enabled);
        if (ModemProvider.IS_MTK || ModemProvider.IS_SPRD) {
            MiuiPhoneUtils.setNetworkModeInDb(null, switchStatus.slotId, switchStatus.networkType);
            return;
        }
        if (ModemProvider.IS_QCOM) {
            Phone phone = MiuiPhoneUtils.getPhone(switchStatus.slotId);
            boolean z = Settings.Global.getInt(FiveGManagerBase.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
            if (MiuiPhoneUtils.shouldRadioOffBeforeChangeFiveGState(phone)) {
                if (message.obj == null) {
                    if (z) {
                        return;
                    }
                    PhoneUtils.setRadioPower(true);
                    return;
                } else {
                    MiuiPhoneUtils.setNetworkModeInDb(null, switchStatus.slotId, switchStatus.networkType);
                    if (!z) {
                        PhoneUtils.setRadioPower(true);
                    }
                    this.mHandle5GNetworkModeCompleted = true;
                    notifyFiveGNetworkModeCompleted();
                    return;
                }
            }
            MiuiPhoneUtils.setNetworkModeInDb(null, switchStatus.slotId, switchStatus.networkType);
        }
    }

    private void execSetUserFiveGNetworkModeInternal(int i, int i2, boolean z) {
        FiveGManagerBase.log("set user SA switch mode to " + i + ", RilHookReady: " + this.mMiRilHookReady);
        if (this.mMiRilHookReady) {
            if (i2 == DefaultSimManager.getDefaultDataSlotId()) {
                SystemProperties.set("persist.radio.sa.user.enabled", isUserFiveGSaEnabled() ? "1" : "0");
            }
            try {
                FiveGManagerBase.log("execSetUserFiveGNetworkMode with result : " + ModemUtils.getInstance().callQcrilHookSet("execSetUserFiveGNetworkMode", Integer.valueOf(i == 2 ? 0 : 1), Integer.valueOf(i2)));
            } catch (Exception e) {
                FiveGManagerBase.loge("Set SA exception: " + e.getMessage());
            }
        }
    }

    public void execSetUserVoNREnabled(int i, boolean z) {
        if (ModemProvider.IS_QCOM) {
            int phoneIdForSubscription = SubscriptionManager.getDefault().getPhoneIdForSubscription(i);
            if (SubscriptionManager.isValidSlotId(phoneIdForSubscription) && this.mMiRilHookReady) {
                FiveGManagerBase.log("set user VoNR Enabled to " + z + ", RilHookReady: " + this.mMiRilHookReady + ", slotId: " + phoneIdForSubscription);
                try {
                    ByteBuffer byteBuffer = (ByteBuffer) ModemUtils.getInstance().callHook("execSetUserVoNREnabled", Integer.valueOf(phoneIdForSubscription), Integer.valueOf(z ? 1 : 0));
                    if (byteBuffer != null) {
                        FiveGManagerBase.log("Set slot " + phoneIdForSubscription + " VoNR Response is: " + byteBuffer.toString() + "mResBuf[0] = " + String.valueOf((int) ((byte) byteBuffer.getInt())));
                        return;
                    }
                    FiveGManagerBase.log("Set slot " + phoneIdForSubscription + " VoNR Response is null.");
                } catch (Exception e) {
                    FiveGManagerBase.loge("Set VoNR exception: " + e.getMessage());
                }
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isHandle5GNetworkModeCompleted() {
        return this.mHandle5GNetworkModeCompleted;
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void execSetUserFiveGNetworkMode(final int i, final int i2, final boolean z) {
        boolean z2 = i == 2;
        if (ModemProvider.IS_XRING) {
            Phone phone = MiuiPhoneUtils.getPhone(i2);
            if (phone == null) {
                FiveGManagerBase.loge("execSetUserFiveGNetworkMode failed, phone is null");
                return;
            } else {
                phone.setN1ModeEnabled(z2, (Message) null);
                return;
            }
        }
        if (ModemProvider.IS_MTK) {
            onSet5GSaAtCommand(z2, i2);
            return;
        }
        if (ModemProvider.IS_SPRD) {
            onSet5GSaForSprd(z2, i2);
            return;
        }
        if (i2 == DefaultSimManager.getDefaultDataSlotId()) {
            if (((MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, i2)).findConnectingInternetDataNetwork() != null) {
                FiveGManagerBase.log("Slot " + i2 + " (default), delay 1s to set 5G mode");
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execSetUserFiveGNetworkMode$1(i, i2, z);
                    }
                }, 1000L);
                return;
            }
            execSetUserFiveGNetworkModeInternal(i, i2, z);
            return;
        }
        execSetUserFiveGNetworkModeInternal(i, i2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$execSetUserFiveGNetworkMode$1(int i, int i2, boolean z) {
        try {
            execSetUserFiveGNetworkModeInternal(i, i2, z);
            FiveGManagerBase.localLog("execSetUserFiveGNetworkMode", "set 5G mode to " + i);
        } catch (Exception e) {
            FiveGManagerBase.loge("Error setting 5G mode: " + e.getMessage());
        }
    }

    private void onSet5GSaAtCommand(boolean z, int i) {
        if (MiuiPhoneUtils.is5gSaOnlyDevice()) {
            onSet5GSaAtCommandForSaOnlyDevice(z, i);
        } else if (z) {
            enableNsaAndEnableSA(i);
        } else {
            enableNsaAndDisableSA(i);
        }
    }

    private void onSet5GSaForSprd(boolean z, int i) {
        try {
            Class<?> cls = Class.forName("com.android.internal.telephony.UniFrameworkTelephonyManager");
            Class[] clsArr = new Class[0];
            Object objInvoke = cls.getMethod("getInstance", null).invoke(null, null);
            if (objInvoke == null) {
                FiveGManagerBase.loge("onSet5GSaForSprd UniFrameworkTelephonyManager is null");
                return;
            }
            FiveGManagerBase.log("onSet5GSaForSprd enabled: " + z + ", slotId: " + i);
            Class<?> cls2 = Integer.TYPE;
            cls.getMethod("setSa", cls2, cls2, Messenger.class, cls2, cls2).invoke(objInvoke, Integer.valueOf(z ? 1 : 0), 1, null, 0, Integer.valueOf(i));
        } catch (Exception e) {
            FiveGManagerBase.loge("onSet5GSaForSprd error: " + e);
        }
    }

    private void onSet5GSaAtCommandForSaOnlyDevice(boolean z, int i) {
        if (z) {
            enableSaForSaOnlyDevice(i);
        } else {
            disableNsaAndDisableSA(i);
        }
    }

    private void enableNsaAndEnableSA(int i) {
        if (MiuiPhoneUtils.shouldUseSAoption3forMtk()) {
            sendAtCommand("AT+E5GOPT=7,2", "", msgWhatSet5GCMDForSlot(i), i);
        } else {
            sendAtCommand("AT+E5GOPT=7", "", msgWhatSet5GCMDForSlot(i), i);
        }
        FiveGManagerBase.log("enabling SA and NSA");
    }

    private void enableSaForSaOnlyDevice(int i) {
        sendAtCommand("AT+E5GOPT=3,2", "", msgWhatSet5GCMDForSlot(i), i);
        FiveGManagerBase.log("enabling SA for SA only device");
    }

    private void enableNsaAndDisableSA(int i) {
        sendAtCommand("AT+E5GOPT=5", "", msgWhatSet5GCMDForSlot(i), i);
        FiveGManagerBase.log("enabling NSA and disabling SA");
    }

    private void disableNsaAndDisableSA(int i) {
        sendAtCommand("AT+E5GOPT=1", "", msgWhatSet5GCMDForSlot(i), i);
        FiveGManagerBase.log("disabling NSA and disabling SA");
    }

    @Override // com.android.phone.FiveGManagerBase
    public void enableDSDA() {
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            hyperRadioManager.setMsimPreference(0, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.1
                public void onResponse(int i) {
                    FiveGManagerBase.log("setMsimPreference with error code " + i);
                }
            });
        } else if (ModemProvider.IS_MTK) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                try {
                    FiveGManagerBase.log("enableDSDA: phoneId=" + i);
                    sendAtCommand("AT+ESBP=5,\"SBP_DSDA\",1", "", 15, i);
                    sendAtCommand("AT+ESBP=5,\"SBP_DRDSDA\",1", "", 10000, i);
                    sendAtCommand(i, new String[]{"AT+ESBP=5,\"SBP_DR_SEARCH\",1", ""}, (Message) null);
                } catch (Exception e) {
                    FiveGManagerBase.log("enableDSDA : Exception " + e);
                    return;
                }
            }
        }
        FiveGManagerBase.log("enabling DSDA");
    }

    @Override // com.android.phone.FiveGManagerBase
    public void disableDSDA() {
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            hyperRadioManager.setMsimPreference(1, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.2
                public void onResponse(int i) {
                    FiveGManagerBase.log("setMsimPreference with error code " + i);
                }
            });
        } else if (ModemProvider.IS_MTK) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                try {
                    FiveGManagerBase.log("disableDSDA: phoneId=" + i);
                    sendAtCommand("AT+ESBP=5,\"SBP_DSDA\",0", "", 16, i);
                    sendAtCommand("AT+ESBP=5,\"SBP_DRDSDA\",0", "", 10000, i);
                    sendAtCommand(i, new String[]{"AT+ESBP=5,\"SBP_DR_SEARCH\",0", ""}, (Message) null);
                } catch (Exception e) {
                    FiveGManagerBase.log("disableDSDA : Exception " + e);
                    return;
                }
            }
        }
        FiveGManagerBase.log("disabling DSDA");
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onQueryFiveGCommandDone(Message message, int i) {
        Object obj;
        FiveGManagerBase.log("Receive msg from modem, slotId: " + i);
        if (SubscriptionManager.isValidSlotId(i)) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult != null && asyncResult.exception == null && (obj = asyncResult.result) != null) {
                String[] strArr = (String[]) obj;
                if (strArr == null || strArr.length < 1) {
                    FiveGManagerBase.log("queryResult is empty,Query NR status failed.");
                    return;
                }
                int currentNrMode = parseCurrentNrMode(strArr[0]);
                FiveGManagerBase.log("EVENT_QUERY_5G_CMD mode = " + currentNrMode);
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    FiveGManagerBase.log("EVENT_QUERY_5G_CMD succeed,result " + i2 + " = " + strArr[i2]);
                }
                if (currentNrMode == 1) {
                    this.mNsaStatus[i] = false;
                    this.mSaStatus[i] = false;
                } else if (currentNrMode == 3) {
                    this.mNsaStatus[i] = false;
                    this.mSaStatus[i] = true;
                } else if (currentNrMode == 5) {
                    this.mNsaStatus[i] = true;
                    this.mSaStatus[i] = false;
                } else if (currentNrMode == 7) {
                    this.mNsaStatus[i] = true;
                    this.mSaStatus[i] = true;
                }
                for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                    FiveGManagerBase.log("EVENT_QUERY_5G_CMD succeed,  mNsaStatus[" + i3 + "]=" + this.mNsaStatus[i3] + ", mSaStatus[" + i3 + "]=" + this.mSaStatus[i3]);
                }
                return;
            }
            FiveGManagerBase.log("Query NR status failed.");
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onQuerySupportedBandsDone(Message message, int i) {
        Object obj;
        FiveGManagerBase.log("Receive msg from modem, slotId: " + i);
        if (SubscriptionManager.isValidSlotId(i)) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult != null && asyncResult.exception == null && (obj = asyncResult.result) != null) {
                String[] strArr = (String[]) obj;
                if (strArr == null || strArr.length < 1) {
                    FiveGManagerBase.log("queryResult is empty,Query NR band failed.");
                    return;
                }
                if (strArr.length > 0) {
                    FiveGManagerBase.log(i + " Query supported bands succeed,result = " + strArr[0]);
                    return;
                }
                return;
            }
            FiveGManagerBase.log("Query supported bands failed.");
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onSetDSDACommandEnabledDone(Message message) {
        FiveGManagerBase.log("onSetDSDACommandEnabledDone");
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult != null && asyncResult.exception == null) {
            setDSDAStateBySetting(1);
            FiveGManagerBase.log("enable DSDA status succeed.");
        } else {
            FiveGManagerBase.log("enable DSDA status failed.");
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onSetDSDACommandDisabledDone(Message message) {
        FiveGManagerBase.log("onSetDSDACommandDisabledDone");
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult != null && asyncResult.exception == null) {
            setDSDAStateBySetting(0);
            FiveGManagerBase.log("disable DSDA status succeed.");
        } else {
            FiveGManagerBase.log("disable DSDA status failed.");
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onSetFiveGCommandDone(Message message, int i) {
        FiveGManagerBase.log("EVENT_SET_5G_CMD configuration is done, slotId: " + i);
        update5GCmdStatus(i);
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onSetModemFeatureCommandDone(Message message, int i) {
        FiveGManagerBase.log("onSetModemFeatureCommandDone, slotId: " + i);
        sendAtCommand(i, new String[]{"AT+EGMC=1,\"NR_CAP_UPDATE\"", ""}, (Message) null);
    }

    private int parseCurrentNrMode(String str) {
        try {
            return Integer.valueOf(str.substring(8).trim()).intValue();
        } catch (NumberFormatException unused) {
            FiveGManagerBase.log("Wrong current mode format: " + str);
            return -1;
        }
    }

    private void update5GCmdStatus(int i) {
        sendAtCommand("AT+E5GOPT?", "+E5GOPT:", i == 0 ? 10 : 11, i);
    }

    private void querySupportedBandsStatus(int i) {
        int i2 = i == 0 ? 22 : 23;
        sendAtCommand("AT" + AT_CMD_BANDS + "?", AT_CMD_BANDS + ":", i2, i);
    }

    private void updateAll5GCmdStatus() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            update5GCmdStatus(i);
        }
    }

    private void querySupportedBandsStatus() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            querySupportedBandsStatus(i);
        }
    }

    private void sendAtCommand(String str, String str2, int i, int i2) {
        sendAtCommand(i2, new String[]{str, str2}, this.mHandler.obtainMessage(i));
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void fetch5GIconDisplayRule(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        try {
            phone.invokeOemRilRequestRaw(getNrDisplayRule(phone).getBytes(), (Message) null);
        } catch (Exception e) {
            FiveGManagerBase.log("endc_deactivation invokeOemRilRequestRaw failed:" + e);
        }
    }

    protected String getNrDisplayRule(Phone phone) {
        int subId = phone.getSubId();
        StringBuilder sb = new StringBuilder("UPDATE_DISPLAY_RULE_BY_SLOT,");
        int intConfigForSubId = CarrierConfigManagerEx.getIntConfigForSubId(phone.getContext(), subId, "config_nr_display_rule", 1);
        int i = 30;
        int intConfigForSubId2 = CarrierConfigManagerEx.getIntConfigForSubId(phone.getContext(), subId, "config_nr_display_timer1", 30);
        int intConfigForSubId3 = CarrierConfigManagerEx.getIntConfigForSubId(phone.getContext(), subId, "config_nr_display_timer2", 30);
        if (MiuiPhoneUtils.isCmccSim(phone) && Build.IS_CM_CUSTOMIZATION) {
            intConfigForSubId = 4;
            intConfigForSubId3 = 30;
        } else {
            i = intConfigForSubId2;
        }
        FiveGManagerBase.log("getNrDisplayRuleForSubId  rule=" + intConfigForSubId + ";timer1=" + i + ";timer2=" + intConfigForSubId3 + ";subId=" + subId);
        sb.append(intConfigForSubId);
        sb.append(",0,0,");
        sb.append(i);
        sb.append(",");
        sb.append(intConfigForSubId3);
        return sb.toString();
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isVoNREnabled(int i) {
        boolean vonrSetting;
        if (ModemProvider.IS_QCOM) {
            return super.isVoNREnabled(i);
        }
        boolean z = ModemProvider.IS_SPRD;
        if (!z && !FiveGUtils.isVonrSettingSupportedForMtk()) {
            FiveGManagerBase.log("isVoNREnabled vonr setting not support.");
            return true;
        }
        int slotIdForSubscription = SubscriptionManager.getDefault().getSlotIdForSubscription(i);
        if (!MiuiPhoneUtils.isValidatePhoneId(slotIdForSubscription)) {
            FiveGManagerBase.log("isVoNREnabled invalid slotId id, return.");
            return false;
        }
        if (z) {
            vonrSetting = PhoneGlobals.getInstance().phoneMgr.isVoNrEnabled(i);
        } else {
            vonrSetting = getVonrSetting(slotIdForSubscription);
        }
        FiveGManagerBase.log("isVoNREnabled subId = " + i + ", enabled = " + vonrSetting);
        return vonrSetting;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserVoNREnabled(int i, boolean z) {
        boolean vonrSetting;
        if (ModemProvider.IS_QCOM) {
            super.setUserVoNREnabled(i, z);
            return;
        }
        boolean z2 = ModemProvider.IS_SPRD;
        if (!z2 && !FiveGUtils.isVonrSettingSupportedForMtk()) {
            FiveGManagerBase.log("setUserVoNREnabled vonr setting not support.");
            return;
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            FiveGManagerBase.log("setUserVoNREnabled invalid slotId, return.");
            return;
        }
        if (!isUserFiveGSaEnabled(i)) {
            FiveGManagerBase.log("setUserVoNREnabled 5G SA switch disabled, return.");
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        if (z2) {
            vonrSetting = PhoneGlobals.getInstance().phoneMgr.isVoNrEnabled(subscriptionIdForSlot);
        } else {
            vonrSetting = getVonrSetting(i);
        }
        FiveGManagerBase.log("setUserVoNREnabled slotId = " + i + ", enabled = " + z + ", current status = " + vonrSetting);
        if (z == vonrSetting) {
            FiveGManagerBase.log("setUserVoNREnabled vonr setting no need to change, return.");
        } else {
            setModemFeatureBySlot(new FiveGManagerBase.ModemFeatureStatus(i, 4, z ? 1 : 0));
            sendMessageQueryVonrState(i);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setDataChannelEnable(int i, boolean z) {
        char c;
        int i2 = 1006;
        if (ModemProvider.IS_QCOM) {
            try {
                ProvisioningManager provisionManager = getProvisionManager(i);
                if (provisionManager != null) {
                    provisionManager.setProvisioningIntValue(1006, z ? 1 : 0);
                    FiveGManagerBase.log("setDataChannelEnable " + z + " slotId: " + i);
                    return;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        if (MiuiPhoneUtils.isSupportFontdoDcDevice()) {
            c = z ? 1 : 0;
        } else {
            c = z ? (char) 2 : (char) 0;
            i2 = 17;
        }
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            try {
                RIL ril = phone.mCi;
                if (ril == null) {
                    FiveGManagerBase.log("setDataChannelEnable ril is null");
                    return;
                }
                ril.setVendorSetting(i2, Integer.toString(c), (Message) null);
                if (!this.mDataChannelInitialized) {
                    initDataChannelService();
                    this.mDataChannelInitialized = true;
                }
                FiveGManagerBase.log("setDataChannelEnable " + z + " slotId: " + i);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isDataChannelEnable(int i) {
        int provisioningIntValue;
        if (ModemProvider.IS_QCOM) {
            try {
                ProvisioningManager provisionManager = getProvisionManager(i);
                provisioningIntValue = provisionManager != null ? provisionManager.getProvisioningIntValue(1006) : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            FiveGManagerBase.log("isDataChannelEnable " + provisioningIntValue + " slotId: " + i);
            return provisioningIntValue == 1;
        }
        if (SystemProperties.getInt("persist.vendor.mtk_dc_call_capability", 0) == 0) {
            return false;
        }
        if (i == 0) {
            return SystemProperties.getInt("persist.vendor.mtk.dch.enable", 0) != 0;
        }
        return i == 1 && SystemProperties.getInt("persist.vendor.mtk.dch.enable.2", 0) != 0;
    }

    private void initDataChannelService() {
        if (MiuiPhoneUtils.isSupportFontdoDcDevice()) {
            return;
        }
        FiveGManagerBase.log("initDataChannelService");
        Intent intent = new Intent();
        intent.setAction("com.newcalllib.datachannel.V1_0.ImsDataChannelService");
        intent.setComponent(new ComponentName("com.mediatek.datachannel.service", "com.mediatek.datachannel.service.DataChannelService"));
        FiveGManagerBase.mContext.startService(intent);
    }

    private void initMTKRFCloudManager() {
        FiveGManagerBase.logi("MTKRFCloudManager initialization started");
        MTKRFCloudManager.init(FiveGManagerBase.mContext);
        MTKRFCloudManager.initCloudDataOnBoot(FiveGManagerBase.mContext);
        FiveGManagerBase.logi("MTKRFCloudManager initialization completed");
    }

    @Deprecated
    public static boolean isFiveGCapable() {
        return MiuiPhoneUtils.isFiveGCapable();
    }

    @Override // com.android.phone.FiveGManagerBase
    public int getNrConfigType(int i) {
        if (ModemProvider.IS_QCOM) {
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            if (fiveGServiceInfo == null) {
                return -1;
            }
            return fiveGServiceInfo.getNrConfigType();
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return -1;
        }
        ServiceState serviceState = phone.getServiceState();
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        int nrState = serviceState.getNrState();
        if (rilDataRadioTechnology == 20) {
            return 1;
        }
        return (nrState == 3 || nrState == 2) ? 0 : -1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public int getXMNetworkType(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return 0;
        }
        if (phone.getServiceState().getNrState() == 3 || phone.getServiceState().getNrState() == 2) {
            return 20;
        }
        return phone.getServiceState().getDataNetworkType();
    }

    @Override // com.android.phone.FiveGManagerBase
    public int get5GIndicatorStatus(int i) {
        if (!ModemProvider.IS_QCOM) {
            return -1;
        }
        if (PhoneFactory.getPhone(i).getServiceState().getState() == 1) {
            return 3;
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        if (fiveGServiceInfo == null || !fiveGServiceInfo.isConnectedOnNsaMode()) {
            return 4;
        }
        return SystemProperties.getInt("persist.sys.lgu.5g.indicator", 4);
    }

    @Override // com.android.phone.FiveGManagerBase
    public int getUpperlayerStatus(int i) {
        FiveGServiceInfo fiveGServiceInfo;
        return (ModemProvider.IS_QCOM && (fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i)) != null && fiveGServiceInfo.getUpperLayerInd() == 1 && fiveGServiceInfo.getPlmn() == 1) ? 1 : 0;
    }

    @Override // com.android.phone.FiveGManagerBase
    public int getRestrictDcnrStatus(int i) {
        if (!ModemProvider.IS_QCOM) {
            return 0;
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        return (fiveGServiceInfo != null && fiveGServiceInfo.getDcnr() == 1) ? 0 : 1;
    }

    public static boolean isDualNrEnabled() {
        return FiveGManagerBase.isDualNrEnabled();
    }

    @Override // com.android.phone.FiveGManagerBase
    public void execSetFiveGCarrierAggregationMode(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(7, i, i2);
            needRefreshMbn(i2);
            return;
        }
        boolean z = i == 1;
        int subId = MiuiPhoneUtils.getPhone(i2).getSubId();
        if (PreferenceUtils.getInstance().getIntPreference("CloudNrcaStatus" + subId, -1) == 0) {
            FiveGManagerBase.log("cloud control nrca status is closed, skip enable nrca");
        } else {
            enableNRCA(i2, z);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean getFiveGCarrierAggregationStatus(int i) {
        String[] strArr;
        String str;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(7, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (ModemProvider.IS_MTK) {
            int subId = MiuiPhoneUtils.getPhone(i).getSubId();
            int iIntValue = Settings.Global.getInt(FiveGManagerBase.mContext.getContentResolver(), "button_5G_carrier_aggregation" + subId, -1);
            if (iIntValue == -1) {
                Object[] objArr = new Object[1];
                if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_CA\"", "+ESBP"}, objArr) && (str = (strArr = (String[]) objArr[0])[0]) != null && str.length() > 0) {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                }
            }
            if (iIntValue == 1) {
                return true;
            }
        }
        return false;
    }

    public void setFiveGCarrierAggregationInDb(int i, int i2) {
        int subId = MiuiPhoneUtils.getPhone(i2).getSubId();
        Settings.Global.putInt(FiveGManagerBase.mContext.getContentResolver(), "button_5G_carrier_aggregation" + subId, i);
        FiveGManagerBase.log("setFiveGCarrierAggregationInDb subId = " + subId + ", mode=" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isFiveGAEnabled(int i) {
        Context context;
        SharedPreferences defaultSharedPreferences;
        int i2;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(20, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (!ModemProvider.IS_MTK || (context = FiveGManagerBase.mContext) == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
            return false;
        }
        int i3 = defaultSharedPreferences.getInt("5ga_enable", -1);
        FiveGManagerBase.log("isFiveGAEnabled, slotId:" + i + ",db-mode:" + i3);
        if (i3 != -1) {
            return i3 == 1;
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=8,\"SBP_NR_CA_MAX_CC_NUM\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                i2 = 0;
            } else {
                try {
                    int iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                    i2 = Build.IS_INTERNATIONAL_BUILD ? iIntValue - 2 : iIntValue - 1;
                } catch (NumberFormatException | Exception unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isFiveGAEnabled, slotId:" + i + ",mode:" + i2 + ",result:" + strArr[0]);
        } else {
            i2 = 0;
        }
        if (i2 >= 1) {
            return true;
        }
        return false;
    }

    private void sync5GAUsermodeToModem(int i) {
        SharedPreferences defaultSharedPreferences;
        try {
            if (!TelephonyManagerEx.getDefault().isFiveGASupported()) {
                FiveGManagerBase.log("sync5gAUsermodeToModem, stop for, not supported, slotId:" + i);
                return;
            }
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (i != defaultDataSlotId) {
                FiveGManagerBase.log("sync5gAUsermodeToModem, stop for:dataSlotId:" + defaultDataSlotId + ",slotId:" + i);
                return;
            }
            Context context = FiveGManagerBase.mContext;
            if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
                return;
            }
            int i2 = defaultSharedPreferences.getInt("5ga_enable", -1);
            if (i2 == -1) {
                FiveGManagerBase.log("sync5gAUsermodeToModem, stop for:dbMode");
                return;
            }
            FiveGManagerBase.log("sync5gAUsermodeToModem, slotId:" + i + ",db-mode:" + i2);
            boolean z = true;
            if (i2 != 1) {
                z = false;
            }
            enable5GA(i, z);
        } catch (Exception e) {
            FiveGManagerBase.log("sync5gAUsermodeToModem" + e);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserFiveGAEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(20, i, i2);
        } else {
            enable5GA(i2, i == 1);
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
        editorEdit.putInt("5ga_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("setUserFiveGAEnabled, slotId:" + i2 + ", mode :" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isFiveGARegistration() {
        List physicalChannelConfigList;
        if (!ModemProvider.IS_MTK) {
            return this.mFiveGARegistration;
        }
        try {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones == null) {
                return false;
            }
            for (Phone phone : phones) {
                if (phone != null && phone.getServiceState() != null && phone.getServiceState().isUsingCarrierAggregation() && phone.getServiceState().getDataNetworkType() == 20 && phone.getServiceStateTracker() != null && (physicalChannelConfigList = phone.getServiceStateTracker().getPhysicalChannelConfigList()) != null && physicalChannelConfigList.size() == 3 && physicalChannelConfigList.get(0) != null && ((PhysicalChannelConfig) physicalChannelConfigList.get(0)).getNetworkType() == 20) {
                    return true;
                }
            }
        } catch (IllegalStateException e) {
            FiveGManagerBase.log("ex:" + e);
        } catch (Exception e2) {
            FiveGManagerBase.log("ex:" + e2);
        }
        return false;
    }

    public boolean isFiveGARegistration(int i) {
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        return fiveGServiceInfo != null && fiveGServiceInfo.getNrIconType() == 2;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setFiveGARegistration(boolean z) {
        this.mFiveGARegistration = z;
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isFiveGA4CCRegistration() {
        return this.mFiveGA4CCRegistration;
    }

    public void setFiveGA4CCRegistration(boolean z) {
        this.mFiveGA4CCRegistration = z;
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isHOMEnabled(int i) {
        if (!ModemProvider.IS_QCOM) {
            return super.isHOMEnabled(i);
        }
        byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(21, 11, i);
        return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserHOMEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(21, i, i2);
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
            editorEdit.putInt("hom_enable", i);
            editorEdit.apply();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isSwulEnabled(int i) {
        if (!ModemProvider.IS_QCOM) {
            return super.isSwulEnabled(i);
        }
        byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(22, 11, i);
        return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserSwulEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(22, i, i2);
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
            editorEdit.putInt("swul_enable", i);
            editorEdit.apply();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isR16PowerEnabled(int i) {
        if (!ModemProvider.IS_QCOM) {
            return super.isR16PowerEnabled(i);
        }
        byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(23, 11, i);
        return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserR16PowerEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(23, i, i2);
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
            editorEdit.putInt("r16_power_enable", i);
            editorEdit.apply();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserNetSlicingEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
            editorEdit.putInt("net_slicing_enable", i);
            editorEdit.apply();
        }
    }

    public synchronized void setEnableNRCA(boolean z, int i) {
        try {
            FiveGManagerBase.log("setEnableNRCA enable: " + z + ", slotId: " + i);
            if (ModemProvider.IS_QCOM) {
                if (ModemUtils.getInstance() != null) {
                    ModemUtils.getInstance().callQcrilHookSet("qcRilEnableNRCA", Boolean.valueOf(z), Integer.valueOf(i));
                }
            } else {
                enableNRCA(i, z);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public int get3GPPVersion(int i) {
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(26, 11, i);
            if (platformModemFeatureStatus != null) {
                return MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length);
            }
        } else if (ModemProvider.IS_MTK) {
            Context context = FiveGManagerBase.mContext;
            if (context == null || context.getResources() == null) {
                return 0;
            }
            return FiveGManagerBase.mContext.getResources().getInteger(R.integer.config_5ga_3gpp_version);
        }
        return super.get3GPPVersion(i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public void set3GPPVersion(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(26, i, i2);
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
            editorEdit.putInt("set_3gpp_version", i);
            editorEdit.apply();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isFiveGA4CCEnabled(int i) {
        int iIntValue;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(29, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (ModemProvider.IS_MTK) {
            Object[] objArr = new Object[1];
            if (atCmdRunSync(i, new String[]{"AT+ESBP=8,\"SBP_NR_CA_MAX_CC_NUM\"", "+ESBP"}, objArr)) {
                String[] strArr = (String[]) objArr[0];
                String str = strArr[0];
                if (str == null || str.length() <= 0) {
                    iIntValue = 0;
                } else {
                    try {
                        iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue() - 3;
                    } catch (NumberFormatException unused) {
                        return false;
                    }
                }
                FiveGManagerBase.log("isFiveGA4CCEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
            } else {
                iIntValue = 0;
            }
            if (iIntValue >= 1) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserFiveGA4CCEnabled(int i, int i2) {
        if (ModemProvider.IS_QCOM) {
            setModemFeature(29, i, i2);
        } else if (ModemProvider.IS_MTK) {
            enable5GA4CC(i2, i == 1);
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGManagerBase.mContext).edit();
        editorEdit.putInt("5ga_4cc_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("setUserFiveGA4CCEnabled, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isUlCaEnabled(int i) {
        int iIntValue;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(28, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (ModemProvider.IS_MTK) {
            Object[] objArr = new Object[1];
            if (atCmdRunSync(i, new String[]{"AT+ESBP=8,\"SBP_NL1_ENABLE_NR_UL_INTRA_CCA\"", "+ESBP"}, objArr)) {
                String[] strArr = (String[]) objArr[0];
                String str = strArr[0];
                if (str == null || str.length() <= 0) {
                    iIntValue = 0;
                } else {
                    try {
                        iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue() - 2;
                    } catch (NumberFormatException unused) {
                        return false;
                    }
                }
                FiveGManagerBase.log("isUlCaEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
            } else {
                iIntValue = 0;
            }
            if (iIntValue >= 1) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUlCaEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_QCOM) {
            setModemFeature(28, i, i2);
        } else if (ModemProvider.IS_MTK) {
            enableUlCa(i2, i == 1);
        }
        Context context = FiveGManagerBase.mContext;
        if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putInt("ulca_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("setUlCaEnabled, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isSulEnabled(int i) {
        int iIntValue;
        if (ModemProvider.IS_MTK) {
            Object[] objArr = new Object[1];
            if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_SUL\"", "+ESBP"}, objArr)) {
                String[] strArr = (String[]) objArr[0];
                String str = strArr[0];
                if (str == null || str.length() <= 0) {
                    iIntValue = 0;
                } else {
                    try {
                        iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                    } catch (NumberFormatException unused) {
                        return false;
                    }
                }
                FiveGManagerBase.log("isSulEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
            } else {
                iIntValue = 0;
            }
            return iIntValue >= 1;
        }
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(27, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        return super.isSulEnabled(i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setSulEnabled(int i, int i2) {
        if (ModemProvider.IS_MTK) {
            enableSul(i2, i == 1);
        } else if (ModemProvider.IS_QCOM) {
            setModemFeature(27, i, i2);
        }
        Context context = FiveGManagerBase.mContext;
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putInt("sul_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("setSulEnabled, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isFullPowerModeEnabled(int i) {
        int iIntValue;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(24, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (!ModemProvider.IS_MTK) {
            return super.isFullPowerModeEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_FULL_TX_PWR\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 0;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isFullPowerModeEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 0;
        }
        return iIntValue >= 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setFullPowerModeEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_QCOM) {
            setModemFeature(24, i, i2);
        } else if (!ModemProvider.IS_MTK) {
            return;
        } else {
            enableFullPowerMode(i2, i == 1);
        }
        Context context = FiveGManagerBase.mContext;
        if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putInt("full_power_mode", i);
        editorEdit.apply();
        FiveGManagerBase.log("setFullPowerModeEnabled, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isWUSEnabled(int i) {
        int iIntValue;
        if (!ModemProvider.IS_MTK) {
            return super.isWUSEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_R16_WUS\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 0;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isWUSEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 0;
        }
        return iIntValue >= 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserWUSEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_MTK) {
            enableWUS(i2, i == 1);
            Context context = FiveGManagerBase.mContext;
            if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
                return;
            }
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            editorEdit.putInt("wus_enable", i);
            editorEdit.apply();
            FiveGManagerBase.log("setUserWUSEnabled, slotId:" + i2 + ",mode:" + i);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isRRCSegmentationEnabled(int i) {
        int iIntValue;
        if (!ModemProvider.IS_MTK) {
            return super.isRRCSegmentationEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_REL16_UL_DEDIC_MSG_SEGMENTATION\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 0;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isRRCSegmentationEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 0;
        }
        return iIntValue >= 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserRRCSegmentationEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_MTK) {
            enableRRCSegmentation(i2, i == 1);
            Context context = FiveGManagerBase.mContext;
            if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
                return;
            }
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            editorEdit.putInt("rrc_segmentation_enable", i);
            editorEdit.apply();
            FiveGManagerBase.log("setUserRRCSegmentationEnabled, slotId:" + i2 + ",mode:" + i);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isRRCInactiveEnabled(int i) {
        int iIntValue;
        if (!ModemProvider.IS_MTK) {
            return super.isRRCInactiveEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_DISABLE_NR_RRC_INACTIVE_STATUS\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 1;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isRRCInactiveEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 1;
        }
        return iIntValue <= 0;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserRRCInactiveEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_MTK) {
            enableRRCInactive(i2, i != 0);
            Context context = FiveGManagerBase.mContext;
            if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
                return;
            }
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            editorEdit.putInt("rrc_inactive_enable", i);
            editorEdit.apply();
            FiveGManagerBase.log("setUserRRCInactiveEnabled, slotId:" + i2 + ",mode:" + i);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isBWPSwitchEnabled(int i) {
        int iIntValue;
        if (!ModemProvider.IS_MTK) {
            return super.isBWPSwitchEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_BWP_SWITCH\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 0;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isBWPSwitchEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 0;
        }
        return iIntValue >= 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setUserBWPSwitchEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_QCOM) {
            return;
        }
        enableBWPSwitch(i2, i == 1);
        Context context = FiveGManagerBase.mContext;
        if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putInt("bwp_switch_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("setUserBWPSwitchEnabled, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isConditionalHandoverEnabled(int i) {
        int iIntValue;
        if (ModemProvider.IS_QCOM) {
            byte[] platformModemFeatureStatus = getPlatformModemFeatureStatus(25, 11, i);
            return platformModemFeatureStatus != null && MiuiPhoneUtils.bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length) == 1;
        }
        if (!ModemProvider.IS_MTK) {
            return super.isConditionalHandoverEnabled(i);
        }
        Object[] objArr = new Object[1];
        if (atCmdRunSync(i, new String[]{"AT+ESBP=7,\"SBP_NR_CHO_EXECUTION\"", "+ESBP"}, objArr)) {
            String[] strArr = (String[]) objArr[0];
            String str = strArr[0];
            if (str == null || str.length() <= 0) {
                iIntValue = 0;
            } else {
                try {
                    iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                } catch (NumberFormatException unused) {
                    return false;
                }
            }
            FiveGManagerBase.log("isConditionalHandoverEnabled, slotId:" + i + ",mode:" + iIntValue + ",result:" + strArr[0]);
        } else {
            iIntValue = 0;
        }
        return iIntValue >= 1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setConditionalHandoverEnabled(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        if (ModemProvider.IS_QCOM) {
            setModemFeature(25, i, i2);
        } else if (!ModemProvider.IS_MTK) {
            return;
        } else {
            enableCHO(i2, i == 1);
        }
        Context context = FiveGManagerBase.mContext;
        if (context == null || (defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putInt("conditional_handover_enable", i);
        editorEdit.apply();
        FiveGManagerBase.log("CONDITIONAL_HANDOVER_ENABLE, slotId:" + i2 + ",mode:" + i);
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setModemFeature(int i, int i2, int i3) {
        RilHook rilHook;
        if (MiuiPhoneUtils.isValidatePhoneId(i3)) {
            FiveGManagerBase.log("setModemFeature featureType: " + i + ", mode: " + i2 + ", slotId: " + i3);
            if (ModemProvider.IS_QCOM) {
                if (ModemUtils.getInstance() != null) {
                    ModemUtils.getInstance().featureSetCmn(i, i2, i3);
                    return;
                }
                return;
            }
            if (i != -1) {
                if (i == 0) {
                    querySupportedBandsStatus(i3);
                } else if (i == 2) {
                    enableBWP(i3, i2 == 1);
                } else if (i != 4) {
                    if (i == 20) {
                        enable5GA(i3, i2 == 1);
                    } else if (i == 12) {
                        enableCRBT(i3, i2 == 1);
                    } else if (i == 13) {
                        enableDRDSDA(i3, i2 == 1);
                    }
                } else {
                    if (!MiuiPhoneUtils.isValidatePhoneId(i3)) {
                        FiveGManagerBase.log("invalid slotId id, return.");
                        return;
                    }
                    boolean z = i2 == 1;
                    FiveGManagerBase.log("enable VONR : " + z + ", slotId : " + i3);
                    if (ModemProvider.IS_SPRD) {
                        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i3);
                        if (!SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                            FiveGManagerBase.log("setModemFeature SPRD VONR invalid subId, return.");
                            return;
                        }
                        PhoneGlobals.getInstance().phoneMgr.setVoNrEnabled(subscriptionIdForSlot, z);
                        FiveGManagerBase.log("setModemFeature SPRD VONR setVoNrEnabled subId=" + subscriptionIdForSlot + ", enabled=" + z);
                    } else {
                        ImsManager imsManager = ImsManager.getInstance(FiveGManagerBase.mContext, i3);
                        if (imsManager == null) {
                            FiveGManagerBase.log("ImsManage is null, return.");
                            return;
                        }
                        imsManager.setVonrSetting(z);
                    }
                }
            } else {
                if (mEnableN5AndN8Cmd[i3].isEmpty() || mDisableN5AndN8Cmd[i3].isEmpty()) {
                    return;
                }
                StringBuilder sb = new StringBuilder("AT");
                sb.append(AT_CMD_BANDS);
                sb.append("=,,,");
                sb.append(i2 == 1 ? mEnableN5AndN8Cmd[i3] : mDisableN5AndN8Cmd[i3]);
                sendAtCommand(i3, new String[]{sb.toString(), ""}, (Message) null);
                FiveGManagerBase.log("onSetN5AndN8AtCommand, mode: " + i2);
                updateSupportBands(i3);
            }
            if (i < 1000 || i >= 1200 || !this.mMiRilHookReady || (rilHook = mMiRilHook) == null) {
                return;
            }
            rilHook.onFeatureCtlCmn(i, i2, i3);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public byte[] getPlatformModemFeatureStatus(int i, int i2, int i3) {
        RilHook rilHook;
        if (!MiuiPhoneUtils.isValidatePhoneId(i3)) {
            return null;
        }
        FiveGManagerBase.log("getPlatformModemFeatureStatus featureType: " + i + ", mode: " + i2 + ", slotId: " + i3);
        if (ModemProvider.IS_QCOM) {
            if (ModemUtils.getInstance() != null) {
                return ModemUtils.getInstance().featureQueryCmn(i, i2, i3);
            }
        } else if (i >= 1000 && i < 1200 && this.mMiRilHookReady && (rilHook = mMiRilHook) != null) {
            return rilHook.onFeatureQueryCmn(i, i2, i3);
        }
        return null;
    }

    public ProvisioningManager getProvisionManager(int i) {
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        if (!SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
            FiveGManagerBase.log("getProvisionManager invalid subid");
            return null;
        }
        try {
            return ProvisioningManager.createForSubscriptionId(subscriptionIdForSlot);
        } catch (IllegalArgumentException unused) {
            FiveGManagerBase.log("ProvisioningManager is not available");
            return null;
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public synchronized void refreshMbn(int i) {
        if (!ModemProvider.IS_QCOM) {
            super.refreshMbn(i);
            return;
        }
        FiveGManagerBase.log("refreshMbn slotId : " + i);
        if (ModemUtils.getInstance() != null) {
            ModemUtils.getInstance().callQcrilHookAction("qcRilRefreshMbn", i);
        }
    }

    private boolean getVonrSetting(int i) {
        if (!FiveGUtils.isVonrSettingSupportedForMtk()) {
            FiveGManagerBase.log("getVonrSetting vonr setting not support.");
            return true;
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            FiveGManagerBase.log("getVonrSetting invalid slotId id, return.");
            return false;
        }
        ImsManager imsManager = ImsManager.getInstance(FiveGManagerBase.mContext, i);
        if (imsManager == null) {
            FiveGManagerBase.log("getVonrSetting ImsManage is null, return.");
            return false;
        }
        return imsManager.isVonrEnabledByUser();
    }

    @Override // com.android.phone.FiveGManagerBase
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).dumpModemInfo(fileDescriptor, printWriter, strArr);
    }

    private void updateSupportBands(int i) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(21, new FiveGManagerBase.ModemFeatureStatus(i, 0, -1)), 3000L);
    }

    @Override // com.android.phone.FiveGManagerBase
    public void sendAtCommand(int i, String[] strArr, Message message) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null) {
            phone.invokeOemRilRequestStrings(strArr, message);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void sendAtCommand(Phone phone, String[] strArr, Message message) {
        if (phone != null) {
            phone.invokeOemRilRequestStrings(strArr, message);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public void setSleepModeState(int i, int i2) {
        FiveGManagerBase.log("setSleepModeState slotId=" + i + ", sleepMode=" + i2);
        HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
        FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
        Objects.requireNonNull(fiveGHandler);
        hyperRadioManager.setSleepModeState(i, i2, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.3
            public void onResponse(int i3) {
                FiveGManagerBase.log("setSleepModeState response errorCode=" + i3);
            }
        });
    }

    private void enableBWP(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_BWP_SWITCH\",1" : "AT+ESBP=5,\"SBP_NR_BWP_SWITCH\",0", ""}, this.mHandler.obtainMessage(25, i == 0 ? 0 : 1, 0));
        } else if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            hyperRadioManager.enableBWP(i, z, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.4
                public void onResponse(int i2) {
                    FiveGManagerBase.log("enableBWP errorCode: " + i2);
                }
            });
        }
    }

    private void enableNRCA(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            sendAtCommand(i, new String[]{(z ? "AT+ESBP=5,\"SBP_NR_CA\",1" : "AT+ESBP=5,\"SBP_NR_CA\",0").concat(",1"), ""}, this.mHandler.obtainMessage(24, i == 0 ? 0 : 1, 0));
        } else if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            hyperRadioManager.enableNRCA(i, z, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.5
                public void onResponse(int i2) {
                    FiveGManagerBase.log("enableNRCA errorCode: " + i2);
                }
            });
        }
    }

    private void enable5GA(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            if (fiveGHandler == null) {
                return;
            }
            String str = Build.IS_INTERNATIONAL_BUILD ? "AT+ESBP=6,\"SBP_NR_CA_MAX_CC_NUM\",2" : "AT+ESBP=6,\"SBP_NR_CA_MAX_CC_NUM\",1";
            if (z) {
                str = "AT+ESBP=6,\"SBP_NR_CA_MAX_CC_NUM\",3";
            }
            sendAtCommand(i, new String[]{str, ""}, fiveGHandler.obtainMessage(28, i == 0 ? 0 : 1, 0));
            return;
        }
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler2 = this.mHandler;
            Objects.requireNonNull(fiveGHandler2);
            hyperRadioManager.enable5GA(i, z, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler2), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.6
                public void onResponse(int i2) {
                    FiveGManagerBase.log("enable5GA errorCode: " + i2);
                }
            });
        }
    }

    private void enable5GA4CC(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=6,\"SBP_NR_CA_MAX_CC_NUM\",4" : "AT+ESBP=6,\"SBP_NR_CA_MAX_CC_NUM\",3", ""}, fiveGHandler.obtainMessage(30, i == 0 ? 0 : 1, 0));
    }

    private void enableUlCa(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=6,\"SBP_NL1_ENABLE_NR_UL_INTRA_CCA\",3" : "AT+ESBP=6,\"SBP_NL1_ENABLE_NR_UL_INTRA_CCA\",0", ""}, fiveGHandler.obtainMessage(31, i == 0 ? 0 : 1, 0));
    }

    private void enableSul(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_SUL\",1" : "AT+ESBP=5,\"SBP_NR_SUL\",0", ""}, fiveGHandler.obtainMessage(32, i == 0 ? 0 : 1, 0));
    }

    private void enableFullPowerMode(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_FULL_TX_PWR\",1" : "AT+ESBP=5,\"SBP_NR_FULL_TX_PWR\",0", ""}, fiveGHandler.obtainMessage(33, i == 0 ? 0 : 1, 0));
    }

    private void enableWUS(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_R16_WUS\",1" : "AT+ESBP=5,\"SBP_R16_WUS\",0", ""}, fiveGHandler.obtainMessage(34, i == 0 ? 0 : 1, 0));
    }

    private void enableRRCSegmentation(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_REL16_UL_DEDIC_MSG_SEGMENTATION\",1" : "AT+ESBP=5,\"SBP_NR_REL16_UL_DEDIC_MSG_SEGMENTATION\",0", ""}, fiveGHandler.obtainMessage(35, i == 0 ? 0 : 1, 0));
    }

    private void enableRRCInactive(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_DISABLE_NR_RRC_INACTIVE_STATUS\",0" : "AT+ESBP=5,\"SBP_DISABLE_NR_RRC_INACTIVE_STATUS\",1", ""}, fiveGHandler.obtainMessage(36, i == 0 ? 0 : 1, 0));
    }

    private void enableBWPSwitch(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            if (fiveGHandler == null) {
                return;
            }
            sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_BWP_SWITCH\",1" : "AT+ESBP=5,\"SBP_NR_BWP_SWITCH\",0", ""}, fiveGHandler.obtainMessage(37, i == 0 ? 0 : 1, 0));
            return;
        }
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler2 = this.mHandler;
            Objects.requireNonNull(fiveGHandler2);
            hyperRadioManager.enableBWP(i, z, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler2), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.7
                public void onResponse(int i2) {
                    FiveGManagerBase.log("enableBWP errorCode: " + i2);
                }
            });
        }
    }

    private void enableCHO(int i, boolean z) {
        FiveGManagerBase.FiveGHandler fiveGHandler;
        if (!ModemProvider.IS_MTK || (fiveGHandler = this.mHandler) == null) {
            return;
        }
        sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_NR_CHO_EXECUTION\",1" : "AT+ESBP=5,\"SBP_NR_CHO_EXECUTION\",0", ""}, fiveGHandler.obtainMessage(38, i == 0 ? 0 : 1, 0));
    }

    private void enableDRDSDA(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            sendAtCommand(i, new String[]{z ? "AT+ESBP=5,\"SBP_LL_DRDSDA_ALLOW\",1" : "AT+ESBP=5,\"SBP_LL_DRDSDA_ALLOW\",0", ""}, (Message) null);
            FiveGManagerBase.log("enableDRDSDA enable = " + z);
        }
    }

    private void enableCRBT(int i, boolean z) {
        if (ModemProvider.IS_MTK) {
            sendAtCommand(i, new String[]{z ? "AT+ECFGSET=\"support_video_early_media\",\"3\"" : "AT+ECFGSET=\"support_video_early_media\",\"0\"", ""}, (Message) null);
            sendAtCommand(i, new String[]{"AT+EGMC=1,\"NR_CAP_UPDATE\"", ""}, (Message) null);
            FiveGManagerBase.log("enableCRBT enable = " + z);
            return;
        }
        if (ModemProvider.IS_SPRD) {
            ATCommandUtils aTCommandUtils = ATCommandUtils.getInstance();
            if (aTCommandUtils != null) {
                String[] strArr = new String[3];
                int iSendAtCmdSync = aTCommandUtils.sendAtCmdSync("AT+SPACTCARD=" + i, strArr, i);
                if (iSendAtCmdSync < 0) {
                    FiveGManagerBase.log("enableCRBT(SPRD) SPACTCARD failed, ret=" + iSendAtCmdSync);
                    return;
                } else {
                    aTCommandUtils.sendAtCmdSync(z ? "AT+SPVOLTEENG=51,1,\"1\"" : "AT+SPVOLTEENG=51,1,\"0\"", strArr, i);
                    FiveGManagerBase.log("enableCRBT(SPRD) enable = " + z);
                    return;
                }
            }
            return;
        }
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
            FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
            Objects.requireNonNull(fiveGHandler);
            hyperRadioManager.enableCRBT(i, z, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.8
                public void onResponse(int i2) {
                    FiveGManagerBase.log("enableCRBT errorCode: " + i2);
                }
            });
        }
    }

    private void enableNRCAIfNeed(int i) {
        if (getFiveGCarrierAggregationStatus(i)) {
            return;
        }
        FiveGManagerBase.logi("user disable NRCA in setting, send command");
        enableNRCA(i, false);
    }

    private void modemFeatureSetNrBands(int i, int i2, int i3, String str, String str2) {
        int i4;
        StringBuilder sb = new StringBuilder(str);
        try {
            FiveGManagerBase.log("modemFeatureSetNrBands type:" + i3 + " mode:" + i2 + " slot:" + i);
            int i5 = 6;
            if (i3 == 5) {
                i4 = 1;
            } else {
                i4 = 8;
                if (i3 != 6) {
                    if (i3 == 1) {
                        i5 = 1;
                    } else {
                        i5 = 7;
                        if (i3 == 8) {
                            i4 = 4;
                        } else if (i3 != 3) {
                            return;
                        } else {
                            i4 = 1;
                        }
                    }
                }
            }
            int i6 = i5 + 1;
            int i7 = Integer.parseInt(sb.substring(i5, i6), 16);
            sb.replace(i5, i6, Integer.toHexString(i2 == 1 ? i7 | i4 : (~i4) & i7).toUpperCase());
            if (TextUtils.equals(str, sb)) {
                return;
            }
            sendBandAtCmd(sb, str2, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void safeRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean simOperatorMatched(String str, String str2) {
        if (str2 == null || str == null) {
            return false;
        }
        return str.contains(str2) || "All".equalsIgnoreCase(str);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean sendAtCmdSync(int i, String[] strArr, Object[] objArr) {
        return atCmdRunSync(i, strArr, objArr);
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean atCmdRunSync(int i, String[] strArr, Object[] objArr) {
        return ATCommandUtils.getInstance().atCmdRunSync(i, strArr, objArr);
    }

    public void setBandModemFeature(int i, int i2, int i3) {
        String[] strArrSplit;
        FiveGManagerBase.log("setBandModemFeature type:" + i + " mode:" + i2 + " slot:" + i3);
        if (i == 8 || i == 5 || i == 6 || i == 1 || i == 3) {
            if (ModemProvider.IS_XRING) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(FiveGManagerBase.mContext);
                FiveGManagerBase.FiveGHandler fiveGHandler = this.mHandler;
                Objects.requireNonNull(fiveGHandler);
                hyperRadioManager.setModemBands(i, i2, i3, new FiveGManager$$ExternalSyntheticLambda0(fiveGHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.FiveGManager.9
                    public void onResponse(int i4) {
                        FiveGManagerBase.log("setBandModemFeature errorCode: " + i4);
                    }
                });
            }
            if (ModemProvider.IS_MTK) {
                Object[] objArr = new Object[1];
                if (atCmdRunSync(i3, new String[]{"AT" + AT_CMD_BANDS + "?", AT_CMD_BANDS}, objArr)) {
                    String[] strArr = (String[]) objArr[0];
                    if (strArr == null || strArr.length < 1) {
                        FiveGManagerBase.log("queryResult is empty,Query NR band failed.");
                        return;
                    }
                    FiveGManagerBase.log(i3 + " setBandModemFeature, query result = " + strArr[0]);
                    String str = strArr[0];
                    if (str == null || str.length() <= 0 || (strArrSplit = strArr[0].split(",")) == null || strArrSplit.length <= 3) {
                        return;
                    }
                    StringBuilder sb = new StringBuilder(strArrSplit[3]);
                    sb.delete(sb.length() - 1, sb.length());
                    sb.delete(0, 1);
                    modemFeatureSetNrBands(i3, i2, i, sb.toString(), strArrSplit.length > 4 ? strArrSplit[4] : strArrSplit[3]);
                }
            }
        }
    }

    private void setModemFeatureOnCloud(int i, String str, List list) {
        boolean z = false;
        if (list == null || list.size() == 0) {
            FiveGManagerBase.log("fetchModemFeatures cloudList is empty");
        } else if (MiuiPhoneUtils.isMtkCloudModemFeatureSupported()) {
            Iterator it = list.iterator();
            boolean z2 = false;
            while (it.hasNext()) {
                MiuiSettings.SettingsCloudData.CloudData cloudData = (MiuiSettings.SettingsCloudData.CloudData) it.next();
                FiveGManagerBase.log("fetchAllModemFeatures data=" + cloudData.toString());
                String string = cloudData.getString("Model", "NA");
                String string2 = cloudData.getString("SimOperator", "NA");
                if (FiveGCloudUtils.modelMatched(string)) {
                    int i2 = cloudData.getInt("ModemFeatureType", -1);
                    if (i2 != 1 && i2 != 3 && i2 != 5 && i2 != 6) {
                        if (i2 == 7) {
                            int i3 = cloudData.getInt("ModemFeatureValue", -1);
                            if (i3 >= 0 && simOperatorMatched(string2, str)) {
                                int subId = MiuiPhoneUtils.getPhone(i).getSubId();
                                PreferenceUtils.getInstance().setIntPreference("CloudNrcaStatus" + subId, i3);
                                enableNRCA(i, i3 == 1);
                                FiveGManagerBase.log("setModemFeatureOnCloud enable NRCA, featureValue = " + i3 + " phoneId = " + i);
                            }
                        } else if (i2 != 8) {
                        }
                    }
                    if (str == null) {
                        FiveGManagerBase.log("setModemFeatureOnCloud reset to ON:" + i2);
                        setBandModemFeature(i2, 1, i);
                    } else if (simOperatorMatched(string2, str)) {
                        setBandModemFeature(i2, cloudData.getInt("ModemFeatureValue", -1), i);
                    }
                    z2 = true;
                }
            }
            z = z2;
        }
        if (!MiuiPhoneUtils.isMtkDefaultDisableN8AndN28Supported() || z) {
            return;
        }
        FiveGManagerBase.log("setModemFeatureOnCloud on:" + str + " hasMdCloud:" + z);
    }

    @Override // com.android.phone.FiveGManagerBase
    protected boolean setBandModeOnDefault(int i, String str) {
        String[] strArrSplit;
        int i2;
        int i3;
        String[] strArr = {"46003", "46005", "46011"};
        String[] strArr2 = {"46000", "46002", "46004", "46007", "46008", "46013", "46020", "46015"};
        String[] strArr3 = {"46001", "46006", "46009"};
        FiveGManagerBase.log(i + " setBandModeOnDefault: simOperatorNumeric = " + str);
        Object[] objArr = new Object[1];
        try {
            if (!atCmdRunSync(i, new String[]{"AT" + AT_CMD_BANDS + "?", AT_CMD_BANDS}, objArr)) {
                return false;
            }
            String[] strArr4 = (String[]) objArr[0];
            if (strArr4 == null || strArr4.length < 1) {
                FiveGManagerBase.log("setBandModeOnDefault: queryResult is empty,Query NR band failed.");
                return false;
            }
            FiveGManagerBase.log(i + "setBandModeOnDefault: query result = " + strArr4[0]);
            String str2 = strArr4[0];
            if (str2 != null && str2.length() > 0 && (strArrSplit = strArr4[0].split(",")) != null && strArrSplit.length > 3) {
                StringBuilder sb = new StringBuilder(strArrSplit[3]);
                sb.delete(sb.length() - 1, sb.length());
                sb.delete(0, 1);
                String str3 = strArrSplit.length > 4 ? strArrSplit[4] : strArrSplit[3];
                String string = sb.toString();
                int i4 = Integer.parseInt(sb.substring(6, 7), 16);
                int i5 = Integer.parseInt(sb.substring(1, 2), 16);
                if (ArrayUtils.contains(strArr2, str)) {
                    i3 = i5 | 8;
                    i2 = i4 & (-9);
                } else if (ArrayUtils.contains(strArr, str) || ArrayUtils.contains(strArr3, str)) {
                    i2 = i4 | 8;
                    i3 = i5 & (-9);
                } else {
                    i2 = i4 | 8;
                    i3 = i5 | 8;
                }
                sb.replace(6, 7, Integer.toHexString(i2).toUpperCase());
                sb.replace(1, 2, Integer.toHexString(i3).toUpperCase());
                if (!TextUtils.equals(string, sb) && !sendBandAtCmd(sb, str3, i)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public synchronized void setModemFeatureOnSecretCode(int i, int i2, int i3) {
        try {
            Phone phone = MiuiPhoneUtils.getPhone(i3);
            if (phone == null) {
                return;
            }
            String operatorNumeric = phone.getOperatorNumeric();
            FiveGManagerBase.log(i3 + " setModemFeatureOnSecretCode: simOperator = " + operatorNumeric);
            if (i == 1) {
                if (i2 == 0) {
                    setBandModeOnDefault(i3, operatorNumeric);
                } else {
                    setBandModemFeature(1, i2, i3);
                }
                PreferenceUtils.getInstance().setIntPreference("n28_band", i2);
            } else if (i == 5) {
                if (i2 == 1) {
                    setBandModemFeature(5, i2, i3);
                }
                PreferenceUtils.getInstance().setIntPreference("n5_band", i2);
            } else if (i == 6) {
                if (i2 == 0) {
                    setBandModeOnDefault(i3, operatorNumeric);
                } else {
                    setBandModemFeature(6, i2, i3);
                }
                PreferenceUtils.getInstance().setIntPreference("n8_band", i2);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean sendBandAtCmd(StringBuilder sb, String str, int i) {
        if (MiuiPhoneUtils.isUseENVBSE()) {
            sb.insert(0, "AT+ENVBSE=,,,\"");
            sb.append("\" ");
        } else {
            sb.insert(0, "AT+EPBSEH=,,,\"");
            sb.append("\",1,");
            sb.append(str);
            if (ModemProvider.IS_MTK) {
                sb.append(",0");
            }
        }
        if (!atCmdRunSync(i, new String[]{sb.toString(), ""}, new Object[1])) {
            return false;
        }
        sendAtCommand(i, new String[]{"AT+EGMC=1,\"NR_CAP_UPDATE\"", ""}, (Message) null);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("ss");
        final int intExtra = intent.getIntExtra("phone", -1);
        if (intExtra < 0 || intExtra >= MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        FiveGManagerBase.log(intExtra + " onIccStateChanged " + stringExtra);
        if (TextUtils.equals(stringExtra, this.mCurretSimState[intExtra])) {
            return;
        }
        this.mCurretSimState[intExtra] = stringExtra;
        if ("LOADED".equals(stringExtra)) {
            checkIfModemFeatureCloudChanged();
            Phone phone = MiuiPhoneUtils.getPhone(intExtra);
            if (phone != null) {
                String operatorNumeric = phone.getOperatorNumeric();
                if (!TextUtils.isEmpty(operatorNumeric)) {
                    setModemFeatureOnCloud(intExtra, operatorNumeric, this.mModemFeatureList);
                    this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onSimStateChanged$2(intExtra);
                        }
                    }, 2000L);
                }
            }
            enableNRCAIfNeed(intExtra);
            sync5GAUsermodeToModem(intExtra);
            return;
        }
        if ("ABSENT".equals(stringExtra)) {
            setModemFeatureOnCloud(intExtra, null, this.mModemFeatureList);
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSimStateChanged$3(intExtra);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setBandFeatureOnSecretCode, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$onSimStateChanged$3(int i) {
        for (String str : this.bandList) {
            byte b = 0;
            int intPreference = PreferenceUtils.getInstance().getIntPreference(str, 0);
            FiveGManagerBase.log(i + " setBandFeatureOnSecretCode: " + str + " status: " + intPreference);
            if (intPreference != 0) {
                str.hashCode();
                switch (str.hashCode()) {
                    case 449252845:
                        if (!str.equals("n5_band")) {
                            b = -1;
                        }
                        break;
                    case 535140298:
                        b = str.equals("n8_band") ? (byte) 1 : (byte) -1;
                        break;
                    case 1555102816:
                        b = str.equals("n28_band") ? (byte) 2 : (byte) -1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        setBandModemFeature(5, intPreference, i);
                        break;
                    case 1:
                        setBandModemFeature(6, intPreference, i);
                        break;
                    case 2:
                        setBandModemFeature(1, intPreference, i);
                        break;
                }
            }
        }
    }

    private List checkIfModemFeatureCloudChanged() {
        List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(FiveGManagerBase.mContext.getContentResolver(), "ModemFeatures");
        if (cloudDataList != null && cloudDataList.equals(this.mModemFeatureList)) {
            FiveGManagerBase.log("setModemFeatureOnCloud SAME cloud data");
            return null;
        }
        List list = this.mModemFeatureList;
        this.mModemFeatureList = cloudDataList;
        return list;
    }

    private void updateModemFeatureCloudControl() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(FiveGManagerBase.mContext.getContentResolver(), "TelephonyModemFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    FiveGManagerBase.log("TelephonyModemFeature data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        int i = cloudData.getInt("GestureAdvancedOpenLoop", -1);
                        if (i != -1 && i != this.mAOLGestureEnable) {
                            FiveGManagerBase.log("GestureAdvancedOpenLoop:" + i);
                            Intent intent = new Intent("com.android.phone.intent.action.aol_gesture_state_changed");
                            intent.putExtra(MEModuleSignalT1s.EXTRA_STATE, i);
                            FiveGManagerBase.mContext.sendBroadcast(intent);
                            this.mAOLGestureEnable = i;
                        }
                        int i2 = cloudData.getInt("ModemFeatureStatusT1", -1);
                        if (i2 == -1 || i2 == this.mT1Enable) {
                            return;
                        }
                        FiveGManagerBase.log("ModemFeatureStatusT1:" + i2);
                        Intent intent2 = new Intent("com.android.phone.intent.action.t1_state_changed");
                        intent2.putExtra(MEModuleSignalT1s.EXTRA_STATE, i2);
                        FiveGManagerBase.mContext.sendBroadcast(intent2);
                        this.mT1Enable = i2;
                        return;
                    }
                }
                return;
            }
            FiveGManagerBase.log("TelephonyModemFeature cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean isSimCardSlotInsertedStatus(int i) {
        String str;
        String str2;
        int iIntValue;
        boolean z = false;
        if (!isSupportObtainCardSlotStatus()) {
            return false;
        }
        if (ModemProvider.IS_QCOM) {
            try {
                if (ModemUtils.getInstance() == null) {
                    FiveGManagerBase.log("isCardSlotPresent, qcril is not ready");
                    return false;
                }
                boolean zBooleanValue = ((Boolean) ModemUtils.getInstance().callHook("isCardSlotPresent", new Object[0])).booleanValue();
                FiveGManagerBase.log("cardSlotStatus =" + zBooleanValue);
                return zBooleanValue;
            } catch (Exception e) {
                FiveGManagerBase.log("isSimCardSlotInsertedStatus Exception: " + e);
                return false;
            }
        }
        if (ModemProvider.IS_MTK) {
            try {
                boolean z2 = HyperOSCustFeatureResolve.getBoolean("config_use_new_modem_sim_state_interface", false);
                if (z2) {
                    str = "AT+ESIMTRAY=1";
                    str2 = "+ESIMTRAY";
                } else {
                    str = "AT+GPIO?SIM";
                    str2 = "+GPIO";
                }
                Object[] objArr = new Object[1];
                if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{str, str2}, objArr)) {
                    String[] strArr = (String[]) objArr[0];
                    if (strArr != null && strArr.length >= 1) {
                        String str3 = strArr[0];
                        if (str3 != null && str3.length() > 0) {
                            if (z2) {
                                iIntValue = Integer.valueOf(strArr[0].split(":")[1].trim()).intValue();
                            } else {
                                iIntValue = Integer.valueOf(strArr[0].substring(6).trim()).intValue();
                            }
                            if (iIntValue == 1) {
                                z = true;
                            }
                        }
                    }
                    FiveGManagerBase.log("queryResult is empty,Query sim status failed.");
                    return false;
                }
                FiveGManagerBase.log("cardSlotStatus =" + z);
                return z;
            } catch (Exception e2) {
                FiveGManagerBase.log("isSimCardSlotInsertedStatus Exception: " + e2);
            }
        }
        return false;
    }

    private void registerSimStateReceiver() {
        if (MiuiPhoneUtils.isUseENVBSE()) {
            AT_CMD_BANDS = "+ENVBSE";
        }
        FiveGManagerBase.mContext.registerReceiver(new AnonymousClass10(), new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda3
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$registerSimStateReceiver$5(baseEvent);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.phone.FiveGManager$10, reason: invalid class name */
    class AnonymousClass10 extends BroadcastReceiver {
        AnonymousClass10() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                FiveGManager.this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.FiveGManager$10$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(intent);
                    }
                }, 1000L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            FiveGManager.this.onSimStateChanged(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerSimStateReceiver$5(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mHandler.post(new Runnable() { // from class: com.android.phone.FiveGManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$registerSimStateReceiver$4();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerSimStateReceiver$4() {
        Phone[] phones;
        updateModemFeatureCloudControl();
        List listCheckIfModemFeatureCloudChanged = checkIfModemFeatureCloudChanged();
        if (listCheckIfModemFeatureCloudChanged == null || (phones = PhoneFactory.getPhones()) == null) {
            return;
        }
        for (Phone phone : phones) {
            String operatorNumeric = phone.getOperatorNumeric();
            if (!TextUtils.isEmpty(operatorNumeric)) {
                setModemFeatureOnCloud(phone.getPhoneId(), null, listCheckIfModemFeatureCloudChanged);
                setModemFeatureOnCloud(phone.getPhoneId(), operatorNumeric, this.mModemFeatureList);
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void onHandleCarrierConfigChanged(Message message) {
        super.onHandleCarrierConfigChanged(message);
        int i = message.arg2;
        if (android.telephony.SubscriptionManager.isValidSubscriptionId(i)) {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(i);
            synchronized (this.mCarrierConfigListeners) {
                try {
                    Iterator it = this.mCarrierConfigListeners.iterator();
                    while (it.hasNext()) {
                        ((BiConsumer) it.next()).accept(Integer.valueOf(i), carrierConfigForSubId);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void registerCarrierConfigListener(BiConsumer biConsumer) {
        synchronized (this.mCarrierConfigListeners) {
            this.mCarrierConfigListeners.add(biConsumer);
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public String getEncryptedIds() {
        if (FiveGManagerBase.mInstance != null && !TextUtils.isEmpty(FiveGManagerBase.mInstance.mDumpVal)) {
            return FiveGManagerBase.mInstance.mDumpVal;
        }
        try {
            String imei = MiuiPhoneInterfaceManager.getInstance().getImei(0);
            if (TextUtils.isEmpty(imei)) {
                return null;
            }
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String strSubstring = imei.substring(1);
            Charset charset = StandardCharsets.UTF_8;
            byte[] bArrDigest = messageDigest.digest(strSubstring.getBytes(charset));
            long jElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(MessageDigest.getInstance("MD5").digest(String.valueOf(jElapsedRealtimeNanos).getBytes(charset)));
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArrDigest, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            String instanceId = Build.IS_INTERNATIONAL_BUILD ? PubSubTrackImpl.getInstanceId() : OneTrackImpl.getInstanceId();
            return jElapsedRealtimeNanos + ":" + IccUtils.bytesToHexString(cipher.doFinal((instanceId + "::" + IdentifierManager.getOAID(FiveGManagerBase.mContext)).getBytes(charset)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean shouldDisplayCrbtButton() {
        return MiuiPhoneUtils.isSupportDisableCrbt() && !FiveGCloudController.isFeatureDisabledByCloud("optimization_config_crbt_feature");
    }

    @Override // com.android.phone.FiveGManagerBase
    public void enableFiveGPowerOptimization() {
        FiveGPowerController.getInstance().enableFiveGPowerOptimization();
    }

    @Override // com.android.phone.FiveGManagerBase
    protected void update5GStateForCarrierConfigChanged(int i) {
        super.update5GStateForCarrierConfigChanged(i);
        if (i == DefaultSimManager.getDefaultDataSlotId()) {
            if (Build.IS_INTERNATIONAL_BUILD) {
                FiveGPowerController.getInstance().updateFiveGPowerSaveEnabled();
            }
            if (MiuiPhoneUtils.isOpTestVersion()) {
                FiveGManagerBase.log("customzation test disable 5G save and 5G host");
                FiveGPowerController.getInstance().updateFiveGPowerDisabled();
            }
            FiveGManagerBase.log("EVENT_CARRIER_CONFIG_CHANGED, notify carrier config changed");
            notifyDataSlotCarrierConfigChanged();
        }
    }

    public void onConnected() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            HyperRadioManager.getInstance(FiveGManagerBase.mContext).registerCallback(i, 24, this.mServiceCallbacks[i]);
        }
    }

    public void onDisconnected() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            HyperRadioManager.getInstance(FiveGManagerBase.mContext).unRegisterCallback(i, 24, this.mServiceCallbacks[i]);
        }
    }

    class ServiceCallback extends HyperRadioCallbackBase {
        private int mPhoneId;

        ServiceCallback(int i, Executor executor) {
            super(executor);
            this.mPhoneId = i;
        }

        public void onNrIconChange(int i, NrIcon nrIcon) {
            FiveGManagerBase.log("onNrIconChange, slotId = " + i + " , mPhoneId = " + this.mPhoneId + ", nrIcon = " + nrIcon);
            if (this.mPhoneId == i) {
                FiveGManager.this.mNrIcons[i] = nrIcon;
                Bundle bundle = new Bundle();
                bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
                FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
                if (fiveGServiceInfo == null) {
                    return;
                }
                fiveGServiceInfo.setNrIconType(nrIcon.getType());
                FiveGManager.this.setFiveGARegistration(nrIcon.getType() == 2);
                EventHubManager.getInstance().postEvent(new TelephonyEvent.FiveGEvent("fiveg_service_info_changed", fiveGServiceInfo, bundle));
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase
    public boolean getSixRXState(int i) {
        if (ModemProvider.IS_QCOM) {
            FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
            return fiveGServiceInfo != null && fiveGServiceInfo.getRxCount() == 6;
        }
        NrIcon nrIcon = this.mNrIcons[i];
        if (nrIcon == null || nrIcon.getRxCount() != 6) {
            return super.getSixRXState(i);
        }
        return true;
    }

    @Override // com.android.phone.FiveGManagerBase
    public int getHwId() {
        if (!ModemProvider.IS_QCOM) {
            return super.getHwId();
        }
        if (ModemUtils.getInstance() != null) {
            return ModemUtils.getInstance().callQcrilHookGet("getHwId");
        }
        return -1;
    }

    @Override // com.android.phone.FiveGManagerBase
    public void registerFiveGNetworkModeCompleted(FiveGManagerBase.FiveGNetworkModeListener fiveGNetworkModeListener) {
        FiveGManagerBase.log("register FiveG Network mode Completed.");
        this.mFiveGNetworkModeListener = fiveGNetworkModeListener;
        notifyFiveGNetworkModeCompleted();
    }

    @Override // com.android.phone.FiveGManagerBase
    public void unregisterFiveGNetworkModeCompleted() {
        FiveGManagerBase.log("unregister FiveG Network mode Completed.");
        this.mFiveGNetworkModeListener = null;
    }

    protected void notifyFiveGNetworkModeCompleted() {
        FiveGManagerBase.FiveGNetworkModeListener fiveGNetworkModeListener;
        if (!isHandle5GNetworkModeCompleted() || (fiveGNetworkModeListener = this.mFiveGNetworkModeListener) == null) {
            return;
        }
        fiveGNetworkModeListener.onFiveGNetworkModeCompleted();
    }

    @Override // com.android.phone.FiveGManagerBase
    public synchronized void registerDataSlotCarrierConfigChangedListener(FiveGManagerBase.IFiveGCarrierConfigChangedListener iFiveGCarrierConfigChangedListener) {
        FiveGManagerBase.logi("register data slot  carrier config changed listener.");
        this.mIFiveGCarrierConfigChangedListeners.add(iFiveGCarrierConfigChangedListener);
    }

    public synchronized void unregisterDataSlotCarrierConfigChangedListener(FiveGManagerBase.IFiveGCarrierConfigChangedListener iFiveGCarrierConfigChangedListener) {
        FiveGManagerBase.logi("unregister data slot  carrier config changed listener.");
        this.mIFiveGCarrierConfigChangedListeners.remove(iFiveGCarrierConfigChangedListener);
    }

    protected void notifyDataSlotCarrierConfigChanged() {
        int i;
        FiveGManagerBase.IFiveGCarrierConfigChangedListener[] iFiveGCarrierConfigChangedListenerArr;
        synchronized (this) {
            FiveGManagerBase.logi("data slot carrier config changed, mIFiveGCarrierConfigChangedListeners: " + this.mIFiveGCarrierConfigChangedListeners);
            iFiveGCarrierConfigChangedListenerArr = (FiveGManagerBase.IFiveGCarrierConfigChangedListener[]) this.mIFiveGCarrierConfigChangedListeners.toArray(new FiveGManagerBase.IFiveGCarrierConfigChangedListener[0]);
        }
        for (FiveGManagerBase.IFiveGCarrierConfigChangedListener iFiveGCarrierConfigChangedListener : iFiveGCarrierConfigChangedListenerArr) {
            iFiveGCarrierConfigChangedListener.onDataSlotCarrierConfigChanged();
        }
    }
}
