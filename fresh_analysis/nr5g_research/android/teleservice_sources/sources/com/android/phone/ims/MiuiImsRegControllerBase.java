package com.android.phone.ims;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.IUtilityModule;
import com.android.phone.statistics.utils.DeviceUtil;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiImsRegControllerBase extends Handler {
    private static String LOG_TAG = "MiuiImsRegControllerBase";
    protected Context mContext;
    protected ImsPhone mImsPhone;
    protected int mImsRadioTech;
    protected ImsReasonInfo mImsReasonInfo;
    protected ImsRegistrationModeInfo mImsRegModeInfo;
    protected ImsRegistrationPdpInfo mImsRegPdpInfo;
    protected boolean mIsAirPlaneMode;
    protected int mProcessImsRadioTech;
    protected ImsReasonInfo mProcessImsReasonInfo;
    public static final long TIMEOUT_REGISTER_FAIL_CHECK = 20000;
    public static final long TIMEOUT_REGISTER_RECOVERY_RETRY = 3600000;
    public static final long TIMEOUT_RE_REGISTER_IMS = 30000;
    public static final long TIMEOUT_RE_REGISTER_RESULT_CHECK = 20000;
    private static final Context CONTEXT = PhoneGlobals.getInstance();
    public static boolean mIsBootCompleted = false;
    private boolean mIsShowingNotification = false;
    protected boolean mIsRecoveringForVoPS = false;
    protected boolean mIsVoPSEnabled = true;
    protected boolean mImsRegisterState = false;
    protected boolean mIsImsInUse = false;
    protected int mReTryTimes = 0;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.ims.MiuiImsRegControllerBase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            boolean z = false;
            if ("org.codeaurora.VOIP_VOPS_SSAC_STATUS".equalsIgnoreCase(action)) {
                if (intent.getIntExtra("phoneId", -1) != MiuiImsRegControllerBase.this.mImsPhone.getPhoneId()) {
                    return;
                }
                boolean booleanExtra = intent.getBooleanExtra("Vops", true);
                MiuiImsRegControllerBase miuiImsRegControllerBase = MiuiImsRegControllerBase.this;
                if (booleanExtra != miuiImsRegControllerBase.mIsVoPSEnabled && !booleanExtra) {
                    miuiImsRegControllerBase.log("ACTION_VOPS_SSAC_STATUS: isVopsEnabled = " + booleanExtra);
                    MiuiImsRegControllerBase.this.sendIMSRegistrationFailMessage(false);
                }
                MiuiImsRegControllerBase.this.mIsVoPSEnabled = booleanExtra;
                return;
            }
            if ("miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT1".equals(action) || "miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT2".equals(action)) {
                if (intent.getBooleanExtra("extra_is_enhanced_4g_lte_on", false) && !MiuiImsRegControllerBase.this.isWhiteCard()) {
                    z = true;
                }
                MiuiImsRegControllerBase.this.log("volteEnabledByUser: volteSwitch = " + z + " mIsImsInUse = " + MiuiImsRegControllerBase.this.mIsImsInUse);
                MiuiImsRegControllerBase miuiImsRegControllerBase2 = MiuiImsRegControllerBase.this;
                if (z == miuiImsRegControllerBase2.mIsImsInUse) {
                    return;
                }
                miuiImsRegControllerBase2.mIsImsInUse = z;
                if (z) {
                    miuiImsRegControllerBase2.sendIMSRegistrationFailMessage(true);
                    return;
                } else {
                    miuiImsRegControllerBase2.resetImsMonitorState();
                    return;
                }
            }
            if ("android.intent.action.BOOT_COMPLETED".equalsIgnoreCase(action)) {
                MiuiImsRegControllerBase.mIsBootCompleted = true;
                return;
            }
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action) && MiuiImsRegControllerBase.this.mImsPhone.getPhoneId() == intent.getIntExtra("phone", -1)) {
                int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                MiuiImsRegControllerBase.this.log("ACTION_SIM_CARD_STATE_CHANGED: simState = " + intExtra);
                MiuiImsRegControllerBase miuiImsRegControllerBase3 = MiuiImsRegControllerBase.this;
                if (miuiImsRegControllerBase3.mSimState != intExtra) {
                    if (intExtra == 11) {
                        miuiImsRegControllerBase3.mInsertSimTimeMillis = -1L;
                        miuiImsRegControllerBase3.removeMessages(3);
                        MiuiImsRegControllerBase.this.sendIMSRegistrationFailMessage(true);
                    } else if (intExtra == 1) {
                        miuiImsRegControllerBase3.resetImsMonitorState();
                    }
                    MiuiImsRegControllerBase miuiImsRegControllerBase4 = MiuiImsRegControllerBase.this;
                    miuiImsRegControllerBase4.mSimState = intExtra;
                    miuiImsRegControllerBase4.onSimStateChange(intExtra);
                }
            }
        }
    };
    private ImsMmTelManager.RegistrationCallback mRegistrationCallback = new ImsMmTelManager.RegistrationCallback() { // from class: com.android.phone.ims.MiuiImsRegControllerBase.2
        public void onRegistered(int i) {
            MiuiImsRegControllerBase miuiImsRegControllerBase = MiuiImsRegControllerBase.this;
            miuiImsRegControllerBase.mImsRegisterState = true;
            miuiImsRegControllerBase.setImsRegistered(i);
        }

        public void onUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2) {
            MiuiImsRegControllerBase miuiImsRegControllerBase = MiuiImsRegControllerBase.this;
            miuiImsRegControllerBase.mImsRegisterState = false;
            miuiImsRegControllerBase.setImsUnregistered(imsReasonInfo, i2);
        }
    };
    protected int mRecoverState = 0;
    protected int mProcessState = 0;
    protected int mSimState = 0;
    protected int mTrackReccovyAction = 0;
    protected long mInsertSimTimeMillis = -1;

    protected abstract void handleRecoveryAction(ImsReasonInfo imsReasonInfo);

    public abstract boolean isSkipErrorCode(int i);

    protected abstract void log(String str);

    protected abstract void onSimStateChange(int i);

    public class ImsRegistrationModeInfo {
        int mDsEndCause;
        boolean mHasImsRegSt;
        int mImsApnDsRat;
        long mImsApnDsSysRatMask;
        int mImsIpv4LastNetDownRat;
        int mImsIpv4LastNetDownRatCache;
        int mImsIpv4LastNetDownReason;
        int mImsIpv4LastNetDownReasonCache;
        int mImsIpv6LastNetDownRat;
        int mImsIpv6LastNetDownRatCache;
        int mImsIpv6LastNetDownReason;
        int mImsIpv6LastNetDownReasonCache;
        int mImsPdnActivedRat;
        int mImsPdpTypeMask;
        int mImsRegFailureRetryTimer;
        int mImsRegFailureType;
        int mImsRegFailureTypeCache;
        int mImsRegRat;
        int mImsRegRatCache;
        int mImsRegSt;
        int mMmcpEndCause;

        public ImsRegistrationModeInfo(MiuiImsRegControllerBase miuiImsRegControllerBase) {
        }

        public String toString() {
            return "ImsRegistrationModeInfo{mMmcpEndCause=" + this.mMmcpEndCause + ", mDsEndCause=" + this.mDsEndCause + ", mHasImsRegSt=" + this.mHasImsRegSt + ", mImsRegSt=" + this.mImsRegSt + ", mImsApnDsRat=" + this.mImsApnDsRat + ", mImsApnDsSysRatMask=" + this.mImsApnDsSysRatMask + ", mImsPdpTypeMask=" + this.mImsPdpTypeMask + ", mImsPdnActivedRat=" + this.mImsPdnActivedRat + ", mImsIpv4LastNetDownReason=" + this.mImsIpv4LastNetDownReason + ", mImsIpv4LastNetDownReasonCache=" + this.mImsIpv4LastNetDownReasonCache + ", mImsIpv6LastNetDownReason=" + this.mImsIpv6LastNetDownReason + ", mImsIpv6LastNetDownReasonCache=" + this.mImsIpv6LastNetDownReasonCache + ", mImsIpv4LastNetDownRat=" + this.mImsIpv4LastNetDownRat + ", mImsIpv4LastNetDownRatCache=" + this.mImsIpv4LastNetDownRatCache + ", mImsIpv6LastNetDownRat=" + this.mImsIpv6LastNetDownRat + ", mImsIpv6LastNetDownRatCache=" + this.mImsIpv6LastNetDownRatCache + ", mImsRegRat=" + this.mImsRegRat + ", mImsRegRatCache=" + this.mImsRegRatCache + ", mImsRegFailureType=" + this.mImsRegFailureType + ", mImsRegFailureTypeCache=" + this.mImsRegFailureTypeCache + ", mImsRegFailureRetryTimer=" + this.mImsRegFailureRetryTimer + '}';
        }
    }

    public class ImsRegistrationPdpInfo {
        int mCallEndReason;
        int mCallEndType;
        boolean mConnectState;
        int mPdpFailedCode;

        public ImsRegistrationPdpInfo(MiuiImsRegControllerBase miuiImsRegControllerBase, boolean z, int i, int i2, int i3) {
            this.mConnectState = z;
            this.mPdpFailedCode = i;
            this.mCallEndType = i2;
            this.mCallEndReason = i3;
        }

        public String toString() {
            return "ImsPdpInfo: pdpState: " + this.mConnectState + " pdpFaildCode: " + this.mPdpFailedCode + " callEndType: " + this.mCallEndType + " callEndReason: " + this.mCallEndReason;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        log("handleMessage what=" + message.what);
        int i = message.what;
        if (i == 1) {
            setImsUnregistered((ImsReasonInfo) message.obj, -1);
            return;
        }
        if (i == 2) {
            log("EVENT_IMS_REG_SUCCESS...");
        } else if (i == 3) {
            handleImsUnregistered(this.mImsReasonInfo, this.mImsRadioTech);
        } else {
            if (i != 4) {
                return;
            }
            handleRecoveryImsRegResult((ImsReasonInfo) message.obj, message.arg1, message.arg2, this.mProcessImsRadioTech);
        }
    }

    public MiuiImsRegControllerBase(Context context, ImsPhone imsPhone) {
        String str;
        this.mContext = context;
        this.mImsPhone = imsPhone;
        new MiuiImsRegistrationMonitor(context, imsPhone.getPhoneId(), this.mRegistrationCallback);
        if (this.mImsPhone.getPhoneId() == 0) {
            str = "miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT1";
        } else {
            str = "miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT2";
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("org.codeaurora.VOIP_VOPS_SSAC_STATUS");
        intentFilter.addAction(str);
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        log("MiuiImsRegControllerBase created");
    }

    public void updateImsCarrierConfig() {
        GsmCdmaPhone defaultPhone = this.mImsPhone.getDefaultPhone();
        if (defaultPhone != null) {
            this.mIsImsInUse = defaultPhone.isImsUseEnabled() && !isWhiteCard();
        }
        log("updateImsConfig: mIsImsInUse = " + this.mIsImsInUse);
        sendIMSRegistrationFailMessage(false);
    }

    public void receiveIMSRegistedStateBroadcast(boolean z, boolean z2) {
        if (!z && !z2) {
            log("receiveIMSRegistedStateBroadcast: register false");
            sendIMSRegistrationFailMessage(true);
        } else {
            this.mImsRegisterState = true;
            resetImsMonitorState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendIMSRegistrationFailMessage(boolean z) {
        if (z) {
            this.mProcessState = 1;
        }
        if (this.mProcessState == 1 && !this.mImsRegisterState && this.mSimState == 11) {
            boolean zHasMessages = hasMessages(3);
            boolean zIsInService = isInService(this.mImsPhone.getDefaultPhone());
            boolean z2 = this.mIsImsInUse && !this.mImsRegisterState && zIsInService && !zHasMessages;
            log("sendIMSRegistrationFailMessage: mIsImsInUse = " + this.mIsImsInUse + " isPsInService = " + zIsInService + " hasMessage = " + zHasMessages + " shouldSendMessage = " + z2);
            if (z2) {
                sendMessageDelayed(obtainMessage(3, this.mImsReasonInfo), TIMEOUT_REGISTER_FAIL_CHECK);
            }
        }
    }

    public static boolean isSupportImsRegOptByDevice(Context context) {
        if (!Build.IS_STABLE_VERSION) {
            return true;
        }
        boolean z = false;
        if (context == null) {
            return false;
        }
        try {
            z = context.getResources().getBoolean(R.bool.config_ims_reg_optimization);
        } catch (Exception unused) {
            Rlog.d(LOG_TAG, "isSupportImsRegOptByDevice error, not support");
        }
        Rlog.d(LOG_TAG, "isSupportImsRegOptByDevice support = " + z);
        return z;
    }

    public static boolean isSupportImsRegOptByCloud() {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportImsRegOptByCloud();
    }

    public static boolean isSupportImsRegOptimization(Context context) {
        return isSupportImsRegOptByDevice(context) && isSupportImsRegOptByCloud() && !MiuiPhoneUtils.isLabTestOn();
    }

    private boolean isSupportImsRegRecovery(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mImsPhone.getPhoneId(), "config_operator_disable_imsRegOpt", false)) {
            Rlog.d(LOG_TAG, "imsRegOpt is disabled by operator");
            return false;
        }
        try {
            if ("cu".equals(SystemProperties.get("ro.cust.test", ""))) {
                return false;
            }
            z = context.getResources().getBoolean(R.bool.config_ims_reg_optimization_recovery);
        } catch (Exception unused) {
            Rlog.d(LOG_TAG, "isSupportImsRegRecovery error, not support");
        }
        Rlog.d(LOG_TAG, "isSupportImsRegRecovery support = " + z);
        return z;
    }

    public void updateServiceState(boolean z) {
        if (z) {
            boolean z2 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
            this.mIsAirPlaneMode = z2;
            if (z2) {
                resetImsMonitorState();
                return;
            }
            return;
        }
        if (isInService(this.mImsPhone.getDefaultPhone())) {
            if (this.mInsertSimTimeMillis == -1) {
                this.mInsertSimTimeMillis = SystemClock.elapsedRealtime();
            }
            sendIMSRegistrationFailMessage(false);
        }
    }

    protected void resetImsMonitorState() {
        log("resetImsMonitorState");
        removeMessages(3);
        this.mReTryTimes = 0;
        this.mImsReasonInfo = null;
        this.mImsRadioTech = -1;
        this.mProcessState = 0;
        this.mRecoverState = 0;
        this.mIsVoPSEnabled = true;
        mIsBootCompleted = true;
        this.mIsRecoveringForVoPS = false;
        showNotification(this.mImsPhone.getPhoneId(), false);
        this.mImsRegPdpInfo = null;
        this.mImsRegModeInfo = null;
    }

    public void setImsRegistered(int i) {
        MiuiImsRegControllerBase miuiImsRegControllerBase;
        ImsReasonInfo imsReasonInfo;
        log("setImsRegistered: imsRadioTech = " + i);
        showNotification(this.mImsPhone.getPhoneId(), false);
        if (this.mTrackReccovyAction == 0 || (imsReasonInfo = this.mProcessImsReasonInfo) == null) {
            miuiImsRegControllerBase = this;
        } else {
            miuiImsRegControllerBase = this;
            miuiImsRegControllerBase.trackerRecoveryEvent(imsReasonInfo.getExtraCode(), this.mProcessImsReasonInfo.getExtraMessage(), this.mTrackReccovyAction, 2, "-1", "-1", this.mProcessImsRadioTech);
        }
        miuiImsRegControllerBase.mTrackReccovyAction = 0;
        miuiImsRegControllerBase.resetImsMonitorState();
    }

    public void setImsUnregistered(ImsReasonInfo imsReasonInfo, int i) {
        if (!isSupportImsRegOptByCloud()) {
            log("setImsUnregistered: disabled by cloud");
            return;
        }
        if (imsReasonInfo == null || imsReasonInfo.mCode != 1000 || isSkipErrorCode(imsReasonInfo.getExtraCode())) {
            return;
        }
        ImsReasonInfo imsReasonInfo2 = this.mImsReasonInfo;
        if (imsReasonInfo2 == null || imsReasonInfo2.getExtraCode() != imsReasonInfo.getExtraCode()) {
            log("setImsUnregistered: " + imsReasonInfo + "imsRadio tech: " + i);
            this.mImsReasonInfo = imsReasonInfo;
            this.mImsRadioTech = i;
        }
    }

    private void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i) {
        if (imsReasonInfo == null) {
            return;
        }
        if (!isInService(this.mImsPhone.getDefaultPhone())) {
            log("handleImsUnregistered: out of service");
            return;
        }
        if (!this.mIsVoPSEnabled && imsReasonInfo.getExtraCode() == 0) {
            imsReasonInfo = new ImsReasonInfo(1000, 5001, "vops not support");
        }
        log("handleImsUnregistered: start process, " + imsReasonInfo);
        this.mProcessState = 2;
        log("onPdpStatusInd pdp info invalid");
        this.mImsRegModeInfo = queryImsUnregisteredReason(this.mImsPhone.getPhoneId());
        trackerImsRegiterFailedEvent(imsReasonInfo.getExtraCode(), imsReasonInfo.getExtraMessage(), i);
        if (!isSupportImsRegRecovery(this.mContext) || isNetworkRoaming()) {
            return;
        }
        this.mProcessImsReasonInfo = imsReasonInfo;
        this.mProcessImsRadioTech = i;
        handleRecoveryAction(imsReasonInfo);
    }

    public ImsRegistrationModeInfo queryImsUnregisteredReason(int i) {
        log("queryImsUnregisteredReason");
        return null;
    }

    private void handleRecoveryImsRegResult(ImsReasonInfo imsReasonInfo, int i, int i2, int i3) {
        if (this.mImsRegisterState || imsReasonInfo == null || this.mTrackReccovyAction == 0) {
            return;
        }
        if (this.mRecoverState == 3) {
            showNotification(this.mImsPhone.getPhoneId(), true);
        }
        trackerRecoveryEvent(imsReasonInfo.getExtraCode(), imsReasonInfo.getExtraMessage(), this.mTrackReccovyAction, 1, String.valueOf(i), String.valueOf(i2), i3);
        this.mTrackReccovyAction = 0;
    }

    public boolean isDuringCall() {
        return MiuiPhoneUtils.isDuringCall();
    }

    public void addExtraParam(HashMap map) {
        boolean z;
        ImsRegistrationPdpInfo imsRegistrationPdpInfo = this.mImsRegPdpInfo;
        if (imsRegistrationPdpInfo != null && !(z = imsRegistrationPdpInfo.mConnectState)) {
            map.put("pdpState", Boolean.valueOf(z));
            map.put("pdpfailedCode", Integer.valueOf(this.mImsRegPdpInfo.mPdpFailedCode));
            map.put("callEndType", Integer.valueOf(this.mImsRegPdpInfo.mCallEndType));
            map.put("callendReason", Integer.valueOf(this.mImsRegPdpInfo.mCallEndReason));
        }
        ImsRegistrationModeInfo imsRegistrationModeInfo = this.mImsRegModeInfo;
        if (imsRegistrationModeInfo != null) {
            map.put("mmcpEndCause", Integer.valueOf(imsRegistrationModeInfo.mMmcpEndCause));
            map.put("dsEndCause", Integer.valueOf(this.mImsRegModeInfo.mDsEndCause));
            ImsRegistrationModeInfo imsRegistrationModeInfo2 = this.mImsRegModeInfo;
            if (imsRegistrationModeInfo2.mHasImsRegSt) {
                map.put("imsRegistrationST", Integer.valueOf(imsRegistrationModeInfo2.mImsRegSt));
                map.put("imsApnDsRat", Integer.valueOf(this.mImsRegModeInfo.mImsApnDsRat));
                map.put("imsApnDsSysRatMask", String.format("0x%08X %08X", Integer.valueOf((int) this.mImsRegModeInfo.mImsApnDsSysRatMask), Integer.valueOf((int) (this.mImsRegModeInfo.mImsApnDsSysRatMask >> 32))));
                map.put("imsPdpTypeMask", Integer.valueOf(this.mImsRegModeInfo.mImsPdpTypeMask));
                map.put("imsPdnActivatedRat", Integer.valueOf(this.mImsRegModeInfo.mImsPdnActivedRat));
                map.put("imsIpv4LastNetDownReason", Integer.valueOf(this.mImsRegModeInfo.mImsIpv4LastNetDownReason));
                map.put("imsIpv4LastNetDownReasonCache", Integer.valueOf(this.mImsRegModeInfo.mImsIpv4LastNetDownReasonCache));
                map.put("imsIpv6LastNetDownReason", Integer.valueOf(this.mImsRegModeInfo.mImsIpv6LastNetDownReason));
                map.put("imsIpv6LastNetDownReasonCache", Integer.valueOf(this.mImsRegModeInfo.mImsIpv6LastNetDownReasonCache));
                map.put("imsIpv4LastNetDownRat", Integer.valueOf(this.mImsRegModeInfo.mImsIpv4LastNetDownRat));
                map.put("imsIpv4LastNetDownRatCache", Integer.valueOf(this.mImsRegModeInfo.mImsIpv4LastNetDownRatCache));
                map.put("imsIpv6LastNetDownRat", Integer.valueOf(this.mImsRegModeInfo.mImsIpv6LastNetDownRat));
                map.put("imsIpv6LastNetDownRatCache", Integer.valueOf(this.mImsRegModeInfo.mImsIpv6LastNetDownRatCache));
                map.put("imsRegistrationRat", Integer.valueOf(this.mImsRegModeInfo.mImsRegRat));
                map.put("imsRegistrationRatCache", Integer.valueOf(this.mImsRegModeInfo.mImsRegRatCache));
                map.put("imsRegistrationFailureType", Integer.valueOf(this.mImsRegModeInfo.mImsRegFailureType));
                map.put("imsRegistrationFailureTypeCache", Integer.valueOf(this.mImsRegModeInfo.mImsRegFailureTypeCache));
                map.put("imsRegistrationFailureRetryTimer", Integer.valueOf(this.mImsRegModeInfo.mImsRegFailureRetryTimer));
            }
        }
    }

    public void trackerImsRegiterFailedEvent(int i, String str, int i2) {
        Phone defaultPhone = this.mImsPhone.getDefaultPhone();
        ServiceState serviceState = defaultPhone.getServiceState();
        String string = "";
        String operatorNumeric = serviceState == null ? "" : serviceState.getOperatorNumeric();
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(defaultPhone.getPhoneId());
        boolean roaming = serviceState == null ? false : serviceState.getRoaming();
        HashMap map = new HashMap();
        map.put("ErrorCode", Integer.valueOf(i));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        map.put("ErrorMessage", str);
        addExtraParam(map);
        map.put("Network", getAvailableOperatorNumeric(this.mImsPhone.getPhoneId()));
        map.put("vplmn", operatorNumeric);
        map.put("hplmn", simOperatorForSlot);
        map.put("roaming", Boolean.valueOf(roaming));
        map.put("ImsRadioTech", Integer.valueOf(i2));
        if (!DeviceUtil.IS_INTERNATIONAL_BUILD) {
            Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
            if (cloudData != null) {
                string = cloudData.getString("city");
            }
            map.put("c", string);
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ims_registration_failed_event", map);
    }

    public void trackerRecoveryEvent(int i, String str, int i2, int i3, String str2, String str3, int i4) {
        HashMap map = new HashMap();
        map.put("ErrorCode", Integer.valueOf(i));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        map.put("ErrorMessage", str);
        map.put("RecoveryAction", String.valueOf(i2));
        map.put("RecoveryResult", String.valueOf(i3));
        map.put("ActonResult", str2);
        map.put("ReturnCode", str3);
        map.put("Network", getAvailableOperatorNumeric(this.mImsPhone.getPhoneId()));
        map.put("ImsRadioTech", Integer.valueOf(i4));
        if (!DeviceUtil.IS_INTERNATIONAL_BUILD) {
            Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
            map.put("c", cloudData != null ? cloudData.getString("city") : "");
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ims_registration_recovery_event", map);
    }

    public String getAvailableOperatorNumeric(int i) {
        ServiceState serviceState = this.mImsPhone.getDefaultPhone().getServiceState();
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : null;
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "NoSim" : operatorNumeric;
    }

    public boolean isWhiteCard() {
        return "00101".equals(android.telephony.TelephonyManager.getDefault().getSimOperatorNumericForPhone(this.mImsPhone.getPhoneId()));
    }

    public boolean isNetworkRoaming() {
        ServiceState serviceState = this.mImsPhone.getDefaultPhone().getServiceState();
        return serviceState != null && serviceState.getRoaming();
    }

    public boolean isInService(Phone phone) {
        ServiceState serviceState = phone.getServiceState();
        if (serviceState == null || this.mIsAirPlaneMode || serviceState.getDataRegState() != 0) {
            return false;
        }
        return MiuiImsPhoneUtils.isRatLTE(phone) || MiuiImsPhoneUtils.isRatNr(phone);
    }

    public String recoveryActionToStr(int i) {
        if (i == 1) {
            return "RECOVER_STATE_FOR_RESET_VOLTE_SWITCH";
        }
        if (i == 2) {
            return "RECOVER_STATE_FOR_403_OR_481";
        }
        if (i == 3) {
            return "RECOVER_STATE_FOR_VOPS";
        }
        return "UNKNOW_ACTION";
    }

    public void showNotification(int i, boolean z) {
        NotificationManager notificationManager = (NotificationManager) PhoneGlobals.getInstance().getSystemService("notification");
        boolean z2 = this.mIsShowingNotification;
        if (!z) {
            if (z2) {
                notificationManager.cancel("no_ims_service", i);
                this.mIsShowingNotification = false;
                log("showNotification miss... ");
                return;
            }
            return;
        }
        log("showNotification mIsShowingNotification = " + z2 + " , mIsBootCompleted = " + mIsBootCompleted);
        if (this.mIsShowingNotification || !mIsBootCompleted) {
            return;
        }
        Context context = CONTEXT;
        String string = context.getString(R.string.no_ims_service_notification_content);
        Notification.Builder builder = new Notification.Builder(PhoneGlobals.getInstance(), "simHighPriority");
        builder.setContentTitle(context.getString(R.string.prompt_to_suggest_no_ims_service_notification_title)).setSmallIcon(R.drawable.ic_esim_global_notify).setStyle(new Notification.BigTextStyle(builder).bigText(string)).setContentText(string);
        notificationManager.notify("no_ims_service", i, builder.build());
        this.mIsShowingNotification = true;
        mIsBootCompleted = false;
        log("showNotification show... ");
    }
}
