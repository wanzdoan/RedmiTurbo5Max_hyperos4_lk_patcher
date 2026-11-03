package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.xiaomi.mirilhook.MiRilHook;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class QcMtnSimLockController extends MtnSimLockControllerBase {
    private static boolean mIsShowSIMLockActivityDelay = false;
    private int[] mLastSimType;
    private int[] mSimState;
    private int[] mSimType;
    private boolean mRealFullScreen = false;
    private boolean mMiRilHookQueryDone = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.QcMtnSimLockController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("QcMtnSimLockController", "receive action : " + action);
            if ("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action) || "android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                QcMtnSimLockController.this.onSimStateChanged(intent);
                return;
            }
            if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                if (QcMtnSimLockController.this.mRealFullScreen) {
                    QcMtnSimLockController qcMtnSimLockController = QcMtnSimLockController.this;
                    qcMtnSimLockController.setFullScreenMode(qcMtnSimLockController.mRealFullScreen);
                    QcMtnSimLockController.this.mRealFullScreen = false;
                    return;
                }
                return;
            }
            if ("android.intent.action.SERVICE_STATE".equals(action)) {
                int intExtra = intent.getIntExtra("slot", 0);
                Bundle extras = intent.getExtras();
                ServiceState serviceStateNewFromBundle = extras != null ? ServiceState.newFromBundle(extras) : null;
                if (serviceStateNewFromBundle == null) {
                    return;
                }
                if (Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_network_unlock", -1) == 0) {
                    Log.d("QcMtnSimLockController", "phone is already unlock");
                    return;
                }
                if (SubscriptionManager.isRealSlotId(intExtra)) {
                    if (MiuiPhoneUtils.isMTNSim(MiuiPhoneUtils.getPhone(intExtra))) {
                        boolean zIsInService = QcMtnSimLockController.isInService(serviceStateNewFromBundle);
                        QcMtnSimLockController qcMtnSimLockController2 = QcMtnSimLockController.this;
                        if (zIsInService) {
                            qcMtnSimLockController2.mSimType[intExtra] = 1;
                        } else if (qcMtnSimLockController2.isInActiveMtnSim(serviceStateNewFromBundle)) {
                            QcMtnSimLockController.this.mSimType[intExtra] = 2;
                        }
                    }
                    Log.d("QcMtnSimLockController", "SERVICE_STATE_CHANGED: simType= " + QcMtnSimLockController.this.mSimType[intExtra] + ", mLastSimType= " + QcMtnSimLockController.this.mLastSimType[intExtra] + ", phoneId= " + intExtra);
                    if (QcMtnSimLockController.this.mLastSimType[intExtra] != QcMtnSimLockController.this.mSimType[intExtra] && QcMtnSimLockController.this.getPresentCardCount() > 1 && QcMtnSimLockController.this.isDisplayLockScreen()) {
                        QcMtnSimLockController.this.setRadioPower(false);
                        QcMtnSimLockController.this.startMiuiSimLockMtnActivity();
                    }
                    QcMtnSimLockController.this.mLastSimType[intExtra] = QcMtnSimLockController.this.mSimType[intExtra];
                }
            }
        }
    };
    private final Handler mHandler = new Handler() { // from class: com.android.phone.QcMtnSimLockController.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 200) {
                QcMtnSimLockController.this.startMiuiSimLockMtnActivityImmediately();
                return;
            }
            if (i == 300) {
                Log.d("QcMtnSimLockController", "EVENT_GET_SIM_RETRY");
                QcMtnSimLockController.this.handeSimState();
            } else if (i == 400) {
                Log.d("QcMtnSimLockController", "EVENT_PRECISE_CALL_STATE_CHANGED");
                QcMtnSimLockController.this.handleCallStateChanged((Phone) ((AsyncResult) message.obj).result);
            } else {
                if (i != 1000) {
                    return;
                }
                Log.d("QcMtnSimLockController", "EVENT_PERSONALIZATION_STATUS_RSP");
                QcMtnSimLockController.this.getSIMLockStateResponse((byte[]) ((AsyncResult) message.obj).result, 0);
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    class RilHookStateCallback implements BaseEvent.IEventListener {
        private RilHookStateCallback() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.RilEvent) {
                if (((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
                    Log.d("QcMtnSimLockController", "onMiRilHookReady");
                    QcMtnSimLockController.this.getSIMLockState(0);
                } else {
                    Log.d("QcMtnSimLockController", "onMiRilHookDisconnected");
                }
            }
        }
    }

    protected QcMtnSimLockController() {
        int i = 0;
        int i2 = MiuiPhoneUtils.PHONE_COUNT;
        int[] iArr = new int[i2];
        this.mSimState = iArr;
        this.mSimType = new int[i2];
        this.mLastSimType = new int[i2];
        Arrays.fill(iArr, 0);
        Arrays.fill(this.mSimType, 0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        while (true) {
            if (i < MiuiPhoneUtils.PHONE_COUNT) {
                Phone phone = PhoneFactory.getPhone(i);
                if (phone == null) {
                    Log.d("QcMtnSimLockController", "phone is null");
                } else {
                    Log.d("QcMtnSimLockController", "registerForPreciseCallStateChanged, phoneId = " + i);
                    phone.registerForPreciseCallStateChanged(this.mHandler, 400, (Object) null);
                    if (phone.getImsPhone() != null) {
                        phone.getImsPhone().registerForPreciseCallStateChanged(this.mHandler, 400, (Object) null);
                    }
                }
                i++;
            } else {
                EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookStateCallback(), this.mHandler);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        if (Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_network_unlock", -1) == 0) {
            Log.d("QcMtnSimLockController", "phone is already unlock");
            return;
        }
        int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        if (intExtra == 6 || intExtra == 0) {
            return;
        }
        int intExtra2 = intent.getIntExtra("phone", -1);
        Log.d("QcMtnSimLockController", "onSimStateChanged: simState= " + intExtra + ", phoneId= " + intExtra2);
        if (SubscriptionManager.isValidPhoneId(intExtra2)) {
            int[] iArr = this.mSimState;
            if (iArr[intExtra2] != 1 && intExtra == 1) {
                setRadioPower(true);
                boolean z = this.mRealFullScreen;
                if (z) {
                    setFullScreenMode(z);
                    this.mRealFullScreen = false;
                }
                this.mSimState[intExtra2] = intExtra;
                this.mSimType[intExtra2] = 0;
                return;
            }
            iArr[intExtra2] = intExtra;
            if (intExtra == 10) {
                boolean zIsMTNSim = MiuiPhoneUtils.isMTNSim(MiuiPhoneUtils.getPhone(intExtra2));
                int[] iArr2 = this.mSimType;
                if (zIsMTNSim) {
                    if (2 != iArr2[intExtra2]) {
                        iArr2[intExtra2] = 1;
                    }
                } else {
                    iArr2[intExtra2] = 3;
                }
            }
            Log.d("QcMtnSimLockController", "onSimStateChanged: simType= " + this.mSimType[intExtra2] + ", phoneId= " + intExtra2);
            if (isAllSimDetected()) {
                this.mHandler.removeMessages(MiRilHook.HOOK_ID_START_URC_BY_AT);
                if (getPresentCardCount() == 1 || !isAllMccMncReady()) {
                    Log.d("QcMtnSimLockController", "Only one card recognizes or mcc is not ready");
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(MiRilHook.HOOK_ID_START_URC_BY_AT), 3000L);
                    return;
                }
                handeSimState();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPresentCardCount() {
        int i = 0;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (TelephonyManager.getDefault().hasIccCard(i2)) {
                i++;
            }
        }
        return i;
    }

    public boolean isAllMccMncReady() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (TelephonyManager.getDefault().hasIccCard(i) && TextUtils.isEmpty(TelephonyManager.getDefault().getSimOperatorNumericForPhone(i))) {
                return false;
            }
        }
        return true;
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

    private boolean isEnabledFullScreenGesture() {
        return MiuiSettings.Global.getBoolean(this.mContext.getContentResolver(), "force_fsg_nav_bar");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFullScreenMode(boolean z) {
        MiuiSettings.Global.putBoolean(this.mContext.getContentResolver(), "force_fsg_nav_bar", z);
    }

    public final void setRadioPower(boolean z) {
        Log.d("QcMtnSimLockController", "setRadioPower: mMiRilHookQueryDone= " + this.mMiRilHookQueryDone);
        if (this.mMiRilHookQueryDone) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                setRadioPower(i, z);
            }
        }
    }

    public final void setRadioPower(int i, boolean z) {
        if (MiuiPhoneUtils.getPhone(i) == null) {
            return;
        }
        boolean radioPowerSp = getRadioPowerSp(i);
        boolean z2 = MiuiPhoneUtils.getPhone(i).mCi.getRadioState() == 1;
        Log.d("QcMtnSimLockController", "setRadioPower: enabled: " + z + ",isRadioOn: " + z2 + ", isRadioPowerSp: " + radioPowerSp + ", phoneId: " + i);
        if (z == radioPowerSp && z == z2) {
            return;
        }
        setRadioPowerSp(i, z);
        MiuiPhoneUtils.getPhone(i).setRadioPower(z, false, false, true);
    }

    private void setRadioPowerSp(int i, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean("radio_power_control" + i, z).commit();
    }

    public boolean getRadioPowerSp(int i) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("radio_power_control" + i, true);
    }

    public boolean isInActiveMtnSim(ServiceState serviceState) {
        if (isInService(serviceState)) {
            return false;
        }
        return getCsRejectCause(serviceState) == 15 || getPsRejectCause(serviceState) == 15;
    }

    public static int getCsRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    public static int getPsRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    public static boolean isInService(ServiceState serviceState) {
        if (serviceState == null) {
            return false;
        }
        return serviceState.getState() == 0 || serviceState.getDataRegistrationState() == 0;
    }

    public boolean isDisplayLockScreen() {
        if (getPresentCardCount() > 1) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                int i2 = this.mSimType[i];
                if (i2 == 1 || i2 == 0) {
                    return false;
                }
            }
            return true;
        }
        if (getPresentCardCount() == 1) {
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                if (this.mSimType[i3] == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handeSimState() {
        Log.d("QcMtnSimLockController", "handeSimState: simType1= " + this.mSimType[0] + ", simType2= " + this.mSimType[1]);
        setRadioPower(true);
        if (isDisplayLockScreen()) {
            setRadioPower(false);
            startMiuiSimLockMtnActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallStateChanged(Phone phone) {
        if (Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_network_unlock", -1) == 0) {
            Log.d("QcMtnSimLockController", "handleCallStateChanged: No lock sim");
            return;
        }
        if (!isDisplayLockScreen()) {
            Log.d("QcMtnSimLockController", "handleCallStateChanged: no display lock screen");
            return;
        }
        if (phone == null || !SubscriptionManager.isValidPhoneId(phone.getPhoneId())) {
            return;
        }
        Log.d("QcMtnSimLockController", "handleCallStateChanged: state =  " + phone.getState() + ", slotId = " + phone.getPhoneId());
        if (isAllPhonesInIdle()) {
            Log.d("QcMtnSimLockController", "handleCallStateChanged: phone state is IDLE");
            setRadioPower(false);
        }
    }

    private static boolean isAllPhonesInIdle() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMiuiSimLockMtnActivity() {
        if (!this.mMiRilHookQueryDone) {
            mIsShowSIMLockActivityDelay = true;
            Log.d("QcMtnSimLockController", "startMiuiSimLockMtnActivity, mMiRilHookQueryDone is not");
            return;
        }
        if (isEmergencyCall()) {
            Log.d("QcMtnSimLockController", "emergency call ongoing");
            return;
        }
        this.mRealFullScreen = isEnabledFullScreenGesture();
        Log.d("QcMtnSimLockController", "mRealFullScreen: " + this.mRealFullScreen);
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
        Log.d("QcMtnSimLockController", "startMiuiSimLockMtnActivityImmediately");
        Intent intent = new Intent();
        intent.setClass(this.mContext, MiuiSimLockMtnActivity.class);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    private static boolean isEmergencyCall() {
        boolean z = false;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            z = z || InternalTelephonyUtils.isEmergencyCall(PhoneFactory.getPhone(i));
        }
        return z;
    }

    public void getSIMLockState(int i) {
        Message messageObtainMessage = this.mHandler.obtainMessage(1000);
        Log.i("QcMtnSimLockController", "getSIMLockState begin  value>>" + i);
        try {
            RilHook.get(this.mContext).qcGetSIMLockState(i, messageObtainMessage);
            Log.i("QcMtnSimLockController", "getSIMLockState end  value>>" + i);
        } catch (Exception e) {
            Log.i("QcMtnSimLockController", "getSIMLockState error=" + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getSIMLockStateResponse(byte[] bArr, int i) {
        BroadcastReceiver broadcastReceiver;
        this.mMiRilHookQueryDone = true;
        if (bArr != null) {
            Log.i("QcMtnSimLockController", "getSIMLockStateResponse SUCCESS,phoneId=" + i + " response=" + Arrays.toString(bArr));
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            Log.i("QcMtnSimLockController", "getSIMLockStateResponse SUCCESS,byteBuf=" + byteBufferWrap.toString());
            int i2 = byteBufferWrap.getInt();
            Log.i("QcMtnSimLockController", "getSIMLockStateResponse errCode=" + i2);
            if (i2 != 0) {
                return;
            }
            int i3 = byteBufferWrap.getInt();
            Log.i("QcMtnSimLockController", "getSIMLockStateResponse has_gwNWPersoStatus=" + i3);
            Settings.Secure.putInt(this.mContext.getContentResolver(), "is_network_unlock", i3 == 0 ? 0 : 1);
            if (i3 != 0 && mIsShowSIMLockActivityDelay && isDisplayLockScreen()) {
                setRadioPower(false);
                startMiuiSimLockMtnActivity();
            } else if (i3 == 0 && (broadcastReceiver = this.mReceiver) != null) {
                this.mContext.unregisterReceiver(broadcastReceiver);
            }
        } else {
            Log.i("QcMtnSimLockController", "getSIMLockStateResponse ERROR,phoneId=" + i);
        }
        mIsShowSIMLockActivityDelay = false;
    }
}
