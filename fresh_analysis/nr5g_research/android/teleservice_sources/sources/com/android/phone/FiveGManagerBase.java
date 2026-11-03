package com.android.phone;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.util.ArrayUtils;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import xiaomi.hypercomm.HyperCommManager;

/* JADX INFO: loaded from: classes.dex */
public class FiveGManagerBase implements DefaultSimManager.DataSlotListener {
    protected static int CARRIER_CONFIG_STATE_CLEAR = 0;
    protected static int CARRIER_CONFIG_STATE_LOADED = 1;
    protected static int CARRIER_CONFIG_STATE_UNKNOWN = -1;
    private static final int DELAY_DATA_SLOT_READY_MSG_MS = HyperOSCustFeatureResolve.getInt("delay_data_slot_ready_msg_in_milliseconds", ResponseObject.RELAY_FAILED_CODE);
    private static String LOG_TAG = "FiveGManager";
    protected static volatile int[] mCarrierConfigState;
    protected static Context mContext;
    protected static volatile FiveGManagerBase mInstance;
    protected static volatile boolean[] sFiveGSwitchCarrierAvailable;
    protected static volatile boolean[] sFiveGSwitchCarrierDisabled;
    public static volatile boolean sIsSaModeCarrierDisabled;
    private static volatile HashMap sLocalLogs;
    protected static volatile boolean[] sSaModeCarrierAvailable;
    protected static volatile boolean[] sSaModeCarrierDisabled;
    private int mDefaultDataSlotId;
    String mDumpVal;
    private volatile boolean[] mFactoryResetState;
    protected FiveGHandler mHandler;
    protected boolean[] mNsaStatus;
    protected boolean[] mSaStatus;
    protected HandlerThread mWorkThread;

    public interface FiveGNetworkModeListener {
        void onFiveGNetworkModeCompleted();
    }

    public interface IFiveGCarrierConfigChangedListener {
        void onDataSlotCarrierConfigChanged();
    }

    public boolean atCmdRunSync(int i, String[] strArr, Object[] objArr) {
        return false;
    }

    protected void bindFivegService() {
    }

    public void disableDSDA() {
    }

    public void enableDSDA() {
    }

    public void enableFiveGPowerOptimization() {
    }

    public void execSetFiveGCarrierAggregationMode(int i, int i2) {
    }

    protected void execSetUserFiveGNetworkMode(int i, int i2, boolean z) {
    }

    protected void fetch5GIconDisplayRule(int i) {
    }

    public int get3GPPVersion(int i) {
        return 0;
    }

    public int get5GIndicatorStatus(int i) {
        return -1;
    }

    public String getEncryptedIds() {
        return null;
    }

    public boolean getFiveGCarrierAggregationStatus(int i) {
        return false;
    }

    public int getHwId() {
        return -1;
    }

    public int getNrConfigType(int i) {
        return -1;
    }

    public int getRestrictDcnrStatus(int i) {
        return 0;
    }

    public boolean getSixRXState(int i) {
        return false;
    }

    public int getUpperlayerStatus(int i) {
        return 0;
    }

    public int getXMNetworkType(int i) {
        return 0;
    }

    public boolean isBWPSwitchEnabled(int i) {
        return false;
    }

    public boolean isConditionalHandoverEnabled(int i) {
        return false;
    }

    public boolean isDataChannelEnable(int i) {
        return false;
    }

    public boolean isFiveGA4CCEnabled(int i) {
        return false;
    }

    public boolean isFiveGA4CCRegistration() {
        return false;
    }

    public boolean isFiveGAEnabled(int i) {
        return false;
    }

    public boolean isFiveGARegistration() {
        return false;
    }

    public boolean isFullPowerModeEnabled(int i) {
        return false;
    }

    public boolean isHOMEnabled(int i) {
        return false;
    }

    public boolean isHandle5GNetworkModeCompleted() {
        return true;
    }

    public boolean isR16PowerEnabled(int i) {
        return false;
    }

    public boolean isRRCInactiveEnabled(int i) {
        return false;
    }

    public boolean isRRCSegmentationEnabled(int i) {
        return false;
    }

    public boolean isSimCardSlotInsertedStatus(int i) {
        return false;
    }

    public boolean isSulEnabled(int i) {
        return false;
    }

    public boolean isSwulEnabled(int i) {
        return false;
    }

    public boolean isUlCaEnabled(int i) {
        return false;
    }

    public boolean isWUSEnabled(int i) {
        return false;
    }

    protected void onEnableFiveGUserAsync(Message message) {
    }

    protected void onHandleRadioPowerOff(Message message) {
    }

    protected void onQueryFiveGCommandDone(Message message, int i) {
    }

    protected void onQuerySupportedBandsDone(Message message, int i) {
    }

    protected void onSetDSDACommandDisabledDone(Message message) {
    }

    protected void onSetDSDACommandEnabledDone(Message message) {
    }

    protected void onSetFiveGCommandDone(Message message, int i) {
    }

    protected void onSetModemFeatureCommandDone(Message message, int i) {
    }

    protected void onSetPreferredNetworkModeDone(Message message) {
    }

    protected void preHandleEnableFiveGUser(boolean z) {
    }

    public void registerFiveGNetworkModeCompleted(FiveGNetworkModeListener fiveGNetworkModeListener) {
    }

    public boolean sendAtCmdSync(int i, String[] strArr, Object[] objArr) {
        return false;
    }

    public void set3GPPVersion(int i, int i2) {
    }

    protected boolean setBandModeOnDefault(int i, String str) {
        return true;
    }

    public void setConditionalHandoverEnabled(int i, int i2) {
    }

    public void setDataChannelEnable(int i, boolean z) {
    }

    public void setFiveGARegistration(boolean z) {
    }

    public void setFullPowerModeEnabled(int i, int i2) {
    }

    public void setSleepModeState(int i, int i2) {
    }

    public void setSulEnabled(int i, int i2) {
    }

    public void setUlCaEnabled(int i, int i2) {
    }

    public void setUserBWPSwitchEnabled(int i, int i2) {
    }

    public void setUserFiveGA4CCEnabled(int i, int i2) {
    }

    public void setUserFiveGAEnabled(int i, int i2) {
    }

    public void setUserHOMEnabled(int i, int i2) {
    }

    public void setUserNetSlicingEnabled(int i, int i2) {
    }

    public void setUserR16PowerEnabled(int i, int i2) {
    }

    public void setUserRRCInactiveEnabled(int i, int i2) {
    }

    public void setUserRRCSegmentationEnabled(int i, int i2) {
    }

    public void setUserSwulEnabled(int i, int i2) {
    }

    public void setUserWUSEnabled(int i, int i2) {
    }

    public boolean shouldDisplayCrbtButton() {
        return false;
    }

    protected void unbindFivegService() {
    }

    public void unregisterFiveGNetworkModeCompleted() {
    }

    public class SwitchStatus {
        boolean enabled;
        int networkType;
        int slotId;

        public SwitchStatus(FiveGManagerBase fiveGManagerBase, int i, boolean z, int i2) {
            this.slotId = i;
            this.enabled = z;
            this.networkType = i2;
        }
    }

    public class ModemFeatureStatus {
        int featureMode;
        int featureType;
        int slotId;

        public ModemFeatureStatus(int i, int i2, int i3) {
            this.slotId = i;
            this.featureType = i2;
            this.featureMode = i3;
        }

        public String toString() {
            return "ModemFeatureStatus{slotId=" + this.slotId + ", featureType=" + this.featureType + ", featureMode=" + this.featureMode + '}';
        }
    }

    static {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        mCarrierConfigState = new int[i];
        sFiveGSwitchCarrierAvailable = new boolean[i];
        sFiveGSwitchCarrierDisabled = new boolean[i];
        sSaModeCarrierAvailable = new boolean[i];
        sSaModeCarrierDisabled = new boolean[i];
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            mCarrierConfigState[i2] = CARRIER_CONFIG_STATE_UNKNOWN;
            sFiveGSwitchCarrierAvailable[i2] = !MiuiPhoneUtils.shouldDisable5GByDefault();
            sFiveGSwitchCarrierDisabled[i2] = MiuiPhoneUtils.shouldDisable5GSwitchByDefault();
            sSaModeCarrierAvailable[i2] = true;
            sSaModeCarrierDisabled[i2] = MiuiPhoneUtils.shouldDisableSaSwitchByDefault();
        }
    }

    private static HashMap getLocalLogs() {
        if (sLocalLogs == null) {
            synchronized (FiveGManagerBase.class) {
                try {
                    if (sLocalLogs == null) {
                        sLocalLogs = new HashMap();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sLocalLogs;
    }

    public static FiveGManagerBase getInstance() {
        if (mInstance == null) {
            synchronized (FiveGManagerBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new FiveGManagerBase("");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public static void safeRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FiveGManagerBase(String str) {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mNsaStatus = new boolean[i];
        this.mSaStatus = new boolean[i];
        this.mFactoryResetState = new boolean[i];
        logi("FiveGManagerBase...");
        mContext = PhoneGlobals.getInstance();
    }

    protected FiveGManagerBase() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mNsaStatus = new boolean[i];
        this.mSaStatus = new boolean[i];
        this.mFactoryResetState = new boolean[i];
        mContext = PhoneGlobals.getInstance();
        this.mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        HandlerThread handlerThread = new HandlerThread("FiveGThread");
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new FiveGHandler(this.mWorkThread.getLooper());
        logi("FiveGManagerBase inited!");
    }

    public void init() {
        logi("FiveGManagerBase init!");
        DefaultSimManager.getInstance().addDataSlotListener(this);
    }

    public class FiveGHandler extends Handler {
        public FiveGHandler(Looper looper) {
            super(looper);
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
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (!MiuiPhoneUtils.isFiveGCapable()) {
                FiveGManagerBase.logi("FiveGHandler handleMessage what=" + message.what + ", no 5G capable, return.");
                return;
            }
            FiveGManagerBase.logi("FiveGHandler handleMessage what=" + message.what);
            switch (message.what) {
                case 0:
                    FiveGManagerBase.this.onInitFiveGData();
                    return;
                case 1:
                    FiveGManagerBase.this.bindFivegService();
                    return;
                case 2:
                    FiveGManagerBase.this.unbindFivegService();
                    return;
                case 3:
                    FiveGManagerBase.this.onEnableFiveGUserAsync(message);
                    return;
                case 4:
                    FiveGManagerBase.this.onHandleRadioPowerOff(message);
                    return;
                case 5:
                    FiveGManagerBase.this.onSetPreferredNetworkModeDone(message);
                    return;
                case 6:
                    FiveGManagerBase.this.onHandleCarrierConfigChanged(message);
                    return;
                case 7:
                    boolean z = message.arg1 == 1;
                    FiveGManagerBase.log("EVENT_SET_USER_5G_ENABLED enabled=" + z + ", slotId = " + message.arg2);
                    FiveGManagerBase.this.onSetUserFiveGEnabled(z, message.arg2);
                    return;
                case 8:
                    break;
                case 9:
                    boolean z2 = message.arg1 == 1;
                    FiveGManagerBase.log("EVENT_SET_USER_5G_HOSTED_MODE_ENABLED enabled=" + z2);
                    FiveGManagerBase.this.onSetUserFiveGHostedModeEnabled(z2);
                    return;
                case 10:
                    FiveGManagerBase.this.onQueryFiveGCommandDone(message, 0);
                    return;
                case 11:
                    FiveGManagerBase.this.onQueryFiveGCommandDone(message, 1);
                    return;
                case 12:
                case 17:
                default:
                    return;
                case 13:
                    FiveGManagerBase.this.onSetFiveGCommandDone(message, 0);
                    return;
                case 14:
                    FiveGManagerBase.this.onSetFiveGCommandDone(message, 1);
                    return;
                case 15:
                    FiveGManagerBase.this.onSetDSDACommandEnabledDone(message);
                    return;
                case 16:
                    FiveGManagerBase.this.onSetDSDACommandDisabledDone(message);
                    return;
                case 18:
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        FiveGManagerBase.mInstance.setModemFeature(message.arg1, message.arg2 == 1 ? 1 : 0, i);
                        FiveGManagerBase.this.needRefreshMbn(i);
                    }
                    return;
                case 19:
                    FiveGManagerBase.this.refreshMbn(message.arg1);
                    sendMessage(obtainMessage(20));
                    return;
                case 20:
                    MiuiPhoneUtils.hideOOS("REFRESH_MBN");
                    return;
                case 21:
                    ModemFeatureStatus modemFeatureStatus = (ModemFeatureStatus) message.obj;
                    FiveGManagerBase.mInstance.setModemFeature(modemFeatureStatus.featureType, modemFeatureStatus.featureMode == 1 ? 1 : 0, modemFeatureStatus.slotId);
                    return;
                case 22:
                    FiveGManagerBase.this.onQuerySupportedBandsDone(message, 0);
                    return;
                case 23:
                    FiveGManagerBase.this.onQuerySupportedBandsDone(message, 1);
                    return;
                case 24:
                case 25:
                case 26:
                case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                case 31:
                case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                case 33:
                case 34:
                case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                case 36:
                case 37:
                case 38:
                    FiveGManagerBase.this.onSetModemFeatureCommandDone(message, message.arg1);
                    return;
                case 27:
                    FiveGManagerBase.this.sendVonrStateBoardcast(message.arg1, message.arg2);
                    return;
                case 29:
                    String str = (String) message.obj;
                    int i2 = message.arg1;
                    int i3 = message.arg2;
                    FiveGManagerBase.log("EVENT_SET_BAND_RETRY slot = " + i2 + " count = " + i3);
                    if (i3 >= 2 || FiveGManagerBase.this.setBandModeOnDefault(i2, str)) {
                        return;
                    }
                    FiveGManagerBase.this.mHandler.sendMessageDelayed(FiveGManagerBase.this.mHandler.obtainMessage(29, i2, i3 + 1, str), 1000L);
                    return;
            }
            for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
                FiveGManagerBase.this.update5GStateForCarrierConfigChanged(i4);
            }
        }
    }

    protected void onInitFiveGData() {
        bindFivegService();
        enableFiveGPowerOptimization();
        boolean z = Build.IS_INTERNATIONAL_BUILD;
        int i = Settings.Global.getInt(mContext.getContentResolver(), "flag_force_enable_sa", 0);
        int originalFiveGNetworkMode = mInstance.getOriginalFiveGNetworkMode();
        int i2 = Settings.System.getInt(mContext.getContentResolver(), "5g_network_mode_selection_visiable", -1);
        StringBuilder sb = new StringBuilder("force enable sa, isChinaMainlandVersion = ");
        sb.append(!z);
        sb.append(", flag = ");
        sb.append(i);
        sb.append(", saMode = ");
        sb.append(originalFiveGNetworkMode);
        sb.append(", saModeSecretCodeAvailable = ");
        sb.append(i2);
        localLog("flagForceEnableSa", sb.toString());
        if (!z && i2 == -1 && i < 2 && (originalFiveGNetworkMode == 0 || originalFiveGNetworkMode == 1)) {
            Settings.Global.putInt(mContext.getContentResolver(), "fiveg_network_mode", -1);
            Settings.Global.putInt(mContext.getContentResolver(), "flag_force_enable_sa", 2);
        }
        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
            initFiveGSettingViewVisible(i3);
        }
        if (getOriginalUserFiveGSaEnabled() != -1) {
            Settings.Global.putInt(mContext.getContentResolver(), "fiveg_sa_user_enabled", -1);
        }
        for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
            getNrConfigType(i4);
            getXMNetworkType(i4);
        }
        initTeleDumpId();
    }

    public void onCarrierConfigChanged(int i, int i2) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip onCarrierConfigChanged.");
            return;
        }
        Message messageObtainMessage = fiveGHandler.obtainMessage(6);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = i2;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public void onSimStateChanged(final int i, final int i2) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip onSimStateChanged.");
        } else {
            fiveGHandler.post(new Runnable() { // from class: com.android.phone.FiveGManagerBase$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FiveGManagerBase.$r8$lambda$39s3twie5LfAdrEzzrL46vQuKOg(i, i2);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$39s3twie5LfAdrEzzrL46vQuKOg(int i, int i2) {
        mCarrierConfigState[i] = i2;
    }

    protected void onHandleCarrierConfigChanged(Message message) {
        int i = message.arg1;
        log("EVENT_CARRIER_CONFIG_CHANGED slotIdForCarrier: " + i + ", subIdForCarrier: " + message.arg2);
        if (SubscriptionManager.isValidSlotId(i)) {
            fetch5GSwitchCarrierStatus();
            fetchSaModeCarrierStatus();
            fetchFiveGPowerSavingCarrierStatus();
            if (DefaultSimManager.getInstance().isDataSlotReady() && mCarrierConfigState[i] == CARRIER_CONFIG_STATE_LOADED) {
                update5GStateForCarrierConfigChanged(i);
            }
            fetch5GIconDisplayRule(i);
        }
    }

    public void onDataSlotReady(boolean z) {
        log("onDataSlotReady, dataSlotChanged: " + z + ", dataSlot: " + DefaultSimManager.getDefaultDataSlotId() + ", oldDataSlot: " + this.mDefaultDataSlotId);
        if (this.mHandler == null) {
            log("skip onDataSlotReady.");
            return;
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (this.mDefaultDataSlotId != defaultDataSlotId) {
            changeFivegNetwork();
            this.mDefaultDataSlotId = defaultDataSlotId;
        }
        log("onDataSlotReady, fiveg network database processing completed");
        Message messageObtainMessage = this.mHandler.obtainMessage(8);
        messageObtainMessage.arg1 = z ? 1 : 0;
        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            this.mHandler.removeMessages(8);
            StringBuilder sb = new StringBuilder("onDataSlotReady, dataSlotChanged delay ");
            int i = DELAY_DATA_SLOT_READY_MSG_MS;
            sb.append(i);
            sb.append("ms to update for MTK.");
            log(sb.toString());
            this.mHandler.sendMessageDelayed(messageObtainMessage, i);
        } else {
            this.mHandler.sendMessage(messageObtainMessage);
        }
        boolean zIsUserFiveGSaEnabled = isUserFiveGSaEnabled();
        boolean zIsDualSaEnabled = isDualSaEnabled();
        if (z) {
            if (zIsUserFiveGSaEnabled || zIsDualSaEnabled) {
                if (FiveGUtils.isXmRadioSupported()) {
                    NrSceneConstants$Scene nrSceneConstants$Scene = NrSceneConstants$Scene.INIT;
                    int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
                    final String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
                    HyperCommManager.getDefault().requestNrOperation(this.mDefaultDataSlotId, strValueOf, typeByScene, false, new ResultReceiver(this.mHandler) { // from class: com.android.phone.FiveGManagerBase.1
                        @Override // android.os.ResultReceiver
                        protected void onReceiveResult(int i2, Bundle bundle) {
                            FiveGManagerBase.log("requestNrOperation result=" + i2 + "; featureId=" + strValueOf + "; phoneId=" + FiveGManagerBase.this.mDefaultDataSlotId);
                        }
                    });
                    return;
                }
                ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).enableSaAsync(true, 0);
            }
        }
    }

    protected void update5GStateForCarrierConfigChanged(int i) {
        boolean zIsCarrierConfigFetchDone = isCarrierConfigFetchDone(i);
        logi("update5GStateForCarrierConfigChanged slotId: " + i + ", carrier state: " + mCarrierConfigState[i]);
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getIccCard() == null || !phone.getIccCard().hasIccCard() || !zIsCarrierConfigFetchDone) {
            return;
        }
        if (i == DefaultSimManager.getDefaultDataSlotId() || TelephonyManager.getDefault().isDualNrSupported()) {
            setFiveGSwitchVisible(MiuiPhoneUtils.isFiveGSettingViewVisible(i), i);
            log("carrier config changed, slot" + i + "  carrier fetch done");
            if (is5GSwitchClosedByCloud()) {
                log("carrier config changed, is5GSwitchClosedByCloud");
                onSetUserFiveGEnabled(false, i);
                return;
            }
            if (disableFiveGSwitchByDefault(i)) {
                log("carrier config changed, disableFiveGSwitchByDefault");
                return;
            }
            int originalUserFiveGEnabled = getOriginalUserFiveGEnabled(i);
            log("carrier config changed, slot: " + i + " userEnabledSetting: " + originalUserFiveGEnabled + ", 5GSwitchCarrierConfig: " + is5GSwitchCarrierAvailable(i));
            if (originalUserFiveGEnabled == -1) {
                if (is5GSwitchCarrierAvailable(i)) {
                    log("carrier config changed, slot: " + i + " open 5G");
                    enableFiveGUser(true, i);
                } else {
                    log("carrier config changed, slot: " + i + " close 5G");
                    enableFiveGUser(false, i);
                }
            } else if (!is5GSwitchCarrierAvailable(i)) {
                log("carrier config changed, user update 5G database, slot: " + i + " disable 5G");
                enableFiveGUser(false, i);
            }
            if (MiuiPhoneUtils.is5gSaSupported()) {
                updateFiveGSaStatus(i);
            }
        }
    }

    protected void updateNetworkTypeImmediately(SwitchStatus switchStatus) {
        if (this.mHandler == null) {
            log("skip updateNetworkTypeImmediately.");
            return;
        }
        Phone phone = PhoneFactory.getPhone(switchStatus.slotId);
        if (phone != null) {
            if (this.mFactoryResetState[switchStatus.slotId]) {
                this.mFactoryResetState[switchStatus.slotId] = false;
                android.telephony.SubscriptionManager.setSubscriptionProperty(phone.getSubId(), "allowed_network_types_for_reasons", "user=" + switchStatus.networkType);
                phone.loadAllowedNetworksFromSubscriptionDatabase();
            }
            phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(switchStatus.networkType), this.mHandler.obtainMessage(5, switchStatus));
            return;
        }
        loge("updateNetworkTypeImmediately phone NULL!");
    }

    protected void enableFiveGUser(SwitchStatus switchStatus) {
        preHandleEnableFiveGUser(switchStatus.enabled);
        updateNetworkTypeImmediately(switchStatus);
    }

    protected void enableFiveGUser(boolean z, int i) {
        int lteNetworkModeValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getLteNetworkModeValue(i);
        int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
        int[][] supportModes = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportModes();
        if (i >= 0 && !ArrayUtils.contains(supportModes[i], preferredNetworkModeFromDb)) {
            int[] iArr = supportModes[i];
            if (iArr.length > 0) {
                preferredNetworkModeFromDb = iArr[0];
            }
        }
        if (!ArrayUtils.contains(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9), preferredNetworkModeFromDb)) {
            lteNetworkModeValue = preferredNetworkModeFromDb;
        }
        int nrNetworkModeValue = z ? ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNrNetworkModeValue(i) : lteNetworkModeValue;
        if (nrNetworkModeValue == -1) {
            log("enableFiveGUser, slotId : " + i + " networkType is invalid");
            return;
        }
        SwitchStatus switchStatus = new SwitchStatus(this, i, z, nrNetworkModeValue);
        localLog("set5GEnabled", "enable 5G, slotId: " + i + ", dbMode: " + preferredNetworkModeFromDb + ", prefNetworkMode: " + lteNetworkModeValue + ", networkType: " + nrNetworkModeValue);
        enableFiveGUser(switchStatus);
    }

    protected void onSetUserFiveGEnabled(boolean z, int i) {
        setFivegEnabled(z, i);
        enableFiveGUser(z, i);
    }

    protected void onSetUserFiveGEnabled(SwitchStatus switchStatus) {
        setFivegEnabled(switchStatus.enabled, switchStatus.slotId);
        enableFiveGUser(switchStatus);
    }

    public void enableFiveGUserAsync(int i, boolean z, int i2) {
        if (this.mHandler == null) {
            log("skip enableFiveGUserAsync.");
        } else {
            this.mHandler.obtainMessage(3, new SwitchStatus(this, i, z, i2)).sendToTarget();
        }
    }

    public void setUserFiveGEnabled(boolean z) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip setUserFiveGEnabled.");
            return;
        }
        Message messageObtainMessage = fiveGHandler.obtainMessage(7);
        messageObtainMessage.arg1 = z ? 1 : 0;
        messageObtainMessage.arg2 = DefaultSimManager.getDefaultDataSlotId();
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public void setUserFiveGEnabled(boolean z, int i) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip setUserFiveGEnabled.");
            return;
        }
        Message messageObtainMessage = fiveGHandler.obtainMessage(7);
        messageObtainMessage.arg1 = z ? 1 : 0;
        messageObtainMessage.arg2 = i;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public boolean isUserFiveGEnabled() {
        if (!is5GSwitchCarrierDisabled(DefaultSimManager.getDefaultDataSlotId()) || getOriginalUserFiveGEnabled(DefaultSimManager.getDefaultDataSlotId()) != -1) {
            return 1 == Settings.Global.getInt(mContext.getContentResolver(), "fiveg_user_enable", 1);
        }
        log("isUserFiveGEnabled carrier disable 5G switch");
        return false;
    }

    public boolean isUserFiveGEnabled(int i) {
        if (i == DefaultSimManager.getDefaultDataSlotId()) {
            return isUserFiveGEnabled();
        }
        return isDualNrEnabled();
    }

    public static boolean isFiveGCapable() {
        return MiuiPhoneUtils.isFiveGCapable();
    }

    public static int getOriginalUserFiveGEnabled(int i) {
        if (i == DefaultSimManager.getDefaultDataSlotId()) {
            return Settings.Global.getInt(mContext.getContentResolver(), "fiveg_user_enable", -1);
        }
        return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_nr_enabled", -1);
    }

    public static synchronized void set5GSwitchCarrierAvailable(boolean z) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            sFiveGSwitchCarrierAvailable[i] = z;
        }
    }

    public static synchronized boolean is5GSwitchCarrierAvailable(int i) {
        return sFiveGSwitchCarrierAvailable[i];
    }

    public static synchronized boolean is5GSwitchCarrierDisabled(int i) {
        return sFiveGSwitchCarrierDisabled[i];
    }

    private static synchronized void fetch5GSwitchCarrierStatus() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            boolean z = true;
            boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, phone.getSubId(), "carrier_5g_switch_available_bool", !MiuiPhoneUtils.shouldDisable5GByDefault());
            boolean booleanConfigForSubId2 = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, phone.getSubId(), "carrier_disable_5g_switch_bool", false);
            if (!MiuiPhoneUtils.shouldDisable5GSwitchByDefault()) {
                z = booleanConfigForSubId2;
            }
            log("fetch5GSwitchCarrierStatus slotId: " + i + ", is5GSwitchAvailable: " + booleanConfigForSubId + ", is5GSwitchDisabled: " + z + ", shouldDisable5GSwitch: " + MiuiPhoneUtils.shouldDisable5GSwitchByDefault());
            sFiveGSwitchCarrierAvailable[i] = booleanConfigForSubId;
            sFiveGSwitchCarrierDisabled[i] = z;
        }
    }

    private synchronized void fetchFiveGPowerSavingCarrierStatus() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, PhoneFactory.getPhone(i).getSubId(), "carrier_fiveg_power_saving_available_bool", true);
            log("phoneId: " + i + ", is5GPowerSavingAvailable: " + booleanConfigForSubId);
            if (!booleanConfigForSubId) {
                setFiveGPowerSavingVisible(false);
                break;
            }
        }
    }

    public static boolean is5GSaModeAvailableBySecretCode() {
        return Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "5g_network_mode_selection_visiable", -1) == 1;
    }

    public static synchronized boolean is5GSaModeCarrierAvailable() {
        return sSaModeCarrierAvailable[DefaultSimManager.getDefaultDataSlotId()];
    }

    public static synchronized boolean is5GSaModeCarrierDisabled() {
        return sSaModeCarrierDisabled[DefaultSimManager.getDefaultDataSlotId()];
    }

    public static void fetchSaModeCarrierStatus() {
        boolean z;
        boolean z2;
        boolean z3 = false;
        int i = 0;
        while (i < MiuiPhoneUtils.PHONE_COUNT) {
            Phone phone = PhoneFactory.getPhone(i);
            int i2 = Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "5g_network_mode_selection_visiable", -1);
            if (!MiuiPhoneUtils.isSupportSecretCodeForSA(i)) {
                i2 = -1;
            }
            boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, phone.getSubId(), "carrier_sa_mode_available_bool", z3);
            boolean z4 = true;
            if (i2 != -1) {
                z = i2 == 1 ? true : z3;
            } else {
                z = booleanConfigForSubId;
            }
            int i3 = Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "5g_sa_mode_disabled", -1);
            boolean booleanConfigForSubId2 = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, phone.getSubId(), "carrier_disable_sa_mode_bool", MiuiPhoneUtils.shouldDisableSaSwitchByDefault());
            sIsSaModeCarrierDisabled = booleanConfigForSubId2;
            if (i3 != -1) {
                z2 = i3 == 1 ? true : z3;
            } else {
                z2 = booleanConfigForSubId2;
            }
            int originalFiveGNetworkMode = mInstance.getOriginalFiveGNetworkMode();
            if (Build.IS_CM_CUSTOMIZATION_TEST) {
                log("CM test enable SA by default");
                z2 = z3;
            }
            boolean z5 = Build.IS_INTERNATIONAL_BUILD;
            int simStateForSlotIndex = android.telephony.TelephonyManager.getSimStateForSlotIndex(i);
            boolean booleanConfigForSubId3 = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, phone.getSubId(), "carrier_config_applied_bool");
            localLog("fetchSaModeCarrierStatus", "fetchSaModeCarrierStatus slotId: " + i + ", isConfigCarrierSpecific: " + booleanConfigForSubId3 + ", DefaultDataSlotId:" + DefaultSimManager.getDefaultDataSlotId() + ", saModeSecretCodeAvailable: " + i2 + ", isSaModeCarrierAvailable: " + booleanConfigForSubId + ", saModeSecretCodeDisabled: " + i3 + ", isSaModeCarrierDisabled: " + booleanConfigForSubId2 + ", isSaModeAvailable: " + z + ", isSaModeDisabled: " + z2 + ", isInternationalBuild: " + z5 + ", originalFiveGNetworkMode: " + originalFiveGNetworkMode + ", simState: " + simStateForSlotIndex);
            if (z5 && i2 == -1 && originalFiveGNetworkMode != -1 && !booleanConfigForSubId && booleanConfigForSubId2 && phone.getPhoneId() == DefaultSimManager.getDefaultDataSlotId() && booleanConfigForSubId3 && simStateForSlotIndex == 10) {
                log("disable sa mode and clear database for main slot only");
                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "fiveg_network_mode", -1);
            } else {
                z4 = z2;
            }
            sSaModeCarrierAvailable[i] = z;
            sSaModeCarrierDisabled[i] = z4;
            i++;
            z3 = false;
        }
    }

    public static boolean is5GSwitchClosedByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("5g_switch_off", false);
        }
        return false;
    }

    public static boolean is5GSaModeDisabledByCloud() {
        return is5GSaModeDisabledByCloud(DefaultSimManager.getDefaultDataSlotId());
    }

    public static boolean is5GSaModeDisabledByCloud(int i) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).is5GSaModeDisabledByCloud(i);
    }

    public static boolean is5GSaModeEnabledByCloud() {
        return is5GSaModeEnabledByCloud(DefaultSimManager.getDefaultDataSlotId());
    }

    public static boolean is5GSaModeEnabledByCloud(int i) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).is5GSaModeEnabledByCloud(i);
    }

    public static boolean isVonrSwitchEnabledByCloud(int i) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isVonrSwitchEnabledByCloud(i);
    }

    public static boolean isVonrSwitchDisabledByCloud(int i) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isVonrSwitchDisabledByCloud(i);
    }

    protected void updateFiveGSaStatus(int i) {
        int fiveGNetworkMode;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (MiuiPhoneUtils.is5gSaSupported() && phone != null && SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
            log("update 5G SA status, slotId = " + i);
            StringBuilder sb = new StringBuilder(512);
            sb.append("update 5G SA status :");
            if (i == DefaultSimManager.getDefaultDataSlotId()) {
                fiveGNetworkMode = isUserFiveGSaEnabled() ? 2 : getFiveGNetworkMode();
                onSetUserFiveGNetworkMode(fiveGNetworkMode, i, false);
                sb.append(" slotId: ");
                sb.append(i);
                sb.append(", dataSlotMode: ");
                sb.append(fiveGNetworkMode);
                localLog("updateFiveGSaStatus", sb.toString());
                return;
            }
            fiveGNetworkMode = isDualSaEnabled() ? 2 : 0;
            onSetUserFiveGNetworkMode(fiveGNetworkMode, i, false);
            sb.append(" viceSlot: ");
            sb.append(i);
            sb.append(", viceSlotMode: ");
            sb.append(fiveGNetworkMode);
            localLog("updateFiveGSaStatus", sb.toString());
        }
    }

    public void setUserFiveGSaEnabled(boolean z) {
        setUserFiveGSaEnabled(z, DefaultSimManager.getDefaultDataSlotId());
    }

    public void setUserFiveGSaEnabled(boolean z, int i) {
        if (!SubscriptionManager.isValidSlotId(i)) {
            log("set user SA enabled=" + z + ", invalid slotId=" + i);
            return;
        }
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(mContext, SubscriptionManager.getDefault().getSubscriptionIdForSlot(i), "setUserFiveGSaEnabled");
        int i2 = z ? 2 : 0;
        log("set user SA enabled=" + z + ", slotId=" + i);
        onSetUserFiveGNetworkMode(i2, i);
    }

    public void setUserFiveGNetworkMode(int i) {
        onSetUserFiveGNetworkMode(i, DefaultSimManager.getDefaultDataSlotId());
    }

    public void setUserFiveGNetworkMode(int i, int i2) {
        log("set user vice SA slotId=" + i2 + ", mode=" + i);
        if (SubscriptionManager.isValidSlotId(i2)) {
            onSetUserFiveGNetworkMode(i, i2);
        }
    }

    protected void onSetUserFiveGNetworkMode(int i, int i2) {
        onSetUserFiveGNetworkMode(i, i2, true);
    }

    protected void onSetUserFiveGNetworkMode(final int i, final int i2, final boolean z) {
        new Thread() { // from class: com.android.phone.FiveGManagerBase.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                FiveGManagerBase.localLog("setSaEnabled", "set user 5G network mode, slotId=" + i2 + ", mode=" + i + ", updateSettingsDb=" + z);
                if (z) {
                    if (i2 == DefaultSimManager.getDefaultDataSlotId()) {
                        Settings.Global.putInt(FiveGManagerBase.mContext.getContentResolver(), "fiveg_network_mode", i);
                    } else {
                        Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "dual_sa_enabled", i == 2 ? 1 : 0);
                    }
                }
                FiveGManagerBase.this.execSetUserFiveGNetworkMode(i, i2, z);
            }
        }.start();
    }

    public boolean isUserFiveGSaEnabled() {
        return 2 == getFiveGNetworkMode();
    }

    public boolean isUserFiveGSaEnabled(int i) {
        if (!SubscriptionManager.isValidSlotId(i)) {
            log("isUserFiveGSaEnabled invalid slotId=" + i);
            return false;
        }
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(mContext, SubscriptionManager.getDefault().getSubscriptionIdForSlot(i), "isUserFiveGSaEnabled");
        if (!isUserFiveGEnabled(i)) {
            return false;
        }
        if (i == DefaultSimManager.getDefaultDataSlotId()) {
            return isUserFiveGSaEnabled();
        }
        return isDualSaEnabled();
    }

    protected int getOriginalUserFiveGSaEnabled() {
        return Settings.Global.getInt(mContext.getContentResolver(), "fiveg_sa_user_enabled", -1);
    }

    public boolean isUserFiveGViceSimSaEnabled() {
        return 2 == getOriginalFiveGViceSimNetworkMode();
    }

    public int getFiveGNetworkMode() {
        int originalFiveGNetworkMode = getOriginalFiveGNetworkMode();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        log("getFiveGNetworkMode origMode: " + originalFiveGNetworkMode + ", dataSlotId: " + defaultDataSlotId + ", disabledByCloud: " + is5GSaModeDisabledByCloud(defaultDataSlotId) + ", enabledByCloud: " + is5GSaModeEnabledByCloud(defaultDataSlotId) + ", is5GSaModeCarrierAvailable: " + is5GSaModeCarrierAvailable() + ", is5GSaModeCarrierDisabled: " + is5GSaModeCarrierDisabled());
        if (originalFiveGNetworkMode != -1 && (MiuiPhoneUtils.isSupportSecretCodeForSA(defaultDataSlotId) || is5GSaModeCarrierAvailable())) {
            return originalFiveGNetworkMode;
        }
        if (is5GSaModeEnabledByCloud()) {
            return 2;
        }
        return (is5GSaModeDisabledByCloud() || is5GSaModeCarrierDisabled()) ? 0 : 2;
    }

    public int getOriginalFiveGNetworkMode() {
        return Settings.Global.getInt(mContext.getContentResolver(), "fiveg_network_mode", -1);
    }

    public int getFiveGViceSimNetworkMode() {
        return Settings.Global.getInt(mContext.getContentResolver(), "fiveg_vice_sim_network_mode", 0);
    }

    protected int getOriginalFiveGViceSimNetworkMode() {
        return Settings.Global.getInt(mContext.getContentResolver(), "fiveg_vice_sim_network_mode", -1);
    }

    protected boolean disableFiveGSwitchByDefault(int i) {
        Phone phone;
        if (!is5GSwitchCarrierDisabled(i)) {
            return false;
        }
        int originalUserFiveGEnabled = getOriginalUserFiveGEnabled(i);
        log("disableFiveGByDefault userEnabledSetting : " + originalUserFiveGEnabled);
        if (originalUserFiveGEnabled != -1 || (phone = PhoneFactory.getPhone(i)) == null || phone.getIccCard() == null || !phone.getIccCard().hasIccCard()) {
            return false;
        }
        localLog("set5GEnabled", "disable 5G by slot" + i);
        enableFiveGUser(false, i);
        updateFiveGSaStatus(i);
        return true;
    }

    public boolean isUserFiveGHostedModeEnabled() {
        int i;
        if (MiuiPhoneUtils.isFivegHostedModeIndiaDefaultOff()) {
            i = Settings.Global.getInt(mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", 0);
        } else {
            i = Settings.Global.getInt(mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", 1);
        }
        return 1 == i;
    }

    public void setUserFiveGHostedModeEnabled(boolean z) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip setUserFiveGHostedModeEnabled.");
            return;
        }
        Message messageObtainMessage = fiveGHandler.obtainMessage(9);
        messageObtainMessage.arg1 = z ? 1 : 0;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    protected void onSetUserFiveGHostedModeEnabled(boolean z) {
        Settings.Global.putInt(mContext.getContentResolver(), "fiveg_hosted_mode_user_enable", z ? 1 : 0);
    }

    public static void setFivegEnabled(boolean z, int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            if (i == DefaultSimManager.getDefaultDataSlotId()) {
                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "fiveg_user_enable", z ? 1 : 0);
            } else {
                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "dual_nr_enabled", (z && TelephonyManager.getDefault().isDualNrSupported()) ? 1 : 0);
            }
        }
    }

    public static boolean isDualNrEnabled() {
        if (MiuiPhoneUtils.PHONE_COUNT <= 1) {
            return false;
        }
        if (!is5GSwitchCarrierDisabled(FiveGUtils.getViceSlotId()) || getOriginalUserFiveGEnabled(FiveGUtils.getViceSlotId()) != -1) {
            return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_nr_enabled", FiveGUtils.isDualNrSupported() ? 1 : 0) == 1;
        }
        log("isUserFiveGEnabled carrier disable 5G switch");
        return false;
    }

    public void setDualSaEnabled(boolean z) {
        setUserFiveGNetworkMode(z ? 2 : 0, FiveGUtils.getViceSlotId());
    }

    public boolean isDualSaEnabled() {
        if (FiveGUtils.isDualSaSupported() && MiuiPhoneUtils.getPhone(FiveGUtils.getViceSlotId()) != null) {
            int i = !CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, MiuiPhoneUtils.getPhone(FiveGUtils.getViceSlotId()).getSubId(), "carrier_disable_vice_sa_mode_bool", true) ? 1 : 0;
            Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
            if (cloudData != null && cloudData.getBoolean("vice_sa_on", false)) {
                i = 1;
            } else if (cloudData != null && cloudData.getBoolean("vice_sa_off", false)) {
                i = 0;
            }
            if (MiuiPhoneUtils.isSupportSecretCodeForSA(FiveGUtils.getViceSlotId())) {
                return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_sa_enabled", i) == 1;
            }
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    public void setUserVoNREnabled(int i, boolean z) {
        if (!isUserFiveGSaEnabled(i)) {
            log("5G SA switch has been disabled and Vonr will not be updated");
            return;
        }
        int platformModemFeatureStatus = MiuiPhoneUtils.getPlatformModemFeatureStatus(4, i);
        StringBuilder sb = new StringBuilder("setUserVoNREnabled slotId = ");
        sb.append(i);
        sb.append(", enabled = ");
        sb.append(z);
        sb.append(", queryStatus = ");
        sb.append(platformModemFeatureStatus == 1);
        log(sb.toString());
        if (z == platformModemFeatureStatus) {
            log("control data is in the same status as the platform and will not be updated");
        } else {
            setModemFeatureBySlot(new ModemFeatureStatus(i, 4, z ? 1 : 0));
            sendMessageQueryVonrState(i);
        }
    }

    public boolean isVoNREnabled(int i) {
        int platformModemFeatureStatus = MiuiPhoneUtils.getPlatformModemFeatureStatus(4, SubscriptionManager.getDefault().getSlotIdForSubscription(i));
        StringBuilder sb = new StringBuilder("isVoNREnabled subId = ");
        sb.append(i);
        sb.append(", enabled = ");
        sb.append(platformModemFeatureStatus == 1);
        log(sb.toString());
        return platformModemFeatureStatus == 1;
    }

    public void setCrbtDisable(boolean z) {
        int i = !z ? 1 : 0;
        Settings.Global.putInt(mContext.getContentResolver(), "button_crbt_mode", i);
        setModemFeatureMode(12, i);
        log("setCrbtDisable disable = " + z);
        HashMap map = new HashMap();
        map.put("crbt_enable", z ? "0" : "1");
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_crbt_enable", map);
    }

    public void setDSDAStateBySetting(int i) {
        if (i == 1) {
            Settings.Global.putInt(mContext.getContentResolver(), "dsda_user_enabled", 1);
        } else {
            Settings.Global.putInt(mContext.getContentResolver(), "dsda_user_enabled", 0);
        }
    }

    public int getDSDAStateBySetting() {
        return Settings.Global.getInt(mContext.getContentResolver(), "dsda_user_enabled", -1);
    }

    public void initFiveGSettingViewVisible(int i) {
        if (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "fiveg_switch_visible" + i, -1) == -1) {
            setFiveGSwitchVisible(true, i);
        }
        if (TelephonyManager.isCustForEsVodafone()) {
            setFiveGPowerSavingVisible(false);
        } else {
            setFiveGPowerSavingVisible(true);
        }
    }

    public static void setFiveGSwitchVisible(boolean z, int i) {
        Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "fiveg_switch_visible" + i, z ? 1 : 0);
    }

    public static void setFiveGPowerSavingVisible(boolean z) {
        Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "fiveg_power_saving_visible", z ? 1 : 0);
    }

    public static boolean isFiveGSwitchVisible(int i) {
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        StringBuilder sb = new StringBuilder("fiveg_switch_visible");
        sb.append(i);
        return Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
    }

    public static int getSpeedIntervalByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("5g_speed_interval");
        }
        return 0;
    }

    public static int getSignalQosWeakSignalBinByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("qos_weak_signal_bin");
        }
        return 0;
    }

    public static int getSignalQosVgSignalCnByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_vg");
        }
        return 0;
    }

    public static int getSignalQosSentPacketsCnByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_sent_packet");
        }
        return 0;
    }

    public static int getSignalQosNrUplinkByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_nr_uplink");
        }
        return 0;
    }

    public static int getSignalQosNrDownlinkByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_nr_downlink");
        }
        return 0;
    }

    public static int getSignalQosLteUplinkByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_lte_downlink");
        }
        return 0;
    }

    public static int getSignalQosLteDownlinkByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("signal_qos_lte_downlink");
        }
        return 0;
    }

    public static int getLowBatteryLevelByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("low_battery_level");
        }
        return 0;
    }

    public static int getLowBatteryHighSpeedByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("low_battery_high_speed");
        }
        return 0;
    }

    public static int getLowBatteryLowSpeedByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("low_battery_low_speed");
        }
        return 0;
    }

    public static int getSpeedWhitelistHighSpeedByCloud(String str) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSpeedWhitelistHighSpeedByCloud(str);
    }

    public static int getSpeedWhitelistLowSpeedByCloud(String str) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSpeedWhitelistLowSpeedByCloud(str);
    }

    public static int getSpeedOnlyStatusByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("speed_only_status");
        }
        return 0;
    }

    public static int getSpeedOnlyHighSpeedByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("speed_only_high_speed");
        }
        return 0;
    }

    public static int getSpeedOnlyLowSpeedByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getInt("speed_only_low_speed");
        }
        return 0;
    }

    public static List getDsda2DsdsWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getDsda2DsdsWhiteListByCloud(context);
    }

    public static List getSmartSwitchWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSmartSwitchWhiteListByCloud(context);
    }

    public synchronized void registerDataSlotCarrierConfigChangedListener(IFiveGCarrierConfigChangedListener iFiveGCarrierConfigChangedListener) {
    }

    public static List getPingPongWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getPingPongWhiteListByCloud(context);
    }

    public static List getCommonSignalWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCommonSignalWhiteListByCloud(context);
    }

    public static List getDualDataConcurrentModeWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getDualDataConcurrentModeWhiteListByCloud(context);
    }

    public static List getDualDataConcurrentLimitedWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getDualDataConcurrentLimitedWhiteListByCloud(context);
    }

    public static List getDualDataRedundantModeWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getDualDataRedundantModeWhiteListByCloud(context);
    }

    public static List getHikingModeCityWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHikingModeCityWhiteListByCloud(context);
    }

    public static List getResolutionVideoAppsWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getResolutionVideoAppsWhiteListByCloud(context);
    }

    public static List getSubwayModeSignalThresholdByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSubwayModeSignalThresholdByCloud(context);
    }

    public static List getPreloadVideoAppsWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getPreloadVideoAppsWhiteListByCloud(context);
    }

    public static List getHyperEngineGameWhiteListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHyperEngineGameWhiteListByCloud(context);
    }

    public static boolean getAirportModeSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getAirportModeSwitchByCloud(context);
    }

    public static boolean getFileUploadSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getFileUploadSwitchByCloud(context);
    }

    public static boolean getArrearSimNwDeniedModeByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getArrearSimNwDeniedModeByCloud(context);
    }

    public static boolean getHstTunnelSmartlinkByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHstTunnelSmartlinkByCloud(context);
    }

    public static boolean getSmartNetworkSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSmartNetworkSwitchByCloud(context);
    }

    public static boolean getFivegLBStreamOptByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getFivegLBStreamOptByCloud(context);
    }

    public static boolean getHstPreferFreqSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHstPreferFreqSwitchByCloud(context);
    }

    public static String getSignalMapSnsCellBlackListByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getSignalMapSnsCellBlackListByCloud(context);
    }

    public static boolean getHyperEngineGameSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHyperEngineGameSwitchByCloud(context);
    }

    public static boolean getHyperEngineHstSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHyperEngineHstSwitchByCloud(context);
    }

    public static boolean getHyperEngineSmartHoSwitchByCloud(Context context) {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getHyperEngineSmartHoSwitchByCloud(context);
    }

    protected boolean isCarrierConfigFetchDone(int i) {
        logi("isCarrierConfigFetchDone slotId: " + i + ", carrier state: " + mCarrierConfigState[i] + ", isDataSlotReady: " + DefaultSimManager.getInstance().isDataSlotReady());
        return mCarrierConfigState[i] == CARRIER_CONFIG_STATE_LOADED && DefaultSimManager.getInstance().isDataSlotReady();
    }

    public boolean isGameFiveGOptimizeSupported() {
        return MiuiPhoneUtils.isGameFiveGOptimizeSupported();
    }

    public void factoryReset(int i) {
        this.mFactoryResetState[i] = true;
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        String[] strArr = {"dual_sa_enabled", "dual_nr_enabled", "fiveg_network_mode", "fiveg_user_enable"};
        for (int i2 = 0; i2 < 4; i2++) {
            String str = strArr[i2];
            if (Settings.Global.getInt(contentResolver, str, -1) > -1) {
                Settings.Global.putString(contentResolver, str, "reset");
            }
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        ModemProvider.getInstance().setNetworkModeInDb(defaultDataSlotId, ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getLteNetworkModeValue(defaultDataSlotId));
        int i3 = 0;
        while (true) {
            if (i3 >= MiuiPhoneUtils.PHONE_COUNT) {
                i3 = -1;
                break;
            } else if (defaultDataSlotId != i3) {
                break;
            } else {
                i3++;
            }
        }
        if (i3 > -1) {
            updateNetworkTypeImmediately(new SwitchStatus(this, i3, false, ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportModes()[i3][0]));
        }
        for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
            update5GStateForCarrierConfigChanged(i4);
        }
    }

    public void setFiveGCarrierAggregationMode(int i) {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        MiuiPhoneUtils.setFiveGCarrierAggregationInDb(mContext, i, defaultDataSlotId);
        execSetFiveGCarrierAggregationMode(i, defaultDataSlotId);
    }

    public synchronized void setModemFeature(int i, int i2, int i3) {
    }

    public void sendAtCommand(int i, String[] strArr, Message message) {
        replyMessageError(message);
    }

    public void sendAtCommand(Phone phone, String[] strArr, Message message) {
        replyMessageError(message);
    }

    public synchronized void setModemFeatureOnSecretCode(int i, int i2, int i3) {
    }

    public synchronized byte[] getPlatformModemFeatureStatus(int i, int i2, int i3) {
        return null;
    }

    public synchronized void refreshMbn(int i) {
    }

    public void setModemFeatureMode(int i, int i2) {
        if (this.mHandler == null) {
            log("skip setModemFeatureMode.");
            return;
        }
        MiuiPhoneUtils.setModemFeatureInDb(mContext, i, i2);
        Message messageObtainMessage = this.mHandler.obtainMessage(18);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = i2;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public void setModemFeatureBySlot(ModemFeatureStatus modemFeatureStatus) {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip setModemFeatureBySlot.");
        } else {
            this.mHandler.sendMessage(fiveGHandler.obtainMessage(21, modemFeatureStatus));
        }
    }

    public void needRefreshMbn(int i) {
        if (ModemProvider.IS_QCOM) {
            FiveGHandler fiveGHandler = this.mHandler;
            if (fiveGHandler == null) {
                log("skip needRefreshMbn.");
                return;
            }
            Message messageObtainMessage = fiveGHandler.obtainMessage(19);
            messageObtainMessage.arg1 = i;
            this.mHandler.sendMessageDelayed(messageObtainMessage, 250L);
        }
    }

    public static int getModemFeatureMode(int i, int i2) {
        int modemFeatureFromDb = MiuiPhoneUtils.getModemFeatureFromDb(mContext, i);
        int slotIdForSubscription = SubscriptionManager.getDefault().getSlotIdForSubscription(i2);
        if (modemFeatureFromDb == -1) {
            if (((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isModemFeatureEnabledByCloud(i, slotIdForSubscription)) {
                return 1;
            }
            if (((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isModemFeatureDisabledByCloud(i, slotIdForSubscription)) {
                return 0;
            }
        }
        return modemFeatureFromDb;
    }

    private void changeFivegNetwork() {
        if (MiuiPhoneUtils.PHONE_COUNT > 1 && isFiveGCapable() && TelephonyManager.getDefault().isDualNrSupported()) {
            log("changeFivegNetwork");
            if (!HyperOSCustFeatureResolve.getBoolean("support_dual_fiveg", true)) {
                int i = Settings.Global.getInt(mContext.getContentResolver(), "fiveg_user_enable", -1);
                int i2 = Settings.Global.getInt(mContext.getContentResolver(), "dual_nr_enabled", -1);
                String strValueOf = "reset";
                Settings.Global.putString(mContext.getContentResolver(), "fiveg_user_enable", i2 == -1 ? "reset" : String.valueOf(i2));
                ContentResolver contentResolver = mContext.getContentResolver();
                if (i != -1) {
                    strValueOf = String.valueOf(i);
                }
                Settings.Global.putString(contentResolver, "dual_nr_enabled", strValueOf);
                return;
            }
            boolean zIsUserFiveGEnabled = isUserFiveGEnabled(0);
            setFivegEnabled(isUserFiveGEnabled(1), 0);
            setFivegEnabled(zIsUserFiveGEnabled, 1);
        }
    }

    public void sendMessageQueryVonrState(int i) {
        if (this.mHandler == null) {
            log("skip sendMessageQueryVonrState.");
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(mContext, subscriptionIdForSlot, "config_show_vonr_icon_in_statusbar", false);
        if (TelephonyManagerEx.getDefault().isVoNRSupported() && booleanConfigForSubId) {
            Message messageObtainMessage = this.mHandler.obtainMessage(27);
            messageObtainMessage.arg1 = i;
            messageObtainMessage.arg2 = subscriptionIdForSlot;
            this.mHandler.sendMessageDelayed(messageObtainMessage, 500L);
        }
    }

    public void sendVonrStateBoardcast(int i, int i2) {
        boolean zIsVoNREnabled = isVoNREnabled(i2);
        Intent intent = new Intent("miui.intent.action.ACTION_VONR_STATE_CHANGED");
        intent.putExtra("slotId", i);
        intent.putExtra("vonrStatus", zIsVoNREnabled ? 1 : 0);
        ActivityManager.broadcastStickyIntent(intent, 51, -1);
        log("updateVonrSetting : slotId = " + i + ", sudId = " + i2 + ", vonrQueryStatus = " + (zIsVoNREnabled ? 1 : 0));
    }

    public String getMobileNetworkCapability(int i) {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getMobileNetworkCapability(i);
    }

    public void setMobileNetworkCapability(String str) {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).setMobileNetworkCapability(str);
    }

    public boolean isSupportObtainCardSlotStatus() {
        return TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8735() || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850() || FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_SUPPORT_OBTAIN_CARD_SLOT_STATUS);
    }

    public int getFiveGAState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("5ga_enable", 1);
    }

    public int getFiveGA4CCState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("5ga_4cc_enable", 1);
    }

    public int getHOMState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("hom_enable", 0);
    }

    public int getSwulState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("swul_enable", 0);
    }

    public int getR16PowerState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("r16_power_enable", 0);
    }

    public int getNetSlicingState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("net_slicing_enable", 0);
    }

    public int getConditionalHandoverState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("conditional_handover_enable", 0);
    }

    public int getFullPowerModeState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("full_power_mode", 0);
    }

    public int get3GppVersionSate() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("set_3gpp_version", 0);
    }

    public int getSulState() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("sul_enable", 0);
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    protected static void logi(String str) {
        Log.i(LOG_TAG, str);
    }

    protected static void loge(String str) {
        Log.e(LOG_TAG, str);
    }

    public static void localLog(String str, String str2) {
        localLog(LOG_TAG, str, str2);
    }

    public static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap localLogs = getLocalLogs();
        synchronized (localLogs) {
            try {
                if (!localLogs.containsKey(str4)) {
                    localLogs.put(str4, new LocalLog(60));
                }
                ((LocalLog) localLogs.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("FiveGManager dump start------");
        HashMap localLogs = getLocalLogs();
        synchronized (localLogs) {
            try {
                for (Map.Entry entry : localLogs.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            printWriter.println("isFiveGSwitchVisible: [" + i + "] = " + isFiveGSwitchVisible(i));
        }
        if (TelephonyManagerEx.getDefault().isFiveGASupported()) {
            printWriter.println("FiveGManager 5GA switch status: " + mInstance.getFiveGAState());
            printWriter.println("FiveGManager 5GA registration: " + mInstance.isFiveGARegistration());
            if (TelephonyManagerEx.getDefault().isHOMSupported()) {
                printWriter.println("FiveGManager HOM switch status: " + getHOMState());
            }
        }
        if (TelephonyManagerEx.getDefault().isFiveGA4CCSupported()) {
            printWriter.println("FiveGManager 5GA 4CC switch status: " + mInstance.getFiveGA4CCState());
            printWriter.println("FiveGManager 5GA 4CC registration: " + mInstance.isFiveGA4CCRegistration());
        }
        printWriter.println("FiveGManager value=" + this.mDumpVal);
        printWriter.println("FiveGManager dump end------");
        printWriter.flush();
    }

    private void initTeleDumpId() {
        FiveGHandler fiveGHandler = this.mHandler;
        if (fiveGHandler == null) {
            log("skip initTeleDumpId.");
        } else {
            fiveGHandler.postDelayed(new Runnable() { // from class: com.android.phone.FiveGManagerBase$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$initTeleDumpId$1();
                }
            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTeleDumpId$1() {
        this.mDumpVal = getEncryptedIds();
    }

    protected static void replyMessageError(Message message) {
        if (message == null || message.getTarget() == null) {
            return;
        }
        AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
        message.sendToTarget();
    }
}
