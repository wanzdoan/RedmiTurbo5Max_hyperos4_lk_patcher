package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.MiuiDefaultPhoneNotifierStub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.util.ArrayUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import miui.os.Build;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SignalStrengthMonitor implements DefaultSimManager.DataSlotListener {
    protected static final HashMap sLocalLogs = new HashMap();
    ContentObserver m5GNetworkTypeObserver;
    private Context mContext;
    private String mCurrentRatNetworkMode;
    private long mFristStatisticTime;
    private SignalHandler mHandler;
    private boolean mIsAllowSignalShake;
    private boolean mIsDuringCall;
    private boolean mIsInService;
    private boolean mIsNsaMode;
    private boolean mIsRadioPowerOff;
    private boolean mIsScreenOn;
    private boolean mIsShow5GIcon;
    private boolean mIsWifiConnected;
    private SignalStrength mLastSignalStrength;
    private long mNrBetterDuration;
    private String mNsaLevelName;
    private long mNsaSignalStartTime;
    private long mOOSDuration;
    private long mOOSStartTime;
    private Phone mPhone;
    private int mPreferredNetworkMode;
    private long mRatNetworkModeStartTime;
    private long mSignalShakeCount;
    private long mSignalStrengthBigChangeCount;
    private SignalStrengthStatisticInfo mSignalStrengthInfos;
    private boolean mSimAbsent;
    private int mSlotId;
    private int mStatisticsTime;
    private TelephonyManager mTelephonyManager;
    private final HandlerThread mWorkThread;
    private TelephonyCallBack mTelephonyCallBack = null;
    private int mRat = 0;
    private final long SIGNAL_SHAKE_MAX_TIMES = 10;
    private final long SIGNAL_SHAKE_MAX_TIME = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    private final long SIGNAL_SHAKE_DELAY_TIME = 300000;
    private final long SIGNAL_RECOVERY_DELAY_TIME = 20000;
    private final long INVAILD_START_TIME = -1;
    private LinkedList SignalShakeChangeList = new LinkedList();
    private LinkedList SignalShakeTimeList = new LinkedList();
    private Map mRatNetworkModeDuration = new HashMap();
    private long mLte2NrStartTime = -1;
    private RegisteredCellInfo mLteRegisteredCellInfo = new RegisteredCellInfo();
    private boolean mIsEndcPowerInLte2Nr = true;
    private boolean mIsSaPowerInLte2Nr = true;
    private long mNsaSignalLevelStartTime = -1;
    private HashMap mNrBetterLevelDuration = new HashMap();
    final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.SignalStrengthMonitor.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo;
            String action = intent.getAction();
            if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                if (SignalStrengthMonitor.this.mSlotId != intent.getIntExtra("android.telephony.extra.SLOT_INDEX", SubscriptionManager.INVALID_SLOT_ID)) {
                    return;
                }
                SignalStrengthMonitor.this.mSignalStrengthInfos.setOperator(miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(SignalStrengthMonitor.this.mSlotId));
                return;
            }
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                if (SignalStrengthMonitor.this.mSlotId != intent.getIntExtra("phone", -1)) {
                    return;
                }
                int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                SignalStrengthMonitor signalStrengthMonitor = SignalStrengthMonitor.this;
                if (intExtra == 11) {
                    signalStrengthMonitor.mSimAbsent = false;
                    return;
                } else {
                    signalStrengthMonitor.mSimAbsent = true;
                    SignalStrengthMonitor.this.handleOOSDuration();
                    return;
                }
            }
            if (!action.equals("android.net.conn.CONNECTIVITY_CHANGE") || (networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo")) == null) {
                return;
            }
            int type = networkInfo.getType();
            SignalStrengthMonitor.this.log("CONNECTIVITY_ACTION, networkType = " + type + ", state = " + networkInfo.getState());
            if (type == 1 && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                if (SignalStrengthMonitor.this.mIsWifiConnected) {
                    return;
                }
                SignalStrengthMonitor.this.mIsWifiConnected = true;
                SignalStrengthMonitor.this.stopLte2NrTimer();
                return;
            }
            if (type == 1 && networkInfo.getState() == NetworkInfo.State.DISCONNECTED && SignalStrengthMonitor.this.mIsWifiConnected) {
                SignalStrengthMonitor.this.mIsWifiConnected = false;
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.SignalStrengthMonitor.3
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (displayEvent.isDefaultDisplay()) {
                    SignalStrengthMonitor.this.mHandler.obtainMessage(8, displayEvent).sendToTarget();
                }
            }
        }
    };
    ContentObserver mPreferredNetworkModeObserver = new ContentObserver(null) { // from class: com.android.phone.statistics.SignalStrengthMonitor.4
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            int i = SignalStrengthMonitor.this.mPreferredNetworkMode;
            SignalStrengthMonitor.this.mPreferredNetworkMode = ModemProvider.getInstance().getPreferredNetworkModeFromDb(SignalStrengthMonitor.this.mSlotId);
            if (SignalStrengthMonitor.this.mPreferredNetworkMode != i) {
                SignalStrengthMonitor.this.stopLte2NrTimer();
                SignalStrengthMonitor.this.handleRatNetworkModeDuration();
            }
        }
    };

    private boolean isLTETech(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrTech(int i) {
        return i == 20 || i == 30;
    }

    private boolean isPsOnlyTech(int i) {
        return i == 13 || i == 19 || i == 20 || i == 30;
    }

    private boolean isVaildLteRsrp(int i) {
        return i >= -140 && i <= -43;
    }

    private boolean isVaildNrRsrp(int i) {
        return i >= -156 && i <= -31;
    }

    protected SignalStrengthMonitor(int i) {
        this.m5GNetworkTypeObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.statistics.SignalStrengthMonitor.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                SignalStrengthMonitor.this.mHandler.obtainMessage(6).sendToTarget();
            }
        };
        HandlerThread handlerThread = new HandlerThread("SignalStrengthMonitor", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new SignalHandler(handlerThread.getLooper());
        this.mSignalStrengthInfos = new SignalStrengthStatisticInfo();
        this.mIsAllowSignalShake = true;
        this.mSlotId = i;
        Phone phone = PhoneFactory.getPhone(i);
        this.mPhone = phone;
        Context context = phone.getContext();
        this.mContext = context;
        this.mTelephonyManager = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(this.mPhone.getSubId());
        this.mPreferredNetworkMode = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mSlotId);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        StringBuilder sb = new StringBuilder("5g_icon_group_mode");
        sb.append(this.mSlotId);
        this.mIsShow5GIcon = Settings.Global.getInt(contentResolver, sb.toString(), 0) != 0;
        registerTelephony();
        initReceiver();
    }

    private void registerTelephony() {
        if (this.mTelephonyCallBack == null) {
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        this.mPhone.mCi.registerForRadioStateChanged(this.mHandler, 5, (Object) null);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("preferred_network_mode"), false, this.mPreferredNetworkModeObserver);
        register5GNetworkTypeObserver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    public void dispose() {
        Log.d("SignalStrengthMonitor", "dispose");
        this.mContext.unregisterReceiver(this.mReceiver);
        TelephonyCallBack telephonyCallBack = this.mTelephonyCallBack;
        if (telephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
        }
        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
        this.mContext.getContentResolver().unregisterContentObserver(this.mPreferredNetworkModeObserver);
        unRegister5GNetworkTypeObserver();
    }

    class SignalHandler extends CountableHandler {
        SignalHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                SignalStrength signalStrength = (SignalStrength) message.obj;
                SignalStrengthMonitor.this.handleNrBetterDuration(signalStrength);
                if (SignalStrengthMonitor.this.mIsInService && SignalStrengthMonitor.this.mIsScreenOn) {
                    SignalStrength signalStrength2 = SignalStrengthMonitor.this.mLastSignalStrength;
                    SignalStrengthMonitor signalStrengthMonitor = SignalStrengthMonitor.this;
                    if (signalStrength2 == null) {
                        Log.d("SignalStrengthMonitor", "slotId [" + signalStrengthMonitor.mSlotId + "] last signal strength is null");
                    } else {
                        int miuiLevel = signalStrengthMonitor.getMiuiLevel(signalStrengthMonitor.mLastSignalStrength);
                        int miuiLevel2 = SignalStrengthMonitor.this.getMiuiLevel(signalStrength);
                        if (miuiLevel - miuiLevel2 >= 2) {
                            SignalStrengthMonitor.this.handleSignalStrengthEvent(miuiLevel + " -> " + miuiLevel2);
                        }
                        if (miuiLevel != miuiLevel2) {
                            SignalStrengthMonitor.this.handleSignalShake(miuiLevel, miuiLevel2);
                        }
                    }
                    SignalStrengthMonitor.this.mLastSignalStrength = signalStrength;
                    return;
                }
                return;
            }
            if (i == 2) {
                ServiceState serviceState = (ServiceState) message.obj;
                boolean unused = SignalStrengthMonitor.this.mIsInService;
                int i2 = SignalStrengthMonitor.this.mRat;
                SignalStrengthMonitor.this.mIsInService = SignalStrengthMonitor.isInService(serviceState);
                NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
                SignalStrengthMonitor signalStrengthMonitor2 = SignalStrengthMonitor.this;
                if (networkRegistrationInfo != null) {
                    signalStrengthMonitor2.mRat = networkRegistrationInfo.getAccessNetworkTechnology();
                } else {
                    signalStrengthMonitor2.mRat = 0;
                }
                boolean z = SignalStrengthMonitor.this.mIsInService;
                SignalStrengthMonitor signalStrengthMonitor3 = SignalStrengthMonitor.this;
                if (!z) {
                    signalStrengthMonitor3.mLastSignalStrength = null;
                    SignalStrengthMonitor.this.mRat = 0;
                } else {
                    signalStrengthMonitor3.mSignalStrengthInfos.setRat(SignalStrengthMonitor.this.mRat);
                    SignalStrengthMonitor.this.mSignalStrengthInfos.setBand(Arrays.toString(serviceState.getCellBandwidths()));
                }
                SignalStrengthMonitor.this.handleOOSDuration();
                SignalStrengthMonitor.this.handleRatNetworkModeDuration();
                SignalStrengthMonitor signalStrengthMonitor4 = SignalStrengthMonitor.this;
                signalStrengthMonitor4.handleLte2NrDuration(i2, signalStrengthMonitor4.mRat);
                return;
            }
            if (i == 3) {
                SignalStrengthMonitor.this.mIsAllowSignalShake = true;
                Log.d("SignalStrengthMonitor", "slotId " + SignalStrengthMonitor.this.mSlotId + " has been 5 minutes since the time statistics");
                return;
            }
            if (i == 5) {
                int radioState = SignalStrengthMonitor.this.mPhone.mCi.getRadioState();
                SignalStrengthMonitor signalStrengthMonitor5 = SignalStrengthMonitor.this;
                if (radioState == 0) {
                    signalStrengthMonitor5.mIsRadioPowerOff = true;
                    SignalStrengthMonitor.this.handleOOSDuration();
                    return;
                } else {
                    signalStrengthMonitor5.mIsRadioPowerOff = false;
                    return;
                }
            }
            if (i == 6) {
                int i3 = Settings.Global.getInt(SignalStrengthMonitor.this.mContext.getContentResolver(), "5g_icon_group_mode" + SignalStrengthMonitor.this.mSlotId, 0);
                SignalStrengthMonitor.this.mIsShow5GIcon = i3 != 0;
                SignalStrengthMonitor signalStrengthMonitor6 = SignalStrengthMonitor.this;
                signalStrengthMonitor6.handleNrBetterDuration(signalStrengthMonitor6.mPhone.getSignalStrengthController().getSignalStrength());
                SignalStrengthMonitor.this.updateNsaBandInfo(i3);
                return;
            }
            if (i != 7) {
                return;
            }
            boolean z2 = SignalStrengthMonitor.this.mIsDuringCall;
            boolean z3 = SignalStrengthMonitor.this.mPhone.getState() != PhoneConstants.State.IDLE;
            SignalStrengthMonitor.this.log("isLastDuringCall = " + z2 + ", isCurrentDuringCall = " + z3);
            SignalStrengthMonitor.this.mIsDuringCall = z3;
            if (z2 != z3) {
                SignalStrengthMonitor.this.stopLte2NrTimer();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInService(ServiceState serviceState) {
        if (serviceState == null) {
            return false;
        }
        return serviceState.getState() == 0 || serviceState.getDataRegistrationState() == 0;
    }

    private void register5GNetworkTypeObserver() {
        if (SubscriptionManager.isValidPhoneId(this.mSlotId)) {
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("5g_icon_group_mode" + this.mSlotId), false, this.m5GNetworkTypeObserver);
        }
    }

    private void unRegister5GNetworkTypeObserver() {
        this.mContext.getContentResolver().unregisterContentObserver(this.m5GNetworkTypeObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMiuiLevel(SignalStrength signalStrength) {
        return miui.telephony.TelephonyManager.getDefault().getMiuiLevel(signalStrength);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSignalStrengthEvent(String str) {
        Log.d("SignalStrengthMonitor", "handleSignalStrengthEvent");
        this.mSignalStrengthBigChangeCount++;
        localLog("SignalStrengthMonitor", "SignalStrengthBigChangeStatistics slot " + this.mSlotId, "SignalStrengthBigChangeCount:" + this.mSignalStrengthBigChangeCount, 1);
        if (Build.IS_STABLE_VERSION || !isHandleSignalInfo()) {
            return;
        }
        Log.d("SignalStrengthMonitor", "statistics not than 1 times");
        broadcastSignalStrengthBigChange(str);
        this.mStatisticsTime++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSignalShake(int i, int i2) {
        this.SignalShakeChangeList.add(i + " -> " + i2);
        this.SignalShakeTimeList.add(Long.valueOf(SystemClock.elapsedRealtime()));
        if (this.SignalShakeTimeList.size() < 10) {
            return;
        }
        long jLongValue = ((Long) this.SignalShakeTimeList.getLast()).longValue();
        if (jLongValue - ((Long) this.SignalShakeTimeList.getFirst()).longValue() <= RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            this.mSignalShakeCount++;
            if (!Build.IS_STABLE_VERSION && this.mIsAllowSignalShake) {
                broadcastSignalShake(this.SignalShakeChangeList.toString());
                this.mIsAllowSignalShake = false;
                SignalHandler signalHandler = this.mHandler;
                signalHandler.sendMessageDelayed(signalHandler.obtainMessage(3), 300000L);
            }
            this.SignalShakeChangeList.clear();
            this.SignalShakeTimeList.clear();
            localLog("SignalStrengthMonitor", "SignalShakeStatistics slot " + this.mSlotId, "SignalShakeCount:" + this.mSignalShakeCount, 1);
            return;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.SignalShakeTimeList.size() - 1; i4++) {
            if (jLongValue - ((Long) this.SignalShakeTimeList.get(i4)).longValue() > RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
                i3++;
            }
        }
        for (int i5 = 0; i5 < i3; i5++) {
            this.SignalShakeTimeList.removeFirst();
            this.SignalShakeChangeList.removeFirst();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNrBetterDuration(SignalStrength signalStrength) {
        if (signalStrength == null) {
            if (this.mNsaSignalStartTime != 0) {
                this.mNrBetterDuration += (SystemClock.elapsedRealtime() - this.mNsaSignalStartTime) / 1000;
                this.mNsaSignalStartTime = 0L;
            }
            if (this.mNsaSignalLevelStartTime != -1) {
                calculateNsaLevelTime(this.mNsaLevelName);
                return;
            }
            return;
        }
        int miuiLevel = 0;
        int rsrp = Integer.MAX_VALUE;
        int ssRsrp = Integer.MAX_VALUE;
        int miuiLevel2 = 0;
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                rsrp = ((CellSignalStrengthLte) cellSignalStrength).getRsrp();
                miuiLevel = cellSignalStrength.getMiuiLevel();
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                ssRsrp = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                miuiLevel2 = cellSignalStrength.getMiuiLevel();
            }
        }
        if (isVaildLteRsrp(rsrp) && isVaildNrRsrp(ssRsrp) && ssRsrp > rsrp && this.mIsShow5GIcon) {
            if (this.mNsaSignalStartTime == 0) {
                this.mNsaSignalStartTime = SystemClock.elapsedRealtime();
            }
            if (miuiLevel2 <= miuiLevel) {
                calculateNsaLevelTime(this.mNsaLevelName);
            } else if (this.mNsaSignalLevelStartTime == -1) {
                this.mNsaSignalLevelStartTime = SystemClock.elapsedRealtime();
            } else if (!TextUtils.equals(this.mNsaLevelName, getNsaLevelName(miuiLevel, miuiLevel2))) {
                calculateNsaLevelTime(this.mNsaLevelName);
                this.mNsaSignalLevelStartTime = SystemClock.elapsedRealtime();
            }
        } else if (this.mNsaSignalStartTime != 0) {
            this.mNrBetterDuration += (SystemClock.elapsedRealtime() - this.mNsaSignalStartTime) / 1000;
            this.mNsaSignalStartTime = 0L;
            calculateNsaLevelTime(this.mNsaLevelName);
        }
        this.mNsaLevelName = getNsaLevelName(miuiLevel, miuiLevel2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNsaBandInfo(int i) {
        if (MiuiTelephonyStatistic.getInstance() == null || this.mSlotId != DefaultSimManager.getDefaultDataSlotId()) {
            return;
        }
        boolean z = i == 1;
        if (z != this.mIsNsaMode) {
            this.mIsNsaMode = z;
            if (!z) {
                MiuiTelephonyStatistic.getInstance().updateNsaNwBandInfo(this.mSlotId, null, true);
            }
            if (MiuiTelephonyStatistic.getInstance().mNsaModemStats != null) {
                MiuiTelephonyStatistic.getInstance().mNsaModemStats.updateNsaState(z);
            }
        }
    }

    private static String getNsaLevelName(int i, int i2) {
        return "LTE" + i + "-NR" + i2;
    }

    private void calculateNsaLevelTime(String str) {
        if (TextUtils.isEmpty(str) || this.mNsaSignalLevelStartTime == -1) {
            this.mNsaSignalLevelStartTime = -1L;
            return;
        }
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mNsaSignalLevelStartTime) / 1000;
        boolean zContainsKey = this.mNrBetterLevelDuration.containsKey(str);
        HashMap map = this.mNrBetterLevelDuration;
        if (!zContainsKey) {
            map.put(str, Long.valueOf(jElapsedRealtime));
        } else {
            map.put(str, Long.valueOf(((Long) map.get(str)).longValue() + jElapsedRealtime));
        }
        this.mNsaSignalLevelStartTime = -1L;
    }

    private void broadcastSignalStrengthBigChange(String str) {
        String str2;
        Intent intent = new Intent("com.android.phone.intent.action.SIGNAL_STRENGTH_BIG_CHANGE");
        intent.putExtra("slotId", this.mSlotId);
        intent.putExtra("operator", this.mSignalStrengthInfos.getOperator());
        intent.putExtra("rat", this.mSignalStrengthInfos.getRat());
        intent.putExtra("band", this.mSignalStrengthInfos.getBand());
        intent.putExtra("levelChange", str);
        ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
        if (foregroundInfo == null) {
            str2 = "";
        } else {
            str2 = foregroundInfo.mForegroundPackageName;
        }
        intent.putExtra("foregroundActivity", str2);
        this.mContext.sendBroadcast(intent);
        localLog("SignalStrengthMonitor", "SignalStrengthInfo", " KEY_SLOT_ID :" + intent.getExtra("slotId") + ", KEY_OPERATOR = " + intent.getStringExtra("operator") + ", KEY_RAT = " + intent.getExtra("rat") + ", KEY_LEVEL_CHANGE = " + intent.getExtra("levelChange") + ", KEY_BAND = " + intent.getExtra("band") + ", KEY_FOREGROUND_ACTIVITY = " + intent.getExtra("foregroundActivity"), 10);
    }

    private void broadcastSignalShake(String str) {
        Intent intent = new Intent("com.android.phone.intent.action.SIGNAL_SHAKE");
        intent.putExtra("slotId", this.mSlotId);
        intent.putExtra("signalShake", str);
        this.mContext.sendBroadcast(intent);
    }

    private boolean isHandleSignalInfo() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mFristStatisticTime == 0) {
            this.mFristStatisticTime = jElapsedRealtime;
        }
        if (jElapsedRealtime - this.mFristStatisticTime > v.b) {
            this.mFristStatisticTime = jElapsedRealtime;
            this.mStatisticsTime = 0;
        }
        return this.mStatisticsTime < 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOOSDuration() {
        if (!this.mIsInService && !this.mSimAbsent && this.mIsScreenOn && !this.mIsRadioPowerOff) {
            if (this.mOOSStartTime == 0) {
                this.mOOSStartTime = SystemClock.elapsedRealtime();
            }
        } else if (this.mOOSStartTime != 0) {
            this.mOOSDuration += (SystemClock.elapsedRealtime() - this.mOOSStartTime) / 1000;
            this.mOOSStartTime = 0L;
            localLog("SignalStrengthMonitor", "OOSStatistics slot " + this.mSlotId, "OOSDuration:" + this.mOOSDuration, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRatNetworkModeDuration() {
        String str = TelephonyManagerEx.getDefault().getNetworkClassName(this.mRat) + "_" + getPreferredNetworkModeName();
        if (this.mIsInService && !this.mSimAbsent) {
            if (TextUtils.isEmpty(this.mCurrentRatNetworkMode) && !TextUtils.isEmpty(str)) {
                this.mRatNetworkModeStartTime = SystemClock.elapsedRealtime();
                this.mCurrentRatNetworkMode = str;
                return;
            } else {
                if (TextUtils.equals(this.mCurrentRatNetworkMode, str)) {
                    return;
                }
                calculateRatNetworkTime(str);
                return;
            }
        }
        if (TextUtils.isEmpty(this.mCurrentRatNetworkMode)) {
            return;
        }
        calculateRatNetworkTime("");
    }

    private void calculateRatNetworkTime(String str) {
        if (TextUtils.isEmpty(this.mCurrentRatNetworkMode)) {
            return;
        }
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mRatNetworkModeStartTime) / 1000;
        if (this.mRatNetworkModeDuration.containsKey(this.mCurrentRatNetworkMode)) {
            jElapsedRealtime += ((Long) this.mRatNetworkModeDuration.get(this.mCurrentRatNetworkMode)).longValue();
        }
        this.mRatNetworkModeDuration.put(this.mCurrentRatNetworkMode, Long.valueOf(jElapsedRealtime));
        this.mRatNetworkModeStartTime = TextUtils.isEmpty(str) ? 0L : SystemClock.elapsedRealtime();
        this.mCurrentRatNetworkMode = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLte2NrDuration(int i, int i2) {
        if (!isPsOnlyTech(i2)) {
            stopLte2NrTimer();
            return;
        }
        if (isLTETech(i2) && isNrTech(i) && isAllowStatisticsPrefNr()) {
            startLte2NrTimer();
        }
        updateEndcAndSaPowerInLte2Nr();
        if (isLTETech(i) && isNrTech(i2) && isMeasuringLte2NrDuration()) {
            recordLte2NrStatEvent();
            stopLte2NrTimer();
        }
    }

    private void recordLte2NrStatEvent() {
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mLte2NrStartTime) / 1000;
        HashMap map = new HashMap();
        RegisteredCellInfo registeredCellInfo = getRegisteredCellInfo();
        RegisteredCellInfo registeredCellInfo2 = this.mLteRegisteredCellInfo;
        map.put("lte_cell_info", registeredCellInfo2 == null ? "" : registeredCellInfo2.toString());
        map.put("nr_cell_info", registeredCellInfo != null ? registeredCellInfo.toString() : "");
        map.put("endc_power_enable", String.valueOf(this.mIsEndcPowerInLte2Nr));
        map.put("sa_power_enable", String.valueOf(this.mIsSaPowerInLte2Nr));
        map.put("lte_to_nr_duration_time", String.valueOf(jElapsedRealtime));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "lte_to_nr_duration", map);
    }

    private boolean isMeasuringLte2NrDuration() {
        return this.mLte2NrStartTime != -1;
    }

    private void updateEndcAndSaPowerInLte2Nr() {
        if (isMeasuringLte2NrDuration()) {
            boolean[] endcPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getEndcPowerEnable();
            boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
            int length = endcPowerEnable.length;
            int i = this.mSlotId;
            if (length <= i || saPowerEnable.length <= i) {
                return;
            }
            if (!endcPowerEnable[i] && this.mIsEndcPowerInLte2Nr) {
                this.mIsEndcPowerInLte2Nr = false;
            }
            if (saPowerEnable[i] || !this.mIsSaPowerInLte2Nr) {
                return;
            }
            this.mIsSaPowerInLte2Nr = false;
        }
    }

    private void startLte2NrTimer() {
        this.mLte2NrStartTime = SystemClock.elapsedRealtime();
        this.mLteRegisteredCellInfo = getRegisteredCellInfo();
    }

    private RegisteredCellInfo getRegisteredCellInfo() {
        List<CellInfo> allCellInfo = this.mPhone.getAllCellInfo();
        if (allCellInfo == null) {
            return null;
        }
        for (CellInfo cellInfo : allCellInfo) {
            if (cellInfo.isRegistered()) {
                new RegisteredCellInfo(cellInfo);
                return new RegisteredCellInfo(cellInfo);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLte2NrTimer() {
        this.mLte2NrStartTime = -1L;
        this.mIsEndcPowerInLte2Nr = true;
        this.mIsSaPowerInLte2Nr = true;
        this.mLteRegisteredCellInfo = null;
    }

    private boolean isAllowStatisticsPrefNr() {
        boolean z = this.mSlotId == DefaultSimManager.getDefaultDataSlotId() && this.mRat != 0 && isMatchNetworkMode(9) && this.mIsScreenOn && !this.mIsWifiConnected && !this.mIsDuringCall;
        StringBuilder sb = new StringBuilder("isAllowStatisticsPrefNr, flag = ");
        sb.append(z);
        sb.append(", isDataSlot = ");
        sb.append(this.mSlotId == DefaultSimManager.getDefaultDataSlotId());
        sb.append(", isVaildRat = ");
        sb.append(this.mRat != 0);
        sb.append(", is5Gpref = ");
        sb.append(isMatchNetworkMode(9));
        sb.append(", IsScreenOn = ");
        sb.append(this.mIsScreenOn);
        sb.append(", !mIsWifiConnected = ");
        sb.append(!this.mIsWifiConnected);
        sb.append(", !mIsDuringCall = ");
        sb.append(!this.mIsDuringCall);
        log(sb.toString());
        return z;
    }

    private String getPreferredNetworkModeName() {
        if (isMatchNetworkMode(0)) {
            return "GSM";
        }
        if (isMatchNetworkMode(1) || isMatchNetworkMode(3) || isMatchNetworkMode(2) || isMatchNetworkMode(10) || isMatchNetworkMode(11)) {
            return "UMTS";
        }
        if (isMatchNetworkMode(6) || isMatchNetworkMode(5) || isMatchNetworkMode(8) || isMatchNetworkMode(4) || isMatchNetworkMode(7)) {
            return "LTE";
        }
        if (isMatchNetworkMode(9)) {
            return "NR";
        }
        return "UNKNOWN";
    }

    private boolean isMatchNetworkMode(int i) {
        return ArrayUtils.contains(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(i), this.mPreferredNetworkMode);
    }

    public void onDataSlotReady(boolean z) {
        if (z) {
            stopLte2NrTimer();
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            if (SignalStrengthMonitor.this.mSlotId == i) {
                SignalStrengthMonitor.this.mHandler.obtainMessage(1, signalStrength).sendToTarget();
            }
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (SignalStrengthMonitor.this.mSlotId == i) {
                SignalStrengthMonitor.this.mHandler.obtainMessage(2, serviceState).sendToTarget();
            }
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (SignalStrengthMonitor.this.mSlotId == i) {
                SignalStrengthMonitor.this.mHandler.obtainMessage(7).sendToTarget();
            }
        }
    }

    class SignalStrengthStatisticInfo {
        private String operator = "";
        private int rat = 0;
        private String band = "";

        public void setOperator(String str) {
            this.operator = str;
        }

        public void setRat(int i) {
            this.rat = i;
        }

        public void setBand(String str) {
            this.band = str;
        }

        public String getOperator() {
            return this.operator;
        }

        public int getRat() {
            return this.rat;
        }

        public String getBand() {
            return this.band;
        }
    }

    class RegisteredCellInfo {
        private long mCi;
        private String mOperator;
        private int mRsrp;
        private int mTac;
        private String mType;

        public RegisteredCellInfo() {
            this.mCi = 2147483647L;
            this.mTac = Integer.MAX_VALUE;
            this.mRsrp = Integer.MAX_VALUE;
            this.mType = "UNKNOWN";
        }

        public RegisteredCellInfo(CellInfo cellInfo) {
            CellIdentity cellIdentity;
            this.mCi = 2147483647L;
            this.mTac = Integer.MAX_VALUE;
            this.mRsrp = Integer.MAX_VALUE;
            this.mType = "UNKNOWN";
            if (cellInfo == null || (cellIdentity = cellInfo.getCellIdentity()) == null) {
                return;
            }
            int type = cellIdentity.getType();
            if (type == 3) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                this.mCi = cellIdentityLte.getCi();
                this.mTac = cellIdentityLte.getTac();
                this.mOperator = cellIdentityLte.getMccString() + cellIdentityLte.getMncString();
                this.mRsrp = ((CellSignalStrengthLte) cellInfo.getCellSignalStrength()).getRsrp();
                this.mType = "LTE";
                return;
            }
            if (type != 6) {
                return;
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            this.mCi = cellIdentityNr.getNci();
            this.mTac = cellIdentityNr.getTac();
            this.mOperator = cellIdentityNr.getMccString() + cellIdentityNr.getMncString();
            this.mRsrp = ((CellSignalStrengthNr) cellInfo.getCellSignalStrength()).getSsRsrp();
            this.mType = "NR";
        }

        public String toString() {
            return "[mCi = " + this.mCi + ", mTac = " + this.mTac + ", mOperator = " + this.mOperator + ", mRsrp = " + this.mRsrp + "]";
        }
    }

    public String getSignalStrengthBigChangeCount() {
        long j = this.mSignalStrengthBigChangeCount;
        this.mSignalStrengthBigChangeCount = 0L;
        return String.valueOf(j);
    }

    public String getOOSDuration() {
        long j = this.mOOSDuration;
        this.mOOSDuration = 0L;
        return String.valueOf(j);
    }

    public String getNrBetterDuration() {
        handleNrBetterDuration(null);
        this.mNrBetterLevelDuration.put("nrRsrpBetterLteDuration", Long.valueOf(this.mNrBetterDuration));
        String string = this.mNrBetterLevelDuration.toString();
        this.mNrBetterDuration = 0L;
        this.mNrBetterLevelDuration.clear();
        handleNrBetterDuration(this.mPhone.getSignalStrengthController().getSignalStrength());
        return string;
    }

    public String getSignalShakeCount() {
        long j = this.mSignalShakeCount;
        this.mSignalShakeCount = 0L;
        return String.valueOf(j);
    }

    public String getRatNetworkModeDuration() {
        String strSubstring = "";
        calculateRatNetworkTime("");
        if (!this.mRatNetworkModeDuration.isEmpty()) {
            strSubstring = this.mRatNetworkModeDuration.toString().substring(1, this.mRatNetworkModeDuration.toString().length() - 1);
        }
        this.mRatNetworkModeDuration.clear();
        handleRatNetworkModeDuration();
        return strSubstring;
    }

    public static HashMap getSignalStrengthOptimizationCountMap() {
        HashMap signalStrengthOptimizationCountMap = MiuiDefaultPhoneNotifierStub.get().getSignalStrengthOptimizationCountMap();
        Log.d("SignalStrengthMonitor", "getSignalStrengthOptimizationCountMap , signalStrengthCountMap = " + signalStrengthOptimizationCountMap);
        return signalStrengthOptimizationCountMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalStrengthMonitor[" + this.mSlotId + "]", str);
    }

    public static void localLog(String str, String str2, String str3, int i) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(i));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public static void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SignalStrengthMonitor dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("SignalStrengthMonitor dump end------");
        printWriter.flush();
    }
}
