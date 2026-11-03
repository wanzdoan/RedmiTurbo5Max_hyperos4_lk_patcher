package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpecialSimBase {
    private static long DELAY_TIMER = 259200000;
    private static final boolean IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
    protected static Context mContext;
    protected static volatile SpecialSimBase mInstance;
    private ContentResolver mContentResolver;
    private boolean[] mDisableSA;
    private long[] mFirstTime;
    private ContentObserver mGameStatusObserver;
    protected boolean mIsDoAction;
    private int[] mRemindTime;
    private long[] mSecondTime;
    protected final Handler mWorkHandler;
    private ArrayList sPhoneIdList;
    boolean setState = true;
    private final BroadcastReceiver mWorkReceiver = new BroadcastReceiver() { // from class: com.android.phone.SpecialSimBase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                SpecialSimBase.this.mWorkHandler.sendEmptyMessage(102);
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action) && "ABSENT".equals(intent.getStringExtra("ss"))) {
                SpecialSimBase.this.mWorkHandler.sendMessage(SpecialSimBase.this.mWorkHandler.obtainMessage(107, intent.getIntExtra("phone", -1), 0));
            }
        }
    };

    protected abstract void onAtUrcInd(Object obj);

    protected void process5GRegisterFailInd(Object obj) {
    }

    protected void registerSpecialSimRecognition() {
    }

    public abstract void sendInvalidSimState(int i, int i2);

    public static SpecialSimBase getInstance() {
        return mInstance;
    }

    public boolean isSpecialSimSaDisabled(int i) {
        if (i < 0) {
            return false;
        }
        boolean[] zArr = this.mDisableSA;
        if (i < zArr.length) {
            return zArr[i];
        }
        return false;
    }

    protected SpecialSimBase() {
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.SpecialSimBase.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int disableSaPhoneId;
                int disableSaPhoneId2;
                switch (message.what) {
                    case 100:
                        SpecialSimBase.this.initAction();
                        break;
                    case 101:
                        try {
                            SpecialSimBase.this.onAtUrcInd(message.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        break;
                    case 102:
                        if (Settings.Global.getInt(SpecialSimBase.mContext.getContentResolver(), "airplane_mode_on", 0) == 1 && (disableSaPhoneId = SpecialSimBase.this.getDisableSaPhoneId()) != -1) {
                            SpecialSimBase.this.processSaOptAction(disableSaPhoneId, true);
                            SpecialSimBase.localLog("notify5GRegisterFailResult", "enable sa by apm for phoneId=" + disableSaPhoneId);
                            break;
                        }
                        break;
                    case 103:
                        SpecialSimBase.this.process5GRegisterFailInd(message.obj);
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        SpecialSimBase.log("EVENT_HYDRA_READY_IND");
                        SpecialSimBase.this.registerSpecialSimRecognition();
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        SpecialSimBase.this.updateCloudSim();
                        SpecialSimBase specialSimBase = SpecialSimBase.this;
                        if (!specialSimBase.setState && (disableSaPhoneId2 = specialSimBase.getDisableSaPhoneId()) != -1) {
                            SpecialSimBase.this.processSaOptAction(disableSaPhoneId2, true);
                            SpecialSimBase.localLog("notify5GRegisterFailResult", "enable sa by cloud for phoneId=" + disableSaPhoneId2);
                            break;
                        }
                        break;
                    case 106:
                        if (!SpecialSimBase.this.sPhoneIdList.isEmpty()) {
                            SpecialSimBase specialSimBase2 = SpecialSimBase.this;
                            specialSimBase2.processShowNoFiveGDialog(((Integer) specialSimBase2.sPhoneIdList.get(0)).intValue());
                            SpecialSimBase.this.sPhoneIdList.remove(0);
                        }
                        break;
                    case 107:
                        int i = message.arg1;
                        if (i >= 0 && i < SpecialSimBase.this.mDisableSA.length && SpecialSimBase.this.mDisableSA[i]) {
                            SpecialSimBase.this.processSaOptAction(i, true);
                            SpecialSimBase.localLog("notify5GRegisterFailResult", "enable sa by sim absent for phoneId=" + i);
                            break;
                        }
                        break;
                }
            }
        };
        this.mWorkHandler = handler;
        log("init SpecialSimBase");
        mContext = PhoneGlobals.getInstance();
        initMemberVariables();
        handler.sendEmptyMessageDelayed(100, 3000L);
    }

    private void initMemberVariables() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mDisableSA = new boolean[i];
        this.mSecondTime = new long[i];
        this.mFirstTime = new long[i];
        this.mRemindTime = new int[i];
        this.sPhoneIdList = new ArrayList();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        mContext.registerReceiver(this.mWorkReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDisableSaPhoneId() {
        int i = 0;
        while (true) {
            boolean[] zArr = this.mDisableSA;
            if (i >= zArr.length) {
                return -1;
            }
            if (zArr[i]) {
                return i;
            }
            i++;
        }
    }

    protected void notifyArrearsApnResult(int i) {
        if (!this.setState) {
            log("notifyArrearsApnResult no use remind by cloud");
            return;
        }
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).recordApnDetectNotification(i);
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION");
        intent.putExtra("slotId", i);
        mContext.sendBroadcast(intent);
        localLog("notifyArrearsApnResult", "phoneId=" + i);
    }

    protected void notify5GRegisterFailResult(int i, int i2) {
        if (i2 == 27) {
            processShowNoFiveGDialog(i);
            recordSpecialSimEvent(i, i2);
            return;
        }
        if (i2 == 111) {
            processShowNoFiveGDialog(i);
            if (this.setState && this.mIsDoAction && !this.mDisableSA[i]) {
                processSaOptAction(i, false);
                localLog("notify5GRegisterFailResult", "disable sa for phoneId=" + i);
            }
            recordSpecialSimEvent(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSaOptAction(int i, boolean z) {
        log("processSaOptAction phoneId=" + i + ", enable=" + z);
        this.mDisableSA[i] = z ^ true;
        Bundle bundle = new Bundle();
        bundle.putInt("slotId", i);
        bundle.putBoolean("enableSA", z);
        notifySpecialSimStateChange(bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processShowNoFiveGDialog(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || IS_INTERNATIONAL_BUILD || !this.mIsDoAction) {
            log("no support remind");
            return;
        }
        if (isAirPlaneModeOn() || !MiuiPhoneUtils.isSimReady(i) || ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isNoPhoneBalance(i) || !this.setState) {
            log("no use remind");
            return;
        }
        boolean zIsNoFiveGDialogEnabled = isNoFiveGDialogEnabled();
        if (isInGame()) {
            boolean zIsEmpty = this.sPhoneIdList.isEmpty();
            ArrayList arrayList = this.sPhoneIdList;
            if (zIsEmpty) {
                arrayList.add(Integer.valueOf(i));
            } else if (!arrayList.contains(Integer.valueOf(i))) {
                this.sPhoneIdList.add(Integer.valueOf(i));
            }
            log("in game status, no show dialog");
            return;
        }
        if (!zIsNoFiveGDialogEnabled) {
            observeGameStatus(false);
        }
        if (!isInGame() && isLingerTimeForFiveG(i) && zIsNoFiveGDialogEnabled) {
            localLog("processShowNoFiveGDialog", "show FiveG Interruption Dialog");
            MiuiPhoneUtils.showNoFiveGDialog(i);
        }
    }

    private void notifySpecialSimStateChange(Bundle bundle) {
        EventHubManager.getInstance().postEvent(new TelephonyEvent.SimEvent("special_sim_state_changed", bundle));
    }

    public boolean updateCloudSim() {
        List<JSONObject> cloudSimInfo = getCloudSimInfo();
        if (cloudSimInfo == null || cloudSimInfo.size() == 0) {
            log("updateCloudSim cloudSim == null return. ");
            return true;
        }
        for (JSONObject jSONObject : cloudSimInfo) {
            if (jSONObject != null) {
                String strOptString = jSONObject.optString("FeatureType");
                String strOptString2 = jSONObject.optString("Accept_Create");
                if (TextUtils.isEmpty(strOptString) || !"FiveGUnavailableSim".equals(strOptString)) {
                    log("updateCloudSim update return");
                    break;
                }
                if (TextUtils.isEmpty(strOptString2) || "true".equals(strOptString2)) {
                    log("accept create true");
                    this.setState = true;
                    return true;
                }
                log("accept create false");
                this.setState = false;
                return false;
            }
        }
        return this.setState;
    }

    private List getCloudSimInfo() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyCommonFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                ArrayList arrayList = new ArrayList();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    if (cloudData != null) {
                        arrayList.add(cloudData.json());
                    }
                }
                return arrayList;
            }
            log("get update sim no cloud dataList.");
            return null;
        } catch (Exception e) {
            loge("get cloud SimInfos error!!!", e);
            return null;
        }
    }

    private boolean isLingerTimeForFiveG(int i) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long[] jArr = this.mFirstTime;
        long j = jArr[i];
        if (j == 0 && this.mSecondTime[i] == 0) {
            jArr[i] = jCurrentTimeMillis;
            int[] iArr = this.mRemindTime;
            iArr[i] = iArr[i] + 1;
            log("no 5g dialog first remind !");
            recordNoFiveGEvent(i);
            return true;
        }
        int[] iArr2 = this.mRemindTime;
        int i2 = iArr2[i];
        if (i2 != 1) {
            return false;
        }
        long[] jArr2 = this.mSecondTime;
        if (jArr2[i] != 0 || jCurrentTimeMillis <= j + DELAY_TIMER) {
            return false;
        }
        jArr2[i] = jCurrentTimeMillis;
        iArr2[i] = i2 + 1;
        log("no 5g dialog second remind !");
        return true;
    }

    private void observeGameStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mGameStatusObserver == null) {
            if (this.mContentResolver == null) {
                log("observeGameStatus: mContentResolver is null");
                return;
            }
            Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
            ContentObserver contentObserver2 = new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.SpecialSimBase.3
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    SpecialSimBase.log("is game:" + SpecialSimBase.this.isInGame());
                    if (!SpecialSimBase.this.isInGame() && !SpecialSimBase.this.sPhoneIdList.isEmpty() && SpecialSimBase.this.sPhoneIdList.size() > 0) {
                        SpecialSimBase.this.mWorkHandler.sendMessageDelayed(SpecialSimBase.this.mWorkHandler.obtainMessage(106), 30000L);
                    } else if (SpecialSimBase.this.isInGame()) {
                        SpecialSimBase.this.mWorkHandler.removeMessages(106);
                    }
                }
            };
            this.mGameStatusObserver = contentObserver2;
            this.mContentResolver.registerContentObserver(uriFor, false, contentObserver2);
            return;
        }
        if (z || (contentObserver = this.mGameStatusObserver) == null) {
            return;
        }
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(contentObserver);
        }
        this.mGameStatusObserver = null;
    }

    private void recordNoFiveGEvent(int i) {
        HashMap map = new HashMap();
        map.put("no_5G_Dialog", "1");
        log("recordNoFiveGEvent");
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_no_5g_dialog", map);
    }

    private void recordSpecialSimEvent(int i, int i2) {
        HashMap map = new HashMap();
        map.put("Type", Integer.valueOf(i2));
        map.put("SimOperator", MiuiPhoneUtils.getSimOperatorNumeric(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_special_sim", map);
    }

    private static boolean isNoFiveGDialogEnabled() {
        return PreferenceUtils.getInstance().getBooleanPreference("preferred_no_5g_remind_showAgain", Boolean.TRUE).booleanValue();
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInGame() {
        return Settings.Secure.getInt(mContext.getContentResolver(), "gb_boosting", 0) == 1;
    }

    private void isDoActionSupport() {
        this.mIsDoAction = HyperOSCustFeatureResolve.getBoolean("support_special_sim_do_action", false);
    }

    protected void initAction() {
        log("init register");
        registerBroadcastReceiver();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.SpecialSimBase$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$initAction$0(baseEvent);
            }
        });
        this.mContentResolver = mContext.getContentResolver();
        observeGameStatus(true);
        isDoActionSupport();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initAction$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mWorkHandler.sendEmptyMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
        }
    }

    protected static void log(String str) {
        Log.d("SpecialSimBase", str);
    }

    protected static void loge(String str, Exception exc) {
        Log.e("SpecialSimBase", str);
    }

    protected static void localLog(String str, String str2) {
        FiveGManagerBase.getInstance();
        FiveGManagerBase.localLog("SpecialSimBase", str, str2);
    }
}
