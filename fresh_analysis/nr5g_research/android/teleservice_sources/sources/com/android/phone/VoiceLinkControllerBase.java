package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public abstract class VoiceLinkControllerBase implements TelephonyStatAdapter.ICallBack {
    protected static volatile VoiceLinkControllerBase sInstance;
    private ConnectivityManager mCM;
    protected Context mContext;
    protected int mDds;
    private final BaseEvent.IEventListener mDisplayListener;
    private final BroadcastReceiver mGameReceiver;
    private boolean mGameReceiverRegistered;
    protected final Handler mHandler;
    protected boolean mInNolimit;
    protected boolean mIs2SimsReady;
    protected boolean mIsFeatureEnabled;
    private boolean mIsGaming;
    private final AtomicBoolean mIsRoamingOrOos;
    protected boolean mIsWifiConn;
    protected int mNdds;
    private final ConnectivityManager.NetworkCallback mNetworkCB;
    private boolean mReged;
    protected final ServiceState[] mSS;
    protected int mSceneMask;
    protected long mScreenOffTime;
    private final SignalStrength[] mSignalStrength;

    protected long getDelayTimer(boolean z) {
        return z ? 0L : 30000L;
    }

    protected abstract boolean isMaskSupported(int i);

    protected abstract boolean isNddsCrossSimEnabled();

    public void onCellLocationChanged(int i, CellLocation cellLocation) {
    }

    public void onDataConnectionChanged(int i, int i2) {
    }

    public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int i, Map map) {
    }

    protected abstract boolean shouldEnableForService();

    protected abstract void tryEnableFeature();

    protected abstract void updateNolimitState(String str);

    public static VoiceLinkControllerBase getInstance() {
        return sInstance;
    }

    public VoiceLinkControllerBase() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mSS = new ServiceState[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mIsRoamingOrOos = new AtomicBoolean(false);
        this.mIsFeatureEnabled = false;
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.VoiceLinkControllerBase.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 100:
                        VoiceLinkControllerBase.this.tryEnableFeature(true);
                        break;
                    case 101:
                        VoiceLinkControllerBase.this.updateNolimitState("ss");
                        break;
                    case 102:
                        VoiceLinkControllerBase voiceLinkControllerBase = VoiceLinkControllerBase.this;
                        voiceLinkControllerBase.mIsWifiConn = message.arg1 == 1;
                        voiceLinkControllerBase.updateNolimitState("wifi");
                        break;
                    case 103:
                        VoiceLinkControllerBase.this.updateNolimitState("signal");
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        VoiceLinkControllerBase.this.updateNolimitState("call");
                        VoiceLinkControllerBase.this.checkScreenStateAndService("call");
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        VoiceLinkControllerBase.this.mIsGaming = message.arg1 == 1;
                        VoiceLinkControllerBase.this.updateNolimitState("game");
                        break;
                    case 106:
                        int i2 = message.arg1;
                        VoiceLinkControllerBase voiceLinkControllerBase2 = VoiceLinkControllerBase.this;
                        if (i2 == 2) {
                            voiceLinkControllerBase2.mScreenOffTime = Long.MAX_VALUE;
                            voiceLinkControllerBase2.updateNolimitState("screenOn");
                        } else {
                            voiceLinkControllerBase2.mScreenOffTime = SystemClock.elapsedRealtime();
                            VoiceLinkControllerBase.this.checkScreenStateAndService("screen");
                        }
                        break;
                    case 107:
                        VoiceLinkControllerBase.this.checkScreenStateAndService("recheck");
                        break;
                    case 108:
                        VoiceLinkControllerBase.this.onSimChanged();
                        break;
                    case 109:
                        VoiceLinkControllerBase.log("feature enable flag reset");
                        VoiceLinkControllerBase.this.mIsFeatureEnabled = false;
                        break;
                }
            }
        };
        this.mHandler = handler;
        this.mNdds = 1;
        this.mNetworkCB = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.VoiceLinkControllerBase.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                VoiceLinkControllerBase.log("onAvailable:" + network);
                VoiceLinkControllerBase.this.updateWifiConnState(network, true);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                VoiceLinkControllerBase.log("onLost:" + network);
                VoiceLinkControllerBase.this.updateWifiConnState(network, false);
            }
        };
        this.mGameReceiver = new BroadcastReceiver() { // from class: com.android.phone.VoiceLinkControllerBase.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(intent.getAction())) {
                    int i2 = (intent.getLongExtra("Level_DL", 1L) == 1 && intent.getLongExtra("Level_UL", 1L) == 1) ? 0 : 1;
                    VoiceLinkControllerBase.this.mHandler.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                    VoiceLinkControllerBase.this.mHandler.sendMessageDelayed(VoiceLinkControllerBase.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i2, 0), 3000L);
                }
            }
        };
        BaseEvent.IEventListener iEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.VoiceLinkControllerBase$$ExternalSyntheticLambda1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$1(baseEvent);
            }
        };
        this.mDisplayListener = iEventListener;
        log("VoiceLinkControllerBase");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        int integer = phoneGlobals.getResources().getInteger(R.integer.config_voice_link_supported_scenes);
        this.mSceneMask = integer;
        if (integer == 0) {
            log("NO voicelink subfeature is supported");
            return;
        }
        this.mCM = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        SettingsObserver settingsObserver = new SettingsObserver(this.mContext, handler);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.VoiceLinkControllerBase.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                VoiceLinkControllerBase.logV(intent.getAction());
                if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) || "android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                    VoiceLinkControllerBase.this.mHandler.removeMessages(108);
                    VoiceLinkControllerBase.this.mHandler.sendEmptyMessageDelayed(108, 3000L);
                }
            }
        }, intentFilter, 2);
        settingsObserver.observe(Settings.Global.getUriFor(MiuiPhoneUtils.VICE_SLOT_VOLTE_DATA_ENABLED), 100);
        settingsObserver.observe(Settings.Global.getUriFor("smart_dual_sim"), 100);
        this.mScreenOffTime = MiuiPhoneUtils.isScreenOn(this.mContext) ? Long.MAX_VALUE : SystemClock.elapsedRealtime();
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", iEventListener, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("VoiceLinkControllerBase", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("VoiceLinkControllerBase", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkScreenStateAndService(String str) {
        logV("checkScreenStateAndService " + str);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mHandler.removeMessages(107);
        if (this.mScreenOffTime < Long.MAX_VALUE) {
            boolean zIsPhoneInCall = MiuiPhoneUtils.isPhoneInCall();
            log("isInCall " + zIsPhoneInCall);
            if (jElapsedRealtime - this.mScreenOffTime > 30000 && !zIsPhoneInCall) {
                updateNolimitState("screenOff");
            } else {
                this.mHandler.sendEmptyMessageDelayed(107, 30000L);
            }
        }
    }

    protected void tryEnableFeature(boolean z) {
        tryEnableFeature();
    }

    protected boolean isNddsDataFeatureEnabled() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dual_sim", 0) == 1 || Settings.Global.getInt(this.mContext.getContentResolver(), MiuiPhoneUtils.VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1;
    }

    protected boolean shouldEnableForGame() {
        return isMaskSupported(4) && this.mIsGaming;
    }

    protected boolean shouldEnableForCall() {
        return isMaskSupported(16) && MiuiPhoneUtils.isPhoneInImsCall(this.mDds) && !TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
    }

    /* JADX WARN: Code duplicated, block: B:44:0x0081  */
    protected int checkSignalStrength() {
        SignalStrength signalStrength;
        int i = 1;
        if (!isMaskSupported(2)) {
            return 1;
        }
        if (!SubscriptionManager.isValidPhoneId(this.mNdds) || (signalStrength = this.mSignalStrength[this.mNdds]) == null) {
            i = 0;
        } else {
            CellSignalStrengthNr nrSignalStrength = MiuiPhoneUtils.getNrSignalStrength(signalStrength);
            int ssRsrp = nrSignalStrength != null ? nrSignalStrength.getSsRsrp() : Integer.MAX_VALUE;
            int lteRsrp = this.mSignalStrength[this.mNdds].getLteRsrp();
            int wcdmaRscp = this.mSignalStrength[this.mNdds].getWcdmaRscp();
            int gsmDbm = this.mSignalStrength[this.mNdds].getGsmDbm();
            boolean z = gsmDbm <= -103 || wcdmaRscp <= -105 || lteRsrp <= -113 || ssRsrp <= -113 || (gsmDbm == Integer.MAX_VALUE && wcdmaRscp == Integer.MAX_VALUE && lteRsrp == Integer.MAX_VALUE && ssRsrp == Integer.MAX_VALUE);
            boolean z2 = (gsmDbm != Integer.MAX_VALUE && gsmDbm > -97) || (wcdmaRscp != Integer.MAX_VALUE && wcdmaRscp > -99) || ((lteRsrp != Integer.MAX_VALUE && lteRsrp > -106) || (ssRsrp != Integer.MAX_VALUE && ssRsrp > -106));
            if (z) {
                i = -1;
            } else if (!z2) {
                i = 0;
            }
        }
        logV("checkSignalStrength:" + i);
        return i;
    }

    protected void regStateMonitor(boolean z) {
        if (z && !this.mReged) {
            TelephonyStatAdapter.registerCallBack(this);
            ConnectivityManager connectivityManager = this.mCM;
            if (connectivityManager != null) {
                connectivityManager.registerDefaultNetworkCallback(this.mNetworkCB, this.mHandler);
            }
            if (isMaskSupported(4) && !this.mGameReceiverRegistered) {
                this.mContext.registerReceiver(this.mGameReceiver, new IntentFilter("com.android.phone.intent.action.CHANGE_LEVEL"), 2);
                this.mGameReceiverRegistered = true;
            }
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                this.mSignalStrength[i] = PhoneFactory.getPhone(i).getSignalStrength();
                this.mSS[i] = PhoneFactory.getPhone(i).getServiceState();
            }
            this.mReged = true;
            return;
        }
        if (z || !this.mReged) {
            return;
        }
        TelephonyStatAdapter.unregisterCallBack(this);
        ConnectivityManager connectivityManager2 = this.mCM;
        if (connectivityManager2 != null) {
            connectivityManager2.unregisterNetworkCallback(this.mNetworkCB);
        }
        if (this.mGameReceiverRegistered) {
            try {
                this.mContext.unregisterReceiver(this.mGameReceiver);
            } catch (IllegalArgumentException e) {
                log("unregister game receiver failed: " + e);
            }
            this.mGameReceiverRegistered = false;
        }
        this.mReged = false;
    }

    public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        this.mSignalStrength[i] = signalStrength;
        if (this.mScreenOffTime >= Long.MAX_VALUE && i != this.mDds) {
            this.mHandler.sendEmptyMessage(103);
        }
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        this.mSS[i] = serviceState;
        if (this.mScreenOffTime < Long.MAX_VALUE) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.phone.VoiceLinkControllerBase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onServiceStateChanged$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceStateChanged$0() {
        this.mHandler.removeMessages(101);
        this.mHandler.sendEmptyMessageDelayed(101, 3000L);
    }

    public void onPreciseCallStateChanged(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("onPreciseCallStateChanged call state : " + i2);
            this.mHandler.removeMessages(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, Integer.valueOf(i)), 500L);
        }
    }

    public void SetCrossSimCallingSwitch() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(100));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r4v8 */
    public void updateWifiConnState(Network network, boolean z) {
        NetworkCapabilities networkCapabilities;
        Network activeNetwork = this.mCM.getActiveNetwork();
        ?? HasTransport = (activeNetwork == null || network.getNetId() != activeNetwork.getNetId() || !z || (networkCapabilities = this.mCM.getNetworkCapabilities(activeNetwork)) == null) ? 0 : networkCapabilities.hasTransport(1);
        logV("wifi conn:" + ((boolean) HasTransport));
        this.mHandler.removeMessages(102);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(102, HasTransport, 0), MiuiPhoneUtils.isPhoneInImsCall(this.mNdds) ? 0L : 3000L);
    }

    protected boolean shouldReturnToDefault() {
        if (MiuiPhoneUtils.isPhoneInCall()) {
            return false;
        }
        if (this.mIsWifiConn || this.mScreenOffTime < Long.MAX_VALUE) {
            return true;
        }
        if (isRoamingScene() && !shouldEnableForService()) {
            log("shouldReturnToDefault roamingScene:true");
            return true;
        }
        boolean z = this.mInNolimit && checkSignalStrength() > 0;
        boolean zShouldEnableForService = shouldEnableForService();
        boolean zShouldEnableForCall = shouldEnableForCall();
        boolean zShouldEnableForGame = shouldEnableForGame();
        StringBuilder sb = new StringBuilder("shouldReturnToDefault signal：");
        sb.append(z);
        sb.append(" srv:");
        sb.append(!zShouldEnableForService);
        sb.append(" call:");
        sb.append(!zShouldEnableForCall);
        sb.append(" game:");
        sb.append(!zShouldEnableForGame);
        log(sb.toString());
        return (zShouldEnableForService || !z || zShouldEnableForCall || zShouldEnableForGame) ? false : true;
    }

    protected boolean shouldEnableNolimit() {
        boolean z = false;
        if (!isNddsCrossSimEnabled() || this.mIsWifiConn) {
            return false;
        }
        boolean zShouldEnableForService = shouldEnableForService();
        if (isRoamingScene()) {
            log("shouldEnableNolimit roamingScene:true srv:" + zShouldEnableForService);
            return zShouldEnableForService;
        }
        if (this.mScreenOffTime == Long.MAX_VALUE) {
            if (!this.mInNolimit && checkSignalStrength() < 0) {
                z = true;
            }
            boolean zShouldEnableForGame = shouldEnableForGame();
            log("shouldEnableNolimit signal:" + z + " srv:" + zShouldEnableForService + " game:" + zShouldEnableForGame);
            if (zShouldEnableForGame || z || zShouldEnableForService) {
                return true;
            }
        }
        return shouldEnableForCall();
    }

    protected boolean isRoamingScene() {
        return isDdsRoaming() || isNddsRoaming();
    }

    protected boolean isDdsRoaming() {
        ServiceState serviceState;
        if (this.mSS == null || !SubscriptionManager.isValidPhoneId(this.mDds)) {
            return false;
        }
        int i = this.mDds;
        ServiceState[] serviceStateArr = this.mSS;
        return i < serviceStateArr.length && (serviceState = serviceStateArr[i]) != null && serviceState.getRoaming();
    }

    protected boolean isNddsRoaming() {
        ServiceState serviceState;
        if (this.mSS == null || !SubscriptionManager.isValidPhoneId(this.mNdds)) {
            return false;
        }
        int i = this.mNdds;
        ServiceState[] serviceStateArr = this.mSS;
        return i < serviceStateArr.length && (serviceState = serviceStateArr[i]) != null && serviceState.getRoaming();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimChanged() {
        boolean z = false;
        boolean z2 = MiuiPhoneUtils.getInsertedSimCount() == 2 && FiveGUtils.isAllSimActivated();
        this.mDds = DefaultSimManager.getDefaultDataSlotId();
        this.mNdds = FiveGUtils.getViceSlotId();
        log("new dds:" + this.mDds + " ndds:" + this.mNdds);
        if (SubscriptionManager.isValidPhoneId(this.mDds) && SubscriptionManager.isValidPhoneId(this.mNdds) && this.mDds != this.mNdds && z2) {
            z = true;
        }
        this.mIs2SimsReady = z;
        tryEnableFeature();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseEvent baseEvent) {
        if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
            if (displayEvent.isDefaultDisplay()) {
                log("last screen on:" + displayEvent.isScreenOn());
                this.mHandler.obtainMessage(106, displayEvent.getDisplayState(), 0).sendToTarget();
            }
        }
    }

    protected void recordVoicelinkScene(int i, int i2, String str) {
        HashMap map = new HashMap();
        map.put("VoiceLink", str);
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
        String simOperatorNumeric2 = MiuiPhoneUtils.getSimOperatorNumeric(i2);
        map.put("ddsSimOperator", simOperatorNumeric);
        map.put("nddsSimOperator", simOperatorNumeric2);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_voicelink_scene", map);
    }
}
