package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.performance.PreferenceUtils;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import miui.os.Build;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class CallOptimizeManager extends CallOptimizeBase {
    private static Context mContext;
    public static CallOptHandler mHandler;
    public final Set MODME_CALL_OPTIMIZATION_MCC_SET;
    private MtkCallOptimzerAction callopt;
    private CallMessageOpt mCallMessageOpt;
    private boolean mCloudGlobalCallOptOn;
    private boolean mCloudGlobalVoNrOptOn;
    private final BroadcastReceiver mDeviceStateReceiver;
    private boolean[] mIsCurrentCall;
    private boolean mIsInitVoNrMode;
    private boolean[] mIsNeedSetVonr;
    private boolean mIsSimLoaded;
    private String mLastGci;
    private boolean[] mPendingVonrEnable;
    private boolean[] mReceiveCallModemOptimizeUrc;
    private boolean[] mSetCallModemOptimize;
    private TelephonyCallBack mTelephonyCallBack;
    public TelephonyManager mTelephonyManager;

    CallOptimizeManager() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mSetCallModemOptimize = new boolean[i];
        this.mReceiveCallModemOptimizeUrc = new boolean[i];
        this.MODME_CALL_OPTIMIZATION_MCC_SET = new HashSet(Arrays.asList("234", "214", "262", "208", "222", "260", "460"));
        this.mTelephonyManager = null;
        this.mTelephonyCallBack = null;
        this.mIsInitVoNrMode = false;
        this.mCloudGlobalCallOptOn = true;
        this.mCloudGlobalVoNrOptOn = true;
        this.mIsSimLoaded = false;
        this.mIsCurrentCall = new boolean[i];
        this.mIsNeedSetVonr = new boolean[i];
        this.mPendingVonrEnable = new boolean[i];
        this.mLastGci = null;
        this.mDeviceStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.CallOptimizeManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String stringExtra;
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    CallOptimizeManager.mHandler.obtainMessage(102, intent).sendToTarget();
                    return;
                }
                if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    CallOptHandler callOptHandler = CallOptimizeManager.mHandler;
                    callOptHandler.sendMessageDelayed(callOptHandler.obtainMessage(103), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    return;
                }
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    Settings.Global.getInt(CallOptimizeManager.mContext.getContentResolver(), "airplane_mode_on", 0);
                    return;
                }
                if (!"CallOptimizeManager.intent.debugall".equals(action) || (stringExtra = intent.getStringExtra("operation")) == null) {
                    return;
                }
                String[] strArrSplit = stringExtra.split("\\s*,\\s*");
                CallOptimizeManager.this.log(" data: " + Arrays.toString(strArrSplit));
                if (CallOptimizeManager.this.callopt != null) {
                    CallOptimizeManager.this.callopt.processVonrCallOpt(strArrSplit);
                }
            }
        };
        log("CallOptimizeManager init...");
        mContext = PhoneGlobals.getInstance();
        super.initCommon();
        this.mTelephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        HandlerThread handlerThread = new HandlerThread("MtkCallOptimizeManager");
        handlerThread.start();
        mHandler = new CallOptHandler(handlerThread.getLooper());
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.CallOptimizeManager$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                CallOptimizeManager.$r8$lambda$o_L6irAXR_a9BJVAJn7SNU9PyRs(baseEvent);
            }
        });
        registerTelephonyCallBack();
        this.callopt = new MtkCallOptimzerAction(this);
        initRegState();
        if (MiuiPhoneUtils.isSupportCallMessageOptimization() && this.mCloudCallMessageVonrOn) {
            CallMessageOpt callMessageOpt = new CallMessageOpt();
            this.mCallMessageOpt = callMessageOpt;
            callMessageOpt.initSimStateForVonrOpt(6);
        }
    }

    public static /* synthetic */ void $r8$lambda$o_L6irAXR_a9BJVAJn7SNU9PyRs(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            mHandler.sendEmptyMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                CallOptimizeManager.this.log("onPreciseCallStateChanged call state : " + i2);
                CallOptimizeManager.mHandler.removeMessages(100);
                CallOptHandler callOptHandler = CallOptimizeManager.mHandler;
                callOptHandler.sendMessageDelayed(callOptHandler.obtainMessage(100, Integer.valueOf(i)), 500L);
            }
        }
    }

    public static CallOptimizeBase make() {
        if (CallOptimizeBase.sInstance == null && (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization() || MiuiPhoneUtils.isSupportCallMessageOptimization())) {
            CallOptimizeBase.sInstance = new CallOptimizeManager();
        }
        return CallOptimizeBase.sInstance;
    }

    private void initRegState() {
        PreferenceUtils preferenceUtils = PreferenceUtils.getInstance();
        Boolean bool = Boolean.TRUE;
        this.mCloudGlobalCallOptOn = preferenceUtils.getBooleanPreference("mtkGlobalCallOptEnable", bool).booleanValue();
        boolean zBooleanValue = PreferenceUtils.getInstance().getBooleanPreference("mtkGlobalVoNrEnable", bool).booleanValue();
        this.mCloudGlobalVoNrOptOn = zBooleanValue;
        if (this.mCloudGlobalCallOptOn || zBooleanValue || this.mCloudCallMessageVonrOn) {
            registerDeviceState();
            registerAtUrc();
            if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
                initModemModel();
            }
        }
        if (this.mCloudGlobalVoNrOptOn) {
            initModemVoNrModel(true, "cloud");
        }
    }

    public class CallOptHandler extends Handler {
        public CallOptHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            CallOptimizeManager.this.log("CallOptHandler handleMessage what=" + message.what);
            try {
                int i = message.what;
                if (i == 104) {
                    CallOptimizeManager.this.updateCloudState();
                    return;
                }
                if (i == 105) {
                    if (CallOptimizeManager.this.shouldDoCallVonrOpt() && CallOptimizeManager.this.mCallMessageOpt != null && CallOptimizeManager.this.mCloudCallMessageVonrOn) {
                        CallOptimizeManager.this.mCallMessageOpt.initVonrOptState();
                        return;
                    }
                    if (CallOptimizeManager.this.mCloudGlobalVoNrOptOn || message.what == 105) {
                        int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
                        CallOptimizeManager.this.log("insertSimCount = " + insertedSimCount);
                        if (insertedSimCount == 1) {
                            int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                            if (CallOptimizeManager.this.isDisableVoNrByOpt(SubscriptionManager.getDefault().getSubscriptionIdForSlot(activeDataPhoneId))) {
                                CallOptimizeManager.this.setVoNrState(activeDataPhoneId, true);
                                return;
                            }
                            return;
                        }
                        if (insertedSimCount == 2) {
                            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(0);
                            int subscriptionIdForSlot2 = SubscriptionManager.getDefault().getSubscriptionIdForSlot(1);
                            if (CallOptimizeManager.this.isDisableVoNrByOpt(subscriptionIdForSlot)) {
                                CallOptimizeManager.this.setVoNrState(0, true);
                            }
                            if (CallOptimizeManager.this.isDisableVoNrByOpt(subscriptionIdForSlot2)) {
                                CallOptimizeManager.this.setVoNrState(1, true);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (i == 200) {
                    Bundle data = message.getData();
                    int i2 = data.getInt("SUB_ID_KEY", -1);
                    String string = data.getString("GCI_KEY");
                    if (CallOptimizeManager.this.mCallMessageOpt != null) {
                        CallOptimizeManager.this.mCallMessageOpt.updateRseidentVonrState(i2, string);
                        return;
                    }
                    return;
                }
                if (i == 300) {
                    Bundle data2 = message.getData();
                    if (data2 == null) {
                        return;
                    }
                    int subscriptionIdForSlot3 = SubscriptionManager.getDefault().getSubscriptionIdForSlot(data2.getInt("SUB_ID_KEY", -1));
                    int i3 = data2.getInt("RAT_KEY", 0);
                    int i4 = data2.getInt("FREQ_KEY", 0);
                    int i5 = data2.getInt("PCI_KEY", 0);
                    data2.getString("GCI_KEY");
                    if (CallOptimizeManager.this.callopt != null) {
                        MtkCallOptimzerAction.ActionContext actionContext = new MtkCallOptimzerAction.ActionContext();
                        actionContext.phoneId = subscriptionIdForSlot3;
                        actionContext.event_id = 100;
                        actionContext.scene = 3;
                        actionContext.rat = i3;
                        actionContext.bar_cell_num = 1;
                        actionContext.freq = i4;
                        actionContext.pci = i5;
                        CallOptimizeManager.this.callopt.performActionOnCurrentCell(actionContext);
                        return;
                    }
                    return;
                }
                if (i != 301) {
                    switch (i) {
                        case 100:
                            if (CallOptimizeManager.this.mCloudGlobalCallOptOn) {
                                CallOptimizeManager.this.log("EVENT_PRECISE_CALL_STATE_CHANGED");
                                CallOptimizeManager.this.processPreciseCallStateChanged(message.arg1);
                                return;
                            }
                            return;
                        case 101:
                            try {
                                CallOptimizeManager.this.onAtUrcInd(message.obj);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        case 102:
                            Intent intent = (Intent) message.obj;
                            int slotIdExtra = SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID);
                            String stringExtra = intent.getStringExtra("ss");
                            int subscriptionIdForSlot4 = SubscriptionManager.getDefault().getSubscriptionIdForSlot(slotIdExtra);
                            CallOptimizeManager.this.log("EVENT_SIM_STATE_CHANGED slotId=" + slotIdExtra + " , subId=" + subscriptionIdForSlot4 + " , simState=" + stringExtra + " isDisableVoNrByOpt = " + CallOptimizeManager.this.isDisableVoNrByOpt(subscriptionIdForSlot4));
                            if (MiuiPhoneUtils.isSupportCallMessageOptimization() && CallOptimizeManager.this.mCloudCallMessageVonrOn && "ABSENT".equals(stringExtra)) {
                                int insertedSimCount2 = MiuiPhoneUtils.getInsertedSimCount();
                                CallOptimizeManager.this.log("insertSimCount: " + insertedSimCount2);
                                if (insertedSimCount2 == 0 && CallOptimizeManager.this.mCallMessageOpt != null && CallOptimizeManager.this.callopt != null) {
                                    CallOptimizeManager.this.mCallMessageOpt.initSimStateForVonrOpt(5);
                                }
                            }
                            if ("LOADED".equals(stringExtra)) {
                                CallOptimizeManager.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.CallOptimizeManager.CallOptHandler.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        CallOptimizeManager.this.initModemVoNrModel(true, "simLoaded");
                                        CallOptimizeManager.mHandler.sendEmptyMessageDelayed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, 1000L);
                                    }
                                }, 3000L);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                    e.printStackTrace();
                }
                if (CallOptimizeManager.this.callopt != null) {
                    if (CallOptimizeManager.this.callopt.isResidentCell()) {
                        CallOptHandler callOptHandler = CallOptimizeManager.mHandler;
                        callOptHandler.sendMessageDelayed(callOptHandler.obtainMessage(MiRilHook.HOOK_ID_STOP_URC_BY_AT), 1800000L);
                        return;
                    }
                    CallOptimizeManager.this.callopt.reSetBaredCells();
                    MtkCallOptimzerAction.ActionContext actionContext2 = new MtkCallOptimzerAction.ActionContext();
                    actionContext2.phoneId = 0;
                    actionContext2.event_id = 100;
                    actionContext2.scene = 2;
                    CallOptimizeManager.this.callopt.performActionOnCurrentCell(actionContext2);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void registerAtUrc() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                Phone phone = PhoneFactory.getPhone(i);
                if (phone != null) {
                    phone.registerForOemIndication(mHandler, 101, phone);
                }
            }
        }
    }

    private void initModemModel() {
        int defaultVoiceSlotId = ModemProvider.getInstance().getDefaultVoiceSlotId();
        if (defaultVoiceSlotId < 0) {
            defaultVoiceSlotId = MiuiPhoneUtils.getActiveDataPhoneId();
        }
        if (defaultVoiceSlotId >= 0) {
            FiveGManagerBase.getInstance().sendAtCommand(defaultVoiceSlotId, new String[]{"AT+EDMFAPP=20007,1", "+EDMFAPP"}, (Message) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initModemVoNrModel(boolean z, String str) {
        String str2;
        if ((this.mCloudGlobalVoNrOptOn || "cloud".equalsIgnoreCase(str)) && isModemVonrOptimizeOperator()) {
            boolean z2 = this.mIsInitVoNrMode;
            if (!z2 && z) {
                this.mIsInitVoNrMode = true;
                str2 = "AT+EDMFAPP=20007,12";
            } else {
                if (!z2 || z) {
                    return;
                }
                this.mIsInitVoNrMode = false;
                str2 = "AT+EDMFAPP=20007,13";
            }
            int defaultVoiceSlotId = ModemProvider.getInstance().getDefaultVoiceSlotId();
            if (defaultVoiceSlotId < 0) {
                defaultVoiceSlotId = MiuiPhoneUtils.getActiveDataPhoneId();
            }
            log(" initModemVoNrModel, phoneId = " + defaultVoiceSlotId + " , cmd = " + str2 + " , isInit = " + z + " , reason = " + str);
            if (defaultVoiceSlotId >= 0) {
                FiveGManagerBase.getInstance().sendAtCommand(defaultVoiceSlotId, new String[]{str2, "+EDMFAPP"}, (Message) null);
            }
        }
    }

    private void registerDeviceState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("CallOptimizeManager.intent.debugall");
        mContext.registerReceiver(this.mDeviceStateReceiver, intentFilter, 2);
    }

    void setVoNrState(final int i, final boolean z) {
        log(" setVoNrState phoneId = " + i + " , enable = " + z);
        if (i < 0 || i >= MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        final int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
            if (MiuiPhoneUtils.isPhoneInCall()) {
                log(" delay setVoNrState subId = " + subscriptionIdForSlot + " is inCall");
                mHandler.postDelayed(new Runnable() { // from class: com.android.phone.CallOptimizeManager.2
                    @Override // java.lang.Runnable
                    public void run() {
                        boolean zIsPhoneInCall = MiuiPhoneUtils.isPhoneInCall();
                        CallOptimizeManager callOptimizeManager = CallOptimizeManager.this;
                        if (zIsPhoneInCall) {
                            callOptimizeManager.log("setVoNrState failed because of in call.");
                            if (CallOptimizeManager.this.shouldDoCallVonrOpt()) {
                                CallOptimizeManager.this.mIsNeedSetVonr[i] = true;
                                CallOptimizeManager.this.mPendingVonrEnable[i] = z;
                                return;
                            }
                            return;
                        }
                        callOptimizeManager.setUserVoNrEnabled(i, z, subscriptionIdForSlot);
                    }
                }, 2000L);
                return;
            }
            setUserVoNrEnabled(i, z, subscriptionIdForSlot);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserVoNrEnabled(final int i, final boolean z, final int i2) {
        if (i < 0 || i >= MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        FiveGManagerBase.getInstance().setUserVoNREnabled(i, z);
        mHandler.postDelayed(new Runnable() { // from class: com.android.phone.CallOptimizeManager.3
            @Override // java.lang.Runnable
            public void run() {
                boolean zIsVoNREnabled = FiveGManagerBase.getInstance().isVoNREnabled(i2);
                boolean z2 = z == zIsVoNREnabled;
                String networkOperatorForPhone = CallOptimizeManager.this.mTelephonyManager.getNetworkOperatorForPhone(i);
                CallOptimizeManager.this.log("setUserVoNrEnabled, enable=" + z + ", isVoNrEnable = " + zIsVoNREnabled + " , isSetVoNrSuccess = " + z2);
                if (z2) {
                    boolean z3 = z;
                    CallOptimizeManager callOptimizeManager = CallOptimizeManager.this;
                    if (z3) {
                        callOptimizeManager.removeDisableVoNrSubId(i2);
                        if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
                            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{"AT+EDMFAPP=20007,101", "+EDMFAPP"}, (Message) null);
                            CallOptimizeManager.this.recordCount("vonr_ap_rvy_cnt", Integer.toString(i), networkOperatorForPhone);
                            CallOptimizeManager.this.recordStatus("is_limit_vonr", Integer.toString(i), "false");
                        }
                    } else {
                        callOptimizeManager.setDisableVoNrSubId(i2);
                        if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
                            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{"AT+EDMFAPP=20007,100", "+EDMFAPP"}, (Message) null);
                            CallOptimizeManager.this.recordCount("vonr_ap_limit_cnt", Integer.toString(i), networkOperatorForPhone);
                            CallOptimizeManager.this.recordStatus("is_limit_vonr", Integer.toString(i), "true");
                        }
                    }
                    if (!CallOptimizeManager.this.shouldDoCallVonrOpt() || CallOptimizeManager.this.callopt == null) {
                        return;
                    }
                    MtkCallOptimzerAction.ActionContext actionContext = new MtkCallOptimzerAction.ActionContext();
                    actionContext.phoneId = i;
                    actionContext.event_id = z ? 102 : 101;
                    CallOptimizeManager.this.callopt.performActionOnCurrentCell(actionContext);
                    CallOptimizeManager.this.log("set vonr finish getDisableVoNrSubIdList : " + CallOptimizeManager.this.getDisableVoNrSubIdList());
                    return;
                }
                CallOptimizeManager.this.log("setUserVoNrEnabled failed.");
            }
        }, 2000L);
    }

    public String getDisableVoNrSubIdList() {
        try {
            String stringPreference = PreferenceUtils.getInstance().getStringPreference("disable_vonr_sub_id");
            log(" getDisableVoNrSubIdList subIdList = " + stringPreference);
            return stringPreference;
        } catch (Exception e) {
            log("Error occurred while getting the disable VoNR subId list: " + e.getMessage());
            return "";
        }
    }

    public void setDisableVoNrSubId(int i) {
        String disableVoNrSubIdList = getDisableVoNrSubIdList();
        log(" setDisableVoNrSubId subId = " + i + " , subIdList = " + disableVoNrSubIdList);
        if (disableVoNrSubIdList == null || disableVoNrSubIdList.isEmpty()) {
            PreferenceUtils.getInstance().setStringPreference("disable_vonr_sub_id", String.valueOf(i));
            return;
        }
        PreferenceUtils.getInstance().setStringPreference("disable_vonr_sub_id", disableVoNrSubIdList + "," + String.valueOf(i));
    }

    public void removeDisableVoNrSubId(int i) {
        String disableVoNrSubIdList = getDisableVoNrSubIdList();
        log("removeDisableVoNrSubId subId = " + i + ", subIdList = " + disableVoNrSubIdList);
        if (disableVoNrSubIdList == null || disableVoNrSubIdList.isEmpty()) {
            return;
        }
        final String strValueOf = String.valueOf(i);
        String strJoin = String.join(",", (List) Arrays.stream(disableVoNrSubIdList.split(",")).filter(new Predicate() { // from class: com.android.phone.CallOptimizeManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CallOptimizeManager.m154$r8$lambda$wPzWna02RDzE9vzagfBvm_t2hs(strValueOf, (String) obj);
            }
        }).collect(Collectors.toList()));
        log("removeDisableVoNrSubId result = " + strJoin);
        PreferenceUtils.getInstance().setStringPreference("disable_vonr_sub_id", strJoin);
    }

    /* JADX INFO: renamed from: $r8$lambda$wPzWna02RDzE9vzagfB-vm_t2hs, reason: not valid java name */
    public static /* synthetic */ boolean m154$r8$lambda$wPzWna02RDzE9vzagfBvm_t2hs(String str, String str2) {
        return !str2.equals(str);
    }

    @Override // com.android.phone.CallOptimizeBase
    public boolean isDisableVoNrByOpt(int i) {
        String disableVoNrSubIdList = getDisableVoNrSubIdList();
        log(" isDisableVoNrByOpt subId = " + i + ", subIdList = " + disableVoNrSubIdList);
        final String strValueOf = String.valueOf(i);
        return (disableVoNrSubIdList == null || disableVoNrSubIdList.isEmpty() || !Arrays.stream(disableVoNrSubIdList.split(",")).anyMatch(new Predicate() { // from class: com.android.phone.CallOptimizeManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((String) obj).equals(strValueOf);
            }
        })) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPreciseCallStateChanged(int i) {
        ImsPhoneCall foregroundCall;
        MtkCallOptimzerAction mtkCallOptimzerAction;
        ImsPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log(" processPreciseCallStateChanged, phone == null !");
            return;
        }
        String networkOperatorForPhone = this.mTelephonyManager.getNetworkOperatorForPhone(i);
        boolean zIsPhoneInImsCall = MiuiPhoneUtils.isPhoneInImsCall(i);
        if (!(phone instanceof ImsPhone) || (foregroundCall = phone.getForegroundCall()) == null) {
            return;
        }
        Call.State state = foregroundCall.getState();
        if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization() && (mtkCallOptimzerAction = this.callopt) != null && mtkCallOptimzerAction.isModemCallOptimizeOperator()) {
            log("processPreciseCallStateChanged, state:" + state + " mSetCallModemOptimize:" + this.mSetCallModemOptimize[i] + " isCallActive: " + zIsPhoneInImsCall + " mReceiveCallModemOptimizeUrc: " + this.mReceiveCallModemOptimizeUrc[i]);
            if (state == Call.State.ACTIVE && !this.mSetCallModemOptimize[i]) {
                this.callopt.setModemCallOptimize(i, "ACTIVE");
                this.mSetCallModemOptimize[i] = true;
                this.mIsCurrentCall[i] = true;
                recordCount("call_count", Integer.toString(i), networkOperatorForPhone);
            } else if ((state == Call.State.DISCONNECTING || state == Call.State.IDLE) && this.mSetCallModemOptimize[i]) {
                this.callopt.setModemCallOptimize(i, "DISCONNECTING");
                if (this.mReceiveCallModemOptimizeUrc[i]) {
                    startOrStopModemCallOptimize(i, "stop");
                    this.mReceiveCallModemOptimizeUrc[i] = false;
                    this.mIsCurrentCall[i] = false;
                }
                this.mSetCallModemOptimize[i] = false;
            }
        }
        if (shouldDoCallVonrOpt()) {
            log("processPreciseCallStateChanged, state:" + state + " mIsNeedSetVonr:" + this.mIsNeedSetVonr[i] + " mPendingVonrEnable: " + this.mPendingVonrEnable[i]);
            if (state == Call.State.IDLE && this.mIsNeedSetVonr[i]) {
                setUserVoNrEnabled(i, this.mPendingVonrEnable[i], SubscriptionManager.getDefault().getSubscriptionIdForSlot(i));
                this.mIsNeedSetVonr[i] = false;
                this.mPendingVonrEnable[i] = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAtUrcInd(Object obj) {
        MtkCallOptimzerAction mtkCallOptimzerAction;
        MtkCallOptimzerAction mtkCallOptimzerAction2;
        AsyncResult asyncResult = (AsyncResult) obj;
        if (asyncResult.exception != null) {
            return;
        }
        int phoneId = ((Phone) asyncResult.userObj).getPhoneId();
        String networkOperatorForPhone = this.mTelephonyManager.getNetworkOperatorForPhone(phoneId);
        String str = (String) asyncResult.result;
        log(phoneId + " onAtUrcInd:" + str);
        if (str.startsWith("+EDMFAPP:")) {
            String[] strArrSplit = str.split(",");
            String strTrim = strArrSplit[0].split(":")[1].trim();
            if ("20007".equals(strTrim)) {
                if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
                    if (strArrSplit.length < 4) {
                        log("onAtUrcInd parse data length < 4");
                        return;
                    }
                    try {
                        int i = Integer.parseInt(strArrSplit[1]);
                        int i2 = Integer.parseInt(strArrSplit[2]);
                        log("eventType is " + i + ", result is " + i2);
                        if (i == 0 && i2 == 500 && (mtkCallOptimzerAction2 = this.callopt) != null && mtkCallOptimzerAction2.isModemCallOptimizeOperator() && this.mCloudGlobalCallOptOn) {
                            if (!this.mReceiveCallModemOptimizeUrc[phoneId]) {
                                recordCount("call_quality_bad_count", Integer.toString(phoneId), networkOperatorForPhone);
                            }
                            startOrStopModemCallOptimize(phoneId, "start");
                        }
                        if (i == 0 && i2 == 501 && (mtkCallOptimzerAction = this.callopt) != null && mtkCallOptimzerAction.isModemCallOptimizeOperator() && this.mCloudGlobalCallOptOn) {
                            boolean[] zArr = this.mIsCurrentCall;
                            if (zArr[phoneId]) {
                                zArr[phoneId] = false;
                                recordCount("call_change_cell_count", Integer.toString(phoneId), networkOperatorForPhone);
                            }
                        }
                        if (i == 0 && i2 == 510 && isModemVonrOptimizeOperator() && this.mCloudGlobalVoNrOptOn) {
                            int i3 = Integer.parseInt(strArrSplit[3]);
                            setVoNrState(phoneId, i3 == 1);
                            if (i3 == 0) {
                                recordCount("vonr_modem_limit_cnt", Integer.toString(phoneId), networkOperatorForPhone);
                                return;
                            } else {
                                if (i3 == 1) {
                                    recordCount("vonr_modem_rvy_cnt", Integer.toString(phoneId), networkOperatorForPhone);
                                    return;
                                }
                                return;
                            }
                        }
                        return;
                    } catch (NumberFormatException e) {
                        Log.e("MtkCallOptimizeManager", "NumberFormatException: " + e);
                        return;
                    }
                }
                return;
            }
            if ("20109".equals(strTrim)) {
                try {
                    MtkCallOptimzerAction mtkCallOptimzerAction3 = this.callopt;
                    if (mtkCallOptimzerAction3 != null) {
                        mtkCallOptimzerAction3.processVonrCallOpt(strArrSplit);
                    }
                } catch (Exception e2) {
                    Log.e("MtkCallOptimizeManager", "Exception: " + e2);
                }
            }
        }
    }

    public void startOrStopModemCallOptimize(int i, String str) {
        String[] callStallCmds;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (this.callopt == null) {
            return;
        }
        boolean zEqualsIgnoreCase = "start".equalsIgnoreCase(str);
        MtkCallOptimzerAction mtkCallOptimzerAction = this.callopt;
        if (zEqualsIgnoreCase) {
            callStallCmds = mtkCallOptimzerAction.getCallStallCmds(1);
        } else {
            callStallCmds = mtkCallOptimzerAction.getCallStallCmds(2);
        }
        if (("start".equalsIgnoreCase(str) && this.mReceiveCallModemOptimizeUrc[i]) || phone == null || callStallCmds == null) {
            return;
        }
        for (String str2 : callStallCmds) {
            String str3 = "MIPC_SET_DATA_STALL_OPT," + str2;
            log("phone startOrStopModemCallOptimize is str ".concat(str3));
            FiveGManagerBase.getInstance().sendAtCommand(i, str3.split(","), (Message) null);
            if ("start".equalsIgnoreCase(str)) {
                this.mReceiveCallModemOptimizeUrc[i] = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudState() {
        boolean z;
        if (!MiuiPhoneUtils.isSupportMtkGlobalCallOptimization() && !MiuiPhoneUtils.isSupportCallMessageOptimization()) {
            log("updateCloudState is not support");
            return;
        }
        FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
        if (fiveGCloudController == null) {
            log("updateCloudState return for FiveGCloud is no Instantiation.");
            return;
        }
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> listFetchSelfStudyCloudStatus = fiveGCloudController.fetchSelfStudyCloudStatus();
            if (listFetchSelfStudyCloudStatus != null && !listFetchSelfStudyCloudStatus.isEmpty()) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : listFetchSelfStudyCloudStatus) {
                    String string = cloudData.getString("DataMode", "NA");
                    log("updateCloudState cloudDataMode = " + string);
                    if ("mtkGlobalCallOptStatus".equalsIgnoreCase(string)) {
                        int i = cloudData.getInt("mtkGlobalCallOptStatus", 1);
                        z = i != 0;
                        log("updateCloudState globalCallOptStatus = " + i + " , callOptCloudStatus = " + z);
                        if (z != this.mCloudGlobalCallOptOn) {
                            this.mCloudGlobalCallOptOn = z;
                            PreferenceUtils.getInstance().setBooleanPreference("mtkGlobalCallOptEnable", this.mCloudGlobalCallOptOn);
                        }
                    } else if ("mtkGlobalVoNrOptStatus".equalsIgnoreCase(string)) {
                        int i2 = cloudData.getInt("mtkGlobalVoNrOptStatus", 1);
                        z = i2 != 0;
                        log("updateCloudState globalVoNrOptStatus = " + i2 + " , voNrCloudStatus = " + z);
                        if (z != this.mCloudGlobalVoNrOptOn) {
                            this.mCloudGlobalVoNrOptOn = z;
                            initModemVoNrModel(z, "cloud");
                            if (!this.mCloudGlobalVoNrOptOn) {
                                mHandler.sendEmptyMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                            }
                            PreferenceUtils.getInstance().setBooleanPreference("mtkGlobalVoNrEnable", this.mCloudGlobalVoNrOptOn);
                        }
                    } else if ("callMessageOptStatus".equalsIgnoreCase(string)) {
                        int i3 = cloudData.getInt("callMessageOptStatus", 1);
                        z = i3 != 0;
                        if (z != this.mCloudCallMessageVonrOn) {
                            this.mCloudCallMessageVonrOn = z;
                            log("updateCloudState callMessageOptStatus = " + i3 + " , callMessageCloudStatus = " + z + " ,mCloudCallMessageVonrOn = " + this.mCloudCallMessageVonrOn);
                        }
                    }
                }
                return;
            }
            log("updateCloudState return for dataList is isEmpty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.phone.CallOptimizeBase
    public boolean isModemVonrOptimizeOperator() {
        MtkCallOptimzerAction mtkCallOptimzerAction;
        log("isModemVonrOptimizeOperator");
        if (!MiuiPhoneUtils.isSupportMtkGlobalCallOptimization() || (mtkCallOptimzerAction = this.callopt) == null) {
            return false;
        }
        String countryMcc = mtkCallOptimzerAction.getCountryMcc();
        if (TextUtils.isEmpty(countryMcc)) {
            return false;
        }
        return this.MODME_CALL_OPTIMIZATION_MCC_SET.contains(countryMcc);
    }

    public void processServiceStateChange(int i, ServiceState serviceState) {
        boolean z = serviceState.getDataRegState() == 1;
        boolean zHasMessages = mHandler.hasMessages(MiRilHook.HOOK_ID_START_URC_BY_AT);
        boolean z2 = this.callopt != null;
        if (MiuiPhoneUtils.isSupportCallMessageOptimization() && this.mCloudCallMessageVonrOn && z && z2 && zHasMessages) {
            log("has EVENT_BAR_RESIDENT_CELL_FOR_CALL_1HOUR");
        }
        mHandler.removeMessages(302);
        mHandler.removeMessages(MiRilHook.HOOK_ID_START_URC_BY_AT);
    }

    void processVonrOpt(String[] strArr, int i) {
        if (strArr.length < 10) {
            log("onAtUrcInd parse data length < 10");
            return;
        }
        if (!shouldDoCallVonrOpt() || this.callopt == null) {
            return;
        }
        int i2 = Integer.parseInt(strArr[1].trim());
        int subId = MiuiPhoneUtils.getPhone(i2).getSubId();
        int i3 = Integer.parseInt(strArr[3].trim());
        String strTrim = strArr[5].trim();
        String strTrim2 = strArr[6].trim();
        StringBuilder sb = new StringBuilder();
        if (strTrim2.length() == 1) {
            sb.append('0');
        }
        sb.append(strTrim2);
        String str = i3 + "_" + subId + "_" + strTrim + ((Object) sb) + "_" + Long.parseLong(strArr[9].trim());
        log("gci: ".concat(str));
        if (i != 101) {
            if (i == 102) {
                log("processCallQualityOpt eventId == 102");
                this.callopt.updateResidentCellDbForVonrOpt(str, 0, false, "vonrLimit", 0L);
                setVoNrState(i2, true);
                mHandler.removeMessages(200);
                return;
            }
            return;
        }
        if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).performActionForVonrOpt(str, i)) {
            log("processCallQualityOpt eventId == 101");
            this.callopt.updateResidentCellDbForVonrOpt(str, 1, false, "vonrLimit", 0L);
            Message messageObtainMessage = mHandler.obtainMessage(200);
            Bundle bundle = new Bundle();
            bundle.putInt("SUB_ID_KEY", subId);
            bundle.putString("GCI_KEY", str);
            messageObtainMessage.setData(bundle);
            mHandler.sendMessageDelayed(messageObtainMessage, 180000L);
            setVoNrState(i2, false);
        }
    }

    @Override // com.android.phone.CallOptimizeBase
    public void updateVonrSwitchState(int i, boolean z) {
        log("updateVonrSwitchState phoneId: " + i + " enable: " + z);
        if (SubscriptionManager.getDefault() == null) {
            log("Error: SubscriptionManager is null");
            return;
        }
        if (i < 0 || i >= MiuiPhoneUtils.PHONE_COUNT || this.callopt == null) {
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        if (!z && isDisableVoNrByOpt(subscriptionIdForSlot)) {
            removeDisableVoNrSubId(subscriptionIdForSlot);
        }
        if (shouldDoCallVonrOpt()) {
            if (!z) {
                this.callopt.updateResidentCellDbForVonrOpt("0", 1, true, "vonrLimit", 0L);
            }
            MtkCallOptimzerAction.ActionContext actionContext = new MtkCallOptimzerAction.ActionContext();
            actionContext.phoneId = i;
            actionContext.event_id = z ? MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT : 103;
            this.callopt.performActionOnCurrentCell(actionContext);
        }
    }

    class CallMessageOpt {
        CallMessageOpt() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initVonrOptState() {
            try {
                int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
                CallOptimizeManager.this.log("initVonrOptState insertSimCount = " + insertedSimCount + " mIsSimLoaded: " + CallOptimizeManager.this.mIsSimLoaded);
                if (CallOptimizeManager.this.callopt != null && !CallOptimizeManager.this.mIsSimLoaded) {
                    CallOptimizeManager.this.mIsSimLoaded = true;
                    if (insertedSimCount == 1) {
                        CallOptimizeManager.this.callopt.setSingleSimVonrOpt();
                    } else {
                        if (insertedSimCount != 2) {
                            return;
                        }
                        CallOptimizeManager.this.callopt.setDualSimVonrOpt();
                    }
                }
            } catch (Exception e) {
                CallOptimizeManager.this.log("initVonrOptState error: " + e.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRseidentVonrState(int i, String str) {
            if (CallOptimizeManager.this.callopt == null || i == -1 || str == null) {
                return;
            }
            if (CallOptimizeManager.this.callopt.isResidentCell()) {
                Message messageObtainMessage = CallOptimizeManager.mHandler.obtainMessage(200);
                Bundle bundle = new Bundle();
                bundle.putInt("SUB_ID_KEY", i);
                bundle.putString("GCI_KEY", str);
                messageObtainMessage.setData(bundle);
                CallOptimizeManager.mHandler.sendMessageDelayed(messageObtainMessage, 1800000L);
                return;
            }
            CallOptimizeManager.this.callopt.updateResidentCellDbForVonrOpt(str, 0, false, "vonrLimit", 0L);
            int phoneId = android.telephony.SubscriptionManager.getPhoneId(i);
            MtkCallOptimzerAction.ActionContext actionContext = new MtkCallOptimzerAction.ActionContext();
            actionContext.phoneId = phoneId;
            actionContext.event_id = 102;
            CallOptimizeManager.this.callopt.performActionOnCurrentCell(actionContext);
            CallOptimizeManager.this.removeDisableVoNrSubId(i);
            CallOptimizeManager.mHandler.removeMessages(200);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initSimStateForVonrOpt(int i) {
            CallOptimizeManager.this.log("initSimStateForVonrOpt");
            if (MiuiPhoneUtils.isSupportCallMessageOptimization() && CallOptimizeManager.this.mCloudCallMessageVonrOn && CallOptimizeManager.this.callopt != null) {
                CallOptimizeManager.this.mIsSimLoaded = false;
                MtkCallOptimzerAction.ActionContext actionContext = new MtkCallOptimzerAction.ActionContext();
                actionContext.event_id = MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT;
                actionContext.scene = i;
                CallOptimizeManager.this.callopt.performActionOnCurrentCell(actionContext);
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    CallOptimizeManager.this.mIsNeedSetVonr[i2] = false;
                }
            }
        }
    }

    @Override // com.android.phone.CallOptimizeBase
    protected void log(String str) {
        Rlog.d("MtkCallOptimizeManager", str);
    }
}
