package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class MtkPingPongOptimizer {
    private static MtkPingPongOptimizer mInstance;
    private boolean isO12Device;
    private final Context mContext;
    private final BroadcastReceiver mDeviceStateReceiver;
    private final BaseEvent.IEventListener mDisplayStateListener;
    private final BaseEvent.IEventListener mPingPongCloudChangeListener;
    private PowerSaveModeListener mPowerSaveModeListener;
    private final Handler mWorkHandler;
    private boolean mPingPongOptStatus = false;
    private boolean mPowerSaveStatus = false;
    private boolean mScreenStatus = false;
    private HashSet mOptimizedActionSet = new HashSet();

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePingPongOptByCloud() {
        int mtkPingPongCloudStatus = FiveGCloudController.getMtkPingPongCloudStatus();
        if (mtkPingPongCloudStatus == -1) {
            return;
        }
        boolean z = mtkPingPongCloudStatus == 1;
        if (this.mPingPongOptStatus != z) {
            log("ping pong opt change to ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
            if (z) {
                sendAtCmd(0, AtCmd.HOOK_ID_START_REG, null);
                sendAtCmd(0, AtCmd.ENABLE_PING_PONG_DMF, null);
                registerDeviceState();
            } else {
                tryRecoverAction();
                sendAtCmd(0, AtCmd.DISABLE_PING_PONG_DMF, null);
                sendAtCmd(0, AtCmd.HOOK_ID_STOP_REG, null);
                unregisterDeviceState();
            }
            this.mPingPongOptStatus = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryRecoverAction() {
        try {
            this.mOptimizedActionSet = (HashSet) ObjectSerializer.deserialize(PreferenceUtils.getInstance().getStringPreference("MtkPingPongOptimizer.SbpAction"));
            log("tryRecoverAction actionSet=" + this.mOptimizedActionSet);
            HashSet hashSet = this.mOptimizedActionSet;
            if (hashSet != null && !hashSet.isEmpty()) {
                Iterator it = this.mOptimizedActionSet.iterator();
                while (it.hasNext()) {
                    String[] strArrSplit = ((String) it.next()).split("_");
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        recoverAction(new int[]{i, Integer.parseInt(strArrSplit[1]), Integer.parseInt(strArrSplit[2])});
                    }
                }
                this.mOptimizedActionSet.clear();
                recordOptimizedAction();
            } else if (this.isO12Device) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    forceRecover(i2);
                }
            }
            if (this.mOptimizedActionSet == null) {
                this.mOptimizedActionSet = new HashSet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAtUrcInd(Object obj) {
        AsyncResult asyncResult = (AsyncResult) obj;
        if (asyncResult.exception != null) {
            return;
        }
        int phoneId = ((Phone) asyncResult.userObj).getPhoneId();
        String str = (String) asyncResult.result;
        log(phoneId + " onAtUrcInd:" + str);
        if (str.startsWith("+EDMFAPP:")) {
            String[] strArrSplit = str.split(",");
            if ("20105".equals(strArrSplit[0].split(":")[1].trim())) {
                for (int i = 0; i < strArrSplit.length; i++) {
                    strArrSplit[i] = strArrSplit[i].trim();
                }
                int i2 = Integer.parseInt(strArrSplit[1]);
                if (i2 == 333) {
                    takeAction(strArrSplit);
                } else if (i2 == 334) {
                    doRecoverAction(strArrSplit);
                }
            }
        }
    }

    enum AtCmd {
        DISABLE_PING_PONG_DMF("AT+EDMFAPP=20105,100", "+EDMFAPP"),
        ENABLE_PING_PONG_DMF("AT+EDMFAPP=20105,101", "+EDMFAPP"),
        SCREEN_OFF("AT+EDMFAPP=20105,102", "+EDMFAPP"),
        SCREEN_ON("AT+EDMFAPP=20105,103", "+EDMFAPP"),
        POWER_SAVE_ON("AT+EDMFAPP=20105,104", "+EDMFAPP"),
        POWER_SAVE_OFF("AT+EDMFAPP=20105,105", "+EDMFAPP"),
        HOOK_ID_START_REG("AT+EDMFAPP=20105,0", "+EDMFAPP"),
        HOOK_ID_STOP_REG("AT+EDMFAPP=20105,1", "+EDMFAPP");

        String mAtCmd0;
        String mAtCmd1;

        AtCmd(String str, String str2) {
            this.mAtCmd0 = str;
            this.mAtCmd1 = str2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String[] getAtCmd() {
            return new String[]{this.mAtCmd0, this.mAtCmd1};
        }
    }

    public static MtkPingPongOptimizer make() {
        if (mInstance == null && MiuiPhoneUtils.isMtkPingPongOptSupported()) {
            mInstance = new MtkPingPongOptimizer();
        }
        return mInstance;
    }

    private MtkPingPongOptimizer() {
        this.isO12Device = false;
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.MtkPingPongOptimizer.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1000) {
                    switch (i) {
                        case 100:
                            MtkPingPongOptimizer.this.initAction();
                            break;
                        case 101:
                            try {
                                MtkPingPongOptimizer.this.onAtUrcInd(message.obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            break;
                        case 102:
                            MtkPingPongOptimizer.this.updatePingPongOptByCloud();
                            break;
                        case 103:
                            boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                            if (MtkPingPongOptimizer.this.mScreenStatus != zBooleanValue) {
                                MtkPingPongOptimizer.this.mScreenStatus = zBooleanValue;
                                boolean z = MtkPingPongOptimizer.this.mScreenStatus;
                                MtkPingPongOptimizer mtkPingPongOptimizer = MtkPingPongOptimizer.this;
                                if (z) {
                                    mtkPingPongOptimizer.sendAtCmd(0, AtCmd.SCREEN_ON, null);
                                } else {
                                    mtkPingPongOptimizer.sendAtCmd(0, AtCmd.SCREEN_OFF, null);
                                }
                            }
                            break;
                        case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                            boolean z2 = ((Bundle) message.obj).getBoolean("PowerSaveMode");
                            if (MtkPingPongOptimizer.this.mPowerSaveStatus != z2) {
                                MtkPingPongOptimizer.this.mPowerSaveStatus = z2;
                                boolean z3 = MtkPingPongOptimizer.this.mPowerSaveStatus;
                                MtkPingPongOptimizer mtkPingPongOptimizer2 = MtkPingPongOptimizer.this;
                                if (z3) {
                                    mtkPingPongOptimizer2.sendAtCmd(0, AtCmd.POWER_SAVE_ON, null);
                                } else {
                                    mtkPingPongOptimizer2.sendAtCmd(0, AtCmd.POWER_SAVE_OFF, null);
                                }
                            }
                            break;
                        case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                            if (Settings.Global.getInt(MtkPingPongOptimizer.this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
                                MtkPingPongOptimizer.this.tryRecoverAction();
                            }
                            break;
                        case 106:
                            Intent intent = (Intent) message.obj;
                            if (SubscriptionManager.isValidPhoneId(intent.getIntExtra("phone", -1)) && "ABSENT".equals(intent.getStringExtra("ss"))) {
                                MtkPingPongOptimizer.this.tryRecoverAction();
                            }
                            break;
                    }
                    return;
                }
                String stringExtra = ((Intent) message.obj).getStringExtra("PingPongAction");
                MtkPingPongOptimizer.this.log("recordOptimizedAction test data: " + stringExtra);
                if (MtkPingPongOptimizer.this.mOptimizedActionSet.add(stringExtra)) {
                    MtkPingPongOptimizer.this.recordOptimizedAction();
                }
                MtkPingPongOptimizer.this.recordOptimizedStats(stringExtra);
            }
        };
        this.mWorkHandler = handler;
        this.mDisplayStateListener = new BaseEvent.IEventListener() { // from class: com.android.phone.MtkPingPongOptimizer.3
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        boolean zIsScreenOn = displayEvent.isScreenOn();
                        MtkPingPongOptimizer.this.mWorkHandler.removeMessages(103);
                        MtkPingPongOptimizer.this.mWorkHandler.sendMessageDelayed(MtkPingPongOptimizer.this.mWorkHandler.obtainMessage(103, Boolean.valueOf(zIsScreenOn)), 3000L);
                    }
                }
            }
        };
        this.mPingPongCloudChangeListener = new BaseEvent.IEventListener() { // from class: com.android.phone.MtkPingPongOptimizer$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        };
        this.mDeviceStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.MtkPingPongOptimizer.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    MtkPingPongOptimizer.this.mWorkHandler.sendEmptyMessageDelayed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, 3000L);
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    MtkPingPongOptimizer.this.mWorkHandler.sendMessageDelayed(MtkPingPongOptimizer.this.mWorkHandler.obtainMessage(106, intent), 3000L);
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        this.isO12Device = HyperOSCustFeatureResolve.getBoolean("support_special_pingpong_recovery_bool", false);
        handler.sendEmptyMessageDelayed(100, 3000L);
        registerAtUrc();
        registerDebugState();
        registerPingPongCloudChangeListener();
    }

    private void registerDebugState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MtkPingPongOptimizer.intent.debugall");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.MtkPingPongOptimizer.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null && "MtkPingPongOptimizer.intent.debugall".equals(intent.getAction())) {
                    MtkPingPongOptimizer.this.mWorkHandler.obtainMessage(1000, intent).sendToTarget();
                }
            }
        }, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        sendAtCmd(0, AtCmd.HOOK_ID_START_REG, null);
        sendAtCmd(0, AtCmd.ENABLE_PING_PONG_DMF, null);
        tryRecoverAction();
        registerDeviceState();
        this.mPingPongOptStatus = true;
    }

    private void registerDeviceState() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mDeviceStateReceiver, intentFilter, 2);
        registerDisplayStateChangeListener();
        registerPowerSaveModeChangeListener();
    }

    private void unregisterDeviceState() {
        this.mContext.unregisterReceiver(this.mDeviceStateReceiver);
        unregisterDisplayStateChangeListener();
        unregisterPowerSaveModeChangeListener();
    }

    private void registerAtUrc() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.registerForOemIndication(this.mWorkHandler, 101, phone);
            }
        }
    }

    private void registerDisplayStateChangeListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayStateListener);
    }

    private void unregisterDisplayStateChangeListener() {
        EventHubManager.getInstance().unregisterListener(this.mDisplayStateListener);
    }

    class PowerSaveModeListener implements BaseEvent.IEventListener {
        PowerSaveModeListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.PowerEvent) {
                Bundle bundle = ((TelephonyEvent.PowerEvent) baseEvent).getBundle();
                MtkPingPongOptimizer.this.mWorkHandler.removeMessages(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
                MtkPingPongOptimizer.this.mWorkHandler.sendMessageDelayed(MtkPingPongOptimizer.this.mWorkHandler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, bundle), 3000L);
            }
        }
    }

    private void registerPowerSaveModeChangeListener() {
        this.mPowerSaveModeListener = new PowerSaveModeListener();
        EventHubManager.getInstance().registerListener("telephony", "power_save_mode_changed", this.mPowerSaveModeListener);
    }

    private void unregisterPowerSaveModeChangeListener() {
        EventHubManager.getInstance().unregisterListener(this.mPowerSaveModeListener);
        this.mPowerSaveModeListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        this.mWorkHandler.sendEmptyMessageDelayed(102, 3000L);
    }

    private void registerPingPongCloudChangeListener() {
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", this.mPingPongCloudChangeListener);
    }

    private void takeAction(String[] strArr) {
        String urcParams;
        int i = Integer.parseInt(strArr[2]);
        int i2 = Integer.parseInt(strArr[3]);
        int i3 = Integer.parseInt(strArr[4]);
        Object[] objArr = new Object[1];
        if (i3 == 1) {
            String urcParams2 = getUrcParams(strArr, 6, 5);
            if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+ESBP=5,\"SBP_4G5_PINGPONG_AVOID\",1", "+ESBP"}, objArr) && urcParams2 != null) {
                if (this.isO12Device) {
                    String urcParams3 = getUrcParams(strArr, 7, 4);
                    if (i2 == 1) {
                        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",".concat("1," + urcParams3), "+EGMC"}, objArr);
                    } else if (i2 == 2) {
                        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",".concat("1," + urcParams3), "+EGMC"}, objArr);
                        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",".concat("2," + getUrcParams(strArr, 7, 4)), "+EGMC"}, objArr);
                    }
                } else {
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",".concat(urcParams2), "+EGMC"}, objArr);
                }
            }
        } else if (i3 == 2) {
            String urcParams4 = getUrcParams(strArr, 6, 7);
            if (urcParams4 != null) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"set_srv_nbr_cell_th\",".concat(urcParams4), "+EGMC"}, objArr);
            }
        } else if (i3 == 3) {
            String urcParams5 = getUrcParams(strArr, 6, 3);
            if (urcParams5 != null) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"nr_lte_prefer_rat_rsrp_th\",".concat(urcParams5), "+EGMC"}, objArr);
            }
            if (this.isO12Device && (urcParams = getUrcParams(strArr, 9, 7)) != null && !"0,0,0,0,0,0,0".equals(urcParams)) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"set_srv_nbr_cell_th\",".concat(urcParams), "+EGMC"}, objArr);
            }
        } else {
            if (i3 != 4) {
                return;
            }
            String urcParams6 = getUrcParams(strArr, 6, 5);
            if (urcParams6 != null) {
                if (i2 == 4) {
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"LTE_high_priority_cell\",1,".concat(urcParams6), "+EGMC"}, objArr);
                } else if (i2 != 5) {
                    return;
                } else {
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"NR_high_priority_cell\",1,".concat(urcParams6), "+EGMC"}, objArr);
                }
            }
        }
        String str = i + "_" + i2 + "_" + i3;
        log("takeAction action=".concat(str));
        if (this.isO12Device) {
            reportPingPongByModem(i);
        }
        if (this.mOptimizedActionSet.add(str)) {
            recordOptimizedAction();
        }
        recordOptimizedStats(str);
    }

    private void reportPingPongByModem(int i) {
        log("reportPingPongByModem slotId:" + i);
        Intent intent = new Intent("com.android.phone.intent.action.PINGPONG_BY_MODEM");
        intent.putExtra("phoneId", i);
        this.mContext.sendBroadcast(intent);
    }

    private void doRecoverAction(String[] strArr) {
        int length = strArr.length;
        if (length != 23) {
            return;
        }
        int[] iArr = {Integer.parseInt(strArr[2]), 0, 0};
        boolean zRemove = false;
        for (int i = 13; i < length; i++) {
            if (!"0".equals(strArr[i])) {
                iArr[1] = Integer.parseInt(strArr[i - 10]);
                iArr[2] = Integer.parseInt(strArr[i]);
                recoverAction(iArr);
                String str = iArr[0] + "_" + iArr[1] + "_" + iArr[2];
                log("doRecoverAction action=".concat(str));
                zRemove = this.mOptimizedActionSet.remove(str);
            }
        }
        if (zRemove) {
            recordOptimizedAction();
        }
    }

    private void recoverAction(int[] iArr) {
        int i = iArr[0];
        int i2 = iArr[1];
        int i3 = iArr[2];
        Object[] objArr = new Object[1];
        if (i3 == 1) {
            if (!this.isO12Device) {
                if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+ESBP=5,\"SBP_4G5_PINGPONG_AVOID\",0", "+ESBP"}, objArr)) {
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",0", "+EGMC"}, objArr);
                    return;
                }
                return;
            }
            if (i2 == 1) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",1,-110,-14,-119,-14", "+EGMC"}, objArr);
                return;
            } else {
                if (i2 == 2) {
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",1,-110,-14,-119,-14", "+EGMC"}, objArr);
                    FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",2,-110,-14,-119,-14", "+EGMC"}, objArr);
                    return;
                }
                return;
            }
        }
        if (i3 == 2) {
            FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"set_srv_nbr_cell_th\",5,-420,-45,-10,-360,-20,10", "+EGMC"}, objArr);
            return;
        }
        if (i3 == 3) {
            FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"nr_lte_prefer_rat_rsrp_th\",-404,-460,-360", "+EGMC"}, objArr);
            if (this.isO12Device) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"set_srv_nbr_cell_th\",5,-420,-45,-10,-360,-20,10", "+EGMC"}, objArr);
                return;
            }
            return;
        }
        if (i3 == 4) {
            if (i2 == 4) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"LTE_high_priority_cell\",0", "+EGMC"}, objArr);
            } else if (i2 == 5) {
                FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"NR_high_priority_cell\",0", "+EGMC"}, objArr);
            }
        }
    }

    private void forceRecover(int i) {
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",1,-110,-14,-119,-14", "+EGMC"}, null);
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"4g5_pingpong_detection\",2,-110,-14,-119,-14", "+EGMC"}, null);
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"set_srv_nbr_cell_th\",5,-420,-45,-10,-360,-20,10", "+EGMC"}, null);
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"nr_lte_prefer_rat_rsrp_th\",-404,-460,-360", "+EGMC"}, null);
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"LTE_high_priority_cell\",0", "+EGMC"}, null);
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EGMC = 1,\"NR_high_priority_cell\",0", "+EGMC"}, null);
    }

    private String getUrcParams(String[] strArr, int i, int i2) {
        if (strArr.length < i + i2) {
            log("invalid data length=" + strArr.length);
            return null;
        }
        int i3 = Integer.parseInt(strArr[4]);
        StringBuilder sb = new StringBuilder();
        for (int i4 = 0; i4 < i2; i4++) {
            if (i3 == 4 && i4 == 1) {
                sb.append(strArr[i + i4] + "f");
            } else {
                sb.append(strArr[i + i4]);
            }
            if (i4 == i2 - 1) {
                break;
            }
            sb.append(",");
        }
        String string = sb.toString();
        log("sbp type " + i3 + " params: " + string);
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordOptimizedAction() {
        try {
            PreferenceUtils.getInstance().setStringPreference("MtkPingPongOptimizer.SbpAction", ObjectSerializer.serialize(this.mOptimizedActionSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
        localLog("recordOptimizedAction", "actionSet=" + this.mOptimizedActionSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordOptimizedStats(String str) {
        HashMap map = new HashMap();
        map.put("action", str);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "mtk_ping_pong_stats", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmd(int i, AtCmd atCmd, Message message) {
        Phone phone;
        if (i >= 0 && (phone = PhoneFactory.getPhone(i)) != null) {
            log("sendAtCmd " + atCmd);
            FiveGManagerBase.getInstance().sendAtCommand(phone.getPhoneId(), atCmd.getAtCmd(), message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("MtkPingPongOptimizer", str);
    }

    private void localLog(String str, String str2) {
        FiveGManagerBase.localLog("MtkPingPongOptimizer", str, str2);
    }
}
