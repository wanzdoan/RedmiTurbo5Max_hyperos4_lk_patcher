package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.MiuiSettings;
import android.telephony.RadioAccessFamily;
import android.util.Log;
import com.android.phone.networkmode.NetworkMode;
import java.util.Arrays;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MtnSimLockController extends MtnSimLockControllerBase {
    private int[] mSimState;
    private boolean mRealFullScreen = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MtnSimLockController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("MtnSimLockController", "receive action : " + action);
            if ("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action) || "android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                MtnSimLockController.this.onSimStateChanged(intent);
            } else if ("android.intent.action.ACTION_SHUTDOWN".equals(action) && MtnSimLockController.this.mRealFullScreen) {
                MtnSimLockController mtnSimLockController = MtnSimLockController.this;
                mtnSimLockController.setFullScreenMode(mtnSimLockController.mRealFullScreen);
                MtnSimLockController.this.mRealFullScreen = false;
            }
        }
    };
    private final Handler mHandler = new Handler() { // from class: com.android.phone.MtnSimLockController.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 100) {
                DefaultSimManager.setNetworkModeInDb(1, message.arg1);
                return;
            }
            if (i == 200) {
                MtnSimLockController.this.startMiuiSimLockMtnActivityImmediately();
                return;
            }
            if (i == 300 && MtnSimLockController.this.isAnySimNetworkLocked()) {
                if (MtnSimLockController.this.hasMtnSim()) {
                    Log.d("MtnSimLockController", "get sim retry,has mtn sim,do nothing");
                } else {
                    Log.d("MtnSimLockController", "All the cards have been Detected, show network lock");
                    MtnSimLockController.this.startMiuiSimLockMtnActivity();
                }
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    public static MtnSimLockControllerBase make() {
        if (ModemProvider.IS_MTK) {
            if (MtnSimLockControllerBase.mInstance == null) {
                MtnSimLockControllerBase.mInstance = new MtnSimLockController();
            }
        } else if (ModemProvider.IS_QCOM && MtnSimLockControllerBase.mInstance == null) {
            MtnSimLockControllerBase.mInstance = new QcMtnSimLockController();
        }
        return MtnSimLockControllerBase.mInstance;
    }

    private MtnSimLockController() {
        int[] iArr = new int[MiuiPhoneUtils.PHONE_COUNT];
        this.mSimState = iArr;
        Arrays.fill(iArr, 0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        if (intExtra == 6 || intExtra == 0) {
            return;
        }
        int intExtra2 = intent.getIntExtra("phone", -1);
        Log.d("MtnSimLockController", "onSimStateChanged: " + intExtra2 + " " + intExtra);
        if (SubscriptionManager.isValidPhoneId(intExtra2)) {
            if (this.mSimState == null) {
                this.mSimState = new int[MiuiPhoneUtils.PHONE_COUNT];
            }
            if (this.mSimState[intExtra2] != 1 && intExtra == 1) {
                boolean z = this.mRealFullScreen;
                if (z) {
                    setFullScreenMode(z);
                    this.mRealFullScreen = false;
                }
                this.mSimState[intExtra2] = intExtra;
                return;
            }
            if (intExtra2 == 1 && intExtra == 10) {
                int i = NetworkMode.getInstance().getSupportModes()[1][0];
                int preferredNetworkModeFromDb = DefaultSimManager.getPreferredNetworkModeFromDb(this.mContext, 1);
                Log.d("MtnSimLockController", "sim2 mode:  " + i + " ,dbMode: " + preferredNetworkModeFromDb);
                if (MiuiPhoneUtils.isMTNSim(MiuiPhoneUtils.getPhone(1)) && i != preferredNetworkModeFromDb) {
                    MiuiPhoneUtils.getPhone(1).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(i), this.mHandler.obtainMessage(100, i, 0));
                }
            }
            this.mSimState[intExtra2] = intExtra;
            if (isAllSimDetected() && isAnySimNetworkLocked()) {
                this.mRealFullScreen = isEnabledFullScreenGesture();
                Log.d("MtnSimLockController", "mRealFullScreen: " + this.mRealFullScreen);
                if (this.mRealFullScreen) {
                    setFullScreenMode(false);
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(200), 100L);
                } else {
                    Log.d("MtnSimLockController", "start MiuiSimLockMtnActivity");
                    startMiuiSimLockMtnActivity();
                }
            }
        }
    }

    public boolean isAllSimDetected() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (!isSimDetected(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSimDetected(int i) {
        int i2 = this.mSimState[i];
        return i2 == 1 || i2 == 10 || i2 == 2 || i2 == 3 || i2 == 4 || i2 == 7;
    }

    public boolean hasMtnSim() {
        int i;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (MiuiPhoneUtils.isMTNSim(MiuiPhoneUtils.getPhone(i2)) || (i = this.mSimState[i2]) == 2 || i == 3) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnySimNetworkLocked() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mSimState[i] == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnabledFullScreenGesture() {
        return MiuiSettings.Global.getBoolean(this.mContext.getContentResolver(), "force_fsg_nav_bar");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFullScreenMode(boolean z) {
        MiuiSettings.Global.putBoolean(this.mContext.getContentResolver(), "force_fsg_nav_bar", z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMiuiSimLockMtnActivity() {
        this.mRealFullScreen = isEnabledFullScreenGesture();
        Log.d("MtnSimLockController", "mRealFullScreen: " + this.mRealFullScreen);
        if (this.mRealFullScreen) {
            setFullScreenMode(false);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(200), 100L);
            return;
        }
        startMiuiSimLockMtnActivityImmediately();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMiuiSimLockMtnActivityImmediately() {
        Log.d("MtnSimLockController", "startMiuiSimLockMtnActivityImmediately");
        Intent intent = new Intent();
        intent.setClass(this.mContext, MiuiSimLockMtnActivity.class);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }
}
