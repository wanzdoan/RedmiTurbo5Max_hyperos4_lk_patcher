package com.android.phone.ntn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class NtnBoxServiceManager {
    private static volatile NtnBoxServiceManager sInstance;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private NtnNetworkRecoveryController mNtnNetworkRecoveryController;
    private NtnStackStateMachine mNtnStackStateMachine;
    private String[] mSimState;
    private int mBtConnectState = 0;
    private int mBtConnectType = -1;
    private int mLastBtConnectType = -1;
    private boolean mIsRildInitialized = false;

    private NtnBoxServiceManager() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.ntn.NtnBoxServiceManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                NtnLogUtils.logd("NtnBoxServiceManager", "onReceive action=".concat(action));
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    NtnBoxServiceManager.this.mHandler.obtainMessage(1, intent).sendToTarget();
                    return;
                }
                if ("com.xiaomi.bluetooth.action.box_connection_state".equals(action)) {
                    NtnBoxServiceManager ntnBoxServiceManager = NtnBoxServiceManager.this;
                    ntnBoxServiceManager.mLastBtConnectType = ntnBoxServiceManager.mBtConnectType;
                    NtnBoxServiceManager.this.mBtConnectType = intent.getIntExtra("type", -1);
                    NtnBoxServiceManager.this.mBtConnectState = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1);
                    NtnLogUtils.logd("NtnBoxServiceManager", "onReceive action=" + action + ", mBtConnectType=" + NtnBoxServiceManager.this.mBtConnectType + ",mLastBtConnectType=" + NtnBoxServiceManager.this.mLastBtConnectType + ",mBtConnectState=" + NtnBoxServiceManager.this.mBtConnectState);
                    NtnBoxServiceManager.this.mHandler.obtainMessage(0).sendToTarget();
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xiaomi.bluetooth.action.box_connection_state");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        PhoneGlobals.getInstance().registerReceiver(broadcastReceiver, intentFilter, 2);
        this.mSimState = new String[MiuiPhoneUtils.PHONE_COUNT];
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mSimState[i] = "ABSENT";
        }
        NtnBoxHandler ntnBoxHandler = new NtnBoxHandler();
        this.mHandler = ntnBoxHandler;
        this.mNtnStackStateMachine = new NtnStackStateMachine(NtnStackStateMachine.NAME, ntnBoxHandler.getLooper());
        NtnNetworkRecoveryController ntnNetworkRecoveryController = new NtnNetworkRecoveryController();
        this.mNtnNetworkRecoveryController = ntnNetworkRecoveryController;
        ntnNetworkRecoveryController.setBoxServiceManager(this);
    }

    public static void init() {
        if (sInstance == null) {
            synchronized (NtnBoxServiceManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new NtnBoxServiceManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static NtnBoxServiceManager getInstance() {
        return sInstance;
    }

    class NtnBoxHandler extends Handler {
        NtnBoxHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NtnLogUtils.logd("NtnBoxServiceManager", "NtnBoxHandler handleMessage:" + NtnBoxServiceManager.this.convertMessageIdToString(message.what));
            int i = message.what;
            if (i == 0) {
                NtnBoxServiceManager.this.onBtConnectStateChange();
                return;
            }
            if (i == 1) {
                Object obj = message.obj;
                if (obj == null) {
                    NtnLogUtils.logw("NtnBoxServiceManager", "Error: Invalid intent when receiving EVENT_SIM_STATE_CHANGED ");
                    return;
                } else {
                    NtnBoxServiceManager.this.onIccStatusChanged((Intent) obj);
                    return;
                }
            }
            if (i == 2) {
                NtnBoxServiceManager.this.updateNtnStackState(1);
                return;
            }
            if (i == 3) {
                NtnBoxServiceManager.this.onRadioStateChanged();
                return;
            }
            if (i == 4) {
                if (NtnBoxServiceManager.this.isBtConnected() && NtnBoxServiceManager.this.isNtnSimReady()) {
                    return;
                }
                NtnBoxServiceManager.this.shutdownNtn();
                return;
            }
            if (i == 5 && NtnBoxServiceManager.this.isNtnRildFirstSetup()) {
                NtnBoxServiceManager.this.updateNtnStackState(7);
                NtnBoxServiceManager.this.mIsRildInitialized = true;
            }
        }
    }

    public void onNtnPhoneCreated() {
        NtnPhoneProxyBase.getInstance().registerForRadioStateChanged(this.mHandler, 3, null);
    }

    public void clearBeforeNtnPhoneDestroyed() {
        NtnPhoneProxyBase.getInstance().unregisterForRadioStateChanged(this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onIccStatusChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("ss");
        int intExtra = intent.getIntExtra("phone", -1);
        Phone phone = MiuiPhoneUtils.getPhone(intExtra);
        if (MiuiPhoneUtils.isValidatePhoneId(intExtra) && phone != null && isSimStateChanged(intExtra, stringExtra)) {
            this.mSimState[intExtra] = stringExtra;
            int i = isNtnSimReady(intExtra) ? 2 : 3;
            NtnLogUtils.logd("NtnBoxServiceManager", "onIccStatusChanged phoneId=" + intExtra + ", iccState=" + stringExtra + ", flag=" + i);
            updateNtnStackState(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNtnSimReady() {
        return isNtnSimReady(getNtnPhoneId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNtnRildFirstSetup() {
        return !this.mIsRildInitialized && NtnPhoneProxyBase.getInstance().getRadioState() == 0;
    }

    private boolean isNtnSimReady(int i) {
        int ntnPhoneId = getNtnPhoneId();
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || i != ntnPhoneId) {
            NtnLogUtils.logd("NtnBoxServiceManager", "isNtnSimReady invalid ntn phoneId" + i + ", ntnPhoneId:" + ntnPhoneId);
            return false;
        }
        return "LOADED".equals(this.mSimState[i]);
    }

    private boolean isSimStateChanged(int i, String str) {
        boolean z = !str.equals(this.mSimState[i]);
        NtnLogUtils.logd("NtnBoxServiceManager", "isSimStateChanged phoneId=" + i + ", changed=" + z + ", simState=" + str + ", mSimState=" + this.mSimState[i]);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBtConnectStateChange() {
        NtnLogUtils.logd("NtnBoxServiceManager", "onBtConnectStateChange isBtConnected()=" + isBtConnected() + ", mBtConnectType=" + this.mBtConnectType);
        NtnCallManager.getInstance().onBtConnectStateChange(this.mLastBtConnectType, this.mBtConnectType);
        if (!isBtConnected()) {
            updateNtnStackState(5);
            return;
        }
        if (this.mHandler.hasMessages(4)) {
            this.mHandler.removeMessages(4);
        }
        if (NtnPhoneProxyBase.getInstance().getRadioState() != 1) {
            updateNtnStackState(4);
        }
    }

    public void turnStackOn() {
        this.mNtnStackStateMachine.obtainMessage(0).sendToTarget();
        if (this.mHandler.hasMessages(5)) {
            this.mHandler.removeMessages(5);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(5), 6000L);
    }

    public void shutdownNtn() {
        this.mNtnStackStateMachine.obtainMessage(1).sendToTarget();
        this.mIsRildInitialized = false;
    }

    public void startNtnStack() {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(2), 100L);
    }

    public void rebootNtnStack() {
        startNtnStack();
    }

    public void turnNtnRadioOn() {
        if (isNtnSimReady()) {
            this.mNtnStackStateMachine.obtainMessage(3).sendToTarget();
            NtnLogUtils.logd("NtnBoxServiceManager", "turnNtnRadioOn SWITCH_RADIOPOWER_ON");
        } else {
            NtnLogUtils.logd("NtnBoxServiceManager", "turnNtnRadioOn sim not ready!");
        }
    }

    public void turnNtnRadioOff() {
        this.mNtnStackStateMachine.obtainMessage(2).sendToTarget();
        NtnLogUtils.logd("NtnBoxServiceManager", "turnNtnRadioOff SWITCH_RADIOPOWER_OFF");
    }

    public void resetNtnStack() {
        this.mNtnStackStateMachine.obtainMessage(4).sendToTarget();
        NtnLogUtils.logd("NtnBoxServiceManager", "resetNtnStack SWITCH_STACK_RESET");
    }

    private int getNtnPhoneId() {
        Phone ntnPhone = getNtnPhone();
        int phoneId = (ntnPhone == null || !isNtnSim(ntnPhone.getPhoneId())) ? -1 : ntnPhone.getPhoneId();
        NtnLogUtils.logd("NtnBoxServiceManager", "getNtnPhoneId ntnPhoneId:" + phoneId);
        return phoneId;
    }

    private Phone getNtnPhone() {
        Phone ntnPhone = NtnPhoneProxyBase.getInstance().getNtnPhone();
        if (ntnPhone != null) {
            return ntnPhone;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.mSimState;
            if (i >= strArr.length) {
                return ntnPhone;
            }
            if (!TextUtils.isEmpty(strArr[i])) {
                ntnPhone = MiuiPhoneUtils.getPhone(i);
            }
            i++;
        }
    }

    public void updateNtnStackState(int i) {
        NtnLogUtils.logd("NtnBoxServiceManager", "updateNtnStackState flag:" + i);
        switch (i) {
            case 1:
                turnStackOn();
                break;
            case 2:
            case 4:
                if (isBtConnected() && isNtnSimReady()) {
                    turnStackOn();
                    break;
                }
                break;
            case 3:
            case 5:
                if (this.mHandler.hasMessages(5)) {
                    this.mHandler.removeMessages(5);
                }
                if (NtnCallManager.getInstance().maybeClearCalls()) {
                    delayReleaseNtnNetwork(2000);
                } else {
                    shutdownNtn();
                }
                break;
            case 6:
                shutdownNtn();
                break;
            case 7:
                turnNtnRadioOn();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRadioStateChanged() {
        int radioState = NtnPhoneProxyBase.getInstance().getRadioState();
        if (radioState == 0) {
            if (this.mIsRildInitialized) {
                return;
            }
            updateNtnStackState(7);
            this.mIsRildInitialized = true;
            return;
        }
        if (radioState == 1) {
            delayReleaseNtnNetwork(600000);
        } else {
            if (radioState != 2) {
                return;
            }
            this.mIsRildInitialized = false;
        }
    }

    private void delayReleaseNtnNetwork(int i) {
        if (this.mHandler.hasMessages(4)) {
            this.mHandler.removeMessages(4);
        }
        NtnLogUtils.logd("NtnBoxServiceManager", "delayReleaseNtnNetwork send release message");
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(4), i);
    }

    public boolean isBtConnected() {
        return getBtConnectState() == 1;
    }

    public boolean isBtTypeChanged() {
        int i = this.mLastBtConnectType;
        return (i == -1 || i == this.mBtConnectType) ? false : true;
    }

    public int getBtConnectType() {
        return this.mBtConnectType;
    }

    public int getBtConnectState() {
        return this.mBtConnectState;
    }

    public void recoverNtnNetwork() {
        if (!this.mIsRildInitialized) {
            NtnLogUtils.logd("NtnBoxServiceManager", "recoverNtnNetwork try to reboot stack");
            this.mNtnNetworkRecoveryController.recoverNtnNetwork(3);
        } else {
            this.mNtnNetworkRecoveryController.recoverNtnNetwork();
        }
    }

    public static boolean isNtnSim(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (MiuiPhoneUtils.isChinaTelecomSim(phone)) {
            return true;
        }
        String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(phone);
        return "46004".equals(operatorNumeric) || "46059".equals(operatorNumeric);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String convertMessageIdToString(int i) {
        if (i == 0) {
            return "EVENT_BT_CONNECT_CHANGED";
        }
        if (i == 1) {
            return "EVENT_SIM_STATE_CHANGED";
        }
        if (i == 2) {
            return "EVENT_ON_INIT";
        }
        if (i == 3) {
            return "EVENT_RADIO_STATE_CHANGED";
        }
        if (i == 4) {
            return "EVENT_RELEASE_NTN_NETWORK";
        }
        return String.valueOf(i);
    }
}
