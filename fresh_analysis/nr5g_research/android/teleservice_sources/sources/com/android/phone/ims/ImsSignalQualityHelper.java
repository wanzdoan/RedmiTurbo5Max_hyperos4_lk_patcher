package com.android.phone.ims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SignalStrength;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.MiuiPhoneInterfaceManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.List;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ImsSignalQualityHelper extends Handler implements TelephonyStatAdapter.ICallBack {
    private static String LOG_TAG = "ImsSignalQualityHelper";
    private static final int T1_RSRP_LOWER_BOUND = PhoneGlobals.getInstance().getResources().getInteger(R.integer.config_t1_rsrp_lower_bound);
    private static ImsSignalQualityHelper sInstance;
    protected Context mContext;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private final BroadcastReceiver mImsSignalReceiver;
    private volatile boolean mInCall;
    private boolean mIsGameForgroud;
    private boolean mIsWhiteListForground;
    private volatile boolean mMtplGameOpt;
    private volatile boolean mMtplVoNROpt;
    private List mOptimizationWhiteList;
    private SignalStrength[] mSignalStrength;
    private volatile boolean mT1CSOrStrongSignalPSCallOpt;
    private volatile boolean mT1VoLteOpt;
    private volatile boolean mT1VoNROpt;
    private volatile boolean mT1WeakSignalOpt;

    public static void init() {
        if (sInstance == null) {
            HandlerThread handlerThread = new HandlerThread("ImsSignalQualityHelperThread");
            handlerThread.start();
            sInstance = new ImsSignalQualityHelper(handlerThread.getLooper());
        }
    }

    public static ImsSignalQualityHelper getInstance() {
        return sInstance;
    }

    protected ImsSignalQualityHelper(Looper looper) {
        super(looper);
        this.mInCall = false;
        this.mT1VoLteOpt = false;
        this.mT1VoNROpt = false;
        this.mMtplVoNROpt = false;
        this.mMtplGameOpt = false;
        this.mT1WeakSignalOpt = false;
        this.mT1CSOrStrongSignalPSCallOpt = false;
        this.mIsGameForgroud = false;
        this.mIsWhiteListForground = false;
        this.mImsSignalReceiver = new BroadcastReceiver() { // from class: com.android.phone.ims.ImsSignalQualityHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action;
                if (intent == null || (action = intent.getAction()) == null || !action.equals("com.android.phone.intent.action.GAME_SCENE")) {
                    return;
                }
                ImsSignalQualityHelper.this.mIsGameForgroud = intent.getBooleanExtra("gameEnabled", false);
                ImsSignalQualityHelper.log("GAME_SCENE forgroud=" + ImsSignalQualityHelper.this.mIsGameForgroud + ", mInCall = " + ImsSignalQualityHelper.this.mInCall);
                if (ImsSignalQualityHelper.this.mInCall) {
                    return;
                }
                ImsSignalQualityHelper.this.removeMessages(2);
                ImsSignalQualityHelper.this.sendEmptyMessageDelayed(2, MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        };
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ims.ImsSignalQualityHelper.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                boolean zContains;
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null || ImsSignalQualityHelper.this.mIsWhiteListForground == (zContains = ImsSignalQualityHelper.this.mOptimizationWhiteList.contains(foregroundInfo.mForegroundPackageName))) {
                    return;
                }
                ImsSignalQualityHelper.this.mIsWhiteListForground = zContains;
                ImsSignalQualityHelper.this.obtainMessage(4).sendToTarget();
            }
        };
        if (MiuiPhoneUtils.isOpTestVersion()) {
            log("op test version, return");
            return;
        }
        this.mContext = PhoneGlobals.getInstance();
        this.mSignalStrength = new SignalStrength[MiuiPhoneUtils.PHONE_COUNT];
        this.mOptimizationWhiteList = HyperOSCustFeatureResolve.getStringArray("config_t1_gps_white_list_package");
        TelephonyStatAdapter.registerCallBack(this);
        if (!Build.IS_INTERNATIONAL_BUILD) {
            registerImsSignalReceiver();
        }
        registerForegroundInfoChangeListener();
    }

    private void registerImsSignalReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.GAME_SCENE");
        this.mContext.registerReceiver(this.mImsSignalReceiver, intentFilter, 2);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        log("receive Message what=" + message.what);
        int i = message.what;
        if (i != 1) {
            if (i == 2) {
                if (isImsSignalOptimizationDisabledfByCloud()) {
                    return;
                }
                if (this.mIsGameForgroud) {
                    processImsSiganlQualityOptimization(DefaultSimManager.getDefaultDataSlotId(), 4);
                    return;
                } else {
                    resetImsSignalOptimizationToDefault(-1, 4);
                    return;
                }
            }
            if (i == 3) {
                if (isImsSignalOptimizationDisabledfByCloud()) {
                    return;
                }
                if (this.mIsGameForgroud && !this.mInCall && isImsRegistered(message.arg1)) {
                    processImsSiganlQualityOptimization(DefaultSimManager.getDefaultDataSlotId(), 4);
                }
                processImsSiganlQualityOptimization(DefaultSimManager.getDefaultDataSlotId(), 16);
                return;
            }
            if (i == 4 && !isImsSignalOptimizationDisabledfByCloud()) {
                processImsSiganlQualityOptimization(DefaultSimManager.getDefaultDataSlotId(), 8);
                return;
            }
            return;
        }
        int i2 = message.arg1;
        boolean z = this.mInCall;
        this.mInCall = isInCall();
        log("call state changed , mInCall = " + this.mInCall + ", oldCallState = " + z);
        if (isImsSignalOptimizationDisabledfByCloud() || z == this.mInCall) {
            return;
        }
        if (!this.mInCall) {
            resetImsSignalOptimizationToDefault(i2, 35);
            return;
        }
        if (isImsRegistered(i2)) {
            if (isInLte(i2)) {
                processImsSiganlQualityOptimization(i2, 1);
                return;
            } else {
                processImsSiganlQualityOptimization(i2, 2);
                return;
            }
        }
        processImsSiganlQualityOptimization(i2, 32);
    }

    private boolean isInCall() {
        boolean z = false;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                z = true;
            }
        }
        return z;
    }

    private boolean isImsSignalOptimizationDisabledfByCloud() {
        return TelephonyCloudController.CLOUD_IMS_SIGNAL_OPT_DISABLED;
    }

    public void resetImsSignalOptimizationToDefault(int i, int i2) {
        log("reset ims signal optimization, scene = " + i2 + ", mT1VoNROpt = " + this.mT1VoNROpt + ", mT1VoLteOpt = " + this.mT1VoLteOpt + ", mMtplVoNROpt = " + this.mMtplVoNROpt + ", mMtplGameOpt = " + this.mMtplGameOpt);
        if (1 == (i2 & 1) && this.mT1VoLteOpt) {
            sendImsCallQualityOptimization(i, 0, 1, 0);
            this.mT1VoLteOpt = false;
        }
        if (2 == (i2 & 2)) {
            if (this.mMtplVoNROpt) {
                sendImsCallQualityOptimization(i, 1, 2, 0);
                this.mMtplVoNROpt = false;
            }
            if (this.mT1VoNROpt) {
                sendImsCallQualityOptimization(i, 1, 1, 0);
                this.mT1VoNROpt = false;
            }
        }
        if (4 == (i2 & 4) && this.mMtplGameOpt) {
            sendImsCallQualityOptimization(i, 2, 2, 0);
            this.mMtplGameOpt = false;
        }
        if (32 == (i2 & 32) && this.mT1CSOrStrongSignalPSCallOpt) {
            sendImsCallQualityOptimization(i, 5, 1, 0);
            this.mT1CSOrStrongSignalPSCallOpt = false;
        }
        if (16 == (i2 & 16) && this.mT1WeakSignalOpt) {
            sendImsCallQualityOptimization(i, 4, 1, 0);
            this.mT1WeakSignalOpt = false;
        }
        if (8 == (i2 & 8)) {
            this.mIsWhiteListForground = false;
            sendImsCallQualityOptimization(i, 3, 1, -1);
        }
    }

    private boolean isImsRegistered(int i) {
        return MiuiPhoneInterfaceManager.getInstance().isImsRegistered(i);
    }

    private boolean isInLte(int i) {
        return MiuiPhoneUtils.isLte(SubscriptionManager.getDefault().getSubscriptionIdForSlot(i));
    }

    private CellSignalStrength getLTESignalStrength(int i) {
        SignalStrength signalStrength = this.mSignalStrength[i];
        if (signalStrength != null) {
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthLte) {
                    return cellSignalStrength;
                }
            }
        }
        return new CellSignalStrengthLte();
    }

    private CellSignalStrength getNRSignalStrength(int i) {
        SignalStrength signalStrength = this.mSignalStrength[i];
        if (signalStrength != null) {
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    return cellSignalStrength;
                }
            }
        }
        return new CellSignalStrengthNr();
    }

    private void processImsSiganlQualityOptimization(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && this.mSignalStrength[i] != null) {
            log("phoneId : " + i + ", scene = " + i2);
            if (i2 == 1) {
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) getLTESignalStrength(i);
                int rsrp = cellSignalStrengthLte.getRsrp();
                int rssnr = cellSignalStrengthLte.getRssnr();
                if (rsrp == Integer.MAX_VALUE || rssnr == Integer.MAX_VALUE) {
                    return;
                }
                if (rsrp < T1_RSRP_LOWER_BOUND || rssnr < 3) {
                    if (this.mT1VoLteOpt) {
                        return;
                    }
                    sendImsCallQualityOptimization(i, 0, 1, 1);
                    this.mT1VoLteOpt = true;
                    return;
                }
                if (this.mT1CSOrStrongSignalPSCallOpt) {
                    return;
                }
                sendImsCallQualityOptimization(i, 5, 1, 1);
                this.mT1CSOrStrongSignalPSCallOpt = true;
                return;
            }
            if (i2 == 2) {
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) getNRSignalStrength(i);
                int ssSinr = cellSignalStrengthNr.getSsSinr();
                int ssRsrp = cellSignalStrengthNr.getSsRsrp();
                if (ssRsrp == Integer.MAX_VALUE || ssSinr == Integer.MAX_VALUE) {
                    return;
                }
                if (ssRsrp < T1_RSRP_LOWER_BOUND || ssSinr < 3) {
                    if (!this.mT1VoNROpt) {
                        sendImsCallQualityOptimization(i, 1, 1, 1);
                        this.mT1VoNROpt = true;
                    }
                    if ((ssRsrp < -110 || ssSinr < 3) && !this.mMtplVoNROpt) {
                        sendImsCallQualityOptimization(i, 1, 2, 1);
                        this.mMtplVoNROpt = true;
                        return;
                    }
                    return;
                }
                if (this.mT1CSOrStrongSignalPSCallOpt) {
                    return;
                }
                sendImsCallQualityOptimization(i, 5, 1, 1);
                this.mT1CSOrStrongSignalPSCallOpt = true;
                return;
            }
            if (i2 == 4) {
                CellSignalStrengthNr cellSignalStrengthNr2 = (CellSignalStrengthNr) getNRSignalStrength(i);
                int ssSinr2 = cellSignalStrengthNr2.getSsSinr();
                int ssRsrp2 = cellSignalStrengthNr2.getSsRsrp();
                if (ssRsrp2 == Integer.MAX_VALUE || ssSinr2 == Integer.MAX_VALUE) {
                    return;
                }
                if (ssRsrp2 < -110 || ssSinr2 < 3) {
                    if (this.mMtplGameOpt) {
                        return;
                    }
                    sendImsCallQualityOptimization(i, 2, 2, 1);
                    this.mMtplGameOpt = true;
                    return;
                }
                if (ssRsrp2 < -105 || ssSinr2 < 6 || !this.mMtplGameOpt) {
                    return;
                }
                sendImsCallQualityOptimization(i, 2, 2, 0);
                this.mMtplGameOpt = false;
                return;
            }
            if (i2 == 32) {
                if (this.mT1CSOrStrongSignalPSCallOpt) {
                    return;
                }
                sendImsCallQualityOptimization(i, 5, 1, 1);
                this.mT1CSOrStrongSignalPSCallOpt = true;
                return;
            }
            if (i2 == 8) {
                log("forground app in white list changed:" + this.mIsWhiteListForground);
                sendImsCallQualityOptimization(i, 3, 1, -1);
                return;
            }
            if (i2 == 16) {
                boolean zIsWeakSignal = isWeakSignal(i);
                if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
                    int i3 = i == 0 ? 1 : 0;
                    if (this.mSignalStrength[i3] != null) {
                        zIsWeakSignal = zIsWeakSignal || isWeakSignal(i3);
                    }
                }
                boolean z = this.mT1WeakSignalOpt;
                if (zIsWeakSignal) {
                    if (z) {
                        return;
                    }
                    sendImsCallQualityOptimization(i, 4, 1, 1);
                    this.mT1WeakSignalOpt = true;
                    return;
                }
                if (z) {
                    sendImsCallQualityOptimization(i, 4, 1, 0);
                    this.mT1WeakSignalOpt = false;
                    return;
                }
                return;
            }
            return;
        }
        log("processImsSiganlQualityOptimization-invalid phone id!");
    }

    private boolean isWeakSignal(int i) {
        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) getNRSignalStrength(i);
        CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) getLTESignalStrength(i);
        int ssSinr = cellSignalStrengthNr.getSsSinr();
        int ssRsrp = cellSignalStrengthNr.getSsRsrp();
        int rsrp = cellSignalStrengthLte.getRsrp();
        int rssnr = cellSignalStrengthLte.getRssnr();
        if (ssRsrp == Integer.MAX_VALUE || ssSinr == Integer.MAX_VALUE) {
            if (rsrp == Integer.MAX_VALUE || rssnr == Integer.MAX_VALUE) {
                return false;
            }
            if (rsrp < T1_RSRP_LOWER_BOUND || rssnr < 3) {
                return true;
            }
        } else if (ssRsrp < T1_RSRP_LOWER_BOUND || ssSinr < 3) {
            return true;
        }
        return false;
    }

    private void registerForegroundInfoChangeListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener, this);
    }

    private void sendImsCallQualityOptimization(int i, int i2, int i3, int i4) {
        if (Build.IS_INTERNATIONAL_BUILD && i3 == 2) {
            return;
        }
        Intent intent = new Intent("com.android.phone.intent.action.receive_call_state");
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        intent.putExtra("call_type", i2);
        intent.putExtra("opt_type", i3);
        intent.putExtra("opt_enabled", i4);
        intent.putExtra("opt_in_white_list", this.mIsWhiteListForground);
        this.mContext.sendBroadcast(intent);
    }

    public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        this.mSignalStrength[i] = signalStrength;
        long jMax = SystemClock.elapsedRealtime() < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS ? Math.max(0L, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS - SystemClock.elapsedRealtime()) : 0L;
        if (hasMessages(3)) {
            removeMessages(3);
        }
        Log.d(LOG_TAG, "onSignalStrengthChanged - phoneId: " + i + ", signalStrength: " + signalStrength + ", delay: " + jMax + "ms");
        sendMessageDelayed(obtainMessage(3, i, 0), jMax);
    }

    public void onPreciseCallStateChanged(int i, int i2) {
        sendMessage(obtainMessage(1, i, i2));
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
