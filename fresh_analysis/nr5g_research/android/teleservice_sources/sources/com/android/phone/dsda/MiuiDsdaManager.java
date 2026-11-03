package com.android.phone.dsda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDsdaManager {
    private static MiuiDsdaManager mInstance;
    private ModemDsdaUtils mModemDsdaUtils;
    private boolean mQcRilReady = false;
    private long mlastUpdateTime = 0;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.dsda.MiuiDsdaManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MiuiDsdaManager.log("onReceive intent: " + action);
            if (action.equals("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED")) {
                if (MiuiDsdaManager.this.mHandler.hasMessages(1)) {
                    MiuiDsdaManager.this.mHandler.removeMessages(1);
                }
                MiuiDsdaManager.this.mHandler.sendEmptyMessageDelayed(1, 500L);
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();
    private Handler mHandler = new DsdaHandler(BackgroundThread.get().getLooper());
    private ModemUtils mHydra = ModemUtils.init(this.mContext);

    public static synchronized MiuiDsdaManager getInstance() {
        try {
            if (mInstance == null && MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
                mInstance = new MiuiDsdaManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private MiuiDsdaManager() {
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new MiuiDsdaQcrilHookStateCallback(), this.mHandler);
        if (isSwitchDsdaModeByResetHwMbn()) {
            log("dsda and dsds mode switch by activating the corresponding hw mbns.");
            this.mModemDsdaUtils = ModemDsdaUtils.init(this.mContext);
        } else {
            this.mContext.registerReceiver(this.mReceiver, new IntentFilter("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED"), 2);
        }
        registerModemRestart();
    }

    public class DsdaHandler extends Handler {
        public DsdaHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiDsdaManager.log("handleMessage  : " + message.what);
            int i = message.what;
            if (i == 1) {
                MiuiDsdaManager.this.setDsdaDisabledIfNeeded();
                return;
            }
            if (i == 2) {
                MiuiDsdaManager.log("is dsda switch success : " + (message.arg1 == MiuiDsdaManager.this.isDsdaEnabledByResetHwMbn()));
                return;
            }
            if (i == 3) {
                Toast.makeText(MiuiDsdaManager.this.mContext, message.arg1 == 1 ? R.string.dsda_turn_on : R.string.dsda_turn_off, 0).show();
            } else {
                if (i != 4) {
                    if (i == 5 && !MiuiDsdaManager.this.mHandler.hasMessages(4)) {
                        MiuiDsdaManager.this.mHandler.sendEmptyMessageDelayed(4, 10000L);
                        return;
                    }
                    return;
                }
                MiuiDsdaManager.this.processModemResetChange();
            }
        }
    }

    public void setDsdaDisabledIfNeeded() {
        boolean zIsConcurrentCallsPossible = TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
        boolean zIsDsdaEnabled = isDsdaEnabled();
        log("setDsdaDisabledIfNeeded  isCurrentDsda : " + zIsConcurrentCallsPossible + " , db val : " + zIsDsdaEnabled);
        if (!zIsConcurrentCallsPossible || zIsDsdaEnabled) {
            return;
        }
        log("setDsdaDisabledIfNeeded sync dsda db status to modem.");
        setDsdaEnabled(false, 4);
    }

    public void setDsdaEnabled(boolean z) {
        setDsdaEnabled(z, 3);
    }

    public void setDsdaEnabled(boolean z, int i) {
        localLog("setDsdaEnabled local", "enabled : " + z + " , operation code : " + i);
        if (!isSwitchDsdaModeByResetHwMbn()) {
            setDsdaEnabledByQmiMsimPreference(z);
        } else if (i == 1 || i == 3) {
            setDsdaEnabledAfterUserReboot(z);
        } else if (i == 2) {
            boolean dsdaEnabledByResetHwMbn = setDsdaEnabledByResetHwMbn(z);
            log("setDsdaEnabled trigger switch dsda mode by reset hw mbn : " + dsdaEnabledByResetHwMbn);
            if (!dsdaEnabledByResetHwMbn) {
                return;
            }
        } else {
            log("do nothing, return.");
            return;
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "hybrid_dsda_mode_enabled", z ? 1 : 0);
        notifyDsdaEnabledListener(z, i);
        HashMap map = new HashMap();
        map.put("UpdateDsdaEnable", z ? "1" : "0");
        map.put("UpdateDsdaScene", String.valueOf(i));
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_update_dsda_sub_mode", map);
    }

    public void setDsdaEnabledByQmiMsimPreference(boolean z) {
        log("setDsdaEnabledByQmiMsimPreference");
        FiveGManagerBase.getInstance().setModemFeature(8, !z ? 1 : 0, 0);
    }

    public boolean isDsdaEnabled() {
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "hybrid_dsda_mode_enabled", 1) == 1;
        log("isDsdaEnabled enabled : " + z);
        return z;
    }

    private void setDsdaEnabledAfterUserReboot(boolean z) {
        ModemDsdaUtils modemDsdaUtils = this.mModemDsdaUtils;
        if (modemDsdaUtils == null) {
            log("setDsdaEnabledAfterUserReboot error.");
            return;
        }
        modemDsdaUtils.setDsdaCapPropertyEnabled(z);
        log("setDsdaEnabledByUserReboot take effect after the user reboot.");
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(3, z ? 1 : 0, 0));
    }

    private boolean setDsdaEnabledByResetHwMbn(boolean z) {
        log("setDsdaEnabledByResetHwMbn");
        if (this.mModemDsdaUtils == null) {
            log("setDsdaEnabledByResetHwMbn error.");
            return false;
        }
        if (MiuiPhoneUtils.isScreenOn(this.mContext) || MiuiPhoneUtils.isDuringCall()) {
            log("setDsdaEnabledByResetHwMbn not current, return.");
            return false;
        }
        if (this.mlastUpdateTime != 0 && SystemClock.elapsedRealtime() - this.mlastUpdateTime < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            log("setDsdaEnabledByResetHwMbn too frequent, try it again later.");
            return false;
        }
        if (isDsdaEnabledByResetHwMbn() == z) {
            log("setDsdaEnabledByResetHwMbn state no change, return.");
            return false;
        }
        this.mModemDsdaUtils.setDsdaEnabled(z);
        this.mlastUpdateTime = SystemClock.elapsedRealtime();
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(2, z ? 1 : 0, 0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        return true;
    }

    public boolean isDsdaEnabledByResetHwMbn() {
        ModemDsdaUtils modemDsdaUtils = this.mModemDsdaUtils;
        if (modemDsdaUtils == null) {
            log("isDsdaEnabledByResetHwMbn error.");
            return false;
        }
        boolean zIsDsdaEnabled = modemDsdaUtils.isDsdaEnabled();
        log("isDsdaEnabledByResetHwMbn enabled : " + zIsDsdaEnabled);
        return zIsDsdaEnabled;
    }

    public boolean isSwitchDsdaModeByResetHwMbn() {
        return this.mContext.getResources().getBoolean(R.bool.config_switch_dsda_mode_using_reset_hw_mbn);
    }

    private void notifyDsdaEnabledListener(boolean z, int i) {
        log("notify dsda enabled listener, enable : " + z + " , opCode: " + i);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.DsdaEvent("dsda_state_changed", z, i));
    }

    private void registerModemRestart() {
        PhoneFactory.getPhone(0).mCi.registerForModemReset(this.mHandler, 5, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processModemResetChange() {
        log("processModemResetChange");
        if (this.mQcRilReady) {
            this.mHydra.regsiterDsdaSubModeIndication();
        }
    }

    class MiuiDsdaQcrilHookStateCallback implements BaseEvent.IEventListener {
        MiuiDsdaQcrilHookStateCallback() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
                MiuiDsdaManager.log("qcrilhookReady");
                MiuiDsdaManager.this.mQcRilReady = true;
                MiuiDsdaManager.this.mHydra.regsiterDsdaSubModeIndication();
            }
        }
    }

    private void localLog(String str, String str2) {
        FiveGManagerBase.getInstance();
        FiveGManagerBase.localLog("MiuiDsdaManager", str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("MiuiDsdaManager", str);
    }
}
