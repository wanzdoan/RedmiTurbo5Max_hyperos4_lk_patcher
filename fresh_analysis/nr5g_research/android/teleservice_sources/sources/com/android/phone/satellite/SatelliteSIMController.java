package com.android.phone.satellite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.SatelliteManager;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteSIMController extends Handler {
    private static SatelliteSIMController sInstance;
    private long mCardDetectedOnSatelliteShutdownTimer;
    private long mCardDetectedOnSatelliteTimer;
    private Context mContext;
    private boolean mHasIccLock;
    private boolean mIsCardDetectedOnSatellite;
    private boolean mIsCardDetectedOnSatelliteShutdown;
    private boolean[] mIsSimPowerDown;
    private String mLastSimState;
    private final BroadcastReceiver mMiuiSatelliteReceiver;
    private int mRetryTimesOfAllSlotDetected;
    private SatelliteManager mSatelliteManager;
    private long mStartCardDetectTime;

    public static SatelliteSIMController getInstance() {
        if (sInstance == null) {
            loge("SatelliteSIMController was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteSIMController make(SatelliteManager satelliteManager, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteSIMController(satelliteManager, context);
        }
        return sInstance;
    }

    public SatelliteSIMController(SatelliteManager satelliteManager, Context context) {
        super(SatelliteManager.getNtnLooper());
        this.mRetryTimesOfAllSlotDetected = 0;
        this.mHasIccLock = false;
        this.mLastSimState = "UNKNOWN";
        this.mIsSimPowerDown = new boolean[MiuiPhoneUtils.PHONE_COUNT];
        this.mStartCardDetectTime = 0L;
        this.mIsCardDetectedOnSatellite = false;
        this.mIsCardDetectedOnSatelliteShutdown = false;
        this.mCardDetectedOnSatelliteTimer = 0L;
        this.mCardDetectedOnSatelliteShutdownTimer = 0L;
        this.mMiuiSatelliteReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.SatelliteSIMController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    SatelliteSIMController.loge("received null intent or action");
                    return;
                }
                String action = intent.getAction();
                SatelliteSIMController.logd("onReceive action = " + action);
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    if (TelephonyCommonUtils.isNtnChipVendor_54() || SatelliteSIMController.this.mSatelliteManager.isHardSimSwapForTT()) {
                        int slotIdExtra = SubscriptionManager.getSlotIdExtra(intent, -1);
                        if (SatelliteSIMController.this.mSatelliteManager.getController().getEnabledSatelliteSlotId() == -1 || slotIdExtra != SatelliteSIMController.this.mSatelliteManager.getController().getEnabledSatelliteSlotId()) {
                            SatelliteSIMController.logd("satellite phone id is invalid, or not current slotId.");
                            return;
                        }
                        String stringExtra = intent.getStringExtra("ss");
                        SatelliteSIMController.logd("sim state changed, last state : " + SatelliteSIMController.this.mLastSimState + ", cur state : " + stringExtra + ", slotId : " + slotIdExtra);
                        if (SatelliteSIMController.this.mSatelliteManager.getController().isSatelliteEnabled()) {
                            SatelliteManager unused = SatelliteSIMController.this.mSatelliteManager;
                            if (SatelliteManager.getDefaultNtnPhone() != null) {
                                SatelliteSIMController satelliteSIMController = SatelliteSIMController.this;
                                if (!satelliteSIMController.isSubscriptionInfoReady(satelliteSIMController.mLastSimState) && SatelliteSIMController.this.isSubscriptionInfoReady(stringExtra)) {
                                    SatelliteSIMController.logd("satellite slot detected in satellite mode.");
                                    SatelliteSIMController satelliteSIMController2 = SatelliteSIMController.this;
                                    satelliteSIMController2.sendMessageDelayed(satelliteSIMController2.obtainMessage(1), 500L);
                                    SatelliteSIMController.this.stopCardDetectStatistics(true, true);
                                }
                            }
                        }
                        if (SatelliteSIMController.this.mSatelliteManager.getController().isExitSatelliteInProgress() && SatelliteSIMController.this.isSubscriptionInfoReady(stringExtra)) {
                            SatelliteSIMController.logd("satellite slot detected when satellite mode exits.");
                            SatelliteSIMController.this.mRetryTimesOfAllSlotDetected = 0;
                            SatelliteSIMController satelliteSIMController3 = SatelliteSIMController.this;
                            satelliteSIMController3.sendMessageDelayed(satelliteSIMController3.obtainMessage(2), 500L);
                            SatelliteSIMController.this.stopCardDetectStatistics(false, true);
                        }
                        if ("LOCKED".equals(stringExtra)) {
                            SatelliteSIMController.logd("satellite sim state is locked.");
                            SatelliteSIMController.this.mHasIccLock = true;
                        }
                        SatelliteSIMController.this.mLastSimState = stringExtra;
                    }
                }
            }
        };
        this.mSatelliteManager = satelliteManager;
        this.mContext = context;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        logd("handleMessage :" + message.what);
        int i = message.what;
        if (i == 1) {
            if (this.mSatelliteManager.getController().isSatelliteEnabled()) {
                this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(this.mSatelliteManager.getController().getEnabledSatelliteSlotId(), true, false);
                return;
            }
            return;
        }
        if (i != 2) {
            if (i == 3) {
                removeMessages(2);
                this.mSatelliteManager.getController().restoreSatellitestate();
                return;
            } else {
                if (i != 4) {
                    return;
                }
                stopCardDetectStatistics(message.getData().getBoolean("is_open_satellite"), false);
                return;
            }
        }
        if (!this.mSatelliteManager.getController().isExitSatelliteInProgress()) {
            logd("satellite mode exit not in progress.");
            return;
        }
        if (hasIccCardForAllSlot() || this.mRetryTimesOfAllSlotDetected >= 3) {
            removeMessages(3);
            sendMessage(obtainMessage(3));
            return;
        }
        logd("wait for non-satellite slot sim ready, times : " + this.mRetryTimesOfAllSlotDetected);
        this.mRetryTimesOfAllSlotDetected = this.mRetryTimesOfAllSlotDetected + 1;
        sendMessageDelayed(obtainMessage(2), 500L);
    }

    public void registerSimStateChangedListener(boolean z) {
        if (z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            this.mContext.registerReceiver(this.mMiuiSatelliteReceiver, intentFilter, 2);
        } else {
            BroadcastReceiver broadcastReceiver = this.mMiuiSatelliteReceiver;
            if (broadcastReceiver != null) {
                this.mContext.unregisterReceiver(broadcastReceiver);
            }
        }
    }

    private boolean hasIccCardForAllSlot() {
        int phoneCount = TelephonyManagerEx.getDefault().getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            if (!TelephonyManagerEx.getDefault().hasIccCard(i)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSubscriptionInfoReady(String str) {
        return ("ABSENT".equals(str) || "UNKNOWN".equals(str) || "NOT_READY".equals(str)) ? false : true;
    }

    private void performCardReset(final int i, final int i2) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        logd("performCardReset slotId = " + i + ", state = " + i2);
        telephonyManager.setSimPowerStateForSlot(i, i2, Executors.newSingleThreadExecutor(), new Consumer() { // from class: com.android.phone.satellite.SatelliteSIMController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$performCardReset$0(i, i2, (Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performCardReset$0(int i, int i2, Integer num) {
        logd("CardReset result received. result = " + num + ", slotId = " + i + ", state = " + i2);
        if (i2 == 0 && num.intValue() == 0) {
            boolean[] zArr = this.mIsSimPowerDown;
            zArr[i] = true;
            for (boolean z : zArr) {
                if (!z) {
                    return;
                }
            }
            if (this.mSatelliteManager.getController().isSatelliteEnabled() || this.mSatelliteManager.getController().isSatelliteXingwangEnabled()) {
                logd("CardReset all slot power down, try to bind ntn rild.");
                this.mSatelliteManager.getController().handleStartNtnRild();
                return;
            }
            return;
        }
        if (i2 == 1 && num.intValue() == 0) {
            logd("CardReset power up success, slotId = " + i);
            return;
        }
        logd("CardReset failed, something wrong.");
    }

    public void setSimPowerState(int i) {
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (i == 0) {
                this.mIsSimPowerDown[i2] = false;
            }
            performCardReset(i2, i);
        }
    }

    public void startCardDetectStatistics(boolean z) {
        if (hasMessages(4)) {
            stopCardDetectStatistics(!z, false);
        }
        logd("startCardDetectStatistics isEnabled = " + z);
        Message messageObtain = Message.obtain();
        Bundle bundle = new Bundle();
        this.mStartCardDetectTime = System.currentTimeMillis();
        bundle.putBoolean("is_open_satellite", z);
        messageObtain.setData(bundle);
        sendMessageDelayed(obtainMessage(4), 6000L);
    }

    public void stopCardDetectStatistics(boolean z, boolean z2) {
        logd("stopCardDetectStatistics isSatelliteEnable = " + z + ", isDetected = " + z2);
        removeMessages(4);
        if (z) {
            this.mCardDetectedOnSatelliteTimer = System.currentTimeMillis() - this.mStartCardDetectTime;
            this.mIsCardDetectedOnSatellite = z2;
        } else {
            this.mCardDetectedOnSatelliteShutdownTimer = System.currentTimeMillis() - this.mStartCardDetectTime;
            this.mIsCardDetectedOnSatelliteShutdown = z2;
            this.mSatelliteManager.getController().sendSatelliteDotBroadcast();
            this.mSatelliteManager.getController().setSatelliteInService(false);
            this.mHasIccLock = false;
        }
        this.mStartCardDetectTime = 0L;
    }

    public void exitSatelliteDone() {
        sendMessageDelayed(obtainMessage(3), 6000L);
    }

    public void removeSatelliteModeSubInfoReadyMsg() {
        removeMessages(1);
    }

    public long getCardDetectedOnSatelliteTimer() {
        return this.mCardDetectedOnSatelliteTimer;
    }

    public boolean isCardDetectedOnSatellite() {
        return this.mIsCardDetectedOnSatellite;
    }

    public long getCardDetectedOnSatelliteShutdownTimer() {
        return this.mCardDetectedOnSatelliteShutdownTimer;
    }

    public boolean isCardDetectedOnSatelliteShutdown() {
        return this.mIsCardDetectedOnSatelliteShutdown;
    }

    public boolean hasIccLock() {
        return this.mHasIccLock;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteSIMController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("SatelliteSIMController", str);
    }
}
