package com.android.phone.ims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.recoveryPolicy.ImsPolicy;
import com.android.phone.recoveryPolicy.Policy;
import com.android.phone.recoveryPolicy.PolicyManager;
import com.android.phone.utils.ModemUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.mirilhook.MiRilHook;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsRegControllerImpl extends MiuiImsRegControllerBase {
    private static String LOG_TAG = "MiuiImsRegControllerImpl";
    private Long mFirstSpecialRecoveryTime;
    private boolean mHasRecoveryMbn;
    private boolean mIsDfsCheckMbn;
    private boolean mIsRecoverMBNStandby;
    private long mLastProcessDfsRecoveryTime;
    MiuiImsRegRecoveryImpl mMiuiImsRegRecovery;
    private ModemUtils mModemUtils;
    private BroadcastReceiver mReceiver;
    private int mSpecialRecoveryTimes;
    public static final long TIMEOUT_RECOVERY_MBN = 3600000;
    public static final long ACTIVE_DURATION = 120000;
    public static final long TIMEOUT_RETRY_ACTION = 600000;
    public static final long DFS_IMS_RECOVERY_TIMER = 30000;
    public static final long RECOVERY_TIMEOUT = v.c;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override // com.android.phone.ims.MiuiImsRegControllerBase, android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        switch (message.what) {
            case 102:
                handleWifiConnected();
                break;
            case 103:
                handleRecoveryAction((ImsReasonInfo) message.obj, message.arg1);
                break;
            case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                onPdpStatusInd((AsyncResult) message.obj);
                break;
            case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                log("handle message EVENT_RESET_VOLTE_STATE ImsRegisterState:" + this.mImsRegisterState);
                if (!this.mImsRegisterState) {
                    int iResetEnhanced4gLteModeSetting = getMiuiImsRegRecovery().resetEnhanced4gLteModeSetting();
                    this.mRecoverState = iResetEnhanced4gLteModeSetting;
                    this.mTrackReccovyAction = 1;
                    checkRecoveryResult(iResetEnhanced4gLteModeSetting == 1 ? 2 : 1, -2);
                }
                break;
            case 106:
                if (!this.mImsRegisterState && MiuiImsSceneControl.getInstance(this.mContext).canDoRecovery((Policy.Action) message.obj, this.mImsPhone)) {
                    doRecovery((Policy.Action) message.obj);
                    break;
                }
                break;
        }
    }

    class MiuiRilHookStateCallback implements BaseEvent.IEventListener {
        private MiuiRilHookStateCallback() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
                MiuiImsRegControllerImpl.this.log("MiuiImsRegControllerImpl ModemUtils qcrilhookReady...");
                ModemUtils modemUtils = MiuiImsRegControllerImpl.this.mModemUtils;
                MiuiImsRegControllerImpl miuiImsRegControllerImpl = MiuiImsRegControllerImpl.this;
                if (modemUtils == null) {
                    miuiImsRegControllerImpl.log("MiuiRilHookStateCallback failed, mModemUtils is null");
                    return;
                }
                miuiImsRegControllerImpl.mMiuiImsRegRecovery.setModemUtils(miuiImsRegControllerImpl.mModemUtils);
                ModemUtils modemUtils2 = MiuiImsRegControllerImpl.this.mModemUtils;
                MiuiImsRegControllerImpl miuiImsRegControllerImpl2 = MiuiImsRegControllerImpl.this;
                modemUtils2.registerOemHookRegistrant(miuiImsRegControllerImpl2, MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, null, "xiaomi.intent.action.ACTION_IMS_PDP_STATUS_IND", miuiImsRegControllerImpl2.mImsPhone.getPhoneId());
            }
        }
    }

    public MiuiImsRegControllerImpl(Context context, ImsPhone imsPhone) {
        super(context, imsPhone);
        this.mSpecialRecoveryTimes = 0;
        this.mFirstSpecialRecoveryTime = 0L;
        this.mIsRecoverMBNStandby = false;
        this.mHasRecoveryMbn = false;
        this.mIsDfsCheckMbn = false;
        this.mLastProcessDfsRecoveryTime = 0L;
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.ims.MiuiImsRegControllerImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                NetworkInfo networkInfo;
                boolean z;
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if ("qualcomm.intent.action.ACTIN_PDC_VALIDATE_DUMPED".equals(action)) {
                    byte[] byteArrayExtra = intent.getByteArrayExtra("dump_file");
                    MiuiImsRegControllerImpl miuiImsRegControllerImpl = MiuiImsRegControllerImpl.this;
                    if (byteArrayExtra == null) {
                        miuiImsRegControllerImpl.log("dump_file is null");
                        return;
                    } else {
                        miuiImsRegControllerImpl.log("dump_file:".concat(new String(byteArrayExtra)));
                        return;
                    }
                }
                if ("qualcomm.intent.action.ACTION_PDC_CONFIGS_VALIDATION".equals(action)) {
                    MiuiImsRegControllerImpl miuiImsRegControllerImpl2 = MiuiImsRegControllerImpl.this;
                    if (miuiImsRegControllerImpl2.mRecoverState == 5 || miuiImsRegControllerImpl2.mIsDfsCheckMbn) {
                        int intExtra = intent.getIntExtra("index", -1);
                        String stringExtra = intent.getStringExtra("nv_item");
                        if (stringExtra != null && MiuiImsRegControllerImpl.this.isNvItemChanged(stringExtra)) {
                            String stringExtra2 = intent.getStringExtra("ref_value");
                            String stringExtra3 = intent.getStringExtra("cur_value");
                            MiuiImsRegControllerImpl.this.log("nv_item " + stringExtra + " has changed...");
                            MiuiImsRegControllerImpl.this.log("refValue: " + stringExtra2);
                            MiuiImsRegControllerImpl.this.log("curValue: " + stringExtra3);
                            ((IModemModule) ModuleDelegate.get(IModemModule.class)).imsErrorCausedByMBN(stringExtra, MiuiImsRegControllerImpl.this.mImsPhone.getPhoneId());
                            if (!MiuiImsRegControllerImpl.this.mIsDfsCheckMbn) {
                                MiuiImsRegControllerImpl.this.mIsRecoverMBNStandby = true;
                            }
                            MiuiImsRegControllerImpl miuiImsRegControllerImpl3 = MiuiImsRegControllerImpl.this;
                            ImsReasonInfo imsReasonInfo = miuiImsRegControllerImpl3.mProcessImsReasonInfo;
                            if (imsReasonInfo != null) {
                                miuiImsRegControllerImpl3.mTrackReccovyAction = 2;
                                int extraCode = imsReasonInfo.getExtraCode();
                                MiuiImsRegControllerImpl miuiImsRegControllerImpl4 = MiuiImsRegControllerImpl.this;
                                miuiImsRegControllerImpl3.trackerRecoveryEvent(extraCode, stringExtra, miuiImsRegControllerImpl4.mTrackReccovyAction, 0, stringExtra2, stringExtra3, miuiImsRegControllerImpl4.mProcessImsRadioTech);
                            }
                        }
                        if (intExtra == -1) {
                            MiuiImsRegControllerImpl.this.log("mIsDfsCheckMbn = " + MiuiImsRegControllerImpl.this.mIsDfsCheckMbn + " mIsRecoverMBNStandby = " + MiuiImsRegControllerImpl.this.mIsRecoverMBNStandby);
                            if (MiuiImsRegControllerImpl.this.mIsDfsCheckMbn || !MiuiImsRegControllerImpl.this.mIsRecoverMBNStandby) {
                                MiuiImsRegControllerImpl.this.mIsDfsCheckMbn = false;
                                return;
                            }
                            String str = MiuiImsRegControllerImpl.sdf.format(new Date());
                            Settings.Global.putString(MiuiImsRegControllerImpl.this.mContext.getContentResolver(), "mbn_refresh", str + "_mbn_refresh");
                            z = SystemClock.elapsedRealtime() - MiuiImsRegControllerImpl.this.mInsertSimTimeMillis < MiuiImsRegControllerImpl.ACTIVE_DURATION;
                            boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
                            MiuiImsRegControllerImpl.this.log("isSupportRecoveryAction = " + MiuiImsRegControllerImpl.this.isSupportRecoveryAction() + " isNewSim = " + z + " isWifiConn = " + zIsWifiConnected);
                            if (MiuiImsRegControllerImpl.this.isSupportRecoveryAction()) {
                                if (z || zIsWifiConnected) {
                                    MiuiImsRegControllerImpl.this.log("recovery mbn after insert new sim");
                                    MiuiImsRegControllerImpl.this.processRecoveryMbn();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if ("com.xiaomi.DFS.intent.action.ACTION_DFS_IMS_CHECK".equals(action)) {
                    MiuiImsRegControllerImpl miuiImsRegControllerImpl5 = MiuiImsRegControllerImpl.this;
                    if (miuiImsRegControllerImpl5.mSimState == 11) {
                        miuiImsRegControllerImpl5.log("ACTION_DFS_IMS_CHECK");
                        MiuiImsRegControllerImpl.this.mIsDfsCheckMbn = true;
                        MiuiImsRegControllerImpl.this.checkMbnConfig();
                        return;
                    }
                    return;
                }
                if ("com.xiaomi.DFS.intent.action.ACTION_DFS_IMS_RECOVERY".equals(action)) {
                    MiuiImsRegControllerImpl.this.log("ACTION_DFS_IMS_RECOVERY");
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    if (jElapsedRealtime - MiuiImsRegControllerImpl.this.mLastProcessDfsRecoveryTime > MiuiImsRegControllerImpl.DFS_IMS_RECOVERY_TIMER) {
                        MiuiImsRegControllerImpl.this.mMiuiImsRegRecovery.recoveryMBN();
                    }
                    MiuiImsRegControllerImpl.this.mLastProcessDfsRecoveryTime = jElapsedRealtime;
                    return;
                }
                if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(action) || (networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo")) == null) {
                    return;
                }
                NetworkInfo.State state = networkInfo.getState();
                NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
                if (state == state2 || state == NetworkInfo.State.DISCONNECTED) {
                    int type = networkInfo.getType();
                    z = state == state2;
                    if (type == 1 && z) {
                        MiuiImsRegControllerImpl.this.log("wifi connected = " + z);
                        MiuiImsRegControllerImpl miuiImsRegControllerImpl6 = MiuiImsRegControllerImpl.this;
                        miuiImsRegControllerImpl6.sendMessage(miuiImsRegControllerImpl6.obtainMessage(102, intent));
                    }
                }
            }
        };
        this.mMiuiImsRegRecovery = new MiuiImsRegRecoveryImpl(context, imsPhone);
        if (ModemProvider.IS_QCOM) {
            this.mModemUtils = ModemUtils.init(this.mContext);
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", new MiuiRilHookStateCallback(), this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("qualcomm.intent.action.ACTIN_PDC_VALIDATE_DUMPED");
            intentFilter.addAction("qualcomm.intent.action.ACTION_PDC_CONFIGS_VALIDATION");
            intentFilter.addAction("com.xiaomi.DFS.intent.action.ACTION_DFS_IMS_CHECK");
            intentFilter.addAction("com.xiaomi.DFS.intent.action.ACTION_DFS_IMS_RECOVERY");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(this.mReceiver, intentFilter, 2);
        }
        log("MiuiImsRegControllerImpl created");
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    public boolean isSkipErrorCode(int i) {
        if (i == 0) {
            return ModemProvider.IS_MTK;
        }
        if (i == 999 || i == 4001 || i == 4002) {
            return ModemProvider.IS_QCOM;
        }
        return false;
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    protected void onSimStateChange(int i) {
        if (i == 1) {
            this.mIsRecoverMBNStandby = false;
            this.mSpecialRecoveryTimes = 0;
            this.mFirstSpecialRecoveryTime = 0L;
        }
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    protected void resetImsMonitorState() {
        if (this.mImsRegisterState) {
            removeMessages(103);
        }
        super.resetImsMonitorState();
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    public MiuiImsRegControllerBase.ImsRegistrationModeInfo queryImsUnregisteredReason(int i) {
        if (!ModemProvider.IS_QCOM) {
            return super.queryImsUnregisteredReason(i);
        }
        log("queryImsUnregisteredReason phoneId:" + i);
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null) {
            return null;
        }
        byte[] bArr = new byte[4];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i);
        ByteBuffer byteBufferOnHookCommonMsgSyncBySub = modemUtilsInit.onHookCommonMsgSyncBySub(143, bArr, i);
        MiuiImsRegControllerBase.ImsRegistrationModeInfo imsRegistrationModeInfo = new MiuiImsRegControllerBase.ImsRegistrationModeInfo(this);
        if (byteBufferOnHookCommonMsgSyncBySub != null) {
            byteBufferOnHookCommonMsgSyncBySub.order(ByteOrder.nativeOrder());
            int i2 = byteBufferOnHookCommonMsgSyncBySub.getInt();
            log("queryImsUnregisteredReason result: " + i2);
            if (i2 == 0) {
                int iRemaining = byteBufferOnHookCommonMsgSyncBySub.remaining();
                log("queryImsUnregisteredReason size: " + iRemaining);
                if (iRemaining == 8) {
                    imsRegistrationModeInfo.mMmcpEndCause = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mDsEndCause = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    log("queryImsUnregisteredReason mMmcpEndCause: " + imsRegistrationModeInfo.mMmcpEndCause);
                    log("queryImsUnregisteredReason mDsEndCause: " + imsRegistrationModeInfo.mDsEndCause);
                    return imsRegistrationModeInfo;
                }
                if (iRemaining >= 84) {
                    imsRegistrationModeInfo.mHasImsRegSt = true;
                    imsRegistrationModeInfo.mMmcpEndCause = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mDsEndCause = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegSt = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsApnDsRat = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsApnDsSysRatMask = byteBufferOnHookCommonMsgSyncBySub.getLong();
                    imsRegistrationModeInfo.mImsPdpTypeMask = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsPdnActivedRat = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv4LastNetDownReason = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv4LastNetDownReasonCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv6LastNetDownReason = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv6LastNetDownReasonCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv4LastNetDownRat = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv4LastNetDownRatCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv6LastNetDownRat = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsIpv6LastNetDownRatCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegRat = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegRatCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegFailureType = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegFailureTypeCache = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    imsRegistrationModeInfo.mImsRegFailureRetryTimer = byteBufferOnHookCommonMsgSyncBySub.getInt();
                    log("queryImsUnregisteredReason: " + imsRegistrationModeInfo.toString());
                }
                return imsRegistrationModeInfo;
            }
            log("get return error");
            return imsRegistrationModeInfo;
        }
        log("queryImsUnregisteredReason byteBuf is null");
        return imsRegistrationModeInfo;
    }

    private void handleWifiConnected() {
        if (this.mIsRecoverMBNStandby && !this.mHasRecoveryMbn && isSupportRecoveryAction()) {
            processRecoveryMbn();
            return;
        }
        if (hasMessages(103)) {
            log("handleWifiConnected: retry recovery, mReTryTimes: " + this.mReTryTimes);
            removeMessages(103);
            sendMessage(obtainMessage(103, this.mReTryTimes, 0, this.mProcessImsReasonInfo));
        }
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    protected void handleRecoveryAction(ImsReasonInfo imsReasonInfo) {
        log("handleRecoveryAction: imsReasonInfo = " + imsReasonInfo + " , mRecoverState = " + recoveryActionToStr(this.mRecoverState));
        this.mReTryTimes = 0;
        removeMessages(103);
        if (ModemProvider.IS_QCOM) {
            handleRecoveryAction(imsReasonInfo, 0);
            return;
        }
        int extraCode = imsReasonInfo.getExtraCode();
        if (extraCode != 0 && extraCode != 408 && extraCode != 500 && extraCode != 508) {
            if (extraCode == 5001 && !this.mImsRegisterState && this.mRecoverState == 0) {
                showNotification(this.mImsPhone.getPhoneId(), true);
                this.mTrackReccovyAction = 3;
            }
        } else if (isSupportRecoveryAction()) {
            int iResetEnhanced4gLteModeSetting = getMiuiImsRegRecovery().resetEnhanced4gLteModeSetting();
            this.mRecoverState = iResetEnhanced4gLteModeSetting;
            this.mTrackReccovyAction = 1;
            checkRecoveryResult(iResetEnhanced4gLteModeSetting == 1 ? 2 : 1, -2);
        }
        if (this.mRecoverState != 0) {
            sendMessageDelayed(obtainMessage(4, imsReasonInfo), MiuiImsRegControllerBase.TIMEOUT_RE_REGISTER_RESULT_CHECK);
        }
    }

    protected void handleRecoveryAction(ImsReasonInfo imsReasonInfo, int i) {
        log("handleRecoveryAction: imsReasonInfo = " + imsReasonInfo + " , mRecoverState = " + recoveryActionToStr(this.mRecoverState) + " mHasRecoveryMbn = " + this.mHasRecoveryMbn + " mTrackReccovyAction = " + this.mTrackReccovyAction + " retry_times = " + i);
        if (this.mFirstSpecialRecoveryTime.longValue() == 0 || SystemClock.elapsedRealtime() - this.mFirstSpecialRecoveryTime.longValue() >= RECOVERY_TIMEOUT) {
            this.mFirstSpecialRecoveryTime = Long.valueOf(SystemClock.elapsedRealtime());
            this.mSpecialRecoveryTimes = 0;
        }
        boolean z = this.mSpecialRecoveryTimes >= 3 || hasMessages(106);
        Policy.Action action = getAction(imsReasonInfo);
        if (action == null || action.name == null || !MiuiImsSceneControl.getInstance(this.mContext).canDoRecovery(action, this.mImsPhone)) {
            if (action != null) {
                if (i <= 3) {
                    int i2 = i + 1;
                    this.mReTryTimes = i;
                    sendMessageDelayed(obtainMessage(103, i2, 0, imsReasonInfo), ((long) i2) * TIMEOUT_RETRY_ACTION);
                    return;
                }
                return;
            }
            if (this.mRecoverState != 5 && !this.mHasRecoveryMbn) {
                this.mRecoverState = 5;
                checkMbnConfig();
            }
            if (hasMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT)) {
                return;
            }
            log("handleRecoveryAction default reset volte switch");
            this.mReTryTimes = i;
            sendMessageDelayed(obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i + 1, 0), 30000L);
            return;
        }
        log("getAction action:" + action.toString());
        if (z && isSpecialAction(action.name)) {
            log("the current recovery : " + action.name + "has not been executed, because the number of executions has reached the maximum:" + this.mSpecialRecoveryTimes);
            return;
        }
        if (hasMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT)) {
            removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
        }
        if (action.conditions.containsKey("delay")) {
            try {
                sendMessageDelayed(obtainMessage(106, action), Long.parseLong((String) action.conditions.get("delay")));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        doRecovery(action);
    }

    private boolean isSpecialAction(String str) {
        return "resetradio".equals(str) || "setnvconfig".equals(str) || "setnvconfigforvops".equals(str);
    }

    private void doRecovery(Policy.Action action) {
        int nvConfig;
        String str = action.name;
        str.hashCode();
        switch (str) {
            case "resetradio":
                log("doRecovery RESETRADIO");
                Intent intent = new Intent("miui.intent.action.RESET_RADIO");
                intent.setFlags(285212672);
                this.mContext.sendBroadcast(intent);
                this.mRecoverState = 7;
                this.mTrackReccovyAction = 7;
                checkRecoveryResult(2, -2);
                this.mSpecialRecoveryTimes++;
                break;
            case "resetvolte":
                log("doRecovery reset volte switch");
                int iResetEnhanced4gLteModeSetting = getMiuiImsRegRecovery().resetEnhanced4gLteModeSetting();
                this.mRecoverState = iResetEnhanced4gLteModeSetting;
                this.mTrackReccovyAction = 1;
                checkRecoveryResult(iResetEnhanced4gLteModeSetting != 1 ? 1 : 2, -2);
                break;
            case "refreshmbn":
                log("doRecovery REFRESHMBN");
                boolean zRecoveryMBN = this.mMiuiImsRegRecovery.recoveryMBN();
                this.mTrackReccovyAction = 6;
                checkRecoveryResult(zRecoveryMBN ? 2 : 1, -2);
                break;
            case "setnvconfig":
                if (this.mRecoverState != 2 && (nvConfig = this.mMiuiImsRegRecovery.setNvConfig()) != -2) {
                    log("doRecovery mRecoverState set RECOVER_STATE_FOR_403_OR_481");
                    this.mRecoverState = 2;
                    this.mTrackReccovyAction = 3;
                    checkRecoveryResult(1, nvConfig);
                    this.mSpecialRecoveryTimes++;
                    break;
                }
                break;
            case "setnvconfigforvops":
                if (this.mRecoverState != 3) {
                    int nVConfigForVoPS = this.mMiuiImsRegRecovery.setNVConfigForVoPS();
                    if (nVConfigForVoPS != -2) {
                        log("doRecovery mRecoverState set RECOVER_STATE_FOR_VOPS");
                        this.mRecoverState = 3;
                        this.mTrackReccovyAction = 4;
                        checkRecoveryResult(nVConfigForVoPS == 1 ? 2 : 1, nVConfigForVoPS);
                        this.mSpecialRecoveryTimes++;
                    }
                    if (nVConfigForVoPS == 2) {
                        showNotification(this.mImsPhone.getPhoneId(), true);
                    }
                    break;
                }
                break;
        }
    }

    protected Policy.Action getAction(ImsReasonInfo imsReasonInfo) {
        MiuiImsRegControllerBase.ImsRegistrationPdpInfo imsRegistrationPdpInfo;
        if (this.mImsRegisterState) {
            return null;
        }
        ImsPolicy imsPolicy = (ImsPolicy) PolicyManager.getInstance().getPolicy("ims");
        if (imsPolicy == null) {
            log("getAction imsPolicy is null");
            return null;
        }
        boolean roaming = this.mImsPhone.getServiceState().getRoaming();
        int voiceNetworkType = this.mImsPhone.getServiceState().getVoiceNetworkType();
        log("getAction imsReasonInfo:[" + imsReasonInfo + "] ImsRegPdpInfo" + this.mImsRegPdpInfo + " isRoaming:" + roaming + " rat:" + voiceNetworkType);
        Policy.Action action = imsReasonInfo != null ? imsPolicy.getAction(String.valueOf(imsReasonInfo.getExtraCode()), String.valueOf(roaming), String.valueOf(voiceNetworkType), String.valueOf(-1), String.valueOf(true)) : null;
        return (action != null || (imsRegistrationPdpInfo = this.mImsRegPdpInfo) == null) ? action : imsPolicy.getAction(String.valueOf(imsRegistrationPdpInfo.mCallEndReason), String.valueOf(roaming), String.valueOf(voiceNetworkType), String.valueOf(this.mImsRegPdpInfo.mCallEndType), String.valueOf(this.mImsRegPdpInfo.mConnectState));
    }

    public void checkMbnConfig() {
        boolean zBooleanValue;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("qcRilValidateConfig", "XIAOMIVALIDATE", Integer.valueOf(this.mImsPhone.getPhoneId()));
            if (objCallHook instanceof Boolean) {
                zBooleanValue = ((Boolean) objCallHook).booleanValue();
            } else {
                log("checkMbnConfig: qcRilValidateConfig return type not Boolean, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                zBooleanValue = false;
            }
            if (zBooleanValue) {
                return;
            }
            new Handler().postDelayed(new Runnable() { // from class: com.android.phone.ims.MiuiImsRegControllerImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    ModemUtils modemUtils2 = MiuiImsRegControllerImpl.this.mModemUtils;
                    MiuiImsRegControllerImpl miuiImsRegControllerImpl = MiuiImsRegControllerImpl.this;
                    if (modemUtils2 == null) {
                        miuiImsRegControllerImpl.log("checkMbnConfig retry failed, mModemUtils is null");
                        return;
                    }
                    Object objCallHook2 = miuiImsRegControllerImpl.mModemUtils.callHook("qcRilValidateConfig", "XIAOMIVALIDATE", Integer.valueOf(MiuiImsRegControllerImpl.this.mImsPhone.getPhoneId()));
                    boolean z = objCallHook2 instanceof Boolean;
                    MiuiImsRegControllerImpl miuiImsRegControllerImpl2 = MiuiImsRegControllerImpl.this;
                    if (z) {
                        miuiImsRegControllerImpl2.log("checkMbnConfig qcRilValidateConfig retry... result: " + objCallHook2);
                        return;
                    }
                    miuiImsRegControllerImpl2.log("checkMbnConfig retry: return type not Boolean, actual: ".concat(objCallHook2 != null ? objCallHook2.getClass().getName() : "null"));
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNvItemChanged(String str) {
        if (str.equals("/nv/item_files/ims/qp_ims_reg_config_db") || str.equals("/nv/item_files/ims/IMS_enable") || str.equals("/nv/item_files/ims/qp_ims_service_enablement_config")) {
            return true;
        }
        if (!str.contains("/Data_Profiles/Profile")) {
            return false;
        }
        String strSubstring = str.substring(22);
        if (TextUtils.isEmpty(strSubstring)) {
            return false;
        }
        try {
            int i = Integer.parseInt(strSubstring);
            return i >= 1 && i <= 5;
        } catch (NumberFormatException unused) {
            log("isNvItemChanged data profile number format error");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRecoveryMbn() {
        boolean zRecoveryMBN = this.mMiuiImsRegRecovery.recoveryMBN();
        this.mTrackReccovyAction = 5;
        this.mHasRecoveryMbn = true;
        this.mIsRecoverMBNStandby = false;
        checkRecoveryResult(zRecoveryMBN ? 2 : 1, -2);
    }

    private void onPdpStatusInd(AsyncResult asyncResult) {
        log("onPdpStatusInd");
        if (asyncResult.exception != null) {
            log("onPdpStatusInd exception, ar.exception = " + asyncResult.exception);
            return;
        }
        Object obj = asyncResult.result;
        if (obj == null) {
            log("onPdpStatusInd Null Response");
            return;
        }
        byte[] bArr = (byte[]) obj;
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        log("onPdpStatusInd response length " + bArr.length);
        this.mImsRegPdpInfo = new MiuiImsRegControllerBase.ImsRegistrationPdpInfo(this, byteBufferWrap.getInt() != 0, byteBufferWrap.getInt(), byteBufferWrap.getInt(), byteBufferWrap.getInt());
        log("onPdpStatusInd " + this.mImsRegPdpInfo);
    }

    public MiuiImsRegRecoveryBase getMiuiImsRegRecovery() {
        return this.mMiuiImsRegRecovery;
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    public String recoveryActionToStr(int i) {
        if (i == 4) {
            return "RECOVER_STATE_FOR_MBN";
        }
        if (i == 5) {
            return "RECOVER_STATE_FOR_MBN_CHECK";
        }
        return super.recoveryActionToStr(i);
    }

    @Override // com.android.phone.ims.MiuiImsRegControllerBase
    protected void log(String str) {
        Rlog.d(LOG_TAG + "[" + this.mImsPhone.getPhoneId() + "]", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSupportRecoveryAction() {
        return (isDuringCall() || this.mImsPhone.isImsRegistered()) ? false : true;
    }

    private void checkRecoveryResult(int i, int i2) {
        sendMessageDelayed(obtainMessage(4, i, i2, this.mProcessImsReasonInfo), MiuiImsRegControllerBase.TIMEOUT_RE_REGISTER_RESULT_CHECK);
    }
}
